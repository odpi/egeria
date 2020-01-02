/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.dataengine.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class PortAlias extends Port {
    private String delegatesTo;

    public String getDelegatesTo() {
        return delegatesTo;
    }

    public void setDelegatesTo(String delegatesTo) {
        this.delegatesTo = delegatesTo;
    }

    @Override
    public String toString() {
        return "PortAlias{" +
                "delegatesTo='" + delegatesTo + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        PortAlias portAlias = (PortAlias) o;
        return Objects.equals(delegatesTo, portAlias.delegatesTo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), delegatesTo);
    }
}
