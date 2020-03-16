<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Configuring the server startup list for the OMAG Server Platform

A [OMAG Server](../concepts/omag-server.md) is typically started on the [OMAG Server Platform](../concepts/omag-server-platform.md)
using a command.  However it is possible to set up a list of servers that are automatically started
whenever the platform is started.  These servers are also automatically shutdown when the platform
is shutdown.


## Setting up the application properties


OMAG servers can be automatically activated at startup by setting spring-boot property
`startup.server.list`, typically in the `application.properties` file.
The server names are listed without quotes. For example:

```properties
startup.server.list=cocoMDS1, cocoMDS2
```
Each server is started with the administration user id set in the spring-boot property startup.user.

For example:

```properties
startup.user=garygeeke
```

By default, this user id is set to the user id `system`.

When the platform shuts down, if any of the servers that were in the startup list are still running,
they will be shut down before the server completes.

If startup.server.list is null then no servers are automatically started or stopped.

```properties
startup.server.list=
```

This is the default setting.


----
Return to [Configuring the OMAG Server Platform](configuring-the-omag-server-platform.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.