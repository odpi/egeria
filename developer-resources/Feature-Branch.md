<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Using Feature Branches

## Background

The standard approach for Egeria is to 
 * make code changes on a branch on one's own fork
 * Create a PR to push from this branch to main
 
Most of the time these are coded by a single developer, with additional review/testing from peers as part of the PR process. On occasion a few developers may directly collaborate on the code changes & can pull/push to/from each others branches, or share updates in other ways.
 
'main' therefore always represents the 'best so far' code, ideally in a 'ready to release' state, through build automation, testing & peer pressure. Everyone benefits from the latest code changes and any divergence between a developer's environment and main is minimized. 

## Purpose of Feature Branches

Sometimes there is a need to co-ordinate a larger piece of work in a team of developers who need the ability to:
 * Reduce the impact of changes on main - ie for everyone else
 * Reduce the impact of constantly updates from main in order to stablize new large changes
 * Have a stable environment for feature oriented testing
 
In these cases a 'feature branch' may be proposed. A github issue should be created, and the proposal discussed in one of the regular Egeria calls to build consensus around the approval.  
 
**NOTE** Feature branches add overhead. They can lead to code divergence and complexity. They will only be created in compelling circumstances for long running feature work.
 
Once agreed, on of the maintainers / admins will make the required setup. See the last section of this document for some more information on this.

## Development team working on Feature Branch

Any work specifically and solely for the feature should be done on the agreed branch, but it's important that normal defect fixes, enhancements unrelated features should continue to be worked on via main - i.e. working on a dev's own fork for a short period (hours/days) and merged back to main. 

This helps other developers working on the project, and reduces the complexity of subsequent merges from the feature branch.

The team working on the feature will need to arrange/agree their own builds for testing/deployment.

## Merging to main & releasing

We do not release from a feature branch. All release branches are made from main.

It is the feature team's responsibility to: 
 * Merge the latest code from main 
 * Merge feature branch back to main

There's no set schedule for this. Longer intervals offers the feature more stability, but can rapidly build up a much more complex merge scenario which the feature team will need to resolve.

It is the feature team's responsibility to respond to any issues in main, and to validate that the feature is 'good'.

## Administrative Tasks

These tasks should only be performed by someone familiar with the process and with appropriate authority after establishing team agreement. As such exact commands are not given below:

### Creating a branch

* Create a feature branch named `feature-XXX` where XXX is a descriptive name for the feature. (With issues, using the issue number can be helpful, but since we expect a small number of feature branches, this seems clearer.)
* Ensure branch protections are set to the same as main, to ensure all changes follow the same process as for main - for example must go via PRs.

### Builds

It's expected that all Feature branches should have PR verification to ensure submitted code changes in a PR do not break the main build. This is purely a compilation test to check against breakage. Build artifacts are not distributed or saved.

Features could benefit from a 'merge' build which ensures the latest code in the branch works well together.  This build also typically generates: 
 * maven artifacts (to a snapshot repository)
 * docker images (to Docker Hub) 
 
 In future it's expected these will get used for automatic tests, and used by other deployment approaches such as Docker compose and Kubernetes. 

However currently our repository and naming/versioning setup is not able to do this since branch names are not taken into account.

In Egeria we also may perform:
 * Scans for code quality
 * Scans for licensing
 * Security related scans
 * Builds for docker images other than core egeria 

These will also not be done for a feature branch

See https://dev.azure.com/ODPi/Egeria/_build

## Closing a feature branch

When the feature branch is no longer required, it can be deleted by an admin.

Similarly to requesting a feature branch, an issue should be raised, and team agreement sought beforehand. 


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
