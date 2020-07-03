/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.CategoryAnchor;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;


/**
 * Mapping methods to map between the categoryAnchor and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class CategoryAnchorMapper extends LineMapper<CategoryAnchor> {
    public static final String CATEGORY_ANCHOR = "CategoryAnchor";

    public CategoryAnchorMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    /**
     * Get proxy1 guid.
     * The proxy has omrs type Glossary
     *
     * @param categoryAnchor line
     * @return guid for entity proxy 1
     */
    @Override
    protected String getProxy1Guid(CategoryAnchor categoryAnchor) {
        return categoryAnchor.getGlossaryGuid();
    }

    /**
     * Get proxy2 guid
     * The proxy has omrs type GlossaryCategory
     *
     * @param categoryAnchor for this Line
     * @return guid for entity proxy 2
     */
    @Override
    protected String getProxy2Guid(CategoryAnchor categoryAnchor) {
        return categoryAnchor.getCategoryGuid();
    }

    @Override
    public String getTypeName() {
        return CATEGORY_ANCHOR;
    }

    @Override
    protected CategoryAnchor getLineInstance() {
        return new CategoryAnchor();
    }

    @Override
    protected void setEnd1GuidInLine(CategoryAnchor categoryAnchor, String guid) {
        categoryAnchor.setGlossaryGuid(guid);
    }

    @Override
    protected void setEnd2GuidInLine(CategoryAnchor categoryAnchor, String guid) {
        categoryAnchor.setCategoryGuid(guid);
    }
}
