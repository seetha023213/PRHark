package com.servicenow.github.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.servicenow.github.utils.ApplicationConstants;
import com.servicenow.github.utils.GHUtils;
import com.servicenow.github.utils.SortByOpenPulls;
import com.servicenow.github.viewModel.GHUser;

/**
 * @author udayaseetha sriramula
 *
 */
@Service
public class GHUserRepoService {

	private static Logger LOG = Logger.getLogger(GHUserRepoService.class);
	private final RestTemplate restTemplate;
	@Value(ApplicationConstants.GITHUB_BASE_URL)
	private String githubBaseUrl;

	/**
	 * UserRepoService constructor handles initialization of rest template and
	 * basic authentication
	 * 
	 * @param restTemplateBuilder
	 *            Loaded by spring automatically
	 * @param username
	 *            GitHub user name from properties file(required)
	 * @param password
	 *            GitHub password from properties file(required)
	 */
	@Autowired
	public GHUserRepoService(RestTemplateBuilder restTemplateBuilder,
			@Value(ApplicationConstants.GITHUB_USER_NAME) String username,
			@Value(ApplicationConstants.GITHUB_PASSWORD) String password) {
		this.restTemplate = restTemplateBuilder.basicAuthorization(username, password).build();
	}

	/**
	 * buildUserReposInfo method calls 1. populateUserRepoInfo for total user
	 * Repositories details 2. populateOpenPullReqCount method to update the
	 * list with no.Of pulls 3. sort the list and returns
	 * 
	 * @param userName
	 *            provided by user, injected through controller
	 */
	public List<GHUser> buildUserReposInfo(String userName) {

		LOG.info("Entered service layer: UserRepoService method :buildUserReposInfo method");
		// create the empty list and send to the recursive methods below to
		// avoid multiple returns
		List<GHUser> list = new ArrayList<GHUser>();
		populateUserRepoInfo(userName, list);
		// if list is empty no repositories found for given user so no need to
		// call open pull requests for the repositories.
		if (list.isEmpty()) {
			return list;
		}
		populateOpenPullReqCount(userName, list);
		Collections.sort(list, new SortByOpenPulls());
		LOG.info("Returning from service layer: UserRepoService method :buildUserReposInfo method");
		return list;
	}

	/**
	 * populateUserRepoInfo builds 1st url GET /user/repos and calls
	 * fetchAllRepoInfoRec method
	 * 
	 * @param userName
	 *            provided by user, injected through controller
	 * @param list
	 *            empty list to build results
	 */
	public void populateUserRepoInfo(String userName, List<GHUser> list) {

		LOG.info("Entered service layer: UserRepoService method :populateUserRepoInfo method");
		String url = buildRepoListUrl(userName, ApplicationConstants.PER_PAGE_DEFAULT_VALUE);
		fetchAllRepoInfoRec(url, list);
		LOG.info("Returning from service layer: UserRepoService method :populateUserRepoInfo method");
	}

	/**
	 * fetchAllRepoInfoRec calls itself recursively until it fetches all pages
	 * information and updates the list
	 * 
	 * @param url
	 *            Initially gets the 1st url from populateUserRepoInfo GET
	 *            /user/repos . further pages urls will be paken from link
	 *            header
	 * @param list
	 *            list will be updated for each recursion call
	 */

	public void fetchAllRepoInfoRec(String url, List<GHUser> list) {
		try {
			ResponseEntity<GHUser[]> response = getResponseEntity(url);
			if (!HttpStatus.OK.equals(response.getStatusCode())) {
				LOG.error("Exception occured while processing url:" + url);
			} else {
				ArrayList<GHUser> pageList = new ArrayList<GHUser>(Arrays.asList(response.getBody()));
				list.addAll(pageList);
			}
			url = GHUtils.fetchUrl(response.getHeaders().get(ApplicationConstants.LINK_HEADER),
					ApplicationConstants.NEXT_PAGE_CRITERIA);
			if (url != null) {
				fetchAllRepoInfoRec(url, list);
			}
		} catch (Exception ex) {
			LOG.error("Exception occured while processing url:" + url);
		}
	}

	/**
	 * populateOpenPullReqCount calculated based on page size and total no.of
	 * pages (link header contains first and last page numbers, pass page size
	 * as 1)
	 * 
	 * @param userName
	 *            provided by user, injected through controller
	 * @param list
	 *            list will be updated with open pull requests count for each
	 *            repository
	 */
	public void populateOpenPullReqCount(String userName, List<GHUser> list) {
		LOG.info("Entered service layer: UserRepoService method :populateOpenPullReqCount method");
		for (GHUser userRepo : list) {
			try {
				String pullUrl = buildUrlOpenPullRequests(userName, userRepo.getName(), 1);
				ResponseEntity<GHUser[]> response = getResponseEntity(pullUrl);
				if (!HttpStatus.OK.equals(response.getStatusCode())) {
					LOG.error("Exception occured while processing url:" + response.getStatusCode());
				}
				String lastUrl = GHUtils.fetchUrl(response.getHeaders().get(ApplicationConstants.LINK_HEADER),
						ApplicationConstants.LAST_PAGE_CRITERIA);
				int openPullRequests = 0;
				if (lastUrl != null) {
					openPullRequests = Integer.valueOf(GHUtils.getpageNumber(lastUrl));
				} else {
					openPullRequests = response.getBody().length;
				}
				userRepo.setOpenPullCount(openPullRequests);
			} catch (Exception ex) {
				LOG.error("Exception occured while processing the list:");
			}
		}
		LOG.info("Returning from service layer: UserRepoService method :populateOpenPullReqCount method");
	}

	/**
	 * getResponseEntity - to make API call to gitHub repository
	 * 
	 * @param url
	 *            No.of repos for each user. No of open pulls for each repo
	 */
	public ResponseEntity<GHUser[]> getResponseEntity(String url) {
		return restTemplate.getForEntity(url, GHUser[].class);
	}

	/**
	 * buildRepoListUrl - build user repo list API url GET /user/repos
	 * 
	 * @param userName
	 *            provided by user, injected through controller
	 * @param pageSize
	 *            default page size is 30. Here we hard coded to 100 to reduce
	 *            no.of API calls
	 */
	public String buildRepoListUrl(String userName, int pageSize) {
		return githubBaseUrl + ApplicationConstants.USERS + ApplicationConstants.SLASH + userName
				+ ApplicationConstants.REPOS + ApplicationConstants.PER_PAGE + pageSize;
	}

	/**
	 * buildUrlOpenPullRequests - build API url GET /repos/:owner/:repo/pulls
	 * default state is open
	 * 
	 * @param userName
	 *            provided by user, injected through controller
	 * @param repoName
	 *            user owned repository name
	 * @param pageSize
	 *            default page size is 30. Here we hard coded to 1 to count the
	 *            no.of open pulls requests based on no of pages
	 */
	public String buildUrlOpenPullRequests(String userName, String repoName, int pageSize) {

		return githubBaseUrl + ApplicationConstants.REPOS + ApplicationConstants.SLASH + userName
				+ ApplicationConstants.SLASH + repoName + ApplicationConstants.PULLS + ApplicationConstants.PER_PAGE
				+ pageSize;
	}

}
