/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.ControlImplementationStatus;
import org.odpi.openmetadata.frameworks.openmetadata.properties.LabeledRelationshipProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ControlApplicabilityProperties records, for one control in a standard, whether it applies to the scope of a
 * statement of applicability, why it was included or excluded, and how far it has been implemented.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ControlApplicabilityProperties extends LabeledRelationshipProperties
{
    private Boolean                     isApplicable              = null;
    private String                      justification             = null;
    private List<String>                inclusionReasons          = null;
    private ControlImplementationStatus implementationStatus      = null;
    private String                      implementationDescription = null;
    private Date                        reviewDate                = null;


    /**
     * Default constructor
     */
    public ControlApplicabilityProperties()
    {
        super();
        super.typeName = OpenMetadataType.CONTROL_APPLICABILITY_RELATIONSHIP.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ControlApplicabilityProperties(ControlApplicabilityProperties template)
    {
        super(template);

        if (template != null)
        {
            isApplicable              = template.getIsApplicable();
            justification             = template.getJustification();
            inclusionReasons          = template.getInclusionReasons();
            implementationStatus      = template.getImplementationStatus();
            implementationDescription = template.getImplementationDescription();
            reviewDate                = template.getReviewDate();
        }
    }


    /**
     * Return whether the control applies to the scope of the statement of applicability.
     *
     * @return boolean
     */
    public Boolean getIsApplicable()
    {
        return isApplicable;
    }


    /**
     * Set up whether the control applies to the scope of the statement of applicability.
     *
     * @param isApplicable boolean
     */
    public void setIsApplicable(Boolean isApplicable)
    {
        this.isApplicable = isApplicable;
    }


    /**
     * Return the justification for including or excluding the control.
     *
     * @return string
     */
    public String getJustification()
    {
        return justification;
    }


    /**
     * Set up the justification for including or excluding the control.
     *
     * @param justification string
     */
    public void setJustification(String justification)
    {
        this.justification = justification;
    }


    /**
     * Return the reasons that the control is included - for example, risk treatment, a legal or regulatory
     * requirement, a contractual obligation or a business requirement.
     *
     * @return list of reasons
     */
    public List<String> getInclusionReasons()
    {
        return inclusionReasons;
    }


    /**
     * Set up the reasons that the control is included - for example, risk treatment, a legal or regulatory
     * requirement, a contractual obligation or a business requirement.
     *
     * @param inclusionReasons list of reasons
     */
    public void setInclusionReasons(List<String> inclusionReasons)
    {
        this.inclusionReasons = inclusionReasons;
    }


    /**
     * Return how far the control has been implemented.
     *
     * @return status
     */
    public ControlImplementationStatus getImplementationStatus()
    {
        return implementationStatus;
    }


    /**
     * Set up how far the control has been implemented.
     *
     * @param implementationStatus status
     */
    public void setImplementationStatus(ControlImplementationStatus implementationStatus)
    {
        this.implementationStatus = implementationStatus;
    }


    /**
     * Return the description of how the control is implemented within the scope of the statement.
     *
     * @return string
     */
    public String getImplementationDescription()
    {
        return implementationDescription;
    }


    /**
     * Set up the description of how the control is implemented within the scope of the statement.
     *
     * @param implementationDescription string
     */
    public void setImplementationDescription(String implementationDescription)
    {
        this.implementationDescription = implementationDescription;
    }


    /**
     * Return the date that the applicability of this control was last reviewed.
     *
     * @return date
     */
    public Date getReviewDate()
    {
        return reviewDate;
    }


    /**
     * Set up the date that the applicability of this control was last reviewed.
     *
     * @param reviewDate date
     */
    public void setReviewDate(Date reviewDate)
    {
        this.reviewDate = reviewDate;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "ControlApplicabilityProperties{" +
                "isApplicable=" + isApplicable +
                ", justification='" + justification + '\'' +
                ", inclusionReasons=" + inclusionReasons +
                ", implementationStatus=" + implementationStatus +
                ", implementationDescription='" + implementationDescription + '\'' +
                ", reviewDate=" + reviewDate +
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        ControlApplicabilityProperties that = (ControlApplicabilityProperties) objectToCompare;
        return Objects.equals(isApplicable, that.isApplicable) &&
                Objects.equals(justification, that.justification) &&
                Objects.equals(inclusionReasons, that.inclusionReasons) &&
                implementationStatus == that.implementationStatus &&
                Objects.equals(implementationDescription, that.implementationDescription) &&
                Objects.equals(reviewDate, that.reviewDate);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), isApplicable, justification, inclusionReasons, implementationStatus,
                            implementationDescription, reviewDate);
    }
}
