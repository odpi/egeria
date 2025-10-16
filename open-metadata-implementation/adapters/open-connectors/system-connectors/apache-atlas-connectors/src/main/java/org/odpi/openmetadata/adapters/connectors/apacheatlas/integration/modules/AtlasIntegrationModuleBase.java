/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.modules;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.*;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataElementSummary;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.RelatedMetadataHierarchySummary;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataAssetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataStoreProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.ProcessProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers.ExternalIdLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers.ExternalIdProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.OwnershipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.enums.KeyPattern;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.ffdc.AtlasIntegrationAuditCode;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.ffdc.AtlasIntegrationErrorCode;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.ApacheAtlasRESTConnector;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties.AtlasEntity;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties.AtlasEntityHeader;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties.AtlasEntityWithExtInfo;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties.AtlasGlossaryBaseProperties;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties.AtlasInstanceStatus;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties.AtlasObjectId;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties.AtlasRelationship;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.TypeEmbeddedAttributeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AtlasIntegrationModuleBase defines the interface that classes that support the synchronization of particular types of metadata with Apache Atlas.
 */
public abstract class AtlasIntegrationModuleBase
{
    /*
     * These definitions help to extract information from the Apache Atlas beans.
     */
    protected final static String openMetadataCorrelationTypeName     = "OpenMetadataCorrelation";
    protected final static String openMetadataCorrelationLinkTypeName = "OpenMetadataCorrelationLink";
    protected final static String openMetadataGlossaryCorrelationLinkTypeName = "OpenMetadataGlossaryCorrelationLink";
    protected final static String openMetadataCorrelationPropertyName = "openMetadataCorrelation";
    protected final static String openMetadataAssociatedElementPropertyName = "associatedElement";
    protected final static String openMetadataAssociatedGlossaryPropertyName = "associatedMeaning";
    protected final static String egeriaGUIDPropertyName  = "egeriaGUID";
    protected final static String egeriaTypeNamePropertyName  = "egeriaTypeName";
    protected final static String egeriaQualifiedNamePropertyName  = "egeriaQualifiedName";
    protected final static String egeriaOwnedPropertyName = "egeriaOwned";

    protected final static String atlasOwnerPropertyName  = "owner";
    protected final static String egeriaOwnerTypeName     = "UserIdentity";
    protected final static String egeriaOwnerPropertyName = "userId";

    protected final static String atlasQualifiedNamePropertyName   = "qualifiedName";
    protected final static String atlasNamePropertyName            = "name";
    protected final static String atlasDisplayNamePropertyName     = "displayName";
    protected final static String atlasDescriptionPropertyName     = "description";
    protected final static String atlasUserDescriptionPropertyName = "userDescription";
    protected final static String atlasPathPropertyName            = "path";
    protected final static String atlasCreateTimePropertyName      = "createTime";
    protected final static String atlasModifiedTimePropertyName    = "modifiedTime";

    protected final static List<String> atlasAssetProperties = Arrays.asList(atlasQualifiedNamePropertyName, atlasNamePropertyName,
                                                                             atlasDisplayNamePropertyName, atlasDescriptionPropertyName,
                                                                             atlasUserDescriptionPropertyName);
    protected final static List<String> atlasDataFileProperties = Arrays.asList(atlasQualifiedNamePropertyName, atlasNamePropertyName,
                                                                                atlasDisplayNamePropertyName, atlasDescriptionPropertyName,
                                                                                atlasUserDescriptionPropertyName, atlasCreateTimePropertyName,
                                                                                atlasModifiedTimePropertyName, atlasPathPropertyName);


    private final static String atlasGUIDPropertyName         = "atlasGUID";
    private final static String atlasNameMappingPropertyName  = "atlasName";
    private final static String egeriaNameMappingPropertyName = "lastKnownEgeriaDisplayName";
    private final static String modulePropertyName            = "originator";

    protected final AuditLog                 auditLog;
    protected final String                   connectorName;
    protected final String                   moduleName;
    protected final Connection               connectionDetails;
    protected final IntegrationContext       myContext;
    protected final List<Connector>          embeddedConnectors;
    protected final ApacheAtlasRESTConnector atlasClient;
    protected final String                   targetRootURL;

    protected final AssetClient                 dataAssetClient;
    protected final SchemaTypeClient            schemaTypeClient;
    protected final SchemaAttributeClient       schemaAttributeClient;
    protected final CollectionClient            glossaryClient;
    protected final GlossaryTermClient          glossaryTermClient;
    protected final InformalTagClient           informalTagClient;
    protected final ClassificationManagerClient classificationManagerClient;
    protected final ExternalIdClient            externalIdClient;
    protected final OpenMetadataStore           openMetadataStore;



    /**
     * Initialize the common properties for an integration module.
     *
     * @param connectorName name of this connector
     * @param moduleName name of this module
     * @param connectionDetails supplied connector used to configure the connector
     * @param auditLog logging destination
     * @param myContext integration context assigned to the connector
     * @param targetRootURL host name and port of Apache Atlas
     * @param atlasClient client to call Apache Atlas
     * @param embeddedConnectors any  connectors embedded in the connector (such as the secrets connector or Kafka connector)
     * @throws UserNotAuthorizedException the data asset service has not been enabled.
     */
    public AtlasIntegrationModuleBase(String                   connectorName,
                                      String                   moduleName,
                                      Connection               connectionDetails,
                                      AuditLog                 auditLog,
                                      IntegrationContext       myContext,
                                      String                   targetRootURL,
                                      ApacheAtlasRESTConnector atlasClient,
                                      List<Connector>          embeddedConnectors) throws UserNotAuthorizedException
    {
        this.auditLog           = auditLog;
        this.connectorName      = connectorName;
        this.moduleName         = moduleName;
        this.connectionDetails  = connectionDetails;
        this.myContext          = myContext;
        this.targetRootURL      = targetRootURL;
        this.atlasClient        = atlasClient;
        this.embeddedConnectors = embeddedConnectors;

        this.dataAssetClient             = myContext.getAssetClient(OpenMetadataType.DATA_ASSET.typeName);
        this.schemaTypeClient            = myContext.getSchemaTypeClient();
        this.schemaAttributeClient       = myContext.getSchemaAttributeClient();
        this.glossaryClient              = myContext.getCollectionClient(OpenMetadataType.GLOSSARY.typeName);
        this.glossaryTermClient          = myContext.getGlossaryTermClient();
        this.classificationManagerClient = myContext.getClassificationManagerClient();
        this.informalTagClient           = myContext.getInformalTagClient();
        this.externalIdClient            = myContext.getExternalIdClient();
        this.openMetadataStore           = myContext.getOpenMetadataStore();

        /*
         * Deduplication is turned off so that the connector works with the entities it created rather than
         * entities from other systems that have been linked as duplicates.
         */
        this.dataAssetClient.setForDuplicateProcessing(true);
        this.schemaTypeClient.setForDuplicateProcessing(true);
        this.schemaAttributeClient.setForDuplicateProcessing(true);
        this.glossaryClient.setForDuplicateProcessing(true);
        this.glossaryTermClient.setForDuplicateProcessing(true);
        this.classificationManagerClient.setForDuplicateProcessing(true);
        this.informalTagClient.setForDuplicateProcessing(true);
        this.externalIdClient.setForDuplicateProcessing(true);
        this.openMetadataStore.setForDuplicateProcessing(true);
    }



    /* ================================================================
     * Determine which direction that metadata is flowing
     */

    /**
     * Return a flag indicating whether an element retrieved from the open metadata ecosystem.
     *
     * @param elementHeader header from open metadata ecosystem
     * @return boolean flag
     */
    protected boolean isAtlasOwnedElement(ElementHeader elementHeader)
    {
        return (elementHeader.getOrigin().getHomeMetadataCollectionId() != null) &&
                       (elementHeader.getOrigin().getHomeMetadataCollectionId().equals(myContext.getMetadataSourceGUID()));
    }


    /**
     * Return a flag to indicate whether an atlas glossary element is owned by Egeria (open metadata ecosystem)
     * or is owned by Apache Atlas.  This becomes important around delete scenarios.
     *
     * @param properties properties of the Atlas element
     * @return boolean - true means the element originated in the open metadata ecosystem
     */
    protected boolean isEgeriaOwned(AtlasGlossaryBaseProperties properties)
    {
        Map<String, Object> additionalAttributes = properties.getAdditionalAttributes();

        return (additionalAttributes != null) &&
                       (additionalAttributes.containsKey(egeriaOwnedPropertyName)) &&
                       ("true".equals(additionalAttributes.get(egeriaOwnedPropertyName).toString()));
    }


    /**
     * Return a flag to indicate whether an atlas glossary element is owned by Egeria (open metadata ecosystem)
     * or is owned by Apache Atlas.  This becomes important around delete scenarios.
     *
     * @param atlasEntityWithExtInfo properties of the Atlas element
     * @return boolean - true means the element originated in the open metadata ecosystem
     * @throws PropertyServerException problem communicating with Apache Atlas
     */
    protected boolean isEgeriaOwned(AtlasEntityWithExtInfo atlasEntityWithExtInfo) throws PropertyServerException
    {
       if ((atlasEntityWithExtInfo != null) && (atlasEntityWithExtInfo.getEntity() != null))
       {
           return isEgeriaOwned(atlasEntityWithExtInfo.getEntity());
       }

       return false;
    }


    /**
     * Return a flag to indicate whether an atlas glossary element is owned by Egeria (open metadata ecosystem)
     * or is owned by Apache Atlas.  This becomes important around delete scenarios.
     *
     * @param atlasEntity properties of the Atlas element
     * @return boolean - true means the element originated in the open metadata ecosystem
     * @throws PropertyServerException problem communicating with Apache Atlas
     */
    protected boolean isEgeriaOwned(AtlasEntity atlasEntity) throws PropertyServerException
    {
        if (atlasEntity != null)
        {
            AtlasEntityWithExtInfo openMetadataCorrelationEntity = atlasClient.getRelatedEntity(atlasEntity,
                                                                                                openMetadataCorrelationPropertyName);

            if ((openMetadataCorrelationEntity != null) &&
                        (openMetadataCorrelationEntity.getEntity() != null) &&
                        (openMetadataCorrelationEntity.getEntity().getAttributes() != null))
            {
                return this.getAtlasBooleanProperty(openMetadataCorrelationEntity.getEntity().getAttributes(), egeriaOwnedPropertyName);
            }
        }

        return false;
    }


    /**
     * Add a new OpenMetadataCorrelation entity to apache atlas and link it to the supplied atlas entity.
     *
     * @param atlasEntityGUID unique identifier of the atlas element that the open metadata correlation entity should be connected to
     * @param egeriaEntityGUID unique identifier from the open metadata ecosystem
     * @param egeriaQualifiedName unique name from the open metadata ecosystem
     * @param egeriaTypeName name of type from the open metadata ecosystem
     * @param isAtlasGlossaryEntity is this a glossary entity?
     * @param egeriaOwned is this element owned by Egeria?
     * @throws PropertyServerException problems communicating with Apache Atlas.
     */
    protected void saveEgeriaGUIDInAtlas(String  atlasEntityGUID,
                                         String  egeriaEntityGUID,
                                         String  egeriaQualifiedName,
                                         String  egeriaTypeName,
                                         boolean isAtlasGlossaryEntity,
                                         boolean egeriaOwned) throws PropertyServerException
    {
        AtlasEntity         openMetadataCorrelationEntity           = new AtlasEntity();
        Map<String, Object> attributes                              = new HashMap<>();
        AtlasRelationship   openMetadataCorrelationLinkRelationship = new AtlasRelationship();

        openMetadataCorrelationEntity.setTypeName(openMetadataCorrelationTypeName);
        openMetadataCorrelationEntity.setIncomplete(false);
        openMetadataCorrelationEntity.setStatus(AtlasInstanceStatus.ACTIVE);
        openMetadataCorrelationEntity.setVersion(1L);

        attributes.put(egeriaGUIDPropertyName, egeriaEntityGUID);
        attributes.put(egeriaQualifiedNamePropertyName, egeriaQualifiedName);
        attributes.put(egeriaTypeNamePropertyName, egeriaTypeName);
        attributes.put(egeriaOwnedPropertyName, egeriaOwned);
        openMetadataCorrelationEntity.setAttributes(attributes);

        String openMetadataCorrelationEntityGUID = atlasClient.addEntity(openMetadataCorrelationEntity);

        if (openMetadataCorrelationEntityGUID != null)
        {
            if (isAtlasGlossaryEntity)
            {
                openMetadataCorrelationLinkRelationship.setTypeName(openMetadataGlossaryCorrelationLinkTypeName);
            }
            else
            {
                openMetadataCorrelationLinkRelationship.setTypeName(openMetadataCorrelationLinkTypeName);
            }

            AtlasObjectId end1 = new AtlasObjectId();

            end1.setGuid(atlasEntityGUID);
            openMetadataCorrelationLinkRelationship.setEnd1(end1);

            AtlasObjectId end2 = new AtlasObjectId();

            end2.setGuid(openMetadataCorrelationEntityGUID);
            openMetadataCorrelationLinkRelationship.setEnd2(end2);

            atlasClient.addRelationship(openMetadataCorrelationLinkRelationship);
        }
    }


    /**
     * Return the unique identifier for the equivalent element in Apache Atlas.
     *
     * @param metadataElement retrieved metadata element
     * @return string guid
     * @throws InvalidParameterException retrieved element is null
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    protected String getAtlasGUID(OpenMetadataRootElement metadataElement) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        final String methodName = "getAtlasGUID";

        RelatedMetadataElementSummary relatedExternalIdentifier = externalIdClient.getRelatedExternalId(metadataElement,
                                                                                                        myContext.getMetadataSourceGUID(),
                                                                                                        null);

        if ((relatedExternalIdentifier != null) &&
                (relatedExternalIdentifier.getRelatedElement().getProperties() instanceof ExternalIdProperties externalIdProperties))
        {
            return externalIdProperties.getKey();
        }

        auditLog.logMessage(methodName, AtlasIntegrationAuditCode.MISSING_ATLAS_GUID.getMessageDefinition(connectorName,
                                                                                                          metadataElement.getElementHeader().getType().getTypeName(),
                                                                                                          metadataElement.getElementHeader().getGUID(),
                                                                                                          myContext.getMetadataSourceGUID()));
        throw new InvalidParameterException(AtlasIntegrationErrorCode.MISSING_ATLAS_GUID.getMessageDefinition(connectorName,
                                                                                                              metadataElement.getElementHeader().getType().getTypeName(),
                                                                                                              metadataElement.getElementHeader().getGUID(),
                                                                                                              myContext.getMetadataSourceGUID()),
                                            this.getClass().getName(),
                                            methodName,
                                            OpenMetadataProperty.KEY.name);
    }



    /**
     * Return the unique identifier for the equivalent element in Apache Atlas.
     *
     * @param metadataElement retrieved metadata element
     * @return string guid
     * @throws InvalidParameterException retrieved element is null
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    protected String getAtlasGUID(RelatedMetadataElementSummary metadataElement) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        final String methodName = "getAtlasGUID";

        if (metadataElement instanceof RelatedMetadataHierarchySummary relatedMetadataHierarchySummary)
        {
            if (relatedMetadataHierarchySummary.getSideLinks() != null)
            {
                for (RelatedMetadataElementSummary relatedMetadataElementSummary : relatedMetadataHierarchySummary.getSideLinks())
                {
                    if ((relatedMetadataElementSummary != null) &&
                            (relatedMetadataElementSummary.getRelatedElement().getProperties() instanceof ExternalIdProperties externalIdProperties))
                    {
                        if (externalIdProperties.getKey() != null)
                        {
                            return externalIdProperties.getKey();
                        }
                    }
                }
            }
        }

        auditLog.logMessage(methodName, AtlasIntegrationAuditCode.MISSING_ATLAS_GUID.getMessageDefinition(connectorName,
                                                                                                          metadataElement.getRelatedElement().getElementHeader().getType().getTypeName(),
                                                                                                          metadataElement.getRelatedElement().getElementHeader().getGUID(),
                                                                                                          myContext.getMetadataSourceGUID()));
        throw new InvalidParameterException(AtlasIntegrationErrorCode.MISSING_ATLAS_GUID.getMessageDefinition(connectorName,
                                                                                                              metadataElement.getRelatedElement().getElementHeader().getType().getTypeName(),
                                                                                                              metadataElement.getRelatedElement().getElementHeader().getGUID(),
                                                                                                              myContext.getMetadataSourceGUID()),
                                            this.getClass().getName(),
                                            methodName,
                                            OpenMetadataProperty.KEY.name);
    }


    /**
     * Confirm whether there have been changes since the last refresh().
     *
     * @param egeriaMetadataElement list of external identifiers used to manage metadata exchange
     * @param atlasEntity atlas entity that is the source of the metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    protected boolean egeriaUpdateRequired(OpenMetadataRootElement egeriaMetadataElement,
                                           AtlasEntity             atlasEntity) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "egeriaUpdateRequired";

        RelatedMetadataElementSummary relatedExternalId = externalIdClient.getRelatedExternalId(egeriaMetadataElement,
                                                                                                myContext.getMetadataSourceGUID(),
                                                                                                atlasEntity.getGuid());

        if (relatedExternalId == null)
        {
            auditLog.logMessage(methodName,
                                AtlasIntegrationAuditCode.MISSING_CORRELATION.getMessageDefinition(connectorName,
                                                                                                   egeriaMetadataElement.getElementHeader().getType().getTypeName(),
                                                                                                   egeriaMetadataElement.getElementHeader().getGUID(),
                                                                                                   myContext.getMetadataSourceGUID(),
                                                                                                   atlasEntity.getGuid(),
                                                                                                   atlasEntity.getTypeName()));
            throw new PropertyServerException(AtlasIntegrationErrorCode.MISSING_CORRELATION.getMessageDefinition(connectorName,
                                                                                                                 egeriaMetadataElement.getElementHeader().getType().getTypeName(),
                                                                                                                 egeriaMetadataElement.getElementHeader().getGUID(),
                                                                                                                 myContext.getMetadataSourceGUID(),
                                                                                                                 atlasEntity.getGuid(),
                                                                                                                 atlasEntity.getTypeName()),
                                              this.getClass().getName(),
                                              methodName);
        }
        else
        {
            if (atlasEntity.getUpdateTime() == null)
            {
                /*
                 * Entity has only been created - confirm that no updates are needed.
                 */
                externalIdClient.confirmSynchronization(relatedExternalId.getRelationshipHeader().getGUID());
            }
            else if ((relatedExternalId.getRelationshipProperties() instanceof ExternalIdLinkProperties externalIdLinkProperties) &&
                     ((externalIdLinkProperties.getLastSynchronized() == null) ||
                      (atlasEntity.getUpdateTime().after(externalIdLinkProperties.getLastSynchronized()))))
            {
                ExternalIdProperties externalIdentifierProperties = this.getExternalIdentifier(atlasEntity.getGuid(),
                                                                                               atlasEntity.getTypeName(),
                                                                                               atlasEntity.getCreatedBy(),
                                                                                               atlasEntity.getCreateTime(),
                                                                                               atlasEntity.getUpdatedBy(),
                                                                                               atlasEntity.getUpdateTime(),
                                                                                               atlasEntity.getVersion());

                externalIdClient.updateExternalId(relatedExternalId.getRelatedElement().getElementHeader().getGUID(),
                                                  externalIdClient.getUpdateOptions(true),
                                                  externalIdentifierProperties);

                externalIdClient.confirmSynchronization(relatedExternalId.getRelationshipHeader().getGUID());

                return true;
            }
            else
            {
                /*
                 * No updates have happened since the last know synchronization
                 */
                externalIdClient.confirmSynchronization(relatedExternalId.getRelationshipHeader().getGUID());
            }
        }

        return false;
    }


    /**
     * Confirm whether there have been changes since the last synchronization.
     *
     * @param egeriaMetadataElement list of external identifiers used to manage metadata exchange
     * @param atlasEntity atlas entity that is the source of the metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    problem accessing the property server
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    protected boolean atlasUpdateRequired(OpenMetadataRootElement egeriaMetadataElement,
                                          AtlasEntity             atlasEntity) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String methodName = "atlasUpdateRequired";

        RelatedMetadataElementSummary relatedExternalId = externalIdClient.getRelatedExternalId(egeriaMetadataElement,
                                                                                                myContext.getMetadataSourceGUID(),
                                                                                                atlasEntity.getGuid());

        if (relatedExternalId == null)
        {
            auditLog.logMessage(methodName,
                                AtlasIntegrationAuditCode.MISSING_CORRELATION.getMessageDefinition(connectorName,
                                                                                                   egeriaMetadataElement.getElementHeader().getType().getTypeName(),
                                                                                                   egeriaMetadataElement.getElementHeader().getGUID(),
                                                                                                   atlasEntity.getGuid(),
                                                                                                   atlasEntity.getTypeName()));
            throw new PropertyServerException(AtlasIntegrationErrorCode.MISSING_CORRELATION.getMessageDefinition(connectorName,
                                                                                                                 egeriaMetadataElement.getElementHeader().getType().getTypeName(),
                                                                                                                 egeriaMetadataElement.getElementHeader().getGUID(),
                                                                                                                 atlasEntity.getGuid(),
                                                                                                                 atlasEntity.getTypeName()),
                                              this.getClass().getName(),
                                              methodName);
        }
        else if ((egeriaMetadataElement.getElementHeader().getVersions().getUpdateTime() != null) &&
                 (relatedExternalId.getRelationshipProperties() instanceof ExternalIdLinkProperties externalIdLinkProperties) &&
                 ((externalIdLinkProperties.getLastSynchronized() == null) ||
                  (egeriaMetadataElement.getElementHeader().getVersions().getUpdateTime().after(externalIdLinkProperties.getLastSynchronized()))))
        {
            return true;
        }


        return false;
    }


    /**
     * Update the correlation properties for an Egeria element after an atlas entity has been updated.
     *
     * @param egeriaElement element in open metadata
     * @param atlasEntity atlas entity that is the source of the metadata
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException user not authorized to issue this request
     * @throws PropertyServerException    problem accessing the property server
     */
    protected void updateExternalIdentifierAfterAtlasUpdate(OpenMetadataRootElement egeriaElement,
                                                            AtlasEntity             atlasEntity) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        final String methodName = "updateExternalIdentifierAfterAtlasUpdate";

        ExternalIdProperties externalIdentifierProperties = this.getExternalIdentifier(atlasEntity.getGuid(),
                                                                                       atlasEntity.getTypeName(),
                                                                                       atlasEntity.getCreatedBy(),
                                                                                       atlasEntity.getCreateTime(),
                                                                                       atlasEntity.getUpdatedBy(),
                                                                                       atlasEntity.getUpdateTime(),
                                                                                       atlasEntity.getVersion());

        RelatedMetadataElementSummary relatedExternalId = externalIdClient.getRelatedExternalId(egeriaElement,
                                                                                                myContext.getMetadataSourceGUID(),
                                                                                                atlasEntity.getGuid());

        if (relatedExternalId != null)
        {
            externalIdClient.updateExternalId(relatedExternalId.getRelatedElement().getElementHeader().getGUID(),
                                              externalIdClient.getUpdateOptions(true),
                                              externalIdentifierProperties);
        }
        else
        {
            auditLog.logMessage(methodName,
                                AtlasIntegrationAuditCode.MISSING_CORRELATION.getMessageDefinition(connectorName,
                                                                                                   egeriaElement.getElementHeader().getType().getTypeName(),
                                                                                                   egeriaElement.getElementHeader().getGUID(),
                                                                                                   myContext.getMetadataSourceGUID(),
                                                                                                   atlasEntity.getGuid(),
                                                                                                   atlasEntity.getTypeName()));
            throw new PropertyServerException(AtlasIntegrationErrorCode.MISSING_CORRELATION.getMessageDefinition(connectorName,
                                                                                                                 egeriaElement.getElementHeader().getType().getTypeName(),
                                                                                                                 egeriaElement.getElementHeader().getGUID(),
                                                                                                                 myContext.getMetadataSourceGUID(),
                                                                                                                 atlasEntity.getGuid(),
                                                                                                                 atlasEntity.getTypeName()),
                                              this.getClass().getName(),
                                              methodName);
        }
    }


    /**
     * Extract the unique identifier for the corresponding open metadata entity to the supplied Apache Atlas entity.  A null value
     * returned means that an open metadata ecosystem entity has not yet been created.
     *
     * @param atlasEntity entity retrieved from Apache Atlas
     * @return string guid
     * @throws PropertyServerException there is a problem calling Apache Atlas
     */
    protected String getEgeriaGUID(AtlasEntityWithExtInfo  atlasEntity) throws PropertyServerException
    {
        if (atlasEntity != null)
        {
            AtlasEntityWithExtInfo openMetadataCorrelationEntity = atlasClient.getRelatedEntity(atlasEntity,
                                                                                                openMetadataCorrelationPropertyName);

            if ((openMetadataCorrelationEntity != null) &&
                        (openMetadataCorrelationEntity.getEntity() != null) &&
                        (openMetadataCorrelationEntity.getEntity().getAttributes() != null))
            {
                return this.getAtlasStringProperty(openMetadataCorrelationEntity.getEntity().getAttributes(), egeriaGUIDPropertyName);
            }
        }

        return null;
    }


    /**
     * Remove the requested entity.
     *
     * @param atlasEntity entity retrieved from Apache Atlas
     * @throws PropertyServerException there is a problem calling Apache Atlas
     */
    protected void removeEgeriaGUID(AtlasEntityWithExtInfo  atlasEntity) throws PropertyServerException
    {
        if (atlasEntity != null)
        {
            AtlasEntityWithExtInfo openMetadataCorrelationEntity = atlasClient.getRelatedEntity(atlasEntity,
                                                                                                openMetadataCorrelationPropertyName);

            if ((openMetadataCorrelationEntity != null) && (openMetadataCorrelationEntity.getEntity() != null))
            {
                atlasClient.deleteEntity(openMetadataCorrelationEntity.getEntity().getGuid());
            }
        }
    }


    /**
     * Return an external identifier properties object for an Atlas GUID.
     *
     * @param atlasGUID guid to encode
     * @param atlasTypeName name of the atlas type
     * @param externalInstanceCreatedBy the username of the person or process that created the instance in the external system
     * @param externalInstanceCreationTime the date/time when the instance in the external system was created
     * @param externalInstanceLastUpdatedBy the username of the person or process that last updated the instance in the external system
     * @param externalInstanceLastUpdateTime the date/time that the instance in the external system was last updated
     * @param externalInstanceVersion the latest version of the element in the external system
     * @return properties object
     */
    protected ExternalIdProperties getExternalIdentifier(String                   atlasGUID,
                                                         String                   atlasTypeName,
                                                         String                   externalInstanceCreatedBy,
                                                         Date                     externalInstanceCreationTime,
                                                         String                   externalInstanceLastUpdatedBy,
                                                         Date                     externalInstanceLastUpdateTime,
                                                         long                     externalInstanceVersion)
    {
        ExternalIdProperties externalIdentifierProperties = new ExternalIdProperties();

        externalIdentifierProperties.setKey(atlasGUID);
        externalIdentifierProperties.setKeyPattern(KeyPattern.LOCAL_KEY);
        externalIdentifierProperties.setExternalInstanceTypeName(atlasTypeName);
        externalIdentifierProperties.setExternalInstanceCreatedBy(externalInstanceCreatedBy);
        externalIdentifierProperties.setExternalInstanceCreationTime(externalInstanceCreationTime);
        externalIdentifierProperties.setExternalInstanceLastUpdatedBy(externalInstanceLastUpdatedBy);
        externalIdentifierProperties.setExternalInstanceLastUpdateTime(externalInstanceLastUpdateTime);
        externalIdentifierProperties.setExternalInstanceVersion(externalInstanceVersion);

        return externalIdentifierProperties;
    }


    /**
     * Return an external identifier link relationship properties object for an Atlas GUID.
     * This creates additional mapping properties for glossary terms and categories.
     *
     * @param atlasName name of the element in atlas
     * @param lastKnownEgeriaDisplayName name of the element in Egeria
     * @param permittedSynchronization the permitted synchronization direction
     * @return properties object
     */
    protected ExternalIdLinkProperties getExternalIdLink(String                   atlasName,
                                                         String                   lastKnownEgeriaDisplayName,
                                                         PermittedSynchronization permittedSynchronization)
    {
        ExternalIdLinkProperties externalIdentifierProperties = new ExternalIdLinkProperties();

        externalIdentifierProperties.setUsage(atlasGUIDPropertyName);
        externalIdentifierProperties.setSource(connectorName);
        externalIdentifierProperties.setLastSynchronized(new Date());
        externalIdentifierProperties.setPermittedSynchronization(permittedSynchronization);

        Map<String, String> mappingProperties = new HashMap<>();

        mappingProperties.put(modulePropertyName, connectorName + "::" + moduleName);

        if (atlasName != null)
        {
            mappingProperties.put(atlasNameMappingPropertyName, atlasName);
        }

        if (lastKnownEgeriaDisplayName != null)
        {
            mappingProperties.put(egeriaNameMappingPropertyName, lastKnownEgeriaDisplayName);
        }

        externalIdentifierProperties.setMappingProperties(mappingProperties);

        return externalIdentifierProperties;
    }


    /**
     * Check that the Atlas GUID is correct stored in the open metadata element.
     *
     * @param egeriaElement element to validate and potentially update
     * @param egeriaDisplayName associated display name - used for glossary members
     * @param atlasEntity equivalent entity from Apache Atlas
     * @param atlasGUID Unique identifier from Apache Atlas
     * @param atlasTypeName name of the atlas type
     * @param atlasName atlas name property
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException problem connecting with Egeria
     */
    protected void ensureAtlasExternalIdentifier(OpenMetadataRootElement egeriaElement,
                                                 String          egeriaDisplayName,
                                                 AtlasEntity     atlasEntity,
                                                 String          atlasGUID,
                                                 String          atlasTypeName,
                                                 String          atlasName) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        /*
         * Check that the Atlas Glossary's GUID is stored as an external identifier.
         */
        RelatedMetadataElementSummary relatedExternalId = externalIdClient.getRelatedExternalId(egeriaElement,
                                                                                                myContext.getMetadataSourceGUID(),
                                                                                                atlasGUID);

        if ((relatedExternalId != null) && (relatedExternalId.getRelatedElement().getProperties() instanceof ExternalIdProperties externalIdProperties))
        {
            if (atlasGUID.equals(externalIdProperties.getKey()))
            {
                /*
                 * Atlas GUID is stored as an external identifier.
                 */
                egeriaUpdateRequired(egeriaElement, atlasEntity);
                // todo check and update external identifier - need to check if update is necessary
                return;
            }
            else
            {
                externalIdClient.deleteExternalId(relatedExternalId.getRelatedElement().getElementHeader().getGUID(),
                                                  externalIdClient.getDeleteOptions(false));
            }
        }

        /*
         * Set up external Identifier
         */
        ExternalIdProperties externalIdentifierProperties = this.getExternalIdentifier(atlasGUID,
                                                                                       atlasTypeName,
                                                                                       atlasEntity.getCreatedBy(),
                                                                                       atlasEntity.getCreateTime(),
                                                                                       atlasEntity.getUpdatedBy(),
                                                                                       atlasEntity.getUpdateTime(),
                                                                                       atlasEntity.getVersion());
        ExternalIdLinkProperties externalIdLinkProperties = this.getExternalIdLink(atlasName, egeriaDisplayName, PermittedSynchronization.TO_THIRD_PARTY);

        externalIdClient.createExternalId(egeriaElement.getElementHeader().getGUID(), externalIdLinkProperties, externalIdentifierProperties);
    }


    /**
     * Return the list of unique identifiers from a list of entities from Apache Atlas.
     *
     * @param atlasEntities list of entities retrieved from Apache Atlas
     * @return list of guids (or empty list but not null)
     */
    protected List<String> getValidAtlasGUIDs(List<AtlasEntityWithExtInfo> atlasEntities)
    {
        List<String> validAtlasGUIDs = new ArrayList<>();

        if (atlasEntities != null)
        {
            for (AtlasEntityWithExtInfo atlasEntityWithExtInfo : atlasEntities)
            {
                if (atlasEntityWithExtInfo != null)
                {
                    AtlasEntity atlasEntity = atlasEntityWithExtInfo.getEntity();

                    if (atlasEntity != null)
                    {
                        validAtlasGUIDs.add(atlasEntity.getGuid());
                    }
                }
            }
        }


        return validAtlasGUIDs;
    }


    /**
     * Retrieve a string property from and Apache Atlas entity's attributes.
     *
     * @param attributes attribute map
     * @param propertyName name of property to extract
     * @return extracted string or null
     */
    protected String getAtlasStringProperty(Map<String, Object> attributes,
                                            String              propertyName)
    {
        if (attributes != null)
        {
            if (attributes.get(propertyName) != null)
            {
                return attributes.get(propertyName).toString();
            }
        }

        return null;
    }


    /**
     * Retrieve a boolean property from and Apache Atlas entity's attributes.
     *
     * @param attributes attribute map
     * @param propertyName name of property to extract
     * @return extracted boolean or null
     */
    protected boolean getAtlasBooleanProperty(Map<String, Object> attributes,
                                              String              propertyName)
    {
        if (attributes != null)
        {
            if (attributes.get(propertyName) != null)
            {
                if (attributes.get(propertyName).toString() == "true")
                {
                    return true;
                }
            }
        }

        return false;
    }


    /**
     * Retrieve a string property map from and Apache Atlas entity's attributes.
     *
     * @param attributes attribute map
     * @param propertyName name of property to extract
     * @return extracted string or null
     */
    protected Map<String, String> getAtlasPropertyMap(Map<String, Object> attributes,
                                                      String              propertyName)
    {
        if (attributes != null)
        {
            if (attributes.get(propertyName) instanceof Map<?,?> propertyObjectMap)
            {
                Map<String, String> propertyMap = new HashMap<>();
                for (Object  propertyMapKey : propertyObjectMap.keySet())
                {
                    if (propertyMapKey != null)
                    {
                        String propertyValue = propertyObjectMap.get(propertyMapKey).toString();

                        if (propertyValue != null)
                        {
                            propertyMap.put(propertyMapKey.toString(), propertyValue);
                        }
                    }
                }

                return propertyMap;
            }
        }

        return null;
    }


    /**
     * Retrieve a string array property from and Apache Atlas entity's attributes.
     *
     * @param attributes attribute map
     * @param propertyName name of property to extract
     * @return extracted string or null
     */
    protected List<String> getAtlasStringArray(Map<String, Object> attributes,
                                               String              propertyName)
    {
        if (attributes != null)
        {
            if (attributes.get(propertyName) instanceof Map<?,?> propertyObjectMap)
            {
                List<String> propertyList = new ArrayList<>();
                for (Object  propertyMapKey : propertyObjectMap.keySet())
                {
                    if (propertyMapKey != null)
                    {
                        String propertyValue = propertyObjectMap.get(propertyMapKey).toString();

                        if (propertyValue != null)
                        {
                            propertyList.add(propertyValue);
                        }
                    }
                }

                return propertyList;
            }
        }

        return null;
    }


    /**
     * Add a property from Apache Atlas to Egeria's additionalProperties map since it has nowhere else to go.
     *
     * @param atlasAttributes all the attributes retrieved from Apache Atlas
     * @param skipPropertyNames names of the properties that have already been extracted
     * @return  additionalProperties populated with properties from Apache Atlas
     */
    protected Map<String, String> addRemainingPropertiesToAdditionalProperties(Map<String, Object> atlasAttributes,
                                                                               List<String>        skipPropertyNames)
    {
        if (atlasAttributes != null)
        {
            Map<String, String> egeriaAdditionalProperties = new HashMap<>();

            for (String propertyName : atlasAttributes.keySet())
            {
                if (! skipPropertyNames.contains(propertyName))
                {
                    Object propertyValue = atlasAttributes.get(propertyName);

                    if (propertyValue != null)
                    {
                        egeriaAdditionalProperties.put(propertyName, propertyValue.toString());
                    }
                }
            }

            if (! egeriaAdditionalProperties.isEmpty())
            {
                return egeriaAdditionalProperties;
            }
        }

        return null;
    }


    /**
     * Add a property from Apache Atlas to Egeria's additionalProperties map since it has nowhere else to go.
     * Since the property is a map of properties, each entry is added as separate property using a dotted
     * notation mapPropertyName.mapPropertyKey.mapPropertyValue.
     *
     * @param atlasAttributes all the attributes retrieved from Apache Atlas
     * @param propertyName name of the property to extract
     * @param egeriaAdditionalProperties additionalProperties to map to populate
     */
    protected void addStringArrayToAdditionalProperties(Map<String, Object> atlasAttributes,
                                                        String              propertyName,
                                                        Map<String, String> egeriaAdditionalProperties)
    {
        List<String> atlasPropertyList = this.getAtlasStringArray(atlasAttributes, propertyName);

        if ((atlasPropertyList != null) && (! atlasPropertyList.isEmpty()))
        {
            int index = 0;
            for (String propertyValue : atlasPropertyList)
            {
                if (propertyValue != null)
                {
                    egeriaAdditionalProperties.put(propertyName + "." + index, propertyValue);
                    index ++;
                }
            }
        }
    }


    /**
     * Add a property from Apache Atlas to Egeria's additionalProperties map since it has nowhere else to go.
     * Since the property is a map of properties, each entry is added as separate property using a dotted
     * notation mapPropertyName.mapPropertyKey.mapPropertyValue.
     *
     * @param atlasAttributes all the attributes retrieved from Apache Atlas
     * @param propertyName name of the property to extract
     * @param egeriaAdditionalProperties additionalProperties to map to populate
     */
    protected void addPropertyMapToAdditionalProperties(Map<String, Object> atlasAttributes,
                                                        String              propertyName,
                                                        Map<String, String> egeriaAdditionalProperties)
    {
        Map<String, String> atlasPropertyMap = this.getAtlasPropertyMap(atlasAttributes, propertyName);

        if ((atlasPropertyMap != null) && (! atlasPropertyMap.isEmpty()))
        {
            for (String propertyKey : atlasPropertyMap.keySet())
            {
                String propertyValue = atlasPropertyMap.get(propertyKey);

                if (propertyValue != null)
                {
                    egeriaAdditionalProperties.put(propertyName + "." + propertyKey, propertyValue);
                }
            }
        }
    }



    /**
     * Synchronize all the data set entities from Apache Atlas to the open metadata ecosystem.
     *
     * @param atlasTypeName name of the type of entity to synchronize from Apache Atlas
     * @param egeriaTypeName name of the type of entity to synchronize to the open metadata ecosystem
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException unable to communicate with Egeria
     */
    protected void syncAtlasDataSetsAsDataSets(String atlasTypeName,
                                               String egeriaTypeName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        /*
         * Retrieve the data sets catalogued in Apache Atlas.  This is turned into an Open Metadata data set entity.
         */
        int startFrom = 0;
        int pageSize  = myContext.getMaxPageSize();

        List<AtlasEntityHeader> atlasSearchResult = atlasClient.getEntitiesForType(atlasTypeName, startFrom, pageSize);

        while ((atlasSearchResult != null) && (! atlasSearchResult.isEmpty()))
        {
            /*
             * Found new datasets - process each one in turn.
             */
            for (AtlasEntityHeader atlasEntityHeader : atlasSearchResult)
            {
                String atlasDataSetGUID = atlasEntityHeader.getGuid();

                AtlasEntityWithExtInfo atlasDataSetEntity = atlasClient.getEntityByGUID(atlasDataSetGUID);

                this.syncAtlasDataSetAsDataSet(atlasDataSetEntity, atlasTypeName, egeriaTypeName);
            }

            /*
             * Retrieve the next page
             */
            startFrom = startFrom + pageSize;
            atlasSearchResult = atlasClient.getEntitiesForType(atlasTypeName, startFrom, pageSize);
        }
    }


    /**
     * Copy the contents of the Atlas data set entity into open metadata as a type of DataSet.
     *
     * @param atlasDataSetEntity entity retrieved from Apache Atlas
     * @param atlasTypeName name of the type of entity in Apache Atlas to synchronize
     * @param egeriaTypeName name of the type of entity in the open metadata ecosystem to synchronize
     *
     * @return unique identifier of the data set
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException unable to communicate with Egeria
     */
    protected String syncAtlasDataSetAsDataSet(AtlasEntityWithExtInfo  atlasDataSetEntity,
                                               String                  atlasTypeName,
                                               String                  egeriaTypeName) throws InvalidParameterException,
                                                                                              UserNotAuthorizedException,
                                                                                              PropertyServerException
    {
        final String methodName = "syncAtlasDataSetAsDataSet";

        if ((atlasDataSetEntity != null) && (atlasDataSetEntity.getEntity() != null))
        {
            String egeriaDataSetGUID = this.getEgeriaGUID(atlasDataSetEntity);

            if (egeriaDataSetGUID == null)
            {
                /*
                 * No record of a previous synchronization with the open metadata ecosystem.
                 */
                egeriaDataSetGUID = createAtlasDataSetAsDataSetInEgeria(atlasDataSetEntity, atlasTypeName, egeriaTypeName);
            }
            else
            {
                try
                {
                    /*
                     * Does the matching entity in the open metadata ecosystem still exist.  Notice effective time is set to null
                     * to make sure that this entity is found no matter what its effectivity dates are set to.
                     */
                    dataAssetClient.getAssetByGUID(egeriaDataSetGUID, null);

                    /*
                     * The Egeria equivalent is still in place.
                     */
                    updateAtlasDataSetAsDataSetInEgeria(atlasDataSetEntity, egeriaDataSetGUID, atlasTypeName, egeriaTypeName);
                }
                catch (InvalidParameterException notKnown)
                {
                    /*
                     * The open metadata ecosystem entity has been deleted - put it back.
                     */
                    auditLog.logMessage(methodName,
                                        AtlasIntegrationAuditCode.REPLACING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                                                                                               egeriaTypeName,
                                                                                                               egeriaDataSetGUID,
                                                                                                               atlasDataSetEntity.getEntity().getGuid()));
                    removeEgeriaGUID(atlasDataSetEntity);
                    egeriaDataSetGUID = createAtlasDataSetAsDataSetInEgeria(atlasDataSetEntity, atlasTypeName, egeriaTypeName);
                }
            }

            return egeriaDataSetGUID;
        }

        return null;
    }


    /**
     * Create the data set in the open metadata ecosystem.
     *
     * @param atlasDataSetEntity entity retrieved from Apache Atlas
     * @param atlasTypeName name of the type of entity to synchronize from Apache Atlas
     * @param egeriaTypeName name of the type of entity to synchronize to the open metadata ecosystem
     *
     * @return unique identifier of the data set
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException unable to communicate with Egeria
     */
    protected String createAtlasDataSetAsDataSetInEgeria(AtlasEntityWithExtInfo  atlasDataSetEntity,
                                                         String                  atlasTypeName,
                                                         String                  egeriaTypeName) throws InvalidParameterException,
                                                                                                        UserNotAuthorizedException,
                                                                                                        PropertyServerException
    {
        final String methodName = "createAtlasDataSetAsDataSetInEgeria";

        if (atlasDataSetEntity != null)
        {
            AtlasEntity atlasEntity = atlasDataSetEntity.getEntity();

            if (atlasEntity != null)
            {
                ExternalIdProperties externalIdentifierProperties = this.getExternalIdentifier(atlasEntity.getGuid(),
                                                                                               atlasEntity.getTypeName(),
                                                                                               atlasEntity.getCreatedBy(),
                                                                                               atlasEntity.getCreateTime(),
                                                                                               atlasEntity.getUpdatedBy(),
                                                                                               atlasEntity.getUpdateTime(),
                                                                                               atlasEntity.getVersion());
                ExternalIdLinkProperties externalIdLinkProperties = this.getExternalIdLink(this.getAtlasStringProperty(atlasEntity.getAttributes(), atlasNamePropertyName),
                                                                                           null,
                                                                                           PermittedSynchronization.FROM_THIRD_PARTY);

                DataAssetProperties dataAssetProperties = this.getEgeriaDataSetProperties(atlasEntity, egeriaTypeName);

                NewElementOptions newElementOptions = new NewElementOptions(dataAssetClient.getMetadataSourceOptions());

                newElementOptions.setIsOwnAnchor(true);

                String egeriaDataSetGUID = dataAssetClient.createAsset(newElementOptions,
                                                                       null,
                                                                       dataAssetProperties,
                                                                       null);

                externalIdClient.createExternalId(egeriaDataSetGUID, externalIdLinkProperties, externalIdentifierProperties);

                auditLog.logMessage(methodName,
                                    AtlasIntegrationAuditCode.CREATING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                                                                                          atlasTypeName,
                                                                                                          egeriaDataSetGUID,
                                                                                                          atlasEntity.getTypeName(),
                                                                                                          atlasEntity.getGuid()));

                saveEgeriaGUIDInAtlas(atlasEntity.getGuid(),
                                      egeriaDataSetGUID,
                                      dataAssetProperties.getQualifiedName(),
                                      egeriaTypeName,
                                      false,
                                      false);

                setOwner(atlasDataSetEntity, egeriaDataSetGUID);

                return egeriaDataSetGUID;
            }
        }

        return null;
    }


    /**
     * Update the properties of an open metadata data set with the current properties from Apache Atlas.
     *
     * @param atlasDataSetEntity entity retrieved from Apache Atlas
     * @param egeriaDataSetGUID unique identifier of the equivalent entity in the open metadata ecosystem
     * @param atlasTypeName name of the type of entity to synchronize from Apache Atlas
     * @param egeriaTypeName name of the type of entity to synchronize to the open metadata ecosystem
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException unable to communicate with Egeria
     */
    protected void updateAtlasDataSetAsDataSetInEgeria(AtlasEntityWithExtInfo  atlasDataSetEntity,
                                                       String                  egeriaDataSetGUID,
                                                       String                  atlasTypeName,
                                                       String                  egeriaTypeName) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        final String methodName = "updateAtlasDataSetAsDataSetInEgeria";

        if (atlasDataSetEntity != null)
        {
            AtlasEntity atlasEntity = atlasDataSetEntity.getEntity();
            OpenMetadataRootElement dataAssetElement = dataAssetClient.getAssetByGUID(egeriaDataSetGUID, dataAssetClient.getGetOptions());

            if (atlasEntity != null)
            {
                if (this.egeriaUpdateRequired(dataAssetElement, atlasEntity))
                {
                    DataAssetProperties dataAssetProperties = this.getEgeriaDataSetProperties(atlasEntity, egeriaTypeName);

                    auditLog.logMessage(methodName,
                                        AtlasIntegrationAuditCode.UPDATING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                                                                                              atlasEntity.getTypeName(),
                                                                                                              atlasEntity.getGuid(),
                                                                                                              atlasTypeName,
                                                                                                              egeriaDataSetGUID));

                    dataAssetClient.updateAsset(egeriaDataSetGUID,
                                                dataAssetClient.getUpdateOptions(true),
                                                dataAssetProperties);
                }
            }
        }
    }


    /**
     * Map the properties from the entity retrieved from Apache Atlas to the Egeria properties for the open metadata entity.
     *
     * @param atlasEntity retrieve entity from Apache Atlas
     * @param egeriaTypeName name of the type used in the open metadata ecosystem
     * @return properties to pass to Egeria
     */
    protected DataAssetProperties getEgeriaDataSetProperties(AtlasEntity atlasEntity,
                                                             String      egeriaTypeName)
    {
        DataAssetProperties dataAssetProperties = this.getDataAssetProperties(atlasEntity, egeriaTypeName);

        dataAssetProperties.setAdditionalProperties(addRemainingPropertiesToAdditionalProperties(atlasEntity.getAttributes(),
                                                                                                 atlasAssetProperties));

        return dataAssetProperties;
    }

    /**
     * Add the standard properties for an Atlas asset entity into an Egeria DataAssetProperties.  This is suitable for a DataSet.
     *
     * @param atlasEntity asset entity retrieved from Apache Atlas
     * @param egeriaTypeName name of the type used in the open metadata ecosystem
     * @return data asset properties used to maintaining data assets in the open metadata ecosystem
     */
    protected DataAssetProperties getDataAssetProperties(AtlasEntity   atlasEntity,
                                                         String        egeriaTypeName)
    {
        if (atlasEntity != null)
        {
            DataAssetProperties dataAssetProperties = new DataAssetProperties();

            Map<String, Object> attributes = atlasEntity.getAttributes();

            dataAssetProperties.setTypeName(egeriaTypeName);
            dataAssetProperties.setDeployedImplementationType(atlasEntity.getTypeName());
            dataAssetProperties.setQualifiedName(myContext.getMetadataSourceQualifiedName() + "::" + atlasEntity.getTypeName() + "::" + getAtlasStringProperty(attributes, atlasQualifiedNamePropertyName));
            dataAssetProperties.setDisplayName(getAtlasStringProperty(attributes, atlasNamePropertyName));
            dataAssetProperties.setResourceName(getAtlasStringProperty(attributes, atlasNamePropertyName));
            dataAssetProperties.setDescription(getAtlasStringProperty(attributes, atlasDescriptionPropertyName));
            dataAssetProperties.setDisplayName(getAtlasStringProperty(attributes, atlasDisplayNamePropertyName));
            //dataAssetProperties.setDisplayDescription(getAtlasStringProperty(attributes, atlasUserDescriptionPropertyName));

            return dataAssetProperties;
        }

        return null;
    }


    /**
     * Map the properties from the entity retrieved from Apache Atlas to the Egeria properties for the open metadata entity.
     *
     * @param atlasFSPathEntity retrieve fs_path entity from Apache Atlas
     * @param egeriaTypeName name of the type used in the open metadata ecosystem
     * @return properties to pass to Egeria
     */
    protected DataAssetProperties getEgeriaDataFileProperties(AtlasEntity atlasFSPathEntity,
                                                              String      egeriaTypeName)
    {
        final String pathNamePropertyName = "path";

        DataStoreProperties dataAssetProperties = new DataStoreProperties();

        Map<String, Object> attributes = atlasFSPathEntity.getAttributes();

        dataAssetProperties.setTypeName(egeriaTypeName);
        dataAssetProperties.setDeployedImplementationType(atlasFSPathEntity.getTypeName());
        dataAssetProperties.setQualifiedName(myContext.getMetadataSourceQualifiedName() + "::" + atlasFSPathEntity.getTypeName() + "::" + getAtlasStringProperty(attributes, atlasQualifiedNamePropertyName));
        dataAssetProperties.setDisplayName(getAtlasStringProperty(attributes, atlasNamePropertyName));
        dataAssetProperties.setResourceName(getAtlasStringProperty(attributes, atlasNamePropertyName));
        dataAssetProperties.setDescription(getAtlasStringProperty(attributes, atlasDescriptionPropertyName));
        dataAssetProperties.setDisplayName(getAtlasStringProperty(attributes, atlasDisplayNamePropertyName));
        //dataAssetProperties.setDisplayDescription(getAtlasStringProperty(attributes, atlasUserDescriptionPropertyName));


        Map<String, String> additionalProperties = addRemainingPropertiesToAdditionalProperties(atlasFSPathEntity.getAttributes(),
                                                                                                atlasDataFileProperties);

        dataAssetProperties.setAdditionalProperties(additionalProperties);

        return dataAssetProperties;
    }


    /**
     * Add the standard properties for an Atlas asset entity into an Egeria DataAssetProperties.  This is suitable for a DataSet.
     *
     * @param atlasEntity asset entity retrieved from Apache Atlas
     * @param egeriaTypeName name of the type used in the open metadata ecosystem
     * @return data asset properties used to maintaining data assets in the open metadata ecosystem
     */
    protected ProcessProperties getProcessProperties(AtlasEntity   atlasEntity,
                                                     String        egeriaTypeName)
    {
        if (atlasEntity != null)
        {
            ProcessProperties processProperties = new ProcessProperties();

            Map<String, Object> attributes = atlasEntity.getAttributes();

            processProperties.setTypeName(egeriaTypeName);
            processProperties.setDeployedImplementationType(atlasEntity.getTypeName());
            processProperties.setQualifiedName(myContext.getMetadataSourceQualifiedName() + "::" + atlasEntity.getTypeName() + "::" + getAtlasStringProperty(attributes, atlasQualifiedNamePropertyName));
            processProperties.setDisplayName(getAtlasStringProperty(attributes, atlasNamePropertyName));
            processProperties.setResourceName(getAtlasStringProperty(attributes, atlasNamePropertyName));
            processProperties.setDescription(getAtlasStringProperty(attributes, atlasDescriptionPropertyName));
            processProperties.setDisplayName(getAtlasStringProperty(attributes, atlasDisplayNamePropertyName));
            //processProperties.setDisplayDescription(getAtlasStringProperty(attributes, atlasUserDescriptionPropertyName));

            return processProperties;
        }

        return null;
    }


    /**
     * Add the standard properties for an Atlas asset entity into an Egeria SchemaAttributeProperties.
     *
     * @param atlasEntity asset entity retrieved from Apache Atlas
     * @param egeriaSchemaAttributeTypeName type name for the schema attribute
     * @return data asset properties used to maintaining data assets in the open metadata ecosystem
     */
    protected SchemaAttributeProperties getSchemaAttributeProperties(AtlasEntity   atlasEntity,
                                                                     String        egeriaSchemaAttributeTypeName)
    {
        if (atlasEntity != null)
        {
            SchemaAttributeProperties schemaAttributeProperties = new SchemaAttributeProperties();

            Map<String, Object> attributes = atlasEntity.getAttributes();

            schemaAttributeProperties.setTypeName(egeriaSchemaAttributeTypeName);
            schemaAttributeProperties.setQualifiedName(myContext.getMetadataSourceQualifiedName() + "::" + atlasEntity.getTypeName() + "::" + getAtlasStringProperty(attributes, "qualifiedName"));

            schemaAttributeProperties.setDisplayName(getAtlasStringProperty(attributes, "name") );
            schemaAttributeProperties.setDescription(getAtlasStringProperty(attributes, "description"));

            return schemaAttributeProperties;
        }

        return null;
    }


    /**
     * Add the standard properties for an Atlas asset entity into an Egeria SchemaAttributeProperties.
     *
     * @param atlasEntity asset entity retrieved from Apache Atlas
     * @param egeriaSchemaTypeTypeName  type name for the schema type
     * @return data asset properties used to maintaining data assets in the open metadata ecosystem
     */
    protected TypeEmbeddedAttributeProperties getSchemaTypeProperties(AtlasEntity   atlasEntity,
                                                                      String        egeriaSchemaTypeTypeName)
    {
        if (atlasEntity != null)
        {
            TypeEmbeddedAttributeProperties  schemaTypeProperties = new TypeEmbeddedAttributeProperties();

            Map<String, Object> attributes = atlasEntity.getAttributes();

            schemaTypeProperties.setTypeName(egeriaSchemaTypeTypeName);
            schemaTypeProperties.setDisplayName(getAtlasStringProperty(attributes, "displayName"));
            schemaTypeProperties.setDescription(getAtlasStringProperty(attributes, "userDescription"));

            return schemaTypeProperties;
        }

        return null;
    }


    /**
     * Set up the Ownership classification in open metadata if the Atlas entity has the owner property set.
     *
     * @param atlasEntityWithExtInfo entity retrieved from Apache Atlas
     * @param egeriaGUID unique identifier of the element in open metadata
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException problem communicating with Egeria.
     */
    protected void setOwner(AtlasEntityWithExtInfo atlasEntityWithExtInfo,
                            String                 egeriaGUID) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException
    {
        if ((atlasEntityWithExtInfo != null) &&
            (atlasEntityWithExtInfo.getEntity() != null) &&
            (atlasEntityWithExtInfo.getEntity().getAttributes() != null))
        {
            String owner = this.getAtlasStringProperty(atlasEntityWithExtInfo.getEntity().getAttributes(),
                                                       atlasOwnerPropertyName);

            if (owner != null)
            {
                OwnershipProperties properties = new OwnershipProperties();

                properties.setOwner(owner);
                properties.setOwnerTypeName(egeriaOwnerTypeName);
                properties.setOwnerPropertyName(egeriaOwnerPropertyName);

                classificationManagerClient.addOwnership(egeriaGUID,
                                                         properties,
                                                         classificationManagerClient.getMetadataSourceOptions());
            }
        }
    }
}
