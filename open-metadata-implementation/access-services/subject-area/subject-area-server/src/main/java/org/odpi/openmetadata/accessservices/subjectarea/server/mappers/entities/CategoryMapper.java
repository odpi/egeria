/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.subjectarea.server.mappers.entities;


import org.odpi.openmetadata.accessservices.subjectarea.ffdc.exceptions.InvalidParameterException;
import org.odpi.openmetadata.accessservices.subjectarea.generated.entities.GlossaryCategory.GlossaryCategory;
import org.odpi.openmetadata.accessservices.subjectarea.properties.classifications.Classification;
import org.odpi.openmetadata.accessservices.subjectarea.properties.objects.category.Category;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;


/**
 * Static mapping methods to map between the Category and the generated GlossaryCategory (the omrsBean).
 * These mapping methods map classifications and attributes that directly map to OMRS.
 *
 */
public class CategoryMapper {
    private static final Logger log = LoggerFactory.getLogger( CategoryMapper.class);
    private static final String className = CategoryMapper.class.getName();

    /**
     * Map the Category to the omrsBean
     * @param category category exposed in the API
     * @return omrs bean that has been mapped to
     * @throws InvalidParameterException one of the parameters is null or invalid.
     */
    static public GlossaryCategory mapCategoryToOMRSBean(Category category) throws InvalidParameterException {

        GlossaryCategory omrsBean = new GlossaryCategory();
        //Set properties
        if (category.getSystemAttributes() !=null) {
            omrsBean.setSystemAttributes(category.getSystemAttributes());
        }
        omrsBean.setEffectiveFromTime(category.getEffectiveFromTime());
        omrsBean.setEffectiveToTime(category.getEffectiveToTime());
        omrsBean.setQualifiedName(category.getQualifiedName());
        omrsBean.setDescription(category.getDescription());
        omrsBean.setDisplayName(category.getName());
        List<Classification> classifications = category.getClassifications();
        // category Classifications should not contain any governance classifications, but it is possible that the requester added them there by mistake.
        if (classifications==null) {
            classifications = new ArrayList<>();
        }

        omrsBean.setClassifications(classifications);
        return omrsBean;
    }

    /**
     * Map the GlossaryCategory OMRS Bean to the Catoegory exposed in the API
     * @param omrsBean generated OMRS Bean for Glossary Category
     * @return Category exposed in the API
     */
    public static Category mapOMRSBeantoCategory(GlossaryCategory omrsBean) {
        Category category = new Category();
        category.setClassifications(omrsBean.getClassifications());
        category.setDescription(omrsBean.getDescription());

        if (omrsBean.getSystemAttributes() !=null) {
            category.setSystemAttributes(omrsBean.getSystemAttributes());
        }
        category.setEffectiveFromTime(omrsBean.getEffectiveFromTime());
        category.setEffectiveToTime((omrsBean.getEffectiveToTime()));
        category.setName(omrsBean.getDisplayName());
        category.setQualifiedName(omrsBean.getQualifiedName());

        // Do not set other parts of Category here - as there require other rest calls to get the content.

        return category;
    }
}
