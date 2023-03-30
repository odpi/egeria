/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.metadatasecurity.server;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ConnectorBroker;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.metadatasecurity.*;
import org.odpi.openmetadata.metadatasecurity.connectors.OpenMetadataServerSecurityConnector;
import org.odpi.openmetadata.metadatasecurity.ffdc.OpenMetadataSecurityErrorCode;
import org.odpi.openmetadata.metadatasecurity.properties.AssetAuditHeader;
import org.odpi.openmetadata.metadatasecurity.properties.Asset;
import org.odpi.openmetadata.metadatasecurity.properties.ConfidenceGovernanceClassification;
import org.odpi.openmetadata.metadatasecurity.properties.ConfidentialityGovernanceClassification;
import org.odpi.openmetadata.metadatasecurity.properties.Connection;
import org.odpi.openmetadata.metadatasecurity.properties.CriticalityGovernanceClassification;
import org.odpi.openmetadata.metadatasecurity.properties.Glossary;
import org.odpi.openmetadata.metadatasecurity.properties.GovernanceClassificationStatus;
import org.odpi.openmetadata.metadatasecurity.properties.ImpactGovernanceClassification;
import org.odpi.openmetadata.metadatasecurity.properties.ReferenceableStatus;
import org.odpi.openmetadata.metadatasecurity.properties.RetentionGovernanceClassification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.OpenMetadataRepositorySecurity;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEvent;
import org.odpi.openmetadata.repositoryservices.events.OpenMetadataEventsSecurity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * OpenMetadataServerSecurityVerifier provides the plug-in point for the open metadata server connector.
 * It supports the same security interfaces, and handles the fact that the security connector is
 * optional.
 */
public class OpenMetadataServerSecurityVerifier implements OpenMetadataRepositorySecurity,
                                                           OpenMetadataEventsSecurity,
                                                           OpenMetadataServerSecurity,
                                                           OpenMetadataServiceSecurity
{
    private static final String QUALIFIED_NAME_PROPERTY_NAME              = "qualifiedName";                        /* from Referenceable entity */
    private static final String ADDITIONAL_PROPERTIES_PROPERTY_NAME       = "additionalProperties";                 /* from Referenceable entity */
    
    private static final String NAME_PROPERTY_NAME                        = "name";                                 /* from Asset entity */

    private static final String DISPLAY_NAME_PROPERTY_NAME                = "displayName";                          /* from many entities */
    private static final String DESCRIPTION_PROPERTY_NAME                 = "description";                          /* from many entity */

    private static final String SECURED_PROPERTIES_PROPERTY_NAME          = "securedProperties";                    /* from Connection entity */
    private static final String CONFIGURATION_PROPERTIES_PROPERTY_NAME    = "configurationProperties";              /* from Connection entity */
    private static final String USER_ID_PROPERTY_NAME                     = "userId";                               /* from Connection entity */
    private static final String CLEAR_PASSWORD_PROPERTY_NAME              = "clearPassword";                        /* from Connection entity */
    private static final String ENCRYPTED_PASSWORD_PROPERTY_NAME          = "encryptedPassword";                    /* from Connection entity */
    private static final String LANGUAGE_PROPERTY_NAME                    = "language";                             /* from Glossary entity */
    private static final String USAGE_PROPERTY_NAME                       = "usage";                                /* from Glossary entity */

    private static final String LEVEL_IDENTIFIER_PROPERTY_NAME              = "levelIdentifier";           /* from many governance entities and classifications */
    private static final String BASIS_IDENTIFIER_PROPERTY_NAME              = "basisIdentifier";           /* from Retention classification */

    private static final String STATUS_IDENTIFIER_PROPERTY_NAME             = "statusIdentifier";

    private static final String CONFIDENTIALITY_CLASSIFICATION_TYPE_NAME          = "Confidentiality";

    private static final String CONFIDENCE_CLASSIFICATION_TYPE_NAME               = "Confidence";

    private static final String CRITICALITY_CLASSIFICATION_TYPE_NAME              = "Criticality";

    private static final String IMPACT_CLASSIFICATION_TYPE_NAME                   = "Impact";
    public static final String SEVERITY_LEVEL_IDENTIFIER_PROPERTY_NAME            = "severityLevelIdentifier";  

    private static final String RETENTION_CLASSIFICATION_TYPE_NAME                = "Retention";

    private static final String GOVERNANCE_CLASSIFICATION_STATUS_PROPERTY_NAME       = "status";
    private static final String GOVERNANCE_CLASSIFICATION_CONFIDENCE_PROPERTY_NAME   = "confidence";
    private static final String GOVERNANCE_CLASSIFICATION_STEWARD_PROPERTY_NAME      = "steward";
    private static final String GOVERNANCE_CLASSIFICATION_SOURCE_PROPERTY_NAME       = "source";
    private static final String GOVERNANCE_CLASSIFICATION_NOTES_PROPERTY_NAME        = "notes";

    private static final String RETENTION_ASSOCIATED_GUID_PROPERTY_NAME              = "associatedGUID";
    private static final String RETENTION_ARCHIVE_AFTER_PROPERTY_NAME                = "archiveAfter";
    private static final String RETENTION_DELETE_AFTER_PROPERTY_NAME                 = "deleteAfter";

    private static final String SECURITY_TAG_CLASSIFICATION_TYPE_NAME             = "SecurityTags";
    private static final String SECURITY_LABELS_PROPERTY_NAME                     = "securityLabels";
    private static final String SECURITY_PROPERTIES_PROPERTY_NAME                 = "securityProperties";
    private static final String ACCESS_GROUPS_PROPERTY_NAME                       = "accessGroups";

    private static final String ASSET_ZONES_CLASSIFICATION_NAME           = "AssetZoneMembership";

    private static final String ZONE_MEMBERSHIP_PROPERTY_NAME             = "zoneMembership";                       /* from Area 4 */


    private static final String ASSET_ORIGIN_CLASSIFICATION_NAME          = "AssetOrigin";

    private static final String ORGANIZATION_PROPERTY_NAME                = "organization";                          /* from AssetOrigin classification */
    private static final String BUSINESS_CAPABILITY_PROPERTY_NAME         = "businessCapability";                    /* from AssetOrigin classification */
    private static final String OTHER_ORIGIN_VALUES_PROPERTY_NAME         = "otherOriginValues";                     /* from AssetOrigin classification */


    private static final String ASSET_OWNERSHIP_CLASSIFICATION_NAME       = "AssetOwnership";
    private static final String OWNERSHIP_CLASSIFICATION_TYPE_NAME        = "Ownership";

    private static final String OWNER_PROPERTY_NAME                       = "owner";                                /* from Area 4 */
    private static final String OWNER_TYPE_PROPERTY_NAME                  = "ownerType"; /* deprecated */


    private OpenMetadataRepositorySecurity repositorySecurityConnector = null;
    private OpenMetadataEventsSecurity     eventsSecurityConnector     = null;
    private OpenMetadataServerSecurity     serverSecurityConnector     = null;
    private OpenMetadataServiceSecurity    serviceSecurityConnector    = null;
    private OpenMetadataConnectionSecurity connectionSecurityConnector = null;
    private OpenMetadataAssetSecurity      assetSecurityConnector      = null;
    private OpenMetadataGlossarySecurity   glossarySecurityConnector   = null;

    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    /**
     * Default constructor
     */
    public OpenMetadataServerSecurityVerifier()
    {
    }


    /**
     * Register an open metadata server security connector to verify access to the server's services.
     *
     * @param localServerUserId local server's userId
     * @param serverName local server's name
     * @param auditLog logging destination
     * @param connection properties used to create the connector
     *
     * @throws InvalidParameterException the connection is invalid
     */
    synchronized public  void registerSecurityValidator(String                                                                    localServerUserId,
                                                        String                                                                    serverName,
                                                        AuditLog                                                                  auditLog,
                                                        org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection   connection) throws InvalidParameterException
    {
        OpenMetadataServerSecurityConnector connector;

        try
        {
            /*
             * The connector standard has lots of optional interfaces.  The tests below set up local variables, one
             * for each interface, to make it easier to call them when the server is processing requests.
             */
            connector = this.getServerSecurityConnector(localServerUserId,
                                                        serverName,
                                                        auditLog,
                                                        connection);

            if (connector instanceof OpenMetadataRepositorySecurity)
            {
                repositorySecurityConnector = (OpenMetadataRepositorySecurity)connector;
            }
            if (connector instanceof OpenMetadataEventsSecurity)
            {
                eventsSecurityConnector = (OpenMetadataEventsSecurity)connector;
            }
            if (connector instanceof OpenMetadataServerSecurity)
            {
                serverSecurityConnector = (OpenMetadataServerSecurity)connector;
            }
            if (connector instanceof OpenMetadataServiceSecurity)
            {
                serviceSecurityConnector = (OpenMetadataServiceSecurity)connector;
            }
            if (connector instanceof OpenMetadataConnectionSecurity)
            {
                connectionSecurityConnector = (OpenMetadataConnectionSecurity)connector;
            }
            if (connector instanceof OpenMetadataAssetSecurity)
            {
                assetSecurityConnector = (OpenMetadataAssetSecurity)connector;
            }
            if (connector instanceof OpenMetadataGlossarySecurity)
            {
                glossarySecurityConnector = (OpenMetadataGlossarySecurity)connector;
            }
        }
        catch (InvalidParameterException error)
        {
            throw new InvalidParameterException(error.getReportedErrorMessage(), error);
        }
    }


    /**
     * Return the Open Metadata Server Security Connector for the connection.
     *
     * @param localServerUserId userId of the server
     * @param serverName name of the server
     * @param auditLog logging destination
     * @param connection connection from the configuration document
     * @return connector or null
     * @throws InvalidParameterException connection did not create a connector
     */
    private   OpenMetadataServerSecurityConnector getServerSecurityConnector(String                                                                    localServerUserId,
                                                                             String                                                                    serverName,
                                                                             AuditLog                                                                  auditLog,
                                                                             org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection   connection) throws InvalidParameterException
    {
        final String methodName = "getServerSecurityConnector";

        OpenMetadataServerSecurityConnector serverSecurityConnector = null;

        if (connection != null)
        {
            try
            {
                ConnectorBroker connectorBroker = new ConnectorBroker(auditLog);
                Connector       connector       = connectorBroker.getConnector(connection);

                serverSecurityConnector = (OpenMetadataServerSecurityConnector)connector;

                serverSecurityConnector.setServerName(serverName);
                serverSecurityConnector.setLocalServerUserId(localServerUserId);
                serverSecurityConnector.start();
            }
            catch (Exception error)
            {
                /*
                 * The assumption is that any exceptions creating the new connector are down to a bad connection
                 */
                throw new InvalidParameterException(OpenMetadataSecurityErrorCode.BAD_SERVER_SECURITY_CONNECTION.getMessageDefinition(serverName,
                                                                                                                                      error.getMessage(),
                                                                                                                                      connection.toString()),
                                                    OpenMetadataPlatformSecurityVerifier.class.getName(),
                                                    methodName,
                                                    error,
                                                    "connection");
            }
        }

        return serverSecurityConnector;
    }



    /**
     * Return the list of supported zones for this asset.  This originates from the configuration of the access server.
     * but may be changed by the security verifier.
     *
     * @param userId calling user
     * @param suppliedSupportedZones supported zones from caller
     * @param serviceName called service
     * @return list of zone names
     * @throws InvalidParameterException invalid parameter
     * @throws PropertyServerException problem from the verifier
     */
    private List<String> getSupportedZones(String       userId,
                                           List<String> suppliedSupportedZones,
                                           String       serviceName) throws InvalidParameterException, PropertyServerException
    {
        if (assetSecurityConnector != null)
        {
            return assetSecurityConnector.setSupportedZonesForUser(suppliedSupportedZones, serviceName, userId);
        }

        return suppliedSupportedZones;
    }


    /**
     * Determine the appropriate setting for the asset zones depending on the content of the asset and the
     * settings of both default zones and supported zones.  This method is called whenever an asset's
     * values are changed.
     *
     * The default behavior is to keep the updated zones as they are.
     *
     * @param defaultZones setting of the default zones for the service
     * @param supportedZones setting of the supported zones for the service
     * @param publishZones setting of the zones that are set when an asset is published for the service
     * @param originalAsset original values for the asset
     * @param updatedAsset updated values for the asset
     *
     * @return list of zones to set in the asset
     * @throws InvalidParameterException one of the asset values is invalid
     * @throws PropertyServerException there is a problem calculating the zones
     */
    public List<String> verifyAssetZones(List<String>  defaultZones,
                                         List<String>  supportedZones,
                                         List<String>  publishZones,
                                         Asset         originalAsset,
                                         Asset         updatedAsset) throws InvalidParameterException,
                                                                            PropertyServerException
    {
        if (assetSecurityConnector != null)
        {
            return assetSecurityConnector.verifyAssetZones(defaultZones, supportedZones, publishZones, originalAsset, updatedAsset);
        }

        List<String>  resultingZones = null;

        if (updatedAsset != null)
        {
            resultingZones = updatedAsset.getZoneMembership();
        }

        return resultingZones;
    }


    /**
     * Determine the appropriate setting for the asset zones depending on the content of the asset and the
     * default zones.  This is called whenever a new asset is created.
     *
     * The default behavior is to use the default values, unless the zones have been explicitly set up,
     * in which case, they are left unchanged.
     *
     * @param defaultZones setting of the default zones for the service
     * @param asset initial values for the asset
     *
     * @return list of zones to set in the asset
     * @throws InvalidParameterException one of the asset values is invalid
     * @throws PropertyServerException there is a problem calculating the zones
     */
    private List<String> setAssetZonesToDefault(List<String>  defaultZones,
                                                Asset         asset) throws InvalidParameterException,
                                                                            PropertyServerException
    {
        List<String>  resultingZones = null;

        if (asset != null)
        {
            if ((asset.getZoneMembership() == null) || (asset.getZoneMembership().isEmpty()))
            {
                resultingZones = defaultZones;
            }
            else
            {
                resultingZones = asset.getZoneMembership();
            }
        }

        if (assetSecurityConnector != null)
        {
            return assetSecurityConnector.setAssetZonesToDefault(resultingZones, asset);
        }

        return resultingZones;
    }


    /**
     * Fill in information about an asset from an entity.  This is to pass to the Open Metadata Security verifier.
     *
     * @param entity properties to add to the bean
     * @param methodName calling method
     */
    private Asset getAssetBeanFromEntity(EntityDetail         entity,
                                         OMRSRepositoryHelper repositoryHelper,
                                         String               serviceName,
                                         String               methodName)
    {
        if ((entity != null) && (entity.getType() != null))
        {
            Asset assetBean = new Asset();

            String              typeId                    = entity.getType().getTypeDefGUID();
            String              typeName                  = entity.getType().getTypeDefName();
            InstanceStatus      instanceStatus            = entity.getStatus();
            String              assetGUID                 = entity.getGUID();
            InstanceProperties  entityProperties          = entity.getProperties();
            InstanceProperties  securityTagProperties     = null;
            InstanceProperties  confidentialityProperties = null;
            InstanceProperties  confidenceProperties      = null;
            InstanceProperties  criticalityProperties     = null;
            InstanceProperties  impactProperties          = null;
            InstanceProperties  retentionProperties       = null;
            InstanceProperties  ownershipProperties       = null;
            InstanceProperties  zoneProperties            = null;
            InstanceProperties  originProperties          = null;

            if (entity.getClassifications() != null)
            {
                for (Classification classification : entity.getClassifications())
                {
                    if (classification != null)
                    {
                        if (SECURITY_TAG_CLASSIFICATION_TYPE_NAME.equals(classification.getName()))
                        {
                            securityTagProperties = classification.getProperties();
                        }
                        else if (CONFIDENTIALITY_CLASSIFICATION_TYPE_NAME.equals(classification.getName()))
                        {
                            confidentialityProperties = classification.getProperties();
                        }
                        else if (CONFIDENCE_CLASSIFICATION_TYPE_NAME.equals(classification.getName()))
                        {
                            confidenceProperties = classification.getProperties();
                        }
                        else if (CRITICALITY_CLASSIFICATION_TYPE_NAME.equals(classification.getName()))
                        {
                            criticalityProperties = classification.getProperties();
                        }
                        else if (IMPACT_CLASSIFICATION_TYPE_NAME.equals(classification.getName()))
                        {
                            impactProperties = classification.getProperties();
                        }
                        else if (RETENTION_CLASSIFICATION_TYPE_NAME.equals(classification.getName()))
                        {
                            retentionProperties = classification.getProperties();
                        }
                        else if (ASSET_OWNERSHIP_CLASSIFICATION_NAME.equals(classification.getName()))
                        {
                            ownershipProperties = classification.getProperties();
                        }
                        else if (OWNERSHIP_CLASSIFICATION_TYPE_NAME.equals(classification.getName()))
                        {
                            ownershipProperties = classification.getProperties();
                        }
                        else if (ASSET_ZONES_CLASSIFICATION_NAME.equals(classification.getName()))
                        {
                            zoneProperties = classification.getProperties();
                        }
                        else if (ASSET_ORIGIN_CLASSIFICATION_NAME.equals(classification.getName()))
                        {
                            originProperties = classification.getProperties();
                        }
                    }
                }
            }

            setupAssetBeanWithEntityProperties(assetBean,
                                               typeId,
                                               typeName,
                                               instanceStatus,
                                               assetGUID,
                                               entityProperties,
                                               securityTagProperties,
                                               confidentialityProperties,
                                               confidenceProperties,
                                               criticalityProperties,
                                               impactProperties,
                                               retentionProperties,
                                               ownershipProperties,
                                               zoneProperties,
                                               originProperties,
                                               repositoryHelper,
                                               serviceName,
                                               methodName);
            return assetBean;
        }

        return null;
    }


    /**
     * Convert an OMRS InstanceStatus enum into a metadata security Referenceable Status enum.
     *
     * @param instanceStatus value from the entity
     * @return mapped enum (default is ReferenceableStatus.UNKNOWN)
     */
    private ReferenceableStatus getReferenceableStatus(InstanceStatus instanceStatus)
    {
        if (instanceStatus != null)
        {
            for (ReferenceableStatus referenceableStatus : ReferenceableStatus.values())
            {
                if (referenceableStatus.getOMRSOrdinal() == instanceStatus.getOrdinal())
                {
                    return referenceableStatus;
                }
            }
        }

        return ReferenceableStatus.UNKNOWN;
    }



    /**
     * Fill in information about an asset from an entity.  This is to pass to the Open Metadata Security verifier.
     *
     * @param assetBean bean to fill out
     * @param typeId unique identifier for the type of the entity
     * @param typeName unique name for the type of the entity
     * @param instanceStatus status from the entity
     * @param assetGUID unique identifier for the entity
     * @param entityProperties properties from the entity
     * @param securityTagProperties properties from the SecurityTags classification
     * @param confidentialityProperties properties from the Confidentiality classification
     * @param confidenceProperties properties from the Confidence classification
     * @param criticalityProperties properties from the Criticality classification
     * @param impactProperties properties from the Impact classification
     * @param retentionProperties properties from the Retention classification
     * @param ownershipProperties properties from the AssetOwnership classification
     * @param zoneProperties properties from the AssetZoneMembership classification
     * @param originProperties properties from the AssetOrigin classification
     * @param repositoryHelper for working with OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     */
    private void setupAssetBeanWithEntityProperties(Asset                assetBean,
                                                    String               typeId,
                                                    String               typeName,
                                                    InstanceStatus       instanceStatus,
                                                    String               assetGUID,
                                                    InstanceProperties   entityProperties,
                                                    InstanceProperties   securityTagProperties,
                                                    InstanceProperties   confidentialityProperties,
                                                    InstanceProperties   confidenceProperties,
                                                    InstanceProperties   criticalityProperties,
                                                    InstanceProperties   impactProperties,
                                                    InstanceProperties   retentionProperties,
                                                    InstanceProperties   ownershipProperties,
                                                    InstanceProperties   zoneProperties,
                                                    InstanceProperties   originProperties,
                                                    OMRSRepositoryHelper repositoryHelper,
                                                    String               serviceName,
                                                    String               methodName)
    {
        assetBean.setTypeGUID(typeId);
        assetBean.setTypeName(typeName);
        assetBean.setStatus(this.getReferenceableStatus(instanceStatus));
        assetBean.setGUID(assetGUID);

        InstanceProperties properties = new InstanceProperties(entityProperties);

        assetBean.setQualifiedName(repositoryHelper.removeStringProperty(serviceName,
                                                                         QUALIFIED_NAME_PROPERTY_NAME,
                                                                         properties,
                                                                         methodName));
        assetBean.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName,
                                                                                       ADDITIONAL_PROPERTIES_PROPERTY_NAME,
                                                                                       properties,
                                                                                       methodName));
        assetBean.setDisplayName(repositoryHelper.removeStringProperty(serviceName,
                                                                       NAME_PROPERTY_NAME,
                                                                       properties,
                                                                       methodName));
        assetBean.setDescription(repositoryHelper.removeStringProperty(serviceName,
                                                                       DESCRIPTION_PROPERTY_NAME,
                                                                       properties,
                                                                       methodName));
        assetBean.setOwner(repositoryHelper.removeStringProperty(serviceName,
                                                                 OWNER_PROPERTY_NAME,
                                                                 properties,
                                                                 methodName));
        assetBean.setOwnerType(repositoryHelper.removeEnumPropertyOrdinal(serviceName,
                                                                          OWNER_TYPE_PROPERTY_NAME,
                                                                          properties,
                                                                          methodName));
        assetBean.setZoneMembership(repositoryHelper.removeStringArrayProperty(serviceName,
                                                                               ZONE_MEMBERSHIP_PROPERTY_NAME,
                                                                               properties,
                                                                               methodName));

        assetBean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(properties));

        if (securityTagProperties != null)
        {
            assetBean.setSecurityLabels(repositoryHelper.getStringArrayProperty(serviceName,
                                                                                SECURITY_LABELS_PROPERTY_NAME,
                                                                                securityTagProperties,
                                                                                methodName));
            assetBean.setSecurityProperties(repositoryHelper.getMapFromProperty(serviceName,
                                                                                SECURITY_PROPERTIES_PROPERTY_NAME,
                                                                                securityTagProperties,
                                                                                methodName));
            assetBean.setAccessGroups(repositoryHelper.getStringArrayStringMapFromProperty(serviceName,
                                                                                           ACCESS_GROUPS_PROPERTY_NAME,
                                                                                           securityTagProperties,
                                                                                           methodName));
        }

        if (confidentialityProperties != null)
        {
            ConfidentialityGovernanceClassification classification = new ConfidentialityGovernanceClassification();

            classification.setStatus(repositoryHelper.getIntProperty(serviceName,
                                                                     STATUS_IDENTIFIER_PROPERTY_NAME,
                                                                     confidentialityProperties,
                                                                     methodName));
            classification.setConfidence(repositoryHelper.getIntProperty(serviceName,
                                                                         GOVERNANCE_CLASSIFICATION_CONFIDENCE_PROPERTY_NAME,
                                                                         confidentialityProperties,
                                                                         methodName));
            classification.setSteward(repositoryHelper.getStringProperty(serviceName,
                                                                         GOVERNANCE_CLASSIFICATION_STEWARD_PROPERTY_NAME,
                                                                         confidentialityProperties,
                                                                         methodName));
            classification.setSource(repositoryHelper.getStringProperty(serviceName,
                                                                        GOVERNANCE_CLASSIFICATION_SOURCE_PROPERTY_NAME,
                                                                        confidentialityProperties,
                                                                        methodName));
            classification.setNotes(repositoryHelper.getStringProperty(serviceName,
                                                                       GOVERNANCE_CLASSIFICATION_NOTES_PROPERTY_NAME,
                                                                       confidentialityProperties,
                                                                       methodName));
            classification.setConfidentialityLevel(repositoryHelper.getIntProperty(serviceName,
                                                                                   LEVEL_IDENTIFIER_PROPERTY_NAME,
                                                                                   confidentialityProperties,
                                                                                   methodName));
        }

        if (confidenceProperties != null)
        {
            ConfidenceGovernanceClassification classification = new ConfidenceGovernanceClassification();

            classification.setStatus(repositoryHelper.getIntProperty(serviceName,
                                                                     STATUS_IDENTIFIER_PROPERTY_NAME,
                                                                     confidenceProperties,
                                                                     methodName));
            classification.setConfidence(repositoryHelper.getIntProperty(serviceName,
                                                                         GOVERNANCE_CLASSIFICATION_CONFIDENCE_PROPERTY_NAME,
                                                                         confidenceProperties,
                                                                         methodName));
            classification.setSteward(repositoryHelper.getStringProperty(serviceName,
                                                                         GOVERNANCE_CLASSIFICATION_STEWARD_PROPERTY_NAME,
                                                                         confidenceProperties,
                                                                         methodName));
            classification.setSource(repositoryHelper.getStringProperty(serviceName,
                                                                        GOVERNANCE_CLASSIFICATION_SOURCE_PROPERTY_NAME,
                                                                        confidenceProperties,
                                                                        methodName));
            classification.setNotes(repositoryHelper.getStringProperty(serviceName,
                                                                       GOVERNANCE_CLASSIFICATION_NOTES_PROPERTY_NAME,
                                                                       confidenceProperties,
                                                                       methodName));
            classification.setConfidenceLevel(repositoryHelper.getIntProperty(serviceName,
                                                                              LEVEL_IDENTIFIER_PROPERTY_NAME,
                                                                              confidenceProperties,
                                                                              methodName));
        }

        if (criticalityProperties != null)
        {
            CriticalityGovernanceClassification classification = new CriticalityGovernanceClassification();

            classification.setStatus(repositoryHelper.getIntProperty(serviceName,
                                                                     STATUS_IDENTIFIER_PROPERTY_NAME,
                                                                     criticalityProperties,
                                                                     methodName));
            classification.setConfidence(repositoryHelper.getIntProperty(serviceName,
                                                                         GOVERNANCE_CLASSIFICATION_CONFIDENCE_PROPERTY_NAME,
                                                                         criticalityProperties,
                                                                         methodName));
            classification.setSteward(repositoryHelper.getStringProperty(serviceName,
                                                                         GOVERNANCE_CLASSIFICATION_STEWARD_PROPERTY_NAME,
                                                                         criticalityProperties,
                                                                         methodName));
            classification.setSource(repositoryHelper.getStringProperty(serviceName,
                                                                        GOVERNANCE_CLASSIFICATION_SOURCE_PROPERTY_NAME,
                                                                        criticalityProperties,
                                                                        methodName));
            classification.setNotes(repositoryHelper.getStringProperty(serviceName,
                                                                       GOVERNANCE_CLASSIFICATION_NOTES_PROPERTY_NAME,
                                                                       criticalityProperties,
                                                                       methodName));
            classification.setCriticalityLevel(repositoryHelper.getIntProperty(serviceName,
                                                                               LEVEL_IDENTIFIER_PROPERTY_NAME,
                                                                               criticalityProperties,
                                                                               methodName));
        }

        if (impactProperties != null)
        {
            ImpactGovernanceClassification classification = new ImpactGovernanceClassification();

            classification.setStatus(repositoryHelper.getIntProperty(serviceName,
                                                                     STATUS_IDENTIFIER_PROPERTY_NAME,
                                                                     impactProperties,
                                                                     methodName));
            classification.setConfidence(repositoryHelper.getIntProperty(serviceName,
                                                                         GOVERNANCE_CLASSIFICATION_CONFIDENCE_PROPERTY_NAME,
                                                                         impactProperties,
                                                                         methodName));
            classification.setSteward(repositoryHelper.getStringProperty(serviceName,
                                                                         GOVERNANCE_CLASSIFICATION_STEWARD_PROPERTY_NAME,
                                                                         impactProperties,
                                                                         methodName));
            classification.setSource(repositoryHelper.getStringProperty(serviceName,
                                                                        GOVERNANCE_CLASSIFICATION_SOURCE_PROPERTY_NAME,
                                                                        impactProperties,
                                                                        methodName));
            classification.setNotes(repositoryHelper.getStringProperty(serviceName,
                                                                       GOVERNANCE_CLASSIFICATION_NOTES_PROPERTY_NAME,
                                                                       impactProperties,
                                                                       methodName));
            classification.setImpactSeverityLevel(repositoryHelper.getIntProperty(serviceName,
                                                                                  SEVERITY_LEVEL_IDENTIFIER_PROPERTY_NAME,
                                                                                  impactProperties,
                                                                                  methodName));
        }

        if (retentionProperties != null)
        {
            RetentionGovernanceClassification classification = new RetentionGovernanceClassification();

            classification.setStatus(repositoryHelper.getIntProperty(serviceName,
                                                                     STATUS_IDENTIFIER_PROPERTY_NAME,
                                                                     retentionProperties,
                                                                     methodName));
            classification.setConfidence(repositoryHelper.getIntProperty(serviceName,
                                                                         GOVERNANCE_CLASSIFICATION_CONFIDENCE_PROPERTY_NAME,
                                                                         retentionProperties,
                                                                         methodName));
            classification.setSteward(repositoryHelper.getStringProperty(serviceName,
                                                                         GOVERNANCE_CLASSIFICATION_STEWARD_PROPERTY_NAME,
                                                                         retentionProperties,
                                                                         methodName));
            classification.setSource(repositoryHelper.getStringProperty(serviceName,
                                                                        GOVERNANCE_CLASSIFICATION_SOURCE_PROPERTY_NAME,
                                                                        retentionProperties,
                                                                        methodName));
            classification.setNotes(repositoryHelper.getStringProperty(serviceName,
                                                                       GOVERNANCE_CLASSIFICATION_NOTES_PROPERTY_NAME,
                                                                       retentionProperties,
                                                                       methodName));
            classification.setRetentionBasis(repositoryHelper.getIntProperty(serviceName,
                                                                             BASIS_IDENTIFIER_PROPERTY_NAME,
                                                                             retentionProperties,
                                                                             methodName));
            classification.setAssociatedGUID(repositoryHelper.getStringProperty(serviceName,
                                                                                RETENTION_ASSOCIATED_GUID_PROPERTY_NAME,
                                                                                retentionProperties,
                                                                                methodName));
            classification.setArchiveAfter(repositoryHelper.getDateProperty(serviceName,
                                                                            RETENTION_ARCHIVE_AFTER_PROPERTY_NAME,
                                                                            retentionProperties,
                                                                            methodName));
            classification.setDeleteAfter(repositoryHelper.getDateProperty(serviceName,
                                                                           RETENTION_DELETE_AFTER_PROPERTY_NAME,
                                                                           retentionProperties,
                                                                           methodName));
        }

        if (ownershipProperties != null)
        {
            assetBean.setOwner(repositoryHelper.getStringProperty(serviceName,
                                                                  OWNER_PROPERTY_NAME,
                                                                  ownershipProperties,
                                                                  methodName));
            assetBean.setOwnerType(repositoryHelper.getEnumPropertyOrdinal(serviceName,
                                                                           OWNER_TYPE_PROPERTY_NAME,
                                                                           ownershipProperties,
                                                                           methodName));
        }

        if (zoneProperties != null)
        {
            assetBean.setZoneMembership(repositoryHelper.getStringArrayProperty(serviceName,
                                                                                ZONE_MEMBERSHIP_PROPERTY_NAME,
                                                                                zoneProperties,
                                                                                methodName));
        }

        if (originProperties != null)
        {
            Map<String, String> origins = new HashMap<>();
            String               propertyValue = repositoryHelper.getStringProperty(serviceName,
                                                                                    ORGANIZATION_PROPERTY_NAME,
                                                                                    originProperties,
                                                                                    methodName);

            if (propertyValue != null)
            {
                origins.put(ORGANIZATION_PROPERTY_NAME, propertyValue);
            }

            propertyValue = repositoryHelper.getStringProperty(serviceName,
                                                               BUSINESS_CAPABILITY_PROPERTY_NAME,
                                                               originProperties,
                                                               methodName);

            if (propertyValue != null)
            {
                origins.put(BUSINESS_CAPABILITY_PROPERTY_NAME, propertyValue);
            }

            Map<String, String> propertyMap = repositoryHelper.getStringMapFromProperty(serviceName,
                                                                                        OTHER_ORIGIN_VALUES_PROPERTY_NAME,
                                                                                        originProperties,
                                                                                        methodName);

            if (propertyMap != null)
            {
                for (String propertyName : propertyMap.keySet())
                {
                    if (propertyName != null)
                    {
                        origins.put(propertyName, propertyMap.get(propertyName));
                    }
                }
            }

            if (! origins.isEmpty())
            {
                assetBean.setOrigin(origins);
            }
        }
    }


    /**
     * Fill in information about a connection from an entity.  This is to pass to the Open Metadata Security verifier.
     *
     * @param entity properties fill out
     * @param repositoryHelper for working with OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     */
    private Connection getConnectionFromEntity(EntityDetail         entity,
                                               OMRSRepositoryHelper repositoryHelper,
                                               String               serviceName,
                                               String               methodName)
    {
        if ((entity != null) && (entity.getType() != null))
        {
            Connection connectionBean = new Connection();

            connectionBean.setTypeGUID(entity.getType().getTypeDefGUID());
            connectionBean.setTypeName(entity.getType().getTypeDefName());

            connectionBean.setGUID(entity.getGUID());

            InstanceProperties properties = new InstanceProperties(entity.getProperties());

            connectionBean.setQualifiedName(repositoryHelper.removeStringProperty(serviceName,
                                                                                  QUALIFIED_NAME_PROPERTY_NAME,
                                                                                  properties,
                                                                                  methodName));

            connectionBean.setDisplayName(repositoryHelper.removeStringProperty(serviceName,
                                                                                DISPLAY_NAME_PROPERTY_NAME,
                                                                                properties,
                                                                                methodName));
            connectionBean.setDescription(repositoryHelper.removeStringProperty(serviceName,
                                                                                DESCRIPTION_PROPERTY_NAME,
                                                                                properties,
                                                                                methodName));

            connectionBean.setUserId(repositoryHelper.removeStringProperty(serviceName,
                                                                           USER_ID_PROPERTY_NAME,
                                                                           properties,
                                                                           methodName));

            connectionBean.setClearPassword(repositoryHelper.removeStringProperty(serviceName,
                                                                                  CLEAR_PASSWORD_PROPERTY_NAME,
                                                                                  properties,
                                                                                  methodName));

            connectionBean.setEncryptedPassword(repositoryHelper.removeStringProperty(serviceName,
                                                                                      ENCRYPTED_PASSWORD_PROPERTY_NAME,
                                                                                      properties,
                                                                                      methodName));

            connectionBean.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName,
                                                                                                ADDITIONAL_PROPERTIES_PROPERTY_NAME,
                                                                                                properties,
                                                                                                methodName));

            connectionBean.setConfigurationProperties(repositoryHelper.removeMapFromProperty(serviceName,
                                                                                             CONFIGURATION_PROPERTIES_PROPERTY_NAME,
                                                                                             properties,
                                                                                             methodName));

            connectionBean.setSecuredProperties(repositoryHelper.removeMapFromProperty(serviceName,
                                                                                       SECURED_PROPERTIES_PROPERTY_NAME,
                                                                                       properties,
                                                                                       methodName));

            connectionBean.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(properties));

            return connectionBean;
        }

        return null;
    }



    /**
     * Return the enum value that matches the ordinal from the classification properties.  If the ordinal is not recognized,
     * the enum returned is null.
     *
     * @param governanceClassificationProperties properties from classification (not null)
     * @param methodName calling methodName
     * @return status level identifier
     */
    private int getGovernanceClassificationStatus(InstanceProperties   governanceClassificationProperties,
                                                  OMRSRepositoryHelper repositoryHelper,
                                                  String               serviceName,
                                                  String               methodName)
    {
        int enumOrdinal = repositoryHelper.getEnumPropertyOrdinal(serviceName,
                                                                  GOVERNANCE_CLASSIFICATION_STATUS_PROPERTY_NAME,
                                                                  governanceClassificationProperties,
                                                                  methodName);

        if (enumOrdinal >= 0)
        {
            GovernanceClassificationStatus[] enums = GovernanceClassificationStatus.values();

            for (GovernanceClassificationStatus status : enums)
            {
                if (status.getOpenTypeOrdinal() == enumOrdinal)
                {
                    return status.getOpenTypeOrdinal();
                }
            }
        }

        return repositoryHelper.getIntProperty(serviceName,
                                               STATUS_IDENTIFIER_PROPERTY_NAME,
                                               governanceClassificationProperties,
                                               methodName);
    }



    /**
     * Validate that the user is able to retrieve the requested connection.
     *
     * @param userId calling user
     * @param entity entity storing the connection's properties
     * @param repositoryHelper for working with OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException user not authorized to access this connection
     */
    public void validateUserForConnection(String               userId,
                                          EntityDetail         entity,
                                          OMRSRepositoryHelper repositoryHelper,
                                          String               serviceName,
                                          String               methodName) throws UserNotAuthorizedException
    {
        Connection connectionBean = this.getConnectionFromEntity(entity, repositoryHelper, serviceName, methodName);

        this.validateUserForConnection(userId, connectionBean);
    }


    /**
     * Use the security connector to make a choice on which connection to supply to the requesting user.
     * 
     * @param userId calling userId
     * @param assetEntity associated asset - may be null
     * @param connectionEntities list of retrieved connections
     * @param repositoryHelper for working with OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @return single connection entity, or null
     * @throws UserNotAuthorizedException the user is not able to use any of the connections
     * @throws PropertyServerException unable to reduce the number of connections to
     */
    public EntityDetail selectConnection(String               userId,
                                         EntityDetail         assetEntity,
                                         List<EntityDetail>   connectionEntities,
                                         OMRSRepositoryHelper repositoryHelper,
                                         String               serviceName,
                                         String               methodName) throws UserNotAuthorizedException,
                                                                                 PropertyServerException
    {
        if (connectionSecurityConnector != null)
        {
            /*
             * Multiple connections have been returned.  The code below asks the security verifier to choose which one should be
             * returned to the caller.
             */
            List<Connection> candidateConnections = new ArrayList<>();

            for (EntityDetail connectionEntity : connectionEntities)
            {
                if (connectionEntity != null)
                {
                    Connection candidateConnection = this.getConnectionFromEntity(connectionEntity,
                                                                                  repositoryHelper,
                                                                                  serviceName,
                                                                                  methodName);

                    candidateConnections.add(candidateConnection);
                }
            }

            Connection connection = connectionSecurityConnector.validateUserForAssetConnectionList(userId,
                                                                                                   this.getAssetBeanFromEntity(assetEntity, repositoryHelper, serviceName, methodName),
                                                                                                   candidateConnections);

            if (connection != null)
            {
                for (EntityDetail connectionEntity : connectionEntities)
                {
                    if ((connectionEntity != null) && (connectionEntity.getGUID() != null))
                    {
                        if (connectionEntity.getGUID().equals(connection.getGUID()))
                        {
                            return connectionEntity;
                        }
                    }
                }

                throw new PropertyServerException(OpenMetadataSecurityErrorCode.UNKNOWN_CONNECTION_RETURNED.getMessageDefinition(Integer.toString(connectionEntities.size()),
                                                                                                                                 assetEntity.getGUID(),
                                                                                                                                 userId,
                                                                                                                                 methodName),
                                                  this.getClass().getName(),
                                                  methodName);
            }
            else
            {
                throw new UserNotAuthorizedException(OpenMetadataSecurityErrorCode.NO_CONNECTIONS_ALLOWED.getMessageDefinition(Integer.toString(connectionEntities.size()),
                                                                                                                               assetEntity.getGUID(),
                                                                                                                               userId,
                                                                                                                               methodName),
                                                     this.getClass().getName(),
                                                     userId,
                                                     methodName);
            }
        }
        else if (connectionEntities.size() == 1)
        {
            return connectionEntities.get(0);
        }
        else
        {
            throw new PropertyServerException(OpenMetadataSecurityErrorCode.MULTIPLE_CONNECTIONS_FOUND.getMessageDefinition(Integer.toString(connectionEntities.size()),
                                                                                                                            assetEntity.getGUID(),
                                                                                                                            userId,
                                                                                                                            methodName),
                                              this.getClass().getName(),
                                              methodName);
        }
    }


    /**
     * Check that the calling user is authorized to issue a (any) request to the OMAG Server Platform.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to access this function
     */
    @Override
    public void  validateUserForServer(String   userId) throws UserNotAuthorizedException
    {
        if (serverSecurityConnector != null)
        {
            serverSecurityConnector.validateUserForServer(userId);
        }
    }


    /**
     * Check that the calling user is authorized to update the configuration for a server.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to change configuration
     */
    @Override
    public void  validateUserAsServerAdmin(String   userId) throws UserNotAuthorizedException
    {
        if (serverSecurityConnector != null)
        {
            serverSecurityConnector.validateUserAsServerAdmin(userId);
        }
    }


    /**
     * Check that the calling user is authorized to issue operator requests to the OMAG Server.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue operator commands to this server
     */
    @Override
    public void  validateUserAsServerOperator(String   userId) throws UserNotAuthorizedException
    {
        if (serverSecurityConnector != null)
        {
            serverSecurityConnector.validateUserAsServerOperator(userId);
        }
    }


    /**
     * Check that the calling user is authorized to issue operator requests to the OMAG Server.
     *
     * @param userId calling user
     *
     * @throws UserNotAuthorizedException the user is not authorized to issue diagnostic commands to this server
     */
    @Override
    public void  validateUserAsServerInvestigator(String   userId) throws UserNotAuthorizedException
    {
        if (serverSecurityConnector != null)
        {
            serverSecurityConnector.validateUserAsServerInvestigator(userId);
        }
    }


    /**
     * Check that the calling user is authorized to issue this request.
     *
     * @param userId calling user
     * @param serviceName name of called service
     *
     * @throws UserNotAuthorizedException the user is not authorized to access this service
     */
    @Override
    public void  validateUserForService(String   userId,
                                        String   serviceName) throws UserNotAuthorizedException
    {
        if (serviceSecurityConnector != null)
        {
            serviceSecurityConnector.validateUserForService(userId, serviceName);
        }
    }


    /**
     * Check that the calling user is authorized to issue this specific request.
     *
     * @param userId calling user
     * @param serviceName name of called service
     * @param serviceOperationName name of called operation
     *
     * @throws UserNotAuthorizedException the user is not authorized to access this service
     */
    @Override
    public void  validateUserForServiceOperation(String   userId,
                                                 String   serviceName,
                                                 String   serviceOperationName) throws UserNotAuthorizedException
    {
        if (serviceSecurityConnector != null)
        {
            serviceSecurityConnector.validateUserForServiceOperation(userId, serviceName, serviceOperationName);
        }
    }


    /**
     * Tests for whether a specific user should have access to a connection.
     *
     * @param userId identifier of user
     * @param connection connection object
     * @throws UserNotAuthorizedException the user is not authorized to access this service
     */
    public void  validateUserForConnection(String     userId,
                                           Connection connection) throws UserNotAuthorizedException
    {
        if (connectionSecurityConnector != null)
        {
            connectionSecurityConnector.validateUserForConnection(userId, new Connection(connection));
        }
    }


    /**
     * Tests for whether a specific user should have the right to create an asset within a zone.
     *
     * @param userId identifier of user
     * @param entityTypeGUID unique identifier of the type of entity to create
     * @param entityTypeName unique name of the type of entity to create
     * @param newProperties properties for new entity
     * @param classifications classifications for new entity
     * @param instanceStatus status for new entity
     * @param defaultZones initial setting of the asset's zone membership
     * @param repositoryHelper manipulates repository service objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to access this zone
     * @throws InvalidParameterException Something wrong with the supplied parameters
     * @throws PropertyServerException logic error because classification type not recognized
     */
    public void  validateUserForAssetCreate(String                         userId,
                                            String                         entityTypeGUID,
                                            String                         entityTypeName,
                                            InstanceProperties             newProperties,
                                            List<Classification>           classifications,
                                            InstanceStatus                 instanceStatus,
                                            List<String>                   defaultZones,
                                            OMRSRepositoryHelper           repositoryHelper,
                                            String                         serviceName,
                                            String                         methodName) throws UserNotAuthorizedException,
                                                                                              InvalidParameterException,
                                                                                              PropertyServerException
    {
        if (assetSecurityConnector != null)
        {
            /*
             * Need to build a description of the asset to pass to the metadata security object.
             */
            Asset assetBeanForMetadataSecurity = new Asset();

            setupAssetBeanWithEntityProperties(assetBeanForMetadataSecurity,
                                               entityTypeGUID,
                                               entityTypeName,
                                               instanceStatus,
                                               null,
                                               newProperties,
                                               repositoryHelper.getClassificationProperties(serviceName, classifications, SECURITY_TAG_CLASSIFICATION_TYPE_NAME, methodName),
                                               repositoryHelper.getClassificationProperties(serviceName, classifications, CONFIDENTIALITY_CLASSIFICATION_TYPE_NAME, methodName),
                                               repositoryHelper.getClassificationProperties(serviceName, classifications, CONFIDENCE_CLASSIFICATION_TYPE_NAME, methodName),
                                               repositoryHelper.getClassificationProperties(serviceName, classifications, CRITICALITY_CLASSIFICATION_TYPE_NAME, methodName),
                                               repositoryHelper.getClassificationProperties(serviceName, classifications, IMPACT_CLASSIFICATION_TYPE_NAME, methodName),
                                               repositoryHelper.getClassificationProperties(serviceName, classifications, RETENTION_CLASSIFICATION_TYPE_NAME, methodName),
                                               repositoryHelper.getClassificationProperties(serviceName, classifications, OWNERSHIP_CLASSIFICATION_TYPE_NAME, methodName),
                                               repositoryHelper.getClassificationProperties(serviceName, classifications, ASSET_ZONES_CLASSIFICATION_NAME, methodName),
                                               repositoryHelper.getClassificationProperties(serviceName, classifications, ASSET_ORIGIN_CLASSIFICATION_NAME, methodName),
                                               repositoryHelper,
                                               serviceName,
                                               methodName);

            assetBeanForMetadataSecurity.setZoneMembership(this.setAssetZonesToDefault(defaultZones, assetBeanForMetadataSecurity));
            assetSecurityConnector.validateUserForAssetCreate(userId, new Asset(assetBeanForMetadataSecurity));
        }
    }


    /**
     * Validate that the user is able to perform the requested action on an attachment.  This method should be used by the other
     * handlers to verify whether the element they are working with is attached to a visible asset
     * (ie is a member of one of the supported zones) that can be operated on by the calling user.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param assetGUIDParameterName name of parameter supplying the assetGUID
     * @param assetEntity entity storing the asset's properties
     * @param suppliedSupportedZones list of supported zones from the caller.
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void validateUserForAssetRead(String               userId,
                                         String               assetGUID,
                                         String               assetGUIDParameterName,
                                         EntityDetail         assetEntity,
                                         List<String>         suppliedSupportedZones,
                                         OMRSRepositoryHelper repositoryHelper,
                                         String               serviceName,
                                         String               methodName) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        /*
         * This method will throw an exception if the asset is not in the supported zones - it will look like
         * the asset is not known.
         */
        invalidParameterHandler.validateAssetInSupportedZone(assetGUID,
                                                             assetGUIDParameterName,
                                                             suppliedSupportedZones,
                                                             this.getSupportedZones(userId, suppliedSupportedZones, serviceName),
                                                             serviceName,
                                                             methodName);

        if (assetSecurityConnector != null)
        {
            /*
             * Create the bean for the security module then call the appropriate security method.
             */
            Asset assetBean = this.getAssetBeanFromEntity(assetEntity, repositoryHelper, serviceName, methodName);

            assetSecurityConnector.validateUserForAssetRead(userId, assetBean);
        }
    }


    /**
     * Validate that the user is able to perform the requested action on an attachment.  This method should be used by the other
     * handlers to verify whether the element they are working with is attached to a visible asset
     * (ie is a member of one of the supported zones) that can be operated on by the calling user.
     *
     * @param userId calling user
     * @param assetGUID unique identifier of the asset
     * @param assetGUIDParameterName name of parameter supplying the assetGUID
     * @param assetEntity entity storing the root of the  asset
     * @param isFeedback       is this request related to a feedback element (comment, like, rating) or an attachment
     * @param isUpdate         is this an update request?
     * @param suppliedSupportedZones list of supported zones from the caller.
     * @param repositoryHelper works with OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void validateUserForAssetAttachment(String               userId,
                                               String               assetGUID,
                                               String               assetGUIDParameterName,
                                               EntityDetail         assetEntity,
                                               boolean              isFeedback,
                                               boolean              isUpdate,
                                               List<String>         suppliedSupportedZones,
                                               OMRSRepositoryHelper repositoryHelper,
                                               String               serviceName,
                                               String               methodName) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        /*
         * This method will throw an exception if the asset is not in the supported zones - it will look like
         * the asset is not known.
         */
        invalidParameterHandler.validateAssetInSupportedZone(assetGUID,
                                                             assetGUIDParameterName,
                                                             suppliedSupportedZones,
                                                             this.getSupportedZones(userId,
                                                                                    suppliedSupportedZones,
                                                                                    serviceName),
                                                             serviceName,
                                                             methodName);

        if (assetSecurityConnector != null)
        {
            Asset asset = this.getAssetBeanFromEntity(assetEntity, repositoryHelper, serviceName, methodName);

            /*
             * Now validate the security.
             */
            if (isUpdate)
            {
                if (isFeedback)
                {
                    assetSecurityConnector.validateUserForAssetFeedback(userId, asset);
                }
                else
                {
                    assetSecurityConnector.validateUserForAssetAttachmentUpdate(userId, asset);
                }
            }
            else
            {
                assetSecurityConnector.validateUserForAssetRead(userId, asset);
            }
        }
    }


    /**
     * Validate that the user is able to perform the requested action on an asset.  This method should be used by the other
     * handlers to verify whether the asset they are working with that can be operated on by the calling user.
     *
     * @param userId calling user
     * @param originalAssetEntity entity storing the current asset
     * @param updatedAssetProperties  properties after the update has completed
     * @param newInstanceStatus status of the entity once the update is complete
     * @param repositoryHelper works with OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException user not authorized to issue this request
     */
    public void validateUserForAssetUpdate(String               userId,
                                           EntityDetail         originalAssetEntity,
                                           InstanceProperties   updatedAssetProperties,
                                           InstanceStatus       newInstanceStatus,
                                           OMRSRepositoryHelper repositoryHelper,
                                           String               serviceName,
                                           String               methodName) throws UserNotAuthorizedException
    {
        if (assetSecurityConnector != null)
        {
            Asset originalAsset = this.getAssetBeanFromEntity(originalAssetEntity, repositoryHelper, serviceName, methodName);

            AssetAuditHeader assetAuditHeader = new AssetAuditHeader();
            assetAuditHeader.setCreatedBy(originalAssetEntity.getCreatedBy());
            assetAuditHeader.setCreateTime(originalAssetEntity.getCreateTime());
            assetAuditHeader.setMaintainedBy(originalAssetEntity.getMaintainedBy());
            assetAuditHeader.setUpdatedBy(originalAssetEntity.getUpdatedBy());
            assetAuditHeader.setUpdateTime(originalAssetEntity.getUpdateTime());
            assetAuditHeader.setVersion(assetAuditHeader.getVersion());

            EntityDetail updatedAssetEntity = new EntityDetail(originalAssetEntity);
            updatedAssetEntity.setProperties(updatedAssetProperties);
            updatedAssetEntity.setStatus(newInstanceStatus);

            Asset updatedAsset = this.getAssetBeanFromEntity(updatedAssetEntity, repositoryHelper, serviceName, methodName);

            assetSecurityConnector.validateUserForAssetDetailUpdate(userId, originalAsset, assetAuditHeader, updatedAsset);
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete an asset.
     *
     * @param userId calling user
     * @param assetEntity entity storing the asset's properties
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws InvalidParameterException the bean properties are invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException problem accessing the property server
     */
    public void  validateUserForAssetDelete(String               userId,
                                            EntityDetail         assetEntity,
                                            OMRSRepositoryHelper repositoryHelper,
                                            String               serviceName,
                                            String               methodName) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        if (assetSecurityConnector != null)
        {
            Asset asset = this.getAssetBeanFromEntity(assetEntity, repositoryHelper, serviceName, methodName);

            assetSecurityConnector.validateUserForAssetDelete(userId, asset);
        }
    }

    /*
     * =========================================================================================================
     * Glossary Security
     */

    /**
     * Fill in information about a glossary from an entity.  This is to pass to the Open Metadata Security verifier.
     *
     * @param entity properties to add to the bean
     * @param methodName calling method
     * @return glossary bean
     */
    private Glossary getGlossaryBeanFromEntity(EntityDetail         entity,
                                               OMRSRepositoryHelper repositoryHelper,
                                               String               serviceName,
                                               String               methodName)
    {
        if ((entity != null) && (entity.getType() != null))
        {
            Glossary glossary = new Glossary();

            String              typeId                    = entity.getType().getTypeDefGUID();
            String              typeName                  = entity.getType().getTypeDefName();
            InstanceStatus      instanceStatus            = entity.getStatus();
            String              glossaryGUID              = entity.getGUID();
            InstanceProperties  entityProperties          = entity.getProperties();
            InstanceProperties  securityTagProperties     = null;
            InstanceProperties  confidentialityProperties = null;
            InstanceProperties  confidenceProperties      = null;
            InstanceProperties  criticalityProperties     = null;
            InstanceProperties  impactProperties          = null;
            InstanceProperties  retentionProperties       = null;
            InstanceProperties  ownershipProperties       = null;

            if (entity.getClassifications() != null)
            {
                for (Classification classification : entity.getClassifications())
                {
                    if (classification != null)
                    {
                        if (SECURITY_TAG_CLASSIFICATION_TYPE_NAME.equals(classification.getName()))
                        {
                            securityTagProperties = classification.getProperties();
                        }
                        else if (CONFIDENTIALITY_CLASSIFICATION_TYPE_NAME.equals(classification.getName()))
                        {
                            confidentialityProperties = classification.getProperties();
                        }
                        else if (CONFIDENCE_CLASSIFICATION_TYPE_NAME.equals(classification.getName()))
                        {
                            confidenceProperties = classification.getProperties();
                        }
                        else if (CRITICALITY_CLASSIFICATION_TYPE_NAME.equals(classification.getName()))
                        {
                            criticalityProperties = classification.getProperties();
                        }
                        else if (IMPACT_CLASSIFICATION_TYPE_NAME.equals(classification.getName()))
                        {
                            impactProperties = classification.getProperties();
                        }
                        else if (RETENTION_CLASSIFICATION_TYPE_NAME.equals(classification.getName()))
                        {
                            retentionProperties = classification.getProperties();
                        }
                        else if (OWNERSHIP_CLASSIFICATION_TYPE_NAME.equals(classification.getName()))
                        {
                            ownershipProperties = classification.getProperties();
                        }
                    }
                }
            }

            setupGlossaryBeanWithEntityProperties(glossary,
                                                  typeId,
                                                  typeName,
                                                  instanceStatus,
                                                  glossaryGUID,
                                                  entityProperties,
                                                  securityTagProperties,
                                                  confidentialityProperties,
                                                  confidenceProperties,
                                                  criticalityProperties,
                                                  impactProperties,
                                                  retentionProperties,
                                                  ownershipProperties,
                                                  repositoryHelper,
                                                  serviceName,
                                                  methodName);
            return glossary;
        }

        return null;
    }


    /**
     * Fill in information about a glossary from an entity.  This is to pass to the Open Metadata Security verifier.
     *
     * @param glossary bean to fill out
     * @param typeId unique identifier for the type of the entity
     * @param typeName unique name for the type of the entity
     * @param instanceStatus status from the entity
     * @param assetGUID unique identifier for the entity
     * @param entityProperties properties from the entity
     * @param securityTagProperties properties from the SecurityTags classification
     * @param confidentialityProperties properties from the Confidentiality classification
     * @param confidenceProperties properties from the Confidence classification
     * @param criticalityProperties properties from the Criticality classification
     * @param impactProperties properties from the Impact classification
     * @param retentionProperties properties from the Retention classification
     * @param ownershipProperties properties from the Ownership classification
     * @param repositoryHelper for working with OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     */
    private void setupGlossaryBeanWithEntityProperties(Glossary             glossary,
                                                       String               typeId,
                                                       String               typeName,
                                                       InstanceStatus       instanceStatus,
                                                       String               assetGUID,
                                                       InstanceProperties   entityProperties,
                                                       InstanceProperties   securityTagProperties,
                                                       InstanceProperties   confidentialityProperties,
                                                       InstanceProperties   confidenceProperties,
                                                       InstanceProperties   criticalityProperties,
                                                       InstanceProperties   impactProperties,
                                                       InstanceProperties   retentionProperties,
                                                       InstanceProperties   ownershipProperties,
                                                       OMRSRepositoryHelper repositoryHelper,
                                                       String               serviceName,
                                                       String               methodName)
    {
        glossary.setTypeGUID(typeId);
        glossary.setTypeName(typeName);
        glossary.setStatus(this.getReferenceableStatus(instanceStatus));
        glossary.setGUID(assetGUID);

        InstanceProperties properties = new InstanceProperties(entityProperties);

        glossary.setQualifiedName(repositoryHelper.removeStringProperty(serviceName,
                                                                        QUALIFIED_NAME_PROPERTY_NAME,
                                                                        properties,
                                                                        methodName));
        glossary.setAdditionalProperties(repositoryHelper.removeStringMapFromProperty(serviceName,
                                                                                      ADDITIONAL_PROPERTIES_PROPERTY_NAME,
                                                                                      properties,
                                                                                      methodName));
        glossary.setDisplayName(repositoryHelper.removeStringProperty(serviceName,
                                                                      NAME_PROPERTY_NAME,
                                                                      properties,
                                                                      methodName));
        glossary.setDescription(repositoryHelper.removeStringProperty(serviceName,
                                                                      DESCRIPTION_PROPERTY_NAME,
                                                                      properties,
                                                                      methodName));

        glossary.setLanguage(repositoryHelper.removeStringProperty(serviceName,
                                                                   LANGUAGE_PROPERTY_NAME,
                                                                   properties,
                                                                   methodName));
        glossary.setUsage(repositoryHelper.removeStringProperty(serviceName,
                                                                USAGE_PROPERTY_NAME,
                                                                properties,
                                                                methodName));

        glossary.setExtendedProperties(repositoryHelper.getInstancePropertiesAsMap(properties));

        if (securityTagProperties != null)
        {
            glossary.setSecurityLabels(repositoryHelper.getStringArrayProperty(serviceName,
                                                                               SECURITY_LABELS_PROPERTY_NAME,
                                                                               securityTagProperties,
                                                                               methodName));
            glossary.setSecurityProperties(repositoryHelper.getMapFromProperty(serviceName,
                                                                               SECURITY_PROPERTIES_PROPERTY_NAME,
                                                                               securityTagProperties,
                                                                               methodName));
            glossary.setAccessGroups(repositoryHelper.getStringArrayStringMapFromProperty(serviceName,
                                                                                          ACCESS_GROUPS_PROPERTY_NAME,
                                                                                          securityTagProperties,
                                                                                          methodName));
        }

        if (confidentialityProperties != null)
        {
            ConfidentialityGovernanceClassification classification = new ConfidentialityGovernanceClassification();

            classification.setStatus(repositoryHelper.getIntProperty(serviceName,
                                                                     STATUS_IDENTIFIER_PROPERTY_NAME,
                                                                     confidentialityProperties,
                                                                     methodName));
            classification.setConfidence(repositoryHelper.getIntProperty(serviceName,
                                                                         GOVERNANCE_CLASSIFICATION_CONFIDENCE_PROPERTY_NAME,
                                                                         confidentialityProperties,
                                                                         methodName));
            classification.setSteward(repositoryHelper.getStringProperty(serviceName,
                                                                         GOVERNANCE_CLASSIFICATION_STEWARD_PROPERTY_NAME,
                                                                         confidentialityProperties,
                                                                         methodName));
            classification.setSource(repositoryHelper.getStringProperty(serviceName,
                                                                        GOVERNANCE_CLASSIFICATION_SOURCE_PROPERTY_NAME,
                                                                        confidentialityProperties,
                                                                        methodName));
            classification.setNotes(repositoryHelper.getStringProperty(serviceName,
                                                                       GOVERNANCE_CLASSIFICATION_NOTES_PROPERTY_NAME,
                                                                       confidentialityProperties,
                                                                       methodName));
            classification.setConfidentialityLevel(repositoryHelper.getIntProperty(serviceName,
                                                                                   LEVEL_IDENTIFIER_PROPERTY_NAME,
                                                                                   confidentialityProperties,
                                                                                   methodName));
        }

        if (confidenceProperties != null)
        {
            ConfidenceGovernanceClassification classification = new ConfidenceGovernanceClassification();

            classification.setStatus(repositoryHelper.getIntProperty(serviceName,
                                                                     STATUS_IDENTIFIER_PROPERTY_NAME,
                                                                     confidenceProperties,
                                                                     methodName));
            classification.setConfidence(repositoryHelper.getIntProperty(serviceName,
                                                                         GOVERNANCE_CLASSIFICATION_CONFIDENCE_PROPERTY_NAME,
                                                                         confidenceProperties,
                                                                         methodName));
            classification.setSteward(repositoryHelper.getStringProperty(serviceName,
                                                                         GOVERNANCE_CLASSIFICATION_STEWARD_PROPERTY_NAME,
                                                                         confidenceProperties,
                                                                         methodName));
            classification.setSource(repositoryHelper.getStringProperty(serviceName,
                                                                        GOVERNANCE_CLASSIFICATION_SOURCE_PROPERTY_NAME,
                                                                        confidenceProperties,
                                                                        methodName));
            classification.setNotes(repositoryHelper.getStringProperty(serviceName,
                                                                       GOVERNANCE_CLASSIFICATION_NOTES_PROPERTY_NAME,
                                                                       confidenceProperties,
                                                                       methodName));
            classification.setConfidenceLevel(repositoryHelper.getIntProperty(serviceName,
                                                                              LEVEL_IDENTIFIER_PROPERTY_NAME,
                                                                              confidenceProperties,
                                                                              methodName));
        }

        if (criticalityProperties != null)
        {
            CriticalityGovernanceClassification classification = new CriticalityGovernanceClassification();

            classification.setStatus(repositoryHelper.getIntProperty(serviceName,
                                                                     STATUS_IDENTIFIER_PROPERTY_NAME,
                                                                     criticalityProperties,
                                                                     methodName));
            classification.setConfidence(repositoryHelper.getIntProperty(serviceName,
                                                                         GOVERNANCE_CLASSIFICATION_CONFIDENCE_PROPERTY_NAME,
                                                                         criticalityProperties,
                                                                         methodName));
            classification.setSteward(repositoryHelper.getStringProperty(serviceName,
                                                                         GOVERNANCE_CLASSIFICATION_STEWARD_PROPERTY_NAME,
                                                                         criticalityProperties,
                                                                         methodName));
            classification.setSource(repositoryHelper.getStringProperty(serviceName,
                                                                        GOVERNANCE_CLASSIFICATION_SOURCE_PROPERTY_NAME,
                                                                        criticalityProperties,
                                                                        methodName));
            classification.setNotes(repositoryHelper.getStringProperty(serviceName,
                                                                       GOVERNANCE_CLASSIFICATION_NOTES_PROPERTY_NAME,
                                                                       criticalityProperties,
                                                                       methodName));
            classification.setCriticalityLevel(repositoryHelper.getIntProperty(serviceName,
                                                                               LEVEL_IDENTIFIER_PROPERTY_NAME,
                                                                               criticalityProperties,
                                                                               methodName));
        }

        if (impactProperties != null)
        {
            ImpactGovernanceClassification classification = new ImpactGovernanceClassification();

            classification.setStatus(repositoryHelper.getIntProperty(serviceName,
                                                                     STATUS_IDENTIFIER_PROPERTY_NAME,
                                                                     impactProperties,
                                                                     methodName));
            classification.setConfidence(repositoryHelper.getIntProperty(serviceName,
                                                                         GOVERNANCE_CLASSIFICATION_CONFIDENCE_PROPERTY_NAME,
                                                                         impactProperties,
                                                                         methodName));
            classification.setSteward(repositoryHelper.getStringProperty(serviceName,
                                                                         GOVERNANCE_CLASSIFICATION_STEWARD_PROPERTY_NAME,
                                                                         impactProperties,
                                                                         methodName));
            classification.setSource(repositoryHelper.getStringProperty(serviceName,
                                                                        GOVERNANCE_CLASSIFICATION_SOURCE_PROPERTY_NAME,
                                                                        impactProperties,
                                                                        methodName));
            classification.setNotes(repositoryHelper.getStringProperty(serviceName,
                                                                       GOVERNANCE_CLASSIFICATION_NOTES_PROPERTY_NAME,
                                                                       impactProperties,
                                                                       methodName));
            classification.setImpactSeverityLevel(repositoryHelper.getIntProperty(serviceName,
                                                                                  SEVERITY_LEVEL_IDENTIFIER_PROPERTY_NAME,
                                                                                  impactProperties,
                                                                                  methodName));
        }

        if (retentionProperties != null)
        {
            RetentionGovernanceClassification classification = new RetentionGovernanceClassification();

            classification.setStatus(repositoryHelper.getIntProperty(serviceName,
                                                                     STATUS_IDENTIFIER_PROPERTY_NAME,
                                                                     retentionProperties,
                                                                     methodName));
            classification.setConfidence(repositoryHelper.getIntProperty(serviceName,
                                                                         GOVERNANCE_CLASSIFICATION_CONFIDENCE_PROPERTY_NAME,
                                                                         retentionProperties,
                                                                         methodName));
            classification.setSteward(repositoryHelper.getStringProperty(serviceName,
                                                                         GOVERNANCE_CLASSIFICATION_STEWARD_PROPERTY_NAME,
                                                                         retentionProperties,
                                                                         methodName));
            classification.setSource(repositoryHelper.getStringProperty(serviceName,
                                                                        GOVERNANCE_CLASSIFICATION_SOURCE_PROPERTY_NAME,
                                                                        retentionProperties,
                                                                        methodName));
            classification.setNotes(repositoryHelper.getStringProperty(serviceName,
                                                                       GOVERNANCE_CLASSIFICATION_NOTES_PROPERTY_NAME,
                                                                       retentionProperties,
                                                                       methodName));
            classification.setRetentionBasis(repositoryHelper.getIntProperty(serviceName,
                                                                             BASIS_IDENTIFIER_PROPERTY_NAME,
                                                                             retentionProperties,
                                                                             methodName));
            classification.setAssociatedGUID(repositoryHelper.getStringProperty(serviceName,
                                                                                RETENTION_ASSOCIATED_GUID_PROPERTY_NAME,
                                                                                retentionProperties,
                                                                                methodName));
            classification.setArchiveAfter(repositoryHelper.getDateProperty(serviceName,
                                                                            RETENTION_ARCHIVE_AFTER_PROPERTY_NAME,
                                                                            retentionProperties,
                                                                            methodName));
            classification.setDeleteAfter(repositoryHelper.getDateProperty(serviceName,
                                                                           RETENTION_DELETE_AFTER_PROPERTY_NAME,
                                                                           retentionProperties,
                                                                           methodName));
        }

        if (ownershipProperties != null)
        {
            glossary.setOwner(repositoryHelper.getStringProperty(serviceName,
                                                                 OWNER_PROPERTY_NAME,
                                                                 ownershipProperties,
                                                                 methodName));
            glossary.setOwnerType(repositoryHelper.getEnumPropertyOrdinal(serviceName,
                                                                          OWNER_TYPE_PROPERTY_NAME,
                                                                          ownershipProperties,
                                                                          methodName));
        }
    }


    /**
     * Tests for whether a specific user should have the right to create a glossary.
     *
     * @param userId identifier of user
     * @param entityTypeGUID unique identifier of the type of entity to create
     * @param entityTypeName unique name of the type of entity to create
     * @param newProperties properties for new entity
     * @param classifications classifications for new entity
     * @param instanceStatus status for new entity
     * @param repositoryHelper manipulates repository service objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to perform this command
     */
    public void  validateUserForGlossaryCreate(String                         userId,
                                               String                         entityTypeGUID,
                                               String                         entityTypeName,
                                               InstanceProperties             newProperties,
                                               List<Classification>           classifications,
                                               InstanceStatus                 instanceStatus,
                                               OMRSRepositoryHelper           repositoryHelper,
                                               String                         serviceName,
                                               String                         methodName) throws UserNotAuthorizedException
    {
        if (glossarySecurityConnector != null)
        {
            /*
             * Need to build a description of the glossary to pass to the metadata security object.
             */
            Glossary glossaryBeanForMetadataSecurity = new Glossary();

            setupGlossaryBeanWithEntityProperties(glossaryBeanForMetadataSecurity,
                                                  entityTypeGUID,
                                                  entityTypeName,
                                                  instanceStatus,
                                                  null,
                                                  newProperties,
                                                  repositoryHelper.getClassificationProperties(serviceName, classifications, SECURITY_TAG_CLASSIFICATION_TYPE_NAME, methodName),
                                                  repositoryHelper.getClassificationProperties(serviceName, classifications, CONFIDENTIALITY_CLASSIFICATION_TYPE_NAME, methodName),
                                                  repositoryHelper.getClassificationProperties(serviceName, classifications, CONFIDENCE_CLASSIFICATION_TYPE_NAME, methodName),
                                                  repositoryHelper.getClassificationProperties(serviceName, classifications, CRITICALITY_CLASSIFICATION_TYPE_NAME, methodName),
                                                  repositoryHelper.getClassificationProperties(serviceName, classifications, IMPACT_CLASSIFICATION_TYPE_NAME, methodName),
                                                  repositoryHelper.getClassificationProperties(serviceName, classifications, RETENTION_CLASSIFICATION_TYPE_NAME, methodName),
                                                  repositoryHelper.getClassificationProperties(serviceName, classifications, OWNERSHIP_CLASSIFICATION_TYPE_NAME, methodName),
                                                  repositoryHelper,
                                                  serviceName,
                                                  methodName);

            glossarySecurityConnector.validateUserForGlossaryCreate(userId, new Glossary(glossaryBeanForMetadataSecurity));
        }
    }


    /**
     * Tests for whether a specific user should have read access to a specific glossary and its contents.
     *
     * @param userId calling user
     * @param entity entity storing the glossary's properties
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException user not authorized to issue this request
     */
    public void validateUserForGlossaryRead(String               userId,
                                            EntityDetail         entity,
                                            OMRSRepositoryHelper repositoryHelper,
                                            String               serviceName,
                                            String               methodName) throws UserNotAuthorizedException
    {
        if (glossarySecurityConnector != null)
        {
            /*
             * Create the bean for the security module then call the appropriate security method.
             */
            Glossary glossary = this.getGlossaryBeanFromEntity(entity, repositoryHelper, serviceName, methodName);

            glossarySecurityConnector.validateUserForGlossaryRead(userId, glossary);
        }
    }


    /**
     * Tests for whether a specific user should have the right to update the properties of a glossary.
     *
     * @param userId identifier of user
     * @param originalEntity original glossary details
     * @param newEntityProperties new glossary details
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this glossary
     */
    public void  validateUserForGlossaryDetailUpdate(String               userId,
                                                     EntityDetail         originalEntity,
                                                     InstanceProperties   newEntityProperties,
                                                     OMRSRepositoryHelper repositoryHelper,
                                                     String               serviceName,
                                                     String               methodName) throws UserNotAuthorizedException
    {
        if (glossarySecurityConnector != null)
        {
            /*
             * Create the bean for the security module then call the appropriate security method.
             */
            Glossary originalGlossary = this.getGlossaryBeanFromEntity(originalEntity, repositoryHelper, serviceName, methodName);

            EntityDetail updatedGlossaryEntity = new EntityDetail(originalEntity);
            updatedGlossaryEntity.setProperties(newEntityProperties);
            Glossary newGlossary = this.getGlossaryBeanFromEntity(updatedGlossaryEntity, repositoryHelper, serviceName, methodName);

            glossarySecurityConnector.validateUserForGlossaryDetailUpdate(userId, originalGlossary, newGlossary);
        }
    }


    /**
     * Tests for whether a specific user should have the right to update elements attached directly
     * to a glossary such as glossary terms and categories.  These updates could be to their properties,
     * classifications and relationships.  It also includes attaching valid values but not semantic assignments
     * since they are considered updates to the associated asset.
     *
     * @param userId identifier of user
     * @param entity glossary details
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this glossary
     */
    public void  validateUserForGlossaryMemberUpdate(String               userId,
                                                     EntityDetail         entity,
                                                     OMRSRepositoryHelper repositoryHelper,
                                                     String               serviceName,
                                                     String               methodName) throws UserNotAuthorizedException
    {
        if (glossarySecurityConnector != null)
        {
            /*
             * Create the bean for the security module then call the appropriate security method.
             */
            Glossary glossary = this.getGlossaryBeanFromEntity(entity, repositoryHelper, serviceName, methodName);

            glossarySecurityConnector.validateUserForGlossaryMemberUpdate(userId, glossary);
        }
    }


    /**
     * Tests for whether a specific user should have the right to update the instance status of a term
     * anchored in a glossary.
     *
     * @param userId identifier of user
     * @param entity glossary details
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this glossary
     */
    public void  validateUserForGlossaryMemberStatusUpdate(String               userId,
                                                           EntityDetail         entity,
                                                           OMRSRepositoryHelper repositoryHelper,
                                                           String               serviceName,
                                                           String               methodName) throws UserNotAuthorizedException
    {
        if (glossarySecurityConnector != null)
        {
            /*
             * Create the bean for the security module then call the appropriate security method.
             */
            Glossary glossary = this.getGlossaryBeanFromEntity(entity, repositoryHelper, serviceName, methodName);

            glossarySecurityConnector.validateUserForGlossaryMemberStatusUpdate(userId, glossary);
        }
    }


    /**
     * Tests for whether a specific user should have the right to attach feedback - such as comments,
     * ratings, tags and likes, to the glossary.
     *
     * @param userId identifier of user
     * @param entity original glossary details
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this glossary
     */
    public void  validateUserForGlossaryFeedback(String               userId,
                                                 EntityDetail         entity,
                                                 OMRSRepositoryHelper repositoryHelper,
                                                 String               serviceName,
                                                 String               methodName) throws UserNotAuthorizedException
    {
        if (glossarySecurityConnector != null)
        {
            /*
             * Create the bean for the security module then call the appropriate security method.
             */
            Glossary glossary = this.getGlossaryBeanFromEntity(entity, repositoryHelper, serviceName, methodName);

            glossarySecurityConnector.validateUserForGlossaryFeedback(userId, glossary);
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete a glossary and all of its contents.
     *
     * @param userId identifier of user
     * @param entity original glossary details
     * @param repositoryHelper helper for OMRS objects
     * @param serviceName calling service
     * @param methodName calling method
     * @throws UserNotAuthorizedException the user is not authorized to change this glossary
     */
    public void  validateUserForGlossaryDelete(String               userId,
                                               EntityDetail         entity,
                                               OMRSRepositoryHelper repositoryHelper,
                                               String               serviceName,
                                               String               methodName) throws UserNotAuthorizedException
    {
        if (glossarySecurityConnector != null)
        {
            /*
             * Create the bean for the security module then call the appropriate security method.
             */
            Glossary glossary = this.getGlossaryBeanFromEntity(entity, repositoryHelper, serviceName, methodName);

            glossarySecurityConnector.validateUserForGlossaryDelete(userId, glossary);
        }
    }


    /*
     * =========================================================================================================
     * Type security
     */

    /**
     * Tests for whether a specific user should have the right to create a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef type details
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeCreate(String  userId,
                                           String  metadataCollectionName,
                                           TypeDef typeDef) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForTypeCreate(userId, metadataCollectionName, typeDef);
        }
    }


    /**
     * Tests for whether a specific user should have the right to create a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param attributeTypeDef type details
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeCreate(String           userId,
                                           String           metadataCollectionName,
                                           AttributeTypeDef attributeTypeDef) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForTypeCreate(userId, metadataCollectionName, attributeTypeDef);
        }
    }


    /**
     * Tests for whether a specific user should have read access to a specific type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef type details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve types
     */
    @Override
    public void  validateUserForTypeRead(String     userId,
                                         String     metadataCollectionName,
                                         TypeDef    typeDef) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForTypeRead(userId, metadataCollectionName, typeDef);
        }
    }


    /**
     * Tests for whether a specific user should have read access to a specific type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param attributeTypeDef type details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve types
     */
    @Override
    public void  validateUserForTypeRead(String              userId,
                                         String              metadataCollectionName,
                                         AttributeTypeDef    attributeTypeDef) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForTypeRead(userId, metadataCollectionName, attributeTypeDef);
        }
    }


    /**
     * Tests for whether a specific user should have the right to update a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef type details
     * @param patch changes to the type
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeUpdate(String       userId,
                                           String       metadataCollectionName,
                                           TypeDef      typeDef,
                                           TypeDefPatch patch) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForTypeUpdate(userId, metadataCollectionName, typeDef, patch);
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param typeDef type details
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeDelete(String     userId,
                                           String     metadataCollectionName,
                                           TypeDef    typeDef) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForTypeDelete(userId, metadataCollectionName, typeDef);
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param attributeTypeDef type details
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeDelete(String              userId,
                                           String              metadataCollectionName,
                                           AttributeTypeDef    attributeTypeDef) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForTypeDelete(userId, metadataCollectionName, attributeTypeDef);
        }
    }


    /**
     * Tests for whether a specific user should have the right to change the identifiers for a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param originalTypeDef type details
     * @param newTypeDefGUID the new identifier for the type.
     * @param newTypeDefName new name for this type.
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeReIdentify(String  userId,
                                               String  metadataCollectionName,
                                               TypeDef originalTypeDef,
                                               String  newTypeDefGUID,
                                               String  newTypeDefName) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForTypeReIdentify(userId, metadataCollectionName, originalTypeDef, newTypeDefGUID, newTypeDefName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to change the identifiers for a type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param originalAttributeTypeDef type details
     * @param newTypeDefGUID the new identifier for the type.
     * @param newTypeDefName new name for this type.
     * @throws UserNotAuthorizedException the user is not authorized to maintain types
     */
    @Override
    public void  validateUserForTypeReIdentify(String           userId,
                                               String           metadataCollectionName,
                                               AttributeTypeDef originalAttributeTypeDef,
                                               String           newTypeDefGUID,
                                               String           newTypeDefName) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForTypeReIdentify(userId,
                                                                      metadataCollectionName,
                                                                      originalAttributeTypeDef,
                                                                      newTypeDefGUID,
                                                                      newTypeDefName);
        }
    }


    /*
     * =========================================================================================================
     * Instance Security
     *
     * No specific security checks made when instances are written and retrieved from the local repository.
     * The methods override the super class that throws a user not authorized exception on all access/update
     * requests.
     */

    /**
     * Tests for whether a specific user should have the right to create an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param entityTypeGUID unique identifier (guid) for the new entity's type.
     * @param initialProperties initial list of properties for the new entity null means no properties.
     * @param initialClassifications initial list of classifications for the new entity null means no classifications.
     * @param initialStatus initial status typically DRAFT, PREPARED or ACTIVE.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityCreate(String                     userId,
                                             String                     metadataCollectionName,
                                             String                     entityTypeGUID,
                                             InstanceProperties         initialProperties,
                                             List<Classification>       initialClassifications,
                                             InstanceStatus             initialStatus) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityCreate(userId,
                                                                    metadataCollectionName,
                                                                    entityTypeGUID,
                                                                    initialProperties,
                                                                    initialClassifications,
                                                                    initialStatus);
        }
    }


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @return entity to return (maybe altered by the connector)
     * @throws UserNotAuthorizedException the user is not authorized to retrieve instances
     */
    @Override
    public EntityDetail  validateUserForEntityRead(String          userId,
                                                   String          metadataCollectionName,
                                                   EntityDetail    instance) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            return repositorySecurityConnector.validateUserForEntityRead(userId, metadataCollectionName, new EntityDetail(instance));
        }

        return instance;
    }


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve instances
     */
    @Override
    public void  validateUserForEntitySummaryRead(String        userId,
                                                  String        metadataCollectionName,
                                                  EntitySummary instance) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntitySummaryRead(userId, metadataCollectionName, new EntitySummary(instance));
        }
    }


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve instances
     */
    @Override
    public void  validateUserForEntityProxyRead(String      userId,
                                                String      metadataCollectionName,
                                                EntityProxy instance) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityProxyRead(userId, metadataCollectionName, new EntityProxy(instance));
        }
    }


    /**
     * Tests for whether a specific user should have the right to update an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityUpdate(String          userId,
                                             String          metadataCollectionName,
                                             EntityDetail    instance) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityUpdate(userId, metadataCollectionName, new EntityDetail(instance));
        }
    }


    /**
     * Tests for whether a specific user should have the right to add a classification to an entity instance
     * within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param classificationName String name for the classification.
     * @param properties list of properties for the classification.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityClassificationAdd(String               userId,
                                                        String               metadataCollectionName,
                                                        EntitySummary        instance,
                                                        String               classificationName,
                                                        InstanceProperties   properties) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityClassificationAdd(userId,
                                                                               metadataCollectionName,
                                                                               instance,
                                                                               classificationName,
                                                                               properties);
        }
    }


    /**
     * Tests for whether a specific user should have the right to update the classification for an entity instance
     * within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param classificationName String name for the classification.
     * @param properties list of properties for the classification.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityClassificationUpdate(String               userId,
                                                           String               metadataCollectionName,
                                                           EntitySummary        instance,
                                                           String               classificationName,
                                                           InstanceProperties   properties) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityClassificationUpdate(userId,
                                                                                  metadataCollectionName,
                                                                                  instance,
                                                                                  classificationName,
                                                                                  properties);
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete a classification from an entity instance
     * within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param classificationName String name for the classification.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityClassificationDelete(String               userId,
                                                           String               metadataCollectionName,
                                                           EntitySummary        instance,
                                                           String               classificationName) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityClassificationDelete(userId,
                                                                                  metadataCollectionName,
                                                                                  instance,
                                                                                  classificationName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityDelete(String       userId,
                                             String       metadataCollectionName,
                                             EntityDetail instance) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityDelete(userId, metadataCollectionName, new EntityDetail(instance));
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param deletedEntityGUID String unique identifier (guid) for the entity.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityRestore(String       userId,
                                              String       metadataCollectionName,
                                              String       deletedEntityGUID) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityRestore(userId, metadataCollectionName, deletedEntityGUID);
        }
    }


    /**
     * Tests for whether a specific user should have the right to change the guid on an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newGUID the new guid for the instance.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityReIdentification(String       userId,
                                                       String       metadataCollectionName,
                                                       EntityDetail instance,
                                                       String       newGUID) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityReIdentification(userId, metadataCollectionName, instance, newGUID);
        }
    }


    /**
     * Tests for whether a specific user should have the right to change an instance's type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newTypeDefSummary details of this instance's new TypeDef.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityReTyping(String         userId,
                                               String         metadataCollectionName,
                                               EntityDetail   instance,
                                               TypeDefSummary newTypeDefSummary) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityReTyping(userId, metadataCollectionName, instance, newTypeDefSummary);
        }
    }


    /**
     * Tests for whether a specific user should have the right to change the home of an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newHomeMetadataCollectionId unique identifier for the new home metadata collection/repository.
     * @param newHomeMetadataCollectionName display name for the new home metadata collection/repository.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForEntityReHoming(String         userId,
                                               String         metadataCollectionName,
                                               EntityDetail   instance,
                                               String         newHomeMetadataCollectionId,
                                               String         newHomeMetadataCollectionName) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityReHoming(userId,
                                                    metadataCollectionName,
                                                    instance,
                                                    newHomeMetadataCollectionId,
                                                    newHomeMetadataCollectionName);
        }
    }


    /**
     * Tests for whether a specific user should have the right to create an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param relationshipTypeGUID unique identifier (guid) for the new relationship's type.
     * @param initialProperties initial list of properties for the new entity null means no properties.
     * @param entityOneSummary the unique identifier of one of the entities that the relationship is connecting together.
     * @param entityTwoSummary the unique identifier of the other entity that the relationship is connecting together.
     * @param initialStatus initial status typically DRAFT, PREPARED or ACTIVE.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipCreate(String               userId,
                                                   String               metadataCollectionName,
                                                   String               relationshipTypeGUID,
                                                   InstanceProperties   initialProperties,
                                                   EntitySummary        entityOneSummary,
                                                   EntitySummary        entityTwoSummary,
                                                   InstanceStatus       initialStatus) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForRelationshipCreate(userId,
                                                        metadataCollectionName,
                                                        relationshipTypeGUID,
                                                        initialProperties,
                                                        entityOneSummary,
                                                        entityTwoSummary,
                                                        initialStatus);
        }
    }


    /**
     * Tests for whether a specific user should have read access to a specific instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to retrieve instances
     */
    @Override
    public Relationship  validateUserForRelationshipRead(String          userId,
                                                         String          metadataCollectionName,
                                                         Relationship    instance) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            return repositorySecurityConnector.validateUserForRelationshipRead(userId, metadataCollectionName, new Relationship(instance));
        }

        return instance;
    }


    /**
     * Tests for whether a specific user should have the right to update an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipUpdate(String          userId,
                                                   String          metadataCollectionName,
                                                   Relationship    instance) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForRelationshipUpdate(userId, metadataCollectionName, new Relationship(instance));
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipDelete(String       userId,
                                                   String       metadataCollectionName,
                                                   Relationship instance) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForRelationshipDelete(userId, metadataCollectionName, new Relationship(instance));
        }
    }


    /**
     * Tests for whether a specific user should have the right to delete an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param deletedRelationshipGUID String unique identifier (guid) for the relationship.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipRestore(String       userId,
                                                    String       metadataCollectionName,
                                                    String       deletedRelationshipGUID) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForEntityRestore(userId, metadataCollectionName, deletedRelationshipGUID);
        }
    }


    /**
     * Tests for whether a specific user should have the right to change the guid on an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newGUID the new guid for the instance.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipReIdentification(String       userId,
                                                             String       metadataCollectionName,
                                                             Relationship instance,
                                                             String       newGUID) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForRelationshipReIdentification(userId, metadataCollectionName, instance, newGUID);
        }
    }


    /**
     * Tests for whether a specific user should have the right to change an instance's type within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newTypeDefSummary details of this instance's new TypeDef.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipReTyping(String         userId,
                                                     String         metadataCollectionName,
                                                     Relationship   instance,
                                                     TypeDefSummary newTypeDefSummary) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForRelationshipReTyping(userId, metadataCollectionName, instance, newTypeDefSummary);
        }
    }


    /**
     * Tests for whether a specific user should have the right to change the home of an instance within a repository.
     *
     * @param userId identifier of user
     * @param metadataCollectionName configurable name of the metadata collection
     * @param instance instance details
     * @param newHomeMetadataCollectionId unique identifier for the new home metadata collection/repository.
     * @param newHomeMetadataCollectionName display name for the new home metadata collection/repository.
     * @throws UserNotAuthorizedException the user is not authorized to maintain instances
     */
    @Override
    public void  validateUserForRelationshipReHoming(String         userId,
                                                     String         metadataCollectionName,
                                                     Relationship   instance,
                                                     String         newHomeMetadataCollectionId,
                                                     String         newHomeMetadataCollectionName) throws UserNotAuthorizedException
    {
        if (repositorySecurityConnector != null)
        {
            repositorySecurityConnector.validateUserForRelationshipReHoming(userId,
                                                          metadataCollectionName,
                                                          instance,
                                                          newHomeMetadataCollectionId,
                                                          newHomeMetadataCollectionName);
        }
    }


    /**
     * Tests for whether a reference copy should be saved to the repository.
     *
     * @param instance instance details
     * @return flag indicating whether the reference copy should be saved
     */
    public boolean  validateEntityReferenceCopySave(EntityDetail   instance)
    {
        if (repositorySecurityConnector != null)
        {
            return repositorySecurityConnector.validateEntityReferenceCopySave(instance);
        }

        return true;
    }


    /**
     * Tests for whether a reference copy should be saved to the repository.
     *
     * @param instance instance details
     * @return flag indicating whether the reference copy should be saved
     */
    public boolean  validateRelationshipReferenceCopySave(Relationship   instance)
    {
        if (repositorySecurityConnector != null)
        {
            return repositorySecurityConnector.validateRelationshipReferenceCopySave(instance);
        }

        return true;
    }


    /**
     * Validate whether an event received from another member of the cohort should be processed
     * by this server.
     *
     * @param cohortName name of the cohort
     * @param event event that has been received
     * @return inbound event to process (maybe updated) or null to indicate that the event should be ignored
     */
    public OMRSInstanceEvent validateInboundEvent(String            cohortName,
                                                  OMRSInstanceEvent event)
    {
        if (eventsSecurityConnector != null)
        {
            return eventsSecurityConnector.validateInboundEvent(cohortName, event);
        }

        return event;
    }


    /**
     * Validate whether an event should be sent to the other members of the cohort by this server.
     *
     * @param cohortName name of the cohort
     * @param event event that has been received
     * @return outbound event to send (maybe updated) or null to indicate that the event should be ignored
     */
    public OMRSInstanceEvent validateOutboundEvent(String            cohortName,
                                                   OMRSInstanceEvent event)
    {
        if (eventsSecurityConnector != null)
        {
            return eventsSecurityConnector.validateOutboundEvent(cohortName, event);
        }

        return event;
    }
}
