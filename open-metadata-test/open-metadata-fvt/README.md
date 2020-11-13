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

----
Return to [open-metadata-test](..)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

