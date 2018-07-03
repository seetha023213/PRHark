package com.servicenow.github.utils;

import java.util.Comparator;

import com.servicenow.github.viewModel.GHUser;

/**
 * @author udayaseetha sriramula
 *
 */
public class SortByOpenPulls implements Comparator<GHUser> {

	public int compare(GHUser a, GHUser b) {
		return b.getOpenPullCount() - a.getOpenPullCount();
	}

}
