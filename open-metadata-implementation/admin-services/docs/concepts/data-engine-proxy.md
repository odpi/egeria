<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Data Engine Proxy

The **Data Engine Proxy** is an [Integration Daemon](integration-daemon.md)
that can capture metadata about data movement processes (such as ETL jobs)
from a data engine.  This information results in new
[Process](../../../../open-metadata-publication/website/open-metadata-types/0010-Base-Model.md) assets
being defined in open metadata linked to the data sources that it works with.
This is valuable information for lineage.

The data engine proxy is paired with the [Data Engine OMAS](../../../access-services/data-engine).
Its connector interfaces are defined
in the [data-engine-proxy-connector](../../../governance-servers/data-engine-proxy-services/data-engine-proxy-connector) module.

----
* Return to [Integration Daemon](integration-daemon.md).
* Return to [Admin Guide](../user).
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.