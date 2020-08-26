/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;
import org.springframework.util.Assert;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * @param <Result> The type of the parameterized class whose objects will be returned in responses.
 *                 The purpose of this interface is to enable capturing and passing a generic result. */
public interface ResponseParameterization<Result> {
    /**
     * @return the response class type inherited from {@link GenericResponse}
     */
    @SuppressWarnings("rawtypes")
    Class<? extends GenericResponse> responseType();

    /**
     * Default method for return class type for Result
     *
     * This only works for classes where the first generic parameter matches the return type Result.
     * If your class has different logic, you can override the method and writing your own logic detecting the class type
     * or explicitly specify the returned class.
     * @return class type for Result
     * @throws IllegalArgumentException if the generic type is not an instance of {@link ParameterizedType}
     * @throws IllegalArgumentException if actualTypeArguments length is zero.
     */
    default Class<Result> resultType() {
        Class<Result> thisClass;
        Type type = getClass().getGenericSuperclass();
        Assert.isInstanceOf(ParameterizedType.class, type, "Type must be a parameterized type");
        ParameterizedType parameterizedType = (ParameterizedType) type;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        Assert.isTrue(actualTypeArguments.length > 0, "Number of type arguments must be > 0");
        thisClass = (Class<Result>) actualTypeArguments[0];
        return thisClass;
    }

    /**
     * Default method for return {@link ParameterizedTypeReference}
     *
     * @return {@link ParameterizedTypeReference} {@link GenericResponse}
     * with the necessary universal type to use universal queries in RestTemplate used in SpringRESTClientConnector.
     */
    default ParameterizedTypeReference<GenericResponse<Result>> getParameterizedType() {
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(responseType(), resultType());
        return ParameterizedTypeReference.forType(resolvableType.getType());
    }
}