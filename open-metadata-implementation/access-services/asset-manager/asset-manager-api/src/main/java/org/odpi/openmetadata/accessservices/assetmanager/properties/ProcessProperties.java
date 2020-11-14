/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Process properties defines the properties of a process.  A process is a series of steps and decisions in operation
 * in the organization.  It is typically an automated process but may be performed by a person.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
              include = JsonTypeInfo.As.PROPERTY,
              property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = DeployedSoftwareComponentProperties.class, name = "DeployedSoftwareComponentProperties"),
        })
public class ProcessProperties extends AssetProperties
{
    private static final long     serialVersionUID = 1L;

    private String formula    = null;

    /**
     * Default constructor
     */
    public ProcessProperties()
    {
        super();
    }


    /**
     * Copy/clone Constructor
     *
     * @param template template object to copy.
     */
    public ProcessProperties(ProcessProperties template)
    {
        super(template);

        if (template != null)
        {
            formula = template.getFormula();
        }
    }


    /**
     * Return the description of the processing performed by this process.
     *
     * @return String description
     */
    public String getFormula() { return formula; }


    /**
     * Set up the the description of the processing performed by this process.
     *
     * @param formula String description
     */
    public void setFormula(String formula)
    {
        this.formula = formula;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ProcessProperties{" +
                       "formula='" + formula + '\'' +
                       ", technicalName='" + getTechnicalName() + '\'' +
                       ", technicalDescription='" + getTechnicalDescription() + '\'' +
                       ", owner='" + getOwner() + '\'' +
                       ", ownerCategory=" + getOwnerCategory() +
                       ", zoneMembership=" + getZoneMembership() +
                       ", originOrganizationGUID='" + getOriginOrganizationGUID() + '\'' +
                       ", originBusinessCapabilityGUID='" + getOriginBusinessCapabilityGUID() + '\'' +
                       ", otherOriginValues=" + getOtherOriginValues() +
                       ", isReferenceAsset=" + getIsReferenceAsset() +
                       ", displayName='" + getDisplayName() + '\'' +
                       ", summary='" + getSummary() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", abbreviation='" + getAbbreviation() + '\'' +
                       ", usage='" + getUsage() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", vendorProperties=" + getVendorProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
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
        ProcessProperties that = (ProcessProperties) objectToCompare;
        return Objects.equals(getFormula(), that.getFormula());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getFormula());
    }
}
