/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.CategoryHierarchyLink;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;

/**
 * Mapping methods to map between the categoryHierarchyLink and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class CategoryHierarchyLinkMapper extends LineMapper<CategoryHierarchyLink> {
    public static final String CATEGORY_HIERARCHY_LINK = "CategoryHierarchyLink";

    public CategoryHierarchyLinkMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    /**
     * Get proxy1 guid.
     * The proxy has omrs type GlossaryCategory
     *
     * @param categoryHierarchyLink line
     * @return guid for entity proxy 1
     */
    @Override
    protected String getProxy1Guid(CategoryHierarchyLink categoryHierarchyLink) {
        return categoryHierarchyLink.getSuperCategoryGuid();
    }

    /**
     * Get proxy2 guid
     * The proxy has omrs type GlossaryCategory
     *
     * @param categoryHierarchyLink for this Line
     * @return guid for entity proxy 2
     */
    @Override
    protected String getProxy2Guid(CategoryHierarchyLink categoryHierarchyLink) {
        return categoryHierarchyLink.getSubCategoryGuid();
    }

    /**
     * Get the TypeDefName associated with this Relationship
     *
     * @return name of the type def
     */
    @Override
    public String getTypeName() {
        return CATEGORY_HIERARCHY_LINK;
    }

    @Override
    protected CategoryHierarchyLink getLineInstance() {
        return new CategoryHierarchyLink();
    }

    @Override
    protected void setEnd1GuidInLine(CategoryHierarchyLink categoryHierarchyLink, String guid) {
        categoryHierarchyLink.setSuperCategoryGuid(guid);
    }

    @Override
    protected void setEnd2GuidInLine(CategoryHierarchyLink categoryHierarchyLink, String guid) {
        categoryHierarchyLink.setSubCategoryGuid(guid);
    }
}
