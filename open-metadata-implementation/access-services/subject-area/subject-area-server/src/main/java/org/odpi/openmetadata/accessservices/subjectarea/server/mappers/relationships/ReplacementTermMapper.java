/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.ReplacementTerm;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
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
            SubjectAreaUtils.setStringPropertyInInstanceProperties(properties, replacementTerm.getDescription(), OpenMetadataProperty.DESCRIPTION.name);
        }
        if (replacementTerm.getExpression() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(properties, replacementTerm.getExpression(), OpenMetadataType.EXPRESSION_PROPERTY_NAME);
        }
        if (replacementTerm.getSteward() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(properties, replacementTerm.getSteward(), OpenMetadataType.STEWARD_PROPERTY_NAME);
        }
        if (replacementTerm.getSource() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(properties, replacementTerm.getSource(), OpenMetadataProperty.SOURCE.name);
        }
        if (replacementTerm.getStatus() != null) {
            SubjectAreaUtils.setStatusPropertyInInstanceProperties(properties, replacementTerm.getStatus(), OpenMetadataProperty.TERM_RELATIONSHIP_STATUS.name);
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
        if (propertyName.equals(OpenMetadataProperty.DESCRIPTION.name)) {
            replacementTerm.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataType.EXPRESSION_PROPERTY_NAME)) {
            replacementTerm.setExpression(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataType.STEWARD_PROPERTY_NAME)) {
            replacementTerm.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataProperty.SOURCE.name)) {
            replacementTerm.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    protected boolean mapEnumToRelationship(ReplacementTerm replacementTerm, String propertyName, EnumPropertyValue enumPropertyValue) {
        boolean foundProperty = false;
        if (propertyName.equals(OpenMetadataProperty.TERM_RELATIONSHIP_STATUS.name)) {
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
