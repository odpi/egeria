/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serial;
import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RepositoryElementHeader provides a common base for all type and instance information from the metadata collection.
 * It implements Serializable.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public abstract class RepositoryElementHeader implements Serializable
{
    @Serial
    private static final long serialVersionUID = 1L;

    /*
     * Version number for this header.  The default is 1 to indicate that the element came from
     * the first version of the OMRS.
     */
    private long headerVersion = 1L;


    /**
     * Default Constructor sets the element to nulls
     */
    public RepositoryElementHeader()
    {
        /*
         * Nothing to do.
         */
    }


    /**
     * Copy/clone constructor set values from the template
     *
     * @param template InstanceElementHeader to copy
     */
    public RepositoryElementHeader(RepositoryElementHeader template)
    {
        if (template != null)
        {
            this.headerVersion = template.getHeaderVersion();
        }
    }


    /**
     * Return the version of this header.  This is used by the OMRS to determine if it is back level and
     * should not process events from a source that is more advanced because it does not have the ability
     * to receive all the header properties.
     *
     * @return long version number - the value is incremented each time a new non-informational field is added
     * to the type definition.
     */
    public long getHeaderVersion()
    {
        return headerVersion;
    }


    /**
     * Return the version of this header.  This is used by the OMRS to determine if it is back level and
     * should not process events from a source that is more advanced because it does not have the ability
     * to receive all the header properties.
     *
     * @param headerVersion long version number - the value is incremented each time a new non-informational field is added
     * to the type definition.
     */
    public void setHeaderVersion(long headerVersion)
    {
        this.headerVersion = headerVersion;
    }


    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof RepositoryElementHeader))
        {
            return false;
        }

        RepositoryElementHeader that = (RepositoryElementHeader) objectToCompare;

        return headerVersion == that.headerVersion;
    }


    /**
     * Return a hash code based on the property values
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return (int) (headerVersion ^ (headerVersion >>> 32));
    }
}
