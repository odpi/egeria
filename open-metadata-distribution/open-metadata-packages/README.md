<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata Packages

Egeria is packaged in a flexible structure to allow
different metadata tools, engines and repositories to be
integrated together in a range of deployment patterns -
from simple projects to large multi-country,
cross enterprise metadata support.

The packages are summarized below.

* **[Open Metadata Client Package](open-metadata-client-package)** - provides Java classes that call
the Open Metadata Access Services (OMAS) REST APIs and helper classes for
generating and parsing events for the OMASs in and out topic.
These classes can be used from a Java client program or web application.

* **[Open Metadata Caller Package](open-metadata-caller-package)** - provides the
server-side support for the OMAS REST APIs.  It can be combined with either
the native or adapter packages below.

* **[Open Metadata Native Package](open-metadata-native-package)** - provides a
library of components to build a metadata repository
and tools package that natively supports the Open Metadata and Governance APIs.  

* **[Open Metadata Adapter Package](open-metadata-adapter-package)** - provides the API and libraries for
building a repository proxy to act as an open metadata adapter for an existing
metadata repository that has proprietary interfaces.




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

 