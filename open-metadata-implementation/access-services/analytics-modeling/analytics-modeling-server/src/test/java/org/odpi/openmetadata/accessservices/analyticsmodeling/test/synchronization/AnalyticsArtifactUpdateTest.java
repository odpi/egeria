/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.test.synchronization;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.testng.Assert.assertEquals;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.mockito.Mockito;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerAssets;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.AnalyticsArtifactHandler;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.IdMap;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.converters.AssetConverter;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.AnalyticsAsset;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.MetadataContainer;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.MetadataItem;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.TestUtilities;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.testng.annotations.Test;


public class AnalyticsArtifactUpdateTest extends SynchronizationBaseTest {

	
	/**
	 * Test update unchanged module.
	 * 
	 * @throws Exception
	 */
	@Test
	void testModuleNotChanged() throws Exception {

		// prepare for test
		obj.createAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET,
				TestUtilities.readJsonFile(FOLDER_INPUT, "baseModule"));
		
		ResponseContainerAssets guidsModule = obj.createAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET,
				TestUtilities.readJsonFile(FOLDER_INPUT, "module"));
		
		// Verify structure before test 
		assertEquals(guidsModule.getAssetsList().size(), 1, "Single asset should be created.");
		String guid = guidsModule.getAssetsList().get(0);
		assertSubgraph(guid, "moduleSubgraph");
		

		AnalyticsArtifactHandler objSpy = Mockito.spy(obj);
		
		guidsModule = objSpy.updateAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET,
				TestUtilities.readObjectJson(TestUtilities.readJsonFile(FOLDER_INPUT, "module"), AnalyticsAsset.class));
		
		// Verify structure and content of the updated asset. 
		assertEquals(guidsModule.getAssetsList().size(), 1, "Single asset should be updated.");
		guid = guidsModule.getAssetsList().get(0);

		assertSubgraph(guid, "moduleSubgraph");
		
		verify(objSpy, never()).createContainer(any(MetadataContainer.class), anyString(), anyString(), anyBoolean(), anyString()); 
		verify(objSpy, never()).createItem(any(MetadataItem.class), anyString(), anyString(), anyBoolean(), anyString()); 

	}
	
	/**
	 * Test removed first container with two items, first item in the module, first item in second container.
	 * 
	 * @throws Exception
	 */
	@Test
	void testRemoveObjects() throws Exception {

		// prepare for test
		obj.createAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET,
				TestUtilities.readJsonFile(FOLDER_INPUT, "baseModule"));
		
		ResponseContainerAssets guidsModule = obj.createAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET,
				TestUtilities.readJsonFile(FOLDER_INPUT, "module"));
		
		// Verify structure before test 
		assertEquals(guidsModule.getAssetsList().size(), 1, "Single asset should be created.");
		String guid = guidsModule.getAssetsList().get(0);
		assertSubgraph(guid, "moduleSubgraph");
		
		
		AnalyticsArtifactHandler objSpy = Mockito.spy(obj);

		guidsModule = objSpy.updateAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET,
				TestUtilities.readObjectJson(TestUtilities.readJsonFile(FOLDER_INPUT, "moduleShort"), AnalyticsAsset.class));
		
		//---------------------------------------------------
		// Verify structure and content of the updated asset. 
		//---------------------------------------------------
		assertEquals(guidsModule.getAssetsList().size(), 1, "Single asset should be updated.");
		guid = guidsModule.getAssetsList().get(0);

		
		assertSubgraph(guid, "moduleShortSubgraph");

		verify(objSpy, never()).createContainer(any(MetadataContainer.class), anyString(), anyString(), anyBoolean(), anyString()); 
		verify(objSpy, never()).createItem(any(MetadataItem.class), anyString(), anyString(), anyBoolean(), anyString());
		
		// new definition is missing module item, module container with two items, and item in second container. 
		verify(objSpy, times(5)).removeMetadataObject(any(EntityDetail.class), anyString()); 
	}

	/**
	 * Test added elements:
	 * 	first container with two items,
	 *  first item in the module,
	 *  first item in second container.
	 * 
	 * @throws Exception
	 */
	@Test
	void testAddedObjects() throws Exception {

		// prepare for test
		obj.createAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET,
				TestUtilities.readJsonFile(FOLDER_INPUT, "baseModule"));
		
		ResponseContainerAssets guidsModule = obj.createAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET,
				TestUtilities.readJsonFile(FOLDER_INPUT, "moduleShort"));
		
		// Verify structure before test 
		assertEquals(guidsModule.getAssetsList().size(), 1, "Single asset should be created.");
		String guid = guidsModule.getAssetsList().get(0);
		assertSubgraph(guid, "moduleShortSubgraph");
		
		AnalyticsArtifactHandler objSpy = Mockito.spy(obj);

		guidsModule = objSpy.updateAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET,
				TestUtilities.readObjectJson(TestUtilities.readJsonFile(FOLDER_INPUT, "module"), AnalyticsAsset.class));
		
		//---------------------------------------------------
		// Verify structure and content of the updated asset. 
		//---------------------------------------------------
		assertEquals(guidsModule.getAssetsList().size(), 1, "Single asset should be updated.");
		guid = guidsModule.getAssetsList().get(0);

		assertSubgraph(guid, "moduleSubgraph");

		verify(objSpy, times(1)).createContainer(any(MetadataContainer.class), anyString(), anyString(), anyBoolean(), anyString()); 
		verify(objSpy, times(4)).createItem(any(MetadataItem.class), anyString(), anyString(), anyBoolean(), anyString());
		
	}
	
	/**
	 * Test sourceId added, deleted, modified
	 * 
	 * @throws Exception
	 */
	@Test
	void testUpdateReferences() throws Exception {

		// prepare for test
		obj.createAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET,
				TestUtilities.readJsonFile(FOLDER_INPUT, "baseModule"));
		
		ResponseContainerAssets guidsModule = obj.createAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET,
				TestUtilities.readJsonFile(FOLDER_INPUT, "module"));
		
		// Verify structure before test 
		assertEquals(guidsModule.getAssetsList().size(), 1, "Single asset should be created.");
		String guid = guidsModule.getAssetsList().get(0);
		assertSubgraph(guid, "moduleSubgraph");
		
		guidsModule = obj.updateAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET,
				TestUtilities.readObjectJson(TestUtilities.readJsonFile(FOLDER_INPUT, "moduleReferences"), AnalyticsAsset.class));
		
		//---------------------------------------------------
		// Verify structure and content of the updated asset. 
		//---------------------------------------------------
		assertEquals(guidsModule.getAssetsList().size(), 1, "Single asset should be updated.");
		guid = guidsModule.getAssetsList().get(0);

		assertSubgraph(guid, "moduleReferencesSubgraph");
	}

	/**
	 * Test restore reference scenario.
	 * Asset M1 is referenced by asset M2, and there is relationship R(M2.C1.I1->M1.C1.I1).
	 * Update M1 where M1.C1.I1 is removed will remove relationship R().
	 * Next asset M1 update with defined M1.C1.I1 should restore the relationship R().  
	 * 
	 * @throws Exception
	 */
	@Test
	void testRestoreReferences() throws Exception {

		// prepare for test
		String baseModule = TestUtilities.readJsonFile(FOLDER_INPUT, "baseModule");
		AnalyticsAsset assetBaseModule = createBean("baseModule");
		// create entities for columns referenced in base module
		Map<String, String>guidMap = new HashMap<>();
		createReferencedEntitiesForMetadataLinks(assetBaseModule.getContainer(), guidMap);
		// replace GUIDs from input with real GUIDs of created entities
		for (Entry<String, String> pair : guidMap.entrySet()) {
			baseModule = baseModule.replace(pair.getKey(), pair.getValue());
		}

		ResponseContainerAssets guidsModule = obj.createAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET, baseModule);
		String guidBase = guidsModule.getAssetsList().get(0);
		
		guidsModule = obj.createAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET, 
				TestUtilities.readJsonFile(FOLDER_INPUT, "module"));
		
		// Verify structure before test 
		String guidModule = guidsModule.getAssetsList().get(0);
		assertSubgraph(guidBase, "baseModuleSubgraph");
		assertSubgraph(guidModule, "moduleSubgraph");

		// identifier change removes old object and creates new one with the new identifier
		AnalyticsAsset updatedBM = TestUtilities.readObjectJson(baseModule, AnalyticsAsset.class);
		updatedBM.getContainer().get(0).setIdentifier("COUNTRY1");

		obj.updateAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET, updatedBM);
		// Verify structure and content of the updated asset and dependent asset. 
		assertSubgraph(guidBase, "baseModuleNewIdentifierSubgraph");
		assertSubgraph(guidModule, "moduleRemovedReferenceSubgraph");	// two relationships were removed from moduleSubgraph.json
		
		// update with original definition: roll back
		obj.updateAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET, 
				TestUtilities.readObjectJson(baseModule, AnalyticsAsset.class));
		// Verify structure and content of the updated asset and dependent asset. 
		assertSubgraph(guidBase, "baseModuleSubgraph");
		assertSubgraph(guidModule, "moduleSubgraph");
	}
	
	/**
	 * Test update properties of a module asset.
	 * 
	 * @throws Exception
	 */
	@Test
	void testUpdateModuleAssetObject() throws Exception {
		
		AssetConverter assetConverter = new AssetConverter(ctx.getRepositoryHelper(), ctx.getServiceName(), ctx.getServerName());

		String UPDATED = "UPDATED: ";
		String TIMESTAMP = "TimeStamp";
		String DBO_GUID = "databaseGUID:dbo";
		String testName = "testUpdateModuleAssetObject";

		// prepare for test
		AnalyticsAsset module = new AnalyticsAsset();
		module.setUid(UID_STOREID);
		module.setDisplayName(MODULE_NAME);
		module.setLocation(UID_PATH);
		module.setSourceGuid(Arrays.asList(DBO_GUID));
		module.setLastModified(TIMESTAMP);
		module.setType("baseModule");
		module.setDescription(TEST_DESCRIPTION);
		
		String strModule = TestUtilities.writeObjectJson(module);
		ResponseContainerAssets guidsModule = obj.createAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET, strModule);
		String guid = guidsModule.getAssetsList().get(0);
		
		AnalyticsArtifactHandler objSpy = Mockito.spy(obj);
		
		// property updates one by one confirm that each update is completed
		module.setDisplayName(UPDATED + MODULE_NAME);
		objSpy.updateAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET, module);
		assertEquals(getAsset(assetConverter, guid, testName).getDisplayName(), UPDATED + MODULE_NAME, "Module name update failed");
		
		module.setDescription(UPDATED + TEST_DESCRIPTION);
		objSpy.updateAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET, module);
		assertEquals(getAsset(assetConverter, guid, testName).getDescription(), UPDATED + TEST_DESCRIPTION, "Description update failed");

		module.setLocation(UPDATED + UID_PATH);
		objSpy.updateAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET, module);
		assertEquals(getAsset(assetConverter, guid, testName).getLocation(), UPDATED + UID_PATH, "Location update failed");

		module.setLastModified(UPDATED + TIMESTAMP);
		objSpy.updateAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET, module);
		AnalyticsAsset repositoryAsset = getAsset(assetConverter, guid, testName);
		assertEquals(repositoryAsset.getLastModified(), UPDATED + TIMESTAMP, "Last modified update failed");

		repositoryAsset.setGuid(GUID_TO_COMPARE);	// replace random GUID with predefined value to compare
		TestUtilities.assertObjectJson(repositoryAsset, TestUtilities.readJsonFile(FOLDER_MASTER, "baseModuleAssetUpdate"));
	}

	/**
	 * Test update report.
	 * @throws Exception
	 */
	@Test
	void testUpdateVisualization() throws Exception {

		obj.createAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET, TestUtilities.readJsonFile(FOLDER_INPUT, "baseModule"));
		obj.createAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET, TestUtilities.readJsonFile(FOLDER_INPUT, "module"));
		
		String report = TestUtilities.readJsonFile(FOLDER_INPUT, "report");
		ResponseContainerAssets guidsReport = obj.createAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET, report);
		String guidModule = guidsReport.getAssetsList().get(0);
		String guidReport = guidsReport.getAssetsList().get(1);
		
		// Verify structure and content of the built asset before test. 
		assertEquals(guidsReport.getAssetsList().size(), 2, "Module and Visualization assets should be created.");
		assertSubgraph(guidModule, "reportModuleSubgraph");
		assertSubgraph(guidReport, "reportSubgraph");
		
		// update report
		obj.updateAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET,
				TestUtilities.readObjectJson(TestUtilities.readJsonFile(FOLDER_INPUT, "report2"), AnalyticsAsset.class));
		assertSubgraph(guidReport, "report2Subgraph");
	}
	

	/**
	 * Test update modified reference to module.
	 * Module M2 references module M1A as M1 alias and has relationships to its items.
	 * When M2 changes reference from M1A to M1B but alias remains M1, the definitions of items (sourceIds) stay the same (M1.C1.I1).
	 * But because M2 points to M1B instead M1A the relationships must be modified to point to correct entities in repository.
	 * @throws Exception
	 */
	@Test
	void testUpdateModifiedModuleReference() throws Exception {

		obj.createAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET, TestUtilities.readJsonFile(FOLDER_INPUT, "baseModule"));
		obj.createAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET, TestUtilities.readJsonFile(FOLDER_INPUT, "baseModule2"));
		
		String module = TestUtilities.readJsonFile(FOLDER_INPUT, "module");
		ResponseContainerAssets guidsReport = obj.createAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET, module);
		String guidModule = guidsReport.getAssetsList().get(0);
		
		// Verify structure and content of the built asset before test. 
		assertSubgraph(guidModule, "moduleSubgraph");
		
		// modify reference from baseModule to baseModule2
		module = module.replaceAll("iBASEMODULE", "iBASEMODULE2").replaceAll("_GOSALES_Egeria", "_GOSALES_Egeria2");
		AnalyticsAsset asset = TestUtilities.readObjectJson(module, AnalyticsAsset.class);
		
		// update report
		obj.updateAssets(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET, asset);
		assertSubgraph(guidModule, "module2Subgraph");
	}	
	/**
	 * Helper function to get Analytics Asset object.
	 * @param assetConverter to convert entity to bean.
	 * @param guid of the entity.
	 * @param test name for logging.
	 * @return
	 * @throws PropertyServerException
	 * @throws InvalidParameterException
	 * @throws UserNotAuthorizedException
	 */
	private AnalyticsAsset getAsset(AssetConverter assetConverter, String guid, String test) throws PropertyServerException, InvalidParameterException, UserNotAuthorizedException {
		return assetConverter.getNewBean(AnalyticsAsset.class,
				ctx.getRepositoryHandler().getEntityByGUID(ctx.getUserId(), guid, "guid", IdMap.ASSET_TYPE_NAME, test), test);
	}

}
