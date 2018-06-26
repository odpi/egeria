/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.validators;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.*;
import org.odpi.openmetadata.accessservices.subjectarea.server.properties.*;

/**
 *
 */
public class DetectAndThrowValidator {
    /**
     * Throw an InvalidParameterException if it is encoded in the REST response.
     *
     * @param methodName - name of the method called
     * @param restResult - response from the rest call.  This generated in the remote org.odpi.openmetadata.accessservices.subjectarea.server.
     * @throws InvalidParameterException - encoded exception from the org.odpi.openmetadata.accessservices.subjectarea.server
     */
    private void detectAndThrowInvalidParameterException(String                       methodName,
                                                         SubjectAreaOMASAPIResponse restResult) throws InvalidParameterException
    {
        final String   exceptionClassName = InvalidParameterException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new InvalidParameterException(restResult.getRelatedHTTPCode(),
                    this.getClass().getName(),
                    methodName,
                    restResult.getExceptionErrorMessage(),
                    restResult.getExceptionSystemAction(),
                    restResult.getExceptionUserAction());
        }
    }


    /**
     * Throw an PropertyServerException if it is encoded in the REST response.
     *
     * @param methodName - name of the method called
     * @param restResult - response from the rest call.  This generated in the remote org.odpi.openmetadata.accessservices.subjectarea.server.
     * @throws PropertyServerException - encoded exception from the org.odpi.openmetadata.accessservices.subjectarea.server
     */
    private void detectAndThrowPropertyServerException(String                       methodName,
                                                       SubjectAreaOMASAPIResponse restResult) throws PropertyServerException
    {
        final String   exceptionClassName = PropertyServerException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new PropertyServerException(restResult.getRelatedHTTPCode(),
                    this.getClass().getName(),
                    methodName,
                    restResult.getExceptionErrorMessage(),
                    restResult.getExceptionSystemAction(),
                    restResult.getExceptionUserAction());
        }
    }


    /**
     * Throw an UnrecognizedGUIDException if it is encoded in the REST response.
     *
     * @param methodName - name of the method called
     * @param restResult - response from the rest call.  This generated in the remote org.odpi.openmetadata.accessservices.subjectarea.server.
     * @throws UnrecognizedGUIDException - encoded exception from the org.odpi.openmetadata.accessservices.subjectarea.server
     */
    private void detectAndThrowUnrecognizedConnectionGUIDException(String                       methodName,
                                                                   SubjectAreaOMASAPIResponse restResult) throws UnrecognizedGUIDException
    {
        final String   exceptionClassName = UnrecognizedGUIDException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {

            throw new UnrecognizedGUIDException(restResult.getRelatedHTTPCode(),
                    this.getClass().getName(),
                    methodName,
                    restResult.getExceptionErrorMessage(),
                    restResult.getExceptionSystemAction(),
                    restResult.getExceptionUserAction(),
                    //TODO sort this out
                    null);
        }
    }


    /**
     * Throw an UnrecognizednNameException if it is encoded in the REST response.
     *
     * @param methodName - name of the method called
     * @param restResult - response from the rest call.  This generated in the remote org.odpi.openmetadata.accessservices.subjectarea.server.
     * @throws UnrecognizednNameException - encoded exception from the org.odpi.openmetadata.accessservices.subjectarea.server
     */
    private void detectAndThrowUnrecognizedConnectionNameException(String                       methodName,
                                                                   SubjectAreaOMASAPIResponse restResult) throws UnrecognizednNameException
    {
        final String   exceptionClassName = UnrecognizednNameException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new UnrecognizednNameException(restResult.getRelatedHTTPCode(),
                    this.getClass().getName(),
                    methodName,
                    restResult.getExceptionErrorMessage(),
                    restResult.getExceptionSystemAction(),
                    restResult.getExceptionUserAction());
        }
    }


    /**
     * Throw an UserNotAuthorizedException if it is encoded in the REST response.
     *
     * @param methodName - name of the method called
     * @param restResult - response from UserNotAuthorizedException - encoded exception from the org.odpi.openmetadata.accessservices.subjectarea.server
     */
    private void detectAndThrowUserNotAuthorizedException(String                       methodName,
                                                          SubjectAreaOMASAPIResponse restResult) throws UserNotAuthorizedException
    {
        final String   exceptionClassName = UserNotAuthorizedException.class.getName();

        if ((restResult != null) && (exceptionClassName.equals(restResult.getExceptionClassName())))
        {
            throw new UserNotAuthorizedException(restResult.getRelatedHTTPCode(),
                    this.getClass().getName(),
                    methodName,
                    restResult.getExceptionErrorMessage(),
                    restResult.getExceptionSystemAction(),
                    restResult.getExceptionUserAction());
        }
    }
}
