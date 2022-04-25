/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.HasA;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;


/**
 * Mapping methods to map between the termHASARelationship and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class TermHasARelationshipMapper extends RelationshipMapper<HasA> {
    public static final String TERM_HASA_RELATIONSHIP = "TermHASARelationship";

    public TermHasARelationshipMapper(OpenMetadataAPIGenericHandler genericHandler){
        super(genericHandler);
    }

    /**
     * Map the supplied omas relationship to omrs InstanceProperties.
     *
     * @param termHasARelationship               supplied relationship
     * @param instanceProperties equivalent instance properties to the relationship
     */
    @Override
    protected void mapRelationshipToInstanceProperties(HasA termHasARelationship, InstanceProperties instanceProperties) {
        if (termHasARelationship.getDescription() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, termHasARelationship.getDescription(), OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME);
        }
        if (termHasARelationship.getSteward() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, termHasARelationship.getSteward(), OpenMetadataAPIMapper.STEWARD_PROPERTY_NAME);
        }
        if (termHasARelationship.getSource() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, termHasARelationship.getSource(), OpenMetadataAPIMapper.SOURCE_PROPERTY_NAME);
        }
        if (termHasARelationship.getStatus() != null) {
            SubjectAreaUtils.setStatusPropertyInInstanceProperties(instanceProperties, termHasARelationship.getStatus(), OpenMetadataAPIMapper.STATUS_PROPERTY_NAME);
        }
    }

    /**
     * Map a primitive omrs property to the termHASARelationship object.
     *
     * @param termHasARelationship         the omas relationship to be updated
     * @param propertyName the omrs property name
     * @param value        the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the relationship, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToRelationship(HasA termHasARelationship, String propertyName, Object value) {
        String stringValue = (String) value;
        boolean foundProperty = false;
        if (propertyName.equals(OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME)) {
            termHasARelationship.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataAPIMapper.STEWARD_PROPERTY_NAME)) {
            termHasARelationship.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataAPIMapper.SOURCE_PROPERTY_NAME)) {
            termHasARelationship.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    protected boolean mapEnumToRelationship(HasA termHasARelationship, String propertyName, EnumPropertyValue enumPropertyValue) {
        boolean foundProperty = false;
        if (propertyName.equals(OpenMetadataAPIMapper.STATUS_PROPERTY_NAME)) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            termHasARelationship.setStatus(status);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    public String getTypeName() {
        return TERM_HASA_RELATIONSHIP;
    }

    @Override
    protected HasA getRelationshipInstance() {
        return new HasA();
    }

}