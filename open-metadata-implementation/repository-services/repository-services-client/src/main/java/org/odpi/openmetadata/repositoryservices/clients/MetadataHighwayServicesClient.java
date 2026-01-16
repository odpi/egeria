/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.clients;

import org.odpi.openmetadata.adapters.connectors.restclients.RESTClientConnector;
import org.odpi.openmetadata.adapters.connectors.restclients.factory.RESTClientFactory;
import org.odpi.openmetadata.commonservices.ffdc.InvalidParameterHandler;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.auditlog.AuditLoggingComponent;
import org.odpi.openmetadata.frameworks.auditlog.ComponentDescription;
import org.odpi.openmetadata.frameworks.connectors.SecretsStoreConnector;
import org.odpi.openmetadata.frameworks.connectors.controls.SecretsStorePurpose;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.cohortregistrystore.properties.MemberRegistration;
import org.odpi.openmetadata.repositoryservices.ffdc.OMRSErrorCode;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.*;
import org.odpi.openmetadata.repositoryservices.properties.CohortDescription;
import org.odpi.openmetadata.repositoryservices.rest.properties.*;
import java.util.List;
import java.util.Map;

/**
 * The MetadataHighwayServicesClient supports the OMRS Metadata Highway REST API.
 * Requests to this client are translated one-for-one to requests to the metadata highway service since
 * the OMRS REST API has a one-to-one correspondence with the metadata highway API.
 * The URLs for the REST APIs are of this form:
 *
 * <ul>
 *     <li><i>restURLroot</i> - serverURLroot + "/servers/" + serverName</li>
 *     <li><i>rootServiceNameInURL</i> - "/open-metadata/repository-services"</li>
 *     <li><i>operationSpecificURL</i> - operation specific part of the URL</li>
 * </ul>
 */
public class MetadataHighwayServicesClient implements AuditLoggingComponent
{
    static final private String rootServiceNameInURL  = "/servers/{0}/open-metadata/repository-services";

    private final String              restURLRoot;                /* Initialized in constructor */

    private final RESTClientConnector restClient;                 /* Initialized in constructor */
    private final String              serverName;                 /* Initialized in constructor */
    private final String              delegatingUserId;           /* Initialized in constructor */

    private final InvalidParameterHandler invalidParameterHandler = new InvalidParameterHandler();

    protected AuditLog          auditLog = null;


    /**
     * Create a new client with bearer token authentication embedded in the HTTP request.
     *
     * @param serverName the name of the remote server
     * @param restURLRoot the network address of the server running the repository services.  This is of the form
     * serverURLroot + "/servers/" + serverName.
     * @param secretsStoreProvider secrets store connector for bearer token
     * @param secretsStoreLocation secrets store location for bearer token
     * @param secretsStoreCollection secrets store collection for bearer token
     * @param delegatingUserId external userId making request
     * @param auditLog destination for log messages.
     *
     * @throws InvalidParameterException bad input parameters
     */
    public MetadataHighwayServicesClient(String   serverName,
                                         String   restURLRoot,
                                         String   secretsStoreProvider,
                                         String   secretsStoreLocation,
                                         String   secretsStoreCollection,
                                         String   delegatingUserId,
                                         AuditLog auditLog) throws InvalidParameterException
    {
        final String methodName = "Constructor (no security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(restURLRoot, methodName);

        this.serverName = serverName;
        this.delegatingUserId = delegatingUserId;
        this.restURLRoot = restURLRoot;
        this.restClient = this.getRESTClientConnector(serverName, restURLRoot, secretsStoreProvider, secretsStoreLocation, secretsStoreCollection, auditLog);
    }

    /**
     * Create a new client with no authentication embedded in the HTTP request.
     *
     * @param serverName the name of the remote server
     * @param restURLRoot the network address of the server running the repository services.  This is of the form
     *                    serverURLroot + "/servers/" + serverName.
     * @param secretsStoreConnectorMap connectors to secrets stores
     * @param delegatingUserId external userId making request
     * @param auditLog destination for log messages.
     *
     * @throws InvalidParameterException bad input parameters
     */
    public MetadataHighwayServicesClient(String                             serverName,
                                         String                             restURLRoot,
                                         Map<String, SecretsStoreConnector> secretsStoreConnectorMap,
                                         String                             delegatingUserId,
                                         AuditLog                           auditLog) throws InvalidParameterException
    {
        final String methodName = "Constructor (no security)";

        invalidParameterHandler.validateOMAGServerPlatformURL(restURLRoot, methodName);

        this.serverName = serverName;
        this.delegatingUserId = delegatingUserId;
        this.restURLRoot = restURLRoot;
        this.restClient = this.getRESTClientConnector(serverName, restURLRoot, secretsStoreConnectorMap, auditLog);
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
     * Returns the descriptions of all cohorts the server is a member of.
     *
     * @return List of CohortDescription object describing cohorts
     * @throws InvalidParameterException one of the supplied parameters caused a problem
     * @throws RepositoryErrorException a problem communicating with the remote server.
     * @throws UserNotAuthorizedException the user is not authorized to perform the operation requested
     */
    public List<CohortDescription> getCohortDescriptions() throws InvalidParameterException,
                                                                  RepositoryErrorException,
                                                                  UserNotAuthorizedException
    {
        final String methodName  = "getCohortDescriptions";
        final String operationSpecificURL = "/metadata-highway/cohort-descriptions?delegatingUserId={1}";

        CohortListResponse restResult;

        try
        {
            restResult = restClient.callGetRESTCall(methodName,
                                                    CohortListResponse.class,
                                                    restURLRoot + rootServiceNameInURL + operationSpecificURL,
                                                    serverName,
                                                    delegatingUserId);
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

        return restResult.getCohorts();
    }



    /**
     * Returns the registration of the local server in the specified cohort.
     *
     * @param cohortName the name of the cohort
     * @return MemberRegistration local registration
     * @throws InvalidParameterException one of the supplied parameters caused a problem
     * @throws RepositoryErrorException a problem communicating with the remote server.
     * @throws UserNotAuthorizedException the user is not authorized to perform the operation requested
     */

    public MemberRegistration getLocalRegistration(String cohortName) throws InvalidParameterException,
                                                                             RepositoryErrorException,
                                                                             UserNotAuthorizedException
    {
        final String methodName  = "getLocalRegistration";
        final String operationSpecificURL = "/metadata-highway/cohorts/"+cohortName+"/local-registration?delegatingUserId={1}";
        final String cohortNameParameter = "cohortName";

        invalidParameterHandler.validateName(cohortName, cohortNameParameter, methodName);

        CohortMembershipResponse restResult = null;

        try
        {
            restResult = restClient.callGetRESTCall(methodName,
                                                    CohortMembershipResponse.class,
                                                    restURLRoot + rootServiceNameInURL + operationSpecificURL,
                                                    serverName,
                                                    delegatingUserId);
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

        return restResult.getCohortMember();
    }


    /**
     * A new server needs to register the metadataCollectionId for its metadata repository with the other servers in the
     * open metadata repository.  It only needs to do this once and uses a timestamp to record that the registration
     * event has been sent.
     * If the server has already registered in the past, it sends a reregistration request.
     *
     * @param cohortName name of cohort
     *
     * @return boolean to indicate that the request has been issued.  If false it is likely that the cohort name is not known
     *
     * @throws InvalidParameterException one of the supplied parameters caused a problem
     * @throws RepositoryErrorException a problem communicating with the remote server.
     * @throws UserNotAuthorizedException the user is not authorized to perform the operation requested
     */
    public boolean connectToCohort(String cohortName) throws InvalidParameterException,
                                                             RepositoryErrorException,
                                                             UserNotAuthorizedException
    {
        final String methodName  = "connectToCohort";
        final String operationSpecificURL = "/metadata-highway/cohorts/"+cohortName+"/connect?delegatingUserId={1}";
        final String cohortNameParameter = "cohortName";

        invalidParameterHandler.validateName(cohortName, cohortNameParameter, methodName);

        BooleanResponse restResult;

        try
        {
            restResult = restClient.callGetRESTCall(methodName,
                                                    BooleanResponse.class,
                                                    restURLRoot + rootServiceNameInURL + operationSpecificURL,
                                                    serverName,
                                                    delegatingUserId);
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

        return restResult.isFlag();
    }


    /**
     * Returns the remote member registrations seen by the local server in the specified cohort.
     *
     * @param cohortName the name of the cohort
     * @return MemberRegistration local registration
     * @throws InvalidParameterException one of the supplied parameters caused a problem
     * @throws RepositoryErrorException a problem communicating with the remote server.
     * @throws UserNotAuthorizedException the user is not authorized to perform the operation requested
     */
    public List<MemberRegistration> getRemoteRegistrations(String cohortName) throws InvalidParameterException,
                                                                                     RepositoryErrorException,
                                                                                     UserNotAuthorizedException
    {
        final String methodName  = "getRemoteRegistrations";
        final String cohortNameParameter = "cohortName";

        invalidParameterHandler.validateName(cohortName, cohortNameParameter, methodName);

        final String operationSpecificURL = "/metadata-highway/cohorts/"+cohortName+"/remote-members?delegatingUserId={1}";

        CohortMembershipListResponse restResult = null;

        try
        {
            restResult = restClient.callGetRESTCall(methodName,
                                                    CohortMembershipListResponse.class,
                                                    restURLRoot + rootServiceNameInURL + operationSpecificURL,
                                                    serverName,
                                                    delegatingUserId);
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

        return restResult.getCohortMembers();
    }


    /**
     * Disconnect communications from a specific cohort.
     *
     * @param cohortName name of cohort
     * @return boolean flag to indicate success.
     * @throws InvalidParameterException one of the supplied parameters caused a problem
     * @throws RepositoryErrorException a problem communicating with the remote server.
     * @throws UserNotAuthorizedException the user is not authorized to perform the operation requested
     */
    public boolean disconnectFromCohort(String cohortName) throws InvalidParameterException,
                                                                  RepositoryErrorException,
                                                                  UserNotAuthorizedException
    {
        final String methodName = "disconnectFromCohort";
        final String operationSpecificURL = "/metadata-highway/cohorts/"+cohortName+"/disconnect?delegatingUserId={1}";
        final String cohortNameParameter = "cohortName";

        invalidParameterHandler.validateName(cohortName, cohortNameParameter, methodName);

        BooleanResponse restResult;

        try
        {
            restResult = restClient.callGetRESTCall(methodName,
                                                    BooleanResponse.class,
                                                    restURLRoot + rootServiceNameInURL + operationSpecificURL,
                                                    serverName,
                                                    delegatingUserId);
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

        return restResult.isFlag();
    }


    /**
     * Unregister from a specific cohort and disconnect from cohort communications.
     *
     * @param cohortName name of cohort
     * @return boolean flag to indicate success.
     * @throws InvalidParameterException one of the supplied parameters caused a problem
     * @throws RepositoryErrorException a problem communicating with the remote server.
     * @throws UserNotAuthorizedException the user is not authorized to perform the operation requested
     */
    public boolean unregisterFromCohort(String cohortName) throws InvalidParameterException,
                                                                  RepositoryErrorException,
                                                                  UserNotAuthorizedException
    {
        final String methodName = "unregisterFromCohort";
        final String operationSpecificURL = "/metadata-highway/cohorts/"+cohortName+"/unregister?delegatingUserId={1}";
        final String cohortNameParameter = "cohortName";

        invalidParameterHandler.validateName(cohortName, cohortNameParameter, methodName);

        BooleanResponse restResult;

        try
        {
            restResult = restClient.callGetRESTCall(methodName,
                                                    BooleanResponse.class,
                                                    restURLRoot + rootServiceNameInURL + operationSpecificURL,
                                                    serverName,
                                                    delegatingUserId);
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

        return restResult.isFlag();
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
     * @param secretsStoreProvider secrets store connector for bearer token
     * @param secretsStoreLocation secrets store location for bearer token
     * @param secretsStoreCollection secrets store collection for bearer token
     * @param auditLog destination for log messages.
     * @return REST Client connector
     * @throws InvalidParameterException an unexpected exception - internal logic error as the parameters should have
     * all been checked before this call.
     */
    private RESTClientConnector getRESTClientConnector(String   serverName,
                                                       String   serverPlatformURLRoot,
                                                       String   secretsStoreProvider,
                                                       String   secretsStoreLocation,
                                                       String   secretsStoreCollection,
                                                       AuditLog auditLog) throws InvalidParameterException
    {
        final String methodName = "getRESTClientConnector";

        try
        {
            RESTClientFactory clientFactory = new RESTClientFactory(serverName,
                                                                    serverPlatformURLRoot,
                                                                    secretsStoreProvider,
                                                                    secretsStoreLocation,
                                                                    secretsStoreCollection,
                                                                    SecretsStorePurpose.REST_BEARER_TOKEN.getName(),
                                                                    auditLog);

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


    /**
     * Create a REST client to call the remote connector.
     *
     * @param serverName name of the remote server.
     * @param serverPlatformURLRoot name of the URL root for the server.
     * @param secretsStoreConnectorMap connectors to secrets stores
     * @param auditLog destination for log messages.
     * @return REST Client connector
     * @throws InvalidParameterException an unexpected exception - internal logic error as the parameters should have
     * all been checked before this call.
     */
    private RESTClientConnector getRESTClientConnector(String                             serverName,
                                                       String                             serverPlatformURLRoot,
                                                       Map<String, SecretsStoreConnector> secretsStoreConnectorMap,
                                                       AuditLog                           auditLog) throws InvalidParameterException
    {
        final String methodName = "getRESTClientConnector";

        try
        {
            RESTClientFactory clientFactory = new RESTClientFactory(serverName,
                                                                    serverPlatformURLRoot,
                                                                    secretsStoreConnectorMap,
                                                                    auditLog);

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
             * all the other expected exceptions have been processed so default exception to RepositoryErrorException
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