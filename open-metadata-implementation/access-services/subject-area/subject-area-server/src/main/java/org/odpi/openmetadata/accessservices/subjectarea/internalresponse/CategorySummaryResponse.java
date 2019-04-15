/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.internalresponse;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.CategorySummary;
import org.odpi.openmetadata.accessservices.subjectarea.responses.ResponseCategory;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * CategorySummaryResponse is a response structure used internally by the Subject Area OMAS REST API
 * to represent an omrs CategorySummary
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class CategorySummaryResponse extends SubjectAreaOMASAPIResponse
{
    private CategorySummary categorySummary= null;

    /**
     * Default constructor
     */
    public CategorySummaryResponse() {
        this.setResponseCategory(ResponseCategory.CategorySummary);
    }
    public CategorySummaryResponse(CategorySummary categorySummary)
    {
        this();
        this.categorySummary = categorySummary;
    }


    /**
     * Return the CategorySummary object.
     *
     * @return categorySummary
     */
    public CategorySummary getCategorySummary()
    {
        return categorySummary;
    }

    /**
     * Set up the CategorySummary object.
     *
     * @param categorySummary - categorySummary object
     */
    public void setCategorySummary(CategorySummary categorySummary)
    {
        this.categorySummary = categorySummary;
    }


    @Override
    public String toString()
    {
        return "CategorySummaryResponse{" +
                "categorySummary=" + categorySummary +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
