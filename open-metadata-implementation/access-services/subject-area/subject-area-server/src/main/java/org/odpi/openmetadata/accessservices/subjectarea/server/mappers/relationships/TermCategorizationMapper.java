/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Categorization;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;


/**
 * Mapping methods to map between the termCategorization and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class TermCategorizationMapper extends LineMapper<Categorization> {
    public static final String TERM_CATEGORIZATION = "TermCategorization";

    public TermCategorizationMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    /**
     * Map the supplied Line to omrs InstanceProperties.
     *
     * @param termCategorization               supplied line
     * @param instanceProperties equivalent instance properties to the Line
     */
    @Override
    protected void mapLineToInstanceProperties(Categorization termCategorization, InstanceProperties instanceProperties) {
        if (termCategorization.getDescription() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, termCategorization.getDescription(), "description");
        }
        if (termCategorization.getStatus() != null) {
            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
            enumPropertyValue.setOrdinal(termCategorization.getStatus().getOrdinal());
            instanceProperties.setProperty("status", enumPropertyValue);
        }
    }

    /**
     * Map a primitive omrs property to the termCategorization object.
     *
     * @param termCategorization         the glossary to be updated
     * @param propertyName the omrs property name
     * @param value        the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the Line, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToLine(Categorization termCategorization, String propertyName, Object value) {
        String stringValue = (String) value;
        boolean foundProperty = false;
        if (propertyName.equals("description")) {
            termCategorization.setDescription(stringValue);
            foundProperty = true;
        }

        return foundProperty;
    }

    @Override
    protected boolean mapEnumToLine(Categorization termCategorization, String propertyName, EnumPropertyValue enumPropertyValue) {
        boolean foundProperty = false;
        if (propertyName.equals("status")) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            termCategorization.setStatus(status);
            foundProperty = true;
        }
        return foundProperty;
    }

    /**
     * Get proxy1 guid.
     * The proxy has omrs type GlossaryCategory
     *
     * @param termCategorization line
     * @return guid for entity proxy 1
     */
    @Override
    protected String getProxy1Guid(Categorization termCategorization) {
        return termCategorization.getCategoryGuid();
    }

    /**
     * Get proxy2 guid
     * The proxy has omrs type GlossaryTerm
     *
     * @param termCategorization for this Line
     * @return guid for entity proxy 2
     */
    @Override
    protected String getProxy2Guid(Categorization termCategorization) {
        return termCategorization.getTermGuid();
    }

    @Override
    public String getTypeName() {
        return TERM_CATEGORIZATION;
    }

    @Override
    protected Categorization getLineInstance() {
        return new Categorization();
    }

    @Override
    protected void setEnd1GuidInLine(Categorization termCategorization, String guid) {
        termCategorization.setCategoryGuid(guid);
    }

    @Override
    protected void setEnd2GuidInLine(Categorization termCategorization, String guid) {
        termCategorization.setTermGuid(guid);
    }
}