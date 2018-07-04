package com.servicenow.github.utils;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.servicenow.github.viewModel.GHUser;

/**
 * @author udayaseetha sriramula
 *
 */
public class GHUtilsTest {

	List<String> linkeHeaders;
	String url;
	String str = "<https://api.github.com/users/user/repos?page=3&per_page=100>; rel=\"next\", <https://api.github.com/users/user/repos?page=50&per_page=100>; rel=\"last\"";

	@Before
	public void setUp() {

		linkeHeaders = new ArrayList<String>();
		linkeHeaders.add(str);
		url = "https://api.github.com/users/user/repos?page=3&per_page=100";
	}

	@Test
	public void fetchUrlTest() {

		String url = GHUtils.fetchUrl(linkeHeaders, ApplicationConstants.NEXT_PAGE_CRITERIA);
		assertThat(url).isEqualTo("https://api.github.com/users/user/repos?page=3&per_page=100");

		url = GHUtils.fetchUrl(linkeHeaders, "rel=\"last\"");
		assertThat(url).isEqualTo("https://api.github.com/users/user/repos?page=50&per_page=100");
	}

	@Test
	public void getpageNumberTest() {

		String pageNumber = GHUtils.getpageNumber(url);
		assertThat(pageNumber).isEqualTo("3");

	}
	
	@Test
	public void getpageNumberNullTest() {

		String pageNumber = GHUtils.getpageNumber(null);
		assertThat(pageNumber).isEqualTo(null);

	}

	@Test
	public void sortByNoOfpullReqTest() {

		List<GHUser> userRepoList = new ArrayList<GHUser>();

		GHUser repo1 = new GHUser();
		repo1.setName("test1");
		repo1.setOpenPullCount(10);
		repo1.setHtml_url("https://github.com/test1");

		GHUser repo2 = new GHUser();
		repo2.setName("test2");
		repo2.setOpenPullCount(56);
		repo2.setHtml_url("https://github.com/test2");

		GHUser repo3 = new GHUser();
		repo3.setName("test3");
		repo3.setOpenPullCount(1);
		repo3.setHtml_url("https://github.com/test3");

		userRepoList.add(repo1);
		userRepoList.add(repo2);
		userRepoList.add(repo3);

		Collections.sort(userRepoList, new SortByOpenPulls());

		assertThat(userRepoList.get(0).getOpenPullCount()).isEqualTo(56);
	}

}
