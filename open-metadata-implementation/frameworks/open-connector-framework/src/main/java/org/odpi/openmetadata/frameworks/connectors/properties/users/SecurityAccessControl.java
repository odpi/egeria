/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.connectors.properties.users;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SecurityAccessControl describes the permissions to be granted to the security list.  It is typically associated
 * with a resource that needs access control protection.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SecurityAccessControl
{
    private String                    controlDisplayName     = null;
    private String                    controlTypeName        = null;
    private Map<String, String>       mappingProperties      = null;
    private String                    description            = null;
    private Map<String, List<String>> associatedSecurityList = null;
    private List<String>              securityLabels         = null;
    private Map<String, Object>       securityProperties     = null;
    private Map<String, Object>       otherProperties        = null;


    /**
     * Default constructor
     */
    public SecurityAccessControl()
    {
        super();
    }



    /**
     * Copy constructor
     */
    public SecurityAccessControl(SecurityAccessControl template)
    {
        if (template != null)
        {
            this.controlDisplayName = template.getControlDisplayName();
            this.controlTypeName    = template.getControlTypeName();
            this.mappingProperties      = template.getMappingProperties();
            this.description            = template.getDescription();
            this.associatedSecurityList = template.getAssociatedSecurityList();
            this.securityLabels         = template.getSecurityLabels();
            this.securityProperties     = template.getSecurityProperties();
            this.otherProperties        = template.getOtherProperties();
        }
    }


    /**
     * Return the name of the security access control.
     *
     * @return string name
     */
    public String getControlDisplayName()
    {
        return controlDisplayName;
    }


    /**
     * Set up the name of the security access control.
     *
     * @param controlDisplayName string name
     */
    public void setControlDisplayName(String controlDisplayName)
    {
        this.controlDisplayName = controlDisplayName;
    }


    /**
     * Return the type name of security access control.  By convention, this is the open metadata type name used to
     * represent this control in the open metadata repositories. EG GovernanceZone
     *
     * @return string
     */
    public String getControlTypeName()
    {
        return controlTypeName;
    }


    /**
     * Set up the type name of security access control.  By convention, this is the open metadata type name used to
     * represent this control in the open metadata repositories. EG GovernanceZone
     *
     * @param controlTypeName string
     */
    public void setControlTypeName(String controlTypeName)
    {
        this.controlTypeName = controlTypeName;
    }


    /**
     * Return the properties used to map the resource to the security control.
     *
     * @return property map
     */
    public Map<String, String> getMappingProperties()
    {
        return mappingProperties;
    }


    /**
     * Set up the properties used to map the resource to the security control.
     *
     * @param mappingProperties property map
     */
    public void setMappingProperties(Map<String, String> mappingProperties)
    {
        this.mappingProperties = mappingProperties;
    }


    /**
     * Return the description of the security access control.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description of the security access control.
     *
     * @param description string
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return details of the actions that are permitted on particular Governance Zones.
     *
     * @return map of accesses
     */
    public Map<String, List<String>> getAssociatedSecurityList()
    {
        return associatedSecurityList;
    }


    /**
     * Set up details of the actions that are permitted on particular Governance Zones.
     *
     * @param associatedSecurityList map of accesses
     */
    public void setAssociatedSecurityList(Map<String, List<String>> associatedSecurityList)
    {
        this.associatedSecurityList = associatedSecurityList;
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
     * Return any other properties that should be shared with security providers.
     *
     * @return property map
     */
    public Map<String, Object> getOtherProperties()
    {
        return otherProperties;
    }


    /**
     * Set up any other properties that should be shared with security providers.
     *
     * @param otherProperties property map
     */
    public void setOtherProperties(Map<String, Object> otherProperties)
    {
        this.otherProperties = otherProperties;
    }



    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SecurityAccessControl{" +
                "controlDisplayName='" + controlDisplayName + '\'' +
                ", controlTypeName='" + controlTypeName + '\'' +
                ", mappingProperties=" + mappingProperties +
                ", description='" + description + '\'' +
                ", associatedSecurityList=" + associatedSecurityList +
                ", securityLabels=" + securityLabels +
                ", securityProperties=" + securityProperties +
                ", otherProperties=" + otherProperties +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        SecurityAccessControl that = (SecurityAccessControl) objectToCompare;
        return Objects.equals(controlDisplayName, that.controlDisplayName) &&
                Objects.equals(controlTypeName, that.controlTypeName) &&
                Objects.equals(mappingProperties, that.mappingProperties) &&
                Objects.equals(description, that.description) &&
                Objects.equals(associatedSecurityList, that.associatedSecurityList) &&
                Objects.equals(securityLabels, that.securityLabels) &&
                Objects.equals(securityProperties, that.securityProperties) &&
                Objects.equals(otherProperties, that.otherProperties);
    }

    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(controlDisplayName, controlTypeName, mappingProperties, description, associatedSecurityList, securityLabels, securityProperties, otherProperties);
    }
}
