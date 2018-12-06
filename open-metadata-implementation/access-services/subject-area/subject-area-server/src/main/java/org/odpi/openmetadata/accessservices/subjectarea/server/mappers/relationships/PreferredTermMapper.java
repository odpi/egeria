/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.PreferredTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * Static mapping methods to map between the preferredTerm and the equivalent generated OMRSRelationshipBean
 */
public class PreferredTermMapper
{
    private static final Logger log = LoggerFactory.getLogger( PreferredTermMapper.class);
    private static final String className = PreferredTermMapper.class.getName();

    /**
     * map PreferredTerm to the omrs relationship bean equivalent
     * @param preferredTerm supplied PreferredTerm
     * @return omrs relationship bean equivalent
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    static public org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.PreferredTerm.PreferredTerm mapPreferredTermToOMRSRelationshipBean(PreferredTerm preferredTerm) throws InvalidParameterException {
        // copy over the Line attributes
        org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.PreferredTerm.PreferredTerm omrsRelationshipBean = new  org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.PreferredTerm.PreferredTerm(preferredTerm);
        //Set properties
        omrsRelationshipBean.setDescription(preferredTerm.getDescription());
        omrsRelationshipBean.setExpression(preferredTerm.getExpression());
        omrsRelationshipBean.setSource(preferredTerm.getSource());
        omrsRelationshipBean.setSteward(preferredTerm.getSteward());
        omrsRelationshipBean.setStatus(preferredTerm.getStatus());
        omrsRelationshipBean.setGuid(preferredTerm.getGuid());
        omrsRelationshipBean.setEntity1Guid(preferredTerm.getAlternateTermGuid());
        omrsRelationshipBean.setEntity2Guid(preferredTerm.getPreferredTermGuid());

        Map<String, Object> extraAttributes = omrsRelationshipBean.getExtraAttributes();
        if (extraAttributes !=null)
        {
            String[] properties = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.PreferredTerm.PreferredTerm.PROPERTY_NAMES_SET_VALUES;
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
     * Map omrs relationship bean equivalent to PreferredTerm
     * @param omrsRelationshipBean omrs relationship bean equivalent
     * @return PreferredTerm preferredTerm
     */
    public static PreferredTerm mapOMRSRelationshipBeanToPreferredTerm(org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.PreferredTerm.PreferredTerm omrsRelationshipBean) {
        // copy over the Line attributes
        PreferredTerm preferredTerm = new PreferredTerm(omrsRelationshipBean);
        preferredTerm.setDescription(omrsRelationshipBean.getDescription());
        preferredTerm.setExpression(omrsRelationshipBean.getExpression());
        preferredTerm.setSource(omrsRelationshipBean.getSource());
        preferredTerm.setSteward(omrsRelationshipBean.getSteward());
        preferredTerm.setStatus(omrsRelationshipBean.getStatus());
        preferredTerm.setGuid(omrsRelationshipBean.getGuid());
        preferredTerm.setAlternateTermGuid(omrsRelationshipBean.getEntity1Guid());
        preferredTerm.setPreferredTermGuid(omrsRelationshipBean.getEntity2Guid());
        String[] properties=org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.PreferredTerm.PreferredTerm.PROPERTY_NAMES_SET_VALUES;
        Map<String, Object> extraAttributes =preferredTerm.getExtraAttributes();
        if (properties!=null && properties.length >0) {
            if (extraAttributes ==null) {
                extraAttributes =new HashMap<>();
            }
            for (String property : properties) {
                if (extraAttributes.containsKey(property)) {
                    extraAttributes.remove(property);
                }
            }
            preferredTerm.setExtraAttributes(extraAttributes);
        }
        return preferredTerm;
    }
}
