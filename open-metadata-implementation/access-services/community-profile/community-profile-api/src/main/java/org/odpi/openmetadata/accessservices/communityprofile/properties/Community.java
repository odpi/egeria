/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.communityprofile.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Community describes the core properties of a community.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Community extends ReferenceableHeader
{
    private static final long    serialVersionUID = 1L;

    private String              mission              = null;
    private Map<String, Object> extendedProperties   = null;
    private Map<String, String> additionalProperties = null;


    /**
     * Default constructor
     */
    public Community()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public Community(Community template)
    {
        super(template);

        if (template != null)
        {
            mission = template.getMission();
            extendedProperties = template.getExtendedProperties();
            additionalProperties = template.getAdditionalProperties();
        }
    }


    /**
     * Return the mission of the community.
     *
     * @return text
     */
    public String getMission()
    {
        return mission;
    }


    /**
     * Set up the mission of the community.
     *
     * @param mission text
     */
    public void setMission(String mission)
    {
        this.mission = mission;
    }


    /**
     * Return any properties associated with the subclass of this element.
     *
     * @return map of property names to property values
     */
    public Map<String, Object> getExtendedProperties()
    {
        if (extendedProperties == null)
        {
            return null;
        }
        else if (extendedProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(extendedProperties);
        }
    }


    /**
     * Set up any additional properties associated with the element.
     *
     * @param additionalProperties map of property names to property values
     */
    public void setExtendedProperties(Map<String, Object> additionalProperties)
    {
        this.extendedProperties = additionalProperties;
    }


    /**
     * Return any additional properties associated with the element.
     *
     * @return map of property names to property values
     */
    public Map<String, String> getAdditionalProperties()
    {
        if (additionalProperties == null)
        {
            return null;
        }
        else if (additionalProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(additionalProperties);
        }
    }


    /**
     * Set up any additional properties associated with the element.
     *
     * @param additionalProperties map of property names to property values
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }



    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "Community{" +
                "mission='" + mission + '\'' +
                ", extendedProperties=" + extendedProperties +
                ", additionalProperties=" + additionalProperties +
                ", qualifiedName='" + getQualifiedName() + '\'' +
                ", name='" + getName() + '\'' +
                ", description='" + getDescription() + '\'' +
                ", classifications=" + getClassifications() +
                ", GUID='" + getGUID() + '\'' +
                ", typeName='" + getTypeName() + '\'' +
                ", typeDescription='" + getTypeDescription() + '\'' +
                '}';
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
        Community community = (Community) objectToCompare;
        return Objects.equals(getMission(), community.getMission()) &&
                Objects.equals(getExtendedProperties(), community.getExtendedProperties()) &&
                Objects.equals(getAdditionalProperties(), community.getAdditionalProperties());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getMission(), getExtendedProperties(), getAdditionalProperties());
    }
}
