/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.directory;

import org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.directory.ffdc.DirectoryBasedOpenMetadataArchiveStoreConnectorAuditCode;
import org.odpi.openmetadata.adapters.repositoryservices.archiveconnector.directory.ffdc.DirectoryBasedOpenMetadataArchiveStoreConnectorErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Connection;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Endpoint;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.OpenMetadataArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.OpenMetadataArchiveStoreConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveInstanceStore;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveTypeStore;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Classification;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ClassificationEntityExtension;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityProxy;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.AttributeTypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.ClassificationDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.CollectionDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EnumDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSLogicErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.List;

public class DirectoryBasedOpenMetadataArchiveStoreConnector extends OpenMetadataArchiveStoreConnector implements OpenMetadataArchiveBuilder
{
    /*
     * This is the default name of the open metadata archive file that is used if there is no file name in the connection.
     */
    private static final String defaultDirectoryName = "open.metadata.archive";


    /*
     * Variables used in writing to the directories of the archive.
     */
    private String                                 archiveStoreName   = null;
    private boolean                                keepVersionHistory = false;
    private DirectoryBasedOpenMetadataArchiveStore archiveStore       = null;

    /*
     * Archive builder manages cache of types used for validation and retrieval of types.
     */
    private OMRSArchiveBuilder                     archiveBuilder    = null;


    /*
     * Variables used for logging and debug.
     */
    private static final Logger log = LoggerFactory.getLogger(DirectoryBasedOpenMetadataArchiveStoreConnector.class);


    /**
     * Default constructor
     */
    public DirectoryBasedOpenMetadataArchiveStoreConnector()
    {
    }


    /**
     * Retrieve the archive store information from the endpoint.
     *
     * @param connectorInstanceId   unique id for the connector instance   useful for messages etc
     * @param connectionDetails   POJO for the configuration used to create the connector.
     */
    @Override
    public void initialize(String connectorInstanceId, Connection connectionDetails)
    {
        super.initialize(connectorInstanceId, connectionDetails);

        Endpoint endpoint = connectionDetails.getEndpoint();

        if (endpoint != null)
        {
            archiveStoreName = endpoint.getAddress();
        }

        if (archiveStoreName == null)
        {
            archiveStoreName = defaultDirectoryName;
        }

        if (connectionDetails.getConfigurationProperties() != null)
        {
            if (connectionDetails.getConfigurationProperties().get(DirectoryBasedOpenMetadataArchiveStoreProvider.KEEP_VERSION_HISTORY_PROPERTY) != null)
            {
                keepVersionHistory = true;
            }
        }
    }


    /**
     * Indicates that the connector is completely configured and can begin processing.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        final String methodName = "start";

        this.archiveStore = new DirectoryBasedOpenMetadataArchiveStore(archiveStoreName, auditLog, keepVersionHistory);

        OpenMetadataArchiveProperties archiveProperties = this.getArchiveProperties();

        if (archiveProperties == null)
        {
            this.archiveStore.initializeArchive(methodName);
        }
        else
        {
            this.archiveBuilder = new OMRSArchiveBuilder(archiveProperties);
        }
    }


    /**
     * Return the contents of the archive.
     *
     * @return OpenMetadataArchive object
     * @throws RepositoryErrorException there is a problem accessing the archive
     */
    @Override
    public OpenMetadataArchive getArchiveContents() throws RepositoryErrorException
    {
        OpenMetadataArchive newOpenMetadataArchive = new OpenMetadataArchive();

        newOpenMetadataArchive.setArchiveProperties(this.getArchiveProperties());
        newOpenMetadataArchive.setArchiveTypeStore(this.getArchiveTypeStore());
        newOpenMetadataArchive.setArchiveInstanceStore(this.getArchiveInstanceStore());

        return newOpenMetadataArchive;
    }


    /**
     * Return the archive properties.
     *
     * @return properties structure stored as a file encoded in JSON
     */
    @Override
    public OpenMetadataArchiveProperties getArchiveProperties()
    {
        final String methodName = "getArchiveProperties";

        return archiveStore.readProperties(methodName);
    }


    /**
     * Set up the type store of the archive.  It has three sections - type defs, attribute type defs and type def patches.
     *
     * @return type store
     */
    private OpenMetadataArchiveTypeStore getArchiveTypeStore()
    {
        OpenMetadataArchiveTypeStore typeStore = new OpenMetadataArchiveTypeStore();

        List<TypeDef> typeDefs = new DirectoryBasedOpenMetadataArchiveStoreList<>(TypeDef.class, archiveStore, archiveStore.getTypeDefDirectories(), auditLog);
        List<AttributeTypeDef> attributeTypeDefs = new DirectoryBasedOpenMetadataArchiveStoreList<>(AttributeTypeDef.class, archiveStore, archiveStore.getAttributeTypeDefDirectories(), auditLog);
        List<TypeDefPatch> typeDefPatches = new DirectoryBasedOpenMetadataArchiveStoreList<>(TypeDefPatch.class, archiveStore, archiveStore.getTypeDefPatchesDirectories(), auditLog);

        typeStore.setNewTypeDefs(typeDefs);
        typeStore.setAttributeTypeDefs(attributeTypeDefs);
        typeStore.setTypeDefPatches(typeDefPatches);

        return typeStore;
    }


    /**
     * Set up the instance store of the archive.  It has three sections - entities, relationships and classifications.
     *
     * @return instance store
     */
    private OpenMetadataArchiveInstanceStore getArchiveInstanceStore()
    {
        OpenMetadataArchiveInstanceStore instanceStore = new OpenMetadataArchiveInstanceStore();

        List<EntityDetail>                  entities = new DirectoryBasedOpenMetadataArchiveStoreList<>(EntityDetail.class, archiveStore, archiveStore.getEntityDirectories(), auditLog);
        List<Relationship>                  relationships = new DirectoryBasedOpenMetadataArchiveStoreList<>(Relationship.class, archiveStore, archiveStore.getRelationshipDirectories(), auditLog);
        List<ClassificationEntityExtension> classifications = new DirectoryBasedOpenMetadataArchiveStoreList<>(ClassificationEntityExtension.class, archiveStore, archiveStore.getClassificationDirectories(), auditLog);

        instanceStore.setEntities(entities);
        instanceStore.setRelationships(relationships);
        instanceStore.setClassifications(classifications);

        return instanceStore;
    }


    /**
     * Set new contents into the archive.  This overrides any content previously stored.
     *
     * @param archiveContents   OpenMetadataArchive object
     */
    @Override
    public void setArchiveContents(OpenMetadataArchive   archiveContents)
    {
        final String methodName = "setArchiveContents";

        try
        {
            log.debug("Removing current contents");

            archiveStore.removeArchive(archiveStoreName, methodName);

            log.debug("Setting up directory structure");

            archiveStore.initializeArchive(methodName);

            if (archiveContents != null)
            {
                log.debug("Writing open metadata archive properties: " + archiveContents.getArchiveProperties());

                OpenMetadataArchiveProperties archiveProperties = archiveContents.getArchiveProperties();
                this.archiveBuilder = new OMRSArchiveBuilder(archiveProperties);

                archiveStore.writeProperties(archiveProperties, methodName);

                if (archiveContents.getArchiveTypeStore() != null)
                {
                    log.debug("Writing optional type store");

                    OpenMetadataArchiveTypeStore typeStore = archiveContents.getArchiveTypeStore();

                    if (typeStore.getAttributeTypeDefs() != null)
                    {
                        log.debug("Writing attribute type defs");

                        for (AttributeTypeDef attributeTypeDef : typeStore.getAttributeTypeDefs())
                        {
                            if (attributeTypeDef != null)
                            {
                                switch(attributeTypeDef.getCategory())
                                {
                                    case PRIMITIVE:
                                        this.addPrimitiveDef((PrimitiveDef) attributeTypeDef);
                                        break;

                                    case COLLECTION:
                                        this.addCollectionDef((CollectionDef) attributeTypeDef);
                                        break;

                                    case ENUM_DEF:
                                        this.addEnumDef((EnumDef) attributeTypeDef);
                                        break;
                                }
                            }
                        }
                    }

                    if (typeStore.getNewTypeDefs() != null)
                    {
                        log.debug("Writing type defs");

                        for (TypeDef typeDef : typeStore.getNewTypeDefs())
                        {
                            if (typeDef != null)
                            {
                                switch (typeDef.getCategory())
                                {
                                    case ENTITY_DEF:
                                        this.addEntityDef((EntityDef) typeDef);
                                        break;

                                    case RELATIONSHIP_DEF:
                                        this.addRelationshipDef((RelationshipDef) typeDef);
                                        break;

                                    case CLASSIFICATION_DEF:
                                        this.addClassificationDef((ClassificationDef) typeDef);
                                        break;
                                }
                            }
                        }
                    }

                    if (typeStore.getTypeDefPatches() != null)
                    {
                        log.debug("Writing type def patches");

                        for (TypeDefPatch typeDefPatch : typeStore.getTypeDefPatches())
                        {
                            if (typeDefPatch != null)
                            {
                                this.addTypeDefPatch(typeDefPatch);
                            }
                        }
                    }
                }

                if (archiveContents.getArchiveInstanceStore() != null)
                {
                    log.debug("Writing optional instance store");

                    OpenMetadataArchiveInstanceStore instanceStore = archiveContents.getArchiveInstanceStore();

                    if (instanceStore.getEntities() != null)
                    {
                        log.debug("Writing entities");

                        for (EntityDetail entity : instanceStore.getEntities())
                        {
                            if (entity != null)
                            {
                                this.addEntity(entity);
                            }
                        }
                    }

                    if (instanceStore.getRelationships() != null)
                    {
                        log.debug("Writing relationships");

                        for (Relationship relationship : instanceStore.getRelationships())
                        {
                            if (relationship != null)
                            {
                                this.addRelationship(relationship);
                            }
                        }
                    }

                    if (instanceStore.getClassifications() != null)
                    {
                        log.debug("Writing classifications");

                        for (ClassificationEntityExtension classification : instanceStore.getClassifications())
                        {
                            if (classification != null)
                            {
                                this.addClassification(classification);
                            }
                        }
                    }
                }
            }
        }
        catch (Exception error)
        {
            log.debug("Unusable Archive Store :(", error);

            if (auditLog != null)
            {
                final String actionDescription = "Unable to open archive directory";

                auditLog.logException(actionDescription,
                                      DirectoryBasedOpenMetadataArchiveStoreConnectorAuditCode.BAD_FILE.getMessageDefinition(archiveStoreName,
                                                                                                                             error.getClass().getName(),
                                                                                                                             error.getMessage()),
                                      error);
            }
        }
    }


    /**
     * Set up archive header and initialize the maps assuming it is building a new archive.
     *
     * It passes parameters used to build the open metadata archive's property header including the
     * default license string.  This determines the license and copyright for all instances in the
     * archive that do not have their own explicit license string.  The default license string
     * will be inserted into each instance with a null license when it is loaded into an open metadata
     * repository.
     *
     * @param archiveGUID unique identifier for this open metadata archive.
     * @param archiveName name of the open metadata archive.
     * @param archiveDescription description of the open metadata archive.
     * @param archiveType enum describing the type of archive this is.
     * @param archiveVersion descriptive name for the version of the archive.
     * @param originatorName name of the originator (person or organization) of the archive.
     * @param originatorLicense default license string for content.
     * @param creationDate data that this archive was created.
     * @param dependsOnArchives list of archives that this archive depends on (null for no dependencies).
     */
    @Override
    public void setArchiveProperties(String                     archiveGUID,
                                     String                     archiveName,
                                     String                     archiveDescription,
                                     OpenMetadataArchiveType    archiveType,
                                     String                     archiveVersion,
                                     String                     originatorName,
                                     String                     originatorLicense,
                                     Date                       creationDate,
                                     List<OpenMetadataArchive>  dependsOnArchives)
    {
        final String methodName = "setArchiveProperties";

        this.archiveBuilder = new OMRSArchiveBuilder(archiveGUID,
                                                     archiveName,
                                                     archiveDescription,
                                                     archiveType,
                                                     archiveVersion,
                                                     originatorName,
                                                     originatorLicense,
                                                     creationDate,
                                                     dependsOnArchives);

        archiveStore.writeProperties(archiveBuilder.getArchiveProperties(), methodName);
    }

    /**
     * Add a new PrimitiveDef to the archive.
     *
     * @param primitiveDef type to add nulls are ignored
     */
    @Override
    public void addPrimitiveDef(PrimitiveDef   primitiveDef)
    {
        final String methodName = "addPrimitiveDef";

        if (primitiveDef != null)
        {
            log.debug("Adding PrimitiveDef: " + primitiveDef.toString());

            archiveBuilder.addPrimitiveDef(primitiveDef);

            archiveStore.writeElement(archiveStore.getFileName(primitiveDef), primitiveDef, primitiveDef.getVersion(), methodName);
        }
    }


    /**
     * Retrieve a PrimitiveDef from the archive.
     *
     * @param primitiveDefName primitive to retrieve
     * @return PrimitiveDef type
     */
    @Override
    public PrimitiveDef getPrimitiveDef(String   primitiveDefName)
    {
        log.debug("Retrieving PrimitiveDef: " + primitiveDefName);

        return archiveBuilder.getPrimitiveDef(primitiveDefName);
    }


    /**
     * Add a new CollectionDef to the archive.
     *
     * @param collectionDef type to add
     */
    @Override
    public void addCollectionDef(CollectionDef  collectionDef)
    {
        final String methodName = "addCollectionDef";

        if (collectionDef != null)
        {
            log.debug("Adding CollectionDef: " + collectionDef.toString());

            archiveBuilder.addCollectionDef(collectionDef);

            archiveStore.writeElement(archiveStore.getFileName(collectionDef), collectionDef, collectionDef.getVersion(), methodName);
        }
    }


    /**
     * Retrieve a CollectionDef from the archive.
     *
     * @param collectionDefName type to retrieve
     * @return CollectionDef type
     */
    @Override
    public CollectionDef getCollectionDef(String  collectionDefName)
    {
        log.debug("Retrieving CollectionDef: " + collectionDefName);

        return archiveBuilder.getCollectionDef(collectionDefName);
    }


    /**
     * Add a new EnumDef to the archive.
     *
     * @param enumDef type to add
     */
    @Override
    public void addEnumDef(EnumDef    enumDef)
    {
        final String methodName = "addEnumDef";

        if (enumDef != null)
        {
            log.debug("Adding EnumDef: " + enumDef.toString());

            archiveBuilder.addEnumDef(enumDef);

            archiveStore.writeElement(archiveStore.getFileName(enumDef), enumDef, enumDef.getVersion(), methodName);
        }
    }


    /**
     * Get an existing EnumDef from the archive.
     *
     * @param enumDefName type to retrieve
     * @return EnumDef object
     */
    @Override
    public EnumDef getEnumDef(String    enumDefName)
    {
        log.debug("Retrieving EnumDef: " + enumDefName);

        return archiveBuilder.getEnumDef(enumDefName);
    }


    /**
     * Add a new ClassificationDef to the archive.
     *
     * @param classificationDef type to add
     */
    @Override
    public void addClassificationDef(ClassificationDef   classificationDef)
    {
        final String methodName = "addClassificationDef";

        archiveBuilder.addClassificationDef(classificationDef);

        archiveStore.writeElement(archiveStore.getFileName(classificationDef), classificationDef, classificationDef.getVersion(), methodName);
    }


    /**
     * Retrieve the relationshipDef or null if it is not defined.
     *
     * @param classificationDefName name of the classification
     * @return the retrieved classification def
     */
    @Override
    public ClassificationDef  getClassificationDef(String classificationDefName)
    {
        log.debug("Retrieving getClassificationDef: " + classificationDefName);

        return archiveBuilder.getClassificationDef(classificationDefName);
    }


    /**
     * Add a new EntityDef to the archive.
     *
     * @param entityDef type to add
     */
    @Override
    public void addEntityDef(EntityDef    entityDef)
    {
        final String methodName = "addEntityDef";

        archiveBuilder.addEntityDef(entityDef);

        archiveStore.writeElement(archiveStore.getFileName(entityDef), entityDef, entityDef.getVersion(), methodName);
    }


    /**
     * Retrieve the entityDef or null if it is not defined.
     *
     * @param entityDefName name of the entity
     * @return the retrieved entity def
     */
    @Override
    public EntityDef  getEntityDef(String   entityDefName)
    {
        log.debug("Retrieving EntityDef: " + entityDefName);

        return archiveBuilder.getEntityDef(entityDefName);
    }


    /**
     * Retrieve the relationshipDef or null if it is not defined.
     *
     * @param relationshipDefName name of the relationship
     * @return the retrieved relationship def
     */
    @Override
    public RelationshipDef  getRelationshipDef(String relationshipDefName)
    {
        log.debug("Retrieving RelationshipDef: " + relationshipDefName);

        return archiveBuilder.getRelationshipDef(relationshipDefName);
    }


    /**
     * Add a new RelationshipDef to the archive.
     *
     * @param relationshipDef type to add
     */
    @Override
    public void addRelationshipDef(RelationshipDef   relationshipDef)
    {
        final String methodName = "addRelationshipDef";

        archiveBuilder.addRelationshipDef(relationshipDef);

        archiveStore.writeElement(archiveStore.getFileName(relationshipDef), relationshipDef, relationshipDef.getVersion(), methodName);
    }


    /**
     * Create a skeleton patch for a TypeDefPatch.
     *
     * @param typeName name of type
     * @return TypeDefPatch
     */
    @Override
    public TypeDefPatch  getPatchForType(String  typeName)
    {
        return archiveBuilder.getPatchForType(typeName);
    }


    /**
     * Add a new patch to the archive.
     *
     * @param typeDefPatch patch
     */
    @Override
    public void addTypeDefPatch(TypeDefPatch  typeDefPatch)
    {
        final String methodName = "addTypeDefPatch";

        archiveBuilder.addTypeDefPatch(typeDefPatch);

        archiveStore.writeElement(archiveStore.getFileName(typeDefPatch), typeDefPatch, typeDefPatch.getApplyToVersion(), methodName);
    }


    /**
     * Return the requested type definition if known.
     *
     * @param typeName name ot type
     * @return type definition
     */
    @Override
    public TypeDef getTypeDefByName(String  typeName)
    {
        return archiveBuilder.getTypeDefByName(typeName);
    }


    /**
     * Add a new entity to the archive.
     *
     * @param entity instance to add
     */
    @Override
    public void addEntity(EntityDetail   entity)
    {
        final String methodName = "addEntity";

        archiveStore.writeElement(archiveStore.getFileName(entity), entity, entity.getVersion(), methodName);
    }


    /**
     * Retrieve an entity from the archive.
     *
     * @param guid unique identifier
     * @return requested entity
     */
    @Override
    public EntityDetail getEntity(String   guid)
    {
        final String methodName = "getEntity";

        EntityDetail dummyEntity = new EntityDetail();

        dummyEntity.setGUID(guid);

        EntityDetail entity = (EntityDetail)archiveStore.readElement(archiveStore.getFileName(dummyEntity), 0, methodName);

        if (entity == null)
        {
            throw new OMRSLogicErrorException(DirectoryBasedOpenMetadataArchiveStoreConnectorErrorCode.UNKNOWN_GUID.getMessageDefinition(methodName, guid),
                                              this.getClass().getName(),
                                              methodName);
        }

        return entity;
    }



    /**
     * Retrieve an entity from the archive.
     *
     * @param guid unique identifier
     * @return requested entity
     */
    @Override
    public EntityDetail queryEntity(String   guid)
    {
        final String methodName = "queryEntity";

        EntityDetail dummyEntity = new EntityDetail();

        dummyEntity.setGUID(guid);

        return (EntityDetail)archiveStore.readElement(archiveStore.getFileName(dummyEntity), 0, methodName);
    }


    /**
     * Add a new relationship to the archive.
     *
     * @param relationship instance to add
     */
    @Override
    public void addRelationship(Relationship  relationship)
    {
        final String methodName = "addRelationship";

        archiveStore.writeElement(archiveStore.getFileName(relationship), relationship, 0, methodName);
    }


    /**
     * Retrieve a relationship from the archive.
     *
     * @param guid unique identifier
     * @return requested relationship
     */
    @Override
    public Relationship getRelationship(String   guid)
    {
        final String methodName = "getRelationship";

        Relationship dummyRelationship = new Relationship();

        dummyRelationship.setGUID(guid);

        Relationship relationship = (Relationship)archiveStore.readElement(archiveStore.getFileName(dummyRelationship), 0, methodName);

        if (relationship == null)
        {
            throw new OMRSLogicErrorException(DirectoryBasedOpenMetadataArchiveStoreConnectorErrorCode.UNKNOWN_GUID.getMessageDefinition(methodName, guid),
                                              this.getClass().getName(),
                                              methodName);
        }

        return relationship;
    }


    /**
     * Retrieve a relationship from the archive.
     *
     * @param guid unique identifier
     * @return requested relationship
     */
    @Override
    public Relationship queryRelationship(String   guid)
    {
        final String methodName = "queryRelationship";

        Relationship dummyRelationship = new Relationship();

        dummyRelationship.setGUID(guid);

        return (Relationship)archiveStore.readElement(archiveStore.getFileName(dummyRelationship), 0, methodName);
    }


    /**
     * Add a new classification to the archive.
     *
     * @param classification instance to add
     */
    @Override
    public void addClassification(ClassificationEntityExtension classification)
    {
        final String methodName = "addClassification";

        archiveStore.writeElement(archiveStore.getFileName(classification), classification, 0, methodName);
    }


    /**
     * Retrieve a classification extension from the archive.
     *
     * @param entityGUID unique identifier of entity
     * @param classificationName name of the classification
     * @return requested classification extension
     */
    @Override
    public ClassificationEntityExtension getClassification(String entityGUID,
                                                           String classificationName)
    {
        final String methodName = "getClassification";

        Classification                dummyClassification = new Classification();
        EntityProxy                   dummyEntity         = new EntityProxy();
        ClassificationEntityExtension dummyExtension      = new ClassificationEntityExtension();

        dummyClassification.setName(classificationName);
        dummyEntity.setGUID(entityGUID);
        dummyExtension.setHeaderVersion(ClassificationEntityExtension.CURRENT_CLASSIFICATION_EXT_HEADER_VERSION);
        dummyExtension.setClassification(dummyClassification);
        dummyExtension.setEntityToClassify(dummyEntity);

        ClassificationEntityExtension extension = (ClassificationEntityExtension)archiveStore.readElement(archiveStore.getFileName(dummyExtension), 0, methodName);

        if (extension == null)
        {
            throw new OMRSLogicErrorException(DirectoryBasedOpenMetadataArchiveStoreConnectorErrorCode.UNKNOWN_GUID.getMessageDefinition(methodName, entityGUID + ":" + classificationName),
                                              this.getClass().getName(),
                                              methodName);
        }

        return extension;
    }


    /**
     * Retrieve a classification extension from the archive.
     *
     * @param entityGUID unique identifier of entity
     * @param classificationName name of the classification
     * @return requested classification extension
     */
    @Override
    public ClassificationEntityExtension queryClassification(String entityGUID,
                                                             String classificationName)
    {
        final String methodName = "queryClassification";

        Classification                dummyClassification = new Classification();
        EntityProxy                   dummyEntity         = new EntityProxy();
        ClassificationEntityExtension dummyExtension      = new ClassificationEntityExtension();

        dummyClassification.setName(classificationName);
        dummyEntity.setGUID(entityGUID);
        dummyExtension.setHeaderVersion(ClassificationEntityExtension.CURRENT_CLASSIFICATION_EXT_HEADER_VERSION);
        dummyExtension.setClassification(dummyClassification);
        dummyExtension.setEntityToClassify(dummyEntity);

        return (ClassificationEntityExtension)archiveStore.readElement(archiveStore.getFileName(dummyExtension), 0, methodName);
    }


    /**
     * Free up any resources held since the connector is no longer needed.
     *
     * @throws ConnectorCheckedException there is a problem within the connector.
     */
    @Override
    public  void disconnect() throws ConnectorCheckedException
    {
        super.disconnect();

        log.debug("Closing Config Store.");
    }
}
