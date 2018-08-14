/* SPDX-License-Identifier: Apache-2.0 */
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
                @JsonSubTypes.Type(value = TypeDefCategoryFindRequest.class, name = "TypeDefCategoryFindRequest")
        })
public abstract class OMRSAPIRequest implements Serializable
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
