/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.GovernanceDomainSetElement;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * GovernanceDomainSetsResponse is the response structure used on the OMAS REST API calls that return a
 * list of governance domain definition sets as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceDomainSetsResponse extends FFDCResponseBase
{
    private List<GovernanceDomainSetElement> elements = null;


    /**
     * Default constructor
     */
    public GovernanceDomainSetsResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public GovernanceDomainSetsResponse(GovernanceDomainSetsResponse template)
    {
        super(template);

        if (template != null)
        {
            this.elements = template.getElements();
        }
    }


    /**
     * Return the list of governance domain definition sets.
     *
     * @return list of objects or null
     */
    public List<GovernanceDomainSetElement> getElements()
    {
        return elements;
    }


    /**
     * Set up the list of governance domain definition sets.
     *
     * @param elements - list of objects or null
     */
    public void setElements(List<GovernanceDomainSetElement> elements)
    {
        this.elements = elements;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "GovernanceDomainSetsResponse{" +
                "elements=" + elements +
                "} " + super.toString();
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (!(objectToCompare instanceof GovernanceDomainSetsResponse that))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(this.getElements(), that.getElements());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(elements, super.hashCode());
    }
}
