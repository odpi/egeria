<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# The Egeria BOM

`bom/build.gradle` is a Gradle [`java-platform`](https://docs.gradle.org/current/userguide/java_platform_plugin.html)
module.  It declares the version of every third-party library the build uses, so that the individual
modules can declare their dependencies without a version:

```groovy
implementation 'org.apache.kafka:kafka-clients'
```

The BOM is also published, so that projects building against Egeria can import it and pick up the same
set of versions.

## Where the versions actually come from

The BOM does **not** decide every version by itself.  It imports the Spring Boot BOM:

```groovy
api(platform("org.springframework.boot:spring-boot-dependencies:${springbootVersion}"))
```

Spring Boot's BOM already fixes several hundred artifacts, including Jackson, SLF4J, Logback, HikariCP,
the JUnit platform and the Jakarta APIs.  **When both the Spring Boot BOM and a local constraint name a
version, Gradle takes the higher of the two.**

That matters when reading this file.  A constraint pinning a version *below* Boot's decides nothing - the
build resolves Boot's version and the constraint sits there stating a number that is never used.  Thirteen
such constraints were removed during the 6.2 work; each one had been quietly overridden, in some cases by
several minor versions.

So a local constraint is worth having when the library is not in Boot's BOM at all, or when Egeria
deliberately needs a *newer* version than Boot ships.

## Changing a version

1. Edit the version variable in the `ext { }` block at the top of `bom/build.gradle`.
2. **Confirm the change reached the resolved graph**, rather than trusting the constraint.  Capturing
   `dependencies` output for the whole build before and after and diffing the two is the reliable check -
   it catches both the constraint that changed nothing and the bump that moved more artifacts than
   expected, which is normal where a library ships its own BOM.
3. Run `./gradlew verifyRelease`, which covers the build, the publication POMs and the platform
   distribution.

## Versions deliberately not taken

A plain "what is the latest version" check recommends several artifacts that must not be taken:

- **Pre-releases.** Alpha, Beta, milestone and preview builds are offered for netty, log4j,
  hibernate-validator, the Jakarta APIs, micrometer and the MSSQL JDBC driver.  Egeria ships releases.
- **springdoc 3.x.** It targets Spring Boot 4.  While this build is on Boot 3.5, springdoc must stay on
  the 2.8.x line.

## Open questions

One `TODO` comment in `bom/build.gradle` is genuine and still open: what the published BOM should
exclude.  Any other `TODO` naming a version *older* than the one in use is a stale note from an abandoned
experiment and can go.

The `tomcat-annotations-api` question that used to sit alongside it is settled.  That artifact carries the
same classes as `jakarta.annotation-api`, and the capability rules applied in the root `build.gradle`
resolve the conflict in favour of `jakarta.annotation-api`, so it is off every classpath here without the
BOM needing to say anything.

## History

This file previously held a snapshot of a Dependabot triage carried out in December 2024 for release 5.2.
Every row in it has since been superseded, and its "do not upgrade" column recorded runtime failures in
XTDB, which the build no longer uses.  The original is in the git history of this file.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.
