/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.server;


import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.governanceprogram.handlers.GovernanceOfficerHandler;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.ExternalReference;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDomain;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler;
import org.odpi.openmetadata.commonservices.ffdc.rest.GUIDResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.VoidResponse;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * The GovernanceProgramRESTServices provides the server-side implementation of the GovernanceProgram Open Metadata
 * Assess Service (OMAS).  This interface provides connections to assets and APIs for adding feedback
 * on the asset.
 */
public class GovernanceProgramRESTServices
{
    static private GovernanceProgramInstanceHandler instanceHandler = new GovernanceProgramInstanceHandler();

    private static final Logger log = LoggerFactory.getLogger(GovernanceProgramRESTServices.class);

    private RESTExceptionHandler restExceptionHandler = new RESTExceptionHandler();

    /**
     * Default constructor
     */
    public GovernanceProgramRESTServices()
    {
    }

    /**
     * Create the governance officer appointment.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param requestBody  properties of the governance officer.
     * @return Unique identifier (guid) of the governance officer or
     * InvalidParameterException the governance domain or appointment id is null or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public GUIDResponse createGovernanceOfficer(String                               serverName,
                                                String                               userId,
                                                GovernanceOfficerDetailsRequestBody  requestBody)

    {
        final String        methodName = "createGovernanceOfficer";

        log.debug("Calling method: " + methodName);

        GUIDResponse response = new GUIDResponse();
        AuditLog     auditLog = null;

        GovernanceDomain           governanceDomain = null;
        String                     appointmentId = null;
        String                     appointmentContext = null;
        String                     title = null;
        Map<String, String>        additionalProperties = null;
        List<ExternalReference>    externalReferences = null;

        if (requestBody != null)
        {
            governanceDomain = requestBody.getGovernanceDomain();
            appointmentId = requestBody.getAppointmentId();
            appointmentContext = requestBody.getAppointmentContext();
            title = requestBody.getTitle();
            additionalProperties = requestBody.getAdditionalProperties();
            externalReferences = requestBody.getExternalReferences();
        }

        try
        {
            GovernanceOfficerHandler handler = instanceHandler.getGovernanceOfficerHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setGUID(handler.createGovernanceOfficer(userId,
                                                             governanceDomain,
                                                             appointmentId,
                                                             appointmentContext,
                                                             title,
                                                             additionalProperties,
                                                             externalReferences));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Update selected fields for the governance officer.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param requestBody  properties of the governance officer
     * @return void response or
     * UnrecognizedGUIDException the unique identifier of the governance officer is either null or invalid or
     * InvalidParameterException the title is null or the governanceDomain/appointmentId does not match the
     *                           the existing values associated with the governanceOfficerGUID or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public VoidResponse   updateGovernanceOfficer(String                               serverName,
                                                  String                               userId,
                                                  String                               governanceOfficerGUID,
                                                  GovernanceOfficerDetailsRequestBody  requestBody)
    {
        final String        methodName = "updateGovernanceOfficer";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        GovernanceDomain           governanceDomain = null;
        String                     appointmentId = null;
        String                     appointmentContext = null;
        String                     title = null;
        Map<String, String>        additionalProperties = null;
        List<ExternalReference>    externalReferences = null;

        if (requestBody != null)
        {
            governanceDomain = requestBody.getGovernanceDomain();
            appointmentId = requestBody.getAppointmentId();
            appointmentContext = requestBody.getAppointmentContext();
            title = requestBody.getTitle();
            additionalProperties = requestBody.getAdditionalProperties();
            externalReferences = requestBody.getExternalReferences();
        }

        try
        {
            GovernanceOfficerHandler handler = instanceHandler.getGovernanceOfficerHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.updateGovernanceOfficer(userId,
                                            governanceOfficerGUID,
                                            governanceDomain,
                                            appointmentId,
                                            appointmentContext,
                                            title,
                                            additionalProperties,
                                            externalReferences);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Remove the requested governance officer.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param requestBody  properties to verify this is the right governance officer
     * @return void response or
     * UnrecognizedGUIDException the unique identifier of the governance officer is either null or invalid or
     * InvalidParameterException the appointmentId or governance domain is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public VoidResponse   deleteGovernanceOfficer(String                                 serverName,
                                                  String                                 userId,
                                                  String                                 governanceOfficerGUID,
                                                  GovernanceOfficerValidatorRequestBody  requestBody)
    {
        final String        methodName = "deleteGovernanceOfficer";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            GovernanceDomain    governanceDomain = null;
            String              appointmentId = null;

            if (requestBody != null)
            {
                governanceDomain = requestBody.getGovernanceDomain();
                appointmentId = requestBody.getAppointmentId();
            }

            GovernanceOfficerHandler handler = instanceHandler.getGovernanceOfficerHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.deleteGovernanceOfficer(userId,
                                            governanceOfficerGUID,
                                            appointmentId,
                                            governanceDomain);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Retrieve a governance officer description by unique guid.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @return governance officer object or
     * UnrecognizedGUIDException the unique identifier of the governance officer is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public GovernanceOfficerResponse getGovernanceOfficerByGUID(String     serverName,
                                                                String     userId,
                                                                String     governanceOfficerGUID)
    {
        final String        methodName = "getGovernanceOfficerByGUID";

        log.debug("Calling method: " + methodName);

        GovernanceOfficerResponse response = new GovernanceOfficerResponse();
        AuditLog                  auditLog = null;

        try
        {
            GovernanceOfficerHandler handler = instanceHandler.getGovernanceOfficerHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setGovernanceOfficer(handler.getGovernanceOfficerByGUID(userId, governanceOfficerGUID));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Retrieve a governance officer by unique appointment id.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param appointmentId  the unique appointment identifier of the governance officer.
     * @return governance officer object or
     * InvalidParameterException the appointmentId or governance domain is either null or invalid or
     * AppointmentIdNotUniqueException more than one governance officer entity was retrieved for this appointmentId
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public GovernanceOfficerResponse   getGovernanceOfficerByAppointmentId(String     serverName,
                                                                           String     userId,
                                                                           String     appointmentId)
    {
        final String        methodName = "getGovernanceOfficerByAppointmentId";

        log.debug("Calling method: " + methodName);

        GovernanceOfficerResponse response = new GovernanceOfficerResponse();
        AuditLog                  auditLog = null;

        try
        {
            GovernanceOfficerHandler handler = instanceHandler.getGovernanceOfficerHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setGovernanceOfficer(handler.getGovernanceOfficerByAppointmentId(userId, appointmentId));
        }
        catch (AppointmentIdNotUniqueException error)
        {
            captureAppointmentIdNotUniqueException(response, error);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return all of the defined governance officers.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @return list of governance officer objects or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public GovernanceOfficerListResponse  getGovernanceOfficers(String     serverName,
                                                                String     userId)
    {
        final String        methodName = "getGovernanceOfficers";

        log.debug("Calling method: " + methodName);

        GovernanceOfficerListResponse response = new GovernanceOfficerListResponse();
        AuditLog                      auditLog = null;

        try
        {
            GovernanceOfficerHandler handler = instanceHandler.getGovernanceOfficerHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setGovernanceOfficers(handler.getGovernanceOfficers(userId));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return all of the defined governance officers.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @return list of governance officer objects or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public GovernanceOfficerListResponse  getActiveGovernanceOfficers(String     serverName,
                                                                      String     userId)
    {
        final String        methodName = "getActiveGovernanceOfficers";

        log.debug("Calling method: " + methodName);

        GovernanceOfficerListResponse response = new GovernanceOfficerListResponse();
        AuditLog                  auditLog = null;

        try
        {
            GovernanceOfficerHandler handler = instanceHandler.getGovernanceOfficerHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setGovernanceOfficers(handler.getActiveGovernanceOfficers(userId));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Return all of the defined governance officers for a specific governance domain.  In a small organization
     * there is typically only one governance officer.   However a large organization may have multiple governance
     * officers, each with a different scope.  The governance officer with a null scope is the overall leader.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param requestBody domain of interest.
     * @return list of governance officer objects or
     * InvalidParameterException the governance domain is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public GovernanceOfficerListResponse  getGovernanceOfficersByDomain(String                        serverName,
                                                                        String                        userId,
                                                                        GovernanceDomainRequestBody   requestBody)
    {
        final String        methodName = "getGovernanceOfficersByDomain";

        log.debug("Calling method: " + methodName);

        GovernanceOfficerListResponse response = new GovernanceOfficerListResponse();
        AuditLog                      auditLog = null;

        try
        {
            GovernanceDomain  governanceDomain = null;

            if (requestBody != null)
            {
                governanceDomain = requestBody.getGovernanceDomain();
            }

            GovernanceOfficerHandler handler = instanceHandler.getGovernanceOfficerHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            response.setGovernanceOfficers(handler.getGovernanceOfficersByDomain(userId, governanceDomain));
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Link a person to a governance officer.  Only one person may be appointed at any one time.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param requestBody unique identifier for the profile
     * @return void response or
     * UnrecognizedGUIDException the unique identifier of the governance officer or profile is either null or invalid or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public VoidResponse appointGovernanceOfficer(String                  serverName,
                                                 String                  userId,
                                                 String                  governanceOfficerGUID,
                                                 AppointmentRequestBody  requestBody)
    {
        final String        methodName = "appointGovernanceOfficer";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String              profileGUID = null;
            Date                startDate   = null;

            if (requestBody != null)
            {
                profileGUID = requestBody.getGUID();
                startDate   = requestBody.getEffectiveDate();
            }

            GovernanceOfficerHandler handler = instanceHandler.getGovernanceOfficerHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.appointGovernanceOfficer(userId,
                                             governanceOfficerGUID,
                                             profileGUID,
                                             startDate);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /**
     * Unlink a person from a governance officer appointment.
     *
     * @param serverName name of server instance to call
     * @param userId the name of the calling user.
     * @param governanceOfficerGUID unique identifier (guid) of the governance officer.
     * @param requestBody unique identifier for the profile.
     * @return void response or
     * UnrecognizedGUIDException the unique identifier of the governance officer or profile is either null or invalid or
     * InvalidParameterException the profile is not linked to this governance officer or
     * PropertyServerException the server is not available or
     * UserNotAuthorizedException the calling user is not authorized to issue the call.
     */
    public VoidResponse relieveGovernanceOfficer(String                  serverName,
                                                 String                  userId,
                                                 String                  governanceOfficerGUID,
                                                 AppointmentRequestBody  requestBody)
    {
        final String        methodName = "relieveGovernanceOfficer";

        log.debug("Calling method: " + methodName);

        VoidResponse response = new VoidResponse();
        AuditLog     auditLog = null;

        try
        {
            String              profileGUID = null;
            Date                endDate     = null;

            if (requestBody != null)
            {
                profileGUID = requestBody.getGUID();
                endDate     = requestBody.getEffectiveDate();
            }

            GovernanceOfficerHandler handler = instanceHandler.getGovernanceOfficerHandler(userId, serverName, methodName);

            auditLog = instanceHandler.getAuditLog(userId, serverName, methodName);
            handler.relieveGovernanceOfficer(userId,
                                             governanceOfficerGUID,
                                             profileGUID,
                                             endDate);
        }
        catch (Exception error)
        {
            restExceptionHandler.captureExceptions(response, error, methodName, auditLog);
        }

        log.debug("Returning from method: " + methodName + " with response: " + response.toString());

        return response;
    }


    /* ==========================
     * Support methods
     * ==========================
     */


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     * @param exceptionClassName  class name of the exception to recreate
     */
    private void captureCheckedException(GovernanceProgramOMASAPIResponse      response,
                                         GovernanceProgramCheckedExceptionBase error,
                                         String                                exceptionClassName)
    {
        response.setRelatedHTTPCode(error.getReportedHTTPCode());
        response.setExceptionClassName(exceptionClassName);
        response.setExceptionErrorMessage(error.getReportedErrorMessage());
        response.setExceptionSystemAction(error.getReportedSystemAction());
        response.setExceptionUserAction(error.getReportedUserAction());
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     * @param exceptionClassName  class name of the exception to recreate
     * @param exceptionProperties map of properties stored in the exception to help with diagnostics
     */
    private void captureCheckedException(GovernanceProgramOMASAPIResponse      response,
                                         GovernanceProgramCheckedExceptionBase error,
                                         String                                exceptionClassName,
                                         Map<String, Object>                   exceptionProperties)
    {
        this.captureCheckedException(response, error, exceptionClassName);
        response.setExceptionProperties(exceptionProperties);
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    private void captureEmployeeNumberNotUniqueException(GovernanceProgramOMASAPIResponse response,
                                                         EmployeeNumberNotUniqueException error)
    {
        List<EntityDetail> duplicateProfiles = error.getDuplicateProfiles();

        if (duplicateProfiles != null)
        {
            Map<String, Object>  exceptionProperties = new HashMap<>();

            exceptionProperties.put("duplicateProfiles", duplicateProfiles);
            captureCheckedException(response, error, error.getClass().getName(), exceptionProperties);
        }
        else
        {
            captureCheckedException(response, error, error.getClass().getName());
        }
    }


    /**
     * Set the exception information into the response.
     *
     * @param response  REST Response
     * @param error returned response.
     */
    private void captureAppointmentIdNotUniqueException(GovernanceProgramOMASAPIResponse response,
                                                        AppointmentIdNotUniqueException  error)
    {
        List<EntityDetail> duplicatePosts = error.getDuplicatePosts();

        if (duplicatePosts != null)
        {
            Map<String, Object>  exceptionProperties = new HashMap<>();

            exceptionProperties.put("duplicatePosts", duplicatePosts);
            captureCheckedException(response, error, error.getClass().getName(), exceptionProperties);
        }
        else
        {
            captureCheckedException(response, error, error.getClass().getName());
        }
    }
}
