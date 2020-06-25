/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.client;

import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;
import org.springframework.core.ParameterizedTypeReference;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public interface Parametrization<T> {
    Class<T> type();

    default ParameterizedTypeReference<SubjectAreaOMASAPIResponse<T>> getType() {
        ParameterizedType parameterizedType = new ParameterizedType() {

            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{type()};
            }

            @Override
            public Type getRawType() {
                return SubjectAreaOMASAPIResponse.class;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };

        return new ParameterizedTypeReference<SubjectAreaOMASAPIResponse<T>>() {
            @Override
            public Type getType() {
                return parameterizedType;
            }
        };
    }
}
