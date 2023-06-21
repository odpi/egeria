/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.integration.openapis.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenAPISchema describes the type of a parameter.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenAPISchema
{
    private String              name         = null;
    private String              title        = null;
    private List<String>        required     = null;
    private String              type         = null;
    private String              description  = null;
    private String              format       = null;
    private boolean             nullable     = false;
    private boolean             readOnly     = false;
    private boolean             writeOnly    = false;
    private boolean             deprecated   = false;
    private OpenAPIExternalDocs externalDocs = null;
    private Map<String, Object> extensions   = null;

    private String              $ref         = null;


    public OpenAPISchema()
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


    public String getTitle()
    {
        return title;
    }


    public void setTitle(String title)
    {
        this.title = title;
    }


    public List<String> getRequired()
    {
        return required;
    }


    public void setRequired(List<String> required)
    {
        this.required = required;
    }


    public String getType()
    {
        return type;
    }


    public void setType(String type)
    {
        this.type = type;
    }


    public String getDescription()
    {
        return description;
    }


    public void setDescription(String description)
    {
        this.description = description;
    }


    public String getFormat()
    {
        return format;
    }


    public void setFormat(String format)
    {
        this.format = format;
    }


    public boolean isNullable()
    {
        return nullable;
    }


    public void setNullable(boolean nullable)
    {
        this.nullable = nullable;
    }


    public boolean isReadOnly()
    {
        return readOnly;
    }


    public void setReadOnly(boolean readOnly)
    {
        this.readOnly = readOnly;
    }


    public boolean isWriteOnly()
    {
        return writeOnly;
    }


    public void setWriteOnly(boolean writeOnly)
    {
        this.writeOnly = writeOnly;
    }


    public boolean isDeprecated()
    {
        return deprecated;
    }


    public void setDeprecated(boolean deprecated)
    {
        this.deprecated = deprecated;
    }


    public OpenAPIExternalDocs getExternalDocs()
    {
        return externalDocs;
    }


    public void setExternalDocs(OpenAPIExternalDocs externalDocs)
    {
        this.externalDocs = externalDocs;
    }


    public Map<String, Object> getExtensions()
    {
        return extensions;
    }


    public void setExtensions(Map<String, Object> extensions)
    {
        this.extensions = extensions;
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
        return "OpenAPISchema{" +
                       "name='" + name + '\'' +
                       ", title='" + title + '\'' +
                       ", required=" + required +
                       ", type='" + type + '\'' +
                       ", description='" + description + '\'' +
                       ", format='" + format + '\'' +
                       ", nullable=" + nullable +
                       ", readOnly=" + readOnly +
                       ", writeOnly=" + writeOnly +
                       ", deprecated=" + deprecated +
                       ", externalDocs=" + externalDocs +
                       ", extensions=" + extensions +
                       ", $ref='" + $ref + '\'' +
                       '}';
    }
}
