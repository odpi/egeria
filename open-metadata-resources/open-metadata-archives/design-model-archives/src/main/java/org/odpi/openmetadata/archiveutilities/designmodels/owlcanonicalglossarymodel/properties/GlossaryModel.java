/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.archiveutilities.designmodels.owlcanonicalglossarymodel.properties;

import org.apache.jena.rdf.model.Literal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Model provides the top level anchor for the model content
 */
public class GlossaryModel
{
    private String                           modelName              = null;
    private String                           modelTechnicalName     = null;
    private String                           modelDescription       = null;
    private String                           modelLocation          = null;
    private String                           modelScope             = null;
    private String                           modelLanguage          = null;
    private String                           license                = null;
    Map<String, Map<String, Literal>> objectPropertyTermLiteralMap = new HashMap<>(); //owl:ObjectProperty
    Map<String, Map<String, Literal>> datatypePropertyTermLiteralMap = new HashMap<>(); //owl:DatatypeProperty

    Map<String, Map<String, Literal>> glossaryLiteralMap = new HashMap<>();    // Owl:Ontology
    Map<String, Map<String, Literal>> categoryLiteralMap = new HashMap<>();    // rdf:container
    Map<String, Map<String, Literal>> conceptTermLiteralMap = new HashMap<>();  // rdfs:class

    // member map - containment relationships for categories and classifications
    Map<String, Set<String>> conceptTermMemberMap = new HashMap<>();
    Map<String, Set<String>> propertyTermMemberMap = new HashMap<>();
    Map<String, Set<String>> containmentMemberMap = new HashMap<>();
    // examples
    Map<String, Set<String>>  exampleMap = new HashMap<>();
    // subclasses
    Map<String, String> LiteralSubClassMap = new HashMap<>();
    Map<String, String> resourceSubClassMap = new HashMap<>();
    // range
    Map<String, Set<String>> rangesMap = new HashMap<>();
    // isDefined by -anchors
    Map<String, String>  isDefinedByMap = new HashMap<>();
    // domain property to owning concept
    Map<String, Set<String>> domainsMap = new HashMap<>();

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


    public String getModelDescription()
    {
        return modelDescription;
    }


    public void setModelDescription(String modelDescription)
    {
        this.modelDescription = modelDescription;
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

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public Map<String, Map<String, Literal>> getObjectPropertyTermLiteralMap() {
        return objectPropertyTermLiteralMap;
    }

    public void setObjectPropertyTermLiteralMap(Map<String, Map<String, Literal>> objectPropertyTermLiteralMap) {
        this.objectPropertyTermLiteralMap = objectPropertyTermLiteralMap;
    }

    public Map<String, Map<String, Literal>> getDatatypePropertyTermLiteralMap() {
        return datatypePropertyTermLiteralMap;
    }

    public Map<String, Map<String, Literal>> getGlossaryLiteralMap() {
        return glossaryLiteralMap;
    }


    public Map<String, Map<String, Literal>> getCategoryLiteralMap() {
        return categoryLiteralMap;
    }

    public Map<String, Map<String, Literal>> getConceptTermLiteralMap() {
        return conceptTermLiteralMap;
    }

    public Map<String, Set<String>> getConceptTermMemberMap() {
        return conceptTermMemberMap;
    }

    public Map<String, Set<String>> getPropertyTermMemberMap() {
        return propertyTermMemberMap;
    }

    public Map<String, Set<String>> getContainmentMemberMap() {
        return containmentMemberMap;
    }

    public Map<String, String> getLiteralSubClassMap() {
        return LiteralSubClassMap;
    }

    public Map<String, String> getResourceSubClassMap() {
        return resourceSubClassMap;
    }

    public Map<String, Set<String>> getRangesMap() {
        return rangesMap;
    }
    public Map<String, String> getIsDefinedByMap() {
        return isDefinedByMap;
    }

    public void setRangesMap(Map<String, Set<String>> rangesMap) {
        this.rangesMap = rangesMap;
    }
    public Map<String, Set<String>> getDomainsMap() {
        return domainsMap;
    }

    public void setDomainsMap(Map<String, Set<String>> domainsMap) {
        this.domainsMap = domainsMap;
    }

    public Map<String, Set<String>> getExampleMap() {
        return exampleMap;
    }

    public void setExampleMap(Map<String, Set<String>> exampleMap) {
        this.exampleMap = exampleMap;
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
