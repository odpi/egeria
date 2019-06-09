/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.events;


import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;

/**
 * OMRSTypeDefEventProcessor is an interface implemented by a component that is able to process incoming
 * TypeDef events for an Open Metadata Repository.  TypeDef events are used to synchronize TypeDefs across
 * an Open Metadata Repository Cohort.
 */
public interface OMRSTypeDefEventProcessorInterface
{
    /**
     * Send the TypeDef event to the OMRS Topic connector (providing TypeDef Events are enabled).
     *
     * @param sourceName    source of the event
     * @param typeDefEvent  properties of the event to send
     */
    void sendTypeDefEvent(String sourceName,
                          OMRSTypeDefEvent typeDefEvent);


    /*
     * ================================================
     * Processor methods requesting specific events.
     */

    /**
     * A new TypeDef has been defined in an open metadata repository.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param typeDef  details of the new TypeDef.
     */
    void processNewTypeDefEvent(String sourceName,
                                String originatorMetadataCollectionId,
                                String originatorServerName,
                                String originatorServerType,
                                String originatorOrganizationName,
                                TypeDef typeDef);


    /**
     * A new AttributeTypeDef has been defined in an open metadata repository.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param attributeTypeDef  details of the new AttributeTypeDef.
     */
    void processNewAttributeTypeDefEvent(String sourceName,
                                         String originatorMetadataCollectionId,
                                         String originatorServerName,
                                         String originatorServerType,
                                         String originatorOrganizationName,
                                         AttributeTypeDef attributeTypeDef);


    /**
     * An existing TypeDef has been updated in an open metadata repository.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param typeDefPatch  details of the new version of the TypeDef
     */
    void processUpdatedTypeDefEvent(String sourceName,
                                    String originatorMetadataCollectionId,
                                    String originatorServerName,
                                    String originatorServerType,
                                    String originatorOrganizationName,
                                    TypeDefPatch typeDefPatch);


    /**
     * An existing TypeDef has been deleted in an open metadata repository.  Both the name and the
     * GUID are provided to ensure the right TypeDef is deleted in other cohort member repositories.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param typeDefGUID  unique identifier of the TypeDef
     * @param typeDefName  unique name of the TypeDef
     */
    void processDeletedTypeDefEvent(String sourceName,
                                    String originatorMetadataCollectionId,
                                    String originatorServerName,
                                    String originatorServerType,
                                    String originatorOrganizationName,
                                    String typeDefGUID,
                                    String typeDefName);


    /**
     * An existing AttributeTypeDef has been deleted in an open metadata repository.  Both the name and the
     * GUID are provided to ensure the right AttributeTypeDef is deleted in other cohort member repositories.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param attributeTypeDefGUID  unique identifier of the AttributeTypeDef
     * @param attributeTypeDefName  unique name of the AttributeTypeDef
     */
    void processDeletedAttributeTypeDefEvent(String sourceName,
                                             String originatorMetadataCollectionId,
                                             String originatorServerName,
                                             String originatorServerType,
                                             String originatorOrganizationName,
                                             String attributeTypeDefGUID,
                                             String attributeTypeDefName);


    /**
     * Process an event that changes either the name or guid of a TypeDef.  It is resolving a Conflicting TypeDef Error.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param originalTypeDef  description of original TypeDef
     * @param typeDef  updated TypeDef with new identifiers inside.
     */
    void processReIdentifiedTypeDefEvent(String sourceName,
                                         String originatorMetadataCollectionId,
                                         String originatorServerName,
                                         String originatorServerType,
                                         String originatorOrganizationName,
                                         TypeDefSummary originalTypeDef,
                                         TypeDef typeDef);


    /**
     * Process an event that changes either the name or guid of an AttributeTypeDef.
     * It is resolving a Conflicting AttributeTypeDef Error.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param originalAttributeTypeDef  description of original AttributeTypeDef
     * @param attributeTypeDef  updated AttributeTypeDef with new identifiers inside.
     */
    void processReIdentifiedAttributeTypeDefEvent(String sourceName,
                                                  String originatorMetadataCollectionId,
                                                  String originatorServerName,
                                                  String originatorServerType,
                                                  String originatorOrganizationName,
                                                  AttributeTypeDef originalAttributeTypeDef,
                                                  AttributeTypeDef attributeTypeDef);


    /**
     * Process a detected conflict in the type definitions (TypeDefs) used in the cohort.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param originatorTypeDef  description of the TypeDef in the event originator.
     * @param otherMetadataCollectionId  the metadataCollection using the conflicting TypeDef.
     * @param conflictingTypeDef  description of the TypeDef in the other metadata collection.
     * @param errorMessage  details of the error that occurs when the connection is used.
     */
    void processTypeDefConflictEvent(String sourceName,
                                     String originatorMetadataCollectionId,
                                     String originatorServerName,
                                     String originatorServerType,
                                     String originatorOrganizationName,
                                     TypeDefSummary originatorTypeDef,
                                     String otherMetadataCollectionId,
                                     TypeDefSummary conflictingTypeDef,
                                     String errorMessage);


    /**
     * Process a detected conflict in the attribute type definitions (AttributeTypeDefs) used in the cohort.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param originatorAttributeTypeDef description of the AttributeTypeDef in the event originator.
     * @param otherMetadataCollectionId  the metadataCollection using the conflicting AttributeTypeDef.
     * @param conflictingAttributeTypeDef  description of the AttributeTypeDef in the other metadata collection.
     * @param errorMessage  details of the error that occurs when the connection is used.
     */
    void processAttributeTypeDefConflictEvent(String sourceName,
                                              String originatorMetadataCollectionId,
                                              String originatorServerName,
                                              String originatorServerType,
                                              String originatorOrganizationName,
                                              AttributeTypeDef originatorAttributeTypeDef,
                                              String otherMetadataCollectionId,
                                              AttributeTypeDef conflictingAttributeTypeDef,
                                              String errorMessage);


    /**
     * A TypeDef from another member in the cohort is at a different version than the local repository.  This may
     * create some inconsistencies in the different copies of instances of this type in different members of the
     * cohort.  The recommended action is to update all TypeDefs to the latest version.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param targetMetadataCollectionId  identifier of the metadata collection that is reporting a TypeDef at a
     *                                   different level to the reporting repository.
     * @param targetTypeDef  details of the TypeDef in the target repository.
     * @param otherTypeDef  details of the TypeDef in the other repository.
     * @param errorMessage  details of the error that occurs when the connection is used.
     */
    void processTypeDefPatchMismatchEvent(String sourceName,
                                          String originatorMetadataCollectionId,
                                          String originatorServerName,
                                          String originatorServerType,
                                          String originatorOrganizationName,
                                          String targetMetadataCollectionId,
                                          TypeDefSummary targetTypeDef,
                                          TypeDef otherTypeDef,
                                          String errorMessage);
}
