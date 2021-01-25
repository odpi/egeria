<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->


# OMAG Server's Platform URL Root

An OMAG Server's **Platform URL Root** is the network address of the [OMAG Server
Platform](omag-server-platform.md) where the OMAG server is going to run.  
This is often the host name of the computer or container whether the platform runs plus the
port number allocated to the OMAG Server Platform.

Its value is needed when creating clients or configuring services that will call the OMAG Server
because it provides the root of the URL used to call the server's 
open metadata and governance REST calls, which have the following format
in their URLs:

```text
{platformURLRoot}/servers/{serverName}/<operation-name-and-parameters>
```

The platform URL root is the content of the URL prior to `/servers/`.
The default value an OMAG Server Platform is **"https://localhost:9443"**.

The `{serverName}` is explained [here](server-name.md).

----
* Go to [Developer Guide](../../../../open-metadata-publication/website/developer-guide).
* Go to [Admin Guide](../user).


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.