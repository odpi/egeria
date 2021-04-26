<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Egeria Release Notes

Below are the official releases of Egeria.  The project aims to
produce a new release about once a month.  Each release includes
new features and fixes to existing function.  The team aims to
provide complete backward compatibility for all components that
are officially released (to understand more about what it means to have released function see
[Egeria Content Status](../open-metadata-publication/website/content-status)).
If backwards compatible changes are not possible, it will be called out explicitly in the
release notes with an explanation on how to upgrade.

Each release will also upgrade the level of its dependencies to
ensure Egeria is running with all of the latest security patches.
We therefore recommend that you keep moving forward with us to
get the best Egeria experience possible.

One way you can help us is to feedback on your experiences, both good
and bad.  We would love to hear from you.

## Latest Release

Below are the release notes for the latest Egeria release:

* [Release 2.8](release-notes-2-8.md) - April 2021
  * New support for event and property filtering for the open metadata server security connector
  * Changes to metadata types
  * New performance workbench for the CTS (technical preview)
  * New interface for retrieving the complete history of a single metadata instance
  * Splitting of CTS results into multiple smaller files
  * Bug Fixes
  * Dependency updates
  * Important notes included on known issues
  
## Older Releases

* [Release 2.7](release-notes-2-7.md) - March 2021
  * Bug Fixes
  * Dependency updates

* [Release 2.6](release-notes-2-6.md) - February 2021
  * Governance Server, replacing Stewardship, Discovery & Security Server
  * Changes to location of configuration files
  * New metadata types for Governance Actions, Duplicate Processing
  * Improvements and build changes for the React based UI
  * A new simple helm chart deployment sample
  * Bug Fixes
  * Dependency updates

* [Release 2.5](release-notes-2-5.md) - December 2020
  * User Interface improvements
  * Additional tests
  * Bug fixes
  * Dependency updates
  
* [Release 2.4](release-notes-2-4.md) - November 2020
    * Integration Daemon
    * Rex/Tex in Presentation Server
    * Additional tests
    * Bug fixes
    * Dependency updates

* [Release 2.3](release-notes-2-3.md) - October 2020
    * Presentation Server User Interface
    * Dino UI function
    * Bug fixes
    * Dependency updates

* [Release 2.2](release-notes-2-2.md) - September 2020
    * Bug fixes
    * Dependency updates

 * [Release 2.1](release-notes-2-1.md) - August 2020
    * Bug fixes
    * Dependency updates

 * [Release 2.0](release-notes-2-0.md) - July 2020
    * https security for server chassis
    * encrypted file connector
    * Bug fixes
    * Dependency updates

* [Release 1.8](release-notes-1-8.md) - June 2020
    * Additional tutorial content in the form of the Egeria Dojo
    * Repository explorer usability and functionality improvements
    * Improved packaging for samples and utilities
    * reliability improvement for Kafka connector
    * metrics and monitoring using spring boot actuator and adding prometheus support
    * Bug fixes
    * Dependency updates
    
* [Release 1.7](release-notes-1-7.md) - May 2020
    * Bug fixes
    * Dependency updates

* [Release 1.6](release-notes-1-6.md) - April 2020
    * Audit Log Framework (ALF) technical preview
    * Repository Explorer (REX) 

* [Release 1.5](release-notes-1-5.md) - March 2020
    * Metadata de-duplication identification and notification
    * Data Engine OMAS Technical Preview
    * Data Engine Proxy Server Technical Preview
    
* [Release 1.4](release-notes-1-4.md) - February 2020
    * Bug fixes, documentation
    * Dependency updates

* [Release 1.3](release-notes-1-3.md) -  January 2019
    * Conformance test suite improvements
    * Open Metadata Archives
    
* [Release 1.2](release-notes-1-2.md) - December 2019
    * Conformance test suite
    * Asset cataloging and consumption
    * Governance zones and metadata security
    * Open metadata archives

* [Release 1.1](release-notes-1-1.md) - November 2019
    * Multi-tenant OMAG Server Platform
    * JanusGraph based metadata repository

* [Release 1.0](release-notes-1-0.md) - February 2019
    * Peer-to-peer metadata exchange


## Future Releases

Our master branch is currently taking code for all future releases.
Many of the features are large and the teams integrate code for
partial function as soon as it is stable and has no impact on released function.
So you will see support for much more function than is officially released.
This way you can monitor and feedback on future items as they are developed.

The [Road map for Egeria](../open-metadata-publication/website/roadmap)
describes the end vision for Egeria and our current status.


## Reporting issues

If you discover an issue in the release you are using, we recommend
first upgrading to the latest available release.  If this does not
resolve the problem, please raise a new
[git issue](https://github.com/odpi/egeria).

You can also follow our discussions by joining the LF AI & Data Foundation Slack team.
Sign up at https://slack.lfai.foundation/ & join the
`egeria-discussions` channel.




























----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
