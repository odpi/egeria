/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.bitol.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents the high-level description of a Bitol Open Data Contract Standard (ODCS) data contract
 * or Open Data Product Standard (ODPS) data product.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class BitolDescription
{
    private String                             purpose = null;
    private String                             limitations = null;
    private String                             usage = null;
    private List<BitolAuthoritativeDefinition> authoritativeDefinitions = null;
    private List<BitolCustomProperty>          customProperties = null;


    /**
     * Default constructor
     */
    public BitolDescription()
    {
    }


    /**
     * Return the intended purpose of the data.
     *
     * @return string description
     */
    public String getPurpose()
    {
        return purpose;
    }


    /**
     * Set up the intended purpose of the data.
     *
     * @param purpose string description
     */
    public void setPurpose(String purpose)
    {
        this.purpose = purpose;
    }


    /**
     * Return the technical, compliance and legal limitations on the use of the data.
     *
     * @return string description
     */
    public String getLimitations()
    {
        return limitations;
    }


    /**
     * Set up the technical, compliance and legal limitations on the use of the data.
     *
     * @param limitations string description
     */
    public void setLimitations(String limitations)
    {
        this.limitations = limitations;
    }


    /**
     * Return the recommended usage of the data.
     *
     * @return string description
     */
    public String getUsage()
    {
        return usage;
    }


    /**
     * Set up the recommended usage of the data.
     *
     * @param usage string description
     */
    public void setUsage(String usage)
    {
        this.usage = usage;
    }


    /**
     * Return the list of links to sources that provide more details about this element.
     *
     * @return list of authoritative definitions
     */
    public List<BitolAuthoritativeDefinition> getAuthoritativeDefinitions()
    {
        return authoritativeDefinitions;
    }


    /**
     * Set up the list of links to sources that provide more details about this element.
     *
     * @param authoritativeDefinitions list of authoritative definitions
     */
    public void setAuthoritativeDefinitions(List<BitolAuthoritativeDefinition> authoritativeDefinitions)
    {
        this.authoritativeDefinitions = authoritativeDefinitions;
    }


    /**
     * Return the list of custom (key/value) properties attached to this element.
     *
     * @return list of custom properties
     */
    public List<BitolCustomProperty> getCustomProperties()
    {
        return customProperties;
    }


    /**
     * Set up the list of custom (key/value) properties attached to this element.
     *
     * @param customProperties list of custom properties
     */
    public void setCustomProperties(List<BitolCustomProperty> customProperties)
    {
        this.customProperties = customProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "BitolDescription{" +
                       "purpose='" + purpose + '\'' +
                       ", limitations='" + limitations + '\'' +
                       ", usage='" + usage + '\'' +
                       ", authoritativeDefinitions=" + authoritativeDefinitions +
                       ", customProperties=" + customProperties +
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
        BitolDescription that = (BitolDescription) objectToCompare;
        return Objects.equals(purpose, that.purpose) &&
                       Objects.equals(limitations, that.limitations) &&
                       Objects.equals(usage, that.usage) &&
                       Objects.equals(authoritativeDefinitions, that.authoritativeDefinitions) &&
                       Objects.equals(customProperties, that.customProperties);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(purpose, limitations, usage, authoritativeDefinitions, customProperties);
    }
}
