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
import org.odpi.openmetadata.frameworks.openmetadata.properties.decisions.AssessmentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.decisions.DecisionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.decisions.OutcomeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * DecisionHandler describes how to maintain and query decisions, their outcomes and the assessments that contributed to them.
 */
public class DecisionHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public DecisionHandler(String             localServerName,
                           AuditLog           auditLog,
                           String             serviceName,
                           OpenMetadataClient openMetadataClient)
    {
        super(localServerName, auditLog, serviceName, openMetadataClient, OpenMetadataType.DECISION.typeName);
    }


    /**
     * Create a decision.
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
    public String createDecision(String                                userId,
                                 NewElementOptions                     newElementOptions,
                                 Map<String, ClassificationProperties> initialClassifications,
                                 DecisionProperties                    properties,
                                 RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                            PropertyServerException,
                                                                                                            UserNotAuthorizedException
    {
        final String methodName = "createDecision";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent a decision using an existing metadata element as a template.  The
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
    public String createDecisionFromTemplate(String                                userId,
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
     * Update the properties of a decision.
     *
     * @param userId        calling user
     * @param decisionGUID  unique identifier of the decision
     * @param updateOptions provides a structure for the additional options when updating an element
     * @param properties    properties for the element
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public boolean updateDecision(String             userId,
                                  String             decisionGUID,
                                  UpdateOptions      updateOptions,
                                  DecisionProperties properties) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        final String methodName                = "updateDecision";
        final String decisionGUIDParameterName = "decisionGUID";

        return super.updateElement(userId,
                                   decisionGUID,
                                   decisionGUIDParameterName,
                                   updateOptions,
                                   properties,
                                   methodName);
    }


    /**
     * Delete a decision.
     *
     * @param userId        calling user
     * @param decisionGUID  unique identifier of the decision
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public void deleteDecision(String        userId,
                               String        decisionGUID,
                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        final String methodName                = "deleteDecision";
        final String decisionGUIDParameterName = "decisionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(decisionGUID, decisionGUIDParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, decisionGUID, deleteOptions);
    }


    /**
     * Returns the list of decisions with a particular name.
     *
     * @param userId       calling user
     * @param name         name of the element to return - match is full text match in qualifiedName, identifier or displayName
     * @param queryOptions multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public List<OpenMetadataRootElement> getDecisionsByName(String       userId,
                                                            String       name,
                                                            QueryOptions queryOptions) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        final String methodName = "getDecisionsByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.IDENTIFIER.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name);

        return super.getRootElementsByName(userId, name, propertyNames, queryOptions, methodName);
    }


    /**
     * Return the properties of a specific decision.
     *
     * @param userId       calling user
     * @param decisionGUID unique identifier of the decision
     * @param getOptions   multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public OpenMetadataRootElement getDecisionByGUID(String     userId,
                                                     String     decisionGUID,
                                                     GetOptions getOptions) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        final String methodName                = "getDecisionByGUID";
        final String decisionGUIDParameterName = "decisionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(decisionGUID, decisionGUIDParameterName, methodName);

        return super.getRootElementByGUID(userId, decisionGUID, getOptions, methodName);
    }


    /**
     * Retrieve the list of decision metadata elements that contain the search string.
     *
     * @param userId        calling user
     * @param searchString  string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public List<OpenMetadataRootElement> findDecisions(String        userId,
                                                       String        searchString,
                                                       SearchOptions searchOptions) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        final String methodName = "findDecisions";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }


    /**
     * Attach a decision to an element that represents an outcome of the decision.
     *
     * @param userId                 calling user
     * @param decisionGUID           unique identifier of the decision
     * @param outcomeGUID            unique identifier of the element that is an outcome of the decision
     * @param makeAnchorOptions      options to control access to open metadata
     * @param relationshipProperties description of the relationship
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public void linkDecisionOutcome(String            userId,
                                    String            decisionGUID,
                                    String            outcomeGUID,
                                    MakeAnchorOptions makeAnchorOptions,
                                    OutcomeProperties relationshipProperties) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        final String methodName                = "linkDecisionOutcome";
        final String decisionGUIDParameterName = "decisionGUID";
        final String outcomeGUIDParameterName  = "outcomeGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(decisionGUID, decisionGUIDParameterName, methodName);
        propertyHelper.validateGUID(outcomeGUID, outcomeGUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.OUTCOME_RELATIONSHIP.typeName,
                                                        decisionGUID,
                                                        outcomeGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a decision from an element that represents an outcome of the decision.
     *
     * @param userId        calling user
     * @param decisionGUID  unique identifier of the decision
     * @param outcomeGUID   unique identifier of the element that is an outcome of the decision
     * @param deleteOptions options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public void detachDecisionOutcome(String        userId,
                                      String        decisionGUID,
                                      String        outcomeGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        final String methodName                = "detachDecisionOutcome";
        final String decisionGUIDParameterName = "decisionGUID";
        final String outcomeGUIDParameterName  = "outcomeGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(decisionGUID, decisionGUIDParameterName, methodName);
        propertyHelper.validateGUID(outcomeGUID, outcomeGUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.OUTCOME_RELATIONSHIP.typeName,
                                                        decisionGUID,
                                                        outcomeGUID,
                                                        deleteOptions);
    }


    /**
     * Attach an element that has been assessed to the decision that the assessment contributes to.  This is a
     * multi-link relationship so a new relationship is always created and its unique identifier is returned.
     *
     * @param userId                 calling user
     * @param elementGUID            unique identifier of the element that was assessed
     * @param decisionGUID           unique identifier of the decision
     * @param makeAnchorOptions      options to control access to open metadata
     * @param relationshipProperties description of the relationship
     * @return unique identifier of the new relationship
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public String linkAssessment(String               userId,
                                 String               elementGUID,
                                 String               decisionGUID,
                                 MakeAnchorOptions    makeAnchorOptions,
                                 AssessmentProperties relationshipProperties) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        final String methodName                = "linkAssessment";
        final String elementGUIDParameterName  = "elementGUID";
        final String decisionGUIDParameterName = "decisionGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(elementGUID, elementGUIDParameterName, methodName);
        propertyHelper.validateGUID(decisionGUID, decisionGUIDParameterName, methodName);

        return openMetadataClient.createRelatedElementsInStore(userId,
                                                               OpenMetadataType.ASSESSMENT_RELATIONSHIP.typeName,
                                                               elementGUID,
                                                               decisionGUID,
                                                               makeAnchorOptions,
                                                               relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Update the properties of an assessment relationship.
     *
     * @param userId                 calling user
     * @param assessmentGUID         unique identifier of the assessment relationship
     * @param updateOptions          provides a structure for the additional options when updating a relationship
     * @param relationshipProperties properties of the relationship
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public void updateAssessment(String               userId,
                                 String               assessmentGUID,
                                 UpdateOptions        updateOptions,
                                 AssessmentProperties relationshipProperties) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        final String methodName                  = "updateAssessment";
        final String assessmentGUIDParameterName = "assessmentGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(assessmentGUID, assessmentGUIDParameterName, methodName);

        openMetadataClient.updateRelationshipInStore(userId,
                                                     assessmentGUID,
                                                     updateOptions,
                                                     relationshipBuilder.getElementProperties(relationshipProperties));
    }


    /**
     * Remove an assessment relationship between an element and a decision.
     *
     * @param userId         calling user
     * @param assessmentGUID unique identifier of the assessment relationship
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public void detachAssessment(String        userId,
                                 String        assessmentGUID,
                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        final String methodName                  = "detachAssessment";
        final String assessmentGUIDParameterName = "assessmentGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(assessmentGUID, assessmentGUIDParameterName, methodName);

        openMetadataClient.deleteRelationshipInStore(userId, assessmentGUID, deleteOptions);
    }
}
