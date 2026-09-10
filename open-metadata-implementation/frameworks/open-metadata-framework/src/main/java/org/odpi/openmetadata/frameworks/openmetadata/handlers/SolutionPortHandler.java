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
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionComponentPortProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionPortDelegationProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.solutions.SolutionPortProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.GetOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.MakeAnchorOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.NewElementOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.QueryOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.SearchOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.TemplateOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.UpdateOptions;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * SolutionPortHandler provides methods to define solution ports and their relationships: the SolutionComponentPort
 * relationship that attaches a port to the solution component that exposes it, and the SolutionPortDelegation
 * relationship that aligns the port of a composite component with the port of one of its subcomponents.
 */
public class SolutionPortHandler extends OpenMetadataHandlerBase
{
    /**
     * Create a new handler.
     *
     * @param localServerName        name of this server (view server)
     * @param auditLog               logging destination
     * @param serviceName            local service name
     * @param openMetadataClient     access to open metadata
     */
    public SolutionPortHandler(String             localServerName,
                               AuditLog           auditLog,
                               String             serviceName,
                               OpenMetadataClient openMetadataClient)
    {
        super(localServerName, auditLog, serviceName, openMetadataClient, OpenMetadataType.SOLUTION_PORT.typeName);
    }


    /**
     * Create a new solution port.
     *
     * @param userId                       userId of user making request.
     * @param newElementOptions            details of the element to create
     * @param initialClassifications       map of classification names to classification properties to include in the entity creation request
     * @param properties                   properties for the new element.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the newly created element
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public String createSolutionPort(String                                userId,
                                     NewElementOptions                     newElementOptions,
                                     Map<String, ClassificationProperties> initialClassifications,
                                     SolutionPortProperties                properties,
                                     RelationshipProperties                parentRelationshipProperties) throws InvalidParameterException,
                                                                                                                PropertyServerException,
                                                                                                                UserNotAuthorizedException
    {
        final String methodName = "createSolutionPort";

        return super.createNewElement(userId,
                                      newElementOptions,
                                      initialClassifications,
                                      properties,
                                      parentRelationshipProperties,
                                      methodName);
    }


    /**
     * Create a new metadata element to represent a solution port using an existing element as a template.
     * The template defines additional classifications and relationships that should be added to the new solution port.
     *
     * @param userId                       calling user
     * @param templateOptions              details of the element to create
     * @param templateGUID                 the unique identifier of the existing element to copy
     * @param replacementProperties        properties that override the template
     * @param replacementClassifications   classifications that override the template
     * @param placeholderProperties        property name-to-property value map to replace any placeholder values in the
     *                                     template element - and their anchored elements, which are also copied as part of this operation.
     * @param parentRelationshipProperties properties to include in parent relationship
     * @return unique identifier of the new metadata element
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createSolutionPortFromTemplate(String                                userId,
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
     * Update the properties of a solution port.
     *
     * @param userId           userId of user making request.
     * @param solutionPortGUID unique identifier of the solution port (returned from create)
     * @param updateOptions    provides a structure for the additional options when updating an element.
     * @param properties       properties for the element.
     * @return boolean - true if an update occurred
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public boolean updateSolutionPort(String                 userId,
                                      String                 solutionPortGUID,
                                      UpdateOptions          updateOptions,
                                      SolutionPortProperties properties) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName        = "updateSolutionPort";
        final String guidParameterName = "solutionPortGUID";

        return super.updateElement(userId,
                                   solutionPortGUID,
                                   guidParameterName,
                                   updateOptions,
                                   properties,
                                   methodName);
    }


    /**
     * Delete a solution port.
     *
     * @param userId           userId of user making request.
     * @param solutionPortGUID unique identifier of the element
     * @param deleteOptions    options for a delete request
     * @throws InvalidParameterException  one of the parameters is invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void deleteSolutionPort(String        userId,
                                   String        solutionPortGUID,
                                   DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                       PropertyServerException,
                                                                       UserNotAuthorizedException
    {
        final String methodName        = "deleteSolutionPort";
        final String guidParameterName = "solutionPortGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(solutionPortGUID, guidParameterName, methodName);

        openMetadataClient.deleteMetadataElementInStore(userId, solutionPortGUID, deleteOptions);
    }


    /**
     * Returns the list of solution ports with a particular name.
     *
     * @param userId       userId of user making request
     * @param name         name of the element to return - match is full text match in qualifiedName, identifier or displayName
     * @param queryOptions multiple options to control the query
     * @return a list of elements
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public List<OpenMetadataRootElement> getSolutionPortsByName(String       userId,
                                                                String       name,
                                                                QueryOptions queryOptions) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        final String methodName = "getSolutionPortsByName";

        List<String> propertyNames = Arrays.asList(OpenMetadataProperty.QUALIFIED_NAME.name,
                                                   OpenMetadataProperty.IDENTIFIER.name,
                                                   OpenMetadataProperty.DISPLAY_NAME.name);

        return super.getRootElementsByName(userId, name, propertyNames, queryOptions, methodName);
    }


    /**
     * Return the properties of a specific solution port.
     *
     * @param userId           userId of user making request
     * @param solutionPortGUID unique identifier of the required element
     * @param getOptions       multiple options to control the query
     * @return retrieved properties
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public OpenMetadataRootElement getSolutionPortByGUID(String     userId,
                                                         String     solutionPortGUID,
                                                         GetOptions getOptions) throws InvalidParameterException,
                                                                                       PropertyServerException,
                                                                                       UserNotAuthorizedException
    {
        final String methodName        = "getSolutionPortByGUID";
        final String guidParameterName = "solutionPortGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(solutionPortGUID, guidParameterName, methodName);

        return super.getRootElementByGUID(userId, solutionPortGUID, getOptions, methodName);
    }


    /**
     * Retrieve the list of solution ports that contain the search string.
     *
     * @param userId        calling user
     * @param searchString  string to find in the properties
     * @param searchOptions multiple options to control the query
     * @return list of matching metadata elements
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<OpenMetadataRootElement> findSolutionPorts(String        userId,
                                                           String        searchString,
                                                           SearchOptions searchOptions) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException
    {
        final String methodName = "findSolutionPorts";

        return super.findRootElements(userId, searchString, searchOptions, methodName);
    }


    /**
     * Attach a solution port to the solution component that exposes it.
     *
     * @param userId                 userId of user making request
     * @param solutionComponentGUID  unique identifier of the solution component
     * @param solutionPortGUID       unique identifier of the solution port
     * @param makeAnchorOptions      options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSolutionComponentPort(String                          userId,
                                          String                          solutionComponentGUID,
                                          String                          solutionPortGUID,
                                          MakeAnchorOptions               makeAnchorOptions,
                                          SolutionComponentPortProperties relationshipProperties) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        final String methodName            = "linkSolutionComponentPort";
        final String end1GUIDParameterName = "solutionComponentGUID";
        final String end2GUIDParameterName = "solutionPortGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(solutionComponentGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(solutionPortGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.SOLUTION_COMPONENT_PORT_RELATIONSHIP.typeName,
                                                        solutionComponentGUID,
                                                        solutionPortGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a solution port from the solution component that exposed it.
     *
     * @param userId                userId of user making request.
     * @param solutionComponentGUID unique identifier of the solution component
     * @param solutionPortGUID      unique identifier of the solution port
     * @param deleteOptions         options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSolutionComponentPort(String        userId,
                                            String        solutionComponentGUID,
                                            String        solutionPortGUID,
                                            DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                PropertyServerException,
                                                                                UserNotAuthorizedException
    {
        final String methodName            = "detachSolutionComponentPort";
        final String end1GUIDParameterName = "solutionComponentGUID";
        final String end2GUIDParameterName = "solutionPortGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(solutionComponentGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(solutionPortGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.SOLUTION_COMPONENT_PORT_RELATIONSHIP.typeName,
                                                        solutionComponentGUID,
                                                        solutionPortGUID,
                                                        deleteOptions);
    }


    /**
     * Attach a solution port to the solution port that it delegates to.  The port that aligns to is typically on a
     * composite solution component and the delegation port is on one of its subcomponents.
     *
     * @param userId                 userId of user making request
     * @param alignsToPortGUID       unique identifier of the solution port that is aligned to
     * @param delegationPortGUID     unique identifier of the solution port that delegates
     * @param makeAnchorOptions      options to control access to open metadata
     * @param relationshipProperties description of the relationship.
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void linkSolutionPortDelegation(String                           userId,
                                           String                           alignsToPortGUID,
                                           String                           delegationPortGUID,
                                           MakeAnchorOptions                makeAnchorOptions,
                                           SolutionPortDelegationProperties relationshipProperties) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        final String methodName            = "linkSolutionPortDelegation";
        final String end1GUIDParameterName = "alignsToPortGUID";
        final String end2GUIDParameterName = "delegationPortGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(alignsToPortGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(delegationPortGUID, end2GUIDParameterName, methodName);

        openMetadataClient.createRelatedElementsInStore(userId,
                                                        OpenMetadataType.SOLUTION_PORT_DELEGATION_RELATIONSHIP.typeName,
                                                        alignsToPortGUID,
                                                        delegationPortGUID,
                                                        makeAnchorOptions,
                                                        relationshipBuilder.getNewElementProperties(relationshipProperties));
    }


    /**
     * Detach a solution port from the solution port that it delegated to.
     *
     * @param userId             userId of user making request.
     * @param alignsToPortGUID   unique identifier of the solution port that is aligned to
     * @param delegationPortGUID unique identifier of the solution port that delegates
     * @param deleteOptions      options to control access to open metadata
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     * @throws PropertyServerException    there is a problem retrieving information from the property server(s).
     * @throws UserNotAuthorizedException the requesting user is not authorized to issue this request.
     */
    public void detachSolutionPortDelegation(String        userId,
                                             String        alignsToPortGUID,
                                             String        delegationPortGUID,
                                             DeleteOptions deleteOptions) throws InvalidParameterException,
                                                                                 PropertyServerException,
                                                                                 UserNotAuthorizedException
    {
        final String methodName            = "detachSolutionPortDelegation";
        final String end1GUIDParameterName = "alignsToPortGUID";
        final String end2GUIDParameterName = "delegationPortGUID";

        propertyHelper.validateUserId(userId, methodName);
        propertyHelper.validateGUID(alignsToPortGUID, end1GUIDParameterName, methodName);
        propertyHelper.validateGUID(delegationPortGUID, end2GUIDParameterName, methodName);

        openMetadataClient.detachRelatedElementsInStore(userId,
                                                        OpenMetadataType.SOLUTION_PORT_DELEGATION_RELATIONSHIP.typeName,
                                                        alignsToPortGUID,
                                                        delegationPortGUID,
                                                        deleteOptions);
    }
}
