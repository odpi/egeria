/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.IsATypeOf;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;


/**
 * Mapping methods to map between the isATypeOf and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class IsATypeOfMapper extends RelationshipMapper<IsATypeOf> {
    private static final String IS_A_TYPE_OF_RELATIONSHIP = "IsATypeOfRelationship";

    public IsATypeOfMapper(OpenMetadataAPIGenericHandler genericHandler){
        super(genericHandler);
    }

    /**
     * Map the supplied relationship to omrs InstanceProperties.
     *
     * @param isATypeOf               supplied relationship
     * @param instanceProperties equivalent instance properties to the relationship
     */
    @Override
    protected void mapRelationshipToInstanceProperties(IsATypeOf isATypeOf, InstanceProperties instanceProperties) {
        if (isATypeOf.getDescription() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, isATypeOf.getDescription(), OpenMetadataProperty.DESCRIPTION.name);
        }
        if (isATypeOf.getSteward() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, isATypeOf.getSteward(), OpenMetadataType.STEWARD_PROPERTY_NAME);
        }
        if (isATypeOf.getSource() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, isATypeOf.getSource(), OpenMetadataProperty.SOURCE.name);
        }
        if (isATypeOf.getStatus() != null) {
            SubjectAreaUtils.setStatusPropertyInInstanceProperties(instanceProperties, isATypeOf.getStatus(), OpenMetadataType.STATUS_PROPERTY_NAME);
        }
    }

    /**
     * Map a primitive omrs property to the IsATypeOf object.
     *
     * @param isATypeOf         the omas relationship to be updated
     * @param propertyName the omrs property name
     * @param value        the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the relationship, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToRelationship(IsATypeOf isATypeOf, String propertyName, Object value) {
        String stringValue = (String) value;
        boolean foundProperty = false;
        if (propertyName.equals(OpenMetadataProperty.DESCRIPTION.name)) {
            isATypeOf.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataType.STEWARD_PROPERTY_NAME)) {
            isATypeOf.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataProperty.SOURCE.name)) {
            isATypeOf.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    protected boolean mapEnumToRelationship(IsATypeOf isATypeOf, String propertyName, EnumPropertyValue enumPropertyValue) {
        boolean foundProperty = false;
        if (propertyName.equals(OpenMetadataType.STATUS_PROPERTY_NAME)) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            isATypeOf.setStatus(status);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    public String getTypeName() {
        return IS_A_TYPE_OF_RELATIONSHIP;
    }

    @Override
    protected IsATypeOf getRelationshipInstance() {
        return new IsATypeOf();
    }

}
