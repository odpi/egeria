/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.service;

import java.time.Clock;
import java.time.Instant;
import java.util.Date;

public class ClockService {

    private Clock clock;

    public ClockService(Clock clock) {
        this.clock = clock;
    }

    public Date getNow() {
        return Date.from(Instant.now(clock));
    }
}
