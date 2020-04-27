/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.enums.TermRelationshipStatus;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Categorization;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Mapping methods to map between the termCategorization and the equivalent omrs Relationship.
 */
public class TermCategorizationMapper extends LineMapper 
{
    private static final Logger log = LoggerFactory.getLogger( TermCategorizationMapper.class);
    private static final String className = TermCategorizationMapper.class.getName();
    public static final String TERM_CATEGORIZATION = "TermCategorization";

    public TermCategorizationMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }
    /**
     * Map the supplied Line to omrs InstanceProperties.
     * @param line supplied line
     * @param instanceProperties equivalent instance properties to the Line
     */
    @Override
    protected void mapLineToInstanceProperties(Line line, InstanceProperties instanceProperties) {
        Categorization termCategorization = (Categorization) line;
        if (termCategorization.getDescription()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, termCategorization.getDescription(), "description");
        }
        if (termCategorization.getStatus()!=null) {
            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
            enumPropertyValue.setOrdinal(termCategorization.getStatus().getOrdinal());
            instanceProperties.setProperty("status",enumPropertyValue);
        }
    }
    /**
     * Map a primitive omrs property to the termCategorization object.
     * @param line the glossary to be updated
     * @param propertyName the omrs property name
     * @param value the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the Line, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToLine(Line line, String propertyName, Object value) {
        String stringValue = (String) value;
       Categorization termCategorization = (Categorization) line;
        boolean foundProperty = false;
        if (propertyName.equals("description")) {
            termCategorization.setDescription(stringValue);
            foundProperty = true;
        }
        
        return foundProperty;
    }
    @Override
    protected boolean mapEnumToLine(Line line, String propertyName, EnumPropertyValue enumPropertyValue)
    {
        Categorization termCategorizatio = (Categorization) line;
        boolean foundProperty = false;
        if (propertyName.equals("status")) {
            TermRelationshipStatus status = TermRelationshipStatus.valueOf(enumPropertyValue.getSymbolicName());
            termCategorizatio.setStatus(status);
            foundProperty = true;
        }
        return foundProperty;
    }

    /**
     * Get proxy1 guid.
     * The proxy has omrs type GlossaryCategory
     * @param line line
     * @return guid for entity proxy 1
     */
    @Override
    protected String getProxy1Guid(Line line)
    {
        Categorization termCategorization = (Categorization) line;
        return termCategorization.getCategoryGuid();
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
        Categorization termCategorization = (Categorization) line;
        return termCategorization.getTermGuid();
    }

    /**
     * Get the relationship type def guid.
     * @param relationship the relationship associated with the typedef whose guid is returned.
     * @return guid of the typedef
     */
    @Override
    protected String getRelationshipTypeDefGuid(Relationship relationship)
    {
        return repositoryHelper.getTypeDefByName(omrsapiHelper.getServiceName(), TERM_CATEGORIZATION).getGUID();
    }
    @Override
    public String getTypeName() {
        return  TERM_CATEGORIZATION;
    }
    @Override
    protected Line getLineInstance() {
        return new Categorization();
    }
    @Override
    protected void setEnd1GuidInLine(Line line, String guid){
        Categorization termCategorizationRelationship = (Categorization)line;
        termCategorizationRelationship.setCategoryGuid(guid);
    }
    @Override
    protected void setEnd2GuidInLine(Line line, String guid) {
        Categorization termCategorizationRelationship = (Categorization)line;
        termCategorizationRelationship.setTermGuid(guid);
    }
}
