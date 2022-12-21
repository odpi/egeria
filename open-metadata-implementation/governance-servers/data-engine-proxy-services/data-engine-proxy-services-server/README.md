<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

[![javadoc](https://javadoc.io/badge2/org.odpi.egeria/data-engine-proxy-services-server/javadoc.svg)](https://javadoc.io/doc/org.odpi.egeria/data-engine-proxy-services-server)

# Data Engine Proxy Services Server

The Data Engine Proxy Services Server implements the data engine proxy logic, making use
of the methods defined through a [Data Engine Proxy Connector](../data-engine-proxy-connector).

In essence, this is to carry out the following:

1. Setup connectivity to a [Data Engine OMAS](../../../access-services/data-engine). (See below section [Configuring interface type for Data Engine OMAS](#configuring-interface-type-for-data-engine-omas))
1. Setup connectivity to a data engine, through the configured data engine connection of
    a [Data Engine Proxy Connector](../data-engine-proxy-connector).
1. Ensure an `Engine` exists to represent this data engine via the Data Engine OMAS.
1. Poll the data engine every defined interval (60 seconds by default) for any changes:
    1. First send any changed `SchemaType`s via the Data Engine OMAS.
    1. Then send any changed `PortImplementation`s via the Data Engine OMAS.
    1. Then send any changed `PortAlias`s via the Data Engine OMAS.
    1. Then send any changed `Process`es via the Data Engine OMAS.
    1. Finally send any changed `DataFlow`s via the Data Engine OMAS.

The sequence of changes is important to ensure that we build up the information from the
bottom up, so that necessary pre-requisites for various relationships can be created via
the Data Engine OMAS before attempting to create the relationships themselves.

## Proxy polling configuration

The polling done by the proxy can be optimized through the following settings:

- `pollIntervalInSeconds` defines how many seconds to sleep between polling intervals (and defaults
    to one minute (`60` seconds))
- `batchWindowInSeconds` defines the maximum span through which changes will be searched. This defaults
    to one day (`86400` seconds).

Based on these settings, the polling operates as follows:

1. Retrieves the last synchronization date from the connector (using its `getChangesLastSynced` method).
1. Retrieves the oldest change since (1) using the connector's `getOldestChangeSince` method.
    1. If there are changes, calculates a change window as the date and time from (2) + the `batchWindowInSeconds`.
    1. If there are not changes, retains the date and time retrieved from (1).
1. Calculates the current date and time.
    1. If there are changes, determines the lesser of this and (2i).
    1. If there are not changes, retains the current date and time.
1. Retrieves changes (in the order specified above) where (2) < change <= (4).

These settings and logic ensure that:

- When there are a large quantity of changes (eg. during an initial synchronization), at
    most only changes within the `batchWindowInSeconds` are ever picked up and processed at
    one time by the connector. This ensures its resource usage can be limited, and it can
    continually checkpoint its synchronization in smaller blocks.
- When there are no changes, the connector can "jump" forward immediately to the latest date
    and time as its last synchronization point. This ensures the polling does not go sequentially
    block-by-block and take longer than necessary when there are no changes to be processed over
    various time-spans (eg. weekends, during which the servers may have been offline for maintenance).

As a result, you may want to tweak these settings further:

- Reducing `batchWindowInSeconds` if you expect there are huge volumes of changes that occur
    even within a day, you might want to reduce this to hourly or perhaps even less.

## Configuring interface type for Data Engine OMAS

By default the Data Engine Proxy services server will use HTTP REST java client to communicate the changes with Data Engine OMAS. Following settings are required to configure the client:

- `accessServiceRootURL` URL location of the platform hosting Data Engine OMAS
- `accessServiceServerName` name of the server hosting Data Engine OMAS

Since Data Engine OMAS also supports events based interface, if you prefer this transport mechanism you can choose to use events client implementation by setting 'eventsClientEnabled' to 'true'. 

- `eventsClientEnabled` defines if events client should be used to communicate to Data Engine OMAS. (default is 'false')

> Note: if you set 'eventsClientEnabled' to 'true' behind the scenes HTTP REST client is still initially used to fetch the connectivity details and topic address from the target Data Engine OMAS server configuration. 



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.