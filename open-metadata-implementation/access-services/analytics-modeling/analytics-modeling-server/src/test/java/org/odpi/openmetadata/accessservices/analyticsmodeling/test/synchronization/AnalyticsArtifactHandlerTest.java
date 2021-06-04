/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.test.synchronization;


import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerAssets;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.AnalyticsAsset;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.AnalyticsAssetUtils;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.MetadataItem;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.MetadataContainer;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.JsonMocks;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.TestUtilities;
import org.testng.annotations.Test;


class AnalyticsArtifactHandlerTest extends SynchronizationBaseTest {

	
	@Test
	void testBuildBaseModule() throws Exception {
		AnalyticsAsset obj = new AnalyticsAsset();
		obj.setUid(UID_STOREID);
		obj.setDisplayName("dbo");
		obj.setLocation(UID_PATH);
		obj.setSourceGuid(Arrays.asList("databaseGUID:dbo"));
		
		MetadataContainer qs = TestUtilities.readObjectJson(
				JsonMocks.getEmptyQuerySubject("Country", "COUNTRY", 1, "tableGUID:COUNTRY@dbo"),
				MetadataContainer.class);

		MetadataItem qi = TestUtilities.readObjectJson(
				JsonMocks.getBaseModuleQueryItem("Country", "COUNTRY", "COUNTRY", 1, "VARCHAR", "columnGUID:COUNTRY@COUNTRY@dbo"),
				MetadataItem.class);
		qs.addItem(qi);
		
		qi = TestUtilities.readObjectJson(
				JsonMocks.getBaseModuleQueryItem("Country Code", "COUNTRYCODE", "COUNTRYCODE", 2, "INTEGER", "columnGUID:COUNTRYCODE@COUNTRY@dbo"),
				MetadataItem.class);
		qs.addItem(qi);

		AnalyticsAssetUtils.addContainer(obj, qs);
		
		qs = TestUtilities.readObjectJson(
				JsonMocks.getEmptyQuerySubject("Sales", "SALES", 2, "tableGUID:@SALES@dbo"),
				MetadataContainer.class);
		MetadataItem [] items = {
				TestUtilities.readObjectJson(JsonMocks.getBaseModuleQueryItem(
					"Sales Country Code", "SALESCOUNTRYCODE", "SALESCOUNTRYCODE", 1, "INTEGER", "columnGUID:SALESCOUNTRYCODE@SALES@dbo"), MetadataItem.class),
				TestUtilities.readObjectJson(JsonMocks.getBaseModuleQueryItem(
					"Quantity", "QUANTITY", "QUANTITY", 2, "INTEGER", "columnGUID:QUANTITY@SALES@dbo"), MetadataItem.class),
		};
		qs.setItem(Arrays.asList(items));
		AnalyticsAssetUtils.addContainer(obj, qs);
		
		TestUtilities.assertObjectJson(obj, TestUtilities.readJsonFile(FOLDER_MASTER, "baseModuleAsset"));
	}	

	@Test
	void testReadBaseModule() throws Exception {
		createBeanWithAssertion("baseModule");
	}
	
	/**
	 * Base module has GUIDs used to connect to original asset.
	 * Identifier resolution is not required.
	 * 
	 * @throws Exception
	 */
	@Test
	void testCreateBaseModule() throws Exception {

		String input = "baseModule";
		AnalyticsAsset baseModule = createBean(input);

		// create entities for columns referenced in base module
		Map<String, String>guidMap = new HashMap<>();
		createReferencedEntitiesForMetadataLinks(baseModule.getContainer(), guidMap);
		String json = TestUtilities.readJsonFile(FOLDER_INPUT, input);
		// replace GUIDs from input with real GUIDs of created entities
		for (Entry<String, String> pair : guidMap.entrySet()) {
			json = json.replace(pair.getKey(), pair.getValue());
		}

		ResponseContainerAssets guids = obj.createAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET, json);
		
		assertEquals(guids.getAssetsList().size(), 1, "Single asset should be created.");
		
		//---------------------------------------------------
		// Verify structure and content of the built asset. 
		//---------------------------------------------------
		String guid = guids.getAssetsList().get(0);

		assertSubgraph(guid, "baseModuleSubgraph");
	}

	/**
	 * Module has identifiers used to connect to existing asset.
	 * Identifier resolution is required.
	 * 
	 * @throws Exception
	 */
	@Test
	void testCreateModule() throws Exception {

		obj.createAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET,
				TestUtilities.readJsonFile(FOLDER_INPUT, "baseModule"));
		
		ResponseContainerAssets guidsModule = obj.createAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET,
				TestUtilities.readJsonFile(FOLDER_INPUT, "module"));
		
		//---------------------------------------------------
		// Verify structure and content of the built asset. 
		//---------------------------------------------------
		assertEquals(guidsModule.getAssetsList().size(), 1, "Single asset should be created.");
		String guid = guidsModule.getAssetsList().get(0);
		
		assertSubgraph(guid, "moduleSubgraph");
	}
	
	/**
	 * Report has module and visualization parts used to create two asset.
	 * Identifier resolution is not required in both assets.
	 * 
	 * @throws Exception
	 */
	@Test
	void testCreateReport() throws Exception {

		obj.createAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET, TestUtilities.readJsonFile(FOLDER_INPUT, "baseModule"));
		obj.createAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET, TestUtilities.readJsonFile(FOLDER_INPUT, "module"));
		
		
		ResponseContainerAssets guidsReport = obj.createAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET,
				TestUtilities.readJsonFile(FOLDER_INPUT, "report"));
		
		//---------------------------------------------------
		// Verify structure and content of the built asset. 
		//---------------------------------------------------
		assertEquals(guidsReport.getAssetsList().size(), 2, "Module and Visualization assets should be created.");
		String guid = guidsReport.getAssetsList().get(1);

		assertSubgraph(guid, "reportSubgraph");
	}

	/**
	 * Dash board has only visualization part.
	 * Identifiers should point to referenced module entities.
	 * 
	 * @throws Exception
	 */
	@Test
	void testCreateDashboard() throws Exception {

		obj.createAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET, TestUtilities.readJsonFile(FOLDER_INPUT, "baseModule"));
		obj.createAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET, TestUtilities.readJsonFile(FOLDER_INPUT, "module"));
		
		ResponseContainerAssets guidsDashBoard = obj.createAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET,
				TestUtilities.readJsonFile(FOLDER_INPUT, "dashboard"));
		
		//---------------------------------------------------
		// Verify structure and content of the built asset. 
		//---------------------------------------------------
		assertEquals(guidsDashBoard.getAssetsList().size(), 1, "Visualization only asset should be created.");
		String guid = guidsDashBoard.getAssetsList().get(0);

		assertSubgraph(guid, "dashboardSubgraph");
	}

}
