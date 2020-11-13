<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

# Stewardship Engine Services

The stewardship engine services are responsible for the resolution of the
**Request for Action** annotations made by the
discovery services or other activity on the Assets.

A Request for Action is raised when an unexpected or invalid
condition is detected in the data landscape.
It is stored in the metadata repository, linked to the description of
the Asset where the situation was detected.

The Stewardship Action OMAS is listening for the creation
of Request for Action entities and sends out an event
for each one.

Stewardship services is a client of the Stewardship Action OMAS,
receiving these Requests for Action, managing the triage process
and initiating remediation action where appropriate.



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.