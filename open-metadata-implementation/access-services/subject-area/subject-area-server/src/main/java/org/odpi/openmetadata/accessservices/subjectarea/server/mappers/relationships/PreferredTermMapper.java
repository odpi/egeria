/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.PreferredTerm;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
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
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, preferredTerm.getDescription(), OpenMetadataProperty.DESCRIPTION.name);
        }
        if (preferredTerm.getExpression() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, preferredTerm.getExpression(), OpenMetadataType.EXPRESSION_PROPERTY_NAME);
        }
        if (preferredTerm.getSteward() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, preferredTerm.getSteward(), OpenMetadataType.STEWARD_PROPERTY_NAME);
        }
        if (preferredTerm.getSource() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, preferredTerm.getSource(), OpenMetadataType.SOURCE_PROPERTY_NAME);
        }
        if (preferredTerm.getStatus() != null) {
            SubjectAreaUtils.setStatusPropertyInInstanceProperties(instanceProperties, preferredTerm.getStatus(), OpenMetadataType.STATUS_PROPERTY_NAME);
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
        if (propertyName.equals(OpenMetadataProperty.DESCRIPTION.name)) {
            preferredTerm.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataType.EXPRESSION_PROPERTY_NAME)) {
            preferredTerm.setExpression(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataType.STEWARD_PROPERTY_NAME)) {
            preferredTerm.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataType.SOURCE_PROPERTY_NAME)) {
            preferredTerm.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    protected boolean mapEnumToRelationship(PreferredTerm preferredTerm, String propertyName, EnumPropertyValue enumPropertyValue) {
        boolean foundProperty = false;
        if (propertyName.equals(OpenMetadataType.STATUS_PROPERTY_NAME)) {
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
