<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Report Utilities (report-utilities)

This module provides the common function used by Egeria's report utilities, such as
[component-id-report](../component-id-report).

The `EgeriaReport` class manages the report's output file and supplies the formatting methods
used to build up its content:

* Printing report titles, subheadings and report lines at a requested indent level, so that
  nested detail is easy to follow.
* Printing common open metadata structures - elements in a table, connections, and the list of
  registered services - in a consistent layout.
* Closing the report off at the end of the run.

Use it as the starting point when you write a new report of your own.

----

* Return to [Egeria Development Utilities](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
