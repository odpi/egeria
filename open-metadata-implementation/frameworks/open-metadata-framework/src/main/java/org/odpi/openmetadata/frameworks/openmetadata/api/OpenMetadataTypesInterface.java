/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.api;

import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;

import java.util.List;

/**
 * OpenMetadataTypesInterface enables callers to query the defined open metadata types, and to maintain the
 * types that are defined through the API.
 */
public interface OpenMetadataTypesInterface
{
    /**
     * Returns the list of different types of metadata organized into two groups.  The first are the
     * attribute type definitions (AttributeTypeDefs).  These provide types for properties in full
     * type definitions.  Full type definitions (TypeDefs) describe types for entities, relationships
     * and classifications.
     *
     * @param userId  unique identifier for requesting user.
     * @param getInheritedAttributes whether to include inherited attributes in the returned TypeDefs
     * @param getRelationshipAttributes whether to include relationship attributes in the returned TypeDefs
     * @return TypeDefGallery  List of different categories of type definitions.
     * @throws InvalidParameterException the userId is null
     * @throws PropertyServerException  a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException  the userId is not permitted to perform this operation.
     */
    OpenMetadataTypeDefGallery getAllTypes(String  userId,
                                           boolean getInheritedAttributes,
                                           boolean getRelationshipAttributes) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException;


    /**
     * Returns all the TypeDefs for a specific category.
     *
     * @param userId  unique identifier for requesting user.
     * @param getInheritedAttributes whether to include inherited attributes in the returned TypeDefs
     * @param getRelationshipAttributes whether to include relationship attributes in the returned TypeDefs
     * @param category  enum value for the category of TypeDef to return.
     * @return TypeDefs list.
     * @throws InvalidParameterException  the TypeDefCategory is null.
     * @throws PropertyServerException  a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException  the userId is not permitted to perform this operation.
     */
    TypeDefList findTypeDefsByCategory(String                      userId,
                                       boolean                     getInheritedAttributes,
                                       boolean                     getRelationshipAttributes,
                                       OpenMetadataTypeDefCategory category) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException;

    /**
     * Returns all the AttributeTypeDefs for a specific category.
     *
     * @param userId  unique identifier for requesting user.
     * @param category  enum value for the category of an AttributeTypeDef to return.
     * @return AttributeTypeDefs list.
     * @throws InvalidParameterException  the TypeDefCategory is null.
     * @throws PropertyServerException  a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException  the userId is not permitted to perform this operation.
     */
    List<OpenMetadataAttributeTypeDef> getAttributeTypeDefs(String                               userId,
                                                            OpenMetadataAttributeTypeDefCategory category) throws InvalidParameterException,
                                                                                                                             PropertyServerException,
                                                                                                                             UserNotAuthorizedException;


    /**
     * Return the types that are linked to the elements from the specified standard.
     *
     * @param userId  unique identifier for requesting user.
     * @param getInheritedAttributes whether to include inherited attributes in the returned TypeDefs
     * @param getRelationshipAttributes whether to include relationship attributes in the returned TypeDefs
     * @param standard  name of the standard null means any.
     * @param organization  name of the organization null means any.
     * @param identifier  identifier of the element in the standard null means any.
     * @return TypeDefs list  each entry in the list contains a TypeDef.  This is a structure
     * describing the TypeDef's category and properties.
     * @throws InvalidParameterException  all attributes of the external id are null.
     * @throws PropertyServerException  a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException  the userId is not permitted to perform this operation.
     */
    TypeDefList findTypesByExternalId(String  userId,
                                      boolean getInheritedAttributes,
                                      boolean getRelationshipAttributes,
                                      String  standard,
                                      String  organization,
                                      String  identifier) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException;


    /**
     * Return the TypeDef identified by the GUID.
     *
     * @param userId  unique identifier for requesting user.
     * @param getInheritedAttributes whether to include inherited attributes in the returned TypeDefs
     * @param getRelationshipAttributes whether to include relationship attributes in the returned TypeDefs
     * @param guid  String unique id of the TypeDef
     * @return TypeDef structure describing its category and properties.
     * @throws InvalidParameterException  the guid is null.
     * @throws PropertyServerException  a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws UserNotAuthorizedException  the userId is not permitted to perform this operation.
     */
    OpenMetadataTypeDef getTypeDefByGUID(String  userId,
                                         boolean getInheritedAttributes,
                                         boolean getRelationshipAttributes,
                                         String  guid) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException;


    /**
     * Returns all the TypeDefs for a specific subtype.  If a null result is returned it means the
     * type has no subtypes.     *
     * @param userId       unique identifier for requesting user.
     * @param getInheritedAttributes whether to include inherited attributes in the returned TypeDefs
     * @param getRelationshipAttributes whether to include relationship attributes in the returned TypeDefs
     * @param typeName     name of the standard null means any.
     *
     * @return TypeDefs list  each entry in the list contains a TypeDef.  This is a structure
     * describing the TypeDef's category and properties.  If null is returned as the TypeDef list it means the type
     * has no known subtypes
     *
     * @throws InvalidParameterException  all attributes of the external id are null.
     * @throws PropertyServerException    a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    TypeDefList getSubTypes(String  userId,
                            boolean getInheritedAttributes,
                            boolean getRelationshipAttributes,
                            String  typeName) throws InvalidParameterException,
                                                     PropertyServerException,
                                                     UserNotAuthorizedException;


    /**
     * Return the AttributeTypeDef identified by the GUID.
     *
     * @param userId  unique identifier for requesting user.
     * @param guid  String unique id of the TypeDef
     * @return TypeDef structure describing its category and properties.
     * @throws InvalidParameterException  the guid is null.
     * @throws PropertyServerException  a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws UserNotAuthorizedException  the userId is not permitted to perform this operation.
     */
    OpenMetadataAttributeTypeDef getAttributeTypeDefByGUID(String    userId,
                                                           String    guid) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException;


    /**
     * Return the TypeDef identified by the unique name.
     *
     * @param userId  unique identifier for requesting user.
     * @param getInheritedAttributes whether to include inherited attributes in the returned TypeDefs
     * @param getRelationshipAttributes whether to include relationship attributes in the returned TypeDefs
     * @param name  String name of the TypeDef.
     * @return TypeDef structure describing its category and properties.
     * @throws InvalidParameterException  the name is null.
     * @throws PropertyServerException  a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws UserNotAuthorizedException  the userId is not permitted to perform this operation.
     */
    OpenMetadataTypeDef getTypeDefByName(String  userId,
                                         boolean getInheritedAttributes,
                                         boolean getRelationshipAttributes,
                                         String  name) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException;


    /**
     * Return the AttributeTypeDef identified by the unique name.
     *
     * @param userId unique identifier for requesting user.
     * @param name String name of the TypeDef.
     * @return TypeDef structure describing its category and properties.
     * @throws InvalidParameterException the name is null.
     * @throws PropertyServerException a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    OpenMetadataAttributeTypeDef getAttributeTypeDefByName(String    userId,
                                                           String    name) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException;


    /* =============================================================================
     * Maintaining the types defined through the API
     */


    /**
     * Add a new type definition for an entity, relationship or classification.  The type definition is homed in
     * the metadata access store's local repository: it is validated against the types already defined, stored
     * with the metadata so that it survives a restart, and announced to the other members of the cohort.
     * If no unique identifier is supplied, one is generated; if no version is supplied, it is version 1.
     *
     * @param userId unique identifier for requesting user.
     * @param newTypeDef the new type definition.
     * @return unique identifier of the new type definition
     * @throws InvalidParameterException the type definition is invalid, already defined, or conflicts with the
     *                                   types that are defined.
     * @throws PropertyServerException a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    String addTypeDef(String              userId,
                      OpenMetadataTypeDef newTypeDef) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException;


    /**
     * Update a type definition that was added through the API.  The types from the open metadata archives, and
     * from other members of the cohort, are maintained by their originators and cannot be updated this way.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefPatch the changes to make and the version they apply to.
     * @return the updated type definition
     * @throws InvalidParameterException the type definition is not known, was not added through the API, is at a
     *                                   different version, or the patch is incompatible with it.
     * @throws PropertyServerException a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    OpenMetadataTypeDef updateTypeDef(String                   userId,
                                      OpenMetadataTypeDefPatch typeDefPatch) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException;


    /**
     * Delete a type definition that was added through the API.  This is only possible while nothing uses it:
     * there must be no instances of the type, including soft-deleted ones, and no other type definition may
     * refer to it.
     *
     * @param userId unique identifier for requesting user.
     * @param typeDefGUID unique identifier of the type definition.
     * @param typeDefName unique name of the type definition.
     * @throws InvalidParameterException the type definition is not known or was not added through the API.
     * @throws PropertyServerException a problem communicating with the metadata repository, or the type
     *                                 definition is still in use.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    void deleteTypeDef(String userId,
                       String typeDefGUID,
                       String typeDefName) throws InvalidParameterException,
                                                  PropertyServerException,
                                                  UserNotAuthorizedException;


    /**
     * Add a new enum definition, which can then be used as the type of attributes in new type definitions.
     * As with addTypeDef(), the enum definition is homed in the metadata access store's local repository.
     *
     * @param userId unique identifier for requesting user.
     * @param newEnumDef the new enum definition.
     * @return unique identifier of the new enum definition
     * @throws InvalidParameterException the enum definition is invalid or already defined.
     * @throws PropertyServerException a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    String addEnumDef(String              userId,
                      OpenMetadataEnumDef newEnumDef) throws InvalidParameterException,
                                                             PropertyServerException,
                                                             UserNotAuthorizedException;


    /**
     * Delete an enum definition that was added through the API.  This is only possible while no type definition
     * has an attribute of this type.
     *
     * @param userId unique identifier for requesting user.
     * @param enumDefGUID unique identifier of the enum definition.
     * @param enumDefName unique name of the enum definition.
     * @throws InvalidParameterException the enum definition is not known or was not added through the API.
     * @throws PropertyServerException a problem communicating with the metadata repository, or the enum
     *                                 definition is still in use.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    void deleteEnumDef(String userId,
                       String enumDefGUID,
                       String enumDefName) throws InvalidParameterException,
                                                  PropertyServerException,
                                                  UserNotAuthorizedException;
}
