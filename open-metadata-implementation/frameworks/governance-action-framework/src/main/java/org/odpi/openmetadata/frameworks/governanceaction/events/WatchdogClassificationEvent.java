/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.events;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.governanceaction.properties.AttachedClassification;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * WatchdogClassificationEvent describes the structure of the events passed to the WatchdogGovernanceActionService
 * that describe changes to classifications attached to metadata elements.
 *
 * This form of the WatchdogGovernanceEvent is used with the following event types:
 *
 * <ul>
 *     <li>NEW_CLASSIFICATION</li>
 *     <li>UPDATED_CLASSIFICATION_PROPERTIES</li>
 *     <li>DELETED_CLASSIFICATION</li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class WatchdogClassificationEvent extends WatchdogMetadataElementEvent
{
    private static final long      serialVersionUID = 1L;

    private AttachedClassification changedClassification  = null;
    private AttachedClassification previousClassification = null;


    /**
     * Default constructor
     */
    public WatchdogClassificationEvent()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public WatchdogClassificationEvent(WatchdogClassificationEvent template)
    {
        super(template);

    }


    /**
     * Return the classification that is the subject of the event.
     *
     * @return classification
     */
    public AttachedClassification getChangedClassification()
    {
        return changedClassification;
    }


    /**
     * Set up the classification that is the subject of the event.
     *
     * @param changedClassification classification
     */
    public void setChangedClassification(AttachedClassification changedClassification)
    {
        this.changedClassification = changedClassification;
    }


    /**
     * For UPDATED_CLASSIFICATION_PROPERTIES, return the previous version of the
     * classification (if available.  For all other event type, this is null.
     *
     * @return classification
     */
    public AttachedClassification getPreviousClassification()
    {
        return previousClassification;
    }


    /**
     * For UPDATED_CLASSIFICATION_PROPERTIES, set up the previous version of the
     * classification (if available.  For all other event type, this is null.
     *
     * @param previousClassification classification
     */
    public void setPreviousClassification(AttachedClassification previousClassification)
    {
        this.previousClassification = previousClassification;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "WatchdogClassificationEvent{" +
                       "changedClassification=" + changedClassification +
                       ", previousClassification=" + previousClassification +
                       ", metadataElement=" + getMetadataElement() +
                       ", previousMetadataElement=" + getPreviousMetadataElement() +
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
        WatchdogClassificationEvent that = (WatchdogClassificationEvent) objectToCompare;
        return Objects.equals(changedClassification, that.changedClassification) &&
                       Objects.equals(previousClassification, that.previousClassification);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), changedClassification, previousClassification);
    }
}
