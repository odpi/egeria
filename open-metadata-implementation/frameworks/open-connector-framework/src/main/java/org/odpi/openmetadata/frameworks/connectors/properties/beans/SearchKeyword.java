/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * SearchKeyword stores information about a keyword connected to an asset.
 * SearchKeyword provide informal classifications to assets
 * and can be added at any time - typically by the asset owner or curator.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class SearchKeyword extends ElementBase
{
    private static final long     serialVersionUID = 1L;
    
    protected String  name         = null;
    protected String  description  = null;


    /**
     * Default constructor
     */
    public SearchKeyword()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template element to copy
     */
    public SearchKeyword(SearchKeyword template)
    {
        super(template);

        if (template != null)
        {
            name = template.getName();
            description = template.getDescription();
        }
    }
    
    /**
     * Return the name of the keyword.  It is not valid to have a keyword with no name.  However, there is a point where
     * the keyword object is created and the keyword name not yet set, so null is a possible response.
     *
     * @return String keyword name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Set up the name of the keyword.  It is not valid to have a keyword with no name.  However, there is a point where
     * the keyword object is created and the keyword name not yet set, so null is a possible response.
     *
     * @param name String keyword name
     */
    public void setName(String name)
    {
        this.name = name;
    }


    /**
     * Return the keyword description null means no description is available.
     *
     * @return String keyword description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Set up the keyword description null means no description is available.
     *
     * @param keywordDescription  keyword description
     */
    public void setDescription(String keywordDescription)
    {
        this.description = keywordDescription;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "SearchKeyword{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", type=" + getType() +
                ", GUID='" + getGUID() + '\'' +
                ", URL='" + getURL() + '\'' +
                ", classifications=" + getClassifications() +
                ", extendedProperties=" + getExtendedProperties() +
                ", headerVersion=" + getHeaderVersion() +
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
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        SearchKeyword that = (SearchKeyword) objectToCompare;
        return Objects.equals(name, that.name) &&
                Objects.equals(description, that.description);
    }


    /**
     * Return code value representing the contents of this object.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), name, description);
    }
}