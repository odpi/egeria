/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.UsedInContext;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;


/**
 * Mapping methods to map between the usedInContext and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class UsedInContextMapper extends RelationshipMapper<UsedInContext> {
    public static final String USED_IN_CONTEXT = "UsedInContext";

    public UsedInContextMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    /**
     * Map the supplied relationship to omrs InstanceProperties.
     *
     * @param usedInContext               supplied relationship
     * @param instanceProperties equivalent instance properties to the relationship
     */
    @Override
    protected void mapRelationshipToInstanceProperties(UsedInContext usedInContext, InstanceProperties instanceProperties) {
        if (usedInContext.getDescription() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, usedInContext.getDescription(), "description");
        }
        if (usedInContext.getExpression() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, usedInContext.getExpression(), "expression");
        }
        if (usedInContext.getSteward() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, usedInContext.getSteward(), "steward");
        }
        if (usedInContext.getSource() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, usedInContext.getSource(), "source");
        }
        if (usedInContext.getStatus() != null) {
            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
            enumPropertyValue.setOrdinal(usedInContext.getStatus().getOrdinal());
            instanceProperties.setProperty("status", enumPropertyValue);
        }
    }

    /**
     * Map a primitive omrs property to the usedInContext object.
     *
     * @param usedInContext         the omas relationship to be updated
     * @param propertyName the omrs property name
     * @param value        the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the relationship, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToRelationship(UsedInContext usedInContext, String propertyName, Object value) {
        String stringValue = (String) value;
        boolean foundProperty = false;
        if (propertyName.equals("description")) {
            usedInContext.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("expression")) {
            usedInContext.setExpression(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("steward")) {
            usedInContext.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("source")) {
            usedInContext.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    protected boolean mapEnumToRelationship(UsedInContext usedInContext, String propertyName, EnumPropertyValue enumPropertyValue) {
        boolean foundProperty = false;
        if (propertyName.equals("status")) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            usedInContext.setStatus(status);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    public String getTypeName() {
        return USED_IN_CONTEXT;
    }

    @Override
    protected UsedInContext getRelationshipInstance() {
        return new UsedInContext();
    }

}
