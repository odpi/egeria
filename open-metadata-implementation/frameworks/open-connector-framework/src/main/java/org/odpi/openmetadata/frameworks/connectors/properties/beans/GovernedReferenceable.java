/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernedReferenceable adds governance relationships and classifications to a referenceable that represents
 * an asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = Asset.class, name = "Asset"),
                @JsonSubTypes.Type(value = SchemaElement.class, name = "SchemaElement"),
        })
public class GovernedReferenceable extends Referenceable
{
    protected List<Meaning>                           meanings                                = null;
    protected List<SearchKeyword>                     searchKeywords                          = null;


    /**
     * Default constructor
     */
    public GovernedReferenceable()
    {
        super();
    }


    /**
     * Copy/clone constructor.  Retrieves values from the supplied template
     *
     * @param template element to copy
     */
    public GovernedReferenceable(GovernedReferenceable template)
    {
        super(template);

        if (template != null)
        {
            meanings                                = template.getMeanings();
            searchKeywords                          = template.getSearchKeywords();
        }
    }


    /**
     * Return the assigned meanings for this metadata entity.
     *
     * @return list of meanings
     */
    public List<Meaning> getMeanings()
    {
        if (meanings == null)
        {
            return null;
        }
        else if (meanings.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(meanings);
        }
    }


    /**
     * Set up the assigned meanings for this metadata entity.
     *
     * @param meanings list of meanings
     */
    public void setMeanings(List<Meaning> meanings)
    {
        this.meanings = meanings;
    }


    /**
     * Return a list of keywords that will help an asset consumer to locate this asset.
     *
     * @return list of strings
     */
    public List<SearchKeyword> getSearchKeywords()
    {
        if (searchKeywords == null)
        {
            return null;
        }

        if (searchKeywords.isEmpty())
        {
            return null;
        }

        return searchKeywords;
    }


    /**
     * Set up a list of keywords that will help an asset consumer to locate this asset.
     *
     * @param searchKeywords list of strings
     */
    public void setSearchKeywords(List<SearchKeyword> searchKeywords)
    {
        this.searchKeywords = searchKeywords;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "GovernedReferenceable{" +
                       "URL='" + getURL() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", status=" + getStatus() +
                       ", type=" + getType() +
                       ", origin=" + getOrigin() +
                       ", versions=" + getVersions() +
                       ", GUID='" + getGUID() + '\'' +
                       ", classifications=" + getClassifications() +
                       ", meanings=" + meanings +
                       ", searchKeywords=" + searchKeywords +
                       ", headerVersion=" + getHeaderVersion() +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
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
        GovernedReferenceable that = (GovernedReferenceable) objectToCompare;
        return  Objects.equals(meanings, that.meanings) &&
                Objects.equals(searchKeywords, that.searchKeywords);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), meanings, searchKeywords);
    }
}