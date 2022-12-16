/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ArrayPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.ClassificationEntityExtension;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceAuditHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceElementHeader;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceGraph;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.typedefs.TypeDefElementHeader;

import java.io.Serializable;
import java.util.Objects;

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
    private static final long serialVersionUID = 1L;

    /*
     * Version number for this header.  This is used to ensure that all the critical header information
     * in read in a back-level version of the OMRS.  The default is 0 to indicate that the element came from
     * a version of the OMRS that does not have a version number encoded.
     */
    private long headerVersion = 0;


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
