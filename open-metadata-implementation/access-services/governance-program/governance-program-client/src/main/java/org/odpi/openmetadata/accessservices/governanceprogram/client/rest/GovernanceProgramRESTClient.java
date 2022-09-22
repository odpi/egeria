/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.client.rest;

import org.odpi.openmetadata.accessservices.governanceprogram.rest.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCRESTClient;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.UserNotAuthorizedException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;

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
     * @return CertificationTypeListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public CertificationTypeListResponse callCertificationTypeListGetRESTCall(String    methodName,
                                                                              String    urlTemplate,
                                                                              Object... params) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        CertificationTypeListResponse restResult = this.callGetRESTCall(methodName, CertificationTypeListResponse.class, urlTemplate, params);

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
     * @return CertificationTypeListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public CertificationTypeListResponse callCertificationTypeListPostRESTCall(String    methodName,
                                                                               String    urlTemplate,
                                                                               Object    requestBody,
                                                                               Object... params) throws InvalidParameterException,
                                                                                                        PropertyServerException,
                                                                                                        UserNotAuthorizedException
    {
        CertificationTypeListResponse restResult = this.callPostRESTCall(methodName, CertificationTypeListResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a ElementStubListResponse object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return ElementStubListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public ElementStubListResponse callElementStubListGetRESTCall(String    methodName,
                                                                  String    urlTemplate,
                                                                  Object... params) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        ElementStubListResponse restResult = this.callGetRESTCall(methodName, ElementStubListResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a ExternalReferenceResponse object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return ExternalReferenceResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public ExternalReferenceResponse callExternalReferenceGetRESTCall(String    methodName,
                                                                      String    urlTemplate,
                                                                      Object... params) throws InvalidParameterException,
                                                                                               PropertyServerException,
                                                                                               UserNotAuthorizedException
    {
        ExternalReferenceResponse restResult = this.callGetRESTCall(methodName, ExternalReferenceResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of ExternalReferenceElement objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return ExternalReferenceListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public ExternalReferenceListResponse callExternalReferenceListGetRESTCall(String    methodName,
                                                                              String    urlTemplate,
                                                                              Object... params) throws InvalidParameterException,
                                                                                                       PropertyServerException,
                                                                                                       UserNotAuthorizedException
    {
        ExternalReferenceListResponse restResult = this.callGetRESTCall(methodName, ExternalReferenceListResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a list of ExternalReferenceElement objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param requestBody request body for the REST call - contains most of the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return ExternalReferenceListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public ExternalReferenceListResponse callExternalReferenceListPostRESTCall(String    methodName,
                                                                               String    urlTemplate,
                                                                               Object    requestBody,
                                                                               Object... params) throws InvalidParameterException,
                                                                                                        PropertyServerException,
                                                                                                        UserNotAuthorizedException
    {
        ExternalReferenceListResponse restResult = this.callPostRESTCall(methodName, ExternalReferenceListResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a list of GovernanceDefinitionListResponse objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param requestBody request body for the REST call - contains most of the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceDefinitionListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceDefinitionListResponse callGovernanceDefinitionListPostRESTCall(String    methodName,
                                                                                     String    urlTemplate,
                                                                                     Object    requestBody,
                                                                                     Object... params) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException
    {
        GovernanceDefinitionListResponse restResult = this.callPostRESTCall(methodName, GovernanceDefinitionListResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a GovernanceDefinitionListResponse object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceDefinitionListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceDefinitionListResponse callGovernanceDefinitionListGetRESTCall(String    methodName,
                                                                                    String    urlTemplate,
                                                                                    Object... params) throws InvalidParameterException,
                                                                                                             PropertyServerException,
                                                                                                             UserNotAuthorizedException
    {
        GovernanceDefinitionListResponse restResult = this.callGetRESTCall(methodName, GovernanceDefinitionListResponse.class, urlTemplate, params);

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
     * Issue a GET REST call that returns a GovernanceMetricImplementationListResponse object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceDefinitionGraphResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceMetricImplementationListResponse callGovernanceMetricsImplementationListGetRESTCall(String    methodName,
                                                                                                         String    urlTemplate,
                                                                                                         Object... params) throws InvalidParameterException,
                                                                                                                                  PropertyServerException,
                                                                                                                                  UserNotAuthorizedException
    {
        GovernanceMetricImplementationListResponse restResult = this.callGetRESTCall(methodName, GovernanceMetricImplementationListResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a list of GovernanceDomainSetListResponse objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param requestBody request body for the REST call - contains most of the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceDomainSetListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceDomainSetListResponse callGovernanceDomainSetListPostRESTCall(String    methodName,
                                                                                   String    urlTemplate,
                                                                                   Object    requestBody,
                                                                                   Object... params) throws InvalidParameterException,
                                                                                                            PropertyServerException,
                                                                                                            UserNotAuthorizedException
    {
        GovernanceDomainSetListResponse restResult = this.callPostRESTCall(methodName, GovernanceDomainSetListResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a GovernanceDomainSetListResponse object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceDomainSetResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceDomainSetListResponse callGovernanceDomainSetListGetRESTCall(String    methodName,
                                                                                  String    urlTemplate,
                                                                                  Object... params) throws InvalidParameterException,
                                                                                                           PropertyServerException,
                                                                                                           UserNotAuthorizedException
    {
        GovernanceDomainSetListResponse restResult = this.callGetRESTCall(methodName, GovernanceDomainSetListResponse.class, urlTemplate, params);

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
     * Issue a POST REST call that returns a list of GovernanceDomainListResponse objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param requestBody request body for the REST call - contains most of the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceDomainListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceDomainListResponse callGovernanceDomainListPostRESTCall(String    methodName,
                                                                             String    urlTemplate,
                                                                             Object    requestBody,
                                                                             Object... params) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        GovernanceDomainListResponse restResult = this.callPostRESTCall(methodName, GovernanceDomainListResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of GovernanceDomainListResponse objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceDomainListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceDomainListResponse callGovernanceDomainListGetRESTCall(String    methodName,
                                                                            String    urlTemplate,
                                                                            Object... params) throws InvalidParameterException,
                                                                                                     PropertyServerException,
                                                                                                     UserNotAuthorizedException
    {
        GovernanceDomainListResponse restResult = this.callGetRESTCall(methodName, GovernanceDomainListResponse.class, urlTemplate, params);

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
     * @return GovernanceMetricListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceMetricListResponse callGovernanceMetricListPostRESTCall(String    methodName,
                                                                             String    urlTemplate,
                                                                             Object    requestBody,
                                                                             Object... params) throws InvalidParameterException,
                                                                                                      PropertyServerException,
                                                                                                      UserNotAuthorizedException
    {
        GovernanceMetricListResponse restResult = this.callPostRESTCall(methodName, GovernanceMetricListResponse.class, urlTemplate, requestBody, params);

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
    public LicenseTypeListResponse callLicenseTypeListGetRESTCall(String    methodName,
                                                                  String    urlTemplate,
                                                                  Object... params) throws InvalidParameterException,
                                                                                           PropertyServerException,
                                                                                           UserNotAuthorizedException
    {
        LicenseTypeListResponse restResult = this.callGetRESTCall(methodName, LicenseTypeListResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }



    /**
     * Issue a GET REST call that returns a list of LicenseElement objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return LicenseListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public LicenseListResponse callLicenseListGetRESTCall(String    methodName,
                                                          String    urlTemplate,
                                                          Object... params) throws InvalidParameterException,
                                                                                   PropertyServerException,
                                                                                   UserNotAuthorizedException
    {
        LicenseListResponse restResult = this.callGetRESTCall(methodName, LicenseListResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a POST REST call that returns a list of LicenseTypeElement objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param requestBody request body for the REST call - contains most of the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return LicenseTypeListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public LicenseTypeListResponse callLicenseTypeListPostRESTCall(String    methodName,
                                                                   String    urlTemplate,
                                                                   Object    requestBody,
                                                                   Object... params) throws InvalidParameterException,
                                                                                            PropertyServerException,
                                                                                            UserNotAuthorizedException
    {
        LicenseTypeListResponse restResult = this.callPostRESTCall(methodName, LicenseTypeListResponse.class, urlTemplate, requestBody, params);

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
     * Issue a GET REST call that returns a GovernanceStatusIdentifierSetListResponse object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceStatusIdentifierSetListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceStatusIdentifierSetListResponse callStatusIdentifierSetListGetRESTCall(String    methodName,
                                                                                            String    urlTemplate,
                                                                                            Object... params) throws InvalidParameterException,
                                                                                                                     PropertyServerException,
                                                                                                                     UserNotAuthorizedException
    {
        GovernanceStatusIdentifierSetListResponse restResult = this.callGetRESTCall(methodName, GovernanceStatusIdentifierSetListResponse.class, urlTemplate, params);

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
     * Issue a POST REST call that returns a list of GovernanceLevelIdentifierListResponse objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param requestBody request body for the REST call - contains most of the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceLevelIdentifierListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceLevelIdentifierListResponse callLevelIdentifierListPostRESTCall(String    methodName,
                                                                                     String    urlTemplate,
                                                                                     Object    requestBody,
                                                                                     Object... params) throws InvalidParameterException,
                                                                                                              PropertyServerException,
                                                                                                              UserNotAuthorizedException
    {
        GovernanceLevelIdentifierListResponse restResult = this.callPostRESTCall(methodName, GovernanceLevelIdentifierListResponse.class, urlTemplate, requestBody, params);

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
     * Issue a GET REST call that returns a list of GovernanceLevelIdentifierSetListResponse objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceLevelIdentifierSetListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceLevelIdentifierSetListResponse callLevelIdentifierSetListGetRESTCall(String    methodName,
                                                                                          String    urlTemplate,
                                                                                          Object... params) throws InvalidParameterException,
                                                                                                                   PropertyServerException,
                                                                                                                   UserNotAuthorizedException
    {
        GovernanceLevelIdentifierSetListResponse restResult = this.callGetRESTCall(methodName, GovernanceLevelIdentifierSetListResponse.class, urlTemplate, params);

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
     * @return GovernanceRoleListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceRoleListResponse callGovernanceRoleListGetRESTCall(String    methodName,
                                                                        String    urlTemplate,
                                                                        Object... params) throws InvalidParameterException,
                                                                                                 PropertyServerException,
                                                                                                 UserNotAuthorizedException
    {
        GovernanceRoleListResponse restResult = this.callGetRESTCall(methodName, GovernanceRoleListResponse.class, urlTemplate, params);

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
     * @return GovernanceRoleListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceRoleListResponse callGovernanceRoleListPostRESTCall(String    methodName,
                                                                         String    urlTemplate,
                                                                         Object    requestBody,
                                                                         Object... params) throws InvalidParameterException,
                                                                                                  PropertyServerException,
                                                                                                  UserNotAuthorizedException
    {
        GovernanceRoleListResponse restResult = this.callPostRESTCall(methodName, GovernanceRoleListResponse.class, urlTemplate, requestBody, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a list of GovernanceRoleAppointee objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceRoleAppointeeListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceRoleAppointeeListResponse callGovernanceRoleAppointeeListGetRESTCall(String    methodName,
                                                                                          String    urlTemplate,
                                                                                          Object... params) throws InvalidParameterException,
                                                                                                                   PropertyServerException,
                                                                                                                   UserNotAuthorizedException
    {
        GovernanceRoleAppointeeListResponse restResult = this.callGetRESTCall(methodName, GovernanceRoleAppointeeListResponse.class, urlTemplate, params);

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
     * Issue a GET REST call that returns a GovernanceZoneListResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return GovernanceZoneListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public GovernanceZoneListResponse callGovernanceZoneListGetRESTCall(String    methodName,
                                                                        String    urlTemplate,
                                                                        Object... params) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        GovernanceZoneListResponse restResult = this.callGetRESTCall(methodName, GovernanceZoneListResponse.class, urlTemplate, params);

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
     * Issue a GET REST call that returns a SubjectAreaListResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return SubjectAreaListResponse
     * @throws InvalidParameterException one of the parameters is invalid.
     * @throws UserNotAuthorizedException the user is not authorized to make this request.
     * @throws PropertyServerException the repository is not available or not working properly.
     */
    public SubjectAreaListResponse callSubjectAreaListGetRESTCall(String    methodName,
                                                                  String    urlTemplate,
                                                                  Object... params) throws InvalidParameterException,
                                                                                           UserNotAuthorizedException,
                                                                                           PropertyServerException
    {
        SubjectAreaListResponse restResult = this.callGetRESTCall(methodName, SubjectAreaListResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


    /**
     * Issue a GET REST call that returns a RelatedElementListResponse object.
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
    public RelatedElementListResponse callRelatedElementListGetRESTCall(String    methodName,
                                                                        String    urlTemplate,
                                                                        Object... params) throws InvalidParameterException,
                                                                                                 UserNotAuthorizedException,
                                                                                                 PropertyServerException
    {
        RelatedElementListResponse restResult = this.callGetRESTCall(methodName, RelatedElementListResponse.class, urlTemplate, params);

        exceptionHandler.detectAndThrowStandardExceptions(methodName, restResult);

        return restResult;
    }


}
