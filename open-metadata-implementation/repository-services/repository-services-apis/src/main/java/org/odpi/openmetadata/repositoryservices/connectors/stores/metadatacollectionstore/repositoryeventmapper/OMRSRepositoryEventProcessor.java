/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryeventmapper;


import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventProcessorInterface;
import org.odpi.openmetadata.repositoryservices.events.OMRSTypeDefEventProcessorInterface;

/**
 * OMRSRepositoryEventProcessor describes the interface of a component that can process both TypeDef and Instance
 * events from an open metadata repository.
 */
public abstract class OMRSRepositoryEventProcessor implements OMRSTypeDefEventProcessorInterface,
                                                              OMRSInstanceEventProcessorInterface
{
    protected String  eventProcessorName;

    /**
     * Return the name of the event processor.
     *
     * @return event processor name
     */
    public String getEventProcessorName()
    {
        return eventProcessorName;
    }


    /**
     * Constructor to update the event processor name.
     *
     * @param eventProcessorName string name
     */
    protected OMRSRepositoryEventProcessor(String eventProcessorName)
    {
        this.eventProcessorName = eventProcessorName;
    }


    /**
     * A new classification has been added to an entity.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entity  details of the entity with the new classification added. No guarantee this is all the classifications.
     */
    @Deprecated
    public void processClassifiedEntityEvent(String         sourceName,
                                             String         originatorMetadataCollectionId,
                                             String         originatorServerName,
                                             String         originatorServerType,
                                             String         originatorOrganizationName,
                                             EntityDetail   entity)
    {
        this.processClassifiedEntityEvent(sourceName,
                                          originatorMetadataCollectionId,
                                          originatorServerName,
                                          originatorServerType,
                                          originatorOrganizationName,
                                          entity,
                                          null);
    }


    /**
     * A classification has been removed from an entity.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entity  details of the entity after the classification has been removed. No guarantee this is all the classifications.
     */
    @Deprecated
    public void processDeclassifiedEntityEvent(String         sourceName,
                                               String         originatorMetadataCollectionId,
                                               String         originatorServerName,
                                               String         originatorServerType,
                                               String         originatorOrganizationName,
                                               EntityDetail   entity)
    {
        this.processDeclassifiedEntityEvent(sourceName,
                                            originatorMetadataCollectionId,
                                            originatorServerName,
                                            originatorServerType,
                                            originatorOrganizationName,
                                            entity,
                                            null);
    }


    /**
     * An existing classification has been changed on an entity.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entity  details of the entity after the classification has been changed. No guarantee this is all the classifications.
     */
    @Deprecated
    public void processReclassifiedEntityEvent(String         sourceName,
                                               String         originatorMetadataCollectionId,
                                               String         originatorServerName,
                                               String         originatorServerType,
                                               String         originatorOrganizationName,
                                               EntityDetail   entity)
    {
        this.processReclassifiedEntityEvent(sourceName,
                                            originatorMetadataCollectionId,
                                            originatorServerName,
                                            originatorServerType,
                                            originatorOrganizationName,
                                            entity,
                                            null,
                                            null);
    }
}
