/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.softwarecapabilities.SoftwareCapabilityProperties;


import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * SoftwareCapabilityRequestBody describes the properties of the software server capability for creation and update.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SoftwareCapabilityRequestBody extends SoftwareCapabilityProperties
{
    private String externalSourceGUID = null;
    private String externalSourceName = null;
    private String classificationName = null;


    /**
     * Default constructor
     */
    public SoftwareCapabilityRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public SoftwareCapabilityRequestBody(SoftwareCapabilityRequestBody template)
    {
        super(template);

        if (template != null)
        {
            externalSourceGUID = template.getExternalSourceGUID();
            externalSourceName = template.getExternalSourceName();
            classificationName = template.getClassificationName();
        }
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public SoftwareCapabilityRequestBody(SoftwareCapabilityProperties template)
    {
        super(template);
    }


    /**
     * Return the unique identifier of the software server capability entity that represented the external source - null for local.
     *
     * @return string guid
     */
    public String getExternalSourceGUID()
    {
        return externalSourceGUID;
    }


    /**
     * Set up the unique identifier of the software server capability entity that represented the external source - null for local.
     *
     * @param externalSourceGUID string guid
     */
    public void setExternalSourceGUID(String externalSourceGUID)
    {
        this.externalSourceGUID = externalSourceGUID;
    }


    /**
     * Return the unique name of the software server capability entity that represented the external source.
     *
     * @return string name
     */
    public String getExternalSourceName()
    {
        return externalSourceName;
    }


    /**
     * Set up the unique name of the software server capability entity that represented the external source.
     *
     * @param externalSourceName string name
     */
    public void setExternalSourceName(String externalSourceName)
    {
        this.externalSourceName = externalSourceName;
    }


    /**
     * Return the optional classification name for the software server capability.
     *
     * @return string name
     */
    public String getClassificationName()
    {
        return classificationName;
    }


    /**
     * Set up the optional classification name for the software server capability.
     *
     * @param classificationName string name
     */
    public void setClassificationName(String classificationName)
    {
        this.classificationName = classificationName;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "SoftwareCapabilityRequestBody{" +
                       "externalSourceGUID='" + externalSourceGUID + '\'' +
                       ", externalSourceName='" + externalSourceName + '\'' +
                       ", classificationName='" + classificationName + '\'' +
                       ", displayName='" + getResourceName() + '\'' +
                       ", description='" + getResourceDescription() + '\'' +
                       ", typeDescription='" + getDeployedImplementationType() + '\'' +
                       ", version='" + getVersion() + '\'' +
                       ", patchLevel='" + getPatchLevel() + '\'' +
                       ", source='" + getSource() + '\'' +
                       ", effectiveFrom=" + getEffectiveFrom() +
                       ", effectiveTo=" + getEffectiveTo() +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
                       ", vendorProperties=" + getVendorProperties() +
                       ", typeName='" + getTypeName() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        SoftwareCapabilityRequestBody that = (SoftwareCapabilityRequestBody) objectToCompare;
        return Objects.equals(externalSourceGUID, that.externalSourceGUID) &&
                       Objects.equals(externalSourceName, that.externalSourceName) &&
                       Objects.equals(classificationName, that.classificationName);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), externalSourceGUID, externalSourceName, classificationName);
    }
}
