/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.eventmanagement;

import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefSummary;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryeventmapper.OMRSRepositoryEventProcessor;
import org.odpi.openmetadata.repositoryservices.events.*;


/**
 * OMRSRepositoryEventBuilder creates OMRS Events ready to be distributed.
 */
public abstract class OMRSRepositoryEventBuilder extends OMRSRepositoryEventProcessor
{
    /**
     * Constructor to update the event processor name.
     *
     * @param eventProcessorName string name
     */
    OMRSRepositoryEventBuilder(String eventProcessorName)
    {
        super(eventProcessorName);
    }


    /**
     * A new TypeDef has been defined.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param typeDef details of the new TypeDef
     */
    public void processNewTypeDefEvent(String       sourceName,
                                       String       originatorMetadataCollectionId,
                                       String       originatorServerName,
                                       String       originatorServerType,
                                       String       originatorOrganizationName,
                                       TypeDef      typeDef)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSTypeDefEvent   typeDefEvent = new OMRSTypeDefEvent(OMRSTypeDefEventType.NEW_TYPEDEF_EVENT, typeDef);

        typeDefEvent.setEventOriginator(eventOriginator);

        this.sendTypeDefEvent(sourceName, typeDefEvent);
    }


    /**
     * A new AttributeTypeDef has been defined in an open metadata repository.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param attributeTypeDef details of the new AttributeTypeDef.
     */
    public void processNewAttributeTypeDefEvent(String           sourceName,
                                                String           originatorMetadataCollectionId,
                                                String           originatorServerName,
                                                String           originatorServerType,
                                                String           originatorOrganizationName,
                                                AttributeTypeDef attributeTypeDef)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSTypeDefEvent   typeDefEvent = new OMRSTypeDefEvent(OMRSTypeDefEventType.NEW_ATTRIBUTE_TYPEDEF_EVENT, attributeTypeDef);

        typeDefEvent.setEventOriginator(eventOriginator);

        this.sendTypeDefEvent(sourceName, typeDefEvent);
    }


    /**
     * An existing TypeDef has been updated.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param typeDefPatch details of the new version of the TypeDef
     */
    public void processUpdatedTypeDefEvent(String       sourceName,
                                           String       originatorMetadataCollectionId,
                                           String       originatorServerName,
                                           String       originatorServerType,
                                           String       originatorOrganizationName,
                                           TypeDefPatch typeDefPatch)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSTypeDefEvent   typeDefEvent = new OMRSTypeDefEvent(OMRSTypeDefEventType.UPDATED_TYPEDEF_EVENT, typeDefPatch);

        typeDefEvent.setEventOriginator(eventOriginator);

        this.sendTypeDefEvent(sourceName, typeDefEvent);
    }


    /**
     * An existing TypeDef has been deleted.  Both the name and the GUID are provided to ensure the right TypeDef is
     * deleted in remote repositories.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param typeDefGUID unique identifier of the TypeDef
     * @param typeDefName unique name of the TypeDef
     */
    public void processDeletedTypeDefEvent(String       sourceName,
                                           String       originatorMetadataCollectionId,
                                           String       originatorServerName,
                                           String       originatorServerType,
                                           String       originatorOrganizationName,
                                           String       typeDefGUID,
                                           String       typeDefName)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSTypeDefEvent typeDefEvent = new OMRSTypeDefEvent(OMRSTypeDefEventType.DELETED_TYPEDEF_EVENT,
                                                             typeDefGUID,
                                                             typeDefName);

        typeDefEvent.setEventOriginator(eventOriginator);

        this.sendTypeDefEvent(sourceName, typeDefEvent);
    }


    /**
     * An existing AttributeTypeDef has been deleted in an open metadata repository.  Both the name and the
     * GUID are provided to ensure the right AttributeTypeDef is deleted in other cohort member repositories.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param attributeTypeDefGUID unique identifier of the AttributeTypeDef
     * @param attributeTypeDefName unique name of the AttributeTypeDef
     */
    public void processDeletedAttributeTypeDefEvent(String      sourceName,
                                                    String      originatorMetadataCollectionId,
                                                    String      originatorServerName,
                                                    String      originatorServerType,
                                                    String      originatorOrganizationName,
                                                    String      attributeTypeDefGUID,
                                                    String      attributeTypeDefName)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSTypeDefEvent typeDefEvent = new OMRSTypeDefEvent(OMRSTypeDefEventType.DELETED_ATTRIBUTE_TYPEDEF_EVENT,
                                                             attributeTypeDefGUID,
                                                             attributeTypeDefName);

        typeDefEvent.setEventOriginator(eventOriginator);

        this.sendTypeDefEvent(sourceName, typeDefEvent);
    }


    /**
     * The guid or name of an existing TypeDef has been changed to a new value.  This is used if two different
     * Typedefs are discovered to have either the same guid or, most likely, the same name.  This type of conflict
     * is rare but typically occurs when a new repository joins the cohort.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param originalTypeDefSummary the details for the original TypeDef.
     * @param typeDef updated TypeDef with new identifiers
     */
    public void processReIdentifiedTypeDefEvent(String         sourceName,
                                                String         originatorMetadataCollectionId,
                                                String         originatorServerName,
                                                String         originatorServerType,
                                                String         originatorOrganizationName,
                                                TypeDefSummary originalTypeDefSummary,
                                                TypeDef        typeDef)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);


        OMRSTypeDefEvent typeDefEvent = new OMRSTypeDefEvent(OMRSTypeDefEventType.RE_IDENTIFIED_TYPEDEF_EVENT,
                                                             originalTypeDefSummary,
                                                             typeDef);

        typeDefEvent.setEventOriginator(eventOriginator);

        this.sendTypeDefEvent(sourceName, typeDefEvent);
    }


    /**
     * Process an event that changes either the name or guid of an AttributeTypeDef.
     * It is resolving a Conflicting AttributeTypeDef Error.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param originalAttributeTypeDef description of original AttributeTypeDef
     * @param attributeTypeDef updated AttributeTypeDef with new identifiers inside.
     */
    public void processReIdentifiedAttributeTypeDefEvent(String           sourceName,
                                                         String           originatorMetadataCollectionId,
                                                         String           originatorServerName,
                                                         String           originatorServerType,
                                                         String           originatorOrganizationName,
                                                         AttributeTypeDef originalAttributeTypeDef,
                                                         AttributeTypeDef attributeTypeDef)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);


        OMRSTypeDefEvent typeDefEvent = new OMRSTypeDefEvent(OMRSTypeDefEventType.RE_IDENTIFIED_ATTRIBUTE_TYPEDEF_EVENT,
                                                             originalAttributeTypeDef,
                                                             attributeTypeDef);

        typeDefEvent.setEventOriginator(eventOriginator);

        this.sendTypeDefEvent(sourceName, typeDefEvent);
    }


    /**
     * Process a detected conflict in type definitions (TypeDefs) used in the cohort.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param originatorTypeDefSummary details of the TypeDef in the event originator
     * @param otherMetadataCollectionId the metadataCollection using the conflicting TypeDef
     * @param conflictingTypeDefSummary the details of the TypeDef in the other metadata collection
     * @param errorMessage details of the error that occurs when the connection is used.
     */
    public void processTypeDefConflictEvent(String         sourceName,
                                            String         originatorMetadataCollectionId,
                                            String         originatorServerName,
                                            String         originatorServerType,
                                            String         originatorOrganizationName,
                                            TypeDefSummary originatorTypeDefSummary,
                                            String         otherMetadataCollectionId,
                                            TypeDefSummary conflictingTypeDefSummary,
                                            String         errorMessage)

    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSTypeDefEvent typeDefEvent = new OMRSTypeDefEvent(OMRSTypeDefEventErrorCode.CONFLICTING_TYPEDEFS,
                                                             errorMessage,
                                                             originatorTypeDefSummary,
                                                             otherMetadataCollectionId,
                                                             conflictingTypeDefSummary);

        typeDefEvent.setEventOriginator(eventOriginator);

        this.sendTypeDefEvent(sourceName, typeDefEvent);
    }


    /**
     * Process a detected conflict in the attribute type definitions (AttributeTypeDefs) used in the cohort.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param originatorAttributeTypeDef- description of the AttributeTypeDef in the event originator.
     * @param otherMetadataCollectionId the metadataCollection using the conflicting AttributeTypeDef.
     * @param conflictingAttributeTypeDef description of the AttributeTypeDef in the other metadata collection.
     * @param errorMessage details of the error that occurs when the connection is used.
     */
    public void processAttributeTypeDefConflictEvent(String           sourceName,
                                                     String           originatorMetadataCollectionId,
                                                     String           originatorServerName,
                                                     String           originatorServerType,
                                                     String           originatorOrganizationName,
                                                     AttributeTypeDef originatorAttributeTypeDef,
                                                     String           otherMetadataCollectionId,
                                                     AttributeTypeDef conflictingAttributeTypeDef,
                                                     String           errorMessage)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSTypeDefEvent typeDefEvent = new OMRSTypeDefEvent(OMRSTypeDefEventErrorCode.CONFLICTING_ATTRIBUTE_TYPEDEFS,
                                                             errorMessage,
                                                             originatorAttributeTypeDef,
                                                             otherMetadataCollectionId,
                                                             conflictingAttributeTypeDef);

        typeDefEvent.setEventOriginator(eventOriginator);

        this.sendTypeDefEvent(sourceName, typeDefEvent);
    }


    /**
     * A TypeDef from another member in the cohort is at a different version than the local repository.  This may
     * create some inconsistencies in the different copies of instances of this type in different members of the
     * cohort.  The recommended action is to update all TypeDefs to the latest version.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param targetMetadataCollectionId identifier of the metadata collection that is reporting a TypeDef at a
     *                                   different level to the local repository.
     * @param targetTypeDefSummary details of the TypeDef being patched
     * @param otherTypeDef details of the TypeDef in the local repository.
     */
    public void processTypeDefPatchMismatchEvent(String         sourceName,
                                                 String         originatorMetadataCollectionId,
                                                 String         originatorServerName,
                                                 String         originatorServerType,
                                                 String         originatorOrganizationName,
                                                 String         targetMetadataCollectionId,
                                                 TypeDefSummary targetTypeDefSummary,
                                                 TypeDef        otherTypeDef,
                                                 String         errorMessage)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);



        OMRSTypeDefEvent typeDefEvent = new OMRSTypeDefEvent(OMRSTypeDefEventErrorCode.TYPEDEF_PATCH_MISMATCH,
                                                             errorMessage,
                                                             targetMetadataCollectionId,
                                                             targetTypeDefSummary,
                                                             otherTypeDef);

        typeDefEvent.setEventOriginator(eventOriginator);

        this.sendTypeDefEvent(sourceName, typeDefEvent);
    }


    /**
     * A new entity has been created.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param entity details of the new entity
     */
    public void processNewEntityEvent(String       sourceName,
                                      String       originatorMetadataCollectionId,
                                      String       originatorServerName,
                                      String       originatorServerType,
                                      String       originatorOrganizationName,
                                      EntityDetail entity)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.NEW_ENTITY_EVENT,
                                                                entity);


        instanceEvent.setEventOriginator(eventOriginator);

        this.sendInstanceEvent(sourceName, instanceEvent);
    }


    /**
     * An existing entity has been updated.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param oldEntity  original values for the entity.
     * @param newEntity details of the new version of the entity.
     */
    public void processUpdatedEntityEvent(String       sourceName,
                                          String       originatorMetadataCollectionId,
                                          String       originatorServerName,
                                          String       originatorServerType,
                                          String       originatorOrganizationName,
                                          EntityDetail oldEntity,
                                          EntityDetail newEntity)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.UPDATED_ENTITY_EVENT,
                                                                oldEntity,
                                                                newEntity);


        instanceEvent.setEventOriginator(eventOriginator);

        this.sendInstanceEvent(sourceName, instanceEvent);
    }


    /**
     * An update to an entity has been undone.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param entity details of the version of the entity that has been restored.
     */
    public void processUndoneEntityEvent(String       sourceName,
                                         String       originatorMetadataCollectionId,
                                         String       originatorServerName,
                                         String       originatorServerType,
                                         String       originatorOrganizationName,
                                         EntityDetail entity)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.UNDONE_ENTITY_EVENT,
                                                                entity);


        instanceEvent.setEventOriginator(eventOriginator);

        this.sendInstanceEvent(sourceName, instanceEvent);
    }


    /**
     * A new classification has been added to an entity.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param entity details of the entity with the new classification added.
     * @param classification new classification
     */
    public void processClassifiedEntityEvent(String         sourceName,
                                             String         originatorMetadataCollectionId,
                                             String         originatorServerName,
                                             String         originatorServerType,
                                             String         originatorOrganizationName,
                                             EntityDetail   entity,
                                             Classification classification)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.CLASSIFIED_ENTITY_EVENT,
                                                                entity,
                                                                null,
                                                                classification);


        instanceEvent.setEventOriginator(eventOriginator);

        this.sendInstanceEvent(sourceName, instanceEvent);
    }


    /**
     * A classification has been removed from an entity.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param entity details of the entity after the classification has been removed.
     * @param originalClassification classification that was removed
     */
    public void processDeclassifiedEntityEvent(String         sourceName,
                                               String         originatorMetadataCollectionId,
                                               String         originatorServerName,
                                               String         originatorServerType,
                                               String         originatorOrganizationName,
                                               EntityDetail   entity,
                                               Classification originalClassification)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.DECLASSIFIED_ENTITY_EVENT,
                                                                entity,
                                                                originalClassification,
                                                                null);


        instanceEvent.setEventOriginator(eventOriginator);

        this.sendInstanceEvent(sourceName, instanceEvent);
    }


    /**
     * An existing classification has been changed on an entity.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param entity details of the entity after the classification has been changed.
     * @param originalClassification classification that was removed
     * @param classification new classification
     */
    public void processReclassifiedEntityEvent(String         sourceName,
                                               String         originatorMetadataCollectionId,
                                               String         originatorServerName,
                                               String         originatorServerType,
                                               String         originatorOrganizationName,
                                               EntityDetail   entity,
                                               Classification originalClassification,
                                               Classification classification)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.RECLASSIFIED_ENTITY_EVENT,
                                                                entity,
                                                                originalClassification,
                                                                classification);


        instanceEvent.setEventOriginator(eventOriginator);

        this.sendInstanceEvent(sourceName, instanceEvent);
    }


    /**
     * An existing entity has been deleted.  This is a soft delete. This means it is still in the repository
     * but it is no longer returned on queries.
     *
     * All relationships to the entity are also soft-deleted and will no longer be usable.  These deleted relationships
     * will be notified through separate events.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param entity deleted entity
     */
    public void processDeletedEntityEvent(String       sourceName,
                                          String       originatorMetadataCollectionId,
                                          String       originatorServerName,
                                          String       originatorServerType,
                                          String       originatorOrganizationName,
                                          EntityDetail entity)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.DELETED_ENTITY_EVENT,
                                                                entity);

        instanceEvent.setEventOriginator(eventOriginator);

        this.sendInstanceEvent(sourceName, instanceEvent);
    }


    /**
     * A deleted entity has been restored to the state it was before it was deleted.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param entity details of the version of the entity that has been restored.
     */
    public void processRestoredEntityEvent(String       sourceName,
                                           String       originatorMetadataCollectionId,
                                           String       originatorServerName,
                                           String       originatorServerType,
                                           String       originatorOrganizationName,
                                           EntityDetail entity)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.RESTORED_ENTITY_EVENT,
                                                                entity);


        instanceEvent.setEventOriginator(eventOriginator);

        this.sendInstanceEvent(sourceName, instanceEvent);
    }


    /**
     * An entity has been permanently removed from the repository.  This request can not be undone.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param entity  details of the version of the entity that has been purged.
     */
    public void processPurgedEntityEvent(String       sourceName,
                                         String       originatorMetadataCollectionId,
                                         String       originatorServerName,
                                         String       originatorServerType,
                                         String       originatorOrganizationName,
                                         EntityDetail entity)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.PURGED_ENTITY_EVENT,
                                                                entity);


        instanceEvent.setEventOriginator(eventOriginator);

        this.sendInstanceEvent(sourceName, instanceEvent);
    }


    /**
     * A deleted entity has been permanently removed from the repository.  This request can not be undone.
     *
     * Details of the TypeDef are included with the entity's unique id (guid) to ensure the right entity is purged in
     * the remote repositories.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param typeDefGUID unique identifier for this entity's TypeDef
     * @param typeDefName name of this entity's TypeDef
     * @param instanceGUID unique identifier for the entity
     */
    public void processPurgedEntityEvent(String       sourceName,
                                         String       originatorMetadataCollectionId,
                                         String       originatorServerName,
                                         String       originatorServerType,
                                         String       originatorOrganizationName,
                                         String       typeDefGUID,
                                         String       typeDefName,
                                         String       instanceGUID)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.PURGED_ENTITY_EVENT,
                                                                typeDefGUID,
                                                                typeDefName,
                                                                instanceGUID);

        instanceEvent.setEventOriginator(eventOriginator);

        this.sendInstanceEvent(sourceName, instanceEvent);
    }


    /**
     * An existing entity has been deleted and purged. This request can not be undone.
     *
     * All relationships to the entity are also soft-deleted and will no longer be usable.  These deleted relationships
     * will be notified through separate events.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param entity deleted entity
     */
    public void processDeletePurgedEntityEvent(String       sourceName,
                                               String       originatorMetadataCollectionId,
                                               String       originatorServerName,
                                               String       originatorServerType,
                                               String       originatorOrganizationName,
                                               EntityDetail entity)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.DELETE_PURGED_ENTITY_EVENT, entity);

        instanceEvent.setEventOriginator(eventOriginator);

        this.sendInstanceEvent(sourceName, instanceEvent);
    }


    /**
     * The guid of an existing entity has been changed to a new value.  This is used if two different
     * entities are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param originalEntityGUID the existing identifier for the entity.
     * @param entity new values for this entity, including the new guid.
     */
    public void processReIdentifiedEntityEvent(String       sourceName,
                                               String       originatorMetadataCollectionId,
                                               String       originatorServerName,
                                               String       originatorServerType,
                                               String       originatorOrganizationName,
                                               String       originalEntityGUID,
                                               EntityDetail entity)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.RE_IDENTIFIED_ENTITY_EVENT,
                                                                entity);

        instanceEvent.setEventOriginator(eventOriginator);
        instanceEvent.setOriginalInstanceGUID(originalEntityGUID);

        this.sendInstanceEvent(sourceName, instanceEvent);
    }


    /**
     * An existing entity has had its type changed.  Typically this action is taken to move an entity's
     * type to either a super type (so the subtype can be deleted) or a new subtype (so additional properties can be
     * added.)  However, the type can be changed to any compatible type.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param originalTypeDefSummary original details for this entity's TypeDef.
     * @param entity new values for this entity, including the new type information.
     */
    public void processReTypedEntityEvent(String         sourceName,
                                          String         originatorMetadataCollectionId,
                                          String         originatorServerName,
                                          String         originatorServerType,
                                          String         originatorOrganizationName,
                                          TypeDefSummary originalTypeDefSummary,
                                          EntityDetail   entity)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.RETYPED_ENTITY_EVENT,
                                                                entity);

        instanceEvent.setEventOriginator(eventOriginator);
        instanceEvent.setOriginalTypeDefSummary(originalTypeDefSummary);

        this.sendInstanceEvent(sourceName, instanceEvent);
    }


    /**
     * An existing entity has changed home repository.  This action is taken for example, if a repository
     * becomes permanently unavailable, or if the user community updating this entity move to working
     * from a different repository in the open metadata repository cohort.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param originalHomeMetadataCollectionId unique identifier for the original home repository.
     * @param entity new values for this entity, including the new home information.
     */
    public void processReHomedEntityEvent(String       sourceName,
                                          String       originatorMetadataCollectionId,
                                          String       originatorServerName,
                                          String       originatorServerType,
                                          String       originatorOrganizationName,
                                          String       originalHomeMetadataCollectionId,
                                          EntityDetail entity)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.RE_HOMED_ENTITY_EVENT,
                                                                entity);

        instanceEvent.setEventOriginator(eventOriginator);
        instanceEvent.setOriginalHomeMetadataCollectionId(originalHomeMetadataCollectionId);

        this.sendInstanceEvent(sourceName, instanceEvent);
    }


    /**
     * The local repository is requesting that an entity from another repository's metadata collection is
     * refreshed so the local repository can create a reference copy.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param typeDefGUID unique identifier for this entity's TypeDef
     * @param typeDefName name of this entity's TypeDef
     * @param instanceGUID unique identifier for the entity
     * @param homeMetadataCollectionId metadata collection id for the home of this instance.
     */
    public void processRefreshEntityRequested(String       sourceName,
                                              String       originatorMetadataCollectionId,
                                              String       originatorServerName,
                                              String       originatorServerType,
                                              String       originatorOrganizationName,
                                              String       typeDefGUID,
                                              String       typeDefName,
                                              String       instanceGUID,
                                              String       homeMetadataCollectionId)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.REFRESH_ENTITY_REQUEST,
                                                                typeDefGUID,
                                                                typeDefName,
                                                                instanceGUID);

        instanceEvent.setEventOriginator(eventOriginator);
        instanceEvent.setHomeMetadataCollectionId(homeMetadataCollectionId);

        this.sendInstanceEvent(sourceName, instanceEvent);
    }


    /**
     * A remote repository in the cohort has sent entity details in response to a refresh request.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param entity details of the requested entity
     */
    public void processRefreshEntityEvent(String       sourceName,
                                          String       originatorMetadataCollectionId,
                                          String       originatorServerName,
                                          String       originatorServerType,
                                          String       originatorOrganizationName,
                                          EntityDetail entity)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.REFRESHED_ENTITY_EVENT,
                                                                entity);

        instanceEvent.setEventOriginator(eventOriginator);

        this.sendInstanceEvent(sourceName, instanceEvent);
    }


    /**
     * A new relationship has been created.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param relationship details of the new relationship
     */
    public void processNewRelationshipEvent(String       sourceName,
                                            String       originatorMetadataCollectionId,
                                            String       originatorServerName,
                                            String       originatorServerType,
                                            String       originatorOrganizationName,
                                            Relationship relationship)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.NEW_RELATIONSHIP_EVENT,
                                                                relationship);

        instanceEvent.setEventOriginator(eventOriginator);

        this.sendInstanceEvent(sourceName, instanceEvent);
    }


    /**
     * An existing relationship has been updated.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param oldRelationship original details of the relationship.
     * @param newRelationship details of the new version of the relationship.
     */
    public void processUpdatedRelationshipEvent(String       sourceName,
                                                String       originatorMetadataCollectionId,
                                                String       originatorServerName,
                                                String       originatorServerType,
                                                String       originatorOrganizationName,
                                                Relationship oldRelationship,
                                                Relationship newRelationship)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.UPDATED_RELATIONSHIP_EVENT,
                                                                oldRelationship,
                                                                newRelationship);

        instanceEvent.setEventOriginator(eventOriginator);

        this.sendInstanceEvent(sourceName, instanceEvent);
    }


    /**
     * An update to a relationship has been undone.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param relationship details of the version of the relationship that has been restored.
     */
    public void processUndoneRelationshipEvent(String       sourceName,
                                               String       originatorMetadataCollectionId,
                                               String       originatorServerName,
                                               String       originatorServerType,
                                               String       originatorOrganizationName,
                                               Relationship relationship)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.UNDONE_RELATIONSHIP_EVENT,
                                                                relationship);

        instanceEvent.setEventOriginator(eventOriginator);

        this.sendInstanceEvent(sourceName, instanceEvent);
    }


    /**
     * An existing relationship has been deleted.  This is a soft delete. This means it is still in the repository
     * but it is no longer returned on queries.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param relationship deleted relationship
     */
    public void processDeletedRelationshipEvent(String       sourceName,
                                                String       originatorMetadataCollectionId,
                                                String       originatorServerName,
                                                String       originatorServerType,
                                                String       originatorOrganizationName,
                                                Relationship relationship)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.DELETED_RELATIONSHIP_EVENT,
                                                                relationship);

        instanceEvent.setEventOriginator(eventOriginator);

        this.sendInstanceEvent(sourceName, instanceEvent);
    }


    /**
     * A deleted relationship has been restored to the state it was before it was deleted.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param relationship details of the version of the relationship that has been restored.
     */
    public void processRestoredRelationshipEvent(String       sourceName,
                                                 String       originatorMetadataCollectionId,
                                                 String       originatorServerName,
                                                 String       originatorServerType,
                                                 String       originatorOrganizationName,
                                                 Relationship relationship)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.RESTORED_RELATIONSHIP_EVENT,
                                                                relationship);

        instanceEvent.setEventOriginator(eventOriginator);

        this.sendInstanceEvent(sourceName, instanceEvent);
    }


    /**
     * A relationship has been permanently removed from the repository.  This request can not be undone.
     *
     * @param sourceName  name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId  unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName  name of the server that the event came from.
     * @param originatorServerType  type of server that the event came from.
     * @param originatorOrganizationName  name of the organization that owns the server that sent the event.
     * @param relationship  details of the  relationship that has been purged.
     */
    public void processPurgedRelationshipEvent(String       sourceName,
                                               String       originatorMetadataCollectionId,
                                               String       originatorServerName,
                                               String       originatorServerType,
                                               String       originatorOrganizationName,
                                               Relationship relationship)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.PURGED_RELATIONSHIP_EVENT,
                                                                relationship);

        instanceEvent.setEventOriginator(eventOriginator);

        this.sendInstanceEvent(sourceName, instanceEvent);
    }


    /**
     * A deleted relationship has been permanently removed from the repository.  This request can not be undone.
     *
     * Details of the TypeDef are included with the relationship's unique id (guid) to ensure the right
     * relationship is purged in the remote repositories.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param typeDefGUID unique identifier for this relationship's TypeDef.
     * @param typeDefName name of this relationship's TypeDef.
     * @param instanceGUID unique identifier for the relationship.
     */
    public void processPurgedRelationshipEvent(String       sourceName,
                                               String       originatorMetadataCollectionId,
                                               String       originatorServerName,
                                               String       originatorServerType,
                                               String       originatorOrganizationName,
                                               String       typeDefGUID,
                                               String       typeDefName,
                                               String       instanceGUID)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.PURGED_RELATIONSHIP_EVENT,
                                                                typeDefGUID,
                                                                typeDefName,
                                                                instanceGUID);

        instanceEvent.setEventOriginator(eventOriginator);

        this.sendInstanceEvent(sourceName, instanceEvent);
    }


    /**
     * An existing relationship has been deleted and purged.  This request can not be undone.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param relationship deleted relationship
     */
    public void processDeletePurgedRelationshipEvent(String       sourceName,
                                                     String       originatorMetadataCollectionId,
                                                     String       originatorServerName,
                                                     String       originatorServerType,
                                                     String       originatorOrganizationName,
                                                     Relationship relationship)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.DELETE_PURGED_RELATIONSHIP_EVENT,
                                                                relationship);

        instanceEvent.setEventOriginator(eventOriginator);

        this.sendInstanceEvent(sourceName, instanceEvent);
    }


    /**
     * The guid of an existing relationship has changed.  This is used if two different
     * relationships are discovered to have the same guid.  This is extremely unlikely but not impossible so
     * the open metadata protocol has provision for this.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param originalRelationshipGUID the existing identifier for the relationship.
     * @param relationship new values for this relationship, including the new guid.
     */
    public void processReIdentifiedRelationshipEvent(String       sourceName,
                                                     String       originatorMetadataCollectionId,
                                                     String       originatorServerName,
                                                     String       originatorServerType,
                                                     String       originatorOrganizationName,
                                                     String       originalRelationshipGUID,
                                                     Relationship relationship)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.RE_IDENTIFIED_RELATIONSHIP_EVENT,
                                                                relationship);

        instanceEvent.setEventOriginator(eventOriginator);
        instanceEvent.setOriginalInstanceGUID(originalRelationshipGUID);

        this.sendInstanceEvent(sourceName, instanceEvent);
    }


    /**
     * An existing relationship has had its type changed.  Typically this action is taken to move a relationship's
     * type to either a super type (so the subtype can be deleted) or a new subtype (so additional properties can be
     * added.)  However, the type can be changed to any compatible type.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param originalTypeDefSummary original details of this relationship's TypeDef.
     * @param relationship new values for this relationship, including the new type information.
     */
    public void processReTypedRelationshipEvent(String         sourceName,
                                                String         originatorMetadataCollectionId,
                                                String         originatorServerName,
                                                String         originatorServerType,
                                                String         originatorOrganizationName,
                                                TypeDefSummary originalTypeDefSummary,
                                                Relationship   relationship)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.RETYPED_RELATIONSHIP_EVENT,
                                                                relationship);

        instanceEvent.setEventOriginator(eventOriginator);
        instanceEvent.setOriginalTypeDefSummary(originalTypeDefSummary);

        this.sendInstanceEvent(sourceName, instanceEvent);
    }


    /**
     * An existing relationship has changed home repository.  This action is taken for example, if a repository
     * becomes permanently unavailable, or if the user community updating this relationship move to working
     * from a different repository in the open metadata repository cohort.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param originalHomeMetadataCollection unique identifier for the original home repository.
     * @param relationship new values for this relationship, including the new home information.
     */
    public void processReHomedRelationshipEvent(String       sourceName,
                                                String       originatorMetadataCollectionId,
                                                String       originatorServerName,
                                                String       originatorServerType,
                                                String       originatorOrganizationName,
                                                String       originalHomeMetadataCollection,
                                                Relationship relationship)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.RE_HOMED_RELATIONSHIP_EVENT,
                                                                relationship);

        instanceEvent.setEventOriginator(eventOriginator);
        instanceEvent.setOriginalHomeMetadataCollectionId(originalHomeMetadataCollection);

        this.sendInstanceEvent(sourceName, instanceEvent);
    }


    /**
     * A repository has requested the home repository of a relationship send details of the relationship so
     * the local repository can create a reference copy of the instance.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param typeDefGUID unique identifier for this instance's TypeDef
     * @param typeDefName name of this relationship's TypeDef
     * @param instanceGUID unique identifier for the instance
     * @param homeMetadataCollectionId metadata collection id for the home of this instance.
     */
    public void processRefreshRelationshipRequest(String       sourceName,
                                                  String       originatorMetadataCollectionId,
                                                  String       originatorServerName,
                                                  String       originatorServerType,
                                                  String       originatorOrganizationName,
                                                  String       typeDefGUID,
                                                  String       typeDefName,
                                                  String       instanceGUID,
                                                  String       homeMetadataCollectionId)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.REFRESH_RELATIONSHIP_REQUEST,
                                                                typeDefGUID,
                                                                typeDefName,
                                                                instanceGUID);

        instanceEvent.setEventOriginator(eventOriginator);
        instanceEvent.setHomeMetadataCollectionId(homeMetadataCollectionId);

        this.sendInstanceEvent(sourceName, instanceEvent);
    }


    /**
     * The local repository is refreshing the information about a relationship for the other
     * repositories in the cohort.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param relationship relationship details
     */
    public void processRefreshRelationshipEvent(String       sourceName,
                                                String       originatorMetadataCollectionId,
                                                String       originatorServerName,
                                                String       originatorServerType,
                                                String       originatorOrganizationName,
                                                Relationship relationship)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.REFRESHED_RELATIONSHIP_EVENT,
                                                                relationship);

        instanceEvent.setEventOriginator(eventOriginator);

        this.sendInstanceEvent(sourceName, instanceEvent);
    }


    /**
     * An open metadata repository is passing information about a collection of entities and relationships
     * with the other repositories in the cohort.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param instances multiple entities and relationships for sharing.
     */
    public void processInstanceBatchEvent(String         sourceName,
                                          String         originatorMetadataCollectionId,
                                          String         originatorServerName,
                                          String         originatorServerType,
                                          String         originatorOrganizationName,
                                          InstanceGraph  instances)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventType.BATCH_INSTANCES_EVENT, instances);

        instanceEvent.setEventOriginator(eventOriginator);

        this.sendInstanceEvent(sourceName, instanceEvent);
    }


    /**
     * A repository has detected two metadata instances with the same identifier (guid).
     * This is a serious error because it could lead to corruption of the metadata collection.
     *
     * For metadata instances managed by the cohort, this is detected when the event publicizing the second instance
     * to be created with the same guid is received by the other members.   Ideally the newest instance is changed.
     * The member that hosts the home repository for the original instance using the guid sends out the
     * conflictingInstancesDetected() notification requesting that the other repository changes the guid on its
     * new instance.  When this event is received by other member in the cohort, they delete their reference copy
     * of this metadata instance.  Once changed, the updated instance with its new guid is redistributed around
     * the cohort using the reIdentifiedEntity/Relationship() event.
     * The reference copies are recreated on receipt of this event.
     *
     * If the duplicate is caused by loading of an open metadata archive and the older instance's guid can be changed
     * by the home metadata collection then the instance is updated in the home metadata collection and the change
     * distributed using the reIdentifiedEntity/Relationship() event.  The reference copies are updated on receipt
     * of this event.
     *
     * If the older instance's guid can not be changed because it too comes from a (different) open metadata archive
     * (or a metadata collection that has left the cohort), then all repositories detecting the duplicate send out
     * a conflictingInstancesDetected() notification requesting that the repository that loaded the newer instance
     * manage the resolution since the newer archive need fixing.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId unique identifier for the metadata collection hosted by the server that
     *                                       sent the event.
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param targetMetadataCollectionId metadata collection id of other repository with the conflicting instance
     * @param targetTypeDefSummary details of the target instance's TypeDef
     * @param targetInstanceGUID unique identifier for the source instance
     * @param otherOrigin origin of the other (older) metadata instance
     * @param otherMetadataCollectionId metadata collection of the other (older) metadata instance
     * @param otherTypeDefSummary details of the other (older) instance's TypeDef
     * @param otherInstanceGUID unique identifier for the other (older) instance
     * @param errorMessage description of the error
     */
    public void processConflictingInstancesEvent(String                 sourceName,
                                                 String                 originatorMetadataCollectionId,
                                                 String                 originatorServerName,
                                                 String                 originatorServerType,
                                                 String                 originatorOrganizationName,
                                                 String                 targetMetadataCollectionId,
                                                 TypeDefSummary         targetTypeDefSummary,
                                                 String                 targetInstanceGUID,
                                                 String                 otherMetadataCollectionId,
                                                 InstanceProvenanceType otherOrigin,
                                                 TypeDefSummary         otherTypeDefSummary,
                                                 String                 otherInstanceGUID,
                                                 String                 errorMessage)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventErrorCode.CONFLICTING_INSTANCES,
                                                                errorMessage,
                                                                targetMetadataCollectionId,
                                                                targetTypeDefSummary,
                                                                targetInstanceGUID,
                                                                otherMetadataCollectionId,
                                                                otherOrigin,
                                                                otherTypeDefSummary,
                                                                otherInstanceGUID);

        instanceEvent.setEventOriginator(eventOriginator);

        this.sendInstanceEvent(sourceName, instanceEvent);
    }


    /**
     * An open metadata repository has detected an inconsistency in the version of the type used in an updated metadata
     * instance compared to its stored version.
     *
     * @param sourceName name of the source of the event.  It may be the cohort name for incoming events or the
     *                   local repository, or event mapper name.
     * @param originatorMetadataCollectionId metadata collection id of the repository reporting the conflicting instance
     * @param originatorServerName name of the server that the event came from.
     * @param originatorServerType type of server that the event came from.
     * @param originatorOrganizationName name of the organization that owns the server that sent the event.
     * @param targetMetadataCollectionId metadata collection id of other repository with the conflicting instance
     * @param targetTypeDefSummary details of the target instance's TypeDef
     * @param targetInstanceGUID unique identifier for the source instance
     * @param otherTypeDefSummary details of the originator's TypeDef
     * @param errorMessage description of the error.
     */
    public void processConflictingTypeEvent(String                 sourceName,
                                            String                 originatorMetadataCollectionId,
                                            String                 originatorServerName,
                                            String                 originatorServerType,
                                            String                 originatorOrganizationName,
                                            String                 targetMetadataCollectionId,
                                            TypeDefSummary         targetTypeDefSummary,
                                            String                 targetInstanceGUID,
                                            TypeDefSummary         otherTypeDefSummary,
                                            String                 errorMessage)
    {
        OMRSEventOriginator eventOriginator = new OMRSEventOriginator();

        eventOriginator.setMetadataCollectionId(originatorMetadataCollectionId);
        eventOriginator.setServerName(originatorServerName);
        eventOriginator.setServerType(originatorServerType);
        eventOriginator.setOrganizationName(originatorOrganizationName);

        OMRSInstanceEvent instanceEvent = new OMRSInstanceEvent(OMRSInstanceEventErrorCode.CONFLICTING_TYPE,
                                                                errorMessage,
                                                                targetMetadataCollectionId,
                                                                targetTypeDefSummary,
                                                                targetInstanceGUID,
                                                                otherTypeDefSummary);

        instanceEvent.setEventOriginator(eventOriginator);

        this.sendInstanceEvent(sourceName, instanceEvent);
    }
}
