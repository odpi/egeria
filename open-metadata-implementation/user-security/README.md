<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# User Security

The user security modules cover mechanisms to enable token-based security to be used as an authentication 
method on [view services](../view-services) and have incoming tokens distributed to runtime modules
and connectors (such as the security connectors) via thread local storage.

* **[token-controller](token-controller)** - provides a base class for the spring resource controller classes of
the view services.  It provides a method to extract the userId from the incoming HTTP request's
authorization header to add as the userId on the URL of other Egeria REST services it calls downstream.

* **[token-manager](token-manager)** - provides the classes to extract the authorization headers from an
incoming HTTP request and add them to thread local storage.  The headers saved are in the `authn.header.name.list`
property in `application.properties`.

* **[user-authn](user-authn)** - provides the runtime services for the token management REST API calls.

----
Return to [open-metadata-implementation](..).


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.