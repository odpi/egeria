/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.PreferredTerm;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;


/**
 * Mapping methods to map between the preferredTerm and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class PreferredTermMapper extends RelationshipMapper<PreferredTerm> {
    public static final String PREFERRED_TERM = "PreferredTerm";

    public PreferredTermMapper(OpenMetadataAPIGenericHandler genericHandler){
        super(genericHandler);
    }


    /**
     * Map the supplied relationship to omrs InstanceProperties.
     *
     * @param preferredTerm      supplied relationship
     * @param instanceProperties equivalent instance properties to the relationship
     */
    @Override
    protected void mapRelationshipToInstanceProperties(PreferredTerm preferredTerm, InstanceProperties instanceProperties) {
        if (preferredTerm.getDescription() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, preferredTerm.getDescription(), OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME);
        }
        if (preferredTerm.getExpression() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, preferredTerm.getExpression(), OpenMetadataAPIMapper.EXPRESSION_PROPERTY_NAME);
        }
        if (preferredTerm.getSteward() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, preferredTerm.getSteward(), OpenMetadataAPIMapper.STEWARD_PROPERTY_NAME);
        }
        if (preferredTerm.getSource() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, preferredTerm.getSource(), OpenMetadataAPIMapper.SOURCE_PROPERTY_NAME);
        }
        if (preferredTerm.getStatus() != null) {
            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
            enumPropertyValue.setOrdinal(preferredTerm.getStatus().getOrdinal());
            instanceProperties.setProperty(OpenMetadataAPIMapper.STATUS_PROPERTY_NAME, enumPropertyValue);
        }
    }

    /**
     * Map a primitive omrs property to the preferredTerm object.
     *
     * @param preferredTerm the omas relationship to be updated
     * @param propertyName  the omrs property name
     * @param value         the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the relationship, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToRelationship(PreferredTerm preferredTerm, String propertyName, Object value) {
        String stringValue = (String) value;
        boolean foundProperty = false;
        if (propertyName.equals(OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME)) {
            preferredTerm.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataAPIMapper.EXPRESSION_PROPERTY_NAME)) {
            preferredTerm.setExpression(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataAPIMapper.STEWARD_PROPERTY_NAME)) {
            preferredTerm.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataAPIMapper.SOURCE_PROPERTY_NAME)) {
            preferredTerm.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    protected boolean mapEnumToRelationship(PreferredTerm preferredTerm, String propertyName, EnumPropertyValue enumPropertyValue) {
        boolean foundProperty = false;
        if (propertyName.equals(OpenMetadataAPIMapper.STATUS_PROPERTY_NAME)) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            preferredTerm.setStatus(status);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    public String getTypeName() {
        return PREFERRED_TERM;
    }

    @Override
    protected PreferredTerm getRelationshipInstance() {
        return new PreferredTerm();
    }

}
