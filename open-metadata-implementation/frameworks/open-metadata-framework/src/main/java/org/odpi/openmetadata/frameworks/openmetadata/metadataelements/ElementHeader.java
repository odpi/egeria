/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.metadataelements;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ElementHeader provides the common identifier and type information for all properties objects
 * that link off of the asset and have a guid associated with them.  This typically means it is
 * represented by an entity in the metadata repository.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
              include = JsonTypeInfo.As.PROPERTY,
              property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = ElementStub.class, name = "ElementStub"),
        })
public class ElementHeader extends ElementControlHeader
{
    /*
     * Common header for entities from a metadata repository
     */
    private String                      guid                     = null;
    private ElementClassification       anchor                   = null;
    private ElementClassification       latestChange             = null;
    private ElementClassification       zoneMembership           = null;
    private ElementClassification       subjectArea              = null;
    private ElementClassification       impact                   = null;
    private ElementClassification       criticality              = null;
    private ElementClassification       confidentiality          = null;
    private ElementClassification       confidence               = null;
    private ElementClassification       retention                = null;
    private ElementClassification       governanceExpectations   = null;
    private ElementClassification       governanceMeasurements   = null;
    private List<ElementClassification> executionPoints          = null;
    private List<ElementClassification> duplicateClassifications = null;
    private ElementClassification       digitalResourceOrigin    = null;
    private ElementClassification       ownership                = null;
    private ElementClassification       memento                  = null;
    private ElementClassification       template                 = null;
    private ElementClassification       templateSubstitute       = null;
    private ElementClassification       schemaType               = null; // TypeEmbeddedAttribute
    private ElementClassification       dataScope                = null;
    private ElementClassification       dataAssetEncoding        = null;
    private ElementClassification       calculatedValue          = null;
    private ElementClassification       primaryKey               = null;
    private ElementClassification       knownDuplicate           = null;
    private ElementClassification       consolidateDuplicate     = null;
    private List<ElementClassification> collectionRoles          = null;
    private List<ElementClassification> projectRoles             = null;
    private List<ElementClassification> otherClassifications     = null;


    /**
     * Default constructor used by subclasses
     */
    public ElementHeader()
    {
    }


    /**
     * Copy/clone constructor.
     *
     * @param template element to copy
     */
    public ElementHeader(ElementHeader template)
    {
        super(template);

        if (template != null)
        {
            this.guid                     = template.getGUID();
            this.anchor                   = template.getAnchor();
            this.latestChange             = template.getLatestChange();
            this.zoneMembership           = template.getZoneMembership();
            this.subjectArea              = template.getSubjectArea();
            this.impact                   = template.getImpact();
            this.criticality              = template.getCriticality();
            this.confidentiality          = template.getConfidentiality();
            this.confidence               = template.getConfidence();
            this.retention                = template.getRetention();
            this.governanceExpectations   = template.getGovernanceExpectations();
            this.governanceMeasurements   = template.getGovernanceMeasurements();
            this.executionPoints          = template.getExecutionPoints();
            this.duplicateClassifications = template.getDuplicateClassifications();
            this.digitalResourceOrigin    = template.getDigitalResourceOrigin();
            this.ownership                = template.getOwnership();
            this.memento                  = template.getMemento();
            this.template                 = template.getTemplate();
            this.templateSubstitute       = template.getTemplateSubstitute();
            this.schemaType               = template.getSchemaType();
            this.dataScope                = template.getDataScope();
            this.dataAssetEncoding        = template.getDataAssetEncoding();
            this.calculatedValue          = template.getCalculatedValue();
            this.primaryKey               = template.getPrimaryKey();
            this.knownDuplicate           = template.getKnownDuplicate();
            this.consolidateDuplicate     = template.getConsolidateDuplicate();
            this.collectionRoles          = template.getCollectionRoles();
            this.projectRoles             = template.getProjectRoles();
            this.otherClassifications     = template.getOtherClassifications();
        }
    }


    /**
     * Copy/clone constructor.
     *
     * @param template element to copy
     */
    public ElementHeader(ElementControlHeader template)
    {
        super(template);
    }


    /**
     * Return the unique id for the properties object.  Null means no guid is assigned.
     *
     * @return String unique id
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Set up the guid for the element.
     *
     * @param guid String unique identifier
     */
    public void setGUID(String guid)
    {
        this.guid = guid;
    }


    /**
     * Return the Anchors classification
     *
     * @return classification
     */
    public ElementClassification getAnchor()
    {
        return anchor;
    }


    /**
     * Set up the Anchors classification.
     *
     * @param anchor classification
     */
    public void setAnchor(ElementClassification anchor)
    {
        this.anchor = anchor;
    }


    /**
     * Return the properties of the LatestChange classification.
     *
     * @return classification
     */
    public ElementClassification getLatestChange()
    {
        return latestChange;
    }


    /**
     * Set up the properties of the LatestChange classification
     *
     * @param latestChange classification
     */
    public void setLatestChange(ElementClassification latestChange)
    {
        this.latestChange = latestChange;
    }


    /**
     * Return the zoneMembership classification.
     *
     * @return classification
     */
    public ElementClassification getZoneMembership()
    {
        return zoneMembership;
    }


    /**
     * Set up the zoneMembership classification.
     *
     * @param zoneMembership classification
     */
    public void setZoneMembership(ElementClassification zoneMembership)
    {
        this.zoneMembership = zoneMembership;
    }


    /**
     * Return the subject area.
     *
     * @return classification
     */
    public ElementClassification getSubjectArea()
    {
        return subjectArea;
    }


    /**
     * Set up the subject area.
     *
     * @param subjectArea classification
     */
    public void setSubjectArea(ElementClassification subjectArea)
    {
        this.subjectArea = subjectArea;
    }


    /**
     * Return the impact assessment for this element.
     *
     * @return classification
     */
    public ElementClassification getImpact()
    {
        return impact;
    }


    /**
     * Set up the impact assessment for this element.
     *
     * @param impact classification
     */
    public void setImpact(ElementClassification impact)
    {
        this.impact = impact;
    }


    /**
     * Return the criticality assessment for this element.
     *
     * @return classification
     */
    public ElementClassification getCriticality()
    {
        return criticality;
    }


    /**
     * Set up the criticality assessment for this element.
     *
     * @param criticality classification
     */
    public void setCriticality(ElementClassification criticality)
    {
        this.criticality = criticality;
    }


    /**
     * Return the level of confidentiality assigned to this element.
     *
     * @return classification
     */
    public ElementClassification getConfidentiality()
    {
        return confidentiality;
    }


    /**
     * Set up the level of confidentiality assigned to this element.
     *
     * @param confidentiality classification
     */
    public void setConfidentiality(ElementClassification confidentiality)
    {
        this.confidentiality = confidentiality;
    }


    /**
     * Return any confidence assessment associated with this element.
     *
     * @return classification
     */
    public ElementClassification getConfidence()
    {
        return confidence;
    }


    /**
     * Set up any confidence assessment associated with this element.
     *
     * @param confidence classification
     */
    public void setConfidence(ElementClassification confidence)
    {
        this.confidence = confidence;
    }


    /**
     * Return any retention requirements.
     *
     * @return classification
     */
    public ElementClassification getRetention()
    {
        return retention;
    }


    /**
     * Set up any retention requirements.
     *
     * @param retention classification
     */
    public void setRetention(ElementClassification retention)
    {
        this.retention = retention;
    }


    /**
     * Return the governance expectations classification.
     *
     * @return classification
     */
    public ElementClassification getGovernanceExpectations()
    {
        return governanceExpectations;
    }


    /**
     * Set up the governance expectations classification.
     *
     * @param governanceExpectations classifications
     */
    public void setGovernanceExpectations(ElementClassification governanceExpectations)
    {
        this.governanceExpectations = governanceExpectations;
    }


    /**
     * Return the governance measurements classification.
     *
     * @return classification
     */
    public ElementClassification getGovernanceMeasurements()
    {
        return governanceMeasurements;
    }


    /**
     * Set up the governance measurements classification.
     *
     * @param governanceMeasurements classification
     */
    public void setGovernanceMeasurements(ElementClassification governanceMeasurements)
    {
        this.governanceMeasurements = governanceMeasurements;
    }


    /**
     * Return details of whether this element is identified as providing the implementation of one or more execution points.
     *
     * @return list
     */
    public List<ElementClassification> getExecutionPoints()
    {
        return executionPoints;
    }


    /**
     * Set up details of whether this element is identified as providing the implementation of one or more execution points.
     *
     * @param executionPoints list
     */
    public void setExecutionPoints(List<ElementClassification> executionPoints)
    {
        this.executionPoints = executionPoints;
    }


    /**
     * Return the DigitalResourceOrigin classification.
     *
     * @return classification
     */
    public ElementClassification getDigitalResourceOrigin()
    {
        return digitalResourceOrigin;
    }


    /**
     * Set up the DigitalResourceOrigin classification.
     *
     * @param digitalResourceOrigin classification
     */
    public void setDigitalResourceOrigin(ElementClassification digitalResourceOrigin)
    {
        this.digitalResourceOrigin = digitalResourceOrigin;
    }


    /**
     * Return the Ownership classification.
     *
     * @return classification
     */
    public ElementClassification getOwnership()
    {
        return ownership;
    }


    /**
     * Set up the Ownership classification.
     *
     * @param ownership classification
     */
    public void setOwnership(ElementClassification ownership)
    {
        this.ownership = ownership;
    }


    /**
     * Return the memento classification that shows that the element has been archived.
     *
     * @return classification
     */
    public ElementClassification getMemento()
    {
        return memento;
    }


    /**
     * Set up the memento classification that shows that the element has been archived.
     *
     * @param memento classification
     */
    public void setMemento(ElementClassification memento)
    {
        this.memento = memento;
    }


    /**
     * Return whether this element has the Template classification that shows that the element is a template.
     *
     * @return classification
     */
    public ElementClassification getTemplate()
    {
        return template;
    }


    /**
     * Set up whether this element has the Template classification that shows that the element is a template.
     *
     * @param template classification
     */
    public void setTemplate(ElementClassification template)
    {
        this.template = template;
    }


    /**
     * Return the classification that indicates this element is part of a template. It
     * links to an element via the SourcedFrom relationship that the template logically links to
     * using a significant relationship.  The template substitute removes confusion that would occur if the
     * template linked directly to this element using the significant relationship type.
     *
     * @return classification
     */
    public ElementClassification getTemplateSubstitute()
    {
        return templateSubstitute;
    }


    /**
     * Set up the template substitute classification.
     *
     * @param templateSubstitute classification
     */
    public void setTemplateSubstitute(ElementClassification templateSubstitute)
    {
        this.templateSubstitute = templateSubstitute;
    }


    /**
     * Return whether this element is involved in duplicate processing.
     *
     * @return list
     */
    public List<ElementClassification> getDuplicateClassifications()
    {
        return duplicateClassifications;
    }


    /**
     * Set up whether this element is involved in duplicate processing.
     *
     * @param duplicateClassifications list
     */
    public void setDuplicateClassifications(List<ElementClassification> duplicateClassifications)
    {
        this.duplicateClassifications = duplicateClassifications;
    }


    /**
     * Return the TypeEmbeddedAttribute classification - only attached to a SchemaAttribute.
     *
     * @return classification
     */
    public ElementClassification getSchemaType()
    {
        return schemaType;
    }


    /**
     * Set the TypeEmbeddedAttribute classification - only attached to a SchemaAttribute.
     *
     * @param schemaType classification
     */
    public void setSchemaType(ElementClassification schemaType)
    {
        this.schemaType = schemaType;
    }


    /**
     * Return the DataScope classifications - used to describe the scope of data stored in the associated digital resource.
     *
     * @return classification
     */
    public ElementClassification getDataScope()
    {
        return dataScope;
    }


    /**
     * Set up the DataScope classifications - used to describe the scope of data stored in the associated digital resource.
     *
     * @param dataScope classification
     */
    public void setDataScope(ElementClassification dataScope)
    {
        this.dataScope = dataScope;
    }


    /**
     * Return the DataAssetEncoding classification that describes the encoding used in the associated digital resource.
     *
     * @return classification
     */
    public ElementClassification getDataAssetEncoding()
    {
        return dataAssetEncoding;
    }


    /**
     * Set up the DataAssetEncoding classification that describes the encoding used in the associated digital resource.
     *
     * @param dataAssetEncoding classification
     */
    public void setDataAssetEncoding(ElementClassification dataAssetEncoding)
    {
        this.dataAssetEncoding = dataAssetEncoding;
    }


    /**
     * Return the CalculatedValue classification - only attached to a SchemaAttribute.
     *
     * @return   classification
     */
    public ElementClassification getCalculatedValue()
    {
        return calculatedValue;
    }


    /**
     * Set the CalculatedValue classification - only attached to a SchemaAttribute.
     *
     * @param calculatedValue classification
     */
    public void setCalculatedValue(ElementClassification calculatedValue)
    {
        this.calculatedValue = calculatedValue;
    }


    /**
     * Return whether this element (TabularColumn) is a primary key.
     *
     * @return classification
     */
    public ElementClassification getPrimaryKey()
    {
        return primaryKey;
    }


    /**
     * Set up whether this element (TabularColumn) is a primary key.
     *
     * @param primaryKey classification
     */
    public void setPrimaryKey(ElementClassification primaryKey)
    {
        this.primaryKey = primaryKey;
    }


    /**
     * Return whether this element has a known duplicate.
     *
     * @return classification
     */
    public ElementClassification getKnownDuplicate()
    {
        return knownDuplicate;
    }


    /**
     * Set up whether this element has a known duplicate.
     *
     * @param knownDuplicate classification
     */
    public void setKnownDuplicate(ElementClassification knownDuplicate)
    {
        this.knownDuplicate = knownDuplicate;
    }


    /**
     * Return whether this element is a consolidated duplication.
     *
     * @return classification
     */
    public ElementClassification getConsolidateDuplicate()
    {
        return consolidateDuplicate;
    }


    /**
     * Set up whether this element is a consolidated duplication.
     *
     * @param consolidateDuplicate classification
     */
    public void setConsolidateDuplicate(ElementClassification consolidateDuplicate)
    {
        this.consolidateDuplicate = consolidateDuplicate;
    }


    /**
     * Return the optional list of category classifications found on a collection entity that indicate how a collection is being used.
     *
     * @return list of classifications
     */
    public List<ElementClassification> getCollectionRoles()
    {
        return collectionRoles;
    }


    /**
     * Set up the optional list of category classifications found on a collection entity that indicate how a collection is being used.

     * @param collectionRoles list of classifications
     */
    public void setCollectionRoles(List<ElementClassification> collectionRoles)
    {
        this.collectionRoles = collectionRoles;
    }


    /**
     * Return the optional list of category classifications found on a project entity that indicate how a collection is being used.
     *
     * @return list of classifications
     */
    public List<ElementClassification> getProjectRoles()
    {
        return projectRoles;
    }


    /**
     * Set up the optional list of category classifications found on a project entity that indicate how a collection is being used.

     * @param projectRoles list of classifications
     */
    public void setProjectRoles(List<ElementClassification> projectRoles)
    {
        this.projectRoles = projectRoles;
    }


    /**
     * Return the list of remaining classifications associated with the metadata element.
     *
     * @return Classifications  list of classifications
     */
    public List<ElementClassification> getOtherClassifications()
    {
        return otherClassifications;
    }


    /**
     * Set up the list of remaining classifications associated with this metadata element.
     *
     * @param otherClassifications list of classifications
     */
    public void setOtherClassifications(List<ElementClassification> otherClassifications)
    {
        this.otherClassifications = otherClassifications;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ElementHeader{" +
                "guid='" + guid + '\'' +
                ", anchor=" + anchor +
                ", latestChange=" + latestChange +
                ", zoneMembership=" + zoneMembership +
                ", subjectArea=" + subjectArea +
                ", impact=" + impact +
                ", criticality=" + criticality +
                ", confidentiality=" + confidentiality +
                ", confidence=" + confidence +
                ", retention=" + retention +
                ", governanceExpectations=" + governanceExpectations +
                ", governanceMeasurements=" + governanceMeasurements +
                ", executionPoints=" + executionPoints +
                ", duplicateClassifications=" + duplicateClassifications +
                ", digitalResourceOrigin=" + digitalResourceOrigin +
                ", ownership=" + ownership +
                ", memento=" + memento +
                ", template=" + template +
                ", schemaType=" + schemaType +
                ", dataScope=" + dataScope +
                ", dataAssetEncoding=" + dataAssetEncoding +
                ", calculatedValue=" + calculatedValue +
                ", primaryKey=" + primaryKey +
                ", collectionCategories=" + collectionRoles +
                ", projectRoles=" + projectRoles +
                ", otherClassifications=" + otherClassifications +
                ", GUID='" + getGUID() + '\'' +
                "} " + super.toString();
    }


    /**
     * Compare the values of the supplied object with those stored in the current object.
     *
     * @param objectToCompare supplied object
     * @return boolean result of comparison
     */
    @Override
    public boolean equals(Object objectToCompare)
    {
        if (this == objectToCompare) return true;
        if (objectToCompare == null || getClass() != objectToCompare.getClass()) return false;
        if (!super.equals(objectToCompare)) return false;
        ElementHeader that = (ElementHeader) objectToCompare;
        return Objects.equals(guid, that.guid) &&
                Objects.equals(anchor, that.anchor) &&
                Objects.equals(latestChange, that.latestChange) &&
                Objects.equals(zoneMembership, that.zoneMembership) &&
                Objects.equals(subjectArea, that.subjectArea) &&
                Objects.equals(impact, that.impact) &&
                Objects.equals(criticality, that.criticality) &&
                Objects.equals(confidentiality, that.confidentiality) &&
                Objects.equals(confidence, that.confidence) &&
                Objects.equals(retention, that.retention) &&
                Objects.equals(governanceExpectations, that.governanceExpectations) &&
                Objects.equals(governanceMeasurements, that.governanceMeasurements) &&
                Objects.equals(executionPoints, that.executionPoints) &&
                Objects.equals(duplicateClassifications, that.duplicateClassifications) &&
                Objects.equals(digitalResourceOrigin, that.digitalResourceOrigin) &&
                Objects.equals(dataScope, that.dataScope) &&
                Objects.equals(dataAssetEncoding, that.dataAssetEncoding) &&
                Objects.equals(ownership, that.ownership) &&
                Objects.equals(memento, that.memento) &&
                Objects.equals(template, that.template) &&
                Objects.equals(templateSubstitute, that.templateSubstitute) &&
                Objects.equals(schemaType, that.schemaType) &&
                Objects.equals(calculatedValue, that.calculatedValue) &&
                Objects.equals(primaryKey, that.primaryKey) &&
                Objects.equals(knownDuplicate, that.knownDuplicate) &&
                Objects.equals(consolidateDuplicate, that.consolidateDuplicate) &&
                Objects.equals(collectionRoles, that.collectionRoles) &&
                Objects.equals(projectRoles, that.projectRoles) &&
                Objects.equals(otherClassifications, that.otherClassifications);
    }


    /**
     * Create a hash code for this element type.
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), guid, anchor, latestChange, zoneMembership, subjectArea, impact, criticality,
                            confidentiality, confidence, retention, governanceExpectations, governanceMeasurements,
                            executionPoints, duplicateClassifications, ownership, digitalResourceOrigin, memento,
                            dataScope, dataAssetEncoding,
                            template, templateSubstitute, schemaType, calculatedValue, primaryKey,
                            knownDuplicate, consolidateDuplicate,
                            collectionRoles, projectRoles, otherClassifications);
    }
}