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
public class TermAnchorMapper extends LineMapper<TermAnchor> {
    public static final String TERM_ANCHOR = "TermAnchor";

    public TermAnchorMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    /**
     * Get proxy1 guid.
     * The proxy has omrs type Glossary
     *
     * @param termAnchor line
     * @return guid for entity proxy 1
     */
    @Override
    protected String getProxy1Guid(TermAnchor termAnchor) {
        return termAnchor.getGlossaryGuid();
    }

    /**
     * Get proxy2 guid
     * The proxy has omrs type GlossaryTerm
     *
     * @param termAnchor for this Line
     * @return guid for entity proxy 2
     */
    @Override
    protected String getProxy2Guid(TermAnchor termAnchor) {
        return termAnchor.getTermGuid();
    }

    @Override
    public String getTypeName() {
        return TERM_ANCHOR;
    }

    @Override
    protected TermAnchor getLineInstance() {
        return new TermAnchor();
    }

    @Override
    protected void setEnd1GuidInLine(TermAnchor termAnchor, String guid) {
        termAnchor.setGlossaryGuid(guid);
    }

    @Override
    protected void setEnd2GuidInLine(TermAnchor termAnchor, String guid) {
        termAnchor.setTermGuid(guid);
    }
}