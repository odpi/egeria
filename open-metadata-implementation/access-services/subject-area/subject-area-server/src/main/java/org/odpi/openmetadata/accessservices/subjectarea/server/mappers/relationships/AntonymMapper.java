/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Antonym;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * Static mapping methods to map between the antonym and the equivalent generated OMRSRelationshipBean
 */
public class AntonymMapper
{
    private static final Logger log = LoggerFactory.getLogger( AntonymMapper.class);
    private static final String className = AntonymMapper.class.getName();

    /**
     * map Antonym to the omrs relationship bean equivalent
     * @param antonym supplied Antonym
     * @return omrs relationship bean equivalent
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    static public org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Antonym.Antonym mapAntonymToOMRSRelationshipBean(Antonym antonym) throws InvalidParameterException {
        // copy over the Line attributes
        org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Antonym.Antonym omrsRelationshipBean = new  org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Antonym.Antonym(antonym);
        //Set properties
        omrsRelationshipBean.setDescription(antonym.getDescription());
        omrsRelationshipBean.setExpression(antonym.getExpression());
        omrsRelationshipBean.setSource(antonym.getSource());
        omrsRelationshipBean.setSteward(antonym.getSteward());
        omrsRelationshipBean.setStatus(antonym.getStatus());
        omrsRelationshipBean.setGuid(antonym.getGuid());
        omrsRelationshipBean.setEntity1Guid(antonym.getAntonym1Guid());
        omrsRelationshipBean.setEntity2Guid(antonym.getAntonym2Guid());

        Map<String, Object> extraAttributes = omrsRelationshipBean.getExtraAttributes();
        if (extraAttributes !=null)
        {
            String[] properties = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Antonym.Antonym.PROPERTY_NAMES_SET_VALUES;
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
     * Map omrs relationship bean equivalent to Antonym
     * @param omrsRelationshipBean omrs relationship bean equivalent
     * @return Antonym antonym
     */
    public static Antonym mapOMRSRelationshipBeanToAntonym(org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Antonym.Antonym omrsRelationshipBean) {
        // copy over the Line attributes
        Antonym antonym = new Antonym(omrsRelationshipBean);
        antonym.setDescription(omrsRelationshipBean.getDescription());
        antonym.setExpression(omrsRelationshipBean.getExpression());
        antonym.setSource(omrsRelationshipBean.getSource());
        antonym.setSteward(omrsRelationshipBean.getSteward());
        antonym.setStatus(omrsRelationshipBean.getStatus());
        antonym.setGuid(omrsRelationshipBean.getGuid());
        antonym.setAntonym1Guid((omrsRelationshipBean.getEntity1Guid()));
        antonym.setAntonym2Guid((omrsRelationshipBean.getEntity2Guid()));
        String[] properties=org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Antonym.Antonym.PROPERTY_NAMES_SET_VALUES;
        Map<String, Object> extraAttributes =antonym.getExtraAttributes();
        if (properties!=null && properties.length >0) {
            if (extraAttributes ==null) {
                extraAttributes =new HashMap<>();
            }
            for (String property : properties) {
                if (extraAttributes.containsKey(property)) {
                    extraAttributes.remove(property);
                }
            }
            antonym.setExtraAttributes(extraAttributes);
        }
        return antonym;
    }
}
