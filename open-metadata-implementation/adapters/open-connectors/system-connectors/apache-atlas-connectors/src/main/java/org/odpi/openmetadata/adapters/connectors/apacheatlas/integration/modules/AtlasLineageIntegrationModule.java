/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.modules;


import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.integration.context.IntegrationContext;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.AssetClient;
import org.odpi.openmetadata.frameworks.openmetadata.connectorcontext.LineageClient;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ElementStatus;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.enums.PermittedSynchronization;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.ffdc.AtlasIntegrationAuditCode;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.integration.ffdc.AtlasIntegrationErrorCode;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties.AtlasEntity;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties.AtlasEntityHeader;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties.AtlasEntityWithExtInfo;
import org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.ApacheAtlasRESTConnector;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.processes.ProcessProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers.ExternalIdLinkProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.externalidentifiers.ExternalIdProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;


import java.util.ArrayList;
import java.util.List;

/**
 * AtlasLineageIntegrationModule synchronizes lineage from Apache Atlas to the open metadata ecosystem.
 * It is called after the registered integration modules have established the key assets into the open metadata ecosystem.
 */
public class AtlasLineageIntegrationModule extends AtlasIntegrationModuleBase
{
    /**
     * Unique name of this module for messages.
     */
    private static final String lineageModuleName = "Apache Atlas Lineage Integration Module";

    protected final static String egeriaDataSetTypeName           = OpenMetadataType.DATA_SET.typeName;
    protected final static String egeriaProcessTypeName           = OpenMetadataType.DEPLOYED_SOFTWARE_COMPONENT.typeName;
    protected final static String atlasProcessTypeName            = "Process";
    protected final static String atlasProcessInputsPropertyName  = "inputs";
    protected final static String atlasProcessOutputsPropertyName = "outputs";


    private final LineageClient lineageClient;
    private final AssetClient   assetClient;



    /**
     * Constructor for the module is supplied with the runtime context in order to operate.
     *
     * @param connectorName name of the connector (for messages)
     * @param connectionDetails connection properties used to start the connector
     * @param auditLog logging destination
     * @param myContext integration context
     * @param targetRootURL URL to connect to Apache Atlas
     * @param atlasClient client to connect to Apache Atlas
     * @param embeddedConnectors list of any embedded connectors (such as secrets connector and topic connector
     * @throws UserNotAuthorizedException security problem
     */
    public AtlasLineageIntegrationModule(String                   connectorName,
                                         Connection               connectionDetails,
                                         AuditLog                 auditLog,
                                         IntegrationContext       myContext,
                                         String                   targetRootURL,
                                         ApacheAtlasRESTConnector atlasClient,
                                         List<Connector>          embeddedConnectors) throws UserNotAuthorizedException
    {
        super(connectorName,
              lineageModuleName,
              connectionDetails,
              auditLog,
              myContext,
              targetRootURL,
              atlasClient,
              embeddedConnectors);

        this.lineageClient = myContext.getLineageClient();
        this.assetClient = myContext.getAssetClient();

        /*
         * Deduplication is turned off so that the connector works with the entities it created rather than
         * entities from other systems that have been linked as duplicates.
         */
        this.lineageClient.setForDuplicateProcessing(true);
        this.assetClient.setForDuplicateProcessing(true);
    }


    /**
     * Retrieves all the processes catalogued in Apache Atlas and creates an equivalent process in the open metadata ecosystem.
     * Then establishes DataFlow relationships between the open metadata entities that match to lineage relationships in Apache Atlas.
     * It is possible that some DataSets in the lineage flow have not been catalogued by the registered modules.
     * A placeholder for each of these data sets is created to ensure the lineage graph is complete.
     *
     * @throws ConnectorCheckedException problem communicating with Apache Atlas or Egeria.
     */
    public void synchronizeLineage() throws ConnectorCheckedException
    {
        final String methodName = "synchronizeLineage()";

        /*
         * The configuration can turn off the cataloguing of assets into the open metadata ecosystem.
         */
        if ((myContext.getPermittedSynchronization() == PermittedSynchronization.BOTH_DIRECTIONS) ||
            (myContext.getPermittedSynchronization() == PermittedSynchronization.TO_THIRD_PARTY))
        {
            try
            {
                /*
                 * Retrieve the processes catalogued in Apache Atlas.  This is turned into an Open Metadata DeployedSoftwareComponent.
                 */
                int startFrom = 0;
                int pageSize  = myContext.getMaxPageSize();

                List<AtlasEntityHeader> atlasSearchResult = atlasClient.getEntitiesForType(atlasProcessTypeName, startFrom, pageSize);

                while ((atlasSearchResult != null) && (! atlasSearchResult.isEmpty()))
                {
                    /*
                     * Found new process - work with each one in turn.
                     */
                    for (AtlasEntityHeader atlasEntityHeader : atlasSearchResult)
                    {
                        String atlasProcessGUID = atlasEntityHeader.getGuid();

                        AtlasEntityWithExtInfo atlasProcessEntity = atlasClient.getEntityByGUID(atlasProcessGUID);

                        String egeriaProcessGUID = this.syncAtlasProcess(atlasProcessEntity);

                        if (egeriaProcessGUID != null)
                        {
                            /*
                             * Synchronize inputs to the process
                             */
                            if ((atlasProcessEntity != null) &&
                                        (atlasProcessEntity.getEntity() != null) &&
                                        (atlasProcessEntity.getEntity().getRelationshipAttributes() != null) &&
                                        (atlasProcessEntity.getEntity().getRelationshipAttributes().get(atlasProcessInputsPropertyName) != null))
                            {
                                List<AtlasEntityWithExtInfo> atlasInputDataSets = atlasClient.getRelatedEntities(atlasProcessEntity,
                                                                                                                 atlasProcessInputsPropertyName);

                                if (atlasInputDataSets != null)
                                {
                                    for (AtlasEntityWithExtInfo atlasInputDataSet : atlasInputDataSets)
                                    {
                                        if ((atlasInputDataSet != null) && (atlasInputDataSet.getEntity() != null))
                                        {
                                            String egeriaDataSetGUID = syncAtlasDataSetAsDataSet(atlasInputDataSet,
                                                                                                 atlasInputDataSet.getEntity().getTypeName(),
                                                                                                 egeriaDataSetTypeName);

                                            /*
                                             * Set up the lineage relationship
                                             */
                                            auditLog.logMessage(methodName,
                                                                AtlasIntegrationAuditCode.ADDING_LINEAGE.getMessageDefinition(connectorName,
                                                                                                                              egeriaDataSetTypeName,
                                                                                                                              egeriaDataSetGUID,
                                                                                                                              egeriaProcessTypeName,
                                                                                                                              egeriaProcessGUID));
                                            lineageClient.linkLineage(egeriaDataSetGUID,
                                                                      egeriaProcessGUID,
                                                                      OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                                      new MakeAnchorOptions(lineageClient.getMetadataSourceOptions()),
                                                                      null);
                                        }
                                    }
                                }
                            }

                            /*
                             * Synchronize outputs to the process
                             */
                            if ((atlasProcessEntity != null) &&
                                        (atlasProcessEntity.getEntity() != null) &&
                                        (atlasProcessEntity.getEntity().getRelationshipAttributes() != null) &&
                                        (atlasProcessEntity.getEntity().getRelationshipAttributes().get(atlasProcessOutputsPropertyName) != null))
                            {
                                List<AtlasEntityWithExtInfo> atlasOutputDataSets = atlasClient.getRelatedEntities(atlasProcessEntity,
                                                                                                                 atlasProcessOutputsPropertyName);

                                if (atlasOutputDataSets != null)
                                {
                                    for (AtlasEntityWithExtInfo atlasInputDataSet : atlasOutputDataSets)
                                    {
                                        if ((atlasInputDataSet != null) && (atlasInputDataSet.getEntity() != null))
                                        {
                                            String egeriaDataSetGUID = syncAtlasDataSetAsDataSet(atlasInputDataSet,
                                                                                                 atlasInputDataSet.getEntity().getTypeName(),
                                                                                                 egeriaDataSetTypeName);

                                            /*
                                             * Set up the lineage relationship
                                             */
                                            auditLog.logMessage(methodName,
                                                                AtlasIntegrationAuditCode.ADDING_LINEAGE.getMessageDefinition(connectorName,
                                                                                                                              egeriaProcessTypeName,
                                                                                                                              egeriaProcessGUID,
                                                                                                                              egeriaDataSetTypeName,
                                                                                                                              egeriaDataSetGUID));
                                            lineageClient.linkLineage(egeriaProcessGUID,
                                                                      egeriaDataSetGUID,
                                                                      OpenMetadataType.DATA_FLOW_RELATIONSHIP.typeName,
                                                                      new MakeAnchorOptions(lineageClient.getMetadataSourceOptions()),
                                                                      null);
                                        }
                                    }
                                }
                            }
                        }
                    }

                    /*
                     * Retrieve the next page
                     */
                    startFrom = startFrom + pageSize;
                    atlasSearchResult = atlasClient.getEntitiesForType(atlasProcessTypeName, startFrom, pageSize);
                }
            }
            catch (Exception error)
            {
                if (auditLog != null)
                {
                    auditLog.logException(methodName,
                                          AtlasIntegrationAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                              error.getClass().getName(),
                                                                                                              methodName,
                                                                                                              error.getMessage()),
                                          error);
                }

                throw new ConnectorCheckedException(AtlasIntegrationErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                                        error.getClass().getName(),
                                                                                                                        methodName,
                                                                                                                        error.getMessage()),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    error);
            }
        }
    }


    /**
     * Copy the contents of the Atlas database entity into open metadata.
     *
     * @param atlasProcessEntity entity retrieved from Apache Atlas
     *
     * @return unique identifier of the equivalent element in open metadata (egeriaProcessGUID)
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException unable to communicate with Egeria
     */
    private String syncAtlasProcess(AtlasEntityWithExtInfo  atlasProcessEntity) throws InvalidParameterException,
                                                                                       UserNotAuthorizedException,
                                                                                       PropertyServerException
    {
        final String methodName = "syncAtlasProcess";

        if ((atlasProcessEntity != null) && (atlasProcessEntity.getEntity() != null))
        {
            String egeriaProcessGUID = this.getEgeriaGUID(atlasProcessEntity);

            if (egeriaProcessGUID == null)
            {
                /*
                 * No record of a previous synchronization with the open metadata ecosystem.
                 */
                egeriaProcessGUID = createAtlasProcessInEgeria(atlasProcessEntity);
            }
            else
            {
                try
                {
                    /*
                     * Does the matching entity in the open metadata ecosystem still exist.  Notice effective time is set to null
                     * to make sure that this entity is found no matter what its effectivity dates are set to.
                     */
                    assetClient.getAssetByGUID(egeriaProcessGUID, assetClient.getGetOptions());

                    /*
                     * The Egeria equivalent is still in place.
                     */
                    updateAtlasProcessInEgeria(atlasProcessEntity, egeriaProcessGUID);
                }
                catch (InvalidParameterException notKnown)
                {
                    /*
                     * The open metadata ecosystem entity has been deleted - put it back.
                     */
                    auditLog.logMessage(methodName,
                                        AtlasIntegrationAuditCode.REPLACING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                                                                                               egeriaProcessTypeName,
                                                                                                               egeriaProcessGUID,
                                                                                                               atlasProcessEntity.getEntity().getGuid()));
                    removeEgeriaGUID(atlasProcessEntity);
                    egeriaProcessGUID = createAtlasProcessInEgeria(atlasProcessEntity);
                }
            }

            return egeriaProcessGUID;
        }

        return null;
    }


    /**
     * Map the properties from the entity retrieved from Apache Atlas to the Egeria properties for the open metadata entity.
     *
     * @param atlasEntity retrieve entity from Apache Atlas
     * @return properties to pass to Egeria
     */
    protected ProcessProperties getEgeriaProcessProperties(AtlasEntity atlasEntity)
    {
        ProcessProperties processProperties = super.getProcessProperties(atlasEntity, egeriaProcessTypeName);

        List<String> excludedProperties = new ArrayList<>(atlasAssetProperties);

        excludedProperties.add(atlasProcessInputsPropertyName);
        excludedProperties.add(atlasProcessOutputsPropertyName);

        processProperties.setAdditionalProperties(addRemainingPropertiesToAdditionalProperties(atlasEntity.getAttributes(),
                                                                                               excludedProperties));

        return processProperties;
    }



    /**
     * Create the database in the open metadata ecosystem.
     *
     * @param atlasProcessEntity entity retrieved from Apache Atlas
     *
     * @return unique identifier of the database entity in open metadata
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException unable to communicate with Egeria
     */
    protected String createAtlasProcessInEgeria(AtlasEntityWithExtInfo  atlasProcessEntity) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException
    {
        final String methodName = "createAtlasProcessInEgeria";

        if (atlasProcessEntity != null)
        {
            AtlasEntity atlasEntity = atlasProcessEntity.getEntity();

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

                ProcessProperties processProperties = this.getEgeriaProcessProperties(atlasEntity);

                NewElementOptions newElementOptions = new NewElementOptions(assetClient.getMetadataSourceOptions());

                newElementOptions.setIsOwnAnchor(true);
                newElementOptions.setInitialStatus(ElementStatus.ACTIVE);

                String egeriaProcessGUID = assetClient.createAsset(newElementOptions,
                                                                   null,
                                                                   processProperties,
                                                                   null);

                auditLog.logMessage(methodName,
                                    AtlasIntegrationAuditCode.CREATING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                                                                                          egeriaProcessTypeName,
                                                                                                          egeriaProcessGUID,
                                                                                                          atlasEntity.getTypeName(),
                                                                                                          atlasEntity.getGuid()));

                saveEgeriaGUIDInAtlas(atlasEntity.getGuid(),
                                      egeriaProcessGUID,
                                      processProperties.getQualifiedName(),
                                      egeriaProcessTypeName,
                                      false,
                                      false);

                setOwner(atlasProcessEntity, egeriaProcessGUID);

                return egeriaProcessGUID;
            }
        }

        return null;
    }


    /**
     * Update the properties of an open metadata database with the current properties from Apache Atlas.
     *
     * @param atlasProcessEntity entity retrieved from Apache Atlas
     * @param egeriaProcessGUID unique identifier of the equivalent entity in the open metadata ecosystem
     * @throws InvalidParameterException invalid parameter - probably a logic error
     * @throws UserNotAuthorizedException security problem
     * @throws PropertyServerException unable to communicate with Egeria
     */
    protected void   updateAtlasProcessInEgeria(AtlasEntityWithExtInfo atlasProcessEntity,
                                                String                 egeriaProcessGUID) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        final String methodName = "updateAtlasProcessInEgeria";

        if (atlasProcessEntity != null)
        {
            AtlasEntity             atlasEntity    = atlasProcessEntity.getEntity();
            OpenMetadataRootElement processElement = assetClient.getAssetByGUID(egeriaProcessGUID, null);

            if (atlasEntity != null)
            {
                if (this.egeriaUpdateRequired(processElement, atlasEntity))
                {
                    ProcessProperties processProperties = this.getEgeriaProcessProperties(atlasEntity);

                    if (assetClient.updateAsset(egeriaProcessGUID, assetClient.getUpdateOptions(true), processProperties))
                    {
                        auditLog.logMessage(methodName,
                                            AtlasIntegrationAuditCode.UPDATING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                                                                                                  atlasEntity.getTypeName(),
                                                                                                                  atlasEntity.getGuid(),
                                                                                                                  egeriaProcessTypeName,
                                                                                                                  egeriaProcessGUID));
                    }
                }
            }
        }
    }
}