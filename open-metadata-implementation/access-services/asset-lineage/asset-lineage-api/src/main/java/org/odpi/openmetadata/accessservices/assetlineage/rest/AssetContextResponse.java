/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetlineage.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetlineage.model.RelationshipsContext;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * AssetContextResponse is the response structure used on the OMAS REST API calls that return a
 * map of RelationshipsContext objects as an asset context response. It is used for schema elements.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssetContextResponse extends FFDCResponseBase
{
    private static final long serialVersionUID = 1L;

    private Map<String, RelationshipsContext> contextMap = null;

    /**
     * Set up the name to description map result.
     *
     * @param contextMap map of strings
     */
    public void setContextMap(Map<String, RelationshipsContext> contextMap)
    {
        this.contextMap = contextMap;
    }

    /**
     * Gets context map.
     *
     * @return the context map
     */
    public Map<String, RelationshipsContext> getContextMap() {
        return contextMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        AssetContextResponse that = (AssetContextResponse) o;

        return Objects.equals(contextMap, that.contextMap);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (contextMap != null ? contextMap.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "AssetContextResponse{" +
                "contextMap=" + contextMap +
                '}';
    }
}
