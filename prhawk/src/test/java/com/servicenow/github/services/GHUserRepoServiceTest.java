package com.servicenow.github.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.requestTo;
import static org.springframework.test.web.client.response.MockRestResponseCreators.withSuccess;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.client.RestClientTest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.client.MockRestServiceServer;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.servicenow.github.viewModel.GHUser;

/**
 * @author udayaseetha sriramula
 *
 */
@RunWith(SpringRunner.class)
@RestClientTest(GHUserRepoService.class)
public class GHUserRepoServiceTest {
	@Autowired
	private GHUserRepoService client;
	@Autowired
	private MockRestServiceServer server;
	@Autowired
	private ObjectMapper objectMapper;
	private List<GHUser> userRepoList;

	String detailsString;

	@Before
	public void setUp() throws Exception {

		userRepoList = new ArrayList<GHUser>();

		GHUser repo1 = new GHUser();
		repo1.setName("test1");
		repo1.setOpenPullCount(10);
		repo1.setHtml_url("https://github.com/test1");

		userRepoList.add(repo1);
		detailsString = objectMapper.writeValueAsString(userRepoList);
	}

	@Test
	public void getResponseEntityTest() throws Exception {

		this.server.expect(requestTo("/user/user1")).andRespond(withSuccess(detailsString, MediaType.APPLICATION_JSON));

		ResponseEntity<GHUser[]> response = this.client.getResponseEntity("/user/user1");
		GHUser[] list = response.getBody();
		assertThat(list[0].getName()).isEqualTo("test1");
	}

	@Test
	public void buildRepoListUrlTest() {

		String url = this.client.buildRepoListUrl("test1", 0);
		assertThat(url).isEqualTo("https://api.github.com/users/test1/repos?per_page=0");
	}

	@Test
	public void buildUrlOpenPullRequestsTest() {

		String url = this.client.buildUrlOpenPullRequests("test1", "repo1", 0);
		assertThat(url).isEqualTo("https://api.github.com/repos/test1/repo1/pulls?per_page=0");
	}

	@Test
	public void populateOpenPullReqCountTest() {

		this.server.expect(requestTo("https://api.github.com/repos/user1/test1/pulls?per_page=1"))
				.andRespond(withSuccess(detailsString, MediaType.APPLICATION_JSON));
		this.client.populateOpenPullReqCount("user1", userRepoList);
		assertThat(userRepoList.get(0).getOpenPullCount()).isEqualTo(1);

	}

	@Test
	public void fetchAllRepoInfoRecTest() throws Exception {
		this.server.expect(requestTo("https://api.github.com/users/user1/repos"))
				.andRespond(withSuccess(detailsString, MediaType.APPLICATION_JSON));

		List<GHUser> resultList = new ArrayList<GHUser>();

		this.client.fetchAllRepoInfoRec("https://api.github.com/users/user1/repos", resultList);
		assertThat(resultList.get(0).getName()).isEqualTo("test1");
	}

	@Test
	public void populateUserRepoInfoTest() throws Exception {
		this.server.expect(requestTo("https://api.github.com/users/user1/repos?per_page=100"))
				.andRespond(withSuccess(detailsString, MediaType.APPLICATION_JSON));

		List<GHUser> resultList = new ArrayList<GHUser>();

		this.client.populateUserRepoInfo("user1", resultList);
		assertThat(resultList.get(0).getName()).isEqualTo("test1");
		assertThat(resultList.get(0).getOpenPullCount()).isEqualTo(10);
	}
	
	

}
