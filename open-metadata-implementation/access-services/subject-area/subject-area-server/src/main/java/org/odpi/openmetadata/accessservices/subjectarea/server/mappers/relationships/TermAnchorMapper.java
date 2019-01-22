/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.TermAnchorRelationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * Static mapping methods to map between the termAnchor and the equivalent generated OMRSRelationshipBean
 */
public class TermAnchorMapper
{
    private static final Logger log = LoggerFactory.getLogger( TermAnchorMapper.class);
    private static final String className = TermAnchorMapper.class.getName();

    /**
     * map TermAnchorRelationship to the omrs relationship bean equivalent
     * @param termAnchorRelationship supplied TermAnchorRelationship
     * @return omrs relationship bean equivalent
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    static public org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermAnchor.TermAnchor mapTermAnchorToOMRSRelationshipBean(TermAnchorRelationship termAnchorRelationship) throws InvalidParameterException {
        // copy over the Line attributes
        org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermAnchor.TermAnchor omrsRelationshipBean = new  org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermAnchor.TermAnchor(termAnchorRelationship);
        //Set properties
        omrsRelationshipBean.setGuid(termAnchorRelationship.getGuid());
        omrsRelationshipBean.setEntity1Guid(termAnchorRelationship.getGlossaryGuid());
        omrsRelationshipBean.setEntity2Guid(termAnchorRelationship.getTermGuid());

        Map<String, Object> extraAttributes = omrsRelationshipBean.getExtraAttributes();
        if (extraAttributes !=null)
        {
            String[] properties = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermAnchor.TermAnchor.PROPERTY_NAMES_SET_VALUES;
            if (properties!=null && properties.length >0) {
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
     * Map omrs relationship bean equivalent to TermAnchorRelationship
     * @param omrsRelationshipBean omrs relationship bean equivalent
     * @return TermAnchorRelationship termAnchor
     */
    public static TermAnchorRelationship mapOMRSRelationshipBeanToTermAnchor(org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermAnchor.TermAnchor omrsRelationshipBean) {
        // copy over the Line attributes
        TermAnchorRelationship termAnchorRelationship = new TermAnchorRelationship(omrsRelationshipBean);
        termAnchorRelationship.setGuid(omrsRelationshipBean.getGuid());
        termAnchorRelationship.setGlossaryGuid((omrsRelationshipBean.getEntity1Guid()));
        termAnchorRelationship.setTermGuid((omrsRelationshipBean.getEntity2Guid()));
        String[] properties=org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermAnchor.TermAnchor.PROPERTY_NAMES_SET_VALUES;
        Map<String, Object> extraAttributes = termAnchorRelationship.getExtraAttributes();
        if (properties!=null && properties.length >0) {
            if (extraAttributes ==null) {
                extraAttributes =new HashMap<>();
            }
            for (String property : properties) {
                if (extraAttributes.containsKey(property)) {
                    extraAttributes.remove(property);
                }
            }
            termAnchorRelationship.setExtraAttributes(extraAttributes);
        }
        return termAnchorRelationship;
    }
}
