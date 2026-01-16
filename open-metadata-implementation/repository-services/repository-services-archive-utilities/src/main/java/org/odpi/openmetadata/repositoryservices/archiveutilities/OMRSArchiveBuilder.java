/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.archiveutilities;

import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.OpenMetadataArchiveBuilder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.OpenMetadataArchiveCache;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ClassificationEntityExtension;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.utilities.OMRSRepositoryPropertiesUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSLogicErrorException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;

import java.util.*;

/**
 * OMRSArchiveBuilder creates an in-memory copy of an open metadata archive that can be saved to disk or processed
 * by a server.
 */
public class OMRSArchiveBuilder implements OpenMetadataArchiveBuilder, OpenMetadataArchiveCache
{
    /*
     * Archive properties supplied on the constructor
     */
    private OpenMetadataArchiveProperties archiveProperties;

    /*
     * Maps and lists for accumulating TypeDefs and instances as the content of the archive is built up.
     * The maps accumulate content from both dependent archives and the new archive being built.
     * The lists contain only the new content, and will ultimately be used when assembling the archive.
     */
    private final Map<String, PrimitiveDef>                  primitiveDefMap       = new HashMap<>();
    private final List<PrimitiveDef>                         primitiveDefList      = new ArrayList<>();
    private final Map<String, EnumDef>                       enumDefMap            = new HashMap<>();
    private final List<EnumDef>                              enumDefList           = new ArrayList<>();
    private final Map<String, CollectionDef>                 collectionDefMap      = new HashMap<>();
    private final List<CollectionDef>                        collectionDefList     = new ArrayList<>();
    private final Map<String, ClassificationDef>             classificationDefMap  = new HashMap<>();
    private final List<ClassificationDef>                    classificationDefList = new ArrayList<>();
    private final Map<String, EntityDef>                     entityDefMap          = new HashMap<>();
    private final List<EntityDef>                            entityDefList         = new ArrayList<>();
    private final Map<String, RelationshipDef>               relationshipDefMap    = new HashMap<>();
    private final List<RelationshipDef>                      relationshipDefList   = new ArrayList<>();
    private final List<TypeDefPatch>                         typeDefPatchList      = new ArrayList<>();
    private final Map<String, EntityDetail>                  entityDetailMap       = new HashMap<>();
    private final List<EntityDetail>                         entityDetailList      = new ArrayList<>();
    private final Map<String, Relationship>                  relationshipMap       = new HashMap<>();
    private final List<Relationship>                         relationshipList      = new ArrayList<>();
    private final Map<String, ClassificationEntityExtension> classificationMap     = new HashMap<>();
    private final List<ClassificationEntityExtension>        classificationList    = new ArrayList<>();
    private final Map<String, Object>                        guidMap               = new HashMap<>();
    private final Map<String, Object>                        nameMap               = new HashMap<>();
    private final Map<String, Set<String>>                   entityAttributeMap    = new HashMap<>();


    private static final Logger log = LoggerFactory.getLogger(OMRSArchiveBuilder.class);


    /**
     * Simple constructor.
     *
     * @param archiveGUID unique identifier for this open metadata archive.
     * @param archiveName name of the open metadata archive.
     * @param archiveDescription description of the open metadata archive.
     * @param archiveType enum describing the type of archive this is.
     * @param originatorName name of the originator (person or organization) of the archive.
     * @param creationDate data that this archive was created.
     * @param dependsOnArchives list of GUIDs for archives that this archive depends on (null for no dependencies).
     */
    public OMRSArchiveBuilder(String                     archiveGUID,
                              String                     archiveName,
                              String                     archiveDescription,
                              OpenMetadataArchiveType    archiveType,
                              String                     originatorName,
                              Date                       creationDate,
                              List<OpenMetadataArchive>  dependsOnArchives)
    {
        this(archiveGUID,
             archiveName,
             archiveDescription,
             archiveType,
             null,
             originatorName,
             null,
             creationDate,
             dependsOnArchives);
    }



    /**
     * Constructor for licensed material.
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
     * @param originatorName name of the originator (person or organization) of the archive.
     * @param originatorLicense default license string for content.
     * @param creationDate data that this archive was created.
     * @param dependsOnArchives list of archives that this archive depends on (null for no dependencies).
     */
    public OMRSArchiveBuilder(String                     archiveGUID,
                              String                     archiveName,
                              String                     archiveDescription,
                              OpenMetadataArchiveType    archiveType,
                              String                     originatorName,
                              String                     originatorLicense,
                              Date                       creationDate,
                              List<OpenMetadataArchive>  dependsOnArchives)
    {
        this(archiveGUID,
             archiveName,
             archiveDescription,
             archiveType,
             null,
             originatorName,
             originatorLicense,
             creationDate,
             dependsOnArchives);
    }


    /**
     * Constructor used when content of dependent archives is not needed in the maps.
     *
     * @param properties filled out archive properties
     */
    public OMRSArchiveBuilder(OpenMetadataArchiveProperties properties)
    {
        this.archiveProperties = properties;
    }


    /**
     * Full constructor.
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
    public OMRSArchiveBuilder(String                     archiveGUID,
                              String                     archiveName,
                              String                     archiveDescription,
                              OpenMetadataArchiveType    archiveType,
                              String                     archiveVersion,
                              String                     originatorName,
                              String                     originatorLicense,
                              Date                       creationDate,
                              List<OpenMetadataArchive>  dependsOnArchives)
    {
        this.setArchiveProperties(archiveGUID,
                                  archiveName,
                                  archiveDescription,
                                  archiveType,
                                  archiveVersion,
                                  originatorName,
                                  originatorLicense,
                                  creationDate,
                                  dependsOnArchives);
    }


    /**
     * Set up archive header and initialize the maps assuming it is building a new archive.
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
        final String methodName = "OMRSArchiveBuilder constructor";

        this.archiveProperties = new OpenMetadataArchiveProperties();

        this.archiveProperties.setArchiveGUID(archiveGUID);
        this.archiveProperties.setArchiveName(archiveName);
        this.archiveProperties.setArchiveDescription(archiveDescription);
        this.archiveProperties.setArchiveType(archiveType);
        this.archiveProperties.setArchiveVersion(archiveVersion);
        this.archiveProperties.setOriginatorName(originatorName);
        this.archiveProperties.setOriginatorLicense(originatorLicense);
        this.archiveProperties.setCreationDate(creationDate);

        if (dependsOnArchives == null)
        {
            this.archiveProperties.setDependsOnArchives(null);
        }
        else
        {
            List<String>  dependsOnArchiveGUIDs = new ArrayList<>();

            for (OpenMetadataArchive  openMetadataArchive : dependsOnArchives)
            {
                if (openMetadataArchive != null)
                {
                    /*
                     * Extract the guid for the new archive dependencies.
                     */
                    OpenMetadataArchiveProperties dependentArchiveProperties = openMetadataArchive.getArchiveProperties();

                    if (dependentArchiveProperties != null)
                    {
                        if (dependentArchiveProperties.getArchiveGUID() != null)
                        {
                            dependsOnArchiveGUIDs.add(dependentArchiveProperties.getArchiveGUID());
                        }
                    }

                    /*
                     * Load up the types from the archive into the maps.
                     */
                    OpenMetadataArchiveTypeStore typeStore = openMetadataArchive.getArchiveTypeStore();

                    if (typeStore != null)
                    {
                        List<AttributeTypeDef> attributeTypeDefs = typeStore.getAttributeTypeDefs();

                        if (attributeTypeDefs != null)
                        {
                            for (AttributeTypeDef attributeTypeDef : attributeTypeDefs)
                            {
                                if (attributeTypeDef != null)
                                {
                                    guidMap.put(attributeTypeDef.getGUID(), attributeTypeDef);
                                    nameMap.put(attributeTypeDef.getName(), attributeTypeDef);

                                    switch (attributeTypeDef.getCategory())
                                    {
                                        case PRIMITIVE:
                                            primitiveDefMap.put(attributeTypeDef.getName(), (PrimitiveDef) attributeTypeDef);
                                            break;

                                        case COLLECTION:
                                            collectionDefMap.put(attributeTypeDef.getName(), (CollectionDef) attributeTypeDef);
                                            break;

                                        case ENUM_DEF:
                                            enumDefMap.put(attributeTypeDef.getName(), (EnumDef) attributeTypeDef);
                                            break;
                                    }
                                }
                            }
                        }

                        List<TypeDef> typeDefs = typeStore.getNewTypeDefs();

                        if (typeDefs != null)
                        {
                            for (TypeDef typeDef : typeDefs)
                            {
                                if (typeDef != null)
                                {
                                    addTypeToMaps(typeDef);
                                }
                            }
                        }

                        List<TypeDefPatch> typeDefPatches = typeStore.getTypeDefPatches();

                        if (typeDefPatches != null)
                        {
                            OMRSRepositoryPropertiesUtilities utilities = new OMRSRepositoryPropertiesUtilities();

                            for (TypeDefPatch typeDefPatch : typeDefPatches)
                            {
                                if (typeDefPatch != null)
                                {
                                    TypeDef originalTypeDef = getTypeDefByName(typeDefPatch.getTypeDefName());

                                    try
                                    {
                                        TypeDef updatedTypeDef = utilities.applyPatch(openMetadataArchive.getArchiveProperties().getArchiveName(),
                                                                                      originalTypeDef,
                                                                                      typeDefPatch,
                                                                                      methodName);

                                        if (updatedTypeDef != null)
                                        {
                                            this.addTypeToMaps(updatedTypeDef);
                                        }
                                    }
                                    catch (Exception error)
                                    {
                                        throw new OMRSLogicErrorException(OMRSErrorCode.UNEXPECTED_EXCEPTION.getMessageDefinition(error.getClass().getName(),
                                                                                                                                  methodName,
                                                                                                                                  error.getMessage()),
                                                                          this.getClass().getName(),
                                                                          methodName,
                                                                          error);
                                    }
                                }
                            }
                        }
                    }

                    /*
                     * Load up the instances from the archive into the maps.
                     */
                    OpenMetadataArchiveInstanceStore instanceStore = openMetadataArchive.getArchiveInstanceStore();

                    if (instanceStore != null)
                    {
                        List<EntityDetail> entities = instanceStore.getEntities();

                        if (entities !=null)
                        {
                            for (EntityDetail entity : entities)
                            {
                                if (entity != null)
                                {
                                    guidMap.put(entity.getGUID(), entity);

                                    entityDetailMap.put(entity.getGUID(), entity);
                                }
                            }
                        }

                        List<Relationship> relationships = instanceStore.getRelationships();

                        if (relationships != null)
                        {
                            for (Relationship relationship : relationships)
                            {
                                if (relationship != null)
                                {
                                    guidMap.put(relationship.getGUID(), relationship);

                                    relationshipMap.put(relationship.getGUID(), relationship);
                                }
                            }
                        }

                        List<ClassificationEntityExtension> classifications = instanceStore.getClassifications();

                        if (classifications != null)
                        {
                            for (ClassificationEntityExtension classification : classifications)
                            {
                                if (classification != null)
                                {
                                    String classificationId = classification.getEntityToClassify().getGUID() + "::" + classification.getClassification().getName();

                                    classificationMap.put(classificationId, classification);
                                }
                            }
                        }
                    }
                }
            }

            if (! dependsOnArchiveGUIDs.isEmpty())
            {
                this.archiveProperties.setDependsOnArchives(dependsOnArchiveGUIDs);
            }
            else
            {
                this.archiveProperties.setDependsOnArchives(null);
            }
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
        return this.archiveProperties;
    }


    /**
     * Update the maps with new TypeDefs.
     *
     * @param typeDef type definition
     */
    private void addTypeToMaps(TypeDef   typeDef)
    {
        guidMap.put(typeDef.getGUID(), typeDef);
        nameMap.put(typeDef.getName(), typeDef);

        switch (typeDef.getCategory())
        {
            case ENTITY_DEF:
                entityDefMap.put(typeDef.getName(), (EntityDef) typeDef);
                break;

            case RELATIONSHIP_DEF:
                relationshipDefMap.put(typeDef.getName(), (RelationshipDef) typeDef);
                break;

            case CLASSIFICATION_DEF:
                classificationDefMap.put(typeDef.getName(), (ClassificationDef) typeDef);
                break;
        }
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
            log.debug("Adding PrimitiveDef: " + primitiveDef);
            this.checkForBlanksInTypeName(primitiveDef.getName());

            PrimitiveDef duplicateElement = primitiveDefMap.put(primitiveDef.getName(), primitiveDef);

            if (duplicateElement != null)
            {
                throw new OMRSLogicErrorException(OMRSErrorCode.DUPLICATE_TYPE_IN_ARCHIVE.getMessageDefinition(primitiveDef.getName(),
                                                                                                               AttributeTypeDefCategory.PRIMITIVE.getName(),
                                                                                                               duplicateElement.toString(),
                                                                                                               primitiveDef.toString()),
                                                  this.getClass().getName(),
                                                  methodName);
            }

            Object  duplicateGUID = guidMap.put(primitiveDef.getGUID(), primitiveDef);

            if (duplicateGUID != null)
            {
                throw new OMRSLogicErrorException(OMRSErrorCode.DUPLICATE_GUID_IN_ARCHIVE.getMessageDefinition(primitiveDef.getGUID(),
                                                                                                               duplicateGUID.toString(),
                                                                                                               primitiveDef.toString()),
                                                  this.getClass().getName(),
                                                  methodName);
            }

            Object  duplicateName = nameMap.put(primitiveDef.getName(), primitiveDef);

            if (duplicateName != null)
            {
                throw new OMRSLogicErrorException(OMRSErrorCode.DUPLICATE_TYPENAME_IN_ARCHIVE.getMessageDefinition(primitiveDef.getGUID(),
                                                                                                                   duplicateName.toString(),
                                                                                                                   primitiveDef.toString()),
                                                  this.getClass().getName(),
                                                  methodName);
            }

            primitiveDefList.add(primitiveDef);
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
        final String methodName = "getPrimitiveDef";

        log.debug("Retrieving PrimitiveDef: " + primitiveDefName);

        if (primitiveDefName != null)
        {
            PrimitiveDef   primitiveDef  = primitiveDefMap.get(primitiveDefName);

            if (primitiveDef == null)
            {
                throw new OMRSLogicErrorException(OMRSErrorCode.MISSING_TYPE_IN_ARCHIVE.getMessageDefinition(primitiveDefName,
                                                                                                             AttributeTypeDefCategory.PRIMITIVE.getName()),
                                                  this.getClass().getName(),
                                                  methodName);
            }

            return primitiveDef;
        }
        else
        {
           throw new OMRSLogicErrorException(OMRSErrorCode.MISSING_NAME_FOR_ARCHIVE.getMessageDefinition(AttributeTypeDefCategory.PRIMITIVE.getName()),
                                             this.getClass().getName(),
                                             methodName);
        }
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
            log.debug("Adding CollectionDef: " + collectionDef);
            
            this.checkForBlanksInTypeName(collectionDef.getName());

            CollectionDef duplicateElement = collectionDefMap.put(collectionDef.getName(), collectionDef);

            if (duplicateElement != null)
            {
                throw new OMRSLogicErrorException(OMRSErrorCode.DUPLICATE_TYPE_IN_ARCHIVE.getMessageDefinition(collectionDef.getName(),
                                                                                                               AttributeTypeDefCategory.COLLECTION.getName(),
                                                                                                               duplicateElement.toString(),
                                                                                                               collectionDef.toString()),
                                                  this.getClass().getName(),
                                                  methodName);
            }

            Object  duplicateGUID = guidMap.put(collectionDef.getGUID(), collectionDef);

            if (duplicateGUID != null)
            {
               throw new OMRSLogicErrorException(OMRSErrorCode.DUPLICATE_GUID_IN_ARCHIVE.getMessageDefinition(collectionDef.getGUID(),
                                                                                                               duplicateGUID.toString(),
                                                                                                               collectionDef.toString()),
                                                  this.getClass().getName(),
                                                  methodName);
            }

            Object  duplicateName = nameMap.put(collectionDef.getName(), collectionDef);

            if (duplicateName != null)
            {
                throw new OMRSLogicErrorException(OMRSErrorCode.DUPLICATE_TYPENAME_IN_ARCHIVE.getMessageDefinition(collectionDef.getGUID(),
                                                                                                                   duplicateName.toString(),
                                                                                                                   collectionDef.toString()),
                                                  this.getClass().getName(),
                                                  methodName);
            }

            collectionDefList.add(collectionDef);
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
        final String methodName = "getCollectionDef";

        log.debug("Retrieving CollectionDef: " + collectionDefName);

        if (collectionDefName != null)
        {
            CollectionDef  collectionDef = collectionDefMap.get(collectionDefName);

            if (collectionDef == null)
            {
                throw new OMRSLogicErrorException(OMRSErrorCode.MISSING_TYPE_IN_ARCHIVE.getMessageDefinition(collectionDefName,
                                                                                                             AttributeTypeDefCategory.COLLECTION.getName()),
                                                  this.getClass().getName(),
                                                  methodName);
            }

            return collectionDef;
        }
        else
        {
            throw new OMRSLogicErrorException(OMRSErrorCode.MISSING_NAME_FOR_ARCHIVE.getMessageDefinition(AttributeTypeDefCategory.COLLECTION.getName()),
                                              this.getClass().getName(),
                                              methodName);
        }
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
            log.debug("Adding EnumDef: " + enumDef);

            this.checkForBlanksInTypeName(enumDef.getName());

            EnumDef duplicateElement = enumDefMap.put(enumDef.getName(), enumDef);

            if (duplicateElement != null)
            {
               throw new OMRSLogicErrorException(OMRSErrorCode.DUPLICATE_TYPE_IN_ARCHIVE.getMessageDefinition(enumDef.getName(),
                                                                                                               AttributeTypeDefCategory.ENUM_DEF.getName(),
                                                                                                               duplicateElement.toString(),
                                                                                                               enumDef.toString()),
                                                  this.getClass().getName(),
                                                  methodName);
            }

            Object  duplicateGUID = guidMap.put(enumDef.getGUID(), enumDef);

            if (duplicateGUID != null)
            {
                throw new OMRSLogicErrorException(OMRSErrorCode.DUPLICATE_GUID_IN_ARCHIVE.getMessageDefinition(enumDef.getGUID(),
                                                                                                               duplicateGUID.toString(),
                                                                                                               enumDef.toString()),
                                                  this.getClass().getName(),
                                                  methodName);
            }

            Object  duplicateName = nameMap.put(enumDef.getName(), enumDef);

            if (duplicateName != null)
            {
                throw new OMRSLogicErrorException(OMRSErrorCode.DUPLICATE_TYPENAME_IN_ARCHIVE.getMessageDefinition(enumDef.getGUID(),
                                                                                                                   duplicateName.toString(),
                                                                                                                   enumDef.toString()),
                                                  this.getClass().getName(),
                                                  methodName);
            }

            enumDefList.add(enumDef);
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
        final String methodName = "getEnumDef";

        log.debug("Retrieving EnumDef: " + enumDefName);

        if (enumDefName != null)
        {
            EnumDef enumDef = enumDefMap.get(enumDefName);

            if (enumDef == null)
            {
                throw new OMRSLogicErrorException(OMRSErrorCode.MISSING_TYPE_IN_ARCHIVE.getMessageDefinition(enumDefName,
                                                                                                             AttributeTypeDefCategory.ENUM_DEF.getName()),
                                                  this.getClass().getName(),
                                                  methodName);
            }

            return enumDef;
        }
        else
        {
            throw new OMRSLogicErrorException(OMRSErrorCode.MISSING_NAME_FOR_ARCHIVE.getMessageDefinition(AttributeTypeDefCategory.ENUM_DEF.getName()),
                                              this.getClass().getName(),
                                              methodName);
        }
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

        if (classificationDef != null)
        {
            log.debug("Adding ClassificationDef: " + classificationDef);
            
            this.checkForBlanksInTypeName(classificationDef.getName());

            ClassificationDef duplicateElement = classificationDefMap.put(classificationDef.getName(), classificationDef);

            if (duplicateElement != null)
            {
                throw new OMRSLogicErrorException(OMRSErrorCode.DUPLICATE_TYPE_IN_ARCHIVE.getMessageDefinition(classificationDef.getName(),
                                                                                                               TypeDefCategory.CLASSIFICATION_DEF.getName(),
                                                                                                               duplicateElement.toString(),
                                                                                                               classificationDef.toString()),
                                                  this.getClass().getName(),
                                                  methodName);
            }

            Object  duplicateGUID = guidMap.put(classificationDef.getGUID(), classificationDef);

            if (duplicateGUID != null)
            {
                throw new OMRSLogicErrorException(OMRSErrorCode.DUPLICATE_GUID_IN_ARCHIVE.getMessageDefinition(classificationDef.getGUID(),
                                                                                                               duplicateGUID.toString(),
                                                                                                               classificationDef.toString()),
                                                  this.getClass().getName(),
                                                  methodName);
            }

            Object  duplicateName = nameMap.put(classificationDef.getName(), classificationDef);

            if (duplicateName != null)
            {
                throw new OMRSLogicErrorException(OMRSErrorCode.DUPLICATE_TYPENAME_IN_ARCHIVE.getMessageDefinition(classificationDef.getName(),
                                                                                                                   duplicateName.toString(),
                                                                                                                   classificationDef.toString()),
                                                  this.getClass().getName(),
                                                  methodName);
            }
            if (classificationDef.getPropertiesDefinition() != null)
            {
                Set<String> attributeSet = new HashSet<>();

                for (TypeDefAttribute typeDefAttr :classificationDef.getPropertiesDefinition())
                {
                    String duplicateAttributeName = typeDefAttr.getAttributeName();
                    
                    if (attributeSet.contains(duplicateAttributeName))
                    {
                        /*
                         * relationship duplicate attribute name
                         */
                        throw new OMRSLogicErrorException(OMRSErrorCode.DUPLICATE_CLASSIFICATION_ATTR_IN_ARCHIVE.getMessageDefinition(duplicateAttributeName,
                                                                                                                                      classificationDef.getName()),
                                                          this.getClass().getName(),
                                                          methodName);
                    }
                    attributeSet.add(duplicateAttributeName);
                }
            }

            classificationDefList.add(classificationDef);
        }
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

        if (entityDef != null)
        {
            log.debug("Adding EntityDef: " + entityDef);
            
            this.checkForBlanksInTypeName(entityDef.getName());

            EntityDef duplicateElement = entityDefMap.put(entityDef.getName(), entityDef);

            if (duplicateElement != null)
            {
                throw new OMRSLogicErrorException(OMRSErrorCode.DUPLICATE_TYPE_IN_ARCHIVE.getMessageDefinition(entityDef.getName(),
                                                                                                               TypeDefCategory.ENTITY_DEF.getName(),
                                                                                                               duplicateElement.toString(),
                                                                                                               entityDef.toString()),
                                                  this.getClass().getName(),
                                                  methodName);
            }

            Object  duplicateGUID = guidMap.put(entityDef.getGUID(), entityDef);

            if (duplicateGUID != null)
            {
                throw new OMRSLogicErrorException(OMRSErrorCode.DUPLICATE_GUID_IN_ARCHIVE.getMessageDefinition(entityDef.getGUID(),
                                                                                                               duplicateGUID.toString(),
                                                                                                               entityDef.toString()),
                                                  this.getClass().getName(),
                                                  methodName);
            }

            Object  duplicateName = nameMap.put(entityDef.getName(), entityDef);

            if (duplicateName != null)
            {
                throw new OMRSLogicErrorException(OMRSErrorCode.DUPLICATE_TYPENAME_IN_ARCHIVE.getMessageDefinition(entityDef.getName(),
                                                                                                                   duplicateName.toString(),
                                                                                                                   entityDef.toString()),
                                                  this.getClass().getName(),
                                                  methodName);
            }
            
            if (entityDef.getPropertiesDefinition() != null)
            {
                Set<String> attrSet = new HashSet<>();

                for (TypeDefAttribute typeDefAttr : entityDef.getPropertiesDefinition())
                {
                    String duplicateAttributeName = typeDefAttr.getAttributeName();
                    if (attrSet.contains(duplicateAttributeName))
                    {
                        /*
                         * Relationship duplicate attribute name
                         */
                        throw new OMRSLogicErrorException(OMRSErrorCode.DUPLICATE_ENTITY_ATTR_IN_ARCHIVE.getMessageDefinition(duplicateAttributeName,
                                                                                                                              entityDef.getName()),
                                                          this.getClass().getName(),
                                                          methodName);
                    }
                    attrSet.add(duplicateAttributeName);
                }
            }
            entityDefList.add(entityDef);
        }
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
        final String methodName = "getEntityDef";

        log.debug("Retrieving EntityDef: " + entityDefName);

        if (entityDefName != null)
        {
            EntityDef retrievedEntityDef = entityDefMap.get(entityDefName);

            if (retrievedEntityDef == null)
            {
                throw new OMRSLogicErrorException(OMRSErrorCode.MISSING_TYPE_IN_ARCHIVE.getMessageDefinition(entityDefName,
                                                                                                             TypeDefCategory.ENTITY_DEF.getName()),
                                                  this.getClass().getName(),
                                                  methodName);
            }

            return retrievedEntityDef;
        }
        else
        {
            throw new OMRSLogicErrorException(OMRSErrorCode.MISSING_NAME_FOR_ARCHIVE.getMessageDefinition(TypeDefCategory.ENTITY_DEF.getName()),
                                              this.getClass().getName(),
                                              methodName);
        }
    }


    /**
     * Retrieve the relationshipDef or null if it is not defined.
     *
     * @param relationshipDefName name of the relationship
     * @return the retrieved relationship def
     */
    @Override
    public RelationshipDef  getRelationshipDef(String   relationshipDefName)
    {
        final String methodName = "getRelationshipDef";

        log.debug("Retrieving RelationshipDef: " + relationshipDefName);

        if (relationshipDefName != null)
        {
            RelationshipDef retrievedRelationshipDef = relationshipDefMap.get(relationshipDefName);

            if (retrievedRelationshipDef == null)
            {
                throw new OMRSLogicErrorException(OMRSErrorCode.MISSING_TYPE_IN_ARCHIVE.getMessageDefinition(relationshipDefName,
                                                                                                             TypeDefCategory.RELATIONSHIP_DEF.getName()),
                                                  this.getClass().getName(),
                                                  methodName);
            }

            return retrievedRelationshipDef;
        }
        else
        {
            throw new OMRSLogicErrorException(OMRSErrorCode.MISSING_NAME_FOR_ARCHIVE.getMessageDefinition(TypeDefCategory.RELATIONSHIP_DEF.getName()),
                                              this.getClass().getName(),
                                              methodName);
        }
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
        final String methodName = "getClassificationDef";

        log.debug("Retrieving getClassificationDef: " + classificationDef);

        if (classificationDef != null)
        {
            ClassificationDef retrievedClassificationDef = classificationDefMap.get(classificationDef);

            if (retrievedClassificationDef == null)
            {
                throw new OMRSLogicErrorException(OMRSErrorCode.MISSING_TYPE_IN_ARCHIVE.getMessageDefinition(classificationDef,
                                                                                                             TypeDefCategory.CLASSIFICATION_DEF.getName()),
                                                  this.getClass().getName(),
                                                  methodName);
            }

            return retrievedClassificationDef;
        }
        else
        {
            throw new OMRSLogicErrorException(OMRSErrorCode.MISSING_NAME_FOR_ARCHIVE.getMessageDefinition(TypeDefCategory.CLASSIFICATION_DEF.getName()),
                                              this.getClass().getName(),
                                              methodName);
        }
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

        if (relationshipDef != null)
        {
            log.debug("Adding RelationshipDef: " + relationshipDef);
            
            this.checkForBlanksInTypeName(relationshipDef.getName());
            RelationshipDef duplicateElement = relationshipDefMap.put(relationshipDef.getName(), relationshipDef);

            if (duplicateElement != null)
            {
                throw new OMRSLogicErrorException(OMRSErrorCode.DUPLICATE_TYPE_IN_ARCHIVE.getMessageDefinition(relationshipDef.getName(),
                                                                                                               TypeDefCategory.RELATIONSHIP_DEF.getName(),
                                                                                                               duplicateElement.toString(),
                                                                                                               relationshipDef.toString()),
                                                  this.getClass().getName(),
                                                  methodName);
            }

            Object  duplicateGUID = guidMap.put(relationshipDef.getGUID(), relationshipDef);

            if (duplicateGUID != null)
            {
                throw new OMRSLogicErrorException(OMRSErrorCode.DUPLICATE_GUID_IN_ARCHIVE.getMessageDefinition(relationshipDef.getGUID(),
                                                                                                               duplicateGUID.toString(),
                                                                                                               relationshipDef.toString()),
                                                  this.getClass().getName(),
                                                  methodName);
            }

            Object  duplicateName = nameMap.put(relationshipDef.getName(), relationshipDef);

            if (duplicateName != null)
            {
                throw new OMRSLogicErrorException(OMRSErrorCode.DUPLICATE_TYPENAME_IN_ARCHIVE.getMessageDefinition(relationshipDef.getName(),
                                                                                                                   duplicateName.toString(),
                                                                                                                   relationshipDef.toString()),
                                                  this.getClass().getName(),
                                                  methodName);
            }
            
            this.checkRelationshipDefDuplicateAttributes(relationshipDef);

            if (relationshipDef.getPropertiesDefinition() != null)
            {
                Set<String> attributeSet = new HashSet<>();
                for (TypeDefAttribute typeDefAttr : relationshipDef.getPropertiesDefinition())
                {
                    String duplicateAttributeName = typeDefAttr.getAttributeName();
                    if (attributeSet.contains(duplicateAttributeName))
                    {
                        /*
                         * relationship duplicate attribute name
                         */
                        throw new OMRSLogicErrorException(OMRSErrorCode.DUPLICATE_RELATIONSHIP_ATTR_IN_ARCHIVE.getMessageDefinition(duplicateAttributeName,
                                                                                                                                    relationshipDef.getName()),
                                                          this.getClass().getName(),
                                                          methodName);
                    }
                    attributeSet.add(duplicateAttributeName);
                }
            }
            relationshipDefList.add(relationshipDef);
        }
    }


    /**
     * Check whether the relationshipDef supplies any attributes that already exist.
     *
     * @param relationshipDef definition to test
     * @throws OMRSLogicErrorException duplicate entry
     */
    private void checkRelationshipDefDuplicateAttributes(RelationshipDef relationshipDef) throws OMRSLogicErrorException
    {
        final String methodName = "checkRelationshipDefDuplicateAttributes";

        /*
         * Check whether the end2 type already has an attribute called end1name already exists locally or in any of its relationships
         */

        RelationshipEndDef end1 = relationshipDef.getEndDef1();
        RelationshipEndDef end2 = relationshipDef.getEndDef2();

        String end1Name = end1.getAttributeName();
        String end1Type = end1.getEntityType().getName();

        String end2Name = end2.getAttributeName();
        String end2Type = end2.getEntityType().getName();

        if ((end1Name.equals(end2Name)) && (end1Type.equals(end2Type)))
        {
            if (entityAttributeMap.get(end1Type) == null)
            {
                /*
                 * we have not seen this entity before
                 */
                Set<String> attributeSet = new HashSet<>();
                attributeSet.add(end1Name);
                entityAttributeMap.put(end1Type, attributeSet);
            }
            else
            {
                Set<String> attributeSet = entityAttributeMap.get(end1Type);

                /*
                 * this attribute name should not already be defined for this entity
                 */

                if (attributeSet.contains(end2Name))
                {
                   throw new OMRSLogicErrorException(OMRSErrorCode.DUPLICATE_ENDDEF2_NAME_IN_ARCHIVE.getMessageDefinition(end1Type,
                                                                                                                          end2Name,
                                                                                                                          relationshipDef.getName()),
                                                      this.getClass().getName(),
                                                      methodName);
                }
                attributeSet.add(end1Name);
            }
        }
        else
        {
            if (entityAttributeMap.get(end1Type) == null)
            {
                /*
                 * We have not seen this entity before
                 */
                Set<String> attributeSet = new HashSet<>();
                attributeSet.add(end2Name);
                entityAttributeMap.put(end1Type, attributeSet);
            } 
            else
            {
                Set<String> attributeSet = entityAttributeMap.get(end1Type);
                
                /*
                 * This attribute name should not already be defined for this entity
                 */
                if (attributeSet.contains(end2Name))
                {
                    throw new OMRSLogicErrorException(OMRSErrorCode.DUPLICATE_ENDDEF2_NAME_IN_ARCHIVE.getMessageDefinition(end1Type,
                                                                                                                           end2Name,
                                                                                                                           relationshipDef.getName()),
                                                      this.getClass().getName(),
                                                      methodName);
                }
                attributeSet.add(end2Name);
            }

            if (entityAttributeMap.get(end2Type) == null)
            {
                /*
                 * We have not seen this entity before
                 */
                Set<String> attrSet = new HashSet<>();
                attrSet.add(end1Name);
                entityAttributeMap.put(end2Type, attrSet);
            } 
            else 
            {
                Set<String> attributeSet = entityAttributeMap.get(end2Type);
                /* 
                 * This attribute name should not already be defined for this entity
                 */
                if (attributeSet.contains(end1Name))
                {
                    throw new OMRSLogicErrorException(OMRSErrorCode.DUPLICATE_ENDDEF1_NAME_IN_ARCHIVE.getMessageDefinition(end2Type,
                                                                                                                           end1Name,
                                                                                                                           relationshipDef.getName()),
                                                      this.getClass().getName(),
                                                      methodName);
                }
                attributeSet.add(end1Name);
            }
        }

        /*
         * check whether end1 has a local attribute name that clashes with a relationship end
         */
        EntityDef entityDef1 = entityDefMap.get(end1Type);

        if (entityDef1.getPropertiesDefinition() != null)
        {
            Set<String> attributeSet = entityAttributeMap.get(end1Type);

            for (TypeDefAttribute typeDefAttr : entityDef1.getPropertiesDefinition())
            {
                String localAttributeName = typeDefAttr.getAttributeName();

                /*
                 * this attribute name should not already be defined for this entity
                 */

                if (localAttributeName.equals(end2Name))
                {
                    throw new OMRSLogicErrorException(OMRSErrorCode.DUPLICATE_ENDDEF2_NAME_IN_ARCHIVE.getMessageDefinition(end1Type,
                                                                                                                           end2Name,
                                                                                                                           relationshipDef.getName()),
                                                      this.getClass().getName(),
                                                      methodName);
                }
                attributeSet.add(end2Name);
            }
        }
        EntityDef entityDef2 = entityDefMap.get(end2Type);
        if (entityDef2.getPropertiesDefinition() != null)
        {
            Set<String> attributeSet = entityAttributeMap.get(end2Type);
            for (TypeDefAttribute typeDefAttr : entityDef2.getPropertiesDefinition())
            {
                String localAttributeName = typeDefAttr.getAttributeName();

                /*
                 * This attribute name should not already be defined for this entity
                 */

                if (localAttributeName.equals(end1Name))
                {
                    throw new OMRSLogicErrorException(OMRSErrorCode.DUPLICATE_ENDDEF1_NAME_IN_ARCHIVE.getMessageDefinition(end2Type,
                                                                                                                           end1Name,
                                                                                                                           relationshipDef.getName()),
                                                      this.getClass().getName(),
                                                      methodName);
                }
                attributeSet.add(end1Name);
            }
        }
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
        TypeDef  typeDef = this.getTypeDefByName(typeName);

        if (typeDef != null)
        {
            long   latestVersion     = typeDef.getVersion();
            String latestVersionName = typeDef.getVersionName();

            if (!typeDefPatchList.isEmpty())
            {
                for (TypeDefPatch typeDefPatch : typeDefPatchList)
                {
                    if (typeDefPatch != null)
                    {
                        if (typeName.equals(typeDefPatch.getTypeDefName()))
                        {
                            if (typeDefPatch.getUpdateToVersion() > latestVersion)
                            {
                                latestVersion     = typeDefPatch.getUpdateToVersion();
                                latestVersionName = typeDefPatch.getNewVersionName();
                            }
                        }
                    }
                }
            }

            TypeDefPatch patch = new TypeDefPatch();

            patch.setHeaderVersion(TypeDefElementHeader.CURRENT_TYPE_DEF_HEADER_VERSION);
            patch.setTypeDefGUID(typeDef.getGUID());
            patch.setTypeDefName(typeDef.getName());
            patch.setApplyToVersion(latestVersion);
            patch.setUpdateToVersion(latestVersion + 1);
            patch.setNewVersionName(latestVersionName);

            return patch;
        }

        return null;
    }


    /**
     * Add a new patch to the archive.
     *
     * @param typeDefPatch patch
     */
    @Override
    public void addTypeDefPatch(TypeDefPatch  typeDefPatch)
    {
        if (typeDefPatch != null)
        {
            log.debug("Adding TypeDefPatch: " + typeDefPatch);

            this.checkForBlanksInTypeName(typeDefPatch.getTypeDefName());

            typeDefPatchList.add(typeDefPatch);
        }
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
        String methodName = "getTypeDefByName";

        Object  namedObject = nameMap.get(typeName);

        if (namedObject instanceof TypeDef)
        {
            return (TypeDef)namedObject;
        }
        else
        {
            throw new OMRSLogicErrorException(OMRSErrorCode.UNKNOWN_TYPENAME.getMessageDefinition(typeName),
                                              this.getClass().getName(),
                                              methodName);
        }
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

        if (entity != null)
        {
            log.debug("Adding Entity: " + entity);

            EntityDetail   duplicateElement = entityDetailMap.put(entity.getGUID(), entity);

            if (duplicateElement != null)
            {
                throw new OMRSLogicErrorException(OMRSErrorCode.DUPLICATE_INSTANCE_IN_ARCHIVE.getMessageDefinition(duplicateElement.getClass().getSimpleName(),
                                                                                                                   entity.getGUID(),
                                                                                                                   duplicateElement.toString(),
                                                                                                                   entity.toString()),
                                                  this.getClass().getName(),
                                                  methodName);
            }

            Object  duplicateGUID = guidMap.put(entity.getGUID(), entity);

            if (duplicateGUID != null)
            {
                throw new OMRSLogicErrorException(OMRSErrorCode.DUPLICATE_GUID_IN_ARCHIVE.getMessageDefinition(entity.getGUID(),
                                                                                                               duplicateGUID.toString(),
                                                                                                               entity.toString()),
                                                  this.getClass().getName(),
                                                  methodName);
            }

            entityDetailList.add(entity);
        }
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

        EntityDetail   entity = entityDetailMap.get(guid);

        if (entity == null)
        {
            throw new OMRSLogicErrorException(OMRSErrorCode.UNKNOWN_GUID.getMessageDefinition(methodName, guid),
                                              this.getClass().getName(),
                                              methodName);
        }

        return entity;
    }


    /**
     * Retrieve an entity from the archive.  Return null if it does not exist
     *
     * @param guid unique identifier
     * @return requested entity
     */
    @Override
    public EntityDetail queryEntity(String   guid)
    {
        return entityDetailMap.get(guid);
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

        if (relationship != null)
        {
            log.debug("Adding Relationship: " + relationship);

            Relationship   duplicateElement = relationshipMap.put(relationship.getGUID(), relationship);

            if ((duplicateElement != null) && (! relationship.equals(duplicateElement)))
            {
                throw new OMRSLogicErrorException(OMRSErrorCode.DUPLICATE_INSTANCE_IN_ARCHIVE.getMessageDefinition(duplicateElement.getClass().getSimpleName(),
                                                                                                                   relationship.getGUID(),
                                                                                                                   duplicateElement.toString(),
                                                                                                                   relationship.toString()),
                                                  this.getClass().getName(),
                                                  methodName);
            }

            Object  duplicateGUID = guidMap.put(relationship.getGUID(), relationship);

            if ((duplicateGUID != null) && (! relationship.equals(duplicateGUID)))
            {
               throw new OMRSLogicErrorException(OMRSErrorCode.DUPLICATE_GUID_IN_ARCHIVE.getMessageDefinition(relationship.getGUID(),
                                                                                                               duplicateGUID.toString(),
                                                                                                               relationship.toString()),
                                                  this.getClass().getName(),
                                                  methodName);
            }

            relationshipList.add(relationship);
        }
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

        Relationship   relationship = relationshipMap.get(guid);

        if (relationship == null)
        {
            throw new OMRSLogicErrorException(OMRSErrorCode.UNKNOWN_GUID.getMessageDefinition(methodName, guid),
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
        return relationshipMap.get(guid);
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

        if (classification != null)
        {
            log.debug("Adding Classification: " + classification);

            String classificationId = classification.getEntityToClassify().getGUID() + "::" + classification.getClassification().getName();

            ClassificationEntityExtension   duplicateElement = classificationMap.put(classificationId, classification);

            if (duplicateElement != null)
            {
               throw new OMRSLogicErrorException(OMRSErrorCode.DUPLICATE_INSTANCE_IN_ARCHIVE.getMessageDefinition(duplicateElement.getClass().getSimpleName(),
                                                                                                                  classificationId,
                                                                                                                  duplicateElement.toString(),
                                                                                                                  classification.toString()),
                                                  this.getClass().getName(),
                                                  methodName);
            }

            classificationList.add(classification);
        }
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

        String identifier = entityGUID + "::" + classificationName;

        ClassificationEntityExtension   classification = classificationMap.get(identifier);

        if (classification == null)
        {
            throw new OMRSLogicErrorException(OMRSErrorCode.UNKNOWN_GUID.getMessageDefinition(methodName, identifier),
                                              this.getClass().getName(),
                                              methodName);
        }

        return classification;
    }



    /**
     * Retrieve a classification extension from the archive if it exists
     *
     * @param entityGUID unique identifier of entity
     * @param classificationName name of the classification
     * @return requested classification extension
     */
    @Override
    public ClassificationEntityExtension queryClassification(String entityGUID,
                                                             String classificationName)
    {
        String identifier = entityGUID + "::" + classificationName;

        return classificationMap.get(identifier);
    }


    /**
     * Once the content of the archive has been added to the archive builder, an archive object can be retrieved.
     *
     * @return open metadata archive object with all the supplied content in it.
     */
    public OpenMetadataArchive getOpenMetadataArchive()
    {
        log.debug("Retrieving Open Metadata Archive: " + archiveProperties.getArchiveName());

        OpenMetadataArchive    archive = new OpenMetadataArchive();

        /*
         * Set up the archive properties
         */
        archive.setArchiveProperties(this.archiveProperties);

        /*
         * Set up the TypeStore.  The types are added in a strict order to ensure that the dependencies are resolved.
         */
        List<AttributeTypeDef>  attributeTypeDefs = new ArrayList<>();
        List<TypeDef>           typeDefs = new ArrayList<>();
        List<TypeDefPatch>      typeDefPatches = new ArrayList<>();

        if (! primitiveDefList.isEmpty())
        {
            attributeTypeDefs.addAll(primitiveDefList);
        }
        if (! collectionDefList.isEmpty())
        {
            attributeTypeDefs.addAll(collectionDefList);
        }
        if (! enumDefList.isEmpty())
        {
            attributeTypeDefs.addAll(enumDefList);
        }
        if (! entityDefList.isEmpty())
        {
            typeDefs.addAll(entityDefList);
        }
        if (! classificationDefList.isEmpty())
        {
            typeDefs.addAll(classificationDefList);
        }
        if (! relationshipDefList.isEmpty())
        {
            typeDefs.addAll(relationshipDefList);
        }

        if (! typeDefPatchList.isEmpty())
        {
            typeDefPatches.addAll(typeDefPatchList);
        }

        if ((! typeDefs.isEmpty()) || (! typeDefPatches.isEmpty()) || (! attributeTypeDefs.isEmpty()))
        {
            OpenMetadataArchiveTypeStore typeStore = new OpenMetadataArchiveTypeStore();

            if (! attributeTypeDefs.isEmpty())
            {
                typeStore.setAttributeTypeDefs(attributeTypeDefs);
            }

            if (! typeDefs.isEmpty())
            {
                typeStore.setNewTypeDefs(typeDefs);
            }

            if (! typeDefPatches.isEmpty())
            {
                typeStore.setTypeDefPatches(typeDefPatches);
            }

            archive.setArchiveTypeStore(typeStore);
        }


        /*
         * Finally, set up the instance store
         */
        List<EntityDetail>                  entities        = new ArrayList<>();
        List<Relationship>                  relationships   = new ArrayList<>();
        List<ClassificationEntityExtension> classifications = new ArrayList<>();

        if (! entityDetailList.isEmpty())
        {
            entities.addAll(entityDetailList);
        }
        if (! relationshipList.isEmpty())
        {
            relationships.addAll(relationshipList);
        }
        if (! classificationList.isEmpty())
        {
            classifications.addAll(classificationList);
        }

        if ((! entities.isEmpty()) || (! relationships.isEmpty()))
        {
            OpenMetadataArchiveInstanceStore instanceStore = new OpenMetadataArchiveInstanceStore();

            if (! entities.isEmpty())
            {
                instanceStore.setEntities(entities);
            }

            if (! relationships.isEmpty())
            {
                instanceStore.setRelationships(relationships);
            }

            if (! classifications.isEmpty())
            {
                instanceStore.setClassifications(classifications);
            }

            archive.setArchiveInstanceStore(instanceStore);
        }

        return archive;
    }
    
    
    /**
     * Ensure that a type definition name does not have blanks in it.
     * 
     * @param typeName name to test
     */
    private void checkForBlanksInTypeName(String typeName)
    {
        final String methodName = "checkForBlanksInTypeName";
        
        if (typeName.contains(" "))
        {
            throw new OMRSLogicErrorException(OMRSErrorCode.BLANK_TYPENAME_IN_ARCHIVE.getMessageDefinition(typeName),
                                              this.getClass().getName(),
                                              methodName);
        }
    }


    /**
     * Throws an exception if a problem retrieving the archive.
     *
     * @param methodName calling method
     */
    public void logBadArchiveContent(String   methodName)
    {
        /*
         * This is a logic error since it means the creation of the archive builder threw an exception
         * in the constructor and so this object should not be used.
         */
        throw new OMRSLogicErrorException(OMRSErrorCode.ARCHIVE_UNAVAILABLE.getMessageDefinition(),
                                          this.getClass().getName(),
                                          methodName);
    }
}
