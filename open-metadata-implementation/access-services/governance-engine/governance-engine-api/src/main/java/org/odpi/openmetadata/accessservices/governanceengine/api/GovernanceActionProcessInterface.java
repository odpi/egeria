/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceengine.api;

import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.GovernanceActionProcessElement;
import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.GovernanceActionTypeElement;
import org.odpi.openmetadata.accessservices.governanceengine.metadataelements.NextGovernanceActionTypeElement;
import org.odpi.openmetadata.accessservices.governanceengine.properties.GovernanceActionProcessProperties;
import org.odpi.openmetadata.accessservices.governanceengine.properties.GovernanceActionTypeProperties;
import org.odpi.openmetadata.accessservices.governanceengine.properties.ProcessStatus;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;

import java.util.List;

/**
 * GovernanceActionProcessInterface maintains Governance Action Processes and their Governance Action Types.
 */
public interface GovernanceActionProcessInterface
{
    /* =====================================================================================================================
     * A governance action process describes a well defined series of steps that gets something done.
     * The steps are defined using GovernanceActionTypes.
     */

    /**
     * Create a new metadata element to represent a governance action process.
     *
     * @param userId calling user
     * @param processProperties properties about the process to store
     * @param initialStatus status value for the new process (default = ACTIVE)
     *
     * @return unique identifier of the new process
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createGovernanceActionProcess(String                            userId,
                                         GovernanceActionProcessProperties processProperties,
                                         ProcessStatus                     initialStatus) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException;


    /**
     * Update the metadata element representing a governance action process.
     *
     * @param userId calling user
     * @param processGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param processStatus new status for the process
     * @param processProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateGovernanceActionProcess(String                            userId,
                                       String                            processGUID,
                                       boolean                           isMergeUpdate,
                                       ProcessStatus                     processStatus,
                                       GovernanceActionProcessProperties processProperties) throws InvalidParameterException,
                                                                                                   UserNotAuthorizedException,
                                                                                                   PropertyServerException;


    /**
     * Update the zones for the asset so that it becomes visible to consumers.
     * (The zones are set to the list of zones in the publishedZones option configured for each
     * instance of the Governance Engine OMAS).
     *
     * @param userId calling user
     * @param processGUID unique identifier of the metadata element to publish
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void publishGovernanceActionProcess(String userId,
                                        String processGUID) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException;


    /**
     * Update the zones for the asset so that it is no longer visible to consumers.
     * (The zones are set to the list of zones in the defaultZones option configured for each
     * instance of the Governance Engine OMAS.  This is the setting when the process is first created).
     *
     * @param userId calling user
     * @param processGUID unique identifier of the metadata element to withdraw
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void withdrawGovernanceActionProcess(String userId,
                                         String processGUID) throws InvalidParameterException,
                                                                    UserNotAuthorizedException,
                                                                    PropertyServerException;


    /**
     * Remove the metadata element representing a governance action process.
     *
     * @param userId calling user
     * @param processGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeGovernanceActionProcess(String userId,
                                       String processGUID) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;


    /**
     * Retrieve the list of governance action process metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<GovernanceActionProcessElement> findGovernanceActionProcesses(String userId,
                                                                       String searchString,
                                                                       int    startFrom,
                                                                       int    pageSize) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException;


    /**
     * Retrieve the list of governance action process metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<GovernanceActionProcessElement> getGovernanceActionProcessesByName(String userId,
                                                                            String name,
                                                                            int    startFrom,
                                                                            int    pageSize) throws InvalidParameterException,
                                                                                                    UserNotAuthorizedException,
                                                                                                    PropertyServerException;


    /**
     * Retrieve the governance action process metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param processGUID unique identifier of the requested metadata element
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    GovernanceActionProcessElement getGovernanceActionProcessByGUID(String userId,
                                                                    String processGUID) throws InvalidParameterException,
                                                                                               UserNotAuthorizedException,
                                                                                               PropertyServerException;



    /* =====================================================================================================================
     * A governance action type describes a step in a governance action process
     */

    /**
     * Create a new metadata element to represent a governance action type.
     *
     * @param userId calling user
     * @param actionTypeProperties properties about the process to store
     *
     * @return unique identifier of the new governance action type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    String createGovernanceActionType(String                         userId,
                                      GovernanceActionTypeProperties actionTypeProperties) throws InvalidParameterException,
                                                                                                  UserNotAuthorizedException,
                                                                                                  PropertyServerException;


    /**
     * Update the metadata element representing a governance action type.
     *
     * @param userId calling user
     * @param actionTypeGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param actionTypeProperties new properties for the metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateGovernanceActionType(String                         userId,
                                    String                         actionTypeGUID,
                                    boolean                        isMergeUpdate,
                                    GovernanceActionTypeProperties actionTypeProperties) throws InvalidParameterException,
                                                                                                UserNotAuthorizedException,
                                                                                                PropertyServerException;

    /**
     * Remove the metadata element representing a governance action type.
     *
     * @param userId calling user
     * @param actionTypeGUID unique identifier of the metadata element to remove
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeGovernanceActionType(String userId,
                                    String actionTypeGUID) throws InvalidParameterException,
                                                                  UserNotAuthorizedException,
                                                                  PropertyServerException;


    /**
     * Retrieve the list of governance action type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<GovernanceActionTypeElement> findGovernanceActionTypes(String userId,
                                                                String searchString,
                                                                int    startFrom,
                                                                int    pageSize) throws InvalidParameterException,
                                                                                        UserNotAuthorizedException,
                                                                                        PropertyServerException;


    /**
     * Retrieve the list of governance action type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<GovernanceActionTypeElement> getGovernanceActionTypesByName(String userId,
                                                                     String name,
                                                                     int    startFrom,
                                                                     int    pageSize) throws InvalidParameterException,
                                                                                             UserNotAuthorizedException,
                                                                                             PropertyServerException;


    /**
     * Retrieve the governance action type metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param actionTypeGUID unique identifier of the governance action type
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    GovernanceActionTypeElement getGovernanceActionTypeByGUID(String userId,
                                                              String actionTypeGUID) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException;


    /**
     * Set up a link between a governance action process and a governance action type.  This defines the first
     * step in the process.
     *
     * @param userId calling user
     * @param processGUID unique identifier of the governance action process
     * @param actionTypeGUID unique identifier of the governance action type
     * @param guard optional guard for the first governance service to run
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void setupFirstActionType(String userId,
                              String processGUID,
                              String actionTypeGUID,
                              String guard) throws InvalidParameterException,
                                                   UserNotAuthorizedException,
                                                   PropertyServerException;


    /**
     * Return the governance action type that is the first step in a governance action process.
     *
     * @param userId calling user
     * @param processGUID unique identifier of the governance action process
     *
     * @return properties of the governance action type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    GovernanceActionTypeElement getFirstActionType(String userId,
                                                   String processGUID) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException;


    /**
     * Remove the link between a governance process and that governance action type that defines its first step.
     *
     * @param userId calling user
     * @param processGUID unique identifier of the governance action process
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeFirstActionType(String userId,
                               String processGUID) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException;


    /**
     * Add a link between two governance action types to show that one follows on from the other when a governance action process
     * is executing.
     *
     * @param userId calling user
     * @param currentActionTypeGUID unique identifier of the governance action type that defines the previous step in the governance action process
     * @param nextActionTypeGUID unique identifier of the governance action type that defines the next step in the governance action process
     * @param guard guard required for this next step to proceed - or null for always run the next step.
     * @param mandatoryGuard means that no next steps can run if this guard is not returned
     * @param ignoreMultipleTriggers prevent multiple instances of the next step to run (or not)
     *
     * @return unique identifier of the new link
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
    */
    String setupNextActionType(String  userId,
                               String  currentActionTypeGUID,
                               String  nextActionTypeGUID,
                               String  guard,
                               boolean mandatoryGuard,
                               boolean ignoreMultipleTriggers) throws InvalidParameterException,
                                                                      UserNotAuthorizedException,
                                                                      PropertyServerException;

    /**
     * Update the properties of the link between two governance action types that shows that one follows on from the other when a governance
     * action process is executing.
     *
     * @param userId calling user
     * @param nextActionLinkGUID unique identifier of the relationship between the governance action types
     * @param guard guard required for this next step to proceed - or null for always run the next step.
     * @param mandatoryGuard means that no next steps can run if this guard is not returned
     * @param ignoreMultipleTriggers prevent multiple instances of the next step to run (or not)
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void updateNextActionType(String  userId,
                              String  nextActionLinkGUID,
                              String  guard,
                              boolean mandatoryGuard,
                              boolean ignoreMultipleTriggers) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException;

    /**
     * Return the lust of next action type defined for the governance action process.
     *
     * @param userId calling user
     * @param actionTypeGUID unique identifier of the current governance action type
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     *
     * @return return the list of relationships and attached governance action types.
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    List<NextGovernanceActionTypeElement> getNextGovernanceActionTypes(String userId,
                                                                       String actionTypeGUID,
                                                                       int    startFrom,
                                                                       int    pageSize) throws InvalidParameterException,
                                                                                                     UserNotAuthorizedException,
                                                                                                     PropertyServerException;


    /**
     * Remove a follow-on step from a governance action process.
     *
     * @param userId calling user
     * @param actionLinkGUID unique identifier of the relationship between the governance action types
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    void removeNextActionType(String userId,
                              String actionLinkGUID) throws InvalidParameterException,
                                                            UserNotAuthorizedException,
                                                            PropertyServerException;

}

