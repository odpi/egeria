/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.SchemaTypeHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.EntityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

import java.util.List;
import java.util.Map;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.databases.RelationalDBSchemaProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APIOperationsProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APIHeaderProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APIRequestProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.apis.APIResponseProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaTypeOptionProperties;

/**
 * Provides services for connectors to work with Schema Type elements.
 */
public class SchemaTypeClient extends ConnectorContextClientBase
{
    private final SchemaTypeHandler schemaTypeHandler;


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
    public SchemaTypeClient(ConnectorContextBase     parentContext,
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

        this.schemaTypeHandler = new SchemaTypeHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template client to copy
     * @param specificTypeName type name override
     */
    public SchemaTypeClient(SchemaTypeClient template,
                            String           specificTypeName)
    {
        super(template);

        this.schemaTypeHandler = new SchemaTypeHandler(template.schemaTypeHandler, specificTypeName);
    }


    /**
     * Retrieve or create the schema type that is attached to the element (typically an asset or port)
     * via the Schema relationship.
     *
     * @param elementGUID unique identifier for the starting element
     * @param schemaTypeTypeName type name of the schema type to create, if needed
     * @return schema type element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s),
     *                                    or there are multiple schemas attached.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getSchemaTypeForElement(String elementGUID,
                                                           String schemaTypeTypeName) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        return schemaTypeHandler.getSchemaTypeForElement(connectorUserId, elementGUID,schemaTypeTypeName, this.getMetadataSourceOptions());
    }


    /**
     * Create a new schema type.
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
    public String createSchemaType(NewElementOptions                     newElementOptions,
                                   Map<String, ClassificationProperties> initialClassifications,
                                   SchemaTypeProperties                  properties,
                                   RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException
    {
        String elementGUID = schemaTypeHandler.createSchemaType(connectorUserId, newElementOptions, initialClassifications, properties, parentRelationshipProperties);

        if (parentContext.getActivityReportWriter() != null)
        {
            parentContext.getActivityReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Create a new metadata element to represent a schema type using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new schemaType.
     *
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing schemaType to copy
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param replacementClassifications map of classification names to classification properties to include in the entity creation request. These override the template values.
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public String createSchemaTypeFromTemplate(TemplateOptions                       templateOptions,
                                               String                                templateGUID,
                                               EntityProperties                      replacementProperties,
                                               Map<String, ClassificationProperties> replacementClassifications,
                                               Map<String, String>                   placeholderProperties,
                                               RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                          UserNotAuthorizedException,
                                                                                                                          PropertyServerException
    {
        String elementGUID = schemaTypeHandler.createSchemaTypeFromTemplate(connectorUserId, templateOptions, templateGUID, replacementProperties, replacementClassifications, placeholderProperties, parentRelationshipProperties);

        if (parentContext.getActivityReportWriter() != null)
        {
            parentContext.getActivityReportWriter().reportElementCreation(elementGUID);
        }

        return elementGUID;
    }


    /**
     * Update the properties of a schema type.
     *
     * @param schemaTypeGUID       unique identifier of the schemaType (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateSchemaType(String               schemaTypeGUID,
                                    UpdateOptions        updateOptions,
                                    SchemaTypeProperties properties) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        boolean updateOccurred = schemaTypeHandler.updateSchemaType(connectorUserId, schemaTypeGUID, updateOptions, properties);

        if ((updateOccurred) && (parentContext.getActivityReportWriter() != null))
        {
            parentContext.getActivityReportWriter().reportElementUpdate(schemaTypeGUID);
        }

        return updateOccurred;
    }


    /**
     * Attach an asset to a Root Schema Type.
     *
     * @param elementGUID       unique identifier of the element (eg asset, port, ...)
     * @param schemaTypeGUID            unique identifier of the IT profile
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSchema(String                    elementGUID,
                           String                    schemaTypeGUID,
                           MakeAnchorOptions         makeAnchorOptions,
                           SchemaProperties          relationshipProperties) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        schemaTypeHandler.linkSchema(connectorUserId, elementGUID, schemaTypeGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach an element from the schema type that describes its structure.
     *
     * @param assetGUID              unique identifier of the asset
     * @param schemaTypeGUID          unique identifier of the IT profile
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSchema(String        assetGUID,
                             String        schemaTypeGUID,
                             DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        schemaTypeHandler.detachSchema(connectorUserId, assetGUID, schemaTypeGUID, deleteOptions);
    }


    /**
     * Delete a schemaType.
     *
     * @param schemaTypeGUID       unique identifier of the element
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteSchemaType(String        schemaTypeGUID,
                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        schemaTypeHandler.deleteSchemaType(connectorUserId, schemaTypeGUID, deleteOptions);

        if (parentContext.getActivityReportWriter() != null)
        {
            parentContext.getActivityReportWriter().reportElementDelete(schemaTypeGUID);
        }
    }


    /**
     * Returns the list of schemaTypes with a particular name.
     *
     * @param name                   name of the element to return - match is full text match in qualifiedName, resourceName or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getSchemaTypesByName(String       name,
                                                              QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        return schemaTypeHandler.getSchemaTypesByName(connectorUserId, name, queryOptions);
    }


    /**
     * Return the properties of a specific schemaType.
     *
     * @param schemaTypeGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getSchemaTypeByGUID(String     schemaTypeGUID,
                                                       GetOptions getOptions) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        return schemaTypeHandler.getSchemaTypeByGUID(connectorUserId, schemaTypeGUID, getOptions);
    }


    /**
     * Return the properties of a specific schema type retrieved using an associated userId.
     *
     * @param assetGUID         identifier of user
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getSchemaTypeForAsset(String     assetGUID,
                                                         GetOptions getOptions) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        return schemaTypeHandler.getSchemaTypeForAsset(connectorUserId, assetGUID, getOptions);
    }


    /**
     * Retrieve the list of schemaTypes metadata elements that contain the search string and show which solution components (if any) are attached to it.
     * The returned schemaTypes include a list of the components that are associated with it.
     * The search string is treated as a regular expression.
     *
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findSchemaTypes(String        searchString,
                                                         SearchOptions searchOptions) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        return schemaTypeHandler.findSchemaTypes(connectorUserId, searchString, searchOptions);
    }


    /**
     * Attach a relational database schema type to the list that contains it.
     *
     * @param databaseSchemaTypeListGUID unique identifier of the relational database schema type list
     * @param relationalDBSchemaTypeGUID unique identifier of the relational database schema type
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkRelationalDBSchema(String                        databaseSchemaTypeListGUID,
                                  String                        relationalDBSchemaTypeGUID,
                                  MakeAnchorOptions             makeAnchorOptions,
                                  RelationalDBSchemaProperties  relationshipProperties) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        schemaTypeHandler.linkRelationalDBSchema(connectorUserId, databaseSchemaTypeListGUID, relationalDBSchemaTypeGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a relational database schema type from the list that contained it.
     *
     * @param databaseSchemaTypeListGUID unique identifier of the relational database schema type list
     * @param relationalDBSchemaTypeGUID unique identifier of the relational database schema type
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachRelationalDBSchema(String        databaseSchemaTypeListGUID,
                                    String        relationalDBSchemaTypeGUID,
                                    DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        schemaTypeHandler.detachRelationalDBSchema(connectorUserId, databaseSchemaTypeListGUID, relationalDBSchemaTypeGUID, deleteOptions);
    }


    /*
     * =====================================================================================================================
     * API and schema option relationships
     */


    /**
     * Attach an API operation to the API schema type that contains it.
     *
     * @param apiSchemaTypeGUID unique identifier of the API schema type
     * @param apiOperationGUID unique identifier of the API operation
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkAPIOperations(String                   apiSchemaTypeGUID,
                             String                   apiOperationGUID,
                             MakeAnchorOptions        makeAnchorOptions,
                             APIOperationsProperties  relationshipProperties) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        schemaTypeHandler.linkAPIOperations(connectorUserId, apiSchemaTypeGUID, apiOperationGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach an API operation from the API schema type that contained it.
     *
     * @param apiSchemaTypeGUID unique identifier of the API schema type
     * @param apiOperationGUID unique identifier of the API operation
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAPIOperations(String        apiSchemaTypeGUID,
                               String        apiOperationGUID,
                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        schemaTypeHandler.detachAPIOperations(connectorUserId, apiSchemaTypeGUID, apiOperationGUID, deleteOptions);
    }


    /**
     * Attach a schema type to the API operation that uses it as its header.
     *
     * @param apiOperationGUID unique identifier of the API operation
     * @param schemaTypeGUID unique identifier of the schema type describing the header
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkAPIHeader(String               apiOperationGUID,
                         String               schemaTypeGUID,
                         MakeAnchorOptions    makeAnchorOptions,
                         APIHeaderProperties  relationshipProperties) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        schemaTypeHandler.linkAPIHeader(connectorUserId, apiOperationGUID, schemaTypeGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a schema type from the API operation that used it as its header.
     *
     * @param apiOperationGUID unique identifier of the API operation
     * @param schemaTypeGUID unique identifier of the schema type describing the header
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAPIHeader(String        apiOperationGUID,
                           String        schemaTypeGUID,
                           DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                         PropertyServerException,
                                                                         UserNotAuthorizedException
    {
        schemaTypeHandler.detachAPIHeader(connectorUserId, apiOperationGUID, schemaTypeGUID, deleteOptions);
    }


    /**
     * Attach a schema type to the API operation that uses it as its request.
     *
     * @param apiOperationGUID unique identifier of the API operation
     * @param schemaTypeGUID unique identifier of the schema type describing the request
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkAPIRequest(String                apiOperationGUID,
                          String                schemaTypeGUID,
                          MakeAnchorOptions     makeAnchorOptions,
                          APIRequestProperties  relationshipProperties) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        schemaTypeHandler.linkAPIRequest(connectorUserId, apiOperationGUID, schemaTypeGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a schema type from the API operation that used it as its request.
     *
     * @param apiOperationGUID unique identifier of the API operation
     * @param schemaTypeGUID unique identifier of the schema type describing the request
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAPIRequest(String        apiOperationGUID,
                            String        schemaTypeGUID,
                            DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        schemaTypeHandler.detachAPIRequest(connectorUserId, apiOperationGUID, schemaTypeGUID, deleteOptions);
    }


    /**
     * Attach a schema type to the API operation that uses it as its response.
     *
     * @param apiOperationGUID unique identifier of the API operation
     * @param schemaTypeGUID unique identifier of the schema type describing the response
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkAPIResponse(String                 apiOperationGUID,
                           String                 schemaTypeGUID,
                           MakeAnchorOptions      makeAnchorOptions,
                           APIResponseProperties  relationshipProperties) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        schemaTypeHandler.linkAPIResponse(connectorUserId, apiOperationGUID, schemaTypeGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a schema type from the API operation that used it as its response.
     *
     * @param apiOperationGUID unique identifier of the API operation
     * @param schemaTypeGUID unique identifier of the schema type describing the response
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAPIResponse(String        apiOperationGUID,
                             String        schemaTypeGUID,
                             DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        schemaTypeHandler.detachAPIResponse(connectorUserId, apiOperationGUID, schemaTypeGUID, deleteOptions);
    }


    /**
     * Attach a schema type to a schema element that may optionally use it.
     *
     * @param schemaElementGUID unique identifier of the schema element
     * @param schemaTypeGUID unique identifier of the schema type that is one of its options
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSchemaTypeOption(String                      schemaElementGUID,
                                String                      schemaTypeGUID,
                                MakeAnchorOptions           makeAnchorOptions,
                                SchemaTypeOptionProperties  relationshipProperties) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        schemaTypeHandler.linkSchemaTypeOption(connectorUserId, schemaElementGUID, schemaTypeGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a schema type from a schema element that may optionally have used it.
     *
     * @param schemaElementGUID unique identifier of the schema element
     * @param schemaTypeGUID unique identifier of the schema type that is one of its options
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSchemaTypeOption(String        schemaElementGUID,
                                  String        schemaTypeGUID,
                                  DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        schemaTypeHandler.detachSchemaTypeOption(connectorUserId, schemaElementGUID, schemaTypeGUID, deleteOptions);
    }
}
