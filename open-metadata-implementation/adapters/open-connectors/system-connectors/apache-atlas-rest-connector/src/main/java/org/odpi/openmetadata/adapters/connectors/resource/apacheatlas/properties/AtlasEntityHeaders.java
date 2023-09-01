/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.resource.apacheatlas.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasEntityHeaders defines a map of GUID to Atlas entities
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasEntityHeaders
{
    private Map<String, AtlasEntityHeader> guidHeaderMap = null;


    public AtlasEntityHeaders()
    {
    }


    public Map<String, AtlasEntityHeader> getGuidHeaderMap()
    {
        return guidHeaderMap;
    }


    public void setGuidHeaderMap(Map<String, AtlasEntityHeader> guidHeaderMap)
    {
        this.guidHeaderMap = guidHeaderMap;
    }


    @Override
    public String toString()
    {
        return "AtlasEntityHeaders{" +
                       "guidHeaderMap=" + guidHeaderMap +
                       '}';
    }
}
