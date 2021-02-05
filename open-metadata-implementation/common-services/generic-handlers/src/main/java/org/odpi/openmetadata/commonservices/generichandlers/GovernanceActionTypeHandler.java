/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.List;
import java.util.Map;

/**
 * GovernanceActionTypeHandler manages GovernanceActionType entities and their relationships.
 */
public class GovernanceActionTypeHandler<B> extends OpenMetadataAPIGenericHandler<B>
{

    /**
     * Construct the handler for metadata elements.
     *
     * @param converter specific converter for this bean class
     * @param beanClass name of bean class that is represented by the generic class B
     * @param serviceName      name of this service
     * @param serverName       name of the local server
     * @param invalidParameterHandler handler for managing parameter errors
     * @param repositoryHandler     manages calls to the repository services
     * @param repositoryHelper provides utilities for manipulating the repository services objects
     * @param localServerUserId userId for this server
     * @param securityVerifier open metadata security services verifier
     * @param supportedZones list of zones that the access service is allowed to serve Asset instances from.
     * @param defaultZones list of zones that the access service should set in all new Asset instances.
     * @param publishZones list of zones that the access service sets up in published Asset instances.
     * @param auditLog destination for audit log events.
     */
    public GovernanceActionTypeHandler(OpenMetadataAPIGenericConverter<B> converter,
                                       Class<B>                           beanClass,
                                       String                             serviceName,
                                       String                             serverName,
                                       InvalidParameterHandler            invalidParameterHandler,
                                       RepositoryHandler                  repositoryHandler,
                                       OMRSRepositoryHelper               repositoryHelper,
                                       String                             localServerUserId,
                                       OpenMetadataServerSecurityVerifier securityVerifier,
                                       List<String>                       supportedZones,
                                       List<String>                       defaultZones,
                                       List<String>                       publishZones,
                                       AuditLog                           auditLog)
    {
        super(converter,
              beanClass,
              serviceName,
              serverName,
              invalidParameterHandler,
              repositoryHandler,
              repositoryHelper,
              localServerUserId,
              securityVerifier,
              supportedZones,
              defaultZones,
              publishZones,
              auditLog);

    }


    /**
     * Create a new metadata element to represent a governance action type.
     *
     * @param userId calling user
     * @param qualifiedName unique name for the governance action
     * @param domainIdentifier governance domain for this governance action
     * @param displayName short display name for the governance action
     * @param description description of the governance action
     * @param owner identifier of the owner of this governance action
     * @param ownerType type of owner (userId or actor profile)
     * @param supportedGuards list of guards that triggered this governance action
     * @param additionalProperties additional properties for a governance action
     * @param methodName calling method
     *
     * @return unique identifier of the new governance action type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String createGovernanceActionType(String               userId,
                                             String               qualifiedName,
                                             int                  domainIdentifier,
                                             String               displayName,
                                             String               description,
                                             String               owner,
                                             int                  ownerType,
                                             List<String>         supportedGuards,
                                             Map<String, String>  additionalProperties,
                                             String               methodName) throws InvalidParameterException,
                                                                                     UserNotAuthorizedException,
                                                                                     PropertyServerException
    {
        final String qualifiedNameParameterName = "actionTypeProperties.getQualifiedName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);

        GovernanceActionTypeBuilder builder = new GovernanceActionTypeBuilder(qualifiedName,
                                                                              domainIdentifier,
                                                                              displayName,
                                                                              description,
                                                                              owner,
                                                                              ownerType,
                                                                              supportedGuards,
                                                                              additionalProperties,
                                                                              repositoryHelper,
                                                                              serviceName,
                                                                              serverName);

        return this.createBeanInRepository(userId,
                                           null,
                                           null,
                                           OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_TYPE_GUID,
                                           OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                           qualifiedName,
                                           OpenMetadataAPIMapper.QUALIFIED_NAME_PROPERTY_NAME,
                                           builder,
                                           methodName);
    }



    /**
     * Update the metadata element representing a governance action type.
     *
     * @param userId calling user
     * @param actionTypeGUID unique identifier of the metadata element to update
     * @param isMergeUpdate should the new properties be merged with existing properties (true) or completely replace them (false)?
     * @param qualifiedName unique name for the governance action
     * @param domainIdentifier governance domain for this governance action
     * @param displayName short display name for the governance action
     * @param description description of the governance action
     * @param owner identifier of the owner of this governance action
     * @param ownerType type of owner (userId or actor profile)
     * @param supportedGuards list of guards that triggered this governance action
     * @param additionalProperties additional properties for a governance action
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateGovernanceActionType(String              userId,
                                           String              actionTypeGUID,
                                           boolean             isMergeUpdate,
                                           String              qualifiedName,
                                           int                 domainIdentifier,
                                           String              displayName,
                                           String              description,
                                           String              owner,
                                           int                 ownerType,
                                           List<String>        supportedGuards,
                                           Map<String, String> additionalProperties,
                                           String              methodName) throws InvalidParameterException,
                                                                                  UserNotAuthorizedException,
                                                                                  PropertyServerException
    {
        final String guidParameterName = "actionTypeGUID";
        final String propertiesParameterName = "actionTypeProperties";
        final String qualifiedNameParameterName = "actionTypeProperties.getQualifiedName";
        final String urlTemplate = "/servers/{0}/open-metadata/access-services/governance-engine/users/{1}/governance-action-types/{2}/update";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(actionTypeGUID, guidParameterName, methodName);

        if (! isMergeUpdate)
        {
            invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);
        }


    }


    /**
     * Remove the metadata element representing a governance action type.
     *
     * @param userId calling user
     * @param actionTypeGUID unique identifier of the metadata element to remove
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeGovernanceActionType(String userId,
                                           String actionTypeGUID,
                                           String methodName) throws InvalidParameterException,
                                                                         UserNotAuthorizedException,
                                                                         PropertyServerException
    {
        final String guidParameterName = "actionTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(actionTypeGUID, guidParameterName, methodName);

        this.deleteBeanInRepository(userId,
                                    null,
                                    null,
                                    actionTypeGUID,
                                    guidParameterName,
                                    OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_TYPE_GUID,
                                    OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_TYPE_NAME,
                                    null,
                                    null,
                                    methodName);
    }


    /**
     * Retrieve the list of governance action type metadata elements that contain the search string.
     * The search string is treated as a regular expression.
     *
     * @param userId calling user
     * @param searchString string to find in the properties
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> findGovernanceActionTypes(String userId,
                                             String searchString,
                                             int    startFrom,
                                             int    pageSize,
                                             String methodName) throws InvalidParameterException,
                                                                       UserNotAuthorizedException,
                                                                       PropertyServerException
    {
        final String searchStringParameterName = "searchString";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateSearchString(searchString, searchStringParameterName, methodName);

        return null;
    }


    /**
     * Retrieve the list of governance action type metadata elements with a matching qualified or display name.
     * There are no wildcards supported on this request.
     *
     * @param userId calling user
     * @param name name to search for
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return list of matching metadata elements
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public List<B> getGovernanceActionTypesByName(String userId,
                                                  String name,
                                                  int    startFrom,
                                                  int    pageSize,
                                                  String methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String nameParameterName = "name";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(name, nameParameterName, methodName);

        return null;
    }


    /**
     * Retrieve the governance action type metadata element with the supplied unique identifier.
     *
     * @param userId calling user
     * @param actionTypeGUID unique identifier of the governance action type
     * @param methodName calling method
     *
     * @return requested metadata element
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public B getGovernanceActionTypeByGUID(String userId,
                                           String actionTypeGUID,
                                           String methodName) throws InvalidParameterException,
                                                                     UserNotAuthorizedException,
                                                                     PropertyServerException
    {
        final String guidParameterName = "actionTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(actionTypeGUID, guidParameterName, methodName);

        return null;
    }



    /**
     * Set up a link between an governance action process and a governance action type.  This defines the first
     * step in the process.
     *
     * @param userId calling user
     * @param processGUID unique identifier of the governance action process
     * @param actionTypeGUID unique identifier of the governance action type
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void setupFirstActionType(String userId,
                                     String processGUID,
                                     String actionTypeGUID,
                                     String methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String processGUIDParameterName = "processGUID";
        final String actionTypeGUIDParameterName = "actionTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, processGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(actionTypeGUID, actionTypeGUIDParameterName, methodName);


    }


    /**
     * Return the governance action type that is the first step in a governance action process.
     *
     * @param userId calling user
     * @param processGUID unique identifier of the governance action process
     * @param methodName calling method
     *
     * @return properties of the governance action type
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public B getFirstActionType(String userId,
                                String processGUID,
                                String methodName) throws InvalidParameterException,
                                                          UserNotAuthorizedException,
                                                          PropertyServerException
    {
        final String guidParameterName = "processGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, guidParameterName, methodName);

        return null;
    }


    /**
     * Remove the link between a governance process and that governance action type that defines its first step.
     *
     * @param userId calling user
     * @param processGUID unique identifier of the governance action process
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeFirstActionType(String userId,
                                      String processGUID,
                                      String methodName) throws InvalidParameterException,
                                                                 UserNotAuthorizedException,
                                                                 PropertyServerException
    {
        final String guidParameterName = "processGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(processGUID, guidParameterName, methodName);


    }



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
     * @param methodName calling method
     *
     * @return unique identifier of the new link
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public String setupNextActionType(String  userId,
                                      String  currentActionTypeGUID,
                                      String  nextActionTypeGUID,
                                      String  guard,
                                      boolean mandatoryGuard,
                                      boolean ignoreMultipleTriggers,
                                      String methodName) throws InvalidParameterException,
                                                                             UserNotAuthorizedException,
                                                                             PropertyServerException
    {
        final String currentGUIDParameterName = "currentActionTypeGUID";
        final String nextGUIDParameterName = "nextActionTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(currentActionTypeGUID, currentGUIDParameterName, methodName);
        invalidParameterHandler.validateGUID(nextActionTypeGUID, nextGUIDParameterName, methodName);

        return null;
    }


    /**
     * Update the properties of the link between two governance action types that shows that one follows on from the other when a governance
     * action process is executing.
     *
     * @param userId calling user
     * @param nextActionLinkGUID unique identifier of the relationship between the governance action types
     * @param guard guard required for this next step to proceed - or null for always run the next step.
     * @param mandatoryGuard means that no next steps can run if this guard is not returned
     * @param ignoreMultipleTriggers prevent multiple instances of the next step to run (or not)
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void updateNextActionType(String  userId,
                                     String  nextActionLinkGUID,
                                     String  guard,
                                     boolean mandatoryGuard,
                                     boolean ignoreMultipleTriggers,
                                     String methodName) throws InvalidParameterException,
                                                                            UserNotAuthorizedException,
                                                                            PropertyServerException
    {
        final String guidParameterName = "nextActionLinkGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(nextActionLinkGUID, guidParameterName, methodName);


    }


    /**
     * Return the lust of next action type defined for the governance action process.
     *
     * @param userId calling user
     * @param actionTypeGUID unique identifier of the current governance action type
     * @param startFrom paging start point
     * @param pageSize maximum results that can be returned
     * @param methodName calling method
     *
     * @return return the list of relationships and attached governance action types.
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public Relationship getNextGovernanceActionTypes(String userId,
                                                                              String actionTypeGUID,
                                                                              int    startFrom,
                                                                              int    pageSize,
                                                                              String methodName) throws InvalidParameterException,
                                                                                                      UserNotAuthorizedException,
                                                                                                      PropertyServerException
    {
        final String guidParameterName = "actionTypeGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(actionTypeGUID, guidParameterName, methodName);

       return null;
    }


    /**
     * Remove a follow on step from a governance action process.
     *
     * @param userId calling user
     * @param actionLinkGUID unique identifier of the relationship between the governance action types
     * @param methodName calling method
     *
     * @throws InvalidParameterException  one of the parameters is invalid
     * @throws UserNotAuthorizedException the user is not authorized to issue this request
     * @throws PropertyServerException    there is a problem reported in the open metadata server(s)
     */
    public void removeNextActionType(String userId,
                                     String actionLinkGUID,
                                     String methodName) throws InvalidParameterException,
                                                                   UserNotAuthorizedException,
                                                                   PropertyServerException
    {
        final String guidParameterName = "processGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(actionLinkGUID, guidParameterName, methodName);

    }
}
