<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Server Platform Origin Service

The server platform origin service is a simple REST API that returns basic information about an
[Open Metadata and governance (OMAG) server Platform](omag-server-platform.md).

It is often used by operational scripts controlling the start up and shutdown of
OMAG Server Platforms to determine if the server platform is running.

There is a single operation on this service called `server-platform-origin`.

The format of the operation is:

```text
{serverPlatformURLroot}/open-metadata/platform-services/users/{userId}/server-platform-origin
```

where `{serverPlatformURLroot}` is typically the host name and port number for the server platform's
network endpoint and `{userId}` is the userId of an authorized administrator.

The response is a single string describing the implementation and version of the server platform.

For example, this is the response from the ODPi Egeria implementation of the OMAG Server Platform
which has an endpoint of `https://localhost:9443`.  Gary Geeke (`garygeeke`) is the administrator.

```bash
$ curl --insecure -X GET https://localhost:9443/open-metadata/platform-services/users/garygeeke/server-platform-origin
Egeria OMAG Server Platform (version 2.10-SNAPSHOT)
```



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
