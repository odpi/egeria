/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.governanceprogram.metadataelements;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStub;


/**
 * GovernanceDelegation is used to show a relationship between different GovernanceDefinitions.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class GovernanceDelegation extends ElementStub
{
    private String rationale = null;


    /**
     * Default Constructor
     */
    public GovernanceDelegation()
    {
        super();
    }


    /**
     * Copy/clone Constructor - the resulting object.
     *
     * @param template object being copied
     */
    public GovernanceDelegation(GovernanceDelegation template)
    {
        super(template);

        if (template != null)
        {
            this.rationale = template.getRationale();
        }
    }


    /**
     * Copy/clone Constructor - the resulting object.
     *
     * @param template object being copied
     */
    public GovernanceDelegation(ElementStub template)
    {
        super(template);
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
        return "GovernanceDelegation{" +
                       "rationale='" + rationale + '\'' +
                       ", uniqueName='" + getUniqueName() + '\'' +
                       ", type=" + getType() +
                       ", GUID='" + getGUID() + '\'' +
                       ", origin=" + getOrigin() +
                       ", classifications=" + getClassifications() +
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
        GovernanceDelegation that = (GovernanceDelegation) objectToCompare;
        return Objects.equals(rationale, that.rationale);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), rationale);
    }
}
