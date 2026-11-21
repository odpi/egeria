/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.decisions;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.LabeledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OutcomeProperties describes the link between a decision and an element that represents an outcome of the decision.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OutcomeProperties extends LabeledRelationshipProperties
{
    private String importance  = null;
    private String disposition = null;


    /**
     * Default constructor
     */
    public OutcomeProperties()
    {
        super();
        super.typeName = OpenMetadataType.OUTCOME_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public OutcomeProperties(OutcomeProperties template)
    {
        super(template);

        if (template != null)
        {
            importance  = template.getImportance();
            disposition = template.getDisposition();
        }
    }


    /**
     * Return the relative importance of this outcome for the organization.
     *
     * @return string
     */
    public String getImportance()
    {
        return importance;
    }


    /**
     * Set up the relative importance of this outcome for the organization.
     *
     * @param importance string
     */
    public void setImportance(String importance)
    {
        this.importance = importance;
    }


    /**
     * Return the status of the outcome.
     *
     * @return string
     */
    public String getDisposition()
    {
        return disposition;
    }


    /**
     * Set up the status of the outcome.
     *
     * @param disposition string
     */
    public void setDisposition(String disposition)
    {
        this.disposition = disposition;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OutcomeProperties{" +
                "importance='" + importance + '\'' +
                ", disposition='" + disposition + '\'' +
                "} " + super.toString();
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        OutcomeProperties that = (OutcomeProperties) objectToCompare;
        return Objects.equals(importance, that.importance) &&
                Objects.equals(disposition, that.disposition);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), importance, disposition);
    }
}
