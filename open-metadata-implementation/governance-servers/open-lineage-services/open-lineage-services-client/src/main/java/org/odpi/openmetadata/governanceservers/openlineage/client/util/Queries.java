/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.governanceservers.openlineage.client.util;

public enum Queries {

    ultimatesource("ultimate-source"),
    ultimatedestination("ultimate-destination"),
    glossary("glossary");

    private String name;

    Queries(String name) {
        this.name = name;
    }


}

