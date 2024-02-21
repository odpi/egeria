/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasMetricsTag defines a map of classification type names to the number of entities attached to each tag.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasMetricsTag
{
    private Map<String, Integer> tagEntities = null;


    /**
     * Default constructor
     */
    public AtlasMetricsTag()
    {
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public AtlasMetricsTag(AtlasMetricsTag template)
    {
        if (template != null)
        {
            this.tagEntities = template.getTagEntities();
        }
    }


    /**
     * Return a map of classification names to counts of the number of entities it is attached to.
     *
     * @return tagged entity metrics
     */
    public Map<String, Integer> getTagEntities()
    {
        return tagEntities;
    }


    /**
     * Set up a map of classification names to counts of the number of entities it is attached to.
     *
     * @param tagEntities tagged entity metrics
     */
    public void setTagEntities(Map<String, Integer> tagEntities)
    {
        this.tagEntities = tagEntities;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AtlasMetricsTag{" +
                       "tagEntities=" + tagEntities +
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
        if (! (objectToCompare instanceof AtlasMetricsTag that))
        {
            return false;
        }
        return Objects.equals(tagEntities, that.tagEntities);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(tagEntities);
    }
}
