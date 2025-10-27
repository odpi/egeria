/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.specificationproperties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.refdata.SpecificationPropertyType;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * SupportedConfigurationProperty characterises one of the configuration parameters supported by a specific connector.
 * This enables the capability of a connector to be correctly matched to the resources and
 * elements that it works with.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SupportedConfigurationProperty extends ConfigurationPropertyType
{
    /**
     * Default constructor
     */
    public SupportedConfigurationProperty()
    {
        super();
        super.setSpecificationPropertyType(SpecificationPropertyType.SUPPORTED_CONFIGURATION_PROPERTY);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SupportedConfigurationProperty(ConfigurationPropertyType template)
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
        return "SupportedConfigurationPropertyType{" +
                "} " + super.toString();
    }
}
