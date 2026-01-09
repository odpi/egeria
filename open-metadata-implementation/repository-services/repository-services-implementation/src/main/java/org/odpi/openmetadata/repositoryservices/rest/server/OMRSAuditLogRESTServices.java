/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.repositoryservices.rest.server;

import org.odpi.openmetadata.adminservices.configuration.registration.CommonServicesDescription;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallLogger;
import org.odpi.openmetadata.commonservices.ffdc.RESTCallToken;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLog;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogRecordSeverity;
import org.odpi.openmetadata.repositoryservices.rest.properties.AuditLogReportResponse;
import org.odpi.openmetadata.repositoryservices.rest.properties.AuditLogSeveritiesResponse;
import org.odpi.openmetadata.repositoryservices.rest.services.OMRSRepositoryServicesInstance;
import org.odpi.openmetadata.repositoryservices.rest.services.OMRSRepositoryServicesInstanceHandler;
import org.odpi.openmetadata.tokencontroller.TokenController;
import org.slf4j.LoggerFactory;

/**
 * OMRSAuditLogRESTServices provides the server-side logic to support the query interface for the audit log.
 */
public class OMRSAuditLogRESTServices extends TokenController
{
    private static final String  serviceName  = CommonServicesDescription.REPOSITORY_SERVICES.getServiceName();

    private static final OMRSRepositoryServicesInstanceHandler instanceHandler = new OMRSRepositoryServicesInstanceHandler(serviceName);
    private static final OMRSRESTExceptionHandler              exceptionHandler = new OMRSRESTExceptionHandler(instanceHandler);

    private static final RESTCallLogger restCallLogger = new RESTCallLogger(LoggerFactory.getLogger(OMRSAuditLogRESTServices.class),
                                                                            instanceHandler.getServiceName());
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
     * @param delegatingUserId external userId making request
     * @return list of severity definitions
     */
    public AuditLogSeveritiesResponse getSeverityList(String serverName,
                                                      String delegatingUserId)
    {
        final  String   methodName = "getSeverityList";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLogSeveritiesResponse response = new AuditLogSeveritiesResponse();
        AuditLog           auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, delegatingUserId, serverName, methodName);

            /*
             * Validate that the serverName and userId permit the request.
             */
            instanceHandler.getInstance(userId, serverName, methodName);
            response.setSeverities(OMRSAuditLogRecordSeverity.getSeverityList());
        }
        catch (Throwable  error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, serverName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }


    /**
     * Return the report from the audit log for this server.
     *
     * @param serverName name of server
     * @param delegatingUserId external userId making request
     * @return report of the audit log
     */
    public AuditLogReportResponse getAuditLogReport(String serverName,
                                                    String delegatingUserId)
    {
        final  String   methodName = "getAuditLogReport";

        RESTCallToken token = restCallLogger.logRESTCall(serverName, methodName);

        AuditLogReportResponse response = new AuditLogReportResponse();
        AuditLog               auditLog = null;

        try
        {
            String userId = super.getUser(instanceHandler.getServiceName(), methodName);

            restCallLogger.setUserId(token, userId);

            auditLog = instanceHandler.getAuditLog(userId, delegatingUserId, serverName, methodName);

            /*
             * Validate that the serverName and userId permit the request.
             */
            OMRSRepositoryServicesInstance instance = instanceHandler.getInstance(userId, serverName, methodName);
            AuditLog                       masterAuditLog = instance.getMasterAuditLog();

            if (masterAuditLog instanceof OMRSAuditLog omrsAuditLog)
            {
                /*
                 * OMRS Adds support for multiple destinations for audit log messages.
                 */
                response.setReport(omrsAuditLog.getFullReport());
            }
        }
        catch (Throwable  error)
        {
            exceptionHandler.captureRuntimeExceptions(response, error, serverName, auditLog);
        }

        restCallLogger.logRESTCallReturn(token, response.toString());

        return response;
    }
}
