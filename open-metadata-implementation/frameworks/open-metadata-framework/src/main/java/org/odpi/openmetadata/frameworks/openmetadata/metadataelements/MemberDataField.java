/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.datadictionaries.MemberDataFieldProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * MemberDataField is used to return the data field members of a data structure.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MemberDataField extends DataFieldElement
{
    private MemberDataFieldProperties memberDataFieldProperties = null;

    /**
     * Default constructor
     */
    public MemberDataField()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public MemberDataField(MemberDataField template)
    {
        super(template);

        if (template != null)
        {
            this.memberDataFieldProperties = template.getMemberDataFieldProperties();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public MemberDataField(DataFieldElement template)
    {
        super(template);
    }


    /**
     * Return the properties of the MemberDataField relationship.
     *
     * @return properties
     */
    public MemberDataFieldProperties getMemberDataFieldProperties()
    {
        return memberDataFieldProperties;
    }


    /**
     * Set up the properties of the MemberDataField relationship.
     *
     * @param memberDataFieldProperties properties
     */
    public void setMemberDataFieldProperties(MemberDataFieldProperties memberDataFieldProperties)
    {
        this.memberDataFieldProperties = memberDataFieldProperties;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "MemberDataField{" +
                "memberDataFieldProperties=" + memberDataFieldProperties +
                "} " + super.toString();
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
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
        MemberDataField that = (MemberDataField) objectToCompare;
        return Objects.equals(memberDataFieldProperties, that.memberDataFieldProperties) ;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), memberDataFieldProperties);
    }
}
