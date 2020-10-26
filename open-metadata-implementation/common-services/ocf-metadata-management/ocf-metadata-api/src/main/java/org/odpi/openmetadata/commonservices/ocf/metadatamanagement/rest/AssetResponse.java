/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.commonservices.ocf.metadatamanagement.rest;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.commonservices.ocf.metadatamanagement.properties.LastAttachment;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.Asset;
import org.odpi.openmetadata.frameworks.connectors.properties.beans.SchemaType;

import java.util.Arrays;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;


/**
 * AssetResponse is the response structure used on the OMAS REST API calls that return a
 * asset bean object as a response.  It also returns counts of the number of connected
 * elements for the asset.  This can be implemented cheaply as a single pass through
 * the relationships linked to the asset and, assuming that the AssetUniverse structure
 * is sparsely populated, and most callers only assess a specific subset of the information,
 * it reduces the number of server calls needed to populate the AssetUniverse.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class AssetResponse extends OCFOMASAPIResponse
{
    private static final long    serialVersionUID = 1L;

    private Asset          asset                      = null;
    private int            certificationCount         = 0;
    private int            commentCount               = 0;
    private int            connectionCount            = 0;
    private int            externalIdentifierCount    = 0;
    private int            externalReferencesCount    = 0;
    private int            informalTagCount           = 0;
    private int            licenseCount               = 0;
    private int            likeCount                  = 0;
    private int            keywordCount               = 0;
    private int            knownLocationsCount        = 0;
    private int            noteLogsCount              = 0;
    private int            ratingsCount               = 0;
    private int            relatedAssetCount          = 0;
    private int            relatedMediaReferenceCount = 0;
    private SchemaType     schemaType                 = null;
    private LastAttachment lastAttachment             = null;


    /**
     * Default constructor
     */
    public AssetResponse()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy
     */
    public AssetResponse(AssetResponse template)
    {
        super(template);

        if (template != null)
        {
            this.asset                      = template.getAsset();
            this.certificationCount         = template.getCertificationCount();
            this.commentCount               = template.getCommentCount();
            this.connectionCount            = template.getConnectionCount();
            this.externalIdentifierCount    = template.getExternalIdentifierCount();
            this.externalReferencesCount    = template.getExternalReferencesCount();
            this.informalTagCount           = template.getInformalTagCount();
            this.licenseCount               = template.getLicenseCount();
            this.likeCount                  = template.getLikeCount();
            this.keywordCount               = template.getKeywordCount();
            this.knownLocationsCount        = template.getKnownLocationsCount();
            this.noteLogsCount              = template.getNoteLogsCount();
            this.ratingsCount               = template.getRatingsCount();
            this.relatedAssetCount          = template.getRelatedAssetCount();
            this.relatedMediaReferenceCount = template.getRelatedMediaReferenceCount();
            this.schemaType                 = template.getSchemaType();
            this.lastAttachment             = template.getLastAttachment();
        }
    }


    /**
     * Return the asset result.
     *
     * @return unique identifier
     */
    public Asset getAsset()
    {
        return asset;
    }


    /**
     * Set up the asset result.
     *
     * @param asset  unique identifier
     */
    public void setAsset(Asset asset)
    {
        this.asset = asset;
    }


    /**
     * Return the count of attached certification.
     *
     * @return count
     */
    public int getCertificationCount()
    {
        return certificationCount;
    }


    /**
     * Set up the count of attached certifications for the asset.
     *
     * @param certificationCount count
     */
    public void setCertificationCount(int certificationCount)
    {
        this.certificationCount = certificationCount;
    }


    /**
     * Return the count of attached comments.
     *
     * @return count
     */
    public int getCommentCount()
    {
        return commentCount;
    }


    /**
     * Set up the count of attached comments.
     *
     * @param commentCount count
     */
    public void setCommentCount(int commentCount)
    {
        this.commentCount = commentCount;
    }


    /**
     * Return the count of connections for the asset.
     *
     * @return count
     */
    public int getConnectionCount()
    {
        return connectionCount;
    }


    /**
     * Set up the count of connections.
     *
     * @param connectionCount count
     */
    public void setConnectionCount(int connectionCount)
    {
        this.connectionCount = connectionCount;
    }


    /**
     * Return the count of external identifiers for this asset.
     *
     * @return count
     */
    public int getExternalIdentifierCount()
    {
        return externalIdentifierCount;
    }


    /**
     * Set up the count of external identifiers for this asset.
     *
     * @param externalIdentifierCount count
     */
    public void setExternalIdentifierCount(int externalIdentifierCount)
    {
        this.externalIdentifierCount = externalIdentifierCount;
    }


    /**
     * Return the count of attached external references.
     *
     * @return count
     */
    public int getExternalReferencesCount()
    {
        return externalReferencesCount;
    }


    /**
     * Set up the count of attached external references.
     *
     * @param externalReferencesCount count
     */
    public void setExternalReferencesCount(int externalReferencesCount)
    {
        this.externalReferencesCount = externalReferencesCount;
    }


    /**
     * Return the count of attached informal tags.
     *
     * @return count
     */
    public int getInformalTagCount()
    {
        return informalTagCount;
    }


    /**
     * Set up the count of attached informal tags.
     *
     * @param informalTagCount count
     */
    public void setInformalTagCount(int informalTagCount)
    {
        this.informalTagCount = informalTagCount;
    }


    /**
     * Return the count of license for this asset.
     *
     * @return count
     */
    public int getLicenseCount()
    {
        return licenseCount;
    }


    /**
     * Set up the count of licenses for this asset.
     *
     * @param licenseCount count
     */
    public void setLicenseCount(int licenseCount)
    {
        this.licenseCount = licenseCount;
    }


    /**
     * Return the number of likes for the asset.
     *
     * @return count
     */
    public int getLikeCount()
    {
        return likeCount;
    }


    /**
     * Set up the count of likes for the asset.
     *
     * @param likeCount count
     */
    public void setLikeCount(int likeCount)
    {
        this.likeCount = likeCount;
    }


    /**
     * Return the count of keywords for the asset.
     *
     * @return count
     */
    public int getKeywordCount()
    {
        return keywordCount;
    }


    /**
     * Set up the count of keywords for the asset.
     *
     * @param keywordCount count
     */
    public void setKeywordCount(int keywordCount)
    {
        this.keywordCount = keywordCount;
    }

    /**
     * Return the count of known locations.
     *
     * @return count
     */
    public int getKnownLocationsCount()
    {
        return knownLocationsCount;
    }


    /**
     * Set up the count of known locations.
     *
     * @param knownLocationsCount count
     */
    public void setKnownLocationsCount(int knownLocationsCount)
    {
        this.knownLocationsCount = knownLocationsCount;
    }


    /**
     * Return the count of attached note logs.
     *
     * @return count
     */
    public int getNoteLogsCount()
    {
        return noteLogsCount;
    }


    /**
     * Set up the count of attached note logs.
     *
     * @param noteLogsCount count
     */
    public void setNoteLogsCount(int noteLogsCount)
    {
        this.noteLogsCount = noteLogsCount;
    }


    /**
     * Return the count of attached ratings.
     *
     * @return count
     */
    public int getRatingsCount()
    {
        return ratingsCount;
    }


    /**
     * Set up the count of attach ratings.
     *
     * @param ratingsCount count
     */
    public void setRatingsCount(int ratingsCount)
    {
        this.ratingsCount = ratingsCount;
    }


    /**
     * Return the count of related assets.
     *
     * @return count
     */
    public int getRelatedAssetCount()
    {
        return relatedAssetCount;
    }


    /**
     * Set up the count of related assets.
     *
     * @param relatedAssetCount count
     */
    public void setRelatedAssetCount(int relatedAssetCount)
    {
        this.relatedAssetCount = relatedAssetCount;
    }


    /**
     * Return the count of related media references.
     *
     * @return count
     */
    public int getRelatedMediaReferenceCount()
    {
        return relatedMediaReferenceCount;
    }


    /**
     * Set up the count of related media references.
     *
     * @param relatedMediaReferenceCount count
     */
    public void setRelatedMediaReferenceCount(int relatedMediaReferenceCount)
    {
        this.relatedMediaReferenceCount = relatedMediaReferenceCount;
    }


    /**
     * Is there an attached schema?
     *
     * @return schema type bean
     */
    public SchemaType getSchemaType()
    {
        return schemaType;
    }


    /**
     * Set up whether there is an attached schema.
     *
     * @param schemaType schema type bean
     */
    public void setSchemaType(SchemaType schemaType)
    {
        this.schemaType = schemaType;
    }


    /**
     * Return the record of the last attachment to be made to the asset.
     *
     * @return last attachment details
     */
    public LastAttachment getLastAttachment()
    {
        return lastAttachment;
    }


    /**
     * Set up the record of the last attachment to be made to the asset.
     *
     * @param lastAttachment last attachment details
     */
    public void setLastAttachment(LastAttachment lastAttachment)
    {
        this.lastAttachment = lastAttachment;
    }


    /**
     * JSON-style toString
     *
     * @return return string containing the property names and values
     */
    @Override
    public String toString()
    {
        return "AssetResponse{" +
                "asset=" + asset +
                ", certificationCount=" + certificationCount +
                ", commentCount=" + commentCount +
                ", connectionCount=" + connectionCount +
                ", externalIdentifierCount=" + externalIdentifierCount +
                ", externalReferencesCount=" + externalReferencesCount +
                ", informalTagCount=" + informalTagCount +
                ", licenseCount=" + licenseCount +
                ", likeCount=" + likeCount +
                ", knownLocationsCount=" + knownLocationsCount +
                ", noteLogsCount=" + noteLogsCount +
                ", ratingsCount=" + ratingsCount +
                ", relatedAssetCount=" + relatedAssetCount +
                ", relatedMediaReferenceCount=" + relatedMediaReferenceCount +
                ", schemaType=" + schemaType +
                ", lastAttachment=" + lastAttachment +
                ", exceptionClassName='" + getExceptionClassName() + '\'' +
                ", exceptionCausedBy='" + getExceptionCausedBy() + '\'' +
                ", actionDescription='" + getActionDescription() + '\'' +
                ", relatedHTTPCode=" + getRelatedHTTPCode() +
                ", exceptionErrorMessage='" + getExceptionErrorMessage() + '\'' +
                ", exceptionErrorMessageId='" + getExceptionErrorMessageId() + '\'' +
                ", exceptionErrorMessageParameters=" + Arrays.toString(getExceptionErrorMessageParameters()) +
                ", exceptionSystemAction='" + getExceptionSystemAction() + '\'' +
                ", exceptionUserAction='" + getExceptionUserAction() + '\'' +
                ", exceptionProperties=" + getExceptionProperties() +
                '}';
    }

    /**
     * Return comparison result based on the content of the properties.
     *
     * @param objectToCompare test object
     * @return result of comparison
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
        AssetResponse that = (AssetResponse) objectToCompare;
        return getCertificationCount() == that.getCertificationCount() &&
                getCommentCount() == that.getCommentCount() &&
                getConnectionCount() == that.getConnectionCount() &&
                getExternalIdentifierCount() == that.getExternalIdentifierCount() &&
                getExternalReferencesCount() == that.getExternalReferencesCount() &&
                getInformalTagCount() == that.getInformalTagCount() &&
                getLicenseCount() == that.getLicenseCount() &&
                getLikeCount() == that.getLikeCount() &&
                getKnownLocationsCount() == that.getKnownLocationsCount() &&
                getNoteLogsCount() == that.getNoteLogsCount() &&
                getRatingsCount() == that.getRatingsCount() &&
                getRelatedAssetCount() == that.getRelatedAssetCount() &&
                getRelatedMediaReferenceCount() == that.getRelatedMediaReferenceCount() &&
                getSchemaType() == that.getSchemaType() &&
                getLastAttachment() == that.getLastAttachment() &&
                Objects.equals(getAsset(), that.getAsset());
    }


    /**
     * Return hash code for this object
     *
     * @return int hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), getAsset(), getCertificationCount(),
                            getCommentCount(),
                            getConnectionCount(), getExternalIdentifierCount(), getExternalReferencesCount(),
                            getInformalTagCount(), getLicenseCount(), getLikeCount(), getKnownLocationsCount(),
                            getNoteLogsCount(), getRatingsCount(), getRelatedAssetCount(),
                            getRelatedMediaReferenceCount(), getSchemaType(), getLastAttachment());
    }
}
