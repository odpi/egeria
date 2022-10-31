/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.MatchCriteria;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchClassifications;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.search.SearchProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.*;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * OMRSRepositoryValidator provides methods to validate TypeDefs and Instances returned from
 * an open metadata repository.  It is typically used by OMRS repository connectors and
 * repository event mappers.
 */
public interface OMRSRepositoryValidator
{
    /**
     * Return a boolean flag indicating whether the list of TypeDefs passed are compatible with the
     * all known typedefs.
     *
     * A valid TypeDef is one that matches name, GUID and version to the full list of TypeDefs.
     * If a new TypeDef is present, it is added to the enterprise list.
     *
     * @param sourceName  source of the request (used for logging)
     * @param typeDefs    list of TypeDefs.
     * @param methodName  calling method
     * @throws RepositoryErrorException  a conflicting or invalid TypeDef has been returned
     */
    void   validateEnterpriseTypeDefs(String        sourceName,
                                      List<TypeDef> typeDefs,
                                      String        methodName) throws RepositoryErrorException;


    /**
     * Return a boolean flag indicating whether the list of TypeDefs passed are compatible with the
     * all known typedefs.
     *
     * A valid TypeDef is one that matches name, GUID and version to the full list of TypeDefs.
     * If a new TypeDef is present, it is added to the enterprise list.
     *
     * @param sourceName  source of the request (used for logging)
     * @param attributeTypeDefs  list of AttributeTypeDefs.
     * @param methodName          calling method
     * @throws RepositoryErrorException  a conflicting or invalid AttributeTypeDef has been returned
     */
    void   validateEnterpriseAttributeTypeDefs(String                 sourceName,
                                               List<AttributeTypeDef> attributeTypeDefs,
                                               String                 methodName) throws RepositoryErrorException;


    /**
     * Return boolean indicating whether the TypeDef/AttributeTypeDef is in use in the repository.
     *
     * @param sourceName  source of the request (used for logging)
     * @param typeGUID  unique identifier of the type
     * @param typeName  unique name of the type
     * @return boolean flag
     */
    boolean isActiveType(String sourceName,
                         String typeGUID,
                         String typeName);


    /**
     * Return boolean indicating whether the TypeDef/AttributeTypeDef is in use in the repository.
     *
     * @param sourceName  source of the request (used for logging)
     * @param typeGUID  unique identifier of the type
     * @return boolean flag
     */
    boolean isActiveTypeId(String sourceName, String typeGUID);


    /**
     * Return boolean indicating whether the TypeDef is one of the open metadata types.
     *
     * @param sourceName  source of the request (used for logging)
     * @param typeGUID  unique identifier of the type
     * @param typeName  unique name of the type
     * @return boolean flag
     */
    boolean isOpenType(String sourceName, String typeGUID, String typeName);


    /**
     * Return boolean indicating whether the TypeDef is one of the open metadata types.
     *
     * @param sourceName  source of the request (used for logging)
     * @param typeGUID  unique identifier of the type
     * @return boolean flag
     */
    boolean isOpenTypeId(String sourceName, String typeGUID);


    /**
     * Return boolean indicating whether the TypeDef/AttributeTypeDef is in use in the repository.
     *
     * @param sourceName  source of the request (used for logging)
     * @param typeGUID  unique identifier of the type
     * @param typeName  unique name of the type
     * @return boolean flag
     */
    boolean isKnownType(String sourceName, String typeGUID, String typeName);


    /**
     * Return boolean indicating whether the TypeDef/AttributeTypeDef is in use in the repository.
     *
     * @param sourceName  source of the request (used for logging)
     * @param typeGUID  unique identifier of the type
     * @return boolean flag
     */
    boolean isKnownTypeId(String sourceName, String typeGUID);


    /**
     * Return boolean indicating whether the TypeDef identifiers are from a single known type or not.
     *
     * @param sourceName  source of the request (used for logging)
     * @param typeGUID  unique identifier of the TypeDef
     * @param typeName  unique name of the TypeDef
     * @return boolean result
     */
    boolean validTypeId(String sourceName,
                        String typeGUID,
                        String typeName);


    /**
     * Return boolean indicating whether the TypeDef identifiers are from a single known type or not.
     *
     * @param sourceName  source of the request (used for logging)
     * @param typeDefGUID  unique identifier of the TypeDef
     * @param typeDefName  unique name of the TypeDef
     * @param category  category for the TypeDef
     * @return boolean result
     */
     boolean validTypeDefId(String          sourceName,
                            String          typeDefGUID,
                            String          typeDefName,
                            TypeDefCategory category);


    /**
     * Return boolean indicating whether the AttributeTypeDef identifiers are from a single known type or not.
     *
     * @param sourceName source of the request (used for logging)
     * @param attributeTypeDefGUID unique identifier of the AttributeTypeDef
     * @param attributeTypeDefName unique name of the AttributeTypeDef
     * @param category category for the AttributeTypeDef
     * @return boolean result
     */
    boolean validAttributeTypeDefId(String                   sourceName,
                                    String                   attributeTypeDefGUID,
                                    String                   attributeTypeDefName,
                                    AttributeTypeDefCategory category);


    /**
     * Return boolean indicating whether the TypeDef identifiers are from a single known type or not.
     *
     * @param sourceName  source of the request (used for logging)
     * @param typeDefGUID  unique identifier of the TypeDef
     * @param typeDefName  unique name of the TypeDef
     * @param typeDefVersion  version of the type
     * @param category  category for the TypeDef
     * @return boolean result
     */
    boolean validTypeDefId(String          sourceName,
                           String          typeDefGUID,
                           String          typeDefName,
                           String          typeDefVersion,
                           TypeDefCategory category);


    /**
     * Return boolean indicating whether the TypeDef identifiers are from a single known type or not.
     *
     * @param sourceName  source of the request (used for logging)
     * @param attributeTypeDefGUID  unique identifier of the TypeDef
     * @param attributeTypeDefName  unique name of the TypeDef
     * @param attributeTypeDefVersion  version of the type
     * @param category  category for the TypeDef
     * @return boolean result
     */
    boolean validAttributeTypeDefId(String                   sourceName,
                                    String                   attributeTypeDefGUID,
                                    String                   attributeTypeDefName,
                                    String                   attributeTypeDefVersion,
                                    AttributeTypeDefCategory category);


    /**
     * Return boolean indicating whether the supplied TypeDef is valid or not.
     *
     * @param sourceName  source of the TypeDef (used for logging)
     * @param typeDef  TypeDef to test
     * @return boolean result
     */
    boolean validTypeDef(String  sourceName,
                         TypeDef typeDef);


    /**
     * Return boolean indicating whether the supplied AttributeTypeDef is valid or not.
     *
     * @param sourceName  source of the request (used for logging)
     * @param attributeTypeDef  TypeDef to test
     * @return boolean result
     */
    boolean validAttributeTypeDef(String           sourceName,
                                  AttributeTypeDef attributeTypeDef);


    /**
     * Return boolean indicating whether the supplied TypeDefSummary is valid or not.
     *
     * @param sourceName  source of the request (used for logging)
     * @param typeDefSummary  TypeDefSummary to test.
     * @return boolean result.
     */
    boolean validTypeDefSummary(String         sourceName,
                                TypeDefSummary typeDefSummary);


    /**
     * Test that the supplied entity is valid.
     *
     * @param sourceName  source of the request (used for logging)
     * @param entity  entity to test
     * @return boolean result
     */
    boolean validEntity(String        sourceName,
                        EntitySummary entity);


    /**
     * Test that the supplied entity is valid.
     *
     * @param sourceName  source of the request (used for logging)
     * @param entity  entity to test
     * @return boolean result
     */
    boolean validEntity(String      sourceName,
                        EntityProxy entity);


    /**
     * Test that the supplied entity is valid.
     *
     * @param sourceName  source of the request (used for logging)
     * @param entity  entity to test
     * @return boolean result
     */
    boolean validEntity(String       sourceName,
                        EntityDetail entity);


    /**
     * Test that the supplied relationship is valid.
     *
     * @param sourceName  source of the request (used for logging)
     * @param relationship  relationship to test
     * @return boolean result
     */
    boolean validRelationship(String       sourceName,
                              Relationship relationship);


    /**
     * Verify that the identifiers for an instance are correct.
     *
     * @param sourceName  source of the request (used for logging)
     * @param typeDefGUID  unique identifier for the type.
     * @param typeDefName  unique name for the type.
     * @param category  expected category of the instance.
     * @param instanceGUID  unique identifier for the instance.
     * @return boolean indicating whether the identifiers are ok.
     */
    boolean validInstanceId(String          sourceName,
                            String          typeDefGUID,
                            String          typeDefName,
                            TypeDefCategory category,
                            String          instanceGUID);


    /* ==============================================================
     * Simple parameter validation methods needed by all repositories
     * ==============================================================
     */


    /**
     * Validate that the supplied user Id is not null.
     *
     * @param sourceName  name of source of request.
     * @param userId  userId passed on call to this metadata collection.
     * @param methodName  name of method requesting the validation.
     * @throws InvalidParameterException the userId is invalid
     */
    void validateUserId(String sourceName,
                        String userId,
                        String methodName) throws InvalidParameterException;


    /**
     * Validate that a TypeDef's identifiers are not null.
     *
     * @param sourceName  source of the request (used for logging)
     * @param guidParameterName  name of the parameter that passed the guid.
     * @param nameParameterName  name of the parameter that passed the name.
     * @param guid  unique identifier for a type or an instance passed on the request
     * @param name  name of TypeDef.
     * @param methodName  method receiving the call
     * @throws InvalidParameterException  no guid provided
     */
    void validateTypeDefIds(String sourceName,
                            String guidParameterName,
                            String nameParameterName,
                            String guid,
                            String name,
                            String methodName) throws InvalidParameterException;

    /**
     * Validate that a TypeDef's identifiers are not null and return the type.
     *
     * @param sourceName  source of the request (used for logging)
     * @param guidParameterName  name of the parameter that passed the guid.
     * @param nameParameterName  name of the parameter that passed the name.
     * @param guid  unique identifier for a type or an instance passed on the request
     * @param name  name of TypeDef.
     * @param methodName  method receiving the call
     * @return retrieved type
     * @throws InvalidParameterException  no guid provided
     */
    TypeDef getValidTypeDefFromIds(String sourceName,
                                   String guidParameterName,
                                   String nameParameterName,
                                   String guid,
                                   String name,
                                   String methodName) throws InvalidParameterException;



    /**
     * Validate that an AttributeTypeDef's identifiers are not null.
     *
     * @param sourceName  source of the request (used for logging)
     * @param guidParameterName  name of the parameter that passed the guid.
     * @param nameParameterName  name of the parameter that passed the name.
     * @param guid  unique identifier for a type or an instance passed on the request
     * @param name  name of TypeDef.
     * @param methodName  method receiving the call
     * @throws InvalidParameterException  no guid, or name provided
     */
    void validateAttributeTypeDefIds(String sourceName,
                                     String guidParameterName,
                                     String nameParameterName,
                                     String guid,
                                     String name,
                                     String methodName) throws InvalidParameterException;


    /**
     * Validate that an AttributeTypeDef's identifiers are not null and are recognized
     * and return the type.
     *
     * @param sourceName  source of the request (used for logging)
     * @param guidParameterName  name of the parameter that passed the guid.
     * @param nameParameterName  name of the parameter that passed the name.
     * @param guid  unique identifier for a type or an instance passed on the request
     * @param name  name of TypeDef.
     * @param methodName  method receiving the call
     * @return retrieved type
     * @throws InvalidParameterException  no guid, or name provided
     */
    AttributeTypeDef getValidAttributeTypeDefFromIds(String sourceName,
                                                     String guidParameterName,
                                                     String nameParameterName,
                                                     String guid,
                                                     String name,
                                                     String methodName) throws InvalidParameterException;



    /**
     * Validate that type's identifier is not null.
     *
     * @param sourceName  source of the request (used for logging)
     * @param guidParameterName  name of the parameter that passed the guid.
     * @param guid  unique identifier for a type or an instance passed on the request
     * @param methodName  method receiving the call
     * @throws InvalidParameterException  no guid provided
     * @throws TypeErrorException  guid is not for a recognized type
     */
    void validateTypeGUID(String sourceName,
                          String guidParameterName,
                          String guid,
                          String methodName) throws InvalidParameterException,
                                                    TypeErrorException;


    /**
     * Validate that type's identifier is not null.
     *
     * @param sourceName  source of the request (used for logging)
     * @param guidParameterName  name of the parameter that passed the guid.
     * @param guid  unique identifier for a type or an instance passed on the request
     * @param methodName  method receiving the call
     * @throws TypeErrorException  unknown type guid
     */
    void validateOptionalTypeGUID(String sourceName,
                                  String guidParameterName,
                                  String guid,
                                  String methodName) throws TypeErrorException;


    /**
     * Validate that the types and subtypes (if specified) fit each other.
     *
     * @param sourceName  source of the request (used for logging)
     * @param guidParameterName  name of the parameter that passed the guid
     * @param guid  unique identifier for a type passed on the request
     * @param subtypeParameterName  name of the parameter that passed a list of subtype guids
     * @param subtypeGuids  list of unique identifiers for the subtypes passed on the request
     * @param methodName  method receiving the call
     * @throws TypeErrorException  unknown type guid, or subtype guids that are not subtypes of the provided guid
     */
    void validateOptionalTypeGUIDs(String sourceName,
                                   String guidParameterName,
                                   String guid,
                                   String subtypeParameterName,
                                   List<String> subtypeGuids,
                                   String methodName) throws TypeErrorException;


    /**
     * Verify that a TypeDefPatch is not null.
     *
     * @param sourceName  source of the request (used for logging)
     * @param patch  patch to test
     * @param methodName  calling method
     * @return current value of the type
     * @throws InvalidParameterException  the patch is null
     * @throws TypeDefNotKnownException the type is not known
     * @throws PatchErrorException  the patch is invalid
     */
    TypeDef validateTypeDefPatch(String       sourceName,
                                 TypeDefPatch patch,
                                 String       methodName) throws InvalidParameterException,
                                                                 TypeDefNotKnownException,
                                                                 PatchErrorException;


    /**
     * Validate that if a type's identifier is passed then it is valid.
     *
     * @param sourceName  source of the request (used for logging)
     * @param guidParameterName  name of the parameter that passed the guid.
     * @param guid  unique identifier for a type or an instance passed on the request
     * @param methodName  method receiving the call
     * @throws TypeErrorException  invalid type provided
     */
    void validateInstanceTypeGUID(String sourceName,
                                  String guidParameterName,
                                  String guid,
                                  String methodName) throws TypeErrorException;


    /**
     * Validate that type's name is not null.
     *
     * @param sourceName  source of the request (used for logging)
     * @param nameParameterName  name of the parameter that passed the name.
     * @param name  unique identifier for a type or an instance passed on the request
     * @param methodName  method receiving the call
     * @throws InvalidParameterException  no name provided
     */
    void validateTypeName(String sourceName,
                          String nameParameterName,
                          String name,
                          String methodName) throws InvalidParameterException;


    /**
     * Validate that a TypeDef's category is not null.
     *
     * @param sourceName  source of the request (used for logging)
     * @param nameParameterName  name of the parameter that passed the name.
     * @param category  category of TypeDef
     * @param methodName  method receiving the call
     * @throws InvalidParameterException no name provided
     */
    void validateTypeDefCategory(String          sourceName,
                                 String          nameParameterName,
                                 TypeDefCategory category,
                                 String          methodName) throws InvalidParameterException;


    /**
     * Validate that a AttributeTypeDef's category is not null.
     *
     * @param sourceName  source of the request (used for logging)
     * @param nameParameterName  name of the parameter that passed the name.
     * @param category  category of TypeDef
     * @param methodName  method receiving the call
     * @throws InvalidParameterException  no name provided
     */
    void validateAttributeTypeDefCategory(String                   sourceName,
                                          String                   nameParameterName,
                                          AttributeTypeDefCategory category,
                                          String                   methodName) throws InvalidParameterException;


    /**
     * Validate the content of a new TypeDef is valid.
     *
     * @param sourceName  source of the request (used for logging)
     * @param parameterName  name of the parameter that passed the typeDef.
     * @param typeDef  unique identifier for a type or an instance passed on the request
     * @param methodName  method receiving the call
     * @throws InvalidParameterException  no typeDef provided
     * @throws InvalidTypeDefException  invalid typeDef provided
     */
    void validateTypeDef(String  sourceName,
                         String  parameterName,
                         TypeDef typeDef,
                         String  methodName) throws InvalidParameterException,
                                                    InvalidTypeDefException;

    /**
     * Validate that the supplied type is a valid active type.
     *
     * @param sourceName  source of the request (used for logging)
     * @param typeParameterName  the name of the parameter that passed the type
     * @param typeDefSummary  the type to test
     * @param category  the expected category of the type
     * @param methodName  the name of the method that supplied the type
     * @throws InvalidParameterException  the type is null or contains invalid values
     * @throws TypeErrorException  the type is not active
     */
    void validateActiveType(String          sourceName,
                            String          typeParameterName,
                            TypeDefSummary  typeDefSummary,
                            TypeDefCategory category,
                            String          methodName) throws TypeErrorException, InvalidParameterException;



    /**
     * Validate the content of a new TypeDef is known.
     *
     * @param sourceName  source of the request (used for logging)
     * @param parameterName  name of the parameter that passed the typeDef.
     * @param typeDef  unique identifier for a type or an instance passed on the request
     * @param methodName  method receiving the call
     * @throws TypeDefNotKnownException  no recognized typeDef provided
     */
    void validateKnownTypeDef(String  sourceName,
                              String  parameterName,
                              TypeDef typeDef,
                              String  methodName) throws TypeDefNotKnownException;


    /**
     * Validate the content of a new TypeDef is known.
     *
     * @param sourceName  source of the request (used for logging)
     * @param parameterName  name of the parameter that passed the typeDef.
     * @param typeDef  unique identifier for a type or an instance passed on the request
     * @param methodName  method receiving the call
     * @throws TypeDefKnownException  the TypeDef is already defined
     * @throws TypeDefConflictException  the TypeDef is already defined but differently
     */
    void validateUnknownTypeDef(String  sourceName,
                                String  parameterName,
                                TypeDef typeDef,
                                String  methodName) throws TypeDefKnownException,
                                                           TypeDefConflictException;



    /**
     * Validate the content of a new TypeDef is known.
     *
     * @param sourceName  source of the request (used for logging)
     * @param parameterName  name of the parameter that passed the typeDef.
     * @param attributeTypeDef  unique identifier for an attribute type or an instance passed on the request
     * @param methodName  method receiving the call
     * @throws TypeDefKnownException  the TypeDef is already defined
     * @throws TypeDefConflictException  the TypeDef is already defined but differently
     */
    void validateUnknownAttributeTypeDef(String           sourceName,
                                         String           parameterName,
                                         AttributeTypeDef attributeTypeDef,
                                         String           methodName) throws TypeDefKnownException,
                                                                             TypeDefConflictException;


    /**
     * Validate the content of a TypeDef associated with a metadata instance.
     *
     * @param sourceName  source of the request (used for logging)
     * @param parameterName  name of the parameter that passed the typeDef.
     * @param typeDef  unique identifier for a type or an instance passed on the request
     * @param methodName  method receiving the call
     * @throws TypeErrorException  no typeDef provided
     * @throws RepositoryErrorException  the TypeDef from the repository is in error.
     */
    void validateTypeDefForInstance(String  sourceName,
                                    String  parameterName,
                                    TypeDef typeDef,
                                    String  methodName) throws TypeErrorException,
                                                               RepositoryErrorException;


    /**
     * Validate that the supplied TypeDef GUID and name matches the type associated with a metadata instance.
     *
     * @param sourceName  source of the request (used for logging)
     * @param typeDefGUID  the supplied typeDef GUID.
     * @param typeDefName  the supplied typeDef name.
     * @param instance  instance retrieved from the store with the supplied instance guid
     * @param methodName  method making this call
     * @throws InvalidParameterException incompatibility detected between the TypeDef and the instance's type
     * @throws RepositoryErrorException the instance from the repository is in error.
     */
    void validateTypeForInstanceDelete(String         sourceName,
                                       String         typeDefGUID,
                                       String         typeDefName,
                                       InstanceHeader instance,
                                       String         methodName) throws InvalidParameterException,
                                                                         RepositoryErrorException;


    /**
     * Validate the content of a new AttributeTypeDef.
     *
     * @param sourceName  source of the request (used for logging)
     * @param parameterName  name of the parameter that passed the attributeTypeDef.
     * @param attributeTypeDef  unique identifier for a type or an instance passed on the request
     * @param methodName  method receiving the call
     * @throws InvalidParameterException  no attributeTypeDef provided
     * @throws InvalidTypeDefException  bad attributeTypeDef provided
     */
    void validateAttributeTypeDef(String           sourceName,
                                  String           parameterName,
                                  AttributeTypeDef attributeTypeDef,
                                  String           methodName) throws InvalidParameterException,
                                                                      InvalidTypeDefException;


    /**
     * Validate that type's name is not null.
     *
     * @param sourceName  source of the request (used for logging)
     * @param parameterName  name of the parameter that passed the name.
     * @param gallery  typeDef gallery
     * @param methodName  method receiving the call
     * @throws InvalidParameterException  no name provided
     */
    void validateTypeDefGallery(String         sourceName,
                                String         parameterName,
                                TypeDefGallery gallery,
                                String         methodName) throws InvalidParameterException;


    /**
     * Validate that the type's name is not null.
     *
     * @param sourceName  source of the request (used for logging)
     * @param standard  name of the standard null means any.
     * @param organization  name of the organization null means any.
     * @param identifier  identifier of the element in the standard null means any.
     * @param methodName  method receiving the call
     * @throws InvalidParameterException  no name provided
     */
    void validateExternalId(String sourceName,
                            String standard,
                            String organization,
                            String identifier,
                            String methodName) throws InvalidParameterException;


    /**
     * Validate that an entity's identifier is not null.
     *
     * @param sourceName  source of the request (used for logging)
     * @param guidParameterName  name of the parameter that passed the guid.
     * @param guid  unique identifier for a type or an instance passed on the request
     * @param methodName  method receiving the call
     * @throws InvalidParameterException  no guid provided
     */
    void validateGUID(String sourceName,
                      String guidParameterName,
                      String guid,
                      String methodName) throws InvalidParameterException;


    /**
     * Validate that a home metadata collection identifier is not null.
     *
     * @param sourceName  source of the request (used for logging)
     * @param guidParameterName  name of the parameter that passed the guid.
     * @param guid  unique identifier for a type or an instance passed on the request
     * @param methodName  method receiving the call
     * @throws InvalidParameterException no guid provided
     */
    void validateHomeMetadataGUID(String sourceName,
                                  String guidParameterName,
                                  String guid,
                                  String methodName) throws InvalidParameterException;


    /**
     * Validate that a home metadata collection identifier in an instance is not null.
     *
     * @param sourceName  source of the request (used for logging)
     * @param instance  instance to test.
     * @param methodName  method receiving the call
     * @throws RepositoryErrorException  no guid provided
     */
    void validateHomeMetadataGUID(String         sourceName,
                                  InstanceHeader instance,
                                  String         methodName) throws RepositoryErrorException;


    /**
     * Validate that a home metadata collection identifier in an classification is not null.
     *
     * @param sourceName source of the request (used for logging)
     * @param classification classification to test.
     * @param methodName method receiving the call
     * @throws RepositoryErrorException no guid provided
     */
    void validateHomeMetadataGUID(String           sourceName,
                                  Classification   classification,
                                  String           methodName) throws RepositoryErrorException;

    /**
     * Validate that the asOfTime parameter is not for the future.
     *
     * @param sourceName  source of the request (used for logging)
     * @param parameterName  name of the parameter that passed the guid.
     * @param asOfTime  unique name for a classification type
     * @param methodName  method receiving the call
     * @throws InvalidParameterException  asOfTime is for the future
     */
    void validateAsOfTime(String sourceName,
                          String parameterName,
                          Date   asOfTime,
                          String methodName) throws InvalidParameterException;


    /**
     * Validate that the asOfTime parameter is not null or for the future.
     *
     * @param sourceName source of the request (used for logging)
     * @param parameterName name of the parameter that passed the guid.
     * @param asOfTime unique name for a classification type
     * @param methodName method receiving the call
     * @throws InvalidParameterException asOfTime is for the future
     */
    void validateAsOfTimeNotNull(String sourceName,
                                 String parameterName,
                                 Date   asOfTime,
                                 String methodName) throws InvalidParameterException;


    /**
     * Validate that the time parameters are not inverted ('from' later than 'to').
     *
     * @param sourceName source of the request (used for logging)
     * @param parameterName name of the parameter that passed the guid.
     * @param fromTime the earliest point in time from which to retrieve historical versions of the instance (inclusive)
     * @param toTime the latest point in time from which to retrieve historical versions of the instance (exclusive)
     * @param methodName method receiving the call
     * @throws InvalidParameterException 'fromTime' is later than 'toTime'
     */
    void validateDateRange(String sourceName,
                           String parameterName,
                           Date   fromTime,
                           Date   toTime,
                           String methodName) throws InvalidParameterException;


    /**
     * Validate that a page size parameter is not negative.
     *
     * @param sourceName  source of the request (used for logging)
     * @param parameterName  name of the parameter that passed the guid.
     * @param pageSize  number of elements to return on a request
     * @param methodName  method receiving the call
     * @throws PagingErrorException  pageSize is negative
     */
    void validatePageSize(String sourceName,
                          String parameterName,
                          int    pageSize,
                          String methodName) throws PagingErrorException;


    /**
     * Validate that a classification name is not null.
     *
     * @param sourceName  source of the request (used for logging)
     * @param parameterName  name of the parameter that passed the guid.
     * @param classificationName  unique name for a classification type
     * @param methodName  method receiving the call
     * @return type definition for the classification
     * @throws InvalidParameterException  classification name is null or invalid
     */
    TypeDef validateClassificationName(String sourceName,
                                       String parameterName,
                                       String classificationName,
                                       String methodName) throws InvalidParameterException;


    /**
     * Validate that a classification is valid for the entity.
     *
     * @param sourceName  source of the request (used for logging)
     * @param classificationName  unique name for a classification type
     * @param propertiesParameterName  name of the parameter that passed the properties.
     * @param classificationProperties  properties to test
     * @param methodName  method receiving the call
     * @throws PropertyErrorException  classification name is null
     */
    void validateClassificationProperties(String             sourceName,
                                          String             classificationName,
                                          String             propertiesParameterName,
                                          InstanceProperties classificationProperties,
                                          String             methodName) throws PropertyErrorException;


    /**
     * Validate that a classification is valid for the entity.
     *
     * @param sourceName  source of the request (used for logging)
     * @param classificationParameterName  name of the parameter that passed the guid.
     * @param classificationName  unique name for a classification type
     * @param entityTypeName  name of entity type
     * @param methodName  method receiving the call
     * @throws InvalidParameterException  classification name is null
     * @throws ClassificationErrorException  the classification is invalid for this entity
     */
    void validateClassification(String sourceName,
                                String classificationParameterName,
                                String classificationName,
                                String entityTypeName,
                                String methodName) throws InvalidParameterException,
                                                          ClassificationErrorException;


    /**
     * Validate that a classification is valid for the entity.
     *
     * @param sourceName  source of the request (used for logging)
     * @param parameterName  name of the parameter that passed the guid.
     * @param classifications  list of classifications
     * @param entityTypeName  name of entity type
     * @param methodName  method receiving the call
     * @throws InvalidParameterException  classification name is null
     * @throws ClassificationErrorException  the classification is invalid for this entity
     * @throws PropertyErrorException  the classification's properties are invalid for its type
     * @throws TypeErrorException  the classification's type is invalid
     */
    void validateClassificationList(String               sourceName,
                                    String               parameterName,
                                    List<Classification> classifications,
                                    String               entityTypeName,
                                    String               methodName) throws InvalidParameterException,
                                                                            ClassificationErrorException,
                                                                            PropertyErrorException,
                                                                            TypeErrorException;


    /**
     * Validate that a TypeDef match criteria set of properties is not null.
     *
     * @param sourceName  source of the request (used for logging)
     * @param parameterName  name of the parameter that passed the match criteria.
     * @param matchCriteria  match criteria properties
     * @param methodName  method receiving the call
     * @throws InvalidParameterException  no guid provided
     */
    void validateMatchCriteria(String            sourceName,
                               String            parameterName,
                               TypeDefProperties matchCriteria,
                               String            methodName) throws InvalidParameterException;


    /**
     * Validate that a metadata instance match criteria and set of properties are either both null or
     * both not null.
     *
     * @param sourceName  source of the request (used for logging)
     * @param matchCriteriaParameterName  name of the parameter that passed the match criteria.
     * @param matchPropertiesParameterName  name of the parameter that passed the match criteria.
     * @param matchCriteria  match criteria enum
     * @param matchProperties  match properties
     * @param methodName  method receiving the call
     * @throws InvalidParameterException  no guid provided
     */
    void validateMatchCriteria(String             sourceName,
                               String             matchCriteriaParameterName,
                               String             matchPropertiesParameterName,
                               MatchCriteria      matchCriteria,
                               InstanceProperties matchProperties,
                               String             methodName) throws InvalidParameterException;


    /**
     * Validate that a search criteria  is not null.
     *
     * @param sourceName  source of the request (used for logging)
     * @param parameterName  name of the parameter that passed the search criteria.
     * @param searchCriteria  match criteria properties
     * @param methodName  method receiving the call
     * @throws InvalidParameterException  no guid provided
     */
    void validateSearchCriteria(String sourceName,
                                String parameterName,
                                String searchCriteria,
                                String methodName) throws InvalidParameterException;


    /**
     * Validate the property-based search conditions.
     *
     * @param sourceName  source of the request (used for logging)
     * @param parameterName  name of the parameter that passed the property-based conditions
     * @param matchProperties  property-based conditions
     * @param methodName  method receiving the call
     * @throws InvalidParameterException  property-based conditions are invalid
     */
    void validateSearchProperties(String sourceName,
                                  String parameterName,
                                  SearchProperties matchProperties,
                                  String methodName) throws InvalidParameterException;


    /**
     * Validate the classification-based search conditions.
     *
     * @param sourceName  source of the request (used for logging)
     * @param parameterName  name of the parameter that passed the classification-based conditions
     * @param matchClassifications  classification-based conditions
     * @param methodName  method receiving the call
     * @throws InvalidParameterException  classification-based conditions are invalid
     */
    void validateSearchClassifications(String sourceName,
                                       String parameterName,
                                       SearchClassifications matchClassifications,
                                       String methodName) throws InvalidParameterException;


    /**
     * Validate that the properties for a metadata instance match its TypeDef.
     *
     * @param sourceName  source of the request (used for logging)
     * @param parameterName  name of the properties parameter.
     * @param typeDef  type information to validate against.
     * @param properties  proposed properties for instance.
     * @param methodName  method receiving the call.
     * @throws PropertyErrorException  invalid property
     */
    void validatePropertiesForType(String             sourceName,
                                   String             parameterName,
                                   TypeDef            typeDef,
                                   InstanceProperties properties,
                                   String             methodName) throws PropertyErrorException;



    /**
     * Validate that the properties for a metadata instance match its TypeDef
     *
     * @param sourceName  source of the request (used for logging)
     * @param parameterName  name of the properties parameter.
     * @param typeDefSummary  type information to validate against.
     * @param properties  proposed properties
     * @param methodName  method receiving the call
     * @throws TypeErrorException  no typeDef provided
     * @throws PropertyErrorException  invalid property
     */
    void validatePropertiesForType(String             sourceName,
                                   String             parameterName,
                                   TypeDefSummary     typeDefSummary,
                                   InstanceProperties properties,
                                   String             methodName) throws PropertyErrorException,
                                                                         TypeErrorException;


    /**
     * Validate that the properties for a metadata instance match its TypeDef
     *
     * @param sourceName  source of the request (used for logging)
     * @param parameterName  name of the properties parameter.
     * @param typeDef  type information to validate against.
     * @param properties  proposed properties
     * @param methodName  method receiving the call
     * @throws PropertyErrorException  invalid property
     */
    void validateNewPropertiesForType(String             sourceName,
                                      String             parameterName,
                                      TypeDef            typeDef,
                                      InstanceProperties properties,
                                      String             methodName) throws PropertyErrorException;



    /**
     * Verify whether the instance passed to this method is of the type indicated by the type guid.
     * A null type guid matches all instances (ie result is true).  A null instance returns false.
     *
     * @param sourceName  name of caller.
     * @param instanceTypeGUID  unique identifier of the type (or null).
     * @param instance  instance to test.
     * @return boolean
     */
    boolean verifyInstanceType(String         sourceName,
                               String         instanceTypeGUID,
                               InstanceHeader instance);


    /**
     * Verify whether the instance passed to this method is of the type indicated by the type guid and restricted by
     * the list of subtype guids.
     * A null type guid matches all instances (ie result is true).  A null instance returns false.
     *
     * @param sourceName  name of caller.
     * @param instanceTypeGUID  unique identifier of the type (or null).
     * @param subtypeGUIDs  list of unique identifiers of the subtypes to include (or null).
     * @param instance  instance to test.
     * @return boolean
     */
    boolean verifyInstanceType(String         sourceName,
                               String         instanceTypeGUID,
                               List<String>   subtypeGUIDs,
                               InstanceHeader instance);



    /**
     * Verify that an entity has been successfully retrieved from the repository and has valid contents.
     *
     * @param sourceName  source of the request (used for logging)
     * @param guid  unique identifier used to retrieve the entity
     * @param entity  the retrieved entity (or null)
     * @param methodName  method receiving the call
     * @throws EntityNotKnownException  No entity found
     * @throws RepositoryErrorException  logic error in the repository corrupted instance
     */
    void validateEntityFromStore(String        sourceName,
                                 String        guid,
                                 EntitySummary entity,
                                 String        methodName) throws RepositoryErrorException,
                                                                  EntityNotKnownException;


    /**
     * Verify that an entity has been successfully retrieved from the repository and has valid contents.
     *
     * @param sourceName  source of the request (used for logging)
     * @param guid  unique identifier used to retrieve the entity
     * @param entity  the retrieved entity (or null)
     * @param methodName  method receiving the call
     * @throws EntityNotKnownException  No entity found
     * @throws RepositoryErrorException  logic error in the repository corrupted instance
     */
    void validateEntityFromStore(String       sourceName,
                                 String       guid,
                                 EntityDetail entity,
                                 String       methodName) throws RepositoryErrorException,
                                                                 EntityNotKnownException;


    /**
     * Verify that a relationship has been successfully retrieved from the repository and has valid contents.
     *
     * @param sourceName  source of the request (used for logging)
     * @param guid  unique identifier used to retrieve the entity
     * @param relationship  the retrieved relationship (or null)
     * @param methodName  method receiving the call
     * @throws RelationshipNotKnownException  No relationship found
     * @throws RepositoryErrorException  logic error in the repository corrupted instance
     */
    void validateRelationshipFromStore(String       sourceName,
                                       String       guid,
                                       Relationship relationship,
                                       String       methodName) throws RepositoryErrorException,
                                                                       RelationshipNotKnownException;


    /**
     * Verify that the instance retrieved from the repository has a valid instance type.
     *
     * @param sourceName  source of the request (used for logging)
     * @param instance  the retrieved instance
     * @throws RepositoryErrorException  logic error in the repository corrupted instance
     */
    void validateInstanceType(String         sourceName,
                              InstanceHeader instance) throws RepositoryErrorException;



    /**
     * Verify that the instance retrieved from the repository has a valid instance type that matches the
     * expected type.
     *
     * @param sourceName  source of the request (used for logging)
     * @param instance  the retrieved instance
     * @param typeGUIDParameterName  name of parameter for TypeDefGUID
     * @param typeNameParameterName  name of parameter for TypeDefName
     * @param expectedTypeGUID  expected GUID of InstanceType
     * @param expectedTypeName  expected name of InstanceType
     * @throws RepositoryErrorException  logic error in the repository corrupted instance
     * @throws TypeErrorException  problem with type
     * @throws InvalidParameterException  invalid parameter
     */
    void validateInstanceType(String         sourceName,
                              InstanceHeader instance,
                              String         typeGUIDParameterName,
                              String         typeNameParameterName,
                              String         expectedTypeGUID,
                              String         expectedTypeName) throws RepositoryErrorException,
                                                                      TypeErrorException,
                                                                      InvalidParameterException;


    /**
     * Verify that the supplied instance is in one of the supplied statuses. Note that if the supplied statuses are
     * null, then only statuses that are not DELETE are considered valid.
     *
     * @param validStatuses  list of statuses the instance should be in any one of them
     * @param instance  instance to test
     * @return boolean result
     */
    boolean verifyInstanceHasRightStatus(List<InstanceStatus> validStatuses,
                                         InstanceHeader       instance);


    /**
     * Validates an instance status where null is permissible.
     *
     * @param sourceName  source of the request (used for logging)
     * @param instanceStatusParameterName  name of the initial status parameter
     * @param instanceStatus  initial status value
     * @param typeDef  type of the instance
     * @param methodName  method called
     * @throws StatusNotSupportedException  the initial status is invalid for this type
     */
    void validateInstanceStatus(String         sourceName,
                                String         instanceStatusParameterName,
                                InstanceStatus instanceStatus,
                                TypeDef        typeDef,
                                String         methodName) throws StatusNotSupportedException;



    /**
     * Validates an instance status where null is not allowed.
     *
     * @param sourceName  source of the request (used for logging)
     * @param instanceStatusParameterName  name of the initial status parameter
     * @param instanceStatus  initial status value
     * @param typeDef  type of the instance
     * @param methodName  method called
     * @throws StatusNotSupportedException  the initial status is invalid for this type
     * @throws InvalidParameterException  invalid parameter
     */
    void validateNewStatus(String         sourceName,
                           String         instanceStatusParameterName,
                           InstanceStatus instanceStatus,
                           TypeDef        typeDef,
                           String         methodName) throws StatusNotSupportedException,
                                                             InvalidParameterException;


    /**
     * Verify that an instance is not already deleted since the repository is processing a delete request
     * and it does not want to look stupid.
     *
     * @param sourceName  source of the request (used for logging)
     * @param instance  instance about to be deleted
     * @param methodName  name of method called
     * @throws InvalidParameterException  the instance is already deleted
     */
    void validateInstanceStatusForDelete(String         sourceName,
                                         InstanceHeader instance,
                                         String         methodName) throws InvalidParameterException;


    /**
     * Verify the status of an entity to check it has not been deleted.  This method is used
     * when retrieving metadata instances from a store that supports soft delete.
     *
     * @param sourceName  source of the request (used for logging)
     * @param instance  instance to validate
     * @param methodName  name of calling method
     * @throws EntityNotKnownException  the entity is in deleted status
     */
    void validateEntityIsNotDeleted(String         sourceName,
                                    InstanceHeader instance,
                                    String         methodName) throws EntityNotKnownException;


    /**
     * Verify the status of an entity to check it has been deleted.
     *
     * @param sourceName  source of the request (used for logging)
     * @param instance  instance to validate
     * @param methodName  name of calling method
     * @throws EntityNotDeletedException  the entity is not in deleted status
     */
    void validateEntityIsDeleted(String         sourceName,
                                 InstanceHeader instance,
                                 String         methodName) throws EntityNotDeletedException;


    /**
     * Verify that an entity instance can be updated by the metadataCollection. This method is used
     * when the metadataCollection is called to update the status properties or classification of an
     * entity instance.
     *
     * @param sourceName  source of the request (used for logging)
     * @param metadataCollectionId unique identifier for the metadata collection
     * @param instance  instance to validate
     * @param methodName  name of calling method
     * @throws InvalidParameterException  the entity is in deleted status
     */
    void validateEntityCanBeUpdated(String         sourceName,
                                    String         metadataCollectionId,
                                    InstanceHeader instance,
                                    String         methodName) throws InvalidParameterException;


    /**
     * Verify that an entity instance can be rehomed by the metadataCollection. This method is used
     * when the metadataCollection is called to rehome an entity instance.
     *
     * @param sourceName  source of the request (used for logging)
     * @param metadataCollectionId unique identifier for the metadata collection
     * @param instance  instance to validate
     * @param methodName  name of calling method
     * @throws InvalidParameterException  the entity is in deleted status
     */
    void validateEntityCanBeRehomed(String         sourceName,
                                    String         metadataCollectionId,
                                    InstanceHeader instance,
                                    String         methodName) throws InvalidParameterException;


    /**
     * Verify the status of a relationship to check it has not been deleted.  This method is used
     * when retrieving metadata instances from a store that supports soft delete.
     *
     * @param sourceName  source of the request (used for logging)
     * @param instance  instance to test
     * @param methodName  name of calling method
     * @throws RelationshipNotKnownException  the relationship is in deleted status
     */
    void validateRelationshipIsNotDeleted(String         sourceName,
                                          InstanceHeader instance,
                                          String         methodName) throws RelationshipNotKnownException;


    /**
     * Verify the status of a relationship to check it has been deleted.
     *
     * @param sourceName  source of the request (used for logging)
     * @param instance  instance to test
     * @param methodName  name of calling method
     * @throws RelationshipNotDeletedException  the relationship is not in deleted status
     */
    void validateRelationshipIsDeleted(String         sourceName,
                                       InstanceHeader instance,
                                       String         methodName) throws RelationshipNotDeletedException;


    /**
     * Verify that a relationship instance can be updated by the metadataCollection. This method is used
     * when the metadataCollection is called to update the status or properties of a relationship instance.
     *
     * @param sourceName  source of the request (used for logging)
     * @param metadataCollectionId unique identifier for the metadata collection
     * @param instance  instance to validate
     * @param methodName  name of calling method
     * @throws InvalidParameterException  the entity is in deleted status
     */
    void validateRelationshipCanBeUpdated(String         sourceName,
                                          String         metadataCollectionId,
                                          InstanceHeader instance,
                                          String         methodName) throws InvalidParameterException;


    /**
     * Verify that a relationship instance can be rehomed by the metadataCollection. This method is used
     * when the metadataCollection is called to rehome a relationship instance.
     *
     * @param sourceName  source of the request (used for logging)
     * @param metadataCollectionId unique identifier for the metadata collection
     * @param instance  instance to validate
     * @param methodName  name of calling method
     * @throws InvalidParameterException  the entity is in deleted status
     */
    void validateRelationshipCanBeRehomed(String         sourceName,
                                          String         metadataCollectionId,
                                          InstanceHeader instance,
                                          String         methodName) throws InvalidParameterException;



    /**
     * Validate that the types of the two ends of a relationship match the relationship's TypeDef.
     *
     * @param sourceName  source of the request (used for logging)
     * @param entityOneProxy  content of end one
     * @param entityTwoProxy  content of end two
     * @param typeDef  typeDef for the relationship
     * @param methodName  name of the method making the request
     * @throws InvalidParameterException  types do not align
     */
    void validateRelationshipEnds(String      sourceName,
                                  EntityProxy entityOneProxy,
                                  EntityProxy entityTwoProxy,
                                  TypeDef     typeDef,
                                  String      methodName) throws InvalidParameterException;


    /**
     * Return a boolean indicating whether the supplied entity is classified with one or more of the supplied
     * classifications.
     *
     * @param requiredClassifications  list of required classification null means that there are no specific
     *                                classification requirements and so results in a true response.
     * @param entity  entity to test.
     * @return boolean result
     */
    boolean verifyEntityIsClassified(List<String>  requiredClassifications,
                                     EntitySummary entity);


    /**
     * Count the number of matching property values that an instance has.  They may come from an entity,
     * classification or relationship.
     *
     * @param matchProperties  the properties to match.
     * @param instanceProperties  the properties from the instance.
     * @return integer count of the matching properties.
     * @throws InvalidParameterException invalid search criteria
     */
    int countMatchingPropertyValues(InstanceProperties matchProperties,
                                    InstanceProperties instanceProperties) throws InvalidParameterException;


    /**
     * Count the number of matching property values that an instance has.  They may come from an entity,
     * or relationship.
     *
     * @param matchProperties  the properties to match.
     * @param instanceHeader  the header properties from the instance.
     * @param instanceProperties  the effectivity dates.
     * @return integer count of the matching properties.
     * @throws InvalidParameterException invalid search criteria
     */
    int countMatchingHeaderPropertyValues(InstanceProperties  matchProperties,
                                          InstanceAuditHeader instanceHeader,
                                          InstanceProperties  instanceProperties) throws InvalidParameterException;


    /**
     * Determine if the instance properties match the match criteria.
     *
     * @param matchProperties  the properties to match.
     * @param instanceHeader the header of the instance.
     * @param instanceProperties  the properties from the instance.
     * @param matchCriteria  rule on how the match should occur.
     * @return boolean flag indicating whether the two sets of properties match
     * @throws InvalidParameterException invalid search criteria
     */
    boolean verifyMatchingInstancePropertyValues(InstanceProperties  matchProperties,
                                                 InstanceAuditHeader instanceHeader,
                                                 InstanceProperties  instanceProperties,
                                                 MatchCriteria       matchCriteria) throws InvalidParameterException;


    /**
     * Retrieve a numeric representation of the provided value, or null if it cannot be converted to a number.
     *
     * @param value to convert
     * @return BigDecimal
     */
    BigDecimal getNumericRepresentation(InstancePropertyValue value);


    /**
     * Determine if the instance properties match the property-based conditions.
     *
     * @param matchProperties  the property-based conditions to match.
     * @param instanceHeader the header of the instance.
     * @param instanceProperties  the properties from the instance.
     * @return boolean flag indicating whether the two sets of properties match
     * @throws InvalidParameterException invalid search criteria
     */
    boolean verifyMatchingInstancePropertyValues(SearchProperties    matchProperties,
                                                 InstanceAuditHeader instanceHeader,
                                                 InstanceProperties  instanceProperties) throws InvalidParameterException;


    /**
     * Determine if the instance properties match the classification-based conditions.
     *
     * @param matchClassifications  the classification-based conditions to match.
     * @param entity  the entity instance.
     * @return boolean flag indicating whether the classifications match
     * @throws InvalidParameterException invalid search criteria
     */
    boolean verifyMatchingClassifications(SearchClassifications matchClassifications,
                                          EntitySummary         entity) throws InvalidParameterException;


    /**
     * Validates that an instance has the correct header for it to be a reference copy.
     *
     * @param sourceName  source of the request (used for logging)
     * @param localMetadataCollectionId   the unique identifier for the local repository' metadata collection.
     * @param instanceParameterName  the name of the parameter that provided the instance.
     * @param instance  the instance to test
     * @param methodName  the name of the method that supplied the instance.
     * @throws RepositoryErrorException  problem with repository
     * @throws InvalidParameterException  the instance is null or linked to local metadata repository
     */
    void validateReferenceInstanceHeader(String         sourceName,
                                         String         localMetadataCollectionId,
                                         String         instanceParameterName,
                                         InstanceHeader instance,
                                         String         methodName) throws InvalidParameterException,
                                                                           RepositoryErrorException;


    /**
     * Validates that an instance has the correct header for it to be a reference copy.
     *
     * @param sourceName  source of the request (used for logging)
     * @param localMetadataCollectionId   the unique identifier for the local repository' metadata collection.
     * @param instanceParameterName  the name of the parameter that provided the instance.
     * @param instance  the instance to test
     * @param auditLog optional logging destination
     * @param methodName  the name of the method that supplied the instance.
     * @throws RepositoryErrorException  problem with repository
     * @throws InvalidParameterException  the instance is null or linked to local metadata repository
     */
    void validateReferenceInstanceHeader(String         sourceName,
                                         String         localMetadataCollectionId,
                                         String         instanceParameterName,
                                         InstanceHeader instance,
                                         AuditLog       auditLog,
                                         String         methodName) throws InvalidParameterException,
                                                                           RepositoryErrorException;


    /**
     * Validates an entity proxy.  It must be a reference copy (ie owned by a different repository).
     *
     * @param sourceName  source of the request (used for logging)
     * @param localMetadataCollectionId  unique identifier for this repository's metadata collection
     * @param proxyParameterName  name of the parameter used to provide the parameter
     * @param entityProxy  proxy to add
     * @param methodName  name of the method that adds the proxy
     * @throws InvalidParameterException the entity proxy is null or for an entity homed in this repository
     */
    void validateEntityProxy(String      sourceName,
                             String      localMetadataCollectionId,
                             String      proxyParameterName,
                             EntityProxy entityProxy,
                             String      methodName) throws InvalidParameterException;


    /**
     * Search for property values matching the search criteria (a regular expression)
     *
     * @param sourceName  source of the request (used for logging)
     * @param properties  list of properties associated with the in instance
     * @param searchCriteria  regular expression for testing the property values
     * @param methodName  name of the method requiring the search.
     * @return boolean indicating whether the search criteria is located in any of the string parameter values.
     * @throws RepositoryErrorException  the properties are not properly set up in the instance
     */
    boolean verifyInstancePropertiesMatchSearchCriteria(String             sourceName,
                                                        InstanceProperties properties,
                                                        String             searchCriteria,
                                                        String             methodName) throws RepositoryErrorException;


    /**
     * Search for property values matching the supplied property value
     *
     * @param sourceName source of the request (used for logging)
     * @param properties list of properties associated with the in instance
     * @param searchPropertyValue property value as a string
     * @param methodName name of the method requiring the search.
     * @return boolean indicating whether the search criteria is located in any of the string parameter values.
     * @throws RepositoryErrorException the properties are not properly set up in the instance
     */
    boolean verifyInstancePropertiesMatchPropertyValue(String             sourceName,
                                                       InstanceProperties properties,
                                                       String             searchPropertyValue,
                                                       String             methodName) throws RepositoryErrorException;


    /**
     * Return the string form of a property value.  Can be used as propertyValue on find property
     * value calls.
     *
     * @param instancePropertyValue value to extract the string from
     * @return extracted string value.
     */
    String getStringFromPropertyValue(InstancePropertyValue instancePropertyValue);


    /**
     * Return the string form of the instance properties.  Can be used as propertyValue on find property
     * value calls.
     *
     * @param instanceProperties value to extract the string from
     * @return extracted string value.
     */
    String getStringValuesFromInstancePropertiesAsArray(InstanceProperties instanceProperties);


    /**
     * Return the string form of the instance properties.  Can be used as propertyValue on find property
     * value calls.
     *
     * @param instanceProperties value to extract the string from
     * @return extracted string value.
     */
    String getStringValuesFromInstancePropertiesAsMap(InstanceProperties instanceProperties);


    /**
     * Return the string form of the instance properties.  Can be used as propertyValue on find property
     * value calls.
     *
     * @param instanceProperties value to extract the string from
     * @return extracted string value.
     */
    String getStringValuesFromInstancePropertiesAsStruct(InstanceProperties instanceProperties);


    /**
     * Returns a boolean indicating that the instance is of the supplied type.  It tests the
     * base type and all the super types.
     *
     * @param sourceName  source of the request (used for logging)
     * @param instance  instance to test
     * @param typeName  name of the type
     * @param localMethodName  local method that is calling isATypeOf
     * @return true if typeName is instance type or a supertype of it, otherwise false
     */
    boolean isATypeOf(String              sourceName,
                      InstanceAuditHeader instance,
                      String              typeName,
                      String              localMethodName);

    /*
     * ===========================================
     * Validation of results from the repository
     * ===========================================
     */


    /**
     * Validate that either zero or one entities were returned from a find request.  This is typically when searching
     * for entities of a specific type using one of its unique properties.
     *
     * @param findResults list of entities returned from the search.
     * @param typeName name of the type of entities requested.
     * @param serviceName service that requested the entities.
     * @param methodName calling method.
     * @throws RepositoryErrorException results are not as expected
     */
    void validateAtMostOneEntityResult(List<EntityDetail> findResults,
                                       String             typeName,
                                       String             serviceName,
                                       String             methodName) throws RepositoryErrorException;


    /**
     * Validate that either zero or one relationships were returned from a find request.  This is typically when searching
     * for relationships of a specific type where the cardinality is set to AT_MOST_ONE in the RelationshipEndCardinality.
     *
     * @param findResults list of relationships returned from the search.
     * @param typeName name of the type of relationships requested.
     * @param serviceName service that requested the relationships.
     * @param methodName calling method.
     * @throws RepositoryErrorException results are not as expected
     */
    void validateAtMostOneRelationshipResult(List<Relationship> findResults,
                                             String             typeName,
                                             String             serviceName,
                                             String             methodName) throws RepositoryErrorException;
}
