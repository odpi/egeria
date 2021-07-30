/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.test.synchronization;


import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;

import java.util.Arrays;
import java.util.List;

import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerAssets;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.AnalyticsAsset;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.TestUtilities;
import org.odpi.openmetadata.commonservices.generichandlers.OpenMetadataAPIMapper;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

import org.testng.annotations.Test;

/**
 * The class contains tests related to DELETE artifact operation.
 * 
 * Tests handling other operation after the DELETE operation should be placed here as well. 
 *
 */
class AnalyticsArtifactDeleteTest extends SynchronizationBaseTest {

	
	/**
	 * Test the delete base module case.
	 * 1. All elements of the module is removed from repository (use QName)
	 * 2. Elements referenced by the deleted module are in repository.
	 * 
	 * @throws Exception
	 */
	@Test
	void testDeleteBaseModule() throws Exception {

		String methodName = "testDeleteBaseModule";
		String baseModule = getBaseModuleJson("baseModule");	// base module definition with fixed GUIDs
		AnalyticsAsset assetBaseModule = TestUtilities.readObjectJson(baseModule, AnalyticsAsset.class);

		ResponseContainerAssets guids = obj.createAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET, assetBaseModule);
		
		//---------------------------------------------------
		// Verify structure and content of the built asset. 
		//---------------------------------------------------
		String guid = guids.getAssetsList().get(0);
		assertSubgraph(guid, "baseModuleSubgraph");

		EntityDetail asset = omEntityDao.getEntityByGuid(guid);
		String assetQName = omEntityDao.getEntityQName(asset);

		guids = obj.deleteAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET, UID_STOREID);
		
		List<EntityDetail> elements = getAssetEntities(assetQName, methodName);
		
		assertNull(elements, "All elements of baseModule should be removed from repository.");

		// delete should not remove entities referenced by the deleted asset
		// thus relationships to the entities created outside of the module should be created again 
		guids = obj.createAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET, TestUtilities.readObjectJson(baseModule, AnalyticsAsset.class));
		assertSubgraph(guids.getAssetsList().get(0), "baseModuleSubgraph");
	}


	private List<EntityDetail> getAssetEntities(String assetQName, String methodName) 
			throws InvalidParameterException, PropertyServerException, UserNotAuthorizedException 
	{
		String pattern = ctx.getRepositoryHelper().getStartsWithRegex(assetQName);

		return ctx.getServerSoftwareCapabilityHandler().getEntitiesByValue(
						ctx.getUserId(),
						pattern,
						"pattern",
						null,
						null,
						Arrays.asList(OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME), false,
						null, null, 0, 0, methodName);
	}


	/**
	 * The delete asset referencing and referenced other assets.
	 * 
	 * 1. All elements of the module is removed from repository (use QName)
	 * 2. Elements referenced by the deleted module are in repository.
	 * 
	 * @throws Exception
	 */
	@Test
	void testDeleteModule() throws Exception {

		String methodName = "testDeleteModule";

		AnalyticsAsset assetBaseModule = getBaseModuleAsset("baseModule");

		ResponseContainerAssets  guidsBModule = obj.createAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET, assetBaseModule);

		ResponseContainerAssets guidsModule = obj.createAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET, createBean("module"));

		assertSubgraph(guidsModule.getAssetsList().get(0), "moduleSubgraph");

		ResponseContainerAssets guidsDashBoard = obj.createAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET, createBean("dashboard"));
		
		assertSubgraph(guidsDashBoard.getAssetsList().get(0), "dashboardSubgraph");

		String assetQName = omEntityDao.getEntityQName(omEntityDao.getEntityByGuid(guidsModule.getAssetsList().get(0)));

		// delete an asset (module) referencing and referenced other assets.
		obj.deleteAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET, UID_STOREID_MODULE);

		//---------------------------------------------------
		// Verify structure and content of the deleted and related assets. 
		//---------------------------------------------------
		assertNull(getAssetEntities(assetQName, methodName), "All elements of module should be removed from repository.");

		assertSubgraph(guidsBModule.getAssetsList().get(0), "baseModuleSubgraph");
		assertSubgraph(guidsDashBoard.getAssetsList().get(0), "dashboardDisconnectedSubgraph");
	}

	/**
	 * The asset delete operation removes artifact represented by two assets.
	 * 
	 * @throws Exception
	 */
	@Test
	void testDeleteVisualizationWithModule() throws Exception {

		String methodName = "testDeleteVisualizationWithModule";
		
		obj.createAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET,	createBean("module"));
		
		ResponseContainerAssets guidsReport = obj.createAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET, createBean("report"));
		
		assertSubgraph(guidsReport.getAssetsList().get(1), "reportSubgraph");
		
		String assetQName1 = omEntityDao.getEntityQName(omEntityDao.getEntityByGuid(guidsReport.getAssetsList().get(0)));
		String assetQName2 = omEntityDao.getEntityQName(omEntityDao.getEntityByGuid(guidsReport.getAssetsList().get(1)));

		// delete report assets 
		guidsReport = obj.deleteAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET, UID_STOREID_REPORT);

		//---------------------------------------------------
		// Verify repository does not have entities for artifact elements 
		//---------------------------------------------------
		assertEquals(guidsReport.getAssetsList().size(), 2, "Two assets should be reported as deleted.");
		assertNull(getAssetEntities(assetQName1, methodName), "All elements of module should be removed from repository.");
		assertNull(getAssetEntities(assetQName2, methodName), "All elements of visualization should be removed from repository.");

	}
}
