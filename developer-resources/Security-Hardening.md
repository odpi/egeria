<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Guidelines for Security Hardening

As part of developing Egeria, we will inevitably come across areas identified by various code analysis tools as
potential security vulnerabilities.  The following guidelines define the way we will work with these (identifying,
reporting, tracking, etc) as well as some common techniques we can apply to address them.

## Way of working

- Weekly call to triage identified vulnerabilities from various sources
    - [Sonar scans](https://sonarcloud.io/organizations/odpi/projects)
    - [Nexus IQ scans](https://nexus-iq.wl.linuxfoundation.org)
    - Any third party inputs (ie. from consumers) -- which can be sent to us at [egeria-security](mailto:egeria-security@lists.lfaidata.foundation)
- Work can then begin on resolving them, with two potential options (depending on complexity):
    - Quick to resolve: create an issue when we believe we have a fix, and link the PR with the fix to the issue
    - For any we cannot quickly resolve, we will use [GitHub's security advisories](https://github.com/odpi/egeria/security/advisories)
        to capture the details and notify publicly about the potential vulnerability

## Techniques to address vulnerabilities

### Code changes

When the code identified as having a potential vulnerability is our own, we should naturally investigate how to change
our code in order to reduce or remove the impact or likelihood of that exposure.  This could be through applying input
or output validation of data we receive, or ensuring that we use features built-in to any external components we use to
do such processing.

### Dependency exclusions

External modules on which we depend often have their own set of embedded dependencies.  Some of these transitive
dependencies may have vulnerabilities, and we may not actually make use of any of the functionality they provide.

In these cases, we can (and should) safely exclude these transitive dependencies as part of the POM dependency
management.

For example, `testng` has a dependency on the `snakeyaml` library, but this is only used when configuring `testng` with
YAML documents (which we do not do).  We can therefore safely exclude the transitive `snakeyaml` dependency of `testng`
using the following in the root-level POM:

```xml
<dependency>
    <groupId>org.testng</groupId>
    <artifactId>testng</artifactId>
    <scope>test</scope>
    <version>7.1.0</version>
    <exclusions>
    <!-- Exclude snakeyaml, which has open CVEs and is unused -->
        <exclusion>
            <groupId>org.yaml</groupId>
            <artifactId>snakeyaml</artifactId>
        </exclusion>
    </exclusions>
</dependency>
```

### Forced dependency version updates

In other cases we actually do rely on the functionality provided by these transitive dependencies, so we cannot simply
exclude them.  However, it may be possible to force the version of these dependencies to be updated so that a
vulnerable older version of the dependency (the minimal version on which the library depends) is not used by default.

Take for example `janusgraph` -- it has a transitive dependency on the `sleepycat` library, and by default quite an old
version which has some known problems.  By adding an explicit dependency for a newer version of the `sleepycat`
module we can force this newer version to be used by `janusgraph` as well.

```xml
<dependency>
    <groupId>com.sleepycat</groupId>
    <artifactId>je</artifactId>
    <version>18.3.12</version>
</dependency>
```

Of course making this change requires testing, to ensure that the newer version of the transitive library is still
compatible with the base dependency.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
