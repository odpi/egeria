/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.samples.archiveutilities.cloudinformationmodel.properties;

import java.util.List;
import java.util.Map;

/**
 * Model provides the top level anchor for the model content
 */
public class Model
{
    private String                           modelName              = null;
    private String                           modelTechnicalName     = null;
    private String                           modelSummary           = null;
    private String                           modelDescription       = null;
    private String                           modelLocation          = null;
    private String                           modelScope             = null;
    private String                           modelLanguage          = null;
    private String                           modelUsage             = null;
    private Map<String, SubjectArea>         subjectAreaMap         = null;
    private Map<String, Concept>             conceptBeadMap         = null;
    private Map<String, PropertyDescription> propertyDescriptionMap = null;
    private Map<String, PropertyGroup>       propertyGroupMap       = null;
    private List<ConceptGroup>               orphanedConceptGroups  = null;
    private List<String>                     errorReport            = null;


    public String getModelName()
    {
        return modelName;
    }


    public void setModelName(String modelName)
    {
        this.modelName = modelName;
    }


    public String getModelTechnicalName()
    {
        return modelTechnicalName;
    }


    public void setModelTechnicalName(String modelTechnicalName)
    {
        this.modelTechnicalName = modelTechnicalName;
    }


    public String getModelSummary()
    {
        return modelSummary;
    }

    public void setModelSummary(String modelSummary)
    {
        this.modelSummary = modelSummary;
    }


    public String getModelDescription()
    {
        return modelDescription;
    }


    public void setModelDescription(String modelDescription)
    {
        this.modelDescription = modelDescription;
    }

    public String getModelUsage()
    {
        return modelUsage;
    }

    public void setModelUsage(String modelUsage)
    {
        this.modelUsage = modelUsage;
    }

    public String getModelLocation()
    {
        return modelLocation;
    }


    public void setModelLocation(String modelLocation)
    {
        this.modelLocation = modelLocation;
    }


    public String getModelScope()
    {
        return modelScope;
    }


    public void setModelScope(String modelScope)
    {
        this.modelScope = modelScope;
    }


    public String getModelLanguage()
    {
        return modelLanguage;
    }


    public void setModelLanguage(String modelLanguage)
    {
        this.modelLanguage = modelLanguage;
    }


    public Map<String, SubjectArea> getSubjectAreaMap()
    {
        return subjectAreaMap;
    }


    public void setSubjectAreaMap(Map<String, SubjectArea> subjectAreaMap)
    {
        this.subjectAreaMap = subjectAreaMap;
    }


    public Map<String, PropertyDescription> getPropertyDescriptionMap()
    {
        return propertyDescriptionMap;
    }


    public void setPropertyDescriptionMap(Map<String, PropertyDescription> propertyDescriptionMap)
    {
        this.propertyDescriptionMap = propertyDescriptionMap;
    }


    public Map<String, Concept> getConceptBeadMap()
    {
        return conceptBeadMap;
    }


    public void setConceptBeadMap(Map<String, Concept> conceptBeadMap)
    {
        this.conceptBeadMap = conceptBeadMap;
    }


    public Map<String, PropertyGroup> getPropertyGroupMap()
    {
        return propertyGroupMap;
    }


    public void setPropertyGroupMap(Map<String, PropertyGroup> propertyGroupMap)
    {
        this.propertyGroupMap = propertyGroupMap;
    }


    public List<ConceptGroup> getOrphanedConceptGroups()
    {
        if ((orphanedConceptGroups == null) || (orphanedConceptGroups.isEmpty()))
        {
            return null;
        }

        return orphanedConceptGroups;
    }


    public void setOrphanedConceptGroups(List<ConceptGroup> orphanedConceptGroups)
    {
        this.orphanedConceptGroups = orphanedConceptGroups;
    }


    public List<String> getErrorReport()
    {
        if (errorReport == null)
        {
            return null;
        }
        else if (errorReport.isEmpty())
        {
            return null;
        }
        else
        {
            return errorReport;
        }
    }


    public void setErrorReport(List<String> errorReport)
    {
        this.errorReport = errorReport;
    }
}
