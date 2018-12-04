/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.TermISATypeOFRelationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * Static mapping methods to map between the isaTypeOf and the equivalent generated OMRSRelationshipBean
 */
public class IsaTypeOfMapper
{
    private static final Logger log = LoggerFactory.getLogger( IsaTypeOfMapper.class);
    private static final String className = IsaTypeOfMapper.class.getName();

    /**
     * map TermISATypeOFRelationship to the omrs relationship bean equivalent
     * @param isaTypeOf supplied TermISATypeOFRelationship
     * @return omrs relationship bean equivalent
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    static public org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermISATypeOFRelationship.TermISATypeOFRelationship mapTermISATypeOFRelationshipToOMRSRelationshipBean(TermISATypeOFRelationship isaTypeOf) throws InvalidParameterException {
        // copy over the Line attributes
        org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermISATypeOFRelationship.TermISATypeOFRelationship omrsRelationshipBean = new  org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermISATypeOFRelationship.TermISATypeOFRelationship(isaTypeOf);
        //Set properties
        omrsRelationshipBean.setDescription(isaTypeOf.getDescription());
        omrsRelationshipBean.setSource(isaTypeOf.getSource());
        omrsRelationshipBean.setSteward(isaTypeOf.getSteward());
        omrsRelationshipBean.setStatus(isaTypeOf.getStatus());
        omrsRelationshipBean.setGuid(isaTypeOf.getGuid());
        omrsRelationshipBean.setEntity1Guid(isaTypeOf.getSuperTypeGuid());
        omrsRelationshipBean.setEntity2Guid(isaTypeOf.getSubTypeGuid());
        Map<String, Object> extraAttributes = omrsRelationshipBean.getExtraAttributes();
        if (extraAttributes !=null)
        {
            String[] properties = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermISATypeOFRelationship.TermISATypeOFRelationship.PROPERTY_NAMES_SET_VALUES;
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
     * Map omrs relationship bean equivalent to TermISATypeOFRelationship
     * @param omrsRelationshipBean omrs relationship bean equivalent
     * @return TermISATypeOFRelationship isaTypeOf
     */
    public static TermISATypeOFRelationship mapOMRSRelationshipBeanToTermISATypeOFRelationship(org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermISATypeOFRelationship.TermISATypeOFRelationship omrsRelationshipBean) {
        // copy over the Line attributes
        TermISATypeOFRelationship isaTypeOf = new TermISATypeOFRelationship(omrsRelationshipBean);
        isaTypeOf.setDescription(omrsRelationshipBean.getDescription());
        isaTypeOf.setSource(omrsRelationshipBean.getSource());
        isaTypeOf.setSteward(omrsRelationshipBean.getSteward());
        isaTypeOf.setStatus(omrsRelationshipBean.getStatus());
        isaTypeOf.setGuid(omrsRelationshipBean.getGuid());
        isaTypeOf.setSuperTypeGuid(omrsRelationshipBean.getEntity1Guid());
        isaTypeOf.setSubTypeGuid(omrsRelationshipBean.getEntity2Guid());
        String[] properties=org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.TermISATypeOFRelationship.TermISATypeOFRelationship.PROPERTY_NAMES_SET_VALUES;
        Map<String, Object> extraAttributes =isaTypeOf.getExtraAttributes();
        if (properties!=null && properties.length >0) {
            if (extraAttributes ==null) {
                extraAttributes =new HashMap<>();
            }
            for (String property : properties) {
                if (extraAttributes.containsKey(property)) {
                    extraAttributes.remove(property);
                }
            }
            isaTypeOf.setExtraAttributes(extraAttributes);
        }
        return isaTypeOf;
    }
}
