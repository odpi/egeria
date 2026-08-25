<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Egeria Development Utilities

These utilities can be used as is.  They were written to support Egeria's developer education but can be useful
when you are working on new connectors for Egeria.
They have hard-coded defaults at the top that you can change for your deployment.
Also, feel free to extend them to match your specific deployment.

* **[guid-generator](guid-generator)** - generates random unique identifiers (GUIDs).

* **[component-id-report](component-id-report)** - list the component ids in use in your Egeria deployment.  
  these component ids are used when registering with the audit log and are included in
  each audit log message from the component.  Using unique component ids helps to pinpoint
  exactly which component produced a specific audit log record.

  Code starts with the components that are shipped with Egeria.  Update to include your
  connector implementation.

* **[messages-and-codes](messages-and-codes)** - generates the [messages and codes](../../messages-and-codes)
  documentation at the root of the repository.  It scans the Egeria source for the message sets that define
  the messages used in Egeria's exceptions and audit log, and writes a markdown page for each one so that the
  messages can be read and searched from GitHub.  The build runs it automatically, so the documentation stays
  in step with the messages.

* **[report-utilities](report-utilities)** - provide common formatting functions used in the reports.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.