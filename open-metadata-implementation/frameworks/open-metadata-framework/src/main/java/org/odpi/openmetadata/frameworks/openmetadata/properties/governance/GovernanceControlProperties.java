/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityAccessControlProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.security.SecurityGroupProperties;
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
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = TechnicalControlProperties.class, name = "TechnicalControlProperties"),
                @JsonSubTypes.Type(value = OrganizationalControlProperties.class, name = "OrganizationalControlProperties"),
        })
public class GovernanceControlProperties extends GovernanceDefinitionProperties
{
    private  String implementationDescription = null;


    /**
     * Default Constructor
     */
    public GovernanceControlProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.GOVERNANCE_CONTROL.typeName);
    }


    /**
     * Copy/Clone Constructor
     *
     * @param template object to copy
     */
    public GovernanceControlProperties(GovernanceControlProperties template)
    {
        super(template);

        if (template != null)
        {
            this.implementationDescription = template.getImplementationDescription();
        }
    }


    /**
     * Return the specific distinguishedName of the security group.
     *
     * @return string description
     */
    public String getImplementationDescription()
    {
        return implementationDescription;
    }


    /**
     * Set up the specific distinguishedName of the security group.
     *
     * @param implementationDescription string description
     */
    public void setImplementationDescription(String implementationDescription)
    {
        this.implementationDescription = implementationDescription;
    }


    /**
     * JSON-style toString
     *
     * @return string containing the properties and their values
     */
    @Override
    public String toString()
    {
        return "GovernanceControlProperties{" +
                "implementationDescription='" + implementationDescription + '\'' +
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
        GovernanceControlProperties that = (GovernanceControlProperties) objectToCompare;
        return Objects.equals(implementationDescription, that.implementationDescription);
    }



    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), implementationDescription);
    }
}
