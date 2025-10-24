/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.security;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.GovernanceControlProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SecurityGroupProperties defines a security group technical control.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SecurityGroupProperties extends GovernanceControlProperties
{
    private  String distinguishedName = null;


    /**
     * Default Constructor
     */
    public SecurityGroupProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.SECURITY_GROUP.typeName);
    }


    /**
     * Copy/Clone Constructor
     *
     * @param template object to copy
     */
    public SecurityGroupProperties(SecurityGroupProperties template)
    {
        super(template);

        if (template != null)
        {
            this.distinguishedName = template.getImplementationDescription();
        }
    }


    /**
     * Return the specific distinguishedName of the security group.
     *
     * @return string description
     */
    public String getDistinguishedName()
    {
        return distinguishedName;
    }


    /**
     * Set up the specific distinguishedName of the security group.
     *
     * @param distinguishedName string description
     */
    public void setDistinguishedName(String distinguishedName)
    {
        this.distinguishedName = distinguishedName;
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
                "distinguishedName='" + distinguishedName + '\'' +
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
        SecurityGroupProperties that = (SecurityGroupProperties) objectToCompare;
        return Objects.equals(distinguishedName, that.distinguishedName);
    }



    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), distinguishedName);
    }
}
