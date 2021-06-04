<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# Configuring the Asset Lineage OMAS


The Asset Lineage OMAS can be activated in an OMAG Server that has the Open Metadata Repository Services (OMRS)
[Enterprise Repository Services](../../../../../repository-services/docs/subsystem-descriptions/enterprise-repository-services.md)
enabled.

For the Asset Lineage Open Metadata Access Service configuration, the example from [the Open Metadata Access Services](../../../../../admin-services/docs/user/configuring-the-access-services.md)
can be used. In order to enable only this access service on the server, "asset-lineage" should be provided for the endpoint available at the access-service level.

This OMAS supports the configuration of the specific list of lineage classifications:

* [Configuring the lineage classifications](configuring-the-lineage-classifications.md)

The size of lineage events for the type GlossaryTerm can be configured using the parameter `glossaryTermLineageEventsChunkSize` set up in `accessServiceOptions`.
The value of `glossaryTermLineageEventsChunkSize` represents the number of relationships related to the GlossaryTerm that will be included in each event.
If `glossaryTermLineageEventsChunkSize` is not defined, the default value is 1 and for each relationship an event will be generated. 


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.