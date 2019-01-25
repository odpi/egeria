/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;

import java.util.List;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * CategoriesResponse is the response structure used on the Subject Area OMAS REST API calls that returns a List of
 * Categories as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CategoriesResponse extends SubjectAreaOMASAPIResponse
{
    private List<Category> categories = null;

    /**
     * Default constructor
     */
    public CategoriesResponse()
    {
        this.setResponseCategory(ResponseCategory.Categories);
    }
    public CategoriesResponse(List<Category> categories)
    {
        this();
        this.categories=categories;
    }


    /**
     * Return the Categories object.
     *
     * @return categories
     */
    public List<Category> getCategories()
    {
        return categories;
    }

    /**
     * Set up the Categories object.
     *
     * @param categories - categories object
     */
    public void setCategories(List<Category> categories)
    {
        this.categories = categories;
    }


    @Override
    public String toString()
    {
        return "CategoryResponse{" +
                "categories=" + categories +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
