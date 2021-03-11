<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata Exchange Rule

The open metadata exchange rule can be used to limit the exchange of metadata.  There are three rules
supported by the [open metadata repository services](../..):

* **EventsToSend Rule**: controls the [instance events](event-descriptions/instance-events.md) that should be sent
as a result of changes to
the metadata stored in the local repository to the
[open metadata repository cohorts](open-metadata-repository-cohort.md) that ii is a [member of](cohort-member.md).

* **EventsToProcess Rule**: controls which incoming events are received from the other members of the
open metadata repository cohort(s).  These events are passed to the:

  * [Enterprise Repository Services](subsystem-descriptions/enterprise-repository-services.md) which passes the
events onto the [Open Metadata Access Services](../../access-services).
  * [Local Repository Services](subsystem-descriptions/local-repository-services.md).
  
* **EventsToSave Rule**: controls which of the inbound events received by the Local Repository Services
should be saved to the local repository.


----
* Return to [Repository Services Design](.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.