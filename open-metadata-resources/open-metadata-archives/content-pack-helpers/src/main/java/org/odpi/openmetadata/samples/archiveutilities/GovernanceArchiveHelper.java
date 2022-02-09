/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities;

import org.odpi.openmetadata.frameworks.connectors.ConnectorProviderBase;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ConnectorType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.OpenMetadataArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceStatus;

import java.util.Date;
import java.util.Map;

/**
 * GovernanceArchiveHelper creates elements for governance.  This includes governance program definitions, governance engine definitions and
 * governance action process definitions.
 */
public class GovernanceArchiveHelper extends SimpleCatalogArchiveHelper
{
    private static final String GOVERNANCE_ENGINE_TYPE_NAME       = "GovernanceEngine";
    private static final String GOVERNANCE_SERVICE_TYPE_NAME      = "GovernanceService";

    private static final String SUPPORTED_GOVERNANCE_SERVICE_TYPE_NAME   = "SupportedGovernanceService";

    private static final String REQUEST_TYPE_PROPERTY                        = "requestType";
    private static final String REQUEST_PARAMETERS_PROPERTY                  = "parameters";


    /**
     * Typical constructor passes parameters used to build the open metadata archive's property header.
     *
     * @param archiveGUID unique identifier for this open metadata archive.
     * @param archiveRootName non-spaced root name of the open metadata archive elements.
     * @param originatorName name of the originator (person or organization) of the archive.
     * @param creationDate data that this archive was created.
     * @param versionNumber version number of the archive.
     * @param versionName version name for the archive.
     */
    public GovernanceArchiveHelper(OpenMetadataArchiveBuilder archiveBuilder,
                                   String                     archiveGUID,
                                   String                     archiveRootName,
                                   String                     originatorName,
                                   Date                       creationDate,
                                   long                       versionNumber,
                                   String                     versionName)
    {
        super(archiveBuilder, archiveGUID, archiveRootName, originatorName, creationDate, versionNumber, versionName);
    }



    /**
     * Create a governance service entity.
     *
     * @param typeName name of governance service subtype to use - default is GovernanceService
     * @param connectorProviderName name of the connector provider for the governance service
     * @param configurationProperties configuration properties for the governance service (goes in the connection)
     * @param qualifiedName unique name for the governance service
     * @param displayName display name for the governance service
     * @param description description about the governance service
     * @param additionalProperties any other properties
     * @param extendedProperties additional properties defined in the sub type
     *
     * @return id for the governance service
     */
    public String addGovernanceService(String              typeName,
                                       String              connectorProviderName,
                                       Map<String, Object> configurationProperties,
                                       String              qualifiedName,
                                       String              displayName,
                                       String              description,
                                       Map<String, String> additionalProperties,
                                       Map<String, Object> extendedProperties)
    {
        String serviceTypeName = GOVERNANCE_SERVICE_TYPE_NAME;

        if (typeName != null)
        {
            serviceTypeName = typeName;
        }

        try
        {
            Class<?>   connectorProviderClass = Class.forName(connectorProviderName);
            Object     potentialConnectorProvider = connectorProviderClass.getDeclaredConstructor().newInstance();

            ConnectorProviderBase serviceProvider = (ConnectorProviderBase)potentialConnectorProvider;
            ConnectorType         connectorType   = serviceProvider.getConnectorType();

            String connectorTypeGUID = this.addConnectorType(null,
                                                             connectorType.getGUID(),
                                                             connectorType.getQualifiedName(),
                                                             connectorType.getDisplayName(),
                                                             connectorType.getDescription(),
                                                             connectorType.getSupportedAssetTypeName(),
                                                             connectorType.getExpectedDataFormat(),
                                                             connectorType.getConnectorProviderClassName(),
                                                             connectorType.getConnectorFrameworkName(),
                                                             connectorType.getConnectorInterfaceLanguage(),
                                                             connectorType.getConnectorInterfaces(),
                                                             connectorType.getTargetTechnologySource(),
                                                             connectorType.getTargetTechnologyName(),
                                                             connectorType.getTargetTechnologyInterfaces(),
                                                             connectorType.getTargetTechnologyVersions(),
                                                             connectorType.getRecognizedSecuredProperties(),
                                                             connectorType.getRecognizedConfigurationProperties(),
                                                             connectorType.getRecognizedAdditionalProperties(),
                                                             connectorType.getAdditionalProperties());

            String connectionGUID = this.addConnection(qualifiedName + "_implementation",
                                                       displayName + " Governance Service Provider Implementation",
                                                       "Connection for governance service: " + qualifiedName,
                                                       null,
                                                       null,
                                                       null,
                                                       null,
                                                       configurationProperties,
                                                       null,
                                                       connectorTypeGUID,
                                                       null);

            String serviceGUID = super.addAsset(serviceTypeName, qualifiedName, displayName, description, additionalProperties, extendedProperties);

            if ((serviceGUID != null) && (connectionGUID != null))
            {
                super.addConnectionForAsset(serviceGUID, null, connectionGUID);
            }

            return serviceGUID;
        }
        catch (Exception error)
        {
            System.out.println("Invalid connector type " + connectorProviderName + " for governance service.  Exception " + error.getClass().getName() + " with message " + error.getMessage());
        }

        return null;
    }


    /**
     * Create a software capability entity.
     *
     * @param typeName name of software capability subtype to use - default is SoftwareCapability
     * @param qualifiedName unique name for the capability
     * @param displayName display name for the capability
     * @param description description about the capability
     * @param additionalProperties any other properties.
     *
     * @return id for the capability
     */
    public String addGovernanceEngine(String              typeName,
                                      String              qualifiedName,
                                      String              displayName,
                                      String              description,
                                      String              capabilityType,
                                      String              capabilityVersion,
                                      String              patchLevel,
                                      String              source,
                                      Map<String, String> additionalProperties,
                                      Map<String, Object> extendedProperties)
    {
        String engineTypeName = GOVERNANCE_ENGINE_TYPE_NAME;

        if (typeName != null)
        {
            engineTypeName = typeName;
        }

        return super.addSoftwareCapability(engineTypeName, qualifiedName, displayName, description, capabilityType, capabilityVersion, patchLevel, source, additionalProperties, extendedProperties);
    }


    /**
     * Create the relationship between a governance engine and one of its its supported governance services.
     *
     * @param engineGUID unique identifier of the asset
     * @param requestType governance request type to use when calling the service
     * @param requestParameters default request parameters to pass to the service when called with this request type
     * @param serviceGUID unique identifier of the service
     */
    public void addSupportedGovernanceService(String              engineGUID,
                                              String              requestType,
                                              Map<String, String> requestParameters,
                                              String              serviceGUID)
    {
        final String methodName = "addSupportedGovernanceService";

        EntityDetail engineEntity = archiveBuilder.getEntity(engineGUID);
        EntityDetail serviceEntity = archiveBuilder.getEntity(serviceGUID);

        EntityProxy end1 = archiveHelper.getEntityProxy(engineEntity);
        EntityProxy end2 = archiveHelper.getEntityProxy(serviceEntity);

        InstanceProperties properties = archiveHelper.addStringPropertyToInstance(archiveRootName, null, REQUEST_TYPE_PROPERTY, requestType, methodName);
        properties = archiveHelper.addStringMapPropertyToInstance(archiveRootName, properties, REQUEST_PARAMETERS_PROPERTY, requestParameters, methodName);

        archiveBuilder.addRelationship(archiveHelper.getRelationship(SUPPORTED_GOVERNANCE_SERVICE_TYPE_NAME,
                                                                     idToGUIDMap.getGUID(engineGUID + "_to_" + serviceGUID + "_supported_governance_service_relationship"),
                                                                     properties,
                                                                     InstanceStatus.ACTIVE,
                                                                     end1,
                                                                     end2));
    }
}
