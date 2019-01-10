<!-- SPDX-License-Identifier: Apache-2.0 -->

# Open Metadata Collection Store Connectors Documentation

The open metadata collection store connectors are used to
integrate an existing metadata repository into the the open
metadata ecosystem.  There are two types of connectors.

* The [**repository connector**](../../../../../repository-services/docs/component-descriptions/connectors/repository-connector.md) provides
a standard repository interface that wraps the proprietary
interface of the metadata repository.
* The [**event mapper connector**](../../../../../repository-services/docs/component-descriptions/connectors/event-mapper-connector.md) captures
events that provide notifications that metadata has changed
in the metadata repository and passes them to the
[Open Metadata Repository Services (OMRS)](../../../../../repository-services/docs/README.md).

The event mapper connector often calls repository connector
to expand out the information needed by the OMRS.
The links below provide more information.

* **[Overview of the Repository Connector Interface](overview-of-the-repository-connector-interface.md)** -
a walk-through of the methods on the repository connector interface.  This connector enables
the Open Metadata Repository Services (OMRS) the ability to search, query, create, update and delete
metadata in an existing metadata repository.

* **[Overview of the Event Mapper Connector Interface](overview-of-the-event-mapper-connector-interface.md)** -
a walk-through of the methods on the event mapper connector interface.  This connector enables
events from an existing metadata repository that indicate that metadata is changing, to be passed
to the OMRS so it can be distributed to the other metadata repositories who are members of the same cohort.

## Getting Started Implementing a New Connector

As implied by the overview above, a logical place to start in implementing a new
Open Metadata Collection Store Connector is by implementing the repository connector.

This requires implementing:

- an `OMRSRepositoryConnectorProvider` specific to your connector, which extends `OMRSRepositoryConnectorProviderBase`.
    The connector provider is a factory for its corresponding connector. Much of the logic needed is coded in the base
    class. Therefore your implementation really only involves defining the connector class and setting this in the
    constructor.
- an `OMRSRepositoryConnector` specific to your connector, which extends `OMRSRepositoryConnector`. Most likely the
    main logic of this class will be implemented by overriding the `initialize()` method of the base class,
    and putting into this any logic for initializing the connection: for example, connecting to an underlying
    database, or starting a REST API session, etc. (Similarly, override the `disconnect()` method to properly
    cleanup / close such resources.)
- an OMRSMetadataCollection specific to your repository, which extends `OMRSMetadataCollectionBase`. Ideally your
    implementation should override each of the methods defined in the base class, but a logical place to start is:

	1. implement the `verifyTypeDef()` method, which indicates which types are supported by your repository,
	1. as part of that implementation, you likely want to configure the mapping from your repository's types to the open metadata types,
	1. then implement be the `getEntityDetail()` method that retrieves an entity by its GUID.

Once these minimal starting points are implemented, you should be able to configure the
[OMAG server chassis](../../../../../governance-servers/server-chassis/server-chassis-spring/README.md)
as a proxy to your repository connector by following the instructions in
[using the admin services](../../../../../governance-servers/admin-services/Using-the-Admin-Services.md).
**Important**: this will *not* necessarily be the
[end-state pattern](../../../../../../open-metadata-publication/website/open-metadata-integration-patterns/README.md)
you intend to use for your repository connector, but can provide a quick way to start testing its functionality.

This very basic initial scaffold of an implementation allows a [connection](../../../connector-configuration-factory/README.md)
to be instantiated to your repository, and translation between your repository's representation of metadata and the open
metadata standard types.

The configuration and startup sequence is important, to start with the following should be sufficient
(ie. you should not need to configure the access services or event bus initially to just test your
`OMRSMetadataCollection` logic):

1. [Enable access to a cohort](../../../../../governance-servers/admin-services/Using-the-Admin-Services.md#enable-access-to-a-cohort),
    by picking a name for your cohort and POSTing according to the instructions linked.
1. [Enable OMAG Server as a repository proxy](../../../../../governance-servers/admin-services/Using-the-Admin-Services.md#enable-omag-server-as-a-repository-proxy),
    specifying your canonical `OMRSRepositoryConnectorProvider` class name for the `connectorProvider={javaClassName}` parameter.
1. [Start the server instance](../../../../../governance-servers/admin-services/Using-the-Admin-Services.md#activate-open-metadata-services),
    by POSTing according to the instructions.

You should then be in a position to invoke the REST API endpoints of the OMAG server, which will
in turn invoke the various methods of your implemented `OMRSMetadataCollection`.

## Adding an Event Mapper

Once you have the basic `OMRSMetadataCollection` working, you can then implement an event mapper.

This requires implementing:

- an `OMRSRepositoryEventMapperProvider` specific to your connector, which extends `OMRSRepositoryConnectorProviderBase`.
    This really only involves defining the your event mapper class and setting this in the constructor.
- an `OMRSRepositoryEventMapper` specific to your connector, which extends `OMRSRepositoryEventMapperBase`. Start
    by overriding the `initialize()` method of the base class, to define how you will initialize your event mapper.
    For example, this could be connecting to an existing event bus for your repository, or some other mechanism
    through which events should be sourced. The `start()` method should also be overridden to define how to startup
    the processing of such events.

The bulk of the logic in the event mapper should then define how events that are received from your repository
are processed (translated) into OMRS events: those dealing with Entities, Classifications and Relationships.

Typically you would want to construct such instances by calling into your `OMRSMetadataCollection`, ensuring you
produce the same payloads of information for these instances both through API connectivity and the events.

Once you have the appropriate OMRS object, you can make use of the methods provided by the `repositoryEventProcessor`,
configured by the base class, to publish these to the cohort. For example:

- `repositoryEventProcessor.processNewEntityEvent(...)` to publish a new entity instance (`EntityDetail`)
- `repositoryEventProcessor.processUpdatedRelationshipEvent(...)` to publish an updated relationship instance (`Relationship`)
- and so on

To add the event mapper configuration to the configuration you started with above, add:

1. [Configure the cohort event bus](../../../../../governance-servers/admin-services/Using-the-Admin-Services.md#setting-up-the-event-bus).
    This can be done first, before any of the other configuration steps above
1. [Configure the event mapper](../../../../../governance-servers/admin-services/Using-the-Admin-Services.md#add-the-local-repositorys-event-mapper).
    This can be done nearly last: after all of the other configuration steps above, but before the start of the
    server instance.
