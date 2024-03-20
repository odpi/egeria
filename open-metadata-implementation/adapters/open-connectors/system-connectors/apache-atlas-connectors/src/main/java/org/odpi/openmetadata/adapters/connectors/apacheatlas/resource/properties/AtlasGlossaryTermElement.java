/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.apacheatlas.resource.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
import java.util.Set;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * AtlasGlossaryTermElement describes an Apache Atlas glossary term used to call Apache Atlas.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AtlasGlossaryTermElement extends AtlasGlossaryMemberBaseProperties
{
    private String                           abbreviation    = null;
    private String                           usage           = null;
    private List<AtlasClassification>        classifications = null;
    private List<AtlasRelatedCategoryHeader> categories      = null;
    private List<String>                     examples        = null;

    private Set<AtlasRelatedTermHeader> seeAlso = null;

    // Term Synonyms
    private Set<AtlasRelatedTermHeader> synonyms = null;

    // Term antonyms
    private Set<AtlasRelatedTermHeader> antonyms = null;

    // Term preference
    private Set<AtlasRelatedTermHeader> preferredTerms = null;
    private Set<AtlasRelatedTermHeader> preferredToTerms = null;

    // Term replacements
    private Set<AtlasRelatedTermHeader> replacementTerms = null;
    private Set<AtlasRelatedTermHeader> replacedBy = null;

    // Term translations
    private Set<AtlasRelatedTermHeader> translationTerms = null;
    private Set<AtlasRelatedTermHeader> translatedTerms = null;

    // Term classification
    private Set<AtlasRelatedTermHeader> isA = null;
    private Set<AtlasRelatedTermHeader> classifies = null;

    // Values for terms
    private Set<AtlasRelatedTermHeader> validValues = null;
    private Set<AtlasRelatedTermHeader> validValuesFor = null;

    private boolean hasTerms = false;



    /**
     * Standard constructor
     */
    public AtlasGlossaryTermElement()
    {
    }


    /**
     * Copy/clone Constructor
     *
     * @param template object to copy
     */
    public AtlasGlossaryTermElement(AtlasGlossaryTermElement template)
    {
        super(template);

        if (template != null)
        {
            abbreviation = template.getAbbreviation();
            usage = template.getUsage();
            classifications = template.getClassifications();
            categories = template.getCategories();
        }
    }


    public String getAbbreviation()
    {
        return abbreviation;
    }


    public void setAbbreviation(String abbreviation)
    {
        this.abbreviation = abbreviation;
    }


    public String getUsage()
    {
        return usage;
    }


    public void setUsage(String usage)
    {
        this.usage = usage;
    }


    public List<AtlasClassification> getClassifications()
    {
        return classifications;
    }


    public void setClassifications(
            List<AtlasClassification> classifications)
    {
        this.classifications = classifications;
    }


    public List<AtlasRelatedCategoryHeader> getCategories()
    {
        return categories;
    }


    public void setCategories(List<AtlasRelatedCategoryHeader> categories)
    {
        this.categories = categories;
    }


    public List<String> getExamples()
    {
        return examples;
    }


    public void setExamples(List<String> examples)
    {
        this.examples = examples;
    }


    public Set<AtlasRelatedTermHeader> getSeeAlso()
    {
        return seeAlso;
    }


    public void setSeeAlso(Set<AtlasRelatedTermHeader> seeAlso)
    {
        this.seeAlso = seeAlso;
    }


    public Set<AtlasRelatedTermHeader> getSynonyms()
    {
        return synonyms;
    }


    public void setSynonyms(Set<AtlasRelatedTermHeader> synonyms)
    {
        this.synonyms = synonyms;
    }


    public Set<AtlasRelatedTermHeader> getAntonyms()
    {
        return antonyms;
    }


    public void setAntonyms(Set<AtlasRelatedTermHeader> antonyms)
    {
        this.antonyms = antonyms;
    }


    public Set<AtlasRelatedTermHeader> getPreferredTerms()
    {
        return preferredTerms;
    }


    public void setPreferredTerms(
            Set<AtlasRelatedTermHeader> preferredTerms)
    {
        this.preferredTerms = preferredTerms;
    }


    public Set<AtlasRelatedTermHeader> getPreferredToTerms()
    {
        return preferredToTerms;
    }


    public void setPreferredToTerms(
            Set<AtlasRelatedTermHeader> preferredToTerms)
    {
        this.preferredToTerms = preferredToTerms;
    }


    public Set<AtlasRelatedTermHeader> getReplacementTerms()
    {
        return replacementTerms;
    }


    public void setReplacementTerms(
            Set<AtlasRelatedTermHeader> replacementTerms)
    {
        this.replacementTerms = replacementTerms;
    }


    public Set<AtlasRelatedTermHeader> getReplacedBy()
    {
        return replacedBy;
    }


    public void setReplacedBy(Set<AtlasRelatedTermHeader> replacedBy)
    {
        this.replacedBy = replacedBy;
    }


    public Set<AtlasRelatedTermHeader> getTranslationTerms()
    {
        return translationTerms;
    }


    public void setTranslationTerms(
            Set<AtlasRelatedTermHeader> translationTerms)
    {
        this.translationTerms = translationTerms;
    }


    public Set<AtlasRelatedTermHeader> getTranslatedTerms()
    {
        return translatedTerms;
    }


    public void setTranslatedTerms(
            Set<AtlasRelatedTermHeader> translatedTerms)
    {
        this.translatedTerms = translatedTerms;
    }


    public Set<AtlasRelatedTermHeader> getIsA()
    {
        return isA;
    }


    public void setIsA(Set<AtlasRelatedTermHeader> isA)
    {
        this.isA = isA;
    }


    public Set<AtlasRelatedTermHeader> getClassifies()
    {
        return classifies;
    }


    public void setClassifies(Set<AtlasRelatedTermHeader> classifies)
    {
        this.classifies = classifies;
    }


    public Set<AtlasRelatedTermHeader> getValidValues()
    {
        return validValues;
    }


    public void setValidValues(Set<AtlasRelatedTermHeader> validValues)
    {
        this.validValues = validValues;
    }


    public Set<AtlasRelatedTermHeader> getValidValuesFor()
    {
        return validValuesFor;
    }


    public void setValidValuesFor(
            Set<AtlasRelatedTermHeader> validValuesFor)
    {
        this.validValuesFor = validValuesFor;
    }


    public boolean getHasTerms()
    {
        return hasTerms;
    }


    public void setHasTerms(boolean hasTerms)
    {
        this.hasTerms = hasTerms;
    }


    @Override
    public String toString()
    {
        return "AtlasGlossaryTermElement{" +
                       "abbreviation='" + abbreviation + '\'' +
                       ", usage='" + usage + '\'' +
                       ", classifications=" + classifications +
                       ", categories=" + categories +
                       ", examples=" + examples +
                       ", seeAlso=" + seeAlso +
                       ", synonyms=" + synonyms +
                       ", antonyms=" + antonyms +
                       ", preferredTerms=" + preferredTerms +
                       ", preferredToTerms=" + preferredToTerms +
                       ", replacementTerms=" + replacementTerms +
                       ", replacedBy=" + replacedBy +
                       ", translationTerms=" + translationTerms +
                       ", translatedTerms=" + translatedTerms +
                       ", isA=" + isA +
                       ", classifies=" + classifies +
                       ", validValues=" + validValues +
                       ", validValuesFor=" + validValuesFor +
                       ", hasTerms=" + hasTerms +
                       ", anchor=" + getAnchor() +
                       ", guid='" + getGuid() + '\'' +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", name='" + getName() + '\'' +
                       ", shortDescription='" + getShortDescription() + '\'' +
                       ", longDescription='" + getLongDescription() + '\'' +
                       ", additionalAttributes=" + getAdditionalAttributes() +
                       '}';
    }
}
