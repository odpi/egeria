/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.client.util;

public enum Queries {

    glossary("glossary"),
    ultimatesource("ultimate-source"),
    ultimatedestination("ultimate-destination");

    private String name;

    Queries(String name) {
        this.name = name;
    }


}

