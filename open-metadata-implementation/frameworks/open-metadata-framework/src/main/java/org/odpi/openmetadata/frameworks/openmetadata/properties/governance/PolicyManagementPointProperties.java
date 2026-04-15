/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.governance;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationBeanProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * PolicyManagementPointProperties defines the common properties for the governance action classifications.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
              include = JsonTypeInfo.As.PROPERTY,
              property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = PolicyAdministrationPointProperties.class, name = "PolicyAdministrationPointProperties"),
                @JsonSubTypes.Type(value = PolicyDecisionPointProperties.class, name = "PolicyDecisionPointProperties"),
                @JsonSubTypes.Type(value = PolicyEnforcementPointProperties.class, name = "PolicyEnforcementPointProperties"),
                @JsonSubTypes.Type(value = PolicyInformationPointProperties.class, name = "PolicyInformationPointProperties"),
                @JsonSubTypes.Type(value = PolicyRetrievalPointProperties.class, name = "PolicyRetrievalPointProperties"),
        })
public class PolicyManagementPointProperties extends ClassificationBeanProperties
{
    private String label       = null;
    private String description = null;
    private String pointType   = null;



    /**
     * Default constructor
     */
    public PolicyManagementPointProperties()
    {
        super();
        super.typeName = OpenMetadataType.POLICY_MANAGEMENT_POINT_CLASSIFICATION.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template element to copy
     */
    public PolicyManagementPointProperties(PolicyManagementPointProperties template)
    {
        super(template);

        if (template != null)
        {
            label       = template.getLabel();
            description = template.getDescription();
            pointType   = template.getPointType();
        }
    }


    /**
     * Return the label describing this policy management component.
     *
     * @return string
     */
    public String getLabel()
    {
        return label;
    }


    /**
     * Set up the label describing this policy management component.
     *
     * @param label string
     */
    public void setLabel(String label)
    {
        this.label = label;
    }


    /**
     * Return the description describing this policy management component.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the description describing this policy management component.
     *
     * @param description string
     */
    public void setDescription(String description)
    {
        this.description = description;
    }


    /**
     * Return the subtype of this policy management component.
     *
     * @return string
     */
    public String getPointType()
    {
        return pointType;
    }


    /**
     * Set up the subtype of this policy management component.
     *
     * @param pointType string
     */
    public void setPointType(String pointType)
    {
        this.pointType = pointType;
    }



    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "PolicyManagementPointProperties{" +
                "label='" + label + '\'' +
                ", description='" + description + '\'' +
                ", pointType='" + pointType + '\'' +
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
        PolicyManagementPointProperties that = (PolicyManagementPointProperties) objectToCompare;
        return Objects.equals(label, that.label) &&
                       Objects.equals(description, that.description) &&
                       Objects.equals(pointType, that.pointType);
    }


    /**
     * Return code value representing the contents of this object.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), label, description, pointType);
    }
}