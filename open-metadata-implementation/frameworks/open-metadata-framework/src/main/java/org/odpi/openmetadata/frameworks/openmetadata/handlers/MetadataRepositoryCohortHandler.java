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
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.MetadataCohortPeerProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.MetadataRepositoryCohortProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * MetadataRepositoryCohortHandler provides methods to define cohorts and their relationships to their members.
 */
public class MetadataRepositoryCohortHandler extends OpenMetadataHandlerBase
{

    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param localServiceName       local service name
     * @param openMetadataClient     access to open metadata
     */
    public MetadataRepositoryCohortHandler(String             localServerName,
                                           AuditLog           auditLog,
                                           String             localServiceName,
                                           OpenMetadataClient openMetadataClient)
    {
        super(localServerName,
              auditLog,
              localServiceName,
              openMetadataClient,
              OpenMetadataType.METADATA_REPOSITORY_COHORT.typeName);
    }


    /**
     * Create a new open metadata repository cohort.
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
    public String createMetadataRepositoryCohort(String                                userId,
                                                 NewElementOptions                     newElementOptions,
                                                 Map<String, ClassificationProperties> initialClassifications,
                                                 MetadataRepositoryCohortProperties    properties,
                                                 RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                            PropertyServerException,
                                                                                                                            UserNotAuthorizedException
    {
        final String methodName = "createMetadataRepositoryCohort";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent an open metadata repository cohort using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new open metadata repository cohort.
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
    public String createMetadataRepositoryCohortFromTemplate(String                 userId,
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
     * Update the properties of an open metadata repository cohort.
     *
     * @param userId                 userId of user making request.
     * @param metadataRepositoryCohortGUID       unique identifier of the open metadata repository cohort (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateMetadataRepositoryCohort(String                             userId,
                                                  String                             metadataRepositoryCohortGUID,
                                                  UpdateOptions                      updateOptions,
                                                  MetadataRepositoryCohortProperties properties) throws InvalidParameterException,
                                                                                                        PropertyServerException,
                                                                                                        UserNotAuthorizedException
    {
        final String methodName        = "updateMetadataRepositoryCohort";
        final String guidParameterName = "metadataRepositoryCohortGUID";

        return super.updateElement(userId,
                                   metadataRepositoryCohortGUID,
                                   guidParameterName,
                                   updateOptions,
                                   properties,
                                   methodName);
    }


    /**
     * Attach an open metadata cohort to a cohort member.
     *
     * @param userId                  userId of user making request
     * @param metadataRepositoryCohortGUID             unique identifier of the cohort
     * @param cohortMemberGUID            unique identifier of the member
     * @param metadataSourceOptions  options to control access to open metadata
     * @param relationshipProperties  description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkCohortToMember(String                       userId,
                                   String                       metadataRepositoryCohortGUID,
                                   String                       cohortMemberGUID,
                                   MetadataSourceOptions        metadataSourceOptions,
                                   MetadataCohortPeerProperties relationshipProperties) throws InvalidParameterException,
                                                                                                    PropertyServerException,
                                                                                                    UserNotAuthorizedException
    {
        final String methodName = "linkCohortToMember";
        final String end1GUIDParameterName = "metadataRepositoryCohortGUID";
        final String end2GUIDParameterName = "cohortMemberGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(metadataRepositoryCohortGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(cohortMemberGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.METADATA_COHORT_PEER_RELATIONSHIP.typeName,
                                                        cohortMemberGUID,
                                                        metadataRepositoryCohortGUID,
                                                        metadataSourceOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach an open metadata cohort from a cohort member.
     *
     * @param userId                 userId of user making request.
     * @param metadataRepositoryCohortGUID             unique identifier of the cohort
     * @param cohortMemberGUID            unique identifier of the member
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachCohortFromMember(String        userId,
                                       String        metadataRepositoryCohortGUID,
                                       String        cohortMemberGUID,
                                       DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                           PropertyServerException,
                                                                           UserNotAuthorizedException
    {
        final String methodName = "detachCohortFromMember";

        final String end1GUIDParameterName = "metadataRepositoryCohortGUID";
        final String end2GUIDParameterName = "cohortMemberGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(metadataRepositoryCohortGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(cohortMemberGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.METADATA_COHORT_PEER_RELATIONSHIP.typeName,
                                                        cohortMemberGUID,
                                                        metadataRepositoryCohortGUID,
                                                        deleteOptions);
    }


    /**
     * Delete an open metadata repository cohort.
     *
     * @param userId                 userId of user making request.
     * @param metadataRepositoryCohortGUID       unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteMetadataRepositoryCohort(String        userId,
                                               String        metadataRepositoryCohortGUID,
                                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName        = "deleteMetadataRepositoryCohort";
        final String guidParameterName = "metadataRepositoryCohortGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(metadataRepositoryCohortGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, metadataRepositoryCohortGUID, deleteOptions);
    }


    /**
     * Returns the list of open metadata repository cohorts with a particular name.
     *
     * @param userId                 userId of user making request
     * @param name                   name of the element to return - match is full text match in qualifiedName or name
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getMetadataRepositoryCohortsByName(String       userId,
                                                                            String       name,
                                                                            QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException
    {
        final String methodName = "getMetadataRepositoryCohortsByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name,
                                                   OpenMetadataProperty.IDENTIFIER.name);

        return super.getRootElementsByName(userId,
                                           name,
                                           propertyNames,
                                           queryOptions,
                                           methodName);
    }


    /**
     * Return the properties of a specific open metadata repository cohort.
     *
     * @param userId                 userId of user making request
     * @param metadataRepositoryCohortGUID       unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getMetadataRepositoryCohortByGUID(String     userId,
                                                                     String     metadataRepositoryCohortGUID,
                                                                     GetOptions getOptions) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        final String methodName = "getMetadataRepositoryCohortByGUID";

        return super.getRootElementByGUID(userId,
                                          metadataRepositoryCohortGUID,
                                          getOptions,
                                          methodName);
    }



    /**
     * Retrieve the list of open metadata repository cohorts metadata elements that contain the search string.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findMetadataRepositoryCohorts(String        userId,
                                                                       String        searchString,
                                                                       SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        final String methodName  = "findMetadataRepositoryCohorts";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }
}
