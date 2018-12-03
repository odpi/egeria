/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.TermHASARelationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * Static mapping methods to map between the hasARelationship and the equivalent generated OMRSRelationshipBean
 */
public class HasAMapper
{
    private static final Logger log = LoggerFactory.getLogger( HasAMapper.class);
    private static final String className = HasAMapper.class.getName();

    /**
     * map TermHASARelationship to the omrs relationship bean equivalent
     * @param hasARelationship has a relationship
     * @return omrs relationship bean equivalent
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    static public org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermHASARelationship.TermHASARelationship mapTermHASARelationshipToOMRSRelationshipBean(TermHASARelationship hasARelationship) throws InvalidParameterException {
        // copy over the Line attributes
        org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermHASARelationship.TermHASARelationship omrsRelationshipBean = new  org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermHASARelationship.TermHASARelationship(hasARelationship);
        //Set properties
        omrsRelationshipBean.setDescription(hasARelationship.getDescription());
        omrsRelationshipBean.setSource(hasARelationship.getSource());
        omrsRelationshipBean.setSteward(hasARelationship.getSteward());
        omrsRelationshipBean.setStatus(hasARelationship.getStatus());
        omrsRelationshipBean.setGuid(hasARelationship.getGuid());
        omrsRelationshipBean.setEntity1Guid(hasARelationship.getOwningTermGuid());
        omrsRelationshipBean.setEntity2Guid(hasARelationship.getOwnedTermGuid());

        Map<String, Object> extraAttributes = omrsRelationshipBean.getExtraAttributes();
        if (extraAttributes !=null)
        {
            String[] properties = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermHASARelationship.TermHASARelationship.PROPERTY_NAMES_SET_VALUES;
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
     * Map omrs relationship bean equivalent to TermHASARelationship
     * @param omrsRelationshipBean omrs relationship bean equivalent
     * @return TermHASARelationship has a relationship
     */
    public static TermHASARelationship mapOMRSRelationshipBeanToTermHASARelationship(org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermHASARelationship.TermHASARelationship omrsRelationshipBean) {
        // copy over the Line attributes
        TermHASARelationship hasARelationship = new TermHASARelationship(omrsRelationshipBean);
        hasARelationship.setDescription(omrsRelationshipBean.getDescription());
        hasARelationship.setSource(omrsRelationshipBean.getSource());
        hasARelationship.setSteward(omrsRelationshipBean.getSteward());
        hasARelationship.setStatus(omrsRelationshipBean.getStatus());
        hasARelationship.setGuid(omrsRelationshipBean.getGuid());
        hasARelationship.setOwningTermGuid(omrsRelationshipBean.getEntity1Guid());
        hasARelationship.setOwnedTermGuid(omrsRelationshipBean.getEntity2Guid());

        String[] properties=org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermHASARelationship.TermHASARelationship.PROPERTY_NAMES_SET_VALUES;
        Map<String, Object> extraAttributes =hasARelationship.getExtraAttributes();
        if (properties!=null && properties.length >0) {
            if (extraAttributes ==null) {
                extraAttributes =new HashMap<>();
            }
            for (String property : properties) {
                if (extraAttributes.containsKey(property)) {
                    extraAttributes.remove(property);
                }
            }
            hasARelationship.setExtraAttributes(extraAttributes);
        }
        return hasARelationship;
    }
}
