/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.client;

import org.odpi.openmetadata.accessservices.governanceprogram.rest.GovernanceOfficerListResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.GovernanceOfficerResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.PersonalProfileListResponse;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.PersonalProfileResponse;
import org.odpi.openmetadata.commonservices.gaf.metadatamanagement.client.GAFRESTClient;
import org.odpi.openmetadata.frameworks.auditlog.AuditLog;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.frameworks.connectors.ffdc.PropertyServerException;

/**
 * RESTClient is responsible for issuing calls to the Governance Program OMAS REST APIs.
 */
class GovernanceProgramRESTClient extends GAFRESTClient
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
    GovernanceProgramRESTClient(String    serverName,
                                String    serverPlatformURLRoot,
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
    GovernanceProgramRESTClient(String serverName,
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
    GovernanceProgramRESTClient(String   serverName,
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
    GovernanceProgramRESTClient(String serverName,
                                String serverPlatformURLRoot,
                                String userId,
                                String password) throws InvalidParameterException
    {
        super(serverName, serverPlatformURLRoot, userId, password);
    }


    /**
     * Issue a GET REST call that returns a PersonalProfileResponse object.
     *
     * @param methodName  name of the method being called.
     * @param urlTemplate template of the URL for the REST API call with place-holders for the parameters.
     * @param params      a list of parameters that are slotted into the url template.
     *
     * @return PersonalProfileResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    PersonalProfileResponse callPersonalProfileGetRESTCall(String    methodName,
                                                           String    urlTemplate,
                                                           Object... params) throws PropertyServerException
    {
        return this.callGetRESTCall(methodName, PersonalProfileResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns a list of PersonalProfile objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return PersonalProfileListResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    PersonalProfileListResponse callPersonalProfileListGetRESTCall(String    methodName,
                                                                   String    urlTemplate,
                                                                   Object... params) throws PropertyServerException
    {
        return this.callGetRESTCall(methodName, PersonalProfileListResponse.class, urlTemplate, params);
    }



    /**
     * Issue a POST REST call that returns a list of Governance Officer objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters
     * @param requestBody request body contains the rest of the parameters packaged as a single object
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceOfficerListResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
     GovernanceOfficerListResponse callGovernanceOfficerListPostRESTCall(String    methodName,
                                                                         String    urlTemplate,
                                                                         Object    requestBody,
                                                                         Object... params) throws PropertyServerException
    {
        return this.callPostRESTCall(methodName,
                                     GovernanceOfficerListResponse.class,
                                     urlTemplate,
                                     requestBody,
                                     params);
    }


    /**
     * Issue a GET REST call that returns a GovernanceOfficer object.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceOfficerResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    GovernanceOfficerResponse callGovernanceOfficerGetRESTCall(String    methodName,
                                                               String    urlTemplate,
                                                               Object... params) throws PropertyServerException
    {
        return this.callGetRESTCall(methodName, GovernanceOfficerResponse.class, urlTemplate, params);
    }


    /**
     * Issue a GET REST call that returns a list GovernanceOfficer objects.
     *
     * @param methodName  name of the method being called
     * @param urlTemplate  template of the URL for the REST API call with place-holders for the parameters
     * @param params  a list of parameters that are slotted into the url template
     * @return GovernanceOfficerListResponse
     * @throws PropertyServerException something went wrong with the REST call stack.
     */
    GovernanceOfficerListResponse callGovernanceOfficerListGetRESTCall(String    methodName,
                                                                       String    urlTemplate,
                                                                       Object... params) throws PropertyServerException
    {
        return this.callGetRESTCall(methodName, GovernanceOfficerListResponse.class, urlTemplate, params);
    }
}
