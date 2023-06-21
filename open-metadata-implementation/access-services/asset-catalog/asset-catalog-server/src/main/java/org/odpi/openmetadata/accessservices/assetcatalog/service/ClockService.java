/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.service;

import java.time.Clock;
import java.time.Instant;
import java.util.Date;


/**
 * A Clock service to get current time as date based on a java.time.Clock object.
 */
public class ClockService {

    private final Clock clock;

    /**
     * Instantiates a new Clock Service.
     *
     * @param clock a java clock providing access to the current instant, date and time using a time-zone
     */
    public ClockService(Clock clock) {
        this.clock = clock;
    }

    /**
     * Gets the current time as Date.
     *
     * @return the current time as Date.
     */
    public Date getNow() {
        return Date.from(Instant.now(clock));
    }
}
