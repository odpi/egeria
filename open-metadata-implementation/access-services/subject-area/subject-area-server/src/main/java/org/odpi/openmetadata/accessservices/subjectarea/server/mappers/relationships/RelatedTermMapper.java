/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.RelatedTerm;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;


/**
 * Mapping methods to map between the relatedTerm and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class RelatedTermMapper extends RelationshipMapper<RelatedTerm> {
    public static final String RELATED_TERM = "RelatedTerm";

    public RelatedTermMapper(OpenMetadataAPIGenericHandler genericHandler){
        super(genericHandler);
    }

    /**
     * Map the supplied relationship to omrs InstanceProperties.
     *
     * @param relatedTerm               supplied relationship
     * @param instanceProperties equivalent instance properties to the relationship
     */
    @Override
    protected void mapRelationshipToInstanceProperties(RelatedTerm relatedTerm, InstanceProperties instanceProperties) {
        if (relatedTerm.getDescription() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, relatedTerm.getDescription(), OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME);
        }
        if (relatedTerm.getExpression() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, relatedTerm.getExpression(), OpenMetadataAPIMapper.EXPRESSION_PROPERTY_NAME);
        }
        if (relatedTerm.getSteward() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, relatedTerm.getSteward(), OpenMetadataAPIMapper.STEWARD_PROPERTY_NAME);
        }
        if (relatedTerm.getSource() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, relatedTerm.getSource(), OpenMetadataAPIMapper.SOURCE_PROPERTY_NAME);
        }
        if (relatedTerm.getStatus() != null) {
            SubjectAreaUtils.setStatusPropertyInInstanceProperties(instanceProperties, relatedTerm.getStatus(), OpenMetadataAPIMapper.STATUS_PROPERTY_NAME);
        }
    }

    /**
     * Map a primitive omrs property to the relatedTerm object.
     *
     * @param relatedTerm         the omas relationship to be updated
     * @param propertyName the omrs property name
     * @param value        the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the relationship, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToRelationship(RelatedTerm relatedTerm, String propertyName, Object value) {
        String stringValue = (String) value;
        boolean foundProperty = false;
        if (propertyName.equals(OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME)) {
            relatedTerm.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataAPIMapper.EXPRESSION_PROPERTY_NAME)) {
            relatedTerm.setExpression(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataAPIMapper.STEWARD_PROPERTY_NAME)) {
            relatedTerm.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataAPIMapper.SOURCE_PROPERTY_NAME)) {
            relatedTerm.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    protected boolean mapEnumToRelationship(RelatedTerm relatedTerm, String propertyName, EnumPropertyValue enumPropertyValue) {
        boolean foundProperty = false;
        if (propertyName.equals(OpenMetadataAPIMapper.STATUS_PROPERTY_NAME)) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            relatedTerm.setStatus(status);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    public String getTypeName() {
        return RELATED_TERM;
    }

    @Override
    protected RelatedTerm getRelationshipInstance() {
        return new RelatedTerm();
    }
}
