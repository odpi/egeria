/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworkservices.omf.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.search.DeleteOptions;
import org.odpi.openmetadata.frameworks.openmetadata.search.MetadataSourceOptions;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataDeleteRequestBody carries the options for a delete element.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataDeleteRequestBody extends DeleteOptions
{

    /**
     * Default constructor
     */
    public OpenMetadataDeleteRequestBody()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public OpenMetadataDeleteRequestBody(DeleteOptions template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public OpenMetadataDeleteRequestBody(MetadataSourceOptions template)
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
        return "OpenMetadataDeleteRequestBody{" +  '}';
    }
}
