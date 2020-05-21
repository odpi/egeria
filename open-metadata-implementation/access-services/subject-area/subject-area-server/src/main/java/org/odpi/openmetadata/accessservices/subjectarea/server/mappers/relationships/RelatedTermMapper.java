/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.RelatedTerm;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Mapping methods to map between the relatedTerm and the equivalent omrs Relationship.
 */
public class RelatedTermMapper extends LineMapper 
{
    private static final Logger log = LoggerFactory.getLogger( RelatedTermMapper.class);
    private static final String className = RelatedTermMapper.class.getName();
    public static final String RELATED_TERM = "RelatedTerm";

    public RelatedTermMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    /**
     * Map the supplied Line to omrs InstanceProperties.
     * @param line supplied line
     * @param instanceProperties equivalent instance properties to the Line
     */
    @Override
    protected void mapLineToInstanceProperties(Line line, InstanceProperties instanceProperties) {
        RelatedTerm relatedTerm = (RelatedTerm)line;
        if (relatedTerm.getDescription()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, relatedTerm.getDescription(), "description");
        }
        if (relatedTerm.getExpression()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, relatedTerm.getExpression(), "expression");
        }
        if (relatedTerm.getSteward()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, relatedTerm.getSteward(), "steward");
        }
        if (relatedTerm.getSource()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, relatedTerm.getSource(), "source");
        }
        if (relatedTerm.getStatus()!=null) {
            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
            enumPropertyValue.setOrdinal(relatedTerm.getStatus().getOrdinal());
            instanceProperties.setProperty("status",enumPropertyValue);
        }
    }
    /**
     * Map a primitive omrs property to the relatedTerm object.
     * @param line the glossary to be updated
     * @param propertyName the omrs property name
     * @param value the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the Line, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToLine(Line line, String propertyName, Object value) {
        String stringValue = (String) value;
       RelatedTerm relatedTerm = (RelatedTerm) line;
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
    protected boolean mapEnumToLine(Line line, String propertyName, EnumPropertyValue enumPropertyValue)
    {
        RelatedTerm relatedTerm = (RelatedTerm) line;
        boolean foundProperty = false;
        if (propertyName.equals("status")) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            relatedTerm.setStatus(status);
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
        RelatedTerm relatedTerm = (RelatedTerm) line;
        return relatedTerm.getRelatedTerm1Guid();
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
        RelatedTerm relatedTerm = (RelatedTerm) line;
        return relatedTerm.getRelatedTerm2Guid();
    }

    /**
     * Get the relationship type def guid.
     * @param relationship the relationship associated with the typedef whose guid is returned.
     * @return guid of the typedef
     */
    @Override
    protected String getRelationshipTypeDefGuid(Relationship relationship)
    {
        return repositoryHelper.getTypeDefByName(omrsapiHelper.getServiceName(), RELATED_TERM).getGUID();
    }
    @Override
    public String getTypeName() {
        return  RELATED_TERM;
    }
    @Override
    protected Line getLineInstance() {
        return new RelatedTerm();
    }
    @Override
    protected void setEnd1GuidInLine(Line line, String guid){
        RelatedTerm relatedTerm = (RelatedTerm)line;
        relatedTerm.setRelatedTerm1Guid(guid);
    }
    @Override
    protected void setEnd2GuidInLine(Line line, String guid) {
        RelatedTerm relatedTerm = (RelatedTerm)line;
        relatedTerm.setRelatedTerm2Guid(guid);
    }
}
