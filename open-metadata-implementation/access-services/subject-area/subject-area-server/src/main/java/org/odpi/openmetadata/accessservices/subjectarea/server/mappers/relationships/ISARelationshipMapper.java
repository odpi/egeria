/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Isa;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Mapping methods to map between the iSARelationship and the equivalent omrs Relationship.
 */
public class ISARelationshipMapper extends LineMapper 
{
    private static final Logger log = LoggerFactory.getLogger( ISARelationshipMapper.class);
    private static final String className = ISARelationshipMapper.class.getName();
    public static final String ISA_RELATIONSHIP = "ISARelationship";

    public ISARelationshipMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    

    /**
     * Map the supplied Line to omrs InstanceProperties.
     * @param line supplied line
     * @param instanceProperties equivalent instance properties to the Line
     */
    @Override
    protected void mapLineToInstanceProperties(Line line, InstanceProperties instanceProperties) {
        Isa iSARelationship = (Isa)line;
        if (iSARelationship.getDescription()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, iSARelationship.getDescription(), "description");
        }
        if (iSARelationship.getExpression()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, iSARelationship.getExpression(), "expression");
        }
        if (iSARelationship.getSteward()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, iSARelationship.getSteward(), "steward");
        }
        if (iSARelationship.getSource()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, iSARelationship.getSource(), "source");
        }
        if (iSARelationship.getStatus()!=null) {
            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
            enumPropertyValue.setOrdinal(iSARelationship.getStatus().getOrdinal());
            instanceProperties.setProperty("status",enumPropertyValue);
        }
    }

    /**
     * Map a primitive omrs property to the iSARelationship object.
     * @param line the glossary to be updated
     * @param propertyName the omrs property name
     * @param value the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the Line, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToLine(Line line, String propertyName, Object value) {
        String stringValue = (String) value;
        Isa iSARelationship = (Isa) line;
        boolean foundProperty = false;
        if (propertyName.equals("description")) {
            iSARelationship.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("expression")) {
            iSARelationship.setExpression(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("steward")) {
            iSARelationship.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("source")) {
            iSARelationship.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }
    @Override
    protected boolean mapEnumToLine(Line line, String propertyName, EnumPropertyValue enumPropertyValue)
    {
        Isa iSARelationship = (Isa) line;
        boolean foundProperty = false;
        if (propertyName.equals("status")) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            iSARelationship.setStatus(status);
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
        Isa iSARelationship = (Isa) line;
        return iSARelationship.getTermGuid();
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
        Isa iSARelationship = (Isa) line;
        return iSARelationship.getSpecialisedTermGuid();
    }

    /**
     * Get the relationship type def guid.
     * @param relationship the relationship associated with the typedef whose guid is returned.
     * @return guid of the typedef
     */
    @Override
    protected String getRelationshipTypeDefGuid(Relationship relationship)
    {
        return repositoryHelper.getTypeDefByName(omrsapiHelper.getServiceName(), ISA_RELATIONSHIP).getGUID();
    }
    @Override
    public String getTypeName() {
        return  ISA_RELATIONSHIP;
    }
    @Override
    protected Line getLineInstance() {
        return new Isa();
    }
    @Override
    protected void setEnd1GuidInLine(Line line, String guid){
        Isa isaRelationship = (Isa)line;
        isaRelationship.setTermGuid(guid);
    }
    @Override
    protected void setEnd2GuidInLine(Line line, String guid) {
        Isa isaRelationship = (Isa)line;
        isaRelationship.setSpecialisedTermGuid(guid);
    }
}
