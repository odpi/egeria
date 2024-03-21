<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Runtime reports

These modules create utilities that produce reports for different aspects of the runtime.

* **[component-id-report](component-id-report)** - list the component ids in use in your Egeria deployment.  
  these component ids are used when registering with the audit log and are included in
  each audit log message from the component.  Using unique component ids helps to pinpoint
  exactly which component produced a specific audit log record.

  Code starts with the components that are shipped with Egeria.  Update to include your
  connector implementation.

* **[egeria-platform-report](egeria-platform-report)** - issue commands to a running platform an reports on the status of
  the platform itself and the servers running on it.  There are different options that control the
  detail displayed.  Extend it with new options and layouts.

* **[database-report](database-report)** - a utility to report on the databases catalogued in open metadata.

* **[survey-report](survey-report)** - a utility to extract details of the 
[Survey Report](https://egeria-project.org/concepts/survey-report/) attached to an asset.

* **[valid-values-report](valid-values-report)** - the valid values report retrieves the reference data
contained within a valid value set.

* **[report-utilities](report-utilities)** - provide common formatting functions used in the reports.




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.