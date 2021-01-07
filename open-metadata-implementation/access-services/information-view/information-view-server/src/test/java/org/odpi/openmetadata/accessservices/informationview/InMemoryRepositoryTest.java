/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.informationview;

import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.odpi.openmetadata.accessservices.informationview.contentmanager.OMEntityDao;
import org.odpi.openmetadata.accessservices.informationview.utils.Constants;
import org.odpi.openmetadata.accessservices.informationview.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.adapters.repositoryservices.inmemory.repositoryconnector.InMemoryOMRSRepositoryConnectorProvider;
import org.odpi.openmetadata.adminservices.configuration.properties.OpenMetadataExchangeRule;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectionCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.archivemanager.OMRSArchiveManager;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.eventmanagement.OMRSRepositoryEventExchangeRule;
import org.odpi.openmetadata.repositoryservices.eventmanagement.OMRSRepositoryEventManager;
import org.odpi.openmetadata.repositoryservices.localrepository.repositoryconnector.LocalOMRSConnectorProvider;
import org.odpi.openmetadata.repositoryservices.localrepository.repositoryconnector.LocalOMRSRepositoryConnector;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentHelper;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentManager;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentValidator;


import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

public class InMemoryRepositoryTest {

    @Mock
    protected OMRSRepositoryConnector enterpriseConnector;
    @Mock
    protected OMRSRepositoryContentHelper omrsRepositoryHelper;
    @Mock
    protected OMRSAuditLog auditLog;
    private OMRSRepositoryContentManager localRepositoryContentManager = null;
    protected OMEntityDao omEntityDao;


    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        omEntityDao = new OMEntityDao(enterpriseConnector, Collections.emptyList(), auditLog);

        OMRSRepositoryConnector repositoryConnector = initializeInMemoryRepositoryConnector();
        when(enterpriseConnector.getMetadataCollection()).thenReturn(repositoryConnector.getMetadataCollection());
        when(enterpriseConnector.getRepositoryHelper()).thenReturn(repositoryConnector.getRepositoryHelper());
        populateRepository();
        when(omrsRepositoryHelper.getStringProperty(eq(Constants.INFORMATION_VIEW_OMAS_NAME),
                any(String.class),
                any(InstanceProperties.class),
                any(String.class))).thenCallRealMethod();
        when(omrsRepositoryHelper.getBooleanProperty(eq(Constants.INFORMATION_VIEW_OMAS_NAME),
                any(String.class),
                any(InstanceProperties.class),
                any(String.class))).thenCallRealMethod();
        when(omrsRepositoryHelper.getIntProperty(eq(Constants.INFORMATION_VIEW_OMAS_NAME),
                any(String.class),
                any(InstanceProperties.class),
                any(String.class))).thenCallRealMethod();
        when(omrsRepositoryHelper.getStringArrayProperty(eq(Constants.INFORMATION_VIEW_OMAS_NAME),
                any(String.class),
                any(InstanceProperties.class),
                any(String.class))).thenCallRealMethod();


        when(omrsRepositoryHelper.addStringArrayPropertyToInstance(eq(Constants.INFORMATION_VIEW_OMAS_NAME),
                any(InstanceProperties.class),
                any(String.class),
                anyList(),
                any(String.class))).thenCallRealMethod();
        when(omrsRepositoryHelper.addStringPropertyToInstance(eq(Constants.INFORMATION_VIEW_OMAS_NAME),
                any(InstanceProperties.class),
                any(String.class),
                any(String.class),
                any(String.class))).thenCallRealMethod();
        when(omrsRepositoryHelper.getExactMatchRegex(any(String.class))).thenCallRealMethod();

    }


    private OMRSRepositoryConnector initializeInMemoryRepositoryConnector() throws ConnectorCheckedException,
                                                                                   ConnectionCheckedException {

        Connection connection = new Connection();
        ConnectorType connectorType = new ConnectorType();
        connection.setConnectorType(connectorType);
        connectorType.setConnectorProviderClassName(InMemoryOMRSRepositoryConnectorProvider.class.getName());
        ConnectorBroker connectorBroker = new ConnectorBroker();
        Connector connector = connectorBroker.getConnector(connection);
        OMRSRepositoryConnector repositoryConnector = (OMRSRepositoryConnector) connector;

        localRepositoryContentManager = new OMRSRepositoryContentManager("userID", auditLog);


        OMRSRepositoryEventManager localRepositoryEventManager = new OMRSRepositoryEventManager("local repository outbound",
                new OMRSRepositoryEventExchangeRule(OpenMetadataExchangeRule.ALL,
                        null),
                new OMRSRepositoryContentValidator(localRepositoryContentManager),
                auditLog);

        LocalOMRSRepositoryConnector localOMRSRepositoryConnector = (LocalOMRSRepositoryConnector) new LocalOMRSConnectorProvider("testLocalMetadataCollectionId",
                connection,
                null,
                localRepositoryEventManager,
                localRepositoryContentManager,
                new OMRSRepositoryEventExchangeRule(OpenMetadataExchangeRule.ALL,
                        null))
                .getConnector(connection);


        localOMRSRepositoryConnector.setRepositoryHelper(new OMRSRepositoryContentHelper(localRepositoryContentManager));
        localOMRSRepositoryConnector.setRepositoryValidator(new OMRSRepositoryContentValidator(localRepositoryContentManager));
        localOMRSRepositoryConnector.setAuditLog(auditLog);
        localOMRSRepositoryConnector.setMetadataCollectionId("1234");
        localRepositoryContentManager.setupEventProcessor(localOMRSRepositoryConnector, localRepositoryEventManager);


        repositoryConnector.setRepositoryHelper(new OMRSRepositoryContentHelper(localRepositoryContentManager));
        repositoryConnector.setRepositoryValidator(new OMRSRepositoryContentValidator(localRepositoryContentManager));
        repositoryConnector.setMetadataCollectionId("testMetadataCollectionId");
        repositoryConnector.start();
        localRepositoryEventManager.start();
        localOMRSRepositoryConnector.start();
        new OMRSArchiveManager(null, auditLog).setLocalRepository(localRepositoryContentManager, localRepositoryEventManager);

        return localOMRSRepositoryConnector;
    }

    private void populateRepository() throws Exception {

        String qualifiedNameForSoftwareServerCapability = "registration-qualified-name";
        InstanceProperties softwareServerProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForSoftwareServerCapability)
                .withStringProperty(Constants.NAME, "test-1234")
                .withStringProperty(Constants.PATCH_LEVEL, "1")
                .withStringProperty(Constants.VERSION, "1.1.1")
                .build();
         omEntityDao.addEntity(Constants.SOFTWARE_SERVER_CAPABILITY,
                qualifiedNameForSoftwareServerCapability,
                softwareServerProperties,
                false);

        String qualifiedNameForEndpoint = "host";
        InstanceProperties endpointProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForEndpoint)
                .withStringProperty(Constants.NAME, qualifiedNameForEndpoint)
                .withStringProperty(Constants.NETWORK_ADDRESS, "host")
                .withStringProperty(Constants.PROTOCOL, "")
                .build();
        EntityDetail endpointEntity = omEntityDao.addEntity(Constants.ENDPOINT,
                qualifiedNameForEndpoint,
                endpointProperties,
                false);

        String qualifiedNameForConnection = qualifiedNameForEndpoint + "::" ;
        InstanceProperties connectionProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForConnection)
                .withStringProperty(Constants.DESCRIPTION, "Connection to " + qualifiedNameForConnection)
                .build();
        EntityDetail connectionEntity = omEntityDao.addEntity(Constants.CONNECTION,
                qualifiedNameForConnection, connectionProperties, false);

        omEntityDao.addRelationship(Constants.CONNECTION_TO_ENDPOINT,
                endpointEntity.getGUID(),
                connectionEntity.getGUID(),
                new InstanceProperties());

        String qualifiedNameForConnectorType = qualifiedNameForConnection + "::" ;
        InstanceProperties connectorTypeProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForConnectorType)
                .withStringProperty(Constants.DESCRIPTION, "Connection to " + qualifiedNameForConnection)
                .build();
        EntityDetail connectorType = omEntityDao.addEntity(Constants.CONNECTOR_TYPE,
                qualifiedNameForConnectorType, connectorTypeProperties, false);

        omEntityDao.addRelationship(Constants.CONNECTION_CONNECTOR_TYPE,
                connectionEntity.getGUID(),
                connectorType.getGUID(),
                new InstanceProperties());


        String qualifiedNameForDataStore = qualifiedNameForConnection + "::" + "XE";
        InstanceProperties dataStoreProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForDataStore)
                .withStringProperty(Constants.NAME, "XE")
                .build();
        EntityDetail dataStore = omEntityDao.addEntity(Constants.DATABASE,
                qualifiedNameForDataStore, dataStoreProperties, true);


        omEntityDao.addRelationship(Constants.CONNECTION_TO_ASSET,
                connectionEntity.getGUID(),
                dataStore.getGUID(),
                new InstanceProperties());


        String qualifiedNameForInformationView = qualifiedNameForDataStore + "::" + "HR";
        InstanceProperties ivProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForInformationView)
                .withStringProperty(Constants.NAME, "HR")
                .withStringProperty(Constants.OWNER, "")
                .withStringProperty(Constants.DESCRIPTION, "This asset is an " + "information " + "view")
                .build();
        EntityDetail informationViewEntity = omEntityDao.addEntity(Constants.INFORMATION_VIEW,
                qualifiedNameForInformationView, ivProperties, true);

        omEntityDao.addRelationship(Constants.DATA_CONTENT_FOR_DATASET,
                dataStore.getGUID(),
                informationViewEntity.getGUID(),
                new InstanceProperties());


        String qualifiedNameForDbSchemaType = qualifiedNameForInformationView + Constants.TYPE_SUFFIX;
        InstanceProperties dbSchemaTypeProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForDbSchemaType)
                .withStringProperty(Constants.DISPLAY_NAME, "HR" + Constants.TYPE_SUFFIX)
                .withStringProperty(Constants.AUTHOR, "")
                .withStringProperty(Constants.USAGE, "")
                .withStringProperty(Constants.ENCODING_STANDARD, "").build();
        EntityDetail relationalDbSchemaType = omEntityDao.addEntity(Constants.RELATIONAL_DB_SCHEMA_TYPE,
                qualifiedNameForDbSchemaType,
                dbSchemaTypeProperties,
                false);

        omEntityDao.addRelationship(Constants.ASSET_SCHEMA_TYPE,
                informationViewEntity.getGUID(),
                relationalDbSchemaType.getGUID(),
                new InstanceProperties());


        String qualifiedNameForTableType = qualifiedNameForInformationView + "::" + "EMPLOYEE" + Constants.TYPE_SUFFIX;
        InstanceProperties tableTypeProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForTableType)
                .withStringProperty(Constants.DISPLAY_NAME, "EMPLOYEE" + Constants.TYPE_SUFFIX)
                .withStringProperty(Constants.AUTHOR, "")
                .withStringProperty(Constants.USAGE, "")
                .withStringProperty(Constants.ENCODING_STANDARD, "")
                .build();
        EntityDetail tableTypeEntity = omEntityDao.addEntity(Constants.RELATIONAL_TABLE_TYPE,
                qualifiedNameForTableType,
                tableTypeProperties,
                false);

        String qualifiedNameForTable = qualifiedNameForInformationView + "::" + "EMPLOYEE";
        InstanceProperties tableProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForTable)
                .withStringProperty(Constants.ATTRIBUTE_NAME, "EMPLOYEE")
                .build();
        EntityDetail tableEntity = omEntityDao.addEntity(Constants.RELATIONAL_TABLE,
                qualifiedNameForTable,
                tableProperties,
                false);

        omEntityDao.addRelationship(Constants.SCHEMA_ATTRIBUTE_TYPE,
                tableEntity.getGUID(),
                tableTypeEntity.getGUID(),
                new InstanceProperties());
        omEntityDao.addRelationship(Constants.ATTRIBUTE_FOR_SCHEMA,
                relationalDbSchemaType.getGUID(),
                tableEntity.getGUID(),
                new InstanceProperties());


        addColumn(tableTypeEntity, qualifiedNameForTable, "FNAME");
        addColumn(tableTypeEntity, qualifiedNameForTable, "LNAME");
        addColumn(tableTypeEntity, qualifiedNameForTable, "ROLE");


    }

    private EntityDetail addColumn(EntityDetail tableTypeEntity, String qualifiedNameForTable, String columnName) throws Exception {
        String qualifiedNameColumnType = qualifiedNameForTable + "::" + columnName + Constants.TYPE_SUFFIX;
        InstanceProperties columnTypeProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameColumnType)
                .withStringProperty(Constants.DISPLAY_NAME, columnName + Constants.TYPE_SUFFIX)
                .withStringProperty(Constants.AUTHOR, "")
                .withStringProperty(Constants.USAGE, "")
                .withStringProperty(Constants.ENCODING_STANDARD, "")
                .withStringProperty(Constants.DATA_TYPE, "VARCHAR2")
                .build();
        EntityDetail columnTypeEntity = omEntityDao.addEntity(Constants.RELATIONAL_COLUMN_TYPE,
                qualifiedNameColumnType,
                columnTypeProperties,
                false);

        String qualifiedNameForColumn = qualifiedNameForTable + "::" + columnName;
        InstanceProperties columnProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForColumn)
                .withStringProperty(Constants.ATTRIBUTE_NAME, columnName)
                .withStringProperty(Constants.FORMULA, "")
                .withIntegerProperty(Constants.ELEMENT_POSITION_NAME, 0)
                .build();
        EntityDetail derivedColumnEntity = omEntityDao.addEntity(Constants.DERIVED_RELATIONAL_COLUMN,
                qualifiedNameForColumn,
                columnProperties,
                false);

        omEntityDao.addRelationship(Constants.SCHEMA_ATTRIBUTE_TYPE,
                derivedColumnEntity.getGUID(),
                columnTypeEntity.getGUID(),
                new InstanceProperties());
//        omEntityDao.addRelationship(Constants.SEMANTIC_ASSIGNMENT,
//                derivedColumnEntity.getGUID(),
//                derivedColumn.getSourceColumn().getBusinessTerms().getGuid(),
//                Constants.INFORMATION_VIEW_OMAS_NAME,
//                new ElementProperties());
        omEntityDao.addRelationship(Constants.ATTRIBUTE_FOR_SCHEMA,
                tableTypeEntity.getGUID(),
                derivedColumnEntity.getGUID(),
                new InstanceProperties());
        return derivedColumnEntity;
    }




}
