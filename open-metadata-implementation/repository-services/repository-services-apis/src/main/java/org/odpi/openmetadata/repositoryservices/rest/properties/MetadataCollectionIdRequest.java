/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * MetadataCollectionIdRequest is the request structure used on the OMRS REST API calls that use the home metadata collection id as
 * a validator/originator for the request.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class MetadataCollectionIdRequest extends OMRSAPIRequest
{
    private static final long    serialVersionUID = 1L;

    private String metadataCollectionId = null;


    /**
     * Default constructor
     */
    public MetadataCollectionIdRequest()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public MetadataCollectionIdRequest(MetadataCollectionIdRequest template)
    {
        super(template);

        if (template != null)
        {
            metadataCollectionId = template.getMetadataCollectionId();
        }
    }

    /**
     * Return the metadata collection id for the local repository.
     *
     * @return string id
     */
    public String getMetadataCollectionId()
    {
        return metadataCollectionId;
    }


    /**
     * Set up the metadata collection id for the local repository.
     *
     * @param metadataCollectionId string id
     */
    public void setMetadataCollectionId(String metadataCollectionId)
    {
        this.metadataCollectionId = metadataCollectionId;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "BooleanResponse{" +
                "metadataCollectionId=" + metadataCollectionId +
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
        if (!(objectToCompare instanceof MetadataCollectionIdRequest))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        MetadataCollectionIdRequest
                that = (MetadataCollectionIdRequest) objectToCompare;
        return metadataCollectionId.equals(that.metadataCollectionId);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getMetadataCollectionId());
    }
}
