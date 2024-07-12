/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.governanceaction.properties.OpenMetadataRelationship;

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
    private OpenMetadataRelationship openMetadataRelationship         = null;
    private OpenMetadataRelationship previousOpenMetadataRelationship = null;


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
            openMetadataRelationship         = template.getRelatedMetadataElements();
            previousOpenMetadataRelationship = template.getPreviousRelatedMetadataElements();
        }
    }


    /**
     * Return the relationship details for the subject of this event.
     *
     * @return relationship
     */
    public OpenMetadataRelationship getRelatedMetadataElements()
    {
        return openMetadataRelationship;
    }


    /**
     * Set up the relationship details for the subject of this event.
     *
     * @param openMetadataRelationship relationship
     */
    public void setRelatedMetadataElements(OpenMetadataRelationship openMetadataRelationship)
    {
        this.openMetadataRelationship = openMetadataRelationship;
    }


    /**
     * For UPDATED_RELATIONSHIP_PROPERTIES only, return the value is set to the previous values
     * for the relationship, if available.  For other event types, this is null.
     *
     * @return relationship
     */
    public OpenMetadataRelationship getPreviousRelatedMetadataElements()
    {
        return previousOpenMetadataRelationship;
    }


    /**
     * For UPDATED_RELATIONSHIP_PROPERTIES only, set up the value is set to the previous values
     * for the relationship, if available.  For other event types, this is null.
     *
     * @param previousOpenMetadataRelationship relationship
     */
    public void setPreviousRelatedMetadataElements(OpenMetadataRelationship previousOpenMetadataRelationship)
    {
        this.previousOpenMetadataRelationship = previousOpenMetadataRelationship;
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
                       "openMetadataRelationship=" + openMetadataRelationship +
                       ", previousOpenMetadataRelationship=" + previousOpenMetadataRelationship +
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
        return Objects.equals(openMetadataRelationship, that.openMetadataRelationship) &&
                       Objects.equals(previousOpenMetadataRelationship, that.previousOpenMetadataRelationship);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), openMetadataRelationship, previousOpenMetadataRelationship);
    }
}
