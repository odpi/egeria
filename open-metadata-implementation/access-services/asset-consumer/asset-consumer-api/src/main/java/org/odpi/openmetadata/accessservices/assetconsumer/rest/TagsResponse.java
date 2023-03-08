/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetconsumer.rest;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.assetconsumer.elements.InformalTagElement;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 *  TagsResponse returns a list of tags from the server.   The list may be too long to
 *  retrieve in a single call so there is support for paging of replies.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class TagsResponse extends PagedResponse
{
    private static final long    serialVersionUID = 1L;

    private List<InformalTagElement> tags = null;


    /**
     * Default constructor
     */
    public TagsResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public TagsResponse(TagsResponse template)
    {
        super(template);

        if (template != null)
        {
            this.tags = template.getTags();
        }
    }


    /**
     * Return the list of informal tags in the response.
     *
     * @return list of informal tags
     */
    public List<InformalTagElement> getTags()
    {
        if (tags == null)
        {
            return null;
        }
        else if (tags.isEmpty())
        {
            return null;
        }
        else
        {
            List<InformalTagElement>  clonedList = new ArrayList<>();

            for (InformalTagElement  existingElement : tags)
            {
                clonedList.add(new InformalTagElement(existingElement));
            }

            return clonedList;
        }
    }


    /**
     * Set up the list of informal tags for the response.
     *
     * @param tags list
     */
    public void setTags(List<InformalTagElement> tags)
    {
        this.tags = tags;
    }

    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "TagsResponse{" +
                "tags=" + tags +
                ", startingFromElement=" + getStartingFromElement() +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionCausedBy='" + getExceptionCausedBy() + '\'' +
                ", actionDescription='" + getActionDescription() + '\'' +
                ", relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionErrorMessageId='" + getExceptionErrorMessageId() + '\'' +
                ", exceptionErrorMessageParameters=" + Arrays.toString(getExceptionErrorMessageParameters()) +
                ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                ", exceptionProperties=" + getExceptionProperties() +
                '}';
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
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
        TagsResponse that = (TagsResponse) objectToCompare;
        return getStartingFromElement() == that.getStartingFromElement() &&
                Objects.equals(getTags(), that.getTags());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getTags(), getStartingFromElement());
    }
}
