/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector;


import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.utilities.OMRSRepositoryPropertiesHelper;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.List;
import java.util.Set;

/**
 * OMRSRepositoryHelper provides methods to repository connectors and repository event mappers to help
 * them build valid type definitions (TypeDefs), entities and relationships.  It is a facade to the
 * repository content manager which holds an in memory cache of all the active TypeDefs in the local server.
 * OMRSRepositoryHelper's purpose is to create an object that the repository connectors and event mappers can
 * create, use and discard without needing to know how to connect to the repository content manager.
 */
public interface OMRSRepositoryHelper extends OMRSRepositoryPropertiesHelper
{
    /**
     * Return the list of typeDefs and attributeTypeDefs active in the local repository.
     *
     * @return TypeDef gallery
     */
    TypeDefGallery getActiveTypeDefGallery();


    /**
     * Return the list of typeDefs active in the local repository.
     *
     * @return TypeDef list
     */
    List<TypeDef>  getActiveTypeDefs();


    /**
     * Return the list of attributeTypeDefs active in the local repository.
     *
     * @return AttributeTypeDef list
     */
    List<AttributeTypeDef>  getActiveAttributeTypeDefs();


    /**
     * Return the list of types active in the connected cohorts.
     *
     * @return TypeDef gallery
     */
    TypeDefGallery getKnownTypeDefGallery();


    /**
     * Return the list of typeDefs active in the connected cohorts.
     *
     * @return TypeDef list
     */
    List<TypeDef>  getKnownTypeDefs();


    /**
     * Return the list of attributeTypeDefs active in the connected cohorts.
     *
     * @return AttributeTypeDef list
     */
    List<AttributeTypeDef>  getKnownAttributeTypeDefs();


    /**
     * Return the TypeDef identified by the name supplied by the caller.  This is used in the connectors when
     * validating the actual types of the repository with the known open metadata types looking specifically
     * for types of the same name but with different content.
     *
     * @param sourceName  source of the request (used for logging)
     * @param typeDefName unique name for the TypeDef
     * @return TypeDef object or null if TypeDef is not known.
     */
    TypeDef getTypeDefByName(String sourceName,
                             String typeDefName);

    /**
     * Gets super types for given type name.
     *
     * @param sourceName  the source of the request (used for logging)
     * @param typeDefName unique name for the TypeDef
     * @return the super types
     */
    List<TypeDefLink> getSuperTypes(String sourceName,
                                    String typeDefName);


    /**
     * Determine the list of attribute names that contain a unique value.  An empty list is returned if none have.
     *
     * @param sourceName caller
     * @param typeName name of instance's type
     * @return list of attribute names
     */
    List<String> getUniqueAttributesList(String sourceName,
                                         String typeName);


    /**
     * Return an instance properties that only contains the properties that uniquely identify the entity.
     * This is used when creating entity proxies.
     *
     * @param sourceName caller
     * @param typeName name of instance's type
     * @param allProperties all the instance's properties
     * @return just the unique properties
     */
    InstanceProperties getUniqueProperties(String              sourceName,
                                           String              typeName,
                                           InstanceProperties  allProperties);


    /**
     * Return the attribute name for the related entity.
     *
     * @param sourceName  source of the request (used for logging)
     * @param anchorEntityGUID unique identifier of the anchor entity
     * @param relationship relationship to another entity
     * @return proxy to the other entity.
     */
    String  getOtherEndName(String                 sourceName,
                            String                 anchorEntityGUID,
                            Relationship           relationship);


    /**
     * Return the entity proxy for the related entity.
     *
     * @param sourceName  source of the request (used for logging)
     * @param anchorEntityGUID unique identifier of the anchor entity
     * @param relationship relationship to another entity
     * @return proxy to the other entity.
     */
    EntityProxy  getOtherEnd(String                 sourceName,
                             String                 anchorEntityGUID,
                             Relationship           relationship);


    /**
     * Return the AttributeTypeDef identified by the name supplied by the caller.  This is used in the connectors when
     * validating the actual types of the repository with the known open metadata types looking specifically
     * for types of the same name but with different content.
     *
     * @param sourceName            source of the request (used for logging)
     * @param attributeTypeDefName  unique name for the TypeDef
     * @return AttributeTypeDef object or null if AttributeTypeDef is not known.
     */
    AttributeTypeDef getAttributeTypeDefByName(String sourceName,
                                               String attributeTypeDefName);



    /**
     * Return the TypeDef identified by the guid supplied by the caller.  This call is used when
     * retrieving a type that only the guid is known.
     *
     * @param sourceName     source of the request (used for logging)
     * @param parameterName  name of guid parameter
     * @param typeDefGUID    unique identifier for the TypeDef
     * @param methodName     calling method
     * @return TypeDef object
     * @throws TypeErrorException  unknown or invalid type
     */
    TypeDef getTypeDef(String sourceName,
                       String parameterName,
                       String typeDefGUID,
                       String methodName) throws TypeErrorException;


    /**
     * Return the AttributeTypeDef identified by the guid and name supplied by the caller.  This call is used when
     * retrieving a type that only the guid is known.
     *
     * @param sourceName            source of the request (used for logging)
     * @param attributeTypeDefGUID  unique identifier for the AttributeTypeDef
     * @param methodName     calling method
     * @return TypeDef object
     * @throws TypeErrorException  unknown or invalid type
     */
    AttributeTypeDef getAttributeTypeDef(String sourceName,
                                         String attributeTypeDefGUID,
                                         String methodName) throws TypeErrorException;


    /**
     * Return the TypeDef identified by the guid and name supplied by the caller.  This call is used when
     * retrieving a type that should exist.  For example, retrieving the type of a metadata instance.
     *
     * @param sourceName   source of the request (used for logging)
     * @param guidParameterName  name of guid parameter
     * @param nameParameterName  name of type name parameter
     * @param typeDefGUID  unique identifier for the TypeDef
     * @param typeDefName  unique name for the TypeDef
     * @param methodName   calling method
     * @return TypeDef object
     * @throws TypeErrorException  unknown or invalid type
     */
    TypeDef getTypeDef(String sourceName,
                       String guidParameterName,
                       String nameParameterName,
                       String typeDefGUID,
                       String typeDefName,
                       String methodName) throws TypeErrorException;


    /**
     * Return the AttributeTypeDef identified by the guid and name supplied by the caller.  This call is used when
     * retrieving a type that should exist.  For example, retrieving the type definition of a metadata instance's
     * property.
     *
     * @param sourceName            source of the request (used for logging)
     * @param attributeTypeDefGUID  unique identifier for the AttributeTypeDef
     * @param attributeTypeDefName  unique name for the AttributeTypeDef
     * @param methodName  calling method
     * @return TypeDef object
     * @throws TypeErrorException  unknown or invalid type
     */
    AttributeTypeDef getAttributeTypeDef(String sourceName,
                                         String attributeTypeDefGUID,
                                         String attributeTypeDefName,
                                         String methodName) throws TypeErrorException;


    /**
     * Add the supplied property to an instance properties object.  If the instance property object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName name of caller
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param enumTypeGUID unique Id of Enum requested
     * @param enumTypeName unique name of enum requested
     * @param ordinal numeric value of property
     * @param methodName calling method name
     * @return instance properties object.
     * @throws TypeErrorException the enum type is not recognized
     */
    InstanceProperties addEnumPropertyToInstance(String             sourceName,
                                                 InstanceProperties properties,
                                                 String             propertyName,
                                                 String             enumTypeGUID,
                                                 String             enumTypeName,
                                                 int                ordinal,
                                                 String             methodName) throws TypeErrorException;


    /**
     * Validate that the type of an instance is of the expected/desired type.  The actual instance may be a subtype
     * of the expected type of course.
     *
     * @param sourceName source of the request (used for logging)
     * @param actualTypeName name of the entity type
     * @param expectedTypeName name of the expected type
     * @return boolean if they match (a null in either results in false)
     */
    boolean  isTypeOf(String sourceName,
                      String actualTypeName,
                      String expectedTypeName);


    /**
     * Return the list of type names for all the subtypes of an entity type.
     *
     * @param sourceName source of the request (used for logging)
     * @param superTypeName name of the super type - this value is not included in the result.
     * @return list of type names (a null means the type is not know or it has no sub types)
     */
    List<String>  getSubTypesOf(String sourceName,
                                String superTypeName);


    /**
     * Return the names of all the properties in the supplied TypeDef and all of its super-types.
     *
     * @param sourceName name of caller.
     * @param typeDef TypeDef to query.
     * @param methodName calling method.
     * @return list of property names.
     */
    List<TypeDefAttribute> getAllPropertiesForTypeDef(String  sourceName,
                                                      TypeDef typeDef,
                                                      String  methodName);


    /**
     * Return the names of all the type definitions that define the supplied property name.
     *
     * @param sourceName name of the caller.
     * @param propertyName property name to query.
     * @param methodName calling method.
     * @return set of names of the TypeDefs that define a property with this name
     */
    Set<String> getAllTypeDefsForProperty(String sourceName,
                                          String propertyName,
                                          String methodName);


    /**
     * Returns an updated TypeDef that has had the supplied patch applied.  It throws an exception if any part of
     * the patch is incompatible with the original TypeDef.  For example, if there is a mismatch between
     * the type or version that either represents.
     *
     * @param sourceName       source of the TypeDef (used for logging)
     * @param originalTypeDef typeDef to update
     * @param typeDefPatch     patch to apply
     * @return updated TypeDef
     * @throws InvalidParameterException the original typeDef or typeDefPatch is null
     * @throws PatchErrorException        the patch is either badly formatted, or does not apply to the supplied TypeDef
     */
    TypeDef applyPatch(String       sourceName,
                       TypeDef      originalTypeDef,
                       TypeDefPatch typeDefPatch) throws InvalidParameterException, PatchErrorException;


    /**
     * Remember the metadata collection name for this metadata collection id. If the metadata collection id
     * is null, it is ignored.
     *
     * @param metadataCollectionId unique identifier (guid) for the metadata collection.
     * @param metadataCollectionName display name for the metadata collection (can be null).
     */
    void registerMetadataCollection(String metadataCollectionId,
                                    String metadataCollectionName);


    /**
     * Return the metadata collection name (or null) for a metadata collection id.
     *
     * @param metadataCollectionId unique identifier (guid) for the metadata collection.
     * @return display name
     */
    String getMetadataCollectionName(String metadataCollectionId);


    /**
     * Return an entity with the header and type information filled out.  The caller only needs to add properties
     * and classifications to complete the set up of the entity.
     *
     * @param sourceName            source of the request (used for logging)
     * @param metadataCollectionId  unique identifier for the home metadata collection
     * @param provenanceType        origin of the entity
     * @param userName              name of the creator
     * @param typeName              name of the type
     * @return partially filled out entity needs classifications and properties
     * @throws TypeErrorException  the type name is not recognized.
     */
    @Deprecated
    EntityDetail getSkeletonEntity(String                 sourceName,
                                   String                 metadataCollectionId,
                                   InstanceProvenanceType provenanceType,
                                   String                 userName,
                                   String                 typeName) throws TypeErrorException;


    /**
     * Return an entity with the header and type information filled out.  The caller only needs to add properties
     * and classifications to complete the set up of the entity.
     *
     * @param sourceName             source of the request (used for logging)
     * @param metadataCollectionId   unique identifier for the home metadata collection
     * @param metadataCollectionName unique name for the home metadata collection
     * @param provenanceType         origin of the entity
     * @param userName               name of the creator
     * @param typeName               name of the type
     * @return partially filled out entity needs classifications and properties
     * @throws TypeErrorException  the type name is not recognized.
     */
    EntityDetail getSkeletonEntity(String                 sourceName,
                                   String                 metadataCollectionId,
                                   String                 metadataCollectionName,
                                   InstanceProvenanceType provenanceType,
                                   String                 userName,
                                   String                 typeName) throws TypeErrorException;


    /**
     * Return an entity with the header and type information filled out.  The caller only needs to classifications
     * to complete the set up of the entity.
     *
     * @param sourceName            source of the request (used for logging)
     * @param metadataCollectionId  unique identifier for the home metadata collection
     * @param provenanceType        origin of the entity
     * @param userName              name of the creator
     * @param typeName              name of the type
     * @return partially filled out entity needs classifications
     * @throws TypeErrorException  the type name is not recognized.
     */
    @Deprecated
    EntitySummary getSkeletonEntitySummary(String                 sourceName,
                                           String                 metadataCollectionId,
                                           InstanceProvenanceType provenanceType,
                                           String                 userName,
                                           String                 typeName) throws TypeErrorException;


    /**
     * Return an entity with the header and type information filled out.  The caller only needs to classifications
     * to complete the set up of the entity.
     *
     * @param sourceName             source of the request (used for logging)
     * @param metadataCollectionId   unique identifier for the home metadata collection
     * @param metadataCollectionName unique name for the home metadata collection
     * @param provenanceType         origin of the entity
     * @param userName               name of the creator
     * @param typeName               name of the type
     * @return partially filled out entity needs classifications
     * @throws TypeErrorException  the type name is not recognized.
     */
    EntitySummary getSkeletonEntitySummary(String                 sourceName,
                                           String                 metadataCollectionId,
                                           String                 metadataCollectionName,
                                           InstanceProvenanceType provenanceType,
                                           String                 userName,
                                           String                 typeName) throws TypeErrorException;


    /**
     * Return a classification with the header and type information filled out.  The caller only needs to add properties
     * and possibility origin information if it is propagated to complete the set up of the classification.
     *
     * @param sourceName              source of the request (used for logging)
     * @param userName                name of the creator
     * @param classificationTypeName  name of the classification type
     * @param entityTypeName          name of the type for the entity that this classification is to be attached to.
     * @return partially filled out classification needs properties and possibly origin information
     * @throws TypeErrorException  the type name is not recognized as a classification type.
     */
    Classification getSkeletonClassification(String sourceName,
                                             String userName,
                                             String classificationTypeName,
                                             String entityTypeName) throws TypeErrorException;


    /**
     * Return a classification with the header and type information filled out.  The caller only needs to add properties
     * and possibility origin information if it is propagated to complete the set up of the classification.
     *
     * @param sourceName              source of the request (used for logging)
     * @param metadataCollectionId    unique identifier for the home metadata collection
     * @param provenanceType          type of home for the new classification
     * @param userName                name of the creator
     * @param classificationTypeName  name of the classification type
     * @param entityTypeName          name of the type for the entity that this classification is to be attached to.
     * @return partially filled out classification needs properties and possibly origin information
     * @throws TypeErrorException  the type name is not recognized as a classification type.
     */
    Classification getSkeletonClassification(String                 sourceName,
                                             String                 metadataCollectionId,
                                             InstanceProvenanceType provenanceType,
                                             String                 userName,
                                             String                 classificationTypeName,
                                             String                 entityTypeName) throws TypeErrorException;


    /**
     * Return a classification with the header and type information filled out.  The caller only needs to add properties
     * and possibility origin information if it is propagated to complete the set up of the classification.
     *
     * @param sourceName              source of the request (used for logging)
     * @param metadataCollectionId    unique identifier for the home metadata collection
     * @param metadataCollectionName  unique name for the home metadata collection
     * @param provenanceType          type of home for the new classification
     * @param userName                name of the creator
     * @param classificationTypeName  name of the classification type
     * @param entityTypeName          name of the type for the entity that this classification is to be attached to.
     * @return partially filled out classification needs properties and possibly origin information
     * @throws TypeErrorException  the type name is not recognized as a classification type.
     */
    Classification getSkeletonClassification(String                 sourceName,
                                             String                 metadataCollectionId,
                                             String                 metadataCollectionName,
                                             InstanceProvenanceType provenanceType,
                                             String                 userName,
                                             String                 classificationTypeName,
                                             String                 entityTypeName) throws TypeErrorException;


    /**
     * Return a relationship with the header and type information filled out.  The caller only needs to add properties
     * to complete the set up of the relationship.
     *
     * @param sourceName            source of the request (used for logging)
     * @param metadataCollectionId  unique identifier for the home metadata collection
     * @param provenanceType        origin type of the relationship
     * @param userName              name of the creator
     * @param typeName              name of the relationship's type
     * @return partially filled out relationship needs properties
     * @throws TypeErrorException  the type name is not recognized as a relationship type.
     */
    Relationship getSkeletonRelationship(String                 sourceName,
                                         String                 metadataCollectionId,
                                         InstanceProvenanceType provenanceType,
                                         String                 userName,
                                         String                 typeName) throws TypeErrorException;


    /**
     * Return a relationship with the header and type information filled out.  The caller only needs to add properties
     * to complete the set up of the relationship.
     *
     * @param sourceName             source of the request (used for logging)
     * @param metadataCollectionId   unique identifier for the home metadata collection
     * @param metadataCollectionName unique name for the home metadata collection
     * @param provenanceType         origin type of the relationship
     * @param userName               name of the creator
     * @param typeName               name of the relationship's type
     * @return partially filled out relationship needs properties
     * @throws TypeErrorException  the type name is not recognized as a relationship type.
     */
    Relationship getSkeletonRelationship(String                 sourceName,
                                         String                 metadataCollectionId,
                                         String                 metadataCollectionName,
                                         InstanceProvenanceType provenanceType,
                                         String                 userName,
                                         String                 typeName) throws TypeErrorException;


    /**
     * Return a relationship with the header and type information filled out.  The caller only needs to add properties
     * to complete the set up of the relationship.
     *
     * @param sourceName      source of the request (used for logging)
     * @param typeDefSummary  details of the new type
     * @return instance type
     * @throws TypeErrorException  the type name is not recognized as a relationship type.
     */
    InstanceType getNewInstanceType(String         sourceName,
                                    TypeDefSummary typeDefSummary) throws TypeErrorException;


    /**
     * Return a filled out entity.  It just needs to add the classifications.
     *
     * @param sourceName            source of the request (used for logging)
     * @param metadataCollectionId  unique identifier for the home metadata collection
     * @param provenanceType        origin of the entity
     * @param userName              name of the creator
     * @param typeName              name of the type
     * @param properties            properties for the entity
     * @param classifications       list of classifications for the entity
     * @return an entity that is filled out
     * @throws TypeErrorException  the type name is not recognized as an entity type
     */
    EntityDetail getNewEntity(String                 sourceName,
                              String                 metadataCollectionId,
                              InstanceProvenanceType provenanceType,
                              String                 userName,
                              String                 typeName,
                              InstanceProperties     properties,
                              List<Classification>   classifications) throws TypeErrorException;


    /**
     * Return a filled out entity.  It just needs to add the classifications.
     *
     * @param sourceName             source of the request (used for logging)
     * @param metadataCollectionName unique name for the home metadata collection
     * @param metadataCollectionId   unique identifier for the home metadata collection
     * @param provenanceType         origin of the entity
     * @param userName               name of the creator
     * @param typeName               name of the type
     * @param properties             properties for the entity
     * @param classifications        list of classifications for the entity
     * @return an entity that is filled out
     * @throws TypeErrorException  the type name is not recognized as an entity type
     */
    EntityDetail getNewEntity(String                 sourceName,
                              String                 metadataCollectionId,
                              String                 metadataCollectionName,
                              InstanceProvenanceType provenanceType,
                              String                 userName,
                              String                 typeName,
                              InstanceProperties     properties,
                              List<Classification>   classifications) throws TypeErrorException;


    /**
     * Return a filled out relationship which just needs the entity proxies added.
     *
     * @param sourceName            source of the request (used for logging)
     * @param metadataCollectionId  unique identifier for the home metadata collection
     * @param provenanceType        origin of the relationship
     * @param userName              name of the creator
     * @param typeName              name of the type
     * @param properties            properties for the relationship
     * @return a relationship that is filled out
     * @throws TypeErrorException  the type name is not recognized as a relationship type
     */
    Relationship getNewRelationship(String                 sourceName,
                                    String                 metadataCollectionId,
                                    InstanceProvenanceType provenanceType,
                                    String                 userName,
                                    String                 typeName,
                                    InstanceProperties     properties) throws TypeErrorException;


    /**
     * Return a filled out relationship which just needs the entity proxies added.
     *
     * @param sourceName             source of the request (used for logging)
     * @param metadataCollectionId   unique identifier for the home metadata collection
     * @param metadataCollectionName unique name for the home metadata collection
     * @param provenanceType         origin of the relationship
     * @param userName               name of the creator
     * @param typeName               name of the type
     * @param properties             properties for the relationship
     * @return a relationship that is filled out
     * @throws TypeErrorException  the type name is not recognized as a relationship type
     */
    Relationship getNewRelationship(String                 sourceName,
                                    String                 metadataCollectionId,
                                    String                 metadataCollectionName,
                                    InstanceProvenanceType provenanceType,
                                    String                 userName,
                                    String                 typeName,
                                    InstanceProperties     properties) throws TypeErrorException;


    /**
     * Return a classification with the header and type information filled out.  The caller only needs to add properties
     * to complete the set up of the classification.
     *
     * @param sourceName      source of the request (used for logging)
     * @param metadataCollectionId  unique identifier for the home metadata collection
     * @param provenanceType        origin of the classification
     * @param userName        name of the creator
     * @param typeName        name of the type
     * @param entityTypeName  name of the type for the entity that this classification is to be attached to.
     * @param classificationOrigin     is this explicitly assigned or propagated
     * @param classificationOriginGUID  if propagated this the GUID of the origin
     * @param properties      properties for the classification
     * @return partially filled out classification needs properties and possibly origin information
     * @throws TypeErrorException  the type name is not recognized as a classification type.
     */
    Classification getNewClassification(String                 sourceName,
                                        String                 metadataCollectionId,
                                        InstanceProvenanceType provenanceType,
                                        String                 userName,
                                        String                 typeName,
                                        String                 entityTypeName,
                                        ClassificationOrigin   classificationOrigin,
                                        String                 classificationOriginGUID,
                                        InstanceProperties     properties) throws TypeErrorException;


    /**
     * Return a classification with the header and type information filled out.  The caller only needs to add properties
     * to complete the set up of the classification.
     *
     * @param sourceName      source of the request (used for logging)
     * @param metadataCollectionId    unique identifier for the home metadata collection
     * @param metadataCollectionName  unique name for the home metadata collection
     * @param provenanceType        origin of the classification
     * @param userName        name of the creator
     * @param typeName        name of the type
     * @param entityTypeName  name of the type for the entity that this classification is to be attached to.
     * @param classificationOrigin     is this explicitly assigned or propagated
     * @param classificationOriginGUID  if propagated this the GUID of the origin
     * @param properties      properties for the classification
     * @return partially filled out classification needs properties and possibly origin information
     * @throws TypeErrorException  the type name is not recognized as a classification type.
     */
    Classification getNewClassification(String                 sourceName,
                                        String                 metadataCollectionId,
                                        String                 metadataCollectionName,
                                        InstanceProvenanceType provenanceType,
                                        String                 userName,
                                        String                 typeName,
                                        String                 entityTypeName,
                                        ClassificationOrigin   classificationOrigin,
                                        String                 classificationOriginGUID,
                                        InstanceProperties     properties) throws TypeErrorException;


    /**
     * Return a classification with the header and type information filled out.  The caller only needs to add properties
     * to complete the set up of the classification.  This method is deprecated because it does not take the provenance information.
     * The implementation of this method sets the provenance information to "LOCAL_COHORT".
     *
     * @param sourceName     source of the request (used for logging)
     * @param userName       name of the creator
     * @param typeName       name of the type
     * @param entityTypeName name of the type for the entity that this classification is to be attached to
     * @param classificationOrigin source of the classification (assigned or propagated)
     * @param classificationOriginGUID unique identifier of element that originated the classification if propagated
     * @param properties     properties for the classification
     * @return partially filled out classification needs properties and possibly origin information
     * @throws TypeErrorException the type name is not recognized as a classification type.
     */
    @Deprecated
    Classification getNewClassification(String               sourceName,
                                        String               userName,
                                        String               typeName,
                                        String               entityTypeName,
                                        ClassificationOrigin classificationOrigin,
                                        String               classificationOriginGUID,
                                        InstanceProperties   properties) throws TypeErrorException;


    /**
     * Throws an exception if an entity is classified with the supplied classification name.
     * It is typically used when adding new classifications to entities.
     *
     * @param sourceName          source of the request (used for logging)
     * @param entity              entity to update
     * @param classificationName  classification to retrieve
     * @param methodName          calling method
     * @throws ClassificationErrorException  the classification is not attached to the entity
     */
    @Deprecated
    void checkEntityNotClassifiedEntity(String        sourceName,
                                        EntitySummary entity,
                                        String        classificationName,
                                        String        methodName) throws ClassificationErrorException;



    /**
     * Throws an exception if an entity is classified with the supplied classification name and the requested
     * properties are different from the existing properties.
     * It is typically used when adding new classifications to entities and there is a possibility of a race condition
     * with multiple threads attempting to add the same classification.
     *
     * @param sourceName          source of the request (used for logging)
     * @param entity              entity to update
     * @param classificationName  classification to retrieve
     * @param classificationProperties list of properties to set in the classification
     * @param auditLog            optional logging destination
     * @param methodName          calling method
     * @return duplicate classification
     * @throws ClassificationErrorException  the classification is not attached to the entity
     */
    Classification checkEntityNotClassifiedEntity(String             sourceName,
                                                  EntitySummary      entity,
                                                  String             classificationName,
                                                  InstanceProperties classificationProperties,
                                                  AuditLog           auditLog,
                                                  String             methodName) throws ClassificationErrorException;


    /**
     * Add a classification to an existing entity.
     *
     * @param sourceName          source of the request (used for logging)
     * @param classificationList  entity classifications to update
     * @param newClassification   classification to add
     * @param methodName          calling method
     * @return updated entity
     */
    List<Classification> addClassificationToList(String                 sourceName,
                                                 List<Classification>   classificationList,
                                                 Classification         newClassification,
                                                 String                 methodName);


    /**
     * Add a classification to an existing entity.
     *
     * @param sourceName         source of the request (used for logging)
     * @param entity             entity to update
     * @param newClassification  classification to update
     * @param methodName         calling method
     * @return updated entity
     */
    EntityDetail addClassificationToEntity(String         sourceName,
                                           EntityDetail   entity,
                                           Classification newClassification,
                                           String         methodName);


    /**
     * Add a classification to an existing entity.
     *
     * @param sourceName         source of the request (used for logging)
     * @param entity             entity to update
     * @param newClassification  classification to update
     * @param methodName         calling method
     * @return updated entity
     */
    EntityProxy addClassificationToEntity(String         sourceName,
                                          EntityProxy    entity,
                                          Classification newClassification,
                                          String         methodName);


    /**
     * Return the properties from a named classification or null if classification not present or without properties.
     *
     * @param sourceName         source of the request (used for logging)
     * @param classifications    list of classifications for an entity
     * @param classificationName classification to retrieve
     * @param methodName         calling method
     * @return located properties - or null if none
     */
    InstanceProperties getClassificationProperties(String                sourceName,
                                                   List<Classification>  classifications,
                                                   String                classificationName,
                                                   String                methodName);


    /**
     * Return the named classification from an existing entity and throws an exception if it is not.
     *
     * @param sourceName          source of the request (used for logging)
     * @param entity              entity to update
     * @param classificationName  classification to retrieve
     * @param methodName          calling method
     * @return located classification
     * @throws ClassificationErrorException  the classification is not attached to the entity
     */
    Classification getClassificationFromEntity(String        sourceName,
                                               EntitySummary entity,
                                               String        classificationName,
                                               String        methodName) throws ClassificationErrorException;


    /**
     * Return the classifications from the requested metadata collection.  Not, this method does not cope with metadata collection ids of null.
     *
     * @param sourceName         source of the request (used for logging)
     * @param entity             entity to update
     * @param metadataCollectionId metadata collection to retrieve
     * @param methodName         calling method
     * @return located classification
     */
    List<Classification> getHomeClassificationsFromEntity(String       sourceName,
                                                          EntityDetail entity,
                                                          String       metadataCollectionId,
                                                          String       methodName);


    /**
     * Replace an existing classification with a new one
     *
     * @param sourceName         source of the request (used for logging)
     * @param userName           name of the editor
     * @param entity             entity to update
     * @param newClassification  classification to update
     * @param methodName         calling method
     * @return updated entity
     */
    EntityDetail updateClassificationInEntity(String         sourceName,
                                              String         userName,
                                              EntityDetail   entity,
                                              Classification newClassification,
                                              String         methodName);


    /**
     * Replace an existing classification with a new one
     *
     * @param sourceName         source of the request (used for logging)
     * @param userName           name of the editor
     * @param entity             entity to update
     * @param newClassification  classification to update
     * @param methodName         calling method
     * @return updated entity
     */
    EntityProxy updateClassificationInEntity(String         sourceName,
                                             String         userName,
                                             EntityProxy    entity,
                                             Classification newClassification,
                                             String         methodName);


    /**
     * Remove a classification from an entity.
     *
     * @param sourceName             source of the request (used for logging)
     * @param entity                 entity to update
     * @param oldClassificationName  classification to remove
     * @param methodName             calling method
     * @return updated entity
     * @throws ClassificationErrorException  the entity was not classified with this classification
     */
    EntityDetail deleteClassificationFromEntity(String       sourceName,
                                                EntityDetail entity,
                                                String       oldClassificationName,
                                                String       methodName) throws ClassificationErrorException;


    /**
     * Remove a classification from an entity
     *
     * @param sourceName             source of the request (used for logging)
     * @param entityProxy                 entity to update
     * @param oldClassificationName  classification to remove
     * @param methodName             calling method
     * @return updated entity
     * @throws ClassificationErrorException  the entity was not classified with this classification
     */
    EntityProxy deleteClassificationFromEntity(String       sourceName,
                                               EntityProxy  entityProxy,
                                               String       oldClassificationName,
                                               String       methodName) throws ClassificationErrorException;


    /**
     * Merge two sets of instance properties.
     *
     * @param sourceName          source of the request (used for logging)
     * @param existingProperties  current set of properties
     * @param newProperties       properties to add/update
     * @return merged properties
     */
    InstanceProperties mergeInstanceProperties(String             sourceName,
                                               InstanceProperties existingProperties,
                                               InstanceProperties newProperties);


    /**
     * Changes the control information to reflect an update in an instance.
     *
     * @param userId            user making the change.
     * @param originalInstance  original instance before the change
     * @param updatedInstance   new version of the instance that needs updating
     * @return updated instance
     */
    Relationship incrementVersion(String              userId,
                                  InstanceAuditHeader originalInstance,
                                  Relationship        updatedInstance);

    /**
     * Changes the control information to reflect an update in an instance.
     *
     * @param userId            user making the change.
     * @param originalInstance  original instance before the change
     * @param updatedInstance   new version of the instance that needs updating
     * @return updated instance
     */
    Classification incrementVersion(String              userId,
                                    InstanceAuditHeader originalInstance,
                                    Classification      updatedInstance);


    /**
     * Changes the control information to reflect an update in an instance.
     *
     * @param userId            user making the change.
     * @param originalInstance  original instance before the change
     * @param updatedInstance   new version of the instance that needs updating
     * @return updated instance
     */
    EntityDetail incrementVersion(String              userId,
                                  InstanceAuditHeader originalInstance,
                                  EntityDetail        updatedInstance);


    /**
     * Generate an entity proxy from an entity and its TypeDef.
     *
     * @param sourceName                 source of the request (used for logging)
     * @param entity                     entity instance
     * @return                           new entity proxy
     * @throws RepositoryErrorException  logic error in the repository corrupted entity
     */
    EntityProxy getNewEntityProxy(String       sourceName,
                                  EntityDetail entity) throws RepositoryErrorException;


    /**
     * Return a filled out entity proxy.
     *
     * @param sourceName            source of the request (used for logging)
     * @param metadataCollectionId  unique identifier for the home metadata collection
     * @param provenanceType        origin of the entity
     * @param userName              name of the creator
     * @param typeName              name of the type
     * @param properties            properties for the entity
     * @param classifications       list of classifications for the entity
     * @return  an entity proxy that is filled out
     * @throws TypeErrorException   the type name is not recognized as an entity type
     */
    EntityProxy getNewEntityProxy(String                 sourceName,
                                  String                 metadataCollectionId,
                                  InstanceProvenanceType provenanceType,
                                  String                 userName,
                                  String                 typeName,
                                  InstanceProperties     properties,
                                  List<Classification>   classifications) throws TypeErrorException;

    /**
     * Return boolean true if entity is linked by this relationship.
     *
     * @param sourceName    name of source requesting help
     * @param entityGUID    unique identifier of entity
     * @param relationship  relationship to test
     * @return boolean indicating whether the entity is mentioned in the relationship
     */
    boolean relatedEntity(String       sourceName,
                          String       entityGUID,
                          Relationship relationship);

    /**
     * Returns the type name from an instance (entity, relationship or classification).
     *
     * @param instance instance to read
     * @return String type name
     * @throws RepositoryErrorException unable to locate type
     * @throws InvalidParameterException invalid property value
     */
    String   getTypeName(InstanceAuditHeader instance) throws RepositoryErrorException,
                                                              InvalidParameterException;


    /**
     * Return the guid of an entity linked to end 1 of the relationship.
     *
     * @param relationship relationship to parse
     * @return String unique identifier
     */
    String  getEnd1EntityGUID(Relationship relationship);


    /**
     * Return the guid of an entity linked to end 2 of the relationship.
     *
     * @param relationship relationship to parse
     * @return String unique identifier
     */
    String  getEnd2EntityGUID(Relationship relationship);

    /**
     * Use the paging and sequencing parameters to format the results for a repository call that returns a list of
     * entity instances.
     *
     * @param fullResults - the full list of results in an arbitrary order
     * @param fromElement - the starting element number of the instances to return. This is used when retrieving elements
     *                    beyond the first page of results. Zero means start from the first element.
     * @param sequencingProperty - String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder - Enum defining how the results should be ordered.
     * @param pageSize - the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return results array as requested
     * @throws PropertyErrorException the sequencing property specified is not valid for any of the requested types of
     *                                  entity.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     */
    List<EntityDetail>  formatEntityResults(List<EntityDetail> fullResults,
                                            int                fromElement,
                                            String             sequencingProperty,
                                            SequencingOrder    sequencingOrder,
                                            int                pageSize) throws PagingErrorException,
                                                                                PropertyErrorException;


    /**
     * Use the paging and sequencing parameters to format the results for a repository call that returns a list of
     * relationship instances.
     *
     * @param fullResults - the full list of results in an arbitrary order. This is supplied not empty.
     * @param fromElement - the starting element number of the instances to return. This is used when retrieving elements
     *                    beyond the first page of results. Zero means start from the first element.
     * @param sequencingProperty - String name of the property that is to be used to sequence the results.
     *                           Null means do not sequence on a property name (see SequencingOrder).
     * @param sequencingOrder - Enum defining how the results should be ordered.
     * @param pageSize - the maximum number of result entities that can be returned on this request.  Zero means
     *                 unrestricted return results size.
     * @return results array as requested
     * @throws PropertyErrorException the sequencing property specified is not valid for any of the requested types of
     *                                  relationship.
     * @throws PagingErrorException the paging/sequencing parameters are set up incorrectly.
     */
     List<Relationship>  formatRelationshipResults(List<Relationship> fullResults,
                                                   int                fromElement,
                                                   String             sequencingProperty,
                                                   SequencingOrder    sequencingOrder,
                                                   int                pageSize) throws PagingErrorException,
                                                                                       PropertyErrorException;


    /**
     * Retrieve an escaped version of the provided string that can be passed to methods that expect regular expressions,
     * without being interpreted as a regular expression (i.e. the returned string will be interpreted as a literal --
     * used to find an exact match of the string, irrespective of whether it contains characters that may have special
     * meanings to regular expressions).
     *
     * Note that usage of the string by methods that cannot handle regular expressions should first un-escape the string
     * using the getUnqualifiedLiteralString helper method.
     *
     * Finally, note that this enforces a case-sensitive search.
     *
     * @param searchString the string to escape to avoid being interpreted as a regular expression
     * @return string that is interpreted literally rather than as a regular expression
     * @see #isExactMatchRegex(String)
     * @see #getUnqualifiedLiteralString(String)
     * @see #getExactMatchRegex(String, boolean)
     */
    String getExactMatchRegex(String searchString);


    /**
     * Retrieve an escaped version of the provided string that can be passed to methods that expect regular expressions,
     * without being interpreted as a regular expression (i.e. the returned string will be interpreted as a literal --
     * used to find an exact match of the string, irrespective of whether it contains characters that may have special
     * meanings to regular expressions).
     *
     * Note that usage of the string by methods that cannot handle regular expressions should first un-escape the string
     * using the getUnqualifiedLiteralString helper method.
     *
     * @param searchString the string to escape to avoid being interpreted as a regular expression
     * @param insensitive set to true to have a case-insensitive exact match regular expression
     * @return string that is interpreted literally rather than as a regular expression
     * @see #isExactMatchRegex(String)
     * @see #getUnqualifiedLiteralString(String)
     */
    String getExactMatchRegex(String searchString, boolean insensitive);


    /**
     * Indicates whether the provided string should be treated as an exact match (true) or any other regular expression
     * (false).
     *
     * Note that this method relies on the use of the getExactMatchRegex helper method having been used to
     * qualify a string when it should be treated as a literal. That is, this method relies on the presence of the
     * escape sequences used by Java's Pattern.quote() method. The method is not intended to work on all strings in
     * general to arbitrarily detect whether they might be a regular expression or not.
     *
     * Primarily a helper method for methods that do not directly handle regular expressions (for those it
     * should be possible to just directly use the string as-is and it will be correctly interpreted).
     *
     * @param searchString the string to check whether it should be interpreted literally or as as a regular expression
     * @return true if the provided string should be interpreted literally, false if it should be interpreted as a regex
     * @see #getExactMatchRegex(String)
     * @see #getUnqualifiedLiteralString(String)
     */
    boolean isExactMatchRegex(String searchString);


    /**
     * Indicates whether the provided string should be treated as an exact match (true) or any other regular expression
     * (false).
     *
     * Note that this method relies on the use of the getExactMatchRegex helper method having been used to
     * qualify a string when it should be treated as a literal. That is, this method relies on the presence of the
     * escape sequences used by Java's Pattern.quote() method. The method is not intended to work on all strings in
     * general to arbitrarily detect whether they might be a regular expression or not.
     *
     * Primarily a helper method for methods that do not directly handle regular expressions (for those it
     * should be possible to just directly use the string as-is and it will be correctly interpreted).
     *
     * @param searchString the string to check whether it should be interpreted literally or as as a regular expression
     * @param insensitive when true, only return true if the string is a case-insensitive exact match regex; when
     *                    false, only return true if the string is a case-sensitive exact match regex
     * @return true if the provided string should be interpreted literally, false if it should be interpreted as a
     *          regex
     * @see #getExactMatchRegex(String, boolean)
     * @see #getUnqualifiedLiteralString(String)
     */
    boolean isExactMatchRegex(String searchString, boolean insensitive);


    /**
     * Retrieve an escaped version of the provided string that can be passed to methods that expect regular expressions,
     * to search for the string with a "contains" semantic. The passed string will NOT be treated as a regular expression;
     * if you intend to use both a "contains" semantic and a regular expression within the string, simply construct your
     * own regular expression directly (not with this helper method).
     *
     * Note that usage of the returned string by methods that cannot handle regular expressions should first un-escape
     * the returned string using the getUnqualifiedLiteralString helper method.
     *
     * Finally, note that this enforces a case-sensitive search.
     *
     * @param searchString the string to escape to avoid being interpreted as a regular expression, but also wrap to obtain a "contains" semantic
     * @return string that is interpreted literally, wrapped for a "contains" semantic
     * @see #isContainsRegex(String)
     * @see #getUnqualifiedLiteralString(String)
     * @see #getContainsRegex(String, boolean)
     */
    String getContainsRegex(String searchString);


    /**
     * Retrieve an escaped version of the provided string that can be passed to methods that expect regular expressions,
     * to search for the string with a "contains" semantic. The passed string will NOT be treated as a regular expression;
     * if you intend to use both a "contains" semantic and a regular expression within the string, simply construct your
     * own regular expression directly (not with this helper method).
     *
     * Note that usage of the returned string by methods that cannot handle regular expressions should first un-escape
     * the returned string using the getUnqualifiedLiteralString helper method.
     *
     * @param searchString the string to escape to avoid being interpreted as a regular expression, but also wrap to obtain a "contains" semantic
     * @param insensitive set to true to have a case-insensitive contains regular expression
     * @return string that is interpreted literally, wrapped for a "contains" semantic
     * @see #isContainsRegex(String)
     * @see #getUnqualifiedLiteralString(String)
     */
    String getContainsRegex(String searchString, boolean insensitive);


    /**
     * Indicates whether the provided string should be treated as a simple "contains" regular expression (true) or any
     * other regular expression (false).
     *
     * Note that this method relies on the use of the getContainsRegex helper method having been used to
     * qualify a string when it should be treated primarily as a literal with only very basic "contains" wrapping.
     *
     * Primarily a helper method for methods that do not directly handle regular expressions (for those it
     * should be possible to just directly use the string as-is and it will be correctly interpreted).
     *
     * @param searchString - the string to check whether it should be interpreted as a simple "contains"
     * @return true if the provided string should be interpreted as a simple "contains", false if it should be interpreted as a full regex
     * @see #getContainsRegex(String)
     * @see #getUnqualifiedLiteralString(String)
     */
    boolean isContainsRegex(String searchString);


    /**
     * Indicates whether the provided string should be treated as a simple "contains" regular expression (true) or any
     * other regular expression (false).
     *
     * Note that this method relies on the use of the getContainsRegex helper method having been used to
     * qualify a string when it should be treated primarily as a literal with only very basic "contains" wrapping.
     *
     * Primarily a helper method for methods that do not directly handle regular expressions (for those it
     * should be possible to just directly use the string as-is and it will be correctly interpreted).
     *
     * @param searchString the string to check whether it should be interpreted as a simple "contains"
     * @param insensitive when true, only return true if the string is a case-insensitive "contains" regex; when
     *                    false, only return true if the string is a case-sensitive "contains" regex
     * @return true if the provided string should be interpreted as a simple "contains", false if it should be interpreted as a full regex
     * @see #getContainsRegex(String, boolean)
     * @see #getUnqualifiedLiteralString(String)
     */
    boolean isContainsRegex(String searchString, boolean insensitive);


    /**
     * Retrieve an escaped version of the provided string that can be passed to methods that expect regular expressions,
     * to search for the string with a "startswith" semantic. The passed string will NOT be treated as a regular expression;
     * if you intend to use both a "startswith" semantic and a regular expression within the string, simply construct your
     * own regular expression directly (not with this helper method).
     *
     * Note that usage of the returned string by methods that cannot handle regular expressions should first un-escape
     * the returned string using the getUnqualifiedLiteralString helper method.
     *
     * Finally, note that this enforces a case-sensitive search.
     *
     * @param searchString the string to escape to avoid being interpreted as a regular expression, but also wrap to obtain a "startswith" semantic
     * @return string that is interpreted literally, wrapped for a "startswith" semantic
     * @see #isStartsWithRegex(String)
     * @see #getUnqualifiedLiteralString(String)
     * @see #getStartsWithRegex(String, boolean)
     */
    String getStartsWithRegex(String searchString);


    /**
     * Retrieve an escaped version of the provided string that can be passed to methods that expect regular expressions,
     * to search for the string with a "startswith" semantic. The passed string will NOT be treated as a regular expression;
     * if you intend to use both a "startswith" semantic and a regular expression within the string, simply construct your
     * own regular expression directly (not with this helper method).
     *
     * Note that usage of the returned string by methods that cannot handle regular expressions should first un-escape
     * the returned string using the getUnqualifiedLiteralString helper method.
     *
     * @param searchString the string to escape to avoid being interpreted as a regular expression, but also wrap to obtain a "startswith" semantic
     * @param insensitive set to true to have a case-insensitive "startswith" regular expression
     * @return string that is interpreted literally, wrapped for a "startswith" semantic
     * @see #isStartsWithRegex(String)
     * @see #getUnqualifiedLiteralString(String)
     */
    String getStartsWithRegex(String searchString, boolean insensitive);


    /**
     * Indicates whether the provided string should be treated as a simple "startswith" regular expression (true) or any
     * other regular expression (false).
     *
     * Note that this method relies on the use of the getStartsWithRegex helper method having been used to
     * qualify a string when it should be treated primarily as a literal with only very basic "startswith" wrapping.
     *
     * Primarily a helper method for methods that do not directly handle regular expressions (for those it
     * should be possible to just directly use the string as-is and it will be correctly interpreted).
     *
     * @param searchString the string to check whether it should be interpreted as a simple "startswith"
     * @return true if the provided string should be interpreted as a simple "startswith", false if it should be interpreted as a full regex
     * @see #getStartsWithRegex(String)
     * @see #getUnqualifiedLiteralString(String)
     */
    boolean isStartsWithRegex(String searchString);


    /**
     * Indicates whether the provided string should be treated as a simple "startswith" regular expression (true) or any
     * other regular expression (false).
     *
     * Note that this method relies on the use of the getStartsWithRegex helper method having been used to
     * qualify a string when it should be treated primarily as a literal with only very basic "startswith" wrapping.
     *
     * Primarily a helper method for methods that do not directly handle regular expressions (for those it
     * should be possible to just directly use the string as-is and it will be correctly interpreted).
     *
     * @param searchString the string to check whether it should be interpreted as a simple "startswith"
     * @param insensitive when true, only return true if the string is a case-insensitive "startswith" regex; when
     *                    false, only return true if the string is a case-sensitive "startswith" regex
     * @return true if the provided string should be interpreted as a simple "startswith", false if it should be interpreted as a full regex
     * @see #getStartsWithRegex(String, boolean)
     * @see #getUnqualifiedLiteralString(String)
     */
    boolean isStartsWithRegex(String searchString, boolean insensitive);


    /**
     * Retrieve an escaped version of the provided string that can be passed to methods that expect regular expressions,
     * to search for the string with an "endswith" semantic. The passed string will NOT be treated as a regular expression;
     * if you intend to use both a "endswith" semantic and a regular expression within the string, simply construct your
     * own regular expression directly (not with this helper method).
     *
     * Note that usage of the returned string by methods that cannot handle regular expressions should first un-escape
     * the returned string using the getUnqualifiedLiteralString helper method.
     *
     * Finally, note that this enforces a case-sensitive search.
     *
     * @param searchString the string to escape to avoid being interpreted as a regular expression, but also wrap to obtain an "endswith" semantic
     * @return string that is interpreted literally, wrapped for an "endswith" semantic
     * @see #isEndsWithRegex(String)
     * @see #getUnqualifiedLiteralString(String)
     * @see #getEndsWithRegex(String, boolean)
     */
    String getEndsWithRegex(String searchString);


    /**
     * Retrieve an escaped version of the provided string that can be passed to methods that expect regular expressions,
     * to search for the string with an "endswith" semantic. The passed string will NOT be treated as a regular expression;
     * if you intend to use both a "endswith" semantic and a regular expression within the string, simply construct your
     * own regular expression directly (not with this helper method).
     *
     * Note that usage of the returned string by methods that cannot handle regular expressions should first un-escape
     * the returned string using the getUnqualifiedLiteralString helper method.
     *
     * @param searchString the string to escape to avoid being interpreted as a regular expression, but also wrap to obtain an "endswith" semantic
     * @param insensitive set to true to have a case-insensitive "endswith" regular expression
     * @return string that is interpreted literally, wrapped for an "endswith" semantic
     * @see #isEndsWithRegex(String)
     * @see #getUnqualifiedLiteralString(String)
     */
    String getEndsWithRegex(String searchString, boolean insensitive);


    /**
     * Indicates whether the provided string should be treated as a simple "endswith" regular expression (true) or any
     * other regular expression (false).
     *
     * Note that this method relies on the use of the getEndsWithRegex helper method having been used to
     * qualify a string when it should be treated primarily as a literal with only very basic "endswith" wrapping.
     *
     * Primarily a helper method for methods that do not directly handle regular expressions (for those it
     * should be possible to just directly use the string as-is and it will be correctly interpreted).
     *
     * @param searchString the string to check whether it should be interpreted as a simple "endswith"
     * @return true if the provided string should be interpreted as a simple "endswith", false if it should be interpreted as a full regex
     * @see #getEndsWithRegex(String)
     * @see #getUnqualifiedLiteralString(String)
     */
    boolean isEndsWithRegex(String searchString);


    /**
     * Indicates whether the provided string should be treated as a simple "endswith" regular expression (true) or any
     * other regular expression (false).
     *
     * Note that this method relies on the use of the getEndsWithRegex helper method having been used to
     * qualify a string when it should be treated primarily as a literal with only very basic "endswith" wrapping.
     *
     * Primarily a helper method for methods that do not directly handle regular expressions (for those it
     * should be possible to just directly use the string as-is and it will be correctly interpreted).
     *
     * @param searchString the string to check whether it should be interpreted as a simple "endswith"
     * @param insensitive when true, only return true if the string is a case-insensitive "endswith" regex; when
     *                    false, only return true if the string is a case-sensitive "endswith" regex
     * @return true if the provided string should be interpreted as a simple "endswith", false if it should be interpreted as a full regex
     * @see #getEndsWithRegex(String, boolean)
     * @see #getUnqualifiedLiteralString(String)
     */
    boolean isEndsWithRegex(String searchString, boolean insensitive);


    /**
     * Retrieve an unescaped version of the provided string that can be treated as a literal (not a regular expression).
     * Primarily a helper method for methods that do not directly leverage regular expressions: so that they have a string
     * they can treat as a literal without needing to un-escape the regex-meaningful characters injected by the various
     * getXYZRegex helper methods.
     *
     * For example, this will translate the input of '.*\Qmy-search-string\E.*' into a return value of 'my-search-string'.
     *
     * @param searchString - the (potentially) wrapped and escaped string to un-escape and un-wrap
     * @return the un-escaped, un-wrapped literal string
     * @see #getExactMatchRegex(String)
     * @see #getContainsRegex(String)
     * @see #getStartsWithRegex(String)
     * @see #getEndsWithRegex(String)
     */
    String getUnqualifiedLiteralString(String searchString);


    /**
     * Indicates whether the provided string should be treated as a case-insensitive regular expression (true) or as a
     * case-sensitive regular expression (false).
     *
     * Note that this method relies on the use of the getXYZRegex helper methods having been used to qualify a string
     * with case-insensitivity.
     *
     * Primarily this is a helper method for methods that do not directly handle regular expressions (for those it
     * should be possible to just directly use the string as-is and it will be correctly interpreted).
     *
     * @param searchString the string to check whether it should be interpreted as case-insensitive
     * @return true if provided string should be interpreted as case-insensitive, false if it should be case-sensitive
     * @see #getExactMatchRegex(String, boolean)
     * @see #getStartsWithRegex(String, boolean)
     * @see #getEndsWithRegex(String, boolean)
     * @see #getContainsRegex(String, boolean)
     */
    boolean isCaseInsensitiveRegex(String searchString);


    /**
     * Calculate the differences between the two provided Relationship objects.
     *
     * @param left one of the Relationship objects to compare
     * @param right the other Relationship object to compare
     * @param ignoreModificationStamps true if we should ignore modification details (Version, UpdateTime, UpdatedBy)
     *                                 as differences, or false if we should include differences on these
     * @return RelationshipDifferences
     */
    RelationshipDifferences getRelationshipDifferences(Relationship left, Relationship right, boolean ignoreModificationStamps);


    /**
     * Calculate the differences between the two provided EntityDetail objects.
     *
     * @param left one of the EntityDetail objects to compare
     * @param right the other EntityDetail object to compare
     * @param ignoreModificationStamps true if we should ignore modification details (Version, UpdateTime, UpdatedBy)
     *                                 as differences, or false if we should include differences on these
     * @return EntityDetailDifferences
     */
    EntityDetailDifferences getEntityDetailDifferences(EntityDetail left, EntityDetail right, boolean ignoreModificationStamps);


    /**
     * Calculate the differences between the two provided EntityProxy objects.
     *
     * @param left one of the EntityProxy objects to compare
     * @param right the other EntityProxy object to compare
     * @param ignoreModificationStamps true if we should ignore modification details (Version, UpdateTime, UpdatedBy)
     *                                 as differences, or false if we should include differences on these
     * @return EntityProxyDifferences
     */
    EntityProxyDifferences getEntityProxyDifferences(EntityProxy left, EntityProxy right, boolean ignoreModificationStamps);


    /**
     * Calculate the differences between the two provided EntitySummary objects.
     *
     * @param left one of the EntitySummary objects to compare
     * @param right the other EntitySummary object to compare
     * @param ignoreModificationStamps true if we should ignore modification details (Version, UpdateTime, UpdatedBy)
     *                                 as differences, or false if we should include differences on these
     * @return EntitySummaryDifferences
     */
    EntitySummaryDifferences getEntitySummaryDifferences(EntitySummary left, EntitySummary right, boolean ignoreModificationStamps);


    /**
     * Convert the provided list of classification names into an equivalent SearchClassifications object.
     *
     * @param classificationNames list of classification names
     * @return SearchClassifications
     */
    SearchClassifications getSearchClassificationsFromList(List<String> classificationNames);


}
