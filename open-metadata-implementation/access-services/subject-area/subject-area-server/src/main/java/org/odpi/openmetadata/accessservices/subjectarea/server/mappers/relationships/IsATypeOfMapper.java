/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.IsATypeOf;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;


/**
 * Mapping methods to map between the ObjectInheritance and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class IsATypeOfMapper extends RelationshipMapper<IsATypeOf> {
    private static final String OBJECT_INHERITANCE_RELATIONSHIP = "IsATypeOfRelationship";

    public IsATypeOfMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    /**
     * Map the supplied relationship to omrs InstanceProperties.
     *
     * @param ObjectInheritance               supplied relationship
     * @param instanceProperties equivalent instance properties to the relationship
     */
    @Override
    protected void mapRelationshipToInstanceProperties(IsATypeOf ObjectInheritance, InstanceProperties instanceProperties) {
        if (ObjectInheritance.getDescription() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, ObjectInheritance.getDescription(), "description");
        }
        if (ObjectInheritance.getSteward() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, ObjectInheritance.getSteward(), "steward");
        }
        if (ObjectInheritance.getSource() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, ObjectInheritance.getSource(), "source");
        }
        if (ObjectInheritance.getStatus() != null) {
            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
            enumPropertyValue.setOrdinal(ObjectInheritance.getStatus().getOrdinal());
            instanceProperties.setProperty("status", enumPropertyValue);
        }
    }

    /**
     * Map a primitive omrs property to the ObjectInheritance object.
     *
     * @param ObjectInheritance         the omas relationship to be updated
     * @param propertyName the omrs property name
     * @param value        the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the relationship, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToRelationship(IsATypeOf ObjectInheritance, String propertyName, Object value) {
        String stringValue = (String) value;
        boolean foundProperty = false;
        if (propertyName.equals("description")) {
            ObjectInheritance.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("steward")) {
            ObjectInheritance.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("source")) {
            ObjectInheritance.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    protected boolean mapEnumToRelationship(IsATypeOf ObjectInheritance, String propertyName, EnumPropertyValue enumPropertyValue) {
        boolean foundProperty = false;
        if (propertyName.equals("status")) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            ObjectInheritance.setStatus(status);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    public String getTypeName() {
        return OBJECT_INHERITANCE_RELATIONSHIP;
    }

    @Override
    protected IsATypeOf getRelationshipInstance() {
        return new IsATypeOf();
    }

}
