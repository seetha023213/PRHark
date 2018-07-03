/**
 * 
 */
package com.servicenow.github.contollers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.servicenow.github.controllers.GHUserRepoController;
import com.servicenow.github.services.GHUserRepoService;
import com.servicenow.github.utils.ApplicationConstants;
import com.servicenow.github.viewModel.GHUser;

/**
 * @author udayaseetha sriramula
 *
 */
@RunWith(SpringRunner.class)
@WebMvcTest(GHUserRepoController.class)
public class GHUserRepoControllerTest {

	@Autowired
	private WebApplicationContext wac;
	private MockMvc mockMvc;
	private List<GHUser> userRepoList;

	@MockBean
	private GHUserRepoService testservice;

	@Before
	public void setUp() {

		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		userRepoList = new ArrayList<GHUser>();

		GHUser repo1 = new GHUser();
		repo1.setName("test1");
		repo1.setOpenPullCount(10);
		repo1.setHtml_url("https://github.com/test1");

		GHUser repo2 = new GHUser();
		repo2.setName("test2");
		repo2.setOpenPullCount(20);
		repo2.setHtml_url("https://github.com/test2");

		userRepoList.add(repo1);
		userRepoList.add(repo2);

	}

	@Test
	public void controllerEndPointTest() throws Exception {
		when(testservice.buildUserReposInfo("user1")).thenReturn(userRepoList);
		MvcResult result = this.mockMvc.perform(get("/user/{userName}", "user1")).andReturn();
		assertThat(result.getModelAndView().getViewName()).isEqualTo(ApplicationConstants.USER_REPO_INFO_VIEW);

	}

	@Test
	public void controllerHelperEndPointTest() throws Exception {
		MvcResult result = this.mockMvc.perform(get("/")).andReturn();
		assertThat(result.getModelAndView().getViewName()).isEqualTo(ApplicationConstants.HELPER_PAGE);

	}

}
