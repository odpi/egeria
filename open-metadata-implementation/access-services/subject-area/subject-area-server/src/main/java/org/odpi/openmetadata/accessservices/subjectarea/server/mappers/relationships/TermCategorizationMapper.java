/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.TermCategorization;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * Static mapping methods to map between the termCategorization and the equivalent generated OMRSRelationshipBean
 */
public class TermCategorizationMapper
{
    private static final Logger log = LoggerFactory.getLogger( TermCategorizationMapper.class);
    private static final String className = TermCategorizationMapper.class.getName();

    /**
     * map TermCategorization to the omrs relationship bean equivalent
     * @param termCategorization supplied TermCategorization
     * @return omrs relationship bean equivalent
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    static public org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermCategorization.TermCategorization mapTermCategorizationToOMRSRelationshipBean(TermCategorization termCategorization) throws InvalidParameterException {
        // copy over the Line attributes
        org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermCategorization.TermCategorization omrsRelationshipBean = new  org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermCategorization.TermCategorization(termCategorization);
        //Set properties
        omrsRelationshipBean.setDescription(termCategorization.getDescription());
        omrsRelationshipBean.setStatus(termCategorization.getStatus());
        omrsRelationshipBean.setGuid(termCategorization.getGuid());

        Map<String, Object> extraAttributes = omrsRelationshipBean.getExtraAttributes();
        if (extraAttributes !=null)
        {
            String[] properties = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermCategorization.TermCategorization.PROPERTY_NAMES_SET_VALUES;
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
     * Map omrs relationship bean equivalent to TermCategorization
     * @param omrsRelationshipBean omrs relationship bean equivalent
     * @return TermCategorization termCategorization
     */
    public static TermCategorization mapOMRSRelationshipBeanToTermCategorization(org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermCategorization.TermCategorization omrsRelationshipBean) {
        // copy over the Line attributes
        TermCategorization termCategorization = new TermCategorization(omrsRelationshipBean);
        termCategorization.setDescription(omrsRelationshipBean.getDescription());
        termCategorization.setStatus(omrsRelationshipBean.getStatus());
        termCategorization.setGuid(omrsRelationshipBean.getGuid());
        String[] properties=org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermCategorization.TermCategorization.PROPERTY_NAMES_SET_VALUES;
        Map<String, Object> extraAttributes =termCategorization.getExtraAttributes();
        if (properties!=null && properties.length >0) {
            if (extraAttributes ==null) {
                extraAttributes =new HashMap<>();
            }
            for (String property : properties) {
                if (extraAttributes.containsKey(property)) {
                    extraAttributes.remove(property);
                }
            }
            termCategorization.setExtraAttributes(extraAttributes);
        }
        return termCategorization;
    }
}
