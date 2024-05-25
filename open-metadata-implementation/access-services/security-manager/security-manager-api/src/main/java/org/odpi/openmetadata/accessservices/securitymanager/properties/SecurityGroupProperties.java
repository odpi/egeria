/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.securitymanager.properties;

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
public class SecurityGroupProperties extends GovernanceDefinitionProperties
{
    private  String distinguishedName = null;


    /**
     * Default Constructor
     */
    public SecurityGroupProperties()
    {
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
            this.distinguishedName = template.getDistinguishedName();
        }
    }


    /**
     * Return the specific distinguishedName of the license.
     *
     * @return string description
     */
    public String getDistinguishedName()
    {
        return distinguishedName;
    }


    /**
     * Set up the specific distinguishedName of the license.
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
