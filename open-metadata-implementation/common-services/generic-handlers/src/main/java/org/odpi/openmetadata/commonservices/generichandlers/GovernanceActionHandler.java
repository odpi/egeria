/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.generichandlers;

import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryHandler;
import org.odpi.openmetadata.commonservices.repositoryhandler.RepositoryRelationshipsIterator;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.metadatasecurity.server.OpenMetadataServerSecurityVerifier;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * MetadataElementHandler manages MetadataElement objects from the Governance Action Framework (GAF).
 * These objects are 1-1 with an open metadata entity.
 */
public class GovernanceActionHandler<B> extends OpenMetadataAPIGenericHandler<B>
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
    public GovernanceActionHandler(OpenMetadataAPIGenericConverter<B> converter,
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
     * Create a governance action in the metadata store which will trigger the governance action service
     * associated with the supplied request type.  The governance action remains to act as a record
     * of the actions taken for auditing.
     *
     * @param userId caller's userId
     * @param qualifiedName unique identifier to give this governance action
     * @param domainIdentifier governance domain associated with this action (0=ALL)
     * @param displayName display name for this action
     * @param description description for this action
     * @param requestSourceGUIDs  request source elements for the resulting governance action service
     * @param actionTargetGUIDs list of action targets for the resulting governance action service
     * @param startTime future start time or null for "as soon as possible"
     * @param governanceEngineName name of the governance engine that should execute the request
     * @param requestType request type to identify the governance action service to run
     * @param requestProperties properties to pass to the governance action service
     * @param originatorGUID unique identifier of the originator - typically an ActorProfile or Process such as a GovernanceService.
     * @param originatorEngineName optional unique name of the governance engine (if initiated by a governance engine).
     *
     * @return unique identifier of the governance action
     * @throws InvalidParameterException null qualified name
     * @throws UserNotAuthorizedException this governance action service is not authorized to create a governance action
     * @throws PropertyServerException there is a problem with the metadata store
     */
    public String initiateGovernanceAction(String              userId,
                                           String              qualifiedName,
                                           int                 domainIdentifier,
                                           String              displayName,
                                           String              description,
                                           List<String>        requestSourceGUIDs,
                                           List<String>        actionTargetGUIDs,
                                           Date                startTime,
                                           String              governanceEngineName,
                                           String              requestType,
                                           Map<String, String> requestProperties,
                                           String              originatorGUID,
                                           String              originatorEngineName) throws InvalidParameterException,
                                                                                            UserNotAuthorizedException,
                                                                                            PropertyServerException
    {
        final String methodName                 = "initiateGovernanceAction";
        final String qualifiedNameParameterName = "qualifiedName";
        final String engineNameParameterName    = "governanceEngineName";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateName(qualifiedName, qualifiedNameParameterName, methodName);
        invalidParameterHandler.validateName(governanceEngineName, engineNameParameterName, methodName);

        return null;

    }


    /**
     * Request the status of an executing governance action request.
     *
     * @param userId identifier of calling user
     * @param governanceActionGUID identifier of the governance action request
     * @param methodName calling method
     *
     * @return status enum
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    public B getGovernanceAction(String userId,
                                 String governanceActionGUID,
                                 String methodName) throws InvalidParameterException,
                                                           UserNotAuthorizedException,
                                                           PropertyServerException
    {
        final String guidParameterName = "governanceActionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceActionGUID, guidParameterName, methodName);

        EntityDetail primaryEntity = this.getEntityFromRepository(userId,
                                                                  governanceActionGUID,
                                                                  guidParameterName,
                                                                  OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_NAME,
                                                                  methodName);

        if (primaryEntity != null)
        {
            List<Relationship> relationships = new ArrayList<>();
            List<EntityDetail> supplementaryEntities = new ArrayList<>();

            RepositoryRelationshipsIterator iterator = new RepositoryRelationshipsIterator(repositoryHandler,
                                                                                           userId,
                                                                                           governanceActionGUID,
                                                                                           OpenMetadataAPIMapper.GOVERNANCE_ACTION_TYPE_NAME,
                                                                                           null,
                                                                                           null,
                                                                                           0,
                                                                                           invalidParameterHandler.getMaxPagingSize(),
                                                                                           methodName);

        }

        return null;
    }


    /**
     * Request that execution of a governance action is allocated to the caller.
     *
     * @param userId identifier of calling user
     * @param governanceActionGUID identifier of the governance action request.
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    public void claimGovernanceAction(String userId,
                                      String governanceActionGUID) throws InvalidParameterException,
                                                                          UserNotAuthorizedException,
                                                                          PropertyServerException
    {
        final String methodName = "claimGovernanceAction";
        final String guidParameterName = "governanceActionGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceActionGUID, guidParameterName, methodName);


    }


    /**
     * Retrieve the governance actions that are still in process and that have been claimed by this caller's userId.
     * This call is used when the caller restarts.
     *
     * @param userId userId of caller
     * @param governanceEngineGUID unique identifier of governance engine
     * @param startFrom starting from element
     * @param pageSize maximum elements to return
     * @return list of governance action elements
     *
     * @throws InvalidParameterException one of the parameters is null or invalid.
     * @throws UserNotAuthorizedException user not authorized to issue this request.
     * @throws PropertyServerException there was a problem detected by the metadata store.
     */
    public List<B>  getActiveClaimedGovernanceActions(String userId,
                                                      String governanceEngineGUID,
                                                      int    startFrom,
                                                      int    pageSize) throws InvalidParameterException,
                                                                              UserNotAuthorizedException,
                                                                              PropertyServerException
    {
        final String methodName = "getActiveClaimedGovernanceActions";
        final String guidParameterName = "governanceEngineGUID";

        invalidParameterHandler.validateUserId(userId, methodName);
        invalidParameterHandler.validateGUID(governanceEngineGUID, guidParameterName, methodName);


        return null;
    }
}
