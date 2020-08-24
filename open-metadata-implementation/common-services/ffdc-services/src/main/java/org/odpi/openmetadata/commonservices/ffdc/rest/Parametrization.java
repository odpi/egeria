/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;

/**
 * @param <Result> The type of the parameterized class whose objects will be returned in responses.
 * The purpose of this interface is to enable capturing and passing a generic result. */
public interface Parametrization<Result> {
    /**
     * @return class type for Result
     * */
    Class<Result> resultType();

    /**
     * @return the response class type inherited from {@link GenericResponse}
     * */
    @SuppressWarnings("rawtypes")
    Class<? extends GenericResponse> responseType();

    /**
     * Default method for return {@link ParameterizedTypeReference}
     * @return {@link ParameterizedTypeReference} {@link GenericResponse}
     * with the necessary universal type to use universal queries in RestTemplate used in SpringRESTClientConnector.
     * */
    default ParameterizedTypeReference<GenericResponse<Result>> getParametrizedType() {
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(responseType(), resultType());
        return ParameterizedTypeReference.forType(resolvableType.getType());
    }
}