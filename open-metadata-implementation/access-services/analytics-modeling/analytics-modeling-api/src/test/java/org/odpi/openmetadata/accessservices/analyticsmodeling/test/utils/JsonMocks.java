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
	
	/**
	 * Body for the request to get module that defines set of tables to exclude.
	 * @param httpStatus of the error.
	 * @param code of the error.
	 * @param errorTitle of the error.
	 * @param errorDetail of the error.
	 * @param severity of the error.
	 * @return error json.
	 */
	public static String getError(String httpStatus, String code, String errorTitle, String errorDetail, String severity) {
		return String.format("{%n" + 
				"  \"status\" : \"%s\",%n" + 
				"  \"code\" : \"%s\",%n" + 
				"  \"title\" : \"%s\",%n" + 
				"  \"detail\" : \"%s\",%n" + 
				"  \"meta\" : {%n" + 
				"    \"severity\" : \"%s\"%n" +
				"  }%n" +
				"}", httpStatus, code, errorTitle, errorDetail, severity);
	}
	

	/**
	 * Get json of ResponseContainerDatabase object from template.
	 * @param guid of the database.
	 * @param name of the database.
	 * @param type of the database.
	 * @param version of the database.
	 * @return ResponseContainerDatabase json
	 */
	public static String getResponseDatabase(String guid, String name, String type, String version) {
		return String.format("{%n" + 
				"  \"attributes\": {%n" + 
				"    \"guid\": \"%s\",%n" + 
				"    \"name\": \"%s\",%n" + 
				"    \"type\": \"%s\",%n" + 
				"    \"version\": \"%s\"%n" + 
				"  },%n" + 
				"  \"type\": \"database\"%n" + 
				"}", guid, name, type, version);
	}
	/**
	 * Get json of ResponseContainerDatabaseSchema object from template.
	 * @param catalog of the database.
	 * @param schema of the database.
	 * @param schemaType of the schema.
	 * @return ResponseContainerDatabaseSchema json.
	 */
	public static String getResponseSchema(String catalog, String schema, String schemaType) {
		return String.format("{%n" + 
				"  \"attributes\": {%n" + 
				"    \"catalog\": \"%s\",%n" + 
				"    \"schema\": \"%s\",%n" + 
				"    \"schemaType\": \"%s\"%n" + 
				"  },%n" + 
				"  \"type\": \"schema\"%n" + 
				"}", catalog, schema, schemaType);
	}
	/**
	 * Get json of ResponseContainerSchemaTables object from template.
	 * @param tableNames from the schema
	 * @return ResponseContainerSchemaTables json.
	 */
	public static String getResponseSchemaTables(String ...tableName) {
		String names = String.join("\", \"", tableName);
		return String.format("{%n" + 
				"  \"attributes\": {%n" + 
				"    \"tables\": [\"%s\"]%n" +
				"  },%n" + 
				"  \"type\": \"tables\"%n" + 
				"}", names);
	}

	/**
	 * Get json of ResponseContainerAssets object from template.
	 * @param assetGuids list of guids in the response.
	 * @return ResponseContainerAssets json
	 */
	public static String getResponseAssets(String ...assetGuids) {
		String names = String.join("\", \"", assetGuids);
		return String.format("{%n" + 
				"  \"attributes\": {%n" + 
				"    \"assets\": [\"%s\"]%n" +
				"  },%n" + 
				"  \"type\": \"assets\"%n" + 
				"}", names);
	}
}
