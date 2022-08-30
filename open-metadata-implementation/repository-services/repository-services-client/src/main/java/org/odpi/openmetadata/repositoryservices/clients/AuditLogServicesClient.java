/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.clients;

import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientConnector;
import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientFactory;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.repositoryservices.auditlog.OMRSAuditLogReport;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.InvalidRelationshipException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.rest.properties.AuditLogReportResponse;
import org.odpi.openmetadata.repositoryservices.rest.properties.OMRSAPIResponse;

import java.util.Map;

/**
 * The AuditLogServicesClient supports the OMRS Repository Services APIs for retrieving audit logs.
 * Requests to this client are translated one-for-one to requests to the audit log service since
 * the OMRS REST API has a one-to-one correspondence with the audit log service API.
 * 
 * The URLs for the REST APIs are of this form:
 * 
 * <ul>
 *     <li><i>restURLroot</i> - serverURLroot + "/servers/" + serverName</li>
 *     <li><i>rootServiceNameInURL</i> - "/open-metadata/repository-services"</li>
 *     <li><i>userIdInURL</i> - optional - "/users/{0}"</li>
 *     <li><i>operationSpecificURL</i> - operation specific part of the URL</li>
 * </ul>
 */
public class AuditLogServicesClient implements AuditLoggingComponent
{
    static final private String rootServiceNameInURL  = "/servers/{0}/open-metadata/repository-services";
    static final private String userIdInURL           = "/users/{1}";

    private final String              localServerUserId   = null;
    private final String              localServerPassword = null;

    private final String              restURLRoot;                /* Initialized in constructor */

    private final RESTClientConnector restClient;                 /* Initialized in constructor */
    private final String              serverName;                 /* Initialized in constructor */

    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    protected AuditLog          auditLog = null;


    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName the name of the remote server
     * @param restURLRoot the network address of the server running the repository services.  This is of the form
     *                    serverURLroot + "/servers/" + serverName.
     *
     * @throws InvalidParameterException bad input parameters
     */
    public AuditLogServicesClient(String serverName, String restURLRoot) throws InvalidParameterException
    {
        final String methodName = "Constructor (no security)";

        try
        {
            invalidParameterHandler.validateOMAGServerPlatformURL(restURLRoot, methodName);
        }
        catch (org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException error)
        {
            throw new InvalidParameterException(error.getReportedHTTPCode(),
                                                error.getReportingClassName(),
                                                error.getReportingActionDescription(),
                                                error.getReportedErrorMessage(),
                                                error.getReportedErrorMessageId(),
                                                error.getReportedErrorMessageParameters(),
                                                error.getReportedSystemAction(),
                                                error.getReportedUserAction(),
                                                error.getClass().getName(),
                                                error.getParameterName(),
                                                error.getRelatedProperties());
        }

        this.serverName = serverName;
        this.restURLRoot = restURLRoot;
        this.restClient = this.getRESTClientConnector(serverName, restURLRoot, null, null);

    }


    /**
     * Create a new client that passes userId and password in each HTTP request.  This is the
     * userId/password of the calling server.  The end user's userId is sent on each request.
     *
     * @param serverName the name of the remote server
     * @param restURLRoot the network address of the server running the repository services.  This is of the form
     *                    serverURLroot + "/servers/" + serverName.
     * @param userId caller's userId embedded in all HTTP requests
     * @param password caller's userId embedded in all HTTP requests
     *
     * @throws InvalidParameterException bad input parameters
     */
    public AuditLogServicesClient(String     serverName,
                                  String     restURLRoot,
                                  String     userId,
                                  String     password) throws InvalidParameterException
    {
        final String methodName = "Constructor (with security)";

        try
        {
            invalidParameterHandler.validateOMAGServerPlatformURL(restURLRoot, methodName);
        }
        catch (org.odpi.openmetadata.commonservices.ffdc.exceptions.InvalidParameterException error)
        {
            throw new InvalidParameterException(error.getReportedHTTPCode(),
                                                error.getReportingClassName(),
                                                error.getReportingActionDescription(),
                                                error.getReportedErrorMessage(),
                                                error.getReportedErrorMessageId(),
                                                error.getReportedErrorMessageParameters(),
                                                error.getReportedSystemAction(),
                                                error.getReportedUserAction(),
                                                error.getClass().getName(),
                                                error.getParameterName(),
                                                error.getRelatedProperties());
        }

        this.serverName = serverName;
        this.restURLRoot = restURLRoot;
        this.restClient = this.getRESTClientConnector(serverName, restURLRoot, userId, password);
    }


    /**
     * Receive an audit log object that can be used to record audit log messages.  The caller has initialized it
     * with the correct component description and log destinations.
     *
     * @param auditLog audit log object
     */
    @Override
    public void setAuditLog(AuditLog auditLog)
    {
        this.auditLog = auditLog;
    }


    /**
     * Return the component description that is used by this connector in the audit log.
     *
     * @return id, name, description, wiki page URL.
     */
    @Override
    public ComponentDescription getConnectorComponentDescription()
    {
        if ((this.auditLog != null) && (this.auditLog.getReport() != null))
        {
            return auditLog.getReport().getReportingComponent();
        }

        return null;
    }


    /**
     * Returns the audit log for the server.
     *
     * @param userId calling user
     * @return OMRSAuditLogReport report containing audit log
     * @throws InvalidParameterException one of the supplied parameters caused a problem
     * @throws RepositoryErrorException there is a problem communicating with the remote server.
     * @throws UserNotAuthorizedException the user is not authorized to perform the operation requested
     */
    public OMRSAuditLogReport getAuditLogReport(String   userId) throws InvalidParameterException,
                                                                        RepositoryErrorException,
                                                                        UserNotAuthorizedException
    {
        final String methodName  = "getAuditLogReport";
        final String operationSpecificURL = "/audit-log/report";

        AuditLogReportResponse restResult;

        try
        {
            restResult = restClient.callGetRESTCall(methodName,
                                                    AuditLogReportResponse.class,
                                                    restURLRoot + rootServiceNameInURL + userIdInURL + operationSpecificURL,
                                                    serverName,
                                                    userId);
        }
        catch (Exception error)
        {
            throw new RepositoryErrorException(OMRSErrorCode.REMOTE_REPOSITORY_ERROR.getMessageDefinition(methodName,
                                                                                                          serverName,
                                                                                                          error.getClass().getSimpleName(),
                                                                                                          error.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               error);
        }

        this.detectAndThrowInvalidParameterException(methodName, restResult);
        this.detectAndThrowRepositoryErrorException(methodName, restResult);
        this.detectAndThrowUserNotAuthorizedException(methodName, restResult);


        return restResult.getReport();
    }





    /*
     * ===============================
     * REST Client
     * ===============================
     */


    /**
     * Create a REST client to call the remote connector.
     *
     * @param serverName name of the remote server.
     * @param serverPlatformURLRoot name of the URL root for the server.
     * @param userId userId of this server.
     * @param password password for this server.
     * @return REST Client connector
     * @throws InvalidParameterException an unexpected exception - internal logic error as the parameters should have
     * all been checked before this call.
     */
    private RESTClientConnector getRESTClientConnector(String       serverName,
                                                       String       serverPlatformURLRoot,
                                                       String       userId,
                                                       String       password) throws InvalidParameterException
    {
        final String methodName = "getRESTClientConnector";

        RESTClientFactory clientFactory;

        if ((localServerUserId != null) && (localServerPassword != null))
        {
            clientFactory = new RESTClientFactory(serverName,
                                                  serverPlatformURLRoot,
                                                  userId,
                                                  password);
        }
        else
        {
            clientFactory = new RESTClientFactory(serverName,
                                                  serverPlatformURLRoot);
        }

        try
        {
            return clientFactory.getClientConnector();
        }
        catch (Exception error)
        {
           throw new InvalidParameterException(OMRSErrorCode.NO_REST_CLIENT.getMessageDefinition(serverName, error.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               error,
                                               "client");
        }
    }


    /* =====================
     * Issuing REST Calls
     * =====================
     */


    /**
     * Issue a GET REST call that returns a AuditLogReportResponse object.
     *
     * @param methodName  name of the method being called
     * @param operationSpecificURL  template of the URL for the REST API call, with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return AttributeTypeDefListResponse
     * @throws RepositoryErrorException something went wrong with the REST call stack. TODO
     */
    private AuditLogReportResponse callAuditLogReportGetRESTCall(String    methodName,
                                                                 String    operationSpecificURL,
                                                                 Object... params) throws RepositoryErrorException
    {
        return this.callGetRESTCall(methodName,
                                    AuditLogReportResponse.class,
                                    operationSpecificURL,
                                    params);
    }


    /**
     * Issue a GET REST call that returns the requested object.
     *
     * @param <T> class name
     * @param methodName  name of the method being called
     * @param returnClass class name of response object
     * @param operationSpecificURL  template of the URL for the REST API call, with place-holders for the parameters
     * @return TypeDefResponse
     * @throws RepositoryErrorException something went wrong with the REST call stack.
     */
    private <T> T callGetRESTCall(String    methodName,
                                  Class<T>  returnClass,
                                  String    operationSpecificURL) throws RepositoryErrorException
    {
        return this.callGetRESTCall(methodName, returnClass, operationSpecificURL, (Object[])null);
    }


    /**
     * Issue a GET REST call that returns a TypeDefResponse object.
     *
     * @param <T> class name
     * @param methodName  name of the method being called
     * @param returnClass class name of response object
     * @param operationSpecificURL  template of the URL for the REST API call, with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return TypeDefResponse
     * @throws RepositoryErrorException something went wrong with the REST call stack.
     */
    private <T> T callGetRESTCall(String    methodName,
                                  Class<T>  returnClass,
                                  String    operationSpecificURL,
                                  Object... params) throws RepositoryErrorException
    {
        try
        {
            return restClient.callGetRESTCall(methodName,
                                              returnClass,
                                              operationSpecificURL,
                                              params);
        }
        catch (Exception error)
        {
            throw new RepositoryErrorException(OMRSErrorCode.CLIENT_SIDE_REST_API_ERROR.getMessageDefinition(methodName,
                                                                                                             serverName,
                                                                                                             error.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               error);
        }
    }

    /**
     * Issue a POST REST call that returns the requested type of object.
     *
     * @param <T> class name
     * @param methodName name of the method being called
     * @param returnClass class name of response object
     * @param operationSpecificURL template of the URL for the REST API call, with place-holders for the parameters
     * @param request request body object
     * @param params a list of parameters that are slotted into the url template
     * @return VoidResponse
     * @throws RepositoryErrorException something went wrong with the REST call stack.
     */
    private <T> T callPostRESTCall(String    methodName,
                                   Class<T>  returnClass,
                                   String    operationSpecificURL,
                                   Object    request,
                                   Object... params) throws RepositoryErrorException
    {
        try
        {
            return restClient.callPostRESTCall(methodName,
                                               returnClass,
                                               operationSpecificURL,
                                               request,
                                               params);
        }
        catch (Exception error)
        {
            throw new RepositoryErrorException(OMRSErrorCode.CLIENT_SIDE_REST_API_ERROR.getMessageDefinition(methodName,
                                                                                                             serverName,
                                                                                                             error.getMessage()),
                                               this.getClass().getName(),
                                               methodName,
                                               error);
        }
    }




    /*
     * ===============================
     * Handling exceptions
     * ===============================
     */




    /**
     * Throw an InvalidParameterException if it is encoded in the REST response.
     *
     * @param methodName name of the method called
     * @param restResult response from the rest call.  This generated in the remote server.
     * @throws InvalidParameterException encoded exception from the server
     * @throws RepositoryErrorException invalid parameter is "serverName"
     */
    private void detectAndThrowInvalidParameterException(String          methodName,
                                                         OMRSAPIResponse restResult) throws InvalidParameterException,
                                                                                            RepositoryErrorException
    {
        final String exceptionClassName = InvalidParameterException.class.getName();
        final String propertyName = "parameterName";
        final String serverName = "serverName";


        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            String parameterName = null;

            Map<String, Object>   exceptionProperties = restResult.getExceptionProperties();

            if (exceptionProperties != null)
            {
                Object  nameObject = exceptionProperties.get(propertyName);

                if (nameObject != null)
                {
                    parameterName = (String)nameObject;
                }
            }

            if (serverName.equals(parameterName))
            {
                /*
                 * If the platform is up but the server is not running on the platform the response is
                 * InvalidParameterException because the serverName is invalid - so this is turned into
                 * a RepositoryErrorException as if the whole platform is missing.
                 */
                throw new RepositoryErrorException(restResult.getRelatedHTTPCode(),
                                                   this.getClass().getName(),
                                                   methodName,
                                                   restResult.getExceptionErrorMessage(),
                                                   restResult.getExceptionErrorMessageId(),
                                                   restResult.getExceptionErrorMessageParameters(),
                                                   restResult.getExceptionSystemAction(),
                                                   restResult.getExceptionUserAction(),
                                                   restResult.getExceptionCausedBy(),
                                                   restResult.getExceptionProperties());
            }
            else
            {
                throw new InvalidParameterException(restResult.getRelatedHTTPCode(),
                                                    this.getClass().getName(),
                                                    methodName,
                                                    restResult.getExceptionErrorMessage(),
                                                    restResult.getExceptionErrorMessageId(),
                                                    restResult.getExceptionErrorMessageParameters(),
                                                    restResult.getExceptionSystemAction(),
                                                    restResult.getExceptionUserAction(),
                                                    restResult.getExceptionCausedBy(),
                                                    parameterName,
                                                    restResult.getExceptionProperties());
            }
        }
    }


    /**  TODO consider reusing this for invalid cohort detection - or delete
     * Throw an InvalidRelationshipException if it is encoded in the REST response.
     *
     * @param methodName name of the method called
     * @param restResult response from the rest call.  This generated in the remote server.
     * @throws InvalidRelationshipException encoded exception from the server
     */
    private void detectAndThrowInvalidRelationshipException(String          methodName,
                                                            OMRSAPIResponse restResult) throws InvalidRelationshipException
    {
        final String   exceptionClassName = InvalidRelationshipException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new InvalidRelationshipException(restResult.getRelatedHTTPCode(),
                                                   this.getClass().getName(),
                                                   methodName,
                                                   restResult.getExceptionErrorMessage(),
                                                   restResult.getExceptionErrorMessageId(),
                                                   restResult.getExceptionErrorMessageParameters(),
                                                   restResult.getExceptionSystemAction(),
                                                   restResult.getExceptionUserAction(),
                                                   restResult.getExceptionCausedBy(),
                                                   restResult.getExceptionProperties());
        }
    }



    /**
     * Throw an UserNotAuthorizedException if it is encoded in the REST response.
     *
     * @param methodName name of the method called
     * @param restResult response from the rest call.  This generated in the remote server.
     * @throws UserNotAuthorizedException encoded exception from the server
     */
    private void detectAndThrowUserNotAuthorizedException(String          methodName,
                                                          OMRSAPIResponse restResult) throws UserNotAuthorizedException
    {
        final String   exceptionClassName = UserNotAuthorizedException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            String userId = null;

            if (restResult.getExceptionProperties() != null)
            {
                userId = (String)restResult.getExceptionProperties().get("userId");
            }
            throw new UserNotAuthorizedException(restResult.getRelatedHTTPCode(),
                                                 this.getClass().getName(),
                                                 methodName,
                                                 restResult.getExceptionErrorMessage(),
                                                 restResult.getExceptionErrorMessageId(),
                                                 restResult.getExceptionErrorMessageParameters(),
                                                 restResult.getExceptionSystemAction(),
                                                 restResult.getExceptionUserAction(),
                                                 restResult.getExceptionCausedBy(),
                                                 userId,
                                                 restResult.getExceptionProperties());
        }
    }


    /**
     * This method should be the last "detect and throw" methods that is called since it converts all remaining
     * exceptions to RepositoryErrorExceptions.
     *
     * @param methodName name of method called
     * @param restResult response from the REST API call.
     * @throws RepositoryErrorException resulting exception if response includes an exception.
     */
    private void detectAndThrowRepositoryErrorException(String          methodName,
                                                        OMRSAPIResponse restResult) throws RepositoryErrorException
    {
        if (restResult == null)
        {
            throw new RepositoryErrorException(OMRSErrorCode.NULL_RESPONSE_FROM_API.getMessageDefinition(methodName, serverName),
                                               this.getClass().getName(),
                                               methodName);
        }
        else if (restResult.getExceptionClassName() != null)
        {
            /*
             * All the other expected exceptions have been processed so default exception to RepositoryErrorException
             */
            throw new RepositoryErrorException(restResult.getRelatedHTTPCode(),
                                               this.getClass().getName(),
                                               methodName,
                                               restResult.getExceptionErrorMessage(),
                                               restResult.getExceptionErrorMessageId(),
                                               restResult.getExceptionErrorMessageParameters(),
                                               restResult.getExceptionSystemAction(),
                                               restResult.getExceptionUserAction(),
                                               restResult.getExceptionCausedBy(),
                                               restResult.getExceptionProperties());
        }
    }
}