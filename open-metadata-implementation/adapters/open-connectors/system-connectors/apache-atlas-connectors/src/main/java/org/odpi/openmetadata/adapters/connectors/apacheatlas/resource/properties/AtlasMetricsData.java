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
 * AtlasMetricsData returns information about a running Apache Atlas.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasMetricsData
{
    private AtlasMetricsGeneral               general = null;
    private AtlasMetricsSystem                system  = null;
    private AtlasMetricsTag                   tag     = null;
    private Map<String, Map<String, Integer>> entity  = null;


    /**
     * Default constructor.
     */
    public AtlasMetricsData()
    {
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public AtlasMetricsData(AtlasMetricsData template)
    {
        if (template != null)
        {
            this.general = template.getGeneral();
            this.system = template.getSystem();
            this.tag = template.getTag();
            this.entity = template.getEntity();
        }
    }


    /**
     * Return summary metrics about the metadata stored in the repository.
     *
     * @return general metrics
     */
    public AtlasMetricsGeneral getGeneral()
    {
        return general;
    }


    /**
     * Set up summary metrics about the metadata stored in the repository.
     *
     * @param general general metrics
     */
    public void setGeneral(AtlasMetricsGeneral general)
    {
        this.general = general;
    }


    /**
     * Return metrics relating to the IT infrastructure supporting Apache Atlas - such as memory, os and Java level.
     *
     * @return system metrics
     */
    public AtlasMetricsSystem getSystem()
    {
        return system;
    }


    /**
     * Set up metrics relating to the IT infrastructure supporting Apache Atlas - such as memory, os and Java level.
     *
     * @param system system metrics
     */
    public void setSystem(AtlasMetricsSystem system)
    {
        this.system = system;
    }


    /**
     * Return details of the classifications in use in the Apache Atlas system.
     *
     * @return tag metrics
     */
    public AtlasMetricsTag getTag()
    {
        return tag;
    }


    /**
     * Set up details of the classifications in use in the Apache Atlas system.
     *
     * @param tag tag metrics
     */
    public void setTag(AtlasMetricsTag tag)
    {
        this.tag = tag;
    }


    /**
     * Return details of the entities in the Apache Atlas metadata repository.  The name of the properties are not
     * legal names in Java which is why it is represented as a map.  It has 6 members in the outer map:
     * <ul>
     *     <li>"entityActive"</li>
     *     <li>"entityDeleted"</li>
     *     <li>"entityActive-typeAndSubTypes"</li>
     *     <li>"entityDeleted-typeAndSubTypes"</li>
     *     <li>"entityShell"</li>
     *     <li>"entityShell-typeAndSubTypes"</li>
     * </ul>
     * The inner maps are entityTypeName to number of instances.
     *
     * @return nested map
     */
    public Map<String, Map<String, Integer>> getEntity()
    {
        return entity;
    }


    /**
     * Set up the map of metrics about entities.
     *
     * @param entity nested map
     */
    public void setEntity(Map<String, Map<String, Integer>> entity)
    {
        this.entity = entity;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AtlasMetricsData{" +
                       "general=" + general +
                       ", system=" + system +
                       ", tag=" + tag +
                       ", entity=" + entity +
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
        if (! (objectToCompare instanceof AtlasMetricsData that))
        {
            return false;
        }
        return Objects.equals(general, that.general) &&
                       Objects.equals(system, that.system) &&
                       Objects.equals(tag, that.tag) &&
                       Objects.equals(entity, that.entity);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(general, system, tag, entity);
    }
}
