/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.client.rest;

import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCRESTClient;
import org.odpi.openmetadata.commonservices.ffdc.rest.*;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.openmetadata.ffdc.PropertyServerException;

/**
 * RESTClient is responsible for issuing calls to the Governance Program OMAS REST APIs.
 */
public class GovernanceProgramRESTClient extends FFDCRESTClient
{
    /**
     * Constructor for no authentication with audit log.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @param auditLog destination for log messages.
     *
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GovernanceProgramRESTClient(String   serverName,
                                       String   serverPlatformURLRoot,
                                       AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, auditLog);
    }


    /**
     * Constructor for no authentication.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GovernanceProgramRESTClient(String serverName,
                                       String serverPlatformURLRoot) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot);
    }


    /**
     * Constructor for simple userId and password authentication with audit log.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @param userId user id for the HTTP request
     * @param password password for the HTTP request
     * @param auditLog destination for log messages.
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GovernanceProgramRESTClient(String   serverName,
                                       String   serverPlatformURLRoot,
                                       String   userId,
                                       String   password,
                                       AuditLog auditLog) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password, auditLog);
    }


    /**
     * Constructor for simple userId and password authentication.
     *
     * @param serverName name of the OMAG Server to call
     * @param serverPlatformURLRoot URL root of the server platform where the OMAG Server is running.
     * @param userId user id for the HTTP request
     * @param password password for the HTTP request
     * @throws InvalidParameterException there is a problem creating the client-side components to issue any
     * REST API calls.
     */
    public GovernanceProgramRESTClient(String serverName,
                                       String serverPlatformURLRoot,
                                       String userId,
                                       String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }


    /**
     * Issue a GET REST call that returns a CertificationTypeResponse object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return CertificationTypeResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public CertificationTypeResponse callCertificationTypeGetRESTCall(String    methodName,
                                                                      String    urlTemplate,
                                                                      Object... params) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        CertificationTypeResponse restResult = this.callGetRESTCall(methodName, CertificationTypeResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of CertificationTypeElement objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return CertificationTypesResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public CertificationTypesResponse callCertificationTypeListGetRESTCall(String    methodName,
                                                                           String    urlTemplate,
                                                                           Object... params) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        CertificationTypesResponse restResult = this.callGetRESTCall(methodName, CertificationTypesResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a list of CertificationTypeElement objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param requestBody request body for the REST call - contains most of the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return CertificationTypesResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public CertificationTypesResponse callCertificationTypeListPostRESTCall(String    methodName,
                                                                            String    urlTemplate,
                                                                            Object    requestBody,
                                                                            Object... params) throws InvalidParameterException,
                                                                                                        PropertyServerException,
                                                                                                        UserNotAuthorizedException
    {
        CertificationTypesResponse restResult = this.callPostRESTCall(methodName, CertificationTypesResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a list of GovernanceDefinitionsResponse objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param requestBody request body for the REST call - contains most of the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceDefinitionsResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceDefinitionsResponse callGovernanceDefinitionsPostRESTCall(String    methodName,
                                                                               String    urlTemplate,
                                                                               Object    requestBody,
                                                                               Object... params) throws InvalidParameterException,
                                                                                                        PropertyServerException,
                                                                                                        UserNotAuthorizedException
    {
        GovernanceDefinitionsResponse restResult = this.callPostRESTCall(methodName, GovernanceDefinitionsResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a GovernanceDefinitionsResponse object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceDefinitionsResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceDefinitionsResponse callGovernanceDefinitionsGetRESTCall(String    methodName,
                                                                              String    urlTemplate,
                                                                              Object... params) throws InvalidParameterException,
                                                                                                             PropertyServerException,
                                                                                                             UserNotAuthorizedException
    {
        GovernanceDefinitionsResponse restResult = this.callGetRESTCall(methodName, GovernanceDefinitionsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a GovernanceDefinitionResponse object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceDefinitionResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceDefinitionResponse callGovernanceDefinitionGetRESTCall(String    methodName,
                                                                            String    urlTemplate,
                                                                            Object... params) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        GovernanceDefinitionResponse restResult = this.callGetRESTCall(methodName, GovernanceDefinitionResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a GovernanceDefinitionGraphResponse object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceDefinitionGraphResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceDefinitionGraphResponse callGovernanceDefinitionGraphGetRESTCall(String    methodName,
                                                                                      String    urlTemplate,
                                                                                      Object... params) throws InvalidParameterException,
                                                                                                               PropertyServerException,
                                                                                                               UserNotAuthorizedException
    {
        GovernanceDefinitionGraphResponse restResult = this.callGetRESTCall(methodName, GovernanceDefinitionGraphResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a GovernanceMetricImplementationsResponse object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceDefinitionGraphResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceMetricImplementationsResponse callGovernanceMetricsImplementationListGetRESTCall(String    methodName,
                                                                                                      String    urlTemplate,
                                                                                                      Object... params) throws InvalidParameterException,
                                                                                                                                  PropertyServerException,
                                                                                                                                  UserNotAuthorizedException
    {
        GovernanceMetricImplementationsResponse restResult = this.callGetRESTCall(methodName, GovernanceMetricImplementationsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a list of GovernanceDomainSetsResponse objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param requestBody request body for the REST call - contains most of the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceDomainSetsResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceDomainSetsResponse callGovernanceDomainSetListPostRESTCall(String    methodName,
                                                                                String    urlTemplate,
                                                                                Object    requestBody,
                                                                                Object... params) throws InvalidParameterException,
                                                                                                            PropertyServerException,
                                                                                                            UserNotAuthorizedException
    {
        GovernanceDomainSetsResponse restResult = this.callPostRESTCall(methodName, GovernanceDomainSetsResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a GovernanceDomainSetsResponse object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceDomainSetResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceDomainSetsResponse callGovernanceDomainSetListGetRESTCall(String    methodName,
                                                                               String    urlTemplate,
                                                                               Object... params) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        GovernanceDomainSetsResponse restResult = this.callGetRESTCall(methodName, GovernanceDomainSetsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a GET REST call that returns a GovernanceDomainSetResponse object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceDomainSetResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceDomainSetResponse callGovernanceDomainSetGetRESTCall(String    methodName,
                                                                          String    urlTemplate,
                                                                          Object... params) throws InvalidParameterException,
                                                                                                   PropertyServerException,
                                                                                                   UserNotAuthorizedException
    {
        GovernanceDomainSetResponse restResult = this.callGetRESTCall(methodName, GovernanceDomainSetResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a list of GovernanceDomainsResponse objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param requestBody request body for the REST call - contains most of the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceDomainsResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceDomainsResponse callGovernanceDomainListPostRESTCall(String    methodName,
                                                                          String    urlTemplate,
                                                                          Object    requestBody,
                                                                          Object... params) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        GovernanceDomainsResponse restResult = this.callPostRESTCall(methodName, GovernanceDomainsResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of GovernanceDomainsResponse objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceDomainsResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceDomainsResponse callGovernanceDomainListGetRESTCall(String    methodName,
                                                                         String    urlTemplate,
                                                                         Object... params) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        GovernanceDomainsResponse restResult = this.callGetRESTCall(methodName, GovernanceDomainsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a GovernanceDomainResponse object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceDomainResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceDomainResponse callGovernanceDomainGetRESTCall(String    methodName,
                                                                    String    urlTemplate,
                                                                    Object... params) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        GovernanceDomainResponse restResult = this.callGetRESTCall(methodName, GovernanceDomainResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a GovernanceMetricResponse object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceMetricResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceMetricResponse callGovernanceMetricGetRESTCall(String    methodName,
                                                                    String    urlTemplate,
                                                                    Object... params) throws InvalidParameterException,
                                                                                             PropertyServerException,
                                                                                             UserNotAuthorizedException
    {
        GovernanceMetricResponse restResult = this.callGetRESTCall(methodName, GovernanceMetricResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a list ofGovernanceMetricElement objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param requestBody request body for the REST call - contains most of the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceMetricsResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceMetricsResponse callGovernanceMetricListPostRESTCall(String    methodName,
                                                                          String    urlTemplate,
                                                                          Object    requestBody,
                                                                          Object... params) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        GovernanceMetricsResponse restResult = this.callPostRESTCall(methodName, GovernanceMetricsResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a LicenseTypeResponse object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return LicenseTypeResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public LicenseTypeResponse callLicenseTypeGetRESTCall(String    methodName,
                                                          String    urlTemplate,
                                                          Object... params) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        LicenseTypeResponse restResult = this.callGetRESTCall(methodName, LicenseTypeResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of LicenseTypeElement objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return LicenseTypeListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public LicenseTypesResponse callLicenseTypesGetRESTCall(String    methodName,
                                                            String    urlTemplate,
                                                            Object... params) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        LicenseTypesResponse restResult = this.callGetRESTCall(methodName, LicenseTypesResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a GovernanceStatusIdentifierResponse object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceStatusIdentifierResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceStatusIdentifierResponse callStatusIdentifierGetRESTCall(String    methodName,
                                                                              String    urlTemplate,
                                                                              Object... params) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        GovernanceStatusIdentifierResponse restResult = this.callGetRESTCall(methodName, GovernanceStatusIdentifierResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a GET REST call that returns a GovernanceLevelIdentifierResponse object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceLevelIdentifierResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceLevelIdentifierResponse callLevelIdentifierGetRESTCall(String    methodName,
                                                                            String    urlTemplate,
                                                                            Object... params) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        GovernanceLevelIdentifierResponse restResult = this.callGetRESTCall(methodName, GovernanceLevelIdentifierResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a GET REST call that returns a GovernanceStatusIdentifierSetResponse object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceStatusIdentifierSetResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceStatusIdentifierSetResponse callStatusIdentifierSetGetRESTCall(String    methodName,
                                                                                    String    urlTemplate,
                                                                                    Object... params) throws InvalidParameterException,
                                                                                                             PropertyServerException,
                                                                                                             UserNotAuthorizedException
    {
        GovernanceStatusIdentifierSetResponse restResult = this.callGetRESTCall(methodName, GovernanceStatusIdentifierSetResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }




    /**
     * Issue a GET REST call that returns a GovernanceStatusIdentifierSetsResponse object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceStatusIdentifierSetsResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceStatusIdentifierSetsResponse callStatusIdentifierSetListGetRESTCall(String    methodName,
                                                                                         String    urlTemplate,
                                                                                         Object... params) throws InvalidParameterException,
                                                                                                                     PropertyServerException,
                                                                                                                     UserNotAuthorizedException
    {
        GovernanceStatusIdentifierSetsResponse restResult = this.callGetRESTCall(methodName, GovernanceStatusIdentifierSetsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of GovernanceLevelIdentifierResponse objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceLevelIdentifierResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceLevelIdentifierResponse callLevelIdentifierListGetRESTCall(String    methodName,
                                                                                String    urlTemplate,
                                                                                Object... params) throws InvalidParameterException,
                                                                                                         PropertyServerException,
                                                                                                         UserNotAuthorizedException
    {
        GovernanceLevelIdentifierResponse restResult = this.callGetRESTCall(methodName, GovernanceLevelIdentifierResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a list of GovernanceLevelIdentifiersResponse objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param requestBody request body for the REST call - contains most of the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceLevelIdentifiersResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceLevelIdentifiersResponse callLevelIdentifierListPostRESTCall(String    methodName,
                                                                                  String    urlTemplate,
                                                                                  Object    requestBody,
                                                                                  Object... params) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException
    {
        GovernanceLevelIdentifiersResponse restResult = this.callPostRESTCall(methodName, GovernanceLevelIdentifiersResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a GovernanceLevelIdentifierSetResponse object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceLevelIdentifierSetResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceLevelIdentifierSetResponse callLevelIdentifierSetGetRESTCall(String    methodName,
                                                                                  String    urlTemplate,
                                                                                  Object... params) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        GovernanceLevelIdentifierSetResponse restResult = this.callGetRESTCall(methodName, GovernanceLevelIdentifierSetResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of GovernanceLevelIdentifierSetsResponse objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceLevelIdentifierSetsResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceLevelIdentifierSetsResponse callLevelIdentifierSetListGetRESTCall(String    methodName,
                                                                                       String    urlTemplate,
                                                                                       Object... params) throws InvalidParameterException,
                                                                                                                   PropertyServerException,
                                                                                                                   UserNotAuthorizedException
    {
        GovernanceLevelIdentifierSetsResponse restResult = this.callGetRESTCall(methodName, GovernanceLevelIdentifierSetsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a list of GovernanceLevelIdentifierSetResponse objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param requestBody request body for the REST call - contains most of the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceLevelIdentifierSetResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceLevelIdentifierSetResponse callLevelIdentifierSetListPostRESTCall(String    methodName,
                                                                                       String    urlTemplate,
                                                                                       Object    requestBody,
                                                                                       Object... params) throws InvalidParameterException,
                                                                                                                PropertyServerException,
                                                                                                                UserNotAuthorizedException
    {
        GovernanceLevelIdentifierSetResponse restResult = this.callPostRESTCall(methodName, GovernanceLevelIdentifierSetResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a GovernanceRoleResponse object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceRoleResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceRoleResponse callGovernanceRoleGetRESTCall(String    methodName,
                                                                String    urlTemplate,
                                                                Object... params) throws InvalidParameterException,
                                                                                         PropertyServerException,
                                                                                         UserNotAuthorizedException
    {
        GovernanceRoleResponse restResult = this.callGetRESTCall(methodName, GovernanceRoleResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a GovernanceRoleHistoryResponse object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceRoleHistoryResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceRoleHistoryResponse callGovernanceRoleHistoryGetRESTCall(String    methodName,
                                                                              String    urlTemplate,
                                                                              Object... params) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        GovernanceRoleHistoryResponse restResult = this.callGetRESTCall(methodName, GovernanceRoleHistoryResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of GovernanceRoleElement objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceRolesResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceRolesResponse callGovernanceRoleListGetRESTCall(String    methodName,
                                                                     String    urlTemplate,
                                                                     Object... params) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        GovernanceRolesResponse restResult = this.callGetRESTCall(methodName, GovernanceRolesResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a POST REST call that returns a list of GovernanceRoleElement objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param requestBody request body for the REST call - contains most of the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceRolesResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceRolesResponse callGovernanceRoleListPostRESTCall(String    methodName,
                                                                      String    urlTemplate,
                                                                      Object    requestBody,
                                                                      Object... params) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        GovernanceRolesResponse restResult = this.callPostRESTCall(methodName, GovernanceRolesResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of GovernanceRoleAppointee objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceRoleAppointeesResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceRoleAppointeesResponse callGovernanceRoleAppointeeListGetRESTCall(String    methodName,
                                                                                       String    urlTemplate,
                                                                                       Object... params) throws InvalidParameterException,
                                                                                                                   PropertyServerException,
                                                                                                                   UserNotAuthorizedException
    {
        GovernanceRoleAppointeesResponse restResult = this.callGetRESTCall(methodName, GovernanceRoleAppointeesResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a GET REST call that returns a GovernanceZoneResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return GovernanceZoneResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceZoneResponse callGovernanceZoneGetRESTCall(String    methodName,
                                                                String    urlTemplate,
                                                                Object... params) throws InvalidParameterException,
                                                                                         UserNotAuthorizedException,
                                                                                         PropertyServerException
    {
        GovernanceZoneResponse restResult = this.callGetRESTCall(methodName, GovernanceZoneResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a GovernanceZonesResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return GovernanceZonesResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceZonesResponse callGovernanceZoneListGetRESTCall(String    methodName,
                                                                     String    urlTemplate,
                                                                     Object... params) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        GovernanceZonesResponse restResult = this.callGetRESTCall(methodName, GovernanceZonesResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a GovernanceZoneDefinitionResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return GovernanceZoneDefinitionResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceZoneDefinitionResponse callGovernanceZoneDefinitionGetRESTCall(String    methodName,
                                                                                    String    urlTemplate,
                                                                                    Object... params) throws InvalidParameterException,
                                                                                                             UserNotAuthorizedException,
                                                                                                             PropertyServerException
    {
        GovernanceZoneDefinitionResponse restResult = this.callGetRESTCall(methodName, GovernanceZoneDefinitionResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }







    /**
     * Issue a GET REST call that returns a SubjectAreaResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return SubjectAreaResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public SubjectAreaResponse callSubjectAreaGetRESTCall(String    methodName,
                                                          String    urlTemplate,
                                                          Object... params) throws InvalidParameterException,
                                                                                   UserNotAuthorizedException,
                                                                                   PropertyServerException
    {
        SubjectAreaResponse restResult = this.callGetRESTCall(methodName, SubjectAreaResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a SubjectAreaDefinitionResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return SubjectAreaDefinitionResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public SubjectAreaDefinitionResponse callSubjectAreaDefinitionGetRESTCall(String    methodName,
                                                                              String    urlTemplate,
                                                                              Object... params) throws InvalidParameterException,
                                                                                                       UserNotAuthorizedException,
                                                                                                       PropertyServerException
    {
        SubjectAreaDefinitionResponse restResult = this.callGetRESTCall(methodName, SubjectAreaDefinitionResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a GET REST call that returns a SubjectAreasResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return SubjectAreasResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public SubjectAreasResponse callSubjectAreaListGetRESTCall(String    methodName,
                                                               String    urlTemplate,
                                                               Object... params) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        SubjectAreasResponse restResult = this.callGetRESTCall(methodName, SubjectAreasResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a RelatedElementsResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate  REST API call URL template with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return response object
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public RelatedElementsResponse callRelatedElementsGetRESTCall(String    methodName,
                                                                  String    urlTemplate,
                                                                  Object... params) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        RelatedElementsResponse restResult = this.callGetRESTCall(methodName, RelatedElementsResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


}
