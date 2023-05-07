/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */

package org.odpi.openmetadata.accessservices.assetmanager.properties;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.Date;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * RetentionClassificationProperties defines the retention requirements for related data items.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class RetentionClassificationProperties extends GovernanceClassificationBase
{
    private int     retentionBasis = 0;
    private String  associatedGUID = null;
    private Date    archiveAfter   = null;
    private Date    deleteAfter    = null;


    /**
     * Default constructor
     */
    public RetentionClassificationProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor.
     *
     * @param template object to copy
     */
    public RetentionClassificationProperties(RetentionClassificationProperties template)
    {
        super(template);

        if (template != null)
        {
            retentionBasis = template.getRetentionBasis();
            associatedGUID = template.getAssociatedGUID();
            archiveAfter   = template.getArchiveAfter();
            deleteAfter    = template.getDeleteAfter();
        }
    }


    /**
     * Return a description of the factor used to set the archiveAfter and deleteAfter dates.
     *
     * @return int
     */
    public int getRetentionBasis()
    {
        return retentionBasis;
    }


    /**
     * Set up a description of the factor used to set the archiveAfter and deleteAfter dates.
     *
     * @param retentionBasis int
     */
    public void setRetentionBasis(int retentionBasis)
    {
        this.retentionBasis = retentionBasis;
    }


    /**
     * Return the unique identifier of the element used in deciding the archiveAfter and deleteAfter dates.
     * For example if the retention basis is determined by the project lifetime, this identifier is the
     * identifier of the associated project.
     *
     * @return string guid
     */
    public String getAssociatedGUID()
    {
        return associatedGUID;
    }


    /**
     * Set up the unique identifier of the element used in deciding the archiveAfter and deleteAfter dates.
     * For example if the retention basis is determined by the project lifetime, this identifier is the
     * identifier of the associated project.
     *
     * @param associatedGUID string guid
     */
    public void setAssociatedGUID(String associatedGUID)
    {
        this.associatedGUID = associatedGUID;
    }


    /**
     * Return the date that this asset will be archived.
     *
     * @return date/timestamp
     */
    public Date getArchiveAfter()
    {
        return archiveAfter;
    }


    /**
     * Set up the date that this asset will be archived.
     *
     * @param archiveAfter date/timestamp
     */
    public void setArchiveAfter(Date archiveAfter)
    {
        this.archiveAfter = archiveAfter;
    }


    /**
     * Return the date that this asset will be permanently deleted.
     *
     * @return date/timestamp
     */
    public Date getDeleteAfter()
    {
        return deleteAfter;
    }


    /**
     * Set up the date that this asset will be permanently deleted.
     *
     * @param deleteAfter date/timestamp
     */
    public void setDeleteAfter(Date deleteAfter)
    {
        this.deleteAfter = deleteAfter;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "RetentionClassificationProperties{" +
                "retentionBasis=" + retentionBasis +
                ", associatedGUID='" + associatedGUID + '\'' +
                ", archiveAfter=" + archiveAfter +
                ", deleteAfter=" + deleteAfter +
                ", status=" + getStatus() +
                ", confidence=" + getConfidence() +
                ", steward='" + getSteward() + '\'' +
                ", source='" + getSource() + '\'' +
                ", notes='" + getNotes() +
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
        RetentionClassificationProperties that = (RetentionClassificationProperties) objectToCompare;
        return retentionBasis == that.retentionBasis &&
                Objects.equals(associatedGUID, that.associatedGUID) &&
                Objects.equals(archiveAfter, that.archiveAfter) &&
                Objects.equals(deleteAfter, that.deleteAfter);
    }


    /**
     * Return code value representing the contents of this object.
     *
     * @return int
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(super.hashCode(), retentionBasis, associatedGUID, archiveAfter, deleteAfter);
    }
}
