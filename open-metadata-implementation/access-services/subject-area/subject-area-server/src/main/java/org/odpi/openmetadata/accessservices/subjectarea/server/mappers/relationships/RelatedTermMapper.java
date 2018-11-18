/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.RelatedTermRelationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * Static mapping methods to map between the related term and the equivalent generated OMRSRelationshipBean
 */
public class RelatedTermMapper
{
    private static final Logger log = LoggerFactory.getLogger( RelatedTermMapper.class);
    private static final String className = RelatedTermMapper.class.getName();

    /**
     * map RelatedTermRelationship to the omrs relationship bean equivalent
     * @param relatedterm supplied related term relationship
     * @return omrs relationship bean equivalent
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    static public org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.RelatedTerm.RelatedTerm mapRelatedTermToOMRSRelationshipBean(RelatedTermRelationship relatedterm) throws InvalidParameterException {
        // copy over the Line attributes
        org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.RelatedTerm.RelatedTerm omrsRelationshipBean = new  org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.RelatedTerm.RelatedTerm(relatedterm);
        //Set properties
        omrsRelationshipBean.setDescription(relatedterm.getDescription());
        omrsRelationshipBean.setExpression(relatedterm.getExpression());
        omrsRelationshipBean.setSource(relatedterm.getSource());
        omrsRelationshipBean.setSteward(relatedterm.getSteward());
        omrsRelationshipBean.setStatus(relatedterm.getStatus());

        Map<String, Object> extraAttributes = omrsRelationshipBean.getExtraAttributes();
        if (extraAttributes !=null)
        {
            String[] properties = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.RelatedTerm.RelatedTerm.PROPERTY_NAMES_SET_VALUES;
            if (properties!=null && properties.length >0) {
                if (extraAttributes ==null) {
                    extraAttributes =new HashMap<>();
                }
                for (String property : properties) {
                    if (extraAttributes.containsKey(property)) {
                        extraAttributes.remove(property);
                    }
                }
                omrsRelationshipBean.setExtraAttributes(extraAttributes);
            }
        }

        return omrsRelationshipBean;
    }

    /**
     * Map omrs relationship bean equivalent to RelatedTermRelationship
     * @param omrsRelationshipBean omrs relationship bean equivalent
     * @return RelatedTermRelationship related term relationship
     */
    public static RelatedTermRelationship mapOMRSRelationshipBeanToRelatedTerm(org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.RelatedTerm.RelatedTerm omrsRelationshipBean) {
        // copy over the Line attributes
        RelatedTermRelationship relatedterm = new RelatedTermRelationship(omrsRelationshipBean);
        relatedterm.setDescription(omrsRelationshipBean.getDescription());
        relatedterm.setExpression(omrsRelationshipBean.getExpression());
        relatedterm.setSource(omrsRelationshipBean.getSource());
        relatedterm.setSteward(omrsRelationshipBean.getSteward());
        relatedterm.setStatus(omrsRelationshipBean.getStatus());
        String[] properties=org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.RelatedTerm.RelatedTerm.PROPERTY_NAMES_SET_VALUES;
        Map<String, Object> extraAttributes =relatedterm.getExtraAttributes();
        if (properties!=null && properties.length >0) {
            if (extraAttributes ==null) {
                extraAttributes =new HashMap<>();
            }
            for (String property : properties) {
                if (extraAttributes.containsKey(property)) {
                    extraAttributes.remove(property);
                }
            }
            relatedterm.setExtraAttributes(extraAttributes);
        }
        return relatedterm;
    }
}
