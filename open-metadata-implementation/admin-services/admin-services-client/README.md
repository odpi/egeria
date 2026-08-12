<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Administration Services Client

The administration services client module contains the client
implementations of the administration services.

`OMAGServerConfigurationClient` provides the configuration calls common to all types of
[OMAG Server](https://egeria-project.org/concepts/omag-server), and `OMAGServerPlatformConfigurationClient`
provides the calls for configuring the [OMAG Server Platform](https://egeria-project.org/concepts/omag-server-platform)
itself.  The remaining clients are organized by server type, extending the common client with the
configuration calls specific to that type of server:

 * `CohortMemberConfigurationClient` - extended by `ConformanceTestServerConfigurationClient`,
   `RepositoryProxyConfigurationClient` and `MetadataAccessServerConfigurationClient` (which is itself extended
   by `MetadataAccessPointConfigurationClient` and `MetadataAccessStoreConfigurationClient`).
 * `GovernanceServerConfigurationClient` - extended by `EngineHostConfigurationClient` and
   `IntegrationDaemonConfigurationClient`.
 * `ViewServerConfigurationClient`

`ConfigurationManagementClient` issues calls to an OMAG Server Platform that manages configuration for OMAG
Servers, including storing, retrieving and deploying configuration between platforms.


Return to [Admin Services](..).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.