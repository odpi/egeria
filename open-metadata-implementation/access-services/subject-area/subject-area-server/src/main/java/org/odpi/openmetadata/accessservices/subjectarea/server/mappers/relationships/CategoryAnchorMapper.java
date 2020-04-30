/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.CategoryAnchor;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Mapping methods to map between the categoryAnchor and the equivalent omrs Relationship.
 */
public class CategoryAnchorMapper extends LineMapper
{
    private static final Logger log = LoggerFactory.getLogger( CategoryAnchorMapper.class);
    private static final String className = CategoryAnchorMapper.class.getName();
    public static final String CATEGORY_ANCHOR = "CategoryAnchor";

    public CategoryAnchorMapper(OMRSAPIHelper omrsapiHelper) {
         super(omrsapiHelper);
    }

    /**
     * Get proxy1 guid.
     * The proxy has omrs type Glossary
     * @param line line
     * @return guid for entity proxy 1
     */
    @Override
    protected String getProxy1Guid(Line line)
    {
        CategoryAnchor categoryAnchor = (CategoryAnchor) line;
        return categoryAnchor.getGlossaryGuid();
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
        CategoryAnchor categoryAnchor = (CategoryAnchor) line;
        return categoryAnchor.getCategoryGuid();
    }

    /**
     * Get the relationship type def guid.
     * @param relationship the relationship associated with the typedef whose guid is returned.
     * @return guid of the typedef
     */
    @Override
    protected String getRelationshipTypeDefGuid(Relationship relationship)
    {
        return repositoryHelper.getTypeDefByName(omrsapiHelper.getServiceName(), CATEGORY_ANCHOR).getGUID();
    }
    @Override
    public String getTypeName() {
        return CATEGORY_ANCHOR;
    }
    @Override
    protected Line getLineInstance() {
        return new CategoryAnchor();
    }
    @Override
    protected void setEnd1GuidInLine(Line line, String guid){
        CategoryAnchor categoryAnchorRelationship = (CategoryAnchor)line;
        categoryAnchorRelationship.setGlossaryGuid(guid);
    }
    @Override
    protected void setEnd2GuidInLine(Line line, String guid) {
        CategoryAnchor categoryAnchorRelationship = (CategoryAnchor)line;
        categoryAnchorRelationship.setCategoryGuid(guid);
    }
}
