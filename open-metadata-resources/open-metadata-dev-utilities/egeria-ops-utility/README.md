<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# The Egeria Operations Utility (egeria-ops-utility)

The Egeria operations utility creates starts and stops OMAG servers on request.  It has two modes
of operation:

* *Interactive* - the utility loops waiting for more commands.  It is designed to run
  IntelliJ (or similar IDE) where it is available for new commands as you work with Egeria.
  
* *Command* - the utility takes parameters to describe the server and it is configured in a
  single request.

This utility works from a set of hard-coded defaults that you can change for your environment.  
There is also plenty of scope to add new options to configure different types of servers and
features.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.