/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.SemanticAssignment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * Static mapping methods to map between the semanticAssignment and the equivalent generated OMRSRelationshipBean
 */
public class SemanticAssignmentMapper
{
    private static final Logger log = LoggerFactory.getLogger( SemanticAssignmentMapper.class);
    private static final String className = SemanticAssignmentMapper.class.getName();

    /**
     * map SemanticAssignment to the omrs relationship bean equivalent
     * @param semanticAssignment supplied SemanticAssignment
     * @return omrs relationship bean equivalent
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    static public org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.SemanticAssignment.SemanticAssignment mapSemanticAssignmentToOMRSRelationshipBean(SemanticAssignment semanticAssignment) throws InvalidParameterException {
        // copy over the Line attributes
        org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.SemanticAssignment.SemanticAssignment omrsRelationshipBean = new  org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.SemanticAssignment.SemanticAssignment(semanticAssignment);
        //Set properties
        omrsRelationshipBean.setDescription(semanticAssignment.getDescription());
        omrsRelationshipBean.setExpression(semanticAssignment.getExpression());
        omrsRelationshipBean.setSource(semanticAssignment.getSource());
        omrsRelationshipBean.setSteward(semanticAssignment.getSteward());
        omrsRelationshipBean.setConfidence(semanticAssignment.getConfidence());
        omrsRelationshipBean.setGuid(semanticAssignment.getGuid());
        omrsRelationshipBean.setEntity1Guid(semanticAssignment.getAssignedElementGuid());
        omrsRelationshipBean.setEntity2Guid(semanticAssignment.getTermGuid());

        Map<String, Object> extraAttributes = omrsRelationshipBean.getExtraAttributes();
        if (extraAttributes !=null)
        {
            String[] properties = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.SemanticAssignment.SemanticAssignment.PROPERTY_NAMES_SET_VALUES;
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
     * Map omrs relationship bean equivalent to SemanticAssignment
     * @param omrsRelationshipBean omrs relationship bean equivalent
     * @return SemanticAssignment semanticAssignment
     */
    public static SemanticAssignment mapOMRSRelationshipBeanToSemanticAssignment(org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.SemanticAssignment.SemanticAssignment omrsRelationshipBean) {
        // copy over the Line attributes
        SemanticAssignment semanticAssignment = new SemanticAssignment(omrsRelationshipBean);
        semanticAssignment.setDescription(omrsRelationshipBean.getDescription());
        semanticAssignment.setExpression(omrsRelationshipBean.getExpression());
        semanticAssignment.setSource(omrsRelationshipBean.getSource());
        semanticAssignment.setSteward(omrsRelationshipBean.getSteward());
        semanticAssignment.setConfidence(omrsRelationshipBean.getConfidence());
        semanticAssignment.setGuid(omrsRelationshipBean.getGuid());
        semanticAssignment.setAssignedElementGuid(omrsRelationshipBean.getEntity1Guid());
        semanticAssignment.setTermGuid(omrsRelationshipBean.getEntity2Guid());
        String[] properties=org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.SemanticAssignment.SemanticAssignment.PROPERTY_NAMES_SET_VALUES;
        Map<String, Object> extraAttributes =semanticAssignment.getExtraAttributes();
        if (properties!=null && properties.length >0) {
            if (extraAttributes ==null) {
                extraAttributes =new HashMap<>();
            }
            for (String property : properties) {
                if (extraAttributes.containsKey(property)) {
                    extraAttributes.remove(property);
                }
            }
            semanticAssignment.setExtraAttributes(extraAttributes);
        }
        return semanticAssignment;
    }
}
