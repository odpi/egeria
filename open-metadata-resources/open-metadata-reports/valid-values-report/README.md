<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Valid Values Report (valid-values-report)

The valid values report provides a list of valid values defined in the open metadata ecosystem, organized by their sets.

The report is passed a platform URL root, client userId and server name.  It calls the Digital Architecture OMAS running on the supplied platform/server with the calling userId.

The valid values returned by the Digital Architecture OMAS are display on the console, and written to a Markdown file called valid-values-report.md.

The report is in two parts:

* The first part lists the valid values that are used to populate and validate properties from open metadata elements.  These valid values are called [**Valid Metadata Values**](https://egeria-project.org/guides/planning/valid-values/overview/).
* The second part lists the valid values for data.  They are used to populate and validate data fields in [digital resources](https://egeria-project.org/concepts/resource/).




----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.