<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Creating a branch for your work

Once you have a [clone of the git repository](task-creating-a-fork-and-clone.md)
there is one more step before you can begin developing. 

Most development work is done on your local clone with occasional calls to GitHub to
synchronize with its versions of the repository.

![Figure 1](../../../developer-resources/tools/git-development.png)
> **Figure 1:** Local development process using git

The top level branch (copy) of a git repository is called `master`.
It is recommended not to make any changes directly to master
but just to use it for reference and as a basis for branching.

It is also recommended to create a new branch for each distinct set of changes. This keeps the commit history pushed
to Egeria clean, and makes reviews of the code much easier for the maintainers.
Since GitHub manages the introduction of new content on a branch basis it is also a way to be able to easily rework, by updating a
change later before it has been incorporated back into the main Egeria code.

First update your copy of master in your clone and push to your GitHub fork.

```bash
$ git checkout master
$ git pull upstream master
$ git push
```

Once you have the latest code on your machine, create a branch for your changes. The
commands below set up a branch called "example-branch" based off of master, and also push
it back to your personal fork:

```bash
$ git checkout -b example-branch master
$ git push origin example-branch

```

When you create your own branch, use a name that describes that the branch is for as you will
use it for all changes you are collecting together to push as one group to Egeria.
This name will be publicly visible too once you start pushing your changes
to Egeria so keep it clean :).

You can also see that 'origin' will point to your GitHub fork, whilst 'upstream' points to the Egeria master branch:

```bash
$ git remote -v

origin https://github.com/USER/egeria (fetch)
origin https://github.com/USER/egeria (push)
upstream https://github.com/odpi/egeria.git (fetch)
upstream https://github.com/odpi/egeria.git (push)

```

Now you can make your changes to the content.  

----
* Return to [Git and GitHub Tutorial](.)
* Return to [Egeria Dojo - Making a contribution step by step](../egeria-dojo/egeria-dojo-day-2-3-contribution-to-egeria.md)


* Link to Git/GitHub overview in [developer-resources/tools](../../../developer-resources/tools/Git-GitHub.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.