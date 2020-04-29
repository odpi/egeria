/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.ConfidentialityLevel;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.GovernanceClassificationStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Synonym;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Synonym;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.ILineMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstancePropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.repositoryconnector.OMRSRepositoryHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;


/**
 * Mapping methods to map between the synonym and the equivalent omrs Relationship.
 */
public class SynonymMapper extends LineMapper 
{
    private static final Logger log = LoggerFactory.getLogger( SynonymMapper.class);
    private static final String className = SynonymMapper.class.getName();
    public static final String SYNONYM = "Synonym";

    public SynonymMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }
    /**
     * Map the supplied Line to omrs InstanceProperties.
     * @param line supplied line
     * @param instanceProperties equivalent instance properties to the Line
     */
    @Override
    protected void mapLineToInstanceProperties(Line line, InstanceProperties instanceProperties) {
        Synonym synonym = (Synonym)line;
        if (synonym.getDescription()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, synonym.getDescription(), "description");
        }
        if (synonym.getExpression()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, synonym.getExpression(), "expression");
        }
        if (synonym.getSteward()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, synonym.getSteward(), "steward");
        }
        if (synonym.getSource()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, synonym.getSource(), "source");
        }
        if (synonym.getStatus()!=null) {
            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
            enumPropertyValue.setOrdinal(synonym.getStatus().getOrdinal());
            instanceProperties.setProperty("status",enumPropertyValue);
        }
    }
    /**
     * Map a primitive omrs property to the synonym object.
     * @param line the glossary to be updated
     * @param propertyName the omrs property name
     * @param value the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the Line, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToLine(Line line, String propertyName, Object value) {
        String stringValue = (String) value;
       Synonym synonym = (Synonym) line;
        boolean foundProperty = false;
        if (propertyName.equals("description")) {
            synonym.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("expression")) {
            synonym.setExpression(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("steward")) {
            synonym.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("source")) {
            synonym.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }
    @Override
    protected boolean mapEnumToLine(Line line, String propertyName, EnumPropertyValue enumPropertyValue)
    {
        Synonym synonym = (Synonym) line;
        boolean foundProperty = false;
        if (propertyName.equals("status")) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            synonym.setStatus(status);
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
        Synonym synonym = (Synonym) line;
        return synonym.getSynonym1Guid();
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
        Synonym synonym = (Synonym) line;
        return synonym.getSynonym2Guid();
    }

    /**
     * Get the relationship type def guid.
     * @param relationship the relationship associated with the typedef whose guid is returned.
     * @return guid of the typedef
     */
    @Override
    protected String getRelationshipTypeDefGuid(Relationship relationship)
    {
        return repositoryHelper.getTypeDefByName(omrsapiHelper.getServiceName(), SYNONYM).getGUID();
    }
    @Override
    public String getTypeName() {
        return  SYNONYM;
    }
    @Override
    protected Line getLineInstance() {
        return new Synonym();
    }
    @Override
    protected void setEnd1GuidInLine(Line line, String guid){
        Synonym synonym =(Synonym) line;
        synonym.setSynonym1Guid(guid);
    }
    @Override
    protected void setEnd2GuidInLine(Line line, String guid) {
        Synonym synonym =(Synonym) line;
        synonym.setSynonym2Guid(guid);
    }
}
