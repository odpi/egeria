<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Release 3.2 (October 2021)

Release 3.2 adds:
* Open Metadata Type changes
* Dependency Updates
* Bug Fixes


Details of these and other changes are in the sections that follow.

## Description of Changes

### Open Metadata Types

The following changes have been made to the open metadata types:

* New types for setting up the list of status for governance definitions.
  See new type descriptions in model [0421](../open-metadata-publication/website/open-metadata-types/0421-Governance-Classification-Levels.md).

* There is a new type called **SoftwareArchive** to describe a software archive such as a JAR file for java.
  See description in model [0030](../open-metadata-publication/website/open-metadata-types/0030-Hosts-and-Platforms.md).

## Beta documentation site

We are in the process of moving our documentation across to a new site at https://odpi.github.io/egeria-docs/ . This is still work in progress so some material is currently missing, and some links will fail.

This site should offer improved usability including navigation & search.

Please continue to refer to the main documentation site for any missing content until this migration is complete.

## Deprecations

* The docker-compose environment was deprecated in release 3.1 . It will be removed in 3.3 . See https://github.com/odpi/egeria/issues/5721 .

## Known Issues

* It is recommended to use a chromium-based browser such as Google Chrome or Microsoft Edge, or alternatively Apple Safari for the Egeria React UI. Some parts of the UI experience such as Dino currently experience problems with Firefox. See https://github.com/odpi/egeria-react-ui/issues/96 .
* Config documents cannot be displayed in the Egeria React UI (Dino). See https://github.com/odpi/egeria-react-ui/issues/264 .
* If deploying helm charts to OpenShift, a security policy change is needed. See https://github.com/odpi/egeria-charts/issues/18
* When using the 'understanding platform services' lab notebook, the query for active servers will fail. See https://github.com/odpi/egeria/issues/5023 .

# Further Help and Support

See the [Community Guide](../Community-Guide.md).

----
* Return to [Release Notes](.)
   
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
