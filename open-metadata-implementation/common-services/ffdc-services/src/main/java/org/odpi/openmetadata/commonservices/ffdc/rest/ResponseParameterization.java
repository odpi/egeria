/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import org.springframework.core.GenericTypeResolver;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;

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
     *
     * @return class type for Result
     * @throws IllegalArgumentException if actualTypeArguments length is not equal one.
     */
    @SuppressWarnings("unchecked")
    default Class<Result> resultType() {
        return (Class<Result>) GenericTypeResolver.resolveTypeArgument(getClass(), ResponseParameterization.class);
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