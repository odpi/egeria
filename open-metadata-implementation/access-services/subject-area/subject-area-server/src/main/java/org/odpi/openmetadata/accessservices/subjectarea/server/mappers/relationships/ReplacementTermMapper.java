/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.ReplacementTerm;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Mapping methods to map between the replacementTerm and the equivalent omrs Relationship.
 */
public class ReplacementTermMapper extends LineMapper 
{
    private static final Logger log = LoggerFactory.getLogger( ReplacementTermMapper.class);
    private static final String className = ReplacementTermMapper.class.getName();
    public static final String REPLACEMENT_TERM = "ReplacementTerm";

    public ReplacementTermMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    /**
     * Map the supplied Line to omrs InstanceProperties.
     * @param line supplied line
     * @param properties equivalent instance properties to the Line
     */
    @Override
    protected void mapLineToInstanceProperties(Line line, InstanceProperties properties) {
        ReplacementTerm replacementTerm = (ReplacementTerm)line;
        if (replacementTerm.getDescription()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(properties, replacementTerm.getDescription(), "description");
        }
        if (replacementTerm.getExpression()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(properties, replacementTerm.getExpression(), "expression");
        }
        if (replacementTerm.getSteward()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(properties, replacementTerm.getSteward(), "steward");
        }
        if (replacementTerm.getSource()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(properties, replacementTerm.getSource(), "source");
        }
        if (replacementTerm.getStatus()!=null) {
            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
            enumPropertyValue.setOrdinal(replacementTerm.getStatus().getOrdinal());
            properties.setProperty("status",enumPropertyValue);
        }
    }
    /**
     * Map a primitive omrs property to the replacementTerm object.
     * @param line the glossary to be updated
     * @param propertyName the omrs property name
     * @param value the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the Line, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToLine(Line line, String propertyName, Object value) {
        String stringValue = (String) value;
       ReplacementTerm replacementTerm = (ReplacementTerm) line;
        boolean foundProperty = false;
        if (propertyName.equals("description")) {
            replacementTerm.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("expression")) {
            replacementTerm.setExpression(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("steward")) {
            replacementTerm.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("source")) {
            replacementTerm.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }
    @Override
    protected boolean mapEnumToLine(Line line, String propertyName, EnumPropertyValue enumPropertyValue)
    {
        ReplacementTerm replacementTerm = (ReplacementTerm) line;
        boolean foundProperty = false;
        if (propertyName.equals("status")) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            replacementTerm.setStatus(status);
            foundProperty = true;
        }
        return foundProperty;
    }

    /**
     * Get proxy1 guid.
     * The proxy has omrs type GlossaryTerm
     * @param line line
     * @return guid for entity proxy 1
     */
    @Override
    protected String getProxy1Guid(Line line)
    {
        ReplacementTerm replacementTerm = (ReplacementTerm) line;
        return replacementTerm.getReplacedTermGuid();
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
        ReplacementTerm replacementTerm = (ReplacementTerm) line;
        return replacementTerm.getReplacementTermGuid();
    }

    /**
     * Get the relationship type def guid.
     * @param relationship the relationship associated with the typedef whose guid is returned.
     * @return guid of the typedef
     */
    @Override
    protected String getRelationshipTypeDefGuid(Relationship relationship)
    {
        return repositoryHelper.getTypeDefByName(omrsapiHelper.getServiceName(), REPLACEMENT_TERM).getGUID();
    }
    @Override
    public String getTypeName() {
        return  REPLACEMENT_TERM;
    }
    @Override
    protected Line getLineInstance() {
        return new ReplacementTerm();
    }
    @Override
    protected void setEnd1GuidInLine(Line line, String guid){
        ReplacementTerm replacementTerm = (ReplacementTerm)line;
        replacementTerm.setReplacedTermGuid(guid);
    }
    @Override
    protected void setEnd2GuidInLine(Line line, String guid) {
        ReplacementTerm replacementTerm = (ReplacementTerm)line;
        replacementTerm.setReplacementTermGuid(guid);
    }
}
