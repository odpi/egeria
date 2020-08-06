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
public class UsedInContextMapper extends LineMapper<UsedInContext> {
    public static final String USED_IN_CONTEXT = "UsedInContext";

    public UsedInContextMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    /**
     * Map the supplied Line to omrs InstanceProperties.
     *
     * @param usedInContext               supplied line
     * @param instanceProperties equivalent instance properties to the Line
     */
    @Override
    protected void mapLineToInstanceProperties(UsedInContext usedInContext, InstanceProperties instanceProperties) {
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
     * @param usedInContext         the glossary to be updated
     * @param propertyName the omrs property name
     * @param value        the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the Line, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToLine(UsedInContext usedInContext, String propertyName, Object value) {
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
    protected boolean mapEnumToLine(UsedInContext usedInContext, String propertyName, EnumPropertyValue enumPropertyValue) {
        boolean foundProperty = false;
        if (propertyName.equals("status")) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            usedInContext.setStatus(status);
            foundProperty = true;
        }
        return foundProperty;
    }

    /**
     * Get proxy1 guid.
     * The proxy has omrs type GlossaryTerm
     *
     * @param usedInContext line
     * @return guid for entity proxy 1
     */
    @Override
    protected String getProxy1Guid(UsedInContext usedInContext) {
        return usedInContext.getContextGuid();
    }

    /**
     * Get proxy2 guid
     * The proxy has omrs type GlossaryTerm
     *
     * @param usedInContext for this Line
     * @return guid for entity proxy 2
     */
    @Override
    protected String getProxy2Guid(UsedInContext usedInContext) {
        return usedInContext.getTermInContextGuid();
    }

    @Override
    public String getTypeName() {
        return USED_IN_CONTEXT;
    }

    @Override
    protected UsedInContext getLineInstance() {
        return new UsedInContext();
    }

    @Override
    protected void setEnd1GuidInLine(UsedInContext usedInContext, String guid) {
        usedInContext.setContextGuid(guid);
    }

    @Override
    protected void setEnd2GuidInLine(UsedInContext usedInContext, String guid) {
        usedInContext.setTermInContextGuid(guid);
    }

}
