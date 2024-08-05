/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.dataprocessing;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceDefinitionProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Privacy regulations, such as GDPR, require permission from individuals to process their data,
 * for each type of processing.  Similarly, licensed data often specifies which type of processing is permitted.
 * Data processing purposes are a way of characterising the types of processing that is being performed in order
 * to request the appropriate permissions, and to also label data in a clean way to help people make good choices
 * of the data they can/should use.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DataProcessingPurposeProperties extends GovernanceDefinitionProperties
{
    private String details = null;


    /**
     * Default Constructor
     */
    public DataProcessingPurposeProperties()
    {
    }


    /**
     * Copy/Clone Constructor
     *
     * @param template object to copy
     */
    public DataProcessingPurposeProperties(DataProcessingPurposeProperties template)
    {
        super(template);

        if (template != null)
        {
            this.details = template.getDetails();
        }
    }


    /**
     * Return the specific details of the certification.
     *
     * @return string description
     */
    public String getDetails()
    {
        return details;
    }


    /**
     * Set up the specific details of the certification.
     *
     * @param details string description
     */
    public void setDetails(String details)
    {
        this.details = details;
    }


    /**
     * JSON-style toString
     *
     * @return string containing the properties and their values
     */
    @Override
    public String toString()
    {
        return "CertificationTypeProperties{" +
                "details='" + details + '\'' +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        DataProcessingPurposeProperties that = (DataProcessingPurposeProperties) objectToCompare;
        return Objects.equals(details, that.details);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), details);
    }
}
