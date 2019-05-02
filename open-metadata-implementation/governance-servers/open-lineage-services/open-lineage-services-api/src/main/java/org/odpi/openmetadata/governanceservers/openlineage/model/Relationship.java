/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.governanceservers.openlineage.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The Relationship object holds properties that are used for displaying a relationship between two assets
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Relationship extends Asset implements Serializable {

    private static final long serialVersionUID = 1L;

    private Asset fromEntity;
    private Asset toEntity;
    private List<String> linkedEntities;

    public Asset getFromEntity() {
        return fromEntity;
    }

    public void setFromEntity(Asset fromEntity) {
        this.fromEntity = fromEntity;
    }

    public Asset getToEntity() {
        return toEntity;
    }

    public void setToEntity(Asset toEntity) {
        this.toEntity = toEntity;
    }

    public List<String> getLinkedEntities() {
        return linkedEntities;
    }

    public void setLinkedEntities(List<String> linkedEntities) {
        this.linkedEntities = linkedEntities;
    }
}
