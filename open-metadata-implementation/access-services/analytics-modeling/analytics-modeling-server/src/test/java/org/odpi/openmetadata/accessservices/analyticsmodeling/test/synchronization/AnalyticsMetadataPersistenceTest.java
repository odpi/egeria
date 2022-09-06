/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.analyticsmodeling.test.synchronization;


import static org.testng.Assert.assertEquals;
import static org.testng.Assert.fail;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.IdMap;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.beans.SchemaAttribute;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.beans.SchemaType;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.builders.AnalyticsMetadataBuilder;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.converters.AnalyticsMetadataConverter;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.converters.SchemaTypeConverter;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.MetadataContainer;
import org.odpi.openmetadata.accessservices.analyticsmodeling.synchronization.model.MetadataItem;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.InMemoryRepositoryTest;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.JsonMocks;
import org.odpi.openmetadata.accessservices.analyticsmodeling.test.utils.TestUtilities;
import org.odpi.openmetadata.accessservices.analyticsmodeling.utils.Constants;
import org.odpi.openmetadata.accessservices.analyticsmodeling.utils.ExecutionContext;
import org.odpi.openmetadata.commonservices.generichandlers.SchemaAttributeHandler;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

class AnalyticsMetadataPersistenceTest extends InMemoryRepositoryTest {

	private static final List<String> SOURCE_LIST = Arrays.asList("uid1", "uid2");
	private static final String ITEM_DATA_TYPE = "VARCHAR";
	private static final String ITEM_EXPRESSION = "qiExpression";
	private static final String ITEM_ID = "qiID";
	private static final String ITEM_DISPLAY_NAME = "displayName";
	private static final String ITEM_QNAME = "PARENT::(SchemaAttribute)=qiID";
	private static final String TEST_ANALYTICS_METADATA_TYPE = "TEST_ANALYTICS_METADATA_TYPE";
	private static final String CONTAINER_DISPLAY_NAME = "displayNameQS";
	private static final String CONTAINER_ID = "qsID";
	private static final String CONTAINER_QNAME = "PARENT::(SchemaAttribute)=qsID";
	private static final String HTTP_LOCALHOST_9300_P2PD_SERVLET = "http://localhost:9300/p2pd/servlet";
	private static final String TEST_DESCRIPTION = "TEST_DESCRIPTION";

	// initialize execution context
	ExecutionContext ctx;
	
	SchemaAttributeHandler<SchemaAttribute, SchemaType> metadataHandler;

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
		
		ctx.initializeSoftwareServerCapability(USER_ID, HTTP_LOCALHOST_9300_P2PD_SERVLET, null);
		
		metadataHandler = new SchemaAttributeHandler<>(
				new AnalyticsMetadataConverter(ctx.getRepositoryHelper(), ctx.getServiceName(), ctx.getServerName()),
                SchemaAttribute.class,
                new SchemaTypeConverter(ctx.getRepositoryHelper(), ctx.getServiceName(), ctx.getServerName()),
                SchemaType.class,
                ctx.getServiceName(), ctx.getServerName(),
				ctx.getInvalidParameterHandler(), ctx.getRepositoryHandler(), ctx.getRepositoryHelper(),
				ctx.getLocalServerUserId(), ctx.getSecurityVerifier(), 
				ctx.getSupportedZones(), ctx.getDefaultZones(), ctx.getPublishZones(), ctx.getAuditLog());
		
	}
	
	/**
	 * Test builder to save attributes in repository from bean
	 * and converter to load from repository into a bean for container
	 */
	@Test
	void testContainerPersistence() throws IOException {
		
		String methodName = "testContainerPersistance";

		// test builder to save bean into repository
		MetadataContainer obj = TestUtilities.readObjectJson(
				JsonMocks.getContainer(CONTAINER_QNAME, CONTAINER_DISPLAY_NAME, CONTAINER_ID),
				MetadataContainer.class);
		
		obj.setDescription(TEST_DESCRIPTION);
		obj.setType(TEST_ANALYTICS_METADATA_TYPE);
		
		AnalyticsMetadataConverter.prepareAnalyticsMetadataProperties(obj);

		
		AnalyticsMetadataBuilder builder = new AnalyticsMetadataBuilder(obj, null, ctx);
		try {
			String guid = metadataHandler.createBeanInRepository(ctx.getUserId(), null, null,
					IdMap.SCHEMA_ATTRIBUTE_TYPE_GUID, IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME,
					builder, new Date(), methodName);
			
			EntityDetail entity = omEntityDao.getEntityByGuid(guid);	
			
			assertEquals(omEntityDao.getEntityQName(entity), CONTAINER_QNAME);
			assertEquals(omEntityDao.getEntityStringProperty(entity, Constants.DISPLAY_NAME), CONTAINER_DISPLAY_NAME);
			assertEquals(omEntityDao.getEntityStringProperty(entity, Constants.SYNC_DESCRIPTION), TEST_DESCRIPTION);
			assertEquals(omEntityDao.getEntityStringProperty(entity, Constants.SYNC_NATIVE_CLASS), obj.getClass().getName());

			InstanceProperties props = omEntityDao.getMapProperty(entity.getProperties(), Constants.ADDITIONAL_PROPERTIES);
			assertEquals(omEntityDao.getStringProperty(Constants.SYNC_IDENTIFIER, props), CONTAINER_ID);
			assertEquals(omEntityDao.getStringProperty(Constants.TYPE, props), TEST_ANALYTICS_METADATA_TYPE);
			assertEquals(omEntityDao.getStringProperty(IdMap.SOURCE_ID, props), "uid1" + Constants.SYNC_ID_LIST_DELIMITER +"uid2");
			
		} catch (Exception e) {
			fail("Test failed with exception: " + e.getLocalizedMessage());
		}
		
		// test converter to fetch bean from repository
		try {
			SchemaAttribute sa = metadataHandler.getBeanByQualifiedName(ctx.getUserId(),
																		IdMap.SCHEMA_ATTRIBUTE_TYPE_GUID, IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME,
																		CONTAINER_QNAME, "CONTAINER_QNAME",
																		false,
																		false,
																		new Date(), methodName);
			
			assertEquals(sa.getClass().getName(), obj.getClass().getName());	// fetched the same type as saved
			
			MetadataContainer objLoaded = (MetadataContainer) sa;
			assertEquals(objLoaded.getQualifiedName(), CONTAINER_QNAME);
			assertEquals(objLoaded.getDisplayName(), CONTAINER_DISPLAY_NAME);
			assertEquals(objLoaded.getDescription(), TEST_DESCRIPTION);
			assertEquals(objLoaded.getNativeJavaClass(), obj.getClass().getName());

			assertEquals(objLoaded.getIdentifier(), CONTAINER_ID);
			assertEquals(objLoaded.getType(), TEST_ANALYTICS_METADATA_TYPE);
			assertEquals(objLoaded.getSourceId(), SOURCE_LIST);

		} catch (Exception e) {
			fail("Test failed with exception: " + e.getLocalizedMessage());
		}
	}

	/**
	 * Test builder to save attributes in repository from bean
	 * and converter to load from repository into a bean for item
	 */
	@Test
	void testItemPersistence() throws IOException {
		
		String methodName = "testItemPersistance";
		
		// test builder to save bean into repository
		MetadataItem obj = TestUtilities.readObjectJson(
				JsonMocks.getItem(ITEM_QNAME, ITEM_DISPLAY_NAME, ITEM_ID, ITEM_EXPRESSION, ITEM_DATA_TYPE),
				MetadataItem.class);
		
		obj.setDescription(TEST_DESCRIPTION);
		obj.setType(TEST_ANALYTICS_METADATA_TYPE);
		
		AnalyticsMetadataConverter.prepareAnalyticsMetadataProperties(obj);

		AnalyticsMetadataBuilder builder = new AnalyticsMetadataBuilder(obj, null, ctx);
		try {
			String guid = metadataHandler.createBeanInRepository(ctx.getUserId(), null, null,
					IdMap.SCHEMA_ATTRIBUTE_TYPE_GUID, IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME,
					builder, new Date(), methodName);
			
			EntityDetail entity = omEntityDao.getEntityByGuid(guid);	
			
			assertEquals(omEntityDao.getEntityQName(entity), ITEM_QNAME);
			assertEquals(omEntityDao.getEntityStringProperty(entity, Constants.DISPLAY_NAME), ITEM_DISPLAY_NAME);
			assertEquals(omEntityDao.getEntityStringProperty(entity, Constants.SYNC_DESCRIPTION), TEST_DESCRIPTION);
			assertEquals(omEntityDao.getEntityStringProperty(entity, Constants.SYNC_NATIVE_CLASS), obj.getClass().getName());

			InstanceProperties props = omEntityDao.getMapProperty(entity.getProperties(), Constants.ADDITIONAL_PROPERTIES);
			assertEquals(omEntityDao.getStringProperty(Constants.SYNC_IDENTIFIER, props), ITEM_ID);
			assertEquals(omEntityDao.getStringProperty(Constants.TYPE, props), TEST_ANALYTICS_METADATA_TYPE);
			assertEquals(omEntityDao.getStringProperty(IdMap.SOURCE_ID, props), "uid1" + Constants.SYNC_ID_LIST_DELIMITER +"uid2");
			
		} catch (Exception e) {
			fail("Test failed with exception: " + e.getLocalizedMessage());
		}
		
		// test converter to fetch bean from repository
		try {
			SchemaAttribute sa = metadataHandler.getBeanByQualifiedName(ctx.getUserId(),
					IdMap.SCHEMA_ATTRIBUTE_TYPE_GUID, IdMap.SCHEMA_ATTRIBUTE_TYPE_NAME,
					ITEM_QNAME, "ITEM_QNAME",
					false, false, new Date(), methodName);
			
			assertEquals(sa.getClass().getName(), obj.getClass().getName());	// fetched the same type as saved
			
			MetadataItem objLoaded = (MetadataItem) sa;
			assertEquals(objLoaded.getQualifiedName(), ITEM_QNAME);
			assertEquals(objLoaded.getDisplayName(), ITEM_DISPLAY_NAME);
			assertEquals(objLoaded.getDescription(), TEST_DESCRIPTION);
			assertEquals(objLoaded.getNativeJavaClass(), obj.getClass().getName());

			assertEquals(objLoaded.getIdentifier(), ITEM_ID);
			assertEquals(objLoaded.getType(), TEST_ANALYTICS_METADATA_TYPE);
			assertEquals(objLoaded.getSourceId(), SOURCE_LIST);
			assertEquals(objLoaded.getDataType(), ITEM_DATA_TYPE);
			assertEquals(objLoaded.getExpression(), ITEM_EXPRESSION);

		} catch (Exception e) {
			fail("Test failed with exception: " + e.getLocalizedMessage());
		}	
	}
}
