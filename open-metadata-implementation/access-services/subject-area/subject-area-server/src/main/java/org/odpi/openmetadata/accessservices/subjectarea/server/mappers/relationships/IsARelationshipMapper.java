/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.IsA;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;


/**
 * Mapping methods to map between the iSARelationship and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class IsARelationshipMapper extends LineMapper<IsA> {
    public static final String ISA_RELATIONSHIP = "ISARelationship";

    public IsARelationshipMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }


    /**
     * Map the supplied Line to omrs InstanceProperties.
     *
     * @param iSARelationship               supplied line
     * @param instanceProperties equivalent instance properties to the Line
     */
    @Override
    protected void mapLineToInstanceProperties(IsA iSARelationship, InstanceProperties instanceProperties) {
        if (iSARelationship.getDescription() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, iSARelationship.getDescription(), "description");
        }
        if (iSARelationship.getExpression() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, iSARelationship.getExpression(), "expression");
        }
        if (iSARelationship.getSteward() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, iSARelationship.getSteward(), "steward");
        }
        if (iSARelationship.getSource() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, iSARelationship.getSource(), "source");
        }
        if (iSARelationship.getStatus() != null) {
            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
            enumPropertyValue.setOrdinal(iSARelationship.getStatus().getOrdinal());
            instanceProperties.setProperty("status", enumPropertyValue);
        }
    }

    /**
     * Map a primitive omrs property to the iSARelationship object.
     *
     * @param iSARelationship         the glossary to be updated
     * @param propertyName the omrs property name
     * @param value        the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the Line, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToLine(IsA iSARelationship, String propertyName, Object value) {
        String stringValue = (String) value;
        boolean foundProperty = false;
        if (propertyName.equals("description")) {
            iSARelationship.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("expression")) {
            iSARelationship.setExpression(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("steward")) {
            iSARelationship.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("source")) {
            iSARelationship.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    protected boolean mapEnumToLine(IsA iSARelationship, String propertyName, EnumPropertyValue enumPropertyValue) {
        boolean foundProperty = false;
        if (propertyName.equals("status")) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            iSARelationship.setStatus(status);
            foundProperty = true;
        }
        return foundProperty;
    }

    /**
     * Get proxy1 guid.
     * The proxy has omrs type GlossaryTerm
     *
     * @param iSARelationship line
     * @return guid for entity proxy 1
     */
    @Override
    protected String getProxy1Guid(IsA iSARelationship) {
        return iSARelationship.getTermGuid();
    }

    /**
     * Get proxy2 guid
     * The proxy has omrs type GlossaryTerm
     *
     * @param iSARelationship for this Line
     * @return guid for entity proxy 2
     */
    @Override
    protected String getProxy2Guid(IsA iSARelationship) {
        return iSARelationship.getSpecialisedTermGuid();
    }

    @Override
    public String getTypeName() {
        return ISA_RELATIONSHIP;
    }

    @Override
    protected IsA getLineInstance() {
        return new IsA();
    }

    @Override
    protected void setEnd1GuidInLine(IsA isARelationship, String guid) {
        isARelationship.setTermGuid(guid);
    }

    @Override
    protected void setEnd2GuidInLine(IsA isARelationship, String guid) {
        isARelationship.setSpecialisedTermGuid(guid);
    }
}
