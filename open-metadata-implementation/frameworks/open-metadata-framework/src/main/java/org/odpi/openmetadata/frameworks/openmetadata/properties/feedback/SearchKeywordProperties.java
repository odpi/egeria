/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.properties.feedback;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.OpenMetadataRootProperties;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * SearchKeywordProperties stores information about a search keyword tag connected to a referenceable.
 * Search keywords are typically anchored to an element so that they are included in the search scope for
 * the referenceable.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SearchKeywordProperties extends OpenMetadataRootProperties
{
    private String displayName = null;
    private String description = null;


    /**
     * Default constructor
     */
    public SearchKeywordProperties()
    {
        super();
        super.setTypeName(OpenMetadataType.SEARCH_KEYWORD.typeName);
    }


    /**
     * Copy/clone constructor.
     *
     * @param template element to copy
     */
    public SearchKeywordProperties(SearchKeywordProperties template)
    {
        super(template);

        if (template != null)
        {
            displayName = template.getDisplayName();
            description = template.getDescription();
        }
    }


    /**
     * Return the name of the keyword.
     *
     * @return String tag name
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Set up the name of the keyword.
     *
     * @param displayName String tag name
     */
    public void setDisplayName(String displayName)
    {
        this.displayName = displayName;
    }


    /**
     * Return the optional keyword description.
     *
     * @return String tag description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the the optional keyword description.
     *
     * @param description  tag description
     */
    public void setDescription(String description) {
        this.description = description;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SearchKeywordProperties{" +
                "displayName='" + displayName + '\'' +
                ", description='" + description + '\'' +
                "} " + super.toString();
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
        if (!(objectToCompare instanceof SearchKeywordProperties that))
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(getDisplayName(), that.getDisplayName()) &&
                Objects.equals(getDescription(), that.getDescription());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), displayName, description);
    }
}