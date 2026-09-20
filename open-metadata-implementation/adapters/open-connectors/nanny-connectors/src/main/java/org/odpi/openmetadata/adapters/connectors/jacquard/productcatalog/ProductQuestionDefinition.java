/* SPDX-License-Identifier: Apache-2.0 */
/* Copyright Contributors to the ODPi Egeria project. */
package org.odpi.openmetadata.adapters.connectors.jacquard.productcatalog;


import org.odpi.openmetadata.frameworks.openmetadata.types.OpenMetadataType;

import java.util.Arrays;
import java.util.List;

/**
 * The ProductQuestionDefinition is used to populate the questions associated with the glossary.  Each question
 * names the perspectives it matters to; a question may matter to more than one.
 */
public enum ProductQuestionDefinition
{

    USER_ACCOUNT_STATUS("User Account Status",
                        "What is the status of the user accounts used to access the open metadata ecosystem?",
                        "User accounts may be in one of 4 statuses:  Active, Disabled, Credentials Expired, or Locked.  Accounts must be in active status for the user to be granted access.  The security officer is interested in those accounts that are not active since this may represent a user who is in difficulty, or an error in the security set up.",
                        "https://egeria-project.org/concepts/user-identity/",
                        ProductPerspectiveDefinition.SECURITY,
                        ProductPerspectiveDefinition.ADMINISTRATION),

    USER_ACCOUNT_PROFILE("User Type Profile",
                        "What is the spread of the different types of the user accounts used to access the open metadata ecosystem?",
                        "User accounts may be for one of four types of user:  Employee, Contractor, External, or Digital.  Changes to profile of user account types may indicate a changing user base, and hence risk to the open metadata ecosystem.",
                        "https://egeria-project.org/concepts/user-identity/",
                        ProductPerspectiveDefinition.SECURITY),

    /**
     * Which standard values are used to describe open metadata, and what does each of them mean?
     */
    STANDARD_VALUES("Standard Values",
                    "Which standard values are used to describe open metadata, and what does each of them mean?",
                    "Many open metadata properties are restricted to a set of valid values, each with a description and often an example.  Knowing these values is what makes it possible to describe a resource consistently, to build an application or model that understands the descriptions it reads, and to join data from different products on a shared vocabulary.",
                    "https://egeria-project.org/guides/planning/valid-values/overview/",
                    ProductPerspectiveDefinition.DATA_EXPERT,
                    ProductPerspectiveDefinition.CONSUMER,
                    ProductPerspectiveDefinition.APP_AI_BUILDER),

    /**
     * Which open metadata properties have a defined set of valid values?
     */
    PROPERTIES_WITH_VALID_VALUES("Properties With Valid Values",
                                 "Which open metadata properties have a defined set of valid values?",
                                 "This is the index to the valid metadata value products: one record for each property that has a valid value set, with the property's description, data type and an example.  It is the place to start when deciding which property to use for a piece of information, or which value sets an application needs to load.",
                                 "https://egeria-project.org/guides/planning/valid-values/overview/",
                                 ProductPerspectiveDefinition.DATA_EXPERT,
                                 ProductPerspectiveDefinition.ARCHITECTURE,
                                 ProductPerspectiveDefinition.APP_AI_BUILDER),

    /**
     * Which reference data sets are available to standardise the values in my data?
     */
    REFERENCE_DATA_AVAILABLE("Reference Data Available",
                             "Which reference data sets are available to standardise the values in my data?",
                             "Reference data sets are the code tables and standard lists - countries, currencies, categories - that data in different systems can be aligned on.  Each product in this family is one such set, kept current from open metadata, so that a consumer can standardise their own data against it rather than maintaining a private copy.",
                             "https://egeria-project.org/features/reference-data-management/overview/",
                             ProductPerspectiveDefinition.DATA_EXPERT,
                             ProductPerspectiveDefinition.CONSUMER,
                             ProductPerspectiveDefinition.APP_AI_BUILDER,
                             ProductPerspectiveDefinition.STEWARD),

    /**
     * Which reference data sets does open metadata manage, and what does each one describe?
     */
    REFERENCE_DATA_SET_CATALOG("Reference Data Set Catalog",
                               "Which reference data sets does open metadata manage, and what does each one describe?",
                               "This is the index to the reference data products: one record per reference data set, with its description and scope.  A steward uses it to see what standard lists exist before creating another; a data expert uses it to find the set that fits a column.",
                               "https://egeria-project.org/features/reference-data-management/overview/",
                               ProductPerspectiveDefinition.DATA_EXPERT,
                               ProductPerspectiveDefinition.STEWARD),

    /**
     * How is the open metadata model structured?
     */
    OPEN_METADATA_MODEL("Open Metadata Model",
                        "How is the open metadata model structured?",
                        "The products in this family describe the open metadata types from different angles: the types themselves, the properties they use, the attributes of each type including those inherited, and the data types of those attributes.  Together they are the reference an architect or developer needs to read or generate metadata correctly.",
                        "https://egeria-project.org/types/",
                        ProductPerspectiveDefinition.ARCHITECTURE,
                        ProductPerspectiveDefinition.APP_AI_BUILDER,
                        ProductPerspectiveDefinition.DATA_EXPERT),

    /**
     * Which data types are used for open metadata properties, and how do they map to programming languages?
     */
    PROPERTY_DATA_TYPES("Property Data Types",
                        "Which data types are used for open metadata properties, and how do they map to programming languages?",
                        "Every open metadata property has one of a small set of data types.  This product lists them with their mapping to common programming languages and database types, which is what a developer needs when binding metadata to code, and what a cataloguer needs when describing a technology's own types.",
                        "https://egeria-project.org/concepts/open-metadata-type-definitions/",
                        ProductPerspectiveDefinition.APP_AI_BUILDER,
                        ProductPerspectiveDefinition.ARCHITECTURE),

    /**
     * Which properties are defined in the open metadata types, and what does each one mean?
     */
    OPEN_METADATA_PROPERTIES("Open Metadata Properties",
                             "Which properties are defined in the open metadata types, and what does each one mean?",
                             "One record per property name used anywhere in the open metadata types, with its description, data type and an example.  It answers the recurring question of whether a property already exists for a piece of information before a new one is invented.",
                             "https://egeria-project.org/concepts/open-metadata-type-definitions/",
                             ProductPerspectiveDefinition.DATA_EXPERT,
                             ProductPerspectiveDefinition.ARCHITECTURE,
                             ProductPerspectiveDefinition.APP_AI_BUILDER),

    /**
     * Which types of element, relationship and classification can open metadata describe?
     */
    OPEN_METADATA_TYPE_CATALOG("Open Metadata Type Catalog",
                               "Which types of element, relationship and classification can open metadata describe?",
                               "One record per open metadata type: its name, category, description, super type and the model area it belongs to.  It is the map of what the ecosystem can represent, for anyone designing how a new subject should be described or writing code that navigates the metadata.",
                               "https://egeria-project.org/concepts/open-metadata-type-definitions/",
                               ProductPerspectiveDefinition.ARCHITECTURE,
                               ProductPerspectiveDefinition.APP_AI_BUILDER,
                               ProductPerspectiveDefinition.DATA_EXPERT),

    /**
     * Which attributes, including inherited ones, does an open metadata type have?
     */
    ATTRIBUTES_OF_A_TYPE("Attributes Of A Type",
                         "Which attributes, including inherited ones, does an open metadata type have?",
                         "One record per attribute per type, with inherited attributes included, so that the complete shape of an element of any type can be read without walking the type hierarchy.  This is what code generators, schema mappers and query builders need.",
                         "https://egeria-project.org/concepts/open-metadata-type-definitions/",
                         ProductPerspectiveDefinition.APP_AI_BUILDER,
                         ProductPerspectiveDefinition.ARCHITECTURE),

    /**
     * Which people, organizations, locations and products does the open metadata ecosystem know about?
     */
    MASTER_DATA("Master Data",
                "Which people, organizations, locations and products does the open metadata ecosystem know about?",
                "The products in this family are the master lists of the key entities that other data refers to.  They are the join keys for reports and models, the reference for who owns and stewards what, and the record of where things are.",
                "https://egeria-project.org/concepts/actor-profile/",
                ProductPerspectiveDefinition.CONSUMER,
                ProductPerspectiveDefinition.DATA_EXPERT,
                ProductPerspectiveDefinition.STEWARD,
                ProductPerspectiveDefinition.OWNER),

    /**
     * Which organizations interact with the open metadata ecosystem?
     */
    ORGANIZATIONS("Organizations",
                  "Which organizations interact with the open metadata ecosystem?",
                  "One record per organization - the enterprise itself, its business units, partners and suppliers - as catalogued in open metadata.  It shows who the ecosystem serves and exchanges data with, which is the starting point for ownership, agreements and accountability.",
                  "https://egeria-project.org/concepts/organization/",
                  ProductPerspectiveDefinition.GOVERNANCE,
                  ProductPerspectiveDefinition.OWNER,
                  ProductPerspectiveDefinition.DATA_EXPERT),

    /**
     * Who are the people interacting with the open metadata ecosystem, and what are their roles?
     */
    PEOPLE("People",
           "Who are the people interacting with the open metadata ecosystem, and what are their roles?",
           "One record per person known to open metadata, with their profile and the roles they hold.  It answers who to contact about a resource, who is accountable for what, and how the community around the ecosystem is made up.",
           "https://egeria-project.org/concepts/personal-profile/",
           ProductPerspectiveDefinition.GOVERNANCE,
           ProductPerspectiveDefinition.STEWARD,
           ProductPerspectiveDefinition.COMMUNITY),

    /**
     * Which digital products are available, and what is the status of each?
     */
    DIGITAL_PRODUCT_CATALOG_CONTENTS("Digital Product Catalog Contents",
                                     "Which digital products are available, and what is the status of each?",
                                     "One record per digital product, with its description, category, status and product manager.  A consumer uses it to find something to subscribe to; a product owner uses it to see the whole portfolio and where each product is in its lifecycle.",
                                     "https://egeria-project.org/concepts/digital-product/",
                                     ProductPerspectiveDefinition.CONSUMER,
                                     ProductPerspectiveDefinition.OWNER,
                                     ProductPerspectiveDefinition.GOVERNANCE),

    /**
     * Which sites and facilities are known to the open metadata ecosystem, and what is at each?
     */
    LOCATIONS("Locations",
              "Which sites and facilities are known to the open metadata ecosystem, and what is at each?",
              "One record per location - a site, or a facility within a site.  Locations matter for data residency and privacy rules, for knowing which infrastructure is where, and for the governance requirements that vary by jurisdiction.",
              "https://egeria-project.org/types/0/0025-Locations/",
              ProductPerspectiveDefinition.PRIVACY,
              ProductPerspectiveDefinition.GOVERNANCE,
              ProductPerspectiveDefinition.ADMINISTRATION),

    /**
     * What have the surveys of the organization's resources discovered?
     */
    SURVEY_INSIGHTS("Survey Insights",
                    "What have the surveys of the organization's resources discovered?",
                    "The products in this family publish the results of surveys run by the survey action services: which resources were surveyed, what was found in each, what measurements were taken, and which findings need someone to act.  They turn survey reports scattered across the catalogue into data sets that can be tracked over time.",
                    "https://egeria-project.org/concepts/survey-report/",
                    ProductPerspectiveDefinition.STEWARD,
                    ProductPerspectiveDefinition.OWNER,
                    ProductPerspectiveDefinition.DATA_EXPERT,
                    ProductPerspectiveDefinition.ARCHITECTURE),

    /**
     * Which resources have been surveyed, when, and by which survey?
     */
    SURVEYS_RUN("Surveys Run",
                "Which resources have been surveyed, when, and by which survey?",
                "One record per survey report, naming the resource surveyed, the survey that ran and when.  It shows what is covered and what is not, and how current the coverage is, which is what a steward needs when deciding where to survey next.",
                "https://egeria-project.org/concepts/survey-report/",
                ProductPerspectiveDefinition.STEWARD,
                ProductPerspectiveDefinition.ADMINISTRATION),

    /**
     * What did the surveys find about each resource?
     */
    SURVEY_FINDINGS("Survey Findings",
                    "What did the surveys find about each resource?",
                    "One record per annotation from every survey report: the type of finding, the resource it concerns, its explanation and confidence.  It is the full detail behind the survey products that follow, for anyone investigating a particular resource.",
                    "https://egeria-project.org/concepts/survey-report/",
                    ProductPerspectiveDefinition.STEWARD,
                    ProductPerspectiveDefinition.DATA_EXPERT,
                    ProductPerspectiveDefinition.OWNER),

    /**
     * Which issues found by surveys need someone to act on them?
     */
    ACTIONS_REQUESTED_BY_SURVEYS("Actions Requested By Surveys",
                                 "Which issues found by surveys need someone to act on them?",
                                 "Surveys raise a request for action when they find something that needs a person's decision - data that does not match its schema, files that cannot be classified, resources that are not what the catalogue says.  This product lists those requests, so the work can be assigned and tracked rather than sitting inside individual reports.",
                                 "https://egeria-project.org/concepts/request-for-action/",
                                 ProductPerspectiveDefinition.STEWARD,
                                 ProductPerspectiveDefinition.OWNER,
                                 ProductPerspectiveDefinition.ADMINISTRATION),

    /**
     * Which elements are the subject of a request for action, and what is asked of them?
     */
    ELEMENTS_NEEDING_ACTION("Elements Needing Action",
                            "Which elements are the subject of a request for action, and what is asked of them?",
                            "One record per element that a request for action names as its target, alongside the request.  Where the request list says what was found, this says exactly which resources are affected, so that the owner of each can be told.",
                            "https://egeria-project.org/concepts/request-for-action/",
                            ProductPerspectiveDefinition.STEWARD,
                            ProductPerspectiveDefinition.OWNER),

    /**
     * How large and how busy are the relational database servers?
     */
    DATABASE_SERVER_SIZE("Database Server Size",
                         "How large and how busy are the relational database servers?",
                         "The measurements a survey collects from a relational data manager as a whole: the databases it hosts, their sizes and the activity against them.  It is the basis for capacity planning, for cost allocation, and for seeing which servers carry the most data.",
                         "https://egeria-project.org/guides/developer/survey-action-services/overview/",
                         ProductPerspectiveDefinition.ADMINISTRATION,
                         ProductPerspectiveDefinition.FINANCIAL,
                         ProductPerspectiveDefinition.ARCHITECTURE),

    /**
     * How large are the database schemas, and how are they growing?
     */
    SCHEMA_SIZE("Schema Size",
                "How large are the database schemas, and how are they growing?",
                "One record per relational schema surveyed, from any database, with its size and table counts.  Comparing surveys over time shows growth, which is what capacity and cost decisions rest on.",
                "https://egeria-project.org/guides/developer/survey-action-services/overview/",
                ProductPerspectiveDefinition.ADMINISTRATION,
                ProductPerspectiveDefinition.FINANCIAL,
                ProductPerspectiveDefinition.DATA_EXPERT),

    /**
     * How large and how actively used are the database tables?
     */
    TABLE_SIZE_AND_ACTIVITY("Table Size And Activity",
                            "How large and how actively used are the database tables?",
                            "One record per relational table surveyed, with row counts, size and the reads and writes seen against it.  It tells a data expert which tables carry the data that matters, and an administrator which are growing or idle.",
                            "https://egeria-project.org/guides/developer/survey-action-services/overview/",
                            ProductPerspectiveDefinition.DATA_EXPERT,
                            ProductPerspectiveDefinition.ADMINISTRATION,
                            ProductPerspectiveDefinition.OWNER),

    /**
     * What values and value patterns are found in each database column?
     */
    COLUMN_PROFILE("Column Profile",
                   "What values and value patterns are found in each database column?",
                   "One record per relational column surveyed, with its data type and the profile of its values: nulls, distinct values, ranges and patterns.  It is how a data expert judges whether a column is fit for a purpose, a steward finds quality problems, and a privacy officer spots columns that may hold personal data.",
                   "https://egeria-project.org/guides/developer/survey-action-services/overview/",
                   ProductPerspectiveDefinition.DATA_EXPERT,
                   ProductPerspectiveDefinition.STEWARD,
                   ProductPerspectiveDefinition.PRIVACY),

    /**
     * Which files were surveyed, and what type, size and age is each?
     */
    FILE_DETAILS("File Details",
                 "Which files were surveyed, and what type, size and age is each?",
                 "One record per file found by a folder survey, with its classification, size, encoding and timestamps.  It gives an inventory of what is on the file systems, what kind of content it is, and how old it is.",
                 "https://egeria-project.org/guides/developer/survey-action-services/overview/",
                 ProductPerspectiveDefinition.ADMINISTRATION,
                 ProductPerspectiveDefinition.DATA_EXPERT,
                 ProductPerspectiveDefinition.STEWARD),

    /**
     * What is in each directory, and how much space does it take?
     */
    DIRECTORY_CONTENTS("Directory Contents",
                       "What is in each directory, and how much space does it take?",
                       "One record per directory surveyed, including subdirectories, with counts and sizes of the files it holds by type.  It shows where the storage goes and which directories hold data worth cataloguing.",
                       "https://egeria-project.org/guides/developer/survey-action-services/overview/",
                       ProductPerspectiveDefinition.ADMINISTRATION,
                       ProductPerspectiveDefinition.FINANCIAL),

    /**
     * What are the physical characteristics of each surveyed resource?
     */
    RESOURCE_PHYSICAL_DETAILS("Resource Physical Details",
                              "What are the physical characteristics of each surveyed resource?",
                              "One record per resource surveyed, with the physical measurements the survey took of it - size, record counts, modification times and the like - so that a resource's footprint and freshness can be seen without opening it.",
                              "https://egeria-project.org/guides/developer/survey-action-services/overview/",
                              ProductPerspectiveDefinition.ADMINISTRATION,
                              ProductPerspectiveDefinition.OWNER),

    /**
     * What does the profile of the data in a resource look like?
     */
    DATA_PROFILE("Data Profile",
                 "What does the profile of the data in a resource look like?",
                 "One record per profile measurement from the surveys: value distributions, patterns, lengths and ranges for the data in each surveyed resource.  It is the evidence for whether data is usable for analysis or training, and where it needs cleaning first.",
                 "https://egeria-project.org/guides/developer/survey-action-services/overview/",
                 ProductPerspectiveDefinition.DATA_EXPERT,
                 ProductPerspectiveDefinition.STEWARD,
                 ProductPerspectiveDefinition.APP_AI_BUILDER),

    /**
     * Which files could not be classified, so that the file reference data can be extended?
     */
    UNCLASSIFIED_FILES("Unclassified Files",
                       "Which files could not be classified, so that the file reference data can be extended?",
                       "The file surveys classify each file using the file type reference data in the Core Content Pack.  This product lists the files that matched nothing, which is the work list for extending that reference data - and a sign of file types arriving that nobody has described yet.",
                       "https://egeria-project.org/concepts/file-type/",
                       ProductPerspectiveDefinition.STEWARD,
                       ProductPerspectiveDefinition.ADMINISTRATION),

    /**
     * How active is the organization in the open metadata ecosystem?
     */
    ORGANIZATION_ACTIVITY("Organization Activity",
                          "How active is the organization in the open metadata ecosystem?",
                          "The products in this family publish regular insights about who is doing what across the ecosystem - contributions, roles taken up, communities forming.  Subscribers keep the history, so trends in engagement can be seen.",
                          "https://egeria-project.org/concepts/community/",
                          ProductPerspectiveDefinition.GOVERNANCE,
                          ProductPerspectiveDefinition.COMMUNITY),

    /**
     * How is governance being applied across the organization's resources?
     */
    GOVERNANCE_ACTIVITY("Governance Activity",
                        "How is governance being applied across the organization's resources?",
                        "The products in this family publish the governance definitions in force - controls, exceptions, certifications and licenses - as data sets refreshed from open metadata.  They let governance be reported on and audited rather than read one definition at a time.",
                        "https://egeria-project.org/concepts/governance-definition/",
                        ProductPerspectiveDefinition.GOVERNANCE,
                        ProductPerspectiveDefinition.STEWARD),

    /**
     * Which governance controls are defined, and what does each one require?
     */
    GOVERNANCE_CONTROLS_IN_PLACE("Governance Controls In Place",
                                 "Which governance controls are defined, and what does each one require?",
                                 "One record per governance control, with its scope, the requirement it implements and its status.  It is the list of what the organization has committed to enforce, which governance leads, stewards and security officers all need to see in one place.",
                                 "https://egeria-project.org/concepts/governance-definition/",
                                 ProductPerspectiveDefinition.GOVERNANCE,
                                 ProductPerspectiveDefinition.STEWARD,
                                 ProductPerspectiveDefinition.SECURITY,
                                 ProductPerspectiveDefinition.ARCHITECTURE),

    /**
     * Which exceptions to governance requirements have been granted, to whom and why?
     */
    GOVERNANCE_EXCEPTIONS("Governance Exceptions",
                          "Which exceptions to governance requirements have been granted, to whom and why?",
                          "One record per exception: the requirement it relaxes, the resource or actor it applies to, its justification and its expiry.  Exceptions are where risk accumulates quietly, so being able to list and review them is a control in itself.",
                          "https://egeria-project.org/concepts/governance-definition/",
                          ProductPerspectiveDefinition.GOVERNANCE,
                          ProductPerspectiveDefinition.SECURITY,
                          ProductPerspectiveDefinition.STEWARD),

    /**
     * Which resources are certified, against which standards, and when do the certifications expire?
     */
    CERTIFICATIONS_HELD("Certifications Held",
                        "Which resources are certified, against which standards, and when do the certifications expire?",
                        "One record per certification known to open metadata, with the resource certified, the certification type and its dates.  A consumer uses it to confirm a resource meets the standard they need; a governance lead uses it to see what is due for renewal.",
                        "https://egeria-project.org/concepts/governance-definition/",
                        ProductPerspectiveDefinition.GOVERNANCE,
                        ProductPerspectiveDefinition.CONSUMER,
                        ProductPerspectiveDefinition.STEWARD),

    /**
     * Which licenses apply to the organization's resources, and what do they permit?
     */
    LICENSES_GRANTED("Licenses Granted",
                     "Which licenses apply to the organization's resources, and what do they permit?",
                     "One record per license known to open metadata: the resource licensed, the license type and its terms.  It answers what a consumer may do with a resource, and what the organization is paying for or has committed to.",
                     "https://egeria-project.org/concepts/governance-definition/",
                     ProductPerspectiveDefinition.GOVERNANCE,
                     ProductPerspectiveDefinition.CONSUMER,
                     ProductPerspectiveDefinition.FINANCIAL),

    /**
     * How healthy is the IT infrastructure supporting the open metadata ecosystem?
     */
    IT_OPERATIONS_HEALTH("IT Operations Health",
                         "How healthy is the IT infrastructure supporting the open metadata ecosystem?",
                         "The products in this family publish regular insights about the servers and services the ecosystem runs on.  Subscribers keep the history, so an administrator can see how the estate is changing and an architect can see what is actually deployed.",
                         "https://egeria-project.org/types/0/0040-Software-Servers/",
                         ProductPerspectiveDefinition.ADMINISTRATION,
                         ProductPerspectiveDefinition.ARCHITECTURE),

    /**
     * Which software servers are catalogued, where do they run, and what do they host?
     */
    SOFTWARE_SERVERS("Software Servers",
                     "Which software servers are catalogued, where do they run, and what do they host?",
                     "One record per software server, with its type, the platform and location it runs on, and the capabilities it hosts.  It is the inventory that operations, architecture and security each need a view of.",
                     "https://egeria-project.org/types/0/0040-Software-Servers/",
                     ProductPerspectiveDefinition.ADMINISTRATION,
                     ProductPerspectiveDefinition.ARCHITECTURE,
                     ProductPerspectiveDefinition.SECURITY),

    /**
     * What is the security posture of the open metadata ecosystem?
     */
    SECURITY_POSTURE("Security Posture",
                     "What is the security posture of the open metadata ecosystem?",
                     "The products in this family publish regular insights about the security settings of the ecosystem: its secrets, its users and accounts, its governance zones and the services it exposes.  Subscribers keep the history, so changes in posture show up rather than passing unnoticed.",
                     "https://egeria-project.org/concepts/user-identity/",
                     ProductPerspectiveDefinition.SECURITY,
                     ProductPerspectiveDefinition.GOVERNANCE),

    /**
     * Where are secrets held, and which systems depend on them?
     */
    SECRETS_MANAGEMENT("Secrets Management",
                       "Where are secrets held, and which systems depend on them?",
                       "The products in this family list the secrets stores and the collections within them that the ecosystem's servers and connectors rely on.  They show where credentials live, which is where a security officer starts when rotating them or assessing exposure.",
                       "https://egeria-project.org/egeria-solutions/distributed-secrets/overview/",
                       ProductPerspectiveDefinition.SECURITY,
                       ProductPerspectiveDefinition.ADMINISTRATION),

    /**
     * Which secrets stores are in use, and which servers and connectors rely on each?
     */
    SECRETS_STORES("Secrets Stores",
                   "Which secrets stores are in use, and which servers and connectors rely on each?",
                   "One record per secrets store known to open metadata, with its location and the elements that reference it.  It answers where a secret would have to be changed, and what would be affected if a store were unavailable.",
                   "https://egeria-project.org/egeria-solutions/distributed-secrets/overview/",
                   ProductPerspectiveDefinition.SECURITY,
                   ProductPerspectiveDefinition.ADMINISTRATION),

    /**
     * Which secrets collections exist in each store, and who uses them?
     */
    SECRETS_COLLECTIONS("Secrets Collections",
                        "Which secrets collections exist in each store, and who uses them?",
                        "One record per secrets collection, with its store and the connections and users that draw on it.  It is the detail beneath the secrets store list, for tracing a credential to the things that use it.",
                        "https://egeria-project.org/egeria-solutions/distributed-secrets/overview/",
                        ProductPerspectiveDefinition.SECURITY,
                        ProductPerspectiveDefinition.ADMINISTRATION),

    /**
     * Who can access the open metadata ecosystem, and how?
     */
    USER_POPULATION("User Population",
                    "Who can access the open metadata ecosystem, and how?",
                    "The products in this family publish regular insights about the ecosystem's users: the identities known to it and the accounts, with their status and type, that grant access.  Subscribers keep the history, so growth and drift in the user base can be seen.",
                    "https://egeria-project.org/concepts/user-identity/",
                    ProductPerspectiveDefinition.SECURITY,
                    ProductPerspectiveDefinition.ADMINISTRATION,
                    ProductPerspectiveDefinition.GOVERNANCE),

    /**
     * Which user identities exist, and which person, team or system does each belong to?
     */
    USER_IDENTITIES("User Identities",
                    "Which user identities exist, and which person, team or system does each belong to?",
                    "One record per user identity, with the actor profile it is linked to.  It connects the identities that appear in audit logs and access controls to the people and systems behind them, which is what security, administration and privacy reviews all depend on.",
                    "https://egeria-project.org/concepts/user-identity/",
                    ProductPerspectiveDefinition.SECURITY,
                    ProductPerspectiveDefinition.ADMINISTRATION,
                    ProductPerspectiveDefinition.PRIVACY),

    /**
     * How is the visibility of metadata controlled through governance zones?
     */
    VISIBILITY_OF_METADATA("Visibility Of Metadata",
                           "How is the visibility of metadata controlled through governance zones?",
                           "The products in this family publish regular insights about the governance zones: how many elements each holds and of which types.  Zones decide who can see what, so their contents are the practical statement of the ecosystem's access model.",
                           "https://egeria-project.org/concepts/governance-zone/",
                           ProductPerspectiveDefinition.SECURITY,
                           ProductPerspectiveDefinition.GOVERNANCE,
                           ProductPerspectiveDefinition.ARCHITECTURE),

    /**
     * How many elements are in each governance zone?
     */
    ZONE_SIZES("Zone Sizes",
               "How many elements are in each governance zone?",
               "One record per governance zone, with the total count of members and related elements.  An empty zone, or one growing unexpectedly, is a sign that zone assignment is not working as intended.",
               "https://egeria-project.org/concepts/governance-zone/",
               ProductPerspectiveDefinition.SECURITY,
               ProductPerspectiveDefinition.GOVERNANCE,
               ProductPerspectiveDefinition.ADMINISTRATION),

    /**
     * What types of element does each governance zone contain?
     */
    ZONE_CONTENTS_BY_TYPE("Zone Contents By Type",
                          "What types of element does each governance zone contain?",
                          "One record per governance zone and element type, with the count of elements of that type in the zone.  It shows whether a zone holds what its purpose says it should - data assets in a data zone, say - and nothing it should not.",
                          "https://egeria-project.org/concepts/governance-zone/",
                          ProductPerspectiveDefinition.SECURITY,
                          ProductPerspectiveDefinition.GOVERNANCE,
                          ProductPerspectiveDefinition.ARCHITECTURE),

    /**
     * Which servers, services and operations are exposed, and how are they secured?
     */
    SERVICES_EXPOSED("Services Exposed",
                     "Which servers, services and operations are exposed, and how are they secured?",
                     "The products in this family publish regular insights about the servers, services and operations that make up the ecosystem's attack surface.  Subscribers keep the history, so a new service appearing, or a security setting changing, is visible.",
                     "https://egeria-project.org/types/0/0040-Software-Servers/",
                     ProductPerspectiveDefinition.SECURITY,
                     ProductPerspectiveDefinition.ADMINISTRATION,
                     ProductPerspectiveDefinition.ARCHITECTURE),

    ;

    private final String                             displayName;
    private final String                             summary;
    private final String                             description;
    private final String                             url;
    private final List<ProductPerspectiveDefinition> perspectives;


    /**
     * The constructor creates an instance of the enum
     *
     * @param displayName  unique id for the enum
     * @param summary      name for the enum
     * @param description  description of the use of this value
     * @param url          optional url for the term
     * @param perspectives perspectives that this question is associated with - none means the question is not
     *                     specific to any perspective
     */
    ProductQuestionDefinition(String                          displayName,
                              String                          summary,
                              String                          description,
                              String                          url,
                              ProductPerspectiveDefinition... perspectives)
    {
        this.displayName  = displayName;
        this.summary      = summary;
        this.description  = description;
        this.url          = url;
        this.perspectives = List.copyOf(Arrays.asList(perspectives));
    }


    /**
     * Return the qualified name to use for the glossary term.
     *
     * @return string
     */
    public String getQualifiedName()
    {
        return OpenMetadataType.GLOSSARY_TERM.typeName + "::Jacquard::Question::" + displayName;
    }


    /**
     * Return the display name for this term.
     *
     * @return string
     */
    public String getDisplayName()
    {
        return displayName;
    }


    /**
     * Return the summary for this term.
     *
     * @return string
     */
    public String getSummary()
    {
        return summary;
    }


    /**
     * Return the description for this term.
     *
     * @return string
     */
    public String getDescription()
    {
        return description;
    }


    /**
     * Return the URL for this term.
     *
     * @return string
     */
    public String getURL()
    {
        return url;
    }


    /**
     * Return the folder for this term.
     *
     * @return ProductFolderDefinition
     */
    public ProductFolderDefinition getFolder()
    {
        return ProductFolderDefinition.GLOSSARY_QUESTIONS;
    }


    /**
     * Return the perspectives that this question is associated with.
     *
     * @return list of perspectives - empty if the question is not specific to any perspective
     */
    public List<ProductPerspectiveDefinition> getPerspectives()
    {
        return perspectives;
    }


    /**
     * Output of this enum class and main value.
     *
     * @return string showing enum value
     */
    @Override
    public String toString()
    {
        return "ProductQuestionDefinition{" + summary + '}';
    }
}
