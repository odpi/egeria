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
 * Mapping methods to map between the termISATypeOFRelationship and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class TermIsATypeOfRelationshipMapper extends LineMapper<IsATypeOf> {
    private static final String TERM_ISA_TYPE_OF_RELATIONSHIP = "TermISATypeOFRelationship";

    public TermIsATypeOfRelationshipMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    /**
     * Map the supplied Line to omrs InstanceProperties.
     *
     * @param termIsATypeOFRelationship               supplied line
     * @param instanceProperties equivalent instance properties to the Line
     */
    @Override
    protected void mapLineToInstanceProperties(IsATypeOf termIsATypeOFRelationship, InstanceProperties instanceProperties) {
        if (termIsATypeOFRelationship.getDescription() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, termIsATypeOFRelationship.getDescription(), "description");
        }
        if (termIsATypeOFRelationship.getSteward() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, termIsATypeOFRelationship.getSteward(), "steward");
        }
        if (termIsATypeOFRelationship.getSource() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, termIsATypeOFRelationship.getSource(), "source");
        }
        if (termIsATypeOFRelationship.getStatus() != null) {
            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
            enumPropertyValue.setOrdinal(termIsATypeOFRelationship.getStatus().getOrdinal());
            instanceProperties.setProperty("status", enumPropertyValue);
        }
    }

    /**
     * Map a primitive omrs property to the termISATypeOFRelationship object.
     *
     * @param termIsATypeOFRelationship         the glossary to be updated
     * @param propertyName the omrs property name
     * @param value        the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the Line, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToLine(IsATypeOf termIsATypeOFRelationship, String propertyName, Object value) {
        String stringValue = (String) value;
        boolean foundProperty = false;
        if (propertyName.equals("description")) {
            termIsATypeOFRelationship.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("steward")) {
            termIsATypeOFRelationship.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("source")) {
            termIsATypeOFRelationship.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    protected boolean mapEnumToLine(IsATypeOf termIsATypeOFRelationship, String propertyName, EnumPropertyValue enumPropertyValue) {
        boolean foundProperty = false;
        if (propertyName.equals("status")) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            termIsATypeOFRelationship.setStatus(status);
            foundProperty = true;
        }
        return foundProperty;
    }

    /**
     * Get proxy1 guid.
     * The proxy has omrs type GlossaryTerm
     *
     * @param termIsATypeOFRelationship line
     * @return guid for entity proxy 1
     */
    @Override
    protected String getProxy1Guid(IsATypeOf termIsATypeOFRelationship) {
        return termIsATypeOFRelationship.getSuperTypeGuid();
    }

    /**
     * Get proxy2 guid
     * The proxy has omrs type GlossaryTerm
     *
     * @param termIsATypeOFRelationship for this Line
     * @return guid for entity proxy 2
     */
    @Override
    protected String getProxy2Guid(IsATypeOf termIsATypeOFRelationship) {
        return termIsATypeOFRelationship.getSubTypeGuid();
    }

    @Override
    public String getTypeName() {
        return TERM_ISA_TYPE_OF_RELATIONSHIP;
    }

    @Override
    protected IsATypeOf getLineInstance() {
        return new IsATypeOf();
    }

    @Override
    protected void setEnd1GuidInLine(IsATypeOf termIsATypeOFRelationship, String guid) {
        termIsATypeOFRelationship.setSuperTypeGuid(guid);
    }

    @Override
    protected void setEnd2GuidInLine(IsATypeOf termIsATypeOFRelationship, String guid) {
        termIsATypeOFRelationship.setSubTypeGuid(guid);
    }
}
