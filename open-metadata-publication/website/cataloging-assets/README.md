<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Building a asset catalog

One of the most fundamental reasons for managing metadata is to offer a catalog service
that allows individuals and automated services to search for, select and retrieve information
about relevant data, systems, applications, software components, processes, ... to help them
achieve their specific task.

Each of these types of "things" that are being searched for are represented in open metadata as 
**[Assets](../../../open-metadata-implementation/access-services/docs/concepts/assets)**.

This page describes the different approaches to building a catalog of assets that are
offered by Egeria.  For each approach there is a description of the types of use cases
and and business value they enable as well as pointers to the services and configuration
needed to get them working.

There are three parts to this:

* [Information managed in an asset catalog](asset-catalog-contents.md) -
  A description of the incremental value offered by the different types of information that
  can be stored in an asset catalog.
  
* [Scaling the asset catalog through automation](scaling-asset-catalog.md) -
  A description of how to use automation to expand the contents of your asset catalog
  without creating a huge workload for your people.
  
* [Providing access to the catalog service](accessing-asset-catalog.md) -
  A description of the APIs available to access your asset catalog.

## Related information

* The [Asset](../../../open-metadata-implementation/access-services/docs/concepts/assets)
  page provides more information on the different types of assets supported by open metadata.
  The asset types can be extended dynamically if needed.
  
* The type definition for the **Asset** entity is found in model [0010 Basic Model](../open-metadata-types/0010-Base-Model.md).

* Examples of representing different types of assets using the open metadata types are found in
  [Modelling Assets]().
  
* The [Open Metadata Labs](../../../open-metadata-resources/open-metadata-labs) provide practical
  examples showing all of the techniques to manage an asset catalog, allowing you to try each of
  the features to assess how they could work in your organization.

----
Return to [Home Page](../../../index.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.