<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Release 3.0 (expected August 2021)

Release 3.0 adds:
* Requirement to build & run with Java 11 (LTS) or above

Details of these and other changes are in the sections that follow.

## Description of Changes

### Java 11 

As of Release 3.0 of Egeria, Java 11 is **required** to build and run Egeria.

Egeria will not build/run/be supported on Java 8. Developers are now able to use Java 11 only functionality.

Java releases beyond Java 11 up to the current release have some informal testing, and we do build verification on the current release (currently 16). 

See [Java](../developer-resources/languages/Java.md) for further information.

### Cohort members

* The option added in release 2.11 to allow multiple topics per cohort now becomes the default in release 3.0 . See [open metadata repository cohort](../open-metadata-implementation/admin-services/docs/concepts/cohort-member.md

### Egeria UI
* Passwords for the samples Coco Pharmaceuticals users changed to 'secret';

### Known Issues

* It is recommended to use a chromium-based browser such as Google Chrome or Microsoft Edge, or alternatively Apple Safari for the Egeria React UI. Some parts of the UI experience such as Dino currently experience problems with Firefox. See [odpi/egeria-react-ui#96](https://github.com/odpi/egeria-react-ui/issues/96) .

### Other changes
* Dependencies have been updated
* Bug Fixes

# Further Help and Support

See the [Community Guide](../Community-Guide.md).

----
* Return to [Release Notes](.)
   
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
