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
 * Search provides a structure for the additional options needed for a search.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SearchOptions extends QueryOptions
{
    private boolean startsWith = true;
    private boolean endsWith   = true;
    private boolean ignoreCase = true;


    /**
     * Default constructor
     */
    public SearchOptions()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SearchOptions(SearchOptions template)
    {
        super(template);

        if (template != null)
        {
            startsWith = template.getStartsWith();
            endsWith   = template.getEndsWith();
            ignoreCase = template.getIgnoreCase();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SearchOptions(MetadataSourceOptions template)
    {
        super(template);
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public SearchOptions(QueryOptions template)
    {
        super(template);
    }


    /**
     * Return whether the value start with the supplied string.
     *
     * @return boolean
     */
    public boolean getStartsWith()
    {
        return startsWith;
    }


    /**
     * Set up whether the value start with the supplied string
     *
     * @param startsWith boolean
     */
    public void setStartsWith(boolean startsWith)
    {
        this.startsWith = startsWith;
    }


    /**
     * Return whether the value ends with the supplied string.
     *
     * @return boolean
     */
    public boolean getEndsWith()
    {
        return endsWith;
    }


    /**
     * Set up whether the value ends with the supplied string.
     *
     * @param endsWith boolean
     */
    public void setEndsWith(boolean endsWith)
    {
        this.endsWith = endsWith;
    }


    /**
     * Return whether the search should ignore the case of the matching element values.
     *
     * @return boolean
     */
    public boolean getIgnoreCase()
    {
        return ignoreCase;
    }


    /**
     * Set up whether the search should ignore the case of the matching element values.
     *
     * @param ignoreCase boolean
     */
    public void setIgnoreCase(boolean ignoreCase)
    {
        this.ignoreCase = ignoreCase;
    }


    /**
     * JSON-style toString.
     *
     * @return list of properties and their values.
     */
    @Override
    public String toString()
    {
        return "SearchOptions{" +
                "startsWith=" + startsWith +
                ", endsWith=" + endsWith +
                ", ignoreCase=" + ignoreCase +
                "} " + super.toString();
    }


    /**
     * Equals method that returns true if containing properties are the same.
     *
     * @param objectToCompare object to compare
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare)
        {
            return true;
        }
        if (! (objectToCompare instanceof SearchOptions that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return endsWith == that.endsWith &&
                       Objects.equals(startsWith, that.startsWith) &&
                       Objects.equals(ignoreCase, that.ignoreCase);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), startsWith, endsWith, ignoreCase);
    }
}
