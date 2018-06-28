/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.archivemanager;

import org.odpi.openmetadata.repositoryservices.archivemanager.opentypes.OpenMetadataTypesArchive;

import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;

import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventProcessor;
import org.odpi.openmetadata.repositoryservices.events.OMRSTypeDefEventProcessor;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.OpenMetadataArchiveStoreConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveInstanceStore;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveTypeStore;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentManager;

import java.util.ArrayList;
import java.util.List;

/**
 * OMRSArchiveManager manages the loading and unloading of open metadata archives from the local OMRS repository.
 * An open metadata archive provides pre-built definitions for types and metadata instances.
 */
public class OMRSArchiveManager
{
    private List<OpenMetadataArchiveStoreConnector> openMetadataArchiveStores   = new ArrayList<>();
    private OMRSRepositoryContentManager            repositoryContentManager    = null;
    private OMRSInstanceEventProcessor              localInstanceEventProcessor = null;


    /*
     * The audit log provides a verifiable record of the open metadata archives that have been loaded into
     * the open metadata repository.  The Logger is for standard debug.
     */
    private static final OMRSAuditLog auditLog = new OMRSAuditLog(OMRSAuditingComponent.ARCHIVE_MANAGER);

    /**
     * Constructor to save the initial list of open metadata archives from the server startup configuration.
     * These will be processed as soon as the event processors are supplied from the local repository.
     *
     * @param startUpOpenMetadataArchives  initial list of open metadata archives provided in startup configuration
     */
    public OMRSArchiveManager(List<OpenMetadataArchiveStoreConnector>    startUpOpenMetadataArchives)
    {
        if (startUpOpenMetadataArchives != null)
        {
            this.openMetadataArchiveStores = new ArrayList<>(startUpOpenMetadataArchives);
        }
    }


    /**
     * Close down any open archives.
     */
    public void close()
    {
        for (OpenMetadataArchiveStoreConnector archiveStore : openMetadataArchiveStores)
        {
            if (archiveStore != null)
            {
                try
                {
                    archiveStore.disconnect();
                }
                catch (Throwable error)
                {
                    /*
                     * nothing to do
                     */
                }
            }
        }
    }


    /**
     * The local repository is accessed through its inbound event processors.  A server will always have
     * the local Content Manager and TypeDef Processor but the local Instance Processor is only available
     * if the local server has a metadata repository defined.
     *
     * @param repositoryContentManager typeDef processor for the local repository
     * @param instanceProcessor  instance processor for the local repository
     */
    public void setLocalRepository(OMRSRepositoryContentManager repositoryContentManager,
                                   OMRSInstanceEventProcessor   instanceProcessor)
    {
        this.repositoryContentManager = repositoryContentManager;
        this.localInstanceEventProcessor = instanceProcessor;

        /*
         * The repository content manager is seeded with all of the open metadata types.
         */
        processOpenMetadataTypes();

        /*
         * Once the open metadata types are in place, the archive stores are processed.
         */
        for (OpenMetadataArchiveStoreConnector archiveStore : this.openMetadataArchiveStores)
        {
            processOpenMetadataArchiveStore(archiveStore, repositoryContentManager, instanceProcessor);
        }
    }


    /**
     * Add a new archive to the local repository. If there are problems processing the archive, an exception is thrown.
     * This method allows archives to be loaded into a running server.
     *
     * @param archiveStore  new open metadata archive to process
     */
    public void addOpenMetadataArchive(OpenMetadataArchiveStoreConnector     archiveStore)
    {
        this.processOpenMetadataArchiveStore(archiveStore, repositoryContentManager, localInstanceEventProcessor);
        this.openMetadataArchiveStores.add(archiveStore);
    }


    /**
     * Unpack and process the contents an open metadata archive store, passing its contents to the local
     * repository (if it exists).
     */
    private void processOpenMetadataTypes()
    {
        OpenMetadataTypesArchive openMetadataTypesArchive = new OpenMetadataTypesArchive();
        OpenMetadataArchive      openMetadataTypes        = openMetadataTypesArchive.getOpenMetadataArchive();

        repositoryContentManager.setOpenMetadataTypesOriginGUID(openMetadataTypesArchive.getArchiveGUID());
        processOpenMetadataArchive(openMetadataTypes, repositoryContentManager, localInstanceEventProcessor);
    }

    /**
     * Unpack and process the contents an open metadata archive , passing its contents to the local
     * repository (if it exists).
     *
     * @param archiveStore open metadata archive  to process
     * @param typeDefProcessor receiver of new TypeDefs
     * @param instanceProcessor receiver of new instances
     */
    private void processOpenMetadataArchiveStore(OpenMetadataArchiveStoreConnector    archiveStore,
                                                 OMRSTypeDefEventProcessor            typeDefProcessor,
                                                 OMRSInstanceEventProcessor           instanceProcessor)
    {
        if (archiveStore != null)
        {
            /*
             * Each archive store has a header, a section of new type definitions (TypeDefs) and a section of
             * metadata instances.
             */
            OpenMetadataArchive archiveContent = archiveStore.getArchiveContents();

            if (archiveContent == null)
            {
                final String     actionDescription = "Process Open Metadata Archive";

                OMRSAuditCode auditCode = OMRSAuditCode.EMPTY_ARCHIVE;
                auditLog.logRecord(actionDescription,
                                   auditCode.getLogMessageId(),
                                   auditCode.getSeverity(),
                                   auditCode.getFormattedLogMessage(),
                                   null,
                                   auditCode.getSystemAction(),
                                   auditCode.getUserAction());
            }
            else
            {
                processOpenMetadataArchive(archiveContent, typeDefProcessor, instanceProcessor);
            }
        }
    }


    /**
     * Step through the content of an open metadata archive, passing its contents to the local repository (if it
     * exists).
     *
     * @param archiveContent open metadata archive to process
     */
    private void processOpenMetadataArchive(OpenMetadataArchive          archiveContent,
                                            OMRSTypeDefEventProcessor    typeDefProcessor,
                                            OMRSInstanceEventProcessor   instanceProcessor)
    {
        OMRSAuditCode    auditCode;
        final String     actionDescription = "Process Open Metadata Archive";

        OpenMetadataArchiveProperties archiveProperties = archiveContent.getArchiveProperties();

        if (archiveProperties != null)
        {
            auditCode = OMRSAuditCode.PROCESSING_ARCHIVE;
            auditLog.logRecord(actionDescription,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(archiveProperties.getArchiveName()),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());


            OpenMetadataArchiveTypeStore     archiveTypeStore     = archiveContent.getArchiveTypeStore();
            OpenMetadataArchiveInstanceStore archiveInstanceStore = archiveContent.getArchiveInstanceStore();

            int                              typeCount            = 0;
            int                              instanceCount        = 0;


            if (archiveTypeStore != null)
            {
                typeCount = this.processTypeDefStore(archiveProperties, archiveTypeStore, typeDefProcessor);
            }

            if (archiveInstanceStore != null)
            {
                instanceCount = this.processInstanceStore(archiveProperties, archiveInstanceStore, instanceProcessor);
            }

            auditCode = OMRSAuditCode.COMPLETED_ARCHIVE;
            auditLog.logRecord(actionDescription,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(Integer.toString(typeCount),
                                                                Integer.toString(instanceCount),
                                                                archiveProperties.getArchiveName()),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());
        }
        else
        {
            auditCode = OMRSAuditCode.NULL_PROPERTIES_IN_ARCHIVE;
            auditLog.logRecord(actionDescription,
                               auditCode.getLogMessageId(),
                               auditCode.getSeverity(),
                               auditCode.getFormattedLogMessage(),
                               null,
                               auditCode.getSystemAction(),
                               auditCode.getUserAction());
        }
    }


    /**
     * The TypeStore is in two parts.  First there is an optional list of patches to existing TypeDefs.
     * Then an optional list of new TypeDefs.  It is possible that this archive has been processed before
     * and so any duplicates detected are ignored.  However, conflicting TypeDefs are detected.
     * Any problems found in applying the archive contents are recorded on the audit log.
     *
     * @param archiveProperties properties of the archive used for logging
     * @param archiveTypeStore TypeStore from the archive
     * @return type count
     */
    private int  processTypeDefStore(OpenMetadataArchiveProperties    archiveProperties,
                                     OpenMetadataArchiveTypeStore     archiveTypeStore,
                                     OMRSTypeDefEventProcessor        typeDefProcessor)
    {
        List<TypeDefPatch>     typeDefPatches       = archiveTypeStore.getTypeDefPatches();
        List<AttributeTypeDef> newAttributeTypeDefs = archiveTypeStore.getAttributeTypeDefs();
        List<TypeDef>          newTypeDefs          = archiveTypeStore.getNewTypeDefs();
        int                    typeCount            = 0;

        if (typeDefProcessor != null)
        {
            String       sourceName = OMRSAuditingComponent.ARCHIVE_MANAGER.getComponentName();
            String       originatorMetadataCollectionId = archiveProperties.getArchiveGUID();
            String       originatorServerName = archiveProperties.getArchiveName();
            String       originatorServerType = null;
            String       originatorName = archiveProperties.getOriginatorName();
            String       originatorOrganizationName = archiveProperties.getOriginatorOrganization();

            /*
             * Originator name can not be null since it is used as the userId for calls to the repository
             */
            if (originatorName == null)
            {
                originatorName = sourceName;
            }

            if (archiveProperties.getArchiveType() != null)
            {
                originatorServerType = archiveProperties.getArchiveType().getArchiveTypeName();
            }

            if (typeDefPatches != null)
            {
                for (TypeDefPatch typeDefPatch : typeDefPatches)
                {
                    if (typeDefPatch != null)
                    {
                        typeDefProcessor.processUpdatedTypeDefEvent(originatorName,
                                                                    originatorMetadataCollectionId,
                                                                    originatorServerName,
                                                                    originatorServerType,
                                                                    originatorOrganizationName,
                                                                    typeDefPatch);
                        typeCount ++;
                    }
                }
            }

            if (newAttributeTypeDefs != null)
            {
                for (AttributeTypeDef newAttributeTypeDef : newAttributeTypeDefs)
                {
                    if (newAttributeTypeDef != null)
                    {
                        typeDefProcessor.processNewAttributeTypeDefEvent(originatorName,
                                                                originatorMetadataCollectionId,
                                                                originatorServerName,
                                                                originatorServerType,
                                                                originatorOrganizationName,
                                                                newAttributeTypeDef);

                        typeCount ++;
                    }
                }
            }

            if (newTypeDefs != null)
            {
                for (TypeDef newTypeDef : newTypeDefs)
                {
                    if (newTypeDef != null)
                    {
                        typeDefProcessor.processNewTypeDefEvent(originatorName,
                                                                originatorMetadataCollectionId,
                                                                originatorServerName,
                                                                originatorServerType,
                                                                originatorOrganizationName,
                                                                newTypeDef);
                        typeCount ++;
                    }
                }
            }
        }

        return typeCount;
    }


    /**
     * The InstanceStore is in two parts: an optional list of entities followed by an optional list
     * of relationships.  It is possible that this archive has been processed before
     * and so any duplicates detected are ignored.  However, conflicting instances are detected.
     * Any problems found in applying the archive contents are recorded on the audit log.
     *
     * @param archiveProperties properties describing the archive used in logging
     * @param archiveInstanceStore the instance store to process
     * @param instanceProcessor the processor to add the instances to the local repository.  It may be null
     *                          if there is no local repository configured for this server.
     * @return instance count
     */
    private int  processInstanceStore(OpenMetadataArchiveProperties    archiveProperties,
                                      OpenMetadataArchiveInstanceStore archiveInstanceStore,
                                      OMRSInstanceEventProcessor       instanceProcessor)
    {
        List<EntityDetail> entities      = archiveInstanceStore.getEntities();
        List<Relationship> relationships = archiveInstanceStore.getRelationships();
        int                instanceCount = 0;

        if (instanceProcessor != null)
        {
            String       sourceName = OMRSAuditingComponent.ARCHIVE_MANAGER.getComponentName();
            String       originatorMetadataCollectionId = archiveProperties.getArchiveGUID();
            String       originatorServerName = archiveProperties.getArchiveName();
            String       originatorServerType = null;
            String       originatorOrganizationName = archiveProperties.getOriginatorName();

            if (archiveProperties.getArchiveType() != null)
            {
                originatorServerType = archiveProperties.getArchiveType().getArchiveTypeName();
            }

            if (entities != null)
            {
                for (EntityDetail entity : entities)
                {
                    if (entity != null)
                    {
                        instanceProcessor.processNewEntityEvent(sourceName,
                                                                originatorMetadataCollectionId,
                                                                originatorServerName,
                                                                originatorServerType,
                                                                originatorOrganizationName,
                                                                entity);

                        instanceCount ++;
                    }
                }
            }


            if (relationships != null)
            {
                for (Relationship relationship : relationships)
                {
                    if (relationship != null)
                    {
                        instanceProcessor.processNewRelationshipEvent(sourceName,
                                                                      originatorMetadataCollectionId,
                                                                      originatorServerName,
                                                                      originatorServerType,
                                                                      originatorOrganizationName,
                                                                      relationship);

                        instanceCount ++;
                    }
                }
            }
        }

        return instanceCount;
    }
}
