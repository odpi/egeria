/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.search;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * LevelIdentifierQueryProperties describes the properties for searching for a governance action classification by its level.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class LevelIdentifierQueryProperties extends FindProperties
{
    private boolean returnSpecificLevel = false;
    private int     levelIdentifier     = 0;


    /**
     * Default constructor
     */
    public LevelIdentifierQueryProperties()
    {
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public LevelIdentifierQueryProperties(LevelIdentifierQueryProperties template)
    {
        super(template);

        if (template != null)
        {
            this.returnSpecificLevel = template.getReturnSpecificLevel();
            this.levelIdentifier = template.getLevelIdentifier();
        }
    }


    /**
     * Return whether the level identifier is in use
     *
     * @return boolean
     */
    public boolean getReturnSpecificLevel()
    {
        return returnSpecificLevel;
    }


    /**
     * Set up whether the level identifier is in use.
     *
     * @param flag boolean
     */
    public void setReturnSpecificLevel(boolean flag)
    {
        returnSpecificLevel = flag;
    }


    /**
     * Return the level to match on.
     *
     * @return int
     */
    public int getLevelIdentifier()
    {
        return levelIdentifier;
    }


    /**
     * Set up the level to match on.
     *
     * @param levelIdentifier int
     */
    public void setLevelIdentifier(int levelIdentifier)
    {
        this.levelIdentifier = levelIdentifier;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "LevelIdentifierQueryProperties{" +
                "returnSpecificLevel=" + returnSpecificLevel +
                ", levelIdentifier=" + levelIdentifier +
                "} " + super.toString();
    }


    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        LevelIdentifierQueryProperties that = (LevelIdentifierQueryProperties) objectToCompare;
        return returnSpecificLevel == that.returnSpecificLevel && levelIdentifier == that.levelIdentifier;
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), returnSpecificLevel, levelIdentifier);
    }
}
