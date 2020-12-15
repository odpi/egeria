<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

[![javadoc](https://javadoc.io/badge2/org.odpi.egeria/data-engine-proxy-connector/javadoc.svg)](https://javadoc.io/doc/org.odpi.egeria/data-engine-proxy-connector)

# Data Engine Proxy Connector

The Data Engine Proxy Connector defines the interfaces that need to be implemented in
order for a data engine to integrate through the [Data Engine Proxy Services](../README.md).

In essence these are the following:

- a data engine connector provider, implemented by extending `DataEngineConnectorProviderBase`.
- a data engine connector, implemented by extending `DataEngineConnectorBase`.

The methods in `DataEngineConnectorBase` should be overridden to implement those set / retrieval
operations according to the specifics of your own data engine. Not all data engines will
necessarily provide all of these methods, so you only need to override those that the data engine
is capable of handling. The un-overridden methods will simply do nothing (set) or return null (read)
by default.

## Polling connector

Currently only polling-based connectors have an interface defined. The key methods used in a connector
for polling are as follows:

- `getChangesLastSynced` to indicate when the changes were last synced by the connector (or null if
    this is the very first time sync is occurring).
- `getOldestChangeSince` to indicate the date and time of the oldest change the connector knows about
    since the provided date and time. This is used to efficiently skip over any periods where no changes
    were made. (If there is no change since the specified date and time, it should return null.)
- `setChangesLastSynced` to persist the date and time when the changes were last synchronized by the
    connector.

At least one `getChangedXYZ` method should also be implemented to actually retrieve that type of
data engine information and provide it to the proxy. Not all connectors will necessarily provide all
of the types, so it may not be necessary to override all of these methods. (Most important from the
perspective of data processing is probably `getChangedProcesses`.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.