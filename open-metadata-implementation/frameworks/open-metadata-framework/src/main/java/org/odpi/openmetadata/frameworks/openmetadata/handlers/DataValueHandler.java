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
import org.odpi.openmetadata.frameworks.openmetadata.properties.datavalues.DataValueProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datavalues.DataValueSourceProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datavalues.EmbeddedDataValueProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datavalues.ProcessingVariableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * DataValueHandler describes how to maintain and query data values and their links to the elements that use and store them.
 */
public class DataValueHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public DataValueHandler(String             localServerName,
                            AuditLog           auditLog,
                            String             serviceName,
                            OpenMetadataClient openMetadataClient)
    {
        super(localServerName, auditLog, serviceName, openMetadataClient, OpenMetadataType.DATA_VALUE.typeName);
    }


    /**
     * Create a data value.
     *
     * @param userId                       calling user
     * @param newElementOptions            details of the element to create
     * @param initialClassifications       map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public String createDataValue(String                                userId,
                                  NewElementOptions                     newElementOptions,
                                  Map<String, ClassificationProperties> initialClassifications,
                                  DataValueProperties                   properties,
                                  RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                             PropertyServerException,
                                                                                                             UserNotAuthorizedException
    {
        final String methodName = "createDataValue";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent a data value using an existing metadata element as a template.  The
     * template defines additional classifications and relationships that should be added to the new element.
     *
     * @param userId                       calling user
     * @param templateOptions              details of the element to create
     * @param templateGUID                 the unique identifier of the existing element to copy
     * @param replacementProperties        properties of the new metadata element.  These override the template values
     * @param replacementClassifications   map of classification names to classification properties to include in the entity creation request. These override the template values
     * @param placeholderProperties        property name-to-property value map to replace any placeholder values in the template element - and their anchored elements, which are also copied as part of this operation
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public String createDataValueFromTemplate(String                                userId,
                                              TemplateOptions                       templateOptions,
                                              String                                templateGUID,
                                              EntityProperties                      replacementProperties,
                                              Map<String, ClassificationProperties> replacementClassifications,
                                              Map<String, String>                   placeholderProperties,
                                              RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                         PropertyServerException,
                                                                                                                         UserNotAuthorizedException
    {
        return super.createElementFromTemplate(userId,
                                               templateOptions,
                                               templateGUID,
                                               replacementProperties,
                                               replacementClassifications,
                                               placeholderProperties,
                                               parentRelationshipProperties);
    }


    /**
     * Update the properties of a data value.
     *
     * @param userId        calling user
     * @param dataValueGUID unique identifier of the data value
     * @param updateOptions provides a structure for the additional options when updating an element
     * @param properties    properties for the element
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public boolean updateDataValue(String              userId,
                                   String              dataValueGUID,
                                   UpdateOptions       updateOptions,
                                   DataValueProperties properties) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName                 = "updateDataValue";
        final String dataValueGUIDParameterName = "dataValueGUID";

        return super.updateElement(userId,
                                   dataValueGUID,
                                   dataValueGUIDParameterName,
                                   updateOptions,
                                   properties,
                                   methodName);
    }


    /**
     * Delete a data value.
     *
     * @param userId        calling user
     * @param dataValueGUID unique identifier of the data value
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public void deleteDataValue(String        userId,
                                String        dataValueGUID,
                                DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        final String methodName                 = "deleteDataValue";
        final String dataValueGUIDParameterName = "dataValueGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(dataValueGUID, dataValueGUIDParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, dataValueGUID, deleteOptions);
    }


    /**
     * Returns the list of data values with a particular name.
     *
     * @param userId       calling user
     * @param name         name of the element to return - match is full text match in qualifiedName, identifier or displayName
     * @param queryOptions multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public List<OpenMetadataRootElement> getDataValuesByName(String       userId,
                                                             String       name,
                                                             QueryOptions queryOptions) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        final String methodName = "getDataValuesByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.IDENTIFIER.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name);

        return super.getRootElementsByName(userId, name, propertyNames, queryOptions, methodName);
    }


    /**
     * Return the properties of a specific data value.
     *
     * @param userId        calling user
     * @param dataValueGUID unique identifier of the data value
     * @param getOptions    multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public OpenMetadataRootElement getDataValueByGUID(String     userId,
                                                      String     dataValueGUID,
                                                      GetOptions getOptions) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String methodName                 = "getDataValueByGUID";
        final String dataValueGUIDParameterName = "dataValueGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(dataValueGUID, dataValueGUIDParameterName, methodName);

        return super.getRootElementByGUID(userId, dataValueGUID, getOptions, methodName);
    }


    /**
     * Retrieve the list of data value metadata elements that contain the search string.
     *
     * @param userId        calling user
     * @param searchString  string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public List<OpenMetadataRootElement> findDataValues(String        userId,
                                                        String        searchString,
                                                        SearchOptions searchOptions) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String methodName = "findDataValues";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }


    /**
     * Attach a data value to the asset that describes the digital resource where the data is stored.
     *
     * @param userId                 calling user
     * @param dataValueGUID          unique identifier of the data value
     * @param assetGUID              unique identifier of the asset for the digital resource where the data is stored
     * @param makeAnchorOptions      options to control access to open metadata
     * @param relationshipProperties description of the relationship
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public void linkDataValueSource(String                    userId,
                                    String                    dataValueGUID,
                                    String                    assetGUID,
                                    MakeAnchorOptions         makeAnchorOptions,
                                    DataValueSourceProperties relationshipProperties) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName                 = "linkDataValueSource";
        final String dataValueGUIDParameterName = "dataValueGUID";
        final String assetGUIDParameterName     = "assetGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(dataValueGUID, dataValueGUIDParameterName, methodName);
        propertyHelper.validateGUID(assetGUID, assetGUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.DATA_VALUE_SOURCE_RELATIONSHIP.typeName,
                                                        dataValueGUID,
                                                        assetGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a data value from the asset that describes the digital resource where the data is stored.
     *
     * @param userId        calling user
     * @param dataValueGUID unique identifier of the data value
     * @param assetGUID     unique identifier of the asset for the digital resource where the data is stored
     * @param deleteOptions options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public void detachDataValueSource(String        userId,
                                      String        dataValueGUID,
                                      String        assetGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName                 = "detachDataValueSource";
        final String dataValueGUIDParameterName = "dataValueGUID";
        final String assetGUIDParameterName     = "assetGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(dataValueGUID, dataValueGUIDParameterName, methodName);
        propertyHelper.validateGUID(assetGUID, assetGUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.DATA_VALUE_SOURCE_RELATIONSHIP.typeName,
                                                        dataValueGUID,
                                                        assetGUID,
                                                        deleteOptions);
    }


    /**
     * Attach an element (such as a process) to a data value that is used to configure its processing.
     *
     * @param userId                 calling user
     * @param elementGUID            unique identifier of the element that uses the data value
     * @param dataValueGUID          unique identifier of the data value
     * @param makeAnchorOptions      options to control access to open metadata
     * @param relationshipProperties description of the relationship
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public void linkProcessingVariable(String                       userId,
                                       String                       elementGUID,
                                       String                       dataValueGUID,
                                       MakeAnchorOptions            makeAnchorOptions,
                                       ProcessingVariableProperties relationshipProperties) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        final String methodName                 = "linkProcessingVariable";
        final String elementGUIDParameterName   = "elementGUID";
        final String dataValueGUIDParameterName = "dataValueGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, elementGUIDParameterName, methodName);
        propertyHelper.validateGUID(dataValueGUID, dataValueGUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.PROCESSING_VARIABLE_RELATIONSHIP.typeName,
                                                        elementGUID,
                                                        dataValueGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach an element (such as a process) from a data value that is used to configure its processing.
     *
     * @param userId        calling user
     * @param elementGUID   unique identifier of the element that uses the data value
     * @param dataValueGUID unique identifier of the data value
     * @param deleteOptions options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public void detachProcessingVariable(String        userId,
                                         String        elementGUID,
                                         String        dataValueGUID,
                                         DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String methodName                 = "detachProcessingVariable";
        final String elementGUIDParameterName   = "elementGUID";
        final String dataValueGUIDParameterName = "dataValueGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, elementGUIDParameterName, methodName);
        propertyHelper.validateGUID(dataValueGUID, dataValueGUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.PROCESSING_VARIABLE_RELATIONSHIP.typeName,
                                                        elementGUID,
                                                        dataValueGUID,
                                                        deleteOptions);
    }


    /**
     * Embed a data value in another data value.
     *
     * @param userId                 calling user
     * @param parentDataValueGUID    unique identifier of the parent data value
     * @param embeddedDataValueGUID  unique identifier of the embedded data value
     * @param makeAnchorOptions      options to control access to open metadata
     * @param relationshipProperties description of the relationship
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public void linkEmbeddedDataValue(String                      userId,
                                      String                      parentDataValueGUID,
                                      String                      embeddedDataValueGUID,
                                      MakeAnchorOptions           makeAnchorOptions,
                                      EmbeddedDataValueProperties relationshipProperties) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        final String methodName                         = "linkEmbeddedDataValue";
        final String parentDataValueGUIDParameterName   = "parentDataValueGUID";
        final String embeddedDataValueGUIDParameterName = "embeddedDataValueGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(parentDataValueGUID, parentDataValueGUIDParameterName, methodName);
        propertyHelper.validateGUID(embeddedDataValueGUID, embeddedDataValueGUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.EMBEDDED_DATA_VALUE_RELATIONSHIP.typeName,
                                                        parentDataValueGUID,
                                                        embeddedDataValueGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Remove an embedded data value from its parent data value.
     *
     * @param userId                calling user
     * @param parentDataValueGUID   unique identifier of the parent data value
     * @param embeddedDataValueGUID unique identifier of the embedded data value
     * @param deleteOptions         options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public void detachEmbeddedDataValue(String        userId,
                                        String        parentDataValueGUID,
                                        String        embeddedDataValueGUID,
                                        DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                            PropertyServerException,
                                                                            UserNotAuthorizedException
    {
        final String methodName                         = "detachEmbeddedDataValue";
        final String parentDataValueGUIDParameterName   = "parentDataValueGUID";
        final String embeddedDataValueGUIDParameterName = "embeddedDataValueGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(parentDataValueGUID, parentDataValueGUIDParameterName, methodName);
        propertyHelper.validateGUID(embeddedDataValueGUID, embeddedDataValueGUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.EMBEDDED_DATA_VALUE_RELATIONSHIP.typeName,
                                                        parentDataValueGUID,
                                                        embeddedDataValueGUID,
                                                        deleteOptions);
    }
}
