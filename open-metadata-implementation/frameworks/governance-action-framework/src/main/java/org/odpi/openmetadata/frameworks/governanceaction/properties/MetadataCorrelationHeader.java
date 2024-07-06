/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.governanceaction.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * MetadataCorrelationHeader provides details of the external identifier(s) and other correlation
 * properties to help the connector/client work out the correlation between the open metadata elements
 * and the third party technology elements.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class MetadataCorrelationHeader extends MetadataCorrelationProperties
{
    private Date lastSynchronized = null;


    /**
     * Default constructor
     */
    public MetadataCorrelationHeader()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public MetadataCorrelationHeader(MetadataCorrelationHeader template)
    {
        super(template);

        if (template != null)
        {
            this.lastSynchronized = template.getLastSynchronized();
        }
    }


    /**
     * Return the last time that the metadata properties were synchronized between open metadata and
     * the third party technology.
     *
     * @return timestamp
     */
    public Date getLastSynchronized()
    {
        return lastSynchronized;
    }


    /**
     * Set up the last time that the metadata properties were synchronized between open metadata and
     * the third party technology.
     *
     * @param lastSynchronized timestamp
     */
    public void setLastSynchronized(Date lastSynchronized)
    {
        this.lastSynchronized = lastSynchronized;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "MetadataCorrelationHeader{" +
                       "lastSynchronized=" + lastSynchronized +
                       ", externalScopeGUID='" + getExternalScopeGUID() + '\'' +
                       ", externalScopeName='" + getExternalScopeName() + '\'' +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        MetadataCorrelationHeader that = (MetadataCorrelationHeader) objectToCompare;
        return Objects.equals(getLastSynchronized(), that.getLastSynchronized());
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getLastSynchronized());
    }
}
