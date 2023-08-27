<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# The Database Report utility (database-report)

The database report utility queries databases in a metadata server and builds reports on the requested databases.  
It has two modes of operation:

* *Interactive* - the utility loops waiting for more commands.  It is designed to run
  IntelliJ (or similar IDE) where it is available for new commands as you work with Egeria.
  
* *Command* - the utility takes the guid of the database to report on.

This utility works from a set of hard-coded defaults that you can change for your environment.  
There is also plenty of scope to add new options to search for different types of elements.

## Related utilities

You can use the Asset Set Up utility to create some interesting metadata for
the asset look up utility to display.  The Asset Look up utility works with all types of assets, not just databases.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.