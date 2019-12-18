<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Configuring the Open Metadata View Services (OMVSs)
# Configuring the Open Metadata View Services (OMVSs)

The [Open Metadata View Services (OMVSs)](../../../view-services) provide the domain-specific
APIs for a User Interface serving metadata management and governance.
They run in a [UI Server](../concepts/ui-server.md) and typically offer a REST API interaction.

## Prerequisite configuration

The view service configuration depends on the definitions of the [local server's userId](configuring-ui-server-basic-properties.md).
  
## Enable the view services

To enable all of the view services with default configuration values use the following command.

```
POST https://localhost:8443/open-metadata/ui-admin-services/users/garygeeke/servers/cocoUIS1/view-services
```

## Disable the view services


The view services can be disabled with the following command.
This also disables the enterprise repository services since they
are not being used.

```
DELETE https://localhost:8443/open-metadata/ui-admin-services/users/garygeeke/servers/cocoUIS1/view-services
```


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.