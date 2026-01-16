/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.handlers;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.EntityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * SchemaAttributeHandler describes how to maintain and query schema attributes.
 */
public class SchemaAttributeHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public SchemaAttributeHandler(String             localServerName,
                                  AuditLog           auditLog,
                                  String             serviceName,
                                  OpenMetadataClient openMetadataClient)
    {
        super(localServerName,
              auditLog,
              serviceName,
              openMetadataClient,
              OpenMetadataType.SCHEMA_ATTRIBUTE.typeName);
    }


    /**
     * Create a new handler.
     *
     * @param template        properties to copy
     * @param specificTypeName   subtype  to control handler
     */
    public SchemaAttributeHandler(SchemaAttributeHandler template,
                                  String                 specificTypeName)
    {
        super(template, specificTypeName);
    }

    /**
     * Create a new schema attribute.
     *
     * @param userId                       userId of user making request.
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createSchemaAttribute(String                                userId,
                                        NewElementOptions                     newElementOptions,
                                        Map<String, ClassificationProperties> initialClassifications,
                                        SchemaAttributeProperties             properties,
                                        RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                   PropertyServerException,
                                                                                                                   UserNotAuthorizedException
    {
        final String methodName = "createSchemaAttribute";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent a schema attribute using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new schema attribute.
     *
     * @param userId                       calling user
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing asset to copy (this will copy all the attachments such as nested content, schema
     *                     connection etc)
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     *
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public String createSchemaAttributeFromTemplate(String                 userId,
                                                    TemplateOptions        templateOptions,
                                                    String                 templateGUID,
                                                    EntityProperties       replacementProperties,
                                                    Map<String, String>    placeholderProperties,
                                                    RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                PropertyServerException
    {
        return super.createElementFromTemplate(userId,
                                               templateOptions,
                                               templateGUID,
                                               replacementProperties,
                                               placeholderProperties,
                                               parentRelationshipProperties);
    }


    /**
     * Update the properties of a schema attribute.
     *
     * @param userId                 userId of user making request.
     * @param schemaAttributeGUID      unique identifier of the schema attribute (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateSchemaAttribute(String                    userId,
                                         String                    schemaAttributeGUID,
                                         UpdateOptions             updateOptions,
                                         SchemaAttributeProperties properties) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String methodName = "updateSchemaAttribute";
        final String guidParameterName = "schemaAttributeGUID";

        return super.updateElement(userId,
                                   schemaAttributeGUID,
                                   guidParameterName,
                                   updateOptions,
                                   properties,
                                   methodName);
    }


    /**
     * Attach a nested schema attribute to a schema attribute.
     *
     * @param userId                  userId of user making request
     * @param schemaAttributeGUID        unique identifier of the parent schema attribute
     * @param nestedSchemaAttributeGUID             unique identifier of the nested schema attribute
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkNestedSchemaAttribute(String                          userId,
                                          String                          schemaAttributeGUID,
                                          String                          nestedSchemaAttributeGUID,
                                          MakeAnchorOptions               makeAnchorOptions,
                                          NestedSchemaAttributeProperties relationshipProperties) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        final String methodName = "linkNestedSchemaAttribute";
        final String end1GUIDParameterName = "schemaAttributeGUID";
        final String end2GUIDParameterName = "nestedSchemaAttributeGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(schemaAttributeGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(nestedSchemaAttributeGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName,
                                                        schemaAttributeGUID,
                                                        nestedSchemaAttributeGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a nested schema attribute for a schema attribute.
     *
     * @param userId                 userId of user making request.
     * @param schemaAttributeGUID       unique identifier of the parent schema attribute
     * @param nestedSchemaAttributeGUID            unique identifier of the nested schema attribute
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachNestedSchemaAttribute(String        userId,
                                            String        schemaAttributeGUID,
                                            String        nestedSchemaAttributeGUID,
                                            DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName = "detachNestedSchemaAttribute";

        final String end1GUIDParameterName = "schemaAttributeGUID";
        final String end2GUIDParameterName = "nestedSchemaAttributeGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(schemaAttributeGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(nestedSchemaAttributeGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName,
                                                        schemaAttributeGUID,
                                                        nestedSchemaAttributeGUID,
                                                        deleteOptions);
    }




    /**
     * Attach a nested schema attribute to a schema type.
     *
     * @param userId                  userId of user making request
     * @param schemaTypeGUID        unique identifier of the parent schema type
     * @param nestedSchemaAttributeGUID             unique identifier of the nested schema attribute
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkAttributeForSchema(String                          userId,
                                       String                          schemaTypeGUID,
                                       String                          nestedSchemaAttributeGUID,
                                       MakeAnchorOptions               makeAnchorOptions,
                                       NestedSchemaAttributeProperties relationshipProperties) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        final String methodName = "linkAttributeForSchema";
        final String end1GUIDParameterName = "schemaTypeGUID";
        final String end2GUIDParameterName = "nestedSchemaAttributeGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(schemaTypeGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(nestedSchemaAttributeGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName,
                                                        schemaTypeGUID,
                                                        nestedSchemaAttributeGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a  schema attribute from a schema type.
     *
     * @param userId                 userId of user making request.
     * @param schemaTypeGUID       unique identifier of the parent schema type
     * @param nestedSchemaAttributeGUID            unique identifier of the nested schema attribute
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAttributeForSchema(String        userId,
                                         String        schemaTypeGUID,
                                         String        nestedSchemaAttributeGUID,
                                         DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String methodName = "detachAttributeForSchema";

        final String end1GUIDParameterName = "schemaTypeGUID";
        final String end2GUIDParameterName = "nestedSchemaAttributeGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(schemaTypeGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(nestedSchemaAttributeGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName,
                                                        schemaTypeGUID,
                                                        nestedSchemaAttributeGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a foreign key relationship to a relational column.
     *
     * @param userId                  userId of user making request
     * @param primaryKeyColumnGUID        unique identifier of the primary key
     * @param foreignKeyColumnGUID             unique identifier of the foreign key
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkForeignKey(String                userId,
                               String                primaryKeyColumnGUID,
                               String                foreignKeyColumnGUID,
                               MakeAnchorOptions     makeAnchorOptions,
                               ForeignKeyProperties  relationshipProperties) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String methodName = "linkForeignKey";
        final String end1GUIDParameterName = "primaryKeyColumnGUID";
        final String end2GUIDParameterName = "foreignKeyColumnGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(primaryKeyColumnGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(foreignKeyColumnGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.FOREIGN_KEY_RELATIONSHIP.typeName,
                                                        primaryKeyColumnGUID,
                                                        foreignKeyColumnGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a foreign key relationship for a relational column.
     *
     * @param userId                 userId of user making request.
     * @param primaryKeyColumnGUID        unique identifier of the primary key
     * @param foreignKeyColumnGUID             unique identifier of the foreign key
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachForeignKey(String        userId,
                                 String        primaryKeyColumnGUID,
                                 String        foreignKeyColumnGUID,
                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String methodName = "detachForeignKey";

        final String end1GUIDParameterName = "primaryKeyColumnGUID";
        final String end2GUIDParameterName = "foreignKeyColumnGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(primaryKeyColumnGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(foreignKeyColumnGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.FOREIGN_KEY_RELATIONSHIP.typeName,
                                                        primaryKeyColumnGUID,
                                                        foreignKeyColumnGUID,
                                                        deleteOptions);
    }



    /**
     * Attach a schema element to an external schema type.
     *
     * @param userId                  userId of user making request
     * @param schemaElementGUID        unique identifier of the schema element (typically this is a schema attribute
     *                                 but may be a root schema type
     * @param schemaTypeGUID             unique identifier of the linked schema type
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkExternalSchemaType(String                              userId,
                                       String                              schemaElementGUID,
                                       String                              schemaTypeGUID,
                                       MakeAnchorOptions                   makeAnchorOptions,
                                       LinkedExternalSchemaTypeProperties  relationshipProperties) throws InvalidParameterException,
                                                                                                          PropertyServerException,
                                                                                                          UserNotAuthorizedException
    {
        final String methodName = "linkExternalSchemaType";
        final String end1GUIDParameterName = "schemaElementGUID";
        final String end2GUIDParameterName = "schemaTypeGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(schemaElementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(schemaTypeGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.LINKED_EXTERNAL_SCHEMA_TYPE_RELATIONSHIP.typeName,
                                                        schemaElementGUID,
                                                        schemaTypeGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a schema element to an external schema type.
     *
     * @param userId                 userId of user making request.
     * @param schemaElementGUID        unique identifier of the primary key
     * @param schemaTypeGUID             unique identifier of the foreign key
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachExternalSchemaType(String        userId,
                                         String        schemaElementGUID,
                                         String        schemaTypeGUID,
                                         DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String methodName = "detachExternalSchemaType";

        final String end1GUIDParameterName = "schemaElementGUID";
        final String end2GUIDParameterName = "schemaTypeGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(schemaElementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(schemaTypeGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.LINKED_EXTERNAL_SCHEMA_TYPE_RELATIONSHIP.typeName,
                                                        schemaElementGUID,
                                                        schemaTypeGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a schema element to a "mapFrom" schema type.
     *
     * @param userId                  userId of user making request
     * @param schemaElementGUID        unique identifier of the schema element (typically this is a schema attribute
     *                                 but may be a root schema type
     * @param schemaTypeGUID             unique identifier of the linked schema type
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkMapFromSchemaType(String                      userId,
                                      String                      schemaElementGUID,
                                      String                      schemaTypeGUID,
                                      MakeAnchorOptions           makeAnchorOptions,
                                      MapToElementTypeProperties  relationshipProperties) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        final String methodName = "linkMapFromSchemaType";
        final String end1GUIDParameterName = "schemaElementGUID";
        final String end2GUIDParameterName = "schemaTypeGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(schemaElementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(schemaTypeGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.MAP_FROM_ELEMENT_TYPE_RELATIONSHIP.typeName,
                                                        schemaElementGUID,
                                                        schemaTypeGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a schema element from a "mapFrom" schema type.
     *
     * @param userId                 userId of user making request.
     * @param schemaElementGUID        unique identifier of the primary key
     * @param schemaTypeGUID             unique identifier of the foreign key
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachMapFromSchemaType(String        userId,
                                        String        schemaElementGUID,
                                        String        schemaTypeGUID,
                                        DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        final String methodName = "detachMapFromSchemaType";

        final String end1GUIDParameterName = "schemaElementGUID";
        final String end2GUIDParameterName = "schemaTypeGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(schemaElementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(schemaTypeGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.MAP_FROM_ELEMENT_TYPE_RELATIONSHIP.typeName,
                                                        schemaElementGUID,
                                                        schemaTypeGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a schema element to a "mapTo" schema type.
     *
     * @param userId                  userId of user making request
     * @param schemaElementGUID        unique identifier of the schema element (typically this is a schema attribute
     *                                 but may be a root schema type
     * @param schemaTypeGUID             unique identifier of the linked schema type
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkMapToSchemaType(String                      userId,
                                    String                      schemaElementGUID,
                                    String                      schemaTypeGUID,
                                    MakeAnchorOptions           makeAnchorOptions,
                                    MapToElementTypeProperties  relationshipProperties) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        final String methodName = "linkMapFromSchemaType";
        final String end1GUIDParameterName = "schemaElementGUID";
        final String end2GUIDParameterName = "schemaTypeGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(schemaElementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(schemaTypeGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.MAP_TO_ELEMENT_TYPE_RELATIONSHIP.typeName,
                                                        schemaElementGUID,
                                                        schemaTypeGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a schema element from a "mapTo" schema type.
     *
     * @param userId                 userId of user making request.
     * @param schemaElementGUID        unique identifier of the primary key
     * @param schemaTypeGUID             unique identifier of the foreign key
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachMapToSchemaType(String        userId,
                                      String        schemaElementGUID,
                                      String        schemaTypeGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName = "detachMapToSchemaType";

        final String end1GUIDParameterName = "schemaElementGUID";
        final String end2GUIDParameterName = "schemaTypeGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(schemaElementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(schemaTypeGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.MAP_TO_ELEMENT_TYPE_RELATIONSHIP.typeName,
                                                        schemaElementGUID,
                                                        schemaTypeGUID,
                                                        deleteOptions);
    }



    /**
     * Attach a graph edge schema element to a graph vertex schema element.
     *
     * @param userId                  userId of user making request
     * @param graphEdgeGUID        unique identifier of the schema element (typically this is a schema attribute
     *                                 but may be a root schema type
     * @param graphVertexGUID             unique identifier of the linked schema type
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkGraphEdge(String                   userId,
                              String                   graphEdgeGUID,
                              String                   graphVertexGUID,
                              MakeAnchorOptions        makeAnchorOptions,
                              GraphEdgeLinkProperties  relationshipProperties) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String methodName = "linkGraphEdge";
        final String end1GUIDParameterName = "graphEdgeGUID";
        final String end2GUIDParameterName = "graphVertexGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(graphEdgeGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(graphVertexGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.GRAPH_EDGE_LINK_RELATIONSHIP.typeName,
                                                        graphEdgeGUID,
                                                        graphVertexGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a graph edge schema element from a graph vertex schema element.
     *
     * @param userId                 userId of user making request.
     * @param graphEdgeGUID        unique identifier of the primary key
     * @param graphVertexGUID             unique identifier of the foreign key
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachGraphEdge(String        userId,
                                String        graphEdgeGUID,
                                String        graphVertexGUID,
                                DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        final String methodName = "detachGraphEdge";

        final String end1GUIDParameterName = "graphEdgeGUID";
        final String end2GUIDParameterName = "graphVertexGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(graphEdgeGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(graphVertexGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.GRAPH_EDGE_LINK_RELATIONSHIP.typeName,
                                                        graphEdgeGUID,
                                                        graphVertexGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a schema element to a "mapTo" schema type.
     *
     * @param userId                  userId of user making request
     * @param schemaElementGUID        unique identifier of the schema element (typically this is a schema attribute
     *                                 but may be a root schema type
     * @param queryTargetSchemaElementGUID  unique identifier of the schema element supplying all/part of the data
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkQueryTarget(String                                 userId,
                                String                                 schemaElementGUID,
                                String                                 queryTargetSchemaElementGUID,
                                MakeAnchorOptions                      makeAnchorOptions,
                                DerivedSchemaTypeQueryTargetProperties relationshipProperties) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        final String methodName = "linkQueryTarget";
        final String end1GUIDParameterName = "schemaElementGUID";
        final String end2GUIDParameterName = "queryTargetSchemaElementGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(schemaElementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(queryTargetSchemaElementGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.DERIVED_SCHEMA_TYPE_QUERY_TARGET_RELATIONSHIP.typeName,
                                                        schemaElementGUID,
                                                        queryTargetSchemaElementGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a schema element from a "mapTo" schema type.
     *
     * @param userId                 userId of user making request.
     * @param schemaElementGUID        unique identifier of the primary key
     * @param schemaTypeGUID             unique identifier of the foreign key
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachQueryTarget(String        userId,
                                  String        schemaElementGUID,
                                  String        schemaTypeGUID,
                                  DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        final String methodName = "detachQueryTarget";

        final String end1GUIDParameterName = "schemaElementGUID";
        final String end2GUIDParameterName = "schemaTypeGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(schemaElementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(schemaTypeGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.DERIVED_SCHEMA_TYPE_QUERY_TARGET_RELATIONSHIP.typeName,
                                                        schemaElementGUID,
                                                        schemaTypeGUID,
                                                        deleteOptions);
    }


    /**
     * Add the PrimaryKey classification to a relational column.
     *
     * @param userId                 userId of user making request.
     * @param relationalColumnGUID    unique identifier of the schema attribute.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addPrimaryKeyClassification(String                userId,
                                            String                relationalColumnGUID,
                                            PrimaryKeyProperties  properties,
                                            MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        final String methodName = "addPrimaryKeyClassification";
        final String guidParameterName = "relationalColumnGUID";
        final String propertiesParameterName = "properties";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(relationalColumnGUID, guidParameterName, methodName);
        propertyHelper.validateObject(properties, propertiesParameterName, methodName);

        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          relationalColumnGUID,
                                                          OpenMetadataType.PRIMARY_KEY_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Remove the PrimaryKey classification for a relational column.
     *
     * @param userId                 userId of user making request.
     * @param relationalColumnGUID    unique identifier of the schema attribute.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removePrimaryKeyClassification(String                userId,
                                               String                relationalColumnGUID,
                                               MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        final String methodName = "removePrimaryKeyClassification";
        final String guidParameterName = "relationalColumnGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(relationalColumnGUID, guidParameterName, methodName);

        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                            relationalColumnGUID,
                                                            OpenMetadataType.PRIMARY_KEY_CLASSIFICATION.typeName,
                                                            metadataSourceOptions);
    }


    /**
     * Add the TypeEmbeddedAttribute classification to the schema attribute.
     *
     * @param userId                 userId of user making request.
     * @param schemaAttributeGUID    unique identifier of the schema attribute.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addTypeEmbeddedAttribute(String                          userId,
                                         String                          schemaAttributeGUID,
                                         TypeEmbeddedAttributeProperties properties,
                                         MetadataSourceOptions           metadataSourceOptions) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        final String methodName = "addTypeEmbeddedAttribute";
        final String guidParameterName = "schemaAttributeGUID";
        final String propertiesParameterName = "properties";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(schemaAttributeGUID, guidParameterName, methodName);
        propertyHelper.validateObject(properties, propertiesParameterName, methodName);

        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          schemaAttributeGUID,
                                                          OpenMetadataType.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Update the TypeEmbeddedAttribute classification for the schema attribute.
     *
     * @param userId                 userId of user making request.
     * @param schemaAttributeGUID    unique identifier of the schema attribute.
     * @param updateOptions provides a structure for the additional options when updating a classification.
     * @param properties            properties for the classification
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateTypeEmbeddedAttribute(String                          userId,
                                            String                          schemaAttributeGUID,
                                            UpdateOptions                   updateOptions,
                                            TypeEmbeddedAttributeProperties properties) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        final String methodName = "updateTypeEmbeddedAttribute";
        final String guidParameterName = "schemaAttributeGUID";
        final String propertiesParameterName = "properties";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(schemaAttributeGUID, guidParameterName, methodName);
        propertyHelper.validateObject(properties, propertiesParameterName, methodName);

        openMetadataClient.reclassifyMetadataElementInStore(userId,
                                                            schemaAttributeGUID,
                                                            OpenMetadataType.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION.typeName,
                                                            updateOptions,
                                                            classificationBuilder.getElementProperties(properties));

        openMetadataClient.updateClassificationEffectivityInStore(userId,
                                                                  schemaAttributeGUID,
                                                                  OpenMetadataType.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION.typeName,
                                                                  updateOptions,
                                                                  properties.getEffectiveFrom(),
                                                                  properties.getEffectiveTo());
    }


    /**
     * Remove the TypeEmbeddedAttribute classification from the schema attribute.
     *
     * @param userId                 userId of user making request.
     * @param schemaAttributeGUID    unique identifier of the schema attribute.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeTypeEmbeddedAttribute(String                userId,
                                            String                schemaAttributeGUID,
                                            MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        final String methodName = "removeTypeEmbeddedAttribute";
        final String guidParameterName = "schemaAttributeGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(schemaAttributeGUID, guidParameterName, methodName);

        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                            schemaAttributeGUID,
                                                            OpenMetadataType.TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION.typeName,
                                                            metadataSourceOptions);
    }


    /**
     * Add the CalculatedValue classification to the schema attribute.
     *
     * @param userId                 userId of user making request.
     * @param schemaAttributeGUID    unique identifier of the schema attribute.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addCalculatedValue(String                    userId,
                                   String                    schemaAttributeGUID,
                                   CalculatedValueProperties properties,
                                   MetadataSourceOptions     metadataSourceOptions) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName = "addCalculatedValue";
        final String guidParameterName = "schemaAttributeGUID";
        final String propertiesParameterName = "properties";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(schemaAttributeGUID, guidParameterName, methodName);
        propertyHelper.validateObject(properties, propertiesParameterName, methodName);

        openMetadataClient.classifyMetadataElementInStore(userId,
                                                          schemaAttributeGUID,
                                                          OpenMetadataType.CALCULATED_VALUE_CLASSIFICATION.typeName,
                                                          metadataSourceOptions,
                                                          classificationBuilder.getNewElementProperties(properties));
    }


    /**
     * Update the CalculatedValue classification for the schema attribute.
     *
     * @param userId                 userId of user making request.
     * @param schemaAttributeGUID    unique identifier of the schema attribute.
     * @param updateOptions provides a structure for the additional options when updating a classification.
     * @param properties            properties for the classification
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateCalculatedValue(String                    userId,
                                      String                    schemaAttributeGUID,
                                      UpdateOptions             updateOptions,
                                      CalculatedValueProperties properties) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName = "updateCalculatedValue";
        final String guidParameterName = "schemaAttributeGUID";
        final String propertiesParameterName = "properties";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(schemaAttributeGUID, guidParameterName, methodName);
        propertyHelper.validateObject(properties, propertiesParameterName, methodName);

        openMetadataClient.reclassifyMetadataElementInStore(userId,
                                                            schemaAttributeGUID,
                                                            OpenMetadataType.CALCULATED_VALUE_CLASSIFICATION.typeName,
                                                            updateOptions,
                                                            classificationBuilder.getElementProperties(properties));

        openMetadataClient.updateClassificationEffectivityInStore(userId,
                                                                  schemaAttributeGUID,
                                                                  OpenMetadataType.CALCULATED_VALUE_CLASSIFICATION.typeName,
                                                                  updateOptions,
                                                                  properties.getEffectiveFrom(),
                                                                  properties.getEffectiveTo());
    }


    /**
     * Remove the CalculatedValue classification from the schema attribute.
     *
     * @param userId                 userId of user making request.
     * @param schemaAttributeGUID    unique identifier of the schema attribute.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeCalculatedValue(String                userId,
                                      String                schemaAttributeGUID,
                                      MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        final String methodName = "removeCalculatedValue";
        final String guidParameterName = "schemaAttributeGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(schemaAttributeGUID, guidParameterName, methodName);

        openMetadataClient.declassifyMetadataElementInStore(userId,
                                                            schemaAttributeGUID,
                                                            OpenMetadataType.CALCULATED_VALUE_CLASSIFICATION.typeName,
                                                            metadataSourceOptions);
    }


    /**
     * Delete a schema attribute.
     *
     * @param userId                 userId of user making request.
     * @param schemaAttributeGUID       unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteSchemaAttribute(String        userId,
                                      String        schemaAttributeGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName        = "deleteSchemaAttribute";
        final String guidParameterName = "schemaAttributeGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(schemaAttributeGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, schemaAttributeGUID, deleteOptions);
    }


    /**
     * Returns the list of schema attributes with a particular name.
     *
     * @param userId                 userId of user making request
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getSchemaAttributesByName(String       userId,
                                                                   String       name,
                                                                   QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        final String methodName = "getSchemaAttributesByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.IDENTIFIER.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name);

        return super.getRootElementsByName(userId, name, propertyNames, queryOptions, methodName);
    }


    /**
     * Return the properties of a specific schema attribute.
     *
     * @param userId                 userId of user making request
     * @param schemaAttributeGUID      unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getSchemaAttributeByGUID(String     userId,
                                                            String     schemaAttributeGUID,
                                                            GetOptions getOptions) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        final String methodName = "getSchemaAttributeByGUID";

        return super.getRootElementByGUID(userId, schemaAttributeGUID, getOptions, methodName);
    }


    /**
     * Retrieve the list of schema attributes metadata elements that contain the search string.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findSchemaAttributes(String        userId,
                                                              String        searchString,
                                                              SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        final String methodName = "findSchemaAttributes";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }



    /**
     * Returns the list of schema attributes nested under a schema attribute.
     *
     * @param userId                 userId of user making request
     * @param schemaTypeGUID           unique identifier of the starting schema attribute
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getAttributesForSchemaType(String       userId,
                                                                    String       schemaTypeGUID,
                                                                    QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        final String methodName = "getAttributesForSchemaType";
        final String guidParameterName = "schemaTypeGUID";

        return super.getRelatedRootElements(userId,
                                            schemaTypeGUID,
                                            guidParameterName,
                                            1,
                                            OpenMetadataType.ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP.typeName,
                                            OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                            queryOptions,
                                            methodName);
    }


    /**
     * Returns the list of schema attributes nested under a schema attribute.
     *
     * @param userId                 userId of user making request
     * @param schemaAttributeGUID           unique identifier of the starting schema attribute
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getNestedSchemaAttributes(String       userId,
                                                                   String       schemaAttributeGUID,
                                                                   QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        final String methodName = "getNestedSchemaAttributes";
        final String guidParameterName = "schemaAttributeGUID";

        return super.getRelatedRootElements(userId,
                                            schemaAttributeGUID,
                                            guidParameterName,
                                            1,
                                            OpenMetadataType.NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP.typeName,
                                            OpenMetadataType.SCHEMA_ATTRIBUTE.typeName,
                                            queryOptions,
                                            methodName);
    }
}
