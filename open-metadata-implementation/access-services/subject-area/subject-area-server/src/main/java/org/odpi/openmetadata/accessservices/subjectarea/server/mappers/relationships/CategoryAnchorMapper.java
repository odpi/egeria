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
public class CategoryAnchorMapper extends RelationshipMapper<CategoryAnchor> {
    public static final String CATEGORY_ANCHOR = "CategoryAnchor";

    public CategoryAnchorMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    @Override
    public String getTypeName() {
        return CATEGORY_ANCHOR;
    }

    @Override
    protected CategoryAnchor getRelationshipInstance() {
        return new CategoryAnchor();
    }

}
