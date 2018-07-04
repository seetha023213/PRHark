package com.servicenow.github.utils;

/**
 * @author udayaseetha sriramula
 *
 */
public final class ApplicationConstants {

	private ApplicationConstants() {
	}

	public static final String GITHUB_BASE_URL = "${github.url}";
	public static final String GITHUB_USER_NAME = "${github.userName}";
	public static final String GITHUB_PASSWORD = "${github.password}";

	public static final String NEXT_PAGE_CRITERIA = "rel=\"next\"";
	public static final String LAST_PAGE_CRITERIA = "rel=\"last\"";
	public static final String LINK_HEADER = "Link";
	public static final String USERS = "/users";
	public static final String PER_PAGE = "?per_page=";
	public static final String REPOS = "/repos";
	public static final String SLASH = "/";
	public static final String PULLS = "/pulls";
	public static final int PER_PAGE_DEFAULT_VALUE = 100;
	public static final String PAGE = "page";
	public static final char GREATER_SYMBOL = '>';
	public static final String COMMA = ", ";
	public static final String AMPERSAND = "&";
	public static final String EQUALS = "=";

	public static final String USER_REPO_INFO_VIEW = "/userRepoInfo";
	public static final String HELPER_PAGE = "/helperPage";
	public static final String ERROR_PAGE = "error";

}
