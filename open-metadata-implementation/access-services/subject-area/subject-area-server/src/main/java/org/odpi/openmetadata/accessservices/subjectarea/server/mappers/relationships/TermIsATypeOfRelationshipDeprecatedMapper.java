/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.IsATypeOfDeprecated;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;


/**
 * Mapping methods to map between the termISATypeOFRelationship and the equivalent omrs Relationship.
 *
 * @deprecated use IsATypeOfRelationship not TermISATypeOFRelationship
 */
@SubjectAreaMapper
public class TermIsATypeOfRelationshipDeprecatedMapper extends RelationshipMapper<IsATypeOfDeprecated> {
    private static final String TERM_ISA_TYPE_OF_DEPRECATED_RELATIONSHIP = "TermISATypeOFRelationship";

    public TermIsATypeOfRelationshipDeprecatedMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    /**
     * Map the supplied relationship to omrs InstanceProperties.
     * @deprecated
     * @param isATypeOfDeprecated               supplied relationship
     * @param instanceProperties equivalent instance properties to the relationship
     */
    @Override
    protected void mapRelationshipToInstanceProperties(IsATypeOfDeprecated isATypeOfDeprecated, InstanceProperties instanceProperties) {
        if (isATypeOfDeprecated.getDescription() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, isATypeOfDeprecated.getDescription(), "description");
        }
        if (isATypeOfDeprecated.getSteward() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, isATypeOfDeprecated.getSteward(), "steward");
        }
        if (isATypeOfDeprecated.getSource() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, isATypeOfDeprecated.getSource(), "source");
        }
        if (isATypeOfDeprecated.getStatus() != null) {
            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
            enumPropertyValue.setOrdinal(isATypeOfDeprecated.getStatus().getOrdinal());
            instanceProperties.setProperty("status", enumPropertyValue);
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
    @Override
    protected boolean mapPrimitiveToRelationship(IsATypeOfDeprecated isATypeOfDeprecated, String propertyName, Object value) {
        String stringValue = (String) value;
        boolean foundProperty = false;
        if (propertyName.equals("description")) {
            isATypeOfDeprecated.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("steward")) {
            isATypeOfDeprecated.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("source")) {
            isATypeOfDeprecated.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    protected boolean mapEnumToRelationship(IsATypeOfDeprecated termIsATypeOFRelationship, String propertyName, EnumPropertyValue enumPropertyValue) {
        boolean foundProperty = false;
        if (propertyName.equals("status")) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            termIsATypeOFRelationship.setStatus(status);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    public String getTypeName() {
        return TERM_ISA_TYPE_OF_DEPRECATED_RELATIONSHIP;
    }

    @Override
    protected IsATypeOfDeprecated getRelationshipInstance() {
        return new IsATypeOfDeprecated();
    }

}
