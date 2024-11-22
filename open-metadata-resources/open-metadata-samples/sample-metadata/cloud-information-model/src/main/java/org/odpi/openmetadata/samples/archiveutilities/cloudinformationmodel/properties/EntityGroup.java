/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.cloudinformationmodel.properties;

import java.util.List;
import java.util.Map;

/**
 * EntityGroup is used for assembling the concept group, class concepts, property concepts and
 * schemas together.  They are dispersed in the model file and need to be grouped together
 * to ensure the parsed model is properly linked
 */
public class EntityGroup
{
    String                    subjectAreaId;
    ConceptGroup              conceptGroup;
    List<Map<String, Object>> classConceptsJsonLD    = null;
    List<Map<String, Object>> propertyConceptsJsonLD = null;
    List<Map<String, Object>> schemasJsonLD          = null;


    public EntityGroup(ConceptGroup conceptGroup,
                       String       subjectAreaId)
    {
        this.conceptGroup = conceptGroup;
        this.subjectAreaId = subjectAreaId;
    }


    public ConceptGroup getConceptGroup()
    {
        return conceptGroup;
    }


    public String getSubjectAreaId()
    {
        return subjectAreaId;
    }


    public List<Map<String, Object>> getClassConceptsJsonLD()
    {
        return classConceptsJsonLD;
    }


    public void setClassConceptsJsonLD(List<Map<String, Object>> classConceptsJsonLD)
    {
        this.classConceptsJsonLD = classConceptsJsonLD;
    }


    public List<Map<String, Object>> getPropertyConceptsJsonLD()
    {
        return propertyConceptsJsonLD;
    }


    public void setPropertyConceptsJsonLD(List<Map<String, Object>> propertyConceptsJsonLD)
    {
        this.propertyConceptsJsonLD = propertyConceptsJsonLD;
    }


    public List<Map<String, Object>> getSchemasJsonLD()
    {
        return schemasJsonLD;
    }


    public void setSchemasJsonLD(List<Map<String, Object>> schemasJsonLD)
    {
        this.schemasJsonLD = schemasJsonLD;
    }
}
