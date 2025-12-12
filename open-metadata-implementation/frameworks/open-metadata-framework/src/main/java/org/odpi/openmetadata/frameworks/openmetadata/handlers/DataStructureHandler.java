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
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.*;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Data structure handler describes how to maintain and query data structures.
 * They are organized into specialized collections called data specs (supported with the
 * collection manager).
 */
public class DataStructureHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public DataStructureHandler(String             localServerName,
                                AuditLog           auditLog,
                                String             serviceName,
                                OpenMetadataClient openMetadataClient)
    {
        super(localServerName, auditLog, serviceName,openMetadataClient, OpenMetadataType.DATA_STRUCTURE.typeName);
    }


    /**
     * Create a new data structure.
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
    public String createDataStructure(String                                userId,
                                      NewElementOptions                     newElementOptions,
                                      Map<String, ClassificationProperties> initialClassifications,
                                      DataStructureProperties               properties,
                                      RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                 PropertyServerException,
                                                                                                                 UserNotAuthorizedException
    {
        final String methodName = "createDataStructure";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent a data structure using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new data structure.
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
    public String createDataStructureFromTemplate(String                 userId,
                                                  TemplateOptions        templateOptions,
                                                  String                 templateGUID,
                                                  ElementProperties      replacementProperties,
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
     * Update the properties of a data structure.
     *
     * @param userId                 userId of user making request.
     * @param actorProfileGUID       unique identifier of the actor profile (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateDataStructure(String                  userId,
                                       String                  actorProfileGUID,
                                       UpdateOptions           updateOptions,
                                       DataStructureProperties properties) throws InvalidParameterException,
                                                                                  PropertyServerException,
                                                                                  UserNotAuthorizedException
    {
        final String methodName = "updateDataStructure";
        final String guidParameterName = "dataStructureGUID";

        return super.updateElement(userId,
                                   actorProfileGUID,
                                   guidParameterName,
                                   updateOptions,
                                   properties,
                                   methodName);
    }


    /**
     * Attach a data field to a data structure.
     *
     * @param userId                  userId of user making request
     * @param dataStructureGUID unique identifier of the data structure
     * @param dataFieldGUID     unique identifier of the data field
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkMemberDataField(String                    userId,
                                    String                    dataStructureGUID,
                                    String                    dataFieldGUID,
                                    MakeAnchorOptions         makeAnchorOptions,
                                    MemberDataFieldProperties relationshipProperties) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        final String methodName = "linkMemberDataField";
        final String end1GUIDParameterName = "dataStructureGUID";
        final String end2GUIDParameterName = "dataFieldGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(dataStructureGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(dataFieldGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.MEMBER_DATA_FIELD_RELATIONSHIP.typeName,
                                                        dataStructureGUID,
                                                        dataFieldGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a data field from a data structure.
     *
     * @param userId                 userId of user making request.
     * @param dataStructureGUID    unique identifier of the data structure.
     * @param dataFieldGUID    unique identifier of the nested data field.
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachMemberDataField(String        userId,
                                      String        dataStructureGUID,
                                      String        dataFieldGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName = "detachMemberDataField";

        final String end1GUIDParameterName = "dataStructureGUID";
        final String end2GUIDParameterName = "dataFieldGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(dataStructureGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(dataFieldGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.MEMBER_DATA_FIELD_RELATIONSHIP.typeName,
                                                        dataStructureGUID,
                                                        dataFieldGUID,
                                                        deleteOptions);
    }


    /**
     * Delete a data structure.
     *
     * @param userId                 userId of user making request.
     * @param dataStructureGUID      unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteDataStructure(String        userId,
                                    String        dataStructureGUID,
                                    DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String methodName = "deleteDataStructure";
        final String guidParameterName = "dataStructureGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(dataStructureGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, dataStructureGUID, deleteOptions);
    }


    /**
     * Returns the list of data structures with a particular name.
     *
     * @param userId                 userId of user making request
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getDataStructuresByName(String       userId,
                                                                 String       name,
                                                                 QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        final String methodName = "getDataStructuresByName";
        final String nameParameterName = "name";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateMandatoryName(name, nameParameterName, methodName);

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.IDENTIFIER.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name,
                                                   OpenMetadataProperty.NAMESPACE.name);

        return super.getRootElementsByName(userId,
                                           name,
                                           propertyNames,
                                           queryOptions,
                                           methodName);
    }


    /**
     * Return the properties of a specific data structure.
     *
     * @param userId                 userId of user making request
     * @param dataStructureGUID      unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getDataStructureByGUID(String  userId,
                                                          String  dataStructureGUID,
                                                          GetOptions getOptions) throws InvalidParameterException,
                                                                                        PropertyServerException,
                                                                                        UserNotAuthorizedException
    {
        final String methodName = "getDataStructureByGUID";
        final String guidParameterName = "dataStructureGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(dataStructureGUID, guidParameterName, methodName);

        return super.getRootElementByGUID(userId, dataStructureGUID, getOptions, methodName);
    }


    /**
     * Retrieve the list of data structures metadata elements that contain the search string.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findDataStructures(String        userId,
                                                            String        searchString,
                                                            SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException
    {
        final String methodName = "findDataStructures";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }


    /**
     * Connect an element that is part of a data design to a data class to show that the data class should be used
     * as the specification for the data values when interpreting the data definition.
     *
     * @param userId                 userId of user making request
     * @param dataDefinitionGUID     unique identifier of the data design element (eg data field) that uses the data class
     * @param dataClassGUID          unique identifier of the data class
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkDataClassDefinition(String                        userId,
                                        String                        dataDefinitionGUID,
                                        String                        dataClassGUID,
                                        MakeAnchorOptions             makeAnchorOptions,
                                        DataClassDefinitionProperties relationshipProperties) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        final String methodName = "linkDataClassDefinition";
        final String end1GUIDParameterName = "dataDefinitionGUID";
        final String end2GUIDParameterName = "dataClassGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(dataDefinitionGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(dataClassGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.DATA_CLASS_DEFINITION_RELATIONSHIP.typeName,
                                                        dataDefinitionGUID,
                                                        dataClassGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a data definition from a data class.
     *
     * @param userId                 userId of user making request.
     * @param dataDefinitionGUID     unique identifier of the data design element (eg data field) that uses the data class
     * @param dataClassGUID          unique identifier of the data class
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachDataClassDefinition(String        userId,
                                          String        dataDefinitionGUID,
                                          String        dataClassGUID,
                                          DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                              PropertyServerException,
                                                                              UserNotAuthorizedException
    {
        final String methodName = "detachDataClassDefinition";

        final String end1GUIDParameterName = "dataDefinitionGUID";
        final String end2GUIDParameterName = "dataClassGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(dataDefinitionGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(dataClassGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.DATA_CLASS_DEFINITION_RELATIONSHIP.typeName,
                                                        dataDefinitionGUID,
                                                        dataClassGUID,
                                                        deleteOptions);
    }


    /**
     * Connect an element that is part of a data design to a glossary term to show that the term should be used
     * as the semantic definition for the data values when interpreting the data definition.
     *
     * @param userId                 userId of user making request
     * @param dataDefinitionGUID     unique identifier of the data design element (eg data field) that uses the data class
     * @param glossaryTermGUID       unique identifier of the glossary term
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSemanticDefinition(String                       userId,
                                       String                       dataDefinitionGUID,
                                       String                       glossaryTermGUID,
                                       MakeAnchorOptions            makeAnchorOptions,
                                       SemanticDefinitionProperties relationshipProperties) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        final String methodName = "linkSemanticDefinition";
        final String end1GUIDParameterName = "parentDataClassGUID";
        final String end2GUIDParameterName = "glossaryTermGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(dataDefinitionGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(glossaryTermGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.SEMANTIC_DEFINITION_RELATIONSHIP.typeName,
                                                        dataDefinitionGUID,
                                                        glossaryTermGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a data definition from a glossary term.
     *
     * @param userId                 userId of user making request.
     * @param dataDefinitionGUID     unique identifier of the data design element (eg data field) that uses the data class
     * @param glossaryTermGUID       unique identifier of the glossary term
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSemanticDefinition(String        userId,
                                         String        dataDefinitionGUID,
                                         String        glossaryTermGUID,
                                         DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                             PropertyServerException,
                                                                             UserNotAuthorizedException
    {
        final String methodName = "detachSemanticDefinition";

        final String end1GUIDParameterName = "dataDefinitionGUID";
        final String end2GUIDParameterName = "glossaryTermGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(dataDefinitionGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(glossaryTermGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.SEMANTIC_DEFINITION_RELATIONSHIP.typeName,
                                                        dataDefinitionGUID,
                                                        glossaryTermGUID,
                                                        deleteOptions);
    }


    /**
     * Connect a certification type to a data structure to guide the survey action service (that checks the data
     * quality of a data resource as part of certifying it with the supplied certification type) to the definition
     * of the data structure to use as a specification of how the data should be both structured and (if
     * data classes are attached to the associated data fields using the DataClassDefinition relationship)
     * contain the valid values.
     *
     * @param userId                 userId of user making request
     * @param certificationTypeGUID  unique identifier of the certification type
     * @param dataStructureGUID      unique identifier of the data structure
     * @param makeAnchorOptions  options to control access to open metadata
     * @param relationshipProperties the properties of the relationship
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkCertificationTypeToDataStructure(String                            userId,
                                                     String                            certificationTypeGUID,
                                                     String                            dataStructureGUID,
                                                     MakeAnchorOptions                 makeAnchorOptions,
                                                     DataStructureDefinitionProperties relationshipProperties) throws InvalidParameterException,
                                                                                                                      PropertyServerException,
                                                                                                                      UserNotAuthorizedException
    {
        final String methodName = "linkCertificationTypeToDataStructure";
        final String end1GUIDParameterName = "certificationTypeGUID";
        final String end2GUIDParameterName = "dataStructureGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(certificationTypeGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(dataStructureGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.DATA_STRUCTURE_DEFINITION_RELATIONSHIP.typeName,
                                                        certificationTypeGUID,
                                                        dataStructureGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a data structure from a certification type.
     *
     * @param userId                 userId of user making request.
     * @param certificationTypeGUID  unique identifier of the certification type
     * @param dataStructureGUID      unique identifier of the data structure
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachCertificationTypeToDataStructure(String        userId,
                                                       String        certificationTypeGUID,
                                                       String        dataStructureGUID,
                                                       DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName = "detachCertificationTypeToDataStructure";

        final String end1GUIDParameterName = "certificationTypeGUID";
        final String end2GUIDParameterName = "dataStructureGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(certificationTypeGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(dataStructureGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.DATA_STRUCTURE_DEFINITION_RELATIONSHIP.typeName,
                                                        certificationTypeGUID,
                                                        dataStructureGUID,
                                                        deleteOptions);
    }
}
