/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.IsATypeOfDeprecated;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;


/**
 * Mapping methods to map between the termISATypeOFRelationship and the equivalent omrs Relationship.
 *
 * @deprecated use IsATypeOfRelationship not TermISATypeOFRelationship
 */
@Deprecated
@SubjectAreaMapper
public class TermIsATypeOfRelationshipDeprecatedMapper extends RelationshipMapper<IsATypeOfDeprecated> {
    private static final String TERM_ISA_TYPE_OF_DEPRECATED_RELATIONSHIP = "TermISATypeOFRelationship";

    public TermIsATypeOfRelationshipDeprecatedMapper(OpenMetadataAPIGenericHandler genericHandler){
        super(genericHandler);
    }

    /**
     * Map the supplied relationship to omrs InstanceProperties.
     * @deprecated
     * @param isATypeOfDeprecated               supplied relationship
     * @param instanceProperties equivalent instance properties to the relationship
     */
    @Deprecated
    @Override
    protected void mapRelationshipToInstanceProperties(IsATypeOfDeprecated isATypeOfDeprecated, InstanceProperties instanceProperties) {
        if (isATypeOfDeprecated.getDescription() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, isATypeOfDeprecated.getDescription(), OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME);
        }
        if (isATypeOfDeprecated.getSteward() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, isATypeOfDeprecated.getSteward(), OpenMetadataAPIMapper.STEWARD_PROPERTY_NAME);
        }
        if (isATypeOfDeprecated.getSource() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, isATypeOfDeprecated.getSource(), OpenMetadataAPIMapper.SOURCE_PROPERTY_NAME);
        }
        if (isATypeOfDeprecated.getStatus() != null) {
            SubjectAreaUtils.setStatusPropertyInInstanceProperties(instanceProperties, isATypeOfDeprecated.getStatus(), OpenMetadataAPIMapper.STATUS_PROPERTY_NAME);
        }
    }

    /**
     * Map a primitive omrs property to the termISATypeOFRelationship object.
     *
     * @deprecated
     * @param isATypeOfDeprecated         the omas relationship to be updated
     * @param propertyName the omrs property name
     * @param value        the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the relationship, otherwise false
     */
    @Deprecated
    @Override
    protected boolean mapPrimitiveToRelationship(IsATypeOfDeprecated isATypeOfDeprecated, String propertyName, Object value) {
        String stringValue = (String) value;
        boolean foundProperty = false;
        if (propertyName.equals(OpenMetadataAPIMapper.DESCRIPTION_PROPERTY_NAME)) {
            isATypeOfDeprecated.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataAPIMapper.STEWARD_PROPERTY_NAME)) {
            isATypeOfDeprecated.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataAPIMapper.SOURCE_PROPERTY_NAME)) {
            isATypeOfDeprecated.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }
    @Deprecated
    @Override
    protected boolean mapEnumToRelationship(IsATypeOfDeprecated termIsATypeOFRelationship, String propertyName, EnumPropertyValue enumPropertyValue) {
        boolean foundProperty = false;
        if (propertyName.equals(OpenMetadataAPIMapper.STATUS_PROPERTY_NAME)) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            termIsATypeOFRelationship.setStatus(status);
            foundProperty = true;
        }
        return foundProperty;
    }
    @Deprecated
    @Override
    public String getTypeName() {
        return TERM_ISA_TYPE_OF_DEPRECATED_RELATIONSHIP;
    }
    @Deprecated
    @Override
    protected IsATypeOfDeprecated getRelationshipInstance() {
        return new IsATypeOfDeprecated();
    }

}
