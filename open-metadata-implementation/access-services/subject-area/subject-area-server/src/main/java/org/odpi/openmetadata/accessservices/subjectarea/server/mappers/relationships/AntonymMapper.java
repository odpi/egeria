/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Antonym;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;


/**
 * Mapping methods to map between the antonym and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class AntonymMapper extends RelationshipMapper<Antonym> {
    public static final String ANTONYM = "Antonym";

    public AntonymMapper(OpenMetadataAPIGenericHandler genericHandler){
        super(genericHandler);
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
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, antonym.getDescription(), OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME);
        }
        if (antonym.getExpression() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, antonym.getExpression(), OpenMetadataAPIMapper.EXPRESSION_PROPERTY_NAME);
        }
        if (antonym.getSteward() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, antonym.getSteward(), OpenMetadataAPIMapper.STEWARD_PROPERTY_NAME);
        }
        if (antonym.getSource() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, antonym.getSource(), OpenMetadataAPIMapper.SOURCE_PROPERTY_NAME);
        }
        if (antonym.getStatus() != null) {
            SubjectAreaUtils.setStatusPropertyInInstanceProperties(instanceProperties, antonym.getStatus(), OpenMetadataAPIMapper.STATUS_PROPERTY_NAME);
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
        if (propertyName.equals(OpenMetadataAPIMapper.EXPRESSION_PROPERTY_NAME)) {
            antonym.setExpression(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataAPIMapper.STEWARD_PROPERTY_NAME)) {
            antonym.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataAPIMapper.SOURCE_PROPERTY_NAME)) {
            antonym.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    protected boolean mapEnumToRelationship(Antonym antonym, String propertyName, EnumPropertyValue enumPropertyValue) {
        boolean foundProperty = false;
        if (propertyName.equals(OpenMetadataAPIMapper.STATUS_PROPERTY_NAME)) {
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
