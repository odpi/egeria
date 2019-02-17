<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Activating and deactivating the OMAG server

Once a [configuration document](../concepts/configuration-document.md) has been completed
for an [OMAG Server](../concepts/logical-omag-server.md), it can be started using the following
REST call:

```

POST http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/instance

```

and stopped, as follows:

```

DELETE http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/instance

```

The configuration document is not changed by these calls.
It is possible to query the running server's configuration using the following REST API:

```

GET http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1/instance/configuration

```

If you want to delete the server's configuration document then issue:

```

DELETE http://localhost:8080/open-metadata/admin-services/users/garygeeke/servers/cocoMDS1

```

If the OMAG server is running, this command also unregisters the named server from the cohorts it
is connected to.
Only use this command if the server is being permanently removed.



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.