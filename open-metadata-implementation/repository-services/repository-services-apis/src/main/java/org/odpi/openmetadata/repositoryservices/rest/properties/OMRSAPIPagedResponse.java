/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.*;

import java.util.Arrays;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMRSRESTAPIPagedResponse provides the base definition for a paged response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = CohortListResponse.class, name = "CohortListResponse"),
                @JsonSubTypes.Type(value = CohortMembershipListResponse.class, name = "CohortMembershipListResponse"),
                @JsonSubTypes.Type(value = EntityListResponse.class, name = "EntityListResponse"),
                @JsonSubTypes.Type(value = RelationshipListResponse.class, name = "RelationshipListResponse")
        })
public abstract class OMRSAPIPagedResponse extends OMRSAPIResponse
{
    private static final long    serialVersionUID = 1L;

    protected String  nextPageURL = null;
    protected int     offset      = 0;
    protected int     pageSize    = 0;


    /**
     * Default constructor
     */
    public OMRSAPIPagedResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public OMRSAPIPagedResponse(OMRSAPIPagedResponse template)
    {
        super(template);

        if (template != null)
        {
            nextPageURL = template.getNextPageURL();
            offset = template.getOffset();
            pageSize = template.getPageSize();
        }
    }


    /**
     * Return the url that can be used to retrieve the next page.
     *
     * @return url string
     */
    public String getNextPageURL()
    {
        return nextPageURL;
    }


    /**
     * Set up the url that can be used to retrieve the next page.
     *
     * @param nextPageURL url string
     */
    public void setNextPageURL(String nextPageURL)
    {
        this.nextPageURL = nextPageURL;
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
        return "OMRSRESTAPIPagedResponse{" +
                "nextPageURL='" + nextPageURL + '\'' +
                ", offset=" + offset +
                ", pageSize=" + pageSize +
                ", relatedHTTPCode=" + relatedHTTPCode +
                ", actionDescription='" + actionDescription + '\'' +
                ", exceptionClassName='" + exceptionClassName + '\'' +
                ", exceptionCausedBy='" + exceptionCausedBy + '\'' +
                ", exceptionErrorMessage='" + exceptionErrorMessage + '\'' +
                ", exceptionErrorMessageId='" + exceptionErrorMessageId + '\'' +
                ", exceptionErrorMessageParameters=" + Arrays.toString(exceptionErrorMessageParameters) +
                ", exceptionSystemAction='" + exceptionSystemAction + '\'' +
                ", exceptionUserAction='" + exceptionUserAction + '\'' +
                ", exceptionProperties=" + exceptionProperties +
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
        if (!(objectToCompare instanceof OMRSAPIPagedResponse))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        OMRSAPIPagedResponse
                that = (OMRSAPIPagedResponse) objectToCompare;
        return getOffset() == that.getOffset() &&
                getPageSize() == that.getPageSize() &&
                Objects.equals(getNextPageURL(), that.getNextPageURL());
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getNextPageURL(), getOffset(), getPageSize());
    }
}
