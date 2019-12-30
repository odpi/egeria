/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.util;

import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class MockedExceptionUtil {
    public static <T> T mockException(Class<T> exceptionClass, String methodName) throws NoSuchMethodException,
                                                                                         IllegalAccessException,
                                                                                         InvocationTargetException,
                                                                                         InstantiationException {
        DataEngineErrorCode errorCode = DataEngineErrorCode.OMRS_NOT_INITIALIZED;
        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

        Constructor<T> constructor = exceptionClass.getConstructor(Integer.TYPE, String.class,
                String.class, String.class, String.class, String.class, String.class);

        return constructor.newInstance(errorCode.getHttpErrorCode(), MockedExceptionUtil.class.getName(), methodName,
                errorMessage, errorCode.getSystemAction(), errorCode.getUserAction(), "");
    }
}
