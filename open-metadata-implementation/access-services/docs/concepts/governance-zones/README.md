<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Governance Zones

A **Governance Zone** defines a list of assets that are grouped together for a specific purpose.
For example they are consumed or managed in a particular way, visible to particular groups of users,
or processed by particular types of engine.

An asset can belong to none, one or many zones.  The list of zones that an asset belongs to is configured in
its **zoneMembership** property.

The Open Metadata Access Services (OMASs) that work specifically with asset, such as
[Asset Catalog](../../../asset-catalog), [Asset Consumer](../../../asset-consumer) and [Asset Owner](../../../asset-owner),
support the following properties that are set in the configuration document at start up.

* **supportedZones** - this defines the zones of assets that can be returned by this instance of the OMAS
* **defaultZones** - this defines the list of zones set up in any new asset created by the OMAS.

The meaning, purpose and governance requirements for assets within a specific zone are maintained through
the [Governance Program](../../../governance-program) OMAS.

It is also possible to associate [security access control with a governance zone](../../../../common-services/metadata-security).


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.