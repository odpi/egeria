/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.ISARelationship;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.ISARelationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * Static mapping methods to map between the isaRelationship and the equivalent generated OMRSRelationshipBean
 */
public class IsaMapper
{
    private static final Logger log = LoggerFactory.getLogger( IsaMapper.class);
    private static final String className = IsaMapper.class.getName();

    /**
     * map ISARelationship to the omrs relationship bean equivalent
     * @param isaRelationship supplied ISARelationship
     * @return omrs relationship bean equivalent
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    static public org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ISARelationship.ISARelationship mapISARelationshipToOMRSRelationshipBean(ISARelationship isaRelationship) throws InvalidParameterException {
        // copy over the Line attributes
        org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ISARelationship.ISARelationship omrsRelationshipBean = new  org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ISARelationship.ISARelationship(isaRelationship);
        //Set properties
        omrsRelationshipBean.setDescription(isaRelationship.getDescription());
        omrsRelationshipBean.setExpression(isaRelationship.getExpression());
        omrsRelationshipBean.setSource(isaRelationship.getSource());
        omrsRelationshipBean.setSteward(isaRelationship.getSteward());
        omrsRelationshipBean.setStatus(isaRelationship.getStatus());
        omrsRelationshipBean.setGuid(isaRelationship.getGuid());
        omrsRelationshipBean.setEntity1Guid(isaRelationship.getSpecialisedTermGuid());
        omrsRelationshipBean.setEntity2Guid(isaRelationship.getTermGuid());

        Map<String, Object> extraAttributes = omrsRelationshipBean.getExtraAttributes();
        if (extraAttributes !=null)
        {
            String[] properties = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ISARelationship.ISARelationship.PROPERTY_NAMES_SET_VALUES;
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
     * Map omrs relationship bean equivalent to ISARelationship
     * @param omrsRelationshipBean omrs relationship bean equivalent
     * @return ISARelationship isaRelationship
     */
    public static ISARelationship mapOMRSRelationshipBeanToISARelationship(org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ISARelationship.ISARelationship omrsRelationshipBean) {
        // copy over the Line attributes
        ISARelationship isaRelationship = new ISARelationship(omrsRelationshipBean);
        isaRelationship.setDescription(omrsRelationshipBean.getDescription());
        isaRelationship.setExpression(omrsRelationshipBean.getExpression());
        isaRelationship.setSource(omrsRelationshipBean.getSource());
        isaRelationship.setSteward(omrsRelationshipBean.getSteward());
        isaRelationship.setStatus(omrsRelationshipBean.getStatus());
        isaRelationship.setGuid(omrsRelationshipBean.getGuid());
        isaRelationship.setTermGuid(omrsRelationshipBean.getEntity1Guid());
        isaRelationship.setSpecialisedTermGuid(omrsRelationshipBean.getEntity2Guid());
        String[] properties=org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ISARelationship.ISARelationship.PROPERTY_NAMES_SET_VALUES;
        Map<String, Object> extraAttributes =isaRelationship.getExtraAttributes();
        if (properties!=null && properties.length >0) {
            if (extraAttributes ==null) {
                extraAttributes =new HashMap<>();
            }
            for (String property : properties) {
                if (extraAttributes.containsKey(property)) {
                    extraAttributes.remove(property);
                }
            }
            isaRelationship.setExtraAttributes(extraAttributes);
        }
        return isaRelationship;
    }
}
