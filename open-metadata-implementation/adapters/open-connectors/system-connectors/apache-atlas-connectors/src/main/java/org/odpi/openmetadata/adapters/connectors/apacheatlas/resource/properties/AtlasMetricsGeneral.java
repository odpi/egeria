/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasMetricsGeneral describes the high-level metrics of the system.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasMetricsGeneral
{
    private Date                collectionTime  = null;
    private int                 entityCount     = 0;
    private Map<String, Object> stats           = null;
    private int                 tagCount        = 0;
    private int                 typeUnusedCount = 0;
    private int                 typeCount       = 0;


    /**
     * Default constructor
     */
    public AtlasMetricsGeneral()
    {
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public AtlasMetricsGeneral(AtlasMetricsGeneral template)
    {
        if (template != null)
        {
            this.collectionTime = template.getCollectionTime();
            this.entityCount = template.getEntityCount();
            this.stats = template.getStats();
            this.tagCount = template.getTagCount();
            this.typeUnusedCount = template.getTypeUnusedCount();
            this.typeCount = template.getTypeCount();
        }
    }


    /**
     * Return the date/time that the stats were extracted.
     *
     * @return date
     */
    public Date getCollectionTime()
    {
        return collectionTime;
    }


    /**
     * Set up the date/time that the stats were extracted.
     *
     * @param collectionTime date
     */
    public void setCollectionTime(Date collectionTime)
    {
        this.collectionTime = collectionTime;
    }


    /**
     * Return the count of entity instances in the Apache Atlas repository.
     *
     * @return int
     */
    public int getEntityCount()
    {
        return entityCount;
    }


    /**
     * Set up the count of entity instances in the Apache Atlas repository.
     *
     * @param entityCount int
     */
    public void setEntityCount(int entityCount)
    {
        this.entityCount = entityCount;
    }


    /**
     * Return a map of the different statistics defined for the system.
     *
     * @return map
     */
    public Map<String, Object> getStats()
    {
        return stats;
    }


    /**
     * Set up a map of the different statistics defined for the system.
     *
     * @param stats map
     */
    public void setStats(Map<String, Object> stats)
    {
        this.stats = stats;
    }


    /**
     * Return the count of classifications found in the repository.
     *
     * @return int
     */
    public int getTagCount()
    {
        return tagCount;
    }


    /**
     * Set up the count of classifications found in the repository.
     *
     * @param tagCount int
     */
    public void setTagCount(int tagCount)
    {
        this.tagCount = tagCount;
    }


    /**
     * Return the number of types that are not used.
     *
     * @return int
     */
    public int getTypeUnusedCount()
    {
        return typeUnusedCount;
    }


    /**
     * Set up the number of types that are not used.
     *
     * @param typeUnusedCount int
     */
    public void setTypeUnusedCount(int typeUnusedCount)
    {
        this.typeUnusedCount = typeUnusedCount;
    }


    /**
     * Return the number of types defined.
     *
     * @return int
     */
    public int getTypeCount()
    {
        return typeCount;
    }


    /**
     * Set up the number of types defined.
     *
     * @param typeCount int
     */
    public void setTypeCount(int typeCount)
    {
        this.typeCount = typeCount;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AtlasMetricsGeneral{" +
                       "collectionTime=" + collectionTime +
                       ", entityCount=" + entityCount +
                       ", stats=" + stats +
                       ", tagCount=" + tagCount +
                       ", typeUnusedCount=" + typeUnusedCount +
                       ", typeCount=" + typeCount +
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
        if (! (objectToCompare instanceof AtlasMetricsGeneral that))
        {
            return false;
        }
        return entityCount == that.entityCount &&
                       tagCount == that.tagCount &&
                       typeUnusedCount == that.typeUnusedCount &&
                       typeCount == that.typeCount &&
                       Objects.equals(collectionTime, that.collectionTime) &&
                       Objects.equals(stats, that.stats);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(collectionTime, entityCount, stats, tagCount, typeUnusedCount, typeCount);
    }
}
