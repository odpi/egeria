<!-- SPDX-License-Identifier: Apache-2.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Connector Broker

The connector broker is the factory class for all open connectors.
Given a valid `Connection` (or `ConnectionProperties`) object, the
connector broker is able to create a new instance of a connector.
This means the caller does not need to know the implementation
details of the connector - just its interface.