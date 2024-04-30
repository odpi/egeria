/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.ValidValue;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;


/**
 * Mapping methods to map between the validValue and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class ValidValueMapper extends RelationshipMapper<ValidValue> {
    public static final String VALID_VALUE = "ValidValue";

    public ValidValueMapper(OpenMetadataAPIGenericHandler genericHandler){
        super(genericHandler);
    }

    /**
     * Map the supplied relationship to omrs InstanceProperties.
     *
     * @param validValue               supplied relationship
     * @param instanceProperties equivalent instance properties to the relationship
     */
    @Override
    protected void mapRelationshipToInstanceProperties(ValidValue validValue, InstanceProperties instanceProperties) {
        if (validValue.getDescription() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, validValue.getDescription(), OpenMetadataProperty.DESCRIPTION.name);
        }
        if (validValue.getExpression() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, validValue.getExpression(), OpenMetadataProperty.EXPRESSION.name);
        }
        if (validValue.getSteward() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, validValue.getSteward(), OpenMetadataProperty.STEWARD.name);
        }
        if (validValue.getSource() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, validValue.getSource(), OpenMetadataProperty.SOURCE.name);
        }
        if (validValue.getStatus() != null) {
            SubjectAreaUtils.setStatusPropertyInInstanceProperties(instanceProperties, validValue.getStatus(), OpenMetadataProperty.TERM_RELATIONSHIP_STATUS.name);
        }
    }

    /**
     * Map a primitive omrs property to the validValue object.
     *
     * @param validValue   the omas relationship to be updated
     * @param propertyName the omrs property name
     * @param value        the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the relationship, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToRelationship(ValidValue validValue, String propertyName, Object value) {
        String stringValue = (String) value;
        boolean foundProperty = false;
        if (propertyName.equals(OpenMetadataProperty.DESCRIPTION.name)) {
            validValue.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataProperty.EXPRESSION.name)) {
            validValue.setExpression(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataProperty.STEWARD.name)) {
            validValue.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataProperty.SOURCE.name)) {
            validValue.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    protected boolean mapEnumToRelationship(ValidValue validValue, String propertyName, EnumPropertyValue enumPropertyValue) {
        boolean foundProperty = false;
        if (propertyName.equals(OpenMetadataProperty.TERM_RELATIONSHIP_STATUS.name)) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            validValue.setStatus(status);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    public String getTypeName() {
        return VALID_VALUE;
    }

    @Override
    protected ValidValue getRelationshipInstance() {
        return new ValidValue();
    }

}