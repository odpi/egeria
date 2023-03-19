/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.governanceaction.properties.RelatedMetadataElements;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * WatchdogRelatedElementsEvent describes the structure of the events passed to the WatchdogGovernanceActionService
 * that represent changes to relationships.  It applies to the following event types:
 *
 * <ul>
 *     <li>NEW_RELATIONSHIP</li>
 *     <li>REFRESHED_RELATIONSHIP</li>
 *     <li>UPDATED_RELATIONSHIP_PROPERTIES</li>
 *     <li>DELETED_RELATIONSHIP</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class WatchdogRelatedElementsEvent extends WatchdogGovernanceEvent
{
    private static final long      serialVersionUID = 1L;

    private RelatedMetadataElements relatedMetadataElements = null;
    private RelatedMetadataElements previousRelatedMetadataElements = null;


    /**
     * Default constructor
     */
    public WatchdogRelatedElementsEvent()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public WatchdogRelatedElementsEvent(WatchdogRelatedElementsEvent template)
    {
        super(template);

        if (template != null)
        {
            relatedMetadataElements = template.getRelatedMetadataElements();
            previousRelatedMetadataElements = template.getPreviousRelatedMetadataElements();
        }
    }


    /**
     * Return the relationship details for the subject of this event.
     *
     * @return relationship
     */
    public RelatedMetadataElements getRelatedMetadataElements()
    {
        return relatedMetadataElements;
    }


    /**
     * Set up the relationship details for the subject of this event.
     *
     * @param relatedMetadataElements relationship
     */
    public void setRelatedMetadataElements(RelatedMetadataElements relatedMetadataElements)
    {
        this.relatedMetadataElements = relatedMetadataElements;
    }


    /**
     * For UPDATED_RELATIONSHIP_PROPERTIES only, return the value is set to the previous values
     * for the relationship, if available.  For other event types, this is null.
     *
     * @return relationship
     */
    public RelatedMetadataElements getPreviousRelatedMetadataElements()
    {
        return previousRelatedMetadataElements;
    }


    /**
     * For UPDATED_RELATIONSHIP_PROPERTIES only, set up the value is set to the previous values
     * for the relationship, if available.  For other event types, this is null.
     *
     * @param previousRelatedMetadataElements relationship
     */
    public void setPreviousRelatedMetadataElements(RelatedMetadataElements previousRelatedMetadataElements)
    {
        this.previousRelatedMetadataElements = previousRelatedMetadataElements;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "WatchdogRelatedElementsEvent{" +
                       "relatedMetadataElements=" + relatedMetadataElements +
                       ", previousRelatedMetadataElements=" + previousRelatedMetadataElements +
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
        WatchdogRelatedElementsEvent that = (WatchdogRelatedElementsEvent) objectToCompare;
        return Objects.equals(relatedMetadataElements, that.relatedMetadataElements) &&
                       Objects.equals(previousRelatedMetadataElements, that.previousRelatedMetadataElements);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), relatedMetadataElements, previousRelatedMetadataElements);
    }
}
