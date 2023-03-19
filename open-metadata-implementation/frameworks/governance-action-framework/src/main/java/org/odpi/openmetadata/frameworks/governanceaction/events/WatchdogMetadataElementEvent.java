/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.events;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataElement;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * WatchdogMetadataElementEvent describes the structure of the events passed to the WatchdogGovernanceActionService
 * that relate to changes to metadata elements.  That is event types:
 *
 * <ul>
 *     <li>NEW_ELEMENT</li>
 *     <li>REFRESHED_ELEMENT</li>
 *     <li>UPDATED_ELEMENT_PROPERTIES</li>
 *     <li>DELETED_ELEMENT</li>
 * </ul>
 *
 * The metadataElement property holds the current value of the metadata element.
 * The previousMetadataElement is optionally set for the UPDATED_ELEMENT_PROPERTIES event.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
              property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = WatchdogClassificationEvent.class, name = "WatchdogClassificationEvent"),
        })
public class WatchdogMetadataElementEvent extends WatchdogGovernanceEvent
{
    private static final long      serialVersionUID = 1L;

    private OpenMetadataElement metadataElement = null;
    private OpenMetadataElement previousMetadataElement = null;


    /**
     * Default constructor
     */
    public WatchdogMetadataElementEvent()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public WatchdogMetadataElementEvent(WatchdogMetadataElementEvent template)
    {
        super(template);

        if (template != null)
        {
            metadataElement = template.getMetadataElement();
            previousMetadataElement = template.getPreviousMetadataElement();
        }
    }


    /**
     * Return the properties for the metadata element that is the subject of this event.
     *
     * @return metadata element properties
     */
    public OpenMetadataElement getMetadataElement()
    {
        return metadataElement;
    }


    /**
     * Set up the properties for the metadata element that is the subject of this event.
     *
     * @param metadataElement metadata element properties
     */
    public void setMetadataElement(OpenMetadataElement metadataElement)
    {
        this.metadataElement = metadataElement;
    }


    /**
     * For UPDATED_ELEMENT_PROPERTIES events, return the previous values for the
     * metadata event if available.  Otherwise this is null.
     *
     * @return metadata element properties
     */
    public OpenMetadataElement getPreviousMetadataElement()
    {
        return previousMetadataElement;
    }


    /**
     * For UPDATED_ELEMENT_PROPERTIES events, set up the previous values for the
     * metadata event if available.  Otherwise this is null.
     *
     * @param previousMetadataElement metadata element properties
     */
    public void setPreviousMetadataElement(OpenMetadataElement previousMetadataElement)
    {
        this.previousMetadataElement = previousMetadataElement;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "WatchdogMetadataElementEvent{" +
                       "metadataElement=" + metadataElement +
                       ", previousMetadataElement=" + previousMetadataElement +
                       ", eventType=" + getEventType() +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        WatchdogMetadataElementEvent that = (WatchdogMetadataElementEvent) objectToCompare;
        return Objects.equals(metadataElement, that.metadataElement) &&
                       Objects.equals(previousMetadataElement, that.previousMetadataElement);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), metadataElement, previousMetadataElement);
    }
}
