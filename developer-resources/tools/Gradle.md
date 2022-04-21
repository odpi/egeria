<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->


# Gradle

Gradle is an alternative build tool to maven & offers
 - better support for parallel builds
 - more flexibility for build tasks
 - breaking the link between directory structure and maven artifacts
 - Extremely fast incremental builds

Our direction is for a Gradle build to replace Maven. However that work is still underway.
See [#3370](https://github.com/odpi/egeria/issues/3370) for current progress. As such our supported build environment remains [Maven](Maven.md.)

As of release 3.0 most components are building with gradle, but artifacts are not being
created, and verification has not been done. Contributions to this work are welcome, as are issue reports!

No gradle installation is required, as we use the 'gradle wrapper' which will automatically install gradle if needed. This reduces the setup steps, and ensure everyone runs the same
version of gradle (currently 7.02 in Release 3.0).

To run a gradle build:
```bash
$ ./gradlew build
```
More details on Gradle itself is on the [Gradle](https://gradle.org/).

Also refer to  [Dependency Management](../Dependency-Management.md). Much of the content is maven focus, but the same principles should apply to Gradle too.

----
* Return to [Developer Tools](.)
* Link to [Egeria's Community Guide](https://egeria-project.org/guides/community/)
* Link to the [Egeria Dojo Education](https://egeria-project.org/education/egeria-dojo/)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
