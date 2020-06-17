/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.IsaTypeOf;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;


/**
 * Mapping methods to map between the termISATypeOFRelationship and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class TermISATypeOFRelationshipMapper extends LineMapper<IsaTypeOf> {
    private static final String TERM_ISA_TYPE_OF_RELATIONSHIP = "TermISATypeOFRelationship";

    public TermISATypeOFRelationshipMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    /**
     * Map the supplied Line to omrs InstanceProperties.
     *
     * @param termISATypeOFRelationship               supplied line
     * @param instanceProperties equivalent instance properties to the Line
     */
    @Override
    protected void mapLineToInstanceProperties(IsaTypeOf termISATypeOFRelationship, InstanceProperties instanceProperties) {
        if (termISATypeOFRelationship.getDescription() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, termISATypeOFRelationship.getDescription(), "description");
        }
        if (termISATypeOFRelationship.getSteward() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, termISATypeOFRelationship.getSteward(), "steward");
        }
        if (termISATypeOFRelationship.getSource() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, termISATypeOFRelationship.getSource(), "source");
        }
        if (termISATypeOFRelationship.getStatus() != null) {
            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
            enumPropertyValue.setOrdinal(termISATypeOFRelationship.getStatus().getOrdinal());
            instanceProperties.setProperty("status", enumPropertyValue);
        }
    }

    /**
     * Map a primitive omrs property to the termISATypeOFRelationship object.
     *
     * @param termISATypeOFRelationship         the glossary to be updated
     * @param propertyName the omrs property name
     * @param value        the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the Line, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToLine(IsaTypeOf termISATypeOFRelationship, String propertyName, Object value) {
        String stringValue = (String) value;
        boolean foundProperty = false;
        if (propertyName.equals("description")) {
            termISATypeOFRelationship.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("steward")) {
            termISATypeOFRelationship.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("source")) {
            termISATypeOFRelationship.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    protected boolean mapEnumToLine(IsaTypeOf termISATypeOFRelationship, String propertyName, EnumPropertyValue enumPropertyValue) {
        boolean foundProperty = false;
        if (propertyName.equals("status")) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            termISATypeOFRelationship.setStatus(status);
            foundProperty = true;
        }
        return foundProperty;
    }

    /**
     * Get proxy1 guid.
     * The proxy has omrs type GlossaryTerm
     *
     * @param termISATypeOFRelationship line
     * @return guid for entity proxy 1
     */
    @Override
    protected String getProxy1Guid(IsaTypeOf termISATypeOFRelationship) {
        return termISATypeOFRelationship.getSuperTypeGuid();
    }

    /**
     * Get proxy2 guid
     * The proxy has omrs type GlossaryTerm
     *
     * @param termISATypeOFRelationship for this Line
     * @return guid for entity proxy 2
     */
    @Override
    protected String getProxy2Guid(IsaTypeOf termISATypeOFRelationship) {
        return termISATypeOFRelationship.getSubTypeGuid();
    }

    @Override
    public String getTypeName() {
        return TERM_ISA_TYPE_OF_RELATIONSHIP;
    }

    @Override
    protected IsaTypeOf getLineInstance() {
        return new IsaTypeOf();
    }

    @Override
    protected void setEnd1GuidInLine(IsaTypeOf termISATypeOFRelationship, String guid) {
        termISATypeOFRelationship.setSuperTypeGuid(guid);
    }

    @Override
    protected void setEnd2GuidInLine(IsaTypeOf termISATypeOFRelationship, String guid) {
        termISATypeOFRelationship.setSubTypeGuid(guid);
    }
}
