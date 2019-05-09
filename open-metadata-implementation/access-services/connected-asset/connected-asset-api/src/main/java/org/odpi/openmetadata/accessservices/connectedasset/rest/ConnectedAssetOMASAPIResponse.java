/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.connectedasset.rest;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.commonservices.ffdc.rest.CountResponse;
import org.odpi.openmetadata.commonservices.ffdc.rest.FFDCResponseBase;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ConnectedAssetOMASAPIResponse provides a common header for Connected Asset OMAS managed rest to its REST API.
 * It manages information about exceptions.  If no exception has been raised exceptionClassName is null.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = AssetResponse.class, name = "AssetResponse"),
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
                @JsonSubTypes.Type(value = SchemaTypeResponse.class, name = "SchemaTypeResponse"),
                @JsonSubTypes.Type(value = SchemaAttributesResponse.class, name = "SchemaAttributesResponse"),
                @JsonSubTypes.Type(value = CountResponse.class, name = "CountResponse")
        })
public abstract class ConnectedAssetOMASAPIResponse extends FFDCResponseBase
{
    /**
     * Default constructor
     */
    public ConnectedAssetOMASAPIResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ConnectedAssetOMASAPIResponse(ConnectedAssetOMASAPIResponse template)
    {
        super(template);
    }


    /**
     * JSON-like toString
     *
     * @return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ConnectedAssetOMASAPIResponse{" +
                "relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                ", exceptionProperties=" + getExceptionProperties() +
                '}';
    }
}
