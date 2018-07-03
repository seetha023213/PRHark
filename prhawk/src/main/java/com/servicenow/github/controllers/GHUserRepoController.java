package com.servicenow.github.controllers;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.servicenow.github.services.GHUserRepoService;
import com.servicenow.github.utils.ApplicationConstants;
import com.servicenow.github.viewModel.GHUser;

/**
 * @author udayaseetha sriramula
 */
@Controller
public class GHUserRepoController {

	private static Logger LOG = Logger.getLogger(GHUserRepoController.class);

	@Autowired
	private GHUserRepoService userRepoService;

	@GetMapping("/")
	public String urlHelper() throws Exception {
		return ApplicationConstants.HELPER_PAGE;
	}

	@GetMapping("/user/{userName}")
	public String getUserRepoInfo(@PathVariable("userName") String userName, Model model) throws Exception {
		// send user name empty and try test
		LOG.info("Request received for user: " + userName);
		List<GHUser> userRepoInfoList = userRepoService.buildUserReposInfo(userName);

		model.addAttribute("list", userRepoInfoList);
		return ApplicationConstants.USER_REPO_INFO_VIEW;
	}

}
