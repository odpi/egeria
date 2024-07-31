/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ReferenceValueAssignmentItemElement;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ReferenceValueAssignmentItemsResponse is a response object for passing back a list of elements.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ReferenceValueAssignmentItemsResponse extends FFDCResponseBase
{
    private List<ReferenceValueAssignmentItemElement> elements = null;


    /**
     * Default constructor
     */
    public ReferenceValueAssignmentItemsResponse()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ReferenceValueAssignmentItemsResponse(ReferenceValueAssignmentItemsResponse template)
    {
        super(template);

        if (template != null)
        {
            elements = template.getElements();
        }
    }


    /**
     * Return the metadata element.
     *
     * @return result object
     */
    public List<ReferenceValueAssignmentItemElement> getElements()
    {
        return elements;
    }


    /**
     * Set up the metadata element to return.
     *
     * @param elements result object
     */
    public void setElements(List<ReferenceValueAssignmentItemElement> elements)
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
        return "ReferenceValueAssignmentItemsResponse{" +
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
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        ReferenceValueAssignmentItemsResponse that = (ReferenceValueAssignmentItemsResponse) objectToCompare;
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
        return Objects.hash(super.hashCode(), elements);
    }
}
