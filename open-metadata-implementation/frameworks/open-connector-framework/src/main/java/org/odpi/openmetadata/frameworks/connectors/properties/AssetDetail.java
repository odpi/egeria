/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.connectors.properties;

import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;

import java.util.Objects;

/**
 * AssetDetail extends AssetSummary to provide all the properties related to this asset.  It includes:
 * <ul>
 *     <li>AssetProperties - properties unique to the particular type of asset including any vendor-specific facets.</li>
 *     <li>ExternalIdentifiers - list of identifiers for this asset that are used in other systems.</li>
 *     <li>RelatedMediaReferences - list of links to external media (images, audio, video) about this asset.</li>
 *     <li>NoteLogs - list of NoteLogs for this asset, often providing more detail on how to use the asset and
 *                   its current status.</li>
 *     <li>ExternalReferences - list of links to additional information about this asset.</li>
 *     <li>Connections - list of connections defined to access this asset.</li>
 *     <li>Licenses - list of licenses associated with the asset.</li>
 *     <li>Certifications - list of certifications that have been awarded to this asset.</li>
 * </ul>
 */
public class AssetDetail extends AssetSummary
{
    private static final long     serialVersionUID = 1L;

    protected ExternalIdentifiers    externalIdentifiers    = null;
    protected RelatedMediaReferences relatedMediaReferences = null;
    protected NoteLogs               noteLogs               = null;
    protected ExternalReferences     externalReferences     = null;
    protected Connections            connections            = null;
    protected Licenses               licenses               = null;
    protected Certifications         certifications         = null;


    /**
     * Default constructor only for subclasses
     */
    protected AssetDetail()
    {
        super();
    }


    /**
     * Typical constructor initialize superclasses
     *
     * @param assetBean details of this asset
     * @param externalIdentifiers ExternalIdentifiers iterator
     * @param relatedMediaReferences RelatedMediaReferences iterator
     * @param noteLogs NoteLogs iterator
     * @param externalReferences ExternalReferences iterator
     * @param connections Iterator of connections attached to the asset
     * @param licenses Iterator of licenses for this asset
     * @param certifications Iterator of certifications for this asset
     */
    public AssetDetail(Asset                  assetBean,
                       ExternalIdentifiers    externalIdentifiers,
                       RelatedMediaReferences relatedMediaReferences,
                       NoteLogs               noteLogs,
                       ExternalReferences     externalReferences,
                       Connections            connections,
                       Licenses               licenses,
                       Certifications         certifications)
    {
        super(assetBean);

        this.externalIdentifiers = externalIdentifiers;
        this.relatedMediaReferences = relatedMediaReferences;
        this.noteLogs = noteLogs;
        this.externalReferences = externalReferences;
        this.connections = connections;
        this.licenses = licenses;
        this.certifications = certifications;
    }


    /**
     * Copy/clone constructor.  Note, this is a deep copy
     *
     * @param templateAssetDetail template to copy
     */
    public AssetDetail(AssetDetail templateAssetDetail)
    {
        /*
         * Initialize superclass
         */
        super(templateAssetDetail);

        if (templateAssetDetail != null)
        {
            ExternalIdentifiers    templateExternalIdentifiers    = templateAssetDetail.getExternalIdentifiers();
            RelatedMediaReferences templateRelatedMediaReferences = templateAssetDetail.getRelatedMediaReferences();
            NoteLogs               templateNoteLogs               = templateAssetDetail.getNoteLogs();
            ExternalReferences     templateExternalReferences     = templateAssetDetail.getExternalReferences();
            Connections            templateConnections            = templateAssetDetail.getConnections();
            Licenses               templateLicenses               = templateAssetDetail.getLicenses();
            Certifications         templateCertifications         = templateAssetDetail.getCertifications();


            if (templateExternalIdentifiers != null)
            {
                externalIdentifiers = templateExternalIdentifiers.cloneIterator();
            }
            if (templateRelatedMediaReferences != null)
            {
                relatedMediaReferences = templateRelatedMediaReferences.cloneIterator();
            }
            if (templateNoteLogs != null)
            {
                noteLogs = templateNoteLogs.cloneIterator();
            }
            if (templateExternalReferences != null)
            {
                externalReferences = templateExternalReferences.cloneIterator();
            }
            if (templateConnections != null)
            {
                connections = templateConnections.cloneIterator();
            }
            if (templateLicenses != null)
            {
                licenses = templateLicenses.cloneIterator();
            }
            if (templateCertifications != null)
            {
                certifications = templateCertifications.cloneIterator();
            }
        }
    }


    /**
     * Return an  list of the external identifiers for this asset (or null if none).
     *
     * @return ExternalIdentifiers  list
     */
    public ExternalIdentifiers getExternalIdentifiers()
    {
        if (externalIdentifiers == null)
        {
            return null;
        }
        else
        {
            return externalIdentifiers.cloneIterator();
        }
    }


    /**
     * Return an  list of references to the related media associated with this asset.
     *
     * @return RelatedMediaReferences  list
     */
    public RelatedMediaReferences getRelatedMediaReferences()
    {
        if (relatedMediaReferences == null)
        {
            return null;
        }
        else
        {
            return relatedMediaReferences.cloneIterator();
        }
    }


    /**
     * Return an  list of NoteLogs linked to this asset.
     *
     * @return Notelogs iterator
     */
    public NoteLogs getNoteLogs()
    {
        if (noteLogs == null)
        {
            return null;
        }
        else
        {
            return noteLogs.cloneIterator();
        }
    }


    /**
     * Return the  list of external references associated with this asset.
     *
     * @return ExternalReferences iterator
     */
    public ExternalReferences getExternalReferences()
    {
        if (externalReferences == null)
        {
            return null;
        }
        else
        {
            return externalReferences.cloneIterator();
        }
    }


    /**
     * Return a list of the connections defined for this asset.
     *
     * @return Connections  list
     */
    public Connections getConnections()
    {
        if (connections == null)
        {
            return null;
        }
        else
        {
            return connections.cloneIterator();
        }
    }


    /**
     * Return the list of licenses associated with the asset.
     *
     * @return Licenses
     */
    public Licenses getLicenses()
    {
        if (licenses == null)
        {
            return null;
        }
        else
        {
            return licenses.cloneIterator();
        }
    }


    /**
     * Return the list of certifications awarded to the asset.
     *
     * @return Certifications list of certifications
     */
    public Certifications getCertifications()
    {
        if (certifications == null)
        {
            return null;
        }
        else
        {
            return certifications.cloneIterator();
        }
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "AssetDetail{" +
                       "externalIdentifiers=" + externalIdentifiers +
                       ", relatedMediaReferences=" + relatedMediaReferences +
                       ", noteLogs=" + noteLogs +
                       ", externalReferences=" + externalReferences +
                       ", connections=" + connections +
                       ", licenses=" + licenses +
                       ", certifications=" + certifications +
                       ", displayName='" + getDisplayName() + '\'' +
                       ", shortDescription='" + getShortDescription() + '\'' +
                       ", description='" + getDescription() + '\'' +
                       ", owner='" + getOwner() + '\'' +
                       ", ownerTypeName='" + getOwnerTypeName() + '\'' +
                       ", ownerPropertyName='" + getOwnerPropertyName() + '\'' +
                       ", ownerType=" + getOwnerType() +
                       ", zoneMembership=" + getZoneMembership() +
                       ", assetOrigin=" + getAssetOrigin() +
                       ", referenceData=" + isReferenceData() +
                       ", URL='" + getURL() + '\'' +
                       ", extendedProperties=" + getExtendedProperties() +
                       ", status=" + getStatus() +
                       ", type=" + getType() +
                       ", origin=" + getOrigin() +
                       ", versions=" + getVersions() +
                       ", GUID='" + getGUID() + '\'' +
                       ", classifications=" + getClassifications() +
                       ", meanings=" + getMeanings() +
                       ", securityTags=" + getSecurityTags() +
                       ", searchKeywords=" + getSearchKeywords() +
                       ", latestChange='" + getLatestChange() + '\'' +
                       ", latestChangeDetails=" + getLatestChangeDetails() +
                       ", confidentialityGovernanceClassification=" + getConfidentialityGovernanceClassification() +
                       ", confidenceGovernanceClassification=" + getConfidenceGovernanceClassification() +
                       ", criticalityGovernanceClassification=" + getCriticalityGovernanceClassification() +
                       ", retentionGovernanceClassification=" + getRetentionGovernanceClassification() +
                       ", headerVersion=" + getHeaderVersion() +
                       ", qualifiedName='" + getQualifiedName() + '\'' +
                       ", additionalProperties=" + getAdditionalProperties() +
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
        AssetDetail that = (AssetDetail) objectToCompare;
        return Objects.equals(externalIdentifiers, that.externalIdentifiers) &&
                       Objects.equals(relatedMediaReferences, that.relatedMediaReferences) &&
                       Objects.equals(noteLogs, that.noteLogs) &&
                       Objects.equals(externalReferences, that.externalReferences) &&
                       Objects.equals(connections, that.connections) &&
                       Objects.equals(licenses, that.licenses) &&
                       Objects.equals(certifications, that.certifications);
    }


    /**
     * Hash of properties
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), externalIdentifiers, relatedMediaReferences, noteLogs, externalReferences, connections, licenses,
                            certifications);
    }
}
