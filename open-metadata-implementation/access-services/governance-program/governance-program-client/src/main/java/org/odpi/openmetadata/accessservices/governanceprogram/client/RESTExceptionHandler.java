/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.client;


import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.GovernanceProgramErrorCode;
import org.odpi.openmetadata.accessservices.governanceprogram.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.governanceprogram.properties.GovernanceDomain;
import org.odpi.openmetadata.accessservices.governanceprogram.rest.*;
import org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EntityDetail;

import java.util.List;
import java.util.Map;

/**
 * RESTExceptionHandler provides the client-side interface for the Governance Program Open Metadata Access Service (OMAS).
 * This client, manages all of the interaction with an open metadata repository.  It is initialized with the URL
 * of the server that is running the Open Metadata Access Services.  This server is responsible for locating and
 * managing the governance program definitions exchanged with this client.
 */
class RESTExceptionHandler extends org.odpi.openmetadata.commonservices.ffdc.RESTExceptionHandler
{
    /**
     * Create a new RESTExceptionHandler.
     */
    RESTExceptionHandler()
    {
        super();
    }


    /**
     * Throw an exception if the supplied GovernanceDomain enum is null
     *
     * @param governanceDomain domain enum to validate
     * @param nameParameter - name of the parameter that passed the name.
     * @param methodName - name of the method making the call.
     * @throws InvalidParameterException the guid is null
     */
    void validateGovernanceDomain(GovernanceDomain governanceDomain,
                                  String           nameParameter,
                                  String           methodName) throws InvalidParameterException
    {
        if (governanceDomain == null)
        {
            GovernanceProgramErrorCode errorCode    = GovernanceProgramErrorCode.NULL_ENUM;
            String                 errorMessage     = errorCode.getErrorMessageId()
                                                    + errorCode.getFormattedErrorMessage(nameParameter, methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                                                this.getClass().getName(),
                                                methodName,
                                                errorMessage,
                                                errorCode.getSystemAction(),
                                                errorCode.getUserAction(),
                                                nameParameter);
        }
    }


    /**
     * Throw an EmployeeNumberNotUniqueException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called
     * @param restResult  response from the rest call.  This generated in the remote server.
     * @throws EmployeeNumberNotUniqueException encoded exception from the server
     */
    @SuppressWarnings("unchecked")
    void detectAndThrowEmployeeNumberNotUniqueException(String                           methodName,
                                                        GovernanceProgramOMASAPIResponse restResult) throws EmployeeNumberNotUniqueException
    {
        final String   exceptionClassName = EmployeeNumberNotUniqueException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            List<EntityDetail> duplicateProfiles = null;

            Map<String, Object>   exceptionProperties = restResult. getExceptionProperties();

            if (exceptionProperties != null)
            {
                Object  duplicateProfilesObject = exceptionProperties.get("duplicateProfiles");

                if (duplicateProfilesObject != null)
                {
                    duplicateProfiles = (List<EntityDetail>)duplicateProfilesObject;
                }
            }
            throw new EmployeeNumberNotUniqueException(restResult.getRelatedHTTPCode(),
                                                       this.getClass().getName(),
                                                       methodName,
                                                       restResult.getExceptionErrorMessage(),
                                                       restResult.getExceptionSystemAction(),
                                                       restResult.getExceptionUserAction(),
                                                       duplicateProfiles);
        }
    }


    /**
     * Throw an AppointmentIdNotUniqueException if it is encoded in the REST response.
     *
     * @param methodName  name of the method called
     * @param restResult  response from the rest call.  This generated in the remote server.
     * @throws AppointmentIdNotUniqueException encoded exception from the server
     */
    @SuppressWarnings("unchecked")
    void detectAndThrowAppointmentIdNotUniqueException(String                           methodName,
                                                       GovernanceProgramOMASAPIResponse restResult) throws AppointmentIdNotUniqueException
    {
        final String   exceptionClassName = EmployeeNumberNotUniqueException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            List<EntityDetail> duplicatesPosts = null;

            Map<String, Object>   exceptionProperties = restResult. getExceptionProperties();

            if (exceptionProperties != null)
            {
                Object  duplicateProfilesObject = exceptionProperties.get("duplicatePosts");

                if (duplicateProfilesObject != null)
                {
                    duplicatesPosts = (List<EntityDetail>)duplicateProfilesObject;
                }
            }
            throw new AppointmentIdNotUniqueException(restResult.getRelatedHTTPCode(),
                                                      this.getClass().getName(),
                                                      methodName,
                                                      restResult.getExceptionErrorMessage(),
                                                      restResult.getExceptionSystemAction(),
                                                      restResult.getExceptionUserAction(),
                                                      duplicatesPosts);
        }
    }
}
