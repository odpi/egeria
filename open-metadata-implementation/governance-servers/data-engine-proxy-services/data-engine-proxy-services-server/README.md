<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Data Engine Proxy Services Server

The Data Engine Proxy Services Server implements the data engine proxy logic, making use
of the methods defined through a [Data Engine Proxy Connector](../data-engine-proxy-connector).

In essence, this is to carry out the following:

1. Setup connectivity to a [Data Engine OMAS](../../../access-services/data-engine).
1. Setup connectivity to a data engine, through the configured data engine connection of
    a [Data Engine Proxy Connector](../data-engine-proxy-connector).
1. Ensure a `SoftwareServerCapability` exists to represent this data engine via the Data Engine OMAS.
1. Poll the data engine every defined interval (60 seconds by default) for any changes:
    1. First send any changed `SchemaType`s via the Data Engine OMAS.
    1. Then send any changed `PortImplementation`s via the Data Engine OMAS.
    1. Then send any changed `PortAlias`s via the Data Engine OMAS.
    1. Then send any changed `Process`es via the Data Engine OMAS.
    1. Finally send any changed `LineageMapping`s via the Data Engine OMAS.

The sequence of changes is important to ensure that we build up the information from the
bottom up, so that necessary pre-requisites for various relationships can be created via
the Data Engine OMAS before attempting to create the relationships themselves.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.