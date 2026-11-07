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
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Data value specification handler describes how to maintain and query data value specifications and data grains.
 */
public class DataValueSpecificationHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public DataValueSpecificationHandler(String             localServerName,
                                         AuditLog           auditLog,
                                         String             serviceName,
                                         OpenMetadataClient openMetadataClient)
    {
        super(localServerName, auditLog, serviceName,openMetadataClient, OpenMetadataType.DATA_VALUE_SPECIFICATION.typeName);
    }


    /**
     * Create a new data value specification.
     *
     * @param userId                       userId of the user making the request.
     * @param newElementOptions details of the element to create
     * @param initialClassifications map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createDataValueSpecification(String                                userId,
                                               NewElementOptions                     newElementOptions,
                                               Map<String, ClassificationProperties> initialClassifications,
                                               DataValueSpecificationProperties      properties,
                                               RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                          PropertyServerException,
                                                                                                                          UserNotAuthorizedException
    {
        final String methodName = "createDataValueSpecification";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent a data value specification using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new data value specification.
     *
     * @param userId                       calling user
     * @param templateOptions details of the element to create
     * @param templateGUID the unique identifier of the existing element to copy
     * @param replacementProperties properties of the new metadata element.  These override the template values
     * @param replacementClassifications map of classification names to classification properties to include in the entity creation request. These override the template values.
     * @param placeholderProperties property name-to-property value map to replace any placeholder values in the
     *                              template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     *
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public String createDataValueSpecificationFromTemplate(String                                userId,
                                                           TemplateOptions                       templateOptions,
                                                           String                                templateGUID,
                                                           EntityProperties                      replacementProperties,
                                                           Map<String, ClassificationProperties> replacementClassifications,
                                                           Map<String, String>                   placeholderProperties,
                                                           RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                                      UserNotAuthorizedException,
                                                                                                                                      PropertyServerException
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
     * Update the properties of a data value specification.
     *
     * @param userId                 userId of the user making the request.
     * @param dataValueSpecificationGUID          unique identifier of the data value specification (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateDataValueSpecification(String                           userId,
                                                String                           dataValueSpecificationGUID,
                                                UpdateOptions                    updateOptions,
                                                DataValueSpecificationProperties properties) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        final String methodName = "updateDataValueSpecification";
        final String guidParameterName = "dataValueSpecificationGUID";

        return super.updateElement(userId,
                                   dataValueSpecificationGUID,
                                   guidParameterName,
                                   updateOptions,
                                   properties,
                                   methodName);
    }


    /**
     * Connect a data value to an element to show that it is part of its definition.
     *
     * @param userId                 userId of the user making the request
     * @param dataDefinitionGUID    unique identifier of the more generic data value specification
     * @param dataValueSpecificationGUID     unique identifier of the more specialized data value specification
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkDataValueDefinition(String                        userId,
                                        String                        dataDefinitionGUID,
                                        String                        dataValueSpecificationGUID,
                                        MakeAnchorOptions             makeAnchorOptions,
                                        DataValueDefinitionProperties relationshipProperties) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        final String methodName = "linkDataValueDefinition";
        final String end1GUIDParameterName = "dataDefinitionGUID";
        final String end2GUIDParameterName = "dataValueSpecificationGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(dataDefinitionGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(dataValueSpecificationGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.DATA_VALUE_DEFINITION_RELATIONSHIP.typeName,
                                                        dataDefinitionGUID,
                                                        dataValueSpecificationGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a data value from an element to show that it is no longer part of its definition.
     *
     * @param userId                 userId of the user making the request.
     * @param dataDefinitionGUID    unique identifier of the more generic data value specification
     * @param dataValueSpecificationGUID     unique identifier of the more specialized
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachDataValueDefinition(String        userId,
                                          String        dataDefinitionGUID,
                                          String        dataValueSpecificationGUID,
                                          DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String methodName = "detachDataValueDefinition";

        final String end1GUIDParameterName = "dataDefinitionGUID";
        final String end2GUIDParameterName = "dataValueSpecificationGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(dataDefinitionGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(dataValueSpecificationGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.DATA_VALUE_DEFINITION_RELATIONSHIP.typeName,
                                                        dataDefinitionGUID,
                                                        dataValueSpecificationGUID,
                                                        deleteOptions);
    }


    /**
     * Connect a data value to an element to show that it describes its data values.
     *
     * @param userId                 userId of the user making the request
     * @param elementGUID    unique identifier of the more generic data value specification
     * @param dataValueSpecificationGUID     unique identifier of the more specialized data value specification
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkDataValueAssignment(String                        userId,
                                        String                        elementGUID,
                                        String                        dataValueSpecificationGUID,
                                        MakeAnchorOptions             makeAnchorOptions,
                                        DataValueAssignmentProperties relationshipProperties) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        final String methodName = "linkDataValueAssignment";
        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "dataValueSpecificationGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(dataValueSpecificationGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.DATA_VALUE_ASSIGNMENT_RELATIONSHIP.typeName,
                                                        elementGUID,
                                                        dataValueSpecificationGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a data value from an element to show that it no longer describes its data values.
     *
     * @param userId                 userId of the user making the request.
     * @param elementGUID    unique identifier of the more generic data value specification
     * @param dataValueSpecificationGUID     unique identifier of the more specialized
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachDataValueAssignment(String        userId,
                                          String        elementGUID,
                                          String        dataValueSpecificationGUID,
                                          DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        final String methodName = "detachDataValueAssignment";

        final String end1GUIDParameterName = "elementGUID";
        final String end2GUIDParameterName = "dataValueSpecificationGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(dataValueSpecificationGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.DATA_VALUE_ASSIGNMENT_RELATIONSHIP.typeName,
                                                        elementGUID,
                                                        dataValueSpecificationGUID,
                                                        deleteOptions);
    }

    /**
     * Connect two data value specifications to show that one provides a more specialist evaluation.
     *
     * @param userId                 userId of the user making the request
     * @param parentDataValueSpecificationGUID    unique identifier of the more generic data value specification
     * @param childDataValueSpecificationGUID     unique identifier of the more specialized data value specification
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSpecializedDataValueSpecification(String                       userId,
                                                      String                       parentDataValueSpecificationGUID,
                                                      String                       childDataValueSpecificationGUID,
                                                      MakeAnchorOptions            makeAnchorOptions,
                                                      DataValueHierarchyProperties relationshipProperties) throws InvalidParameterException,
                                                                                                                  PropertyServerException,
                                                                                                                  UserNotAuthorizedException
    {
        final String methodName = "linkSpecializedDataValueSpecification";
        final String end1GUIDParameterName = "parentDataValueSpecificationGUID";
        final String end2GUIDParameterName = "childDataValueSpecificationGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(parentDataValueSpecificationGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(childDataValueSpecificationGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.DATA_VALUE_HIERARCHY_RELATIONSHIP.typeName,
                                                        parentDataValueSpecificationGUID,
                                                        childDataValueSpecificationGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach two data value specifications from one another.
     *
     * @param userId                 userId of the user making the request.
     * @param parentDataValueSpecificationGUID    unique identifier of the more generic data value specification
     * @param childDataValueSpecificationGUID     unique identifier of the more specialized
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSpecializedDataValueSpecification(String        userId,
                                                        String        parentDataValueSpecificationGUID,
                                                        String        childDataValueSpecificationGUID,
                                                        DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        final String methodName = "detachSpecializedDataValueSpecification";

        final String end1GUIDParameterName = "parentDataValueSpecificationGUID";
        final String end2GUIDParameterName = "childDataValueSpecificationGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(parentDataValueSpecificationGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(childDataValueSpecificationGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.DATA_VALUE_HIERARCHY_RELATIONSHIP.typeName,
                                                        parentDataValueSpecificationGUID,
                                                        childDataValueSpecificationGUID,
                                                        deleteOptions);
    }


    /**
     * Connect two data classes to show that one is used by the other when it is validating (typically a complex data item).
     *
     * @param userId                 userId of the user making the request
     * @param parentDataClassGUID    unique identifier of the parent data class
     * @param childDataClassGUID     unique identifier of the child data class
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * @param userId                 userId of the user making the request.
     * @param parentDataClassGUID    unique identifier of the  parent data class.
     * @param childDataClassGUID     unique identifier of the child data class.
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
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
     * Delete a data value specification.
     *
     * @param userId                 userId of the user making the request.
     * @param dataValueSpecificationGUID          unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteDataValueSpecification(String        userId,
                                             String        dataValueSpecificationGUID,
                                             DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        final String methodName = "deleteDataValueSpecification";
        final String guidParameterName = "dataValueSpecificationGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(dataValueSpecificationGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, dataValueSpecificationGUID, deleteOptions);
    }


    /**
     * Returns the list of data value specifications with a particular name.
     *
     * @param userId                 userId of the user making the request
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getDataValueSpecificationsByName(String       userId,
                                                                          String       name,
                                                                          QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                            PropertyServerException,
                                                                                                            UserNotAuthorizedException
    {
        final String methodName = "getDataValueSpecificationsByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.IDENTIFIER.name,
                                                   OpenMetadataProperty.NAMESPACE_PATH.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name);

        return super.getRootElementsByName(userId, name, propertyNames, queryOptions, methodName);
    }


    /**
     * Return the properties of a specific data value specification.
     *
     * @param userId                 userId of the user making the request
     * @param dataValueSpecificationGUID          unique identifier of the required element
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getDataValueSpecificationByGUID(String     userId,
                                                                   String     dataValueSpecificationGUID,
                                                                   GetOptions getOptions) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        final String methodName = "getDataValueSpecificationByGUID";

        return super.getRootElementByGUID(userId, dataValueSpecificationGUID, getOptions, methodName);
    }


    /**
     * Retrieve the list of data value specifications metadata elements that contain the search string.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findDataValueSpecifications(String        userId,
                                                                     String        searchString,
                                                                     SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                          UserNotAuthorizedException,
                                                                                                          PropertyServerException
    {
        final String methodName = "findDataValueSpecifications";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }


    /**
     * Attach a measurement specification to the data field that describes the basis for the measurement - for example, the denominator for a percentage.
     *
     * @param userId                 userId of the user making the request
     * @param measurementSpecificationGUID unique identifier of the measurement specification
     * @param dataFieldGUID unique identifier of the data field
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkBasisDataField(String            userId,
                        String            measurementSpecificationGUID,
                        String            dataFieldGUID,
                        MakeAnchorOptions makeAnchorOptions,
                        BasisDataFieldProperties relationshipProperties) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String methodName            = "linkBasisDataField";
        final String end1GUIDParameterName = "measurementSpecificationGUID";
        final String end2GUIDParameterName = "dataFieldGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(measurementSpecificationGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(dataFieldGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.BASIS_DATA_FIELD_RELATIONSHIP.typeName,
                                                        measurementSpecificationGUID,
                                                        dataFieldGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a measurement specification from the data field that describes the basis for the measurement.
     *
     * @param userId                 userId of the user making the request.
     * @param measurementSpecificationGUID unique identifier of the measurement specification
     * @param dataFieldGUID unique identifier of the data field
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachBasisDataField(String        userId,
                          String        measurementSpecificationGUID,
                          String        dataFieldGUID,
                          DeleteOptions deleteOptions) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        final String methodName            = "detachBasisDataField";
        final String end1GUIDParameterName = "measurementSpecificationGUID";
        final String end2GUIDParameterName = "dataFieldGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(measurementSpecificationGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(dataFieldGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.BASIS_DATA_FIELD_RELATIONSHIP.typeName,
                                                        measurementSpecificationGUID,
                                                        dataFieldGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a measurement specification to the data field that describes the time of the measurement.
     *
     * @param userId                 userId of the user making the request
     * @param measurementSpecificationGUID unique identifier of the measurement specification
     * @param dataFieldGUID unique identifier of the data field
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkAsOfDataField(String            userId,
                        String            measurementSpecificationGUID,
                        String            dataFieldGUID,
                        MakeAnchorOptions makeAnchorOptions,
                        AsOfDataFieldProperties relationshipProperties) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String methodName            = "linkAsOfDataField";
        final String end1GUIDParameterName = "measurementSpecificationGUID";
        final String end2GUIDParameterName = "dataFieldGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(measurementSpecificationGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(dataFieldGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.AS_OF_DATA_FIELD_RELATIONSHIP.typeName,
                                                        measurementSpecificationGUID,
                                                        dataFieldGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a measurement specification from the data field that describes the time of the measurement.
     *
     * @param userId                 userId of the user making the request.
     * @param measurementSpecificationGUID unique identifier of the measurement specification
     * @param dataFieldGUID unique identifier of the data field
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachAsOfDataField(String        userId,
                          String        measurementSpecificationGUID,
                          String        dataFieldGUID,
                          DeleteOptions deleteOptions) throws InvalidParameterException,
                                                              PropertyServerException,
                                                              UserNotAuthorizedException
    {
        final String methodName            = "detachAsOfDataField";
        final String end1GUIDParameterName = "measurementSpecificationGUID";
        final String end2GUIDParameterName = "dataFieldGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(measurementSpecificationGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(dataFieldGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.AS_OF_DATA_FIELD_RELATIONSHIP.typeName,
                                                        measurementSpecificationGUID,
                                                        dataFieldGUID,
                                                        deleteOptions);
    }
}
