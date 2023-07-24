/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.apacheatlas.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasConstraintDef captures details of a constraint.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasConstraintDef
{
    public static final String CONSTRAINT_TYPE_OWNED_REF   = "ownedRef";
    public static final String CONSTRAINT_TYPE_INVERSE_REF = "inverseRef";
    public static final String CONSTRAINT_PARAM_ATTRIBUTE  = "attribute";

    private String              type = null;   // foreignKey/mappedFromRef/valueInRange
    private Map<String, Object> params = null; // onDelete=cascade/refAttribute=attr2/min=0,max=23


    public AtlasConstraintDef()
    {
    }


    public String getType()
    {
        return type;
    }


    public void setType(String type)
    {
        this.type = type;
    }


    public Map<String, Object> getParams()
    {
        return params;
    }


    public void setParams(Map<String, Object> params)
    {
        this.params = params;
    }


    @Override
    public String toString()
    {
        return "AtlasConstraintDef{" +
                       "type='" + type + '\'' +
                       ", params=" + params +
                       '}';
    }
}
