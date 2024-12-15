/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.viewservices.automatedcuration.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TechnologyTypeHierarchy allows the return of a technology type hierarchy
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TechnologyTypeHierarchy extends TechnologyTypeSummary
{
    private List<TechnologyTypeHierarchy> subTypes = null;


    /**
     * Default constructor
     */
    public TechnologyTypeHierarchy()
    {
        super();
    }


    /**
     * Copy constructor
     *
     * @param template super type
     */
    public TechnologyTypeHierarchy(TechnologyTypeSummary template)
    {
        super(template);
    }


    /**
     * Return details of the subtypes of this technology type.
     *
     * @return list of subtypes
     */
    public List<TechnologyTypeHierarchy> getSubTypes()
    {
        return subTypes;
    }


    /**
     * Set up the details of the subtypes of this technology type.
     *
     * @param subTypes list of subtypes
     */
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
