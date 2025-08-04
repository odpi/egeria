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
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.SubjectAreaDefinitionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Maintain and query subject area definitions.
 */
public class SubjectAreaHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public SubjectAreaHandler(String             localServerName,
                              AuditLog           auditLog,
                              String             serviceName,
                              OpenMetadataClient openMetadataClient)
    {
        super(localServerName, auditLog, serviceName, openMetadataClient, OpenMetadataType.SUBJECT_AREA_DEFINITION.typeName);
    }


    /**
     * Create a new subject area.
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
    public String createSubjectArea(String                                userId,
                                    NewElementOptions                     newElementOptions,
                                    Map<String, ClassificationProperties> initialClassifications,
                                    SubjectAreaDefinitionProperties       properties,
                                    RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                               PropertyServerException,
                                                                                                               UserNotAuthorizedException
    {
        final String methodName = "createSubjectArea";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent a subject area using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new subject area.
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
    public String createSubjectAreaFromTemplate(String                 userId,
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
     * Update the properties of a subject area.
     *
     * @param userId                 userId of user making request.
     * @param subjectAreaGUID      unique identifier of the subject area (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void updateSubjectArea(String                          userId,
                                  String                          subjectAreaGUID,
                                  UpdateOptions                   updateOptions,
                                  SubjectAreaDefinitionProperties properties) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        final String methodName = "updateSubjectArea";
        final String guidParameterName = "subjectAreaGUID";

        super.updateElement(userId,
                            subjectAreaGUID,
                            guidParameterName,
                            updateOptions,
                            properties,
                            methodName);
    }


    /**
     * Attach a profile to a subject area.
     *
     * @param userId                  userId of user making request
     * @param subjectAreaGUID        unique identifier of the parent
     * @param nestedSubjectAreaGUID             unique identifier of the actor profile
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSubjectAreas(String                 userId,
                                 String                 subjectAreaGUID,
                                 String                 nestedSubjectAreaGUID,
                                 MetadataSourceOptions  metadataSourceOptions,
                                 RelationshipProperties relationshipProperties) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        final String methodName = "linkSubjectAreas";
        final String end1GUIDParameterName = "subjectAreaGUID";
        final String end2GUIDParameterName = "nestedSubjectAreaGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(subjectAreaGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(nestedSubjectAreaGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.SUBJECT_AREA_HIERARCHY_RELATIONSHIP.typeName,
                                                        subjectAreaGUID,
                                                        nestedSubjectAreaGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach an actor profile from a subject area.
     *
     * @param userId                 userId of user making request.
     * @param subjectAreaGUID       unique identifier of the parent actor profile
     * @param nestedSubjectAreaGUID            unique identifier of the nested actor profile
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSubjectAreas(String        userId,
                                   String        subjectAreaGUID,
                                   String        nestedSubjectAreaGUID,
                                   DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        final String methodName = "detachSubjectAreas";

        final String end1GUIDParameterName = "subjectAreaGUID";
        final String end2GUIDParameterName = "nestedSubjectAreaGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(subjectAreaGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(nestedSubjectAreaGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.SUBJECT_AREA_HIERARCHY_RELATIONSHIP.typeName,
                                                        subjectAreaGUID,
                                                        nestedSubjectAreaGUID,
                                                        deleteOptions);
    }


    /**
     * Delete a subject area.
     *
     * @param userId                 userId of user making request.
     * @param subjectAreaGUID       unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteSubjectArea(String        userId,
                                  String        subjectAreaGUID,
                                  DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                      PropertyServerException,
                                                                      UserNotAuthorizedException
    {
        final String methodName = "deleteSubjectArea";
        final String guidParameterName = "subjectAreaGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(subjectAreaGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, subjectAreaGUID, deleteOptions);
    }


    /**
     * Returns the list of subject areas with a particular name.
     *
     * @param userId                 userId of user making request
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getSubjectAreasByName(String       userId,
                                                               String       name,
                                                               QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        final String methodName = "getSubjectAreasByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name,
                                                   OpenMetadataProperty.SUBJECT_AREA_NAME.name);

        return super.getRootElementsByName(userId, name, propertyNames, queryOptions, methodName);
    }


    /**
     * Return the properties of a specific subject area.
     *
     * @param userId                 userId of user making request
     * @param subjectAreaGUID      unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getSubjectAreaByGUID(String     userId,
                                                        String     subjectAreaGUID,
                                                        GetOptions getOptions) throws InvalidParameterException,
                                                                                      PropertyServerException,
                                                                                      UserNotAuthorizedException
    {
        final String methodName = "getSubjectAreaByGUID";

        return super.getRootElementByGUID(userId, subjectAreaGUID, getOptions, methodName);
    }


    /**
     * Retrieve the list of subject areas metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findSubjectAreas(String        userId,
                                                          String        searchString,
                                                          SearchOptions searchOptions) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName = "findSubjectAreas";

        return super.findRootElements(userId,
                                      searchString,
                                      searchOptions,
                                      methodName);
    }
}
