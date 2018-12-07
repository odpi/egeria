/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.TermTYPEDBYRelationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * Static mapping methods to map between the termTYPEDBYRelationship and the equivalent generated OMRSRelationshipBean
 */
public class TypedByMapper
{
    private static final Logger log = LoggerFactory.getLogger( TypedByMapper.class);
    private static final String className = TypedByMapper.class.getName();

    /**
     * map TermTYPEDBYRelationship to the omrs relationship bean equivalent
     * @param termTYPEDBYRelationship supplied TermTYPEDBYRelationship
     * @return omrs relationship bean equivalent
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    static public org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermTYPEDBYRelationship.TermTYPEDBYRelationship mapTermTYPEDBYRelationshipToOMRSRelationshipBean(TermTYPEDBYRelationship termTYPEDBYRelationship) throws InvalidParameterException {
        // copy over the Line attributes
        org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermTYPEDBYRelationship.TermTYPEDBYRelationship omrsRelationshipBean = new  org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermTYPEDBYRelationship.TermTYPEDBYRelationship(termTYPEDBYRelationship);
        //Set properties
        omrsRelationshipBean.setDescription(termTYPEDBYRelationship.getDescription());
        omrsRelationshipBean.setSource(termTYPEDBYRelationship.getSource());
        omrsRelationshipBean.setSteward(termTYPEDBYRelationship.getSteward());
        omrsRelationshipBean.setStatus(termTYPEDBYRelationship.getStatus());
        omrsRelationshipBean.setGuid(termTYPEDBYRelationship.getGuid());
        omrsRelationshipBean.setEntity1Guid(termTYPEDBYRelationship.getAttributeGuid());
        omrsRelationshipBean.setEntity2Guid(termTYPEDBYRelationship.getTypeGuid());
        Map<String, Object> extraAttributes = omrsRelationshipBean.getExtraAttributes();
        if (extraAttributes !=null)
        {
            String[] properties = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermTYPEDBYRelationship.TermTYPEDBYRelationship.PROPERTY_NAMES_SET_VALUES;
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
     * Map omrs relationship bean equivalent to TermTYPEDBYRelationship
     * @param omrsRelationshipBean omrs relationship bean equivalent
     * @return TermTYPEDBYRelationship termTYPEDBYRelationship
     */
    public static TermTYPEDBYRelationship mapOMRSRelationshipBeanToTermTYPEDBYRelationship(org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermTYPEDBYRelationship.TermTYPEDBYRelationship omrsRelationshipBean) {
        // copy over the Line attributes
        TermTYPEDBYRelationship termTYPEDBYRelationship = new TermTYPEDBYRelationship(omrsRelationshipBean);
        termTYPEDBYRelationship.setDescription(omrsRelationshipBean.getDescription());
        termTYPEDBYRelationship.setSource(omrsRelationshipBean.getSource());
        termTYPEDBYRelationship.setSteward(omrsRelationshipBean.getSteward());
        termTYPEDBYRelationship.setStatus(omrsRelationshipBean.getStatus());
        termTYPEDBYRelationship.setGuid(omrsRelationshipBean.getGuid());
        termTYPEDBYRelationship.setAttributeGuid(omrsRelationshipBean.getEntity1Guid());
        termTYPEDBYRelationship.setTypeGuid(omrsRelationshipBean.getEntity2Guid());
        String[] properties=org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermTYPEDBYRelationship.TermTYPEDBYRelationship.PROPERTY_NAMES_SET_VALUES;
        Map<String, Object> extraAttributes =termTYPEDBYRelationship.getExtraAttributes();
        if (properties!=null && properties.length >0) {
            if (extraAttributes ==null) {
                extraAttributes =new HashMap<>();
            }
            for (String property : properties) {
                if (extraAttributes.containsKey(property)) {
                    extraAttributes.remove(property);
                }
            }
            termTYPEDBYRelationship.setExtraAttributes(extraAttributes);
        }
        return termTYPEDBYRelationship;
    }
}
