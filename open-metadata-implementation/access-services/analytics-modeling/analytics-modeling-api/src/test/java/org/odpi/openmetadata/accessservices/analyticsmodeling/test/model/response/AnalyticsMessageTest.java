/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.test.model.response;

import org.odpi.openmetadata.accessservices.analyticsmodeling.model.response.AnalyticsMessage;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.JsonMocks;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.TestUtilities;
import org.testng.annotations.Test;

public class AnalyticsMessageTest {
	
	
	private static final String SEVERITY = "error";
	private static final String ERROR_TITLE = "Error title";
	private static final String ERROR_DETAIL = "Error Detail";
	private static final String HTTP_STATUS = "200";
	private static final String CODE = "CODE";

	String master = JsonMocks.getError(HTTP_STATUS, CODE, ERROR_TITLE, ERROR_DETAIL, SEVERITY);

	String master_empty = String.format("{%n" + 
			"  \"meta\" : {%n" + 
			"    \"severity\" : \"%s\"%n" + 
			"  }%n" + 
			"}", SEVERITY);

	@Test
	public void toJson() {
		AnalyticsMessage obj = new AnalyticsMessage();

		obj.setCode(CODE);
		obj.setStatus(HTTP_STATUS);
		obj.setDetail(ERROR_DETAIL);
		obj.setTitle(ERROR_TITLE);
		obj.setSeverity(SEVERITY);
		
		TestUtilities.assertObjectJson(obj, master);
	}

	@Test
	public void toJsonEmpty() {
		AnalyticsMessage obj = new AnalyticsMessage();

		TestUtilities.assertObjectJson(obj, master_empty);
	}

	@Test
	public void fromJson() {

		AnalyticsMessage obj = TestUtilities.readObjectJson(master, AnalyticsMessage.class);
		TestUtilities.assertObjectJson(obj, master);
	}}
