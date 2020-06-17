/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.TypedBy;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;


/**
 * Mapping methods to map between the termTYPEDBYRelationship and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class TermTYPEDBYRelationshipMapper extends LineMapper<TypedBy> {
    public static final String TERM_TYPED_BY_RELATIONSHIP = "TermTYPEDBYRelationship";

    public TermTYPEDBYRelationshipMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    /**
     * Map the supplied Line to omrs InstanceProperties.
     *
     * @param termTYPEDBYRelationship               supplied line
     * @param instanceProperties equivalent instance properties to the Line
     */
    @Override
    protected void mapLineToInstanceProperties(TypedBy termTYPEDBYRelationship, InstanceProperties instanceProperties) {
        if (termTYPEDBYRelationship.getDescription() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, termTYPEDBYRelationship.getDescription(), "description");
        }
        if (termTYPEDBYRelationship.getSteward() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, termTYPEDBYRelationship.getSteward(), "steward");
        }
        if (termTYPEDBYRelationship.getSource() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, termTYPEDBYRelationship.getSource(), "source");
        }
        if (termTYPEDBYRelationship.getStatus() != null) {
            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
            enumPropertyValue.setOrdinal(termTYPEDBYRelationship.getStatus().getOrdinal());
            instanceProperties.setProperty("status", enumPropertyValue);
        }
    }

    /**
     * Map a primitive omrs property to the termTYPEDBYRelationship object.
     *
     * @param termTYPEDBYRelationship         the glossary to be updated
     * @param propertyName the omrs property name
     * @param value        the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the Line, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToLine(TypedBy termTYPEDBYRelationship, String propertyName, Object value) {
        String stringValue = (String) value;
        boolean foundProperty = false;
        if (propertyName.equals("description")) {
            termTYPEDBYRelationship.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("steward")) {
            termTYPEDBYRelationship.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("source")) {
            termTYPEDBYRelationship.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    protected boolean mapEnumToLine(TypedBy termTYPEDBYRelationship, String propertyName, EnumPropertyValue enumPropertyValue) {
        boolean foundProperty = false;
        if (propertyName.equals("status")) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            termTYPEDBYRelationship.setStatus(status);
            foundProperty = true;
        }
        return foundProperty;
    }

    /**
     * Get proxy1 guid.
     * The proxy has omrs type GlossaryTerm
     *
     * @param termTYPEDBYRelationship line
     * @return guid for entity proxy 1
     */
    @Override
    protected String getProxy1Guid(TypedBy termTYPEDBYRelationship) {
        return termTYPEDBYRelationship.getAttributeGuid();
    }

    /**
     * Get proxy2 guid
     * The proxy has omrs type GlossaryTerm
     *
     * @param termTYPEDBYRelationship for this Line
     * @return guid for entity proxy 2
     */
    @Override
    protected String getProxy2Guid(TypedBy termTYPEDBYRelationship) {
        return termTYPEDBYRelationship.getTypeGuid();
    }

    @Override
    public String getTypeName() {
        return TERM_TYPED_BY_RELATIONSHIP;
    }

    @Override
    protected TypedBy getLineInstance() {
        return new TypedBy();
    }

    @Override
    protected void setEnd1GuidInLine(TypedBy termTYPEDBYRelationship, String guid) {
        termTYPEDBYRelationship.setAttributeGuid(guid);
    }

    @Override
    protected void setEnd2GuidInLine(TypedBy termTYPEDBYRelationship, String guid) {
        termTYPEDBYRelationship.setTypeGuid(guid);
    }
}