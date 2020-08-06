/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Antonym;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;


/**
 * Mapping methods to map between the antonym and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class AntonymMapper extends LineMapper<Antonym> {
    public static final String ANTONYM = "Antonym";

    public AntonymMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }


    /**
     * Map the supplied Line to omrs InstanceProperties.
     *
     * @param antonym            supplied line
     * @param instanceProperties equivalent instance properties to the Line
     */
    @Override
    protected void mapLineToInstanceProperties(Antonym antonym, InstanceProperties instanceProperties) {
        if (antonym.getDescription() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, antonym.getDescription(), "description");
        }
        if (antonym.getExpression() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, antonym.getExpression(), "expression");
        }
        if (antonym.getSteward() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, antonym.getSteward(), "steward");
        }
        if (antonym.getSource() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, antonym.getSource(), "source");
        }
        if (antonym.getStatus() != null) {
            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
            enumPropertyValue.setOrdinal(antonym.getStatus().getOrdinal());
            instanceProperties.setProperty("status", enumPropertyValue);
        }
    }

    /**
     * Map a primitive omrs property to the antonym object.
     *
     * @param antonym      the glossary to be updated
     * @param propertyName the omrs property name
     * @param value        the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the Line, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToLine(Antonym antonym, String propertyName, Object value) {
        String stringValue = (String) value;
        boolean foundProperty = false;
        if (propertyName.equals("description")) {
            antonym.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("expression")) {
            antonym.setExpression(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("steward")) {
            antonym.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("source")) {
            antonym.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    protected boolean mapEnumToLine(Antonym antonym, String propertyName, EnumPropertyValue enumPropertyValue) {
        boolean foundProperty = false;
        if (propertyName.equals("status")) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            antonym.setStatus(status);
            foundProperty = true;
        }
        return foundProperty;
    }

    /**
     * Get proxy1 guid.
     * The proxy has omrs type GlossaryTerm
     *
     * @param antonym line
     * @return guid for entity proxy 1
     */
    @Override
    protected String getProxy1Guid(Antonym antonym) {
        return antonym.getAntonym1Guid();
    }

    /**
     * Get proxy2 guid
     * The proxy has omrs type GlossaryTerm
     *
     * @param antonym for this Line
     * @return guid for entity proxy 2
     */
    @Override
    protected String getProxy2Guid(Antonym antonym) {
        return antonym.getAntonym2Guid();
    }

    @Override
    public String getTypeName() {
        return ANTONYM;
    }

    @Override
    protected Antonym getLineInstance() {
        return new Antonym();
    }

    @Override
    protected void setEnd1GuidInLine(Antonym line, String guid) {
        line.setAntonym1Guid(guid);
    }

    @Override
    protected void setEnd2GuidInLine(Antonym line, String guid) {
        line.setAntonym2Guid(guid);
    }
}
