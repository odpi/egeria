/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.ExternallySourcedGlossary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * Static mapping methods to map between the externallySourcedGlossary and the equivalent generated OMRSRelationshipBean
 */
public class ExternallySourceGlossaryMapper
{
    private static final Logger log = LoggerFactory.getLogger( ExternallySourceGlossaryMapper.class);
    private static final String className = ExternallySourceGlossaryMapper.class.getName();

    /**
     * map ExternallySourcedGlossary to the omrs relationship bean equivalent
     * @param externallySourcedGlossary supplied ExternallySourcedGlossary
     * @return omrs relationship bean equivalent
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    static public org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ExternallySourcedGlossary.ExternallySourcedGlossary mapExternallySourcedGlossaryToOMRSRelationshipBean(ExternallySourcedGlossary externallySourcedGlossary) throws InvalidParameterException {
        // copy over the Line attributes
        org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ExternallySourcedGlossary.ExternallySourcedGlossary omrsRelationshipBean = new  org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ExternallySourcedGlossary.ExternallySourcedGlossary(externallySourcedGlossary);
        //Set properties
        omrsRelationshipBean.setGuid(externallySourcedGlossary.getGuid());

        Map<String, Object> extraAttributes = omrsRelationshipBean.getExtraAttributes();
        if (extraAttributes !=null)
        {
            String[] properties = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ExternallySourcedGlossary.ExternallySourcedGlossary.PROPERTY_NAMES_SET_VALUES;
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
     * Map omrs relationship bean equivalent to ExternallySourcedGlossary
     * @param omrsRelationshipBean omrs relationship bean equivalent
     * @return ExternallySourcedGlossary externallySourcedGlossary
     */
    public static ExternallySourcedGlossary mapOMRSRelationshipBeanToExternallySourcedGlossary(org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ExternallySourcedGlossary.ExternallySourcedGlossary omrsRelationshipBean) {
        // copy over the Line attributes
        ExternallySourcedGlossary externallySourcedGlossary = new ExternallySourcedGlossary(omrsRelationshipBean);
        externallySourcedGlossary.setGuid(omrsRelationshipBean.getGuid());
        String[] properties=org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.ExternallySourcedGlossary.ExternallySourcedGlossary.PROPERTY_NAMES_SET_VALUES;
        Map<String, Object> extraAttributes =externallySourcedGlossary.getExtraAttributes();
        if (properties!=null && properties.length >0) {
            if (extraAttributes ==null) {
                extraAttributes =new HashMap<>();
            }
            for (String property : properties) {
                if (extraAttributes.containsKey(property)) {
                    extraAttributes.remove(property);
                }
            }
            externallySourcedGlossary.setExtraAttributes(extraAttributes);
        }
        return externallySourcedGlossary;
    }
}
