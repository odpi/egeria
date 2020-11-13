<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Developer import Egeria content using Gradle

You will first need to decide if you are developing against production or snapshot code.  
Most consumers should be obtaining content from [Maven Central](https://search.maven.org) 
using the `org.odpi.egeria` group. 

## Select repository

If you are developing against the current code you may want to use the current repository.  Selecting the repository from [Artifact Repository](https://odpi.jfrog.io/odpi/egeria-snapshot).  For documentation 
here, we chose the `snapshot` repository. 

## Add repository to your gradle

In our `build.gradle` file we would the following: 

```groovy
repositories {
        maven {
            url "https://odpi.jfrog.io/odpi/egeria-snapshot"
        }
    }
```

## Add artifacts as needed

We import for `compile` the artifacts needed to build our code.

```sh
compile "org.odpi.egeria:repository-services-apis:+"
```

This allows us to get the latest `repository-services-apis` artifact from the `snapshot` repository. 

## Build and check your gradle. 

We rebuild our project to see the new dependencies and ensure we are getting the desired artifacts.

```sh
gradlew build --refresh-dependencies
```

There should now be dependencies within your project for the Egeria artifacts you imported.



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
