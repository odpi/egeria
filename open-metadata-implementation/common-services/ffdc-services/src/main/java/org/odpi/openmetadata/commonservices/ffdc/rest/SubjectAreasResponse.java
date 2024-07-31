/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.SubjectAreaElement;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * SubjectAreasResponse is the response structure used on the OMAS REST API calls that return a list of subject area descriptions.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SubjectAreasResponse extends FFDCResponseBase
{
    private List<SubjectAreaElement> elements = null;


    /**
     * Default constructor
     */
    public SubjectAreasResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SubjectAreasResponse(SubjectAreasResponse template)
    {
        super(template);

        if (template != null)
        {
            this.elements = template.getElements();
        }
    }


    /**
     * Return the list of elements.
     *
     * @return list of results
     */
    public List<SubjectAreaElement> getElements()
    {
        return elements;
    }


    /**
     * Set up the list of elements to return.
     *
     * @param elements list of results
     */
    public void setElements(List<SubjectAreaElement> elements)
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
        return "SubjectAreasResponse{" +
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
        if (!(objectToCompare instanceof SubjectAreasResponse that))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(elements, that.elements);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(elements);
    }
}
