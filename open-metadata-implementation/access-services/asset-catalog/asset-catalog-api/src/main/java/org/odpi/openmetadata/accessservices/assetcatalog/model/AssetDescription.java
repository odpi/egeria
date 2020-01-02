/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.assetcatalog.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AssetDescription object holds properties that are used for displaying details of an asset, plus the properties
 * and classifications ans relationships.
 * Also the connection to asset is available in this object.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AssetDescription extends Element implements Serializable {

    private static final long serialVersionUID = 1L;

    private List<Relationship> relationships;

    /**
     * Returns the list of available relationships
     *
     * @return the list of relationships
     */
    public List<Relationship> getRelationships() {
        return relationships;
    }

    /**
     * Set up the list of relationships
     *
     * @param relationships of the element
     */
    public void setRelationships(List<Relationship> relationships) {
        this.relationships = relationships;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        AssetDescription that = (AssetDescription) o;
        return Objects.equals(relationships, that.relationships);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), relationships);
    }
}
