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
     * @param maxPageSize max elements that can be returned on a query
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
     * type definitions.  Full type definitions (TypeDefs) describe types for entities, relationships,
     * and classifications.
     *
     * @param getInheritedAttributes whether to include inherited attributes in the returned TypeDefs
     * @param getRelationshipAttributes whether to include relationship attributes in the returned TypeDefs
     * @return TypeDefGallery  List of different categories of type definitions.
     *
     * @throws InvalidParameterException  the userId is null
     * @throws PropertyServerException    a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public OpenMetadataTypeDefGallery getAllTypes(boolean getInheritedAttributes,
                                                  boolean getRelationshipAttributes) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        return openMetadataClient.getAllTypes(connectorUserId, getInheritedAttributes, getRelationshipAttributes);
    }


    /**
     * Returns all the TypeDefs for a specific category.
     *
     * @param getInheritedAttributes whether to include inherited attributes in the returned TypeDefs
     * @param getRelationshipAttributes whether to include relationship attributes in the returned TypeDefs
     * @param category enum value for the category of TypeDef to return.
     *
     * @return TypeDefs list.
     *
     * @throws InvalidParameterException  the TypeDefCategory is null.
     * @throws PropertyServerException    a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public TypeDefList findTypeDefsByCategory(boolean                     getInheritedAttributes,
                                              boolean                     getRelationshipAttributes,
                                              OpenMetadataTypeDefCategory category) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        return openMetadataClient.findTypeDefsByCategory(connectorUserId, getInheritedAttributes, getRelationshipAttributes, category);
    }


    /**
     * Returns all the AttributeTypeDefs for an optional specific category.  If the category is null then
     * all attribute type defs are returned.
     *
     * @param category enum value for the category of an AttributeTypeDef to return.
     *
     * @return AttributeTypeDefs list.
     *
     * @throws InvalidParameterException  the TypeDefCategory is null.
     * @throws PropertyServerException    a problem communicating with the metadata repository.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public List<OpenMetadataAttributeTypeDef> getAttributeTypeDefs(OpenMetadataAttributeTypeDefCategory category) throws InvalidParameterException,
                                                                                                                         PropertyServerException,
                                                                                                                         UserNotAuthorizedException
    {
        return openMetadataClient.getAttributeTypeDefs(connectorUserId, category);
    }


    /**
     * Return the types that are linked to the elements from the specified standard.
     *
     * @param getInheritedAttributes whether to include inherited attributes in the returned TypeDefs
     * @param getRelationshipAttributes whether to include relationship attributes in the returned TypeDefs
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
    public TypeDefList findTypesByExternalId(boolean getInheritedAttributes,
                                             boolean getRelationshipAttributes,
                                             String  standard,
                                             String  organization,
                                             String  identifier) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        return openMetadataClient.findTypesByExternalId(connectorUserId, getInheritedAttributes, getRelationshipAttributes, standard, organization, identifier);
    }


    /**
     * Returns all the TypeDefs for a specific subtype.  If a null result is returned it means the
     * type has no subtypes.
     *
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
    public TypeDefList getSubTypes(boolean getInheritedAttributes,
                                   boolean getRelationshipAttributes,
                                   String  typeName) throws InvalidParameterException,
                                                            PropertyServerException,
                                                            UserNotAuthorizedException
    {
        return openMetadataClient.getSubTypes(connectorUserId, getInheritedAttributes, getRelationshipAttributes, typeName);
    }

    /**
     * Return the TypeDef identified by the GUID.
     *
     * @param getInheritedAttributes whether to include inherited attributes in the returned TypeDefs
     * @param getRelationshipAttributes whether to include relationship attributes in the returned TypeDefs
     * @param guid   String unique id of the TypeDef
     *
     * @return TypeDef structure describing its category and properties.
     *
     * @throws InvalidParameterException  the guid is null.
     * @throws PropertyServerException    a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public OpenMetadataTypeDef getTypeDefByGUID(boolean getInheritedAttributes,
                                                boolean getRelationshipAttributes,
                                                String  guid) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        return openMetadataClient.getTypeDefByGUID(connectorUserId, getInheritedAttributes, getRelationshipAttributes, guid);
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
     * @param getInheritedAttributes whether to include inherited attributes in the returned TypeDefs
     * @param getRelationshipAttributes whether to include relationship attributes in the returned TypeDefs
     * @param name   String name of the TypeDef.
     *
     * @return TypeDef structure describing its category and properties.
     *
     * @throws InvalidParameterException  the name is null.
     * @throws PropertyServerException    a problem communicating with the metadata repository where
     *                                    the metadata collection is stored.
     * @throws UserNotAuthorizedException the userId is not permitted to perform this operation.
     */
    public OpenMetadataTypeDef getTypeDefByName(boolean getInheritedAttributes,
                                                boolean getRelationshipAttributes,
                                                String  name) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        return openMetadataClient.getTypeDefByName(connectorUserId, getInheritedAttributes, getRelationshipAttributes, name);
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