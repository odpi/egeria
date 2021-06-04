<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Local Server URL Root

The local server URL root is a default value for the URL root of the OMAG Server
Platform where the OMAG server is going to run.  It is used as a default destination
when deploying the server configuration document to a different OMAG Server Platform
from the one use to maintain the configuration document.

It is also used when constructing the 
the local repository configuration in a metadata server.

It is the root of the URL used to call the local server's 
open metadata and governance REST calls.
This is typically the host name and port number of the local server.

Open Metadata and Governance REST APIs have the following format
in their URLs:

```text
{platformURLRoot}/servers/{serverName}/<operation-name-and-parameters>
```

The local server URL root is the content of the URL prior to `/servers/`.
The default value is **"https://localhost:9443"**.

----
* Return to [configuring an OMAG server](configuring-an-omag-server.md)
* Return to [configuration document structure](../concepts/configuration-document.md)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.