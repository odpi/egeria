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
public class AntonymMapper extends RelationshipMapper<Antonym> {
    public static final String ANTONYM = "Antonym";

    public AntonymMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }


    /**
     * Map the supplied relationship to omrs InstanceProperties.
     *
     * @param antonym            supplied relationship
     * @param instanceProperties equivalent instance properties to the relationship
     */
    @Override
    protected void mapRelationshipToInstanceProperties(Antonym antonym, InstanceProperties instanceProperties) {
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
     * @param antonym      the omas relationship to be updated
     * @param propertyName the omrs property name
     * @param value        the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the relationship, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToRelationship(Antonym antonym, String propertyName, Object value) {
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
    protected boolean mapEnumToRelationship(Antonym antonym, String propertyName, EnumPropertyValue enumPropertyValue) {
        boolean foundProperty = false;
        if (propertyName.equals("status")) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            antonym.setStatus(status);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    public String getTypeName() {
        return ANTONYM;
    }

    @Override
    protected Antonym getRelationshipInstance() {
        return new Antonym();
    }

}
