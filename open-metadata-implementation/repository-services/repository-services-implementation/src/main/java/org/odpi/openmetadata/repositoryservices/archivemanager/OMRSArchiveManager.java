/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.archivemanager;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.opentypes.OpenMetadataTypesArchive;

import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.OpenMetadataArchiveStore;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventProcessorClassificationExtension;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditingComponent;

import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.events.OMRSInstanceEventProcessorInterface;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.OpenMetadataArchiveStoreConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;
import org.odpi.openmetadata.repositoryservices.events.OMRSTypeDefEventProcessorInterface;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.localrepository.repositoryconnector.LocalOMRSInstanceEventProcessor;
import org.odpi.openmetadata.repositoryservices.localrepository.repositorycontentmanager.OMRSRepositoryContentManager;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * OMRSArchiveManager manages the loading and unloading of open metadata archives from the local OMRS repository.
 * An open metadata archive provides pre-built definitions for types and metadata instances.
 */
public class OMRSArchiveManager
{
    private String                                  localMetadataCollectionId   = null;
    private List<OpenMetadataArchiveStore>          openMetadataArchiveStores   = new ArrayList<>();
    private OMRSRepositoryContentManager            repositoryContentManager    = null;
    private LocalOMRSInstanceEventProcessor         localInstanceEventProcessor = null;


    /*
     * The audit log provides a verifiable record of the open metadata archives that have been loaded into
     * the open metadata repository.  The Logger is for standard debug.
     */
    private final AuditLog auditLog;

    /**
     * Constructor to save the initial list of open metadata archives from the server startup configuration.
     * These will be processed as soon as the event processors are supplied from the local repository.
     *
     * @param startUpOpenMetadataArchives  initial list of open metadata archives provided in startup configuration
     * @param auditLog audit log for this component.
     */
    public OMRSArchiveManager(List<OpenMetadataArchiveStoreConnector>    startUpOpenMetadataArchives,
                              AuditLog                                   auditLog)
    {
        this.auditLog = auditLog;

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
        for (OpenMetadataArchiveStore archiveStore : openMetadataArchiveStores)
        {
            if (archiveStore instanceof OpenMetadataArchiveStoreConnector archiveStoreConnector)
            {
                try
                {
                    archiveStoreConnector.disconnect();
                }
                catch (Exception error)
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
     * @param localMetadataCollectionId unique identifier of the local repository
     * @param repositoryContentManager typeDef processor for the local repository
     * @param instanceProcessor  instance processor for the local repository
     * @throws RepositoryErrorException a problem accessing the archive
     */
    public void setLocalRepository(String                           localMetadataCollectionId,
                                   OMRSRepositoryContentManager     repositoryContentManager,
                                   LocalOMRSInstanceEventProcessor  instanceProcessor) throws RepositoryErrorException
    {
        this.localMetadataCollectionId = localMetadataCollectionId;
        this.repositoryContentManager = repositoryContentManager;
        this.localInstanceEventProcessor = instanceProcessor;

        /*
         * The repository content manager is seeded with all the open metadata types.
         */
        processOpenMetadataTypes();

        /*
         * Once the open metadata types are in place, the archive stores are processed.
         */
        for (OpenMetadataArchiveStore archiveStore : this.openMetadataArchiveStores)
        {
            processOpenMetadataArchiveStore(archiveStore, "Startup archive list", repositoryContentManager, instanceProcessor);
        }
    }


    /**
     * Add a new archive to the local repository. If there are problems processing the archive, an exception is thrown.
     * This method allows archives to be loaded into a running server.
     *
     * @param archiveStore  new open metadata archive to process
     * @param archiveSource source of the archive
     * @throws RepositoryErrorException a problem accessing the archive
     */
    public void addOpenMetadataArchive(OpenMetadataArchiveStore archiveStore,
                                       String                   archiveSource) throws RepositoryErrorException
    {
        this.processOpenMetadataArchiveStore(archiveStore, archiveSource, repositoryContentManager, localInstanceEventProcessor);
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
        processOpenMetadataArchive(openMetadataTypes, "Open Metadata Types", repositoryContentManager, localInstanceEventProcessor);
    }


    /**
     * Unpack and process the contents an open metadata archive , passing its contents to the local
     * repository (if it exists).
     *
     * @param archiveStore open metadata archive  to process
     * @param archiveSource source of the archive - such as file name
     * @param typeDefProcessor receiver of new TypeDefs
     * @param instanceProcessor receiver of new instances
     * @throws RepositoryErrorException a problem accessing the archive
     */
    private void processOpenMetadataArchiveStore(OpenMetadataArchiveStore             archiveStore,
                                                 String                               archiveSource,
                                                 OMRSTypeDefEventProcessorInterface   typeDefProcessor,
                                                 OMRSInstanceEventProcessorInterface  instanceProcessor) throws RepositoryErrorException
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

                auditLog.logMessage(actionDescription, OMRSAuditCode.EMPTY_ARCHIVE.getMessageDefinition(archiveSource));
            }
            else
            {
                processOpenMetadataArchive(archiveContent, archiveSource, typeDefProcessor, instanceProcessor);
            }
        }
    }


    /**
     * Step through the content of an open metadata archive, passing its contents to the local repository (if it
     * exists).
     *
     * @param archiveContent open metadata archive to process
     * @param archiveSource source of the archive - such as file name
     * @param typeDefProcessor processor of type definitions found in the archive
     * @param instanceProcessor processor of instances that have been found in the archive
     */
    private void processOpenMetadataArchive(OpenMetadataArchive                   archiveContent,
                                            String                                archiveSource,
                                            OMRSTypeDefEventProcessorInterface    typeDefProcessor,
                                            OMRSInstanceEventProcessorInterface   instanceProcessor)
    {
        final String     actionDescription = "Process Open Metadata Archive";

        OpenMetadataArchiveProperties archiveProperties = archiveContent.getArchiveProperties();

        if (archiveProperties != null)
        {
            auditLog.logMessage(actionDescription, OMRSAuditCode.PROCESSING_ARCHIVE.getMessageDefinition(archiveProperties.getArchiveName()));


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

            auditLog.logMessage(actionDescription,
                                OMRSAuditCode.COMPLETED_ARCHIVE.getMessageDefinition(Integer.toString(typeCount),
                                                                                     Integer.toString(instanceCount),
                                                                                     archiveProperties.getArchiveName()));
        }
        else
        {
            auditLog.logMessage(actionDescription, OMRSAuditCode.NULL_PROPERTIES_IN_ARCHIVE.getMessageDefinition(archiveSource));
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
     * @param typeDefProcessor processor of type definitions found in the archive
     * @return type count
     */
    private int  processTypeDefStore(OpenMetadataArchiveProperties          archiveProperties,
                                     OpenMetadataArchiveTypeStore           archiveTypeStore,
                                     OMRSTypeDefEventProcessorInterface     typeDefProcessor)
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
            String       archiveVersion = archiveProperties.getArchiveVersion();
            String       originatorOrganizationName = archiveProperties.getOriginatorOrganization();

            /*
             * Originator name can not be null since it is used as the userId for calls to the repository
             */
            if (originatorName == null)
            {
                originatorName = sourceName;
            }

            String archiveId = originatorName + " (" + archiveVersion + ")";

            if (archiveProperties.getArchiveType() != null)
            {
                originatorServerType = archiveProperties.getArchiveType().getName();
            }

            if (newAttributeTypeDefs != null)
            {
                for (AttributeTypeDef newAttributeTypeDef : newAttributeTypeDefs)
                {
                    if (newAttributeTypeDef != null)
                    {
                        typeDefProcessor.processNewAttributeTypeDefEvent(archiveId,
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
                        typeDefProcessor.processNewTypeDefEvent(archiveId,
                                                                originatorMetadataCollectionId,
                                                                originatorServerName,
                                                                originatorServerType,
                                                                originatorOrganizationName,
                                                                newTypeDef);
                        typeCount ++;
                    }
                }
            }

            if (typeDefPatches != null)
            {
                for (TypeDefPatch typeDefPatch : typeDefPatches)
                {
                    if (typeDefPatch != null)
                    {
                        typeDefProcessor.processUpdatedTypeDefEvent(archiveId,
                                                                    originatorMetadataCollectionId,
                                                                    originatorServerName,
                                                                    originatorServerType,
                                                                    originatorOrganizationName,
                                                                    typeDefPatch);
                        typeCount ++;
                    }
                }
            }
        }
        else
        {
            final String actionDescription = "Processing type definitions from archive";

            auditLog.logMessage(actionDescription, OMRSAuditCode.NO_TYPE_DEF_PROCESSOR.getMessageDefinition());
        }

        return typeCount;
    }


    /**
     * The InstanceStore is in three parts: an optional list of entities followed by an optional list
     * of relationships followed by an optional list of classifications.
     * It is possible that this archive has been processed before
     * and so any duplicates detected are ignored.  However, conflicting instances are detected.
     * Any problems found in applying the archive contents are recorded on the audit log.
     *
     * @param archiveProperties properties describing the archive used in logging
     * @param archiveInstanceStore the instance store to process
     * @param instanceProcessor the processor to add the instances to the local repository.  It may be null
     *                          if there is no local repository configured for this server.
     * @return instance count
     */
    private int  processInstanceStore(OpenMetadataArchiveProperties             archiveProperties,
                                      OpenMetadataArchiveInstanceStore          archiveInstanceStore,
                                      OMRSInstanceEventProcessorInterface       instanceProcessor)
    {
        List<EntityDetail>                  entities        = archiveInstanceStore.getEntities();
        List<Relationship>                  relationships   = archiveInstanceStore.getRelationships();
        List<ClassificationEntityExtension> classifications = archiveInstanceStore.getClassifications();
        int                                 instanceCount   = 0;

        if (instanceProcessor != null)
        {
            String                 homeMetadataCollectionId   = archiveProperties.getArchiveGUID();
            String                 archiveName                = archiveProperties.getArchiveName();
            String                 originatorServerType       = OpenMetadataArchiveType.CONTENT_PACK.getName();
            InstanceProvenanceType provenanceType             = InstanceProvenanceType.CONTENT_PACK;
            Date                   archiveCreationTime        = archiveProperties.getCreationDate();
            String                 originatorName             = archiveProperties.getOriginatorName();
            String                 archiveVersion             = archiveProperties.getArchiveVersion();
            String                 originatorOrganizationName = archiveProperties.getOriginatorOrganization();
            String                 originatorLicense          = archiveProperties.getOriginatorLicense();

            String archiveId = originatorName + " (" + archiveVersion + ")";

            if (archiveProperties.getArchiveType() == OpenMetadataArchiveType.METADATA_EXPORT)
            {
                provenanceType       = InstanceProvenanceType.EXPORT_ARCHIVE;
                originatorServerType = OpenMetadataArchiveType.METADATA_EXPORT.getName();
            }
            else if (archiveProperties.getArchiveType() == OpenMetadataArchiveType.REPOSITORY_BACKUP)
            {
                provenanceType       = InstanceProvenanceType.LOCAL_COHORT;
                originatorServerType = OpenMetadataArchiveType.REPOSITORY_BACKUP.getName();
            }

            if (entities != null)
            {
                for (EntityDetail entity : entities)
                {
                    if (entity != null)
                    {
                        this.setInstanceAuditHeader(localMetadataCollectionId,
                                                    homeMetadataCollectionId,
                                                    archiveName,
                                                    originatorName,
                                                    archiveCreationTime,
                                                    provenanceType,
                                                    originatorLicense,
                                                    entity);

                        /*
                         * There is no need to support delete in archive because the elements are
                         * reference copies and can be deleted from the receiving repositories.
                         */
                        if (entity.getVersion() == 1L)
                        {
                            instanceProcessor.processNewEntityEvent(archiveId,
                                                                    homeMetadataCollectionId,
                                                                    archiveName,
                                                                    originatorServerType,
                                                                    originatorOrganizationName,
                                                                    entity);
                        }
                        else
                        {
                            instanceProcessor.processUpdatedEntityEvent(archiveId,
                                                                        homeMetadataCollectionId,
                                                                        archiveName,
                                                                        originatorServerType,
                                                                        originatorOrganizationName,
                                                                        null,
                                                                        entity);
                        }

                        instanceCount++;
                    }
                }
            }

            if (relationships != null)
            {
                for (Relationship relationship : relationships)
                {
                    if (relationship != null)
                    {
                        this.setInstanceAuditHeader(localMetadataCollectionId,
                                                    homeMetadataCollectionId,
                                                    archiveName,
                                                    originatorName,
                                                    archiveCreationTime,
                                                    provenanceType,
                                                    originatorLicense,
                                                    relationship);

                        /*
                         * There is no need to support delete in archive because the elements are
                         * reference copies and can be deleted from the receiving repositories.
                         */
                        if (relationship.getVersion() == 1L)
                        {
                            instanceProcessor.processNewRelationshipEvent(archiveId,
                                                                          homeMetadataCollectionId,
                                                                          archiveName,
                                                                          originatorServerType,
                                                                          originatorOrganizationName,
                                                                          relationship);
                        }
                        else
                        {
                            instanceProcessor.processUpdatedRelationshipEvent(archiveId,
                                                                              homeMetadataCollectionId,
                                                                              archiveName,
                                                                              originatorServerType,
                                                                              originatorOrganizationName,
                                                                              null,
                                                                              relationship);
                        }

                        instanceCount ++;
                    }
                }
            }

            if (instanceProcessor instanceof OMRSInstanceEventProcessorClassificationExtension classificationInstanceProcessor)
            {
                if (classifications != null)
                {
                    for (ClassificationEntityExtension classificationEntityExtension : classifications)
                    {
                        if (classificationEntityExtension != null)
                        {
                            Classification classification = classificationEntityExtension.getClassification();

                            this.setInstanceAuditHeader(localMetadataCollectionId,
                                                        homeMetadataCollectionId,
                                                        archiveName,
                                                        originatorName,
                                                        archiveCreationTime,
                                                        provenanceType,
                                                        originatorLicense,
                                                        classification);

                            classificationEntityExtension.setClassification(classification);

                            if (classification.getVersion() == 1L)
                            {
                                classificationInstanceProcessor.processClassifiedEntityEvent(archiveId,
                                                                                             homeMetadataCollectionId,
                                                                                             archiveName,
                                                                                             originatorServerType,
                                                                                             originatorOrganizationName,
                                                                                             classificationEntityExtension.getEntityToClassify(),
                                                                                             classification);
                            }
                            else
                            {
                                classificationInstanceProcessor.processReclassifiedEntityEvent(archiveId,
                                                                                               homeMetadataCollectionId,
                                                                                               archiveName,
                                                                                               originatorServerType,
                                                                                               originatorOrganizationName,
                                                                                               classificationEntityExtension.getEntityToClassify(),
                                                                                               null,
                                                                                               classification);
                            }

                            instanceCount ++;
                        }
                    }
                }
            }
        }
        else
        {
            final String actionDescription = "Processing instances from archive";

            auditLog.logMessage(actionDescription, OMRSAuditCode.NO_INSTANCE_PROCESSOR.getMessageDefinition());
        }

        return instanceCount;
    }


    /**
     * Set up the header of an archive instance.  The header information from the archive
     * is used in the instance header unless it is set up explicitly in an export archive.
     * The local metadata collection id is set in the replicatedBy attribute to enable this
     * repository to send out refresh events about this archive.
     *
     * @param localMetadataCollectionId metadata collection id of the local repository
     * @param metadataCollectionId home metadata collection id
     * @param metadataConnectionName name of the metadata collection
     * @param originatorName originator name
     * @param creationTime creation time of archive
     * @param provenanceType type of archive
     * @param originatorLicense any license info
     * @param instance instance to fill in
     */
    private void setInstanceAuditHeader(String                 localMetadataCollectionId,
                                        String                 metadataCollectionId,
                                        String                 metadataConnectionName,
                                        String                 originatorName,
                                        Date                   creationTime,
                                        InstanceProvenanceType provenanceType,
                                        String                 originatorLicense,
                                        InstanceAuditHeader    instance)
    {
        if (provenanceType == InstanceProvenanceType.EXPORT_ARCHIVE)
        {
            if (instance.getMetadataCollectionId() == null)
            {
                instance.setMetadataCollectionId(metadataCollectionId);
            }

            if (instance.getMetadataCollectionName() == null)
            {
                instance.setMetadataCollectionName(metadataConnectionName);
            }

            if (instance.getCreatedBy() == null)
            {
                instance.setCreatedBy(originatorName);
            }

            if (instance.getCreateTime() == null)
            {
                instance.setCreateTime(creationTime);
            }

            if (instance.getInstanceProvenanceType() == null)
            {
                instance.setInstanceProvenanceType(provenanceType);
            }

            if (instance.getInstanceLicense() == null)
            {
                instance.setInstanceLicense(originatorLicense);
            }

            if (instance.getReplicatedBy() == null)
            {
                instance.setReplicatedBy(localMetadataCollectionId);
            }
        }
        else if (provenanceType == InstanceProvenanceType.CONTENT_PACK)
        {
            instance.setMetadataCollectionId(metadataCollectionId);
            instance.setMetadataCollectionName(metadataConnectionName);
            instance.setCreatedBy(originatorName);
            instance.setCreateTime(creationTime);
            instance.setInstanceProvenanceType(InstanceProvenanceType.CONTENT_PACK);
            instance.setInstanceLicense(originatorLicense);
            instance.setReplicatedBy(localMetadataCollectionId);
        }

        /*
         * The first version is 1 not 0.
         */
        if (instance.getVersion() == 0L)
        {
            instance.setVersion(1L);
        }
    }
}
