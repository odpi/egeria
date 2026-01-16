/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.SchemaAttributeHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.EntityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

import java.util.List;
import java.util.Map;

/**
 * SchemaAttributeClient describes how to maintain and query schema attributes.
 */
public class SchemaAttributeClient extends ConnectorContextClientBase
{
    private final SchemaAttributeHandler schemaAttributeHandler;

    /**
     * Constructor for connector context client.
     *
     * @param parentContext connector's context
     * @param localServerName local server where this client is running - used for error handling
     * @param localServiceName local service that his connector is hosted by - used for error handling
     * @param connectorUserId the userId to use with all requests for open metadata
     * @param connectorGUID the unique identifier that represents this connector in open metadata
     * @param externalSourceGUID unique identifier of the software server capability for the source of metadata
     * @param externalSourceName unique name of the software server capability for the source of metadata
     * @param openMetadataClient client to access the open metadata store
     * @param auditLog logging destination
     * @param maxPageSize max number of elements that can be returned on a query
     */
    public SchemaAttributeClient(ConnectorContextBase     parentContext,
                                 String                   localServerName,
                                 String                   localServiceName,
                                 String                   connectorUserId,
                                 String                   connectorGUID,
                                 String                   externalSourceGUID,
                                 String                   externalSourceName,
                                 OpenMetadataClient       openMetadataClient,
                                 AuditLog                 auditLog,
                                 int                      maxPageSize)
    {
        super(parentContext, localServerName, localServiceName, connectorUserId, connectorGUID, externalSourceGUID, externalSourceName, auditLog, maxPageSize);

        this.schemaAttributeHandler = new SchemaAttributeHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template client to copy
     * @param specificTypeName type name override
     */
    public SchemaAttributeClient(SchemaAttributeClient template,
                                 String                specificTypeName)
    {
        super(template);

        this.schemaAttributeHandler = new SchemaAttributeHandler(template.schemaAttributeHandler, specificTypeName);
    }


    /**
     * Create a new schema attribute.
     *
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createSchemaAttribute(NewElementOptions                     newElementOptions,
                                        Map<String, ClassificationProperties> initialClassifications,
                                        SchemaAttributeProperties             properties,
                                        RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                   PropertyServerException,
                                                                                                                   UserNotAuthorizedException
    {
        String elementGUID = schemaAttributeHandler.createSchemaAttribute(connectorUserId, newElementOptions, initialClassifications, properties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Create a new metadata element to represent a schema attribute using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new schema attribute.
     *
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
    public String createSchemaAttributeFromTemplate(TemplateOptions        templateOptions,
                                                    String                 templateGUID,
                                                    EntityProperties       replacementProperties,
                                                    Map<String, String>    placeholderProperties,
                                                    RelationshipProperties parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                UserNotAuthorizedException,
                                                                                                                PropertyServerException
    {
        String elementGUID = schemaAttributeHandler.createSchemaAttributeFromTemplate(connectorUserId, templateOptions, templateGUID, replacementProperties, placeholderProperties, parentRelationshipProperties);

        if (parentContext.getIntegrationReportWriter() != null)
        {
            parentContext.getIntegrationReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Update the properties of a schema attribute.
     *
     * @param schemaAttributeGUID      unique identifier of the schema attribute (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateSchemaAttribute(String                    schemaAttributeGUID,
                                         UpdateOptions             updateOptions,
                                         SchemaAttributeProperties properties) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        boolean updateOccurred = schemaAttributeHandler.updateSchemaAttribute(connectorUserId, schemaAttributeGUID, updateOptions, properties);

        if ((updateOccurred) && (parentContext.getIntegrationReportWriter() != null))
        {
            parentContext.getIntegrationReportWriter().reportElementUpdate(schemaAttributeGUID);
        }

        return updateOccurred;
    }


    /**
     * Attach a nested schema attribute to a schema attribute.
     *
     * @param schemaAttributeGUID        unique identifier of the parent schema attribute
     * @param nestedSchemaAttributeGUID             unique identifier of the nested schema attribute
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkNestedSchemaAttribute(String                          schemaAttributeGUID,
                                          String                          nestedSchemaAttributeGUID,
                                          MakeAnchorOptions               makeAnchorOptions,
                                          NestedSchemaAttributeProperties relationshipProperties) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        schemaAttributeHandler.linkNestedSchemaAttribute(connectorUserId, schemaAttributeGUID, nestedSchemaAttributeGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a nested schema attribute for a schema attribute.
     *
     * @param schemaAttributeGUID       unique identifier of the parent schema attribute
     * @param nestedSchemaAttributeGUID            unique identifier of the nested schema attribute
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachNestedSchemaAttribute(String        schemaAttributeGUID,
                                            String        nestedSchemaAttributeGUID,
                                            DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        schemaAttributeHandler.detachNestedSchemaAttribute(connectorUserId, schemaAttributeGUID, nestedSchemaAttributeGUID, deleteOptions);
    }


    /**
     * Attach a nested schema attribute to a schema type.
     *
     * @param schemaTypeGUID        unique identifier of the parent schema type
     * @param nestedSchemaAttributeGUID             unique identifier of the nested schema attribute
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkAttributeForSchema(String                          schemaTypeGUID,
                                       String                          nestedSchemaAttributeGUID,
                                       MakeAnchorOptions               makeAnchorOptions,
                                       NestedSchemaAttributeProperties relationshipProperties) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        schemaAttributeHandler.linkAttributeForSchema(connectorUserId, schemaTypeGUID, nestedSchemaAttributeGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a  schema attribute from a schema type.
     *
     * @param schemaTypeGUID       unique identifier of the parent schema type
     * @param nestedSchemaAttributeGUID            unique identifier of the nested schema attribute
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAttributeForSchema(String        schemaTypeGUID,
                                         String        nestedSchemaAttributeGUID,
                                         DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        schemaAttributeHandler.detachAttributeForSchema(connectorUserId, schemaTypeGUID, nestedSchemaAttributeGUID, deleteOptions);
    }


    /**
     * Attach a foreign key relationship to a relational column.
     *
     * @param primaryKeyColumnGUID        unique identifier of the primary key
     * @param foreignKeyColumnGUID             unique identifier of the foreign key
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkForeignKey(String                primaryKeyColumnGUID,
                               String                foreignKeyColumnGUID,
                               MakeAnchorOptions     makeAnchorOptions,
                               ForeignKeyProperties  relationshipProperties) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        schemaAttributeHandler.linkForeignKey(connectorUserId, primaryKeyColumnGUID, foreignKeyColumnGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a foreign key relationship for a relational column.
     *
     * @param primaryKeyColumnGUID        unique identifier of the primary key
     * @param foreignKeyColumnGUID             unique identifier of the foreign key
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachForeignKey(String        primaryKeyColumnGUID,
                                 String        foreignKeyColumnGUID,
                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        schemaAttributeHandler.detachForeignKey(connectorUserId, primaryKeyColumnGUID, foreignKeyColumnGUID, deleteOptions);
    }



    /**
     * Attach a schema element to an external schema type.
     *
     * @param schemaElementGUID        unique identifier of the schema element (typically this is a schema attribute
     *                                 but may be a root schema type
     * @param schemaTypeGUID             unique identifier of the linked schema type
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkExternalSchemaType(String                              schemaElementGUID,
                                       String                              schemaTypeGUID,
                                       MakeAnchorOptions                   makeAnchorOptions,
                                       LinkedExternalSchemaTypeProperties  relationshipProperties) throws InvalidParameterException,
                                                                                                          PropertyServerException,
                                                                                                          UserNotAuthorizedException
    {
        schemaAttributeHandler.linkExternalSchemaType(connectorUserId, schemaElementGUID, schemaTypeGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a schema element to an external schema type.
     *
     * @param schemaElementGUID        unique identifier of the primary key
     * @param schemaTypeGUID             unique identifier of the foreign key
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachExternalSchemaType(String        schemaElementGUID,
                                         String        schemaTypeGUID,
                                         DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        schemaAttributeHandler.detachExternalSchemaType(connectorUserId, schemaElementGUID, schemaTypeGUID, deleteOptions);
    }


    /**
     * Attach a schema element to a "mapFrom" schema type.
     *
     * @param schemaElementGUID        unique identifier of the schema element (typically this is a schema attribute
     *                                 but may be a root schema type
     * @param schemaTypeGUID             unique identifier of the linked schema type
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkMapFromSchemaType(String                      schemaElementGUID,
                                      String                      schemaTypeGUID,
                                      MakeAnchorOptions           makeAnchorOptions,
                                      MapToElementTypeProperties  relationshipProperties) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        schemaAttributeHandler.linkMapFromSchemaType(connectorUserId, schemaElementGUID, schemaTypeGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a schema element from a "mapFrom" schema type.
     *
     * @param schemaElementGUID        unique identifier of the primary key
     * @param schemaTypeGUID             unique identifier of the foreign key
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachMapFromSchemaType(String        schemaElementGUID,
                                        String        schemaTypeGUID,
                                        DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        schemaAttributeHandler.detachMapFromSchemaType(connectorUserId, schemaElementGUID, schemaTypeGUID, deleteOptions);
    }


    /**
     * Attach a schema element to a "mapTo" schema type.
     *
     * @param schemaElementGUID        unique identifier of the schema element (typically this is a schema attribute
     *                                 but may be a root schema type
     * @param schemaTypeGUID             unique identifier of the linked schema type
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkMapToSchemaType(String                      schemaElementGUID,
                                    String                      schemaTypeGUID,
                                    MakeAnchorOptions           makeAnchorOptions,
                                    MapToElementTypeProperties  relationshipProperties) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        schemaAttributeHandler.linkMapToSchemaType(connectorUserId, schemaElementGUID, schemaTypeGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a schema element from a "mapTo" schema type.
     *
     * @param schemaElementGUID        unique identifier of the primary key
     * @param schemaTypeGUID             unique identifier of the foreign key
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachMapToSchemaType(String        schemaElementGUID,
                                      String        schemaTypeGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        schemaAttributeHandler.detachMapToSchemaType(connectorUserId, schemaElementGUID, schemaTypeGUID, deleteOptions);
    }


    /**
     * Attach a graph edge schema element to a graph vertex schema element.
     *
     * @param graphEdgeGUID        unique identifier of the schema element (typically this is a schema attribute
     *                                 but may be a root schema type
     * @param graphVertexGUID             unique identifier of the linked schema type
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkGraphEdge(String                   graphEdgeGUID,
                              String                   graphVertexGUID,
                              MakeAnchorOptions        makeAnchorOptions,
                              GraphEdgeLinkProperties  relationshipProperties) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        schemaAttributeHandler.linkGraphEdge(connectorUserId, graphEdgeGUID, graphVertexGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a graph edge schema element from a graph vertex schema element.
     *
     * @param graphEdgeGUID        unique identifier of the primary key
     * @param graphVertexGUID             unique identifier of the foreign key
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachGraphEdge(String        graphEdgeGUID,
                                String        graphVertexGUID,
                                DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        schemaAttributeHandler.detachGraphEdge(connectorUserId, graphEdgeGUID, graphVertexGUID, deleteOptions);
    }


    /**
     * Attach a schema element to a "mapTo" schema type.
     *
     * @param schemaElementGUID        unique identifier of the schema element (typically this is a schema attribute
     *                                 but may be a root schema type
     * @param queryTargetSchemaElementGUID  unique identifier of the schema element supplying all/part of the data
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkQueryTarget(String                                 schemaElementGUID,
                                String                                 queryTargetSchemaElementGUID,
                                MakeAnchorOptions                      makeAnchorOptions,
                                DerivedSchemaTypeQueryTargetProperties relationshipProperties) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        schemaAttributeHandler.linkQueryTarget(connectorUserId, schemaElementGUID, queryTargetSchemaElementGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a schema element from a "mapTo" schema type.
     *
     * @param schemaElementGUID        unique identifier of the primary key
     * @param schemaTypeGUID             unique identifier of the foreign key
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachQueryTarget(String        schemaElementGUID,
                                  String        schemaTypeGUID,
                                  DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        schemaAttributeHandler.detachQueryTarget(connectorUserId, schemaElementGUID, schemaTypeGUID, deleteOptions);
    }


    /**
     * Add the PrimaryKey classification to a relational column.
     *
     * @param relationalColumnGUID    unique identifier of the schema attribute.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addPrimaryKeyClassification(String                relationalColumnGUID,
                                            PrimaryKeyProperties  properties,
                                            MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        schemaAttributeHandler.addPrimaryKeyClassification(connectorUserId, relationalColumnGUID, properties, metadataSourceOptions);
    }


    /**
     * Remove the PrimaryKey classification for a relational column.
     *
     * @param relationalColumnGUID    unique identifier of the schema attribute.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removePrimaryKeyClassification(String                relationalColumnGUID,
                                               MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        schemaAttributeHandler.removePrimaryKeyClassification(connectorUserId, relationalColumnGUID, metadataSourceOptions);
    }


    /**
     * Add the TypeEmbeddedAttribute classification to the schema attribute.
     *
     * @param schemaAttributeGUID    unique identifier of the schema attribute.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addTypeEmbeddedAttribute(String                          schemaAttributeGUID,
                                         TypeEmbeddedAttributeProperties properties,
                                         MetadataSourceOptions           metadataSourceOptions) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        schemaAttributeHandler.addTypeEmbeddedAttribute(connectorUserId, schemaAttributeGUID, properties, metadataSourceOptions);
    }


    /**
     * Update the TypeEmbeddedAttribute classification for the schema attribute.
     *
     * @param schemaAttributeGUID    unique identifier of the schema attribute.
     * @param updateOptions provides a structure for the additional options when updating a classification.
     * @param properties            properties for the classification
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateTypeEmbeddedAttribute(String                          schemaAttributeGUID,
                                            UpdateOptions                   updateOptions,
                                            TypeEmbeddedAttributeProperties properties) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        schemaAttributeHandler.updateTypeEmbeddedAttribute(connectorUserId, schemaAttributeGUID, updateOptions, properties);
    }


    /**
     * Remove the TypeEmbeddedAttribute classification from the schema attribute.
     *
     * @param schemaAttributeGUID    unique identifier of the schema attribute.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeTypeEmbeddedAttribute(String                schemaAttributeGUID,
                                            MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        schemaAttributeHandler.removeTypeEmbeddedAttribute(connectorUserId, schemaAttributeGUID, metadataSourceOptions);
    }


    /**
     * Add the CalculatedValue classification to the schema attribute.
     *
     * @param schemaAttributeGUID    unique identifier of the schema attribute.
     * @param properties            properties for the classification
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void addCalculatedValue(String                    schemaAttributeGUID,
                                   CalculatedValueProperties properties,
                                   MetadataSourceOptions     metadataSourceOptions) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        schemaAttributeHandler.addCalculatedValue(connectorUserId, schemaAttributeGUID, properties, metadataSourceOptions);
    }


    /**
     * Update the CalculatedValue classification for the schema attribute.
     *
     * @param schemaAttributeGUID    unique identifier of the schema attribute.
     * @param updateOptions provides a structure for the additional options when updating a classification.
     * @param properties            properties for the classification
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateCalculatedValue(String                    schemaAttributeGUID,
                                      UpdateOptions             updateOptions,
                                      CalculatedValueProperties properties) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        schemaAttributeHandler.updateCalculatedValue(connectorUserId, schemaAttributeGUID, updateOptions, properties);
    }


    /**
     * Remove the CalculatedValue classification from the schema attribute.
     *
     * @param schemaAttributeGUID    unique identifier of the schema attribute.
     * @param metadataSourceOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void removeCalculatedValue(String                schemaAttributeGUID,
                                      MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        schemaAttributeHandler.removeCalculatedValue(connectorUserId, schemaAttributeGUID, metadataSourceOptions);
    }


    /**
     * Delete a schema attribute.
     *
     * @param schemaAttributeGUID       unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteSchemaAttribute(String        schemaAttributeGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        schemaAttributeHandler.deleteSchemaAttribute(connectorUserId, schemaAttributeGUID, deleteOptions);
    }


    /**
     * Returns the list of schema attributes with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getSchemaAttributesByName(String       name,
                                                                  QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        return schemaAttributeHandler.getSchemaAttributesByName(connectorUserId, name, queryOptions);
    }


    /**
     * Return the properties of a specific schema attribute.
     *
     * @param schemaAttributeGUID      unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getSchemaAttributeByGUID(String     schemaAttributeGUID,
                                                            GetOptions getOptions) throws InvalidParameterException,
                                                                                          PropertyServerException,
                                                                                          UserNotAuthorizedException
    {
        return schemaAttributeHandler.getSchemaAttributeByGUID(connectorUserId, schemaAttributeGUID, getOptions);
    }


    /**
     * Retrieve the list of schema attributes metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findSchemaAttributes(String        searchString,
                                                              SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException
    {
        return schemaAttributeHandler.findSchemaAttributes(connectorUserId, searchString, searchOptions);
    }


    /**
     * Returns the list of schema attributes nested under a schema attribute.
     *
     * @param schemaTypeGUID           unique identifier of the starting schema attribute
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getAttributesForSchemaType(String       schemaTypeGUID,
                                                                    QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        return schemaAttributeHandler.getAttributesForSchemaType(connectorUserId, schemaTypeGUID, queryOptions);
    }


    /**
     * Returns the list of schema attributes nested under a schema attribute.
     *
     * @param schemaAttributeGUID           unique identifier of the starting schema attribute
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getNestedSchemaAttributes(String       schemaAttributeGUID,
                                                                   QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        return schemaAttributeHandler.getNestedSchemaAttributes(connectorUserId, schemaAttributeGUID, queryOptions);
    }
}
