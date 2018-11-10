/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.Synonym;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * Static mapping methods to map between the synonym and the equivalent generated OMRSRelationshipBean
 */
public class SynonymMapper
{
    private static final Logger log = LoggerFactory.getLogger( SynonymMapper.class);
    private static final String className = SynonymMapper.class.getName();

    /**
     * map Synonym to the omrs relationship bean equivalent
     * @param synonym supplied Synonym
     * @return omrs relationship bean equivalent
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    static public org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Synonym.Synonym mapSynonymToOMRSRelationshipBean(Synonym synonym) throws InvalidParameterException {
        // copy over the Line attributes
        org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Synonym.Synonym omrsRelationshipBean = new  org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Synonym.Synonym(synonym);
        //Set properties
        omrsRelationshipBean.setDescription(synonym.getDescription());
        omrsRelationshipBean.setExpression(synonym.getExpression());
        omrsRelationshipBean.setSource(synonym.getSource());
        omrsRelationshipBean.setSteward(synonym.getSteward());
        omrsRelationshipBean.setStatus(synonym.getStatus());

        Map<String, Object> extraAttributes = omrsRelationshipBean.getExtraAttributes();
        if (extraAttributes !=null)
        {
            String[] properties = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Synonym.Synonym.PROPERTY_NAMES_SET_VALUES;
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
     * Map omrs relationship bean equivalent to Synonym
     * @param omrsRelationshipBean omrs relationship bean equivalent
     * @return Synonym synonym
     */
    public static Synonym mapOMRSRelationshipBeanToSynonym(org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Synonym.Synonym omrsRelationshipBean) {
        // copy over the Line attributes
        Synonym synonym = new Synonym(omrsRelationshipBean);
        synonym.setDescription(omrsRelationshipBean.getDescription());
        synonym.setExpression(omrsRelationshipBean.getExpression());
        synonym.setSource(omrsRelationshipBean.getSource());
        synonym.setSteward(omrsRelationshipBean.getSteward());
        synonym.setStatus(omrsRelationshipBean.getStatus());
        String[] properties=org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.Synonym.Synonym.PROPERTY_NAMES_SET_VALUES;
        Map<String, Object> extraAttributes =synonym.getExtraAttributes();
        if (properties!=null && properties.length >0) {
            if (extraAttributes ==null) {
                extraAttributes =new HashMap<>();
            }
            for (String property : properties) {
                if (extraAttributes.containsKey(property)) {
                    extraAttributes.remove(property);
                }
            }
            synonym.setExtraAttributes(extraAttributes);
        }
        return synonym;
    }
}
