/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.dynamicarchivers;

import org.odpi.openmetadata.engineservices.repositorygovernance.connector.RepositoryGovernanceContext;
import org.odpi.openmetadata.engineservices.repositorygovernance.connector.RepositoryGovernanceServiceConnector;
import org.odpi.openmetadata.engineservices.repositorygovernance.ffdc.RepositoryGovernanceErrorCode;
import org.odpi.openmetadata.frameworks.connectors.Connector;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.archiveutilities.OMRSArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.OpenMetadataArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.OpenMetadataArchiveCache;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.OpenMetadataArchiveStoreConnector;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchive;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.OpenMetadataArchiveType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ClassificationEntityExtension;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.ClassificationDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.CollectionDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EntityDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.EnumDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.PrimitiveDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.RelationshipDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDef;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefPatch;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * DynamicArchiveService describes a specific type of connector that is responsible for managing the content
 * of a specific open metadata archive.  Information about the available metadata is passed in the archive context.
 * The returned archive context also contains the status of this service.
 */
public abstract class DynamicArchiveService extends RepositoryGovernanceServiceConnector
{
    /*
     * This is the header information for the archive.
     */
    protected static final String                  archiveGUID        = "9cbd2b33-e80f-4df2-adc6-d859ebff4c34";
    protected static final String                  archiveName        = "CocoGovernanceEngineDefinitions";
    protected static final String                  archiveLicense     = "Apache-2.0";
    protected static final String                  archiveDescription = "Governance Engines for Coco Pharmaceuticals.";
    protected static final OpenMetadataArchiveType archiveType        = OpenMetadataArchiveType.CONTENT_PACK;
    protected static final String                  originatorName     = "Egeria Project";
    protected static final Date                    creationDate       = new Date(1639984840038L);

    /*
     * Specific values for initializing TypeDefs
     */
    protected static final long   versionNumber = 1L;
    protected static final String versionName   = "1.0";


    protected List<ArchiveDestination>    archiveDestinations             = new ArrayList<>();



    /**
     * Retrieve and validate the list of embedded connectors and shift them for OpenMetadataArchiveStore connectors.
     *
     * @return list of open metadata archive store connectors
     */
    private List<ArchiveDestination> getEmbeddedArchiveStoresConnectors()
    {
        List<ArchiveDestination> archiveDestinations = new ArrayList<>();

        if (embeddedConnectors != null)
        {
            for (Connector embeddedConnector : embeddedConnectors)
            {
                if (embeddedConnector != null)
                {
                    if (embeddedConnector instanceof OpenMetadataArchiveStoreConnector)
                    {
                        ArchiveDestination archiveDestination = new ArchiveDestination((OpenMetadataArchiveStoreConnector) embeddedConnector);

                        archiveDestinations.add(archiveDestination);
                    }
                }
            }
        }

        return archiveDestinations;
    }


    /**
     * Indicates that the archive service is completely configured and can begin processing.
     * Any embedded connectors are started.
     * This is the method where the function of the archive service is implemented in the subclass.
     * This is a standard method from the Open Connector Framework (OCF) so
     * be sure to call super.start() in your version.
     *
     * @throws ConnectorCheckedException there is a problem within the archive service.
     * @throws UserNotAuthorizedException the connector was disconnected before/during start
     */
    @Override
    public void start() throws ConnectorCheckedException, UserNotAuthorizedException
    {
        super.start();

        validateContext(repositoryGovernanceContext);

        this.archiveDestinations = this.getEmbeddedArchiveStoresConnectors();
    }


    /**
     * Provide a common exception for unexpected errors.
     *
     * @param methodName calling method
     * @param error caught exception
     * @throws ConnectorCheckedException wrapped exception
     */
    protected void handleUnexpectedException(String    methodName,
                                             Exception error) throws ConnectorCheckedException
    {
        throw new ConnectorCheckedException(RepositoryGovernanceErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(repositoryGovernanceServiceName,
                                                                                                                    error.getClass().getName(),
                                                                                                                    methodName,
                                                                                                                    error.getMessage()),
                                            this.getClass().getName(),
                                            methodName);
    }



    /**
     * Verify that the context has been set up for the subclass
     *
     * @param governanceContext context from the subclass
     * @throws ConnectorCheckedException error to say that the connector (governance action service) is not able to proceed because
     * it has not been set up correctly.
     */
    protected void validateContext(RepositoryGovernanceContext governanceContext) throws ConnectorCheckedException
    {
        final String methodName = "start";

        if (governanceContext == null)
        {
            throw new ConnectorCheckedException(RepositoryGovernanceErrorCode.NULL_REPOSITORY_GOVERNANCE_CONTEXT.getMessageDefinition(
                    repositoryGovernanceServiceName),
                                                this.getClass().getName(),
                                                methodName);
        }
    }


    /**
     * Once the content of the archive has been added to the archive builder, an archive object can be retrieved.
     *
     * @return open metadata archive object with all the supplied content in it.
     */
    public OpenMetadataArchive getOpenMetadataArchive()
    {
        if (archiveDestinations != null)
        {
            for (ArchiveDestination archiveDestination : archiveDestinations)
            {
                if (archiveDestination != null)
                {
                    OpenMetadataArchive openMetadataArchive = archiveDestination.getOpenMetadataArchive();

                    if (openMetadataArchive != null)
                    {
                        return openMetadataArchive;
                    }
                }
            }
        }

        return null;
    }


    /**
     * Set up archive header.
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
        if (archiveDestinations != null)
        {
            for (ArchiveDestination archiveDestination : archiveDestinations)
            {
                if (archiveDestination != null)
                {
                    archiveDestination.setArchiveProperties(archiveGUID, archiveName, archiveDescription, archiveType, archiveVersion, originatorName, originatorLicense, creationDate, dependsOnArchives);
                }
            }
        }
    }


    /**
     * Return the archive properties as will appear in the archive.  Null is returned if archive properties not set up.
     *
     * @return property bean
     */
    protected OpenMetadataArchiveProperties getArchiveProperties()
    {
        if (archiveDestinations != null)
        {
            for (ArchiveDestination archiveDestination : archiveDestinations)
            {
                if (archiveDestination != null)
                {
                    return archiveDestination.getArchiveProperties();
                }
            }
        }
        return null;
    }


    /**
     * Add a new PrimitiveDef to the archive.
     *
     * @param primitiveDef type to add nulls are ignored
     */
    protected void addPrimitiveDef(PrimitiveDef primitiveDef)
    {
        if (archiveDestinations != null)
        {
            for (ArchiveDestination archiveDestination : archiveDestinations)
            {
                if (archiveDestination != null)
                {
                    archiveDestination.addPrimitiveDef(primitiveDef);
                }
            }
        }
    }


    /**
     * Retrieve a PrimitiveDef from the archive.
     *
     * @param primitiveDefName primitive to retrieve
     * @return PrimitiveDef type
     */
    protected PrimitiveDef getPrimitiveDef(String   primitiveDefName)
    {
        if (archiveDestinations != null)
        {
            for (ArchiveDestination archiveDestination : archiveDestinations)
            {
                if (archiveDestination != null)
                {
                    return archiveDestination.getPrimitiveDef(primitiveDefName);
                }
            }
        }
        return null;
    }


    /**
     * Add a new CollectionDef to the archive.
     *
     * @param collectionDef type to add
     */
    protected void addCollectionDef(CollectionDef collectionDef)
    {
        if (archiveDestinations != null)
        {
            for (ArchiveDestination archiveDestination : archiveDestinations)
            {
                if (archiveDestination != null)
                {
                    archiveDestination.addCollectionDef(collectionDef);
                }
            }
        }
    }


    /**
     * Retrieve a CollectionDef from the archive.
     *
     * @param collectionDefName type to retrieve
     * @return CollectionDef type
     */
    protected CollectionDef getCollectionDef(String  collectionDefName)
    {
        if (archiveDestinations != null)
        {
            for (ArchiveDestination archiveDestination : archiveDestinations)
            {
                if (archiveDestination != null)
                {
                    return archiveDestination.getCollectionDef(collectionDefName);
                }
            }
        }
        return null;
    }


    /**
     * Add a new EnumDef to the archive.
     *
     * @param enumDef type to add
     */
    protected void addEnumDef(EnumDef enumDef)
    {
        if (archiveDestinations != null)
        {
            for (ArchiveDestination archiveDestination : archiveDestinations)
            {
                if (archiveDestination != null)
                {
                    archiveDestination.addEnumDef(enumDef);
                }
            }
        }
    }


    /**
     * Get an existing EnumDef from the archive.
     *
     * @param enumDefName type to retrieve
     * @return EnumDef object
     */
    protected EnumDef getEnumDef(String    enumDefName)
    {
        if (archiveDestinations != null)
        {
            for (ArchiveDestination archiveDestination : archiveDestinations)
            {
                if (archiveDestination != null)
                {
                    return archiveDestination.getEnumDef(enumDefName);
                }
            }
        }
        return null;
    }


    /**
     * Add a new ClassificationDef to the archive.
     *
     * @param classificationDef type to add
     */
    protected void addClassificationDef(ClassificationDef classificationDef)
    {
        if (archiveDestinations != null)
        {
            for (ArchiveDestination archiveDestination : archiveDestinations)
            {
                if (archiveDestination != null)
                {
                    archiveDestination.addClassificationDef(classificationDef);
                }
            }
        }
    }


    /**
     * Retrieve the relationshipDef or null if it is not defined.
     *
     * @param classificationDef name of the classification
     * @return the retrieved classification def
     */
    protected ClassificationDef  getClassificationDef(String   classificationDef)
    {
        if (archiveDestinations != null)
        {
            for (ArchiveDestination archiveDestination : archiveDestinations)
            {
                if (archiveDestination != null)
                {
                    return archiveDestination.getClassificationDef(classificationDef);
                }
            }
        }
        return null;
    }


    /**
     * Add a new EntityDef to the archive.
     *
     * @param entityDef type to add
     */
    protected void addEntityDef(EntityDef entityDef)
    {
        if (archiveDestinations != null)
        {
            for (ArchiveDestination archiveDestination : archiveDestinations)
            {
                if (archiveDestination != null)
                {
                    archiveDestination.addEntityDef(entityDef);
                }
            }
        }
    }


    /**
     * Retrieve the entityDef or null if it is not defined.
     *
     * @param entityDefName name of the entity
     * @return the retrieved entity def
     */
    protected EntityDef  getEntityDef(String   entityDefName)
    {
        if (archiveDestinations != null)
        {
            for (ArchiveDestination archiveDestination : archiveDestinations)
            {
                if (archiveDestination != null)
                {
                    return archiveDestination.getEntityDef(entityDefName);
                }
            }
        }

        return null;
    }


    /**
     * Retrieve the relationshipDef or null if it is not defined.
     *
     * @param relationshipDefName name of the relationship
     * @return the retrieved relationship def
     */
    protected RelationshipDef getRelationshipDef(String   relationshipDefName)
    {
        if (archiveDestinations != null)
        {
            for (ArchiveDestination archiveDestination : archiveDestinations)
            {
                if (archiveDestination != null)
                {
                    return archiveDestination.getRelationshipDef(relationshipDefName);
                }
            }
        }

        return null;
    }

    /**
     * Add a new RelationshipDef to the archive.
     *
     * @param relationshipDef type to add
     */
    protected void addRelationshipDef(RelationshipDef   relationshipDef)
    {
        if (archiveDestinations != null)
        {
            for (ArchiveDestination archiveDestination : archiveDestinations)
            {
                if (archiveDestination != null)
                {
                    archiveDestination.addRelationshipDef(relationshipDef);
                }
            }
        }
    }


    /**
     * Create a skeleton patch for a TypeDefPatch.
     *
     * @param typeName name of type
     * @return TypeDefPatch
     */
    protected TypeDefPatch getPatchForType(String  typeName)
    {
        if (archiveDestinations != null)
        {
            for (ArchiveDestination archiveDestination : archiveDestinations)
            {
                if (archiveDestination != null)
                {
                    return archiveDestination.getPatchForType(typeName);
                }
            }
        }

        return null;
    }


    /**
     * Add a new patch to the archive.
     *
     * @param typeDefPatch patch
     */
    protected void addTypeDefPatch(TypeDefPatch  typeDefPatch)
    {
        if (archiveDestinations != null)
        {
            for (ArchiveDestination archiveDestination : archiveDestinations)
            {
                if (archiveDestination != null)
                {
                    archiveDestination.addTypeDefPatch(typeDefPatch);
                }
            }
        }
    }


    /**
     * Return the requested type definition if known.
     *
     * @param typeName name ot type
     * @return type definition
     */
    protected TypeDef getTypeDefByName(String  typeName)
    {
        if (archiveDestinations != null)
        {
            for (ArchiveDestination archiveDestination : archiveDestinations)
            {
                if (archiveDestination != null)
                {
                    return archiveDestination.getTypeDefByName(typeName);
                }
            }
        }

        return null;
    }


    /**
     * Add a new entity to the archive.
     *
     * @param entity instance to add
     */
    protected void addEntity(EntityDetail entity)
    {
        if (archiveDestinations != null)
        {
            for (ArchiveDestination archiveDestination : archiveDestinations)
            {
                if (archiveDestination != null)
                {
                    archiveDestination.addEntity(entity);
                }
            }
        }
    }


    /**
     * Retrieve an entity from the archive.
     *
     * @param guid unique identifier
     * @return requested entity
     */
    protected EntityDetail getEntity(String   guid)
    {
        if (archiveDestinations != null)
        {
            for (ArchiveDestination archiveDestination : archiveDestinations)
            {
                if (archiveDestination != null)
                {
                    return archiveDestination.getEntity(guid);
                }
            }
        }

        return null;
    }


    /**
     * Add a new relationship to the archive.
     *
     * @param relationship instance to add
     */
    protected void addRelationship(Relationship relationship)
    {
        if (archiveDestinations != null)
        {
            for (ArchiveDestination archiveDestination : archiveDestinations)
            {
                if (archiveDestination != null)
                {
                    archiveDestination.addRelationship(relationship);
                }
            }
        }
    }


    /**
     * Retrieve a relationship from the archive.
     *
     * @param guid unique identifier
     * @return requested relationship
     */
    protected Relationship getRelationship(String   guid)
    {
        if (archiveDestinations != null)
        {
            for (ArchiveDestination archiveDestination : archiveDestinations)
            {
                if (archiveDestination != null)
                {
                    return archiveDestination.getRelationship(guid);
                }
            }
        }

        return null;
    }


    /**
     * Add a new classification to the archive.
     *
     * @param classification instance to add
     */
    protected void addClassification(ClassificationEntityExtension classification)
    {
        if (archiveDestinations != null)
        {
            for (ArchiveDestination archiveDestination : archiveDestinations)
            {
                if (archiveDestination != null)
                {
                    archiveDestination.addClassification(classification);
                }
            }
        }
    }


    /**
     * Retrieve a classification extension from the archive.
     *
     * @param entityGUID unique identifier of entity
     * @param classificationName name of the classification
     * @return requested classification extension
     */
    protected ClassificationEntityExtension getClassification(String entityGUID,
                                                              String classificationName)
    {
        if (archiveDestinations != null)
        {
            for (ArchiveDestination archiveDestination : archiveDestinations)
            {
                if (archiveDestination != null)
                {
                    return archiveDestination.getClassification(entityGUID, classificationName);
                }
            }
        }

        return null;
    }


    /**
     * Details of one of the archive destination that this archive service is managing.  It is able to support an archive that builds in memory and
     * is written out in one go - or one that stores one element at a time.
     */
    protected class ArchiveDestination implements OpenMetadataArchiveBuilder, OpenMetadataArchiveCache
    {
        OpenMetadataArchiveBuilder        builder = null;
        OpenMetadataArchiveStoreConnector store;
        OpenMetadataArchiveCache          cache = null;

        /**
         * Simple constructor.
         *
         * @param store connector to the store
         */
        protected ArchiveDestination(OpenMetadataArchiveStoreConnector store)
        {
            this.store = store;

            if (store instanceof OpenMetadataArchiveBuilder)
            {
                this.builder = (OpenMetadataArchiveBuilder) store;
            }
        }


        /**
         * Once the content of the archive has been added to the archive builder, an archive object can be retrieved.
         *
         * @return open metadata archive object with all the supplied content in it.
         */
        @Override
        public OpenMetadataArchive getOpenMetadataArchive()
        {
            if (this.cache != null)
            {
                return this.cache.getOpenMetadataArchive();
            }

            return null;
        }


        /**
         * Set up archive header.
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
            if (this.builder != null)
            {
                this.builder.setArchiveProperties(archiveGUID, archiveName, archiveDescription, archiveType, archiveVersion, originatorName, originatorLicense, creationDate, dependsOnArchives);
            }
            else
            {
                OMRSArchiveBuilder cacheBuilder = new OMRSArchiveBuilder(archiveGUID,
                                                                         archiveName,
                                                                         archiveDescription,
                                                                         archiveType,
                                                                         archiveVersion,
                                                                         originatorName,
                                                                         originatorLicense,
                                                                         creationDate,
                                                                         dependsOnArchives);

                this.builder = cacheBuilder;
                this.cache = cacheBuilder;
            }
        }


        /**
         * Return the archive properties as will appear in the archive.  Null is returned if archive properties not set up.
         *
         * @return property bean
         */
        @Override
        public OpenMetadataArchiveProperties getArchiveProperties()
        {
            return this.builder.getArchiveProperties();
        }


        /**
         * Add a new PrimitiveDef to the archive.
         *
         * @param primitiveDef type to add nulls are ignored
         */
        @Override
        public void addPrimitiveDef(PrimitiveDef primitiveDef)
        {
            this.builder.addPrimitiveDef(primitiveDef);
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
            return this.builder.getPrimitiveDef(primitiveDefName);
        }


        /**
         * Add a new CollectionDef to the archive.
         *
         * @param collectionDef type to add
         */
        @Override
        public void addCollectionDef(CollectionDef collectionDef)
        {
            this.builder.addCollectionDef(collectionDef);
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
            return this.builder.getCollectionDef(collectionDefName);
        }


        /**
         * Add a new EnumDef to the archive.
         *
         * @param enumDef type to add
         */
        @Override
        public void addEnumDef(EnumDef enumDef)
        {
            this.builder.addEnumDef(enumDef);
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
            return this.builder.getEnumDef(enumDefName);
        }


        /**
         * Add a new ClassificationDef to the archive.
         *
         * @param classificationDef type to add
         */
        @Override
        public void addClassificationDef(ClassificationDef classificationDef)
        {
            this.builder.addClassificationDef(classificationDef);
        }


        /**
         * Add a new EntityDef to the archive.
         *
         * @param entityDef type to add
         */
        @Override
        public void addEntityDef(EntityDef entityDef)
        {
            this.builder.addEntityDef(entityDef);
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
            return this.builder.getEntityDef(entityDefName);
        }


        /**
         * Retrieve the relationshipDef or null if it is not defined.
         *
         * @param relationshipDefName name of the relationship
         * @return the retrieved relationship def
         */
        @Override
        public RelationshipDef getRelationshipDef(String   relationshipDefName)
        {
            return this.builder.getRelationshipDef(relationshipDefName);
        }


        /**
         * Retrieve the relationshipDef or null if it is not defined.
         *
         * @param classificationDef name of the classification
         * @return the retrieved classification def
         */
        @Override
        public ClassificationDef  getClassificationDef(String   classificationDef)
        {
            return this.builder.getClassificationDef(classificationDef);
        }


        /**
         * Add a new RelationshipDef to the archive.
         *
         * @param relationshipDef type to add
         */
        @Override
        public void addRelationshipDef(RelationshipDef   relationshipDef)
        {
            this.builder.addRelationshipDef(relationshipDef);
        }


        /**
         * Create a skeleton patch for a TypeDefPatch.
         *
         * @param typeName name of type
         * @return TypeDefPatch
         */
        @Override
        public TypeDefPatch getPatchForType(String  typeName)
        {
            return this.builder.getPatchForType(typeName);
        }


        /**
         * Add a new patch to the archive.
         *
         * @param typeDefPatch patch
         */
        @Override
        public void addTypeDefPatch(TypeDefPatch  typeDefPatch)
        {
            this.builder.addTypeDefPatch(typeDefPatch);
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
            return this.builder.getTypeDefByName(typeName);
        }


        /**
         * Add a new entity to the archive.
         *
         * @param entity instance to add
         */
        @Override
        public void addEntity(EntityDetail entity)
        {
            this.builder.addEntity(entity);
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
            return this.builder.getEntity(guid);
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
            return this.builder.queryEntity(guid);
        }


        /**
         * Add a new relationship to the archive.
         *
         * @param relationship instance to add
         */
        @Override
        public void addRelationship(Relationship relationship)
        {
            this.builder.addRelationship(relationship);
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
            return this.builder.getRelationship(guid);
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
            return this.builder.queryRelationship(guid);
        }


        /**
         * Add a new classification to the archive.
         *
         * @param classification instance to add
         */
        @Override
        public void addClassification(ClassificationEntityExtension classification)
        {
            this.builder.addClassification(classification);
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
            return this.builder.getClassification(entityGUID, classificationName);
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
            return this.builder.queryClassification(entityGUID, classificationName);
        }
    }
}
