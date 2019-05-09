<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Local Server URL Root

The local server URL root is a default value used when constructing
the local repository configuration.

It is the root of the URL used to call the local server's 
open metadata and governance REST calls.
This is typically the host name and port number of the local server.

Open Metadata and Governance REST APIs have the following format
in their URLs:

```text

   <server-url-root>/servers/{serverName}/<operation-name-and-parameters>

```

The local server URL root is the content of the URL prior to `/servers/`.
If the local server 




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.