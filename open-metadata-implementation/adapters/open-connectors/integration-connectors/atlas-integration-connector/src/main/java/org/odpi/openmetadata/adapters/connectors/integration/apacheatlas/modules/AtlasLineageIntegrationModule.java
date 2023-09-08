/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.modules;


import org.odpi.openmetadata.accessservices.assetmanager.metadataelements.ProcessElement;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ExternalIdentifierProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ProcessProperties;
import org.odpi.openmetadata.accessservices.assetmanager.properties.ProcessStatus;
import org.odpi.openmetadata.accessservices.assetmanager.properties.SynchronizationDirection;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.ffdc.ApacheAtlasAuditCode;
import org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.ffdc.ApacheAtlasErrorCode;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasEntity;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasEntityHeader;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties.AtlasEntityWithExtInfo;
import org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.ApacheAtlasRESTConnector;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.properties.ConnectionProperties;
import org.odpi.openmetadata.frameworks.integration.contextmanager.PermittedSynchronization;
import org.odpi.openmetadata.integrationservices.catalog.connector.CatalogIntegratorContext;
import org.odpi.openmetadata.integrationservices.catalog.connector.LineageExchangeService;

import java.util.ArrayList;
import java.util.List;

/**
 * AtlasLineageIntegrationModule synchronizes lineage from Apache Atlas to the open metadata ecosystem.
 * It is called after the registered integration modules have established the key assets into the open metadata ecosystem.
 */
public class AtlasLineageIntegrationModule extends AtlasIntegrationModuleBase
{
    private static final String lineageModuleName = "Apache Atlas Lineage Integration Module";

    protected final static String egeriaDataSetTypeName           = "DataSet";
    protected final static String egeriaProcessTypeName           = "DeployedSoftwareComponent";
    protected final static String atlasProcessTypeName            = "Process";
    protected final static String atlasProcessInputsPropertyName  = "inputs";
    protected final static String atlasProcessOutputsPropertyName = "outputs";


    private final LineageExchangeService lineageExchangeService;



    /**
     * Constructor for the module is supplied with the runtime context in order to operate.
     *
     * @param connectorName name of the connector (for messages)
     * @param connectionProperties connection properties used to start the connector
     * @param auditLog logging destination
     * @param myContext integration context
     * @param targetRootURL URL to connect to Apache Atlas
     * @param atlasClient client to connect to Apache Atlas
     * @param embeddedConnectors list of any embedded connectors (such as secrets connector and topic connector
     * @throws UserNotAuthorizedException security problem
     */
    public AtlasLineageIntegrationModule(String                   connectorName,
                                         ConnectionProperties     connectionProperties,
                                         AuditLog                 auditLog,
                                         CatalogIntegratorContext myContext,
                                         String                   targetRootURL,
                                         ApacheAtlasRESTConnector atlasClient,
                                         List<Connector>          embeddedConnectors) throws UserNotAuthorizedException
    {
        super(connectorName,
              lineageModuleName,
              connectionProperties,
              auditLog,
              myContext,
              targetRootURL,
              atlasClient,
              embeddedConnectors);

        this.lineageExchangeService = myContext.getLineageExchangeService();

        /*
         * Deduplication is turned off so that the connector works with the entities it created rather than
         * entities from other systems that have been linked as duplicates.
         */
        this.lineageExchangeService.setForDuplicateProcessing(true);
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
                                                                ApacheAtlasAuditCode.ADDING_LINEAGE.getMessageDefinition(connectorName,
                                                                                                                         egeriaDataSetTypeName,
                                                                                                                         egeriaDataSetGUID,
                                                                                                                         egeriaProcessTypeName,
                                                                                                                         egeriaProcessGUID));
                                            lineageExchangeService.setupDataFlow(true,
                                                                                 egeriaDataSetGUID,
                                                                                 egeriaProcessGUID,
                                                                                 null,
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
                                                                ApacheAtlasAuditCode.ADDING_LINEAGE.getMessageDefinition(connectorName,
                                                                                                                         egeriaProcessTypeName,
                                                                                                                         egeriaProcessGUID,
                                                                                                                         egeriaDataSetTypeName,
                                                                                                                         egeriaDataSetGUID));
                                            lineageExchangeService.setupDataFlow(true,
                                                                                 egeriaProcessGUID,
                                                                                 egeriaDataSetGUID,
                                                                                 null,
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
                                          ApacheAtlasAuditCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
                                                                                                         error.getClass().getName(),
                                                                                                         methodName,
                                                                                                         error.getMessage()),
                                          error);
                }

                throw new ConnectorCheckedException(ApacheAtlasErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(connectorName,
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
                    dataAssetExchangeService.getDataAssetByGUID(egeriaProcessGUID, null);

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
                                        ApacheAtlasAuditCode.REPLACING_EGERIA_ENTITY.getMessageDefinition(connectorName,
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
                ExternalIdentifierProperties externalIdentifierProperties = this.getExternalIdentifier(atlasEntity.getGuid(),
                                                                                                       atlasEntity.getTypeName(),
                                                                                                       atlasEntity.getCreatedBy(),
                                                                                                       atlasEntity.getCreateTime(),
                                                                                                       atlasEntity.getUpdatedBy(),
                                                                                                       atlasEntity.getUpdateTime(),
                                                                                                       atlasEntity.getVersion(),
                                                                                                       SynchronizationDirection.FROM_THIRD_PARTY);

                ProcessProperties processProperties = this.getEgeriaProcessProperties(atlasEntity);

                String egeriaProcessGUID = lineageExchangeService.createProcess(true,
                                                                                externalIdentifierProperties,
                                                                                processProperties,
                                                                                ProcessStatus.ACTIVE);

                auditLog.logMessage(methodName,
                                    ApacheAtlasAuditCode.CREATING_EGERIA_ENTITY.getMessageDefinition(connectorName,
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
            AtlasEntity atlasEntity = atlasProcessEntity.getEntity();
            ProcessElement processElement = lineageExchangeService.getProcessByGUID(egeriaProcessGUID, null);

            if (atlasEntity != null)
            {
                if (this.egeriaUpdateRequired(egeriaProcessGUID,
                                              egeriaProcessTypeName,
                                              processElement,
                                              atlasEntity))
                {
                    ProcessProperties processProperties = this.getEgeriaProcessProperties(atlasEntity);

                    auditLog.logMessage(methodName,
                                        ApacheAtlasAuditCode.UPDATING_EGERIA_ENTITY.getMessageDefinition(connectorName,
                                                                                                         atlasEntity.getTypeName(),
                                                                                                         atlasEntity.getGuid(),
                                                                                                         egeriaProcessTypeName,
                                                                                                         egeriaProcessGUID));

                    lineageExchangeService.updateProcess(egeriaProcessGUID, atlasEntity.getGuid(), false, processProperties, null);
                }
            }
        }
    }
}