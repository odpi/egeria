/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client;

import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.ResolvableType;

/**
 * @param <T> The type of the parameterized class whose objects will be returned in responses.
 * The purpose of this interface is to enable capturing and passing a generic type. */
public interface Parametrization<T> {
    /**
     * @return class type for T
     * */
    Class<T> type();

    /**
     * Default method for return {@link ParameterizedTypeReference}
     * @return {@link ParameterizedTypeReference SubjectAreaOMASAPIResponse}
     * with the necessary universal type to use universal queries in RestTemplate used in SpringRESTClientConnector.
     * */
    default ParameterizedTypeReference<SubjectAreaOMASAPIResponse<T>> getParametrizedType() {
        ResolvableType resolvableType = ResolvableType.forClassWithGenerics(SubjectAreaOMASAPIResponse.class, type());
        return ParameterizedTypeReference.forType(resolvableType.getType());
    }
}