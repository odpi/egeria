/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.server;

import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.metadatahighway.OMRSMetadataHighwayManager;
import org.odpi.openmetadata.repositoryservices.rest.properties.BooleanResponse;
import org.odpi.openmetadata.repositoryservices.rest.properties.CohortListResponse;
import org.odpi.openmetadata.repositoryservices.rest.properties.CohortMembershipListResponse;
import org.odpi.openmetadata.repositoryservices.rest.properties.CohortMembershipResponse;
import org.odpi.openmetadata.repositoryservices.rest.services.OMRSRepositoryServicesInstance;
import org.odpi.openmetadata.repositoryservices.rest.services.OMRSRepositoryServicesInstanceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * OMRSMetadataHighwayRESTServices provides the server-side implementation for REST services that support the
 * runtime queries of the connected cohorts.
 */
public class OMRSMetadataHighwayRESTServices
{
    private static final String  serviceName  = CommonServicesDescription.REPOSITORY_SERVICES.getServiceName();

    private static final OMRSRepositoryServicesInstanceHandler instanceHandler = new OMRSRepositoryServicesInstanceHandler(serviceName);
    private static final OMRSRESTExceptionHandler exceptionHandler = new OMRSRESTExceptionHandler(instanceHandler);

    private static final Logger log = LoggerFactory.getLogger(OMRSMetadataHighwayRESTServices.class);

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
     * @param userId calling user
     * @return variety of properties
     */
    public CohortListResponse getCohortList(String     serverName,
                                            String     userId)
    {
        final  String   methodName = "getCohortList";

        log.debug("Calling method: " + methodName);

        CohortListResponse response = new CohortListResponse();

        try
        {
            OMRSMetadataHighwayManager metadataHighwayManager = getMetadataHighway(userId, serverName, methodName);

            response.setCohorts(metadataHighwayManager.getCohortDescriptions());
        }
        catch (InvalidParameterException  error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (UserNotAuthorizedException  error)
        {
            exceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (RepositoryErrorException error)
        {
            exceptionHandler.captureRepositoryErrorException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.captureGenericException(response, error, userId, serverName, methodName);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response);

        return response;
    }


    /**
     * Return the local registration information used by this server to register with open metadata repository cohorts.
     * No registration time is provided.  Use the cohort specific version to retrieve the registration time.
     *
     * @param serverName server to query
     * @param userId calling user
     * @return registration properties for server
     */
    public CohortMembershipResponse getLocalRegistration(String     serverName,
                                                         String     userId)
    {
        final  String   methodName = "getLocalRegistration";

        log.debug("Calling method: " + methodName);

        CohortMembershipResponse response = new CohortMembershipResponse();

        try
        {
            OMRSMetadataHighwayManager metadataHighwayManager = getMetadataHighway(userId, serverName, methodName);

            response.setCohortMember(metadataHighwayManager.getLocalRegistration());
        }
        catch (InvalidParameterException  error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (UserNotAuthorizedException  error)
        {
            exceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (RepositoryErrorException error)
        {
            exceptionHandler.captureRepositoryErrorException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.captureGenericException(response, error, userId, serverName, methodName);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response);

        return response;
    }


    /**
     * Return the local registration information used by this server to register with the requested
     * open metadata repository cohort.
     *
     * @param serverName server to query
     * @param userId calling user
     * @param cohortName name of cohort
     * @return registration properties for server
     */
    public CohortMembershipResponse getLocalRegistration(String     serverName,
                                                         String     userId,
                                                         String     cohortName)
    {
        final  String   methodName = "getLocalRegistration (cohort version)";

        log.debug("Calling method: " + methodName);

        CohortMembershipResponse response = new CohortMembershipResponse();

        try
        {
            OMRSMetadataHighwayManager metadataHighwayManager = getMetadataHighway(userId, serverName, methodName);

            response.setCohortMember(metadataHighwayManager.getLocalRegistration(cohortName));
        }
        catch (InvalidParameterException  error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (UserNotAuthorizedException  error)
        {
            exceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (RepositoryErrorException error)
        {
            exceptionHandler.captureRepositoryErrorException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.captureGenericException(response, error, userId, serverName, methodName);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response);

        return response;
    }


    /**
     * A new server needs to register the metadataCollectionId for its metadata repository with the other servers in the
     * open metadata repository.  It only needs to do this once and uses a timestamp to record that the registration
     * event has been sent.
     *
     * If the server has already registered in the past, it sends a reregistration request.
     *
     * @param serverName server to query
     * @param userId calling user
     * @param cohortName name of cohort
     * @return flag indicating that the cohort name was recognized
     */
    public BooleanResponse connectToCohort(String          serverName,
                                           String          userId,
                                           String          cohortName)
    {
        final String methodName = "connectToCohort";

        log.debug("Calling method: " + methodName);

        BooleanResponse response = new BooleanResponse();

        try
        {
            OMRSMetadataHighwayManager metadataHighwayManager = getMetadataHighway(userId, serverName, methodName);

            response.setFlag(metadataHighwayManager.connectToCohort(cohortName));
        }
        catch (InvalidParameterException  error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (UserNotAuthorizedException  error)
        {
            exceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (RepositoryErrorException error)
        {
            exceptionHandler.captureRepositoryErrorException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.captureGenericException(response, error, userId, serverName, methodName);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response);

        return response;
    }


    /**
     * Return the list of remote registrations received from a specific cohort.
     *
     * @param serverName name of this server
     * @param userId calling user
     * @param cohortName name of cohort
     * @return list of remote members
     */
    public CohortMembershipListResponse getRemoteRegistrations(String     serverName,
                                                               String     userId,
                                                               String     cohortName)
    {
        final  String   methodName = "getRemoteRegistrations";

        log.debug("Calling method: " + methodName);

        CohortMembershipListResponse response = new CohortMembershipListResponse();

        try
        {
            OMRSMetadataHighwayManager metadataHighwayManager = getMetadataHighway(userId, serverName, methodName);

            response.setCohortMembers(metadataHighwayManager.getRemoteMembers(cohortName));
        }
        catch (InvalidParameterException  error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (UserNotAuthorizedException  error)
        {
            exceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (RepositoryErrorException error)
        {
            exceptionHandler.captureRepositoryErrorException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.captureGenericException(response, error, userId, serverName, methodName);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response);

        return response;
    }


    /**
     * Disconnect communications from a specific cohort.
     *
     * @param serverName server to query
     * @param userId calling user
     * @param cohortName name of cohort
     * @return boolean flag to indicate success.
     */
    public BooleanResponse disconnectFromCohort(String serverName,
                                                String userId,
                                                String cohortName)
    {
        final String methodName = "disconnectFromCohort";

        log.debug("Calling method: " + methodName);

        BooleanResponse response = new BooleanResponse();

        try
        {
            OMRSMetadataHighwayManager metadataHighwayManager = getMetadataHighway(userId, serverName, methodName);

            response.setFlag(metadataHighwayManager.disconnectFromCohort(cohortName, false));
        }
        catch (InvalidParameterException  error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (UserNotAuthorizedException  error)
        {
            exceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (RepositoryErrorException error)
        {
            exceptionHandler.captureRepositoryErrorException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.captureGenericException(response, error, userId, serverName, methodName);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response);

        return response;
    }


    /**
     * Unregister from a specific cohort and disconnect from cohort communications.
     *
     * @param serverName server to query
     * @param userId calling user
     * @param cohortName name of cohort
     * @return boolean flag to indicate success.
     */
    public BooleanResponse unregisterFromCohort(String serverName,
                                                String userId,
                                                String cohortName)
    {
        final String methodName = "unregisterFromCohort";

        log.debug("Calling method: " + methodName);

        BooleanResponse response = new BooleanResponse();

        try
        {
            OMRSMetadataHighwayManager metadataHighwayManager = getMetadataHighway(userId, serverName, methodName);

            response.setFlag(metadataHighwayManager.disconnectFromCohort(cohortName, true));
        }
        catch (InvalidParameterException  error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (UserNotAuthorizedException  error)
        {
            exceptionHandler.captureUserNotAuthorizedException(response, error);
        }
        catch (RepositoryErrorException error)
        {
            exceptionHandler.captureRepositoryErrorException(response, error);
        }
        catch (Exception  error)
        {
            exceptionHandler.captureGenericException(response, error, userId, serverName, methodName);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response);

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
