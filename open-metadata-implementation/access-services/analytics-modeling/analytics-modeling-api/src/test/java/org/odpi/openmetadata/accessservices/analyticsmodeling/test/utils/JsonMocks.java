/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils;

public class JsonMocks {

	public static final String PROPERTY_NAME = "property_name";
	public static final String PROPERTY_VALUE = "property_value";
	public static final String PROPERTY_NAME2 = "property_name2";
	public static final String PROPERTY_VALUE2 = "property_value2";

	public static String getEmptyJson() { 
		return "{ }"; 
	}
	
	public static String getProperty(String name, String value) { 
		return String.format(
				"{%n" +
				"  \"name\" : \"%s\",%n" +
				"  \"value\" : \"%s\"%n" +
				"}", name, value);
	}

	public static String getBaseObjectType() {
		return String.format(
				"{%n" + 
				"  \"property\" : [ {%n" + 
				"    \"name\" : \"property_name\",%n" + 
				"    \"value\" : \"property_value\"%n" + 
				"  }, {%n" + 
				"    \"name\" : \"property_name2\",%n" + 
				"    \"value\" : \"property_value2\"%n" + 
				"  } ]%n" +
				"}");
	}
	
	/**
	 * Body for the request to get module that defines set of tables to include.
	 * @return the request body
	 */
	public static String getModuleTableIncludedFilterBody() {
		return String.format(
				"{%n" + 
				"  \"meta\" : {%n" + 
				"    \"includedTables\" : [ \"ORDERDETAIL\", \"PRODUCT\" ]%n" + 
				"  }%n" + 
				"}");
	}
	/**
	 * Body for the request to get module that defines set of tables to exclude.
	 * @return the request body
	 */
	public static String getModuleTableExcludedFilterBody() {
		return String.format(
				"{%n" + 
				"  \"meta\" : {%n" + 
				"    \"excludedTables\" : [ \"ORDERDETAIL\", \"PRODUCT\" ]%n" + 
				"  }%n" + 
				"}");
	}
}
