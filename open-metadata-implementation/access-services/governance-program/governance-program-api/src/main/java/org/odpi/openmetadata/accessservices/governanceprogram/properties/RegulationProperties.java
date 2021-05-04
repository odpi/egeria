/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Regulation defines a legal set of obligations that the organization must satisfy.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Regulation extends GovernanceDriverProperties
{
    private static final long    serialVersionUID = 1L;

    private String                   jurisdiction = null;

    /**
     * Default constructor
     */
    public Regulation()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public Regulation(Regulation template)
    {
        super(template);

        if (template != null)
        {
            this.jurisdiction = template.getJurisdiction();
        }
    }


    /**
     * Return the jurisdiction that this regulation applies to.
     *
     * @return name of jurisdiction
     */
    public String getJurisdiction()
    {
        return jurisdiction;
    }


    /**
     * Set up the jurisdiction that this regulation applies to.
     *
     * @param jurisdiction name of jurisdiction
     */
    public void setJurisdiction(String jurisdiction)
    {
        this.jurisdiction = jurisdiction;
    }


    /**
     * JSON-style toString
     *
     * @return string containing the properties and their values
     */
    @Override
    public String toString()
    {
        return "Regulation{" +
                       "jurisdiction='" + jurisdiction + '\'' +
                       ", typeName='" + getTypeName() + '\'' +
                       ", documentIdentifier='" + getDocumentIdentifier() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", title='" + getTitle() + '\'' +
                       ", summary='" + getSummary() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", scope='" + getScope() + '\'' +
                       ", domainIdentifier=" + getDomainIdentifier() +
                       ", status=" + getStatus() +
                       ", priority='" + getPriority() + '\'' +
                       ", implications=" + getImplications() +
                       ", outcomes=" + getOutcomes() +
                       ", results=" + getResults() +
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
        Regulation that = (Regulation) objectToCompare;
        return Objects.equals(jurisdiction, that.jurisdiction);
    }


    /**
     * Return has code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), jurisdiction);
    }
}
