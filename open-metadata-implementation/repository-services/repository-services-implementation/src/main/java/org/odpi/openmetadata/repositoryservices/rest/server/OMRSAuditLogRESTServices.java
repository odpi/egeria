/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.rest.server;

import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.rest.properties.AuditLogReportResponse;
import org.odpi.openmetadata.repositoryservices.rest.properties.AuditLogSeveritiesResponse;
import org.odpi.openmetadata.repositoryservices.rest.services.OMRSRepositoryServicesInstance;
import org.odpi.openmetadata.repositoryservices.rest.services.OMRSRepositoryServicesInstanceHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * OMRSAuditLogRESTServices provides the server-side logic to support the query interface for the audit log.
 */
public class OMRSAuditLogRESTServices
{
    private static final String  serviceName  = CommonServicesDescription.REPOSITORY_SERVICES.getServiceName();

    private static final OMRSRepositoryServicesInstanceHandler instanceHandler = new OMRSRepositoryServicesInstanceHandler(serviceName);
    private static final OMRSRESTExceptionHandler              exceptionHandler = new OMRSRESTExceptionHandler(instanceHandler);

    private static final Logger log = LoggerFactory.getLogger(OMRSAuditLogRESTServices.class);

    /**
     * Default constructor
     */
    public OMRSAuditLogRESTServices()
    {
    }


    /**
     * Return the details of the severities defined for audit log records from this server.
     *
     * @param serverName name of server
     * @param userId calling user
     * @return list of severity definitions
     */
    public AuditLogSeveritiesResponse getSeverityList(String     serverName,
                                                      String     userId)
    {
        final  String   methodName = "getSeverityList";

        log.debug("Calling method: " + methodName);

        AuditLogSeveritiesResponse response = new AuditLogSeveritiesResponse();

        try
        {
            /*
             * Validate that the serverName and userId permit the request.
             */
            instanceHandler.getInstance(userId, serverName, methodName);
            response.setSeverities(OMRSAuditLogRecordSeverity.getSeverityList());
        }
        catch (InvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (UserNotAuthorizedException error)
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

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return the report from the audit log for this server.
     *
     * @param serverName name of server
     * @param userId calling user
     * @return report of the audit log
     */
    public AuditLogReportResponse getAuditLogReport(String     serverName,
                                                    String     userId)
    {
        final  String   methodName = "getAuditLogReport";

        log.debug("Calling method: " + methodName);

        AuditLogReportResponse response = new AuditLogReportResponse();

        try
        {
            /*
             * Validate that the serverName and userId permit the request.
             */
            OMRSRepositoryServicesInstance instance = instanceHandler.getInstance(userId, serverName, methodName);
            OMRSAuditLog                   masterAuditLog = instance.getMasterAuditLog();

            if (masterAuditLog != null)
            {
                response.setReport(masterAuditLog.getFullReport());
            }
        }
        catch (InvalidParameterException error)
        {
            exceptionHandler.captureInvalidParameterException(response, error);
        }
        catch (UserNotAuthorizedException error)
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

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }
}
