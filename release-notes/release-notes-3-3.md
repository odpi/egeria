<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Release 3.3 (November 2021)

Release 3.3 adds:
  * Open Metadata Type changes
  * New hand-on lab introducing **Lineage management**


Details of these and other changes are in the sections that follow.

## Description of Changes

**Lineage management** is new chapter in our hand-on labs describing different scenarios to capture, preserve and use lineage information. 
[Open Lineage Lab](https://github.com/odpi/egeria/blob/master/open-metadata-resources/open-metadata-labs/asset-management-labs/capturing-lineage.ipynb) brings the first exercise focusing on design time lineage using Open Lineage Services.  


### Open Metadata Types

The following changes have been made to the open metadata types:

* New type **Incomplete** for classifying referencable entities used in special lineage cases. One example for such case is representing 'virtual asset' form IBM IGC external repository.  
  See description in model [0790](https://odpi.github.io/egeria-docs/types/7/0790-Incomplete/).

## Beta documentation site

We are in the process of moving our documentation across to a new site at https://odpi.github.io/egeria-docs/ . This is still work in progress so some material is currently missing, and some links will fail.

This site should offer improved usability including navigation & search.

Please continue to refer to the main documentation site for any missing content until this migration is complete.

## Deprecations



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
