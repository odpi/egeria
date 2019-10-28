package org.odpi.openmetadata.accessservices.assetcatalog.util;

import org.odpi.openmetadata.accessservices.assetcatalog.exception.AssetCatalogErrorCode;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class MockedExceptionUtil {
    public static <T> T mockException(Class<T> exceptionClass, String methodName)
            throws NoSuchMethodException,
            IllegalAccessException,
            InvocationTargetException,
            InstantiationException {
        AssetCatalogErrorCode errorCode = AssetCatalogErrorCode.SERVICE_NOT_INITIALIZED;
        String errorMessage = errorCode.getErrorMessageId() + errorCode.getFormattedErrorMessage(methodName);

        Constructor<T> constructor = exceptionClass.getConstructor(Integer.TYPE, String.class,
                String.class, String.class, String.class, String.class, String.class);

        return constructor.newInstance(errorCode.getHttpErrorCode(), MockedExceptionUtil.class.getName(), methodName,
                errorMessage, errorCode.getSystemAction(), errorCode.getUserAction(), "");
    }
}
