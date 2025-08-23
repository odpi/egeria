/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ffdc.rest;

import com.fasterxml.jackson.annotation.*;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions.GovernanceActionProcessProperties;
import org.odpi.openmetadata.frameworks.openmetadata.properties.governance.governanceactions.GovernanceActionTypeProperties;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * DeleteRequestBody carries the options for a delete element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = DeleteElementRequestBody.class, name = "DeleteElementRequestBody"),
                @JsonSubTypes.Type(value = DeleteRelationshipRequestBody.class, name = "DeleteRelationshipRequestBody"),
                @JsonSubTypes.Type(value = DeleteClassificationRequestBody.class, name = "DeleteClassificationRequestBody"),
        })
public class DeleteRequestBody extends DeleteOptions
{

    /**
     * Default constructor
     */
    public DeleteRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DeleteRequestBody(DeleteOptions template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public DeleteRequestBody(MetadataSourceOptions template)
    {
        super(template);
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "DeleteRequestBody{" +  '}';
    }
}
