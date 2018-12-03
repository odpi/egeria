/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.ValidValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * Static mapping methods to map between the validValue and the equivalent generated OMRSRelationshipBean
 */
public class ValidValueMapper
{
    private static final Logger log = LoggerFactory.getLogger( ValidValueMapper.class);
    private static final String className = ValidValueMapper.class.getName();

    /**
     * map ValidValue to the omrs relationship bean equivalent
     * @param validValue supplied ValidValue
     * @return omrs relationship bean equivalent
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    static public org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ValidValue.ValidValue mapValidValueToOMRSRelationshipBean(ValidValue validValue) throws InvalidParameterException {
        // copy over the Line attributes
        org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ValidValue.ValidValue omrsRelationshipBean = new  org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ValidValue.ValidValue(validValue);
        //Set properties
        omrsRelationshipBean.setDescription(validValue.getDescription());
        omrsRelationshipBean.setExpression(validValue.getExpression());
        omrsRelationshipBean.setSource(validValue.getSource());
        omrsRelationshipBean.setSteward(validValue.getSteward());
        omrsRelationshipBean.setStatus(validValue.getStatus());
        omrsRelationshipBean.setGuid(validValue.getGuid());
        omrsRelationshipBean.setEntity1Guid(validValue.getTermGuid());
        omrsRelationshipBean.setEntity2Guid(validValue.getValidValueGuid());
        Map<String, Object> extraAttributes = omrsRelationshipBean.getExtraAttributes();
        if (extraAttributes !=null)
        {
            String[] properties = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ValidValue.ValidValue.PROPERTY_NAMES_SET_VALUES;
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
     * Map omrs relationship bean equivalent to ValidValue
     * @param omrsRelationshipBean omrs relationship bean equivalent
     * @return ValidValue validValue
     */
    public static ValidValue mapOMRSRelationshipBeanToValidValue(org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ValidValue.ValidValue omrsRelationshipBean) {
        // copy over the Line attributes
        ValidValue validValue = new ValidValue(omrsRelationshipBean);
        validValue.setDescription(omrsRelationshipBean.getDescription());
        validValue.setExpression(omrsRelationshipBean.getExpression());
        validValue.setSource(omrsRelationshipBean.getSource());
        validValue.setSteward(omrsRelationshipBean.getSteward());
        validValue.setStatus(omrsRelationshipBean.getStatus());
        validValue.setGuid(omrsRelationshipBean.getGuid());
        validValue.setTermGuid(omrsRelationshipBean.getEntity1Guid());
        validValue.setValidValueGuid(omrsRelationshipBean.getEntity2Guid());
        String[] properties=org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ValidValue.ValidValue.PROPERTY_NAMES_SET_VALUES;
        Map<String, Object> extraAttributes =validValue.getExtraAttributes();
        if (properties!=null && properties.length >0) {
            if (extraAttributes ==null) {
                extraAttributes =new HashMap<>();
            }
            for (String property : properties) {
                if (extraAttributes.containsKey(property)) {
                    extraAttributes.remove(property);
                }
            }
            validValue.setExtraAttributes(extraAttributes);
        }
        return validValue;
    }
}
