/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.SequencingOrder;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMRSAPIPagedFindRequest provides support for the paging parameters of a find request.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = TypeLimitedFindRequest.class, name = "TypeLimitedFindRequest"),
                @JsonSubTypes.Type(value = RelatedEntitiesFindRequest.class, name = "RelatedEntitiesFindRequest"),
                @JsonSubTypes.Type(value = PropertyMatchFindRequest.class, name = "PropertyMatchFindRequest")
        })
public class OMRSAPIPagedFindRequest extends OMRSAPIFindRequest
{
    private static final long    serialVersionUID = 1L;

    private String               sequencingProperty   = null;
    private SequencingOrder      sequencingOrder      = null;
    private int                  offset               = 0;
    private int                  pageSize             = 0;

    /**
     * Default constructor
     */
    public OMRSAPIPagedFindRequest()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public OMRSAPIPagedFindRequest(OMRSAPIPagedFindRequest template)
    {
        super(template);

        if (template != null)
        {
            this.sequencingProperty = template.getSequencingProperty();
            this.sequencingOrder = template.getSequencingOrder();
            this.offset = template.getOffset();
            this.pageSize = getPageSize();
        }
    }


    /**
     * Return the name of the property that should be used to sequence the results.
     *
     * @return property name
     */
    public String getSequencingProperty()
    {
        return sequencingProperty;
    }


    /**
     * Set up the name of the property that should be used to sequence the results.
     *
     * @param sequencingProperty property name
     */
    public void setSequencingProperty(String sequencingProperty)
    {
        this.sequencingProperty = sequencingProperty;
    }


    /**
     * Return the sequencing order for the results.
     *
     * @return sequencing order enum
     */
    public SequencingOrder getSequencingOrder()
    {
        return sequencingOrder;
    }


    /**
     * Set up the sequencing order for the results.
     *
     * @param sequencingOrder sequencing order enum
     */
    public void setSequencingOrder(SequencingOrder sequencingOrder)
    {
        this.sequencingOrder = sequencingOrder;
    }


    /**
     * Return the starting element number for this set of results.  This is used when retrieving elements
     * beyond the first page of results. Zero means the results start from the first element.
     *
     * @return offset number
     */
    public int getOffset()
    {
        return offset;
    }


    /**
     * Set up the starting element number for this set of results.  This is used when retrieving elements
     * beyond the first page of results. Zero means the results start from the first element.
     *
     * @param offset offset number
     */
    public void setOffset(int offset)
    {
        this.offset = offset;
    }


    /**
     * Return the maximum number of elements that can be returned on this request.
     *
     * @return page size
     */
    public int getPageSize()
    {
        return pageSize;
    }


    /**
     * Set up the maximum number of elements that can be returned on this request.
     *
     * @param pageSize integer number
     */
    public void setPageSize(int pageSize)
    {
        this.pageSize = pageSize;
    }



    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OMRSAPIPagedFindRequest{" +
                "sequencingProperty='" + sequencingProperty + '\'' +
                ", sequencingOrder=" + sequencingOrder +
                ", offset=" + offset +
                ", pageSize=" + pageSize +
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
        if (!(objectToCompare instanceof OMRSAPIPagedFindRequest))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        OMRSAPIPagedFindRequest
                that = (OMRSAPIPagedFindRequest) objectToCompare;
        return getOffset() == that.getOffset() &&
                getPageSize() == that.getPageSize() &&
                Objects.equals(getSequencingProperty(), that.getSequencingProperty()) &&
                getSequencingOrder() == that.getSequencingOrder();
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {

        return Objects.hash(super.hashCode(),
                            getSequencingProperty(),
                            getSequencingOrder(),
                            getOffset(),
                            getPageSize());
    }
}
