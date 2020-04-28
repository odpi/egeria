/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.responses;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.CategoryAnchor;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * CategoryAnchorResponse is the response structure used on the Subject Area OMAS REST API calls that returns a
 * CategoryAnchorRelationship object as a response.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CategoryAnchorRelationshipResponse extends SubjectAreaOMASAPIResponse
{
    private CategoryAnchor categoryAnchorRelationship = null;

    /**
     * Default constructor
     */
    public CategoryAnchorRelationshipResponse()
    {
        this.setResponseCategory(ResponseCategory.CategoryAnchorRelationship);
    }
    public CategoryAnchorRelationshipResponse(CategoryAnchor categoryAnchorRelationship)
    {
        this();
        this.categoryAnchorRelationship = categoryAnchorRelationship;
    }

    /**
     * Return the CategoryAnchorRelationship object.
     *
     * @return CategoryAnchorRelationship
     */
    public CategoryAnchor getCategoryAnchorRelationship()
    {
        return categoryAnchorRelationship;
    }

    public void setCategoryAnchorRelationship(CategoryAnchor CategoryAnchorRelationship)
    {
        this.categoryAnchorRelationship = CategoryAnchorRelationship;
    }


    @Override
    public String toString()
    {
        return "CategoryAnchorResponse{" +
                "categoryAnchorRelationship=" + categoryAnchorRelationship +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
