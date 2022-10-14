<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
  
# ODPi Egeria Functional Verification Test (FVT) Suite
  
Functional Verification Tests (FVTs) test multiple components together to
ensure they function correctly.   Typically they load test data into the in-memory repository and drive the
external APIs of the components under test to ensure they are robust enough to support the range of functions
needed by their consumers.

* **[access-services-fvt](access-services-fvt)** - provides an FVT test suite for each of the Open Metadata Access Services (OMAS).
  They each focus on the functional correctness of a specific OMAS.  
* **[open-types-fvt](open-types-fvt)** - uses code generated ot build a bean layer from the open metadata types
and then tests the resulting beans.

# NOTE

It is important that unique ports are used for each test, to prevent concurrency issues - this is particularly true of gradle

This is a current list of known port usage

* 10441 Community Profile
* 10442 Data Engine
* 10443 Asset Consumer
* 10444 Asset Manager
* 10445 Asset Owner
* 10446 Data Manager
* 10447 Digital Architecture
* 10448 Subject Area 
* 10450 Discovery Engine
* 10451 Governance Engine
* 10452 Governance Program
* 10453 Analytics Modeling 
* 10455 Stewardship Action
* 10454 Glossary Author (view services)
----
Return to [open-metadata-test](..)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

