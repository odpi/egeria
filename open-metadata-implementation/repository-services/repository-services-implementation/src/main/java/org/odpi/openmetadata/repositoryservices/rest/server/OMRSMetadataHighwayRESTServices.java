/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.server;

import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.metadatahighway.OMRSMetadataHighwayManager;
import org.odpi.openmetadata.repositoryservices.rest.properties.BooleanResponse;
import org.odpi.openmetadata.repositoryservices.rest.properties.CohortListResponse;
import org.odpi.openmetadata.repositoryservices.rest.properties.CohortMembershipListResponse;
import org.odpi.openmetadata.repositoryservices.rest.properties.CohortMembershipResponse;
import org.odpi.openmetadata.repositoryservices.rest.services.OMRSRepositoryServicesInstance;
import org.odpi.openmetadata.repositoryservices.rest.services.OMRSRepositoryServicesInstanceHandler;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;


/**
 * OMRSMetadataHighwayRESTServices provides the server-side implementation for REST services that support the
 * runtime queries of the connected cohorts.
 */
public class OMRSMetadataHighwayRESTServices extends TokenController
{
    private static final String  serviceName  = CommonServicesDescription.REPOSITORY_SERVICES.getServiceName();

    private static final OMRSRepositoryServicesInstanceHandler instanceHandler = new OMRSRepositoryServicesInstanceHandler(serviceName);
    private static final OMRSRESTExceptionHandler exceptionHandler = new OMRSRESTExceptionHandler(instanceHandler);

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(OMRSMetadataHighwayRESTServices.class),
                                                                            instanceHandler.getServiceName());
    /**
     * Default constructor
     */
    public OMRSMetadataHighwayRESTServices()
    {
    }


    /**
     * Return the details of the cohorts that this server is participating in.
     *
     * @param serverName name of server
     * @param delegatingUserId external userId making request
     * @return variety of properties
     */
    public CohortListResponse getCohortList(String serverName,
                                            String delegatingUserId)
    {
        final  String   methodName = "getCohortList";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        CohortListResponse response = new CohortListResponse();
        AuditLog           auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, delegatingUserId, serverName, methodName);

            OMRSMetadataHighwayManager metadataHighwayManager = getMetadataHighway(userId, serverName, methodName);

            if (metadataHighwayManager != null)
            {
                response.setCohorts(metadataHighwayManager.getCohortDescriptions());
            }
            else
            {
                response.setCohorts(new ArrayList<>());
            }
        }
        catch (Throwable  error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, serverName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return the local registration information used by this server to register with open metadata repository cohorts.
     * No registration time is provided.  Use the cohort specific version to retrieve the registration time.
     *
     * @param serverName server to query
     * @param delegatingUserId external userId making request
     * @return registration properties for server
     */
    public CohortMembershipResponse getLocalRegistration(String serverName,
                                                         String delegatingUserId)
    {
        final  String   methodName = "getLocalRegistration";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        CohortMembershipResponse response = new CohortMembershipResponse();
        AuditLog                 auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, delegatingUserId, serverName, methodName);

            OMRSMetadataHighwayManager metadataHighwayManager = getMetadataHighway(userId, serverName, methodName);

            if (metadataHighwayManager != null)
            {
                response.setCohortMember(metadataHighwayManager.getLocalRegistration());
            }
        }
        catch (Throwable  error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, serverName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return the local registration information used by this server to register with the requested
     * open metadata repository cohort.
     *
     * @param serverName server to query
     * @param delegatingUserId external userId making request
     * @param cohortName name of cohort
     * @return registration properties for server
     */
    public CohortMembershipResponse getLocalRegistration(String serverName,
                                                         String delegatingUserId,
                                                         String cohortName)
    {
        final  String   methodName = "getLocalRegistration (cohort version)";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        CohortMembershipResponse response = new CohortMembershipResponse();
        AuditLog                 auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, delegatingUserId, serverName, methodName);

            OMRSMetadataHighwayManager metadataHighwayManager = getMetadataHighway(userId, serverName, methodName);

            if (metadataHighwayManager != null)
            {
                response.setCohortMember(metadataHighwayManager.getLocalRegistration(cohortName));
            }
        }
        catch (Throwable  error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, serverName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * A new server needs to register the metadataCollectionId for its metadata repository with the other servers in the
     * open metadata repository.  It only needs to do this once and uses a timestamp to record that the registration
     * event has been sent.
     * If the server has already registered in the past, it sends a reregistration request.
     *
     * @param serverName server to query
     * @param delegatingUserId external userId making request
     * @param cohortName name of cohort
     * @return flag indicating that the cohort name was recognized
     */
    public BooleanResponse connectToCohort(String serverName,
                                           String delegatingUserId,
                                           String cohortName)
    {
        final String methodName = "connectToCohort";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        BooleanResponse response = new BooleanResponse();
        AuditLog        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, delegatingUserId, serverName, methodName);

            OMRSMetadataHighwayManager metadataHighwayManager = getMetadataHighway(userId, serverName, methodName);

            /*
             * False means "the cohort name was not recognized", which is exactly the state of a server that
             * has no metadata highway at all.
             */
            response.setFlag((metadataHighwayManager != null) && metadataHighwayManager.connectToCohort(cohortName));
        }
        catch (Throwable  error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, serverName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Return the list of remote registrations received from a specific cohort.
     *
     * @param serverName name of this server
     * @param delegatingUserId external userId making request
     * @param cohortName name of cohort
     * @return list of remote members
     */
    public CohortMembershipListResponse getRemoteRegistrations(String serverName,
                                                               String delegatingUserId,
                                                               String cohortName)
    {
        final  String   methodName = "getRemoteRegistrations";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        CohortMembershipListResponse response = new CohortMembershipListResponse();
        AuditLog                     auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, delegatingUserId, serverName, methodName);

            OMRSMetadataHighwayManager metadataHighwayManager = getMetadataHighway(userId, serverName, methodName);

            if (metadataHighwayManager != null)
            {
                response.setCohortMembers(metadataHighwayManager.getRemoteMembers(cohortName));
            }
            else
            {
                response.setCohortMembers(new ArrayList<>());
            }
        }
        catch (Throwable  error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, serverName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Disconnect communications from a specific cohort.
     *
     * @param serverName server to query
     * @param delegatingUserId external userId making request
     * @param cohortName name of cohort
     * @return boolean flag to indicate success.
     */
    public BooleanResponse disconnectFromCohort(String serverName,
                                                String delegatingUserId,
                                                String cohortName)
    {
        final String methodName = "disconnectFromCohort";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        BooleanResponse response = new BooleanResponse();
        AuditLog        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, delegatingUserId, serverName, methodName);

            OMRSMetadataHighwayManager metadataHighwayManager = getMetadataHighway(userId, serverName, methodName);

            response.setFlag((metadataHighwayManager != null) && metadataHighwayManager.disconnectFromCohort(cohortName, false));
        }
        catch (Throwable  error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, serverName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Unregister from a specific cohort and disconnect from cohort communications.
     *
     * @param serverName server to query
     * @param delegatingUserId external userId making request
     * @param cohortName name of cohort
     * @return boolean flag to indicate success.
     */
    public BooleanResponse unregisterFromCohort(String serverName,
                                                String delegatingUserId,
                                                String cohortName)
    {
        final String methodName = "unregisterFromCohort";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        BooleanResponse response = new BooleanResponse();
        AuditLog        auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, delegatingUserId, serverName, methodName);

            OMRSMetadataHighwayManager metadataHighwayManager = getMetadataHighway(userId, serverName, methodName);

            response.setFlag((metadataHighwayManager != null) && metadataHighwayManager.disconnectFromCohort(cohortName, true));
        }
        catch (Throwable  error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, serverName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response);

        return response;
    }


    /**
     * Validate that the repository connector is available.
     *
     * @param userId name of the calling user.
     * @param serverName name of the server associated with the request.
     * @param methodName method being called
     * @return OMRSMetadataCollection object for the local repository
     * @throws InvalidParameterException unknown servername
     * @throws UserNotAuthorizedException unsupported userId
     * @throws RepositoryErrorException problem retrieving the server instance
     */
    private OMRSMetadataHighwayManager getMetadataHighway(String userId,
                                                          String serverName,
                                                          String methodName) throws InvalidParameterException,
                                                                                    UserNotAuthorizedException,
                                                                                    RepositoryErrorException
    {
        OMRSMetadataHighwayManager   metadataHighwayManager = null;

        if (serverName != null)
        {
            OMRSRepositoryServicesInstance instance = instanceHandler.getInstance(userId, serverName, methodName);

            if (instance != null)
            {
                metadataHighwayManager = instance.getMetadataHighwayManager();
            }
        }

        /*
         * A null manager is returned rather than an error being raised, and the callers above treat it as a
         * server that is a member of no cohorts.
         *
         * Being in no cohort is the ordinary state of most servers, not a failure: a server only has a
         * metadata highway once a cohort has been configured into it.  Reporting it as
         * NO_METADATA_HIGHWAY made a correctly configured server indistinguishable from a broken one, and
         * meant a caller asking "which cohorts is this server in?" could not receive the honest answer of
         * "none".
         *
         * Nothing else is being relaxed here.  An unknown or unstarted server still fails, because
         * instanceHandler.getInstance above raises that, and it is the failure the caller needs.  This is
         * also consistent with how the manager itself answers about a cohort it does not have - null, an
         * empty list, or false, rather than an exception - so a server with no highway now behaves exactly
         * like a server whose highway holds no matching cohort.
         */
        return metadataHighwayManager;
    }
}
