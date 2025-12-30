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
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataClassCompositionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataClassHierarchyProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.DataClassProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Data class handler describes how to maintain and query data classes..
 */
public class DataClassHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public DataClassHandler(String             localServerName,
                            AuditLog           auditLog,
                            String             serviceName,
                            OpenMetadataClient openMetadataClient)
    {
        super(localServerName, auditLog, serviceName,openMetadataClient, OpenMetadataType.DATA_CLASS.typeName);
    }


    /**
     * Create a new data class.
     *
     * @param userId                       userId of user making request.
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createDataClass(String                                userId,
                                  NewElementOptions                     newElementOptions,
                                  Map<String, ClassificationProperties> initialClassifications,
                                  DataClassProperties                   properties,
                                  RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                             PropertyServerException,
                                                                                                             UserNotAuthorizedException
    {
        final String methodName = "createDataClass";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent a data class using an existing  element as a template.
     * The template defines additional classifications and relationships that should be added to the new data class.
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
    public String createDataClassFromTemplate(String                 userId,
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
     * Update the properties of a data class.
     *
     * @param userId                 userId of user making request.
     * @param dataClassGUID          unique identifier of the data class (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateDataClass(String              userId,
                                   String              dataClassGUID,
                                   UpdateOptions       updateOptions,
                                   DataClassProperties properties) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName = "updateDataClass";
        final String guidParameterName = "dataClassGUID";

        return super.updateElement(userId,
                                   dataClassGUID,
                                   guidParameterName,
                                   updateOptions,
                                   properties,
                                   methodName);
    }


    /**
     * Connect two data classes to show that one is used by the other when it is validating (typically a complex data item).
     *
     * @param userId                 userId of user making request
     * @param parentDataClassGUID    unique identifier of the parent data class
     * @param childDataClassGUID     unique identifier of the chile data class
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkNestedDataClass(String                         userId,
                                    String                         parentDataClassGUID,
                                    String                         childDataClassGUID,
                                    MakeAnchorOptions              makeAnchorOptions,
                                    DataClassCompositionProperties relationshipProperties) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        final String methodName = "linkNestedDataClass";
        final String end1GUIDParameterName = "parentDataClassGUID";
        final String end2GUIDParameterName = "childDataClassGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(parentDataClassGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(childDataClassGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.DATA_CLASS_COMPOSITION_RELATIONSHIP.typeName,
                                                        parentDataClassGUID,
                                                        childDataClassGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach two nested data classes from one another.
     *
     * @param userId                 userId of user making request.
     * @param parentDataClassGUID    unique identifier of the  parent data class.
     * @param childDataClassGUID     unique identifier of the child data class.
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachNestedDataClass(String        userId,
                                      String        parentDataClassGUID,
                                      String        childDataClassGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName = "detachNestedDataClass";

        final String end1GUIDParameterName = "parentDataClassGUID";
        final String end2GUIDParameterName = "childDataClassGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(parentDataClassGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(childDataClassGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.DATA_CLASS_COMPOSITION_RELATIONSHIP.typeName,
                                                        parentDataClassGUID,
                                                        childDataClassGUID,
                                                        deleteOptions);
    }


    /**
     * Connect two data classes to show that one provides a more specialist evaluation.
     *
     * @param userId                 userId of user making request
     * @param parentDataClassGUID    unique identifier of the more generic data class
     * @param childDataClassGUID     unique identifier of the more specialized data class
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSpecializedDataClass(String                       userId,
                                         String                       parentDataClassGUID,
                                         String                       childDataClassGUID,
                                         MakeAnchorOptions            makeAnchorOptions,
                                         DataClassHierarchyProperties relationshipProperties) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        final String methodName = "linkSpecializedDataClass";
        final String end1GUIDParameterName = "parentDataClassGUID";
        final String end2GUIDParameterName = "childDataClassGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(parentDataClassGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(childDataClassGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.DATA_CLASS_HIERARCHY_RELATIONSHIP.typeName,
                                                        parentDataClassGUID,
                                                        childDataClassGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach two data classes from one another.
     *
     * @param userId                 userId of user making request.
     * @param parentDataClassGUID    unique identifier of the more generic data class
     * @param childDataClassGUID     unique identifier of the more specialized
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSpecializedDataClass(String        userId,
                                           String        parentDataClassGUID,
                                           String        childDataClassGUID,
                                           DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        final String methodName = "detachSpecializedDataClass";

        final String end1GUIDParameterName = "parentDataClassGUID";
        final String end2GUIDParameterName = "childDataClassGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(parentDataClassGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(childDataClassGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.DATA_CLASS_HIERARCHY_RELATIONSHIP.typeName,
                                                        parentDataClassGUID,
                                                        childDataClassGUID,
                                                        deleteOptions);
    }


    /**
     * Delete a data class.
     *
     * @param userId                 userId of user making request.
     * @param dataClassGUID          unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteDataClass(String        userId,
                                String        dataClassGUID,
                                DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                    PropertyServerException,
                                                                    UserNotAuthorizedException
    {
        final String methodName = "deleteDataClass";
        final String guidParameterName = "dataClassGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(dataClassGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, dataClassGUID, deleteOptions);
    }


    /**
     * Returns the list of data classes with a particular name.
     *
     * @param userId                 userId of user making request
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getDataClassesByName(String       userId,
                                                              String       name,
                                                              QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                PropertyServerException,
                                                                                                UserNotAuthorizedException
    {
        final String methodName = "getDataClassesByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.IDENTIFIER.name,
                                                   OpenMetadataProperty.NAMESPACE.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name);

        return super.getRootElementsByName(userId, name, propertyNames, queryOptions, methodName);
    }


    /**
     * Return the properties of a specific data class.
     *
     * @param userId                 userId of user making request
     * @param dataClassGUID          unique identifier of the required element
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getDataClassByGUID(String     userId,
                                                      String     dataClassGUID,
                                                      GetOptions getOptions) throws InvalidParameterException,
                                                                                    PropertyServerException,
                                                                                    UserNotAuthorizedException
    {
        final String methodName = "getDataClassByGUID";

        return super.getRootElementByGUID(userId, dataClassGUID, getOptions, methodName);
    }


    /**
     * Retrieve the list of data classes metadata elements that contain the search string.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findDataClasses(String        userId,
                                                         String        searchString,
                                                         SearchOptions searchOptions) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException
    {
        final String methodName = "findDataClasses";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }
}
