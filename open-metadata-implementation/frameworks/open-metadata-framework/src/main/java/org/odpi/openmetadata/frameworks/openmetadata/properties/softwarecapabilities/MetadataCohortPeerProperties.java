/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.RelationshipBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * MetadataCohortPeerProperties describes a server's registration into a cohort.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MetadataCohortPeerProperties extends RelationshipBeanProperties
{
    private Date registrationDate = null;


    /**
     * Default constructor
     */
    public MetadataCohortPeerProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.METADATA_COHORT_PEER_RELATIONSHIP.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template to copy.
     */
    public MetadataCohortPeerProperties(MetadataCohortPeerProperties template)
    {
        super(template);

        if (template != null)
        {
            registrationDate = template.getRegistrationDate();
        }
    }


    /**
     * Return the date that the server joined the cohort.
     *
     * @return date
     */
    public Date getRegistrationDate() { return registrationDate; }


    /**
     * Set up the date that the server joined the cohort.
     *
     * @param registrationDate date
     */
    public void setRegistrationDate(Date registrationDate)
    {
        this.registrationDate = registrationDate;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "MetadataCohortPeerProperties{" +
                "registrationDate=" + registrationDate +
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
        MetadataCohortPeerProperties that = (MetadataCohortPeerProperties) objectToCompare;
        return Objects.equals(registrationDate, that.registrationDate);
    }

    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), registrationDate);
    }
}