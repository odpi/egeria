<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Developer Guidelines

Egeria provides technology for an open standard that seeks to improve the processing and
protection of data across organizations.  For its developers, this carries the benefit that
their work receives high recognition, but also additional responsibilities to ensure its
wide applicability and longevity.

For example, Egeria seeks a broad audience - from developers to adopting vendors to consuming users.
Building this audience and allowing the community to scale requires clarity in the way
the software is written, documented, packaged and used.  Many of the guidelines seek to make
it easier for someone new to pick up the software, at the expense of maybe a little more work,
or a little less freedom of action for the original developer.

As such, these guidelines exist to remind us of these broader responsibilities.

## Java

The minimum level required to build & run Egeria is Java 11. 

See [Java](languages/Java.md) for more information.

## Operating System

Most developers use MacOS, our official builds use Linux (Ubuntu/Centos/RHEL should all be fine). 

The traditional Windows environment is not directly supported. It is recommended to use [WSL2](https://docs.microsoft.com/en-us/windows/wsl/) which offers a full Linux environment. 

## Maven

[Apache Maven](tools/Maven.md) is used to control the builds.
Maven 3.5 or higher is required to build Egeria. 3.6.x or above is recommended.

## Gradle

Gradle is not currently supported but is being developed. See [Gradle](tools/Gradle.md)

## Build warnings

Build output should be checked for any warnings ie `[WARNING]` and these should be eliminated.

For example the Java compiler is set to use `-Xlint:all` and may report warnings about deprecated function, unsafe casts, unchecked conversions etc which should be addressed.

Other tools used in the build may also result in warnings which should also be addressed, whilst testcases should ensure output is captured to avoid such warnings appear in the build logs.

## License text in files

All files for Egeria should have a license included.  We are using the Apache 2.0 license,
which protects our code whilst still allowing commercial exploitation of the code.  There is
an example of the license text at the top of this file.  The following files in the
**License-Example-Files** directory have the correct
license information formatted for different file types to make it easy to use.

* [License for Markdown Files.md](./License-Example-Files/License_for_Markdown_Files.md)
* [License for POM Files.xml](./License-Example-Files/License_for_POM_Files.xml)
* [License for Java Files.java](./License-Example-Files/License_for_Java_Files.java)

Notice that the license information is coded using [SPDX](https://spdx.org/ids).

## Documentation

Although all code for Egeria should be clear and easy to read, the code itself can only
describe **what** it is doing.  It can rarely describe **why** it is doing it.  Also, the
Egeria codebase is quite large and hard to digest in one go.  Having summaries of its
behaviour and philosophy helps people to understand its capability faster.

### README markdown files

Each directory (apart from Java packages) should have a `README.md` file that describes the
content of the directory.  These files are displayed automatically by GitHub when the
directory is accessed and this helps someone navigating through the directory structures.

The exception is that directories representing Java packages do not need README files
because they are covered by Javadoc.

### Javadoc

[Javadoc](https://docs.oracle.com/javase/7/docs/technotes/tools/solaris/javadoc.html)
is used to build a code reference for our public site.  It is generated
as part of the build.  There are three places where Javadoc should be provided
by the developer of Java code:

* Every Java source file should begin with a header javadoc tag just before the start of the
class/interface/enum, which explains the purpose and responsibilities of the code.
* All public methods should have a clear Javadoc header describing the purpose, parameters and
results (including exceptions).  This includes test cases.
* Each Java package should include a `package-info.java` file describing the purpose of the
package and its content.

Java code files may have additional comments, particularly where the processing is complex.
The most useful comments are those that describe the purpose, or intent of the code,
rather than a description of what each line of code is doing.

The output from a build should be checked to ensure there are no javadoc warnings - 
for example about undocumented parameters or exceptions.

## Diagnostics

Egeria will typically be embedded in complex deployment environments.
This means that we can not rely on standard developer logging provided by components
such as SLF4J.  We try to practice First Failure Data Capture (FFDC).
This is describes by the [FFDC Services](../open-metadata-implementation/common-services/ffdc-services) and the 
[Audit Log Framework (ALF)](../open-metadata-implementation/frameworks/audit-log-framework).

## Dependent libraries

New dependencies **must** only be introduced with the agreement of the broader
community.  These include frameworks, utility classes, annotations and external packages.
This may seem annoying but there are good reasons for this:

* The Egeria code needs to be embeddable in many different vendor products.
This is made easier by keeping the code libraries we are dependent on to the minimum
in order to avoid conflicts with libraries a consuming vendor may have already chosen.
* As developers, we have legal obligations to ensure we only use appropriately
licensed software in our work and part of the discussion related a new dependency
is to understand its license.
* Some projects may provide useful functionality but are only supported by one
person who may get bored with it, or no longer have the time to support it.
We should aim to build on dependent libraries that are backed by a strong
community or vendor.
* Each library function, or set of annotations, adds to the learning curve of
new people joining the team.  By only bringing in the really beneficial
libraries we ensure that the complexity they see relates only to the complexity
of the problem space, rather than the additional complexity we have introduced in
pursuit of playing with new functions.

If a developer wishes to introduce a new dependency to the Egeria project,
they should prepare a short guide (in a markdown file) that explains the value of
the new library, how it is to be used and links to more information.
They should then present their recommendation to the community and
and if agreed by the community, store the guide in the developer resources.

Once in place, the dependency should be maintained across the smallest appropriate
number of modules, and should be consistent throughout. - particularly when it may impact consuming technologies.

For more on how dependencies are managed in the codebase refer to [Dependency Management](./Dependency-Management.md).

## Coding style and layout

There are many coding and layout styles that provide clear and readable code.
Developers can choose the layout they prefer but with the following
restrictions/suggestions:

* Try to use full words rather than abbreviations or shortened versions of
a word for names such as class names, method names and variable names.
Cryptic names create more effort for the reader to follow the code.
* Use the same style throughout a file.
* If changing an existing file, use the same style and layout as the original
developer.  Don't impose your own style in the middle of the code since the
inconsistency that you introduce makes the whole file harder to read.
It should not be possible to see where you have made the changes once the
code is committed into git.

* For java unit tests use /src/test/java folder of the module (standard maven location), and postfix java file names with "Test".

### Working with Date and Time

In Egeria, date / time instants are always represented as Unix Epoch time with millisecond precision (milliseconds elapsed since January 1, 1970).

- The Egeria OMRS layer handles date / time as either `java.lang.Long` or as `java.util.Date` objects. It does not store localised versions of the date / time.
- In other Egeria APIs that might be developed, it is **strongly recommended** to store dates and times as a Long or Date. 
- In addition, it is possible to expose localised date representations if required.

## Testing

Egeria is an integration technology which means that it uses
a comprehensive multi-level approach to testing.

Modules include unit tests.  These unit tests should focus on
simple validation of Java Beans, utilities and code that can easily be tested
in isolation.  The unit tests run as part of the build and a pull request
can not be incorporated into master if
any unit tests are failing.  They should not significantly extend the time of the
build since this impacts all of the contributors productivity.
Our preferred Java frameworks for unit testing are [TestNG](http://testng.org) and [Mockito](http://mockito.org).

External APIs (typically they include both a client and a server component)
are tested using functional verification tests (FVTs).  These are located
in the [open-metadata-test/open-metadata-fvt](../open-metadata-test/open-metadata-fvt).
The aim of these tests is to check that the APIs validate all of their parameters and function correctly in a single server environment.
These tests also operate as part of the build but are not run as part of the PR process.
Modules should ensure they include some FVTs as they move to from development to
technical preview.  By the time the module is moving to released function, the
FVTs should be able to validate that this function is stable and correct.
(Details of the development phases are defined on the 
[Content Status](https://egeria-project.org/release-notes/content-status/) page.)

Some connectors are tested via the [Conformance Test Suite](../open-metadata-conformance-suite).
If you deliver a connector that is covered by this test suite, you should run the tests before
merging changes into master.  The conformance test suite is also
run as part of the release process.

Egeria's hands on labs
provide a complex multiserver environment and are typically used
by contributors to verify that their changes have not regressed any of the
basic function.

We are also interested in building out a comprehensive integration test to 
allow automated complex multi-server scenarios that can be running
continuously.

## Using an IDE

IDEs can make navigating the Egeria code easier. Each IDE can vary a lot. 
Many of our team use [JetBrains IntelliJ](tools/IntelliJ.md).

In the case of problems the first problem determination step is to check you can build Egeria normally at the command line ie `mvn clean install` from the source 
root. That will prove at least java, maven are correct . 

We have also noticed that you need to ensure JAVA_HOME is set (see under 'Java' earlier on this page) or the build will fail running javadoc. 

## Issue Tracking

See [Issue Tracking](Issue-Tracking.md) for information about how we use issues in Egeria.

## Creating Postman Samples

[Postman](https://www.getpostman.com) is a great tool for experimenting with REST APIs.  It helps during development
and also while someone is learning how to call Egeria.
[Creating Postman Samples](Postman-Samples.md) describes how to create a Postman sample for a new API.

----
* Return to [Developer Resources](.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
