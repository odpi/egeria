/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.HasA;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;


/**
 * Mapping methods to map between the termHASARelationship and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class TermHasARelationshipMapper extends LineMapper<HasA> {
    public static final String TERM_HASA_RELATIONSHIP = "TermHASARelationship";

    public TermHasARelationshipMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    /**
     * Map the supplied Line to omrs InstanceProperties.
     *
     * @param termHasARelationship               supplied line
     * @param instanceProperties equivalent instance properties to the Line
     */
    @Override
    protected void mapLineToInstanceProperties(HasA termHasARelationship, InstanceProperties instanceProperties) {
        if (termHasARelationship.getDescription() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, termHasARelationship.getDescription(), "description");
        }
        if (termHasARelationship.getSteward() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, termHasARelationship.getSteward(), "steward");
        }
        if (termHasARelationship.getSource() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, termHasARelationship.getSource(), "source");
        }
        if (termHasARelationship.getStatus() != null) {
            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
            enumPropertyValue.setOrdinal(termHasARelationship.getStatus().getOrdinal());
            instanceProperties.setProperty("status", enumPropertyValue);
        }
    }

    /**
     * Map a primitive omrs property to the termHASARelationship object.
     *
     * @param termHasARelationship         the glossary to be updated
     * @param propertyName the omrs property name
     * @param value        the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the Line, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToLine(HasA termHasARelationship, String propertyName, Object value) {
        String stringValue = (String) value;
        boolean foundProperty = false;
        if (propertyName.equals("description")) {
            termHasARelationship.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("steward")) {
            termHasARelationship.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("source")) {
            termHasARelationship.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    protected boolean mapEnumToLine(HasA termHasARelationship, String propertyName, EnumPropertyValue enumPropertyValue) {
        boolean foundProperty = false;
        if (propertyName.equals("status")) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            termHasARelationship.setStatus(status);
            foundProperty = true;
        }
        return foundProperty;
    }

    /**
     * Get proxy1 guid.
     * The proxy has omrs type GlossaryTerm
     *
     * @param termHasARelationship line
     * @return guid for entity proxy 1
     */
    @Override
    protected String getProxy1Guid(HasA termHasARelationship) {
        return termHasARelationship.getOwningTermGuid();
    }

    /**
     * Get proxy2 guid
     * The proxy has omrs type GlossaryTerm
     *
     * @param termHasARelationship for this Line
     * @return guid for entity proxy 2
     */
    @Override
    protected String getProxy2Guid(HasA termHasARelationship) {
        return termHasARelationship.getOwnedTermGuid();
    }

    @Override
    public String getTypeName() {
        return TERM_HASA_RELATIONSHIP;
    }

    @Override
    protected HasA getLineInstance() {
        return new HasA();
    }

    @Override
    protected void setEnd1GuidInLine(HasA termHasARelationship, String guid) {
        termHasARelationship.setOwningTermGuid(guid);
    }

    @Override
    protected void setEnd2GuidInLine(HasA termHasARelationship, String guid) {
        termHasARelationship.setOwnedTermGuid(guid);
    }
}