/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.TermAnchor;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;


/**
 * Mapping methods to map between the termAnchor and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class TermAnchorMapper extends RelationshipMapper<TermAnchor> {
    public static final String TERM_ANCHOR = "TermAnchor";

    public TermAnchorMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    @Override
    public String getTypeName() {
        return TERM_ANCHOR;
    }

    @Override
    protected TermAnchor getRelationshipInstance() {
        return new TermAnchor();
    }

}