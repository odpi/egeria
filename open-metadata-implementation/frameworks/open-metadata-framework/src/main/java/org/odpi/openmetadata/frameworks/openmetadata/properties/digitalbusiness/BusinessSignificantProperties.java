/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.digitalbusiness;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * StagingCollectionProperties is used to provide the properties for an EditingGlossary classification.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BusinessSignificantProperties extends ClassificationBeanProperties
{
    private String description = null;
    private String scope = null;
    private String businessCapability = null;


    /**
     * Default constructor
     */
    public BusinessSignificantProperties()
    {
        super();
        super.typeName = OpenMetadataType.BUSINESS_SIGNIFICANT_CLASSIFICATION.typeName;
    }


    /**
     * Copy/clone constructor for an editing glossary classification.
     *
     * @param template template object to copy.
     */
    public BusinessSignificantProperties(BusinessSignificantProperties template)
    {
        super(template);

        if (template != null)
        {
            this.description = template.getDescription();
            this.scope = template.getScope();
            this.businessCapability = template.getBusinessCapability();
        }
    }


    /**
     * Return the description (typically and overview of the revision) of the glossary.
     *
     * @return string description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description (typically and overview of the revision) of the glossary.
     *
     * @param description string description
     */
    public void setDescription(String description)
    {
        this.description = description;
    }

    /**
     * Return the context in which the element is significant to the business capability.
     *
     * @return string description
     */
    public String getScope()
    {
        return scope;
    }


    /**
     * Set up the context in which the element is significant to the business capability.
     *
     * @param scope string description
     */
    public void setScope(String scope)
    {
        this.scope = scope;
    }


    /**
     * Return the identifier of the business capability that this element is significant to.
     *
     * @return string
     */
    public String getBusinessCapability()
    {
        return businessCapability;
    }


    /**
     * Set up the identifier of the business capability that this element is significant to.
     *
     * @param businessCapability string
     */
    public void setBusinessCapability(String businessCapability)
    {
        this.businessCapability = businessCapability;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "BusinessSignificantProperties{" +
                "description='" + description + '\'' +
                ", scope='" + scope + '\'' +
                ", businessCapability='" + businessCapability + '\'' +
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
        BusinessSignificantProperties that = (BusinessSignificantProperties) objectToCompare;
        return Objects.equals(description, that.description) &&
                Objects.equals(scope, that.scope) &&
                Objects.equals(businessCapability, that.businessCapability);
    }

    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), description, scope, businessCapability);
    }
}
