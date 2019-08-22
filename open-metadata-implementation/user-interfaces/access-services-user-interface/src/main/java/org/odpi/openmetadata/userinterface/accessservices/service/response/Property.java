/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.userinterface.accessservices.service.response;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Property {

    private Id id;
    private String value;

    public Id getId() {
        return id;
    }

    public void setId(Id id) {
        this.id = id;
    }

    @JsonProperty("value")
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
