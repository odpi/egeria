<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Runtime reports

These utilities produce reports for different aspects of the runtime.

* **component-id-report** - list the component ids in use in your Egeria deployment.  
  these component ids are used when registering with the audit log and are included in
  each audit log message from the component.  Using unique component ids helps to pinpoint
  exactly which component produced a specific audit log record.

  Code starts with the components that are shipped with Egeria.  Update to include your
  connector implementation.

* **egeria-platform-report** - issue commands to a running platform an reports on the status of
  the platform itself and the servers running on it.  There are different options that control the
  detail displayed.  Extend it with new options and layouts.

* **database-report** - a utility to report on the databases catalogued in open metadata.



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.