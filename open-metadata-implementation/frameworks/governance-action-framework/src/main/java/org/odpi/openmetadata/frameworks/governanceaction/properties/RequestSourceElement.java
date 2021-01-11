/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RequestSourceElement describes the element that triggered the request to the governance action service.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RequestSourceElement implements Serializable
{
    private static final long      serialVersionUID = 1L;

    private OpenMetadataElement requestSourceElement    = null;
    private OpenMetadataElement associatedAnchorElement = null;

    /**
     * Typical Constructor
     */
    public RequestSourceElement()
    {
    }


    /**
     * Copy/clone Constructor the resulting object will return true if tested with this.equals(template) as
     * long as the template object is not null;
     *
     * @param template object being copied
     */
    public RequestSourceElement(RequestSourceElement template)
    {
        if (template != null)
        {
            requestSourceElement    = template.getRequestSourceElement();
            associatedAnchorElement = template.getAssociatedAnchorElement();
        }
    }


    /**
     * Return the specific element that caused the governance action service to be started.
     *
     * @return metadata element
     */
    public OpenMetadataElement getRequestSourceElement()
    {
        return requestSourceElement;
    }


    /**
     * Set up the specific element that caused the governance action service to be started.
     *
     * @param requestSourceElement metadata element
     */
    public void setRequestSourceElement(OpenMetadataElement requestSourceElement)
    {
        this.requestSourceElement = requestSourceElement;
    }


    /**
     * Return the associated anchor element (if any).
     *
     * @return metadata element
     */
    public OpenMetadataElement getAssociatedAnchorElement()
    {
        return associatedAnchorElement;
    }


    /**
     * Set up the associated anchor element (if any).
     *
     * @param associatedAnchorElement metadata element
     */
    public void setAssociatedAnchorElement(OpenMetadataElement associatedAnchorElement)
    {
        this.associatedAnchorElement = associatedAnchorElement;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "RequestSourceElement{" +
                       "requestSourceElement=" + requestSourceElement +
                       ", associatedAnchorElement=" + associatedAnchorElement +
                       '}';
    }



    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
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
        RequestSourceElement that = (RequestSourceElement) objectToCompare;
        return Objects.equals(requestSourceElement, that.requestSourceElement) &&
                       Objects.equals(associatedAnchorElement, that.associatedAnchorElement);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(requestSourceElement, associatedAnchorElement);
    }
}
