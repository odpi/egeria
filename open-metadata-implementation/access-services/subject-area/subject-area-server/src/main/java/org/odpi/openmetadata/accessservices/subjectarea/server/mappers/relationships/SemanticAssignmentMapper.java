/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermAssignmentStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.SemanticAssignment;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;

import java.util.Map;


/**
 * Mapping methods to map between the semanticAssignment and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class SemanticAssignmentMapper extends RelationshipMapper<SemanticAssignment> {
    public static final String SEMANTIC_ASSIGNMENT = "SemanticAssignment";

    public SemanticAssignmentMapper(OpenMetadataAPIGenericHandler genericHandler){
        super(genericHandler);
    }

    /**
     * Map the supplied relationship to omrs InstanceProperties.
     *
     * @param semanticAssignment       supplied relationship
     * @param properties equivalent instance properties to the relationship
     */
    @Override
    protected void mapRelationshipToInstanceProperties(SemanticAssignment semanticAssignment, InstanceProperties properties) {
        if (semanticAssignment.getDescription() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(properties, semanticAssignment.getDescription(), OpenMetadataProperty.DESCRIPTION.name);
        }
        if (semanticAssignment.getExpression() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(properties, semanticAssignment.getExpression(), OpenMetadataProperty.EXPRESSION.name);
        }
        if (semanticAssignment.getConfidence() != null) {
            SubjectAreaUtils.setIntegerPropertyInInstanceProperties(properties, semanticAssignment.getConfidence(), OpenMetadataProperty.CONFIDENCE.name);
        }
        if (semanticAssignment.getSteward() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(properties, semanticAssignment.getSteward(), OpenMetadataProperty.STEWARD.name);
        }
        if (semanticAssignment.getSource() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(properties, semanticAssignment.getSource(), OpenMetadataProperty.SOURCE.name);
        }

        Map<String, InstancePropertyValue> instancePropertyMap = properties.getInstanceProperties();
        InstancePropertyValue instancePropertyValue = instancePropertyMap.get(OpenMetadataProperty.TERM_RELATIONSHIP_STATUS.name);
        if (instancePropertyValue != null) {
            EnumPropertyValue enumPropertyValue = (EnumPropertyValue) instancePropertyValue;
            TermAssignmentStatus status = TermAssignmentStatus.valueOf(enumPropertyValue.getSymbolicName());
            semanticAssignment.setStatus(status);
        }
    }

    /**
     * Map a primitive omrs property to the semanticAssignment object.
     *
     * @param semanticAssignment         the omas relationship to be updated
     * @param propertyName the omrs property name
     * @param value        the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the relationship, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToRelationship(SemanticAssignment semanticAssignment, String propertyName, Object value) {
        String stringValue = (String) value;
        boolean foundProperty = false;
        if (propertyName.equals(OpenMetadataProperty.DESCRIPTION.name)) {
            semanticAssignment.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataProperty.EXPRESSION.name)) {
            semanticAssignment.setExpression(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataProperty.CONFIDENCE.name)) {
            semanticAssignment.setConfidence((Integer) value);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataProperty.STEWARD.name)) {
            semanticAssignment.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataProperty.SOURCE.name)) {
            semanticAssignment.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    protected boolean mapEnumToRelationship(SemanticAssignment semanticAssignment, String propertyName, EnumPropertyValue enumPropertyValue) {
        boolean foundProperty = false;
        if (propertyName.equals(OpenMetadataProperty.TERM_RELATIONSHIP_STATUS.name)) {
            TermAssignmentStatus status = TermAssignmentStatus.valueOf(enumPropertyValue.getSymbolicName());
            semanticAssignment.setStatus(status);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    public String getTypeName() {
        return SEMANTIC_ASSIGNMENT;
    }

    @Override
    protected SemanticAssignment getRelationshipInstance() {
        return new SemanticAssignment();
    }

}
