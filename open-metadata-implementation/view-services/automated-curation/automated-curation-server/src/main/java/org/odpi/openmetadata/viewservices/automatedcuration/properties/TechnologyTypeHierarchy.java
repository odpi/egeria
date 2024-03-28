/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.automatedcuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TechnologyTypeHierarchy extends TechnologyTypeSummary
{
    private List<TechnologyTypeHierarchy> subTypes = null;


    public TechnologyTypeHierarchy()
    {
        super();
    }


    public List<TechnologyTypeHierarchy> getSubTypes()
    {
        return subTypes;
    }

    public void setSubTypes(List<TechnologyTypeHierarchy> subTypes)
    {
        this.subTypes = subTypes;
    }


    /**
     * Generate a string containing the properties.
     *
     * @return string value
     */
    @Override
    public String toString()
    {
        return "TechnologyTypeHierarchy{" +
                "subTypes=" + subTypes +
                "} " + super.toString();
    }
}
