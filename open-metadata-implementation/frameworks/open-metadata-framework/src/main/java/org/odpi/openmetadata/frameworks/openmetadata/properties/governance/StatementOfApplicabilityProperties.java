/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.AuthoredReferenceableProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * StatementOfApplicabilityProperties describes an organization's statement of which controls from a standard,
 * such as the Annex A controls of ISO/IEC 27001, apply to it, why, and how far each one has been implemented.
 * The controls are linked to the statement using the ControlApplicability relationship.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class StatementOfApplicabilityProperties extends AuthoredReferenceableProperties
{
    private String scope      = null;
    private Date   reviewDate = null;


    /**
     * Default constructor
     */
    public StatementOfApplicabilityProperties()
    {
        super();
        super.typeName = OpenMetadataType.STATEMENT_OF_APPLICABILITY.typeName;
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public StatementOfApplicabilityProperties(StatementOfApplicabilityProperties template)
    {
        super(template);

        if (template != null)
        {
            scope      = template.getScope();
            reviewDate = template.getReviewDate();
        }
    }


    /**
     * Return the description of the part of the organization that the statement covers - for example, the scope of
     * the information security management system.
     *
     * @return string
     */
    public String getScope()
    {
        return scope;
    }


    /**
     * Set up the description of the part of the organization that the statement covers - for example, the scope of
     * the information security management system.
     *
     * @param scope string
     */
    public void setScope(String scope)
    {
        this.scope = scope;
    }


    /**
     * Return the date that the statement was last reviewed.
     *
     * @return date
     */
    public Date getReviewDate()
    {
        return reviewDate;
    }


    /**
     * Set up the date that the statement was last reviewed.
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
        return "StatementOfApplicabilityProperties{" +
                "scope='" + scope + '\'' +
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
        StatementOfApplicabilityProperties that = (StatementOfApplicabilityProperties) objectToCompare;
        return Objects.equals(scope, that.scope) &&
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
        return Objects.hash(super.hashCode(), scope, reviewDate);
    }
}
