/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.ValidValue;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Mapping methods to map between the validValue and the equivalent omrs Relationship.
 */
public class ValidValueMapper extends LineMapper 
{
    private static final Logger log = LoggerFactory.getLogger( ValidValueMapper.class);
    private static final String className = ValidValueMapper.class.getName();
    public static final String VALID_VALUE = "ValidValue";

    public ValidValueMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    /**
     * Map the supplied Line to omrs InstanceProperties.
     * @param line supplied line
     * @param instanceProperties equivalent instance properties to the Line
     */
    @Override
    protected void mapLineToInstanceProperties(Line line, InstanceProperties instanceProperties) {
        ValidValue validValue = (ValidValue)line;
        if (validValue.getDescription()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, validValue.getDescription(), "description");
        }
        if (validValue.getExpression()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, validValue.getExpression(), "expression");
        }
        if (validValue.getSteward()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, validValue.getSteward(), "steward");
        }
        if (validValue.getSource()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, validValue.getSource(), "source");
        }
        if (validValue.getStatus()!=null) {
            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
            enumPropertyValue.setOrdinal(validValue.getStatus().getOrdinal());
            instanceProperties.setProperty("status",enumPropertyValue);
        }
    }
    /**
     * Map a primitive omrs property to the validValue object.
     * @param line the glossary to be updated
     * @param propertyName the omrs property name
     * @param value the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the Line, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToLine(Line line, String propertyName, Object value) {
        String stringValue = (String) value;
        ValidValue validValue = (ValidValue) line;
        boolean foundProperty = false;
        if (propertyName.equals("description")) {
            validValue.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("expression")) {
            validValue.setExpression(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("steward")) {
            validValue.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("source")) {
            validValue.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }
    @Override
    protected boolean mapEnumToLine(Line line, String propertyName, EnumPropertyValue enumPropertyValue)
    {
        ValidValue validValue = (ValidValue) line;
        boolean foundProperty = false;
        if (propertyName.equals("status")) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            validValue.setStatus(status);
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
        ValidValue validValue = (ValidValue) line;
        return validValue.getTermGuid();
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
        ValidValue validValue = (ValidValue) line;
        return validValue.getValidValueGuid();
    }

    /**
     * Get the relationship type def guid.
     * @param relationship the relationship associated with the typedef whose guid is returned.
     * @return guid of the typedef
     */
    @Override
    protected String getRelationshipTypeDefGuid(Relationship relationship)
    {
        return repositoryHelper.getTypeDefByName(omrsapiHelper.getServiceName(), VALID_VALUE).getGUID();
    }
    @Override
    public String getTypeName() {
        return  VALID_VALUE;
    }
    @Override
    protected Line getLineInstance() {
        return new ValidValue();
    }
    @Override
    protected void setEnd1GuidInLine(Line line, String guid){
        ValidValue validValue = (ValidValue)line;
        validValue.setTermGuid(guid);
    }
    @Override
    protected void setEnd2GuidInLine(Line line, String guid) {
        ValidValue validValue = (ValidValue)line;
        validValue.setValidValueGuid(guid);
    }
}
