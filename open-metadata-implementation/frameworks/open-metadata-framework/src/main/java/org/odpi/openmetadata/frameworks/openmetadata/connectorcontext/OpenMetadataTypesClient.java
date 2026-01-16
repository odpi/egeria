/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.properties.*;

import java.util.List;

/**
 * OpenMetadataTypesClient provides access to the open metadata type definitions used in the metadata repositories.
 * It is possible to retrieve all types or search/retrieve types based on their name and/or unique identifier.
 */
public class OpenMetadataTypesClient extends ConnectorContextClientBase
{
    private final OpenMetadataClient openMetadataClient;

    /**
     * Constructor for connector context client.
     *
     * @param parentContext connector's context
     * @param localServerName local server where this client is running - used for error handling
     * @param localServiceName local service that his connector is hosted by - used for error handling
     * @param connectorUserId the userId to use with all requests for open metadata
     * @param externalSourceGUID unique identifier of the software server capability for the source of metadata
     * @param externalSourceName unique name of the software server capability for the source of metadata
     * @param openMetadataClient client to access the open metadata store
     * @param auditLog logging destination
     * @param maxPageSize max number of elements that can be returned on a query
     */
    public OpenMetadataTypesClient(ConnectorContextBase parentContext,
                                   String               localServerName,
                                   String               localServiceName,
                                   String               connectorUserId,
                                   String               connectorGUID,
                                   String               externalSourceGUID,
                                   String               externalSourceName,
                                   OpenMetadataClient   openMetadataClient,
                                   AuditLog             auditLog,
                                   int                  maxPageSize)
    {
        super(parentContext, localServerName, localServiceName, connectorUserId, connectorGUID, externalSourceGUID, externalSourceName, auditLog, maxPageSize);

        this.openMetadataClient = openMetadataClient;
    }


    /**
     * Returns the list of different types of metadata organized into two groups.  The first are the
     * attribute type definitions (AttributeTypeDefs).  These provide types for properties in full
     * type definitions.  Full type definitions (TypeDefs) describe types for entities, relationships
     * and classifications.
     *
     *
     * @return TypeDefGallery  List of different categories of type definitions.
     *
     * @throws InvalidParameterException  the userId is null
     * @throws PropertyServerException    a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public OpenMetadataTypeDefGallery getAllTypes() throws InvalidParameterException,
                                                           PropertyServerException,
                                                           UserNotAuthorizedException
    {
        return openMetadataClient.getAllTypes(connectorUserId);
    }


    /**
     * Returns all the TypeDefs for a specific category.
     *
     * @param category enum value for the category of TypeDef to return.
     *
     * @return TypeDefs list.
     *
     * @throws InvalidParameterException  the TypeDefCategory is null.
     * @throws PropertyServerException    a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public TypeDefList findTypeDefsByCategory(OpenMetadataTypeDefCategory category) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        return openMetadataClient.findTypeDefsByCategory(connectorUserId, category);
    }


    /**
     * Returns all the AttributeTypeDefs for a specific category.
     *
     * @param category enum value for the category of an AttributeTypeDef to return.
     *
     * @return AttributeTypeDefs list.
     *
     * @throws InvalidParameterException  the TypeDefCategory is null.
     * @throws PropertyServerException    a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public List<OpenMetadataAttributeTypeDef> findAttributeTypeDefsByCategory(OpenMetadataAttributeTypeDefCategory category) throws InvalidParameterException,
                                                                                                                                    PropertyServerException,
                                                                                                                                    UserNotAuthorizedException
    {
        return openMetadataClient.findAttributeTypeDefsByCategory(connectorUserId, category);
    }


    /**
     * Return the types that are linked to the elements from the specified standard.
     *
     * @param standard     name of the standard null means any.
     * @param organization name of the organization null means any.
     * @param identifier   identifier of the element in the standard null means any.
     *
     * @return TypeDefs list  each entry in the list contains a TypeDef.  This is a structure
     * describing the TypeDef's category and properties.
     *
     * @throws InvalidParameterException  all attributes of the external id are null.
     * @throws PropertyServerException    a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public TypeDefList findTypesByExternalID(String standard,
                                                           String organization,
                                                           String identifier) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        return openMetadataClient.findTypesByExternalId(connectorUserId, standard, organization, identifier);
    }


    /**
     * Returns all the TypeDefs for a specific subtype.  If a null result is returned it means the
     * type has no subtypes.
     *
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
    public TypeDefList getSubTypes(String typeName) throws InvalidParameterException,
                                                           PropertyServerException,
                                                           UserNotAuthorizedException
    {
        return openMetadataClient.getSubTypes(connectorUserId, typeName);
    }

    /**
     * Return the TypeDef identified by the GUID.
     *
     * @param guid   String unique id of the TypeDef
     *
     * @return TypeDef structure describing its category and properties.
     *
     * @throws InvalidParameterException  the guid is null.
     * @throws PropertyServerException    a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public OpenMetadataTypeDef getTypeDefByGUID(String guid) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        return openMetadataClient.getTypeDefByGUID(connectorUserId, guid);
    }


    /**
     * Return the AttributeTypeDef identified by the GUID.
     *
     * @param guid   String unique id of the TypeDef
     *
     * @return TypeDef structure describing its category and properties.
     *
     * @throws InvalidParameterException  the guid is null.
     * @throws PropertyServerException    a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public OpenMetadataAttributeTypeDef getAttributeTypeDefByGUID(String guid) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        return openMetadataClient.getAttributeTypeDefByGUID(connectorUserId, guid);
    }


    /**
     * Return the TypeDef identified by the unique name.
     *
     * @param name   String name of the TypeDef.
     *
     * @return TypeDef structure describing its category and properties.
     *
     * @throws InvalidParameterException  the name is null.
     * @throws PropertyServerException    a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public OpenMetadataTypeDef getTypeDefByName(String name) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        return openMetadataClient.getTypeDefByName(connectorUserId, name);
    }


    /**
     * Return the AttributeTypeDef identified by the unique name.
     *
     * @param name   String name of the TypeDef.
     *
     * @return TypeDef structure describing its category and properties.
     *
     * @throws InvalidParameterException  the name is null.
     * @throws PropertyServerException    a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public OpenMetadataAttributeTypeDef getAttributeTypeDefByName(String name) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        return openMetadataClient.getAttributeTypeDefByName(connectorUserId, name);
    }
}