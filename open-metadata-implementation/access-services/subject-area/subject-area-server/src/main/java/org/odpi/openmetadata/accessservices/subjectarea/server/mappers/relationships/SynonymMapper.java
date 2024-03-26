/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Synonym;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.commonservices.generichandlers.*;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataProperty;
import org.odpi.openmetadata.frameworks.governanceaction.mapper.OpenMetadataType;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;


/**
 * Mapping methods to map between the synonym and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class SynonymMapper extends RelationshipMapper<Synonym> {
    public static final String SYNONYM = "Synonym";

    public SynonymMapper(OpenMetadataAPIGenericHandler genericHandler){
        super(genericHandler);
    }

    /**
     * Map the supplied relationship to omrs InstanceProperties.
     *
     * @param synonym               supplied relationship
     * @param instanceProperties equivalent instance properties to the relationship
     */
    @Override
    protected void mapRelationshipToInstanceProperties(Synonym synonym, InstanceProperties instanceProperties) {
        if (synonym.getDescription() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, synonym.getDescription(), OpenMetadataProperty.DESCRIPTION.name);
        }
        if (synonym.getExpression() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, synonym.getExpression(), OpenMetadataType.EXPRESSION_PROPERTY_NAME);
        }
        if (synonym.getSteward() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, synonym.getSteward(), OpenMetadataType.STEWARD_PROPERTY_NAME);
        }
        if (synonym.getSource() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, synonym.getSource(), OpenMetadataProperty.SOURCE.name);
        }
        if (synonym.getStatus() != null) {
            SubjectAreaUtils.setStatusPropertyInInstanceProperties(instanceProperties, synonym.getStatus(), OpenMetadataType.STATUS_PROPERTY_NAME);
        }
    }

    /**
     * Map a primitive omrs property to the synonym object.
     *
     * @param synonym         the omas relationship to be updated
     * @param propertyName the omrs property name
     * @param value        the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the relationship, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToRelationship(Synonym synonym, String propertyName, Object value) {
        String stringValue = (String) value;
        boolean foundProperty = false;
        if (propertyName.equals(OpenMetadataProperty.DESCRIPTION.name)) {
            synonym.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataType.EXPRESSION_PROPERTY_NAME)) {
            synonym.setExpression(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataType.STEWARD_PROPERTY_NAME)) {
            synonym.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals(OpenMetadataProperty.SOURCE.name)) {
            synonym.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    protected boolean mapEnumToRelationship(Synonym synonym, String propertyName, EnumPropertyValue enumPropertyValue) {
        boolean foundProperty = false;
        if (propertyName.equals(OpenMetadataType.STATUS_PROPERTY_NAME)) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            synonym.setStatus(status);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    public String getTypeName() {
        return SYNONYM;
    }

    @Override
    protected Synonym getRelationshipInstance() {
        return new Synonym();
    }

}
