/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.internalresponse;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.nodesummary.IconSummary;
import org.odpi.openmetadata.accessservices.subjectarea.responses.ResponseCategory;
import org.odpi.openmetadata.accessservices.subjectarea.responses.SubjectAreaOMASAPIResponse;

import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * IconSummarySetResponse is a response structure used internally by the Subject Area OMAS REST API
 * to represent an omrs IconSummarySet
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class IconSummarySetResponse extends SubjectAreaOMASAPIResponse
{
    private Set<IconSummary> iconSummarySet= null;

    /**
     * Default constructor
     */
    public IconSummarySetResponse() {
        this.setResponseCategory(ResponseCategory.IconSummarySet);
    }
    public IconSummarySetResponse(Set<IconSummary> iconSummarySet)
    {
        this();
        this.iconSummarySet = iconSummarySet;
    }


    /**
     * Return the IconSummarySet object.
     *
     * @return iconSummarySet
     */
    public Set<IconSummary> getIconSummarySet()
    {
        return iconSummarySet;
    }

    /**
     * Set up the IconSummarySet object.
     *
     * @param iconSummarySet - iconSummarySet object
     */
    public void setIconSummarySet(Set<IconSummary> iconSummarySet)
    {
        this.iconSummarySet = iconSummarySet;
    }


    @Override
    public String toString()
    {
        return "Set<IconSummary>Response{" +
                "iconSummarySet=" + iconSummarySet +
                ", relatedHTTPCode=" + relatedHTTPCode +
                '}';
    }
}
