/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.PreferredTerm;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Mapping methods to map between the preferredTerm and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class PreferredTermMapper extends LineMapper<PreferredTerm> {
    public static final String PREFERRED_TERM = "PreferredTerm";

    public PreferredTermMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }


    /**
     * Map the supplied Line to omrs InstanceProperties.
     *
     * @param preferredTerm      supplied line
     * @param instanceProperties equivalent instance properties to the Line
     */
    @Override
    protected void mapLineToInstanceProperties(PreferredTerm preferredTerm, InstanceProperties instanceProperties) {
        if (preferredTerm.getDescription() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, preferredTerm.getDescription(), "description");
        }
        if (preferredTerm.getExpression() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, preferredTerm.getExpression(), "expression");
        }
        if (preferredTerm.getSteward() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, preferredTerm.getSteward(), "steward");
        }
        if (preferredTerm.getSource() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, preferredTerm.getSource(), "source");
        }
        if (preferredTerm.getStatus() != null) {
            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
            enumPropertyValue.setOrdinal(preferredTerm.getStatus().getOrdinal());
            instanceProperties.setProperty("status", enumPropertyValue);
        }
    }

    /**
     * Map a primitive omrs property to the preferredTerm object.
     *
     * @param preferredTerm the glossary to be updated
     * @param propertyName  the omrs property name
     * @param value         the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the Line, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToLine(PreferredTerm preferredTerm, String propertyName, Object value) {
        String stringValue = (String) value;
        boolean foundProperty = false;
        if (propertyName.equals("description")) {
            preferredTerm.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("expression")) {
            preferredTerm.setExpression(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("steward")) {
            preferredTerm.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("source")) {
            preferredTerm.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    protected boolean mapEnumToLine(PreferredTerm preferredTerm, String propertyName, EnumPropertyValue enumPropertyValue) {
        boolean foundProperty = false;
        if (propertyName.equals("status")) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            preferredTerm.setStatus(status);
            foundProperty = true;
        }
        return foundProperty;
    }

    /**
     * Get proxy1 guid.
     * The proxy has omrs type GlossaryTerm
     *
     * @param preferredTerm line
     * @return guid for entity proxy 1
     */
    @Override
    protected String getProxy1Guid(PreferredTerm preferredTerm) {
        return preferredTerm.getAlternateTermGuid();
    }

    /**
     * Get proxy2 guid
     * The proxy has omrs type GlossaryTerm
     *
     * @param preferredTerm for this Line
     * @return guid for entity proxy 2
     */
    @Override
    protected String getProxy2Guid(PreferredTerm preferredTerm) {
        return preferredTerm.getPreferredTermGuid();
    }

    @Override
    public String getTypeName() {
        return PREFERRED_TERM;
    }

    @Override
    protected PreferredTerm getLineInstance() {
        return new PreferredTerm();
    }

    @Override
    protected void setEnd1GuidInLine(PreferredTerm preferredTerm, String guid) {
        preferredTerm.setAlternateTermGuid(guid);
    }

    @Override
    protected void setEnd2GuidInLine(PreferredTerm preferredTerm, String guid) {
        preferredTerm.setPreferredTermGuid(guid);
    }
}
