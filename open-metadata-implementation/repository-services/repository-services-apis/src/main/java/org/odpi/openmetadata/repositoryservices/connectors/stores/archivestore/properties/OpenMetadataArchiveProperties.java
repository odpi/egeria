/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.archivestore.properties;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * OpenMetadataArchiveProperties defines the properties of an open metadata archive.  This includes the following
 * properties:
 * <ul>
 *     <li>
 *         Unique identifier (GUID) of the archive.  This is used as the metadata collection id for the
 *         elements in the archive.
 *     </li>
 *     <li>
 *         Archive name.  This is used as the name of the metadata collection for the elements in the archive.
 *     </li>
 *     <li>
 *         Archive description.  This helps people choose which archive they want.
 *     </li>
 *     <li>
 *         Archive Type (CONTENT_PACK or METADATA_EXPORT).  A content pack is a reusable collection of
 *         metadata elements.  A metadata export is an extraction of metadata from a cohort for backup/restore
 *         or to create metadata to send to a disconnected cohort.
 *     </li>
 *     <li>
 *         Archive Version.  A descriptive name for the version of this archive.
 *     </li>
 *     <li>
 *         Originator name.  This becomes the creation user id in the elements if it is not
 *         already specified.
 *     </li>
 *     <li>
 *         Originator organization.  Name of the organization that created the archive.
 *     </li>
 *     <li>
 *         Originator license.  The license associated with the metadata content. Null means no restrictions.
 *     </li>
 *     <li>
 *         Creation date is the date that the archive was created.  This will become the creationTime in the elements
 *         if not already specified.
 *     </li>
 *     <li>
 *         GUIDs for archives that this archive depends on.  This helps the open metadata repository services
 *         load the archives in the right order.  Null here means no dependencies.
 *     </li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public class OpenMetadataArchiveProperties extends OpenMetadataArchiveElementHeader
{
    private static final long    serialVersionUID = 1L;

    private String                  archiveGUID            = null;
    private String                  archiveName            = null;
    private String                  archiveDescription     = null;
    private OpenMetadataArchiveType archiveType            = null;
    private String                  archiveVersion         = null;
    private String                  originatorName         = null;
    private String                  originatorOrganization = null;
    private String                  originatorLicense      = null;
    private Date                    creationDate           = null;
    private List<String>            dependsOnArchives      = null;


    /**
     * Default constructor that relies on initialization of variables in their declaration.
     */
    public OpenMetadataArchiveProperties()
    {
        super();
    }


    /**
     * Copy/clone constructor
     *
     * @param template object to copy.
     */
    public OpenMetadataArchiveProperties(OpenMetadataArchiveProperties  template)
    {
        super(template);

        if (template != null)
        {
            archiveGUID = template.getArchiveGUID();
            archiveName = template.getArchiveName();
            archiveDescription = template.getArchiveDescription();
            archiveType = template.getArchiveType();
            originatorName = template.getOriginatorName();
            originatorOrganization = template.getOriginatorOrganization();
            originatorLicense = template.getOriginatorLicense();
            creationDate = template.getCreationDate();
            dependsOnArchives = template.getDependsOnArchives();
        }
    }


    /**
     * Return the unique identifier for this archive.
     *
     * @return String guid
     */
    public String getArchiveGUID()
    {
        return archiveGUID;
    }


    /**
     * Set up the unique identifier for this open metadata archive.
     *
     * @param archiveGUID  String guid
     */
    public void setArchiveGUID(String archiveGUID)
    {
        this.archiveGUID = archiveGUID;
    }


    /**
     * Return the descriptive name for this open metadata archive.
     *
     * @return String name
     */
    public String getArchiveName()
    {
        return archiveName;
    }


    /**
     * Set up the descriptive name for this open metadata archive.
     *
     * @param archiveName String name
     */
    public void setArchiveName(String archiveName)
    {
        this.archiveName = archiveName;
    }


    /**
     * Return the description for this open metadata archive.
     *
     * @return String description
     */
    public String getArchiveDescription()
    {
        return archiveDescription;
    }


    /**
     * Set up the description for this open metadata archive.
     *
     * @param archiveDescription String description
     */
    public void setArchiveDescription(String archiveDescription)
    {
        this.archiveDescription = archiveDescription;
    }


    /**
     * Return the type of this open metadata archive.
     *
     * @return OpenMetadataArchiveType enum
     */
    public OpenMetadataArchiveType getArchiveType()
    {
        return archiveType;
    }


    /**
     * Set up the type of this open metadata archive.
     *
     * @param archiveType OpenMetadataArchiveType enum
     */
    public void setArchiveType(OpenMetadataArchiveType archiveType)
    {
        this.archiveType = archiveType;
    }


    /**
     * Return the descriptive version name for this archive.
     *
     * @return string version
     */
    public String getArchiveVersion()
    {
        return archiveVersion;
    }


    /**
     * Set up the descriptive version name for this archive.
     *
     * @param archiveVersion string version
     */
    public void setArchiveVersion(String archiveVersion)
    {
        this.archiveVersion = archiveVersion;
    }


    /**
     * Return the name of the originator of this open metadata archive This will be used as the name of the
     * creator for each element in the archive.
     *
     * @return String name
     */
    public String getOriginatorName()
    {
        return originatorName;
    }


    /**
     * Set up the name of the originator of this open metadata archive.  This will be used as the name of the
     * creator for each element in the archive.
     *
     * @param originatorName String name
     */
    public void setOriginatorName(String originatorName)
    {
        this.originatorName = originatorName;
    }


    /**
     * Return the name of the organization that provided this archive.
     *
     * @return String organization name
     */
    public String getOriginatorOrganization()
    {
        return originatorOrganization;
    }

    /**
     * Set up the name of the organization that provided this archive.
     *
     * @param originatorOrganization String name
     */
    public void setOriginatorOrganization(String originatorOrganization)
    {
        this.originatorOrganization = originatorOrganization;
    }


    /**
     * Return the default license for all instance in this archive (this value can be overridden in
     * individual instances in the archive).  A null value means no restrictions.
     *
     * @return license string
     */
    public String getOriginatorLicense()
    {
        return originatorLicense;
    }


    /**
     * Set up the default license for all instances in this archive (this value can be overridden in
     * individual instances in the archive).  A null value means no restrictions.
     *
     * @param originatorLicense license string
     */
    public void setOriginatorLicense(String originatorLicense)
    {
        this.originatorLicense = originatorLicense;
    }


    /**
     * Return the date that this open metadata archive was created.
     *
     * @return Date object
     */
    public Date getCreationDate()
    {
        return creationDate;
    }


    /**
     * Set up the date that this open metadata archive was created.
     *
     * @param creationDate Date object
     */
    public void setCreationDate(Date creationDate)
    {
        this.creationDate = creationDate;
    }


    /**
     * Return the list of GUIDs for open metadata archives that need to be loaded before this one.
     *
     * @return list of guids
     */
    public List<String> getDependsOnArchives()
    {
        if (dependsOnArchives == null)
        {
            return null;
        }
        else if (dependsOnArchives.isEmpty())
        {
            return null;
        }
        else
        {
            return new ArrayList<>(dependsOnArchives);
        }
    }


    /**
     * Set up the list of GUIDs for open metadata archives that need to be loaded before this one.
     *
     * @param dependsOnArchives list of guids
     */
    public void setDependsOnArchives(List<String> dependsOnArchives)
    {
        this.dependsOnArchives = dependsOnArchives;
    }


    /**
     * Standard toString method.
     *
     * @return JSON style description of variables.
     */
    @Override
    public String toString()
    {
        return "OpenMetadataArchiveProperties{" +
                "archiveGUID='" + archiveGUID + '\'' +
                ", archiveName='" + archiveName + '\'' +
                ", archiveDescription='" + archiveDescription + '\'' +
                ", archiveType=" + archiveType +
                ", archiveVersion=" + archiveVersion +
                ", originatorName='" + originatorName + '\'' +
                ", originatorOrganization='" + originatorOrganization + '\'' +
                ", originatorLicense='" + originatorLicense + '\'' +
                ", creationDate=" + creationDate +
                ", dependsOnArchives=" + dependsOnArchives +
                '}';
    }


    /**
     * Validate that an object is equal depending on their stored values.
     *
     * @param objectToCompare object
     * @return boolean result
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
        OpenMetadataArchiveProperties that = (OpenMetadataArchiveProperties) objectToCompare;
        return Objects.equals(getArchiveGUID(), that.getArchiveGUID()) &&
                Objects.equals(getArchiveName(), that.getArchiveName()) &&
                Objects.equals(getArchiveDescription(), that.getArchiveDescription()) &&
                getArchiveType() == that.getArchiveType() &&
                Objects.equals(getArchiveVersion(), that.getArchiveVersion()) &&
                Objects.equals(getOriginatorName(), that.getOriginatorName()) &&
                Objects.equals(getOriginatorOrganization(), that.getOriginatorOrganization()) &&
                Objects.equals(getOriginatorLicense(), that.getOriginatorLicense()) &&
                Objects.equals(getCreationDate(), that.getCreationDate()) &&
                Objects.equals(getDependsOnArchives(), that.getDependsOnArchives());
    }


    /**
     * Return a hash code based on the values of this object.
     *
     * @return in hash code
     */
    @Override
    public int hashCode()
    {
        return Objects.hash(getArchiveGUID(), getArchiveName(), getArchiveDescription(), getArchiveType(), getArchiveVersion(),
                            getOriginatorName(), getOriginatorOrganization(), getOriginatorLicense(), getCreationDate(),
                            getDependsOnArchives());
    }
}
