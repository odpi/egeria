<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Egeria Operations

The Egeria project provides content (standards, data, code and documentation) that is intended for wide consumption
across many types of organizations - from those that rely on data in their operation - to organizations that have products
or technology designed to help manage data and its related processing.

A project of this scope requires input from a wide range of subject matter experts with different backgrounds and allegiances.
As such we need a set of principles, roles and operating practices to ensure the results of our contributions are useful,
have high quality and are widely consumable.

## General principles

The principles set the tone of the operation of Egeria:

* The activities of the project ensure open collaboration.
Through this open collaboration we aim to build a community of people who are interested in the success of the project.
* The scope of the content is determined by the individuals who are actively contributing.
* The resulting content is licensed under the Apache 2.0 license.
* An individual's privileges and position is awarded through their contribution and engagement.

These principles should be respected as the procedures used to manage the Egeria project are evolved and matured.

## Egeria community members

Anyone can become a member of the Egeria community by signing up to the
the Egeria mailing list, joining the slack community, attending the project online meetings
or contributing content to one of more of the GitHub repositories.

The [Community Guide](./Community-Guide.md) describes how to connect to these channels.

All participants in the Egeria community are bound by the ODPi's
[Code of Conduct](https://github.com/odpi/specs/wiki/ODPi-Code-of-Conduct).

As a member you are able to attend our meetings, just to listen, or to play an active part in the discussion.
The online meetings are recorded to allow community members to catch up if they are not able to attend the live meeting.
When you attend the community meetings specifically, your name will be recorded in the meeting minutes along with any remarks or suggestions you make.
The agenda and minutes of our community meetings are publicly available on the [Egeria wiki](https://github.com/odpi/egeria/wiki).

A member may make contributions to the Egeria content by submitting a
Git pull request on the appropriate Git repository.   This will be reviewed and processed by the Egeria maintainers.
Making a contribution is also described in the [Community Guide](./Community-Guide.md).

Community members can progress to be **Egeria Contributors** and then **Egeria Maintainers**.

### Egeria contributors

Egeria contributors are members who have actively taken additional steps to promote and foster the success of Egeria and its acceptance/adoption across the IT community. The activities that contributors engage in might include:

* Provide best practices for information governance, lineage, metadata management and other related disciplines during active discussions and/or development of material
* Actively participate in meetings and discussions
* Promote the goals of ODPi Egeria and the benefits of open metadata to the IT community (deliver presentations, informal talks, assist at trade shows, independent blogs, etc.)
* Assist in the recruitment of new members
* Contribute where appropriate to documentation and code reviews, specification development, demonstration assets and other artifacts that help move Egeria forward

#### How to become a contributor

Being recognized as an Egeria contributor is done by nomination of an Egeria maintainer with a majority vote
of Egeria maintainers to confirm. Once confirmed, you will receive 
[an Egeria Contributor badge](developer-resources/badges) to add to
your social profiles and/or website, and can publicly refer to yourself as an Egeria contributor.

### Egeria project maintainers

Maintainers are members of the Egeria community that have permission to change the Egeria content.
This may be content that they have created themselves, or has been provided by another member.
Maintainers also have responsibility for helping other project members with their contributions.
This includes:
* Monitoring email aliases.
* Monitoring Slack (delayed response is perfectly acceptable).
* Triage GitHub issues and perform pull request reviews for other maintainers and the community.
* Make sure that ongoing git pull requests are moving forward at the right pace or closing them.
* In general continue to be willing to spend at least 25% of one's time
working on the project (approximately 1.25 business days per week).

#### How to become a maintainer

New maintainers are voted onto the maintainers list by the existing maintainers - see
[maintainer list](./MAINTAINERS.md).

A person wishing to become a maintainer sends a note to the existing maintainers
at odpi-project-egeria-maintainers@lists.odpi.org, listing their Egeria contributions to date and
requesting to be made a maintainer.
The maintainers vote and if a majority agree then the requester
is added to the maintainers list and given write access to our
[git repository](https://github.com/odpi/egeria). 

Once confirmed, you will receive an
[an Egeria Maintainer badge](developer-resources/badges) to add to
to add to your social profiles and/or website,
and can publicly refer to yourself as an Egeria maintainer.

#### When does a maintainer lose maintainer status

If a maintainer is no longer interested or cannot perform the maintainer duties listed above, they
should volunteer to be moved to emeritus status. In extreme cases this can also occur by a vote of
the maintainers per the voting process below.
Emeritus maintainers can rejoin the maintainer list through a vote of the
existing maintainers.

### ODPi Egeria leadership

The leadership of ODPi Egeria is granted through a vote of the Egeria maintainers.
ODPi Egeria is currently led by Mandy Chessell.

## Egeria project meetings

Some meetings are face-to-face, but most are conference calls.  
Attendance at meetings is open to all.  Conference calls can be joined without an explicit invitation.
However, due to physical security requirements at some of the venues we use,
it is necessary to ensure you are added to the invitee list of any face-to-face meetings
that you wish to attend and complete the necessary formalities for the venue.

For example, the face-to-face meeting may be at a conference that requires you to register for the conference to attend.
Or a meeting may be at an organization's offices that are required to maintain a list of everyone on site.

## Egeria on Slack

Egeria uses the [ODPi's Slack community](http://slack.odpi.org/) to provide an ongoing dialogue between members.
This creates a recorded discussion of design decisions and discussions that complement the project meetings.
This is the main slack channel for the Egeria project:
[https://odpi.slack.com/messages/CAZDMLTFF](https://odpi.slack.com/messages/CAZDMLTFF]).
Additional channels will be added as new workgroups and discussion topics are established.

## Egeria email

Egeria uses the [following distribution list](https://lists.odpi.org/g/odpi-project-egeria)
to advertise events and news for the community.

## Egeria content management tools

The Egeria content is managed in GitHub under [https://github.com/odpi/egeria](https://github.com/odpi/egeria).
It may be developed using patches, branches from master, or forks/git pull requests.
Each change should have a [GitHub issue](https://github.com/odpi/egeria/issues) explaining why the change is being made.
The new or updated content should follow the Egeria
[developer guidelines](https://egeria.odpi.org/developer-resources/Developer-Guidelines).

When new content proposed to the project, the person contributing is required to sign the contribution
to confirm it conforms to the [Developer Certificate of Origin (DCO)](https://developercertificate.org/).

## Egeria project releases

The Egeria team aim to create an official release of the open metadata and governance capability every month.
This release will be available to include in products and other technology through
[Maven's Central Repository](https://search.maven.org), or through a download from the ODPi site.

In between official releases, the latest build is also available to developers, through the Egeria site.

### Release Process Overview

Releases are published to [Bintray](https://bintray.com/odpi) where they
are GPG signed and distributed to [Maven
Central](https://oss.sonatype.org).

Creating a new release has the following stages:

* Creating a branch off master for the release code.
* Incrementing the release numbers in the pom files in master and committing through a pull request.
* Within the branch removing "`-SNAPSHOT`" from all of the Egeria version
  numbers in the pom files and committing with a pull request.
* Staging a release through [Azure Pipelines](https://dev.azure.com/ODPi/Egeria/_release?_a=releases&definitionId=1&view=mine)
* Verifying release criteria are met on the staged build
* Promotion of the release from staging to release
* Distribution of the release to [Bintray](https://bintray.com/odpi), which is then replicated to [Maven Central](https://oss.sonatype.org).
* Documenting the release with a [Git Release](https://github.com/odpi/egeria/releases).

### Release Process Steps for Maintainers

New releases can be created by Egeria maintainers that have the
appropriate access on [Azure
Pipelines](https://dev.azure.com/ODPi/Egeria/_release).

Creating a new release has the following stages following the release
branch creation:

1. Update the Azure [Release Pipeline](https://dev.azure.com/ODPi/Egeria/_release?_a=releases&definitionId=1&view=mine) to use the release branch
   1. Click "`Edit`"
   1. Click *\_ODPI\_Egeria\_Commit* under the "Artifacts" column
   1. Modify *Default branch* to be the new release branch
   1. Click *Save* to the right of the breadcrumb header
1. Stage a release though [Azure Pipelines](https://dev.azure.com/ODPi/Egeria/_release?_a=releases&definitionId=1&view=mine)
   1. Click *Create release*
   1. Select commit id from *Version* dropdown.
   1. Click *Create*

   Pulling from the staging repository can be done through Maven with the
   following settings:

   ```
   <repositories>
     <repository>
       <snapshots>
         <enabled>false</enabled>
       </snapshots>
       <id>central</id>
       <name>egeria-staging</name>
       <url>https://odpi.jfrog.io/odpi/egeria-staging</url>
     </repository>
   </repositories>
   <pluginRepositories>
     <pluginRepository>
       <snapshots>
         <enabled>false</enabled>
       </snapshots>
       <id>central</id>
       <name>egeria-staging</name>
       <url>https://odpi.jfrog.io/odpi/egeria-staging</url>
     </pluginRepository>
   </pluginRepositories>
   ```

1. After the community verifies the staged artifacts meet the release
   criteria, the *Promote to Release* step will be approved (and the
   *Abandon Release* step canceled)
   If the artifacts do not meet the criteria, the *Promote to Release*
   step should be denied, and *Abandon Release* should be
   approved. The release process should then be started again at step #2
   from a different commit once changes have been merged.
1. After the release promotion is approved, the artifacts will be
   distributed to [Bintray](https://bintray.com/odpi) which will sign
   them and sync them to [Maven Central](https://oss.sonatype.org).

### Release Process Troubleshooting

The Linux Foundation maintains a Knowledge Base (KB) of articles on
possible issues that may arise during the release process:
 * [Including a New Package in the Release](https://confluence.linuxfoundation.org/display/ITKB/Including+Bintray+Packages+in+JCenter)
 * [Fixing Package Corruption](https://confluence.linuxfoundation.org/display/ITKB/Redistribute+Artifacts+to+Bintray)
 * [Getting Packages Synced to Maven-Central](https://confluence.linuxfoundation.org/display/ITKB/Sync+Artifacts+from+Bintray+to+Maven+Central)

If the KB articles are not able to fix the problem, please open a ticket
with [Linux Foundation support](https://jira.linuxfoundation.org/servicedesk/customer/portal/2)

## Conflict resolution and voting

In general, we prefer that technical issues and maintainer membership are amicably worked out
between the persons involved. If a dispute cannot be decided independently, the maintainers can be
called in to decide an issue. If the maintainers themselves cannot decide an issue, the issue will
be resolved by voting. The voting process is a simple majority in which each maintainer receives one vote.



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.

