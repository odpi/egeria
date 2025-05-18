/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.properties.MetadataCorrelationHeader;
import org.odpi.openmetadata.frameworks.openmetadata.properties.glossaries.GlossaryCategoryProperties;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ChildCategoryElement contains the properties and header for a summary element for a category and its
 * sub categories.  The effect is to build a category hierarchy.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class ChildCategoryElement extends RelatedMetadataElementSummary
{
    private List<ChildCategoryElement> childCategories = null;


    /**
     * Default constructor
     */
    public ChildCategoryElement()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ChildCategoryElement(ChildCategoryElement template)
    {
        super(template);

        if (template != null)
        {
            childCategories = template.getChildCategories();
        }
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public ChildCategoryElement(RelatedMetadataElementSummary template)
    {
        super(template);
    }


    /**
     * Return the optional child categories.
     *
     * @return categories
     */
    public List<ChildCategoryElement> getChildCategories()
    {
        return childCategories;
    }


    /**
     * Set up the optional child categories.
     *
     * @param childCategories categories
     */
    public void setChildCategories(List<ChildCategoryElement> childCategories)
    {
        this.childCategories = childCategories;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "ChildCategoryElement{" +
                "childCategories=" + childCategories +
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
        ChildCategoryElement that = (ChildCategoryElement) objectToCompare;
        return Objects.equals(childCategories, that.childCategories);
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), childCategories);
    }
}
