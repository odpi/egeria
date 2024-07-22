/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ActorProfileProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.ContributionRecord;
import org.odpi.openmetadata.frameworks.openmetadata.properties.actors.UserIdentityProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.projects.ProjectProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.MetadataSourceProperties;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Many open metadata entities are referenceable.  It means that they have a qualified name and additional
 * properties.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ActorProfileProperties.class, name = "ActorProfileProperties"),
                @JsonSubTypes.Type(value = ContributionRecord.class, name = "ContributionRecord"),
                @JsonSubTypes.Type(value = MetadataSourceProperties.class, name = "MetadataSourceProperties"),
                @JsonSubTypes.Type(value = ProjectProperties.class, name = "ProjectProperties"),
                @JsonSubTypes.Type(value = UserIdentityProperties.class, name = "UserIdentityProperties"),
        })
public class ReferenceableProperties extends OpenMetadataRootProperties
{
    private String               qualifiedName        = null;
    private Map<String, String>  additionalProperties = null;
    private Map<String, String>  vendorProperties     = null;

    /**
     * Default constructor
     */
    public ReferenceableProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.  Retrieves values from the supplied template
     *
     * @param template element to copy
     */
    public ReferenceableProperties(ReferenceableProperties template)
    {
        super(template);

        if (template != null)
        {
            qualifiedName        = template.getQualifiedName();
            additionalProperties = template.getAdditionalProperties();

            vendorProperties     = template.getVendorProperties();
        }
    }


    /**
     * Set up the fully qualified name.
     *
     * @param qualifiedName String name
     */
    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
    }


    /**
     * Returns the stored qualified name property for the metadata entity.
     * If no qualified name is available then the empty string is returned.
     *
     * @return qualifiedName
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Set up additional properties.
     *
     * @param additionalProperties Additional properties object
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Return a copy of the additional properties.  Null means no additional properties are available.
     *
     * @return AdditionalProperties
     */
    public Map<String, String> getAdditionalProperties()
    {
        return additionalProperties;
    }


    /**
     * Return specific properties for the data manager vendor.
     *
     * @return name value pairs
     */
    public Map<String, String> getVendorProperties()
    {
        return vendorProperties;
    }


    /**
     * Set up specific properties for the data manager vendor.
     *
     * @param vendorProperties name value pairs
     */
    public void setVendorProperties(Map<String, String> vendorProperties)
    {
        this.vendorProperties = vendorProperties;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ReferenceableProperties{" +
                "qualifiedName='" + qualifiedName + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", vendorProperties=" + vendorProperties +
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
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        ReferenceableProperties that = (ReferenceableProperties) objectToCompare;
        return Objects.equals(qualifiedName, that.qualifiedName) &&
                Objects.equals(additionalProperties, that.additionalProperties) &&
                Objects.equals(vendorProperties, that.vendorProperties);
    }

    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), qualifiedName, additionalProperties, vendorProperties);
    }
}