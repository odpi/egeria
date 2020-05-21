/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermAssignmentStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.SemanticAssignment;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


/**
 * Mapping methods to map between the semanticAssignment and the equivalent omrs Relationship.
 */
public class SemanticAssignmentMapper extends LineMapper 
{
    private static final Logger log = LoggerFactory.getLogger( SemanticAssignmentMapper.class);
    private static final String className = SemanticAssignmentMapper.class.getName();
    public static final String SEMANTIC_ASSIGNMENT = "SemanticAssignment";

    public SemanticAssignmentMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    /**
     * Map the supplied Line to omrs InstanceProperties.
     * @param line supplied line
     * @param properties equivalent instance properties to the Line
     */
    @Override
    protected void mapLineToInstanceProperties(Line line, InstanceProperties properties) {
        SemanticAssignment semanticAssignment = (SemanticAssignment)line;
        if (semanticAssignment.getDescription()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(properties, semanticAssignment.getDescription(), "description");
        }
        if (semanticAssignment.getExpression()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(properties, semanticAssignment.getExpression(), "expression");
        }
        if (semanticAssignment.getConfidence()!=null) {
            SubjectAreaUtils.setIntegerPropertyInInstanceProperties(properties, semanticAssignment.getConfidence(), "confidence");
        }
        if (semanticAssignment.getSteward()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(properties, semanticAssignment.getSteward(), "steward");
        }
        if (semanticAssignment.getSource()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(properties, semanticAssignment.getSource(), "source");
        }
        if (semanticAssignment.getAssignedElementGuid()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(properties, semanticAssignment.getAssignedElementGuid(), "assignedElementGuid");
        }

        Map<String, InstancePropertyValue> instancePropertyMap = properties.getInstanceProperties();
        InstancePropertyValue instancePropertyValue = instancePropertyMap.get("status");
        if (instancePropertyValue!=null) {
            EnumPropertyValue enumPropertyValue = (EnumPropertyValue) instancePropertyValue;
            TermAssignmentStatus status = TermAssignmentStatus.valueOf(enumPropertyValue.getSymbolicName());
            semanticAssignment.setStatus(status);
        }
    }
    /**
     * Map a primitive omrs property to the semanticAssignment object.
     * @param line the glossary to be updated
     * @param propertyName the omrs property name
     * @param value the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the Line, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToLine(Line line, String propertyName, Object value) {
        String stringValue = (String) value;
        SemanticAssignment semanticAssignment = (SemanticAssignment) line;
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
            semanticAssignment.setConfidence((Integer)value);
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
    protected boolean mapEnumToLine(Line line, String propertyName, EnumPropertyValue enumPropertyValue)
    {
        SemanticAssignment semanticAssignment = (SemanticAssignment) line;
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
     * @param line line
     * @return guid for entity proxy 1
     */
    @Override
    protected String getProxy1Guid(Line line)
    {
        SemanticAssignment semanticAssignment = (SemanticAssignment) line;
        return semanticAssignment.getAssignedElementGuid();
    }

    /**
     * Get proxy2 guid
     * The proxy has omrs type GlossaryTerm
     * @param line for this Line
     * @return guid for entity proxy 2
     */
    @Override
    protected String getProxy2Guid(Line line)
    {
        SemanticAssignment semanticAssignment = (SemanticAssignment) line;
        return semanticAssignment.getTermGuid();
    }

    /**
     * Get the relationship type def guid.
     * @param relationship the relationship associated with the typedef whose guid is returned.
     * @return guid of the typedef
     */
    @Override
    protected String getRelationshipTypeDefGuid(Relationship relationship)
    {
        return repositoryHelper.getTypeDefByName(omrsapiHelper.getServiceName(), SEMANTIC_ASSIGNMENT).getGUID();
    }
    @Override
    public String getTypeName() {
        return  SEMANTIC_ASSIGNMENT;
    }
    @Override
    protected Line getLineInstance() {
        return new SemanticAssignment();
    }
    @Override
    protected void setEnd1GuidInLine(Line line, String guid){
        SemanticAssignment semanticAssignment = (SemanticAssignment)line;
        semanticAssignment.setAssignedElementGuid(guid);
    }
    @Override
    protected void setEnd2GuidInLine(Line line, String guid) {
        SemanticAssignment semanticAssignment = (SemanticAssignment)line;
        semanticAssignment.setTermGuid(guid);
    }
}
