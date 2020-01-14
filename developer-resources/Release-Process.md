<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Release Management

## Overall Release Policy

* Aim to release approximately every month
* Typically target end of month for external availability
* Will only release an update between releases in exceptional circumstances
* Preserves backwards compatability as much as possible
* Try and maintain regular heartbeat - even if completion of some features continues in a subsequent release
* master kept open for new code features


## Obtaining releases / artifacts

* Maven Central - https://mvnrepository.com/artifact/org.odpi.egeria - typically used by other developers integrating with our code
* Github Release - https://github.com/odpi/egeria/releases - source code in zip & tar.gz formats
* Git - 'git checkout Vx.y' to get version as-shipped (each release is tagged at the point it is shipped)
* Release notes are available online (in master) at https://github.com/odpi/egeria/tree/master/release-notes
 
 ## Release process summary
 
 * Agree schedule
 * Track remaining issues & PRs
 * Create branch
 * Update master from x.y-SNAPSHOT to x.z-SNAPSHOT
 * Test, Merge any remaining required changes into branch
 * Update branch's release version from x.y-SNAPSHOT to x.y
 * Create a release in Azure dev pipelines
 * Final testing
 * Approve release
 
 
## Release Process in detail

#### 1. Agreement of overall schedule

* Agree on appropriate dates for branching given expected duration for testing, vacation/public holidays
* Communicate with team on regular calls, and via #egeria-github on slack

#### 2. Tracking remaining issues

* 


#### 2. Communicate 

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
