<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

![InDev](../../../open-metadata-publication/website/images/egeria-content-status-in-development.png#pagewidth)

# Stewardship Action Open Metadata Access Service (OMAS)

The Stewardship Action OMAS provides APIs and events for tools and applications
focused on resolving issues detected in the data landscape.

It works in partnership with the stewardship server to execute
functions that detect, report on and implement the resolution of issues.
These functions are called **stewardship actions**.

For example, when a change is detected in an Asset metadata entity,
the Stewardship Action OMAS runs the stewardship actions associated with that asset.
These could be:
* detect and emit an event if a new asset is created without an owner.
* create an entry in a maintenance NoteLog to record each change to the Asset.

Where a stewardship action creates an event, this is published to the stewardship action
OMASs OutTopic where it is picked up by the Stewardship server to process.
The stewardship server is configured with the stewardship actions to
process ite incoming events.  For example, there could be a stewardship action to assign
an owner to an Asset without one based on values in a reference table or
if there was no default owner defined for the asset type, to initiate a workflow
to enable a human steward to assign it.


## Implementing Stewardship Actions

The implementation of stewardship actions is through the
[Governance Action Framework (GAF)](../../frameworks/governance-action-framework/README.md).
The GAF defines plug-in components that can be configured and executed by
the stewardship action OMAS and in the stewardship server.

The the stewardship actions are open connectors
(see [Open Connector Framework (OCF)](../../frameworks/open-connector-framework/README.md))
that implement the interfaces defined by the governance action framework.


## Configuring the Stewardship Action OMAS

The stewardship action OMAS is controlled by the 
definition of **StewardshipAction** and **RequestForAction** entities in the metadata
repositories.  StewardshipAction entities relate to conditions found in the metadata
and include details of how to detect the condition and the action to take.
RequestForAction entities refer to issues found in the data landscape itself.
They do not contain information about how to fix the problem.  This is decided
in the stewardship server.

These entities are linked to the assets that need the automated stewardship actions.
They can be created through the stewardship action OMAS API, or by other OMASs.
For example, the Asset Owner OMAS may set up stewardship actions on behalf of
the owner of the asset.  A discovery service may use the Discovery Engine OMAS to
attach RequestForAction entities to an asset as a result of issues it has found in the
data access through the asset.

The stewardship action OMAS is triggered by listening for metadata changes through its OMRS topic listener.
It detects new **RequestForAction** entities being created
and it listens for changes to the assets.

All new RequestForAction entities are actioned by sending an event on the stewardship
action OMAS OutTopic.
Asset changes are assessed using information in any attached StewardshipAction
entities.
If the assessment determines action is required then an event is sent
through the stewardship action OMAS OutTopic.

The stewardship server is listening for these events and running the GAF stewardship actions
specified in the request for action.  These actions may call back to the
open metadata access services.

## Module structure

The module structure for the Stewardship Action OMAS is as follows:

* [stewardship-action-client](stewardship-action-client) supports the client library.
* [stewardship-action-api](stewardship-action-api) supports the common Java classes that are used both by the client and the server.
* [stewardship-action-server](stewardship-action-server) supports in implementation of the access service and its related event management.
* [stewardship-action-spring](stewardship-action-spring) supports the REST API using the [Spring](../../../developer-resources/Spring.md) libraries.

----
Return to the [access-services](..) module.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
