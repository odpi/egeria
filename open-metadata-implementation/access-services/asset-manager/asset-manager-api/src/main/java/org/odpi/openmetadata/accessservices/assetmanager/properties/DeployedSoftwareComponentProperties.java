/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetmanager.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DeployedSoftwareComponentProperties defines the properties of a process that is implemented in software.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class DeployedSoftwareComponentProperties extends ProcessProperties
{
    private static final long     serialVersionUID = 1L;

    private String implementationLanguage = null;

    /**
     * Default constructor
     */
    public DeployedSoftwareComponentProperties()
    {
        super();
    }


    /**
     * Copy/clone Constructor
     *
     * @param template template object to copy.
     */
    public DeployedSoftwareComponentProperties(DeployedSoftwareComponentProperties template)
    {
        super(template);

        if (template != null)
        {
            implementationLanguage = template.getImplementationLanguage();
        }
    }


    /**
     * Return the description of the processing performed by this process.
     *
     * @return String description
     */
    public String getImplementationLanguage() { return implementationLanguage; }


    /**
     * Set up the the description of the processing performed by this process.
     *
     * @param implementationLanguage String description
     */
    public void setImplementationLanguage(String implementationLanguage)
    {
        this.implementationLanguage = implementationLanguage;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "DeployedSoftwareComponentProperties{" +
                       "implementationLanguage='" + implementationLanguage + '\'' +
                       ", formula='" + getFormula() + '\'' +
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
        DeployedSoftwareComponentProperties that = (DeployedSoftwareComponentProperties) objectToCompare;
        return Objects.equals(getImplementationLanguage(), that.getImplementationLanguage());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getImplementationLanguage());
    }
}
