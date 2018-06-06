/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * The AdditionalProperties bean extends the AdditionalProperties from the properties package with a default constructor and
 * setter methods.  This means it can be used for REST calls and other JSON based functions.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AdditionalProperties extends org.odpi.openmetadata.frameworks.connectors.properties.AdditionalProperties
{

    /**
     * Default constructor
     */
    public AdditionalProperties()
    {
        super(null);
    }


    /**
     * Set up the additional properties.
     *
     * @param additionalProperties - property map
     */
    public void setAdditionalProperties(Map<String, Object> additionalProperties)
    {
        super.additionalProperties = additionalProperties;
    }


    /**
     * Copy/clone Constructor for additional properties that are connected to an asset.
     *
     * @param templateProperties - template object to copy.
     */
    public AdditionalProperties(AdditionalProperties templateProperties)
    {
        super(null, templateProperties);
    }
}
