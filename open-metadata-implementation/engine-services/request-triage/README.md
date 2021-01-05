<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

# Request Triage Open Metadata Engine Service (OMES)

The Request Triage OMES is responsible for the resolution of the
**Request for Action** annotations made by the discovery services or other activity on the Assets.

A Request for Action is raised when an unexpected or invalid
condition is detected in the data landscape.
It is stored in the metadata repository, linked to the description of
the Asset where the situation was detected.

The [Stewardship Action OMAS](../../access-services/stewardship-action) is listening for the creation
of Request for Action entities and sends out an event
for each one.

Request Triage OMES is a client of the Stewardship Action OMAS,
receiving these Requests for Action events.

It runs triage rules to 
determine how to manage the request.  This could be to initiate an external workflow, wait for manual decision or 
initiate a remediation request.

Request Triage OMES loads **Open Triage Engines**,
runs **Open Triage Services** and
calls the [Stewardship Action OMAS](../../access-services/stewardship-action) to manage the status of the
Request for Action.

----
* Return to the [Engine Services](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.