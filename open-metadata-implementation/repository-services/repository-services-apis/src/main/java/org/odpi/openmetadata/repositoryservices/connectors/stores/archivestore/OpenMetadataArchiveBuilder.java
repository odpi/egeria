/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore;

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

import java.util.Date;
import java.util.List;

/**
 * OpenMetadataArchiveBuilder defines the interface for an open metadata archive store connector that is able
 * to manage individual elements of the open metadata archive.  It is an optional interface for an open metadata
 * archive store connector.  For connectors where this interface is not implemented, the OMRS provides
 * the OMRSArchiveBuilder utility that manages the content of an archive in memory, so it can be passed
 * to the open metadata archive store connector as a single structure
 */
public interface OpenMetadataArchiveBuilder
{
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
    void setArchiveProperties(String                     archiveGUID,
                              String                     archiveName,
                              String                     archiveDescription,
                              OpenMetadataArchiveType    archiveType,
                              String                     archiveVersion,
                              String                     originatorName,
                              String                     originatorLicense,
                              Date                       creationDate,
                              List<OpenMetadataArchive>  dependsOnArchives);


    /**
     * Return the archive properties as will appear in the archive.  Null is returned if archive properties not set up.
     *
     * @return property bean
     */
    OpenMetadataArchiveProperties getArchiveProperties();

    /**
     * Add a new PrimitiveDef to the archive.
     *
     * @param primitiveDef type to add nulls are ignored
     */
    void addPrimitiveDef(PrimitiveDef   primitiveDef);


    /**
     * Retrieve a PrimitiveDef from the archive.
     *
     * @param primitiveDefName primitive to retrieve
     * @return PrimitiveDef type
     */
    PrimitiveDef getPrimitiveDef(String   primitiveDefName);


    /**
     * Add a new CollectionDef to the archive.
     *
     * @param collectionDef type to add
     */
    void addCollectionDef(CollectionDef  collectionDef);


    /**
     * Retrieve a CollectionDef from the archive.
     *
     * @param collectionDefName type to retrieve
     * @return CollectionDef type
     */
    CollectionDef getCollectionDef(String  collectionDefName);


    /**
     * Add a new EnumDef to the archive.
     *
     * @param enumDef type to add
     */
    void addEnumDef(EnumDef    enumDef);


    /**
     * Get an existing EnumDef from the archive.
     *
     * @param enumDefName type to retrieve
     * @return EnumDef object
     */
    EnumDef getEnumDef(String    enumDefName);


    /**
     * Add a new ClassificationDef to the archive.
     *
     * @param classificationDef type to add
     */
    void addClassificationDef(ClassificationDef   classificationDef);


    /**
     * Retrieve the relationshipDef or null if it is not defined.
     *
     * @param classificationDef name of the classification
     * @return the retrieved classification def
     */
    ClassificationDef  getClassificationDef(String   classificationDef);


    /**
     * Add a new EntityDef to the archive.
     *
     * @param entityDef type to add
     */
    void addEntityDef(EntityDef    entityDef);


    /**
     * Retrieve the entityDef or null if it is not defined.
     *
     * @param entityDefName name of the entity
     * @return the retrieved entity def
     */
     EntityDef  getEntityDef(String   entityDefName);


    /**
     * Retrieve the relationshipDef or null if it is not defined.
     *
     * @param relationshipDefName name of the relationship
     * @return the retrieved relationship def
     */
    RelationshipDef  getRelationshipDef(String   relationshipDefName);


    /**
     * Add a new RelationshipDef to the archive.
     *
     * @param relationshipDef type to add
     */
    void addRelationshipDef(RelationshipDef   relationshipDef);


    /**
     * Create a skeleton patch for a TypeDefPatch.
     *
     * @param typeName name of type
     * @return TypeDefPatch
     */
    TypeDefPatch  getPatchForType(String  typeName);


    /**
     * Add a new patch to the archive.
     *
     * @param typeDefPatch patch
     */
    void addTypeDefPatch(TypeDefPatch  typeDefPatch);


    /**
     * Return the requested type definition if known.
     *
     * @param typeName name ot type
     * @return type definition
     */
    TypeDef getTypeDefByName(String  typeName);


    /**
     * Add a new entity to the archive.
     *
     * @param entity instance to add
     */
    void addEntity(EntityDetail   entity);


    /**
     * Retrieve an entity from the archive.   Throw an exception if not present.
     *
     * @param guid unique identifier
     * @return requested entity
     */
    EntityDetail getEntity(String   guid);


    /**
     * Retrieve an entity from the archive if available.
     *
     * @param guid unique identifier
     * @return requested entity
     */
    EntityDetail queryEntity(String   guid);


    /**
     * Add a new relationship to the archive.
     *
     * @param relationship instance to add
     */
    void addRelationship(Relationship  relationship);


    /**
     * Retrieve a relationship from the archive.  Throw an exception if not present.
     *
     * @param guid unique identifier
     * @return requested relationship
     */
     Relationship getRelationship(String   guid);


    /**
     * Retrieve a relationship from the archive if it exists.
     *
     * @param guid unique identifier
     * @return requested relationship
     */
    Relationship queryRelationship(String   guid);


    /**
     * Add a new classification to the archive.
     *
     * @param classification instance to add
     */
    void addClassification(ClassificationEntityExtension classification);


    /**
     * Retrieve a classification extension from the archive.   Throw an exception if not present.
     *
     * @param entityGUID unique identifier of entity
     * @param classificationName name of the classification
     * @return requested classification extension
     */
    ClassificationEntityExtension getClassification(String entityGUID,
                                                    String classificationName);


    /**
     * Retrieve a classification extension from the archive if it exists.
     *
     * @param entityGUID unique identifier of entity
     * @param classificationName name of the classification
     * @return requested classification extension
     */
    ClassificationEntityExtension queryClassification(String entityGUID,
                                                      String classificationName);
}
