/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Translation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * Static mapping methods to map between the translation and the equivalent generated OMRSRelationshipBean
 */
public class TranslationMapper
{
    private static final Logger log = LoggerFactory.getLogger( TranslationMapper.class);
    private static final String className = TranslationMapper.class.getName();

    /**
     * map Translation to the omrs relationship bean equivalent
     * @param translation supplied Translation
     * @return omrs relationship bean equivalent
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    static public org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Translation.Translation mapTranslationToOMRSRelationshipBean(Translation translation) throws InvalidParameterException {
        // copy over the Line attributes
        org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Translation.Translation omrsRelationshipBean = new  org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Translation.Translation(translation);
        //Set properties
        omrsRelationshipBean.setDescription(translation.getDescription());
        omrsRelationshipBean.setExpression(translation.getExpression());
        omrsRelationshipBean.setSource(translation.getSource());
        omrsRelationshipBean.setSteward(translation.getSteward());
        omrsRelationshipBean.setStatus(translation.getStatus());
        omrsRelationshipBean.setGuid(translation.getGuid());
        omrsRelationshipBean.setEntity1Guid(translation.getTranslation1Guid());
        omrsRelationshipBean.setEntity2Guid(translation.getTranslation2Guid());

        Map<String, Object> extraAttributes = omrsRelationshipBean.getExtraAttributes();
        if (extraAttributes !=null)
        {
            String[] properties = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Translation.Translation.PROPERTY_NAMES_SET_VALUES;
            for (String property : properties)
            {
                if (extraAttributes.containsKey(property))
                {
                    extraAttributes.remove(property);
                }
            }
            omrsRelationshipBean.setExtraAttributes(extraAttributes);
        }

        return omrsRelationshipBean;
    }

    /**
     * Map omrs relationship bean equivalent to Translation
     * @param omrsRelationshipBean omrs relationship bean equivalent
     * @return Translation translation
     */
    public static Translation mapOMRSRelationshipBeanToTranslation(org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Translation.Translation omrsRelationshipBean) {
        // copy over the Line attributes
        Translation translation = new Translation(omrsRelationshipBean);
        translation.setDescription(omrsRelationshipBean.getDescription());
        translation.setExpression(omrsRelationshipBean.getExpression());
        translation.setSource(omrsRelationshipBean.getSource());
        translation.setSteward(omrsRelationshipBean.getSteward());
        translation.setStatus(omrsRelationshipBean.getStatus());
        translation.setGuid(omrsRelationshipBean.getGuid());
        translation.setTranslation1Guid(omrsRelationshipBean.getEntity1Guid());
        translation.setTranslation2Guid(omrsRelationshipBean.getEntity2Guid());
        String[] properties=org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Translation.Translation.PROPERTY_NAMES_SET_VALUES;
        Map<String, Object> extraAttributes =translation.getExtraAttributes();
        if (properties!=null && properties.length >0) {
            if (extraAttributes ==null) {
                extraAttributes =new HashMap<>();
            }
            for (String property : properties) {
                if (extraAttributes.containsKey(property)) {
                    extraAttributes.remove(property);
                }
            }
            translation.setExtraAttributes(extraAttributes);
        }
        return translation;
    }
}
