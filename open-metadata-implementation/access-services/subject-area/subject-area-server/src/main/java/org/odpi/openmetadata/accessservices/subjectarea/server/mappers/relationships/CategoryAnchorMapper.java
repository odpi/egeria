/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.relationships;

import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.properties.relationships.CategoryAnchorRelationship;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * Static mapping methods to map between the categoryAnchor and the equivalent generated OMRSRelationshipBean
 */
public class CategoryAnchorMapper
{
    private static final Logger log = LoggerFactory.getLogger( CategoryAnchorMapper.class);
    private static final String className = CategoryAnchorMapper.class.getName();

    /**
     * map CategoryAnchorRelationship to the omrs relationship bean equivalent
     * @param categoryAnchorRelationship supplied CategoryAnchorRelationship
     * @return omrs relationship bean equivalent
     * @throws InvalidParameterException  one of the parameters is null or invalid.
     */
    static public org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.CategoryAnchor.CategoryAnchor mapCategoryAnchorToOMRSRelationshipBean(CategoryAnchorRelationship categoryAnchorRelationship) throws InvalidParameterException {
        // copy over the Line attributes
        org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.CategoryAnchor.CategoryAnchor omrsRelationshipBean = new  org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.CategoryAnchor.CategoryAnchor(categoryAnchorRelationship);
        //Set properties
        omrsRelationshipBean.setGuid(categoryAnchorRelationship.getGuid());
        omrsRelationshipBean.setEntity1Guid(categoryAnchorRelationship.getGlossaryGuid());
        omrsRelationshipBean.setEntity2Guid(categoryAnchorRelationship.getCategoryGuid());

        Map<String, Object> extraAttributes = omrsRelationshipBean.getExtraAttributes();
        if (extraAttributes !=null)
        {
            String[] properties = org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.CategoryAnchor.CategoryAnchor.PROPERTY_NAMES_SET_VALUES;
            if (properties!=null && properties.length >0) {
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
     * Map omrs relationship bean equivalent to CategoryAnchorRelationship
     * @param omrsRelationshipBean omrs relationship bean equivalent
     * @return CategoryAnchorRelationship categoryAnchor
     */
    public static CategoryAnchorRelationship mapOMRSRelationshipBeanToCategoryAnchor(org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.CategoryAnchor.CategoryAnchor omrsRelationshipBean) {
        // copy over the Line attributes
        CategoryAnchorRelationship categoryAnchorRelationship = new CategoryAnchorRelationship(omrsRelationshipBean);
        categoryAnchorRelationship.setGuid(omrsRelationshipBean.getGuid());
        categoryAnchorRelationship.setGlossaryGuid((omrsRelationshipBean.getEntity1Guid()));
        categoryAnchorRelationship.setCategoryGuid((omrsRelationshipBean.getEntity2Guid()));
        String[] properties=org.odpi.openmetadata.accessservices.subjectarea.generated.relationships.CategoryAnchor.CategoryAnchor.PROPERTY_NAMES_SET_VALUES;
        Map<String, Object> extraAttributes = categoryAnchorRelationship.getExtraAttributes();
        if (properties!=null && properties.length >0) {
            if (extraAttributes ==null) {
                extraAttributes =new HashMap<>();
            }
            for (String property : properties) {
                if (extraAttributes.containsKey(property)) {
                    extraAttributes.remove(property);
                }
            }
            categoryAnchorRelationship.setExtraAttributes(extraAttributes);
        }
        return categoryAnchorRelationship;
    }
}
