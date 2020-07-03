/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.ValidValue;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;


/**
 * Mapping methods to map between the validValue and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class ValidValueMapper extends LineMapper<ValidValue> {
    public static final String VALID_VALUE = "ValidValue";

    public ValidValueMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    /**
     * Map the supplied Line to omrs InstanceProperties.
     *
     * @param validValue               supplied line
     * @param instanceProperties equivalent instance properties to the Line
     */
    @Override
    protected void mapLineToInstanceProperties(ValidValue validValue, InstanceProperties instanceProperties) {
        if (validValue.getDescription() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, validValue.getDescription(), "description");
        }
        if (validValue.getExpression() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, validValue.getExpression(), "expression");
        }
        if (validValue.getSteward() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, validValue.getSteward(), "steward");
        }
        if (validValue.getSource() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, validValue.getSource(), "source");
        }
        if (validValue.getStatus() != null) {
            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
            enumPropertyValue.setOrdinal(validValue.getStatus().getOrdinal());
            instanceProperties.setProperty("status", enumPropertyValue);
        }
    }

    /**
     * Map a primitive omrs property to the validValue object.
     *
     * @param validValue         the glossary to be updated
     * @param propertyName the omrs property name
     * @param value        the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the Line, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToLine(ValidValue validValue, String propertyName, Object value) {
        String stringValue = (String) value;
        boolean foundProperty = false;
        if (propertyName.equals("description")) {
            validValue.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("expression")) {
            validValue.setExpression(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("steward")) {
            validValue.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("source")) {
            validValue.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    protected boolean mapEnumToLine(ValidValue validValue, String propertyName, EnumPropertyValue enumPropertyValue) {
        boolean foundProperty = false;
        if (propertyName.equals("status")) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            validValue.setStatus(status);
            foundProperty = true;
        }
        return foundProperty;
    }

    /**
     * Get proxy1 guid.
     * The proxy has omrs type GlossaryTerm
     *
     * @param validValue line
     * @return guid for entity proxy 1
     */
    @Override
    protected String getProxy1Guid(ValidValue validValue) {
        return validValue.getTermGuid();
    }

    /**
     * Get proxy2 guid
     * The proxy has omrs type GlossaryTerm
     *
     * @param validValue for this Line
     * @return guid for entity proxy 2
     */
    @Override
    protected String getProxy2Guid(ValidValue validValue) {
        return validValue.getValidValueGuid();
    }

    @Override
    public String getTypeName() {
        return VALID_VALUE;
    }

    @Override
    protected ValidValue getLineInstance() {
        return new ValidValue();
    }

    @Override
    protected void setEnd1GuidInLine(ValidValue validValue, String guid) {
        validValue.setTermGuid(guid);
    }

    @Override
    protected void setEnd2GuidInLine(ValidValue validValue, String guid) {
        validValue.setValidValueGuid(guid);
    }
}