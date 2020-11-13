/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest;

import com.fasterxml.jackson.annotation.*;

import java.util.Arrays;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * PagedResponse is used for responses that can contain paged responses
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = AssetsResponse.class, name = "AssetsResponse"),
                @JsonSubTypes.Type(value = CertificationsResponse.class, name = "CertificationsResponse"),
                @JsonSubTypes.Type(value = CommentsResponse.class, name = "CommentsResponse"),
                @JsonSubTypes.Type(value = ConnectionsResponse.class, name = "ConnectionsResponse"),
                @JsonSubTypes.Type(value = ExternalIdentifiersResponse.class, name = "ExternalIdentifiersResponse"),
                @JsonSubTypes.Type(value = ExternalReferencesResponse.class, name = "ExternalReferencesResponse"),
                @JsonSubTypes.Type(value = InformalTagsResponse.class, name = "InformalTagsResponse"),
                @JsonSubTypes.Type(value = LicensesResponse.class, name = "LicensesResponse"),
                @JsonSubTypes.Type(value = LikesResponse.class, name = "LikesResponse"),
                @JsonSubTypes.Type(value = LocationsResponse.class, name = "LocationsResponse"),
                @JsonSubTypes.Type(value = NoteLogsResponse.class, name = "NoteLogsResponse"),
                @JsonSubTypes.Type(value = NotesResponse.class, name = "NotesResponse"),
                @JsonSubTypes.Type(value = RatingsResponse.class, name = "RatingsResponse"),
                @JsonSubTypes.Type(value = RelatedAssetsResponse.class, name = "RelatedAssetsResponse"),
                @JsonSubTypes.Type(value = RelatedMediaReferencesResponse.class, name = "RelatedMediaReferencesResponse"),
                @JsonSubTypes.Type(value = SchemaAttributesResponse.class, name = "SchemaAttributesResponse"),
                @JsonSubTypes.Type(value = TagsResponse.class, name = "TagsResponse"),
                @JsonSubTypes.Type(value = ValidValuesResponse.class, name = "ValidValuesResponse"),
                @JsonSubTypes.Type(value = ReferenceablesResponse.class, name = "ReferenceablesResponse")
        })
public class PagedResponse extends OCFOMASAPIResponse
{
    private static final long    serialVersionUID = 1L;

    private int        startingFromElement = 0;

    /**
     * Default constructor
     */
    public PagedResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public PagedResponse(PagedResponse template)
    {
        super(template);

        if (template != null)
        {
            this.startingFromElement = template.getStartingFromElement();
        }
    }


    /**
     * Return the starting element number from the server side list that this response contains.
     *
     * @return int
     */
    public int getStartingFromElement()
    {
        return startingFromElement;
    }


    /**
     * Set up the starting element number from the server side list that this response contains.
     *
     * @param startingFromElement int
     */
    public void setStartingFromElement(int startingFromElement)
    {
        this.startingFromElement = startingFromElement;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "PagedResponse{" +
                "startingFromElement=" + startingFromElement +
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
        PagedResponse that = (PagedResponse) objectToCompare;
        return getStartingFromElement() == that.getStartingFromElement();
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getStartingFromElement());
    }
}
