/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.metadataelements.ElementClassificationHeader;

import java.util.*;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SecurityTags holds the list of labels and properties used by a security enforcement engine to control access
 * and visibility to the contents of the real-world object described by the Referenceable.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SecurityTags extends ElementClassificationHeader
{
    private List<String>        securityLabels     = null;
    private Map<String, Object> securityProperties = null;


    /**
     * Default constructor
     */
    public SecurityTags()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SecurityTags(SecurityTags template)
    {
        super(template);

        if (template != null)
        {
            this.securityLabels = template.getSecurityLabels();
            this.securityProperties = template.getSecurityProperties();
        }
    }


    /**
     * Return the list of security labels attached to the element.
     *
     * @return list of label strings
     */
    public List<String> getSecurityLabels()
    {
        return securityLabels;
    }


    /**
     * Set up the list of security labels for the element.
     *
     * @param securityLabels list of label strings
     */
    public void setSecurityLabels(List<String> securityLabels)
    {
        this.securityLabels = securityLabels;
    }


    /**
     * Return the security properties associated with the element.  These are name-value pairs.
     *
     * @return map of properties
     */
    public Map<String, Object> getSecurityProperties()
    {
        return securityProperties;
    }


    /**
     * Set up the security properties associated with the element.  These are name-value pairs.
     *
     * @param securityProperties map of properties
     */
    public void setSecurityProperties(Map<String, Object> securityProperties)
    {
        this.securityProperties = securityProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SecurityTags{" +
                       "classificationOrigin=" + getClassificationOrigin() +
                       ", classificationOriginGUID='" + getClassificationOriginGUID() + '\'' +
                       ", status=" + getStatus() +
                       ", type=" + getType() +
                       ", origin=" + getOrigin() +
                       ", versions=" + getVersions() +
                       ", headerVersion=" + getHeaderVersion() +
                       ", securityLabels=" + securityLabels +
                       ", securityProperties=" + securityProperties +
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
        SecurityTags that = (SecurityTags) objectToCompare;
        return Objects.equals(securityLabels, that.securityLabels) &&
                Objects.equals(securityProperties, that.securityProperties);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), securityLabels, securityProperties);
    }
}
