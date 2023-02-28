/* SPDX-License-Identifier: Apache 2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.conformance.workbenches.repository;

import java.io.Serializable;

/**
 * RepositoryConformanceProfileRequirement documents the different requirements for each open
 * metadata repository conformance profile.  The Open Metadata Repository Tests
 * report their findings for each requirement
 */
public enum RepositoryConformanceProfileRequirement implements Serializable
{
    COHORT_REGISTRATION                    (0, "Cohort registration",
                                            "The technology under test is able to register with a cohort.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/metadata-sharing/#cohort-registration",
                                            RepositoryConformanceProfile.METADATA_SHARING),
    REPOSITORY_CONNECTOR                   (1, "Repository connector",
                                            "The technology under test provides a connection to a valid repository connector.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/metadata-sharing/#repository-connector",
                                            RepositoryConformanceProfile.METADATA_SHARING),
    METADATA_COLLECTION_ID                 (2, "Metadata collection id",
                                            "The technology under test broadcasts a unique metadata collection identifier.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/metadata-sharing/#metadata-collection-id",
                                            RepositoryConformanceProfile.METADATA_SHARING),
    SUPPORTED_TYPE_QUERIES                 (3, "Supported type queries",
                                            "The technology under test is able to respond appropriately to queries about its supported types.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/metadata-sharing/#supported-type-queries",
                                            RepositoryConformanceProfile.METADATA_SHARING),
    SUPPORTED_TYPE_NOTIFICATIONS           (4, "Supported type notifications",
                                            "The technology under test is able to send out notifications for its supported types.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/metadata-sharing/#supported-type-notifications",
                                            RepositoryConformanceProfile.METADATA_SHARING),
    CONSISTENT_TYPES                       (5, "Support for consistent types",
                                            "The technology under test supports entity, relationship and classification types that link together.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/metadata-sharing/#consistent-types",
                                            RepositoryConformanceProfile.METADATA_SHARING),
    METADATA_INSTANCE_ACCESS               (6, "Metadata instance access",
                                            "The technology under test supports the retrieval of the current state of specific metadata instances from its repository.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/metadata-sharing/#metadata-instance-access",
                                            RepositoryConformanceProfile.METADATA_SHARING),
    ENTITY_PROPERTY_SEARCH                 (7, "Entity property search",
                                            "The technology under test supports the search and retrieval of current metadata instances from its repository based on matching specific property values.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/entity-search/#entity-property-search",
                                            RepositoryConformanceProfile.ENTITY_SEARCH),
    ENTITY_VALUE_SEARCH                    (8, "Entity value search",
                                            "The technology under test supports the search and retrieval of current metadata instances from its repository based on matching any values.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/entity-search/#entity-value-search",
                                            RepositoryConformanceProfile.ENTITY_SEARCH),
    ENTITY_CONDITION_SEARCH                 (7, "Entity condition search",
                                            "The technology under test supports the search and retrieval of current metadata instances from its repository based on matching logical combinations of property conditions.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/entity-search/#entity-condition-search",
                                            RepositoryConformanceProfile.ENTITY_SEARCH),
    INSTANCE_NOTIFICATIONS                 (9, "Instance notifications",
                                            "The technology under test sends out events when metadata changes in its repository.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/metadata-sharing/#instance-notifications",
                                            RepositoryConformanceProfile.METADATA_SHARING),
    INSTANCE_VERSIONING                    (10, "Instance versioning",
                                            "The technology under test supports incrementing version numbers within metadata instances as they change.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/metadata-sharing/#instance-versioning",
                                            RepositoryConformanceProfile.METADATA_SHARING),
    TYPE_ENFORCEMENT                       (11, "Type enforcement",
                                            "The technology under test ensures metadata instances conform to their type definition.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/metadata-sharing/#type-enforcement",
                                            RepositoryConformanceProfile.METADATA_SHARING),
    UNSUPPORTED_TYPE_ERRORS                (12, "Unsupported type errors",
                                            "The technology under test will not create metadata instances for unsupported types.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/metadata-sharing/#unsupported-type-errors",
                                            RepositoryConformanceProfile.METADATA_SHARING),
    TYPEDEF_CONFLICT_MANAGEMENT            (13, "Type conflict management",
                                            "The technology under test receives/handles and sends type conflict events when type errors are detected.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/metadata-sharing/#type-conflict-management",
                                            RepositoryConformanceProfile.METADATA_SHARING),
    REFERENCE_COPY_STORAGE                 (14, "Reference copy storage",
                                            "The technology under test supports the storage of metadata instances from other repositories in the cohort.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/reference-copies/#reference-copy-storage",
                                            RepositoryConformanceProfile.REFERENCE_COPIES),
    REFERENCE_COPY_LOCKING                 (15, "Reference copy locking",
                                            "The technology under test ensures metadata instances from other repositories can not be updated.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/reference-copies/#reference-copy-locking",
                                            RepositoryConformanceProfile.REFERENCE_COPIES),
    REFERENCE_COPY_DELETE                  (16, "Reference copy delete",
                                            "The technology under test ensures that references copies are removed from its repository with integrity when the master copy is deleted.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/reference-copies/#reference-copy-delete",
                                            RepositoryConformanceProfile.REFERENCE_COPIES),
    ENTITY_LIFECYCLE                       (17, "Entity lifecycle",
                                            "The technology under test supports requests to create, update and purge entity instances.",
                                            "https://egeria-project.org/egeria-docs/guides/cts/repository-profiles/metadata-maintenance/#entity-lifecycle",
                                            RepositoryConformanceProfile.METADATA_MAINTENANCE),
    CLASSIFICATION_LIFECYCLE               (18, "Classification lifecycle",
                                            "The technology under test supports requests to create, update and purge classification instances.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/metadata-maintenance/#classification-lifecycle",
                                            RepositoryConformanceProfile.METADATA_MAINTENANCE),
    RELATIONSHIP_LIFECYCLE                 (19, "Relationship lifecycle",
                                            "The technology under test supports requests to create, update and purge relationship instances.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/metadata-maintenance/#relationship-lifecycle",
                                            RepositoryConformanceProfile.METADATA_MAINTENANCE),
    TYPEDEF_ADD                            (20, "TypeDef add",
                                            "The technology under test supports the addition of new type definitions.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/dynamic-types/#typedef-add",
                                            RepositoryConformanceProfile.DYNAMIC_TYPES),
    TYPEDEF_MAINTENANCE                    (21, "TypeDef maintenance",
                                            "The technology under test supports the commands to change a type definition.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/dynamic-types/#type-def-maintenance",
                                            RepositoryConformanceProfile.DYNAMIC_TYPES),
    ENTITY_NEIGHBORHOOD                    (22, "Entity neighborhood",
                                            "The technology under test supports the ability to query metadata instances that radiate from a specific entity instance through multiple hops.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/graph-queries/#entity-neighborhood",
                                            RepositoryConformanceProfile.GRAPH_QUERIES),
    CONNECTED_ENTITIES                     (23, "Connected entities",
                                            "The technology under test supports the ability to query the entity instances that are connected to a specific entity instance.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/graph-queries/#connected-entities",
                                            RepositoryConformanceProfile.GRAPH_QUERIES),
    LINKED_ENTITIES                        (24, "Linked entities",
                                            "The technology under test supports the ability to locate the metadata instances that connect two entity instances together.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/graph-queries/#linked-entities",
                                            RepositoryConformanceProfile.GRAPH_QUERIES),
    HISTORICAL_PROPERTY_SEARCH             (25, "Historical property queries",
                                            "The technology under test supports the point in time search and retrieval of metadata instances from its repository based on specific property values.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/historical-search/#historical-property-queries",
                                            RepositoryConformanceProfile.HISTORICAL_SEARCH),
    HISTORICAL_VALUE_SEARCH                (26, "Historical value queries",
                                            "The technology under test supports the point in time search and retrieval of metadata instances from its repository based on arbitrary values.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/historical-search/#historical-value-queries",
                                            RepositoryConformanceProfile.HISTORICAL_SEARCH),
    STORE_ENTITY_PROXIES                   (27, "Store entity proxies",
                                            "The technology under test supports the storage of metadata instances that are proxies for instances from other repositories in the cohort.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/entity-proxies/#store-entity-proxies",
                                            RepositoryConformanceProfile.ENTITY_PROXIES),

    RETRIEVE_ENTITY_PROXIES                (28, "Retrieve entity proxies",
                                            "The technology under test supports the command to retrieve a proxy instance of a metadata instance.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/entity-proxies/#retrieve-entity-proxies",
                                            RepositoryConformanceProfile.ENTITY_PROXIES),

    ENTITY_PROXY_LOCKING                   (29, "Entity proxy locking",
                                            "The technology under test ensures metadata instances that are proxies for instances from other repositories can not be updated.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/entity-proxies/#entity-proxy-locking",
                                            RepositoryConformanceProfile.ENTITY_PROXIES),

    ENTITY_PROXY_DELETE                    (30, "Retrieve entity proxies",
                                            "The technology under test ensures that proxy instances are removed from its repository with integrity when the master copy is deleted.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/entity-proxies/#entity-proxy-delete",
                                            RepositoryConformanceProfile.ENTITY_PROXIES),

    SOFT_DELETE_INSTANCE                   (31, "Soft-delete instance",
                                            "The technology under test supports the command to change the type of a metadata instance to either its super type or a subtype.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/soft-delete-restore/#soft-delete-instance",
                                            RepositoryConformanceProfile.SOFT_DELETE_RESTORE),
    UNDELETE_INSTANCE                      (32, "Undelete instance",
                                            "The technology under test supports the command to change the type of a metadata instance to either its super type or a subtype.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/soft-delete-restore/#undelete-instance",
                                            RepositoryConformanceProfile.SOFT_DELETE_RESTORE),
    NEW_VERSION_NUMBER_ON_RESTORE          (33, "Incremented version number on restore",
                                            "The technology under test ensures a restored instance has an incremented version number.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/soft-delete-restore/#incremented-version-number-on-restore",
                                            RepositoryConformanceProfile.SOFT_DELETE_RESTORE),
    RETURN_PREVIOUS_VERSION                (34, "Return previous version",
                                            "The technology under test supports the command to restore the previous version of an instance.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/undo-update/#return-previous-version",
                                            RepositoryConformanceProfile.UNDO_UPDATE),
    NEW_VERSION_NUMBER_ON_UNDO             (35, "New version number on undo",
                                            "The technology under test ensures a restored instance has an incremented version number.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/undo-update/#new-version-number-on-undo",
                                            RepositoryConformanceProfile.UNDO_UPDATE),
    UPDATE_INSTANCE_IDENTIFIER             (36, "Update instance identifier",
                                            "The technology under test supports the command to change the unique identifier (guid) of a metadata instance mastered in its repository.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/reidentify-instance/#update-instance-identifier",
                                            RepositoryConformanceProfile.REIDENTIFY_INSTANCE),
    SEND_REIDENTIFIED_EVENT                (37, "Send reidentified event",
                                            "The technology under test supports the broadcasting of instance re-identified events to other members of the cohort.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/reidentify-instance/#send-reidentified-event",
                                            RepositoryConformanceProfile.REIDENTIFY_INSTANCE),
    PROCESS_REIDENTIFIED_EVENT             (38, "Process reidentified event",
                                            "The technology under test supports the receipt/processing of instance re-identified events from other members of the cohort.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/reidentify-instance/#process-reidentified-event",
                                            RepositoryConformanceProfile.REIDENTIFY_INSTANCE),
    UPDATE_INSTANCE_TYPE                   (39, "Update instance type",
                                            "The technology under test supports the command to change the type of a metadata instance mastered in its repository to either its super type or a subtype.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/retype-instance/#update-instance-type",
                                            RepositoryConformanceProfile.RETYPE_INSTANCE),
    SEND_RETYPED_EVENT                     (40, "Send retyped event",
                                            "The technology under test supports the broadcasting of instance re-typed events to other members of the cohort.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/retype-instance/#send-retyped-event",
                                            RepositoryConformanceProfile.RETYPE_INSTANCE),
    PROCESS_RETYPED_EVENT                  (41, "Process retyped event",
                                            "The technology under test supports the receipt/processing of instance re-typed events from other members of the cohort.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/retype-instance/#process-retyped-event",
                                            RepositoryConformanceProfile.RETYPE_INSTANCE),
    UPDATE_INSTANCE_HOME                   (42, "Update instance home",
                                            "The technology under test supports the command to update the metadata collection id for a metadata instance.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/rehome-instance/#update-instance-home",
                                            RepositoryConformanceProfile.REHOME_INSTANCE),
    SEND_REHOMED_EVENT                     (43, "Send re-homed event",
                                            "The technology under test supports the broadcasting of instance re-homed events to other members of the cohort.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/rehome-instance/#send-re-homed-event",
                                            RepositoryConformanceProfile.REHOME_INSTANCE),
    PROCESS_REHOMED_EVENT                  (44, "Process re-homed event",
                                            "The technology under test supports the receipt/processing of instance re-homed events from other members of the cohort.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/rehome-instance/#process-re-homed-event",
                                            RepositoryConformanceProfile.REHOME_INSTANCE),
    RELATIONSHIP_PROPERTY_SEARCH           (45, "Relationship property search",
                                            "The technology under test supports the search and retrieval of current relationship instances from its repository based on matching specific property values.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/relationship-search/#relationship-property-search",
                                            RepositoryConformanceProfile.RELATIONSHIP_SEARCH),
    RELATIONSHIP_VALUE_SEARCH              (46, "Relationship value search",
                                            "The technology under test supports the search and retrieval of current relationship instances from its repository based on matching any values.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/relationship-search/#relationship-value-search",
                                            RepositoryConformanceProfile.RELATIONSHIP_SEARCH),
    RELATIONSHIP_CONDITION_SEARCH          (7, "Relationship condition search",
                                             "The technology under test supports the search and retrieval of current metadata instances from its repository based on matching logical combinations of property conditions.",
                                             "https://egeria-project.org/guides/cts/repository-profiles/relationship-search/#relationship-condition-search",
                                             RepositoryConformanceProfile.RELATIONSHIP_SEARCH),
    ENTITY_ADVANCED_PROPERTY_SEARCH        (47, "Advanced property search",
                                            "The technology under test supports the use of regular expressions within match properties to search for entity instances.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/entity-advanced-search/#advanced-property-search",
                                            RepositoryConformanceProfile.ENTITY_ADVANCED_SEARCH),
    ENTITY_ADVANCED_VALUE_SEARCH           (48, "Advanced value search",
                                            "The technology under test supports the use of regular expressions within value search criteria to search for entity instances.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/entity-advanced-search/#advanced-value-search",
                                            RepositoryConformanceProfile.ENTITY_ADVANCED_SEARCH),
    RELATIONSHIP_ADVANCED_PROPERTY_SEARCH  (49, "Relationship advanced property search",
                                            "The technology under test supports the use of regular expressions within match properties to search for relationship instances.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/relationship-advanced-search/#advanced-property-search",
                                            RepositoryConformanceProfile.RELATIONSHIP_ADVANCED_SEARCH),
    RELATIONSHIP_ADVANCED_VALUE_SEARCH     (50, "Relationship advanced value search",
                                            "The technology under test supports the use of regular expressions within value search criteria to search for relationship instances.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/relationship-advanced-search/#advanced-value-search",
                                            RepositoryConformanceProfile.RELATIONSHIP_ADVANCED_SEARCH),
    GET_ENTITY_VERSIONS                    (51, "Get entity versions",
                                            "The technology under test supports the command to retrieve stored versions of an entity.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/instance-versions/#get-entity-versions",
                                            RepositoryConformanceProfile.INSTANCE_VERSIONS),
    GET_RELATIONSHIP_VERSIONS              (52, "Get relationship versions",
                                            "The technology under test supports the command to retrieve stored versions of a relationship.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/instance-versions/#get-relationship-versions",
                                            RepositoryConformanceProfile.INSTANCE_VERSIONS),
    STORE_EXTERNAL_ENTITIES               (53, "Store external entities",
                                            "The technology under test supports the command to create external entities and retrieve them with the correct provenance.",
                                            "https://egeria-project.org/guides/cts/repository-profiles/external-instances/#store-external-entities",
                                            RepositoryConformanceProfile.EXTERNAL_INSTANCES),
    STORE_EXTERNAL_RELATIONSHIPS          (54, "Store external relationships",
                                           "The technology under test supports the command to create external relationships and retrieve them with the correct provenance.",
                                           "https://egeria-project.org/guides/cts/repository-profiles/external-instances/#store-external-relationships",
                                           RepositoryConformanceProfile.EXTERNAL_INSTANCES),
    STORE_EXTERNAL_CLASSIFICATIONS        (55, "Store external classifications",
                                           "The technology under test supports the command to create external classifications and retrieve them with the correct provenance.",
                                           "https://egeria-project.org/guides/cts/repository-profiles/external-instances/#store-external-classifications",
                                           RepositoryConformanceProfile.EXTERNAL_INSTANCES);




    private static final long serialVersionUID = 1L;

    private final int                          ordinal;
    private final String                       name;
    private final String                       description;
    private final String                       documentationURL;
    private final RepositoryConformanceProfile profile;



    /**
     * Default constructor sets up the values for this enum instance.
     *
     * @param ordinal int identifier for the enum, used for indexing arrays etc. with the enum.
     * @param name String name for the enum, used for message content.
     * @param description String default description for the enum, used when there is no natural
     *                             language resource bundle available.
     * @param documentationURL link to more information
     * @param profile parent profile
     */
    RepositoryConformanceProfileRequirement(int                           ordinal,
                                            String                        name,
                                            String                        description,
                                            String                        documentationURL,
                                            RepositoryConformanceProfile  profile)
    {
        this.ordinal = ordinal;
        this.name = name;
        this.description = description;
        this.documentationURL = documentationURL;
        this.profile = profile;
    }


    /**
     * Return the identifier for the enum, used for indexing arrays etc. with the enum.
     *
     * @return int identifier
     */
    public int getOrdinal()
    {
        return ordinal;
    }


    /**
     * Return the name for the enum, used for message content.
     *
     * @return String name
     */
    public String getName()
    {
        return name;
    }


    /**
     * Return the default description for the enum, used when there is no natural
     * language resource bundle available.
     *
     * @return String default description
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the URL to link to more documentation about this profile.
     *
     * @return url
     */
    public String getDocumentationURL()
    {
        return documentationURL;
    }


    /**
     * Return the profile that this requirement belongs to.
     *
     * @return priority enum
     */
    public RepositoryConformanceProfile getProfile()
    {
        return profile;
    }


    /**
     * Return the profile identifier that this requirement belongs to.
     *
     * @return profile id
     */
    public Integer getProfileId()
    {
        return profile.getProfileId();
    }


    /**
     * Return the requirement identifier as an Integer.
     *
     * @return requirement id
     */
    public Integer getRequirementId()
    {
        return ordinal;
    }


    /**
     * toString() JSON-style
     *
     * @return string description
     */
    @Override
    public String toString()
    {
        return "RepositoryConformanceProfileRequirement{" +
                "ordinal=" + ordinal +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", documentationURL='" + documentationURL + '\'' +
                ", profile=" + profile +
                ", profileId=" + getProfileId() +
                ", requirementId=" + getRequirementId() +
                '}';
    }
}
