/* SPDX-License-Identifier: Apache-2.0 */

package org.odpi.openmetadata.accessservices.governanceengine.server.handlers;

import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.errorcode.GovernanceEngineErrorCode;
import org.odpi.openmetadata.accessservices.governanceengine.api.ffdc.exceptions.InvalidParameterException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.lang.invoke.MethodHandles;
import java.util.List;

/**
 * ErrorValidator class that provides api validation routines for the other handler classes
 */
public final class GovernanceEngineValidator {

    private static final Logger log = LoggerFactory.getLogger(GovernanceEngineValidator.class);

    private GovernanceEngineValidator() {
    }


    /**
     * Throw an exception if the supplied userId is null
     *
     * @param userId     - user name to validate
     * @param methodName - name of the method making the call.
     * @throws InvalidParameterException - the userId is null
     */
    public static void validateUserId(String userId,
                                      String methodName) throws InvalidParameterException {
        if (StringUtils.isEmpty(userId)) {
            GovernanceEngineErrorCode errorCode = GovernanceEngineErrorCode.EMPTY_USER_ID;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    MethodHandles.lookup().lookupClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }


    /**
     * Throw an exception if the supplied unique identifier is null
     *
     * @param guid          - unique identifier to validate
     * @param parameterName - name of the parameter that passed the guid.
     * @param methodName    - name of the method making the call.
     * @throws InvalidParameterException - the guid is null
     */
    public static void validateGUID(String guid,
                                    String parameterName,
                                    String methodName) throws InvalidParameterException {
        if (StringUtils.isEmpty(guid)) {
            GovernanceEngineErrorCode errorCode = GovernanceEngineErrorCode.NULL_GUID;
            String errorMessage = errorCode.getErrorMessageId()
                    + errorCode.getFormattedErrorMessage(parameterName, methodName);

            throw new InvalidParameterException(errorCode.getHTTPErrorCode(),
                    MethodHandles.lookup().lookupClass().getName(),
                    methodName,
                    errorMessage,
                    errorCode.getSystemAction(),
                    errorCode.getUserAction());
        }
    }


    /**
     * Throw an exception if the supplied unique identifier is null
     *
     * @param rootClassification - unique identifier to validate
     * @param parameterName      - name of the parameter that passed the guid.
     * @param methodName         - name of the method making the call.
     * @throws InvalidParameterException - the guid is null
     */
    public static void validateClassification(List<String> rootClassification,
                                              String parameterName,
                                              String methodName) throws InvalidParameterException {

        return;
    }

    /**
     * Throw an exception if the supplied unique identifier is null
     *
     * @param rootAssetType - unique identifier to validate
     * @param parameterName - name of the parameter that passed the guid.
     * @param methodName    - name of the method making the call.
     * @throws InvalidParameterException - the guid is null
     */
    public static void validateType(List<String> rootAssetType,
                                    String parameterName,
                                    String methodName) throws InvalidParameterException {
        // NULL is valid, so no further checks for now
        return;
    }
}
