/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataplatform.handlers;

import org.odpi.openmetadata.accessservices.dataplatform.events.NewDeployedDatabaseSchemaEvent;
import org.odpi.openmetadata.accessservices.dataplatform.properties.DeployedDatabaseSchema;
import org.odpi.openmetadata.accessservices.dataplatform.utils.Constants;
import org.odpi.openmetadata.accessservices.dataplatform.utils.EntityPropertiesBuilder;
import org.odpi.openmetadata.accessservices.dataplatform.utils.QualifiedNameUtils;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import static org.odpi.openmetadata.accessservices.dataplatform.utils.Constants.DATA_PLATFORM_USER_ID;

/**
 * The type DeployedDatabaseSchema asset handler.
 */
public class DeployedDatabaseSchemaAssetHandler {

    private String serviceName;
    private String serverName;
    private OMRSRepositoryHelper repositoryHelper;
    private RepositoryHandler repositoryHandler;
    private InvalidParameterHandler invalidParameterHandler;

    /**
     * Instantiates a new Deployed database schema asset handler.
     *
     * @param serviceName             the service name
     * @param serverName              the server name
     * @param repositoryHelper        the repository helper
     * @param repositoryHandler       the repository handler
     * @param invalidParameterHandler the invalid parameter handler
     */
    public DeployedDatabaseSchemaAssetHandler(String serviceName, String serverName, OMRSRepositoryHelper repositoryHelper, RepositoryHandler repositoryHandler, InvalidParameterHandler invalidParameterHandler) {
        this.serviceName=serviceName;
        this.serverName=serverName;
        this.repositoryHelper = repositoryHelper;
        this.repositoryHandler = repositoryHandler;
        this.invalidParameterHandler = invalidParameterHandler;
    }

    /**
     * Create deployed database schema asset string.
     *
     * @param deployedDatabaseSchema the deployed database schema
     * @return the string
     * @throws PropertyServerException    the property server exception
     * @throws InvalidParameterException  the invalid parameter exception
     */
    public String createDeployedDatabaseSchemaAsset(DeployedDatabaseSchema deployedDatabaseSchema)
            throws PropertyServerException,
            UserNotAuthorizedException,
            InvalidParameterException{

        String methodName = "create Deployed Database Schema Asset";

        String qualifiedNameForDeployedDatabaseSchema = deployedDatabaseSchema.getQualifiedName();

        InstanceProperties deployedDbSchemaProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForDeployedDatabaseSchema)
                .withStringProperty(Constants.NAME, deployedDatabaseSchema.getName())
                .withStringProperty(Constants.OWNER, "Owner Info")
                .withStringProperty(Constants.DESCRIPTION, "Description")
                .build();

        invalidParameterHandler.validateName(qualifiedNameForDeployedDatabaseSchema, Constants.QUALIFIED_NAME, methodName);

        return repositoryHandler.createEntity(
                DATA_PLATFORM_USER_ID,
                repositoryHelper.getTypeDefByName(DATA_PLATFORM_USER_ID, Constants.DEPLOYED_DATABASE_SCHEMA).getGUID(),
                Constants.DEPLOYED_DATABASE_SCHEMA,
                deployedDbSchemaProperties,
                methodName);
    }


    /**
     * Create deployed database schema asset.
     *
     * @param event the event
     * @throws PropertyServerException    the property server exception
     * @throws UserNotAuthorizedException the user not authorized exception
     * @throws InvalidParameterException  the invalid parameter exception
     */
    public void createDeployedDatabaseSchemaAsset(NewDeployedDatabaseSchemaEvent event) throws
            PropertyServerException,
            org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException,
            InvalidParameterException {


        String methodName = "create Deployed Database Schema Asset";
        String qualifiedNameForSoftwareServer = QualifiedNameUtils.buildQualifiedName("", Constants.SOFTWARE_SERVER,
                event.getDataPlatform().getDataPlatformEndpoint().getDisplayName() + event.getDataPlatform().getDataPlatformEndpoint().getAddress().split(":")[0]);
        invalidParameterHandler.validateUserId(DATA_PLATFORM_USER_ID, methodName);
        invalidParameterHandler.validateName(qualifiedNameForSoftwareServer, Constants.QUALIFIED_NAME, methodName);

        InstanceProperties softwareServerProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForSoftwareServer)
                .withStringProperty(Constants.NAME, qualifiedNameForSoftwareServer)
                .build();

        String softwareServerEntityGuid = repositoryHandler.createEntity(
                DATA_PLATFORM_USER_ID,
                repositoryHelper.getTypeDefByName(DATA_PLATFORM_USER_ID, Constants.SOFTWARE_SERVER).getGUID(),
                Constants.SOFTWARE_SERVER,
                softwareServerProperties,
                methodName);


        String qualifiedNameForEndpoint = QualifiedNameUtils.buildQualifiedName("", Constants.ENDPOINT,
                event.getDataPlatform().getDataPlatformEndpoint().getEncryptionMethod()+event.getDataPlatform().getDataPlatformEndpoint().getAddress());

        InstanceProperties endpointProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForEndpoint)
                .withStringProperty(Constants.NAME, qualifiedNameForEndpoint)
                .withStringProperty(Constants.NETWORK_ADDRESS, event.getDataPlatform().getDataPlatformEndpoint().getAddress())
                .withStringProperty(Constants.PROTOCOL, event.getDataPlatform().getDataPlatformEndpoint().getProtocol())
                .build();

        invalidParameterHandler.validateName(qualifiedNameForEndpoint, Constants.QUALIFIED_NAME, methodName);
        String endpointEntityGuid = repositoryHandler.createEntity(
                DATA_PLATFORM_USER_ID,
                repositoryHelper.getTypeDefByName(DATA_PLATFORM_USER_ID, Constants.ENDPOINT).getGUID(),
                Constants.ENDPOINT,
                endpointProperties,
                methodName);


        repositoryHandler.createRelationship(
                DATA_PLATFORM_USER_ID,
                repositoryHelper.getTypeDefByName(DATA_PLATFORM_USER_ID, Constants.SERVER_ENDPOINT).getGUID(),
                softwareServerEntityGuid,
                endpointEntityGuid,
                new InstanceProperties(),
                methodName);


        String qualifiedNameForConnection = QualifiedNameUtils.buildQualifiedName(qualifiedNameForEndpoint, Constants.CONNECTION,
                event.getDataPlatform().getDataPlatformEndpoint().getDisplayName());

        InstanceProperties connectionProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForConnection)
                .withStringProperty(Constants.DESCRIPTION, "Connection to " + qualifiedNameForConnection)
                .build();

        invalidParameterHandler.validateName(qualifiedNameForConnection, Constants.QUALIFIED_NAME, methodName);
        String connectionEntityGuid = repositoryHandler.createEntity(
                DATA_PLATFORM_USER_ID,
                repositoryHelper.getTypeDefByName(DATA_PLATFORM_USER_ID, Constants.CONNECTION).getGUID(),
                Constants.CONNECTION,
                connectionProperties,
                methodName);


        repositoryHandler.createRelationship(
                DATA_PLATFORM_USER_ID,
                repositoryHelper.getTypeDefByName(DATA_PLATFORM_USER_ID, Constants.CONNECTION_TO_ENDPOINT).getGUID(),
                endpointEntityGuid,
                connectionEntityGuid,
                new InstanceProperties(),
                methodName);


        String qualifiedNameForConnectorType = QualifiedNameUtils.buildQualifiedName("", Constants.CONNECTOR_TYPE,
                event.getDataPlatform().getDataPlatformConnectorType().get(0).getConnectorProviderClassName());

        InstanceProperties connectorTypeProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForConnectorType)
                .withStringProperty(Constants.CONNECTOR_PROVIDER_CLASSNAME, event.getDataPlatform().getDataPlatformConnectorType().get(0).getConnectorProviderClassName())
                .build();

        invalidParameterHandler.validateName(qualifiedNameForConnectorType, Constants.QUALIFIED_NAME, methodName);
        String connectorTypeEntityGuid = repositoryHandler.createEntity(
                DATA_PLATFORM_USER_ID,
                repositoryHelper.getTypeDefByName(DATA_PLATFORM_USER_ID, Constants.CONNECTOR_TYPE).getGUID(),
                Constants.CONNECTOR_TYPE,
                connectorTypeProperties,
                methodName);

        repositoryHandler.createRelationship(
                DATA_PLATFORM_USER_ID,
                repositoryHelper.getTypeDefByName(DATA_PLATFORM_USER_ID, Constants.CONNECTION_CONNECTOR_TYPE).getGUID(),
                connectionEntityGuid,
                connectorTypeEntityGuid,
                new InstanceProperties(),
                methodName);


        //TODO: fill in details in event payload about database server side info and change the qulified name
        String qualifiedNameForDatabase = QualifiedNameUtils.buildQualifiedName(qualifiedNameForSoftwareServer, Constants.DATABASE,
                "Apache Cassandra Data Store");
        InstanceProperties databaseProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForDatabase)
                .withStringProperty(Constants.NAME, "Apache Cassandra Data Store")
                .build();

        invalidParameterHandler.validateName(qualifiedNameForDatabase, Constants.QUALIFIED_NAME, methodName);
        String databaseEntityGuid =repositoryHandler.createEntity(
                DATA_PLATFORM_USER_ID,
                repositoryHelper.getTypeDefByName(DATA_PLATFORM_USER_ID, Constants.DATABASE).getGUID(),
                Constants.DATABASE,
                databaseProperties,
                methodName);
        String qualifiedNameForDeployedDatabaseSchema = QualifiedNameUtils.buildQualifiedName(qualifiedNameForDatabase, Constants.DEPLOYED_DATABASE_SCHEMA,
                event.getDeployedDatabaseSchema().getName());

        InstanceProperties deployedDbSchemaProperties = new EntityPropertiesBuilder()
                .withStringProperty(Constants.QUALIFIED_NAME, qualifiedNameForDeployedDatabaseSchema)
                .withStringProperty(Constants.NAME, event.getDeployedDatabaseSchema().getName())
                //TODO: complete database source info from data platform service side
                .withStringProperty(Constants.OWNER, "Owner Info")
                .withStringProperty(Constants.DESCRIPTION, "Description")
                .build();

        invalidParameterHandler.validateName(qualifiedNameForDeployedDatabaseSchema, Constants.QUALIFIED_NAME, methodName);
        String deployedDbSchemaEntityGuid =repositoryHandler.createEntity(
                DATA_PLATFORM_USER_ID,
                repositoryHelper.getTypeDefByName(DATA_PLATFORM_USER_ID, Constants.DEPLOYED_DATABASE_SCHEMA).getGUID(),
                Constants.DEPLOYED_DATABASE_SCHEMA,
                deployedDbSchemaProperties,
                methodName);

        repositoryHandler.createRelationship(
                DATA_PLATFORM_USER_ID,
                repositoryHelper.getTypeDefByName(DATA_PLATFORM_USER_ID, Constants.DATA_CONTENT_FOR_DATASET).getGUID(),
                databaseEntityGuid,
                deployedDbSchemaEntityGuid,
                new InstanceProperties(),
                methodName);

        repositoryHandler.createRelationship(
                DATA_PLATFORM_USER_ID,
                repositoryHelper.getTypeDefByName(DATA_PLATFORM_USER_ID, Constants.CONNECTION_TO_ASSET).getGUID(),
                connectionEntityGuid,
                deployedDbSchemaEntityGuid,
                new InstanceProperties(),
                methodName);

        //TODO: Check whether the new Deployed DB also contains any schema types or schema attributes
    }


}

