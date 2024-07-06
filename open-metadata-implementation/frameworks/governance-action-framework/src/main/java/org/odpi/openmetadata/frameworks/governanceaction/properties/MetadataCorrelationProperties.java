/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.governanceaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

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
    private String externalScopeGUID = null;
    private String externalScopeName = null;


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
            externalScopeGUID = template.getExternalScopeGUID();
            externalScopeName = template.getExternalScopeName();
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
     * Return the unique identifier of the software server capability that represents the asset manager.
     *
     * @return string guid
     */
    public String getExternalScopeGUID()
    {
        return externalScopeGUID;
    }


    /**
     * Set up the unique identifier of the software server capability that represents the asset manager.
     *
     * @param externalScopeGUID string guid
     */
    public void setExternalScopeGUID(String externalScopeGUID)
    {
        this.externalScopeGUID = externalScopeGUID;
    }


    /**
     * Return the qualified name of the software server capability that represents the asset manager.
     *
     * @return string name
     */
    public String getExternalScopeName()
    {
        return externalScopeName;
    }


    /**
     * Set up the qualified name of the software server capability that represents the asset manager.
     *
     * @param externalScopeName string name
     */
    public void setExternalScopeName(String externalScopeName)
    {
        this.externalScopeName = externalScopeName;
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
        return Objects.equals(externalScopeGUID, that.externalScopeGUID) && Objects.equals(externalScopeName, that.externalScopeName);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), externalScopeGUID, externalScopeName);
    }
}
