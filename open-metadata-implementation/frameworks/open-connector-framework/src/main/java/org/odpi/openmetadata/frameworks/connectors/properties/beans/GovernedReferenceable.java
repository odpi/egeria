/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties.beans;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * GovernedReferenceable adds governance relationships and classifications to a referenceable that represents
 * an asset.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "class")
@JsonSubTypes(
        {
                @JsonSubTypes.Type(value = Asset.class, name = "Asset"),
                @JsonSubTypes.Type(value = SchemaElement.class, name = "SchemaElement"),
        })
public class GovernedReferenceable extends Referenceable
{
    private static final long     serialVersionUID = 1L;

    protected List<Meaning>                           meanings                                = null;
    protected SecurityTags                            securityTags                            = null;
    protected List<SearchKeyword>                     searchKeywords                          = null;
    protected String                                  latestChange                            = null;
    protected LatestChange                            latestChangeDetails                     = null;
    protected ConfidentialityGovernanceClassification confidentialityGovernanceClassification = null;
    protected ConfidenceGovernanceClassification      confidenceGovernanceClassification      = null;
    protected CriticalityGovernanceClassification     criticalityGovernanceClassification     = null;
    protected RetentionGovernanceClassification       retentionGovernanceClassification       = null;


    /**
     * Default constructor
     */
    public GovernedReferenceable()
    {
        super();
    }


    /**
     * Copy/clone constructor.  Retrieves values from the supplied template
     *
     * @param template element to copy
     */
    public GovernedReferenceable(GovernedReferenceable template)
    {
        super(template);

        if (template != null)
        {
            meanings                                = template.getMeanings();
            securityTags                            = template.getSecurityTags();
            searchKeywords                          = template.getSearchKeywords();
            latestChange                            = template.getLatestChange();
            latestChangeDetails                     = template.getLatestChangeDetails();
            confidentialityGovernanceClassification = template.getConfidentialityGovernanceClassification();
            confidenceGovernanceClassification      = template.getConfidenceGovernanceClassification();
            criticalityGovernanceClassification     = template.getCriticalityGovernanceClassification();
            retentionGovernanceClassification       = template.getRetentionGovernanceClassification();
        }
    }


    /**
     * Return the assigned meanings for this metadata entity.
     *
     * @return list of meanings
     */
    public List<Meaning> getMeanings()
    {
        if (meanings == null)
        {
            return null;
        }
        else if (meanings.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(meanings);
        }
    }


    /**
     * Set up the assigned meanings for this metadata entity.
     *
     * @param meanings list of meanings
     */
    public void setMeanings(List<Meaning> meanings)
    {
        this.meanings = meanings;
    }


    /**
     * Return the information used by security engines to secure access to the asset's content.
     *
     * @return security labels and properties
     */
    public SecurityTags getSecurityTags()
    {
        return securityTags;
    }


    /**
     * Set up the information used by security engines to secure access to the asset's content.
     *
     * @param securityTags security labels and properties
     */
    public void setSecurityTags(SecurityTags securityTags)
    {
        this.securityTags = securityTags;
    }


    /**
     * Return a list of keywords that will help an asset consumer to locate this asset.
     *
     * @return list of strings
     */
    public List<SearchKeyword> getSearchKeywords()
    {
        if (searchKeywords == null)
        {
            return null;
        }

        if (searchKeywords.isEmpty())
        {
            return null;
        }

        return searchKeywords;
    }


    /**
     * Set up a list of keywords that will help an asset consumer to locate this asset.
     *
     * @param searchKeywords list of strings
     */
    public void setSearchKeywords(List<SearchKeyword> searchKeywords)
    {
        this.searchKeywords = searchKeywords;
    }


    /**
     * Return a short description of the last change to the asset.  If it is null it means
     * the agent that last updated the asset did not provide a description.
     *
     * @return string description
     */
    public String getLatestChange()
    {
        if (latestChange == null)
        {
            if (latestChangeDetails != null)
            {
                return latestChangeDetails.getActionDescription();
            }
        }

        return latestChange;
    }


    /**
     * Set up a short description of the last change to the asset.
     *
     * @param latestChange string description
     */
    public void setLatestChange(String latestChange)
    {
        this.latestChange = latestChange;
    }


    /**
     * Return full details of the latest change to the asset universe.
     *
     * @return latest change properties
     */
    public LatestChange getLatestChangeDetails()
    {
        return latestChangeDetails;
    }


    /**
     * Set up full details of the latest change to the asset universe.
     *
     * @param latestChangeDetails latest change properties
     */
    public void setLatestChangeDetails(LatestChange latestChangeDetails)
    {
        this.latestChangeDetails = latestChangeDetails;
    }


    /**
     * Return the classification that defines how confidential the contents of this referenceable are.
     *
     * @return confidentiality classification properties
     */
    public ConfidentialityGovernanceClassification getConfidentialityGovernanceClassification()
    {
        return confidentialityGovernanceClassification;
    }


    /**
     * Set up the classification that defines how confidential the contents of this referenceable are.
     *
     * @param confidentialityGovernanceClassification confidentiality classification properties
     */
    public void setConfidentialityGovernanceClassification(ConfidentialityGovernanceClassification confidentialityGovernanceClassification)
    {
        this.confidentialityGovernanceClassification = confidentialityGovernanceClassification;
    }


    /**
     * Return the classification that defines how confident a user should be in the quality of the contents of this referenceable are.
     *
     * @return confidence classification properties
     */
    public ConfidenceGovernanceClassification getConfidenceGovernanceClassification()
    {
        return confidenceGovernanceClassification;
    }


    /**
     * Set up the classification that defines how confident a user should be in the quality of the contents of this referenceable are.
     *
     * @param confidenceGovernanceClassification confidence classification properties
     */
    public void setConfidenceGovernanceClassification(ConfidenceGovernanceClassification confidenceGovernanceClassification)
    {
        this.confidenceGovernanceClassification = confidenceGovernanceClassification;
    }


    /**
     * Return the classification that defines how critical this referenceable is to the business.
     *
     * @return criticality classification properties
     */
    public CriticalityGovernanceClassification getCriticalityGovernanceClassification()
    {
        return criticalityGovernanceClassification;
    }


    /**
     * Set up the classification that defines how critical this referenceable is to the business.
     *
     * @param criticalityGovernanceClassification criticality classification properties
     */
    public void setCriticalityGovernanceClassification(CriticalityGovernanceClassification criticalityGovernanceClassification)
    {
        this.criticalityGovernanceClassification = criticalityGovernanceClassification;
    }


    /**
     * Return the classification that defines when this referenceable will be archived and then permanently deleted.
     *
     * @return retention classification properties
     */
    public RetentionGovernanceClassification getRetentionGovernanceClassification()
    {
        return retentionGovernanceClassification;
    }


    /**
     * Set up the classification that defines when this referenceable will be archived and then permanently deleted.
     *
     * @param retentionGovernanceClassification retention classification properties
     */
    public void setRetentionGovernanceClassification(RetentionGovernanceClassification retentionGovernanceClassification)
    {
        this.retentionGovernanceClassification = retentionGovernanceClassification;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "GovernedReferenceable{" +
                       "URL='" + getURL() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", status=" + getStatus() +
                       ", type=" + getType() +
                       ", origin=" + getOrigin() +
                       ", versions=" + getVersions() +
                       ", GUID='" + getGUID() + '\'' +
                       ", classifications=" + getClassifications() +
                       ", meanings=" + meanings +
                       ", securityTags=" + securityTags +
                       ", searchKeywords=" + searchKeywords +
                       ", latestChange='" + latestChange + '\'' +
                       ", latestChangeDetails=" + latestChangeDetails +
                       ", confidentialityGovernanceClassification=" + confidentialityGovernanceClassification +
                       ", confidenceGovernanceClassification=" + confidenceGovernanceClassification +
                       ", criticalityGovernanceClassification=" + criticalityGovernanceClassification +
                       ", retentionGovernanceClassification=" + retentionGovernanceClassification +
                       ", headerVersion=" + getHeaderVersion() +
                       '}';
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
        if (this == objectToCompare)
        {
            return true;
        }
        if (objectToCompare == null || getClass() != objectToCompare.getClass())
        {
            return false;
        }
        if (!super.equals(objectToCompare))
        {
            return false;
        }
        GovernedReferenceable that = (GovernedReferenceable) objectToCompare;
        return  Objects.equals(meanings, that.meanings) &&
                Objects.equals(securityTags, that.securityTags) &&
                Objects.equals(searchKeywords, that.searchKeywords) &&
                Objects.equals(latestChange, that.latestChange) &&
                Objects.equals(latestChangeDetails, that.latestChangeDetails) &&
                Objects.equals(confidentialityGovernanceClassification, that.confidentialityGovernanceClassification) &&
                Objects.equals(confidenceGovernanceClassification, that.confidenceGovernanceClassification) &&
                Objects.equals(criticalityGovernanceClassification, that.criticalityGovernanceClassification) &&
                Objects.equals(retentionGovernanceClassification, that.retentionGovernanceClassification);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), meanings, securityTags, searchKeywords, latestChange,
                            latestChangeDetails, confidentialityGovernanceClassification, confidenceGovernanceClassification, criticalityGovernanceClassification, retentionGovernanceClassification);
    }
}