<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata Security Samples

The open metadata security samples provide sample implementations of the
connectors that can be added to the OMAG Server Platform and to any OMAG server
running on the platform.  With these samples it is possible to experiment with
how security authorization works with the
[governance zones](../../../open-metadata-implementation/access-services/docs/concepts/governance-zones)
that control the visibility of assets
through the [Open Metadata Access Services (OMASs)](../../../open-metadata-implementation/access-services).

There is one connector for the platform services that are not specific to any server
(that is there is no server name passed in the URL).  The other connector is specific to an OMAG server instance and
is defined in the configuration document for a server.

The samples show how a security connector extends the appropriate base class and uses their methods to provide
a security service.

The samples are based on the [Coco Pharmaceuticals persona](https://opengovernance.odpi.org/coco-pharmaceuticals/personas/).

Gary Geeke (userId=garygeeke) is the IT Infrastructure Administrator and the IT Infrastructure Governance Officer.
He is the only person able to issue platform services, and work with assets in the **it-infrastructure** zone.

Peter Profile (userId=peterprofile), Information Analyst, and Erin Overview (userId=erinoverview), their Information Architect and Deputy Chief Data Officer,
are the only people permitted to on board new assets through the **quarantine** zone using the
[Asset Owner OMAS](../../../open-metadata-implementation/access-services/asset-owner).  Specifically
only Erin can move assets from the quarantine zone to other zones.


The other zones defined in the sample are:




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.