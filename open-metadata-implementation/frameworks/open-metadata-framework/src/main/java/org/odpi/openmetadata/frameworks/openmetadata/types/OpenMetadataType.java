/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.frameworks.openmetadata.types;

import java.util.HashMap;
import java.util.Map;

/**
 * OpenMetadataType provides property name mapping for the open metadata types.
 * It includes identifiers for all the types.
 */
public enum OpenMetadataType
{
    /* ============================================================================================================================*/
    /* Area 0 - Basic definitions and Infrastructure                                                                               */
    /* ============================================================================================================================*/

    /* Model 0010 Basic Model */

    /**
     * Common root for all open metadata entity types.
     */
    OPEN_METADATA_ROOT("4e7761e8-3969-4627-8f40-bfe3cde85a1d",
                       "OpenMetadataRoot",
                       OpenMetadataWikiPages.MODEL_0010_BASIC_MODEL,
                       "9f665566-2144-4c6c-95db-b1d1f86c412f",
                       "Common root for all open metadata entity types."),

    /**
     * An element whose real-world counterpart has been deleted or moved to offline archived.
     */
    MEMENTO_CLASSIFICATION("ecdcd472-6701-4303-8dec-267bcb54feb9",
                           "Memento",
                           OpenMetadataWikiPages.MODEL_0010_BASIC_MODEL,
                           "7332d853-1c43-4518-ab96-44f374b9966d",
                           "An element whose real-world counterpart has been deleted or moved to offline archived."),

    /**
     * Identifies the anchor entity for an element that is part of a large composite object such as an asset.
     */
    ANCHORS_CLASSIFICATION("aa44f302-2e43-4669-a1e7-edaae414fc6e",
                           "Anchors",
                           OpenMetadataWikiPages.MODEL_0010_BASIC_MODEL,
                           "09708101-5b38-4c3c-b1e5-983f9c731e5c",
                           "Identifies the anchor entity for an element that is part of a large composite object such as an asset."),

    /**
     * An open metadata entity that has a unique identifier.
     */
    REFERENCEABLE("a32316b8-dc8c-48c5-b12b-71c1b2a080bf",
                  "Referenceable",
                  OpenMetadataWikiPages.MODEL_0010_BASIC_MODEL,
                  "d9a26761-40ba-441d-9981-cc8f137fe379",
                  "An open metadata entity that has a unique identifier."),


    /**
     * The description of an asset that needs to be catalogued and governed.
     */
    ASSET("896d14c2-7522-4f6c-8519-757711943fe6",
          "Asset",
          OpenMetadataWikiPages.MODEL_0010_BASIC_MODEL,
          "11b20cb3-2149-47f9-ad0a-058332a3cb5f",
          "The description of a resource that needs to be catalogued and governed."),

    /**
     * The description of a collection of data.
     */
    DATA_ASSET("ca826f9e-7fb1-4005-921a-fee1c4cd221b",
          "DataAsset",
          OpenMetadataWikiPages.MODEL_0010_BASIC_MODEL,
          "8661a98b-1a2e-4a33-bb71-078f48532385",
          "The description of a collection of data."),


    /**
     * Well-defined sequence of activities performed by people or software components.
     */
    PROCESS("d8f33bd7-afa9-4a11-a8c7-07dcec83c050",
            "Process",
            OpenMetadataWikiPages.MODEL_0010_BASIC_MODEL,
            "f1fecd94-215c-4e42-9cd9-a945156af2a8",
            "Well-defined sequence of activities performed by people or software components."),

    /**
     * Physical infrastructure or software platform.
     */
    INFRASTRUCTURE("c19746ac-b3ec-49ce-af4b-83348fc55e07",
                   "Infrastructure",
                   OpenMetadataWikiPages.MODEL_0010_BASIC_MODEL,
                   "002a59fc-9ee8-4e7c-b18f-39e035128127",
                   "Physical infrastructure or software platform."),

    /**
     * Links an Asset entity describing a collection of sample data that originates from the resource represented by the Referenceable entity.
     */
    SAMPLE_DATA_RELATIONSHIP("0ee9c0f1-a89b-4806-8276-7c74f07fe190",
                             "SampleData",
                             OpenMetadataWikiPages.MODEL_0010_BASIC_MODEL,
                             "bfb3a740-756d-45d7-9f49-8194b52a5cb0",
                             "Links an Asset entity describing a collection of sample data that originates from the resource represented by the Referenceable entity."),

    /**
     * Defines the latest change to an anchor entity and its associated attachments.
     */
    LATEST_CHANGE_CLASSIFICATION("adce83ac-10f1-4279-8a35-346976e94466",
                                 "LatestChange",
                                 OpenMetadataWikiPages.MODEL_0010_BASIC_MODEL,
                                 "5f6bf15f-0c9d-434b-a84e-588269e70b0b",
                                 "Defines the latest change to an anchor entity and its associated attachments."),


    /**
     * Marks the referenceable as a template for creating new objects.
     */
    TEMPLATE_CLASSIFICATION("25fad4a2-c2d6-440d-a5b1-e537881f84ee",
                            "Template",
                            OpenMetadataWikiPages.MODEL_0011_MANAGING_REFERENCEABLES,
                            "8982fa20-41a8-4ad2-a72a-519bba6e3f82",
                            "Marks the referenceable as a template for creating new objects."),

    /**
     * Provides the template for creating a metadata representation of the linked from valid value.
     */
    CATALOG_TEMPLATE_RELATIONSHIP("e0a32163-00d3-4748-afdb-478a1dfbba23",
                                  "CatalogTemplate",
                                  OpenMetadataWikiPages.MODEL_0011_MANAGING_REFERENCEABLES,
                                  "fc9af2cd-851b-46a7-9198-b9f95ad64811",
                                  "Provides the template for creating a metadata representation of the linked from valid value."),

    /**
     * An element that has been introduced in a template to provide an end to a relationship that is part of the template but should not be visible outside the template use.
     */
    TEMPLATE_SUBSTITUTE_CLASSIFICATION("93b293c3-1185-4921-aa1c-237d3f0a5d5c",
                                       "TemplateSubstitute",
                                       OpenMetadataWikiPages.MODEL_0011_MANAGING_REFERENCEABLES,
                                       "29d921c0-d0fa-4047-97c0-81d6f9e87573",
                                       "An element that has been introduced in a template to provide an end to a relationship that is part of the template but should not be visible outside the template use."),

    /**
     * Defines source of the information for a referenceable that was created by copying from a template.
     */
    SOURCED_FROM_RELATIONSHIP("87b7371e-e311-460f-8849-08646d0d6ad3",
                              "SourcedFrom",
                              OpenMetadataWikiPages.MODEL_0011_MANAGING_REFERENCEABLES,
                              "e5794f9e-adf0-461d-bee2-7ba807dc1511",
                              "Defines source of the information for a referenceable that was created by copying from a template."),

    /**
     * Link to indicate that a referenceable provides additional information about another referenceable.
     */
    MORE_INFORMATION_RELATIONSHIP("1cbf059e-2c11-4e0c-8aae-1da42c1ee73f",
                                  "MoreInformation",
                                  OpenMetadataWikiPages.MODEL_0019_MORE_INFORMATION,
                                  "2d3f4815-d8e7-44c3-88cc-f587cfe26e70",
                                  "Link to indicate that a referenceable provides additional information about another referenceable."),

    /**
     * Links supporting resources to a referenceable (typically an Actor Profile, Connector, Governance Domain, Project, Meeting or Community).
     */
    RESOURCE_LIST_RELATIONSHIP ("73cf5658-6a73-4ebc-8f4d-44fdfac0b437",
                                "ResourceList",
                                OpenMetadataWikiPages.MODEL_0019_MORE_INFORMATION,
                                "cb027494-8de7-43cc-845c-57d4f0bbf6d5",
                                "Links supporting resources to a referenceable (typically an Actor Profile, Connector, Governance Domain, Project, Meeting or Community)."),

    /**
     * A shareable keyword to help locating relevant assets.
     */
    SEARCH_KEYWORD("0134c9ae-0fe6-4224-bb3b-e18b78a90b1e",
                   "SearchKeyword",
                   OpenMetadataWikiPages.MODEL_0012_SEARCH_KEYWORDS,
                   "26707bea-f65c-43bc-9576-5991d9bafc24",
                   "A shareable keyword to help locating relevant assets."),

    /**
     * Provides a link to a keyword that helps to identify specific elements in a search.
     */
    SEARCH_KEYWORD_LINK_RELATIONSHIP("d2f8df24-6905-49b8-b389-31b2da156ece",
                                     "SearchKeywordLink",
                                     OpenMetadataWikiPages.MODEL_0012_SEARCH_KEYWORDS,
                                     "17f7fc3d-3b4d-4216-9546-256af94c9c99",
                                     "Provides a link to a keyword that helps to identify specific elements in a search."),

    /**
     * Links search keywords that have similar meanings together.
     */
    RELATED_KEYWORD_RELATIONSHIP("f9ffa8a8-80f5-4e6d-9c05-a3a5e0277d62",
                                 "RelatedKeyword",
                                 OpenMetadataWikiPages.MODEL_0012_SEARCH_KEYWORDS,
                                 "e8b2dba2-6d3a-4a83-8ebf-1f0555b5164d",
                                 "Links search keywords that have similar meanings together."),

    /**
     * Link to more information.
     */
    EXTERNAL_REFERENCE_LINK_RELATIONSHIP("7d818a67-ab45-481c-bc28-f6b1caf12f06",
                                         "ExternalReferenceLink",
                                         OpenMetadataWikiPages.MODEL_0015_LINKED_MEDIA_TYPES,
                                         "906ea4d6-1825-40c8-b5cb-07ecca6b848e",
                                         "Link to more information."),

    /**
     * A link to an external reference source such as a web page, article or book.
     */
    EXTERNAL_REFERENCE("af536f20-062b-48ef-9c31-1ddd05b04c56",
                       "ExternalReference",
                       OpenMetadataWikiPages.MODEL_0015_LINKED_MEDIA_TYPES,
                       "cd59edf8-ef6d-4436-bfc5-e93403238df4",
                       "A link to an external reference source such as a web page, article or book."),

    /**
     * Images, video or sound media.
     */
    RELATED_MEDIA("747f8b86-fe7c-4c9b-ba75-979e093cc307",
                  "RelatedMedia",
                  OpenMetadataWikiPages.MODEL_0015_LINKED_MEDIA_TYPES,
                  "4b4f379f-0d69-46ae-b5cd-0b6c08dffb21",
                  "Images, video or sound media."),

    /**
     * Link to related media such as images, videos and audio.
     */
    MEDIA_REFERENCE_RELATIONSHIP("1353400f-b0ab-4ab9-ab09-3045dd8a7140",
                                 "MediaReference",
                                 OpenMetadataWikiPages.MODEL_0015_LINKED_MEDIA_TYPES,
                                 "3e7b1513-f7df-4de2-ba32-0b1cc82e2946",
                                 "Link to related media such as images, videos and audio."),

    /**
     * Alternative identifier used in another system.
     */
    EXTERNAL_ID("7c8f8c2c-cc48-429e-8a21-a1f1851ccdb0",
                "ExternalId",
                OpenMetadataWikiPages.MODEL_0017_EXTERNAL_IDENTIFIERS,
                null,
                "Alternative identifier used in another system."),

    /**
     * Link between an external identifier and an asset or related item.
     */
    EXTERNAL_ID_LINK_RELATIONSHIP("28ab0381-c662-4b6d-b787-5d77208de126",
                                  "ExternalIdLink",
                                  OpenMetadataWikiPages.MODEL_0017_EXTERNAL_IDENTIFIERS,
                                  "4c97ac83-5c1f-4d15-90d8-39b28290c898",
                                  "Link between an external identifier and an asset or related item."),

    /**
     * Places where an external identifier is recognized.
     */
    EXTERNAL_ID_SCOPE_RELATIONSHIP("8c5b1415-2d1f-4190-ba6c-1fdd47f03269",
                                   "ExternalIdScope",
                                   OpenMetadataWikiPages.MODEL_0017_EXTERNAL_IDENTIFIERS,
                                   "e1e481ce-0536-4637-8658-5224353c7d69",
                                   "Places where an external identifier is recognized."),

    /**
     * Additional properties that support a particular vendor or service.
     */
    PROPERTY_FACET("6403a704-aad6-41c2-8e08-b9525c006f85",
                   "PropertyFacet",
                   OpenMetadataWikiPages.MODEL_0020_PROPERTY_FACETS,
                   "ff8c80af-5eb2-43a4-ae1c-1920d4a1a619",
                   "Additional properties that support a particular vendor or service."),


    /**
     * Link between a property facet and the element it relates to.
     */
    REFERENCEABLE_FACET("58c87647-ada9-4c90-a3c3-a40ace46b1f7",
                        "ReferenceableFacet",
                        OpenMetadataWikiPages.MODEL_0020_PROPERTY_FACETS,
                        "2393b97a-436a-44f6-a5ef-45ad8b16b5d4",
                        "Link between a property facet and the element it relates to."),


    /**
     * A group of related items.
     */
    COLLECTION("347005ba-2b35-4670-b5a7-12c9ebed0cf7",
               "Collection",
               OpenMetadataWikiPages.MODEL_0021_COLLECTIONS,
               "b5ff9f14-e7eb-465b-8803-b1ba6ea3a8afe",
               "A group of related items."),


    /**
     * Identifies a member of a collection.
     */
    COLLECTION_MEMBERSHIP_RELATIONSHIP("5cabb76a-e25b-4bb5-8b93-768bbac005af",
                                       "CollectionMembership",
                                       OpenMetadataWikiPages.MODEL_0021_COLLECTIONS,
                                       "5fdfa2b1-7511-4700-aa27-e87458b93446",
                                       "Identifies a member of a collection."),


    /**
     * This collection is the root collection in a collection hierarchy.
     */
    ROOT_COLLECTION("9fdb6d71-fd69-4c40-81f3-5eab1c44d1f4",
                    "RootCollection",
                    OpenMetadataWikiPages.MODEL_0021_COLLECTIONS,
                    "f531c0b8-712c-4327-a8e5-7500727bab1d",
                    "This collection is the root collection in a collection hierarchy."),


    /**
     * This is a collection of data fields that describe some desired data.
     */
    DATA_SPEC_COLLECTION("781c5319-af83-4195-ada7-a44914f3e63a",
                         "DataSpec",
                         OpenMetadataWikiPages.MODEL_0021_COLLECTIONS,
                         "3d80f3ea-86d9-4a76-9531-d0ffd0650116",
                         "This is a collection of data fields that describe some desired data."),


    /**
     * This collection is the home collection for a referenceable.
     */
    HOME_COLLECTION("16274db0-ebd8-4a2b-b8ba-134a3f4d6130",
                    "HomeCollection",
                    OpenMetadataWikiPages.MODEL_0021_COLLECTIONS,
                    "e1138106-f222-4847-ba1e-c6f014de7b4a",
                    "This collection is the home collection for a referenceable."),

    /**
     * Defines that a collection is a set of results from an activity, query, ...
     */
    RESULTS_SET("3947f08d-7412-4022-81fc-344a20dfbb26",
                "ResultsSet",
                OpenMetadataWikiPages.MODEL_0021_COLLECTIONS,
                "883529d0-f230-4fd1-93d3-13820090f320",
                "Defines that a collection is a set of results from an activity, query, ..."),

    /**
     * A collection that lists elements that have been part of recent activity.
     */
    RECENT_ACCESS("e68d7cdf-08bc-4eee-844b-502f5940082",
                  "RecentAccess",
                  OpenMetadataWikiPages.MODEL_0021_COLLECTIONS,
                  "e3c0d70e-8d9c-4575-a21d-dcc7a2bb5dcc",
                  "A collection that lists elements that have been part of recent activity."),

    /**
     * Defines that a collection should be treated like a folder.
     */
    FOLDER("3c0fa687-8a63-4c8e-8bda-ede9c78be6c7",
           "Folder",
           OpenMetadataWikiPages.MODEL_0021_COLLECTIONS,
           "b2fe2ac5-f4d5-4eac-b4cf-c5fd112395eb",
           "Defines that a collection should be treated like a folder."),

    /**
     * Defines a list of activities such as ToDos, Tasks etc...
     */
    WORK_ITEM_LIST("9d958a7c-5fca-4acc-83b3-f59b70e73f54",
                   "WorkItemList",
                   OpenMetadataWikiPages.MODEL_0021_COLLECTIONS,
                   "2ee49f29-da72-490f-a6c4-e7525516e6f0",
                   "Defines a list of activities such as ToDos, Tasks etc..."),


    /**
     * A physical place, digital location or area.
     */
    LOCATION("3e09cb2b-5f15-4fd2-b004-fe0146ad8628",
                  "Location",
                  OpenMetadataWikiPages.MODEL_0025_LOCATIONS,
                  "b9ab8933-9453-4cfd-9fa7-61c8dd99934a",
                  "A physical place, digital location or area."),

    /**
     * A location linked to a physical place.
     */
    FIXED_LOCATION_CLASSIFICATION("bc111963-80c7-444f-9715-946c03142dd2",
                                  "FixedLocation",
                                  OpenMetadataWikiPages.MODEL_0025_LOCATIONS,
                                  "6c543c13-890e-4df5-968e-d06a38f11bf5",
                                  "A location linked to a physical place."),

    /**
     * A location that protects the assets in its care.
     */
    SECURE_LOCATION_CLASSIFICATION("e7b563c0-fcdd-4ba7-a046-eecf5c4638b8",
                                   "SecureLocation",
                                   OpenMetadataWikiPages.MODEL_0025_LOCATIONS,
                                   "efdc3472-bb97-4340-aefe-b954b64aba4d",
                                   "A location that protects the assets in its care."),

    /**
     * A digital location.
     */
    CYBER_LOCATION_CLASSIFICATION("f9ec3633-8ac8-480b-aa6d-5e674b9e1b17",
                                  "CyberLocation",
                                  OpenMetadataWikiPages.MODEL_0025_LOCATIONS,
                                  "51be87f3-f16e-4ff7-bce1-9d91d6f26959",
                                  "A digital location."),

    /**
     * An asset not restricted to a single physical location.
     */
    MOBILE_ASSET_CLASSIFICATION("b25fb90d-8fa2-4aa9-b884-ff0a6351a697",
                                "MobileAsset",
                                OpenMetadataWikiPages.MODEL_0025_LOCATIONS,
                                "ccc9a6f2-f6dc-4236-8a9b-e122a2988bc3",
                                "An asset not restricted to a single physical location."),

    /**
     * Location of an Asset.
     */
    ASSET_LOCATION_RELATIONSHIP("bc236b62-d0e6-4c5c-93a1-3a35c3dba7b1",
                                "AssetLocation",
                                OpenMetadataWikiPages.MODEL_0025_LOCATIONS,
                                "1879264f-938b-457a-9e4a-cd8960195868",
                                "Location of an Asset."),

    /**
     * Identifies an association between an Actor Profile and a Location, such as a person's primary work location.
     */
    PROFILE_LOCATION_RELATIONSHIP("4d652ef7-99c7-4ec3-a2fd-b10c0a1ab4b4",
                                  "ProfileLocation",
                                  OpenMetadataWikiPages.MODEL_0025_LOCATIONS,
                                  "7e772ba9-56b4-4236-9512-b559d2c0ab43",
                                  "Identifies an association between an Actor Profile and a Location, such as a person's primary work location."),

    /**
     * Link between two locations to show one is nested inside another.
     */
    NESTED_LOCATION_RELATIONSHIP("f82a96c2-95a3-4223-88c0-9cbf2882b772",
                                 "NestedLocation",
                                 OpenMetadataWikiPages.MODEL_0025_LOCATIONS,
                                 "de4a6f41-55ff-4532-8810-c70c0e5253ed",
                                 "Link between two locations to show one is nested inside another."),

    /**
     * Link between two locations that are next to one another.
     */
    ADJACENT_LOCATION_RELATIONSHIP("017d0518-fc25-4e5e-985e-491d91e61e17",
                                   "AdjacentLocation",
                                   OpenMetadataWikiPages.MODEL_0025_LOCATIONS,
                                   "ec37be06-e8ed-48e6-9e94-40da18c7366b",
                                   "Link between two locations that are next to one another."),

    /**
     * Defines an endpoint associated with a server.
     */
    SERVER_ENDPOINT_RELATIONSHIP("2b8bfab4-8023-4611-9833-82a0dc95f187",
                                 "ServerEndpoint",
                                 OpenMetadataWikiPages.MODEL_0026_ENDPOINTS,
                                 "18dc6138-5745-4a66-b162-11e75d6b514e",
                                 "Defines an endpoint associated with a server."),

    /**
     * Description of the network address and related information needed to call a software service.
     */
    ENDPOINT("dbc20663-d705-4ff0-8424-80c262c6b8e7",
             "Endpoint",
             OpenMetadataWikiPages.MODEL_0026_ENDPOINTS,
             "86da5332-80cf-4bef-8881-646eb410c53c",
             "Description of the network address and related information needed to call a software service."),


    /**
     * Hardware and base software that supports an IT system.
     */
    IT_INFRASTRUCTURE("151e6dd1-54a0-4b7f-a072-85caa09d1dda",
                      "ITInfrastructure",
                      OpenMetadataWikiPages.MODEL_0030_OPERATING_PLATFORMS,
                      "6da11871-b04e-4973-beb5-4b1aac37e164",
                      "Hardware and base software that supports an IT system."),

    /**
     * Identifies the operating platform installed on the IT Infrastructure asset.
     */
    OPERATING_PLATFORM_USE("0943e0ba-73ac-476b-8ebe-2ef30ba44976",
                            "OperatingPlatformUse",
                            OpenMetadataWikiPages.MODEL_0030_OPERATING_PLATFORMS,
                            "8030885a-6639-40a6-8b02-277553ab6041",
                            "Identifies the operating platform installed on the IT Infrastructure asset."),

    /**
     * Named IT infrastructure system that supports multiple software platforms and servers.
     */
    HOST("1abd16db-5b8a-4fd9-aee5-205db3febe99",
         "Host",
         OpenMetadataWikiPages.MODEL_0035_HOSTS,
         "2f97b8f6-a136-4e30-8fc2-4fc129a4f272",
         "Named IT infrastructure system that supports multiple software platforms and servers."),

    /**
     * A group of hosts operating together to provide a scalable platform.
     */
    HOST_CLUSTER("9794f42f-4c9f-4fe6-be84-261f0a7de890",
                 "HostCluster",
                 OpenMetadataWikiPages.MODEL_0035_HOSTS,
                 "cfa602ae-5109-4ae0-8e8c-d066f8f3fe2d",
                 "A group of hosts operating together to provide a scalable platform."),

    /**
     * Identifies a host as a member of a host cluster.
     */
    HOST_CLUSTER_MEMBER_RELATIONSHIP("1a1c3933-a583-4b0c-9e42-c3691296a8e0",
                 "HostClusterMember",
                 OpenMetadataWikiPages.MODEL_0035_HOSTS,
                 "0381865a-4230-4689-ba10-7f8d511bc3b0",
                 "Identifies a host as a member of a host cluster."),

    /**
     * A computer that is hosting software directly on its operating system.
     */
    BARE_METAL_COMPUTER("8ef355d4-5cd7-4038-8337-62671b088920",
                        "BareMetalComputer",
                        OpenMetadataWikiPages.MODEL_0035_HOSTS,
                        "05e43e67-63c6-4133-af07-a19f5c5b33d6",
                        "A computer that is hosting software directly on its operating system."),

    /**
     * A virtual machine that uses a hypervisor to virtualize hardware.
     */
    VIRTUAL_MACHINE("28452091-6b27-4f40-8e31-47ce34f58387",
                    "VirtualMachine",
                    OpenMetadataWikiPages.MODEL_0035_HOSTS,
                    "d8eef565-11b0-4e0c-8700-c80641405b8d",
                    "A virtual machine that uses a hypervisor to virtualize hardware."),


    /**
     * Container-based virtual host that mimics a cut-down operating system.
     */
    VIRTUAL_CONTAINER("e2393236-100f-4ac0-a5e6-ce4e96c521e7",
                    "VirtualContainer",
                    OpenMetadataWikiPages.MODEL_0035_HOSTS,
                    "af2ae526-9f7b-4244-9bfc-819b7a902cb6",
                    "Container-based virtual host that mimics a cut-down operating system."),


    /**
     * A virtual container using the docker platform.
     */
    DOCKER_CONTAINER("9882b8aa-eba3-4a30-94c6-43117efd11cc",
                     "DockerContainer",
                     OpenMetadataWikiPages.MODEL_0035_HOSTS,
                     "85e33f77-f595-4e68-8d1f-baf16ecd230b",
                     "A virtual container using the docker platform."),

    /**
     * A cluster of nodes for big data workloads.
     */
    HADOOP_CLUSTER("abc27cf7-e526-4d1b-9c25-7dd60a7993e4",
                   "HadoopCluster",
                   OpenMetadataWikiPages.MODEL_0035_HOSTS,
                   "2851a6e4-a0a1-471d-8c73-1b666244789d",
                   "A cluster of nodes for big data workloads."),

    /**
     * A host cluster managing containerized applications.
     */
    KUBERNETES_CLUSTER("101f1c93-7f5d-44e2-9ea4-5cf21726ba5c",
                       "KubernetesCluster",
                       OpenMetadataWikiPages.MODEL_0035_HOSTS,
                       "3c0401e0-846c-4d6a-adb4-cba2d83a0390",
                       "A host cluster managing containerized applications."),

    /**
     * Characteristics of the operating system in use within a host.
     */
    OPERATING_PLATFORM("bd96a997-8d78-42f6-adf7-8239bc98501c",
                       "OperatingPlatform",
                       OpenMetadataWikiPages.MODEL_0030_OPERATING_PLATFORMS,
                       "b1dc416d-7750-4ec5-88ce-b5125ebf4497",
                       "Characteristics of the operating system in use within a host."),

    /**
     * Identifies an IT Infrastructure asset that is deployed to a specific destination.
     */
    DEPLOYED_ON("6932ba75-9522-4a06-a4a4-ee60a4df6aab",
                "DeployedOn",
                OpenMetadataWikiPages.MODEL_0042_SOFTWARE_CAPABILITIES,
                "d727b3ce-d58b-45d5-8abc-55b1394e030a",
                "Identifies an IT Infrastructure asset that is deployed to a specific destination."),

    /**
     * Software services packaged as an operating system process to support a runtime environment for a virtual software server.
     */
    SOFTWARE_SERVER_PLATFORM("ba7c7884-32ce-4991-9c41-9778f1fec6aa",
                             "SoftwareServerPlatform",
                             OpenMetadataWikiPages.MODEL_0037_SOFTWARE_SERVER_PLATFORMS,
                             "a69951e5-feab-4964-a23f-02d40a8f8938",
                             "Software services packaged as an operating system process to support a runtime environment for a virtual software server."),

    /**
     * Software services to support a runtime environment for applications and data stores.
     */
    SOFTWARE_SERVER("aa7c7884-32ce-4991-9c41-9778f1fec6aa",
                    "SoftwareServer",
                    OpenMetadataWikiPages.MODEL_0040_SOFTWARE_SERVERS,
                    "8cf6cb11-4a8d-4c1f-a246-8c8079a0319d",
                    "Software services to support a runtime environment for applications and data stores."),

    /**
     * Adds more detail about the purpose of a deployed instance of IT infrastructure.
     */
    SERVER_PURPOSE_CLASSIFICATION("78f68757-600f-4e8e-843b-00e77cdee37c",
                                  "ServerPurpose",
                                  OpenMetadataWikiPages.MODEL_0041_SERVER_PURPOSES,
                                  "403e188a-39b9-4109-92f9-2c792dc4ffdb",
                                  "Adds more detail about the purpose of a deployed instance of IT infrastructure."),

    /**
     * A server that hosts applications.
     */
    APPLICATION_SERVER_CLASSIFICATION("19196efb-2706-47bf-8e51-e8ba5b36d033",
                                      "ApplicationServer",
                                      OpenMetadataWikiPages.MODEL_0041_SERVER_PURPOSES,
                                      "d51766f2-40db-4d69-9d67-b0432c3972b0",
                                      "A server that hosts applications."),

    /**
     * A server that supports HTTP-based application such as websites and REST services.
     */
    WEBSERVER_CLASSIFICATION("d13e1cc5-bb7e-41ec-8233-9647fbf92a19",
                             "Webserver",
                             OpenMetadataWikiPages.MODEL_0041_SERVER_PURPOSES,
                             "c2f8f60c-e18c-4020-aeeb-8d7d0a5635d0",
                             "A server that supports HTTP-based application such as websites and REST services."),

    /**
     * Identifies a server as one that manages one or more databases.
     */
    DATABASE_SERVER_CLASSIFICATION("6bb58cc9-ed9e-4f75-b2f2-6d308554eb52",
                                   "DatabaseServer",
                                   OpenMetadataWikiPages.MODEL_0041_SERVER_PURPOSES,
                                   "5eb84a5c-2bfc-4c92-8941-084f3cf82ff5",
                                   "Identifies a server as one that manages one or more databases."),

    /**
     * Identifies a server that exchanges data between between other servers.
     */
    INTEGRATION_SERVER_CLASSIFICATION("c165b760-d9ab-47ac-a2ee-7854ec74605a",
                                      "IntegrationServer",
                                      OpenMetadataWikiPages.MODEL_0041_SERVER_PURPOSES,
                                      "d93a4834-6d68-43a5-926f-8c526f969e43",
                                      "Identifies a server that exchanges data between between other servers."),

    /**
     * A server hosting a metadata collection.
     */
    METADATA_SERVER_CLASSIFICATION("74a256ad-4022-4518-a446-c65fe082d4d3",
                                   "MetadataServer",
                                   OpenMetadataWikiPages.MODEL_0041_SERVER_PURPOSES,
                                   "0624e263-ca23-40e5-abe5-71cc6698421d",
                                   "A server hosting a metadata collection."),

    /**
     * A server acting as an open metadata adapter for a metadata repository.
     */
    REPOSITORY_PROXY_CLASSIFICATION("ae81c35e-7078-46f0-9b2c-afc99accf3ec",
                                    "RepositoryProxy",
                                    OpenMetadataWikiPages.MODEL_0041_SERVER_PURPOSES,
                                    "122d5b15-932c-4c27-8a36-e39ed93bd69c",
                                    "A server acting as an open metadata adapter for a metadata repository."),

    /**
     * A server dedicated to managing activity relating to governance of data.
     */
    GOVERNANCE_DAEMON_CLASSIFICATION("7815f222-529d-4902-8f0b-e37cbc779885",
                                     "GovernanceDaemon",
                                     OpenMetadataWikiPages.MODEL_0041_SERVER_PURPOSES,
                                     "2f106d43-ed20-4ef1-aa0b-e15520006640",
                                     "A server dedicated to managing activity relating to governance of data."),

    /**
     * A server dedicated to managing stewardship activity relating to governance of data.
     */
    STEWARDSHIP_SERVER_CLASSIFICATION("eaaeaa31-6f8b-4ed5-88fe-422ed3733158",
                                      "StewardshipServer",
                                      OpenMetadataWikiPages.MODEL_0041_SERVER_PURPOSES,
                                      "2f106d43-ed20-4ef1-aa0b-e15520006640",
                                      "A server dedicated to managing stewardship activity relating to governance of data."),

    /**
     * A software implemented function such as a software service or engine.
     */
    SOFTWARE_CAPABILITY("54055c38-b9ad-4a66-a75b-14dc643d4c69",
                        "SoftwareCapability",
                        OpenMetadataWikiPages.MODEL_0042_SOFTWARE_CAPABILITIES,
                        "7fb02c7c-4a41-455b-ab7e-73e0afe7789f",
                        "A software implemented function such as a software service or engine."),


    /**
     * A software capability such as an application, that is deployed to a software server.
     */
    SOFTWARE_SERVER_CAPABILITY("fe30a033-8f86-4d17-8986-e6166fa24177",
                               "SoftwareServerCapability",
                               OpenMetadataWikiPages.MODEL_0042_SOFTWARE_CAPABILITIES,
                               "a8cfffa4-a761-4fe0-be8b-6be43ac55020",
                               "A software capability such as an application, that is deployed to a software server."),

    /**
     * Identifies a software capability that is deployed to an instance of IT infrastructure.
     */
    SUPPORTED_CAPABILITY_RELATIONSHIP("2480aa71-44c5-414d-8b32-9c4340786d77",
                                      "SupportedSoftwareCapability",
                                      OpenMetadataWikiPages.MODEL_0042_SOFTWARE_CAPABILITIES,
                                      "47fc8ae9-5f6b-42e8-bb9d-c261cc9371f3",
                                      "Identifies a software capability that is deployed to an instance of IT infrastructure."),

    /**
     * Defines that a server capability is associated with an asset.
     */
    SERVER_ASSET_USE_RELATIONSHIP("56315447-88a6-4235-ba91-fead86524ebf",
                                  "ServerAssetUse",
                                  OpenMetadataWikiPages.MODEL_0045_SERVERS_AND_ASSETS,
                                  "fb79cb7f-fcec-4798-a165-ebf4649c8513",
                                  "Defines that a server capability is associated with an asset."),

    /**
     * A server capability supporting a specific business function.
     */
    APPLICATION("58280f3c-9d63-4eae-9509-3f223872fb25",
                     "Application",
                     OpenMetadataWikiPages.MODEL_0050_APPS_AND_PROCESSES,
                     "920182b0-bf3c-4558-ad4b-a23804f00221",
                     "A server capability supporting a specific business function."),

    /**
     * A capability that manages callable APIs.
     */
    API_MANAGER("283a127d-3acd-4d64-b558-1fce9db9a35b",
                "APIManager",
                OpenMetadataWikiPages.MODEL_0050_APPS_AND_PROCESSES,
                "dfbbb1d9-6c9c-4cfb-bc07-e48e07936ebf",
                "A capability that manages callable APIs."),

    /**
     * A capability that supports REST APIs in a server.
     */
    REST_API_MANAGER("cb337e45-929d-48a2-89c7-a25b8de578c6",
                     "RESTAPIManager",
                     OpenMetadataWikiPages.MODEL_0050_APPS_AND_PROCESSES,
                     "86f6504a-f7e1-441e-9143-38d67c52ff40",
                     "A capability that supports REST APIs in a server."),

    /**
     * A capability that supports the authorization of requests.
     */
    AUTHORIZATION_MANAGER("f862880f-222d-4827-8412-9013cd1f85c2",
                          "AuthorizationManager",
                          OpenMetadataWikiPages.MODEL_0050_APPS_AND_PROCESSES,
                          "6a33c5e9-39d7-4f0d-a81a-92faeda756d6",
                          "A capability that supports the authorization of requests."),

    /**
     * A capability that supports the identification/authentication of users.
     */
    USER_AUTHENTICATION_MANAGER("2de822e6-dad2-4df0-b5d8-13f82ffc33c1",
                          "UserAuthenticationManager",
                          OpenMetadataWikiPages.MODEL_0050_APPS_AND_PROCESSES,
                          "4a8d9d89-7936-4ae8-ae1d-5d327e0e7cad",
                          "A capability that supports the identification/authentication of users."),

    /**
     * A capability that ends and/or receives events as part of its software function.
     */
    EVENT_MANAGER("98383304-ca78-492a-b0a2-1bd46d690ed3",
                           "EventManager",
                           OpenMetadataWikiPages.MODEL_0050_APPS_AND_PROCESSES,
                           "1d4324e9-0696-4bab-bbf8-6f4c7c0643d4",
                           "A capability that ends and/or receives events as part of its software function."),

    /**
     * A capability that supports event-based services, typically around topics.
     */
    EVENT_BROKER("309dfc3c-663b-4732-957b-e4a084436314",
                          "EventBroker",
                          OpenMetadataWikiPages.MODEL_0050_APPS_AND_PROCESSES,
                          "5cde95af-6bb8-4c83-9178-b05a995e32cb",
                          "A capability that supports event-based services, typically around topics."),

    /**
     * A capability that manages collections of stored data.
     */
    DATA_MANAGER("82efa1fa-501f-4ac7-942c-6536c4a1cd61",
                 "DataManager",
                 OpenMetadataWikiPages.MODEL_0050_APPS_AND_PROCESSES,
                 "b2151b29-e115-417f-a711-521a9d66f99b",
                 "A capability that manages collections of stored data.  It typically maintains a schema to describe how data is formatted and that schema is used in the query and maintenance APIs by the caller to work with the data they desire."),

    /**
     * Defines a capability that manages data organized as relational schemas.
     */
    DATABASE_MANAGER("68b35c1e-6c28-4ac3-94f9-2c3dbcbb79e9",
                     "DatabaseManager",
                     OpenMetadataWikiPages.MODEL_0050_APPS_AND_PROCESSES,
                     "b41f74bf-a633-49c4-9fda-3d4d5f82b066",
                     "Defines a capability that manages data organized as relational schemas.  It is also responsible for managing the data including maintaining query indexes, statistics and backups."),

    /**
     * Defines a capability that manages data organized as relational schemas.
     */
    DATA_ACCESS_MANAGER("c7e4008e-779e-4586-85f1-ab6264eb54e9",
                        "DataAccessManager",
                        OpenMetadataWikiPages.MODEL_0050_APPS_AND_PROCESSES,
                        "d54ad0a0-5254-4bf6-903c-82c1337eb209",
                        "Defines a capability that manages data organized as structured schemas (typically relational tables and columns).  It does this by linking the schemas to data sources that are external to its own storage."),

    /**
     * A capability that manages collections of descriptions about people, places, digital assets, things, ...
     */
    INVENTORY_CATALOG("f4fffcc0-d9eb-4bb9-8aff-0718932f689e",
                      "InventoryCatalog",
                      OpenMetadataWikiPages.MODEL_0050_APPS_AND_PROCESSES,
                      "df539ee4-fb5a-4555-8697-c391e244557d",
                      "A capability that manages collections of descriptions about people, places, digital assets, things, ..."),

    /**
     * A programmable engine for running automated processes.
     */
    ENGINE("3566527f-b1bd-4e7a-873e-a3e04d5f2a14",
            "Engine",
            OpenMetadataWikiPages.MODEL_0055_DATA_PROCESSING_ENGINES,
            "8742ae9e-6ce4-48f1-baaf-8c7cae123092",
            "A programmable engine for running automated processes."),


    /**
     * An engine capable of running a mixture of human and automated tasks as part of a workflow process.
     */
    WORKFLOW_ENGINE_CLASSIFICATION("37a6d212-7c4a-4a82-b4e2-601d4358381c",
                                   "WorkflowEngine",
                                   OpenMetadataWikiPages.MODEL_0055_DATA_PROCESSING_ENGINES,
                                   "c980f0ad-c7f1-492b-96c3-ea85327f56de",
                                   "An engine capable of running a mixture of human and automated tasks as part of a workflow process."),

    /**
     * An engine capable of creating reports by combining information from multiple data sets.
     */
    REPORTING_ENGINE_CLASSIFICATION("e07eefaa-16e0-46cf-ad54-bed47fb15812",
                                    "ReportingEngine",
                                    OpenMetadataWikiPages.MODEL_0055_DATA_PROCESSING_ENGINES,
                                    "04051763-2945-47c0-856c-dbe9278d7cb9",
                                    "An engine capable of creating reports by combining information from multiple data sets."),

    /**
     * An engine capable of running analytics models using data from one or more data sets.
     */
    ANALYTICS_ENGINE("1a0dc6f6-7980-42f5-98bd-51e56543a07e",
                     "AnalyticsEngine",
                     OpenMetadataWikiPages.MODEL_0055_DATA_PROCESSING_ENGINES,
                     "a1db5c5b-c7c9-41b1-9b37-7f1f3eb40dea",
                     "An engine capable of running analytics models using data from one or more data sets."),

    /**
     * An engine capable of copying data from one data store to another.
     */
    DATA_MOVEMENT_ENGINE("d2ed6621-9d99-4fe8-843a-b28d816cf888",
                         "DataMovementEngine",
                         OpenMetadataWikiPages.MODEL_0055_DATA_PROCESSING_ENGINES,
                         "b4df7850-45a5-4ba0-8216-3828480dc2c1",
                         "An engine capable of copying data from one data store to another."),

    /**
     * An engine capable of creating new data sets by dynamically combining data from one or more data stores or data sets.
     */
    DATA_VIRTUALIZATION_ENGINE("03e25cd0-03d7-4d96-b28b-eed671824ed6",
                               "DataVirtualizationEngine",
                               OpenMetadataWikiPages.MODEL_0055_DATA_PROCESSING_ENGINES,
                               "faa47e2b-4987-4ad8-bc39-5a4f91f9658c",
                               "An engine capable of creating new data sets by dynamically combining data from one or more data stores or data sets."),

    /**
     * Defines a capability that manages metadata about assets.
     */
    ASSET_MANAGER("03170ce7-edf1-4e94-b6ab-2d5cbbf1f13c",
                  "AssetManager",
                  OpenMetadataWikiPages.MODEL_0056_RESOURCE_MANAGERS,
                  "44a1d90a-689e-4dc1-8dbb-cf3107388a1b",
                  "Defines a capability that manages metadata about assets."),

    /**
     * A capability that supports a store of files organized into a hierarchy of file folders.
     */
    FILE_SYSTEM_CLASSIFICATION("cab5ba1d-cfd3-4fca-857d-c07711fc4157",
                               "FileSystem",
                               OpenMetadataWikiPages.MODEL_0056_RESOURCE_MANAGERS,
                               "5a2077a7-6ff3-46b0-8799-93082e8a43b3",
                               "A capability that supports a store of files organized into a hierarchy of file folders."),

    /**
     * Identifies a software server capability as a manager of a collection of files and folders.
     */
    FILE_MANAGER_CLASSIFICATION("eadec807-02f0-4d6f-911c-261eddd0c2f5",
                               "FileManager",
                               OpenMetadataWikiPages.MODEL_0056_RESOURCE_MANAGERS,
                               "f126a519-bbae-4b2a-9e88-0c9ebf4494a9",
                               "Identifies a software server capability as a manager of a collection of files and folders."),

    /**
     * A system that stores descriptions of individuals and their roles/interests in an organization.
     */
    USER_PROFILE_MANAGER("53ef4062-9e0a-4892-9824-8d51d4ad59d3",
                         "UserProfileManager",
                         OpenMetadataWikiPages.MODEL_0056_RESOURCE_MANAGERS,
                         "c62b9be8-c01e-4750-9ef6-4687077614a2",
                         "A system that stores descriptions of individuals and their roles/interests in an organization."),

    /**
     * A system that stores the access rights and groups for users (people and automated processes).
     */
    USER_ACCESS_DIRECTORY("29c98cf7-32b3-47d2-a411-48c1c9967e6d",
                          "UserAccessDirectory",
                          OpenMetadataWikiPages.MODEL_0056_RESOURCE_MANAGERS,
                          "e8f1cd26-31db-4663-9cd6-67e9e6a4ac19",
                          "A system that stores the access rights and groups for users (people and automated processes)."),

    /**
     * A system that manages the consolidation and reconciliation of master data - typically people, organizations, products and accounts.
     */
    MASTER_DATA_MANAGER("5bdad12e-57e7-4ff9-b7be-5d869e77d30b",
                        "MasterDataManager",
                        OpenMetadataWikiPages.MODEL_0056_RESOURCE_MANAGERS,
                        "5e9d07f7-977c-4f60-a3c1-1e1107db6df1",
                        "A system that manages the consolidation and reconciliation of master data - typically people, organizations, products and accounts."),

    /**
     * Identifies a server capability that is distributing events from a topic to its subscriber list.
     */
    NOTIFICATION_MANAGER("3e7502a7-396a-4737-a106-378c9c94c105",
                        "NotificationManager",
                        OpenMetadataWikiPages.MODEL_0056_RESOURCE_MANAGERS,
                        "6a6ce1e2-7d09-47c7-a996-6ac839ca4ae8",
                        "Identifies a server capability that is distributing events from a topic to its subscriber list."),

    /**
     * Identifies a software capability as a manager of controlled documents and related media.
     */
    CONTENT_COLLECTION_MANAGER("dbde6a5b-fc89-4b04-969a-9dc09a60ebd7",
                               "ContentCollectionManager",
                               OpenMetadataWikiPages.MODEL_0056_RESOURCE_MANAGERS,
                               "8cb201bf-4213-48cd-bf13-412b1a14e1fc",
                               "Identifies a software capability as a manager of controlled documents and related media."),

    /**
     * Defines a capability that provides externally callable functions to other services.
     */
    SOFTWARE_SERVICE("f3f69251-adb1-4042-9d95-70082f95a028",
                     "SoftwareService",
                     OpenMetadataWikiPages.MODEL_0057_SOFTWARE_SERVICES,
                     "b3c83676-88a3-49a5-84d0-15c735dfcd91",
                     "Defines a capability that provides externally callable functions to other services."),

    /**
     * Repository services for the Open Metadata Access Services (OMAS) supporting federated queries and aggregated events from the connected cohorts.
     */
    ENTERPRISE_ACCESS_LAYER("39444bf9-638e-4124-a5f9-1b8f3e1b008b",
                            "EnterpriseAccessLayer",
                            OpenMetadataWikiPages.MODEL_0057_SOFTWARE_SERVICES,
                            "c1dfc168-f8cb-4cd5-b6fe-9d2dcbab638c",
                            "Repository services for the Open Metadata Access Services (OMAS) supporting federated queries and aggregated events from the connected cohorts."),

    /**
     * A capability enabling a server to access an open metadata repository cohort.
     */
    COHORT_MEMBER("42063797-a78a-4720-9353-52026c75f667",
                  "CohortMember",
                  OpenMetadataWikiPages.MODEL_0057_SOFTWARE_SERVICES,
                  "02a99f36-8a58-4c40-834e-47df456bae31",
                  "A capability enabling a server to access an open metadata repository cohort."),

    /**
     * A group of collaborating open metadata repositories.
     */
    METADATA_REPOSITORY_COHORT("43e7dca2-c7b4-4cdf-a1ea-c9d4f7093893",
                               "MetadataRepositoryCohort",
                               OpenMetadataWikiPages.MODEL_0057_SOFTWARE_SERVICES,
                               "d14c995b-95f0-4ab1-99d7-5cc1b417123d",
                               "A group of collaborating open metadata repositories."),

    /**
     * A metadata repository's registration with an open metadata cohort.
     */
    METADATA_COHORT_PEER_RELATIONSHIP("954cdba1-3d69-4db1-bf0e-d59fd2c25a27",
                                      "MetadataCohortPeer",
                                      OpenMetadataWikiPages.MODEL_0057_SOFTWARE_SERVICES,
                                      "e962cc4c-d673-46c7-b17d-8fa009cebd85",
                                      "A metadata repository's registration with an open metadata cohort."),

    /**
     * Defines a capability that exchanges metadata between servers.
     */
    METADATA_INTEGRATION_SERVICE("92f7fe27-cd2f-441c-a084-156821aa5bca",
                                 "MetadataIntegrationService",
                                 OpenMetadataWikiPages.MODEL_0057_SOFTWARE_SERVICES,
                                 "1dbc4ce0-d3ed-4246-a53e-470cb49a12ba",
                                 "Defines a capability that exchanges metadata between servers."),

    /**
     * Defines a capability that provides access to stored metadata.
     */
    METADATA_ACCESS_SERVICE("0bc3a16a-e8ed-4ad0-a302-0773365fdef0",
                            "MetadataAccessService",
                            OpenMetadataWikiPages.MODEL_0057_SOFTWARE_SERVICES,
                            "4fd9838d-8ad1-4f25-a573-4320c54a8e85",
                            "Defines a capability that provides access to stored metadata."),

    /**
     * Defines a capability that provides services that delegate to a hosted engine.
     */
    ENGINE_HOSTING_SERVICES("90880f0b-c7a3-4d1d-93cc-0b877f27cd33",
                            "EngineHostingService",
                            OpenMetadataWikiPages.MODEL_0057_SOFTWARE_SERVICES,
                            "61d24feb-bffc-4429-ac6d-098d1d3ac961",
                            "Defines a capability that provides services that delegate to a hosted engine."),

    /**
     * Defines a capability that provides user interfaces access to digital resources.
     */
    USER_VIEW_SERVICE("1f83fc7c-75bb-491d-980d-ff9a6f80ae02",
                      "UserViewService",
                      OpenMetadataWikiPages.MODEL_0057_SOFTWARE_SERVICES,
                      "3aa18b40-aa95-4bd0-94bd-25280ee611b3",
                      "Defines a capability that provides user interfaces access to digital resources."),

    /**
     * Provides access to a metadata repository - either local or remote.
     */
    METADATA_REPOSITORY_SERVICE("27891e52-1255-4a33-98a2-377717a25334",
                                "MetadataRepositoryService",
                                OpenMetadataWikiPages.MODEL_0057_SOFTWARE_SERVICES,
                                "0457d94e-f6f9-4169-b1fc-4ff081cec5d0",
                                "Provides access to a metadata repository - either local or remote."),

    /**
     * Provides security services - classifications identify specific capabilities.
     */
    SECURITY_SERVICE("2df2069f-6475-400c-bf8c-6d2072a55d47",
                     "SecurityService",
                     OpenMetadataWikiPages.MODEL_0057_SOFTWARE_SERVICES,
                     "c1294981-bfac-421f-8649-73e10ca87bec",
                     "Provides security services - classifications identify specific capabilities."),

    /**
     * Interconnectivity for systems.
     */
    NETWORK("e0430f59-f021-411a-9d81-883e1ff3f6f6",
            "Network",
            OpenMetadataWikiPages.MODEL_0070_NETWORKS_AND_GATEWAYS,
            "48cecd57-b223-4d23-af15-0efe65f74639",
            "Interconnectivity for systems."),

    /**
     * A connection point enabling network traffic to pass between two networks.
     */
    NETWORK_GATEWAY("9bbae94d-e109-4c96-b072-4f97123f04fd",
                    "NetworkGateway",
                    OpenMetadataWikiPages.MODEL_0070_NETWORKS_AND_GATEWAYS,
                    "7e665e17-f0bc-4c20-95e3-be1fec37bc52",
                    "A connection point enabling network traffic to pass between two networks."),

    /**
     * f2bd7401-c064-41ac-862c-e5bcdc98fa1e
     */
   HOST_NETWORK("f2bd7401-c064-41ac-862c-e5bcdc98fa1e",
                "HostNetwork",
                OpenMetadataWikiPages.MODEL_0070_NETWORKS_AND_GATEWAYS,
                "575f2f35-a81d-4fdc-94d8-6a043bc9631d",
                "One of the hosts connected to a network."),

    /**
     * Link from a network to one of its network gateways.
     */
    NETWORK_GATEWAY_LINK_RELATIONSHIP("5bece460-1fa6-41fb-a29f-fdaf65ec8ce3",
                                      "NetworkGatewayLink",
                                      OpenMetadataWikiPages.MODEL_0070_NETWORKS_AND_GATEWAYS,
                                      "c8f17969-ee97-41cf-b5dc-80353a864220",
                                      "Link from a network to one of its network gateways."),

    /**
     * Shows that network that an endpoint is visible through.
     */
    VISIBLE_ENDPOINT("5e1722c7-0167-49a0-bd77-fbf9dc5eb5bb",
                     "VisibleEndpoint",
                     OpenMetadataWikiPages.MODEL_0070_NETWORKS_AND_GATEWAYS,
                     "fc2a05a1-69bd-437f-bef7-20940a97f052",
                     "Shows that network that an endpoint is visible through."),

    /**
     * A host supporting cloud services.
     */
    CLOUD_PROVIDER_CLASSIFICATION("a2bfdd08-d0a8-49db-bc97-7f240628104",
                                  "CloudProvider",
                                  OpenMetadataWikiPages.MODEL_0090_CLOUD_PLATFORMS,
                                  "0ef5ffaa-6497-40be-8781-5e7e9d74e890",
                                  "A host supporting cloud services."),


    /**
     * A software server platform supporting cloud services.
     */
    CLOUD_PLATFORM_CLASSIFICATION("1b8f8511-e606-4f65-86d3-84891706ad12",
                                  "CloudPlatform",
                                  OpenMetadataWikiPages.MODEL_0090_CLOUD_PLATFORMS,
                                  "4cf8cae4-e9e1-4ab2-8aeb-ffb393a1c7ff",
                                  "A software server platform supporting cloud services."),

    /**
     * A software server supporting cloud services.
     */
    CLOUD_TENANT_CLASSIFICATION("1b8f8522-e606-4f65-86d3-84891706ad12",
                                "CloudTenant",
                                OpenMetadataWikiPages.MODEL_0090_CLOUD_PLATFORMS,
                                "5516ce5f-df5c-485d-90ca-7ab5647e2371",
                                "A software server supporting cloud services."),


    /**
     * A service running on a cloud platform.
     */
    CLOUD_SERVICE_CLASSIFICATION("337e7b1a-ad4b-4818-aa3e-0ff3307b2fbe6",
                                 "CloudService",
                                 OpenMetadataWikiPages.MODEL_0090_CLOUD_PLATFORMS,
                                 "78ad50d9-8d7a-4dc3-ac87-697614d7c417",
                                 "A service running on a cloud platform."),


    /* ============================================================================================================================*/
    /* Area 1 - Collaboration                                                                                                      */
    /* ============================================================================================================================*/

    /**
     * The representation of a person or group of people that are identified to perform an action or take on a responsibility.
     */
    ACTOR("16d2c34a-43db-476b-93ae-6a2996f514ec",
          "Actor",
          OpenMetadataWikiPages.MODEL_0110_ACTORS,
          "93583bce-636d-4981-90db-729aafa6a76f",
          "The representation of a person or group of people that are identified to perform an action or take on a responsibility."),


    /**
     * Description of a person, team or automated process that is working with data.
     */
    ACTOR_PROFILE("5a2f38dc-d69d-4a6f-ad26-ac86f118fa35",
                  "ActorProfile",
                  OpenMetadataWikiPages.MODEL_0110_ACTORS,
                  "5669141f-816d-45b4-899d-379674f0bcf3",
                  "Description of a person, team or automated process that is working with data."),

    /**
     * Name of the security account for a person or automated process.
     */
    USER_IDENTITY("fbe95779-1f3c-4ac6-aa9d-24963ff16282",
                  "UserIdentity",
                  OpenMetadataWikiPages.MODEL_0110_ACTORS,
                  "d1313986-d512-49bf-bc1c-a3a529316978",
                  "Name of the security account for a person or automated process."),

    /**
     * Correlates a user identity with an actor profile.
     */
    PROFILE_IDENTITY_RELATIONSHIP("01664609-e777-4079-b543-6baffe910ff1",
                                  "ProfileIdentity",
                                  OpenMetadataWikiPages.MODEL_0110_ACTORS,
                                  "f8ef0a3c-aa8d-4de3-8baf-2577365afa67",
                                  "Correlates a user identity with an actor profile."),


    /**
     * Information on how to send a message to an individual or automated process.
     */
    CONTACT_DETAILS("79296df8-645a-4ef7-a011-912d1cdcf75a",
                    "ContactDetails",
                    OpenMetadataWikiPages.MODEL_0110_ACTORS,
                    "d3966e27-bed1-47df-830a-99b1e4d056d9",
                    "Information on how to send a message to an individual or automated process."),

    /**
     * The contact details associated with an actor profile.
     */
    CONTACT_THROUGH_RELATIONSHIP("6cb9af43-184e-4dfa-854a-1572bcf0fe75",
                                 "ContactThrough",
                                 OpenMetadataWikiPages.MODEL_0110_ACTORS,
                                 "e192b0fc-f483-4555-a09d-13f71c3829db",
                                 "he contact details associated with an actor profile."),

    /**
     * An individual.
     */
    PERSON("ac406bf8-e53e-49f1-9088-2af28bbbd285",
           "Person",
           OpenMetadataWikiPages.MODEL_0112_PEOPLE,
           "dc55b455-b2ea-4065-a972-6bbdc52ca688",
           "An individual."),


    /**
     * Relationship identifying a person's contribution record (replaced by Contribution).
     */
   PERSONAL_CONTRIBUTION_RELATIONSHIP("4a316abe-eeee-4d11-ad5a-4bfb4079b80b",
                                      "PersonalContribution",
                                      OpenMetadataWikiPages.MODEL_0125_CONTRIBUTION,
                                      "73d21888-20f0-485f-b88e-36617b51b0a8",
                                      "Deprecated Relationship identifying a person's contribution record (replaced by Contribution)."),


    /**
     * Relationship identifying an actor's contribution record.
     */
    CONTRIBUTION_RELATIONSHIP("4a383961-fa82-45b7-9018-d02233c80754",
                                       "Contribution",
                                       OpenMetadataWikiPages.MODEL_0125_CONTRIBUTION,
                                       "47acc23d-a5d3-4ce3-acbd-0bb50433037b",
                                       "Relationship identifying an actor's contribution record."),


    /**
     * A record of the contribution of an actor profile.
     */
    CONTRIBUTION_RECORD("6aaa1cea-d55f-4dcc-8c28-63a44132d1d7",
                        "ContributionRecord",
                        OpenMetadataWikiPages.MODEL_0125_CONTRIBUTION,
                        "84ac1aa6-f82c-4723-9c57-2b064d3c8418",
                        "A record of the contribution of an actor profile."),


    /**
     * Relationship identifying a person's peer network.
     */
    PEER_RELATIONSHIP("4a316abe-bccd-4d11-ad5a-4bfb4079b80b",
                      "Peer",
                      OpenMetadataWikiPages.MODEL_0112_PEOPLE,
                      "1d21a0e8-a80b-455f-9e5e-475026a6b8f4",
                      "Relationship identifying a person's peer network."),

    /**
     * Group of people working together.
     */
    TEAM("36db26d5-aba2-439b-bc15-d62d373c5db6",
         "Team",
         OpenMetadataWikiPages.MODEL_0115_TEAMS,
         "1e008894-4221-4409-838b-e78125da060b",
         "Group of people working together."),

    /**
     * Relationship identifying a team reporting hierarchy.
     */
    TEAM_STRUCTURE_RELATIONSHIP("5ebc4fb2-b62a-4269-8f18-e9237a2229ca",
                                "TeamStructure",
                                OpenMetadataWikiPages.MODEL_0115_TEAMS,
                                "51a4a318-d9d9-4833-a55b-b3f7948a1a37",
                                "Relationship identifying a team reporting hierarchy."),


    /**
     * Descriptive details about a processing engine or other IT infrastructure.
     */
    IT_PROFILE("81394f85-6008-465b-926e-b3fae4668937",
               "ITProfile",
               OpenMetadataWikiPages.MODEL_0117_IT_PROFILES,
               "c4fcd82e-24c8-40ef-8d80-e787841a6f92",
               "Descriptive details about a processing engine or other IT infrastructure."),

    /**
     * Link between an ITProfile and the asset for the piece of infrastructure it describes.
     */
    IT_INFRASTRUCTURE_PROFILE_RELATIONSHIP("4c579e3d-a4ff-41c1-9931-33e6fc992f2b",
                                           "ITInfrastructureProfile",
                                           OpenMetadataWikiPages.MODEL_0117_IT_PROFILES,
                                           "f52f4e19-f148-495b-84df-8ec2f2b02263",
                                           "Link between an ITProfile and the asset for the piece of infrastructure it describes."),


    /**
     * A collection of related responsibilities.
     */
    ACTOR_ROLE("8f87b861-4d11-43ab-8212-c6fc0a0caccd",
               "ActorRole",
               OpenMetadataWikiPages.MODEL_0118_ACTOR_ROLES,
               "5919c473-3769-4637-a638-45ef17bd0585",
               "A collection of related responsibilities."),


    /**
     * A role performed by one or more individuals.
     */
    PERSON_ROLE("ac406bf8-e53e-49f1-9088-2af28bcbd285",
                "PersonRole",
                OpenMetadataWikiPages.MODEL_0118_ACTOR_ROLES,
                "6259c839-f310-4b73-87dc-28e60a119e1a",
                "A role performed by one or more individuals."),


    /**
     * Relationship identifying a person's roles.
     */
    PERSON_ROLE_APPOINTMENT_RELATIONSHIP("4a316abe-bcce-4d11-ad5a-4bfb4079b80b",
                                         "PersonRoleAppointment",
                                         OpenMetadataWikiPages.MODEL_0118_ACTOR_ROLES,
                                         "41282fe0-3d45-403f-97bc-c9c80c5081de",
                                         "Relationship identifying a person's roles."),


    /**
     * A role performed by a team.
     */
    TEAM_ROLE("2dfe3f58-ff31-4933-a0d3-cf8c242400f2",
                "TeamRole",
                OpenMetadataWikiPages.MODEL_0118_ACTOR_ROLES,
                "455fdae7-3f36-4adb-a402-af0ddaa59c25",
                "A role performed by a team."),


    /**
     * Relationship identifying a team's roles.
     */
    TEAM_ROLE_APPOINTMENT_RELATIONSHIP("2c474468-e5f8-48bf-8140-854d516af3f3",
                                         "TeamRoleAppointment",
                                         OpenMetadataWikiPages.MODEL_0118_ACTOR_ROLES,
                                         "e8b9570f-2eba-4a97-87a0-cb5300a65a62",
                                         "Relationship identifying a team's roles."),


    /**
     * A role performed by some software automation.
     */
    IT_PROFILE_ROLE("3fe208fc-cff4-4fcf-8e74-b12267b154bb",
                "ITProfileRole",
                OpenMetadataWikiPages.MODEL_0118_ACTOR_ROLES,
                "9cd02fe0-4926-401f-859d-0ccddd444510",
                "A role performed by some software automation."),


    /**
     * Relationship identifying a software automation executable's roles.
     */
    IT_PROFILE_ROLE_APPOINTMENT_RELATIONSHIP("ebf49c54-319d-4c5e-80e4-d3084d4ff9f1",
                                         "ITProfileRoleAppointment",
                                         OpenMetadataWikiPages.MODEL_0118_ACTOR_ROLES,
                                         "6166a902-bd0d-42de-821a-f062f69b6e61",
                                         "Relationship identifying a software automation executable's roles."),


    /**
     * Person assigned to a team.
     */
    TEAM_MEMBER("46db26d5-abb2-538b-bc15-d62d373c5db6",
                "TeamMember",
                OpenMetadataWikiPages.MODEL_0119_TEAM_ROLES,
                "69b06feb-3880-4796-ae15-f85fcce35900",
                "Person assigned to a team."),

    /**
     * Relationship identifying the members of teams.
     */
    TEAM_MEMBERSHIP_RELATIONSHIP("1ebc4fb2-b62a-4269-8f18-e9237a2119ca",
                                 "TeamMembership",
                                 OpenMetadataWikiPages.MODEL_0119_TEAM_ROLES,
                                 "623667d6-2d09-4e2c-8078-bbfd7f8bdbcc",
                                 "Relationship identifying the members of teams."),

    /**
     * Person leading a team.
     */
    TEAM_LEADER("36db26d5-abb2-439b-bc15-d62d373c5db6",
                "TeamLeader",
                OpenMetadataWikiPages.MODEL_0119_TEAM_ROLES,
                "021c3a71-c912-49e1-a099-1866746e4825",
                "Person leading a team."),

    /**
     * Relationship identifying the leaders of teams.
     */
    TEAM_LEADERSHIP_RELATIONSHIP("5ebc4fb2-b62a-4269-8f18-e9237a2119ca",
                                 "TeamLeadership",
                                 OpenMetadataWikiPages.MODEL_0119_TEAM_ROLES,
                                 "83e54789-e20e-439c-99af-e5dcb21e3b29",
                                 "Relationship identifying the leaders of teams."),

    /**
     * Links a profile, role or project to the elements that they are responsible for managing.
     */
    ASSIGNMENT_SCOPE_RELATIONSHIP("e3fdafe3-692a-46c6-a595-c538cc189dd9",
                                  "AssignmentScope",
                                  OpenMetadataWikiPages.MODEL_0120_ASSIGNMENT_SCOPES,
                                  "d2f5cbd5-36f7-4471-83fb-b1aec78d39df",
                                  "Links a profile, role or project to the elements that they are responsible for managing."),


    /**
     * Identifies the Actor that commissioned work (such as a project or a community) or a capability, service or assets.
     */
   STAKEHOLDER_RELATIONSHIP("efd8a136-0aea-4668-b91a-30f947e38b82",
                            "Stakeholder",
                            OpenMetadataWikiPages.MODEL_0120_ASSIGNMENT_SCOPES,
                            "97aa5776-e978-4684-b014-5be35c497ffe",
                            "Identifies the Actor that commissioned work (such as a project or a community) or a capability, service or assets."),

    /**
     * An organized activity, typically to achieve a well-defined goal.
     */
    PROJECT("0799569f-0c16-4a1f-86d9-e2e89568f7fd",
            "Project",
            OpenMetadataWikiPages.MODEL_0130_PROJECTS,
            "1d8eed39-17e2-400c-adac-4d0c8f3063ad",
            "An organized activity, typically to achieve a well-defined goal."),

    /**
     * A person with overall responsibility for one or more project.
     */
    PROJECT_MANAGER("0798569f-0c16-4a1f-86d9-e2e89568f7fd",
                    "ProjectManager",
                    OpenMetadataWikiPages.MODEL_0130_PROJECTS,
                    "fbc22a33-8a07-45a3-afdf-839717be9f05",
                    "A person with overall responsibility for one or more projects."),

    /**
     * The link between a project and its project manager role.
     */
    PROJECT_MANAGEMENT_RELATIONSHIP("ac63ac45-a4d0-4fba-b583-92859de77dd8",
                                    "ProjectManagement",
                                    OpenMetadataWikiPages.MODEL_0130_PROJECTS,
                                    "8f09b512-1f13-417d-99ed-beb628e47e39",
                                    "The link between a project and its project manager role."),

    /**
     * The actors assigned to a project.
     */
    PROJECT_TEAM_RELATIONSHIP("746875af-2e41-4d1f-864b-35265df1d5dc",
                              "ProjectTeam",
                              OpenMetadataWikiPages.MODEL_0130_PROJECTS,
                              "fdae8a21-8713-466c-9446-bf6b482ec103",
                              "The actors assigned to a project."),

    /**
     * A nesting relationship between projects.
     */
    PROJECT_HIERARCHY_RELATIONSHIP("8f1134f6-b9fe-4971-bc57-6e1b8b302b55",
                                   "ProjectHierarchy",
                                   OpenMetadataWikiPages.MODEL_0130_PROJECTS,
                                   "d061fb2c-0315-4dcc-9f49-5dcf22f562f3",
                                   "A nesting relationship between projects."),

    /**
     * A dependency relationship between projects.
     */
    PROJECT_DEPENDENCY_RELATIONSHIP("5b6a56f1-68e2-4e10-85f0-fda47a4263fd",
                                    "ProjectDependency",
                                    OpenMetadataWikiPages.MODEL_0130_PROJECTS,
                                    "ee7973dc-022b-4d49-bb5d-5231ad64630b",
                                    "A dependency relationship between projects."),

    /**
     * A long-term strategic initiative that is implemented through multiple related projects.
     */
    CAMPAIGN_CLASSIFICATION("41437629-8609-49ef-8930-8c435c912572",
                            "Campaign",
                            OpenMetadataWikiPages.MODEL_0130_PROJECTS,
                            "5414bc9b-ff73-4eeb-a8a7-3cbb5beae166",
                            "A long-term strategic initiative that is implemented through multiple related projects."),

    /**
     * A self-contained, short activity, typically for one or two people.
     */
    TASK_CLASSIFICATION("2312b668-3670-4845-a140-ef88d5a6db0c",
                        "Task",
                        OpenMetadataWikiPages.MODEL_0130_PROJECTS,
                        "0b00b39b-4079-475e-b598-f31003078831",
                        "A self-contained, short activity, typically for one or two people."),

    /**
     * This is an informal project that has been created by an individual to help them organize their work.
     */
    PERSONAL_PROJECT_CLASSIFICATION("3d7b8500-cebd-4f18-b85c-a459bec3e3ef",
                                    "PersonalProject",
                                    OpenMetadataWikiPages.MODEL_0130_PROJECTS,
                                    "da65ba96-b9d8-45e7-b16f-24342b6695c0",
                                    "This is an informal project that has been created by an individual to help them organize their work."),

    /**
     * A focused analysis of a topic, person, object or situation.
     */
    STUDY_PROJECT_CLASSIFICATION("e68ae56a-7567-4c6a-9bff-04076bcc0b3b",
                                 "StudyProject",
                                 OpenMetadataWikiPages.MODEL_0130_PROJECTS,
                                 "04b9427e-b6cc-45d4-b880-6eaa9a47f063",
                                 "A focused analysis of a topic, person, object or situation."),

    /**
     * Two or more people come together to discuss a topic, agree and action or exchange information.
     */
    MEETING("6bf90c79-32f4-47ad-959c-8fff723fe744",
            "Meeting",
            OpenMetadataWikiPages.MODEL_0135_MEETINGS,
            "dd75c9f3-8e17-4307-9729-c391bd7981bd",
            "Two or more people come together to discuss a topic, agree and action or exchange information."),

    /**
     * A meeting about a specific project, deliverable, situation or plan of action.
     */
    MEETINGS("a05f918e-e7e2-419d-8016-5b37406df63a",
             "Meetings",
             OpenMetadataWikiPages.MODEL_0135_MEETINGS,
             "02ff69de-bb0d-40ad-a7ec-367938b7767b",
             "A meeting about a specific project, deliverable, situation or plan of action."),


    /**
     * An action that has been identified to support the development, improvement, or remedy of an object or situation.
     */
    ACTION("95261f26-8fe0-4723-b953-4ae5789ec639",
           "Action",
           OpenMetadataWikiPages.MODEL_0137_ACTIONS_FOR_PEOPLE,
           "d1df6cb0-80af-473f-aacf-f29972fd4c34",
           "An action that has been identified to support the development, improvement, or remedy of an object or situation."),


    /**
     * The source of the to do, such as a person, meeting or a governance action.
     */
    TO_DO_SOURCE_RELATIONSHIP("a0b7ba50-4c97-4b76-9a7d-c6a00e1be646",
                              "ToDoSource",
                              OpenMetadataWikiPages.MODEL_0137_ACTIONS_FOR_PEOPLE,
                              "4b7c5d49-cef0-495e-8710-23c293828575",
                              "The source of the to do, such as a person, meeting or a governance action."),


    /**
     * Identifies the sponsor that requires the action (To Do) to be completed.
     */
    ACTION_SPONSOR_RELATIONSHIP("aca1277b-bf1c-42f5-9b3b-fbc2c9047325",
                                "ActionSponsor",
                                OpenMetadataWikiPages.MODEL_0137_ACTIONS_FOR_PEOPLE,
                                "c3d68cbc-9e1e-4820-8647-cdf29a64fb2b",
                                "Identifies the sponsor that requires the action (ToDo) to be completed."),

    /**
     * A person who has been assigned to complete the to do (action).
     */
    ACTION_ASSIGNMENT_RELATIONSHIP("af2b5fab-8f83-4a2b-b749-1e6219f61f79",
                                   "ActionAssignment",
                                   OpenMetadataWikiPages.MODEL_0137_ACTIONS_FOR_PEOPLE,
                                   "5d77cc38-d0fa-410b-85fd-e077a6f9efb1",
                                   "A person who has been assigned to complete the to do (action)."),


    /**
     * An action assigned to an individual.
     */
    TO_DO("93dbc58d-c826-4bc2-b36f-195148d46f86",
          "ToDo",
          OpenMetadataWikiPages.MODEL_0137_ACTIONS_FOR_PEOPLE,
          "12780d85-66e8-45b3-9d0e-ef4bebbf1ed9",
          "An action assigned to an individual."),

    /**
     * Associates a To Do with one or more elements to work on.
     */
    ACTION_TARGET_RELATIONSHIP("207e2594-e3e4-4be8-a12c-4c401656e241",
                               "ActionTarget",
                               OpenMetadataWikiPages.MODEL_0137_ACTIONS_FOR_PEOPLE,
                               "e087a192-707d-41fb-97c6-84861383e4b5",
                               "Associates a To Do with one or more elements to work on."),

    /**
     * A group of people with a common interest or skill.
     */
    COMMUNITY("fbd42379-f6c3-4f08-b6f7-378565cda993",
              "Community",
              OpenMetadataWikiPages.MODEL_0140_COMMUNITIES,
              "e7300c49-b5ad-4808-aaa8-64ee81f40df6",
              "A group of people with a common interest or skill."),

    /**
     * A person who has joined a community.
     */
    COMMUNITY_MEMBER("fbd42379-f6c3-4f09-b6f7-378565cda993",
              "CommunityMember",
              OpenMetadataWikiPages.MODEL_0140_COMMUNITIES,
              "38833f01-dbad-4cf7-b825-54a9a97d6d78",
              "A person who has joined a community."),

    /**
     * Associates an actor profile with a community.
     */
    COMMUNITY_MEMBERSHIP_RELATIONSHIP("7c7da1a3-01b3-473e-972e-606eff0cb112",
                                      "CommunityMembership",
                                      OpenMetadataWikiPages.MODEL_0140_COMMUNITIES,
                                      "a4f0935f-78f3-4d0a-aa11-a3f86a50ea36",
                                      "Associates an actor profile with a community."),


    /**
     * Quantitative feedback related to an item.
     */
    RATING("7299d721-d17f-4562-8286-bcd451814478",
           "Rating",
           OpenMetadataWikiPages.MODEL_0150_FEEDBACK,
           "bd05b809-b094-431d-905d-dad65bbe6484",
           "Quantitative feedback related to an item."),

    /**
     * Links a rating to an item.
     */
    ATTACHED_RATING_RELATIONSHIP("0aaad9e9-9cc5-4ad8-bc2e-c1099bab6344",
                                 "AttachedRating",
                                 OpenMetadataWikiPages.MODEL_0150_FEEDBACK,
                                 "87ea2c02-dae5-4d7d-8bc8-c4f2589026b0",
                                 "Links a rating to an item."),

    /**
     * Descriptive feedback or discussion related to the attached element.
     */
    COMMENT("1a226073-9c84-40e4-a422-fbddb9b84278",
            "Comment",
            OpenMetadataWikiPages.MODEL_0150_FEEDBACK,
            "af656cd2-a73c-421f-9429-4b6749cb6f09",
            "Descriptive feedback or discussion related to the attached element."),

    /**
     * Links a comment to an item, or another comment.
     */
    ATTACHED_COMMENT_RELATIONSHIP("0d90501b-bf29-4621-a207-0c8c953bdac9",
                                  "AttachedComment",
                                  OpenMetadataWikiPages.MODEL_0150_FEEDBACK,
                                  "7025c1a3-2026-422e-92f3-cf7d65832713",
                                  "Links a comment to an item, or another comment."),

    /**
     * Identifies a comment as answering a question asked in another comment.
     */
    ACCEPTED_ANSWER_RELATIONSHIP("ecf1a3ca-adc5-4747-82cf-10ec590c5c69",
                                 "AcceptedAnswer",
                                 OpenMetadataWikiPages.MODEL_0150_FEEDBACK,
                                 "2f207a82-e7e2-4fd6-9a91-c6055f287f66",
                                 "Identifies a comment as answering a question asked in another comment."),

    /**
     * Boolean type of rating expressing a favorable impression.
     */
    LIKE("deaa5ca0-47a0-483d-b943-d91c76744e01",
         "Like",
         OpenMetadataWikiPages.MODEL_0150_FEEDBACK,
         "b2bbaacb-c547-41b9-9bb2-e11d55bf74f7",
         "Boolean type of rating expressing a favorable impression."),



    /**
     * Links a like to an item.
     */
    ATTACHED_LIKE_RELATIONSHIP("e2509715-a606-415d-a995-61d00503dad4",
                               "AttachedLike",
                               OpenMetadataWikiPages.MODEL_0150_FEEDBACK,
                               "727783d2-de50-44f9-b5bc-d21a23b1a203",
                               "Links a like to an item."),

    /**
     * A descriptive tag for an item.
     */
    INFORMAL_TAG("ba846a7b-2955-40bf-952b-2793ceca090a",
                 "InformalTag",
                 OpenMetadataWikiPages.MODEL_0150_FEEDBACK,
                 "74bf2e98-b334-4cc8-b89f-a03239a7b574",
                 "A descriptive tag for an item."),

    /**
     * Links an informal tag to an item.
     */
    ATTACHED_TAG_RELATIONSHIP("4b1641c4-3d1a-4213-86b2-d6968b6c65ab",
                              "AttachedTag",
                              OpenMetadataWikiPages.MODEL_0150_FEEDBACK,
                              "f5f7ed7d-bebe-454f-94aa-1029ff37ac6a",
                              "Links an informal tag to an item."),

    /**
     * Person contributing new content.
     */
    CROWD_SOURCING_CONTRIBUTOR("3a84c94c-ac6f-4be1-a72a-07dcec7b1fe3",
                               "CrowdSourcingContributor",
                               OpenMetadataWikiPages.MODEL_0155_CROWD_SOURCING,
                               "ee2046c9-aadd-4519-a76d-43e6cfce03a8",
                               "Person contributing new content."),

    /**
     * Defines one of the actors contributing content to a new description or asset.
     */
    CROWD_SOURCING_CONTRIBUTION("4db83564-b200-4956-94a4-c95a5c30e65a",
                                "CrowdSourcingContribution",
                                 OpenMetadataWikiPages.MODEL_0155_CROWD_SOURCING,
                                "715c667a-7a9b-45f0-b44e-06c1c0401929",
                                "Defines one of the actors contributing content to a new description or asset."),

    /**
     * An ordered list of related notes.
     */
    NOTE_LOG("646727c7-9ad4-46fa-b660-265489ad96c6",
             "NoteLog",
             OpenMetadataWikiPages.MODEL_0160_NOTES,
             "cd8b86b2-115d-4a74-a6f3-8f3001a03c58",
             "An ordered list of related notes."),

    /**
     * An entry in a note log.
     */
    NOTE_ENTRY("2a84d94c-ac6f-4be1-a72a-07dcec7b1fe3",
               "NoteEntry",
               OpenMetadataWikiPages.MODEL_0160_NOTES,
               "33e3f368-d226-4dfe-895b-38e75a49b891",
               "An entry in a note log."),

    /**
     * Links a note log to an item.
     */
    ATTACHED_NOTE_LOG_RELATIONSHIP("4f798c0c-6769-4a2d-b489-d2714d89e0a4",
                                   "AttachedNoteLog",
                                   OpenMetadataWikiPages.MODEL_0160_NOTES,
                                   "6ea034cf-f669-480b-ab7e-5be658420a4e",
                                   "Links a note log to an item."),

    /**
     * Link between a note log and one of its note log entries.
     */
    ATTACHED_NOTE_LOG_ENTRY_RELATIONSHIP("38edecc6-f385-4574-8144-524a44e3e712",
                                         "AttachedNoteLogEntry",
                                         OpenMetadataWikiPages.MODEL_0160_NOTES,
                                         "b91a2d26-566b-4795-baea-78097556c227",
                                         "Link between a note log and one of its note log entries."),

    /**
     * A person adding notes to a note log.
     */
    NOTE_LOG_AUTHOR("3a84d94c-ac6f-4be1-a72a-07dbec7b1fe3",
                    "NoteLogAuthor",
                    OpenMetadataWikiPages.MODEL_0160_NOTES,
                    "58213b72-68b7-4259-938c-f0f5c59396b0",
                    "A person adding notes to a note log."),


    /**
     * Links a note log to an author.
     */
    NOTE_LOG_AUTHORSHIP_RELATIONSHIP("8f798c0c-6769-4a2d-b489-12714d89e0a4",
                                     "NoteLogAuthorship",
                                     OpenMetadataWikiPages.MODEL_0160_NOTES,
                                     "1f972f03-e7a7-4fd9-923e-bdbcdf37c4f9",
                                     "Links a note log to an author."),


    /* ============================================================================================================================*/
    /* Area 2 - Assets                                                                                                             */
    /* ============================================================================================================================*/

    CONNECTION("114e9f8f-5ff3-4c32-bd37-a7eb42712253",
               "Connection",
               OpenMetadataWikiPages.MODEL_0201_CONNECTIONS,
               "6c57f5f6-e692-4688-8a16-e81e5eb985f6",
               "A set of properties to identify and configure a connector instance."),

    /**
     * A set of properties describing a type of connector.
     */
    CONNECTOR_TYPE("954421eb-33a6-462d-a8ca-b5709a1bd0d4",
                   "ConnectorType",
                   OpenMetadataWikiPages.MODEL_0201_CONNECTIONS,
                   "83d59e58-8f1d-4554-8a93-3c3b9c9808d1",
                   "A set of properties describing a type of connector."),

    /**
     * A detailed description of the effect of some data processing.
     */
    CONNECTOR_CATEGORY("fb60761f-7afd-4d3d-9efa-24bc85a7b22e",
                       "ConnectorCategory",
                       OpenMetadataWikiPages.MODEL_0201_CONNECTIONS,
                       "81791f28-6d73-439d-ac2f-30b8aeaebcf9",
                       "A detailed description of the effect of some data processing."),

    /**
     * Identifies a collection of related connector types.
     */
    CONNECTOR_TYPE_DIRECTORY_CLASSIFICATION("9678ef11-ed7e-404b-a041-736df7514339",
                                            "ConnectorTypeDirectory",
                                            OpenMetadataWikiPages.MODEL_0201_CONNECTIONS,
                                            "1387ff91-42bc-4eb3-8945-ab21826802ca",
                                            "Identifies a collection of related connector types."),

    /**
     * Relates a connector category for a specific type of technology with the connector types that support it.
     */
    CONNECTOR_IMPLEMENTATION_CHOICE_RELATIONSHIP("633648f3-c951-4ad7-b975-9fc04e0f3d2e",
                                                 "ConnectorImplementationChoice",
                                                 OpenMetadataWikiPages.MODEL_0201_CONNECTIONS,
                                                 "62313ce0-7a48-461e-910e-6ec8a17abcae",
                                                 "Relates a connector category for a specific type of technology with the connector types that support it."),

    /**
     * A link between a connection and the endpoint that the connector should use.
     */
    CONNECTION_ENDPOINT_RELATIONSHIP("887a7132-d6bc-4b92-a483-e80b60c86fb2",
                                     "ConnectionEndpoint",
                                     OpenMetadataWikiPages.MODEL_0201_CONNECTIONS,
                                     "02af6e89-05df-4cc5-ac32-2de8299c6e2f",
                                     "A link between a connection and the endpoint that the connector should use."),

    /**
     * A link between a connection and the connector type that should be used.
     */
    CONNECTION_CONNECTOR_TYPE_RELATIONSHIP("e542cfc1-0b4b-42b9-9921-f0a5a88aaf96",
                                           "ConnectionConnectorType",
                                           OpenMetadataWikiPages.MODEL_0201_CONNECTIONS,
                                           "3ee0cc02-f058-4d88-a510-d463f448b4ac",
                                           "A link between a connection and the connector type that should be used."),

    /**
     * A connector for a virtual resource that needs to retrieve data from multiple places.
     */
    VIRTUAL_CONNECTION("82f9c664-e59d-484c-a8f3-17088c23a2f3",
                       "VirtualConnection",
                       OpenMetadataWikiPages.MODEL_0205_CONNECTION_LINKAGE,
                       "04928167-1a20-4137-bbd8-b42151a38d8c",
                       "A connector for a virtual resource that needs to retrieve data from multiple places."),

    /**
     * A link between a virtual connection and one of the connections it depends on.
     */
    EMBEDDED_CONNECTION_RELATIONSHIP("eb6dfdd2-8c6f-4f0d-a17d-f6ce4799f64f",
                                     "EmbeddedConnection",
                                     OpenMetadataWikiPages.MODEL_0205_CONNECTION_LINKAGE,
                                     "c05c1143-3ac4-4044-a0aa-eed71e949097",
                                     "A link between a virtual connection and one of the connections it depends on."),

    /**
     * Link between a connection and the description of the asset it can be used to access.
     */
    CONNECTION_TO_ASSET_RELATIONSHIP("e777d660-8dbe-453e-8b83-903771f054c0",
                                     "ConnectionToAsset",
                                     OpenMetadataWikiPages.MODEL_0205_CONNECTION_LINKAGE,
                                     "4fedfcf8-05c4-4c8c-8284-3554c4f4c295",
                                     "Link between a connection and the description of the asset it can be used to access."),

    /**
     * Collection of related data, not necessarily stored together.
     */
    DATA_SET("1449911c-4f44-4c22-abc0-7540154feefb",
             "DataSet",
             OpenMetadataWikiPages.MODEL_0210_DATA_STORES,
             "353a074a-079b-47ad-914f-c27a6174a8ed",
             "Collection of related data, not necessarily stored together."),


    /**
     * A physical store of data
     */
    DATA_STORE("30756d0b-362b-4bfa-a0de-fce6a8f47b47",
               "DataStore",
               OpenMetadataWikiPages.MODEL_0210_DATA_STORES,
               "186e8199-1987-4578-9799-c13a8eaa08b6",
               "A physical store of data."),


    /**
     * Identifies the scope of the data stored in the digital resource(s).
     */
    DATA_SCOPE_CLASSIFICATION("22f996d0-c4b7-433a-af0b-6a3e9478e488",
                              "DataScope",
                              OpenMetadataWikiPages.MODEL_0210_DATA_STORES,
                              "b734ba5c-ce2c-4b02-b54d-9232db2f1c51",
                              "Identifies the scope of the data stored in the digital resource(s)."),


    /**
     * Description for how data is organized and represented in a data store. (Deprecated)
     */
    DATA_STORE_ENCODING_CLASSIFICATION("f08e48b5-6b66-40f5-8ff6-c2bfe527330b",
                                       "DataStoreEncoding",
                                       OpenMetadataWikiPages.MODEL_0210_DATA_STORES,
                                       "327553db-b35b-49cc-8435-d2c665f5e260",
                                       "Description for how data is organized and represented in a data store. (Deprecated)"),

    /**
     * Description for how data is organized and represented in a data asset.
     */
    DATA_ASSET_ENCODING_CLASSIFICATION("3f6a1513-d3ea-4666-b5fd-c76477b0245e",
                                       "DataAssetEncoding",
                                       OpenMetadataWikiPages.MODEL_0210_DATA_STORES,
                                       "6899c5aa-e815-4404-948c-52fc0188cf1e",
                                       "Description for how data is organized and represented in a data asset."),

    /**
     * A data source that provides a constant stream of data, such as a sensor monitoring the environment.
     */
    DATA_FEED("e87836ad-f8bd-4c52-aecd-0f1872c692e5",
             "DataFeed",
             OpenMetadataWikiPages.MODEL_0210_DATA_STORES,
             "26776fcd-0f7c-49c6-874e-729898ec2193",
             "A data source that provides a constant stream of data, such as a sensor monitoring the environment."),

    /**
     * The assets that provides data for a data set.
     */
    DATA_CONTENT_FOR_DATA_SET_RELATIONSHIP("b827683c-2924-4df3-a92d-7be1888e23c0",
                                           "DataContentForDataSet",
                                           OpenMetadataWikiPages.MODEL_0210_DATA_STORES,
                                           "64b044b3-f765-447a-aca7-5f72d0f3f9d0",
                                           "The assets that provides data for a data set."),

    /**
     * A callable interface running at an endpoint.
     */
     DEPLOYED_API("7dbb3e63-138f-49f1-97b4-66313871fc14",
                  "DeployedAPI",
                  OpenMetadataWikiPages.MODEL_0212_DEPLOYED_APIS,
                  "ce967655-024b-446d-a5b8-3fdf2a1b6f56",
                  "A callable interface running at an endpoint."),

    /**
     * The endpoint for a deployed API.
     */
    API_ENDPOINT_RELATIONSHIP("de5b9501-3ad4-4803-a8b2-e311c72a4336",
                              "APIEndpoint",
                              OpenMetadataWikiPages.MODEL_0212_DEPLOYED_APIS,
                              "481182e8-90cf-47b2-ae1e-6247bf89698b",
                              "The endpoint for a deployed API."),

    /**
     * Identifies an API that supports a request response interaction style.
     */
    REQUEST_RESPONSE_INTERFACE_CLASSIFICATION("14a29330-e830-4343-a41e-d57e2cec82f8",
                                              "RequestResponseInterface",
                                              OpenMetadataWikiPages.MODEL_0212_DEPLOYED_APIS,
                                              "343d4892-1f13-4a68-a9fc-32719cecb4a3",
                                              "Identifies an API that supports a request response interaction style."),

    /**
     * Identifies an API that listens for incoming events and processes them.
     */
    LISTENER_INTERFACE_CLASSIFICATION("4099d2ed-2a5e-4c44-8443-9de4e378a4ba",
                                      "ListenerInterface",
                                      OpenMetadataWikiPages.MODEL_0212_DEPLOYED_APIS,
                                      "39e5a428-663e-4fff-a4dd-e6d5e627c143",
                                      "Identifies an API that listens for incoming events and processes them."),

    /**
     * Identifies an API that sends out events to other listening components.
     */
    PUBLISHER_INTERFACE_CLASSIFICATION("4fdedcd5-b186-4bee-887a-02fa29a10750",
                                      "PublisherInterface",
                                      OpenMetadataWikiPages.MODEL_0212_DEPLOYED_APIS,
                                      "7c7d9e1c-72cd-4d64-89bf-be898edad980",
                                      "Identifies an API that sends out events to other listening components."),


    /**
     * A packaged and deployed software component supporting a well-defined function.
     */
    DEPLOYED_SOFTWARE_COMPONENT("486af62c-dcfd-4859-ab24-eab2e380ecfd",
                                "DeployedSoftwareComponent",
                                OpenMetadataWikiPages.MODEL_0215_SOFTWARE_COMPONENTS,
                                "37c211f4-80e3-40a7-b12b-8fdb7ad7af86",
                                "A packaged and deployed software component supporting a well-defined function."),

    /**
     * A connector that is configured and deployed to run in a specific software server capability.
     */
    DEPLOYED_CONNECTOR("c9a183ab-67f4-46a4-8836-16fa041769b7",
                       "DeployedConnector",
                       OpenMetadataWikiPages.MODEL_0215_SOFTWARE_COMPONENTS,
                       "98bb02cd-a05c-4b06-b723-e295213ec9b0",
                       "A connector that is configured and deployed to run in a specific software server capability."),

    /**
     * A child process.
     */
    EMBEDDED_PROCESS("8145967e-bb83-44b2-bc8c-68112c6a5a06",
                     "EmbeddedProcess",
                       OpenMetadataWikiPages.MODEL_0215_SOFTWARE_COMPONENTS,
                     "0a1e7e7b-ce2e-4371-b9fa-8a37d9c0ce3e",
                     "A child process."),

    /**
     * A child process that runs for a short period of time.
     */
    TRANSIENT_EMBEDDED_PROCESS("9bd9d37a-b2ae-48ec-9776-080f667e91c5",
                     "TransientEmbeddedProcess",
                     OpenMetadataWikiPages.MODEL_0215_SOFTWARE_COMPONENTS,
                     "ab03dfea-cfa7-4ab2-96db-ac6cb894f242",
                     "A child process that runs for a short period of time compared to its parent process."),

    /**
     * A hierarchical relationship between processes.
     */
    PROCESS_HIERARCHY_RELATIONSHIP("70dbbda3-903f-49f7-9782-32b503c43e0e",
                                   "ProcessHierarchy",
                                   OpenMetadataWikiPages.MODEL_0215_SOFTWARE_COMPONENTS,
                                   "bc7ee6c0-8130-42c4-a27f-476ebea2c108",
                                   "A hierarchical relationship between processes."),

    /**
     * An interface where data flows in and/or out of a process.
     */
    PORT("e3d9FD9F-d5eD-2aed-CC98-0bc21aB6f71C",
         "Port",
         OpenMetadataWikiPages.MODEL_0217_PORTS,
         "e4abcf2e-62c5-4370-b7e3-c6478900312b",
         "An interface where data flows in and/or out of a process."),

    /**
     * A port for a composed process whose implementation comes from the port linked via a port delegation relationship.
     */
    PORT_ALIAS("DFa5aEb1-bAb4-c25B-bDBD-B95Ce6fAB7F5",
               "PortAlias",
               OpenMetadataWikiPages.MODEL_0217_PORTS,
               "bbb9d111-01f9-4a28-974d-80ac22985fdf",
               "A port for a composed process whose implementation comes from the port linked via a port delegation relationship."),

    /**
     * A port with a concrete implementation.
     */
    PORT_IMPLEMENTATION("ADbbdF06-a6A3-4D5F-7fA3-DB4Cb0eDeC0E",
                        "PortImplementation",
                        OpenMetadataWikiPages.MODEL_0217_PORTS,
                        "1415862a-bcde-42f7-b9ad-44be29ae8c60",
                        "A port with a concrete implementation."),

    /**
     * A link between a process and one of its ports.
     */
    PROCESS_PORT_RELATIONSHIP("fB4E00CF-37e4-88CE-4a94-233BAdB84DA2",
                              "ProcessPort",
                              OpenMetadataWikiPages.MODEL_0217_PORTS,
                              "9584078c-8a28-4852-bd9b-699d20385b6f",
                              "A link between a process and one of its ports."),

    /**
     * A relationship between a parent (composed) process port and a port from a more granular process.  The relationship shows where data passed to the parent process is directed.
     */
    PORT_DELEGATION_RELATIONSHIP("98bB8BA1-dc6A-eb9D-32Cf-F837bEbCbb8E",
                                 "PortDelegation",
                                 OpenMetadataWikiPages.MODEL_0217_PORTS,
                                 "7a820388-49f6-4843-aa68-a2eda5501386",
                                 "A relationship between a parent (composed) process port and a port from a more granular process.  The relationship shows where data passed to the parent process is directed."),

    /**
     * A description of a folder (directory) in a file system.
     */
    FILE_FOLDER("229ed5cc-de31-45fc-beb4-9919fd247398",
                "FileFolder",
                OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS,
                "4a4d75eb-9098-4e06-8873-47d026f5867f",
                "A description of a folder (directory) in a file system."),

    /**
     * A folder (directory) in a file system that contains a collection of data.
     */
    DATA_FOLDER("9f1fb984-db15-43ee-85fb-f8b0353bfb8b",
                "DataFolder",
                OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS,
                "0432df9c-d04d-4a41-8a27-599e0331d167",
                "A folder (directory) in a file system that contains a collection of data stored in individual files."),

    /**
     * A description of a file stored in a file system.
     */
    DATA_FILE("10752b4a-4b5d-4519-9eae-fdd6d162122f",
              "DataFile",
              OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS,
              "9805da73-c308-44ee-979c-86b1ef018dad",
              "A description of a file stored in a file system."),

    /**
     * A description of a comma separated value (CSV) file.
     */
    CSV_FILE("2ccb2117-9cee-47ca-8150-9b3a543adcec",
             "CSVFile",
             OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS,
             "4072c15f-9792-45a3-a4c9-d0602f4ffe87",
             "A file containing comma-separated (or similar delimited) data."),

    /**
     * A description of a file that follows the Apache Avro specification.
     */
    AVRO_FILE("75293260-3373-4777-af7d-7274d5c0b9a5",
              "AvroFile",
              OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS,
              "ac017c45-d167-424a-b0db-501d1dbe7096",
              "A description of a file that follows the Apache Avro specification."),

    /**
     * A description of a file that follows the JavaScript Object Notation specification.
     */
    JSON_FILE("baa608fa-510e-42d7-95cd-7c12fa37bb35",
              "JSONFile",
              OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS,
              "8fc52dce-17fe-426a-b504-c15d5df35524",
              "A description of a file that follows the JavaScript Object Notation specification."),

    /**
     * A file containing tabular data with formula.
     */
    SPREADSHEET_FILE("2f38d248-8633-402b-b085-c88fcbc33fa8",
                     "SpreadsheetFile",
                     OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS,
                     "777064c4-756e-4c78-84d7-268316481b9d",
                     "A file containing tabular data with formula."),

    /**
     * A file containing an XML structure.
     */
    XML_FILE("e1d8d6f1-3e75-41c7-a038-6e25ab985b44",
             "XMLFile",
             OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS,
             "0dbaed30-1cd5-4259-a0b2-f586966890ee",
             "A file containing an XML structure."),

    /**
     * A data file which is formatted using the Apache Parquet format.
     */
    PARQUET_FILE("97cba3a0-1dfd-4129-82b6-798de3eec0a4",
                 "ParquetFile",
                 OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS,
                 "b472c4a1-708d-4787-b0c3-730052cf19f4",
                 "A data file which is formatted using the Apache Parquet format."),

    /**
     * A nested relationship between two file folders.
     */
    FOLDER_HIERARCHY_RELATIONSHIP("48ac9028-45dd-495d-b3e1-622685b54a01",
                                  "FolderHierarchy",
                                  OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS,
                                  "1c35e932-a116-486c-891a-32c3300db62d",
                                  "A nested relationship between two file folders."),

    /**
     * The link between a data file and its containing folder.
     */
    NESTED_FILE_RELATIONSHIP("4cb88900-1446-4eb6-acea-29cd9da45e63",
                             "NestedFile",
                             OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS,
                             "25f5b1b2-3ed4-452c-8e67-9dae915195ef",
                             "The link between a data file and its containing folder."),

    /**
     * A data file that is linked to a file folder (rather than stored in it).
     */
    LINKED_FILE_RELATIONSHIP("970a3405-fde1-4039-8249-9aa5f56d5151",
                             "LinkedFile",
                             OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS,
                             "214c7af3-89e7-4ff1-9642-68af6b9fa12d",
                             "A data file that is linked to a file folder (rather than stored in it)."),

    /**
     * A data set that consists of a collection files (do not need to be co-located).
     */
    DATA_FILE_COLLECTION("962de053-ab51-40eb-b843-85b98013f5ca",
                         "DataFileCollection",
                         OpenMetadataWikiPages.MODEL_0220_FILE_AND_FOLDERS,
                         "c2833bb5-5508-4328-a81d-f9b5971be095",
                         "A data set that consists of a collection files (do not need to be co-located)."),


    /**
     * A group of related media files.
     */
    MEDIA_COLLECTION("0075d603-1627-41c5-8cae-f5458d1247fe",
                     "MediaCollection",
                     OpenMetadataWikiPages.MODEL_0221_DOCUMENT_STORES,
                     "5d968079-c51d-4168-8208-d6dabeab2105",
                     "A group of related media files."),

    /**
     * Identifies a data store as one that contains documents.
     */
    DOCUMENT_STORE("37156790-feac-4e1a-a42e-88858ae6f8e1",
                   "DocumentStore",
                   OpenMetadataWikiPages.MODEL_0221_DOCUMENT_STORES,
                   "a39cc327-bd4f-4cb7-9355-b7dbf55fa5c2",
                   "Identifies a data store as one that contains documents."),

    /**
     * Links a media file to another media file and describes relationship.
     */
    LINKED_MEDIA_RELATIONSHIP("cee3a190-fc8d-4e53-908a-f1b9689581e0",
                              "LinkedMedia",
                              OpenMetadataWikiPages.MODEL_0221_DOCUMENT_STORES,
                              "7eabb4c2-017e-4215-8ca0-5596ad4880d6",
                              "Links a media file to another media file and describes relationship."),

    /**
     * A data file containing formatted media such as images, audio or video.
     */
    MEDIA_FILE("c5ce5499-9582-42ea-936c-9771fbd475f8",
               "MediaFile",
               OpenMetadataWikiPages.MODEL_0221_DOCUMENT_STORES,
               "6e9220c4-6f47-417a-93b5-18f365e19ade",
               "A data file containing formatted media such as images, audio or video."),

    /**
     * A data file containing formatted text.
     */
    DOCUMENT("b463827c-c0a0-4cfb-a2b2-ddc63746ded4",
             "Document",
             OpenMetadataWikiPages.MODEL_0221_DOCUMENT_STORES,
             "d5f2b2eb-9fc9-4088-b64e-539ec5a70f2a",
             "A data file containing formatted text."),

    /**
     * A file containing an audio recording.
     */
    AUDIO_FILE("713c26b6-7158-4cd7-918b-7d6f9d216893",
               "AudioFile",
               OpenMetadataWikiPages.MODEL_0221_DOCUMENT_STORES,
               "a064efd8-eff6-44ae-8608-4580691a3fb0",
               "A file containing an audio recording."),

    /**
     * A file containing a video recording.
     */
    VIDEO_FILE("68f06c88-083e-42f0-8268-f4f822aeab0e",
               "VideoFile",
               OpenMetadataWikiPages.MODEL_0221_DOCUMENT_STORES,
               "adc363ff-49ba-449c-9e0b-8e1705dcd436",
               "A file containing a video recording."),

    /**
     * A file containing a three-dimensional image.
     */
    THREE_D_IMAGE_FILE("b2d56d90-ef55-4fa4-b1d6-a6049fd49466",
                       "3DImageFile",
                       OpenMetadataWikiPages.MODEL_0221_DOCUMENT_STORES,
                       "a37ea95a-4e43-499b-b2c6-a73ac99abbf8",
                       "A file containing a three-dimensional image."),

    /**
     * A file containing an image as a matrix of pixels.
     */
    RASTER_FILE("6703bfd6-3f0f-4e35-a3e7-b94e2b5c9147",
                "RasterFile",
                OpenMetadataWikiPages.MODEL_0221_DOCUMENT_STORES,
                "b92af812-05c4-4764-a8f0-d898277f69c1",
                "A file containing an image as a matrix of pixels."),

    /**
     * A file containing an image described using mathematical formulas.
     */
    VECTOR_FILE("007620a2-960e-4c3b-b625-cbefebefc737",
                "VectorFile",
                OpenMetadataWikiPages.MODEL_0221_DOCUMENT_STORES,
                "35d56124-280c-4434-9206-84eff153f73b",
                "A file containing an image described using mathematical formulas."),


    /**
     * Identifies a data store as one that contains one or more graphs.
     */
    GRAPH_STORE("86de3633-eec8-4bf9-aad1-e92df1ca2024",
                "GraphStore",
                OpenMetadataWikiPages.MODEL_0222_GRAPH_STORES,
                "ca06f2ea-ba2d-4538-9deb-76240bb616d2",
                "Identifies a data store as one that contains one or more graphs."),

    /**
     * A location for storing and distributing related events.
     */
    TOPIC("29100f49-338e-4361-b05d-7e4e8e818325",
          "Topic",
          OpenMetadataWikiPages.MODEL_0223_EVENTS_AND_LOGS,
          "a724d481-625b-4855-bc29-76f90983311f",
          "A location for storing and distributing related events."),

    /**
     * An event topic supported by Apache Kafka.
     */
    KAFKA_TOPIC("f2f5dae9-8410-420f-81f4-5d08543e07aa",
                "KafkaTopic",
                OpenMetadataWikiPages.MODEL_0223_EVENTS_AND_LOGS,
                "38bdd80b-706a-483c-8c39-4cba69e9a767,",
                "An event topic supported by Apache Kafka."),

    /**
     * A collection of elements registered to receive events from a topic.
     */
    SUBSCRIBER_LIST("69751093-35f9-42b1-944b-ba6251ff513d",
                    "SubscriberList",
                    OpenMetadataWikiPages.MODEL_0223_EVENTS_AND_LOGS,
                    "934517a0-c664-4d08-80c9-d61da42a3167",
                    "A collection of elements registered to receive events from a topic."),

    /**
     * Links the list of subscribers to a topic.
     */
    TOPIC_SUBSCRIBERS_RELATIONSHIP("bc91a28c-afb9-41a7-8eb2-fc8b5271fe9e",
                                   "TopicSubscribers",
                                   OpenMetadataWikiPages.MODEL_0223_EVENTS_AND_LOGS,
                                   "e42ab697-7457-48ae-89f8-b786a08e94aa",
                                   "Links the list of subscribers to a topic."),

    /**
     * "Identifies a data file as one containing log records."
     */
    LOG_FILE("ff4c8484-9127-464a-97fc-99579d5bc429",
             "LogFile",
             OpenMetadataWikiPages.MODEL_0223_EVENTS_AND_LOGS,
             "4ee71abb-c5b0-4b40-ac0e-63d6c4dad9dd",
             "Identifies a data file as one containing log records."),

    /**
     * Defines destination information for the log of activity associated with an element.
     */
    ASSOCIATED_LOG_RELATIONSHIP("0999e2b9-45d6-42c4-9767-4b74b0b48b89",
                                "AssociatedLog",
                                OpenMetadataWikiPages.MODEL_0223_EVENTS_AND_LOGS,
                                "b6a17f72-2add-4db8-bf4d-9c6460386318",
                                "Defines destination information for the log of activity associated with an element."),

    /**
     * A data store.
     */
    DATABASE("0921c83f-b2db-4086-a52c-0d10e52ca078",
             "Database",
             OpenMetadataWikiPages.MODEL_0224_DATABASES,
             "735bb7f4-342c-4830-a8e9-6c56ec5f9e50",
             "A data store."),

    /**
     * A data store organized to facilitate a relational data model.
     */
    RELATIONAL_DATABASE("6a28e242-4eca-4664-81cb-e2096d769568",
                        "RelationalDatabase",
                        OpenMetadataWikiPages.MODEL_0224_DATABASES,
                        "36e696b2-6795-4f53-9867-baaa4d50800e",
                        "A data store organized to facilitate a relational data model."),

    /**
     * A collection of database tables and views running in a database server.
     */
    DEPLOYED_DATABASE_SCHEMA("eab811ec-556a-45f1-9091-bc7ac8face0f",
                             "DeployedDatabaseSchema",
                             OpenMetadataWikiPages.MODEL_0224_DATABASES,
                             "85fcc8e5-01ec-4898-aac7-e7ca97326b94",
                             "A collection of database tables and views running in a database server."),

    /**
     * A tabular data source (typically a database table) that is an asset in its own right.
     */
    TABLE_DATA_SET("20c45531-5d2e-4eb6-9a47-035cb1067b82",
                   "TableDataSet",
                   OpenMetadataWikiPages.MODEL_0224_DATABASES,
                   "ff9fced9-daaf-4512-97c1-88381ffe05aa",
                   "A tabular data source (typically a database table) that is an asset in its own right."),

    /**
     * A data store containing metadata.
     */
    METADATA_REPOSITORY("c40397bd-eab0-4b2e-bffb-e7fa0f93a5a9",
                        "MetadataRepository",
                        OpenMetadataWikiPages.MODEL_0224_DATABASES,
                        "6e816783-17d2-43cf-88c9-2aef7aafc62e",
                        "A data store containing metadata."),

    /**
     * A data store containing cohort membership registration details.
     */
    COHORT_REGISTRY_STORE("2bfdcd0d-68bb-42c3-ae75-e9fb6c3dff70",
                          "CohortRegistryStore",
                          OpenMetadataWikiPages.MODEL_0224_DATABASES,
                          "5f3920a6-32db-4ddf-a81b-08a62b981f62",
                          "A data store containing cohort membership registration details."),

    /**
     * A data set containing metadata.
     */
    METADATA_COLLECTION("ea3b15af-ed0e-44f7-91e4-bdb299dd4976",
                        "MetadataCollection",
                        OpenMetadataWikiPages.MODEL_0225_METADATA_REPOSITORIES,
                        "84d14b66-1c9b-4dd9-92c1-07c31ed4cbc7",
                        "A data set containing metadata."),

    /**
     * The local metadata collection associated with a cohort peer.
     */
    COHORT_MEMBER_METADATA_COLLECTION_RELATIONSHIP("8b9dd3ea-057b-4709-9b42-f16098523907",
                                                   "CohortMemberMetadataCollection",
                                                   OpenMetadataWikiPages.MODEL_0225_METADATA_REPOSITORIES,
                                                   "9f3a29eb-8ff2-4891-ac2a-1e0c7a64b837",
                                                   "The local metadata collection associated with a cohort peer."),

    /**
     * A file containing compressed files.  These files may be organized into a directory (folder) structure.
     */
    ARCHIVE_FILE("ba5111df-3878-4694-82d7-0b0e47565523",
                 "ArchiveFile",
                 OpenMetadataWikiPages.MODEL_0226_ARCHIVE_FILES,
                 "de153806-44cd-465f-981b-4b703536018b",
                 "A file containing compressed files.  These files may be organized into a directory (folder) structure."),

    /**
     * Links an archive to a collection that has a description of the archive's contents as its members.
     */
    ARCHIVE_CONTENTS_RELATIONSHIP("51e59b71-013b-4f77-9a51-2d6fbb3dfeeb",
                                  "ArchiveContents",
                                  OpenMetadataWikiPages.MODEL_0226_ARCHIVE_FILES,
                                  "01341540-1132-4412-adb1-e5725d55ab6f",
                                  "Links an archive to a collection that has a description of the archive's contents as its members."),

    /**
     * An encrypted data store containing authentication and related security information.
     */
    KEYSTORE_FILE("17bee904-5b35-4c81-ac63-871c615424a2",
                  "KeystoreFile",
                  OpenMetadataWikiPages.MODEL_0227_KEYSTORES,
                  "8b628220-8c35-4ca7-814f-7f391ab0466e",
                  "An encrypted data store containing authentication and related security information."),

    /**
     * A data set containing authentication and related security information.
     */
    SECRETS_COLLECTION("979d97dd-6782-4648-8e2a-8982994533e6",
                       "SecretsCollection",
                       OpenMetadataWikiPages.MODEL_0227_KEYSTORES,
                       "4aa6aae8-a0d6-4e05-8b93-6bab1962c3a6",
                       "A data set containing authentication and related security information."),

    /**
     * A data set containing code values and their translations.
     */
    REFERENCE_CODE_TABLE("201f48c5-4e4b-41dc-9c5f-0bc9742190cf",
                         "ReferenceCodeTable",
                         OpenMetadataWikiPages.MODEL_0230_CODE_TABLES,
                         "6b3023b5-0795-4346-b950-cfeaee9a7334",
                         "A data set containing code values and their translations."),

    /**
     * A data set containing mappings between code values from different data sets.
     */
    REFERENCE_CODE_MAPPING_TABLE("9c6ec0c6-0b26-4414-bffe-089144323213",
                                 "ReferenceCodeMappingTable",
                                 OpenMetadataWikiPages.MODEL_0230_CODE_TABLES,
                                 "0f16856a-7be3-404e-bf01-282b0ce122a4",
                                 "A data set containing mappings between code values from different data sets."),

    /**
     * An asset supported by data virtualization technology.
     */
    INFORMATION_VIEW("68d7b905-6438-43be-88cf-5de027b4aaaf",
                     "InformationView",
                     OpenMetadataWikiPages.MODEL_0235_INFORMATION_VIEW,
                     "c0709c9e-8ff8-4af3-8b5d-85d89355bf64",
                     "An asset supported by data virtualization technology."),


    /**
     * A virtual asset that can be called as a table through a SQL-like API.
     */
    VIRTUAL_RELATIONAL_TABLE("fd166582-8ff5-46ad-978e-7770e7339949",
                     "VirtualRelationalTable",
                     OpenMetadataWikiPages.MODEL_0235_INFORMATION_VIEW,
                     "b6a9209c-de82-4357-8e03-447f904814e0",
                     "A virtual asset that can be called as a table through a SQL-like API."),

    /**
     * A template for generating report.
     */
    DEPLOYED_REPORT_TYPE("ed53a480-e6d4-44f1-aac7-3fac60bbb00e",
                         "DeployedReportType",
                         OpenMetadataWikiPages.MODEL_0239_REPORTS,
                         "b7ba1739-bf1d-4c12-aa3f-dd5ef8dc7674",
                         "A template for generating report."),

    /**
     * A collection if data items that describe a situation.  This is an instance of a report.
     */
    DEPLOYED_REPORT("e9077f4f-955b-4d7b-b1f7-12ee769ff0c3",
                    "DeployedReport",
                    OpenMetadataWikiPages.MODEL_0239_REPORTS,
                    "c01207de-0f57-491a-9377-84b80317c81e",
                    "A collection if data items that describe a situation.  This is an instance of a report."),

    /**
     * A collection of data items used to request activity.
     */
    FORM("8078e3d1-0c63-4ace-aafa-68498b39ccd6",
         "Form",
         OpenMetadataWikiPages.MODEL_0239_REPORTS,
         "18ae6f80-98de-4f05-8684-426a90523834",
         "A collection of data items used to request activity."),

    /**
     * A packaged and deployed analytics model.
     */
    DEPLOYED_ANALYTICS_MODEL("ddace5d7-3f4d-4ff8-a23e-d4992b2e874f",
                                "DeployedAnalyticsModel",
                                OpenMetadataWikiPages.MODEL_0265_ANALYTIC_ASSETS,
                                "0172e405-3500-42de-9d0b-cf9f851628e7",
                                "A packaged and deployed analytics model."),

    /**
     * An execution (run) of a deployed analytics model.
     */
    ANALYTICS_MODEL_RUN("8178e11b-abad-49c1-b24a-a868d760d603",
                        "AnalyticsModelRun",
                        OpenMetadataWikiPages.MODEL_0265_ANALYTIC_ASSETS,
                        "52e73acc-fa40-4def-9df5-b5911ba0418b",
                        "An execution (run) of a deployed analytics model."),

    /**
     * A text file containing a program written in a language that needs to be complied into an executable form before it can run.
     */
    SOURCE_CODE_FILE("5b26a2d2-3159-4e8e-bf28-e71904113fc8",
                     "SourceCodeFile",
                     OpenMetadataWikiPages.MODEL_0280_SOFTWARE_DEVELOPMENT_ASSETS,
                     "e241c208-9055-476f-bf56-38c627d98848",
                     "A text file containing a program written in a language that needs to be complied into an executable form before it can run."),

    /**
     * A file containing instructions to run a build of a software artifact or system.
     */
    BUILD_INSTRUCTION_FILE("b1697a55-c731-4ef8-a9ff-d29c143cc1c3",
                           "BuildInstructionFile",
                           OpenMetadataWikiPages.MODEL_0280_SOFTWARE_DEVELOPMENT_ASSETS,
                           "e26b1579-2397-4a42-ad1a-55fa351117d8",
                           "A file containing instructions to run a build of a software artifact or system."),

    /**
     * A file containing compiled code that can be executed.
     */
    EXECUTABLE_FILE("314219ed-4b81-4e1d-b66b-22958a05f0c9",
                    "ExecutableFile",
                    OpenMetadataWikiPages.MODEL_0280_SOFTWARE_DEVELOPMENT_ASSETS,
                    "b1ed127d-c5c7-41aa-b295-d3a13053e24f",
                    "A file containing compiled code that can be executed."),

    /**
     * A file containing code that is interpreted when it is run.
     */
    SCRIPT_FILE("cae5d609-16b0-4812-8582-adb742bbef89",
                "ScriptFile",
                OpenMetadataWikiPages.MODEL_0280_SOFTWARE_DEVELOPMENT_ASSETS,
                "b03e1bcd-6451-4e9c-b8ec-0e686c2ed18b",
                "A file containing code that is interpreted when it is run."),

    /**
     * A file containing a list of properties, typically used for configuration of some software.
     */
    PROPERTIES_FILE("febdb5b9-92cc-4eb1-b058-86934f2ec18b",
                    "PropertiesFile",
                    OpenMetadataWikiPages.MODEL_0280_SOFTWARE_DEVELOPMENT_ASSETS,
                    "b09718ca-f956-41a7-9528-a0976b87a5ea",
                    "A file containing a list of properties, typically used for configuration of some software."),

    /**
     * A file containing properties in YAML format.  This it typically used for configuration.
     */
    YAML_FILE("2bd6feb5-1b79-417a-b430-4e8e1e0a63dd",
              "YAMLFile",
              OpenMetadataWikiPages.MODEL_0280_SOFTWARE_DEVELOPMENT_ASSETS,
              "5e0ea8d1-bb58-4a85-931d-886f8388b9d2",
              "A file containing properties in YAML format.  This it typically used for configuration."),


    /* ============================================================================================================================*/
    /* Area 3 - Glossary                                                                                                         */
    /* ============================================================================================================================*/

    /**
     * A semantic description of something, such as a concept, object, asset, technology, role or group.
     */
    GLOSSARY_TERM("0db3e6ec-f5ef-4d75-ae38-b7ee6fd6ec0a",
                  "GlossaryTerm",
                  OpenMetadataWikiPages.MODEL_0330_TERMS,
                  "a1286149-2dae-414b-8913-462da900d395",
                  "A semantic description of something, such as a concept, object, asset, technology, role or group."),

    /**
     * Links a term to its owning glossary.
     */
    TERM_ANCHOR("1d43d661-bdc7-4a91-a996-3239b8f82e56",
                "TermAnchor",
                OpenMetadataWikiPages.MODEL_0330_TERMS,
                "712d129b-1d52-41d9-8a39-861c1fc09f85",
                "Links a term to its owning glossary."),

    /**
     * Links a glossary term into a glossary category.
     */
    TERM_CATEGORIZATION("696a81f5-ac60-46c7-b9fd-6979a1e7ad27",
                        "TermCategorization",
                        OpenMetadataWikiPages.MODEL_0330_TERMS,
                        "22dc7726-e7d3-4c41-b32a-e11ddded0908",
                        "Links a glossary term into a glossary category."),

    /**
     * Links a glossary term to a glossary term in an external glossary.
     */
    LIBRARY_TERM_REFERENCE("38c346e4-ddd2-42ef-b4aa-55d53c078d22",
                           "LibraryTermReference",
                           OpenMetadataWikiPages.MODEL_0330_TERMS,
                           "5a1eac69-6f48-443c-8a19-4f3c0efc637a",
                           "Links a glossary term to a glossary term in an external glossary."),

    /**
     * Identifies that this glossary term describes an activity.
     */
    ACTIVITY_DESCRIPTION_CLASSIFICATION("317f0e52-1548-41e6-b90c-6ae5e6c53fed",
                                        "ActivityDescription",
                                        OpenMetadataWikiPages.MODEL_0340_DICTIONARY,
                                        "1e2296b4-f7c5-45d0-9fa4-de5f27e68bef",
                                        "Identifies that this glossary term describes an activity."),

    /**
     * Link between glossary terms where on describes the context where the other one is valid to use.
     */
    USED_IN_CONTEXT("2dc524d2-e29f-4186-9081-72ea956c75de",
                    "UsedInContext",
                    OpenMetadataWikiPages.MODEL_0360_CONTEXTS,
                    "27226e7d-c7b3-4eaf-b1ff-bf38445a857f",
                    "Link between glossary terms where on describes the context where the other one is valid to use."),

    /**
     * Links a glossary term to another element such as an asset or schema element to define its meaning.
     */
    SEMANTIC_ASSIGNMENT("e6670973-645f-441a-bec7-6f5570345b92",
                        "SemanticAssignment",
                        OpenMetadataWikiPages.MODEL_0370_SEMANTIC_ASSIGNMENT,
                        "6b98085b-b70b-49cd-98e2-e87474d27e69",
                        "Links a glossary term to another element such as an asset or schema element to define its meaning."),

    /* ============================================================================================================================*/
    /* Area 4 - Governance                                                                                                         */
    /* ============================================================================================================================*/

    /**
     * Defines the level of confidentiality of related data items.
     */
    CONFIDENTIALITY_CLASSIFICATION("742ddb7d-9a4a-4eb5-8ac2-1d69953bd2b6",
                                   "Confidentiality",
                                   OpenMetadataWikiPages.MODEL_0422_GOVERNANCE_ACTION_CLASS,
                                   "0d791cad-bac5-4df3-8e0b-9bf326b4faf1",
                                   "Defines the level of confidentiality of related data items."),


    /**
     * Defines the level of confidence that should be placed in the accuracy of related data items.
     */
    CONFIDENCE_CLASSIFICATION("25d8f8d5-2998-4983-b9ef-265f58732965",
                              "Confidence",
                              OpenMetadataWikiPages.MODEL_0422_GOVERNANCE_ACTION_CLASS,
                              "50c33566-ec2d-488c-91f3-87c66648857a",
                              "Defines the level of confidence that should be placed in the accuracy of related data items."),

    /**
     * Defines the retention requirements for related data items.
     */
    RETENTION_CLASSIFICATION("83dbcdf2-9445-45d7-bb24-9fa661726553",
                             "Retention",
                             OpenMetadataWikiPages.MODEL_0422_GOVERNANCE_ACTION_CLASS,
                             "6615bfa4-ed4f-40f8-8ab9-3fd9fd3600ac",
                             "Defines the retention requirements for related data items."),

    /**
     * Defines how critical the related data items are to the organization.
     */
    CRITICALITY_CLASSIFICATION("d46d211a-bd22-40d5-b642-87b4954a167e",
                               "Criticality",
                               OpenMetadataWikiPages.MODEL_0422_GOVERNANCE_ACTION_CLASS,
                               "12f769eb-f0a1-4d21-8afb-e005cc6ca2c5",
                               "Defines how critical the related data items are to the organization."),

    /**
     * Who is responsible for making decisions on the management and governance of this element.
     */
    OWNERSHIP_CLASSIFICATION("8139a911-a4bd-432b-a9f4-f6d11c511abe",
                             "Ownership",
                             OpenMetadataWikiPages.MODEL_0445_GOVERNANCE_ROLES,
                             "a2825a97-ba55-4fd9-bc04-4d7adbd66f57",
                             "Who is responsible for making decisions on the management and governance of this element."),

    /**
     * A collection of related governance services of the same type.
     */
    GOVERNANCE_ENGINE("3fa23d4a-aceb-422f-9301-04ed474c6f74",
                      "GovernanceEngine",
                      OpenMetadataWikiPages.MODEL_0461_GOVERNANCE_ENGINES,
                      "11b96995-f6f1-46e6-abee-846a2f77f2a8",
                      "A collection of related governance services of the same type."),

    /**
     * A connector that performs some governance operation.
     */
    GOVERNANCE_SERVICE("191d870c-26f4-4310-a021-b8ca8772719d",
                       "GovernanceService",
                       OpenMetadataWikiPages.MODEL_0461_GOVERNANCE_ENGINES,
                       "e091225c-1092-4f28-b7e1-bf53456f9705",
                       "A connector that performs some governance operation."),

    /**
     * A collection of related governance services of the same type from the Governance Action Framework (GAF).
     */
    GOVERNANCE_ACTION_ENGINE("5d74250a-57ca-4197-9475-8911f620a94e",
                             "GovernanceActionEngine",
                             OpenMetadataWikiPages.MODEL_0461_GOVERNANCE_ENGINES,
                             "a8f14964-5028-4269-81be-5d23757c9caa",
                             "A collection of related governance services of the same type from the Governance Action Framework (GAF)."),

    /**
     * A governance service that conforms to the Governance Action Framework (GAF).
     */
    GOVERNANCE_ACTION_SERVICE("ececb378-31ac-4cc3-99b4-1c44e5fbc4d9",
                              "GovernanceActionService",
                              OpenMetadataWikiPages.MODEL_0461_GOVERNANCE_ENGINES,
                              "c927dcac-3481-4246-98ec-e0662e5e3a77",
                              "A governance service that conforms to the Governance Action Framework (GAF)."),

    /**
     * A governance engine for managing context events and associated actions.
     */
    CONTEXT_EVENT_ENGINE("796f6493-3c3e-4091-8b21-46ea4e54d011",
                         "ContextEventEngine",
                         OpenMetadataWikiPages.MODEL_0461_GOVERNANCE_ENGINES,
                         "cebad26a-08f6-40b7-a0e0-4f9b1b439992",
                         "A governance engine for managing context events and associated actions."),

    /**
     * A governance service for managing context events and associated actions.
     */
    CONTEXT_EVENT_SERVICE("464bb4d8-f865-4b9d-a06e-7ed19518ff13",
                          "ContextEventService",
                          OpenMetadataWikiPages.MODEL_0461_GOVERNANCE_ENGINES,
                          "6e030483-39ff-4b1b-bd50-1faa64e44690",
                          "A governance service for managing context events and associated actions."),

    /**
     * A governance engine for managing the surveying of real-world resources and capturing the results in survey report attached to the associated asset.
     */
    SURVEY_ACTION_ENGINE("9a6f3982-ebc0-4002-8762-21d415a0c21d",
                         "SurveyActionEngine",
                         OpenMetadataWikiPages.MODEL_0461_GOVERNANCE_ENGINES,
                         "325b61dd-4d43-4bd7-bcf9-af5f20b77d05",
                         "A governance engine for managing the surveying of real-world resources and capturing the results in survey report attached to the associated asset."),

    /**
     * A governance service for managing the surveying of real-world resources and capturing the results in survey report attached to the associated asset.
     */
    SURVEY_ACTION_SERVICE("f387389b-77c0-4386-b169-fc701919460a",
                          "SurveyActionService",
                          OpenMetadataWikiPages.MODEL_0461_GOVERNANCE_ENGINES,
                          "16009219-1913-466c-994b-853e0c5c961b",
                          "A governance service for managing the surveying of real-world resources and capturing the results in survey report attached to the associated asset."),

    /**
     * A governance engine for open metadata repositories.
     */
    REPOSITORY_GOVERNANCE_ENGINE("2b3bed05-c227-47d7-87a3-139ab0568361",
                                 "RepositoryGovernanceEngine",
                                 OpenMetadataWikiPages.MODEL_0461_GOVERNANCE_ENGINES,
                                 "c283bb2c-f007-485c-b90f-cb81194d4c25",
                                 "A governance engine for open metadata repositories."),

    /**
     * A governance service for open metadata repositories.
     */
    REPOSITORY_GOVERNANCE_SERVICE("978e7674-8231-4158-a4e3-a5ccdbcad60e",
                                  "RepositoryGovernanceService",
                                  OpenMetadataWikiPages.MODEL_0461_GOVERNANCE_ENGINES,
                                  "30671690-f94e-440b-abdb-2384ed36d8bf",
                                  "A governance service for open metadata repositories."),

    /**
     * Link between a governance engine and one of its services.
     */
    SUPPORTED_GOVERNANCE_SERVICE_RELATIONSHIP("2726df0e-4f3a-44e1-8433-4ca5301457fd",
                                              "SupportedGovernanceService",
                                              OpenMetadataWikiPages.MODEL_0461_GOVERNANCE_ENGINES,
                                              "346ca38f-287d-401f-bbe9-375ecd2b938f",
                                              "Link between a governance engine and one of its services."),

    /**
     * The element(s) that form the initial list of targets for action that are passed to the engine action as part of a request to run this governance action type.  Additional targets for action can be supplied by the caller.
     */
    TARGET_FOR_ACTION_TYPE("059ed11f-f8dd-45cc-991e-2cf9ad3be4a7",
                      "TargetForActionType",
                      OpenMetadataWikiPages.MODEL_0462_GOVERNANCE_ACTION_PROCESSES,
                      "433f34f5-572e-44cf-888d-653bed1bce54",
                      "The element(s) that form the initial list of targets for action that are passed to the engine action as part of a request to run this governance action type.  Additional targets for action can be supplied by the caller."),

    /**
     * The element(s) that form the initial list of targets for action that are passed to the engine action as part of a request to run this governance action process.  Additional targets for action can be supplied by the caller.
     */
    TARGET_FOR_ACTION_PROCESS("7a72262e-c466-49cf-b67d-01b7b45c3f19",
                      "TargetForActionProcess",
                      OpenMetadataWikiPages.MODEL_0462_GOVERNANCE_ACTION_PROCESSES,
                      "90dea768-2b9e-4a3c-b065-95efcfd5a48f",
                      "The element(s) that form the initial list of targets for action that are passed to the engine action as part of a request to run this governance action process.  Additional targets for action can be supplied by the caller."),

    /**
     * Represents a single run of a governance action process.  It is linked to the parent governance action process using the ProcessHierarchy relationship..
     */
    GOVERNANCE_ACTION_PROCESS_INSTANCE("206a6e44-ffe7-408b-8e59-79842d362776",
                              "GovernanceActionProcessInstance",
                              OpenMetadataWikiPages.MODEL_0462_GOVERNANCE_ACTION_PROCESSES,
                              "6fcfbc4c-f08d-4946-a311-e76237fa1263",
                              "Represents a single run of a governance action process.  It is linked to the parent governance action process using the ProcessHierarchy relationship."),

    /**
     * An engine action that has been created to support the active governance of the open metadata ecosystem and/or digital landscape.
     */
    ENGINE_ACTION("c976d88a-2b11-4b40-b972-c38d41bfc6be",
                  "EngineAction",
                  OpenMetadataWikiPages.MODEL_0463_ENGINE_ACTIONS,
                  "c002430c-ca96-4680-b930-1fc258747c39",
                  "An engine action that has been created to support the active governance of the open metadata ecosystem and/or digital landscape."),

    /**
     * Link between an engine action and the source of the request that created it.
     */
    ENGINE_ACTION_REQUEST_SOURCE("5323a705-4c1f-456a-9741-41fdcb8e93ac",
                                 "EngineActionRequestSource",
                                 OpenMetadataWikiPages.MODEL_0463_ENGINE_ACTIONS,
                                 "cda636e7-c5ac-45ad-a1c0-edcdd5451f13",
                                 "Link between an engine action and the source of the request that created it."),

    /**
     * The element(s) that the engine action will work on.
     */
    TARGET_FOR_ACTION("46ec49bf-af66-4575-aab7-06ce895120cd",
                      "TargetForAction",
                      OpenMetadataWikiPages.MODEL_0463_ENGINE_ACTIONS,
                      "3ab0a122-9bbe-4c96-bb8a-e8d8dae84a12",
                      "The element(s) that the engine action will work on."),

    /**
     * Link between an engine action and the governance engine that will execute it.
     */
    ENGINE_ACTION_EXECUTOR("e690ab17-6779-46b4-a8f1-6872d88c1bbb",
                           "EngineActionExecutor",
                           OpenMetadataWikiPages.MODEL_0463_ENGINE_ACTIONS,
                           "f86f2040-652e-495e-b27d-ffcc7c75a05e",
                           "Link between an engine action and the governance engine that will execute it."),

    /**
     * Linking of engine actions to show execution sequence.
     */
    NEXT_ENGINE_ACTION("4efd16d4-f397-449c-a75d-ebea42fe581b",
                       "NextEngineAction",
                       OpenMetadataWikiPages.MODEL_0463_ENGINE_ACTIONS,
                       "6b3e721b-b11d-4de9-bcce-62a62466d866",
                       "Linking of engine actions to show execution sequence."),

    /**
     * A collection of integration connectors to run together.
     */
    INTEGRATION_GROUP("4d7c43ec-983b-40e4-af78-6fb66c4f5136",
                      "IntegrationGroup",
                      OpenMetadataWikiPages.MODEL_0464_INTEGRATION_GROUPS,
                      "1c5e6f3c-96ff-4e3d-876d-26c8905add7b",
                      "A collection of integration connectors to run together."),

    /**
     * A definition to control the execution of an integration connector.
     */
    INTEGRATION_CONNECTOR("759da11b-ebb6-4382-bdc9-72adc7c922db",
                          "IntegrationConnector",
                          OpenMetadataWikiPages.MODEL_0464_INTEGRATION_GROUPS,
                          "fe907752-40e5-4bbc-9984-5a363bfd0241",
                          "A definition to control the execution of an integration connector."),

    /**
     * A description of an adverse situation or activity.
     */
    INCIDENT_REPORT("072f252b-dea7-4b88-bb2e-8f741c9ca7f6e",
                    "IncidentReport",
                    OpenMetadataWikiPages.MODEL_0470_INCIDENT_REPORTING,
                    "56c76116-1b1c-447c-b74d-ff860a2e41c6",
                    "A description of an adverse situation or activity."),


    /* ============================================================================================================================*/
    /* Area 5 - Schemas                                                                                                            */
    /* ============================================================================================================================*/

    /**
     * An element that is part of a schema definition.
     */
    SCHEMA_ELEMENT("718d4244-8559-49ed-ad5a-10e5c305a656",
                   "SchemaElement",
                   OpenMetadataWikiPages.MODEL_0501_SCHEMA_ELEMENTS,
                   "e134ce59-0252-4876-b6cd-196a56713deb",
                   "An element that is part of a schema definition."),

    /**
     * A specific type description.
     */
    SCHEMA_TYPE("5bd4a3e7-d22d-4a3d-a115-066ee8e0754f",
                "SchemaType",
                OpenMetadataWikiPages.MODEL_0501_SCHEMA_ELEMENTS,
                "e1eaec90-41e9-4816-802d-3069663790f5",
                "A specific type description."),

    /**
     * A fixed simple value.
     */
    LITERAL_SCHEMA_TYPE("520ebb91-c4eb-4d46-a3b1-974875cdcf0d",
                        "LiteralSchemaType",
                        OpenMetadataWikiPages.MODEL_0501_SCHEMA_ELEMENTS,
                        "61686e00-ed01-4462-a665-151d20340946",
                        "A fixed simple value."),

    /**
     * A single valued data item.
     */
    SIMPLE_SCHEMA_TYPE("b5ec6e07-6419-4225-9dc4-fb55aba255c6",
                       "SimpleSchemaType",
                       OpenMetadataWikiPages.MODEL_0501_SCHEMA_ELEMENTS,
                       "571cf586-6d31-4b69-ba3f-22f6db250765",
                       "A single valued data item."),

    /**
     * A specific primitive type.
     */
    PRIMITIVE_SCHEMA_TYPE("f0f75fba-9136-4082-8352-0ad74f3c36ed",
                          "PrimitiveSchemaType",
                          OpenMetadataWikiPages.MODEL_0501_SCHEMA_ELEMENTS,
                          "ed8d01c9-d413-4fb1-affd-56d1fa523558",
                          "A specific primitive type."),

    /**
     * A single valued type with fixed list of valid values.
     */
    ENUM_SCHEMA_TYPE("24b092ac-42e9-43dc-aeca-eb034ce307d9",
                     "EnumSchemaType",
                     OpenMetadataWikiPages.MODEL_0501_SCHEMA_ELEMENTS,
                     "794b3c89-a2ab-49ea-b77f-14c491518cfa",
                     "A single valued type with fixed list of valid values."),

    /**
     * A list of alternative schema types for attribute.
     */
    SCHEMA_TYPE_CHOICE("5caf954a-3e33-4cbd-b17d-8b8613bd2db8",
                       "SchemaTypeChoice",
                       OpenMetadataWikiPages.MODEL_0501_SCHEMA_ELEMENTS,
                       "72cfe678-3160-4ed6-9f90-e04ced0aa98f",
                       "A list of alternative schema types for attribute."),

    /**
     * The list of alternative schema types.
     */
    SCHEMA_TYPE_OPTION_RELATIONSHIP("eb4f1f98-c649-4560-8a46-da17c02764a9",
                                    "SchemaTypeOption",
                                    OpenMetadataWikiPages.MODEL_0501_SCHEMA_ELEMENTS,
                                    "e3a332ff-cbf2-4d2b-bb74-cb04b002690d",
                                    "The list of alternative schema types."),

    /**
     * The structure of the data stored in a digital resource described by the attached asset.
     */
    ASSET_SCHEMA_TYPE_RELATIONSHIP("815b004d-73c6-4728-9dd9-536f4fe803cd",
                                   "AssetSchemaType",
                                   OpenMetadataWikiPages.MODEL_0503_ASSET_SCHEMA,
                                   "ea278feb-8073-49a3-8acf-006124ef0959",
                                   "The structure of the data stored in a digital resource described by the attached asset."),

    /**
     * A concrete implementation example for a schema element.
     */
    IMPLEMENTATION_SNIPPET("49990755-2faa-4a62-a1f3-9124b9c73df4",
                           "ImplementationSnippet",
                           OpenMetadataWikiPages.MODEL_0504_SNIPPETS,
                           "1c085070-8d26-4488-b973-f46a4c49498d",
                           "A concrete implementation example for a schema element."),

    /**
     * Link between an element such as a schema type or data class and an implementation snippet.
     */
    ASSOCIATED_SNIPPET_RELATIONSHIP("6f89c320-22aa-4d99-9a97-442e8d214655",
                                    "AssociatedSnippet",
                                    OpenMetadataWikiPages.MODEL_0504_SNIPPETS,
                                    "6e39e1a1-20a2-428c-b823-0c7ee0a9fdd1",
                                    "Link between an element such as a schema type or data class and an implementation snippet."),

    /**
     * Link between a schema type and an implementation component.
     */
    SCHEMA_TYPE_IMPLEMENTATION_RELATIONSHIP("eed5565d-7ac2-46fe-9a26-4722fad8d993",
                                            "SchemaTypeImplementation",
                                            OpenMetadataWikiPages.MODEL_0504_SNIPPETS,
                                            "29c9224b-9120-4a09-aaf1-a0a9d881f037",
                                            "Link between a schema type and an implementation component."),

    /**
     * A schema type that has a complex structure of nested attributes and types.
     */
    COMPLEX_SCHEMA_TYPE("786a6199-0ce8-47bf-b006-9ace1c5510e4",
                        "ComplexSchemaType",
                        OpenMetadataWikiPages.MODEL_0505_SCHEMA_ATTRIBUTES,
                        "d4d92678-4b22-410b-8563-5d2b0d107c6b",
                        "A schema type that has a complex structure of nested attributes and types."),

    /**
     * A schema type that has a list of attributes, typically of different types.
     */
    STRUCT_SCHEMA_TYPE("a13b409f-fd67-4506-8d94-14dfafd250a4",
                       "StructSchemaType",
                       OpenMetadataWikiPages.MODEL_0505_SCHEMA_ATTRIBUTES,
                       "26d86816-e8b0-4a17-9250-fed94f47ea5b",
                       "A schema type that has a list of attributes, typically of different types."),


    /**
     * Link between a complex schema type and its attributes.
     */
    ATTRIBUTE_FOR_SCHEMA_RELATIONSHIP("86b176a2-015c-44a6-8106-54d5d69ba661",
                                      "AttributeForSchema",
                                      OpenMetadataWikiPages.MODEL_0505_SCHEMA_ATTRIBUTES,
                                      "40ed8943-c33f-4d55-912c-4bc0b97cb509",
                                      "Link between a complex schema type and its attributes."),

    /**
     * A schema element that nests another schema type in its parent.
     */
    SCHEMA_ATTRIBUTE("1a5e159b-913a-43b1-95fe-04433b25fca9",
                     "SchemaAttribute",
                     OpenMetadataWikiPages.MODEL_0505_SCHEMA_ATTRIBUTES,
                     "473366bc-c474-439d-b7c8-8a36af5a523b",
                     "A schema element that nests another schema type in its parent."),

    /**
     * Type information embedded within an attribute.
     */
    TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION("e2bb76bb-774a-43ff-9045-3a05f663d5d9",
                                           "TypeEmbeddedAttribute",
                                           OpenMetadataWikiPages.MODEL_0505_SCHEMA_ATTRIBUTES,
                                           "b7daca27-d524-4f04-9c01-788a1306a651",
                                           "Type information embedded within an attribute."),


    /**
     * The direct parent-child relationship between attributes with an embedded type.
     */
    NESTED_SCHEMA_ATTRIBUTE_RELATIONSHIP("0ffb9d87-7074-45da-a9b0-ae0859611133",
                                         "NestedSchemaAttribute",
                                         OpenMetadataWikiPages.MODEL_0505_SCHEMA_ATTRIBUTES,
                                         "31b00f9b-a5c8-4408-800a-e7238e663a37",
                                         "The direct parent-child relationship between attributes with an embedded type."),

    /**
     * The schema type for an attribute.
     */
    SCHEMA_ATTRIBUTE_TYPE_RELATIONSHIP("2d955049-e59b-45dd-8e62-cde1add59f9e",
                                       "SchemaAttributeType",
                                       OpenMetadataWikiPages.MODEL_0505_SCHEMA_ATTRIBUTES,
                                       "156e4d0f-301f-4472-a45d-b2b4dfb4a13d",
                                       "The schema type for an attribute."),

    /**
     * The root of a complex schema - normally attaches to an asset or port.
     */
    ROOT_SCHEMA_TYPE("126962bf-dd26-4fcf-97d8-d0ad1fdd2d50",
                     "RootSchemaType",
                     OpenMetadataWikiPages.MODEL_0530_TABULAR_SCHEMAS,
                     "3e788ad5-4cad-4790-8744-0ad6674cb7b4",
                     "The root of a complex schema - normally attaches to an asset or port."),

    /**
     * A schema type for a table oriented data structure.
     */
    TABULAR_SCHEMA_TYPE("248975ec-8019-4b8a-9caf-084c8b724233",
                        "TabularSchemaType",
                        OpenMetadataWikiPages.MODEL_0530_TABULAR_SCHEMAS,
                        "62230df6-9d05-416d-9015-da428e5cd0e1",
                        "A schema type for a table oriented data structure."),

    /**
     * A column attribute for a table oriented data structure.
     */
    TABULAR_COLUMN("d81a0425-4e9b-4f31-bc1c-e18c3566da10",
                   "TabularColumn",
                   OpenMetadataWikiPages.MODEL_0530_TABULAR_SCHEMAS,
                   "f1eae42e-50ff-44dc-8fd4-4954915fb09c",
                   "A column attribute for a table oriented data structure."),

    /**
     * A column in a tabular file.
     */
    TABULAR_FILE_COLUMN("af6265e7-5f58-4a9c-9ae7-8d4284be62bd",
                        "TabularFileColumn",
                        OpenMetadataWikiPages.MODEL_0530_TABULAR_SCHEMAS,
                        "ebe8d246-9c59-415b-b920-ea251a02185f",
                        "A column in a tabular file."),

    /**
     * A schema type for a graph data structure.
     */
    GRAPH_SCHEMA_TYPE("983c5e72-801b-4e42-bc51-f109527f2317",
                      "GraphSchemaType",
                      OpenMetadataWikiPages.MODEL_0533_GRAPH_SCHEMAS,
                      "f5200884-ff02-4f25-bf3b-2f7ca24a074d",
                      "A schema type for a graph data structure."),

    /**
     * A schema attribute for a node in a graph data structure.
     */
    GRAPH_VERTEX("1252ce12-540c-4724-ad70-f70940956de0",
                 "GraphVertex",
                 OpenMetadataWikiPages.MODEL_0533_GRAPH_SCHEMAS,
                 "f5a74d2d-7d8e-4e42-9187-7e10d0979256",
                 "A schema attribute for a node in a graph data structure."),

    /**
     * A schema attribute for a relationship in graph data structure.
     */
    GRAPH_EDGE("d4104eb3-4f2d-4d83-aca7-e58dd8d5e0b1",
               "GraphEdge",
               OpenMetadataWikiPages.MODEL_0533_GRAPH_SCHEMAS,
               "b39112b8-e22d-4510-9420-1ef72f84b8e9",
               "A schema attribute for a relationship in graph data structure."),

    /**
     * A link between a graph edge and a vertex.   Each edge should have two of these relationships.
     */
    GRAPH_EDGE_LINK_RELATIONSHIP("503b4221-71c8-4ba9-8f3d-6a035b27971c",
                                 "GraphEdgeLink",
                                 OpenMetadataWikiPages.MODEL_0533_GRAPH_SCHEMAS,
                                 "15c806cf-c51e-4ab8-991b-0bf45bd0a96a",
                                 "A link between a graph edge and a vertex.   Each edge should have two of these relationships."),

    /**
     * A logical data type specification.
     */
    DATA_CLASS("6bc727dc-e855-4979-8736-78ac3cfcd32f",
                           "DataClass",
                           OpenMetadataWikiPages.MODEL_0540_DATA_CLASSES,
                           "b5ee2d50-c30f-4cf1-869b-d83294fab681",
                           "A logical data type specification."),

    /**
     * Links a data class to an asset or schema element to define its logical data type.
     */
    DATA_CLASS_ASSIGNMENT("4df37335-7f0c-4ced-82df-3b2fd07be1bd",
               "DataClassAssignment",
               OpenMetadataWikiPages.MODEL_0540_DATA_CLASSES,
               "296c55bc-0d5e-4a84-bb58-c5484d363ec2",
               "Links a data class to an asset or schema element to define its logical data type."),

    /**
     * Links a data class to another in a parent child hierarchy.
     */
    DATA_CLASS_HIERARCHY("6b947ccc-1a70-4785-9ca3-d6326bc51291",
                          "DataClassHierarchy",
                          OpenMetadataWikiPages.MODEL_0540_DATA_CLASSES,
                          "1949ace2-59c2-4f8f-9f3b-62def8b3d029",
                          "Links a data class to another in a parent child hierarchy."),

    /**
     * Links a data class to another in a part of hierarchy.
     */
    DATA_CLASS_COMPOSITION("767fb343-4699-49c1-a0f8-af6da78505f8",
                          "DataClassComposition",
                          OpenMetadataWikiPages.MODEL_0540_DATA_CLASSES,
                          "e11693b6-f2f7-4693-aac7-a7a98c6c1c49",
                          "Links a data class to another in a part of hierarchy."),

    /**
     * A single valid value for a referenceable.
     */
    VALID_VALUE_DEFINITION("09b2133a-f045-42cc-bb00-ee602b74c618",
                           "ValidValueDefinition",
                           OpenMetadataWikiPages.MODEL_0545_REFERENCE_DATA,
                           "7aebb099-27f7-4aad-9e22-d7b97d450b56",
                           "A single valid value for a referenceable."),

    /**
     * A collection of related valid values.
     */
    VALID_VALUE_SET("7de10805-7c44-40e3-a410-ffc51306801b",
                    "ValidValuesSet",
                    OpenMetadataWikiPages.MODEL_0545_REFERENCE_DATA,
                    "8a786df1-738d-44f4-960f-bce7e929f36e",
                    "A collection of related valid values."),

    /**
     * An asset that contains trusted values for use as a reference.
     */
    REFERENCE_DATA_CLASSIFICATION("55e5ae33-39c6-4834-9d05-ef0ae4e0163b",
                                  "ReferenceData",
                                  OpenMetadataWikiPages.MODEL_0545_REFERENCE_DATA,
                                  "2d88a635-81ee-4ad8-9e1b-7cccea448776",
                                  "An asset that contains trusted values for use as a reference."),

    /**
     * Defines a data field that contains metadata for the row/record/object.
     */
    INSTANCE_METADATA_CLASSIFICATION("e6d5c097-a5e9-4bc4-a614-2506276059af",
                                     "InstanceMetadata",
                                     OpenMetadataWikiPages.MODEL_0550_INSTANCE_METADATA,
                                     "2045991b-e043-42b7-9424-a0b81d1b29ff",
                                     "Defines a data field that contains metadata for the row/record/object."),


    /**
     * Links a referenceable to its valid values.
     */
    VALID_VALUES_ASSIGNMENT_RELATIONSHIP("c5d48b73-eadd-47db-ab64-3be99b2fb32d",
                                         "ValidValuesAssignment",
                                         OpenMetadataWikiPages.MODEL_0545_REFERENCE_DATA,
                                         "5b4a2eb8-e959-4360-b4f0-298c504aeba7",
                                         "Links a referenceable to its valid values."),

    /**
     * Enables valid values to be used as tags to help group and locate referenceables.
     */
    REFERENCE_VALUE_ASSIGNMENT_RELATIONSHIP("111e6d2e-94e9-43ed-b4ed-f0d220668cbf",
                                            "ReferenceValueAssignment",
                                            OpenMetadataWikiPages.MODEL_0545_REFERENCE_DATA,
                                            "94376fc4-7ca6-4650-8505-24d1127840e1",
                                            "Enables valid values to be used as tags to help group and locate referenceables."),


    /**
     * A link between a valid value representing a specification property and the element representing the implementation.
     */
    SPECIFICATION_PROPERTY_ASSIGNMENT_RELATIONSHIP("ae9118b0-b898-4f5b-968a-be3c17025580",
                                            "SpecificationPropertyAssignment",
                                            OpenMetadataWikiPages.MODEL_0545_REFERENCE_DATA,
                                            "a161504d-861a-4eed-a7c6-4954c8ae837d",
                                            "A link between a valid value representing a specification property and the element representing the implementation."),

    /**
     * Represents an association between two valid values.
     */
    VALID_VALUE_ASSOCIATION_RELATIONSHIP("364cabe6-a983-4a2b-81ba-190b8e7b8390",
                                         "ValidValueAssociation",
                                         OpenMetadataWikiPages.MODEL_0545_REFERENCE_DATA,
                                         "3543498a-3afe-410e-bf8c-c7dc5ab03c0f",
                                         "Represents an association between two valid values."),

    /**
     * Links valid value set to the values.
     */
    VALID_VALUE_MEMBER_RELATIONSHIP("6337c9cd-8e5a-461b-97f9-5151bcb97a9e",
                                    "ValidValueMember",
                                    OpenMetadataWikiPages.MODEL_0545_REFERENCE_DATA,
                                    "4974410c-952d-4c27-bc7a-172e195f1c6b",
                                    "Links valid value set to the values."),

    /**
     * Identifies two valid values from different valid value sets (properties) that should be used together when in the same element for consistency.
     */
    CONSISTENT_VALID_VALUES_RELATIONSHIP("16f08074-1f66-4394-98f0-f81a2fb65f18",
                                         "ConsistentValidValues",
                                         OpenMetadataWikiPages.MODEL_0545_REFERENCE_DATA,
                                         "c82c3bbd-3ef4-46ed-9153-ca2ae56e1113",
                                         "Identifies two valid values from different valid value sets (properties) that should be used together when in the same element for consistency."),

    /**
     * Link to an asset that implements the list of valid values.
     */
    VALID_VALUES_IMPL_RELATIONSHIP("d9a39553-6a47-4477-a217-844300c07cf2",
                                   "ValidValuesImplementation",
                                   OpenMetadataWikiPages.MODEL_0545_REFERENCE_DATA,
                                   "4dc65e59-c376-464e-80bb-88783399e454",
                                   "Link to an asset that implements the list of valid values."),

    /**
     * A link between two valid values from different valid value sets that have equivalent meanings and can be used to translate values from one set to another.
     */
    VALID_VALUES_MAPPING_RELATIONSHIP("203ce62c-3cbf-4542-bf82-81820cba718f",
                                      "ValidValuesMapping",
                                      OpenMetadataWikiPages.MODEL_0545_REFERENCE_DATA,
                                      "b1e75f5d-0c80-49ee-8550-8e1a54b91cb6",
                                      "A link between two valid values from different valid value sets that have equivalent meanings and can be used to translate values from one set to another."),

    /**
     * A detailed description of the effect of some data processing.
     */
    DATA_PROCESSING_DESCRIPTION("685f91fb-c74b-437b-a9b6-c5e557c6d3b2",
                                "DataProcessingDescription",
                                OpenMetadataWikiPages.MODEL_0485_DATA_PROCESSING_PURPOSES,
                                "3c1d6681-5ef6-404a-8876-4ad6d9b48e37",
                                "A detailed description of the effect of some data processing."),

    /**
     * Expected outcome, service or value from processing.
     */
    DATA_PROCESSING_PURPOSE("9062df4c-9f4a-4012-a67a-968d7a3f4bcf",
                            "DataProcessingPurpose",
                            OpenMetadataWikiPages.MODEL_0485_DATA_PROCESSING_PURPOSES,
                            "596481bd-0771-406b-8dd2-bac9487bf565",
                            "Expected outcome, service or value from processing."),

    /**
     * Description of the processing on a single target item.
     */
    DATA_PROCESSING_ACTION("7f53928f-9148-4710-ad37-47633f33cb08",
                           "DataProcessingActionProperties",
                           OpenMetadataWikiPages.MODEL_0485_DATA_PROCESSING_PURPOSES,
                           "85954b5a-ef3e-4bf7-94a6-ce7a24153ef6",
                           "Description of the processing on a single target item."),

    /**
     * Relationship relates data processing descriptions with purposes (outcomes).
     */
    PERMITTED_PROCESSING_RELATIONSHIP("b472a2ec-f419-4d3f-86fb-e9d97365f961",
                                      "PermittedProcessing",
                                      OpenMetadataWikiPages.MODEL_0485_DATA_PROCESSING_PURPOSES,
                                      "0cf13128-42c3-4d22-a3c6-c3ab70f7ae43",
                                      "Relationship relates data processing descriptions with purposes (outcomes)."),

    /**
     * Relationship identifying the proposes that processes/people have permission to process data for.
     */
    APPROVED_PURPOSE_RELATIONSHIP("33ec3aaa-dfb6-4f58-8d5d-c42d077be1b3",
                                  "ApprovedPurpose",
                                  OpenMetadataWikiPages.MODEL_0485_DATA_PROCESSING_PURPOSES,
                                  "f39090c4-c4bb-4594-8a9d-e9d43f3bdb1a",
                                  "Relationship identifying the proposes that processes/people have permission to process data for."),

    /**
     * Relationship identifying the individual actions in a data processing description.
     */
    DETAILED_PROCESSING_ACTION_RELATIONSHIP("0ac0e793-6727-45d2-9403-06bd19d9ce2e",
                                            "DetailedProcessingAction",
                                            OpenMetadataWikiPages.MODEL_0485_DATA_PROCESSING_PURPOSES,
                                            "11d4a348-15ab-4520-b5cc-ddfe9c88559d",
                                            "Relationship identifying the individual actions in a data processing description."),

    /**
     * Relationship identifying the processing being performed by processes or people.
     */
    DATA_PROCESSING_SPECIFICATION_RELATIONSHIP("1dfdec0f-f206-4db7-bac8-ec344205fb3c",
                                               "DataProcessingSpecification",
                                               OpenMetadataWikiPages.MODEL_0485_DATA_PROCESSING_PURPOSES,
                                               "21ba7de7-62ff-442c-aa97-84b8ac4d6291",
                                               "Relationship identifying the processing being performed by processes or people."),

    /**
     * Relationship identifying the actions being performed on data.
     */
    DATA_PROCESSING_TARGET_RELATIONSHIP("6ad18aa4-f5fc-47e7-99e1-80acfc536c9a",
                                        "DataProcessingTarget",
                                        OpenMetadataWikiPages.MODEL_0485_DATA_PROCESSING_PURPOSES,
                                        "bb2dfb94-fa8e-4e01-83a2-8a8558fa8515",
                                        "Relationship identifying the actions being performed on data."),



    /* ============================================================================================================================*/
    /* Area 6 - Metadata Surveys                                                                                                   */
    /* ============================================================================================================================*/


    /**
     * A server capability for running open discovery services.
     */
    OPEN_DISCOVERY_ENGINE("be650674-790b-487a-a619-0a9002488055",
                          "OpenDiscoveryEngine",
                          OpenMetadataWikiPages.MODEL_0601_DISCOVERY_ENGINES,
                          "bde9e812-35a0-49fb-b5ed-9cb1ffa48d60",
                          "A server capability for running open discovery services."),

    /**
     * A pluggable component for discovering properties about an asset.
     */
    OPEN_DISCOVERY_SERVICE("2f278dfc-4640-4714-b34b-303e84e4fc40",
                           "OpenDiscoveryService",
                           OpenMetadataWikiPages.MODEL_0601_DISCOVERY_ENGINES,
                           "38b8d73d-3c60-4e86-8561-256c77e10c16",
                           "A pluggable component for discovering properties about an asset."),

    /**
     * A pluggable component that calls multiple discovery services.
     */
    OPEN_DISCOVERY_PIPELINE("081abe00-740e-4143-b0d5-a1f55450fc22",
                            "OpenDiscoveryPipeline",
                            OpenMetadataWikiPages.MODEL_0601_DISCOVERY_ENGINES,
                            "85a985ab-2f07-4ffc-99d5-d423d787c51e",
                            "A pluggable component that calls multiple discovery services."),

    /**
     * A set of results describing the analysis from the execution of a survey action service.
     */
    SURVEY_REPORT("db9d02a6-11f1-4b6e-86ce-95df2352c3a2",
                  "SurveyReport",
                  OpenMetadataWikiPages.MODEL_0603_SURVEY_REPORTS,
                  "97b45655-4393-499b-a997-589015342284",
                  "A set of results describing the analysis from the execution of a survey action service."),

    /**
     * Link between an Asset and a SurveyReport generated against its associated resource.
     */
    ASSET_SURVEY_REPORT_RELATIONSHIP("0a5572d4-71fe-4a13-beba-e6ece5104799",
                                     "AssetSurveyReport",
                                     OpenMetadataWikiPages.MODEL_0603_SURVEY_REPORTS,
                                     "4b76f90e-3222-480e-9ee3-845f93c72e4f",
                                     "Link between an Asset and a SurveyReport generated against its associated resource."),

    /**
     * Link to a SurveyReport from the EngineAction that initiated the request.
     */
    ENGINE_ACTION_SURVEY_REPORT_RELATIONSHIP("9ee592d0-ee01-4e47-a7b9-fa97ad9f395e",
                                             "EngineActionSurveyReport",
                                             OpenMetadataWikiPages.MODEL_0603_SURVEY_REPORTS,
                                             "efc672a1-4b8f-4197-9038-5eb6ebd7a075",
                                             "Link to a SurveyReport from the EngineAction that initiated the request."),

    /**
     * Link between a SurveyReport and an Annotation generated from the same run of a survey action service.
     */
    REPORTED_ANNOTATION_RELATIONSHIP("3af278ed-f4e8-4afc-851b-a5b0908ba06f",
                                     "ReportedAnnotation",
                                     OpenMetadataWikiPages.MODEL_0610_ANNOTATIONS,
                                     "69b91341-3f40-4e0a-a78d-cc6ff0aa524a",
                                     "Link between a SurveyReport and an Annotation generated from the same run of a survey action service."),

    /**
     * Link between an element and an Annotation that describes a characteristic of its associated real-world counterpart.
     */
    ASSOCIATED_ANNOTATION_RELATIONSHIP("5d4ec403-7417-4146-99da-dd9ea34d4f0a",
                                       "AssociatedAnnotation",
                                       OpenMetadataWikiPages.MODEL_0610_ANNOTATIONS,
                                       "09b7622e-e0ea-4197-8f82-83fe69fb70de",
                                       "Link between an element and an Annotation that describes a characteristic of its associated real-world counterpart."),

    /**
     * A set of results from specific analysis of a resource by a survey action service.
     */
    ANNOTATION("6cea5b53-558c-48f1-8191-11d48db29fb4",
               "Annotation",
               OpenMetadataWikiPages.MODEL_0610_ANNOTATIONS,
               "42de3ec5-b76f-45b7-98b3-7b09d3d4e76a",
               "A set of results from specific analysis of a resource by a survey action service."),

    /**
     * A description of a data field.
     */
    DATA_FIELD("3c5bbc8b-d562-4b04-b189-c7b7f0bf2cea",
               "DataField",
               OpenMetadataWikiPages.MODEL_0616_DATA_FIELD_MODELLING,
               "3ae9c615-b214-46e2-b8d6-2f5eae14e0a3",
               "A description of a data field."),

    /**
     * A collection of properties about a data field, or number of data fields, in an Asset.
     */
    DATA_FIELD_ANNOTATION("72ed6de6-79d9-4e7d-aefc-b969382fc4b0",
                          "DataFieldAnnotation",
                          OpenMetadataWikiPages.MODEL_0610_ANNOTATIONS,
                          "772b7c96-0838-4b96-8547-6e581b93e8d2",
                          "A collection of properties about a data field, or number of data fields, in an Asset."),

    /**
     * Additional information to augment an annotation.
     */
    ANNOTATION_EXTENSION_RELATIONSHIP("605aaa6d-682e-405c-964b-ca6aaa94be1b",
                                      "AnnotationExtension",
                                      OpenMetadataWikiPages.MODEL_0610_ANNOTATIONS,
                                      "8b4fd712-505a-4d9c-a39d-3e23e0cbcd0e",
                                      "Additional information to augment an annotation."),

    /**
     * The results of a stewardship review of an annotation.
     */
    ANNOTATION_REVIEW("b893d6fc-642a-454b-beaf-809ee4dd876a",
                      "AnnotationReview",
                      OpenMetadataWikiPages.MODEL_0610_ANNOTATIONS,
                      "0b625826-4663-44a0-b524-b04e1eddd7d0",
                      "The results of a stewardship review of an annotation."),

    /**
     * Review results for an annotation.
     */
    ANNOTATION_REVIEW_LINK_RELATIONSHIP("5d3c2fb7-fa04-4d77-83cb-fd9216a07769",
                                        "AnnotationReviewLink",
                                        OpenMetadataWikiPages.MODEL_0610_ANNOTATIONS,
                                        "d6e66e06-5f3f-45cc-b6eb-f951b423603b",
                                        "Review results for an annotation."),

    /**
     * A description of the internal structure of an Asset.
     */
    SCHEMA_ANALYSIS_ANNOTATION("3c5aa68b-d562-4b04-b189-c7b7f0bf2ced",
                               "SchemaAnalysisAnnotation",
                               OpenMetadataWikiPages.MODEL_0615_SCHEMA_EXTRACTION,
                               "32dde7be-5c68-41ea-89f4-31b53fa2e9f2",
                               "A description of the internal structure of an Asset."),

    /**
     * Attached data field level annotations.
     */
    DATA_FIELD_ANALYSIS_RELATIONSHIP("833e849d-eda2-40bb-9e6b-c3ca0b56d581",
                                     "DataFieldAnalysis",
                                     OpenMetadataWikiPages.MODEL_0617_DATA_FIELD_ANALYSIS,
                                     "9a845b4c-66be-4443-b750-7f7e6739e8b1",
                                     "Attached data field level annotations."),

    /**
     * Attached data field level annotations.
     */
    CLASSIFICATION_ANNOTATION("23e8287f-5c7e-4e03-8bd3-471fc7fc029c",
                              "ClassificationAnnotation",
                              OpenMetadataWikiPages.MODEL_0635_CLASSIFICATION_DISCOVERY,
                              "6a2b2805-5e59-48d9-af11-92472a3650d4",
                              "Attached data field level annotations."),

    /**
     * A collection of properties that characterize an aspect of a resource.
     */
    RESOURCE_PROFILE_ANNOTATION("bff1f694-afd0-4829-ab11-50a9fbaf2f5f",
                                "ResourceProfileAnnotation",
                                OpenMetadataWikiPages.MODEL_0620_RESOURCE_PROFILING,
                                "e1ba2eb5-bf03-4dc0-ab7e-0d99ba6ece55",
                                "A collection of properties that characterize an aspect of a resource."),

    /**
     * A link to a log file containing profile measures for a resource.
     */
    RESOURCE_PROFILE_LOG_ANNOTATION("368e6fb3-7323-4f81-a723-5182491594bd",
                                    "ResourceProfileLogAnnotation",
                                    OpenMetadataWikiPages.MODEL_0620_RESOURCE_PROFILING,
                                    "016944a4-5a11-4351-9b09-b4e4056d4816",
                                    "A link to a log file containing profile measures for a resource."),

    /**
     * An assessment of the match between a data class and the values stored in a data field, or number of data fields, in a resource.
     */
    DATA_CLASS_ANNOTATION("0c8a3673-04ef-406f-899d-e88de67f6176",
                          "DataClassAnnotation",
                          OpenMetadataWikiPages.MODEL_0625_DATA_CLASS_DISCOVERY,
                          "d0c467cf-6be0-4bf0-9260-6a5aeeee1e52",
                          "An assessment of the match between a data class and the values stored in a data field, or number of data fields, in a resource."),

    /**
     * A recommendation of likely mappings to Glossary Terms for all or part of an Asset.
     */
    SEMANTIC_ANNOTATION("0b494819-28be-4604-b238-3af20963eea6",
                        "SemanticAnnotation",
                        OpenMetadataWikiPages.MODEL_0630_SEMANTIC_DISCOVERY,
                        "231f71f9-300c-453b-aa65-02a155ec5804",
                        "A recommendation of likely mappings to Glossary Terms for all or part of an Asset."),

    /**
     * A calculation of the level of quality found in the values associated with a resource.
     */
    QUALITY_ANNOTATION("72e6473d-4ce0-4609-80a4-e6e949a7f520",
                       "QualityAnnotation",
                       OpenMetadataWikiPages.MODEL_0640_QUALITY_SCORES,
                       "b9a28f99-3f10-480f-9e00-81a552de69ca",
                       "A calculation of the level of quality found in the values associated with a resource."),

    /**
     * A recommendation of the relationships that could be added to all or part of an asset.
     */
    RELATIONSHIP_ADVICE_ANNOTATION("740f07dc-4ee8-4c2a-baba-efb55c73eb68",
                                   "RelationshipAdviceAnnotation",
                                   OpenMetadataWikiPages.MODEL_0650_RELATIONSHIP_DISCOVERY,
                                   "b38ac277-4279-4c6a-bfd6-12b3aeaa0a1e",
                                   "A recommendation of the relationships that could be added to all or part of an Asset."),

    /**
     * Annotation relating two referenceables.
     */
    RELATIONSHIP_ANNOTATION_RELATIONSHIP("73510abd-49e6-4097-ba4b-23bd3ef15baa",
                                         "RelationshipAnnotation",
                                         OpenMetadataWikiPages.MODEL_0650_RELATIONSHIP_DISCOVERY,
                                         "d67a16de-407a-48d0-8011-4f7f3a3e4c85",
                                         "Annotation relating two referenceables."),

    /**
     * A summary set of measurements for a resource.
     */
    RESOURCE_MEASURE_ANNOTATION("c85bea73-d7af-46d7-8a7e-cb745910b1d",
                                "ResourceMeasureAnnotation",
                                OpenMetadataWikiPages.MODEL_0660_MEASUREMENTS,
                                "dbb4a6af-67af-449d-a5a6-b12bf6933955",
                                "A summary set of measurements for a resource."),

    /**
     * A set of summary properties about the physical status of a resource.
     */
    RESOURCE_PHYSICAL_STATUS_ANNOTATION("e9ba276e-6d9f-4999-a5a9-9ddaaabfae23",
                                        "ResourcePhysicalStatusAnnotation",
                                        OpenMetadataWikiPages.MODEL_0660_MEASUREMENTS,
                                        "109a8865-2ead-452e-b72f-9d9e09b2763f",
                                        "A set of summary properties about the physical status of a resource."),

    /**
     * A request for a stewardship action to be initiated against an element.
     */
    REQUEST_FOR_ACTION_ANNOTATION("f45765a9-f3ae-4686-983f-602c348e020d",
                                  "RequestForAction",
                                  OpenMetadataWikiPages.MODEL_0690_REQUEST_FOR_ACTION,
                                  "2a94d1d6-ead8-4d57-a419-90c8704a27a2",
                                  "A request for a stewardship action to be initiated against an element."),

    /**
     * A link to the element that should be acted upon by the resulting action.
     */
    REQUEST_FOR_ACTION_TARGET("b6943670-93aa-4ce5-a00a-a50581de997d",
                                  "RequestForActionTarget",
                                  OpenMetadataWikiPages.MODEL_0690_REQUEST_FOR_ACTION,
                                  "c2d0f01b-25f8-412f-abb1-2e88c9fba1e4",
                                  "A link to the element that should be acted upon by the resulting action."),

    /**
     * Link to the external data resource containing the surveyed resource's profile data.
     */
    RESOURCE_PROFILE_DATA_RELATIONSHIP("2ebf2c09-b272-42a1-8fc0-e3eb44df296d",
                                       "ResourceProfileData",
                                       OpenMetadataWikiPages.MODEL_0620_RESOURCE_PROFILING,
                                       "11050162-ed05-4e5a-8a72-872c50001b5b",
                                       "Link to the external data resource containing the surveyed resource's profile data."),


    /* ============================================================================================================================*/
    /* Area 7 - Lineage and Data Products                                                                                          */
    /* ============================================================================================================================*/


    /**
     * Identifies an element that represents a digital product.
     */
    DIGITAL_PRODUCT_CLASSIFICATION("4aaaa7ca-6b4b-4c4b-997f-d5dfd42917b0",
                                   "DigitalProduct",
                                   OpenMetadataWikiPages.MODEL_0710_DIGITAL_SERVICE,
                                   "6751673f-a4e7-4b64-84e4-4c59163d0102",
                                   "Identifies an element that represents a digital product."),


    /**
     * Shows that data flows in one direction from one element to another.
     */
    DATA_FLOW("d2490c0c-06cc-458a-add2-33cf2f5dd724",
              "DataFlow",
              OpenMetadataWikiPages.MODEL_0750_DATA_PASSING,
              "e965ab1e-5c04-44a9-b301-3b359e6f169f",
              "Shows that data flows in one direction from one element to another."),

    /**
     * Shows that when one element completes processing, control passes to the next element.
     */
    CONTROL_FLOW("35450726-1c32-4d41-b928-22db6d1ae2f4",
                 "ControlFlow",
                 OpenMetadataWikiPages.MODEL_0750_DATA_PASSING,
                 "ce664de9-b65e-443e-bf58-15e8e44503d3",
                 "Shows that when one element completes processing, control passes to the next element."),

    /**
     * Shows a request-response call between two elements.
     */
    PROCESS_CALL("af904501-6347-4f52-8378-da50e8d74828",
                 "ProcessCall",
                 OpenMetadataWikiPages.MODEL_0750_DATA_PASSING,
                 "e5892596-1be2-4a22-9fb0-2aae6627f127",
                 "Shows a request-response call between two elements."),

    /**
     * Links a node in the lineage graph to its ultimate source - ie the node at the start of the lineage data flow.
     */
    ULTIMATE_SOURCE("e5649e7a-4d97-4a41-a91d-20f521f961aa",
                    "UltimateSource",
                    OpenMetadataWikiPages.MODEL_0755_ULTIMATE_SOURCE_DESTINATION,
                    "f85c52e9-cbfa-4e8c-9a54-17c0e0576cef",
                    "Links a node in the lineage graph to its ultimate source - ie the node at the start of the lineage data flow."),

    /**
     * Links a node in the lineage graph to its ultimate destination - ie the node at the end of the lineage data flow.
     */
    ULTIMATE_DESTINATION("27d48f4a-a5bd-4320-a4ba-55f03adbb27b",
                         "UltimateDestination",
                         OpenMetadataWikiPages.MODEL_0755_ULTIMATE_SOURCE_DESTINATION,
                         "f8e24bca-3fe7-48f6-af28-48a1cc9f18e7",
                         "Links a node in the lineage graph to its ultimate destination - ie the node at the end of the lineage data flow."),

    /**
     * A lineage stitching link between two equivalent elements
     */
    LINEAGE_MAPPING("a5991bB2-660D-A3a1-2955-fAcDA2d5F4Ff",
                    "LineageMapping",
                    OpenMetadataWikiPages.MODEL_0770_LINEAGE_MAPPING,
                    "a1c53199-2c7c-4709-8e64-6ba1b303d5e3",
                    "A lineage stitching link between two equivalent elements"),

    ;


    private final static Map<String, String> openMetadataTypeGUIDs = new HashMap<>();

    static
    {
        for (OpenMetadataType openMetadataType : OpenMetadataType.values())
        {
            openMetadataTypeGUIDs.put(openMetadataType.typeName, openMetadataType.descriptionGUID);
        }
    }


    /**
     * Return the description GUID for the named type.
     *
     * @param typeName name of type
     * @return description GUID
     */
    public static String getDescriptionGUIDForType(String typeName)
    {
        return openMetadataTypeGUIDs.get(typeName);
    }


    public final String typeGUID;
    public final String typeName;
    public final String wikiURL;
    public final String descriptionGUID;
    public final String description;

    OpenMetadataType(String typeGUID,
                     String typeName,
                     String wikiURL,
                     String descriptionGUID,
                     String description)
    {
        this.typeGUID        = typeGUID;
        this.typeName        = typeName;
        this.wikiURL         = wikiURL;
        this.descriptionGUID = descriptionGUID;
        this.description     = description;
    }


    /* ============================================================================================================================*/
    /* Area 2 - Assets                                                                                                             */
    /* ============================================================================================================================*/


    /**
     * 740e76e1-77b4-4426-ad52-d0a4ed15fff9
     */
    public static final String DATA_FIELD_VALUES_CLASSIFICATION_GUID = "740e76e1-77b4-4426-ad52-d0a4ed15fff9";

    /**
     * DataFieldValues
     */
    public static final String DATA_FIELD_VALUES_CLASSIFICATION_NAME = "DataFieldValues";

    /**
     * defaultValue
     */
    public static final String DEFAULT_VALUE_PROPERTY_NAME = "defaultValue";      /* from DataFieldValues classification */

    /**
     * sampleValues
     */
    public static final String SAMPLE_VALUES_PROPERTY_NAME = "sampleValues";      /* from DataFieldValues classification */

    /**
     * dataPattern
     */
    public static final String DATA_PATTERN_PROPERTY_NAME = "dataPattern";       /* from DataFieldValues classification */

    /**
     * namePattern
     */
    public static final String NAME_PATTERN_PROPERTY_NAME = "namePattern";       /* from DataFieldValues classification */

    /**
     * minLongitude
     */
    public static final String MIN_LONGITUDE_PROPERTY_NAME = "minLongitude";

    /**
     * minLatitude
     */
    public static final String MIN_LATITUDE_PROPERTY_NAME = "minLatitude";

    /**
     * maxLongitude
     */
    public static final String MAX_LONGITUDE_PROPERTY_NAME = "maxLongitude";

    /**
     * maxLatitude
     */
    public static final String MAX_LATITUDE_PROPERTY_NAME = "maxLatitude";

    /**
     * minHeight
     */
    public static final String MIN_HEIGHT_PROPERTY_NAME = "minHeight";

    /**
     * maxHeight
     */
    public static final String MAX_HEIGHT_PROPERTY_NAME = "maxHeight";


    /* ============================================================================================================================*/
    /* Area 3 - Glossary                                                                                                           */
    /* ============================================================================================================================*/

    public static final String GLOSSARY_TYPE_GUID = "36f66863-9726-4b41-97ee-714fd0dc6fe4";
    public static final String GLOSSARY_TYPE_NAME = "Glossary";               /* from Area 3 */
    /* Referenceable */

    public static final String LANGUAGE_PROPERTY_NAME = "language";      /* from Glossary entity*/

    public static final String TAXONOMY_CLASSIFICATION_TYPE_GUID = "37116c51-e6c9-4c37-942e-35d48c8c69a0";
    public static final String TAXONOMY_CLASSIFICATION_TYPE_NAME = "Taxonomy";               /* from Area 3 */

    public static final String ORGANIZING_PRINCIPLE_PROPERTY_NAME = "organizingPrinciple";  /* from Taxonomy classification */

    public static final String CANONICAL_VOCAB_CLASSIFICATION_TYPE_GUID = "33ad3da2-0910-47be-83f1-daee018a4c05";
    public static final String CANONICAL_VOCAB_CLASSIFICATION_TYPE_NAME = "CanonicalVocabulary";               /* from Area 3 */

    public static final String SCOPE_PROPERTY_NAME = "scope";  /* from CanonicalVocabulary classification */

    public static final String EXTERNAL_GLOSSARY_LINK_TYPE_GUID = "183d2935-a950-4d74-b246-eac3664b5a9d";
    public static final String EXTERNAL_GLOSSARY_LINK_TYPE_NAME = "ExternalGlossaryLink";               /* from Area 3 */
    /* ExternalReference */

    public static final String EXTERNALLY_SOURCED_GLOSSARY_TYPE_GUID = "7786a39c-436b-4538-acc7-d595b5856add";
    public static final String EXTERNALLY_SOURCED_GLOSSARY_TYPE_NAME = "ExternallySourcedGlossary";     /* from Area 3 */

    public static final String GLOSSARY_CATEGORY_TYPE_GUID = "e507485b-9b5a-44c9-8a28-6967f7ff3672";
    public static final String GLOSSARY_CATEGORY_TYPE_NAME = "GlossaryCategory";       /* from Area 3 */
    /* Referenceable */

    public static final String ROOT_CATEGORY_CLASSIFICATION_TYPE_GUID = "1d0fec82-7444-4e4c-abd4-4765bb855ce3";
    public static final String ROOT_CATEGORY_CLASSIFICATION_TYPE_NAME = "RootCategory";

    public static final String CATEGORY_ANCHOR_TYPE_GUID = "c628938e-815e-47db-8d1c-59bb2e84e028";
    public static final String CATEGORY_ANCHOR_TYPE_NAME = "CategoryAnchor";     /* from Area 3 */

    public static final String CATEGORY_HIERARCHY_TYPE_GUID = "71e4b6fb-3412-4193-aff3-a16eccd87e8e";
    public static final String CATEGORY_HIERARCHY_TYPE_NAME = "CategoryHierarchyLink";     /* from Area 3 */

    public static final String LIBRARY_CATEGORY_REFERENCE_TYPE_GUID = "3da21cc9-3cdc-4d87-89b5-c501740f00b2e";
    public static final String LIBRARY_CATEGORY_REFERENCE_TYPE_NAME = "LibraryCategoryReference";     /* from Area 3 */

    public static final String LAST_VERIFIED_PROPERTY_NAME = "lastVerified";       /* from LibraryCategoryReference and LibraryTermReference entity */

    public static final String GLOSSARY_TERM_TYPE_GUID = "0db3e6ec-f5ef-4d75-ae38-b7ee6fd6ec0a";
    public static final String GLOSSARY_TERM_TYPE_NAME = "GlossaryTerm";           /* from Area 3 */
    /* Referenceable */

    public static final String CONTROLLED_GLOSSARY_TERM_TYPE_GUID = "c04e29b2-2d66-48fc-a20d-e59895de6040";
    public static final String CONTROLLED_GLOSSARY_TERM_TYPE_NAME = "ControlledGlossaryTerm";           /* from Area 3 */
    /* GlossaryTerm */

    public static final String EDITING_GLOSSARY_CLASSIFICATION_TYPE_GUID = "173614ba-c582-4ecc-8fcc-cde5fb664548";
    public static final String EDITING_GLOSSARY_CLASSIFICATION_TYPE_NAME = "EditingGlossary";           /* from Area 3 */
    /* Glossary */

    public static final String STAGING_GLOSSARY_CLASSIFICATION_TYPE_GUID = "361fa044-e703-404c-bb83-9402f9221f54";
    public static final String STAGING_GLOSSARY_CLASSIFICATION_TYPE_NAME = "StagingGlossary";       /* from Area 3 */
    /* Glossary */

    public static final String TERM_RELATIONSHIP_STATUS_ENUM_TYPE_GUID = "42282652-7d60-435e-ad3e-7cfe5291bcc7";
    public static final String TERM_RELATIONSHIP_STATUS_ENUM_TYPE_NAME = "TermRelationshipStatus";     /* from Area 3 */


    public static final String RELATED_TERM_RELATIONSHIP_GUID = "b1161696-e563-4cf9-9fd9-c0c76e47d063";
    public static final String RELATED_TERM_RELATIONSHIP_NAME = "RelatedTerm";

    public static final String SYNONYM_RELATIONSHIP_GUID = "74f4094d-dba2-4ad9-874e-d422b69947e2";
    public static final String SYNONYM_RELATIONSHIP_NAME = "Synonym";

    public static final String ANTONYM_RELATIONSHIP_GUID = "ea5e126a-a8fa-4a43-bcfa-309a98aa0185";
    public static final String ANTONYM_RELATIONSHIP_NAME = "Antonym";

    public static final String PREFERRED_TERM_RELATIONSHIP_GUID = "8ac8f9de-9cdd-4103-8a33-4cb204b78c2a";
    public static final String PREFERRED_TERM_RELATIONSHIP_NAME = "PreferredTerm";

    public static final String REPLACEMENT_TERM_RELATIONSHIP_GUID = "3bac5f35-328b-4bbd-bfc9-3b3c9ba5e0ed";
    public static final String REPLACEMENT_TERM_RELATIONSHIP_NAME = "ReplacementTerm";

    public static final String TRANSLATION_RELATIONSHIP_GUID = "6ae42e95-efc5-4256-bfa8-801140a29d2a";
    public static final String TRANSLATION_RELATIONSHIP_NAME = "Translation";

    public static final String ISA_RELATIONSHIP_GUID = "50fab7c7-68bc-452f-b8eb-ec76829cac85";
    public static final String ISA_RELATIONSHIP_NAME = "ISARelationship";

    public static final String VALID_VALUE_RELATIONSHIP_GUID = "707a156b-e579-4482-89a5-de5889da1971";
    public static final String VALID_VALUE_RELATIONSHIP_NAME = "ValidValue";

    public static final String USED_IN_CONTEXT_RELATIONSHIP_GUID = "2dc524d2-e29f-4186-9081-72ea956c75de";
    public static final String USED_IN_CONTEXT_RELATIONSHIP_NAME = "UsedInContext";


    public static final String TERM_ANCHOR_TYPE_GUID = "1d43d661-bdc7-4a91-a996-3239b8f82e56";
    public static final String TERM_ANCHOR_TYPE_NAME = "TermAnchor";     /* from Area 3 */

    public static final String TERM_CATEGORIZATION_TYPE_GUID = "696a81f5-ac60-46c7-b9fd-6979a1e7ad27";
    public static final String TERM_CATEGORIZATION_TYPE_NAME = "TermCategorization";     /* from Area 3 */

    public static final String ABSTRACT_CONCEPT_CLASSIFICATION_TYPE_GUID = "9d725a07-4abf-4939-a268-419d200b69c2";
    public static final String ABSTRACT_CONCEPT_CLASSIFICATION_TYPE_NAME = "AbstractConcept";   /* from Area 3 */
    /* GlossaryTerm */

    public static final String DATA_VALUE_CLASSIFICATION_TYPE_GUID = "ab253e31-3d8a-45a7-8592-24329a189b9e";
    public static final String DATA_VALUE_CLASSIFICATION_TYPE_NAME = "DataValue";   /* from Area 3 */
    /* GlossaryTerm */

    public static final String CONTEXT_DEFINITION_CLASSIFICATION_TYPE_GUID = "54f9f41a-3871-4650-825d-59a41de01330e";
    public static final String CONTEXT_DEFINITION_CLASSIFICATION_TYPE_NAME = "ContextDefinition";   /* from Area 3 */
    /* GlossaryTerm */

    public static final String SPINE_OBJECT_CLASSIFICATION_TYPE_GUID = "a41ee152-de1e-4533-8535-2f8b37897cac";
    public static final String SPINE_OBJECT_CLASSIFICATION_TYPE_NAME = "SpineObject";   /* from Area 3 */
    /* GlossaryTerm */

    public static final String SPINE_ATTRIBUTE_CLASSIFICATION_TYPE_GUID = "ccb749ba-34ec-4f71-8755-4d8b383c34c3";
    public static final String SPINE_ATTRIBUTE_CLASSIFICATION_TYPE_NAME = "SpineAttribute";   /* from Area 3 */
    /* GlossaryTerm */

    public static final String OBJECT_IDENTIFIER_CLASSIFICATION_TYPE_GUID = "3d1e4389-27de-44fa-8df4-d57bfaf809ea";
    public static final String OBJECT_IDENTIFIER_CLASSIFICATION_TYPE_NAME = "ObjectIdentifier";   /* from Area 3 */
    /* GlossaryTerm */

    public static final String TERM_HAS_A_RELATIONSHIP_GUID = "d67f16d1-5348-419e-ba38-b0bb6fe4ad6c";
    public static final String TERM_HAS_A_RELATIONSHIP_NAME = "TermHASARelationship";

    public static final String TERM_IS_A_TYPE_OF_RELATIONSHIP_GUID = "71f83296-2007-46a5-a4c7-919a7c4a12f5";
    public static final String TERM_IS_A_TYPE_OF_RELATIONSHIP_NAME = "TermISATYPEOFRelationship";

    public static final String TERM_TYPED_BY_RELATIONSHIP_GUID = "669e8aa4-c671-4ee7-8d03-f37d09b9d006";
    public static final String TERM_TYPED_BY_RELATIONSHIP_NAME = "TermTYPEDBYRelationship";


    public static final String USER_DEFINED_STATUS_PROPERTY_NAME   = "userDefinedStatus";
    public static final String CREATED_BY_PROPERTY_NAME            = "createdBy";


    public static final String ELEMENT_SUPPLEMENT_CLASSIFICATION_TYPE_GUID = "58520015-ce6e-47b7-a1fd-864030544819";
    public static final String ELEMENT_SUPPLEMENT_CLASSIFICATION_TYPE_NAME = "ElementSupplement";   /* from Area 3 */
    /* Referencable */

    public static final String SUPPLEMENTARY_PROPERTIES_TYPE_GUID = "2bb10ba5-7aa2-456a-8b3a-8fdbd75c95cd";
    public static final String SUPPLEMENTARY_PROPERTIES_TYPE_NAME = "SupplementaryProperties";  /* from Area 3 */
    /* End1 = Referenceable; End 2 = GlossaryTerm */


    /* ============================================================================================================================*/
    /* Area 4 - Governance                                                                                                         */
    /* ============================================================================================================================*/

    public static final String GOVERNANCE_DOMAIN_DESCRIPTION_TYPE_GUID = "084cd115-5d0d-4f12-8093-697526a120ea";
    public static final String GOVERNANCE_DOMAIN_DESCRIPTION_TYPE_NAME = "GovernanceDomainDescription";
    /* Referenceable */

    public static final String GOVERNANCE_DOMAIN_SET_CLASSIFICATION_NAME = "GovernanceDomainSet";

    public static final String GOVERNANCE_ROLE_TYPE_GUID = "de2d7f2e-1759-44e3-b8a6-8af53e8fb0ee";
    public static final String GOVERNANCE_ROLE_TYPE_NAME = "GovernanceRole";
    /* PersonRole */

    public static final String GOVERNANCE_OFFICER_TYPE_GUID = "578a3500-9ad3-45fe-8ada-e4e9572c37c8";
    public static final String GOVERNANCE_OFFICER_TYPE_NAME = "GovernanceOfficer";
    /* GovernanceRole */

    /* GovernanceRole */

    public static final String GOVERNANCE_DEFINITION_TYPE_GUID = "578a3500-9ad3-45fe-8ada-e4e9572c37c8";
    public static final String GOVERNANCE_DEFINITION_TYPE_NAME = "GovernanceDefinition";
    /* Referenceable */



    public static final String GOVERNANCE_RESPONSIBILITY_TYPE_GUID = "89a76b24-deb8-45bf-9304-a578a610326f";
    public static final String GOVERNANCE_RESPONSIBILITY_TYPE_NAME = "GovernanceResponsibility";
    /* OrganizationalControl */

    public static final String GOVERNED_BY_TYPE_GUID = "89c3c695-9e8d-4660-9f44-ed971fd55f89";
    public static final String GOVERNED_BY_TYPE_NAME = "GovernedBy";  /* from Area 4 */
    /* End1 = GovernanceDefinition; End 2 = Referenceable */

    public static final String GOVERNANCE_DEFINITION_SCOPE_TYPE_GUID = "3845b5cc-8c85-462f-b7e6-47472a568793";
    public static final String GOVERNANCE_DEFINITION_SCOPE_TYPE_NAME = "GovernanceDefinitionScope";  /* from Area 4 */
    /* End1 = Referenceable; End 2 = GovernanceDefinition */

    public static final String GOVERNANCE_RESPONSE_TYPE_GUID = "8845990e-7fd9-4b79-a19d-6c4730dadd6b";
    public static final String GOVERNANCE_RESPONSE_TYPE_NAME = "GovernanceResponse";  /* from Area 4 */
    /* End1 = GovernanceDriver; End 2 = GovernancePolicy */

    public static final String GOVERNANCE_DRIVER_LINK_TYPE_NAME = "GovernanceDriverLink";  /* from Area 4 */
    /* End1 = GovernanceDriver; End 2 = GovernanceDriver */

    public static final String GOVERNANCE_POLICY_LINK_TYPE_GUID = "0c42c999-4cac-4da4-afab-0e381f3a818e";
    public static final String GOVERNANCE_POLICY_LINK_TYPE_NAME = "GovernancePolicyLink";  /* from Area 4 */
    /* End1 = GovernancePolicy; End 2 = GovernancePolicy */

    public static final String GOVERNANCE_IMPLEMENTATION_TYPE_GUID = "787eaf46-7cf2-4096-8d6e-671a0819d57e";
    public static final String GOVERNANCE_IMPLEMENTATION_TYPE_NAME = "GovernanceImplementation";  /* from Area 4 */
    /* End1 = GovernancePolicy; End 2 = GovernanceControl */

    public static final String GOVERNANCE_CONTROL_LINK_TYPE_GUID = "806933fb-7925-439b-9876-922a960d2ba1";
    public static final String GOVERNANCE_CONTROL_LINK_TYPE_NAME = "GovernanceControlLink";  /* from Area 4 */
    /* End1 = GovernanceControl; End 2 = GovernanceControl */

    public static final String GOVERNANCE_ROLE_ASSIGNMENT_TYPE_GUID = "cb10c107-b7af-475d-aab0-d78b8297b982";
    public static final String GOVERNANCE_ROLE_ASSIGNMENT_TYPE_NAME = "GovernanceRoleAssignment";    /* from Area 4 */
    /* End1 = Referenceable; End 2 = PersonRole */

    public static final String GOVERNANCE_RESPONSIBILITY_ASSIGNMENT_TYPE_GUID = "cb15c107-b7af-475d-aab0-d78b8297b982";
    public static final String GOVERNANCE_RESPONSIBILITY_ASSIGNMENT_TYPE_NAME = "GovernanceResponsibilityAssignment";    /* from Area 4 */
    /* End1 = PersonRole; End 2 = GovernanceResponsibility */

    public static final String DOMAIN_IDENTIFIER_PROPERTY_NAME = "domainIdentifier";          /* from many governance entities */
    public static final String CRITERIA_PROPERTY_NAME          = "criteria";                  /* from many governance entities */

    public static final String IMPLICATIONS_PROPERTY_NAME               = "implications";              /* from GovernanceDefinition entity */
    public static final String OUTCOMES_PROPERTY_NAME                   = "outcomes";                  /* from GovernanceDefinition entity */
    public static final String RESULTS_PROPERTY_NAME                    = "results";                   /* from GovernanceDefinition entity */
    public static final String BUSINESS_IMPERATIVES_PROPERTY_NAME       = "businessImperatives";       /* from GovernanceStrategy entity */
    public static final String JURISDICTION_PROPERTY_NAME               = "jurisdiction";              /* from Regulation entity */
    public static final String IMPLEMENTATION_DESCRIPTION_PROPERTY_NAME = "implementationDescription"; /* from GovernanceControl entity */
    public static final String NOTES_PROPERTY_NAME                      = "notes";                     /* from multiple entities */
    public static final String ATTRIBUTE_NAME_PROPERTY_NAME             = "attributeName";             /* from ReferenceValueAssignment relationship */
    public static final String RATIONALE_PROPERTY_NAME                  = "rationale";                 /* from GovernanceResponse, GovernanceImplementation relationship */

    public static final String GOVERNANCE_PROJECT_CLASSIFICATION_TYPE_GUID = "37142317-4125-4046-9514-71dc5031563f";
    public static final String GOVERNANCE_PROJECT_CLASSIFICATION_TYPE_NAME = "GovernanceProject";
    /* Attached to Project */


    public static final String LEVEL_IDENTIFIER_PROPERTY_NAME    = "levelIdentifier";           /* from many governance entities and classifications */



    public static final String RETENTION_ASSOCIATED_GUID_PROPERTY_NAME = "associatedGUID";
    public static final String RETENTION_ARCHIVE_AFTER_PROPERTY_NAME   = "archiveAfter";
    public static final String RETENTION_DELETE_AFTER_PROPERTY_NAME    = "deleteAfter";

    public static final String SECURITY_GROUP_TYPE_GUID = "042d9b5c-677e-477b-811f-1c39bf716759";
    public static final String SECURITY_GROUP_TYPE_NAME = "SecurityGroup";
    /* TechnicalControl */
    public static final String GROUPS_PROPERTY_NAME                               = "groups";

    public static final String SECURITY_TAGS_CLASSIFICATION_TYPE_GUID = "a0b07a86-9fd3-40ca-bb9b-fe83c6981deb";
    public static final String SECURITY_TAGS_CLASSIFICATION_TYPE_NAME = "SecurityTags";
    public static final String SECURITY_LABELS_PROPERTY_NAME          = "securityLabels";
    public static final String SECURITY_PROPERTIES_PROPERTY_NAME      = "securityProperties";
    public static final String ACCESS_GROUPS_PROPERTY_NAME            = "accessGroups";


    public static final String ZONE_TYPE_GUID = "290a192b-42a7-449a-935a-269ca62cfdac";
    public static final String ZONE_TYPE_NAME = "GovernanceZone";
    /* Referenceable */

    public static final String ZONE_NAME_PROPERTY_NAME = "zoneName";

    public static final String ZONE_HIERARCHY_TYPE_GUID = "ee6cf469-cb4d-4c3b-a4c7-e2da1236d139";
    public static final String ZONE_HIERARCHY_TYPE_NAME = "ZoneHierarchy";  /* from Area 4 */
    /* End1 = Parent Zone; End 2 = Child Zone */

    public static final String SUBJECT_AREA_TYPE_GUID = "d28c3839-bc6f-41ad-a882-5667e01fea72";
    public static final String SUBJECT_AREA_TYPE_NAME = "SubjectAreaDefinition";
    /* Referenceable */

    public static final String SUBJECT_AREA_NAME_PROPERTY_NAME = "subjectAreaName";

    public static final String SUBJECT_AREA_HIERARCHY_TYPE_GUID = "fd3b7eaf-969c-4c26-9e1e-f31c4c2d1e4b";
    public static final String SUBJECT_AREA_HIERARCHY_TYPE_NAME = "SubjectAreaHierarchy";  /* from Area 4 */
    /* End1 = Parent Subject Area; End 2 = Child Subject Area */

    public static final String SUBJECT_AREA_CLASSIFICATION_TYPE_GUID = "480e6993-35c5-433a-b50b-0f5c4063fb5d";
    public static final String SUBJECT_AREA_CLASSIFICATION_TYPE_NAME = "SubjectArea";
    /* Referenceable */

    public static final String ORGANIZATION_TYPE_GUID = "50a61105-35be-4ee3-8b99-bdd958ed0685";
    public static final String ORGANIZATION_TYPE_NAME = "Organization";                /* from Area 4 */
    /* Team */

    public static final String BUSINESS_CAPABILITY_TYPE_GUID = "7cc6bcb2-b573-4719-9412-cf6c3f4bbb15";
    public static final String BUSINESS_CAPABILITY_TYPE_NAME = "BusinessCapability";          /* from Area 4 */
    /* Referenceable */

    public static final String ORGANIZATIONAL_CAPABILITY_TYPE_GUID = "47f0ad39-db77-41b0-b406-36b1598e0ba7";
    public static final String ORGANIZATIONAL_CAPABILITY_TYPE_NAME = "OrganizationalCapability";    /* from Area 4 */
    /* End1 = BusinessCapability; End 2 = Team */

    public static final String BUSINESS_CAPABILITY_CONTROLS_TYPE_GUID = "b5de932a-738c-4c69-b852-09fec2b9c678";
    public static final String BUSINESS_CAPABILITY_CONTROLS_TYPE_NAME = "BusinessCapabilityControls";  /* from Area 4 */
    /* End1 = GovernanceControl; End 2 = BusinessCapability */

    public static final String ASSET_ORIGIN_CLASSIFICATION_GUID = "e530c566-03d2-470a-be69-6f52bfbd5fb7";
    public static final String ASSET_ORIGIN_CLASSIFICATION_NAME = "AssetOrigin";

    public static final String ORGANIZATION_PROPERTY_NAME                      = "organization";                          /* from AssetOrigin classification */
    public static final String ORGANIZATION_PROPERTY_NAME_PROPERTY_NAME        = "organizationPropertyName";              /* from AssetOrigin classification */
    public static final String BUSINESS_CAPABILITY_PROPERTY_NAME               = "businessCapability";                    /* from AssetOrigin classification */
    public static final String BUSINESS_CAPABILITY_PROPERTY_NAME_PROPERTY_NAME = "businessCapabilityPropertyName";  /* from AssetOrigin classification */
    public static final String OTHER_ORIGIN_VALUES_PROPERTY_NAME               = "otherOriginValues";                     /* from AssetOrigin classification */

    public static final String BUSINESS_CAPABILITY_TYPE_PROPERTY_NAME = "businessCapabilityType";                /* from BusinessCapability entity */

    public static final String BUSINESS_CAPABILITY_TYPE_ENUM_TYPE_NAME = "BusinessCapabilityType";

    public static final int BUSINESS_CAPABILITY_TYPE_UNCLASSIFIED     = 0;
    public static final int BUSINESS_CAPABILITY_TYPE_BUSINESS_SERVICE = 1;
    public static final int BUSINESS_CAPABILITY_TYPE_BUSINESS_AREA    = 2;
    public static final int BUSINESS_CAPABILITY_TYPE_OTHER            = 99;

    public static final String ASSET_ZONES_CLASSIFICATION_GUID = "a1c17a86-9fd3-40ca-bb9b-fe83c6981deb";
    public static final String ASSET_ZONES_CLASSIFICATION_NAME = "AssetZoneMembership";

    public static final String GOVERNANCE_METRIC_TYPE_GUID = "9ada8e7b-823c-40f7-adf8-f164aabda77e";
    public static final String GOVERNANCE_METRIC_TYPE_NAME = "GovernanceMetric";
    /* Referenceable */

    public static final String MEASUREMENT_PROPERTY_NAME = "measurement";            /* from Area 4 */
    public static final String TARGET_PROPERTY_NAME      = "target";                 /* from Area 4 */

    public static final String GOVERNANCE_RESULTS_TYPE_GUID = "89c3c695-9e8d-4660-9f44-ed971fd55f88";
    public static final String GOVERNANCE_RESULTS_TYPE_NAME = "GovernanceResults";
    /* End1 = GovernanceMetric; End 2 = DataSet */

    public static final String GOVERNANCE_DEFINITION_METRIC_TYPE_GUID = "e076fbb3-54f5-46b8-8f1e-a7cb7e792673";
    public static final String GOVERNANCE_DEFINITION_METRIC_TYPE_NAME = "GovernanceDefinitionMetric";
    /* End1 = GovernanceDefinition; End 2 = GovernanceMetric */

    public static final String GOVERNANCE_EXPECTATIONS_CLASSIFICATION_TYPE_GUID = "fcda7261-865d-464d-b279-7d9880aaab39";
    public static final String GOVERNANCE_EXPECTATIONS_CLASSIFICATION_TYPE_NAME = "GovernanceExpectations";
    /* Referenceable */

    public static final String COUNTS_PROPERTY_NAME = "counts";                 /* from Area 4 */
    public static final String VALUES_PROPERTY_NAME = "values";                 /* from Area 4 */
    public static final String FLAGS_PROPERTY_NAME  = "flags";                  /* from Area 4 */


    public static final String GOVERNANCE_MEASUREMENTS_CLASSIFICATION_TYPE_GUID = "9d99d962-0214-49ba-83f7-c9b1f9f5bed4";
    public static final String GOVERNANCE_MEASUREMENTS_CLASSIFICATION_TYPE_NAME = "GovernanceMeasurements";
    /* Referenceable */

    public static final String MEASUREMENT_COUNTS_PROPERTY_NAME = "measurementCounts";                 /* from Area 4 */
    public static final String MEASUREMENT_VALUES_PROPERTY_NAME = "measurementValues";                 /* from Area 4 */
    public static final String MEASUREMENT_FLAGS_PROPERTY_NAME  = "measurementFlags";                  /* from Area 4 */

    public static final String GOVERNANCE_MEASUREMENTS_DATA_SET_CLASSIFICATION_TYPE_GUID = "789f2e89-accd-4489-8eca-dc43b432c022";
    public static final String GOVERNANCE_MEASUREMENTS_DATA_SET_CLASSIFICATION_TYPE_NAME = "GovernanceMeasurementsResultsDataSet";
    /* Referenceable */





    public static final String POINT_TYPE_PROPERTY_NAME = "pointType";                                /* from Area 4 */


    public static final String GOVERNANCE_ACTION_PROCESS_TYPE_GUID = "4d3a2b8d-9e2e-4832-b338-21c74e45b238";
    public static final String GOVERNANCE_ACTION_PROCESS_TYPE_NAME = "GovernanceActionProcess";
    /* Process */

    public static final String GOVERNANCE_ACTION_TYPE_TYPE_GUID = "92e20083-0393-40c0-a95b-090724a91ddc";
    public static final String GOVERNANCE_ACTION_TYPE_TYPE_NAME = "GovernanceActionType";
    /* Referenceable */

    public static final String GOVERNANCE_ACTION_PROCESS_STEP_TYPE_GUID = "7ae66fcc-09be-4d16-b39f-b5e299473586";
    public static final String GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME = "GovernanceActionProcessStep";
    /* GovernanceActionType */


    public static final String SUPPORTED_GUARDS_PROPERTY_NAME            = "supportedGuards";             /* from GovernanceActionType entity */
    public static final String REQUIRED_REQUEST_PARAMETERS_PROPERTY_NAME = "requiredRequestParameters";   /* from GovernanceActionType entity */
    public static final String REQUIRED_ACTION_TARGETS_PROPERTY_NAME     = "requiredActionTargets";       /* from GovernanceActionType entity */
    public static final String PRODUCED_REQUEST_PARAMETERS_PROPERTY_NAME = "producedRequestParameters";   /* from GovernanceActionType entity */
    public static final String PRODUCED_ACTION_TARGETS_PROPERTY_NAME     = "producedActionTargets";       /* from GovernanceActionType entity */
    public static final String PRODUCED_GUARDS_PROPERTY_NAME             = "producedGuards";              /* from GovernanceActionType entity */

    public static final String GOVERNANCE_ACTION_EXECUTOR_TYPE_GUID = "f672245f-35b5-4ca7-b645-014cf61d5b75";
    public static final String GOVERNANCE_ACTION_EXECUTOR_TYPE_NAME = "GovernanceActionExecutor";
    /* End1 = GovernanceActionType; End 2 = GovernanceEngine */

    public static final String NEXT_GOVERNANCE_ACTION_PROCESS_STEP_TYPE_GUID = "d9567840-9904-43a5-990b-4585c0446e00";
    public static final String NEXT_GOVERNANCE_ACTION_PROCESS_STEP_TYPE_NAME = "NextGovernanceActionProcessStep";
    /* End1 = GovernanceActionProcessStep; End 2 = GovernanceActionProcessStep */

    public static final String GOVERNANCE_ACTION_PROCESS_FLOW_TYPE_GUID = "5f6ddee5-31ea-4d4f-9c3f-00ad2fcb2aa0";
    public static final String GOVERNANCE_ACTION_PROCESS_FLOW_TYPE_NAME = "GovernanceActionProcessFlow";
    /* End1 = GovernanceActionProcess; End 2 = GovernanceActionProcessStep */

    public static final String GUARD_PROPERTY_NAME                    = "guard"; /* from NextGovernanceActionProcessStep relationship */
    public static final String MANDATORY_GUARD_PROPERTY_NAME          = "mandatoryGuard"; /* from NextGovernanceActionProcessStep relationship */
    public static final String IGNORE_MULTIPLE_TRIGGERS_PROPERTY_NAME = "ignoreMultipleTriggers"; /* from NextGovernanceActionProcessStep relationship */
    public static final String WAIT_TIME_PROPERTY_NAME                = "waitTime"; /* from NextGovernanceActionProcessStep relationship */


    public static final String EXECUTOR_ENGINE_GUID_PROPERTY_NAME      = "executorEngineGUID";       /* from EngineAction entity */
    public static final String EXECUTOR_ENGINE_NAME_PROPERTY_NAME      = "executorEngineName";       /* from EngineAction entity */
    public static final String MANDATORY_GUARDS_PROPERTY_NAME          = "mandatoryGuards";           /* from EngineAction entity */
    public static final String RECEIVED_GUARDS_PROPERTY_NAME           = "receivedGuards";            /* from EngineAction entity */
    public static final String REQUESTED_START_DATE_PROPERTY_NAME      = "requestedStartDate";        /* from EngineAction entity */
    public static final String START_DATE_PROPERTY_NAME                = "startDate";                 /* from EngineAction and Project entity and RegisteredIntegrationConnector relationship*/
    public static final String PLANNED_END_DATE_PROPERTY_NAME          = "plannedEndDate";            /* from Project entity */
    public static final String PROCESSING_ENGINE_USER_ID_PROPERTY_NAME = "processingEngineUserId";    /* from EngineAction entity */
    public static final String COMPLETION_DATE_PROPERTY_NAME           = "completionDate";            /* from EngineAction entity */
    public static final String COMPLETION_GUARDS_PROPERTY_NAME         = "completionGuards";          /* from EngineAction entity */
    public static final String COMPLETION_MESSAGE_PROPERTY_NAME        = "completionMessage";         /* from EngineAction entity and TargetForAction relationship*/

    public static final String ORIGIN_GOVERNANCE_SERVICE_PROPERTY_NAME = "originGovernanceService";   /* from EngineActionRequestSource relationship */
    public static final String ORIGIN_GOVERNANCE_ENGINE_PROPERTY_NAME  = "originGovernanceEngine";    /* from EngineActionRequestSource relationship */


    public static final String REQUEST_SOURCE_NAME_PROPERTY_NAME = "requestSourceName"; /* from GovernanceActionRequestSource relationship */

    public static final String ACTION_TARGET_NAME_PROPERTY_NAME = "actionTargetName"; /* from TargetForAction relationship */


    public static final String USES_BLOCKING_CALLS_PROPERTY_NAME = "usesBlockingCalls";     /* from IntegrationConnector entity */

    public static final String REGISTERED_INTEGRATION_CONNECTOR_TYPE_GUID = "7528bcd4-ae4c-47d0-a33f-4aeebbaa92c2";
    public static final String REGISTERED_INTEGRATION_CONNECTOR_TYPE_NAME = "RegisteredIntegrationConnector";
    /* End1 = IntegrationGroup; End 2 = IntegrationConnector */

    public static final String CONNECTOR_NAME_PROPERTY_NAME                 = "connectorName";  /* from RegisteredIntegrationConnector relationship */
    public static final String CONNECTOR_USER_ID_PROPERTY_NAME              = "connectorUserId";/* from RegisteredIntegrationConnector  relationship */
    public static final String METADATA_SOURCE_QUALIFIED_NAME_PROPERTY_NAME = "metadataSourceQualifiedName";  /* from RegisteredIntegrationConnector relationship */
    public static final String TEMPLATES_PROPERTY_NAME                      = "templates";  /* from RegisteredIntegrationConnector relationship */
    public static final String CONNECTION_NAME_PROPERTY_NAME                = "connectionName";  /* from RegisteredIntegrationConnector relationship */
    public static final String STOP_DATE_PROPERTY_NAME                      = "stopDate";       /* from RegisteredIntegrationConnector relationship */
    public static final String REFRESH_TIME_INTERVAL_PROPERTY_NAME          = "refreshTimeInterval"; /* from RegisteredIntegrationConnector relationship */
    public static final String GENERATE_INTEGRATION_REPORT_PROPERTY_NAME    = "generateIntegrationReport"; /* from RegisteredIntegrationConnector relationship */

    public static final String CATALOG_TARGET_RELATIONSHIP_TYPE_GUID = "bc5a5eb1-881b-4055-aa2c-78f314282ac2";
    public static final String CATALOG_TARGET_RELATIONSHIP_TYPE_NAME = "CatalogTarget";
    /* End1 = IntegrationConnector; End 2 = OpenMetadataRoot */

    public static final String CATALOG_TARGET_NAME_PROPERTY_NAME = "catalogTargetName";     /* from CatalogTarget relationship */

    public static final String INTEGRATION_REPORT_TYPE_GUID = "b8703d3f-8668-4e6a-bf26-27db1607220d";
    public static final String INTEGRATION_REPORT_TYPE_NAME = "IntegrationReport";

    public static final String SERVER_NAME_PROPERTY_NAME             = "serverName";             /* from IntegrationReport entity */
    public static final String CONNECTOR_ID_PROPERTY_NAME            = "connectorId";            /* from IntegrationReport entity */
    public static final String REFRESH_START_DATE_PROPERTY_NAME      = "refreshStartDate";       /* from IntegrationReport entity */
    public static final String REFRESH_COMPLETION_DATE_PROPERTY_NAME = "refreshCompletionDate";  /* from IntegrationReport entity */
    public static final String CREATED_ELEMENTS_PROPERTY_NAME        = "createdElements";        /* from IntegrationReport entity */
    public static final String UPDATED_ELEMENTS_PROPERTY_NAME        = "updatedElements";        /* from IntegrationReport entity */
    public static final String DELETED_ELEMENTS_PROPERTY_NAME        = "deletedElements";        /* from IntegrationReport entity */

    public static final String RELATED_INTEGRATION_REPORT_TYPE_GUID = "83d12156-f8f3-4b4b-b31b-18c140df9aa3";
    public static final String RELATED_INTEGRATION_REPORT_TYPE_NAME = "RelatedIntegrationReport";
    /* End1 = OpenMetadataRoot; End 2 = IntegrationReport */

    public static final String KNOWN_DUPLICATE_CLASSIFICATION_TYPE_GUID = "e55062b2-907f-44bd-9831-255642285731";
    public static final String KNOWN_DUPLICATE_CLASSIFICATION_TYPE_NAME = "KnownDuplicate";
    /* Referenceable */

    public static final String PEER_DUPLICATE_LINK_TYPE_GUID = "a94b2929-9e62-4b12-98ab-8ac45691e5bd";
    public static final String PEER_DUPLICATE_LINK_TYPE_NAME = "PeerDuplicateLink";
    /* End1 = Referenceable (Oldest); End 2 = Referenceable (Newest) */

    public static final String CONSOLIDATED_DUPLICATE_TYPE_GUID = "e40e80d7-5a29-482c-9a88-0dc7251f08de";
    public static final String CONSOLIDATED_DUPLICATE_TYPE_NAME = "ConsolidatedDuplicate";

    public static final String CONSOLIDATED_DUPLICATE_LINK_TYPE_GUID = "a1fabffd-d6ec-4b2d-bfe4-646f27c07c82";
    public static final String CONSOLIDATED_DUPLICATE_LINK_TYPE_NAME = "ConsolidatedDuplicateLink";
    /* End1 = Referenceable (Detected Duplicate); End 2 = Referenceable (Result) */

    public static final String INCIDENT_REPORT_STATUS_ENUM_TYPE_GUID = "a9d4f64b-fa24-4eb8-8bf6-308926ef2c14";
    public static final String INCIDENT_REPORT_STATUS_ENUM_TYPE_NAME = "IncidentReportStatus";
    public static final int    RAISED_INCIDENT_ORDINAL               = 0;
    public static final int    REVIEWED_INCIDENT_ORDINAL             = 1;
    public static final int    VALIDATED_INCIDENT_ORDINAL            = 2;
    public static final int    RESOLVED_INCIDENT_ORDINAL             = 3;
    public static final int    INVALID_INCIDENT_ORDINAL              = 4;
    public static final int    IGNORED_INCIDENT_ORDINAL              = 5;
    public static final int    OTHER_INCIDENT_ORDINAL                = 99;

    public static final String INCIDENT_CLASSIFIER_TYPE_GUID = "361158c0-ade1-4c92-a6a7-64f7ac39b87d";
    public static final String INCIDENT_CLASSIFIER_TYPE_NAME = "IncidentClassifier";
    /* Referenceable */

    public static final String INCIDENT_CLASSIFIER_SET_TYPE_GUID = "361158c0-ade1-4c92-a6a7-64f7ac39b87d";
    public static final String INCIDENT_CLASSIFIER_SET_TYPE_NAME = "IncidentClassifierSet";
    /* Collection */

    public static final String CLASSIFIER_LABEL_PROPERTY_NAME       = "classifierLabel";       /* from IncidentClassifier entity */
    public static final String CLASSIFIER_IDENTIFIER_PROPERTY_NAME  = "classifierIdentifier";  /* from IncidentClassifier entity */
    public static final String CLASSIFIER_NAME_PROPERTY_NAME        = "classifierName";        /* from IncidentClassifier entity */
    public static final String CLASSIFIER_DESCRIPTION_PROPERTY_NAME = "classifierDescription"; /* from IncidentClassifier entity */


    public static final String INCIDENT_REPORT_TYPE_GUID = "072f252b-dea7-4b88-bb2e-8f741c9ca7f6e";
    public static final String INCIDENT_REPORT_TYPE_NAME = "IncidentReport";
    /* Referenceable */

    public static final String BACKGROUND_PROPERTY_NAME      = "background";     /* from IncidentReport entity */
    public static final String INCIDENT_STATUS_PROPERTY_NAME = "incidentStatus"; /* from IncidentReport entity */

    public static final String INCIDENT_ORIGINATOR_TYPE_GUID = "e490772e-c2c5-445a-aea6-1aab3499a76c";
    public static final String INCIDENT_ORIGINATOR_TYPE_NAME = "IncidentOriginator";
    /* End1 = Referenceable; End 2 = IncidentReport */

    public static final String IMPACTED_RESOURCE_TYPE_GUID = "0908e153-e0fd-499c-8a30-5ea8b81395cd";
    public static final String IMPACTED_RESOURCE_TYPE_NAME = "ImpactedResource";
    /* End1 = Referenceable; End 2 = IncidentReport */

    public static final String SEVERITY_LEVEL_IDENTIFIER_PROPERTY_NAME = "severityLevelIdentifier";       /* from Certification relationship */

    public static final String INCIDENT_DEPENDENCY_TYPE_GUID = "017be6a8-0037-49d8-af5d-c45c41f25e0b";
    public static final String INCIDENT_DEPENDENCY_TYPE_NAME = "IncidentDependency";
    /* End1 = IncidentReport; End 2 = IncidentReport */

    public static final String CERTIFICATION_TYPE_TYPE_GUID = "97f9ffc9-e2f7-4557-ac12-925257345eea";
    public static final String CERTIFICATION_TYPE_TYPE_NAME = "CertificationType";
    /* GovernanceDefinition */

    public static final String DETAILS_PROPERTY_NAME = "details";             /* from CertificationType/LicenseType entity */

    public static final String CERTIFICATION_OF_REFERENCEABLE_TYPE_GUID = "390559eb-6a0c-4dd7-bc95-b9074caffa7f";
    public static final String CERTIFICATION_OF_REFERENCEABLE_TYPE_NAME = "Certification";
    /* End1 = Referenceable; End 2 = CertificationType */

    public static final String CERTIFICATE_GUID_PROPERTY_NAME           = "certificateGUID";           /* from Certification relationship */
    public static final String START_PROPERTY_NAME                      = "start";                     /* from Certification relationship */
    public static final String END_PROPERTY_NAME                        = "end";                       /* from Certification relationship */
    public static final String CONDITIONS_PROPERTY_NAME                 = "conditions";                /* from Certification relationship */
    public static final String CERTIFIED_BY_PROPERTY_NAME               = "certifiedBy";               /* from Certification relationship */
    public static final String CERTIFIED_BY_TYPE_NAME_PROPERTY_NAME     = "certifiedByTypeName";       /* from Certification relationship */
    public static final String CERTIFIED_BY_PROPERTY_NAME_PROPERTY_NAME = "certifiedByPropertyName";   /* from Certification relationship */
    public static final String CUSTODIAN_PROPERTY_NAME                  = "custodian";                 /* from Certification and License relationship */
    public static final String CUSTODIAN_TYPE_NAME_PROPERTY_NAME        = "custodianTypeName";         /* from Certification and License relationship */
    public static final String CUSTODIAN_PROPERTY_NAME_PROPERTY_NAME    = "custodianPropertyName";     /* from Certification and License relationship */
    public static final String RECIPIENT_PROPERTY_NAME                  = "recipient";                 /* from Certification relationship */
    public static final String RECIPIENT_TYPE_NAME_PROPERTY_NAME        = "recipientTypeName";         /* from Certification relationship */
    public static final String RECIPIENT_PROPERTY_NAME_PROPERTY_NAME    = "recipientPropertyName";     /* from Certification relationship */

    public static final String REFERENCEABLE_TO_LICENSE_TYPE_GUID = "35e53b7f-2312-4d66-ae90-2d4cb47901ee";
    public static final String REFERENCEABLE_TO_LICENSE_TYPE_NAME = "License";
    /* End1 = Referenceable; End 2 = LicenseType */

    public static final String LICENSE_TYPE_TYPE_GUID = "046a049d-5f80-4e5b-b0ae-f3cf6009b513";
    public static final String LICENSE_TYPE_TYPE_NAME = "LicenseType";
    /* GovernanceDefinition */

    public static final String LICENSE_OF_REFERENCEABLE_TYPE_GUID = "35e53b7f-2312-4d66-ae90-2d4cb47901ee";
    public static final String LICENSE_OF_REFERENCEABLE_TYPE_NAME = "License";
    /* End1 = Referenceable; End 2 = LicenseType */

    public static final String LICENSE_GUID_PROPERTY_NAME              = "licenseGUID";            /* from License relationship */
    public static final String LICENSED_BY_PROPERTY_NAME               = "licensedBy";             /* from License relationship */
    public static final String LICENSED_BY_TYPE_NAME_PROPERTY_NAME     = "licensedByTypeName";     /* from License relationship */
    public static final String LICENSED_BY_PROPERTY_NAME_PROPERTY_NAME = "licensedByPropertyName"; /* from License relationship */
    public static final String LICENSEE_PROPERTY_NAME                  = "licensee";               /* from License relationship */
    public static final String LICENSEE_TYPE_NAME_PROPERTY_NAME        = "licenseeTypeName";       /* from License relationship */
    public static final String LICENSEE_PROPERTY_NAME_PROPERTY_NAME    = "licenseePropertyName";   /* from License relationship */
    public static final String ENTITLEMENTS_PROPERTY_NAME              = "entitlements";   /* from License relationship */
    public static final String RESTRICTIONS_PROPERTY_NAME              = "restrictions";   /* from License relationship */
    public static final String OBLIGATIONS_PROPERTY_NAME               = "obligations";   /* from License relationship */


    /* ============================================================================================================================*/
    /* Area 5 - Schemas and Models                                                                                                 */
    /* ============================================================================================================================*/

    public static final String ASSET_TO_SCHEMA_TYPE_TYPE_GUID = "815b004d-73c6-4728-9dd9-536f4fe803cd";  /* from Area 5 */
    public static final String ASSET_TO_SCHEMA_TYPE_TYPE_NAME = "AssetSchemaType";
    /* End1 = Asset; End 2 = SchemaType */

    public static final String SCHEMA_ELEMENT_TYPE_GUID = "718d4244-8559-49ed-ad5a-10e5c305a656";   /* from Area 5 */
    public static final String SCHEMA_ELEMENT_TYPE_NAME = "SchemaElement";
    /* Referenceable */

    public static final String SCHEMA_DISPLAY_NAME_PROPERTY_NAME = "displayName";          /* from SchemaElement entity */
    public static final String SCHEMA_DESCRIPTION_PROPERTY_NAME  = "description";          /* from SchemaElement entity */
    public static final String IS_DEPRECATED_PROPERTY_NAME       = "isDeprecated";         /* from SchemaElement and ValidValueDefinition entity */

    /* For Schema Type */
    public static final String SCHEMA_TYPE_TYPE_GUID = "5bd4a3e7-d22d-4a3d-a115-066ee8e0754f";   /* from Area 5 */
    public static final String SCHEMA_TYPE_TYPE_NAME = "SchemaType";
    /* SchemaElement */

    public static final String VERSION_NUMBER_PROPERTY_NAME    = "versionNumber";        /* from SchemaType entity */
    public static final String AUTHOR_PROPERTY_NAME            = "author";               /* from SchemaType entity */
    public static final String SCHEMA_USAGE_PROPERTY_NAME      = "usage";                /* from SchemaType entity */
    public static final String ENCODING_STANDARD_PROPERTY_NAME = "encodingStandard";     /* from SchemaType entity */
    public static final String NAMESPACE_PROPERTY_NAME         = "namespace";            /* from SchemaType entity */

    /* For Complex Schema Type */
    public static final String COMPLEX_SCHEMA_TYPE_TYPE_GUID = "786a6199-0ce8-47bf-b006-9ace1c5510e4";    /* from Area 5 */
    public static final String COMPLEX_SCHEMA_TYPE_TYPE_NAME = "ComplexSchemaType";
    /* SchemaType */

    public static final String STRUCT_SCHEMA_TYPE_TYPE_GUID = "a13b409f-fd67-4506-8d94-14dfafd250a4";    /* from Area 5 */
    public static final String STRUCT_SCHEMA_TYPE_TYPE_NAME = "StructSchemaType";
    /* ComplexSchemaType */

    public static final String TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_GUID = "86b176a2-015c-44a6-8106-54d5d69ba661";  /* from Area 5 */
    public static final String TYPE_TO_ATTRIBUTE_RELATIONSHIP_TYPE_NAME = "AttributeForSchema";
    /* End1 = ComplexSchemaType; End 2 = SchemaAttribute */

    /* For Literal Schema Type */
    public static final String LITERAL_SCHEMA_TYPE_TYPE_GUID = "520ebb91-c4eb-4d46-a3b1-974875cdcf0d";  /* from Area 5 */
    public static final String LITERAL_SCHEMA_TYPE_TYPE_NAME = "LiteralSchemaType";
    /* SchemaType */

    /* For External Schema Type */
    public static final String EXTERNAL_SCHEMA_TYPE_TYPE_GUID = "78de00ea-3d69-47ff-a6d6-767587526624";  /* from Area 5 */
    public static final String EXTERNAL_SCHEMA_TYPE_TYPE_NAME = "ExternalSchemaType";
    /* SchemaType */

    public static final String LINKED_EXTERNAL_SCHEMA_TYPE_RELATIONSHIP_TYPE_GUID = "9a5d78c2-1716-4783-bfc6-c300a9e2d092";  /* from Area 5 */
    public static final String LINKED_EXTERNAL_SCHEMA_TYPE_RELATIONSHIP_TYPE_NAME = "LinkedExternalSchemaType";
    /* End1 = SchemaElement; End 2 = SchemaType */

    /* For Schema Type Choice */
    public static final String SCHEMA_TYPE_CHOICE_TYPE_GUID = "5caf954a-3e33-4cbd-b17d-8b8613bd2db8";  /* from Area 5 */
    public static final String SCHEMA_TYPE_CHOICE_TYPE_NAME = "SchemaTypeChoice";
    /* SchemaType */

    public static final String SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_GUID = "eb4f1f98-c649-4560-8a46-da17c02764a9";   /* from Area 5 */
    public static final String SCHEMA_TYPE_OPTION_RELATIONSHIP_TYPE_NAME = "SchemaTypeOption";
    /* End1 = SchemaTypeChoice; End 2 = SchemaType */

    /* For Schema Type Choice */
    public static final String SIMPLE_SCHEMA_TYPE_TYPE_GUID = "b5ec6e07-6419-4225-9dc4-fb55aba255c6";  /* from Area 5 */
    public static final String SIMPLE_SCHEMA_TYPE_TYPE_NAME = "SimpleSchemaType";
    /* SchemaType */

    /* For Primitive Schema Type */
    public static final String PRIMITIVE_SCHEMA_TYPE_TYPE_GUID = "f0f75fba-9136-4082-8352-0ad74f3c36ed";  /* from Area 5 */
    public static final String PRIMITIVE_SCHEMA_TYPE_TYPE_NAME = "PrimitiveSchemaType";
    /* SimpleSchemaType */

    /* For Enum Schema Type */
    public static final String ENUM_SCHEMA_TYPE_TYPE_GUID = "24b092ac-42e9-43dc-aeca-eb034ce307d9";  /* from Area 5 */
    public static final String ENUM_SCHEMA_TYPE_TYPE_NAME = "EnumSchemaType";
    /* SimpleSchemaType */

    public static final String DATA_TYPE_PROPERTY_NAME   = "dataType";     /* from SimpleSchemaType and LiteralSchemaType entity */
    public static final String FIXED_VALUE_PROPERTY_NAME = "fixedValue";   /* from LiteralSchemaType entity */

    /* For Map Schema Type */
    public static final String MAP_SCHEMA_TYPE_TYPE_GUID = "bd4c85d0-d471-4cd2-a193-33b0387a19fd";   /* from Area 5 */
    public static final String MAP_SCHEMA_TYPE_TYPE_NAME = "MapSchemaType";
    /* SchemaType */

    public static final String MAP_TO_RELATIONSHIP_TYPE_GUID = "8b9856b3-451e-45fc-afc7-fddefd81a73a";   /* from Area 5 */
    public static final String MAP_TO_RELATIONSHIP_TYPE_NAME = "MapToElementType";
    /* End1 = MapSchemaType; End 2 = SchemaType */

    public static final String MAP_FROM_RELATIONSHIP_TYPE_GUID = "6189d444-2da4-4cd7-9332-e48a1c340b44";   /* from Area 5 */
    public static final String MAP_FROM_RELATIONSHIP_TYPE_NAME = "MapFromElementType";
    /* End1 = MapSchemaType; End 2 = SchemaType */

    public static final String ELEMENT_POSITION_PROPERTY_NAME       = "position";              /* from SchemaAttribute entity */
    public static final String CARDINALITY_PROPERTY_NAME            = "cardinality";           /* from SchemaAttribute entity */
    public static final String MAX_CARDINALITY_PROPERTY_NAME        = "maxCardinality";        /* from SchemaAttribute entity */
    public static final String MIN_CARDINALITY_PROPERTY_NAME        = "minCardinality";        /* from SchemaAttribute entity */
    public static final String DEFAULT_VALUE_OVERRIDE_PROPERTY_NAME = "defaultValueOverride";  /* from SchemaAttribute entity */
    public static final String ALLOWS_DUPLICATES_PROPERTY_NAME      = "allowsDuplicateValues"; /* from SchemaAttribute entity */
    public static final String ORDERED_VALUES_PROPERTY_NAME         = "orderedValues";         /* from SchemaAttribute entity */
    public static final String NATIVE_CLASS_PROPERTY_NAME           = "nativeClass";           /* from SchemaAttribute entity */
    public static final String ALIASES_PROPERTY_NAME                = "aliases";               /* from SchemaAttribute entity */
    public static final String MIN_LENGTH_PROPERTY_NAME             = "minimumLength";         /* from SchemaAttribute entity */
    public static final String LENGTH_PROPERTY_NAME                 = "length";                /* from SchemaAttribute entity */
    public static final String SIGNIFICANT_DIGITS_PROPERTY_NAME     = "significantDigits";     /* from SchemaAttribute entity */
    public static final String PRECISION_PROPERTY_NAME              = "precision";             /* from SchemaAttribute entity */
    public static final String IS_NULLABLE_PROPERTY_NAME            = "isNullable";            /* from SchemaAttribute entity */

    public static final String ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_GUID = "2d955049-e59b-45dd-8e62-cde1add59f9e";  /* from Area 5 */
    public static final String ATTRIBUTE_TO_TYPE_RELATIONSHIP_TYPE_NAME = "SchemaAttributeType";
    /* End1 = SchemaAttribute; End 2 = SchemaType */

    public static final String NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_GUID = "0ffb9d87-7074-45da-a9b0-ae0859611133";  /* from Area 5 */
    public static final String NESTED_ATTRIBUTE_RELATIONSHIP_TYPE_NAME = "NestedSchemaAttribute";
    /* End1 = SchemaAttribute; End 2 = SchemaAttribute */

    public static final String TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION_TYPE_GUID = "e2bb76bb-774a-43ff-9045-3a05f663d5d9";  /* from Area 5 */
    public static final String TYPE_EMBEDDED_ATTRIBUTE_CLASSIFICATION_TYPE_NAME = "TypeEmbeddedAttribute";
    /* Linked to SchemaAttribute */
    public static final String SCHEMA_TYPE_NAME_PROPERTY_NAME                   = "schemaTypeName";      /* from TypeEmbeddedAttribute classification */

    /* For Schema Link */
    public static final String SCHEMA_LINK_TYPE_GUID = "67e08705-2d2a-4df6-9239-1818161a41e0";      /* from Area 5 */
    public static final String SCHEMA_LINK_TYPE_NAME = "SchemaLinkElement";
    /* SchemaElement */

    public static final String LINK_NAME_PROPERTY_NAME       = "linkName";             /* from SchemaAttribute entity */
    public static final String LINK_PROPERTIES_PROPERTY_NAME = "linkProperties";       /* from SchemaAttribute entity */

    public static final String LINK_TO_TYPE_RELATIONSHIP_TYPE_GUID = "292125f7-5660-4533-a48a-478c5611922e";     /* from Area 5 */
    public static final String LINK_TO_TYPE_RELATIONSHIP_TYPE_NAME = "LinkedType";
    /* End1 = SchemaLinkElement; End 2 = SchemaType */

    public static final String ATTRIBUTE_TO_LINK_RELATIONSHIP_TYPE_GUID = "db9583c5-4690-41e5-a580-b4e30a0242d3";     /* from Area 5 */
    public static final String ATTRIBUTE_TO_LINK_RELATIONSHIP_TYPE_NAME = "SchemaLinkToType";
    /* End1 = SchemaAttribute; End 2 = SchemaLinkElement */

    public static final String SCHEMA_QUERY_TARGET_RELATIONSHIP_TYPE_GUID = "1c2622b7-ac21-413c-89e1-6f61f348cd19"; /* from Area 5 */
    public static final String SCHEMA_QUERY_TARGET_RELATIONSHIP_TYPE_NAME = "DerivedSchemaTypeQueryTarget";
    /* End1 = SchemaElement; End 2 = SchemaElement (target) */

    public static final String QUERY_ID_PROPERTY_NAME = "queryId"; /* from DerivedSchemaTypeQueryTarget relationship */
    public static final String QUERY_PROPERTY_NAME    = "query";   /* from DerivedSchemaTypeQueryTarget relationship */

    /* - Known Subtypes ------------------------------------------------------- */

    public static final String ARRAY_SCHEMA_TYPE_TYPE_GUID = "ba8d29d2-a8a4-41f3-b29f-91ad924dd944";   /* from Area 5 */
    public static final String ARRAY_SCHEMA_TYPE_TYPE_NAME = "ArraySchemaType";
    /* BoundedSchemaType */

    public static final String SET_SCHEMA_TYPE_TYPE_GUID = "b2605d2d-10cd-443c-b3e8-abf15fb051f0";   /* from Area 5 */
    public static final String SET_SCHEMA_TYPE_TYPE_NAME = "SetSchemaType";
    /* BoundedSchemaType */

    public static final String PORT_SCHEMA_RELATIONSHIP_TYPE_GUID = "B216fA00-8281-F9CC-9911-Ae6377f2b457"; /* from Area 5 */
    public static final String PORT_SCHEMA_RELATIONSHIP_TYPE_NAME = "PortSchema";
    /* End1 = Port; End 2 = SchemaType */

    public static final String TABULAR_SCHEMA_TYPE_TYPE_GUID = "248975ec-8019-4b8a-9caf-084c8b724233";   /* from Area 5 */
    public static final String TABULAR_SCHEMA_TYPE_TYPE_NAME = "TabularSchemaType";
    /* ComplexSchemaType */

    public static final String TABULAR_COLUMN_TYPE_GUID = "d81a0425-4e9b-4f31-bc1c-e18c3566da10";   /* from Area 5 */
    public static final String TABULAR_COLUMN_TYPE_NAME = "TabularColumn";
    /* PrimitiveSchemaType */

    public static final String TABULAR_FILE_COLUMN_TYPE_GUID = "af6265e7-5f58-4a9c-9ae7-8d4284be62bd";   /* from Area 5 */
    public static final String TABULAR_FILE_COLUMN_TYPE_NAME = "TabularFileColumn";
    /* TabularColumn */

    public static final String DOCUMENT_SCHEMA_TYPE_TYPE_GUID = "33da99cd-8d04-490c-9457-c58908da7794";   /* from Area 5 */
    public static final String DOCUMENT_SCHEMA_TYPE_TYPE_NAME = "DocumentSchemaType";
    /* ComplexSchemaType */

    public static final String DOCUMENT_SCHEMA_ATTRIBUTE_TYPE_GUID = "b5cefb7e-b198-485f-a1d7-8e661012499b";   /* from Area 5 */
    public static final String DOCUMENT_SCHEMA_ATTRIBUTE_TYPE_NAME = "DocumentSchemaAttribute";
    /* SchemaAttribute */

    public static final String SIMPLE_DOCUMENT_TYPE_TYPE_GUID = "42cfccbf-cc68-4980-8c31-0faf1ee002d3";   /* from Area 5 */
    public static final String SIMPLE_DOCUMENT_TYPE_TYPE_NAME = "SimpleDocumentType";
    /* PrimitiveSchemaType */

    public static final String STRUCT_DOCUMENT_TYPE_TYPE_GUID = "f6245c25-8f73-45eb-8fb5-fa17a5f27649";   /* from Area 5 */
    public static final String STRUCT_DOCUMENT_TYPE_TYPE_NAME = "StructDocumentType";
    /* StructSchemaType */

    public static final String MAP_DOCUMENT_TYPE_TYPE_GUID = "b0f09598-ceb6-415b-befc-563ecadd5727";   /* from Area 5 */
    public static final String MAP_DOCUMENT_TYPE_TYPE_NAME = "MapDocumentType";
    /* MapSchemaType */

    public static final String OBJECT_SCHEMA_TYPE_TYPE_GUID = "6920fda1-7c07-47c7-84f1-9fb044ae153e";   /* from Area 5 */
    public static final String OBJECT_SCHEMA_TYPE_TYPE_NAME = "ObjectSchemaType";
    /* ComplexSchemaType */

    public static final String OBJECT_SCHEMA_ATTRIBUTE_TYPE_GUID = "ccb408c0-582e-4a3a-a926-7082d53bb669";   /* from Area 5 */
    public static final String OBJECT_SCHEMA_ATTRIBUTE_TYPE_NAME = "ObjectSchemaAttribute";
    /* SchemaAttribute */

    public static final String RELATIONAL_DB_SCHEMA_TYPE_TYPE_GUID = "f20f5f45-1afb-41c1-9a09-34d8812626a4";   /* from Area 5 */
    public static final String RELATIONAL_DB_SCHEMA_TYPE_TYPE_NAME = "RelationalDBSchemaType";
    /* ComplexSchemaType */

    public static final String RELATIONAL_TABLE_TYPE_TYPE_GUID = "1321bcc0-dc6a-48ed-9ca6-0c6f934b0b98";   /* from Area 5 */
    public static final String RELATIONAL_TABLE_TYPE_TYPE_NAME = "RelationalTableType";
    /* TabularSchemaType */

    public static final String RELATIONAL_TABLE_TYPE_GUID = "ce7e72b8-396a-4013-8688-f9d973067425";   /* from Area 5 */
    public static final String RELATIONAL_TABLE_TYPE_NAME = "RelationalTable";
    /* SchemaAttribute */

    public static final String CALCULATED_VALUE_CLASSIFICATION_TYPE_GUID = "4814bec8-482d-463d-8376-160b0358e139";
    public static final String CALCULATED_VALUE_CLASSIFICATION_TYPE_NAME = "CalculatedValue";
    /* Linked to SchemaType */

    public static final String RELATIONAL_COLUMN_TYPE_GUID = "aa8d5470-6dbc-4648-9e2f-045e5df9d2f9";   /* from Area 5 */
    public static final String RELATIONAL_COLUMN_TYPE_NAME = "RelationalColumn";
    /* TabularColumn */

    public static final String PRIMARY_KEY_CLASSIFICATION_TYPE_GUID = "b239d832-50bd-471b-b17a-15a335fc7f40";
    public static final String PRIMARY_KEY_CLASSIFICATION_TYPE_NAME = "PrimaryKey";
    /* Linked to RelationalColumn */
    public static final String PRIMARY_KEY_PATTERN_PROPERTY_NAME    = "keyPattern";    /* from PrimaryKey classification */
    public static final String PRIMARY_KEY_NAME_PROPERTY_NAME       = "name";          /* from PrimaryKey classification */

    public static final String FOREIGN_KEY_RELATIONSHIP_TYPE_GUID    = "3cd4e0e7-fdbf-47a6-ae88-d4b3205e0c07"; /* from Area 5 */
    public static final String FOREIGN_KEY_RELATIONSHIP_TYPE_NAME    = "ForeignKey";
    /* End1 = RelationalColumn; End 2 = RelationalColumn */
    public static final String FOREIGN_KEY_NAME_PROPERTY_NAME        = "name";          /* from ForeignKey relationship */
    public static final String FOREIGN_KEY_DESCRIPTION_PROPERTY_NAME = "description";   /* from ForeignKey relationship */
    public static final String FOREIGN_KEY_CONFIDENCE_PROPERTY_NAME  = "confidence";    /* from ForeignKey relationship */
    public static final String FOREIGN_KEY_STEWARD_PROPERTY_NAME     = "steward";       /* from ForeignKey relationship */
    public static final String FOREIGN_KEY_SOURCE_PROPERTY_NAME      = "source";        /* from ForeignKey relationship */

    /* For Event Types */
    public static final String EVENT_TYPE_LIST_TYPE_GUID = "77ccda3d-c4c6-464c-a424-4b2cb27ac06c";   /* from Area 5 */
    public static final String EVENT_TYPE_LIST_TYPE_NAME = "EventTypeList";
    /* ComplexSchemaType */

    public static final String EVENT_TYPE_TYPE_GUID = "8bc88aba-d7e4-4334-957f-cfe8e8eadc32";   /* from Area 5 */
    public static final String EVENT_TYPE_TYPE_NAME = "EventType";
    /* ComplexSchemaType */

    public static final String EVENT_SCHEMA_ATTRIBUTE_TYPE_GUID = "5be4ee8f-4d0c-45cd-a411-22a468950342";   /* from Area 5 */
    public static final String EVENT_SCHEMA_ATTRIBUTE_TYPE_NAME = "EventSchemaAttribute";
    /* SchemaAttribute */

    public static final String EVENT_SET_TYPE_GUID = "bead9aa4-214a-4596-8036-aa78395bbfb1";   /* from Area 5 */
    public static final String EVENT_SET_TYPE_NAME = "EventSet";
    /* Collection */

    /* For API Schema Type */
    public static final String API_SCHEMA_TYPE_TYPE_GUID = "b46cddb3-9864-4c5d-8a49-266b3fc95cb8";   /* from Area 5 */
    public static final String API_SCHEMA_TYPE_TYPE_NAME = "APISchemaType";
    /* SchemaType */
    public static final String API_OPERATION_TYPE_GUID   = "f1c0af19-2729-4fac-996e-a7badff3c21c";   /* from Area 5 */
    public static final String API_OPERATION_TYPE_NAME   = "APIOperation";
    /* SchemaType */

    public static final String PATH_PROPERTY_NAME    = "path";        /* from APIOperation entity */
    public static final String COMMAND_PROPERTY_NAME = "command";     /* from APIOperation entity */

    public static final String API_PARAMETER_LIST_TYPE_GUID = "ba167b12-969f-49d3-8bea-d04228d9a44b";   /* from Area 5 */
    public static final String API_PARAMETER_LIST_TYPE_NAME = "APIParameterList";
    /* ComplexSchemaType */

    public static final String REQUIRED_PROPERTY_NAME       = "required";        /* from APIParameterList entity */
    public static final String PARAMETER_TYPE_PROPERTY_NAME = "parameterType";   /* from APIParameter entity */

    public static final String API_PARAMETER_TYPE_GUID = "10277b13-509c-480e-9829-bc16d0eafc53";   /* from Area 5 */
    public static final String API_PARAMETER_TYPE_NAME = "APIParameter";
    /* SchemaAttribute */

    public static final String API_OPERATIONS_RELATIONSHIP_TYPE_GUID = "03737169-ceb5-45f0-84f0-21c5929945af"; /* from Area 5 */
    public static final String API_OPERATIONS_RELATIONSHIP_TYPE_NAME = "APIOperations";
    /* End1 = APISchemaType; End 2 = APIOperation */
    public static final String API_HEADER_RELATIONSHIP_TYPE_GUID     = "e8fb46d1-5f75-481b-aa66-f43ad44e2cc6"; /* from Area 5 */
    public static final String API_HEADER_RELATIONSHIP_TYPE_NAME     = "APIHeader";
    /* End1 = APIOperation; End 2 = SchemaType */
    public static final String API_REQUEST_RELATIONSHIP_TYPE_GUID    = "4ab3b466-31bd-48ea-8aa2-75623476f2e2"; /* from Area 5 */
    public static final String API_REQUEST_RELATIONSHIP_TYPE_NAME    = "APIRequest";
    /* End1 = APIOperation; End 2 = SchemaType */
    public static final String API_RESPONSE_RELATIONSHIP_TYPE_GUID   = "e8001de2-1bb1-442b-a66f-9addc3641eae"; /* from Area 5 */
    public static final String API_RESPONSE_RELATIONSHIP_TYPE_NAME   = "APIResponse";
    /* End1 = APIOperation; End 2 = SchemaType */

    public static final String DISPLAY_DATA_SCHEMA_TYPE_TYPE_GUID = "2f5796f5-3fac-4501-9d0d-207aa8620d16";   /* from Area 5 */
    public static final String DISPLAY_DATA_SCHEMA_TYPE_TYPE_NAME = "DisplayDataSchemaType";
    /* ComplexSchemaType */

    public static final String DISPLAY_DATA_CONTAINER_TYPE_GUID = "f2a4ff99-1954-48c0-8081-92d1a4dfd910";   /* from Area 5 */
    public static final String DISPLAY_DATA_CONTAINER_TYPE_NAME = "DisplayDataContainer";
    /* SchemaAttribute */



    public static final String INPUT_FIELD_PROPERTY_NAME = "inputField";            /* from DisplayDataField entity */

    public static final String QUERY_SCHEMA_TYPE_TYPE_GUID = "4d11bdbb-5d4a-488b-9f16-bf1e34d34dd9";   /* from Area 5 */
    public static final String QUERY_SCHEMA_TYPE_TYPE_NAME = "QuerySchemaType";
    /* ComplexSchemaType */


    /**
     * strictRequirement
     */
    public static final String IS_STRICT_REQUIREMENT_PROPERTY_NAME = "strictRequirement";          /* from ValidValuesAssignment relationship */

    /**
     * isDefaultValue
     */
    public static final String IS_DEFAULT_VALUE_PROPERTY_NAME = "isDefaultValue";             /* from ValidValuesMember relationship */

    /**
     * symbolicName
     */
    public static final String SYMBOLIC_NAME_PROPERTY_NAME = "symbolicName";            /* from ValidValuesImplementation relationship */

    /**
     * implementationValue
     */
    public static final String IMPLEMENTATION_VALUE_PROPERTY_NAME = "implementationValue";     /* from ValidValuesImplementation relationship */

    /**
     * additionalValues
     */
    public static final String ADDITIONAL_VALUES_PROPERTY_NAME = "additionalValues";        /* from ValidValuesImplementation relationship */

    /**
     * associationDescription
     */
    public static final String ASSOCIATION_DESCRIPTION_PROPERTY_NAME = "associationDescription";  /* from ValidValuesMapping relationship */

    /**
     * associationName
     */
    public static final String ASSOCIATION_NAME_PROPERTY_NAME = "associationName";  /* from ValidValueAssociation relationship */

    public static final String DESIGN_MODEL_TYPE_NAME = "DesignModel";

    public static final String TECHNICAL_NAME_PROPERTY_NAME = "technicalName";

    public static final String CONCEPT_MODEL_CLASSIFICATION_NAME = "ConceptModel";

    public static final String DESIGN_MODEL_SCOPE_TYPE_NAME                  = "DesignModelScope";
    public static final String DESIGN_MODEL_ELEMENTS_IN_SCOPE_TYPE_NAME      = "DesignModelElementsInScope";
    public static final String DESIGN_MODEL_ELEMENT_TYPE_NAME                = "DesignModelElement";
    public static final String DESIGN_MODEL_GROUP_TYPE_NAME                  = "DesignModelGroup";
    public static final String DESIGN_MODEL_GROUP_MEMBERSHIP_NAME            = "DesignModelGroupMembership";
    public static final String DESIGN_MODEL_OWNERSHIP_RELATIONSHIP_TYPE_NAME = "DesignModelOwnership";
    public static final String DESIGN_MODEL_IMPL_RELATIONSHIP_TYPE_NAME      = "DesignModelImplementation";

    public static final String METAMODEL_INSTANCE_CLASSIFICATION_NAME = "MetamodelInstance";
    public static final String METAMODEL_ELEMENT_GUID_PROPERTY_NAME   = "metamodelElementGUID";

    public static final String CONCEPT_BEAD_TYPE_NAME                = "ConceptBead";
    public static final String CONCEPT_BEAD_LINK_TYPE_NAME           = "ConceptBeadLink";
    public static final String CONCEPT_BEAD_ATTRIBUTE_TYPE_NAME      = "ConceptBeadAttribute";
    public static final String CONCEPT_BEAD_RELATIONSHIP_END_NAME    = "ConceptBeadRelationshipEnd";
    public static final String CONCEPT_BEAD_ATTRIBUTE_LINK_TYPE_NAME = "ConceptBeadAttributeLink";
    public static final String CONCEPT_MODEL_DECORATION_ENUM_NAME    = "ConceptModelDecoration";
    public static final int    CONCEPT_MODEL_DECORATION_NONE         = 0;
    public static final int    CONCEPT_MODEL_DECORATION_AGGREGATION  = 1;
    public static final int    CONCEPT_MODEL_DECORATION_COMPOSITION  = 2;
    public static final int    CONCEPT_MODEL_DECORATION_EXTENSION    = 3;

    public static final String CONCEPT_BEAD_COVERAGE_CATEGORY_ENUM_TYPE_NAME = "ConceptBeadAttributeCoverageCategory";
    public static final int    CONCEPT_BEAD_COVERAGE_UNKNOWN                 = 0;
    public static final int    CONCEPT_BEAD_COVERAGE_UNIQUE_IDENTIFIER       = 1;
    public static final int    CONCEPT_BEAD_COVERAGE_IDENTIFIER              = 2;
    public static final int    CONCEPT_BEAD_COVERAGE_CORE_DETAIL             = 3;
    public static final int    CONCEPT_BEAD_COVERAGE_EXTENDED_DETAIL         = 4;
    public static final String CONCEPT_BEAD_COVERAGE_CLASSIFICATION_NAME     = "ConceptBeadAttributeCoverage";
    public static final String CONCEPT_BEAD_COVERAGE_CATEGORY_PROPERTY_NAME  = "coverageCategory";

    public static final String UNIQUE_VALUES_PROPERTY_NAME = "uniqueValues";
    public static final String NAVIGABLE_PROPERTY_NAME     = "navigable";
    public static final String DECORATION_PROPERTY_NAME    = "decoration";


    /* ============================================================================================================================*/
    /* Area 7 - Lineage                                                                                                            */
    /* ============================================================================================================================*/


    public static final String IMPLEMENTED_BY_RELATIONSHIP_TYPE_GUID = "28f63c94-aaef-4c84-98f7-d77aa605272e";
    public static final String IMPLEMENTED_BY_RELATIONSHIP_TYPE_NAME = "ImplementedBy";
    /* End1 = Referenceable; End 2 = Referenceable */

    public static final String DESIGN_STEP_PROPERTY_NAME    = "designStep";
    public static final String ROLE_PROPERTY_NAME           = "role";
    public static final String TRANSFORMATION_PROPERTY_NAME = "transformation";

    public static final String SOLUTION_PORT_SCHEMA_RELATIONSHIP_TYPE_GUID = "bf02c703-57a2-4ab7-b6db-f49b57b05985";
    public static final String SOLUTION_PORT_SCHEMA_RELATIONSHIP_TYPE_NAME = "SolutionPortSchema";
    /* End1 = SolutionPort; End 2 = SchemaType */


    public static final String BUSINESS_SIGNIFICANCE_CLASSIFICATION_TYPE_GUID = "085febdd-f129-4f4b-99aa-01f3e6294e9f";
    public static final String BUSINESS_SIGNIFICANCE_CLASSIFICATION_TYPE_NAME = "BusinessSignificance";
    /* Linked to Referenceable */

    public static final String BUSINESS_CAPABILITY_GUID_PROPERTY_NAME = "businessCapabilityGUID";  /* from BusinessSignificant entity */




    /* ============================================================================================================================*/
    /* Deprecated Types                                                                                                            */
    /* ============================================================================================================================*/


    public static final String DISCOVERY_ANALYSIS_REPORT_TYPE_GUID = "acc7cbc8-09c3-472b-87dd-f78459323dcb";
    public static final String DISCOVERY_ANALYSIS_REPORT_TYPE_NAME = "OpenDiscoveryAnalysisReport";
    /* Referenceable */

    public static final String EXECUTION_DATE_PROPERTY_NAME           = "executionDate";              /* from OpenDiscoveryAnalysisReport entity */


    public static final String REPORT_TO_ASSET_TYPE_GUID = "7eded424-f176-4258-9ae6-138a46b2845f";     /* from Area 6 */
    public static final String REPORT_TO_ASSET_TYPE_NAME = "AssetDiscoveryReport";
    /* End1 = Asset; End 2 = OpenDiscoveryAnalysisReport */

    public static final String REPORT_TO_ENGINE_TYPE_GUID = "2c318c3a-5dc2-42cd-a933-0087d852f67f";    /* from Area 6 */
    public static final String REPORT_TO_ENGINE_TYPE_NAME = "DiscoveryEngineReport";
    /* End1 = OpenDiscoveryEngine; End 2 = OpenDiscoveryAnalysisReport */

    public static final String REPORT_TO_SERVICE_TYPE_GUID = "1744d72b-903d-4273-9229-de20372a17e2";   /* from Area 6 */
    public static final String REPORT_TO_SERVICE_TYPE_NAME = "DiscoveryInvocationReport";
    /* End1 = OpenDiscoveryService; End 2 = OpenDiscoveryAnalysisReport */

    public static final String REPORT_TO_ANNOTATIONS_TYPE_GUID = "51d386a3-3857-42e3-a3df-14a6cad08b93";   /* from Area 6 */
    public static final String REPORT_TO_ANNOTATIONS_TYPE_NAME = "DiscoveredAnnotation";
    /* End1 = Annotation; End 2 = OpenDiscoveryAnalysisReport */



    public static final String DATA_FIELD_NAME_PROPERTY_NAME        = "dataFieldName";        /* from DataField entity */
    public static final String DATA_FIELD_TYPE_PROPERTY_NAME        = "dataFieldType";        /* from DataField entity */
    public static final String DATA_FIELD_DESCRIPTION_PROPERTY_NAME = "dataFieldDescription"; /* from DataField entity */
    public static final String DATA_FIELD_ALIASES_PROPERTY_NAME     = "dataFieldAliases";     /* from DataField entity */
    public static final String DATA_FIELD_NAMESPACE_PROPERTY_NAME   = "dataFieldNamespace";   /* from DataField entity */
    public static final String VERSION_PROPERTY_NAME                = "version";              /* from DataField entity */

    /* For DiscoveredDataField relationship */
    public static final String DISCOVERED_DATA_FIELD_TYPE_GUID = "60f2d263-e24d-4f20-8c0d-b5e22222cd54";
    public static final String DISCOVERED_DATA_FIELD_TYPE_NAME = "DiscoveredDataField";
    /* End1 = SchemaAnalysisAnnotation; End 2 = DataField */

    /* For DiscoveredDataField relationship */
    public static final String NESTED_DATA_FIELD_TYPE_GUID = "60f2d263-e24d-4f20-8c0d-b5e12356cd54";
    public static final String NESTED_DATA_FIELD_TYPE_NAME = "NestedDataField";
    /* End1 = (parent)DataField; End 2 = DataField */

    public static final String LINKED_DATA_FIELD_TYPE_GUID = "cca4b116-4490-44c4-84e1-535231ae46a1";
    public static final String LINKED_DATA_FIELD_TYPE_NAME = "LinkedDataField";
    /* End1 = (parent)DataField; End 2 = DataField */

    public static final String DATA_FIELD_POSITION_PROPERTY_NAME = "dataFieldPosition";


    /* ============================================================================================================================*/
    /* Enums                                                                                                                       */
    /* ============================================================================================================================*/

    public static final String LATEST_CHANGE_TARGET_ENUM_TYPE_GUID                    = "a0b7d7a0-4af5-4539-9b81-cbef52d8cc5d";
    public static final String LATEST_CHANGE_TARGET_ENUM_TYPE_NAME                    = "LatestChangeTarget";
    public static final int    ENTITY_STATUS_LATEST_CHANGE_TARGET_ORDINAL             = 0;
    public static final int    ENTITY_PROPERTY_LATEST_CHANGE_TARGET_ORDINAL           = 1;
    public static final int    ENTITY_CLASSIFICATION_LATEST_CHANGE_TARGET_ORDINAL     = 2;
    public static final int    ENTITY_RELATIONSHIP_LATEST_CHANGE_TARGET_ORDINAL       = 3;
    public static final int    ATTACHMENT_LATEST_CHANGE_TARGET_ORDINAL                = 4;
    public static final int    ATTACHMENT_STATUS_LATEST_CHANGE_TARGET_ORDINAL         = 5;
    public static final int    ATTACHMENT_PROPERTY_LATEST_CHANGE_TARGET_ORDINAL       = 6;
    public static final int    ATTACHMENT_CLASSIFICATION_LATEST_CHANGE_TARGET_ORDINAL = 7;
    public static final int    ATTACHMENT_RELATIONSHIP_LATEST_CHANGE_TARGET_ORDINAL   = 8;
    public static final int    OTHER_LATEST_CHANGE_TARGET_ORDINAL                     = 99;

    public static final String LATEST_CHANGE_ACTION_ENUM_TYPE_GUID  = "032d844b-868f-4c4a-bc5d-81f0f9704c4d";
    public static final String LATEST_CHANGE_ACTION_ENUM_TYPE_NAME  = "LatestChangeAction";
    public static final int    CREATED_LATEST_CHANGE_ACTION_ORDINAL = 0;
    public static final int    UPDATED_LATEST_CHANGE_ACTION_ORDINAL = 1;
    public static final int    DELETED_LATEST_CHANGE_ACTION_ORDINAL = 2;
    public static final int    OTHER_LATEST_CHANGE_ACTION_ORDINAL   = 99;


    public static final String ANNOTATION_STATUS_ENUM_TYPE_GUID = "71187df6-ef66-4f88-bc03-cd3c7f925165";
    public static final String ANNOTATION_STATUS_ENUM_TYPE_NAME = "AnnotationStatus";

    public static final String OPERATIONAL_STATUS_ENUM_TYPE_GUID = "24e1e33e-9250-4a6c-8b07-05c7adec3a1d";
    public static final String OPERATIONAL_STATUS_ENUM_TYPE_NAME = "OperationalStatus";


    public static final String KEY_PATTERN_ENUM_TYPE_GUID = "8904df8f-1aca-4de8-9abd-1ef2aadba300";
    public static final String KEY_PATTERN_ENUM_TYPE_NAME = "KeyPattern";
    public static final String PERMITTED_SYNC_ENUM_TYPE_GUID = "973a9f4c-93fa-43a5-a0c5-d97dbd164e78";
    public static final String PERMITTED_SYNC_ENUM_TYPE_NAME = "PermittedSynchronization";

}
