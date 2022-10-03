/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.metadatasecurity.properties;

import java.io.Serializable;
import java.util.*;

/**
 * Referenceable is a set of properties that describes an open metadata referencable object.  A referenceable is an open metadata
 * type that has enough significance to be awarded a unique qualified name.
 * This bean is designed to convey the important properties needed to make a security decision relating to this object
 */
public class Referenceable implements Serializable
{
    private static final long     serialVersionUID = 1L;

    private String              typeGUID             = null;
    private String              typeName             = null;
    private ReferenceableStatus status               = null;
    private String              guid                 = null;
    private String              qualifiedName        = null;
    private Map<String, String> additionalProperties = null;
    private Map<String, Object> extendedProperties   = null;

    private List<String>                            securityLabels     = null;
    private Map<String, Object>                     securityProperties = null;
    private ConfidentialityGovernanceClassification confidentiality    = null;
    private ConfidenceGovernanceClassification      confidence         = null;
    private CriticalityGovernanceClassification     criticality        = null;
    private ImpactGovernanceClassification          impact             = null;
    private RetentionGovernanceClassification       retention          = null;



    /**
     * Default constructor
     */
    public Referenceable()
    {
        super();
    }


    /**
     * Copy/clone constructor.  Retrieves values from the supplied template
     *
     * @param template element to copy
     */
    public Referenceable(Referenceable template)
    {
        if (template != null)
        {
            typeGUID             = template.getTypeGUID();
            typeName             = template.getTypeName();
            status               = template.getStatus();
            guid                 = template.getGUID();
            qualifiedName        = template.getQualifiedName();
            additionalProperties = template.getAdditionalProperties();
            securityLabels       = template.getSecurityLabels();
            securityProperties   = template.getSecurityProperties();
            confidentiality      = template.getConfidentiality();
            confidence           = template.getConfidence();
            criticality          = template.getCriticality();
            impact               = template.getImpact();
            retention            = template.getRetention();
            extendedProperties   = template.getExtendedProperties();

        }
    }


    /**
     * Return the unique identifier for this object's type.
     *
     * @return string guid
     */
    public String getTypeGUID()
    {
        return typeGUID;
    }


    /**
     * Set up the unique identifier for this object's type.
     *
     * @param typeGUID string guid
     */
    public void setTypeGUID(String typeGUID)
    {
        this.typeGUID = typeGUID;
    }


    /**
     * Return the unique name of this object's type.
     *
     * @return string name
     */
    public String getTypeName()
    {
        return typeName;
    }


    /**
     * Return the status for the bean.
     *
     * @return enum value
     */
    public ReferenceableStatus getStatus()
    {
        return status;
    }


    /**
     * Set up the status for the bean.
     *
     * @param status enum value
     */
    public void setStatus(ReferenceableStatus status)
    {
        this.status = status;
    }


    /**
     * Return the unique identifier for this object.
     *
     * @return string guid
     */
    public String getGUID()
    {
        return guid;
    }


    /**
     * Set up the unique identifier for this object.
     *
     * @param guid string guid
     */
    public void setGUID(String guid)
    {
        this.guid = guid;
    }


    /**
     * Set up the unique name of this object's type.
     *
     * @param typeName string name
     */
    public void setTypeName(String typeName)
    {
        this.typeName = typeName;
    }


    /**
     * Set up the fully qualified name.
     *
     * @param qualifiedName String name
     */
    public void setQualifiedName(String qualifiedName)
    {
        this.qualifiedName = qualifiedName;
    }


    /**
     * Returns the stored qualified name property for the metadata entity.
     * If no qualified name is available then the empty string is returned.
     *
     * @return qualifiedName
     */
    public String getQualifiedName()
    {
        return qualifiedName;
    }


    /**
     * Set up additional properties.
     *
     * @param additionalProperties Additional properties object
     */
    public void setAdditionalProperties(Map<String, String> additionalProperties)
    {
        this.additionalProperties = additionalProperties;
    }


    /**
     * Return a copy of the additional properties.  Null means no additional properties are available.
     *
     * @return AdditionalProperties
     */
    public Map<String, String> getAdditionalProperties()
    {
        if (additionalProperties == null)
        {
            return null;
        }
        else if (additionalProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(additionalProperties);
        }
    }


    /**
     * Return the properties that have been defined for a subtype of this object that are not supported explicitly
     * by this bean.
     *
     * @return property map
     */
    public Map<String, Object> getExtendedProperties()
    {
        if (extendedProperties == null)
        {
            return null;
        }
        else if (extendedProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(extendedProperties);
        }
    }


    /**
     * Set up the properties that have been defined for a subtype of this object that are not supported explicitly
     * by this bean.
     *
     * @param extendedProperties property map
     */
    public void setExtendedProperties(Map<String, Object> extendedProperties)
    {
        this.extendedProperties = extendedProperties;
    }


    /**
     * Return the list of security labels attached to the element.
     *
     * @return list of label strings
     */
    public List<String> getSecurityLabels()
    {
        if (securityLabels == null)
        {
            return null;
        }
        else if (securityLabels.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(securityLabels);
        }
    }


    /**
     * Set up the list of security labels for the element.
     *
     * @param securityLabels list of label strings
     */
    public void setSecurityLabels(List<String> securityLabels)
    {
        this.securityLabels = securityLabels;
    }


    /**
     * Return the security properties associated with the element.  These are name-value pairs.
     *
     * @return map of properties
     */
    public Map<String, Object> getSecurityProperties()
    {
        if (securityProperties == null)
        {
            return null;
        }
        else if (securityProperties.isEmpty())
        {
            return null;
        }
        else
        {
            return new HashMap<>(securityProperties);
        }
    }


    /**
     * Set up the security properties associated with the element.  These are name-value pairs.
     *
     * @param securityProperties map of properties
     */
    public void setSecurityProperties(Map<String, Object> securityProperties)
    {
        this.securityProperties = securityProperties;
    }


    /**
     * Return the classification that defines how confidential the contents of this referenceable are.
     *
     * @return confidentiality classification properties
     */
    public ConfidentialityGovernanceClassification getConfidentiality()
    {
        return confidentiality;
    }


    /**
     * Set up the classification that defines how confidential the contents of this referenceable are.
     *
     * @param confidentiality confidentiality classification properties
     */
    public void setConfidentiality(ConfidentialityGovernanceClassification confidentiality)
    {
        this.confidentiality = confidentiality;
    }


    /**
     * Return the classification that defines how confident a user should be in the quality of the contents of this referenceable are.
     *
     * @return confidence classification properties
     */
    public ConfidenceGovernanceClassification getConfidence()
    {
        return confidence;
    }


    /**
     * Set up the classification that defines how confident a user should be in the quality of the contents of this referenceable are.
     *
     * @param confidence confidence classification properties
     */
    public void setConfidence(ConfidenceGovernanceClassification confidence)
    {
        this.confidence = confidence;
    }


    /**
     * Return the classification that defines how critical this referenceable is to the business.
     *
     * @return criticality classification properties
     */
    public CriticalityGovernanceClassification getCriticality()
    {
        return criticality;
    }


    /**
     * Set up the classification that defines how critical this referenceable is to the business.
     *
     * @param criticality criticality classification properties
     */
    public void setCriticality(CriticalityGovernanceClassification criticality)
    {
        this.criticality = criticality;
    }


    /**
     * Return the classification that defines the impact of this referenceable to the business.
     *
     * @return criticality classification properties
     */
    public ImpactGovernanceClassification getImpact()
    {
        return impact;
    }


    /**
     * Set up the classification that defines the impact of this referenceable to the business.
     *
     * @param impact impact classification properties
     */
    public void setImpact(ImpactGovernanceClassification impact)
    {
        this.impact = impact;
    }


    /**
     * Return the classification that defines when this referenceable will be archived and then permanently deleted.
     *
     * @return retention classification properties
     */
    public RetentionGovernanceClassification getRetention()
    {
        return retention;
    }


    /**
     * Set up the classification that defines when this referenceable will be archived and then permanently deleted.
     *
     * @param retention retention classification properties
     */
    public void setRetention(RetentionGovernanceClassification retention)
    {
        this.retention = retention;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "Referenceable{" +
                "typeGUID='" + typeGUID + '\'' +
                ", typeName='" + typeName + '\'' +
                ", guid='" + guid + '\'' +
                ", qualifiedName='" + qualifiedName + '\'' +
                ", additionalProperties=" + additionalProperties +
                ", extendedProperties=" + extendedProperties +
                ", securityLabels=" + securityLabels +
                ", securityProperties=" + securityProperties +
                ", confidentiality=" + confidentiality +
                ", confidence=" + confidence +
                ", criticality=" + criticality +
                ", retention=" + retention +
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
        Referenceable that = (Referenceable) objectToCompare;
        return Objects.equals(typeGUID, that.typeGUID) &&
                Objects.equals(typeName, that.typeName) &&
                Objects.equals(guid, that.guid) &&
                Objects.equals(qualifiedName, that.qualifiedName) &&
                Objects.equals(additionalProperties, that.additionalProperties) &&
                Objects.equals(extendedProperties, that.extendedProperties) &&
                Objects.equals(securityLabels, that.securityLabels) &&
                Objects.equals(securityProperties, that.securityProperties) &&
                Objects.equals(confidentiality, that.confidentiality) &&
                Objects.equals(confidence, that.confidence) &&
                Objects.equals(criticality, that.criticality) &&
                Objects.equals(retention, that.retention);
    }


    /**
     * Return hash code based on properties.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(typeGUID, typeName, guid, qualifiedName, additionalProperties, extendedProperties, securityLabels, securityProperties, confidentiality, confidence, criticality, retention);
    }
}
