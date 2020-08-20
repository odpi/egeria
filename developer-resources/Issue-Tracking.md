<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Using Issues for Egeria

Egeria uses [GitHub Issues](https://help.github.com/en/github/managing-your-work-on-github/managing-your-work-with-issues).

Full documentation is provided on the GitHub site. Use these notes to understand how we use issues specifically in Egeria.

## Opening Issues (Anyone)
* Issues can be opened by any GitHub user.
* They may be about a problem getting egeria working, a proposed new feature, a bug, a process change - anything that affects Egeria.
* All PRs should have an associated issue to facilitate discussion
* Try and include a helpful abstract and as many notes as possible about what you see, what you've tried, your environment, any logs, what should happen
  
## Dealing with new issues (Egeria maintainers)
* Assign the issue to someone who can take care of what is reported  - even if not the final owner
* Assign a milestone if it is immediately obvious that the issue relates to capability set out in a release plan, or is needed very soon, otherwise leave blank
* Assign relevant tags to the issue

## Working on issues (Issue Owner)
* Update the issue as soon as possible with an initial response
* When raising a PR, refer to the issue number ie #1234 so that the discussion is clearly linked with the proposed code change. Use 'Fixes #1234' if this PR will completely address the issue
* Keep the milestone realistic if set
* Refer to [Developer Guide](Developer-Guidelines.md) for more guidance
* Regularly review outstanding issues you own & update, reassign, close as needed

## Closing issues
* Issues with PRs marked as `fixes #1234` will close automatically when the PR is merged
* Other issues fixed in other ways should be closed manually
* Any issues open after 60 days with no activity (including assignments) will have a comment added saying they will be closed
* 20 days later the issue will be closed
* If an issue is closed accidentally or prematurely, reopen & add appropriate comments

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
