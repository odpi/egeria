<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

![InDev](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

# Asset Manager Open Metadata Access Service (OMAS)

The Asset Manager OMAS provides APIs for supporting the
exchange of metadata with a third party asset manager.
An [asset manager](../docs/concepts/server-capabilities/asset-manager.md)
is typically a catalog of [assets](../docs/concepts/assets).  It is responsible
for maintaining details of the assets including their characteristics,
ownership, assessments and governance requirements.

The Asset Manager OMAS (in conjunction with the
[Catalog Integrator OMIS](../../integration-services/catalog-integrator))
provides a new integration path for metadata catalogs the goes
via an integration service hosted in an
[integration daemon](../../admin-services/docs/concepts/integration-daemon.md).

Catalogs will be able to have a two-way integration through this
path without needing to conform to the repository service rules
for managing home and reference copies. This is possible for two reasons:

* Since the metadata from the catalog passes through an OMAS, 
Egeria will be able to have a better control of the metadata from the catalog.
* Since the catalog is not part of a federated query, 
any inconsistent updates to metadata that occurs in its repository, 
only impacts the users of that catalog and not the whole cohort.

## External Identifiers

A major challenge when exchanging metadata with third party asset managers is that
there is often a mismatch between the structure of the metadata in a
third party asset manager and the structure of the
[open metadata types](../../../open-metadata-publication/website/open-metadata-types)
used by the Asset Manager OMAS.

For this reason, the Asset Manager OMAS supports the ability to associate multiple
external identifiers with an open metadata instance.  These external identifiers are scoped
to a particular third party asset manager so that there is no confusion if two
third party asset managers happen to use the same unique identifier within their repositories.
Each of these external identifiers can be mapped to the appropriate open metadata instances without confusion.

There are also API calls for querying open metadata instances
using external identifiers and the identifier of the third party asset manager.

More information on the use of external identifiers to map between metadata elements in
third party asset managers and open metadata instances can be found
[here](../../../open-metadata-publication/website/external-identifiers).


## Supplementary Properties

It is common for external asset managers to include extensive descriptive properties for assets
that include both a technical name and description as well as a business name and description.
The Asset Manager OMAS supports this distinction and stores the technical name and
description in an Asset metadata instance and the business name and description in a glossary term
metadata instance that is linked to the asset using a
[**MoreInformation** relationship](../../../open-metadata-publication/website/open-metadata-types/0019-More-Information.md).
The properties that are stored in the glossary term are referred to as supplementary properties.


----
Return to the [access-services](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.