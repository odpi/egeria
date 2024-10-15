<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata Collection Store Connectors Documentation

The open metadata collection store connectors are used to
integrate an existing metadata repository into the open
metadata ecosystem.  There are two types of connectors.

* The [**repository connector**](../../../../../repository-services/docs/component-descriptions/connectors/repository-connector.md) provides
a standard repository interface that wraps the proprietary
interface of the metadata repository.
* The [**event mapper connector**](../../../../../repository-services/docs/component-descriptions/connectors/event-mapper-connector.md) captures
events that provide notifications that metadata has changed
in the metadata repository and passes them to the
[Open Metadata Repository Services (OMRS)](../../../../../repository-services/docs/README.md).

The event mapper connector often calls the repository connector
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

### Setup a project

Egeria has been designed to allow connectors to be developed in independent projects from the core itself.
As an example, the [IBM InfoSphere Information Governance Catalog Repository Connector](https://github.com/odpi/egeria-connector-ibm-information-server)
is available in its own separate GitHub repository, and could be a useful reference point when following the
remainder of these instructions.

Start by defining a new Maven project in your IDE of choice, and at the root-level POM be sure to include the
following:

```
<properties>
    <open-metadata.version>5.2-SNAPSHOT</open-metadata.version>
</properties>
<dependencies>
    <dependency>
        <groupId>org.odpi.egeria</groupId>
        <artifactId>open-metadata-adapter-package</artifactId>
        <version>${open-metadata.version}</version>
    </dependency>
</dependencies>
```

Naturally change the version to whichever version of Egeria you'd like to build against; the `open-metadata-adapter-package`
dependency ensures you'll have the necessary portion of Egeria to build your connector against (ie. the base classes that
you'll need to extend in the steps below).

### Implement the repository connector

As implied by the overview above, a logical place to start in implementing a new
Open Metadata Collection Store Connector is by implementing the repository connector.

You can start to build this within your new project by creating a new Maven module called something like `adapter`.
Within this `adapter` module, in a package like `...repositoryconnector`, implement the following:

- an `OMRSRepositoryConnectorProvider` specific to your connector, which extends `OMRSRepositoryConnectorProviderBase`.
    The connector provider is a factory for its corresponding connector. Much of the logic needed is coded in the base
    class. Therefore your implementation really only involves defining the connector class and setting this in the
    constructor.
- an `OMRSRepositoryConnector` specific to your connector, which extends `OMRSRepositoryConnector`. Most likely the
    main logic of this class will be implemented by overriding the `initialize()` method of the base class,
    and putting into this any logic for initializing the connection: for example, connecting to an underlying
    database, or starting a REST API session, etc. (Similarly, override the `disconnect()` method to properly
    cleanup / close such resources.)
- an `OMRSMetadataCollection` specific to your repository, which extends `OMRSMetadataCollectionBase`. Ideally your
    implementation should override each of the methods defined in the base class, but a logical place to start is:

    1. implement the `addTypeDef()` method. For each TypeDef this method should either extend your metadata repository to include this TypeDef,
        configure the mapping from your repository's types to the open metadata types, or throw a `TypeDefNotSupportedException`. (For those that
        are implemented it may make sense to store these in a class-local member for comparison in the next step.)
	1. implement the `verifyTypeDef()` method, which can check the types you have implemented (above) conform to the open metadata TypeDef received
	    (ie. that all properties are available, of the same data type, etc), and that if none have yet been listed as implemented that `false` is
	    returned (this will cause `addTypeDef()` above to automatically be called),
	1. then implement be the `getEntityDetail()` method that retrieves an entity by its GUID.

Once these minimal starting points are implemented, you should be able to configure the
[OMAG Server Platform](../../../../../platform-chassis/platform-chassis-spring)
as a proxy to your repository connector by following the instructions in
[using the admin services](https://egeria-project.org/guides/admin).
**Important**: this will *not* necessarily be the
end-state pattern
you intend to use for your repository connector, but can provide a quick way to start testing its functionality.

This very basic initial scaffold of an implementation allows a [connection](../../../connector-configuration-factory)
to be instantiated to your repository, and translation between your repository's representation of metadata and the open
metadata standard types.

The configuration and startup sequence is important, to start with the following should be sufficient
(ie. you should not need to configure the access services or event bus initially to just test your
`OMRSMetadataCollection` logic):

1. Enable access to a cohort,
    by picking a name for your cohort and POSTing according to the instructions linked.
1. Enable OMAG Server as a repository proxy,
    specifying your canonical `OMRSRepositoryConnectorProvider` class name for the `connectorProvider={javaClassName}` parameter.
1. Start the server instance,
    by POSTing according to the instructions.

You should then be in a position to invoke the REST API endpoints of the OMAG server, which will
in turn invoke the various methods of your implemented `OMRSMetadataCollection`.

(And of course from there you can continue to Override methods of the `OMRSMetadataCollectionBase` class to implement the
other metadata functionality for searching, updating and deleting as well as retrieving other instances of metadata like relationships.)

### Add an event mapper

Once you have the basic `OMRSMetadataCollection` working, you can then implement an event mapper.`

Within the same `adapter` Maven module, perhaps under a new sub-package like `...eventmapper`, implement the following:

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

1. [Configure the cohort event bus](https://egeria-project.org/guides/admin/servers/configuring-event-bus/).
    This can be done first, before any of the other configuration steps above
1. [Configure the event mapper](https://egeria-project.org/guides/admin/servers/configuring-a-repository-proxy/#configure-the-event-mapper-connector).
    This can be done nearly last: after all the other configuration steps above, but before the start of the
    server instance.

### Test your connector

As you progress with the implementation of your connector, it is a good idea to test it against the
[Open Metadata Conformance Suite](https://egeria-project.org/guides/cts/overview/), in particular to
gain guidance on what features you may still need to implement to conform to the open metadata standards.

### Package your connector

Once you have sufficiently implemented and tested your connector, and if you intend to simply make it available
through the proxy ability of the OMAG Server Platform we've used above, you can package it into a distributable
`.jar` file using another Maven module, something like `distribution`.

In this module's POM file include your `adapter` module (by `artifactId`) as a dependency, and consider
building an assembly that uses the following in order to compile a minimal `.jar` file for just your connector
and its non-Egeria dependencies.

```xml
<useTransitiveDependencies>false</useTransitiveDependencies>
```

Again, since we would just be using this connector alongside the existing OMAG Server Platform, this avoids
ending up with a `.jar` file that includes the entirety of the Egeria OMAG Server Platform (and its dependencies)
in your connector `.jar` -- and instead allows your minimal `.jar` to be loaded at startup of the core OMAG Server
Platform and configured through the REST calls covered above.

(Of course, if you intend to embed or otherwise implement your own server, the packaging mechanism will likely
by different.)

For a more detailed example of this minimal packaging to make use of the proxy, refer to the
[IBM InfoSphere Information Governance Catalog Repository Connector's distribution module](https://github.com/odpi/egeria-connector-ibm-information-server/tree/main/distribution).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
