/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openapis.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenAPIParameter describes a single parameter passed on the operation of the API.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenAPIParameter
{
    private String  name            = null;
    private String  description     = null;
    private String  in              = null;
    private boolean required        = true;
    private boolean deprecated      = false;
    private boolean allowEmptyValue = false;
    private String  $ref            = null;


    public OpenAPIParameter()
    {
    }


    public String getName()
    {
        return name;
    }


    public void setName(String name)
    {
        this.name = name;
    }


    public String getDescription()
    {
        return description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getIn()
    {
        return in;
    }


    public void setIn(String in)
    {
        this.in = in;
    }


    public boolean isRequired()
    {
        return required;
    }


    public void setRequired(boolean required)
    {
        this.required = required;
    }


    public boolean isDeprecated()
    {
        return deprecated;
    }


    public void setDeprecated(boolean deprecated)
    {
        this.deprecated = deprecated;
    }


    public boolean isAllowEmptyValue()
    {
        return allowEmptyValue;
    }


    public void setAllowEmptyValue(boolean allowEmptyValue)
    {
        this.allowEmptyValue = allowEmptyValue;
    }


    public String get$ref()
    {
        return $ref;
    }


    public void set$ref(String $ref)
    {
        this.$ref = $ref;
    }


    @Override
    public String toString()
    {
        return "OpenAPIParameter{" +
                       "name='" + name + '\'' +
                       ", description='" + description + '\'' +
                       ", in='" + in + '\'' +
                       ", required=" + required +
                       ", deprecated=" + deprecated +
                       ", allowEmptyValue=" + allowEmptyValue +
                       ", $ref='" + $ref + '\'' +
                       '}';
    }
}
