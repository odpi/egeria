<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Integrated cataloging

Integrated cataloging uses an **integration daemon**
to monitor a specific digital technology that hosts particular types of assets and automatically
maintain the catalog with information about these assets.

For example, an integration daemon may be monitoring a database server, updating the asset catalog each time a
new database is added, or a schema is changed.

Integration daemons support different integration patterns to meet the variety of capabilities
of digital technologies.  For example, it may poll the technology or listen for events from it
that indicate changes in the assets.
They are limited to the metadata supported by the technology they are working with

There is more information on the use of integrated cataloging on the
[integration daemon](../../../open-metadata-implementation/admin-services/docs/concepts/integration-daemon.md)
page.


## Other types of automation

Below are other types of automation to minimise the effort in managing your asset catalog.

* [Templated cataloging](templated-cataloging.md) - copying predefined assets.
* [Discovery and stewardship](discovery-and-stewardship.md) - analysis of asset contents to create metadata

----
* Return to [cataloging assets](.)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.