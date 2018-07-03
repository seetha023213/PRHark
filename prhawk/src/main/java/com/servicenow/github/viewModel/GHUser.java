package com.servicenow.github.viewModel;

/**
 * @author udayaseetha sriramula
 *
 */
public class GHUser {

	private String html_url;
	private String name;
	private int openPullCount;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOpenPullCount() {
		return openPullCount;
	}

	public void setOpenPullCount(int openPullCount) {
		this.openPullCount = openPullCount;
	}

	public String getHtml_url() {
		return html_url;
	}

	public void setHtml_url(String html_url) {
		this.html_url = html_url;
	}
}
