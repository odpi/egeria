/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.test.synchronization;


import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.odpi.openmetadata.accessservices.analyticsmodeling.ffdc.exceptions.AnalyticsModelingCheckedException;
import org.odpi.openmetadata.accessservices.analyticsmodeling.model.ResponseContainerAssets;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.AnalyticsArtifactHandler;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.ExecutionContext;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.AnalyticsAsset;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.AnalyticsMetadata;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.MetadataItem;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.MetadataContainer;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.InMemoryRepositoryTest;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.JsonMocks;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.TestUtilities;
import org.odpi.openmetadata.accessservices.analyticsmodeling.utils.Constants;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

class AnalyticsArtifactHandlerTest extends InMemoryRepositoryTest {

	private static final String FOLDER_INPUT = "/src/test/resources/synchronization/input/";
	private static final String FOLDER_MASTER = "/src/test/resources/synchronization/master/";
	
	private static final String HTTP_LOCALHOST_9300_P2PD_SERVLET = "http://localhost:9300/p2pd/servlet";

	private static final String UID_PATH = "CAMID(\":\")/dataSource[@name='_GOSALES_Egeria']/dataSourceConnection[@name='_GOSALES_Egeria']"
											+ "/dataSourceSchema[@name='GOSALES/dbo']/baseModule[@name='dbo']";

	private static final String UID_STOREID = "iBASEMODULE";
	
	// initialize execution context
	ExecutionContext ctx;
	
	// handler to test functionality 
	AnalyticsArtifactHandler obj;
	
	@BeforeMethod
	public void setup() throws Exception {
		super.setup();
		
		ctx = new ExecutionContext(
				serviceName, 
				serverName, 
				invalidParameterHandler,
				repositoryHandler,
				enterpriseConnector.getRepositoryHelper(),
				LOCAL_SERVER_USER_ID,
				null, // securityVerifier,
				null, // supportedZones,
				null, // defaultZones,
				null, // publishZones,
				auditLog);
		
		ctx.initializeSoftwareServerCapability(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET);
		
		obj = new AnalyticsArtifactHandler(ctx);
	}
	
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

		obj.addContainer(qs);
		
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
		obj.addContainer(qs);
		
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
	
	
	//----------------------------------------------------------------------------------
	// Verify structure and content of the built asset as graph with nodes and edges. 
	//----------------------------------------------------------------------------------
	private void assertSubgraph(String guid, String fileMaster) throws IOException, AnalyticsModelingCheckedException {
		ArrayList<EntityDetail> nodes = new ArrayList<>();
		ArrayList<Relationship> edges = new ArrayList<>();
		
		String assetQName = omEntityDao.getEntityQName(omEntityDao.getEntityByGuid(guid));

		try {
			
			collectRelatedNodes(nodes, edges, guid, assetQName);
			orderNodes(nodes);
			orderEdges(edges);
			
			String output = printGraph(nodes, edges);
			
			String master = TestUtilities.readJsonFile(FOLDER_MASTER, fileMaster);

			assertEquals(output, master);
		
		} catch (AnalyticsModelingCheckedException e) {
			fail(e.getLocalizedMessage());
		}
		
	}

	private String printGraph(ArrayList<EntityDetail> nodes, ArrayList<Relationship> edges) {

		StringBuilder content = new StringBuilder();
		content.append("{")
		.append(buildJsonArray("node", nodes))
		.append(",")
		.append(buildJsonArray("edge", edges))
		.append("}");
		
		try {
			ObjectMapper mapper = new ObjectMapper();
			JsonNode jn = mapper.readTree(content.toString().getBytes());
			return jn.toPrettyString();
		} catch (IOException e) {
			return content.toString();
		}
	}

	private String buildJsonArray(String name, List<? extends Object> list) {
		StringBuilder content = new StringBuilder();
		for ( Object element : list) {
			if (content.length() != 0) {
				content.append(",");
			}
			if (element instanceof EntityDetail) {
				content.append(printNode((EntityDetail)element));
			} else if (element instanceof Relationship) {
				content.append(printEdge((Relationship)element));
			} else if (element instanceof Classification) {
				content.append(printClassification((Classification)element));
			}
		}
		return String.format("\"%s\":[%s]", name, content.toString());
	}

	private Object printClassification(Classification element) {
		return String.format("\"%s\"", element.getName());
	}

	private String printEdge(Relationship edge) {
		try {
			return JsonMocks.getEdge(omEntityDao.getEntityQName(omEntityDao.getEntityByGuid(edge.getEntityOneProxy().getGUID())),
					omEntityDao.getEntityQName(omEntityDao.getEntityByGuid(edge.getEntityTwoProxy().getGUID())),
					edge.getType().getTypeDefName());
		} catch (AnalyticsModelingCheckedException e) {
			return composeRelationshipQName(edge);
		}
	}

	private String printNode(EntityDetail node) {
		InstanceProperties props = omEntityDao.getMapProperty(node.getProperties(), Constants.ADDITIONAL_PROPERTIES);
		List<? extends Object> list = node.getClassifications();
		
		String displayName = omEntityDao.getEntityStringProperty(node, Constants.DISPLAY_NAME);
		
		if (displayName == null) {
			// for asset entity
			displayName = omEntityDao.getEntityStringProperty(node, Constants.NAME);
		}
		
		return JsonMocks.getNode(displayName,
				omEntityDao.getEntityQName(node),
				omEntityDao.getStringProperty(Constants.TYPE, props),
				buildJsonArray("classification", list));
	}

	private String composeRelationshipQName(Relationship r) {
		try {
			return String.format("{%s->%s}", 
					omEntityDao.getEntityQName(omEntityDao.getEntityByGuid(r.getEntityOneProxy().getGUID())),
					omEntityDao.getEntityQName(omEntityDao.getEntityByGuid(r.getEntityTwoProxy().getGUID()))
				);
		} catch (AnalyticsModelingCheckedException e) {
			return r.getGUID();
		}
	}
	
	private void orderEdges(ArrayList<Relationship> edges) {
		
		Collections.sort(edges, new Comparator<Relationship>() {
		    @Override
		    public int compare(Relationship lhs, Relationship rhs) {
				return composeRelationshipQName(lhs).compareTo(composeRelationshipQName(rhs));
		    }
		});
	}

	/**
	 * Order nodes by qualified name.
	 * @param nodes to sort.
	 */
	private void orderNodes(ArrayList<EntityDetail> nodes) {
		Collections.sort(nodes, new Comparator<EntityDetail>() {
		    @Override
		    public int compare(EntityDetail lhs, EntityDetail rhs) {
		        return omEntityDao.getEntityQName(lhs).compareTo(omEntityDao.getEntityQName(rhs));
		    }
		});
	}

	private void collectRelatedNodes(ArrayList<EntityDetail> nodes, ArrayList<Relationship> edges, String guid, String assetQName)
			throws AnalyticsModelingCheckedException {

		if (nodes.stream()
				.map(node->omEntityDao.getEntityStringProperty(node, Constants.GUID))
				.filter(nodeGuid->guid.equals(nodeGuid))
				.findFirst().isPresent()) 
		{
			// already in the list
			return;
		}
		
		EntityDetail start = omEntityDao.getEntityByGuid(guid);
		
		if (assetQName != null && !omEntityDao.getEntityQName(start).startsWith(assetQName)) {
			return;	// node out of boundaries of the request
		}

		nodes.add(start);

		// collect all nodes connected to the start as second end of a relationship
		List<Relationship> rels = omEntityDao.getRelationshipsForEntity(start, null).stream()
			.filter(r->r.getEntityOneProxy().getGUID().equals(guid))
			.collect(Collectors.toList());
		
		for (Relationship r : rels) {
			String relGuid = r.getGUID();
			
			if (edges.stream()
					.map(edge->edge.getGUID())
					.filter(edgeGuid->edgeGuid.equals(relGuid))
					.findFirst().isPresent()) 
			{
				// already in the list
				continue;
			}

			edges.add(r);
			collectRelatedNodes(nodes, edges, r.getEntityTwoProxy().getGUID(), assetQName);
		}
	}


	//----------------------------------------------------------------------------------------------------------------
	// Tests support functions.
	//----------------------------------------------------------------------------------------------------------------
	private void createReferencedEntitiesForMetadataLinks(List<? extends AnalyticsMetadata> list, Map<String, String> guidMap ) {
		if (list == null) {
			return;
		}
		
		for (AnalyticsMetadata mtdObject : list) {
			
			if (mtdObject instanceof MetadataItem) {
				if (mtdObject.getSourceGuid() != null) {
					for( String orgGuid : mtdObject.getSourceGuid()) {
						try {
							InstanceProperties columnTypeProperties = new EntityPropertiesBuilder("JUnitTest", "createReferencedEntitiesForMetadataLinks", null)
					                .withStringProperty(Constants.QUALIFIED_NAME, orgGuid).build();
							
							String guid = omEntityDao.createOrUpdateEntity(Constants.RELATIONAL_COLUMN_TYPE, orgGuid, columnTypeProperties, null, false, false)
									.getEntityDetail().getGUID();
							
							guidMap.put(orgGuid, guid);
						} catch (Exception e) {
							fail("Failed create referenced entity: " + orgGuid);
						}
					}
				}
			}
			
			if (mtdObject instanceof MetadataContainer) {
				createReferencedEntitiesForMetadataLinks(((MetadataContainer) mtdObject).getContainer(), guidMap);
				createReferencedEntitiesForMetadataLinks(((MetadataContainer) mtdObject).getItem(), guidMap);
			} else if (mtdObject instanceof MetadataItem) {
				createReferencedEntitiesForMetadataLinks(((MetadataItem) mtdObject).getItem(), guidMap);
			}
		}
	}

	private AnalyticsAsset createBean(String input) throws IOException {
		String json = TestUtilities.readJsonFile(FOLDER_INPUT, input);
		return TestUtilities.readObjectJson(json, AnalyticsAsset.class);
	}
	
	private AnalyticsAsset createBeanWithAssertion(String input) throws IOException {
		AnalyticsAsset asset = createBean(input);
		// confirm data read successful
		TestUtilities.assertObjectJson(asset, TestUtilities.readJsonFile(FOLDER_MASTER, input));
		return asset;
	}
}
