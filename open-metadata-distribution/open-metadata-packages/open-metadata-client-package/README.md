<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Open Metadata Client Package

The **open-metadata-client-package** provides Java classes that call the Open Metadata Access Services (OMAS) REST APIs.
These classes can be used from a Java client program or web application.

Each OMAS provides its own Java client classes.
There are typically:
* one Java class for the REST API client and
* one or more message helper Java classes for supporting event formatting and parsing for the In and Out topics.

The REST API client requires the IP address and port number of the server where the OMASs are deployed.

This package is used by data platforms, engines, tools and applications to
integrate with an independently deployed open metadata repository
(or cohort of open metadata repositories).

The server-side package that supports this client
is **[open-metadata-caller-package](../open-metadata-caller-package/README.md)**.
 