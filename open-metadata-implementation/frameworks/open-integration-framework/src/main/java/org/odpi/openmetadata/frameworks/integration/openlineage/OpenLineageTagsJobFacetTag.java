/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.integration.openlineage;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * This class represents a single tag applied to a job.
 * It is part of the OpenLineage spec.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenLineageTagsJobFacetTag
{
    private String key = null;
    private String value = null;
    private String source = null;


    /**
     * Default constructor
     */
    public OpenLineageTagsJobFacetTag()
    {
    }


    /**
     * Return the key that identifies the tag.
     *
     * @return string
     */
    public String getKey()
    {
        return key;
    }


    /**
     * Set up the key that identifies the tag.
     *
     * @param key string
     */
    public void setKey(String key)
    {
        this.key = key;
    }


    /**
     * Return the value of the tag.
     *
     * @return string
     */
    public String getValue()
    {
        return value;
    }


    /**
     * Set up the value of the tag.
     *
     * @param value string
     */
    public void setValue(String value)
    {
        this.value = value;
    }


    /**
     * Return the source of the tag, for example INTEGRATION, USER, DBT CORE or SPARK.
     *
     * @return string
     */
    public String getSource()
    {
        return source;
    }


    /**
     * Set up the source of the tag, for example INTEGRATION, USER, DBT CORE or SPARK.
     *
     * @param source string
     */
    public void setSource(String source)
    {
        this.source = source;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "OpenLineageTagsJobFacetTag{" +
                       "key='" + key + '\'' +
                       ", value='" + value + '\'' +
                       ", source='" + source + '\'' +
                       '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
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
        OpenLineageTagsJobFacetTag that = (OpenLineageTagsJobFacetTag) objectToCompare;
        return Objects.equals(key, that.key) &&
                       Objects.equals(value, that.value) &&
                       Objects.equals(source, that.source);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(key, value, source);
    }
}
