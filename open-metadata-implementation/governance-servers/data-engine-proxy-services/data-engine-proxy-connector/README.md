<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

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

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.