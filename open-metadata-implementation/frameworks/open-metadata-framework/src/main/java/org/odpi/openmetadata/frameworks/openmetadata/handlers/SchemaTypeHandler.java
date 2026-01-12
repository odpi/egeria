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
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelatedMetadataElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.schema.SchemaTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * SchemaTypeHandler provides methods to define schema types
 */
public class SchemaTypeHandler extends OpenMetadataHandlerBase
{

    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param localServiceName       local service name
     * @param openMetadataClient     access to open metadata
     */
    public SchemaTypeHandler(String             localServerName,
                             AuditLog           auditLog,
                             String             localServiceName,
                             OpenMetadataClient openMetadataClient)
    {
        super(localServerName,
              auditLog,
              localServiceName,
              openMetadataClient,
              OpenMetadataType.SCHEMA_TYPE.typeName);
    }


    /**
     * Create a new handler.
     *
     * @param template        properties to copy
     * @param specificTypeName   subtype to control handler
     */
    public SchemaTypeHandler(SchemaTypeHandler template,
                             String            specificTypeName)
    {
        super(template, specificTypeName);
    }


    /**
     * Retrieve or create the schema type that is attached to the element (typically an asset or port)
     * via the Schema relationship.
     *
     * @param elementGUID unique identifier for the starting element
     * @param schemaTypeTypeName type name of the schema type to create, if needed
     * @return schema type element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s),
     *                                    or there are multiple schemas attached.
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getSchemaTypeForElement(String                userId,
                                                           String                elementGUID,
                                                           String                schemaTypeTypeName,
                                                           MetadataSourceOptions metadataSourceOptions) throws InvalidParameterException,
                                                                                                               PropertyServerException,
                                                                                                               UserNotAuthorizedException
    {
        final String methodName = "getSchemaTypeForElement";

        RelatedMetadataElement schemaType = openMetadataClient.getRelatedMetadataElement(userId,
                                                                                         elementGUID,
                                                                                         1,
                                                                                         OpenMetadataType.SCHEMA_RELATIONSHIP.typeName,
                                                                                         new GetOptions(metadataSourceOptions));

        if (schemaType == null)
        {
            SchemaTypeProperties schemaTypeProperties = new SchemaTypeProperties();

            schemaTypeProperties.setTypeName(schemaTypeTypeName);
            schemaTypeProperties.setQualifiedName(schemaTypeTypeName + " for " + elementGUID);

            NewElementOptions newElementOptions = new NewElementOptions(metadataSourceOptions);

            newElementOptions.setIsOwnAnchor(false);
            newElementOptions.setAnchorGUID(elementGUID);
            newElementOptions.setParentGUID(elementGUID);
            newElementOptions.setParentAtEnd1(true);
            newElementOptions.setParentRelationshipTypeName(OpenMetadataType.SCHEMA_RELATIONSHIP.typeName);

            String schemaTypeGUID = this.createSchemaType(userId,
                                                          newElementOptions,
                                                          null,
                                                          schemaTypeProperties,
                                                          null);

            return this.getSchemaTypeByGUID(userId, schemaTypeGUID, new GetOptions(metadataSourceOptions));
        }
        else
        {
            return super.convertRootElement(userId, schemaType, new QueryOptions(metadataSourceOptions), methodName);
        }
    }


    /**
     * Create a new schema type.
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
    public String createSchemaType(String                                userId,
                                   NewElementOptions                     newElementOptions,
                                   Map<String, ClassificationProperties> initialClassifications,
                                   SchemaTypeProperties                  properties,
                                   RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException
    {
        final String methodName = "createSchemaType";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent a schema type using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new schema type.
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
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSchemaTypeFromTemplate(String                 userId,
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
     * Update the properties of a schema type.
     *
     * @param userId                 userId of user making request.
     * @param schemaTypeGUID       unique identifier of the schema type (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateSchemaType(String               userId,
                                    String               schemaTypeGUID,
                                    UpdateOptions        updateOptions,
                                    SchemaTypeProperties properties) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        final String methodName        = "updateSchemaType";
        final String guidParameterName = "schemaTypeGUID";

        return super.updateElement(userId,
                                   schemaTypeGUID,
                                   guidParameterName,
                                   updateOptions,
                                   properties,
                                   methodName);
    }


    /**
     * Attach an element to a Schema Type that describes its structure.
     *
     * @param userId                 userId of user making request
     * @param elementGUID       unique identifier of the element (eg asset, port, ...)
     * @param schemaTypeGUID            unique identifier of the IT profile
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSchema(String                    userId,
                           String                    elementGUID,
                           String                    schemaTypeGUID,
                           MakeAnchorOptions         makeAnchorOptions,
                           SchemaProperties          relationshipProperties) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        final String methodName            = "linkSchema";
        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "schemaTypeGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(schemaTypeGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.SCHEMA_RELATIONSHIP.typeName,
                                                        elementGUID,
                                                        schemaTypeGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach an element from the schema that describes its structure.
     *
     * @param userId                 userId of user making request.
     * @param elementGUID       unique identifier of the element (eg asset, port, ...)
     * @param schemaTypeGUID          unique identifier of the IT profile
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSchema(String        userId,
                             String        elementGUID,
                             String        schemaTypeGUID,
                             DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                 PropertyServerException,
                                                                 UserNotAuthorizedException
    {
        final String methodName = "detachSchema";
        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "schemaTypeGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(schemaTypeGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.SCHEMA_RELATIONSHIP.typeName,
                                                        schemaTypeGUID,
                                                        schemaTypeGUID,
                                                        deleteOptions);
    }


    /**
     * Delete a schema type.
     *
     * @param userId                 userId of user making request.
     * @param schemaTypeGUID       unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteSchemaType(String        userId,
                                 String        schemaTypeGUID,
                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String methodName        = "deleteSchemaType";
        final String guidParameterName = "schemaTypeGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(schemaTypeGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, schemaTypeGUID, deleteOptions);
    }


    /**
     * Returns the list of schema types with a particular name.
     *
     * @param userId                 userId of user making request
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getSchemaTypesByName(String       userId,
                                                              String       name,
                                                              QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        final String methodName = "getSchemaTypesByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.IDENTIFIER.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name);

        return super.getRootElementsByName(userId, name, propertyNames, queryOptions, methodName);
    }


    /**
     * Return the properties of a specific schema type.
     *
     * @param userId                 userId of user making request
     * @param schemaTypeGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getSchemaTypeByGUID(String     userId,
                                                       String     schemaTypeGUID,
                                                       GetOptions getOptions) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        final String methodName = "getSchemaTypeByGUID";

        return super.getRootElementByGUID(userId, schemaTypeGUID, getOptions, methodName);
    }


    /**
     * Return the properties of a specific schema type retrieved using an associated userId.
     *
     * @param userId                 userId of user making request
     * @param assetGUID         identifier of user
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getSchemaTypeForAsset(String     userId,
                                                         String     assetGUID,
                                                         GetOptions getOptions) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        final String methodName        = "getSchemaTypeForAsset";
        final String guidParameterName = "assetGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(assetGUID, guidParameterName, methodName);

        List<OpenMetadataRootElement> attachedSchemas = super.getRelatedRootElements(userId,
                                                                                     assetGUID,
                                                                                     guidParameterName,
                                                                                     1,
                                                                                     OpenMetadataType.SCHEMA_RELATIONSHIP.typeName,
                                                                                     OpenMetadataType.SCHEMA_TYPE.typeName,
                                                                                     new QueryOptions(getOptions),
                                                                                     methodName);

        /*
         * There should be only one so we return the first non-null result.
         */
        if (attachedSchemas != null)
        {
            for (OpenMetadataRootElement returnedSchema : attachedSchemas)
            {
                if (returnedSchema != null)
                {
                    return returnedSchema;
                }
            }
        }

        return null;
    }


    /**
     * Retrieve the list of schema types metadata elements that contain the search string.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findSchemaTypes(String        userId,
                                                         String        searchString,
                                                         SearchOptions searchOptions) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName  = "findSchemaTypes";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }
}
