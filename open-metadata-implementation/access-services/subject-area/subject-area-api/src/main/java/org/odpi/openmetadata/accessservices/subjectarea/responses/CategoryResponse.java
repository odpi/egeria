/* SPDX-License-Identifier: Apache-2.0 */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * CategoryResponse is the response structure used on the Subject Area OMAS REST API calls that returns a
 * Category object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CategoryResponse extends SubjectAreaOMASAPIResponse
{
    private Category category = null;

    /**
     * Default constructor
     */
    public CategoryResponse() {
        this.setResponseCategory(ResponseCategory.Category);
    }
    public CategoryResponse(Category category)
    {
        this();
        this.category=category;
    }


    /**
     * Return the Category object.
     *
     * @return category
     */
    public Category getCategory()
    {
        return category;
    }

    /**
     * Set up the Category object.
     *
     * @param category - category object
     */
    public void setCategory(Category category)
    {
        this.category = category;
    }


    @Override
    public String toString()
    {
        return "CategoryResponse{" +
                "category=" + category +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
