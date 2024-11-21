/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.governanceaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * MetadataCorrelationProperties describes the common properties used to pass the properties of metadata elements
 * to the metadata repositories (aka properties server).  It includes details of the external source of the
 * metadata and any properties that assists in the mapping of the open metadata elements to the external
 * asset manager's copy.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetadataCorrelationProperties extends ExternalIdentifierProperties
{
    private String externalScopeGUID     = null;
    private String externalScopeName     = null;
    private String externalScopeTypeName = OpenMetadataType.INVENTORY_CATALOG.typeName;


    /**
     * Default constructor
     */
    public MetadataCorrelationProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public MetadataCorrelationProperties(MetadataCorrelationProperties template)
    {
        super(template);
        if (template != null)
        {
            externalScopeGUID     = template.getExternalScopeGUID();
            externalScopeName     = template.getExternalScopeName();
            externalScopeTypeName = template.getExternalScopeTypeName();
        }
    }


    /**
     * Copy/clone constructor.
     *
     * @param template template object to copy.
     */
    public MetadataCorrelationProperties(ExternalIdentifierProperties template)
    {
        super(template);
    }


    /**
     * Return the unique identifier of the software server capability that represents the manager for the external identifier.
     *
     * @return string guid
     */
    public String getExternalScopeGUID()
    {
        return externalScopeGUID;
    }


    /**
     * Set up the unique identifier of the software server capability that represents the manager for the external identifier.
     *
     * @param externalScopeGUID string guid
     */
    public void setExternalScopeGUID(String externalScopeGUID)
    {
        this.externalScopeGUID = externalScopeGUID;
    }


    /**
     * Return the qualified name of the software server capability that represents the manager for the external identifier.
     *
     * @return string name
     */
    public String getExternalScopeName()
    {
        return externalScopeName;
    }


    /**
     * Set up the qualified name of the software server capability that represents the manager for the external identifier.
     *
     * @param externalScopeName string name
     */
    public void setExternalScopeName(String externalScopeName)
    {
        this.externalScopeName = externalScopeName;
    }


    /**
     * Return the type of the manager for the external identifier.  The default is InventoryCatalog.
     *
     * @return type name
     */
    public String getExternalScopeTypeName()
    {
        return externalScopeTypeName;
    }


    /**
     * Set up the type of the manager for the external identifier.  The default is InventoryCatalog.
     *
     * @param externalScopeTypeName type name
     */
    public void setExternalScopeTypeName(String externalScopeTypeName)
    {
        this.externalScopeTypeName = externalScopeTypeName;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "MetadataCorrelationProperties{" +
                       "externalScopeGUID='" + externalScopeGUID + '\'' +
                       ", externalScopeName='" + externalScopeName + '\'' +
                       ", externalScopeTypeName='" + externalScopeTypeName + '\'' +
                       ", synchronizationDirection=" + getSynchronizationDirection() +
                       ", synchronizationDescription='" + getSynchronizationDescription() + '\'' +
                       ", externalIdentifier='" + getExternalIdentifier() + '\'' +
                       ", externalIdentifierName='" + getExternalIdentifierName() + '\'' +
                       ", externalIdentifierUsage='" + getExternalIdentifierUsage() + '\'' +
                       ", externalIdentifierSource='" + getExternalIdentifierSource() + '\'' +
                       ", keyPattern=" + getKeyPattern() +
                       ", externalInstanceCreatedBy='" + getExternalInstanceCreatedBy() + '\'' +
                       ", externalInstanceCreationTime=" + getExternalInstanceCreationTime() +
                       ", externalInstanceLastUpdatedBy='" + getExternalInstanceLastUpdatedBy() + '\'' +
                       ", externalInstanceLastUpdateTime=" + getExternalInstanceLastUpdateTime() +
                       ", externalInstanceVersion=" + getExternalInstanceVersion() +
                       ", mappingProperties=" + getMappingProperties() +
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
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        MetadataCorrelationProperties that = (MetadataCorrelationProperties) objectToCompare;
        return Objects.equals(externalScopeGUID, that.externalScopeGUID) && Objects.equals(externalScopeName, that.externalScopeName)  && Objects.equals(externalScopeTypeName, that.externalScopeTypeName);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), externalScopeGUID, externalScopeName, externalScopeTypeName);
    }
}
