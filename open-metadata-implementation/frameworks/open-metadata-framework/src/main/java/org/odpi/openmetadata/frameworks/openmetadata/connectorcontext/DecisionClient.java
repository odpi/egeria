/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.connectorcontext;

import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.client.OpenMetadataClient;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.handlers.DecisionHandler;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.OpenMetadataRootElement;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.EntityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.decisions.AssessmentProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.decisions.DecisionProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.decisions.OutcomeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.*;

import java.util.List;
import java.util.Map;

/**
 * Provides services for connectors to work with decision elements.
 */
public class DecisionClient extends ConnectorContextClientBase
{
    private final DecisionHandler decisionHandler;


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
    public DecisionClient(ConnectorContextBase parentContext,
                          String               localServerName,
                          String               localServiceName,
                          String               connectorUserId,
                          String               connectorGUID,
                          String               externalSourceGUID,
                          String               externalSourceName,
                          OpenMetadataClient   openMetadataClient,
                          AuditLog             auditLog,
                          int                  maxPageSize)
    {
        super(parentContext, localServerName, localServiceName, connectorUserId, connectorGUID, externalSourceGUID, externalSourceName, auditLog, maxPageSize);

        this.decisionHandler = new DecisionHandler(localServerName, auditLog, localServiceName, openMetadataClient);
    }


    /**
     * Create a decision.
     *
     * @param newElementOptions            details of the element to create
     * @param initialClassifications       map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public String createDecision(NewElementOptions                     newElementOptions,
                                 Map<String, ClassificationProperties> initialClassifications,
                                 DecisionProperties                    properties,
                                 RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                            PropertyServerException,
                                                                                                            UserNotAuthorizedException
    {
        return decisionHandler.createDecision(connectorUserId, newElementOptions, initialClassifications, properties, parentRelationshipProperties);
    }


    /**
     * Create a new metadata element to represent a decision using an existing metadata element as a template.  The
     * template defines additional classifications and relationships that should be added to the new element.
     *
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
    public String createDecisionFromTemplate(TemplateOptions                       templateOptions,
                                             String                                templateGUID,
                                             EntityProperties                      replacementProperties,
                                             Map<String, ClassificationProperties> replacementClassifications,
                                             Map<String, String>                   placeholderProperties,
                                             RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                        PropertyServerException,
                                                                                                                        UserNotAuthorizedException
    {
        return decisionHandler.createDecisionFromTemplate(connectorUserId, templateOptions, templateGUID, replacementProperties, replacementClassifications, placeholderProperties, parentRelationshipProperties);
    }


    /**
     * Update the properties of a decision.
     *
     * @param decisionGUID  unique identifier of the decision
     * @param updateOptions provides a structure for the additional options when updating an element
     * @param properties    properties for the element
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public boolean updateDecision(String             decisionGUID,
                                  UpdateOptions      updateOptions,
                                  DecisionProperties properties) throws InvalidParameterException,
                                                                        PropertyServerException,
                                                                        UserNotAuthorizedException
    {
        return decisionHandler.updateDecision(connectorUserId, decisionGUID, updateOptions, properties);
    }


    /**
     * Delete a decision.
     *
     * @param decisionGUID  unique identifier of the decision
     * @param deleteOptions options for a delete request
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public void deleteDecision(String        decisionGUID,
                               DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                   PropertyServerException,
                                                                   UserNotAuthorizedException
    {
        decisionHandler.deleteDecision(connectorUserId, decisionGUID, deleteOptions);
    }


    /**
     * Returns the list of decisions with a particular name.
     *
     * @param name         name of the element to return - match is full text match in qualifiedName, identifier or displayName
     * @param queryOptions multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public List<OpenMetadataRootElement> getDecisionsByName(String       name,
                                                            QueryOptions queryOptions) throws InvalidParameterException,
                                                                                              PropertyServerException,
                                                                                              UserNotAuthorizedException
    {
        return decisionHandler.getDecisionsByName(connectorUserId, name, queryOptions);
    }


    /**
     * Return the properties of a specific decision.
     *
     * @param decisionGUID unique identifier of the decision
     * @param getOptions   multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public OpenMetadataRootElement getDecisionByGUID(String     decisionGUID,
                                                     GetOptions getOptions) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        return decisionHandler.getDecisionByGUID(connectorUserId, decisionGUID, getOptions);
    }


    /**
     * Retrieve the list of decision metadata elements that contain the search string.
     *
     * @param searchString  string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public List<OpenMetadataRootElement> findDecisions(String        searchString,
                                                       SearchOptions searchOptions) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        return decisionHandler.findDecisions(connectorUserId, searchString, searchOptions);
    }


    /**
     * Attach a decision to an element that represents an outcome of the decision.
     *
     * @param decisionGUID           unique identifier of the decision
     * @param outcomeGUID            unique identifier of the element that is an outcome of the decision
     * @param makeAnchorOptions      options to control access to open metadata
     * @param relationshipProperties description of the relationship
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public void linkDecisionOutcome(String            decisionGUID,
                                    String            outcomeGUID,
                                    MakeAnchorOptions makeAnchorOptions,
                                    OutcomeProperties relationshipProperties) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        decisionHandler.linkDecisionOutcome(connectorUserId, decisionGUID, outcomeGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Detach a decision from an element that represents an outcome of the decision.
     *
     * @param decisionGUID  unique identifier of the decision
     * @param outcomeGUID   unique identifier of the element that is an outcome of the decision
     * @param deleteOptions options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public void detachDecisionOutcome(String        decisionGUID,
                                      String        outcomeGUID,
                                      DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                          PropertyServerException,
                                                                          UserNotAuthorizedException
    {
        decisionHandler.detachDecisionOutcome(connectorUserId, decisionGUID, outcomeGUID, deleteOptions);
    }


    /**
     * Attach an element that has been assessed to the decision that the assessment contributes to.  This is a
     * multi-link relationship so a new relationship is always created and its unique identifier is returned.
     *
     * @param elementGUID            unique identifier of the element that was assessed
     * @param decisionGUID           unique identifier of the decision
     * @param makeAnchorOptions      options to control access to open metadata
     * @param relationshipProperties description of the relationship
     * @return unique identifier of the new relationship
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public String linkAssessment(String               elementGUID,
                                 String               decisionGUID,
                                 MakeAnchorOptions    makeAnchorOptions,
                                 AssessmentProperties relationshipProperties) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        return decisionHandler.linkAssessment(connectorUserId, elementGUID, decisionGUID, makeAnchorOptions, relationshipProperties);
    }


    /**
     * Update the properties of an assessment relationship.
     *
     * @param assessmentGUID         unique identifier of the assessment relationship
     * @param updateOptions          provides a structure for the additional options when updating a relationship
     * @param relationshipProperties properties of the relationship
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public void updateAssessment(String               assessmentGUID,
                                 UpdateOptions        updateOptions,
                                 AssessmentProperties relationshipProperties) throws InvalidParameterException,
                                                                                     PropertyServerException,
                                                                                     UserNotAuthorizedException
    {
        decisionHandler.updateAssessment(connectorUserId, assessmentGUID, updateOptions, relationshipProperties);
    }


    /**
     * Remove an assessment relationship between an element and a decision.
     *
     * @param assessmentGUID unique identifier of the assessment relationship
     * @param deleteOptions  options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws PropertyServerException    a problem reported in the open metadata server(s)
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     */
    public void detachAssessment(String        assessmentGUID,
                                 DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                     PropertyServerException,
                                                                     UserNotAuthorizedException
    {
        decisionHandler.detachAssessment(connectorUserId, assessmentGUID, deleteOptions);
    }
}
