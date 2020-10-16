/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.rest.properties;

import com.fasterxml.jackson.annotation.*;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OMRSAPIRequest provides a common header for complex OMRS request to the OMRS REST API.   It manages
 * the parameters in the request body.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = OMRSAPIFindRequest.class, name = "OMRSAPIFindRequest"),
                @JsonSubTypes.Type(value = TypeDefReIdentifyRequest.class, name = "TypeDefReIdentifyRequest"),
                @JsonSubTypes.Type(value = InstancePropertiesRequest.class, name = "InstancePropertiesRequest"),
                @JsonSubTypes.Type(value = ClassificationRequest.class, name = "ClassificationRequest"),
                @JsonSubTypes.Type(value = InstanceGraphRequest.class, name = "InstanceGraphRequest"),
                @JsonSubTypes.Type(value = MetadataCollectionIdRequest.class, name = "MetadataCollectionIdRequest"),
                @JsonSubTypes.Type(value = EntityCreateRequest.class, name = "EntityCreateRequest"),
                @JsonSubTypes.Type(value = RelationshipCreateRequest.class, name = "RelationshipCreateRequest")
        })
public class OMRSAPIRequest implements Serializable
{
    private static final long       serialVersionUID = 1L;


    /**
     * Default constructor
     */
    public OMRSAPIRequest()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public OMRSAPIRequest(OMRSAPIRequest template)
    {
    }
}
