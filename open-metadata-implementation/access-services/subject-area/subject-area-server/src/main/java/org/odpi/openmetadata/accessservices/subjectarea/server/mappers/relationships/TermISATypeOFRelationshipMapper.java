/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.IsaTypeOf;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Mapping methods to map between the termISATypeOFRelationship and the equivalent omrs Relationship.
 */
public class TermISATypeOFRelationshipMapper extends LineMapper 
{
    private static final Logger log = LoggerFactory.getLogger( TermISATypeOFRelationshipMapper.class);
    private static final String className = TermISATypeOFRelationshipMapper.class.getName();
    private static final String TERM_ISA_TYPE_OF_RELATIONSHIP = "TermISATypeOFRelationship";

    public TermISATypeOFRelationshipMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    /**
     * Map the supplied Line to omrs InstanceProperties.
     * @param line supplied line
     * @param instanceProperties equivalent instance properties to the Line
     */
    @Override
    protected void mapLineToInstanceProperties(Line line, InstanceProperties instanceProperties) {
        IsaTypeOf termISATypeOFRelationship = (IsaTypeOf)line;
        if (termISATypeOFRelationship.getDescription()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, termISATypeOFRelationship.getDescription(), "description");
        }
        if (termISATypeOFRelationship.getSteward()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, termISATypeOFRelationship.getSteward(), "steward");
        }
        if (termISATypeOFRelationship.getSource()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, termISATypeOFRelationship.getSource(), "source");
        }
        if (termISATypeOFRelationship.getStatus()!=null) {
            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
            enumPropertyValue.setOrdinal(termISATypeOFRelationship.getStatus().getOrdinal());
            instanceProperties.setProperty("status",enumPropertyValue);
        }
    }
    /**
     * Map a primitive omrs property to the termISATypeOFRelationship object.
     * @param line the glossary to be updated
     * @param propertyName the omrs property name
     * @param value the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the Line, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToLine(Line line, String propertyName, Object value) {
        String stringValue = (String) value;
       IsaTypeOf termISATypeOFRelationship = (IsaTypeOf) line;
        boolean foundProperty = false;
        if (propertyName.equals("description")) {
            termISATypeOFRelationship.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("steward")) {
            termISATypeOFRelationship.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("source")) {
            termISATypeOFRelationship.setSource(stringValue);
            foundProperty = true;
        }
        return foundProperty;
    }
    @Override
    protected boolean mapEnumToLine(Line line, String propertyName, EnumPropertyValue enumPropertyValue)
    {
        IsaTypeOf termISATypeOFRelationship = (IsaTypeOf) line;
        boolean foundProperty = false;
        if (propertyName.equals("status")) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            termISATypeOFRelationship.setStatus(status);
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
        IsaTypeOf termISATypeOFRelationship = (IsaTypeOf) line;
        return termISATypeOFRelationship.getSuperTypeGuid();
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
        IsaTypeOf termISATypeOFRelationship = (IsaTypeOf) line;
        return termISATypeOFRelationship.getSubTypeGuid();
    }

    /**
     * Get the relationship type def guid.
     * @param relationship the relationship associated with the typedef whose guid is returned.
     * @return guid of the typedef
     */
    @Override
    protected String getRelationshipTypeDefGuid(Relationship relationship)
    {
        return repositoryHelper.getTypeDefByName(omrsapiHelper.getServiceName(), TERM_ISA_TYPE_OF_RELATIONSHIP).getGUID();
    }
    @Override
    public String getTypeName() {
        return  TERM_ISA_TYPE_OF_RELATIONSHIP;
    }
    @Override
    protected Line getLineInstance() {
        return new IsaTypeOf();
    }
    @Override
    protected void setEnd1GuidInLine(Line line, String guid){
        IsaTypeOf termISATypeOFRelationship = (IsaTypeOf)line;
        termISATypeOFRelationship.setSuperTypeGuid(guid);
    }
    @Override
    protected void setEnd2GuidInLine(Line line, String guid) {
        IsaTypeOf termISATypeOFRelationship = (IsaTypeOf)line;
        termISATypeOFRelationship.setSubTypeGuid(guid);
    }
}
