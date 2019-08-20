/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.glossaryview.converters;

import org.odpi.openmetadata.accessservices.glossaryview.rest.GlossaryViewEntityDetail;

import java.util.function.Function;

/**
 * Narrowed the generic type return of a {@code Function} to within the borders of {@code GlossaryViewEntityDetail}
 */
@FunctionalInterface
public interface ToSubClass<R extends GlossaryViewEntityDetail> extends Function<GlossaryViewEntityDetail, R> {

    /**
     * Used to create objects for subclasses of {@code GlossaryViewEntityDetail}
     *
     * @param glossaryViewEntityDetail
     *
     * @return subclass
     */
    R apply(GlossaryViewEntityDetail glossaryViewEntityDetail);

}
