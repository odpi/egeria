/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.client;

import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataAttributeTypeDef;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataAttributeTypeDefCategory;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataTypeDef;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataTypeDefCategory;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataTypeDefGallery;

import java.util.List;

/**
 * OpenMetadataTypesInterface enables callers to query the defined open metadata types.
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
     * @return TypeDefGallery  List of different categories of type definitions.
     * @throws InvalidParameterException the userId is null
     * @throws PropertyServerException  there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException  the userId is not permitted to perform this operation.
     */
    OpenMetadataTypeDefGallery getAllTypes(String  userId) throws InvalidParameterException,
                                                                  PropertyServerException,
                                                                  UserNotAuthorizedException;


    /**
     * Returns all the TypeDefs for a specific category.
     *
     * @param userId  unique identifier for requesting user.
     * @param category  enum value for the category of TypeDef to return.
     * @return TypeDefs list.
     * @throws InvalidParameterException  the TypeDefCategory is null.
     * @throws PropertyServerException  there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException  the userId is not permitted to perform this operation.
     */
    List<OpenMetadataTypeDef> findTypeDefsByCategory(String                      userId,
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
     * @throws PropertyServerException  there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException  the userId is not permitted to perform this operation.
     */
    List<OpenMetadataAttributeTypeDef> findAttributeTypeDefsByCategory(String                               userId,
                                                                       OpenMetadataAttributeTypeDefCategory category) throws InvalidParameterException,
                                                                                                                             PropertyServerException,
                                                                                                                             UserNotAuthorizedException;


    /**
     * Return the types that are linked to the elements from the specified standard.
     *
     * @param userId  unique identifier for requesting user.
     * @param standard  name of the standard null means any.
     * @param organization  name of the organization null means any.
     * @param identifier  identifier of the element in the standard null means any.
     * @return TypeDefs list  each entry in the list contains a TypeDef.  This is a structure
     * describing the TypeDef's category and properties.
     * @throws InvalidParameterException  all attributes of the external id are null.
     * @throws PropertyServerException  there is a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException  the userId is not permitted to perform this operation.
     */
    List<OpenMetadataTypeDef> findTypesByExternalId(String    userId,
                                                    String    standard,
                                                    String    organization,
                                                    String    identifier) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException;


    /**
     * Return the TypeDef identified by the GUID.
     *
     * @param userId  unique identifier for requesting user.
     * @param guid  String unique id of the TypeDef
     * @return TypeDef structure describing its category and properties.
     * @throws InvalidParameterException  the guid is null.
     * @throws PropertyServerException  there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws UserNotAuthorizedException  the userId is not permitted to perform this operation.
     */
    OpenMetadataTypeDef getTypeDefByGUID(String    userId,
                                         String    guid) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException;

    /**
     * Return the AttributeTypeDef identified by the GUID.
     *
     * @param userId  unique identifier for requesting user.
     * @param guid  String unique id of the TypeDef
     * @return TypeDef structure describing its category and properties.
     * @throws InvalidParameterException  the guid is null.
     * @throws PropertyServerException  there is a problem communicating with the metadata repository where
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
     * @param name  String name of the TypeDef.
     * @return TypeDef structure describing its category and properties.
     * @throws InvalidParameterException  the name is null.
     * @throws PropertyServerException  there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws UserNotAuthorizedException  the userId is not permitted to perform this operation.
     */
    OpenMetadataTypeDef getTypeDefByName(String    userId,
                                         String    name) throws InvalidParameterException,
                                                                PropertyServerException,
                                                                UserNotAuthorizedException;


    /**
     * Return the AttributeTypeDef identified by the unique name.
     *
     * @param userId unique identifier for requesting user.
     * @param name String name of the TypeDef.
     * @return TypeDef structure describing its category and properties.
     * @throws InvalidParameterException the name is null.
     * @throws PropertyServerException there is a problem communicating with the metadata repository where
     *                                  the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    OpenMetadataAttributeTypeDef getAttributeTypeDefByName(String    userId,
                                                           String    name) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException;

}
