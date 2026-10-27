/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.assets.filesandfolders;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.assets.DataSetProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SecretsCollectionProperties describes a collection of secrets.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SecretsCollectionProperties extends DataSetProperties
{
    private Long refreshTimeInterval = null;

    /**
     * Default constructor
     */
    public SecretsCollectionProperties()
    {
        super();
        super.typeName = OpenMetadataType.SECRETS_COLLECTION.typeName;
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public SecretsCollectionProperties(SecretsCollectionProperties template)
    {
        super(template);

        if (template != null)
        {
            refreshTimeInterval = template.getRefreshTimeInterval();
        }
    }


    /**
     * Return the time between refresh calls to load the secrets cache.
     *
     * @return long (minutes)
     */
    public Long getRefreshTimeInterval()
    {
        return refreshTimeInterval;
    }


    /**
     * Set up the time between refresh calls to load the secrets cache.
     *
     * @param refreshTimeInterval long (minutes)
     */
    public void setRefreshTimeInterval(Long refreshTimeInterval)
    {
        this.refreshTimeInterval = refreshTimeInterval;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SecretsCollectionProperties{" +
                "refreshTimeInterval=" + refreshTimeInterval +
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
        SecretsCollectionProperties that = (SecretsCollectionProperties) objectToCompare;
        return Objects.equals(refreshTimeInterval, that.refreshTimeInterval);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), refreshTimeInterval);
    }
}
