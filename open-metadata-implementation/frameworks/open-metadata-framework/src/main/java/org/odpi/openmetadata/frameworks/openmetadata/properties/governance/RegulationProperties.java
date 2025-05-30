/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SecurityGroupProperties defines a security group technical control.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RegulationProperties extends GovernanceDefinitionProperties
{
    private  String jurisdiction = null;


    /**
     * Default Constructor
     */
    public RegulationProperties()
    {
    }


    /**
     * Copy/Clone Constructor
     *
     * @param template object to copy
     */
    public RegulationProperties(RegulationProperties template)
    {
        super(template);

        if (template != null)
        {
            this.jurisdiction = template.getJurisdiction();
        }
    }


    /**
     * Return the specific distinguishedName of the license.
     *
     * @return string description
     */
    public String getJurisdiction()
    {
        return jurisdiction;
    }


    /**
     * Set up the specific distinguishedName of the license.
     *
     * @param jurisdiction string description
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
        return "SecurityGroupProperties{" +
                "distinguishedName='" + jurisdiction + '\'' +
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
        RegulationProperties that = (RegulationProperties) objectToCompare;
        return Objects.equals(jurisdiction, that.jurisdiction);
    }



    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), jurisdiction);
    }
}
