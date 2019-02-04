<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Egeria Community Guide

This project welcomes contributors from any organization or background, provided they are
willing to follow the simple processes outlined below, as well as adhere to the 
[Code of Conduct](https://github.com/odpi/specs/wiki/ODPi-Code-of-Conduct).

## Steps to contribute

1. Review the [developer guidelines](developer-resources/Developer-Guidelines.md).
1. Open an issue on [GitHub](https://github.com/) to cover the proposed change (you may need to create an account on GitHub first).
1. Clone the repository and prepare your contribution in a new git branch (see [Using git to prepare a contribution](#using-git-to-prepare-a-contribution)).
1. As you commit your changes, make sure they are signed (see [Why the DCO?](#why-the-dco)).
1. Issue a [git pull request](#creating-a-pull-request) from [GitHub](https://github.com/odpi/egeria) to initiate the review.
1. Wait for a maintainer to review and approve the commit.

## Important additional setup for Windows users

If using git on windows, it is **essential** to configure long pathname support, since 
otherwise the git clone, and other git operations on Egeria **will** fail.

Ensure you have a current version of git installed (for example 2.17 or above), and in an Elevated command prompt run:

```posh
git config --system core.longpaths true
```

For more detail see [this article](https://github.com/msysgit/msysgit/wiki/Git-cannot-create-a-file-or-directory-with-a-long-path).

This is not required on MacOS or Linux.

## Creating a Linux Foundation account and creating an issue

The Linux Foundation provide build and distribution facilities.
You need an account to access some of the reports from the build.

This is the link to create a [Linux Foundation account](https://identity.linuxfoundation.org). 
Note the username and password you selected.

## Using git to prepare a contribution

The Egeria content is located in git at https://github.com/odpi/egeria.

Whilst project maintainers are able to directly work with the Egeria repository, as an open source project we recommend use of the [GitHub's fork and pull model](https://help.github.com/articles/about-collaborative-development-models/), which will support contributions from anyone without requiring direct write access to the Egeria repository.

### Creating a fork

If you are intending to contribute code rather than browse, the easiest way to prepare 
a contribution is to start off by
creating a [fork](https://guides.github.com/activities/forking/) of the Egeria repository.

This can be done by navigating the the Egeria URL above, and logging into the github UI with a 
registered account. You will then see a 'Fork' button at the top right, and should click this to 
create your own fork to work with Egeria. 

This needs doing only once.

### Creating a local copy of the code

To create a copy of the code on your machine enter the following in a new directory. Replace `USER` with your GitHub userId.

```bash
$ git clone https://github.com/USER/egeria.git
$ cd egeria
```

The git clone command creates a new directory called `egeria` containing the Egeria content.

It is recommended not to make any changes to master, other than pulling from upstream (the master Egeria project)
but just to use for reference and as a basis for branching.

You should also set the upstream repository to connect your fork to the main Egeria repository:

```bash
$ git remote add upstream https://github.com/odpi/egeria.git
```

### Creating a branch for your work

Always create a branch for each distinct set of changes. This keeps the commit history pushed
to Egeria clean, and makes reviews of the code much easier. Since GitHub manages pull
requests on a branch basis it is also a way to be able to easily rework, by updating a
change later before it has been incorporated back into the main Egeria code.

First update your copy of master and push to your github fork

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

You can also see that 'origin' will point to your GitHub fork, whilst 'upstream' points to the Egeria master branch:

```bash
$ git remote -v

origin https://github.com/USER/egeria (fetch)
origin https://github.com/USER/egeria (push)
upstream https://github.com/odpi/egeria.git (fetch)
upstream https://github.com/odpi/egeria.git (push)

```

### Working with the Egeria code in your personal environment

Now you can make your changes to the content.  

Once this is complete,
issue the `git status` command to verify all of your changes are
included.  This will list any files that have been changed, but not included
for git control.  Use `git add` to add all of the files you want included.

For example:

```bash
$ git status

On branch example-branch
Untracked files:
  (use "git add <file>..." to include in what will be committed)

	new-file.java

$ git add new-file.java
$ git status

On branch example-branch
Changes to be committed:
  (use "git reset HEAD <file>..." to unstage)

	new file:   new-file.java
```

You can now commit changes to your local repository, making sure 
to use the `-s` option to sign your changes (see [Why the DCO?](#why-the-dco))
and the `-m` option to provide a useful commit message. In the message you can
make use of [special strings](https://blog.github.com/2011-10-12-introducing-issue-mentions/) to
directly link to GitHub
issues. By doing this others following the issue will see the commits to your fork
easily so can track the work going on even before you submit to the **egeria** repository.

It is also essential to push the changes from your local machine up to GitHub ready for the next step - note this references GitHub issues, also read [the section on JIRA](#creating-a-linux-foundation-account-and-creating-an-issue):

```bash
$ git commit -s -m 'Best code change ever as per Issue #1433'
$ git push 
```

If you think there is ongoing work in a similar area to that of your changes, you may find it useful to pull
the latest master code prior to completing your changes ie

```bash
$ git pull upstream master
```

and then making any necessary changes to merge conflicts, and commit/push as above.

### Creating a pull request

The easiest way to create a pull request is by navigating to your local fork of the Egeria repository eg. `https://github.com/USER/egeria`, selecting your working branch, and clicking on 'pull request'. Add an explanation and links to any JIRA or GitHub Issues, and then submit to the Egeria maintainers for review and inclusion in the code.

Further changes can be done using the same branch, and will be added to the same pull request
automatically.

### Cleaning up

Once all work has been completed, including changes appearing in master, only then can your temporary branch be deleted:

```bash
$ git branch -d example-branch
$ git push -d origin example-branch
```

You may need to use `-D` if not all changes are merged, but check carefully!

### Additional git tips

If you are working locally, and realise you have accidentally been making changes on master instead of another branch:

```bash
$ git stash
$ git checkout -b correct-branch
$ git stash pop
```

Messed up your master branch?

```bash
$ git checkout master
$ git fetch upstream
$ git reset --hard HEAD
$ git push
```

Correct your last commit

```bash
$ git commit --amend -s -m "New commit message"
```

View recent changes

```bash
$ git log
```

View recent changes in a prettier way:

```bash
$ git log --pretty=format:"%h %ad | %s%d [%an]" --graph --date=short
```

Take a fix you have pushed to a different branch (perhaps a top-level pom change, or something else you need) and apply it to your current branch:

```bash
$ git cherry-pick <commit-id>
```

# Why the DCO?

We have tried to make it as easy as possible to make contributions. 
This applies to how we handle the legal aspects of contribution.

We simply ask that when submitting a patch for review,
the developer must include a sign-off statement in the commit message.
This is the same approach that the
[Linux® Kernel community](http://elinux.org/Developer_Certificate_Of_Origin)
uses to manage code contributions.

Here is an example Signed-off-by line, which indicates that the submitter accepts the DCO:

```
Signed-off-by: John Doe <john.doe@hisdomain.com>
```

You can include this automatically when you commit a change
to your local git repository using

```bash
$ git commit -s
```

By signing your work, you are confirming that the origin of the content
makes it suitable to add to this project.  See
[Developer Certificate of Origin (DCO)](https://developercertificate.org/).



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
