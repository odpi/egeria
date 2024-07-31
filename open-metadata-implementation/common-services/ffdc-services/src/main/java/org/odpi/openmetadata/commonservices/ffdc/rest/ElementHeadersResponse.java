/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementHeader;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ElementHeadersResponse is a response object for passing back a list of element headers
 * or an exception if the request failed.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ElementHeadersResponse extends FFDCResponseBase
{
    private List<ElementHeader> elementHeaders = null;


    /**
     * Default constructor
     */
    public ElementHeadersResponse()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ElementHeadersResponse(ElementHeadersResponse template)
    {
        super(template);

        if (template != null)
        {
            elementHeaders = template.getElementHeaders();
        }
    }


    /**
     * Return the list of metadata elements.
     *
     * @return result object
     */
    public List<ElementHeader> getElementHeaders()
    {
        return elementHeaders;
    }


    /**
     * Set up the metadata element to return.
     *
     * @param elementHeaders result object
     */
    public void setElementHeaders(List<ElementHeader> elementHeaders)
    {
        this.elementHeaders = elementHeaders;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ElementHeadersResponse{" +
                "elementHeaders=" + elementHeaders +
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
        ElementHeadersResponse that = (ElementHeadersResponse) objectToCompare;
        return Objects.equals(elementHeaders, that.elementHeaders);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), elementHeaders);
    }
}
