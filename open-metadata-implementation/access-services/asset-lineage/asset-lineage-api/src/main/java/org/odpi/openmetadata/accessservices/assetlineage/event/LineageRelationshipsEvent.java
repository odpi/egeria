/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetlineage.event;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetlineage.model.RelationshipsContext;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The lineage event contains new or updated lineage graph context from assets.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class LineageRelationshipsEvent extends AssetLineageEventHeader
{
    private RelationshipsContext relationshipsContext;

    public RelationshipsContext getRelationshipsContext() {
        return relationshipsContext;
    }

    public void setRelationshipsContext(RelationshipsContext relationshipsContext) {
        this.relationshipsContext = relationshipsContext;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LineageRelationshipsEvent that = (LineageRelationshipsEvent) o;
        return Objects.equals(relationshipsContext, that.relationshipsContext);
    }

    @Override
    public int hashCode() {
        return Objects.hash(relationshipsContext);
    }
}

