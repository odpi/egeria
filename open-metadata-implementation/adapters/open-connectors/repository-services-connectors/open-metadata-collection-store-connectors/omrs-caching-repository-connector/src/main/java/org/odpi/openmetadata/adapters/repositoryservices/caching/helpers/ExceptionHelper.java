/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.repositoryservices.caching.helpers;

import org.odpi.openmetadata.adapters.repositoryservices.caching.auditlog.CachingOMRSErrorCode;
import org.odpi.openmetadata.frameworks.connectors.ffdc.ConnectorCheckedException;
import org.odpi.openmetadata.repositoryservices.ffdc.exception.RepositoryErrorException;

public class ExceptionHelper {

    /**
     * Throws a ConnectorCheckedException based on the provided parameters.
     *
     * @param className  name of the class where the exception was thrown
     * @param errorCode  the error code for the exception
     * @param methodName the method name throwing the exception
     * @param cause      the underlying cause of the exception (if any, otherwise null)
     * @param params     any additional parameters for formatting the error message
     * @throws ConnectorCheckedException always
     */
    public static void raiseConnectorCheckedException(String className, CachingOMRSErrorCode errorCode, String methodName, Exception cause, String... params) throws ConnectorCheckedException {
        if (cause == null) {
            throw new ConnectorCheckedException(errorCode.getMessageDefinition(params),
                    className,
                    methodName);
        } else {
            throw new ConnectorCheckedException(errorCode.getMessageDefinition(params),
                    className,
                    methodName,
                    cause);
        }
    }

    /**
     * Throws a RepositoryErrorException using the provided parameters.
     *
     * @param className  name of the class where the exception was thrown
     * @param errorCode  the error code for the exception
     * @param methodName the name of the method throwing the exception
     * @param cause      the underlying cause of the exception (or null if none)
     * @param params     any parameters for formatting the error message
     * @throws RepositoryErrorException always
     */
    public static void raiseRepositoryErrorException(String className, CachingOMRSErrorCode errorCode, String methodName, Throwable cause, String... params) throws RepositoryErrorException {
        if (cause == null) {
            throw new RepositoryErrorException(errorCode.getMessageDefinition(params),
                    className,
                    methodName);
        } else {
            throw new RepositoryErrorException(errorCode.getMessageDefinition(params),
                    className,
                    methodName,
                    cause);
        }
    }
}
