/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.ReplacementTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * Static mapping methods to map between the replacementTerm and the equivalent generated OMRSRelationshipBean
 */
public class TermReplacementMapper
{
    private static final Logger log = LoggerFactory.getLogger( TermReplacementMapper.class);
    private static final String className = TermReplacementMapper.class.getName();

    /**
     * map ReplacementTerm to the omrs relationship bean equivalent
     * @param replacementTerm supplied ReplacementTerm
     * @return omrs relationship bean equivalent
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    static public org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ReplacementTerm.ReplacementTerm mapReplacementTermToOMRSRelationshipBean(ReplacementTerm replacementTerm) throws InvalidParameterException {
        // copy over the Line attributes
        org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ReplacementTerm.ReplacementTerm omrsRelationshipBean = new  org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ReplacementTerm.ReplacementTerm(replacementTerm);
        //Set properties
        omrsRelationshipBean.setDescription(replacementTerm.getDescription());
        omrsRelationshipBean.setExpression(replacementTerm.getExpression());
        omrsRelationshipBean.setSource(replacementTerm.getSource());
        omrsRelationshipBean.setSteward(replacementTerm.getSteward());
        omrsRelationshipBean.setStatus(replacementTerm.getStatus());
        omrsRelationshipBean.setGuid(replacementTerm.getGuid());
        omrsRelationshipBean.setEntity1Guid(replacementTerm.getReplacedTermGuid());
        omrsRelationshipBean.setEntity2Guid(replacementTerm.getReplacementTermGuid());

        Map<String, Object> extraAttributes = omrsRelationshipBean.getExtraAttributes();
        if (extraAttributes !=null)
        {
            String[] properties = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ReplacementTerm.ReplacementTerm.PROPERTY_NAMES_SET_VALUES;
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
     * Map omrs relationship bean equivalent to ReplacementTerm
     * @param omrsRelationshipBean omrs relationship bean equivalent
     * @return ReplacementTerm replacementTerm
     */
    public static ReplacementTerm mapOMRSRelationshipBeanToReplacementTerm(org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ReplacementTerm.ReplacementTerm omrsRelationshipBean) {
        // copy over the Line attributes
        ReplacementTerm replacementTerm = new ReplacementTerm(omrsRelationshipBean);
        replacementTerm.setDescription(omrsRelationshipBean.getDescription());
        replacementTerm.setExpression(omrsRelationshipBean.getExpression());
        replacementTerm.setSource(omrsRelationshipBean.getSource());
        replacementTerm.setSteward(omrsRelationshipBean.getSteward());
        replacementTerm.setStatus(omrsRelationshipBean.getStatus());
        replacementTerm.setGuid(omrsRelationshipBean.getGuid());
        replacementTerm.setReplacedTermGuid(omrsRelationshipBean.getEntity1Guid());
        replacementTerm.setReplacementTermGuid(omrsRelationshipBean.getEntity2Guid());
        String[] properties=org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ReplacementTerm.ReplacementTerm.PROPERTY_NAMES_SET_VALUES;
        Map<String, Object> extraAttributes =replacementTerm.getExtraAttributes();
        if (properties!=null && properties.length >0) {
            if (extraAttributes ==null) {
                extraAttributes =new HashMap<>();
            }
            for (String property : properties) {
                if (extraAttributes.containsKey(property)) {
                    extraAttributes.remove(property);
                }
            }
            replacementTerm.setExtraAttributes(extraAttributes);
        }
        return replacementTerm;
    }
}
