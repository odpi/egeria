<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Discovery and stewardship

Integrated cataloging typically automates the creation the basic asset entry, its connection and
optionally, its schema.  This is what is called technical metadata.

Metadata discovery uses advanced analysis to inspect the content of specific assets to derive
new insights that can augment or validate their catalog entry.

The results can either be automatically applied to the asset's catalog entry or
it can go through a stewardship process to have a subject matter expert confirm the findings (or not).

Discovery and stewardship are the most advanced form of automation for asset cataloging.
Egeria provides the server runtime environment and component framework to
allow third parties to create discovery services and stewardship action implementations.
It has only simple implementations of these components, mostly for demonstration purposes.
This is the area where vendors and other open source projects are expected to
provide additional value.

There is more information on this topic on the
[metadata discovery](../metadata-discovery) page.

## Adding automation

Below are three types of automation to minimise the effort in managing your asset catalog.

* [Templated cataloging](templated-cataloging.md) - copying predefined assets.
* [Integrated cataloging](integrated-cataloging.md) - automated extraction of metadata from third party technologies.


----
* Return to [cataloging assets](.)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.