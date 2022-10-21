/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.repositoryservices.connectors.stores.metadatacollectionstore.properties.instances;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;

/**
 * InstanceProvenanceType defines where the metadata comes from and, hence if it can be updated.
 * <ul>
 *     <li>
 *         UNKNOWN: uninitialized provenance value.
 *     </li>
 *     <li>
 *         LOCAL_COHORT: the element is being maintained within the local cohort.
 *         The metadata collection id is for one of the repositories in the cohort.
 *         This metadata collection id identifies the home repository for this element.
 *     </li>
 *     <li>
 *         EXPORT_ARCHIVE: the element was created from an export archive.
 *         The metadata collection id for the element is the metadata collection id of the originating server.
 *         If the originating server later joins the cohort with the same metadata collection Id then these
 *         elements will be refreshed from the originating server's current repository.
 *     </li>
 *     <li>
 *         CONTENT_PACK: the element comes from an open metadata content pack.
 *         The metadata collection id of the elements is set to the GUID of the pack.
 *     </li>
 *     <li>
 *         DEREGISTERED_REPOSITORY: the element comes from a metadata repository that used to be a part
 *         of the repository cohort but has been deregistered. The metadata collection id remains the same.
 *         If the repository rejoins the cohort then these elements can be refreshed from the rejoining repository.
 *     </li>
 *     <li>
 *         CONFIGURATION: the element comes from a configuration server. The metadata collection id is null.
 *     </li>
 *     <li>
 *         DATA_PLATFORM: the element is being managed by an external data platform using the Data Platform OMAS.
 *         This data platform hosts the data assets it documents and the metadata is an integral part of its
 *         operation.
 *     </li>
 *     <li>
 *         EXTERNAL_ENGINE: the element is being maintained by an external engine that is manipulating data
 *         assets in real-time.  The metadata describes events in real-time and as such should not be
 *         updated by other metadata processes.
 *     </li>
 *     <li>
 *         EXTERNAL_TOOL: the element is being maintained by an external tool.  Typically this is descriptive
 *         metadata that the tool is using to provide a service.  Hence it has tagged the metadata as
 *         read-only.
 *     </li>
 * </ul>
 */
@JsonAutoDetect(getterVisibility=PUBLIC_ONLY, setterVisibility=PUBLIC_ONLY, fieldVisibility=NONE)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown=true)
public enum InstanceProvenanceType implements Serializable
{
    UNKNOWN                 (0, "<Unknown>",               "Unknown provenance"),
    LOCAL_COHORT            (1, "Local to cohort",         "The instance is being maintained within one of the local cohort members. " +
                                                                          "The metadata collection id is for one of the repositories in the cohort. " +
                                                                          "This metadata collection id identifies the home repository for this element. "),
    EXPORT_ARCHIVE          (2, "Export Archive",          "The instance was created from an export archive. " +
                                                                          "The metadata collection id for the element is the metadata collection id of the originating server. " +
                                                                          "If the originating server later joins the cohort with the same metadata collection Id " +
                                                                          "then these elements will be refreshed from the originating server's current repository."),
    CONTENT_PACK            (3, "Content Pack",            "The instance comes from an open metadata content pack. " +
                                                                          "The metadata collection id of the elements is set to the GUID of the pack."),
    DEREGISTERED_REPOSITORY (4, "Deregistered Repository", "The instance comes from a metadata repository that " +
                                                                          "used to be a member of the one of the local repository's cohorts but it has been deregistered. " +
                                                                          "The metadata collection id remains the same. If the repository rejoins the cohort " +
                                                                          "then these elements can be refreshed from the rejoining repository."),
    CONFIGURATION           (5, "Configuration",           "The instance is part of a service's configuration.  The metadata collection id is null."),
    EXTERNAL_SOURCE         (6, "External Source",         "The instance is maintained by an external technology.  The metadata collection id is the guid of the technology's descriptive entity.");

    private static final long serialVersionUID = 1L;

    private final int            ordinal;
    private final String         name;
    private final String         description;


    /**
     * Default constructor for the instance provenance type.
     *
     * @param ordinal numerical representation of the instance provenance type
     * @param name default string name of the instance provenance type
     * @param description default string description of the instance provenance type
     */
    InstanceProvenanceType(int  ordinal, String name, String description)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
    }


    /**
     * Return the numeric representation of the instance provenance type.
     *
     * @return int ordinal
     */
    public int getOrdinal() { return ordinal; }


    /**
     * Return the default name of the instance provenance type.
     *
     * @return String name
     */
    public String getName() { return name; }


    /**
     * Return the default description of the instance provenance type.
     *
     * @return String description
     */
    public String getDescription() { return description; }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "InstanceProvenanceType{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
