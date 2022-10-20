<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Release Management

New releases can be created by Egeria maintainers that have the
appropriate access on [Azure Pipelines](https://dev.azure.com/ODPi/Egeria/_release).

Releases are published to [Bintray](https://bintray.com/odpi) where they
are GPG signed and distributed to [Maven Central](https://oss.sonatype.org).
 
## Overall Release Policy

* Aim to release approximately every month
* Typically target end of month for external availability
* Will only release an update between releases in exceptional circumstances
* Preserves backwards compatibility as much as possible
* Try and maintain regular heartbeat - even if completion of some features continues in a subsequent release
* main kept open for new code features

## Obtaining releases / artifacts

* Maven Central - https://mvnrepository.com/artifact/org.odpi.egeria - typically used by other developers integrating with our code
* Github Release - https://github.com/odpi/egeria/releases - source code in zip & tar.gz formats
* Git - 'git checkout Vx.y' to get version as-shipped (each release is tagged at the point it is shipped)
* Release notes are available online (in main) at [https://github.com/odpi/egeria/tree/main/release-notes](../release-notes).

 ## Release process summary
 
 * Agree schedule
 * Track remaining issues & PRs
 * Create branch
 * Update main from x.y-SNAPSHOT to x.z-SNAPSHOT
 * Test, Merge any remaining required changes into branch
 * Update branch's release version from x.y-SNAPSHOT to x.y
 * Create a release in Azure dev pipelines
 * Final testing
 * Approve release
 
 
## Release Process in detail

#### 1. Agreement of overall schedule

* Agree on appropriate dates for branching given expected duration for testing, vacation/public holidays
* Typically allow 1-2 weeks between branching and availability
* Communicate with team on regular calls, and via #egeria-github on slack
* In the last week before branching discuss holding off on any big changes in main that could destabilize the codebase

#### 2. Tracking remaining issues (stability)

* Ensure any required issues/PRs for the release have the correct milestone set
* Move any issues/PRs not expected to make/not required for the release to a future milestone
* Aim to branch when most issues/PRs are complete to minimize backporting from main, but not at the expense of impacting ongoing main development
* Agree final branch date/criteria

#### 3. Create branch 

* Checkout main `git checkout main`
* Ensure local update `git pull upstream main`
* Create branch `git branch egeria-release-x.y`
* Push to upstream `git push upstream egeria-release-x.y`

#### 4. Update main version & cleanup notes
* `git checkout main`
* `git pull upstream main`
* Edit all files (command line or IDE) to replace 'x.y-SNAPSHOT' with the next version, ie change '1.3-SNAPSHOT' to '3.13-SNAPSHOT'. Most of the changes are in pom.xml files, however some code & documentation also has references to our versions and all need modifying. 
* If using an IDE like IntelliJ make sure you have all hits by searching again as by default only a limited number of hits are shown - see https://youtrack.jetbrains.com/issue/IDEA-157855 for Intellij advice.
* Commit
* Now remove all the release notes from the 'release-notes' directory other than README.md - so users will always get directed to the latest in main
* Commit
* Create a PR, have reviewed/approved & merged as usual - aim to do this as quickly as reasonable so that there is no potential for version clash 

### 5. Test and Merge
* Run appropriate tests for the release. For example in addition to automated tests - Check notebooks, run the CTS & check for compliance, check the User Interface.
* Raise issues for any changes required as usual
* Note that approval is required for changes going into a release branch
* PR builds are run as usual, however merge builds, sonar etc do not run
* To backport changes from main, first wait until the PR is merged into main, then use `git cherrypick -s <commithash>` to apply to egeria-release-x.y, then push as usual. 
* In some cases a merge commit will need to be made using `git cherrypick -s -m 1 <commithash>`
* If code has diverged significantly a manual recode may be easiest

#### 4. Update branch version
* Aim to make this change when the code appears to be ready to ship apart from final tests in order to avoid version confusion
* `git checkout egeria-release-x.y`
* `git pull upstream egeria-release-x.y`
* Edit all files (command line or IDE) to replace 'x.y-SNAPSHOT' with 'x.y' ie removing the -SNAPSHOT designation. Most of the changes are in pom.xml files, however some code & documentation also has references to our versions and all need modifying. 
* Commit, and do not make any other changes.
* Create a PR, have reviewed/approved & merged as usual

#### 5. Create a release in Azure dev pipelines
* Navigate to https://dev.azure.com/odpi/egeria/_build & ensure logged on (top right) - you will need special priviliges for this. Contact the Egeria team leaders for access
* Click on 'Egeria - m.n' in the list of releases
* Click on 'Edit'
* Change 'Default branch' to the desired release x.y & save
* Click 'Create release' & leave settings at default (optionally add a comment) then click create
* The release process will now run - by starting a build
* Test maven artifacts will be available at https://odpi.jfrog.io/odpi/egeria-staging/org/odpi/egeria/
* Ensure any final tests are run

#### 6. Finalizing the release
* Approve the release by responding to the automated email (or reject, and go back to the previous step)
* tag the release in git `git tag -a Vx.y -m "Release x.y"`
* The final steps will now run & push artifacts first to bintray, and then maven central
* Meanwhile create the git release at https://github.com/odpi/egeria/releases. Use 'Vx.y' as the tag, and ensure the correct branch is set for the target ie egeria-release-x.y
* Fill in the release notes using a title of 'Release x.y' and copy the notes from the appropriate release notes at https://github.com/odpi/egeria/tree/main/release-notes
* Artifacts will be available on maven central within around half a day.
* Source archives will be added to the release on git within an hour or so.

## Useful links
* Azure pipelines - https://dev.azure.com/odpi/egeria/_build

## Release Process Troubleshooting
 
 The Linux Foundation maintains a Knowledge Base (KB) of articles on
 possible issues that may arise during the release process:
  * [Including a New Package in the Release](https://confluence.linuxfoundation.org/display/ITKB/Including+Bintray+Packages+in+JCenter)
  * [Fixing Package Corruption](https://confluence.linuxfoundation.org/display/ITKB/Redistribute+Artifacts+to+Bintray)
  * [Getting Packages Synced to Maven-Central](https://confluence.linuxfoundation.org/display/ITKB/Sync+Artifacts+from+Bintray+to+Maven+Central)
 
 If the KB articles are not able to fix the problem, please open a ticket
 with [Linux Foundation support](https://jira.linuxfoundation.org/servicedesk/customer/portal/2)
 
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
