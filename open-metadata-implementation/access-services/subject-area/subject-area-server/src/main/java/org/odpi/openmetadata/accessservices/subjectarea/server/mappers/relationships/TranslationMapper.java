/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Translation;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;


/**
 * Mapping methods to map between the translation and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class TranslationMapper extends RelationshipMapper<Translation> {
    public static final String TRANSLATION = "Translation";

    public TranslationMapper(OpenMetadataAPIGenericHandler genericHandler){
        super(genericHandler);
    }

    /**
     * Map the supplied relationship to omrs InstanceProperties.
     *
     * @param translation        supplied relationship
     * @param instanceProperties equivalent instance properties to the relationship
     */
    @Override
    protected void mapRelationshipToInstanceProperties(Translation translation, InstanceProperties instanceProperties) {
        if (translation.getDescription() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, translation.getDescription(), OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME);
        }
        if (translation.getExpression() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, translation.getExpression(), OpenMetadataAPIMapper.EXPRESSION_PROPERTY_NAME);
        }
        if (translation.getSteward() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, translation.getSteward(), OpenMetadataAPIMapper.STEWARD_PROPERTY_NAME);
        }
        if (translation.getSource() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, translation.getSource(), OpenMetadataAPIMapper.SOURCE_PROPERTY_NAME);
        }
        if (translation.getStatus() != null) {
            SubjectAreaUtils.setStatusPropertyInInstanceProperties(instanceProperties, translation.getStatus(), OpenMetadataAPIMapper.STATUS_PROPERTY_NAME);
        }
    }

    /**
     * Map a primitive omrs property to the translation object.
     *
     * @param translation  the omas relationship to be updated
     * @param propertyName the omrs property name
     * @param value        the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the relationship, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToRelationship(Translation translation, String propertyName, Object value) {
        String stringValue = (String) value;
        boolean foundProperty = false;
        if (propertyName.equals(OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME)) {
            translation.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataAPIMapper.EXPRESSION_PROPERTY_NAME)) {
            translation.setExpression(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataAPIMapper.STEWARD_PROPERTY_NAME)) {
            translation.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataAPIMapper.SOURCE_PROPERTY_NAME)) {
            translation.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    public String getTypeName() {
        return TRANSLATION;
    }

    @Override
    protected Translation getRelationshipInstance() {
        return new Translation();
    }

}