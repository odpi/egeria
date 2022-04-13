/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.openconnectors.governancedaemonconnectors.openlineageconnectors.janusconnector.utils;

import java.util.function.Function;

@FunctionalInterface
public interface QuadFunction<T, U, V, W, R> {

    R apply(T t, U u, V v, W w);

    default <S> QuadFunction<T, U, V, W, S> andThen(Function<? super R, S> after) {
        return (t, u, v, w) -> after.apply(apply(t, u, v, w));
    }

}
