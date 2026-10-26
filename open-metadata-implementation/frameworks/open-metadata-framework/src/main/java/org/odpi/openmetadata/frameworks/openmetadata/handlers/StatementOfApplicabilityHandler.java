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
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.ControlApplicabilityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.StatementOfApplicabilityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * StatementOfApplicabilityHandler provides methods to maintain statements of applicability and the
 * ControlApplicability relationships that record which controls from a standard apply, why, and how far
 * each one has been implemented.
 */
public class StatementOfApplicabilityHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param localServiceName       local service name
     * @param openMetadataClient     access to open metadata
     */
    public StatementOfApplicabilityHandler(String             localServerName,
                                           AuditLog           auditLog,
                                           String             localServiceName,
                                           OpenMetadataClient openMetadataClient)
    {
        super(localServerName,
              auditLog,
              localServiceName,
              openMetadataClient,
              OpenMetadataType.STATEMENT_OF_APPLICABILITY.typeName);
    }


    /**
     * Create a new statement of applicability.
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
    public String createStatementOfApplicability(String                                userId,
                                                 NewElementOptions                     newElementOptions,
                                                 Map<String, ClassificationProperties> initialClassifications,
                                                 StatementOfApplicabilityProperties    properties,
                                                 RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                            PropertyServerException,
                                                                                                                            UserNotAuthorizedException
    {
        final String methodName = "createStatementOfApplicability";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent a statement of applicability using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new statement of applicability.
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
    public String createStatementOfApplicabilityFromTemplate(String                                userId,
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
     * Update the properties of a statement of applicability.
     *
     * @param userId                 userId of the user making the request.
     * @param statementOfApplicabilityGUID unique identifier of the statement of applicability (returned from create)
     * @param updateOptions provides a structure for the additional options when updating an element.
     * @param properties             properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateStatementOfApplicability(String                             userId,
                                                  String                             statementOfApplicabilityGUID,
                                                  UpdateOptions                      updateOptions,
                                                  StatementOfApplicabilityProperties properties) throws InvalidParameterException,
                                                                                                        PropertyServerException,
                                                                                                        UserNotAuthorizedException
    {
        final String methodName        = "updateStatementOfApplicability";
        final String guidParameterName = "statementOfApplicabilityGUID";

        return super.updateElement(userId,
                                   statementOfApplicabilityGUID,
                                   guidParameterName,
                                   updateOptions,
                                   properties,
                                   methodName);
    }


    /**
     * Link a control from a standard to a statement of applicability, recording whether the control applies, why,
     * and how far it has been implemented.  ControlApplicability is not a multi-link relationship: there is one
     * relationship between a statement and each control it assesses.  Linking a control that is already linked
     * updates the properties of the existing relationship, so this is also how the applicability of a control is
     * changed - for example, as its implementation progresses or after a review.
     *
     * @param userId                       userId of the user making the request
     * @param statementOfApplicabilityGUID unique identifier of the statement of applicability
     * @param controlGUID                  unique identifier of the governance definition describing the control
     * @param makeAnchorOptions            options to control access to open metadata
     * @param relationshipProperties       applicability of the control
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkControlApplicability(String                         userId,
                                         String                         statementOfApplicabilityGUID,
                                         String                         controlGUID,
                                         MakeAnchorOptions              makeAnchorOptions,
                                         ControlApplicabilityProperties relationshipProperties) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        final String methodName            = "linkControlApplicability";
        final String end1GUIDParameterName = "statementOfApplicabilityGUID";
        final String end2GUIDParameterName = "controlGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(statementOfApplicabilityGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(controlGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.CONTROL_APPLICABILITY_RELATIONSHIP.typeName,
                                                        statementOfApplicabilityGUID,
                                                        controlGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Remove a control from a statement of applicability.
     *
     * @param userId                       userId of the user making the request.
     * @param statementOfApplicabilityGUID unique identifier of the statement of applicability
     * @param controlGUID                  unique identifier of the governance definition describing the control
     * @param deleteOptions                options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachControlApplicability(String        userId,
                                           String        statementOfApplicabilityGUID,
                                           String        controlGUID,
                                           DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                               PropertyServerException,
                                                                               UserNotAuthorizedException
    {
        final String methodName            = "detachControlApplicability";
        final String end1GUIDParameterName = "statementOfApplicabilityGUID";
        final String end2GUIDParameterName = "controlGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(statementOfApplicabilityGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(controlGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.CONTROL_APPLICABILITY_RELATIONSHIP.typeName,
                                                        statementOfApplicabilityGUID,
                                                        controlGUID,
                                                        deleteOptions);
    }


    /**
     * Delete a statement of applicability.
     *
     * @param userId                 userId of the user making the request.
     * @param statementOfApplicabilityGUID unique identifier of the element
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteStatementOfApplicability(String        userId,
                                               String        statementOfApplicabilityGUID,
                                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName        = "deleteStatementOfApplicability";
        final String guidParameterName = "statementOfApplicabilityGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(statementOfApplicabilityGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, statementOfApplicabilityGUID, deleteOptions);
    }


    /**
     * Returns the list of statements of applicability with a particular name.
     *
     * @param userId                 userId of the user making the request
     * @param name                   name of the element to return - match is full text match in qualifiedName, identifier or displayName
     * @param queryOptions           multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getStatementsOfApplicabilityByName(String       userId,
                                                                            String       name,
                                                                            QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException
    {
        final String methodName = "getStatementsOfApplicabilityByName";

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
     * Return the properties of a specific statement of applicability.
     *
     * @param userId                 userId of the user making the request
     * @param statementOfApplicabilityGUID unique identifier of the required element
     * @param getOptions multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getStatementOfApplicabilityByGUID(String     userId,
                                                                     String     statementOfApplicabilityGUID,
                                                                     GetOptions getOptions) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        final String methodName = "getStatementOfApplicabilityByGUID";

        return super.getRootElementByGUID(userId,
                                          statementOfApplicabilityGUID,
                                          getOptions,
                                          methodName);
    }


    /**
     * Retrieve the list of statement of applicability metadata elements that contain the search string.
     *
     * @param userId                 calling user
     * @param searchString           string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findStatementsOfApplicability(String        userId,
                                                                       String        searchString,
                                                                       SearchOptions searchOptions) throws InvalidParameterException,
                                                                                                           UserNotAuthorizedException,
                                                                                                           PropertyServerException
    {
        final String methodName  = "findStatementsOfApplicability";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }
}
