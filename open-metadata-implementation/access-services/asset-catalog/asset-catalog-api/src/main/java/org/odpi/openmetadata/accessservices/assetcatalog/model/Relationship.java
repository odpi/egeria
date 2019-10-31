/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The Relationship object holds properties that are used for displaying a relationship between two assets
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Relationship extends Element implements Serializable {

    private static final long serialVersionUID = 1L;

    private Element fromEntity;
    private Element toEntity;

    public Element getFromEntity() {
        return fromEntity;
    }

    public void setFromEntity(Element fromEntity) {
        this.fromEntity = fromEntity;
    }

    public Element getToEntity() {
        return toEntity;
    }

    public void setToEntity(Element toEntity) {
        this.toEntity = toEntity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Relationship that = (Relationship) o;
        return Objects.equals(fromEntity, that.fromEntity) &&
                Objects.equals(toEntity, that.toEntity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), fromEntity, toEntity);
    }
}
