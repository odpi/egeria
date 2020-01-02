/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.openconnectors.base;

import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchive;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveGUIDMap;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * OpenConnectorArchiveBuilder creates the open metadata compliant instances for connectors that
 * follow the Open Connector Framework (OCF).
 */
public class OpenConnectorArchiveBuilder
{
    private static final String guidMapFileNamePostFix    = "GUIDMap.json";

    private static final String CONNECTION_TYPE_NAME                     = "Connection";
    private static final String CONNECTOR_TYPE_TYPE_NAME                 = "ConnectorType";
    private static final String ENDPOINT_TYPE_NAME                       = "Endpoint";
    private static final String CONNECTION_CONNECTOR_TYPE_TYPE_NAME      = "ConnectionConnectorType";
    private static final String CONNECTION_ENDPOINT_TYPE_NAME            = "ConnectionEndpoint";

    private static final String QUALIFIED_NAME_PROPERTY                      = "qualifiedName";
    private static final String ADDITIONAL_PROPERTIES_PROPERTY               = "additionalProperties";
    private static final String DISPLAY_NAME_PROPERTY                        = "displayName";
    private static final String DESCRIPTION_PROPERTY                         = "description";
    private static final String SECURED_PROPERTIES_PROPERTY                  = "securedProperties";
    private static final String CONFIGURATION_PROPERTIES_PROPERTY            = "configurationProperties";
    private static final String USER_ID_PROPERTY                             = "userId";
    private static final String CLEAR_PASSWORD_PROPERTY                      = "clearPassword";
    private static final String ENCRYPTED_PASSWORD_PROPERTY                  = "encryptedPassword";
    private static final String CONNECTOR_PROVIDER_PROPERTY                  = "connectorProviderClassName";
    private static final String RECOGNIZED_ADDITIONAL_PROPERTIES_PROPERTY    = "recognizedAdditionalProperties";
    private static final String RECOGNIZED_SECURED_PROPERTIES_PROPERTY       = "recognizedSecuredProperties";
    private static final String RECOGNIZED_CONFIGURATION_PROPERTIES_PROPERTY = "recognizedConfigurationProperties";
    private static final String NETWORK_ADDRESS_PROPERTY                     = "networkAddress";
    private static final String PROTOCOL_PROPERTY                            = "protocol";

    private OMRSArchiveBuilder archiveBuilder;
    private OMRSArchiveHelper  archiveHelper;
    private OMRSArchiveGUIDMap idToGUIDMap;

    private String             archiveRootName;
    private String             originatorName;
    private String             versionName;


    /**
     * Typical constructor passes parameters used to build the open metadata archive's property header.
     *
     * @param archiveGUID unique identifier for this open metadata archive.
     * @param archiveName name of the open metadata archive.
     * @param archiveDescription description of the open metadata archive.
     * @param archiveType enum describing the type of archive this is.
     * @param archiveRootName non-spaced root name of the open metadata archive elements.
     * @param originatorName name of the originator (person or organization) of the archive.
     * @param originatorLicense license for the content.
     * @param creationDate data that this archive was created.
     * @param versionNumber version number of the archive.
     * @param versionName version name for the archive.
     */
    protected OpenConnectorArchiveBuilder(String                     archiveGUID,
                                          String                     archiveName,
                                          String                     archiveDescription,
                                          OpenMetadataArchiveType    archiveType,
                                          String                     archiveRootName,
                                          String                     originatorName,
                                          String                     originatorLicense,
                                          Date                       creationDate,
                                          long                       versionNumber,
                                          String                     versionName)
    {
        List<OpenMetadataArchive>  dependentOpenMetadataArchives = new ArrayList<>();

        dependentOpenMetadataArchives.add(new OpenMetadataTypesArchive().getOpenMetadataArchive());

        this.archiveBuilder = new OMRSArchiveBuilder(archiveGUID,
                                                     archiveName,
                                                     archiveDescription,
                                                     archiveType,
                                                     originatorName,
                                                     originatorLicense,
                                                     creationDate,
                                                     dependentOpenMetadataArchives);

        this.archiveHelper = new OMRSArchiveHelper(archiveBuilder,
                                                   archiveGUID,
                                                   originatorName,
                                                   creationDate,
                                                   versionNumber,
                                                   versionName);

        this.idToGUIDMap = new OMRSArchiveGUIDMap(archiveRootName + guidMapFileNamePostFix);

        this.archiveRootName = archiveRootName;
        this.originatorName = originatorName;
        this.versionName = versionName;
    }


    /**
     * Returns the open metadata type archive containing all of the content loaded by the subclass.
     *
     * @return populated open metadata archive object
     */
    protected OpenMetadataArchive getOpenMetadataArchive()
    {
        System.out.println("GUIDs map size: " + idToGUIDMap.getSize());

        idToGUIDMap.saveGUIDs();

        return archiveBuilder.getOpenMetadataArchive();
    }


    /**
     * Throws an exception if there is a problem building the archive.
     *
     * @param methodName calling method
     */
    protected void logBadArchiveContent(String   methodName)
    {
        archiveBuilder.logBadArchiveContent(methodName);
    }


    /**
     * Create a connection entity.
     *
     * @param qualifiedName unique name for the connection
     * @param displayName display name for the connection
     * @param description description about the connection
     * @param userId userId that the connector should use to connect to the platform that hosts the asset.
     * @param clearPassword possible password for the connector
     * @param encryptedPassword possible password for the connector
     * @param securedProperties properties hidden from the client
     * @param configurationProperties properties used to configure the connector
     * @param additionalProperties any other properties.
     * @param connectorTypeGUID unique identifier for the connector type
     * @param endpointGUID unique identifier for the endpoint of the asset
     *
     * @return id for the connection
     */
    protected String addConnection(String              qualifiedName,
                                   String              displayName,
                                   String              description,
                                   String              userId,
                                   String              clearPassword,
                                   String              encryptedPassword,
                                   Map<String, String> securedProperties,
                                   Map<String, Object> configurationProperties,
                                   Map<String, String> additionalProperties,
                                   String              connectorTypeGUID,
                                   String              endpointGUID)
    {
        final String methodName = "addConnection";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DISPLAY_NAME_PROPERTY, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, USER_ID_PROPERTY, userId, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, CLEAR_PASSWORD_PROPERTY, clearPassword, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, ENCRYPTED_PASSWORD_PROPERTY, encryptedPassword, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, SECURED_PROPERTIES_PROPERTY, securedProperties, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);
        properties = archiveHelper.addMapPropertyToInstance(archiveRootName, properties, CONFIGURATION_PROPERTIES_PROPERTY, configurationProperties, methodName);

        EntityDetail connectionEntity = archiveHelper.getEntityDetail(CONNECTION_TYPE_NAME,
                                                                      idToGUIDMap.getGUID(qualifiedName),
                                                                      properties,
                                                                      InstanceStatus.ACTIVE,
                                                                      null);

        archiveBuilder.addEntity(connectionEntity);

        if (connectorTypeGUID != null)
        {
            EntityDetail connectorTypeEntity = archiveBuilder.getEntity(connectorTypeGUID);

            EntityProxy end1 = archiveHelper.getEntityProxy(connectionEntity);
            EntityProxy end2 = archiveHelper.getEntityProxy(connectorTypeEntity);

            archiveBuilder.addRelationship(archiveHelper.getRelationship(CONNECTION_CONNECTOR_TYPE_TYPE_NAME,
                                                                         idToGUIDMap.getGUID(qualifiedName + "_connectorType_relationship"),
                                                                         null,
                                                                         InstanceStatus.ACTIVE,
                                                                         end1,
                                                                         end2));
        }

        if (endpointGUID != null)
        {
            EntityDetail endpointEntity = archiveBuilder.getEntity(connectorTypeGUID);

            EntityProxy end1 = archiveHelper.getEntityProxy(endpointEntity);
            EntityProxy end2 = archiveHelper.getEntityProxy(connectionEntity);

            archiveBuilder.addRelationship(archiveHelper.getRelationship(CONNECTION_ENDPOINT_TYPE_NAME,
                                                                         idToGUIDMap.getGUID(qualifiedName + "_endpoint_relationship"),
                                                                         null,
                                                                         InstanceStatus.ACTIVE,
                                                                         end1,
                                                                         end2));
        }

        return connectionEntity.getGUID();
    }


    /**
     * Create a connector type entity.
     *
     * @param connectorTypeGUID fixed unique identifier for connector type - comes from the Connector Provider
     * @param qualifiedName unique name for the connector type
     * @param displayName display name for the connector type
     * @param description description about the connector type
     * @param connectorProviderClassName code for this type of connector
     * @param recognizedSecuredProperties names of supported properties hidden from the client - for connection object.
     * @param recognizedConfigurationProperties names of supported properties used to configure the connector - for connection object.
     * @param recognizedAdditionalProperties names of any other properties for connection object.
     * @param additionalProperties any other properties.
     *
     * @return id for the connector type
     */
    protected String addConnectorType(String              connectorTypeGUID,
                                      String              qualifiedName,
                                      String              displayName,
                                      String              description,
                                      String              connectorProviderClassName,
                                      List<String>        recognizedSecuredProperties,
                                      List<String>        recognizedConfigurationProperties,
                                      List<String>        recognizedAdditionalProperties,
                                      Map<String, String> additionalProperties)
    {
        idToGUIDMap.setGUID(qualifiedName, connectorTypeGUID);

        return this.addConnectorType(qualifiedName,
                                     displayName,
                                     description,
                                     connectorProviderClassName,
                                     recognizedSecuredProperties,
                                     recognizedConfigurationProperties,
                                     recognizedAdditionalProperties,
                                     additionalProperties);
    }

    /**
     * Create a connector type entity.
     *
     * @param qualifiedName unique name for the connector type
     * @param displayName display name for the connector type
     * @param description description about the connector type
     * @param connectorProviderClassName code for this type of connector
     * @param recognizedSecuredProperties names of supported properties hidden from the client - for connection object.
     * @param recognizedConfigurationProperties names of supported properties used to configure the connector - for connection object.
     * @param recognizedAdditionalProperties names of any other properties for connection object.
     * @param additionalProperties any other properties.
     *
     * @return id for the connector type
     */
    protected String addConnectorType(String              qualifiedName,
                                      String              displayName,
                                      String              description,
                                      String              connectorProviderClassName,
                                      List<String>        recognizedSecuredProperties,
                                      List<String>        recognizedConfigurationProperties,
                                      List<String>        recognizedAdditionalProperties,
                                      Map<String, String> additionalProperties)
    {
        final String methodName = "addConnectorType";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DISPLAY_NAME_PROPERTY, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, CONNECTOR_PROVIDER_PROPERTY, connectorProviderClassName, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, RECOGNIZED_SECURED_PROPERTIES_PROPERTY, recognizedSecuredProperties, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, RECOGNIZED_ADDITIONAL_PROPERTIES_PROPERTY, recognizedAdditionalProperties, methodName);
        properties = archiveHelper.addStringArrayPropertyToInstance(archiveRootName, properties, RECOGNIZED_CONFIGURATION_PROPERTIES_PROPERTY, recognizedConfigurationProperties, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);

        EntityDetail connectorTypeEntity = archiveHelper.getEntityDetail(CONNECTOR_TYPE_TYPE_NAME,
                                                                         idToGUIDMap.getGUID(qualifiedName),
                                                                         properties,
                                                                         InstanceStatus.ACTIVE,
                                                                         null);

        archiveBuilder.addEntity(connectorTypeEntity);

        return connectorTypeEntity.getGUID();
    }


    /**
     * Create a endpoint entity.
     *
     * @param qualifiedName unique name for the endpoint
     * @param displayName display name for the endpoint
     * @param description description about the endpoint
     * @param networkAddress location of the asset
     * @param protocol protocol to use to connect to the asset
     * @param additionalProperties any other properties.
     *
     * @return id for the endpoint
     */
    protected String addEndpoint(String              qualifiedName,
                                 String              displayName,
                                 String              description,
                                 String              networkAddress,
                                 String              protocol,
                                 Map<String, String> additionalProperties)
    {
        final String methodName = "addEndpoint";

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, QUALIFIED_NAME_PROPERTY, qualifiedName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DISPLAY_NAME_PROPERTY, displayName, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, DESCRIPTION_PROPERTY, description, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, NETWORK_ADDRESS_PROPERTY, networkAddress, methodName);
        properties = archiveHelper.addStringPropertyToInstance(archiveRootName, properties, PROTOCOL_PROPERTY, protocol, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, ADDITIONAL_PROPERTIES_PROPERTY, additionalProperties, methodName);

        EntityDetail connectorTypeEntity = archiveHelper.getEntityDetail(CONNECTOR_TYPE_TYPE_NAME,
                                                                         idToGUIDMap.getGUID(qualifiedName),
                                                                         properties,
                                                                         InstanceStatus.ACTIVE,
                                                                         null);

        archiveBuilder.addEntity(connectorTypeEntity);

        return connectorTypeEntity.getGUID();
    }
}
