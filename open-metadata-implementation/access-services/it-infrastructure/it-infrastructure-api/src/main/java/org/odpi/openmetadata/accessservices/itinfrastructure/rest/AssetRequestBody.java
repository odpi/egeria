/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.itinfrastructure.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.itinfrastructure.properties.AssetProperties;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.ElementStatus;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * AssetRequestBody describes the properties of the asset for creation and update.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssetRequestBody extends AssetProperties
{
    private static final long    serialVersionUID = 1L;

    private String        externalSourceGUID = null;
    private String        externalSourceName = null;
    private ElementStatus initialStatus      = null;


    /**
     * Default constructor
     */
    public AssetRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public AssetRequestBody(AssetRequestBody template)
    {
        super(template);

        if (template != null)
        {
            externalSourceGUID = template.getExternalSourceGUID();
            externalSourceName = template.getExternalSourceName();
        }
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public AssetRequestBody(AssetProperties template)
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
     * Return the initial status ti use (if different from the default value).
     *
     * @return enum or null for default value
     */
    public ElementStatus getInitialStatus()
    {
        return initialStatus;
    }


    /**
     * Set up the initial status ti use (if different from the default value).
     *
     * @param initialStatus enum or null for default value
     */
    public void setInitialStatus(ElementStatus initialStatus)
    {
        this.initialStatus = initialStatus;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AssetRequestBody{" +
                       "externalSourceGUID='" + externalSourceGUID + '\'' +
                       ", externalSourceName='" + externalSourceName + '\'' +
                       ", initialStatus=" + initialStatus +
                       ", displayName='" + getDisplayName() + '\'' +
                       ", description='" + getDescription() + '\'' +
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
        AssetRequestBody that = (AssetRequestBody) objectToCompare;
        return Objects.equals(externalSourceGUID, that.externalSourceGUID) &&
                       Objects.equals(externalSourceName, that.externalSourceName) &&
                       initialStatus == that.initialStatus;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), externalSourceGUID, externalSourceName, initialStatus);
    }
}
