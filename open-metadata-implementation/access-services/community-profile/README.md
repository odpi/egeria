<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

# Community Profile Open Metadata Access Service (OMAS)

The Community Profile OMAS provides APIs and events for tools and applications
that are managing information about people and the way they work together.

In particular the Community Profile OMAS supports [personal profiles](docs/concepts/personal-profile.md),
[organizations](../docs/concepts/organizations/README.md) and [teams](../docs/concepts/organizations/team.md) along with 
[communities](docs/concepts/community.md) of people collaborating around data.

With this information, open metadata reduces the friction between people
from different silos of an organization that can prevent the effective use of data.
For example, an [Asset Owner](../docs/concepts/user-roles/asset-owner.md) can monitor who is using their
[asset](../../../open-metadata-implementation/access-services/docs/concepts/assets) and for what purposes.
At the same time a data scientist or business analyst can find out which assets
their colleagues are using, and any feedback that they gave, which helps to guide them
to the most appropriate assets for their work.

## Is this metadata ?

Data about people and organizations is not strictly metadata.  It is master data.
This means it is a type of data that is widely duplicated across an organization's systems
but mercifully slowly changing.  Open metadata is therefore only one of many systems
making use of this data.

Many organizations use a centralized user directory, such as LDAP, for their employees.
In addition large organizations with thousands of systems may also have a
master data management (MDM) program to keep their data
about people and organization's synchronized amongst their systems.

With or without MDM, it is important that the Community Profile OMAS can operate as a
downstream consumer of this data, rather than operating as an independent island.
This way it adds value to the organization by enabling the recording of asset ownership,
use and feedback, without an excessive amount of additional administration.

## Digging Deeper

* [User Documentation](docs/user)
* [Design Documentation](docs/design)

----
Return to the [access-services](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

