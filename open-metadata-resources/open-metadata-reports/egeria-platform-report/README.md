<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Egeria Platform Report (egeria-platform-report)

The Egeria platform report is passed a platform URL root (and optional server name) that
it uses to query the operational status of the platform.  For example:

* Which *version* of Egeria is running
* Which *Platform Metadata Security Connector* is configured.
* Which *Configuration Document Store Connector* is configured.
* Which *Registered Services* are installed.
* Which *OMAG Servers* are visible to the platform.
   * For each configured server
      * What type of server is it and its description
      * Which services are configured
      * Which connectors are configured and whether they are installed and usable on the platform
      * Which cohorts are configured for the server to join (if applicable)
   * For each known server (ie has run at least once since the platform started):
      * The time when it has been running on the platform
   * If the server is still running the following details are added
      * The running status of its services
      * The report from the audit log describing the statistics from each component
      * the registration status of the server with its cohorts (if applicable)
      * the status of its engines/connectors (if applicable)



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.