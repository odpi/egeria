/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.server;

import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditCode;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.OMRSCheckedExceptionBase;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.metadatahighway.OMRSMetadataHighwayManager;
import org.odpi.openmetadata.repositoryservices.rest.properties.CohortListResponse;
import org.odpi.openmetadata.repositoryservices.rest.properties.CohortMembershipListResponse;
import org.odpi.openmetadata.repositoryservices.rest.properties.CohortMembershipResponse;
import org.odpi.openmetadata.repositoryservices.rest.properties.OMRSAPIResponse;
import org.odpi.openmetadata.repositoryservices.rest.services.OMRSRepositoryServicesInstance;
import org.odpi.openmetadata.repositoryservices.rest.services.OMRSRepositoryServicesInstanceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

/**
 * OMRSMetadataHighwayRESTServices provides the server-side implementation for REST services that support the
 * runtime queries of the connected cohorts.
 */
public class OMRSMetadataHighwayRESTServices
{
    private static final String  serviceName  = CommonServicesDescription.REPOSITORY_SERVICES.getServiceName();

    private static OMRSRepositoryServicesInstanceHandler instanceHandler = new OMRSRepositoryServicesInstanceHandler(serviceName);

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
            captureInvalidParameterException(response, error);
        }
        catch (UserNotAuthorizedException  error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (RepositoryErrorException error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (Throwable  error)
        {
            captureThrowable(response, error, methodName, instanceHandler.getAuditLog(userId, serverName, methodName));
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

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
            captureInvalidParameterException(response, error);
        }
        catch (UserNotAuthorizedException  error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (RepositoryErrorException error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (Throwable  error)
        {
            captureThrowable(response, error, methodName, instanceHandler.getAuditLog(userId, serverName, methodName));
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

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
            captureInvalidParameterException(response, error);
        }
        catch (UserNotAuthorizedException  error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (RepositoryErrorException error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (Throwable  error)
        {
            captureThrowable(response, error, methodName, instanceHandler.getAuditLog(userId, serverName, methodName));
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

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
            captureInvalidParameterException(response, error);
        }
        catch (UserNotAuthorizedException  error)
        {
            captureUserNotAuthorizedException(response, error);
        }
        catch (RepositoryErrorException error)
        {
            captureRepositoryErrorException(response, error);
        }
        catch (Throwable  error)
        {
            captureThrowable(response, error, methodName, instanceHandler.getAuditLog(userId, serverName, methodName));
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Validate that the repository connector is available.
     *
     * @param userId name of the calling user.
     * @param serverName name of the server associated with the request.
     * @param methodName method being called
     * @throws InvalidParameterException unknown servername
     * @throws UserNotAuthorizedException unsupported userId
     * @throws RepositoryErrorException null local repository
     * @return OMRSMetadataCollection object for the local repository
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
                OMRSErrorCode errorCode = OMRSErrorCode.NO_METADATA_HIGHWAY;
                String errorMessage = errorCode.getErrorMessageId()
                                              + errorCode.getFormattedErrorMessage(methodName);

                throw new RepositoryErrorException(errorCode.getHTTPErrorCode(),
                                                   this.getClass().getName(),
                                                   methodName,
                                                   errorMessage,
                                                   errorCode.getSystemAction(),
                                                   errorCode.getUserAction());

        }

        return metadataHighwayManager;
    }



    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    private void captureUserNotAuthorizedException(OMRSAPIResponse response, UserNotAuthorizedException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    private void captureRepositoryErrorException(OMRSAPIResponse response, RepositoryErrorException error)
    {
        captureCheckedException(response, error, error.getClass().getName());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     */
    private void captureInvalidParameterException(OMRSAPIResponse response, InvalidParameterException error)
    {
        final String propertyName = "parameterName";

        if (error.getParameterName() == null)
        {
            captureCheckedException(response, error, error.getClass().getName());
        }
        else
        {
            Map<String, Object> exceptionProperties = new HashMap<>();

            exceptionProperties.put(propertyName, error.getParameterName());

            captureCheckedException(response, error, error.getClass().getName(), exceptionProperties);
        }
    }




    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response
     * @param methodName calling method
     * @param auditLog log location for recording an unexpected exception
     */
    private void captureThrowable(OMRSAPIResponse response,
                                  Throwable       error,
                                  String          methodName,
                                  OMRSAuditLog    auditLog)
    {
        OMRSErrorCode errorCode = OMRSErrorCode.UNEXPECTED_EXCEPTION;

        String  message = error.getMessage();

        if (message == null)
        {
            message = "null";
        }
        response.setRelatedHTTPCode(errorCode.getHTTPErrorCode());
        response.setExceptionClassName(error.getClass().getName());
        response.setExceptionErrorMessage(errorCode.getFormattedErrorMessage(error.getClass().getName(),
                                                                             methodName,
                                                                             message));
        response.setExceptionSystemAction(errorCode.getSystemAction());
        response.setExceptionUserAction(errorCode.getUserAction());
        response.setExceptionProperties(null);

        if (auditLog != null)
        {
            OMRSAuditCode auditCode = OMRSAuditCode.UNEXPECTED_EXCEPTION;
            auditLog.logException(methodName,
                                  auditCode.getLogMessageId(),
                                  auditCode.getSeverity(),
                                  auditCode.getFormattedLogMessage(error.getClass().getName(),
                                                                   methodName,
                                                                   message),
                                  null,
                                  auditCode.getSystemAction(),
                                  auditCode.getUserAction(),
                                  error);
        }
    }




    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     * @param exceptionClassName class name of the exception to recreate
     */
    private void captureCheckedException(OMRSAPIResponse          response,
                                         OMRSCheckedExceptionBase error,
                                         String                   exceptionClassName)
    {
        this.captureCheckedException(response, error, exceptionClassName, null);
    }


    /**
     * Set the exception information into the response.
     *
     * @param response REST Response
     * @param error returned response.
     * @param exceptionClassName class name of the exception to recreate
     */
    private void captureCheckedException(OMRSAPIResponse          response,
                                         OMRSCheckedExceptionBase error,
                                         String                   exceptionClassName,
                                         Map<String, Object>      exceptionProperties)
    {
        response.setRelatedHTTPCode(error.getReportedHTTPCode());
        response.setExceptionClassName(exceptionClassName);
        response.setExceptionErrorMessage(error.getErrorMessage());
        response.setExceptionSystemAction(error.getReportedSystemAction());
        response.setExceptionUserAction(error.getReportedUserAction());
        response.setExceptionProperties(exceptionProperties);
    }
}
