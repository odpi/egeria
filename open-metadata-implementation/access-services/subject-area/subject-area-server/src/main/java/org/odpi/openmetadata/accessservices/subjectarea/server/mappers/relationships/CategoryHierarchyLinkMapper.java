/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.CategoryHierarchyLink;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Mapping methods to map between the categoryHierarchyLink and the equivalent omrs Relationship.
 */
public class CategoryHierarchyLinkMapper extends LineMapper
{
    private static final Logger log = LoggerFactory.getLogger( CategoryHierarchyLinkMapper.class);
    private static final String className = CategoryHierarchyLinkMapper.class.getName();
    public static final String CATEGORY_HIERARCHY_LINK = "CategoryHierarchyLink";

    public CategoryHierarchyLinkMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    /**
     * Get proxy1 guid.
     * The proxy has omrs type GlossaryCategory
     * @param line line
     * @return guid for entity proxy 1
     */
    @Override
    protected String getProxy1Guid(Line line)
    {
        CategoryHierarchyLink categoryHierarchyLink = (CategoryHierarchyLink) line;
        return categoryHierarchyLink.getSuperCategoryGuid();
    }

    /**
     * Get proxy2 guid
     * The proxy has omrs type GlossaryCategory
     * @param line for this Line
     * @return guid for entity proxy 2
     */
    @Override
    protected String getProxy2Guid(Line line)
    {
        CategoryHierarchyLink categoryHierarchyLink = (CategoryHierarchyLink) line;
        return categoryHierarchyLink.getSubCategoryGuid();
    }

    /**
     * Get the relationship type def guid.
     * @param relationship the relationship associated with the typedef whose guid is returned.
     * @return guid of the typedef
     */
    @Override
    protected String getRelationshipTypeDefGuid(Relationship relationship)
    {
        return repositoryHelper.getTypeDefByName(omrsapiHelper.getServiceName(), CATEGORY_HIERARCHY_LINK).getGUID();
    }

    /**
     * Get the TypeDefName associated with this Relationship
     * @return name of the type def
     */
    @Override
    public String getTypeName() {
        return CATEGORY_HIERARCHY_LINK;
    }
    @Override
    protected Line getLineInstance() {
        return new CategoryHierarchyLink();
    }
    @Override
    protected void setEnd1GuidInLine(Line line, String guid){
        CategoryHierarchyLink categoryHierarchyLink = (CategoryHierarchyLink)line;
        categoryHierarchyLink.setSuperCategoryGuid(guid);
    }
    @Override
    protected void setEnd2GuidInLine(Line line, String guid) {
        CategoryHierarchyLink categoryHierarchyLink = (CategoryHierarchyLink)line;
        categoryHierarchyLink.setSubCategoryGuid(guid);
    }
}
