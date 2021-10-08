/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.TypedBy;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;


/**
 * Mapping methods to map between the termTYPEDBYRelationship and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class TermTypedByRelationshipMapper extends RelationshipMapper<TypedBy> {
    public static final String TERM_TYPED_BY_RELATIONSHIP = "TermTYPEDBYRelationship";

    public TermTypedByRelationshipMapper(OpenMetadataAPIGenericHandler genericHandler){
        super(genericHandler);
    }

    /**
     * Map the supplied relationship to omrs InstanceProperties.
     *
     * @param termTYPEDBYRelationship               supplied relationship
     * @param instanceProperties equivalent instance properties to the relationship
     */
    @Override
    protected void mapRelationshipToInstanceProperties(TypedBy termTYPEDBYRelationship, InstanceProperties instanceProperties) {
        if (termTYPEDBYRelationship.getDescription() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, termTYPEDBYRelationship.getDescription(), OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME);
        }
        if (termTYPEDBYRelationship.getSteward() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, termTYPEDBYRelationship.getSteward(), OpenMetadataAPIMapper.STEWARD_PROPERTY_NAME);
        }
        if (termTYPEDBYRelationship.getSource() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, termTYPEDBYRelationship.getSource(), OpenMetadataAPIMapper.SOURCE_PROPERTY_NAME);
        }
        if (termTYPEDBYRelationship.getStatus() != null) {
            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
            enumPropertyValue.setOrdinal(termTYPEDBYRelationship.getStatus().getOrdinal());
            instanceProperties.setProperty(OpenMetadataAPIMapper.STATUS_PROPERTY_NAME, enumPropertyValue);
        }
    }

    /**
     * Map a primitive omrs property to the termTYPEDBYRelationship object.
     *
     * @param termTYPEDBYRelationship         the omas relationship to be updated
     * @param propertyName the omrs property name
     * @param value        the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the relationship, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToRelationship(TypedBy termTYPEDBYRelationship, String propertyName, Object value) {
        String stringValue = (String) value;
        boolean foundProperty = false;
        if (propertyName.equals(OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME)) {
            termTYPEDBYRelationship.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataAPIMapper.STEWARD_PROPERTY_NAME)) {
            termTYPEDBYRelationship.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataAPIMapper.SOURCE_PROPERTY_NAME)) {
            termTYPEDBYRelationship.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    protected boolean mapEnumToRelationship(TypedBy termTYPEDBYRelationship, String propertyName, EnumPropertyValue enumPropertyValue) {
        boolean foundProperty = false;
        if (propertyName.equals(OpenMetadataAPIMapper.STATUS_PROPERTY_NAME)) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            termTYPEDBYRelationship.setStatus(status);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    public String getTypeName() {
        return TERM_TYPED_BY_RELATIONSHIP;
    }

    @Override
    protected TypedBy getRelationshipInstance() {
        return new TypedBy();
    }

}