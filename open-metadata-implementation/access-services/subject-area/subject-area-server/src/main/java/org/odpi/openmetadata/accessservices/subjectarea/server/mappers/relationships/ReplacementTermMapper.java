/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.ReplacementTerm;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;


/**
 * Mapping methods to map between the replacementTerm and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class ReplacementTermMapper extends RelationshipMapper<ReplacementTerm> {
    public static final String REPLACEMENT_TERM = "ReplacementTerm";

    public ReplacementTermMapper(OpenMetadataAPIGenericHandler genericHandler){
        super(genericHandler);
    }

    /**
     * Map the supplied relationship to omrs InstanceProperties.
     *
     * @param replacementTerm       supplied relationship
     * @param properties equivalent instance properties to the relationship
     */
    @Override
    protected void mapRelationshipToInstanceProperties(ReplacementTerm replacementTerm, InstanceProperties properties) {
        if (replacementTerm.getDescription() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(properties, replacementTerm.getDescription(), OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME);
        }
        if (replacementTerm.getExpression() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(properties, replacementTerm.getExpression(), OpenMetadataAPIMapper.EXPRESSION_PROPERTY_NAME);
        }
        if (replacementTerm.getSteward() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(properties, replacementTerm.getSteward(), OpenMetadataAPIMapper.STEWARD_PROPERTY_NAME);
        }
        if (replacementTerm.getSource() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(properties, replacementTerm.getSource(), OpenMetadataAPIMapper.SOURCE_PROPERTY_NAME);
        }
        if (replacementTerm.getStatus() != null) {
            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
            enumPropertyValue.setOrdinal(replacementTerm.getStatus().getOrdinal());
            properties.setProperty(OpenMetadataAPIMapper.STATUS_PROPERTY_NAME, enumPropertyValue);
        }
    }

    /**
     * Map a primitive omrs property to the replacementTerm object.
     *
     * @param replacementTerm         the omas relationship to be updated
     * @param propertyName the omrs property name
     * @param value        the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the relationship, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToRelationship(ReplacementTerm replacementTerm, String propertyName, Object value) {
        String stringValue = (String) value;
        boolean foundProperty = false;
        if (propertyName.equals(OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME)) {
            replacementTerm.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataAPIMapper.EXPRESSION_PROPERTY_NAME)) {
            replacementTerm.setExpression(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataAPIMapper.STEWARD_PROPERTY_NAME)) {
            replacementTerm.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataAPIMapper.SOURCE_PROPERTY_NAME)) {
            replacementTerm.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    protected boolean mapEnumToRelationship(ReplacementTerm replacementTerm, String propertyName, EnumPropertyValue enumPropertyValue) {
        boolean foundProperty = false;
        if (propertyName.equals(OpenMetadataAPIMapper.STATUS_PROPERTY_NAME)) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            replacementTerm.setStatus(status);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    public String getTypeName() {
        return REPLACEMENT_TERM;
    }

    @Override
    protected ReplacementTerm getRelationshipInstance() {
        return new ReplacementTerm();
    }

}
