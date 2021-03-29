<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Release 2.7 (March 2021)

Release 2.7 adds:
* Performance improvements in the graph repo
* Changes to metadata types
* Changes to the distribution process

Details of these and other changes are in the sections that follow.

## Description of Changes

### Build & Release changes

* Previously release maven artifacts have been pushed to JCenter (in addition to Maven Central), and snapshots have been pushed to odpi.jfrog.io/odpi/egeria-snapshot. As of 2.7 this no longer occurs and release and snapshots are pushed only to Maven Central.
* Due to the above, maven artifacts may be signed by a different user to previously (and this may change in a future release). 

### Performance Improvements

* Release 2.7 includes performance improvements to the graph repository's search methods, which select an efficient query strategy based on the properties and type filters supplied to the search.

### Metadata Types

* We have removed overloaded properties that existed at multiple levels in a type definition: for example, length was defined both on SchemaAttribute and again on RelationalColumn (which is a subtype of SchemaAttribute). This change removes them from being defined again the lower level (RelationalColumn in this example); however, the property itself will still be available at the lower level due to inheriting it from the supertype.

### Bug fixes and other updates
* Additional Bug Fixes
* Dependency Updates

For details on both see the commit history in GitHub.

## Known Issues

* It is recommended to use a chromium-based browser such as Google Chrome or Microsoft Edge, or Apple Safari for the Egeria React UI. Some parts of the UI experience such as Dino currently experience problems with Firefox. See [odpi/egeria-react-ui#96](https://github.com/odpi/egeria-react-ui/issues/96) .
* When running the 'Understanding Platform Services' lab, ensure you run the 'egeria-service-config' notebook first and do not restart the python kernel before running this lab. See [#4842](https://github.com/odpi/egeria/issues/4842) .

# Egeria Implementation Status at Release 2.7

![Egeria Implementation Status](../open-metadata-publication/website/roadmap/functional-organization-showing-implementation-status-for-2.7.png#pagewidth)

Link to Egeria's [Roadmap](../open-metadata-publication/website/roadmap) for more details about the
Open Metadata and Governance vision, strategy and content.


# Further Help and Support

As part of the Linux AI & Data Foundation, our slack channels have moved to the [LF AI & Data Slack workspace](slack.lfaidata.foundation), and our mailing lists can now be found at https://lists.lfaidata.foundation/groups

Continue to use these resources, along with GitHub to report bugs or ask questions.

----
* Return to [Release Notes](.)
   
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
