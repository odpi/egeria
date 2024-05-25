/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetowner.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SecurityTagsRequestBody provides a structure for passing information about the security tags that should be
 * attached to an asset or one of its fields.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SecurityTagsRequestBody extends AssetOwnerOMASAPIRequestBody
{
    private List<String>              securityLabels     = null;
    private Map<String, Object>       securityProperties = null;
    private Map<String, List<String>> accessGroups = null;


    /**
     * Default constructor
     */
    public SecurityTagsRequestBody()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SecurityTagsRequestBody(SecurityTagsRequestBody template)
    {
        super(template);

        if (template != null)
        {
            this.securityLabels     = template.getSecurityLabels();
            this.securityProperties = template.getSecurityProperties();
            this.accessGroups       = template.getAccessGroups();
        }
    }

    /**
     * Return the list of security labels that should be attached to this element.
     *
     * @return list of strings
     */
    public List<String> getSecurityLabels()
    {
        if (securityLabels == null)
        {
            return null;
        }
        else if (securityLabels.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(securityLabels);
        }
    }


    /**
     * Set up the list of security labels that should be attached to this element.
     *
     * @param securityLabels list of strings
     */
    public void setSecurityLabels(List<String> securityLabels)
    {
        this.securityLabels = securityLabels;
    }


    /**
     * Return the security properties that should be attached to this element.
     *
     * @return map of property values
     */
    public Map<String, Object> getSecurityProperties()
    {
        if (securityProperties == null)
        {
            return null;
        }
        else if (securityProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(securityProperties);
        }
    }


    /**
     * Set up the security properties that should be attached to this element.
     *
     * @param securityProperties map of property values
     */
    public void setSecurityProperties(Map<String, Object> securityProperties)
    {
        this.securityProperties = securityProperties;
    }


    /**
     * Return the map from operation to list of security groups.
     *
     * @return map
     */
    public Map<String, List<String>> getAccessGroups()
    {
        if (accessGroups == null)
        {
            return null;
        }
        if (accessGroups.isEmpty())
        {
            return null;
        }
        return accessGroups;
    }


    /**
     * Setup the map from operation to list of security groups.
     *
     * @param accessGroups map
     */
    public void setAccessGroups(Map<String, List<String>> accessGroups)
    {
        this.accessGroups = accessGroups;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "SecurityTagsRequestBody{" +
                       "securityLabels=" + securityLabels +
                       ", securityProperties=" + securityProperties +
                       ", accessGroups=" + accessGroups +
                       '}';
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof SecurityTagsRequestBody that))
        {
            return false;
        }
        return Objects.equals(securityLabels, that.securityLabels) &&
                       Objects.equals(securityProperties, that.securityProperties) &&
                       Objects.equals(accessGroups, that.accessGroups);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(securityLabels, securityProperties, accessGroups);
    }
}
