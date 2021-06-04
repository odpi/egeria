/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.test.synchronization;


import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.IdMap;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.MetadataItem;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.MetadataContainer;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.JsonMocks;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.TestUtilities;
import org.odpi.openmetadata.accessservices.analyticsmodeling.utils.QualifiedNameUtils;

class MetadataContainerTest {

	private static final String FOLDER_MASTER = "/src/test/resources/synchronization/master/";

	@Test
	void testBuildContainer() throws IOException {
		MetadataContainer obj = new MetadataContainer();
		String id = "qsID";

		// from Referenceable
		obj.setQualifiedName(QualifiedNameUtils.buildQualifiedName("PARENT", IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME, id));
		Map<String, String> props = new TreeMap<>();
		props.put("p1", "v1");
		props.put("p2", "v2");
		obj.setAdditionalProperties(props);
		
		// from SchemaElement
		obj.setDisplayName("displayNameQS");
		obj.setDescription("Query Subject description");
		
		// from SchemaAttribute
		obj.setElementPosition(1);

		// from QuerySubject
		obj.setIdentifier(id);
		obj.setSourceGuid(Arrays.asList("guid1", "guid2"));
		obj.setSourceId(Arrays.asList("uid1", "uid2"));
		
		MetadataItem qi1 = TestUtilities.readObjectJson(
				JsonMocks.getItem(
						QualifiedNameUtils.buildQualifiedName(obj.getQualifiedName(), IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME, "qiID1"),
						"qiName1", "qiID1", "qiExpression1", "VARCHAR(1)"),	MetadataItem.class);
		
		MetadataItem qi2 = TestUtilities.readObjectJson(
				JsonMocks.getItem(
						QualifiedNameUtils.buildQualifiedName(obj.getQualifiedName(), IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME, "qiID2"),
						"qiName2", "qiID2", "qiExpression2", "VARCHAR(2)"),	MetadataItem.class);
		
		obj.setItem(Arrays.asList(qi1, qi2));
		
		TestUtilities.assertObjectJson(obj, TestUtilities.readJsonFile(FOLDER_MASTER, "container"));
	}

	@Test
	void testContainerFromJson() throws IOException {
		MetadataContainer obj = TestUtilities.readObjectJson(
				JsonMocks.getContainer("PARENT::(SchemaAttribute)=qsID", "displayNameQS", "qsID"),
				MetadataContainer.class);

		TestUtilities.assertObjectJson(obj, TestUtilities.readJsonFile(FOLDER_MASTER, "containerPartial"));
	}
}
