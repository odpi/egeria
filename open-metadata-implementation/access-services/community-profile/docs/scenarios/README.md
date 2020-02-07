<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Using the Community Profile OMAS

Below are the list of tasks supported by the Community Profile OMAS.

## One time set up by the administrator

Many organizations already have [a system that maintains information about their employees
and/or customers and/or business partners](../../../docs/concepts/server-capabilities/master-data-manager.md).
The Community Profile OMAS therefore supports an event exchange with such a system
to keep the profiles synchronized. 
The following tasks cover the work of the IT team to integrate the Community Profile
OMAS with other systems.

* [Loading personal profiles of existing members of an organization](loading-personal-profiles.md)
  into an open metadata repository.
* [Synchronizing updates to personal profiles from another system](synchronizing-personal-profiles.md)
* [Loading the organization's departmental structure](loading-departmental-structure.md)
  into an open metadata repository.
* [Synchronizing the organization's departmental structure with another system](synchronizing-departmental-structure.md)
* [Capturing karma point plateaus](capturing-karma-point-plateaus.md)
  emitted from the Community Profile OMAS [OutTopic](../../../docs/concepts/client-server/out-topic.md).
* [Synchronizing collaboration activity with another system](synchronizing-collaboration-activity.md)


## Individuals working with their personal profile

A [personal profile](../concepts/personal-profile.md) provides a place for
an individual to share information about themselves
with the other people they are collaborating with.

Each personal profile is associated with one or more of the person's userIds.
It is retrieved using one of these userIds.

* [Retrieving my personal profile](retrieving-my-personal-profile.md)

For organizations where the personal profile is not loaded from another system (see above)
an individual can maintain their own profile.

* [Creating my personal profile](creating-my-personal-profile.md)
* [Updating my personal profile](updating-my-personal-profile.md)
* [Removing my personal profile](removing-my-personal-profile.md)

Once an individual has a personal profile, they will be awarded [karma points](../concepts/karma-point.md)
when they contribute content to open metadata.  An individual can query their karma points.

* [Retrieving my karma points](retrieving-my-karma-points.md)

The individual can also maintain [collections](../concepts/favorite-things-collection.md) of their
favourite [Assets](../../../docs/concepts/assets), [Projects](../../../docs/concepts/projects)
and [Communities](../concepts/community.md) and control notifications
about changes to the contents of these lists.

* [Accessing my favorite assets](accessing-my-favorite-assets.md)
* [Managing my favorite assets](managing-my-favorite-assets.md)
* [Accessing my favorite projects](accessing-my-favorite-projects.md)
* [Managing my favorite projects](managing-my-favorite-projects.md)
* [Accessing my favorite communities](accessing-my-favorite-communities.md)
* [Managing my favorite communities](managing-my-favorite-communities.md)

An individual can link other [resources](../concepts/useful-resource.md) such as glossaries, and external references to their profile.

* [Finding resources](finding-a-resource.md)
* [Accessing my resource list](accessing-my-resource-list.md)
* [Managing my resource list](managing-my-resource-list.md)
* [Accessing my external reference list](accessing-my-external-references.md)
* [Managing my external reference list](managing-my-external-references.md)

An individual can create a series of personal notes.  These are like a personal blog.
They are visible to other users who can comment on and review the content.

* [Setting up my personal notes](setting-up-my-personal-notes.md)
* [Accessing my personal notes](accessing-my-personal-notes.md)
* [Removing my personal notes](removing-my-personal-notes.md)
* [Adding a personal note](adding-a-personal-note.md)
* [Updating a personal note](updating-a-personal-note.md)
* [Removing a personal note](removing-a-personal-note.md)

An individual can query the [teams](../../../docs/concepts/organizations/team.md) and
[communities](../concepts/community.md) they belong to.

* [Accessing my communities](accessing-my-communities.md)
* [Accessing my teams](accessing-my-teams.md)

An individual can access an manage a list of close colleagues
called their [peer network](../concepts/peer-network.md).

* [Accessing my peer network](../scenarios/accessing-my-peer-network.md)
* [Managing my peer network](../scenarios/managing-my-peer-network.md)

An individual can query their [roles](../concepts/personal-roles.md) in the organization
and any [actions (to dos)](../concepts/to-do.md)
that have been assigned to them as part of one of these roles.

* [Accessing my roles](accessing-my-roles.md)
* [Accessing my to dos for each role](accessing-my-to-dos.md)
* [Managing to dos](managing-a-to-do.md)

An individual can add feedback to their profile and others (see below).

## Providing feedback and content to personal profiles

An individual can send a personal message to themselves, or another user.
This message is attached to the recipient's profile.

* [Sending a personal message](sending-a-personal-message.md)
* [Replying to a personal message](replying-to-a-personal-message.md)
* [Updating a personal message](updating-a-personal-message.md)
* [Removing a personal message](removing-a-personal-message.md)

It is possible to add comments, likes and reviews to a personal note.

* [Adding a comment to a personal note](adding-a-comment-to-a-personal-note.md)
* [Updating a comment to a personal note](updating-a-comment.md)
* [Removing my comment from a personal note](removing-my-comment-from-a-personal-note.md)
* [Adding a review to a personal note](adding-a-review-to-a-personal-note.md)
* [Updating my review to a personal note](updating-my-review.md)
* [Removing my review from a personal note](removing-my-review.md)
* [Adding a like to a personal note](adding-a-like-to-a-personal-note.md)
* [Removing a like from a personal note](removing-my-like-from-a-personal-note.md)

It is possible to create and attach [tags](../concepts/tag.md) to
personal profiles, personal notes,
and comments either for your personal profile or someone else's.

* [Finding existing tags](finding-a-tag.md)
* [Accessing resources attached to a tag](accessing-tagged-resources.md)
* [Accessing my tags](accessing-my-tags.md)
* [Creating a tag](creating-a-tag.md)
* [Attaching a tag](attaching-a-tag.md)
* [Detaching my tag](detaching-my-tag.md)
* [Detaching a tag from a resource](detaching-a-tag.md)
* [Deleting my private tag](removing-my-tag.md)
* [Deleting public tags](removing-a-tag.md)

## Individuals searching for other people and teams

Below are different ways to locate people in the organization.

* [Finding a Person](finding-a-person.md)
* [Querying another's personal profile](querying-anothers-personal-profile.md)
* [Navigating the Departmental Structure](navigating-the-departmental-structure.md)
* [Finding a Team](finding-a-team.md)
* [Accessing my teams](accessing-my-teams.md)
* [Viewing Leaders of a Team](viewing-leaders-of-a-team.md)
* [Viewing Members of a Team](viewing-members-of-a-team.md)


## Communities

[Communities](../concepts/community.md) collect
together resources, best practices and ideas for a group of people
who are collaborating on a specific topic or skill.

Anyone can create a community.

* [Creating a community](creating-a-community.md)

The person creating the community is the [community leader](../concepts/community-leader.md).
They can then add other people as [community members](../concepts/community-member.md)
with different [community roles](../concepts/community-roles.md).

* [Adding a new community member](adding-a-new-community-member.md)
* [Changing a community member's role](changing-community-member-role.md)
* [Removing a community member](removing-a-community-member.md)

Community leaders and [administrators](../concepts/community-administrator.md)
can remove inappropriate content from a community and close it.

* [Removing a comment from a community](removing-a-community-comment.md)
* [Removing a note from a community](removing-a-community-note.md)
* [Removing a review from a community](removing-a-review.md)
* [Removing a resource from a community](removing-a-community-resource.md)
* [Closing a community](closing-a-community.md)

Individuals can locate and connect with a community.

* [Finding the communities I am a member of](accessing-my-communities.md)
* [Finding a community](finding-a-community.md)
* [Querying a community](querying-a-community.md)
* [Watching a community](watching-a-community.md)
* [Joining a community](joining-a-community.md)
* [Leaving a community](leaving-a-community.md)

Once someone is a member of a community they can add content to it.

* [Adding a comment to a community](adding-a-comment-to-a-community.md)
* [Replying to a community comment](replying-to-a-comment.md)
* [Removing my comment from a community](removing-my-comment-from-a-community.md)
* [Adding a resource to a community](adding-a-resource-to-a-resource-list.md)
* [Creating a community forum](creating-a-community-forum.md)
* [Adding a contribution to a community forum](adding-a-contribution-to-a-forum.md)
* [Removing my contribution from a community forum](removing-a-contribution-from-a-forum.md)

Community content can have feedback attached in the form of tags, reviews and likes.

* [Attaching feedback to a community](attaching-feedback-to-a-community.md)
* [Attaching feedback to a community comment](attaching-feedback-to-a-community-comment.md)
* [Attaching feedback to a community forum](attaching-feedback-to-a-community-forum.md)
* [Attaching feedback to a community forum contribution](attaching-feedback-to-a-community-forum-contribution.md)

## Related information

* [Community Profile OMAS Concepts](../concepts)
* [Community Profile OMAS Design](../design)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.