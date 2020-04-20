/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accesservices.dataplatform;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.odpi.openmetadata.accessservices.dataplatform.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.dataplatform.events.DataPlatformEventType;
import org.odpi.openmetadata.accessservices.dataplatform.events.NewViewEvent;
import org.odpi.openmetadata.accessservices.dataplatform.listeners.DataPlatformInTopicListener;
import org.odpi.openmetadata.accessservices.dataplatform.server.DataPlatformServicesInstance;
import org.odpi.openmetadata.accessservices.dataplatform.utils.Constants;
import org.odpi.openmetadata.accessservices.dataplatform.utils.EntityPropertiesUtils;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OMRSMetadataCollection;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProvenanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.PrimitivePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDefCategory;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.ClassificationErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.EntityNotKnownException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.FunctionNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.PropertyErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.StatusNotSupportedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.TypeErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentHelper;
import org.odpi.openmetadata.repositoryservices.rest.properties.TypeLimitedFindRequest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;

import static junit.framework.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.anyList;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


public class DataPlatformOmasListenerTest {


    private static final String COLUMNTYPE_TYPE_GUID = "columnTypeGuid";
    private static final String DERIVED_COLUMN_TYPE_GUID = "derivedColumnTypeGuid";
    private static final String ENDPOINT_TYPE_GUID = "endpointTypeGuid";
    private static final String CONNECTION_TYPE_GUID = "connectionTypeGuid";
    private static final String CONNECTOR_TYPE_GUID = "connectorTypeGuid";
    private static final String DATABASE_TYPE_GUID = "databaseTypeGuid";
    private static final String DB_SCHEMA_TYPE_GUID = "dbSchemaTypeGuid";
    private static final String INFORMATION_VIEW_TYPE_GUID = "infoViewTypeGuid";
    private static final String TABLETYPE_TYPE_GUID = "tableTypeTypeGuid";
    private static final String TABLE_TYPE_GUID = "tableTypeGuid";
    private static final String BUSINESS_TERM_GUID = "businessTermGuid";
    private static final String SOFTWARE_SERVER_TYPE_GUID = "softwareServerTypeGuid";

    private static final String DERIVED_COLUMN_GUID = "derivedColumnGuid";
    private static final String SOFTWARE_SERVER_GUID = "softwareServerGuid";
    private static final String ENDPOINT_GUID = "endpointGuid";
    private static final String CONNECTION_GUID = "connectionGuid";
    private static final String CONNECTOR_GUID = "connectorGuid";
    private static final String DATABASE_GUID = "databaseGuid";
    private static final String INFORMATION_VIEW_GUID = "infoViewGuid";
    private static final String TABLETYPE_GUID = "tableTypeGuid";
    private static final String TABLE_GUID = "tableGuid";

    private static final String SOFTWARE_SERVER_QUALIFIED_NAME = "localhost";
    private static final String ENDPOINT_QUALIFIED_NAME = "jdbc:derby://localhost:9393";
    private static final String CONNECTION_QUALIFIED_NAME = "jdbc:derby://localhost:9393.Connection";
    private static final String CONNECTOR_TYPE_QUALIFIED_NAME = "jdbc:derby://localhost:9393.Connection.GaianConnectorProvider_type";
    private static final String DATABASE_QUALIFIED_NAME = "jdbc:derby://localhost:9393.databaseTest";
    private static final String INFORMATION_VIEW_QUALIFIED_NAME = "(SoftwareServer)=localhost::(Database)=databaseTest::(InformationView)=schema";
    private static final String DB_SCHEMA_TYPE_QUALIFIED_NAME = "(SoftwareServer)=localhost::(Database)=databaseTest::(RelationalDBSchemaType)=schema_type";
    private static final String TABLE_TYPE_QUALIFIED_NAME = "(SoftwareServer)=localhost::(Database)=databaseTest::(DataPlatform)=schema::(RelationalTableType)=customer_table_type";
    private static final String TABLE_QUALIFIED_NAME = "(SoftwareServer)=localhost::(Database)=databaseTest::(DataPlatform)=schema::(RelationalTable)=customer_table";
    private static final String DERIVED_COLUMN_QUALIFIED_NAME = "(SoftwareServer)=localhost::(Database)=databaseTest::(DataPlatform)=schema::(RelationalTable)=customer_table::(DerivedRelationalColumn)=client_name";
    private static final String DERIVED_COLUMN_TYPE_QUALIFIED_NAME = "(SoftwareServer)=localhost::(Database)=databaseTest::(DataPlatform)=schema::(RelationalTable)=customer_table::(RelationalColumnType)=client_name_type";
    private static final String IV_PREFIX = "iv_";

    private DataPlatformInTopicListener listener;
    private TestDataHelper testDataHelper;
    @Mock
    private OMRSRepositoryConnector enterpriseConnector;
    @Mock
    private OMRSMetadataCollection omrsMetadataCollection;
    @Mock
    private EntityDetail connection;
    @Mock
    private EntityDetail endpoint;
    @Mock
    private EntityDetail softwareServer;
    @Mock
    private EntityDetail connectorType;
    @Mock
    private EntityDetail informationView;
    @Mock
    private EntityDetail database;
    @Mock
    private EntityDetail dbSchemaType;
    @Mock
    private EntityDetail table;
    @Mock
    private EntityDetail tableType;
    @Mock
    private EntityDetail derivedColumn;
    @Mock
    private EntityDetail derivedColumnType;
    @Mock
    private EntityDetail businessTerm;
    @Mock
    private OMRSRepositoryContentHelper helper;

    @Mock
    private DataPlatformServicesInstance instance;

    private OMRSAuditLog auditLog;


    private ArgumentCaptor<InstanceProperties> softwareServerInstanceProperties;
    private ArgumentCaptor<InstanceProperties> endpointInstanceProperties;
    private ArgumentCaptor<InstanceProperties> connectionInstanceProperties;
    private ArgumentCaptor<InstanceProperties> connectorTypeInstanceProperties;
    private ArgumentCaptor<InstanceProperties> databaseInstanceProperties;
    private ArgumentCaptor<InstanceProperties> informationViewInstanceProperties;
    private ArgumentCaptor<InstanceProperties> dbSchemaTypeInstanceProperties;
    private ArgumentCaptor<InstanceProperties> tableTypeInstanceProperties;
    private ArgumentCaptor<InstanceProperties> tableInstanceProperties;
    private ArgumentCaptor<InstanceProperties> derivedColumnInstanceProperties;
    private ArgumentCaptor<InstanceProperties> derivedColumnTypeInstanceProperties;


    @BeforeMethod
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        OMEntityDao omEntityDao = new OMEntityDao(enterpriseConnector, Collections.EMPTY_LIST, auditLog);
        listener = new DataPlatformInTopicListener(instance,omEntityDao, auditLog,  enterpriseConnector.getRepositoryHelper());
        when(enterpriseConnector.getMetadataCollection()).thenReturn(omrsMetadataCollection);
        when(enterpriseConnector.getRepositoryHelper()).thenReturn(helper);
        when(helper.getStringProperty(eq(Constants.DATA_PLATFORM_OMAS_NAME),
                any(String.class),
                any(InstanceProperties.class),
                any(String.class))).thenCallRealMethod();
        when(helper.getBooleanProperty(eq(Constants.DATA_PLATFORM_OMAS_NAME),
                any(String.class),
                any(InstanceProperties.class),
                any(String.class))).thenCallRealMethod();
        when(helper.getIntProperty(eq(Constants.DATA_PLATFORM_OMAS_NAME),
                any(String.class),
                any(InstanceProperties.class),
                any(String.class))).thenCallRealMethod();
        when(helper.getStringArrayProperty(eq(Constants.DATA_PLATFORM_OMAS_NAME),
                any(String.class),
                any(InstanceProperties.class),
                any(String.class))).thenCallRealMethod();


        when(helper.addStringArrayPropertyToInstance(eq(Constants.DATA_PLATFORM_OMAS_NAME),
                any(InstanceProperties.class),
                any(String.class),
                anyList(),
                any(String.class))).thenCallRealMethod();

        testDataHelper = new TestDataHelper();
        buildInstanceTypes();
        buildRelationshipsTypes();
        buildEntities();
        buildClassifications();
        captureRepositoryCalls();


    }

    private void buildClassifications() throws TypeErrorException {
        addClassification(Constants.DATABASE_SERVER, TestDataHelper.DATABASE_SERVER_TYPE_GUID, Constants.SOFTWARE_SERVER);
    }

    private void addClassification(String classificationTypeName, String classificationTypeGuid, String entityTypeName) throws TypeErrorException {
        Classification skeletonClassification = mock(Classification.class);
        InstanceType mockType = mock(InstanceType.class);
        when(mockType.getTypeDefGUID()).thenReturn(classificationTypeGuid);
        when(skeletonClassification.getType()).thenReturn(mockType);
        when(skeletonClassification.getStatus()).thenReturn(InstanceStatus.ACTIVE);
        when(helper.getSkeletonClassification(Constants.DATA_PLATFORM_OMAS_NAME,
                Constants.USER_ID,
                classificationTypeName,
                entityTypeName))
                .thenReturn(skeletonClassification);
    }

    private void captureRepositoryCalls() throws InvalidParameterException, RepositoryErrorException, TypeErrorException, PropertyErrorException, ClassificationErrorException, StatusNotSupportedException, UserNotAuthorizedException, EntityNotKnownException, FunctionNotSupportedException {

        softwareServerInstanceProperties = ArgumentCaptor.forClass(InstanceProperties.class);
        endpointInstanceProperties = ArgumentCaptor.forClass(InstanceProperties.class);
        connectionInstanceProperties = ArgumentCaptor.forClass(InstanceProperties.class);
        connectorTypeInstanceProperties = ArgumentCaptor.forClass(InstanceProperties.class);
        databaseInstanceProperties = ArgumentCaptor.forClass(InstanceProperties.class);
        informationViewInstanceProperties = ArgumentCaptor.forClass(InstanceProperties.class);
        dbSchemaTypeInstanceProperties = ArgumentCaptor.forClass(InstanceProperties.class);
        tableTypeInstanceProperties = ArgumentCaptor.forClass(InstanceProperties.class);
        tableInstanceProperties = ArgumentCaptor.forClass(InstanceProperties.class);
        derivedColumnInstanceProperties = ArgumentCaptor.forClass(InstanceProperties.class);
        derivedColumnTypeInstanceProperties = ArgumentCaptor.forClass(InstanceProperties.class);


        mockAddEntityCall(SOFTWARE_SERVER_TYPE_GUID, Constants.SOFTWARE_SERVER, softwareServerInstanceProperties, this.softwareServer);
        mockAddEntityCall(ENDPOINT_TYPE_GUID, Constants.ENDPOINT, endpointInstanceProperties, this.endpoint);
        mockAddEntityCall(CONNECTION_TYPE_GUID, Constants.CONNECTION, connectionInstanceProperties, this.connection);
        mockAddEntityCall(CONNECTOR_TYPE_GUID, Constants.CONNECTOR_TYPE, connectorTypeInstanceProperties, this.connectorType);
        mockAddEntityCall(DATABASE_TYPE_GUID, Constants.DATABASE, databaseInstanceProperties, this.database);
        mockAddEntityCall(INFORMATION_VIEW_TYPE_GUID, Constants.INFORMATION_VIEW, informationViewInstanceProperties, this.informationView);
        mockAddEntityCall(DB_SCHEMA_TYPE_GUID, Constants.RELATIONAL_DB_SCHEMA_TYPE, dbSchemaTypeInstanceProperties, this.dbSchemaType);
        mockAddEntityCall(TABLETYPE_TYPE_GUID, Constants.RELATIONAL_TABLE_TYPE, tableTypeInstanceProperties, this.tableType);
        mockAddEntityCall(TABLE_TYPE_GUID, Constants.RELATIONAL_TABLE, tableInstanceProperties, this.table);
        mockAddEntityCall(DERIVED_COLUMN_TYPE_GUID, Constants.DERIVED_RELATIONAL_COLUMN, derivedColumnInstanceProperties, this.derivedColumn);
        mockAddEntityCall(COLUMNTYPE_TYPE_GUID, Constants.RELATIONAL_COLUMN_TYPE, derivedColumnTypeInstanceProperties, this.derivedColumnType);

        mockAddRelationshipCall(Constants.SERVER_ENDPOINT, TestDataHelper.SERVER_ENDPOINT_REL_TYPE_GUID);
        mockAddRelationshipCall(Constants.CONNECTION_TO_ENDPOINT, TestDataHelper.CONNECTION_ENDPOINT_REL_TYPE_GUID);
        mockAddRelationshipCall(Constants.CONNECTION_CONNECTOR_TYPE, TestDataHelper.CONNECTION_CONNECTOR_REL_TYPE_GUID);
        mockAddRelationshipCall(Constants.CONNECTION_TO_ASSET, TestDataHelper.CONNECTION_ASSET_REL_TYPE_GUID);
        mockAddRelationshipCall(Constants.DATA_CONTENT_FOR_DATASET, TestDataHelper.DATA_CONTENT_DATASET_REL_TYPE_GUID);
        mockAddRelationshipCall(Constants.ASSET_SCHEMA_TYPE, TestDataHelper.ASSET_SCHEMA_REL_TYPE_GUID);
        mockAddRelationshipCall(Constants.SCHEMA_ATTRIBUTE_TYPE, TestDataHelper.SCHEMA_ATTRIBUTE_TYPE_REL_TYPE_GUID);
        mockAddRelationshipCall(Constants.ATTRIBUTE_FOR_SCHEMA, TestDataHelper.ATTRIBUTE_FOR_SCHEMA_REL_TYPE_GUID);
        mockAddRelationshipCall(Constants.SCHEMA_QUERY_IMPLEMENTATION, TestDataHelper.SCHEMA_QUERY_IMPLEMENTATION_REL_TYPE_GUID);
        mockAddRelationshipCall(Constants.SCHEMA_ATTRIBUTE_TYPE, TestDataHelper.SCHEMA_ATTRIBUTE_TYPE_REL_TYPE_GUID);
        mockAddRelationshipCall(Constants.SEMANTIC_ASSIGNMENT, TestDataHelper.SEMANTIC_ASSIGNMENT_REL_TYPE_GUID);
        mockAddRelationshipCall(Constants.ATTRIBUTE_FOR_SCHEMA, TestDataHelper.ATTRIBUTE_FOR_SCHEMA_REL_TYPE_GUID);

    }

    private void mockAddRelationshipCall(String typeName, String typeGuid) throws TypeErrorException {
        Relationship skeletonRelationship = mock(Relationship.class);
        InstanceType mockType = mock(InstanceType.class);
        when(mockType.getTypeDefGUID()).thenReturn(typeGuid);
        when(skeletonRelationship.getType()).thenReturn(mockType);
        when(skeletonRelationship.getStatus()).thenReturn(InstanceStatus.ACTIVE);
        when(helper.getSkeletonRelationship(Constants.DATA_PLATFORM_OMAS_NAME,
                "",
                InstanceProvenanceType.LOCAL_COHORT,
                Constants.USER_ID,
                typeName)).thenReturn(skeletonRelationship);
    }

    private void mockAddEntityCall(String typeGuid, String typeName, ArgumentCaptor<InstanceProperties> propertiesArgumentCaptor, EntityDetail entityDetail) throws TypeErrorException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, ClassificationErrorException, StatusNotSupportedException, UserNotAuthorizedException, FunctionNotSupportedException {
        EntityDetail skeletonEntity = mock(EntityDetail.class);
        InstanceType mockType = mock(InstanceType.class);
        when(mockType.getTypeDefGUID()).thenReturn(typeGuid);
        when(skeletonEntity.getType()).thenReturn(mockType);
        when(skeletonEntity.getStatus()).thenReturn(InstanceStatus.ACTIVE);
        when(skeletonEntity.getClassifications()).thenReturn(new ArrayList<>());
        when(helper.getSkeletonEntity(Constants.DATA_PLATFORM_OMAS_NAME,
                "",
                InstanceProvenanceType.LOCAL_COHORT,
                Constants.USER_ID,
                typeName)).thenReturn(skeletonEntity);
        when(omrsMetadataCollection.addEntity(any(String.class), eq(typeGuid), propertiesArgumentCaptor.capture(), any(ArrayList.class), any(InstanceStatus.class))).thenReturn(entityDetail);
    }

    private void buildEntities() throws Exception {

        when(softwareServer.getGUID()).thenReturn(SOFTWARE_SERVER_GUID);
        when(endpoint.getGUID()).thenReturn(ENDPOINT_GUID);
        when(connection.getGUID()).thenReturn(CONNECTION_GUID);
        when(connectorType.getGUID()).thenReturn(CONNECTOR_GUID);
        when(database.getGUID()).thenReturn(DATABASE_GUID);
        when(informationView.getGUID()).thenReturn(INFORMATION_VIEW_GUID);
        when(dbSchemaType.getGUID()).thenReturn(DB_SCHEMA_TYPE_GUID);
        when(tableType.getGUID()).thenReturn(TABLE_TYPE_GUID);
        when(table.getGUID()).thenReturn(TABLE_GUID);
        when(derivedColumn.getGUID()).thenReturn(DERIVED_COLUMN_GUID);
        when(derivedColumnType.getGUID()).thenReturn(DERIVED_COLUMN_TYPE_GUID);

        InstanceProperties softwareServerInstanceProperties = buildInstanceProperties(SOFTWARE_SERVER_QUALIFIED_NAME);
        when(softwareServer.getProperties()).thenReturn(softwareServerInstanceProperties);
        InstanceProperties endpointInstanceProperties = buildInstanceProperties(ENDPOINT_QUALIFIED_NAME);
        when(endpoint.getProperties()).thenReturn(endpointInstanceProperties);
        InstanceProperties connectionInstanceProperties = buildInstanceProperties(CONNECTION_QUALIFIED_NAME);
        when(connection.getProperties()).thenReturn(connectionInstanceProperties);
        InstanceProperties connectorTypeInstanceProperties = buildInstanceProperties(CONNECTOR_TYPE_QUALIFIED_NAME);
        when(connectorType.getProperties()).thenReturn(connectorTypeInstanceProperties);
        InstanceProperties databaseInstanceProperties = buildInstanceProperties(DATABASE_QUALIFIED_NAME);
        when(database.getProperties()).thenReturn(databaseInstanceProperties);
        InstanceProperties dbSchemaTypeInstanceProperties = buildInstanceProperties(DB_SCHEMA_TYPE_QUALIFIED_NAME);
        when(dbSchemaType.getProperties()).thenReturn(dbSchemaTypeInstanceProperties);
        InstanceProperties informationViewInstanceProperties = buildInstanceProperties(INFORMATION_VIEW_QUALIFIED_NAME);
        when(informationView.getProperties()).thenReturn(informationViewInstanceProperties);
        InstanceProperties tableInstanceProperties = buildInstanceProperties(TABLE_QUALIFIED_NAME);
        when(table.getProperties()).thenReturn(tableInstanceProperties);
        InstanceProperties tableTypeInstanceProperties = buildInstanceProperties(TABLE_TYPE_QUALIFIED_NAME);
        when(tableType.getProperties()).thenReturn(tableTypeInstanceProperties);
        InstanceProperties derivedColumnInstanceProperties = buildInstanceProperties(DERIVED_COLUMN_QUALIFIED_NAME);
        when(derivedColumn.getProperties()).thenReturn(derivedColumnInstanceProperties);
        InstanceProperties derivedColumnTypeInstanceProperties = buildInstanceProperties(DERIVED_COLUMN_TYPE_QUALIFIED_NAME);
        when(derivedColumnType.getProperties()).thenReturn(derivedColumnTypeInstanceProperties);


        when(omrsMetadataCollection.findEntitiesByProperty(Constants.USER_ID, DATABASE_TYPE_GUID,
                buildInstanceProperties(DATABASE_QUALIFIED_NAME),
                MatchCriteria.ALL,
                0,
                Collections.singletonList(InstanceStatus.ACTIVE),
                null,
                null,
                null,
                SequencingOrder.ANY,
                0)).thenReturn(Collections.singletonList(database));


    }

    private InstanceProperties buildInstanceProperties(String qualifiedName) {
        InstanceProperties instanceProperties = new InstanceProperties();
        PrimitivePropertyValue nameValue = new PrimitivePropertyValue();
        nameValue.setPrimitiveDefCategory(PrimitiveDefCategory.OM_PRIMITIVE_TYPE_STRING);
        nameValue.setPrimitiveValue(qualifiedName);
        instanceProperties.setProperty(Constants.QUALIFIED_NAME, nameValue);
        return instanceProperties;
    }

    private void buildInstanceTypes() throws Exception {

        TypeDef typeDef = testDataHelper.buildInstanceType(Constants.SOFTWARE_SERVER, SOFTWARE_SERVER_TYPE_GUID);
        when(helper.getTypeDefByName(Constants.USER_ID, Constants.SOFTWARE_SERVER)).thenReturn(typeDef);
        when(helper.getTypeDefByName("", typeDef.getName())).thenReturn(typeDef);

        typeDef = testDataHelper.buildInstanceType(Constants.ENDPOINT, ENDPOINT_TYPE_GUID);
        when(helper.getTypeDefByName(Constants.USER_ID, Constants.ENDPOINT)).thenReturn(typeDef);
        when(helper.getTypeDefByName("", typeDef.getName())).thenReturn(typeDef);

        typeDef = testDataHelper.buildInstanceType(Constants.CONNECTION, CONNECTION_TYPE_GUID);
        when(helper.getTypeDefByName(Constants.USER_ID, Constants.CONNECTION)).thenReturn(typeDef);
        when(helper.getTypeDefByName("", typeDef.getName())).thenReturn(typeDef);

        typeDef = testDataHelper.buildInstanceType(Constants.CONNECTOR_TYPE, CONNECTOR_TYPE_GUID);
        when(helper.getTypeDefByName(Constants.USER_ID, typeDef.getName())).thenReturn(typeDef);
        when(helper.getTypeDefByName("", typeDef.getName())).thenReturn(typeDef);

        typeDef = testDataHelper.buildInstanceType(Constants.DATABASE, DATABASE_TYPE_GUID);
        when(helper.getTypeDefByName(Constants.USER_ID, typeDef.getName())).thenReturn(typeDef);
        when(helper.getTypeDefByName("", typeDef.getName())).thenReturn(typeDef);

        typeDef = testDataHelper.buildInstanceType(Constants.INFORMATION_VIEW, INFORMATION_VIEW_TYPE_GUID);
        when(helper.getTypeDefByName(Constants.USER_ID, typeDef.getName())).thenReturn(typeDef);
        when(helper.getTypeDefByName("", typeDef.getName())).thenReturn(typeDef);

        typeDef = testDataHelper.buildInstanceType(Constants.RELATIONAL_DB_SCHEMA_TYPE, DB_SCHEMA_TYPE_GUID);
        when(helper.getTypeDefByName(Constants.USER_ID, typeDef.getName())).thenReturn(typeDef);
        when(helper.getTypeDefByName("", typeDef.getName())).thenReturn(typeDef);

        typeDef = testDataHelper.buildInstanceType(Constants.RELATIONAL_TABLE_TYPE, TABLETYPE_TYPE_GUID);
        when(helper.getTypeDefByName(Constants.USER_ID, typeDef.getName())).thenReturn(typeDef);
        when(helper.getTypeDefByName("", typeDef.getName())).thenReturn(typeDef);

        typeDef = testDataHelper.buildInstanceType(Constants.RELATIONAL_TABLE, TABLE_TYPE_GUID);
        when(helper.getTypeDefByName(Constants.USER_ID, typeDef.getName())).thenReturn(typeDef);
        when(helper.getTypeDefByName("", typeDef.getName())).thenReturn(typeDef);

        typeDef = testDataHelper.buildInstanceType(Constants.DERIVED_RELATIONAL_COLUMN, DERIVED_COLUMN_TYPE_GUID);
        when(helper.getTypeDefByName(Constants.USER_ID, typeDef.getName())).thenReturn(typeDef);
        when(helper.getTypeDefByName("", typeDef.getName())).thenReturn(typeDef);

        typeDef = testDataHelper.buildInstanceType(Constants.RELATIONAL_COLUMN_TYPE, COLUMNTYPE_TYPE_GUID);
        when(helper.getTypeDefByName(Constants.USER_ID, typeDef.getName())).thenReturn(typeDef);
        when(helper.getTypeDefByName("", typeDef.getName())).thenReturn(typeDef);

    }

    private void buildRelationshipsTypes() throws Exception {

        TypeDef typeDef = testDataHelper.buildRelationshipType(Constants.SERVER_ENDPOINT, TestDataHelper.SERVER_ENDPOINT_REL_TYPE_GUID);
        when(helper.getTypeDefByName(Constants.USER_ID, typeDef.getName())).thenReturn(typeDef);
        typeDef = testDataHelper.buildRelationshipType(Constants.CONNECTION_TO_ENDPOINT, TestDataHelper.CONNECTION_ENDPOINT_REL_TYPE_GUID);
        when(helper.getTypeDefByName(Constants.USER_ID, typeDef.getName())).thenReturn(typeDef);
        typeDef = testDataHelper.buildRelationshipType(Constants.CONNECTION_CONNECTOR_TYPE, TestDataHelper.CONNECTION_CONNECTOR_REL_TYPE_GUID);
        when(helper.getTypeDefByName(Constants.USER_ID, typeDef.getName())).thenReturn(typeDef);
        typeDef = testDataHelper.buildRelationshipType(Constants.CONNECTION_TO_ASSET, TestDataHelper.CONNECTION_ASSET_REL_TYPE_GUID);
        when(helper.getTypeDefByName(Constants.USER_ID, typeDef.getName())).thenReturn(typeDef);
        typeDef = testDataHelper.buildRelationshipType(Constants.DATA_CONTENT_FOR_DATASET, TestDataHelper.DATA_CONTENT_DATASET_REL_TYPE_GUID);
        when(helper.getTypeDefByName(Constants.USER_ID, typeDef.getName())).thenReturn(typeDef);
        typeDef = testDataHelper.buildRelationshipType(Constants.ASSET_SCHEMA_TYPE, TestDataHelper.ASSET_SCHEMA_REL_TYPE_GUID);
        when(helper.getTypeDefByName(Constants.USER_ID, typeDef.getName())).thenReturn(typeDef);
        typeDef = testDataHelper.buildRelationshipType(Constants.SCHEMA_ATTRIBUTE_TYPE, TestDataHelper.SCHEMA_ATTRIBUTE_TYPE_REL_TYPE_GUID);
        when(helper.getTypeDefByName(Constants.USER_ID, typeDef.getName())).thenReturn(typeDef);
        typeDef = testDataHelper.buildRelationshipType(Constants.ATTRIBUTE_FOR_SCHEMA, TestDataHelper.ATTRIBUTE_FOR_SCHEMA_REL_TYPE_GUID);
        when(helper.getTypeDefByName(Constants.USER_ID, typeDef.getName())).thenReturn(typeDef);
        typeDef = testDataHelper.buildRelationshipType(Constants.SCHEMA_QUERY_IMPLEMENTATION, TestDataHelper.SCHEMA_QUERY_IMPLEMENTATION_REL_TYPE_GUID);
        when(helper.getTypeDefByName(Constants.USER_ID, typeDef.getName())).thenReturn(typeDef);
        typeDef = testDataHelper.buildRelationshipType(Constants.SEMANTIC_ASSIGNMENT, TestDataHelper.SEMANTIC_ASSIGNMENT_REL_TYPE_GUID);
        when(helper.getTypeDefByName(Constants.USER_ID, typeDef.getName())).thenReturn(typeDef);
    }


    @Test
    public void testListener() throws StatusNotSupportedException, UserNotAuthorizedException, EntityNotKnownException, InvalidParameterException, RepositoryErrorException, PropertyErrorException, TypeErrorException, JsonProcessingException, ClassificationErrorException, FunctionNotSupportedException {

        TypeLimitedFindRequest type = (new TypeLimitedFindRequest());
        type.setTypeGUID("dbc20663-d705-4ff0-8424-80c262c6b8e7");

        NewViewEvent newViewEvent = testDataHelper.buildEvent();
        newViewEvent.setEventType(DataPlatformEventType.NEW_INFORMATION_VIEW_EVENT);
        listener.processEvent(new ObjectMapper().writeValueAsString(newViewEvent));

        verify(omrsMetadataCollection, Mockito.times(1)).addEntity(eq(Constants.USER_ID), eq(INFORMATION_VIEW_TYPE_GUID), informationViewInstanceProperties.capture(), any(ArrayList.class), eq(InstanceStatus.ACTIVE));

        assertEquals(EntityPropertiesUtils.getStringValueForProperty(informationViewInstanceProperties.getValue(), Constants.QUALIFIED_NAME), INFORMATION_VIEW_QUALIFIED_NAME);
        assertEquals(EntityPropertiesUtils.getStringValueForProperty(dbSchemaTypeInstanceProperties.getValue(), Constants.QUALIFIED_NAME), DB_SCHEMA_TYPE_QUALIFIED_NAME);
        assertEquals(EntityPropertiesUtils.getStringValueForProperty(tableTypeInstanceProperties.getValue(), Constants.QUALIFIED_NAME), TABLE_TYPE_QUALIFIED_NAME);
        assertEquals(EntityPropertiesUtils.getStringValueForProperty(tableInstanceProperties.getValue(), Constants.QUALIFIED_NAME), TABLE_QUALIFIED_NAME);
        assertEquals(EntityPropertiesUtils.getStringValueForProperty(derivedColumnInstanceProperties.getValue(), Constants.QUALIFIED_NAME), DERIVED_COLUMN_QUALIFIED_NAME);
        assertEquals(EntityPropertiesUtils.getStringValueForProperty(derivedColumnTypeInstanceProperties.getValue(), Constants.QUALIFIED_NAME), DERIVED_COLUMN_TYPE_QUALIFIED_NAME);

        verify(omrsMetadataCollection, Mockito.times(1)).addRelationship(eq(Constants.USER_ID), eq(TestDataHelper.DATA_CONTENT_DATASET_REL_TYPE_GUID), any(InstanceProperties.class), eq(DATABASE_GUID), eq(INFORMATION_VIEW_GUID), eq(InstanceStatus.ACTIVE));
        verify(omrsMetadataCollection, Mockito.times(1)).addRelationship(eq(Constants.USER_ID), eq(TestDataHelper.ASSET_SCHEMA_REL_TYPE_GUID), any(InstanceProperties.class), eq(INFORMATION_VIEW_GUID), eq(DB_SCHEMA_TYPE_GUID), eq(InstanceStatus.ACTIVE));
        verify(omrsMetadataCollection, Mockito.times(1)).addRelationship(eq(Constants.USER_ID), eq(TestDataHelper.SCHEMA_ATTRIBUTE_TYPE_REL_TYPE_GUID), any(InstanceProperties.class), eq(TABLE_GUID), eq(TABLE_TYPE_GUID), eq(InstanceStatus.ACTIVE));
        verify(omrsMetadataCollection, Mockito.times(1)).addRelationship(eq(Constants.USER_ID), eq(TestDataHelper.SCHEMA_ATTRIBUTE_TYPE_REL_TYPE_GUID), any(InstanceProperties.class), eq(DERIVED_COLUMN_GUID), eq(DERIVED_COLUMN_TYPE_GUID), eq(InstanceStatus.ACTIVE));
        verify(omrsMetadataCollection, Mockito.times(1)).addRelationship(eq(Constants.USER_ID), eq(TestDataHelper.ATTRIBUTE_FOR_SCHEMA_REL_TYPE_GUID), any(InstanceProperties.class), eq(DB_SCHEMA_TYPE_GUID), eq(TABLE_GUID), eq(InstanceStatus.ACTIVE));
        verify(omrsMetadataCollection, Mockito.times(1)).addRelationship(eq(Constants.USER_ID), eq(TestDataHelper.SCHEMA_QUERY_IMPLEMENTATION_REL_TYPE_GUID), any(InstanceProperties.class), eq(DERIVED_COLUMN_GUID), eq(TestDataHelper.REAL_COLUMN_GUID), eq(InstanceStatus.ACTIVE));
        verify(omrsMetadataCollection, Mockito.times(1)).addRelationship(eq(Constants.USER_ID), eq(TestDataHelper.SEMANTIC_ASSIGNMENT_REL_TYPE_GUID), any(InstanceProperties.class), eq(DERIVED_COLUMN_GUID), eq(TestDataHelper.BUSINESS_TERM_GUID), eq(InstanceStatus.ACTIVE));
        verify(omrsMetadataCollection, Mockito.times(1)).addRelationship(eq(Constants.USER_ID), eq(TestDataHelper.ATTRIBUTE_FOR_SCHEMA_REL_TYPE_GUID), any(InstanceProperties.class), eq(TABLE_TYPE_GUID), eq(DERIVED_COLUMN_GUID), eq(InstanceStatus.ACTIVE));

    }
}
