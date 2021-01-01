/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.properties.objects.common;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * Config is used by the Subject Area OMAS to retrieve useful configuration information when using the subject area APIs
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class Config
{
    private Integer               maxPageSize       = null;

    /**
     * Default constructor
     */
    public Config() {
    }

    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public Config(Config template) {
        if (template != null) {
            this.maxPageSize = template.getMaxPageSize();
        }
    }

    /**
     * Get the maximum page size that will be accepted by the Subject Area OMAS
     * @return the maximum page size
     */
    public Integer getMaxPageSize() {
        return maxPageSize;
    }

    /**
     * Set the maximum page size that will be accepted by the Subject Area OMAS
     * @param maxPageSize maximum page size
     */
    public void setMaxPageSize(Integer maxPageSize) {
        this.maxPageSize = maxPageSize;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "Config{" +
                "maxPageSize='" + maxPageSize +
                '}';
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Config that = (Config) o;
        return
                Objects.equals(maxPageSize, that.maxPageSize);
    }

    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(maxPageSize);
    }
}