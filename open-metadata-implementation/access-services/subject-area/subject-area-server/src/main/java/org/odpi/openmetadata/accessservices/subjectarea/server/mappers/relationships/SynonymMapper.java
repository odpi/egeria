/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Synonym;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;


/**
 * Mapping methods to map between the synonym and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class SynonymMapper extends LineMapper<Synonym> {
    public static final String SYNONYM = "Synonym";

    public SynonymMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    /**
     * Map the supplied Line to omrs InstanceProperties.
     *
     * @param synonym               supplied line
     * @param instanceProperties equivalent instance properties to the Line
     */
    @Override
    protected void mapLineToInstanceProperties(Synonym synonym, InstanceProperties instanceProperties) {
        if (synonym.getDescription() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, synonym.getDescription(), "description");
        }
        if (synonym.getExpression() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, synonym.getExpression(), "expression");
        }
        if (synonym.getSteward() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, synonym.getSteward(), "steward");
        }
        if (synonym.getSource() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, synonym.getSource(), "source");
        }
        if (synonym.getStatus() != null) {
            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
            enumPropertyValue.setOrdinal(synonym.getStatus().getOrdinal());
            instanceProperties.setProperty("status", enumPropertyValue);
        }
    }

    /**
     * Map a primitive omrs property to the synonym object.
     *
     * @param synonym         the glossary to be updated
     * @param propertyName the omrs property name
     * @param value        the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the Line, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToLine(Synonym synonym, String propertyName, Object value) {
        String stringValue = (String) value;
        boolean foundProperty = false;
        if (propertyName.equals("description")) {
            synonym.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("expression")) {
            synonym.setExpression(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("steward")) {
            synonym.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("source")) {
            synonym.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    protected boolean mapEnumToLine(Synonym synonym, String propertyName, EnumPropertyValue enumPropertyValue) {
        boolean foundProperty = false;
        if (propertyName.equals("status")) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            synonym.setStatus(status);
            foundProperty = true;
        }
        return foundProperty;
    }

    /**
     * Get proxy1 guid.
     * The proxy has omrs type GlossaryTerm
     *
     * @param synonym line
     * @return guid for entity proxy 1
     */
    @Override
    protected String getProxy1Guid(Synonym synonym) {
        return synonym.getSynonym1Guid();
    }

    /**
     * Get proxy2 guid
     * The proxy has omrs type GlossaryTerm
     *
     * @param synonym for this Line
     * @return guid for entity proxy 2
     */
    @Override
    protected String getProxy2Guid(Synonym synonym) {
        return synonym.getSynonym2Guid();
    }

    @Override
    public String getTypeName() {
        return SYNONYM;
    }

    @Override
    protected Synonym getLineInstance() {
        return new Synonym();
    }

    @Override
    protected void setEnd1GuidInLine(Synonym synonym, String guid) {
        synonym.setSynonym1Guid(guid);
    }

    @Override
    protected void setEnd2GuidInLine(Synonym synonym, String guid) {
        synonym.setSynonym2Guid(guid);
    }
}
