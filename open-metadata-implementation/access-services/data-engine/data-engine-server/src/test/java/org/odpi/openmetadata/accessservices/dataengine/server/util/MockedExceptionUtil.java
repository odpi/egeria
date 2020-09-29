/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.util;

import org.odpi.openmetadata.accessservices.dataengine.ffdc.DataEngineErrorCode;
import org.odpi.openmetadata.frameworks.auditlog.messagesets.ExceptionMessageDefinition;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class MockedExceptionUtil {
    public static <T> T mockException(Class<T> exceptionClass, String methodName) throws NoSuchMethodException,
                                                                                         IllegalAccessException,
                                                                                         InvocationTargetException,
                                                                                         InstantiationException {

        Constructor<T> constructor = exceptionClass.getConstructor(ExceptionMessageDefinition.class, String.class,
                String.class, String.class);

        return constructor.newInstance(DataEngineErrorCode.OMRS_NOT_INITIALIZED.getMessageDefinition(),
                exceptionClass.getName(), methodName, "");
    }
}
