package com.servicenow.github.controllers;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.ModelAndView;

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

	@GetMapping(value ={"/","/user"})
	public String urlHelper() throws Exception {
		return ApplicationConstants.HELPER_PAGE;
	}

	@GetMapping("/user/{userName}")
	public String getUserRepoInfo(@PathVariable("userName") String userName, Model model,final HttpServletResponse response) throws Exception {
		
		LOG.info("Request received for user: " + userName);
		List<GHUser> userRepoInfoList = userRepoService.buildUserReposInfo(userName);
		model.addAttribute("list", userRepoInfoList);
		return ApplicationConstants.USER_REPO_INFO_VIEW;
	}
	
	  @ExceptionHandler(Exception.class)
	  public ModelAndView handleError(HttpServletRequest req, Exception ex) {
		
		LOG.error("Exception occured for the Request: " + req.getRequestURL());
		ModelAndView mav = new ModelAndView();
	    mav.addObject("exception", ex.getMessage());
	    mav.setViewName(ApplicationConstants.ERROR_PAGE);
	    return mav;
	  }

}
