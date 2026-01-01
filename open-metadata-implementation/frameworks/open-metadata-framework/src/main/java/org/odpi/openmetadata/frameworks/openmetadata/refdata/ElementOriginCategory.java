/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.refdata;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import org.odpi.openmetadata.frameworks.openmetadata.enums.OpenMetadataEnum;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ElementOriginCategory defines where the metadata comes from.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum ElementOriginCategory implements OpenMetadataEnum
{
    /**
     * Unknown provenance.
     */
    UNKNOWN                 (0, "<Unknown>",               "Unknown provenance", "9594fcfc-ad55-48d4-b473-eb2968be754a"),

    /**
     * The element is being maintained within one of the local cohort members. The metadata collection id is for one of the
     * repositories in the cohort. This metadata collection id identifies the home repository for this element.
     */
    LOCAL_COHORT            (1,
                             "Local to cohort",
                             "The element is being maintained within one of the local cohort members. " +
                                     "The metadata collection id is for one of the repositories in the cohort. " +
                                     "This metadata collection id identifies the home repository for this element. ",
                             "37685e52-7815-486c-a3bc-476a1d44f3f8"),

    /**
     * The element was created from an export archive. The metadata collection id for the element is the metadata
     * collection id of the originating server. If the originating server later joins the cohort with the same
     * metadata collection id then these elements will be refreshed from the originating server's current repository.
     */
    EXPORT_ARCHIVE          (2,
                             "Export Archive",
                             "The element was created from an export archive. " +
                                     "The metadata collection id for the element is the metadata collection id of the originating server. " +
                                     "If the originating server later joins the cohort with the same metadata collection id " +
                                     "then these elements will be refreshed from the originating server's current repository.",
                             "71655c74-245f-446f-b6db-65261de5a4c1"),

    /**
     * The element comes from an open metadata content pack. The metadata collection id of the elements is set to the GUID of the pack.
     */
    CONTENT_PACK            (3,
                             "Content Pack",
                             "The element comes from an open metadata content pack. " +
                                     "The metadata collection id of the elements is set to the GUID of the pack.",
                             "b23263bb-59f1-41df-8b60-0a8ac17cbf08"),

    /**
     * The element comes from a metadata repository that used to be a member of the one of the local repository's cohorts, but it has been deregistered.
     * The metadata collection id remains the same. If the repository rejoins the cohort then these elements can be refreshed from the rejoining repository.
     */
    DEREGISTERED_REPOSITORY (4,
                             "Deregistered Repository",
                             "The element comes from a metadata repository that " +
                                     "used to be a member of the one of the local repository's cohorts, but it has been deregistered. " +
                                     "The metadata collection id remains the same. If the repository rejoins the cohort " +
                                     "then these elements can be refreshed from the rejoining repository.",
                             "5e828176-6883-4da0-acd1-5681ed48c2d8"),

    /**
     * The element is part of a service's configuration.  The metadata collection id is null.
     */
    CONFIGURATION           (5,
                             "Configuration",
                             "The element is part of a service's configuration.  The metadata collection id is null.",
                             "303bce06-0254-44e0-9353-3eab7f13e6d6"),

    /**
     * The element is maintained by an external technology.  The metadata collection id is the guid of the technology's descriptive entity.
     */
    EXTERNAL_SOURCE         (6,
                             "External Source",
                             "The element is maintained by an external technology.  The metadata collection id is the guid of the technology's descriptive entity.",
                             "a1550c51-6953-4f31-8ab7-3f0b9d9f45fe");

    private static final long     serialVersionUID = 1L;

    private final int    originCode;
    private final String originName;
    private final String originDescription;
    private final String descriptionGUID;


    /**
     * Constructor for the enum.
     *
     * @param originCode code number for origin
     * @param originName name for origin
     * @param originDescription description for origin
     * @param descriptionGUID unique identifier of the valid value
     *
     */
    ElementOriginCategory(int originCode,
                          String originName,
                          String originDescription,
                          String descriptionGUID)
    {
        this.originCode = originCode;
        this.originName = originName;
        this.originDescription = originDescription;
        this.descriptionGUID = descriptionGUID;
    }


    /**
     * Return the code for metadata element.
     *
     * @return int code for the origin
     */
    @Override
    public int getOrdinal()
    {
        return originCode;
    }


    /**
     * Return the name of the metadata element origin.
     *
     * @return String name
     */
    @Override
    public String getName()
    {
        return originName;
    }


    /**
     * Return the description of the metadata element origin.
     *
     * @return String description
     */
    @Override
    public String getDescription()
    {
        return originDescription;
    }


    /**
     * Return the unique identifier for the valid value that represents the enum value.
     *
     * @return guid
     */
    @Override
    public String getDescriptionGUID()
    {
        return descriptionGUID;
    }


    /**
     * Return whether the enum is the default value or not.
     *
     * @return boolean
     */
    @Override
    public boolean isDefault()
    {
        return false;
    }


    /**
     * Standard toString method.
     *
     * @return print out of variables in a JSON-style
     */
    @Override
    public String toString()
    {
        return "ElementOriginCategory{" +
                "ordinal=" + originCode +
                ", name='" + originName + '\'' +
                ", description='" + originDescription + '\'' +
                '}';
    }
}
