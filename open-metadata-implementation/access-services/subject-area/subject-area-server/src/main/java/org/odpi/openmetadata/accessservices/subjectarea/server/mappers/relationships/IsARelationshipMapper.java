/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.IsA;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;


/**
 * Mapping methods to map between the iSARelationship and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class IsARelationshipMapper extends RelationshipMapper<IsA> {
    public static final String ISA_RELATIONSHIP = "ISARelationship";

    public IsARelationshipMapper(OpenMetadataAPIGenericHandler genericHandler){
        super(genericHandler);
    }


    /**
     * Map the supplied relationship to omrs InstanceProperties.
     *
     * @param iSARelationship               supplied relationship
     * @param instanceProperties equivalent instance properties to the relationship
     */
    @Override
    protected void mapRelationshipToInstanceProperties(IsA iSARelationship, InstanceProperties instanceProperties) {
        if (iSARelationship.getDescription() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, iSARelationship.getDescription(), OpenMetadataProperty.DESCRIPTION.name);
        }
        if (iSARelationship.getExpression() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, iSARelationship.getExpression(), OpenMetadataType.EXPRESSION_PROPERTY_NAME);
        }
        if (iSARelationship.getSteward() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, iSARelationship.getSteward(), OpenMetadataType.STEWARD_PROPERTY_NAME);
        }
        if (iSARelationship.getSource() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, iSARelationship.getSource(), OpenMetadataProperty.SOURCE.name);
        }
        if (iSARelationship.getStatus() != null) {
            SubjectAreaUtils.setStatusPropertyInInstanceProperties(instanceProperties, iSARelationship.getStatus(), OpenMetadataType.STATUS_PROPERTY_NAME);
        }
    }

    /**
     * Map a primitive omrs property to the iSARelationship object.
     *
     * @param iSARelationship         the omas relationship to be updated
     * @param propertyName the omrs property name
     * @param value        the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the relationship, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToRelationship(IsA iSARelationship, String propertyName, Object value) {
        String stringValue = (String) value;
        boolean foundProperty = false;
        if (propertyName.equals(OpenMetadataProperty.DESCRIPTION.name)) {
            iSARelationship.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataType.EXPRESSION_PROPERTY_NAME)) {
            iSARelationship.setExpression(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataType.STEWARD_PROPERTY_NAME)) {
            iSARelationship.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataProperty.SOURCE.name)) {
            iSARelationship.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    protected boolean mapEnumToRelationship(IsA iSARelationship, String propertyName, EnumPropertyValue enumPropertyValue) {
        boolean foundProperty = false;
        if (propertyName.equals(OpenMetadataType.STATUS_PROPERTY_NAME)) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            iSARelationship.setStatus(status);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    public String getTypeName() {
        return ISA_RELATIONSHIP;
    }

    @Override
    protected IsA getRelationshipInstance() {
        return new IsA();
    }

  }
