/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;


import com.fasterxml.jackson.annotation.*;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TypeLimitedFindRequest extends the paged find request to allow the caller to request results from only
 * one type of instance.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = PropertyMatchFindRequest.class, name = "PropertyMatchFindRequest"),
                @JsonSubTypes.Type(value = TypeLimitedHistoricalFindRequest.class, name = "TypeLimitedHistoricalFindRequest"),
                @JsonSubTypes.Type(value = SubtypeLimitedFindRequest.class, name = "SubtypeLimitedFindRequest")
        })
public class TypeLimitedFindRequest extends OMRSAPIPagedFindRequest
{
    private static final long    serialVersionUID = 1L;

    private String                     typeGUID = null;


    /**
     * Default constructor
     */
    public TypeLimitedFindRequest()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public TypeLimitedFindRequest(TypeLimitedFindRequest template)
    {
        super(template);

        if (template != null)
        {
            this.typeGUID = template.getTypeGUID();
        }
    }


    /**
     * Return the type guid to limit the results of the find request.
     *
     * @return String guid
     */
    public String getTypeGUID()
    {
        return typeGUID;
    }


    /**
     * Set up the type guid to limit the results of the find request.
     *
     * @param typeGUID String guid
     */
    public void setTypeGUID(String typeGUID)
    {
        this.typeGUID = typeGUID;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "TypeLimitedFindRequest{" +
                "typeGUID='" + typeGUID + '\'' +
                ", sequencingProperty='" + getSequencingProperty() + '\'' +
                ", sequencingOrder=" + getSequencingOrder() +
                ", offset=" + getOffset() +
                ", pageSize=" + getPageSize() +
                ", limitResultsByStatus=" + getLimitResultsByStatus() +
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
        if (!(objectToCompare instanceof TypeLimitedFindRequest))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        TypeLimitedFindRequest
                that = (TypeLimitedFindRequest) objectToCompare;
        return Objects.equals(getTypeGUID(), that.getTypeGUID());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(super.hashCode(), getTypeGUID());
    }
}
