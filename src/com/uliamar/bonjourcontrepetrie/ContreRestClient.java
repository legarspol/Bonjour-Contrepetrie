package com.uliamar.bonjourcontrepetrie;

import com.loopj.android.http.*;

public class ContreRestClient {
	private static final String BASE_URL = "http://uliamar.com/h/contre/ajax.php";

	private static AsyncHttpClient client = new AsyncHttpClient();

	public static void getAllContre(AsyncHttpResponseHandler responseHandler) {
		client.get(BASE_URL, responseHandler);
	}
	
	public static void getRecentContre(int date, AsyncHttpResponseHandler responseHandler) {
		RequestParams params = new RequestParams();
		params.put("timestamp", "" + date);
		client.get(BASE_URL, params, responseHandler);
	}
}
