/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.RelatedTerm;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;


/**
 * Mapping methods to map between the relatedTerm and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class RelatedTermMapper extends RelationshipMapper<RelatedTerm> {
    public static final String RELATED_TERM = "RelatedTerm";

    public RelatedTermMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    /**
     * Map the supplied relationship to omrs InstanceProperties.
     *
     * @param relatedTerm               supplied relationship
     * @param instanceProperties equivalent instance properties to the relationship
     */
    @Override
    protected void mapRelationshipToInstanceProperties(RelatedTerm relatedTerm, InstanceProperties instanceProperties) {
        if (relatedTerm.getDescription() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, relatedTerm.getDescription(), "description");
        }
        if (relatedTerm.getExpression() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, relatedTerm.getExpression(), "expression");
        }
        if (relatedTerm.getSteward() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, relatedTerm.getSteward(), "steward");
        }
        if (relatedTerm.getSource() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, relatedTerm.getSource(), "source");
        }
        if (relatedTerm.getStatus() != null) {
            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
            enumPropertyValue.setOrdinal(relatedTerm.getStatus().getOrdinal());
            instanceProperties.setProperty("status", enumPropertyValue);
        }
    }

    /**
     * Map a primitive omrs property to the relatedTerm object.
     *
     * @param relatedTerm         the omas relationship to be updated
     * @param propertyName the omrs property name
     * @param value        the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the relationship, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToRelationship(RelatedTerm relatedTerm, String propertyName, Object value) {
        String stringValue = (String) value;
        boolean foundProperty = false;
        if (propertyName.equals("description")) {
            relatedTerm.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("expression")) {
            relatedTerm.setExpression(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("steward")) {
            relatedTerm.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("source")) {
            relatedTerm.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    protected boolean mapEnumToRelationship(RelatedTerm relatedTerm, String propertyName, EnumPropertyValue enumPropertyValue) {
        boolean foundProperty = false;
        if (propertyName.equals("status")) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            relatedTerm.setStatus(status);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    public String getTypeName() {
        return RELATED_TERM;
    }

    @Override
    protected RelatedTerm getRelationshipInstance() {
        return new RelatedTerm();
    }
}
