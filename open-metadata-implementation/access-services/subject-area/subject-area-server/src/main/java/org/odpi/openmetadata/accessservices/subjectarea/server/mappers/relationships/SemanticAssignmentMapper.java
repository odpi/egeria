/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermAssignmentStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.SemanticAssignment;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;

import java.util.Map;


/**
 * Mapping methods to map between the semanticAssignment and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class SemanticAssignmentMapper extends LineMapper<SemanticAssignment> {
    public static final String SEMANTIC_ASSIGNMENT = "SemanticAssignment";

    public SemanticAssignmentMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    /**
     * Map the supplied Line to omrs InstanceProperties.
     *
     * @param semanticAssignment       supplied line
     * @param properties equivalent instance properties to the Line
     */
    @Override
    protected void mapLineToInstanceProperties(SemanticAssignment semanticAssignment, InstanceProperties properties) {
        if (semanticAssignment.getDescription() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(properties, semanticAssignment.getDescription(), "description");
        }
        if (semanticAssignment.getExpression() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(properties, semanticAssignment.getExpression(), "expression");
        }
        if (semanticAssignment.getConfidence() != null) {
            SubjectAreaUtils.setIntegerPropertyInInstanceProperties(properties, semanticAssignment.getConfidence(), "confidence");
        }
        if (semanticAssignment.getSteward() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(properties, semanticAssignment.getSteward(), "steward");
        }
        if (semanticAssignment.getSource() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(properties, semanticAssignment.getSource(), "source");
        }
        if (semanticAssignment.getAssignedElementGuid() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(properties, semanticAssignment.getAssignedElementGuid(), "assignedElementGuid");
        }

        Map<String, InstancePropertyValue> instancePropertyMap = properties.getInstanceProperties();
        InstancePropertyValue instancePropertyValue = instancePropertyMap.get("status");
        if (instancePropertyValue != null) {
            EnumPropertyValue enumPropertyValue = (EnumPropertyValue) instancePropertyValue;
            TermAssignmentStatus status = TermAssignmentStatus.valueOf(enumPropertyValue.getSymbolicName());
            semanticAssignment.setStatus(status);
        }
    }

    /**
     * Map a primitive omrs property to the semanticAssignment object.
     *
     * @param semanticAssignment         the glossary to be updated
     * @param propertyName the omrs property name
     * @param value        the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the Line, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToLine(SemanticAssignment semanticAssignment, String propertyName, Object value) {
        String stringValue = (String) value;
        boolean foundProperty = false;
        if (propertyName.equals("description")) {
            semanticAssignment.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("expression")) {
            semanticAssignment.setExpression(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("confidence")) {
            semanticAssignment.setConfidence((Integer) value);
            foundProperty = true;
        }
        if (propertyName.equals("steward")) {
            semanticAssignment.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("source")) {
            semanticAssignment.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }

    @Override
    protected boolean mapEnumToLine(SemanticAssignment semanticAssignment, String propertyName, EnumPropertyValue enumPropertyValue) {
        boolean foundProperty = false;
        if (propertyName.equals("status")) {
            TermAssignmentStatus status = TermAssignmentStatus.valueOf(enumPropertyValue.getSymbolicName());
            semanticAssignment.setStatus(status);
            foundProperty = true;
        }
        return foundProperty;
    }

    /**
     * Get proxy1 guid.
     * The proxy has omrs type Referenceable
     *
     * @param semanticAssignment line
     * @return guid for entity proxy 1
     */
    @Override
    protected String getProxy1Guid(SemanticAssignment semanticAssignment) {
        return semanticAssignment.getAssignedElementGuid();
    }

    /**
     * Get proxy2 guid
     * The proxy has omrs type GlossaryTerm
     *
     * @param semanticAssignment for this Line
     * @return guid for entity proxy 2
     */
    @Override
    protected String getProxy2Guid(SemanticAssignment semanticAssignment) {
        return semanticAssignment.getTermGuid();
    }

    @Override
    public String getTypeName() {
        return SEMANTIC_ASSIGNMENT;
    }

    @Override
    protected SemanticAssignment getLineInstance() {
        return new SemanticAssignment();
    }

    @Override
    protected void setEnd1GuidInLine(SemanticAssignment semanticAssignment, String guid) {
        semanticAssignment.setAssignedElementGuid(guid);
    }

    @Override
    protected void setEnd2GuidInLine(SemanticAssignment semanticAssignment, String guid) {
        semanticAssignment.setTermGuid(guid);
    }
}
