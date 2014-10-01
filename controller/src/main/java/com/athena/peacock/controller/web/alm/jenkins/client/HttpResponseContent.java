package com.athena.peacock.controller.web.alm.jenkins.client;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

import org.apache.http.HttpResponse;

public class HttpResponseContent {

	public String contentAsString(HttpResponse response) throws IOException {
		Scanner scanner = new Scanner(contentAsInputStream(response));
		scanner.useDelimiter("\\z");
		StringBuffer sb = new StringBuffer();
		while (scanner.hasNext()) {
			sb.append(scanner.next());
		}
		return sb.toString();
	}

	public InputStream contentAsInputStream(HttpResponse response)
			throws IOException {

		return response.getEntity().getContent();
	}
}
