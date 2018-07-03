package com.servicenow.github.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author udayaseetha sriramula
 *
 */
public class GHUtils {

	/**
	 * fetchUrl generate next/last url from link header
	 * 
	 * @param link
	 *            from the response header
	 * @param criteria
	 *            Either Next page or Last page
	 */
	public static String fetchUrl(List<String> link, String criteria) {

		String url = null;
		if (link == null)
			return url;

		String token = link.get(0);
		for (String str : token.split(ApplicationConstants.COMMA)) {
			if (str.endsWith(criteria)) {
				int idx = str.indexOf(ApplicationConstants.GREATER_SYMBOL);
				url = str.substring(1, idx);
				return url;
			}
		}
		return url;
	}

	/**
	 * getpageNumber get the page number from url - The last page number to
	 * count no.of pull requests
	 * 
	 * @param url
	 *            url
	 */
	public static String getpageNumber(String url) {

		if (url == null)
			return url;

		String urlParams = url.substring(url.indexOf('?') + 1, url.length());

		String[] params = urlParams.split(ApplicationConstants.AMPERSAND);
		Map<String, String> map = new HashMap<String, String>();
		for (String param : params) {
			String[] keyValue = param.split(ApplicationConstants.EQUALS, 2);

			String key = keyValue[0];
			String value = keyValue[1];
			if (!key.isEmpty()) {
				map.put(key, value);
			}
		}

		return map.get(ApplicationConstants.PAGE);
	}
}
