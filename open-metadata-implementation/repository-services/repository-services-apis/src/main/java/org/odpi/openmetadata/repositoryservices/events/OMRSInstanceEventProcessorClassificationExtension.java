/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.events;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;

/**
 * OMRSInstanceEventProcessorClassificationExtension adds methods for classifications that include an entity proxy rather than an entity proxy.
 */
public interface OMRSInstanceEventProcessorClassificationExtension
{
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
     * @param entity  proxy of the entity with the new classification added. No guarantee this is all the classifications.
     * @param classification new classification
     */
    void processClassifiedEntityEvent(String         sourceName,
                                      String         originatorMetadataCollectionId,
                                      String         originatorServerName,
                                      String         originatorServerType,
                                      String         originatorOrganizationName,
                                      EntityProxy    entity,
                                      Classification classification);


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
     * @param entity  proxy of the entity after the classification has been removed. No guarantee this is all the classifications.
     * @param originalClassification classification that was removed
     */
    void processDeclassifiedEntityEvent(String         sourceName,
                                        String         originatorMetadataCollectionId,
                                        String         originatorServerName,
                                        String         originatorServerType,
                                        String         originatorOrganizationName,
                                        EntityProxy    entity,
                                        Classification originalClassification);


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
     * @param entity  proxy of the entity after the classification has been changed. No guarantee this is all the classifications.
     * @param originalClassification classification that was removed
     * @param classification new classification
     */
    void processReclassifiedEntityEvent(String         sourceName,
                                        String         originatorMetadataCollectionId,
                                        String         originatorServerName,
                                        String         originatorServerType,
                                        String         originatorOrganizationName,
                                        EntityProxy    entity,
                                        Classification originalClassification,
                                        Classification classification);
}
