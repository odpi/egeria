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
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.JsonMocks;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.TestUtilities;
import org.odpi.openmetadata.accessservices.analyticsmodeling.utils.QualifiedNameUtils;

class MetadataItemTest {

	private static final String FOLDER_MASTER = "/src/test/resources/synchronization/master/";

	@Test
	void testBuildMetadataItem() throws IOException {
		MetadataItem obj = new MetadataItem();
		String id = "qiID";

		// from Referenceable
		obj.setQualifiedName(QualifiedNameUtils.buildQualifiedName("PARENT", IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME, id));
		Map<String, String> props = new TreeMap<>();
		props.put("p1", "v1");
		props.put("p2", "v2");
		obj.setAdditionalProperties(props);
		
		// from SchemaElement
		obj.setDisplayName("displayName");
		obj.setDescription("Query Item description");
		
		// from SchemaAttribute
		obj.setElementPosition(1);

		// from QueryItem
		obj.setIdentifier(id);
		obj.setExpression("qiExpression");
		obj.setDataType("VARCHAR");
		obj.setSourceGuid(Arrays.asList("guid1", "guid2"));
		obj.setSourceId(Arrays.asList("uid1", "uid2"));
		
		TestUtilities.assertObjectJson(obj, TestUtilities.readJsonFile(FOLDER_MASTER, "item"));
	}

	@Test
	void testMetadataItemFromJson() throws IOException {
		MetadataItem obj = TestUtilities.readObjectJson(
				JsonMocks.getItem("PARENT::(SchemaAttribute)=qiID", "displayName", "qiID", "qiExpression", "VARCHAR"),
				MetadataItem.class);

		TestUtilities.assertObjectJson(obj, TestUtilities.readJsonFile(FOLDER_MASTER, "itemPartial"));
	}
}
