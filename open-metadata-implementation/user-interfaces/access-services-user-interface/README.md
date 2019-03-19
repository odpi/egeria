<!-- SPDX-License-Identifier: Apache-2.0 -->

# Access Services User Interface

The Access Services User Interface provides a user interface that supports
the user-facing open metadata access services.  It is a role-based UI that
offers different capabilities based on the end user's role.  This makes it
useful for demonstrations.

## Catalog Search

One of the important reasons for managing metadata about data assets is the
ability for individuals to search and locate the data they need to make new uses
of an organization's data.
The metadata describes the location, structure and content of the data to
varying degrees of detail.
By bringing the metadata for data assets from across the enterprise,
an organization is able to manage and use this data more effectively.

Collectively, the open metadata repositories should bring together all of
the knowledge the organization has about each data source in order
to have enough information to differentiate between them during the data selection process.

Figure 1 below shows the types of information about a data source that should
be available through the the open metadata repositories. 
 
![Figure 1: Drill-down from catalog search results to explore the content and qualities of a data set](drilldown-of-metadata-entity-pharma.png)
> Figure 1: Drill-down from catalog search results to explore the content and qualities of a data set

This metadata is returned by the 
**[Connected Asset OMAS](../../../open-metadata-implementation/access-services/connected-asset/README.md)**.

###Search solution
The search will return Assets and Glossary terms that match the search criteria.
As the asset search is to be performed against on one or more repositories Elasticsearch will be used. 
The search will be performed using name/displayNamer, descriptions and qualified name of the asset and/or Glossary terms.
Initial indexing will be performed by the **[Asset Catalog OMAS](../../../open-metadata-implementation/access-services/asset-asset/README.md)** according to supported zones of the instance.
The search result will contain: guid, name (name or displayName), description, qualifiedName, classifications, zoneMembership.
    
![Figure 2: Search engine integration](docs/egeria-asset-search.png)
> Figure 2: Search engine integration

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.