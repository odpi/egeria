/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.RelatedTerm;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Mapping methods to map between the relatedTerm and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class RelatedTermMapper extends LineMapper<RelatedTerm> {
    public static final String RELATED_TERM = "RelatedTerm";

    public RelatedTermMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    /**
     * Map the supplied Line to omrs InstanceProperties.
     *
     * @param relatedTerm               supplied line
     * @param instanceProperties equivalent instance properties to the Line
     */
    @Override
    protected void mapLineToInstanceProperties(RelatedTerm relatedTerm, InstanceProperties instanceProperties) {
        if (relatedTerm.getDescription() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, relatedTerm.getDescription(), "description");
        }
        if (relatedTerm.getExpression() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, relatedTerm.getExpression(), "expression");
        }
        if (relatedTerm.getSteward() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, relatedTerm.getSteward(), "steward");
        }
        if (relatedTerm.getSource() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, relatedTerm.getSource(), "source");
        }
        if (relatedTerm.getStatus() != null) {
            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
            enumPropertyValue.setOrdinal(relatedTerm.getStatus().getOrdinal());
            instanceProperties.setProperty("status", enumPropertyValue);
        }
    }

    /**
     * Map a primitive omrs property to the relatedTerm object.
     *
     * @param relatedTerm         the glossary to be updated
     * @param propertyName the omrs property name
     * @param value        the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the Line, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToLine(RelatedTerm relatedTerm, String propertyName, Object value) {
        String stringValue = (String) value;
        boolean foundProperty = false;
        if (propertyName.equals("description")) {
            relatedTerm.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("expression")) {
            relatedTerm.setExpression(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("steward")) {
            relatedTerm.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("source")) {
            relatedTerm.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    protected boolean mapEnumToLine(RelatedTerm relatedTerm, String propertyName, EnumPropertyValue enumPropertyValue) {
        boolean foundProperty = false;
        if (propertyName.equals("status")) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            relatedTerm.setStatus(status);
            foundProperty = true;
        }
        return foundProperty;
    }

    /**
     * Get proxy1 guid.
     * The proxy has omrs type GlossaryTerm
     *
     * @param relatedTerm line
     * @return guid for entity proxy 1
     */
    @Override
    protected String getProxy1Guid(RelatedTerm relatedTerm) {
        return relatedTerm.getRelatedTerm1Guid();
    }

    /**
     * Get proxy2 guid
     * The proxy has omrs type GlossaryTerm
     *
     * @param relatedTerm for this Line
     * @return guid for entity proxy 2
     */
    @Override
    protected String getProxy2Guid(RelatedTerm relatedTerm) {
        return relatedTerm.getRelatedTerm2Guid();
    }

    @Override
    public String getTypeName() {
        return RELATED_TERM;
    }

    @Override
    protected RelatedTerm getLineInstance() {
        return new RelatedTerm();
    }

    @Override
    protected void setEnd1GuidInLine(RelatedTerm relatedTerm, String guid) {
        relatedTerm.setRelatedTerm1Guid(guid);
    }

    @Override
    protected void setEnd2GuidInLine(RelatedTerm relatedTerm, String guid) {
        relatedTerm.setRelatedTerm2Guid(guid);
    }
}
