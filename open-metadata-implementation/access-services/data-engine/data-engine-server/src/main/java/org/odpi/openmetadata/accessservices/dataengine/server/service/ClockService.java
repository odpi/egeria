/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.server.service;

import java.time.Clock;
import java.time.Instant;
import java.util.Date;

/**
 * ClockService manages a Clock instance and provides an utility method for getting the current dte and time
 */
public class ClockService {

    private final Clock clock;

    public ClockService(Clock clock) {
        this.clock = clock;
    }

    /**
     * Retrieves the current date
     *
     * @return current date and time
     */
    public Date getNow() {
        return Date.from(Instant.now(clock));
    }
}
