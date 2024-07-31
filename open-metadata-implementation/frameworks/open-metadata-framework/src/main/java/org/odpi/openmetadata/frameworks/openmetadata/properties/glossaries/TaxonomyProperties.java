/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.ClassificationProperties;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * TaxonomyProperties is used to classify a glossary that has the terms organized in a taxonomy.
 * This means each term is attached to one, and only one category and the categories are organized as a hierarchy
 * with a single root category.
 * <br><br>
 * Taxonomies are used as a way of organizing assets and other related metadata.  The terms in the taxonomy
 * are linked to the assets and as such they are logically categorized by the linked category.
 */
@JsonAutoDetect(getterVisibility = PUBLIC_ONLY, setterVisibility = PUBLIC_ONLY, fieldVisibility = NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class TaxonomyProperties extends ClassificationProperties
{
    private String organizingPrinciple = null;


    /**
     * Default constructor
     */
    public TaxonomyProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor for a primary key.
     *
     * @param template template object to copy.
     */
    public TaxonomyProperties(TaxonomyProperties template)
    {
        super(template);

        if (template != null)
        {
            organizingPrinciple   = template.getOrganizingPrinciple();
        }
    }


    /**
     * Return the organizing principle used to create the taxonomy in the glossary.
     *
     * @return string description
     */
    public String getOrganizingPrinciple()
    {
        return organizingPrinciple;
    }


    /**
     * Set up the organizing principle used to create the taxonomy in the glossary.
     *
     * @param organizingPrinciple string description
     */
    public void setOrganizingPrinciple(String organizingPrinciple)
    {
        this.organizingPrinciple = organizingPrinciple;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "TaxonomyProperties{" +
                "organizingPrinciple='" + organizingPrinciple + '\'' +
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
        if (! (objectToCompare instanceof TaxonomyProperties that))
        {
            return false;
        }
        if (! super.equals(objectToCompare))
        {
            return false;
        }
        return Objects.equals(organizingPrinciple, that.organizingPrinciple);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), organizingPrinciple);
    }
}
