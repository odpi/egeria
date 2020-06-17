/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Hasa;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;


/**
 * Mapping methods to map between the termHASARelationship and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class TermHASARelationshipMapper extends LineMapper<Hasa> {
    public static final String TERM_HASA_RELATIONSHIP = "TermHASARelationship";

    public TermHASARelationshipMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    /**
     * Map the supplied Line to omrs InstanceProperties.
     *
     * @param termHASARelationship               supplied line
     * @param instanceProperties equivalent instance properties to the Line
     */
    @Override
    protected void mapLineToInstanceProperties(Hasa termHASARelationship, InstanceProperties instanceProperties) {
        if (termHASARelationship.getDescription() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, termHASARelationship.getDescription(), "description");
        }
        if (termHASARelationship.getSteward() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, termHASARelationship.getSteward(), "steward");
        }
        if (termHASARelationship.getSource() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, termHASARelationship.getSource(), "source");
        }
        if (termHASARelationship.getStatus() != null) {
            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
            enumPropertyValue.setOrdinal(termHASARelationship.getStatus().getOrdinal());
            instanceProperties.setProperty("status", enumPropertyValue);
        }
    }

    /**
     * Map a primitive omrs property to the termHASARelationship object.
     *
     * @param termHASARelationship         the glossary to be updated
     * @param propertyName the omrs property name
     * @param value        the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the Line, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToLine(Hasa termHASARelationship, String propertyName, Object value) {
        String stringValue = (String) value;
        boolean foundProperty = false;
        if (propertyName.equals("description")) {
            termHASARelationship.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("steward")) {
            termHASARelationship.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("source")) {
            termHASARelationship.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    protected boolean mapEnumToLine(Hasa termHASARelationship, String propertyName, EnumPropertyValue enumPropertyValue) {
        boolean foundProperty = false;
        if (propertyName.equals("status")) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            termHASARelationship.setStatus(status);
            foundProperty = true;
        }
        return foundProperty;
    }

    /**
     * Get proxy1 guid.
     * The proxy has omrs type GlossaryTerm
     *
     * @param termHASARelationship line
     * @return guid for entity proxy 1
     */
    @Override
    protected String getProxy1Guid(Hasa termHASARelationship) {
        return termHASARelationship.getOwningTermGuid();
    }

    /**
     * Get proxy2 guid
     * The proxy has omrs type GlossaryTerm
     *
     * @param termHASARelationship for this Line
     * @return guid for entity proxy 2
     */
    @Override
    protected String getProxy2Guid(Hasa termHASARelationship) {
        return termHASARelationship.getOwnedTermGuid();
    }

    @Override
    public String getTypeName() {
        return TERM_HASA_RELATIONSHIP;
    }

    @Override
    protected Hasa getLineInstance() {
        return new Hasa();
    }

    @Override
    protected void setEnd1GuidInLine(Hasa termHASARelationship, String guid) {
        termHASARelationship.setOwningTermGuid(guid);
    }

    @Override
    protected void setEnd2GuidInLine(Hasa termHASARelationship, String guid) {
        termHASARelationship.setOwnedTermGuid(guid);
    }
}