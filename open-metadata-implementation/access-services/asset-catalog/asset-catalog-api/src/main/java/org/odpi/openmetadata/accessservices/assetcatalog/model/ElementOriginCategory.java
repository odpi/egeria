/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.accessservices.assetcatalog.model;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * ElementOriginCategory defines where the metadata comes from.
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
@Getter
@ToString
@AllArgsConstructor
public enum ElementOriginCategory implements Serializable
{
    UNKNOWN                 (0, "<Unknown>",               "Unknown provenance"),
    LOCAL_COHORT            (1, "Local to cohort",         "The element is being maintained within one of the local cohort members. " +
                                                                  "The metadata collection id is for one of the repositories in the cohort. " +
                                                                  "This metadata collection id identifies the home repository for this element. "),
    EXPORT_ARCHIVE          (2, "Export Archive",          "The element was created from an export archive. " +
                                                                  "The metadata collection id for the element is the metadata collection id of the originating server. " +
                                                                  "If the originating server later joins the cohort with the same metadata collection Id " +
                                                                  "then these elements will be refreshed from the originating server's current repository."),
    CONTENT_PACK            (3, "Content Pack",            "The element comes from an open metadata content pack. " +
                                                                  "The metadata collection id of the elements is set to the GUID of the pack."),
    DEREGISTERED_REPOSITORY (4, "Deregistered Repository", "The element comes from a metadata repository that " +
                                                                  "used to be a member of the one of the local repository's cohorts but it has been deregistered. " +
                                                                  "The metadata collection id remains the same. If the repository rejoins the cohort " +
                                                                  "then these elements can be refreshed from the rejoining repository."),
    CONFIGURATION           (5, "Configuration",           "The element is part of a service's configuration.  The metadata collection id is null."),
    EXTERNAL_SOURCE         (6, "External Source",         "The element is maintained by an external technology.  The metadata collection id is the guid of the technology's descriptive entity.");

    private static final long serialVersionUID = 1L;

    /**
     *  The code for metadata element.
     * -- GETTER --
     * Return the code for metadata element.
     * @return int code for the origin
     */
    private int    originCode;

    /**
     *  Name of the metadata element origin.
     * -- GETTER --
     * Return the name of the metadata element origin.
     * @return name of the metadata origin
     */
    private String originName;

    /**
     *  Description of the metadata element origin.
     * -- GETTER --
     * Return the description of the metadata element origin.
     * @return description of the metadata origin
     */
    private String originDescription;
}
