/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Translation;
import org.odpi.openmetadata.accessservices.subjectarea.server.mappers.SubjectAreaMapper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.OMRSAPIHelper;
import org.odpi.openmetadata.accessservices.subjectarea.utilities.SubjectAreaUtils;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.EnumPropertyValue;
import org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances.InstanceProperties;


/**
 * Mapping methods to map between the translation and the equivalent omrs Relationship.
 */
@SubjectAreaMapper
public class TranslationMapper extends LineMapper<Translation> {
    public static final String TRANSLATION = "Translation";

    public TranslationMapper(OMRSAPIHelper omrsapiHelper) {
        super(omrsapiHelper);
    }

    /**
     * Map the supplied Line to omrs InstanceProperties.
     *
     * @param translation        supplied line
     * @param instanceProperties equivalent instance properties to the Line
     */
    @Override
    protected void mapLineToInstanceProperties(Translation translation, InstanceProperties instanceProperties) {
        if (translation.getDescription() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, translation.getDescription(), "description");
        }
        if (translation.getExpression() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, translation.getExpression(), "expression");
        }
        if (translation.getSteward() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, translation.getSteward(), "steward");
        }
        if (translation.getSource() != null) {
            SubjectAreaUtils.setStringPropertyInInstanceProperties(instanceProperties, translation.getSource(), "source");
        }
        if (translation.getStatus() != null) {
            EnumPropertyValue enumPropertyValue = new EnumPropertyValue();
            enumPropertyValue.setOrdinal(translation.getStatus().getOrdinal());
            instanceProperties.setProperty("status", enumPropertyValue);
        }
    }

    /**
     * Map a primitive omrs property to the translation object.
     *
     * @param translation  the glossary to be updated
     * @param propertyName the omrs property name
     * @param value        the omrs primitive property value
     * @return true if the propertyName was recognised and mapped to the Line, otherwise false
     */
    @Override
    protected boolean mapPrimitiveToLine(Translation translation, String propertyName, Object value) {
        String stringValue = (String) value;
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
     *
     * @param translation line
     * @return guid for entity proxy 1
     */
    @Override
    protected String getProxy1Guid(Translation translation) {
        return translation.getTranslation1Guid();
    }

    /**
     * Get proxy2 guid
     * The proxy has omrs type GlossaryTerm
     *
     * @param translation for this Line
     * @return guid for entity proxy 2
     */
    @Override
    protected String getProxy2Guid(Translation translation) {
        return translation.getTranslation2Guid();
    }

    @Override
    public String getTypeName() {
        return TRANSLATION;
    }

    @Override
    protected Translation getLineInstance() {
        return new Translation();
    }

    @Override
    protected void setEnd1GuidInLine(Translation translation, String guid) {
        translation.setTranslation1Guid(guid);
    }

    @Override
    protected void setEnd2GuidInLine(Translation translation, String guid) {
        translation.setTranslation2Guid(guid);
    }
}