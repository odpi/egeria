/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.properties;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * GovernanceRelationship is used to show a relationship between different GovernanceDefinitions.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceRelationship implements Serializable
{
    private static final long    serialVersionUID = 1L;

    private String rationale = null;


    /**
     * Default Constructor
     */
    public GovernanceRelationship()
    {
        super();
    }


    /**
     * Copy/clone Constructor - the resulting object.
     *
     * @param template object being copied
     */
    public GovernanceRelationship(GovernanceRelationship template)
    {
        if (template != null)
        {
            this.rationale = template.getRationale();
        }
    }


    /**
     * Return the description of why these two governance definitions are linked.
     *
     * @return string description
     */
    public String getRationale()
    {
        return rationale;
    }


    /**
     * Set up the description of why these two governance definitions are linked.
     *
     * @param rationale string description
     */
    public void setRationale(String rationale)
    {
        this.rationale = rationale;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "GovernanceRelationship{" +
                "rationale='" + rationale + '\'' +
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
        GovernanceRelationship that = (GovernanceRelationship) objectToCompare;
        return Objects.equals(rationale, that.rationale);
    }


    /**
     * Return has code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(rationale);
    }
}
