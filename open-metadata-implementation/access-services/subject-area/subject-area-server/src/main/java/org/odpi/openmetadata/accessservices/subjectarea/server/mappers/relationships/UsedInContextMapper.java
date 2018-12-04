/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.UsedInContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * Static mapping methods to map between the usedInContext and the equivalent generated OMRSRelationshipBean
 */
public class UsedInContextMapper
{
    private static final Logger log = LoggerFactory.getLogger( UsedInContextMapper.class);
    private static final String className = UsedInContextMapper.class.getName();

    /**
     * map UsedInContext to the omrs relationship bean equivalent
     * @param usedInContext supplied UsedInContext
     * @return omrs relationship bean equivalent
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    static public org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.UsedInContext.UsedInContext mapUsedInContextToOMRSRelationshipBean(UsedInContext usedInContext) throws InvalidParameterException {
        // copy over the Line attributes
        org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.UsedInContext.UsedInContext omrsRelationshipBean = new  org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.UsedInContext.UsedInContext(usedInContext);
        //Set properties
        omrsRelationshipBean.setDescription(usedInContext.getDescription());
        omrsRelationshipBean.setExpression(usedInContext.getExpression());
        omrsRelationshipBean.setSource(usedInContext.getSource());
        omrsRelationshipBean.setSteward(usedInContext.getSteward());
        omrsRelationshipBean.setStatus(usedInContext.getStatus());
        omrsRelationshipBean.setGuid(usedInContext.getGuid());
        omrsRelationshipBean.setEntity1Guid(usedInContext.getTermInContextGuid());
        omrsRelationshipBean.setEntity2Guid(usedInContext.getContextGuid());

        Map<String, Object> extraAttributes = omrsRelationshipBean.getExtraAttributes();
        if (extraAttributes !=null)
        {
            String[] properties = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.UsedInContext.UsedInContext.PROPERTY_NAMES_SET_VALUES;
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
     * Map omrs relationship bean equivalent to UsedInContext
     * @param omrsRelationshipBean omrs relationship bean equivalent
     * @return UsedInContext usedInContext
     */
    public static UsedInContext mapOMRSRelationshipBeanToUsedInContext(org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.UsedInContext.UsedInContext omrsRelationshipBean) {
        // copy over the Line attributes
        UsedInContext usedInContext = new UsedInContext(omrsRelationshipBean);
        usedInContext.setDescription(omrsRelationshipBean.getDescription());
        usedInContext.setExpression(omrsRelationshipBean.getExpression());
        usedInContext.setSource(omrsRelationshipBean.getSource());
        usedInContext.setSteward(omrsRelationshipBean.getSteward());
        usedInContext.setStatus(omrsRelationshipBean.getStatus());
        usedInContext.setGuid(omrsRelationshipBean.getGuid());
        usedInContext.setTermInContextGuid(omrsRelationshipBean.getEntity1Guid());
        usedInContext.setContextGuid(omrsRelationshipBean.getEntity2Guid());
        String[] properties=org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.UsedInContext.UsedInContext.PROPERTY_NAMES_SET_VALUES;
        Map<String, Object> extraAttributes =usedInContext.getExtraAttributes();
        if (properties!=null && properties.length >0) {
            if (extraAttributes ==null) {
                extraAttributes =new HashMap<>();
            }
            for (String property : properties) {
                if (extraAttributes.containsKey(property)) {
                    extraAttributes.remove(property);
                }
            }
            usedInContext.setExtraAttributes(extraAttributes);
        }
        return usedInContext;
    }
}
