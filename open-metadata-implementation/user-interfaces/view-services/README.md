<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata View Services (OMVS)

The Open MetadataView Services (OMVS or view services) provide domain-specific services
for user interfaces to integrate with open metadata.

The view services are as follows:

* **[asset-search-view](asset-search-view)** - search for assets.

  The Asset Search OMVS provides search and query capabilities to support an asset catalog function.
It supports search requests for assets with specific characteristics
and returns summaries of the matching assets, plus methods to allow drill-down
into the details of a specific asset to related metadata.

* **[open-lineage-view](open-lineage-view)** - explore open metadata lineage

  The Open Lineage OMVS provides access to open metadata lineage, to support a UI in showing
  meaningful subsets of lineage; for example the ultimate source of an asset, which means showing the 
  information supply chain back to where it originated.    
 
* **[type-explorer-view](type-explorer-view)** - provides APIs that support a UI to explore and navigate around 
the open metadata types.

* **[subject-area-view](subject-area-view)** - provides APIS that support a Ui to author subject area content. 

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.