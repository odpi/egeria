/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector;


import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.util.*;

/**
 * OMRSRepositoryHelper provides methods to repository connectors and repository event mappers to help
 * them build valid type definitions (TypeDefs), entities and relationships.  It is a facade to the
 * repository content manager which holds an in memory cache of all the active TypeDefs in the local server.
 * OMRSRepositoryHelper's purpose is to create an object that the repository connectors and event mappers can
 * create, use and discard without needing to know how to connect to the repository content manager.
 */
public interface OMRSRepositoryHelper
{
    /**
     * Return the list of typedefs active in the local repository.
     *
     * @return TypeDef gallery
     */
    TypeDefGallery getActiveTypeDefGallery();


    /**
     * Return the list of typedefs known by the local repository.
     *
     * @return TypeDef gallery
     */
    TypeDefGallery getKnownTypeDefGallery();


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
     * Return the TypeDefs identified by the name supplied by the caller.  The TypeDef name may have wild
     * card characters in it which is why the results are returned in a list.
     *
     * @param sourceName   source of the request (used for logging)
     * @param typeDefName  unique name for the TypeDef
     * @return TypeDef object or null if TypeDef is not known.
     */
    TypeDefGallery getActiveTypesByWildCardName(String sourceName,
                                                String typeDefName);


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
     * Returns an updated TypeDef that has had the supplied patch applied.  It throws an exception if any part of
     * the patch is incompatible with the original TypeDef.  For example, if there is a mismatch between
     * the type or version that either represents.
     *
     * @param sourceName       source of the TypeDef (used for logging)
     * @param typeDefPatch     patch to apply
     * @param originalTypeDef  typeDef to patch
     * @return updated TypeDef
     * @throws PatchErrorException        the patch is either badly formatted, or does not apply to the supplied TypeDef
     * @throws InvalidParameterException  the TypeDefPatch is null.
     */
    TypeDef applyPatch(String       sourceName,
                       TypeDef      originalTypeDef,
                       TypeDefPatch typeDefPatch) throws PatchErrorException, InvalidParameterException;


    /**
     * Validate that the type's name is not null.
     *
     * @param sourceName source of the request (used for logging)
     * @param standard name of the standard, null means any.
     * @param organization name of the organization, null means any.
     * @param identifier identifier of the element in the standard, null means any.
     * @param methodName method receiving the call
     * @return list of typeDefs
     */
    List<TypeDef> getMatchingActiveTypes(String sourceName,
                                         String standard,
                                         String organization,
                                         String identifier,
                                         String methodName);


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
    EntityDetail getSkeletonEntity(String                 sourceName,
                                   String                 metadataCollectionId,
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
     * Return a classification with the header and type information filled out.  The caller only needs to add properties
     * to complete the set up of the classification.
     *
     * @param sourceName      source of the request (used for logging)
     * @param userName        name of the creator
     * @param typeName        name of the type
     * @param entityTypeName  name of the type for the entity that this classification is to be attached to.
     * @param classificationOrigin     is this explicitly assigned or propagated
     * @param classificationOriginGUID  if propagated this the GUID of the origin
     * @param properties      properties for the classification
     * @return partially filled out classification needs properties and possibly origin information
     * @throws TypeErrorException  the type name is not recognized as a classification type.
     */
    Classification getNewClassification(String               sourceName,
                                        String               userName,
                                        String               typeName,
                                        String               entityTypeName,
                                        ClassificationOrigin classificationOrigin,
                                        String               classificationOriginGUID,
                                        InstanceProperties   properties) throws TypeErrorException;


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
     * Return the names classification from an existing entity.
     *
     * @param sourceName          source of the request (used for logging)
     * @param entity              entity to update
     * @param classificationName  classification to retrieve
     * @param methodName          calling method
     * @return located classification
     * @throws ClassificationErrorException  the classification is not attached to the entity
     */
    Classification getClassificationFromEntity(String       sourceName,
                                               EntityDetail entity,
                                               String       classificationName,
                                               String       methodName) throws ClassificationErrorException;


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
     * Return a oldClassification with the header and type information filled out.  The caller only needs to add properties
     * to complete the set up of the oldClassification.
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
     * Return a filled out entity.
     *
     * @param sourceName            source of the request (used for logging)
     * @param metadataCollectionId  unique identifier for the home metadata collection
     * @param provenanceType        origin of the entity
     * @param userName              name of the creator
     * @param typeName              name of the type
     * @param properties            properties for the entity
     * @param classifications       list of classifications for the entity
     * @return                      an entity that is filled out
     * @throws TypeErrorException   the type name is not recognized as an entity type
     */
    EntityProxy getNewEntityProxy(String                    sourceName,
                                  String                    metadataCollectionId,
                                  InstanceProvenanceType    provenanceType,
                                  String                    userName,
                                  String                    typeName,
                                  InstanceProperties        properties,
                                  List<Classification>      classifications) throws TypeErrorException;

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
     * Return the requested property or null if property is not found.  If the property is not
     * a string property then a logic exception is thrown
     *
     * @param sourceName  source of call
     * @param propertyName  name of requested property
     * @param properties  properties from the instance.
     * @param methodName  method of caller
     * @return string property value or null
     */
    String getStringProperty(String             sourceName,
                             String             propertyName,
                             InstanceProperties properties,
                             String             methodName);


    /**
     * Return the requested property or null if property is not found.  If the property is not
     * a map property then a logic exception is thrown
     *
     * @param sourceName  source of call
     * @param propertyName  name of requested property
     * @param properties  properties from the instance.
     * @param methodName  method of caller
     * @return string property value or null
     */
    InstanceProperties getMapProperty(String             sourceName,
                                      String             propertyName,
                                      InstanceProperties properties,
                                      String             methodName);



    /**
     * Return the requested property or null if property is not found.  If the property is not
     * a map property then a logic exception is thrown.
     *
     * @param sourceName source of call
     * @param propertyName name of requested property
     * @param properties properties from the instance.
     * @param methodName method of caller
     * @return string property value or null
     */
    Map<String, Object> getMapFromProperty(String             sourceName,
                                           String             propertyName,
                                           InstanceProperties properties,
                                           String             methodName);


    /**
     * Return the requested property or 0 if property is not found.  If the property is not
     * a int property then a logic exception is thrown
     *
     * @param sourceName  source of call
     * @param propertyName  name of requested property
     * @param properties  properties from the instance.
     * @param methodName  method of caller
     * @return string property value or null
     */
    int    getIntProperty(String             sourceName,
                          String             propertyName,
                          InstanceProperties properties,
                          String             methodName);


    /**
     * Return the requested property or false if property is not found.  If the property is not
     * a boolean property then a logic exception is thrown
     *
     * @param sourceName  source of call
     * @param propertyName  name of requested property
     * @param properties  properties from the instance.
     * @param methodName  method of caller
     * @return string property value or null
     */
    boolean getBooleanProperty(String             sourceName,
                               String             propertyName,
                               InstanceProperties properties,
                               String             methodName);


    /**
     * Add the supplied property to an instance properties object.  If the instance property object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName  name of caller
     * @param properties  properties object to add property to may be null.
     * @param propertyName  name of property
     * @param propertyValue  value of property
     * @param methodName  calling method name
     * @return instance properties object.
     */
    InstanceProperties addStringPropertyToInstance(String             sourceName,
                                                   InstanceProperties properties,
                                                   String             propertyName,
                                                   String             propertyValue,
                                                   String             methodName);


    /**
     * Add the supplied property to an instance properties object.  If the instance property object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName  name of caller
     * @param properties  properties object to add property to may be null.
     * @param propertyName  name of property
     * @param propertyValue  value of property
     * @param methodName  calling method name
     * @return instance properties object.
     */
    InstanceProperties addIntPropertyToInstance(String             sourceName,
                                                InstanceProperties properties,
                                                String             propertyName,
                                                int                propertyValue,
                                                String             methodName);


    /**
     * Add the supplied property to an instance properties object.  If the instance property object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName  name of caller
     * @param properties  properties object to add property to may be null.
     * @param propertyName  name of property
     * @param propertyValue  value of property
     * @param methodName  calling method name
     * @return instance properties object.
     */
    InstanceProperties addBooleanPropertyToInstance(String             sourceName,
                                                    InstanceProperties properties,
                                                    String             propertyName,
                                                    boolean            propertyValue,
                                                    String             methodName);


    /**
     * Add the supplied property to an instance properties object.  If the instance property object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName  name of caller
     * @param properties  properties object to add property to may be null.
     * @param propertyName  name of property
     * @param ordinal  numeric value of property
     * @param symbolicName  String value of property
     * @param description  String description of property value
     * @param methodName  calling method name
     * @return instance properties object.
     */
    InstanceProperties addEnumPropertyToInstance(String             sourceName,
                                                 InstanceProperties properties,
                                                 String             propertyName,
                                                 int                ordinal,
                                                 String             symbolicName,
                                                 String             description,
                                                 String             methodName);

    /**
     * Add the supplied property to an instance properties object.  If the instance property object
     * supplied is null, a new instance properties object is created.
     *
     * @param sourceName name of caller
     * @param properties properties object to add property to, may be null.
     * @param propertyName name of property
     * @param mapValues contents of the map
     * @param methodName calling method name
     * @return instance properties object.
     */
    InstanceProperties addMapPropertyToInstance(String              sourceName,
                                                InstanceProperties  properties,
                                                String              propertyName,
                                                Map<String, Object> mapValues,
                                                String              methodName);
}
