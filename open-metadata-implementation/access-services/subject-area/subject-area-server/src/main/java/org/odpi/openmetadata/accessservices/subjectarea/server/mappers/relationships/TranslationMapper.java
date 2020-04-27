/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.graph.Line;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Translation;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Translation;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.Relationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * Mapping methods to map between the translation and the equivalent omrs Relationship.
 */
public class TranslationMapper extends LineMapper 
{
    private static final Logger log = LoggerFactory.getLogger( TranslationMapper.class);
    private static final String className = TranslationMapper.class.getName();
    public static final String TRANSLATION = "Translation";

    public TranslationMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    /**
     * Map the supplied Line to omrs InstanceProperties.
     * @param line supplied line
     * @param instanceProperties equivalent instance properties to the Line
     */
    @Override
    protected void mapLineToInstanceProperties(Line line, InstanceProperties instanceProperties) {
        Translation translation = (Translation)line;
        if (translation.getDescription()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, translation.getDescription(), "description");
        }
        if (translation.getExpression()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, translation.getExpression(), "expression");
        }
        if (translation.getSteward()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, translation.getSteward(), "steward");
        }
        if (translation.getSource()!=null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, translation.getSource(), "source");
        }
        if (translation.getStatus()!=null) {
            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
            enumPropertyValue.setOrdinal(translation.getStatus().getOrdinal());
            instanceProperties.setProperty("status",enumPropertyValue);
        }
    }
    /**
     * Map a primitive omrs property to the translation object.
     * @param line the glossary to be updated
     * @param propertyName the omrs property name
     * @param value the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the Line, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToLine(Line line, String propertyName, Object value) {
        String stringValue = (String) value;
       Translation translation = (Translation) line;
        boolean foundProperty = false;
        if (propertyName.equals("description")) {
            translation.setDescription(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("expression")) {
            translation.setExpression(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("steward")) {
            translation.setSteward(stringValue);
            foundProperty = true;
        }
        if (propertyName.equals("source")) {
            translation.setSource(stringValue);
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
        Translation translation = (Translation) line;
        return translation.getTranslation1Guid();
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
        Translation translation = (Translation) line;
        return translation.getTranslation2Guid();
    }

    /**
     * Get the relationship type def guid.
     * @param relationship the relationship associated with the typedef whose guid is returned.
     * @return guid of the typedef
     */
    @Override
    protected String getRelationshipTypeDefGuid(Relationship relationship)
    {
        return repositoryHelper.getTypeDefByName(omrsapiHelper.getServiceName(), TRANSLATION).getGUID();
    }
    @Override
    public String getTypeName() {
        return  TRANSLATION;
    }
    @Override
    protected Line getLineInstance() {
        return new Translation();
    }
    @Override
    protected void setEnd1GuidInLine(Line line, String guid){
        Translation translation =(Translation)line;
        translation.setTranslation1Guid(guid);
    }
    @Override
    protected void setEnd2GuidInLine(Line line, String guid) {
        Translation translation =(Translation)line;
        translation.setTranslation2Guid(guid);
    }
}
