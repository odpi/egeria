/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils;

public class JsonMocks {

	public static String getItem(String qName, String name, String id, String exp, String dType) {
		return String.format(
				"{%n" + 
				"  \"qualifiedName\" : \"%s\",%n" + 
				"  \"additionalProperties\" : {%n" + 
				"    \"p1\" : \"v1\",%n" + 
				"    \"p2\" : \"v2\"%n" + 
				"  },%n" + 
				"  \"displayName\" : \"%s\",%n" + 
				"  \"description\" : \"Query Item description\",%n" +
				"  \"identifier\" : \"%s\",%n" + 
				"  \"expression\" : \"%s\",%n" + 
				"  \"dataType\" : \"%s\",%n" + 
				"  \"sourceId\" : [ \"uid1\", \"uid2\" ]%n" + 
				"}", qName, name, id, exp, dType);
	}
	public static String getBaseModuleQueryItem(String name, String id, String exp, int pos, String dataType, String guid) {
		return String.format(
				"{%n" + 
				"  \"displayName\" : \"%s\",%n" + 
				"  \"identifier\" : \"%s\",%n" + 
				"  \"elementPosition\" : %d,%n" + 
				"  \"expression\" : \"%s\",%n" + 
				"  \"dataType\" : \"%s\",%n" + 
				"  \"sourceGuid\" : [ \"%s\" ],%n" + 
				"  \"type\" : \"queryItem\"%n" + 
				"}", name, id, pos, exp, dataType, guid);
	}
	
	public static String getContainer(String qName, String name, String id) {
		return String.format(
				"{%n" + 
				"  \"qualifiedName\" : \"%s\",%n" + 
				"  \"displayName\" : \"%s\",%n" + 
				"  \"description\" : \"Query Subject description\",%n" + 
				"  \"elementPosition\" : 1,%n" + 
				"  \"identifier\" : \"%s\",%n" + 
				"  \"sourceGuid\" : [ \"guid1\", \"guid2\" ],%n" + 
				"  \"sourceId\" : [ \"uid1\", \"uid2\" ],%n" + 
				"  \"item\" : [ {%n" + 
				"    \"qualifiedName\" : \"PARENT::(SchemaAttribute)=qsID::(SchemaAttribute)=qiID1\",%n" + 
				"    \"displayName\" : \"qiName1\",%n" + 
				"    \"description\" : \"Query Item 1 description\",%n" + 
				"    \"elementPosition\" : 1,%n" + 
				"    \"identifier\" : \"qiID1\",%n" + 
				"    \"expression\" : \"qiExpression1\",%n" + 
				"    \"dataType\" : \"VARCHAR\",%n" + 
				"    \"sourceId\" : [ \"uid1\", \"uid2\" ]%n" + 
				"  }, {%n" + 
				"    \"qualifiedName\" : \"PARENT::(SchemaAttribute)=qsID::(SchemaAttribute)=qiID2\",%n" + 
				"    \"displayName\" : \"qiName2\",%n" + 
				"    \"description\" : \"Query Item 2 description\",%n" + 
				"    \"elementPosition\" : 2,%n" + 
				"    \"identifier\" : \"qiID2\",%n" + 
				"    \"expression\" : \"qiExpression2\",%n" + 
				"    \"dataType\" : \"VARCHAR\",%n" + 
				"    \"sourceId\" : [ \"uid1\", \"uid2\" ]%n" + 
				"  } ]%n" + 
				"}", qName, name, id);
	}
	
	public static String getEmptyQuerySubject(String name, String id, int position, String guid) {
		return String.format(
				"{%n" + 
				"  \"displayName\" : \"%s\",%n" + 
				"  \"elementPosition\" : %d,%n" + 
				"  \"identifier\" : \"%s\",%n" + 
				"  \"sourceGuid\" : [ \"%s\" ],%n" + 
				"  \"type\" : \"querySubject\"%n" + 
				"}", name, position, id, guid);
	}
	
	public static String getNode(String name, String qName, String type, String classification) {
		return String.format(
				"{%n" + 
				"  \"displayName\" : \"%s\",%n" + 
				"  \"qName\" : \"%s\",%n" + 
				"  \"type\" : \"%s\",%n" + 
				"%s" + 
				"}", name, qName, type, classification);
	}

	public static String getEdge(String from, String to, String type) {
		return String.format(
				"{%n" + 
				"  \"from\" : \"%s\",%n" + 
				"  \"to\" : \"%s\",%n" + 
				"  \"type\" : \"%s\"%n" + 
				"}", from, to, type);
	}
}
