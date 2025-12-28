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

            response.setCohorts(metadataHighwayManager.getCohortDescriptions());
        }
        catch (Throwable  error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, serverName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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

            response.setCohortMember(metadataHighwayManager.getLocalRegistration());
        }
        catch (Throwable  error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, serverName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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

            response.setCohortMember(metadataHighwayManager.getLocalRegistration(cohortName));
        }
        catch (Throwable  error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, serverName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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

            response.setFlag(metadataHighwayManager.connectToCohort(cohortName));
        }
        catch (Throwable  error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, serverName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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

            response.setCohortMembers(metadataHighwayManager.getRemoteMembers(cohortName));
        }
        catch (Throwable  error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, serverName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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

            response.setFlag(metadataHighwayManager.disconnectFromCohort(cohortName, false));
        }
        catch (Throwable  error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, serverName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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

            response.setFlag(metadataHighwayManager.disconnectFromCohort(cohortName, true));
        }
        catch (Throwable  error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, serverName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

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
     * @throws RepositoryErrorException null local repository
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
         * If the local repository is not set up then do not attempt to process the request.
         */
        if (metadataHighwayManager == null)
        {
            throw new RepositoryErrorException(OMRSErrorCode.NO_METADATA_HIGHWAY.getMessageDefinition(methodName),
                                               this.getClass().getName(),
                                               methodName);
        }

        return metadataHighwayManager;
    }
}
