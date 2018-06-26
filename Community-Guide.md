<!-- SPDX-License-Identifier: Apache-2.0 -->

# Egeria Community Guide

This project welcomes contributors from any organization or background, provided they are
willing to follow the simple processes outlined below, as well as adhere to the 
[Code of Conduct](https://github.com/odpi/specs/wiki/ODPi-Code-of-Conduct).


## Steps to contribute

* Review the [developer guidelines](developer-resources/Developer-Guidelines.md)
* Open an issue on JIRA to cover the proposed change (see "Getting a Jira account and submitting an issue")
* Clone the repository and prepare your contribution in a new git branch (see "Using git to prepare a contribution").
* As you commit your changes, make sure they are signed (see "Why the DCO?")
* Issue a git pull request from [GitHub](https://github.com/odpi/egeria) to initiate the review.
* Wait for a maintainer to review and approve the commit.

## Important Additional setup for Windows Users
If using git on windows, it is **essential** to configure long pathname support, since otherwise the git clone, and other
git operations on egeria **will** fail.

Ensure you have a current version of git installed (for example 2.17 or above), and in an Elevated command prompt run:
```
git config --system core.longpaths true
```
For more detail see [this article](https://github.com/msysgit/msysgit/wiki/Git-cannot-create-a-file-or-directory-with-a-long-path)

This is not required on MacOS or Linux.

## Using git to prepare a contribution

The egeria content is located in git at https://github.com/odpi/egeria . 

Whilst project maintainers are able to directly work with the egeria repository, as an open source project we recommend use of the github [fork and pull model](https://help.github.com/articles/about-collaborative-development-models/) which will support contributions from anyone without requiring direct write access to the egeria repository.


### Creating a fork
If you are
intending to contribute code rather than browse, the easiest way to prepare a contribution is to start off by
creating a [fork](https://guides.github.com/activities/forking/) of the egeria repository. 

This can be done by navigating the the egeria URL above, and logging into the github UI with a registered account. You will then see a 'Fork' button top right, and should click this to create 
your own fork to work with egeria. 

This needs doing only once.
### Creating a local copy of the code

To create a copy of the code on your machine enter the following in a new directory. Replace USER with your github userid.

```
$ git clone https://github.com/USER/egeria.git
$ cd egeria
```

The git clone command creates a new directory called <code>egeria</code> containing the egeria content.

It's recommended not to make any changes to master, other than pulling from upstream (the master egeria project) but just to use for reference and as a basis for branching

You should also set the upstream repository to connect your fork to the main egeria repository
```
git remote add upstream https://github.com/odpi/egeria.git
```



### Creating a branch for your work

Always create a branch for each distinct set of changes. This keeps the commit history pushed to egeria clean, and makes reviews of the code much easier. Since github manages pull requests on a branch basis it's also a way to be able to easily rework, update a change later before it's been incorporated back into the main egeria code.

First update your copy of master and push to your github fork
```
git checkout master
git pull upstream master
git push
```

Once you have the latest code on your machine, create a branch for your changes. The commands below set up a branch called "example-branch" based off of master, and also push it back to your personal fork:

```
$ git checkout -b example-branch master
$ git push origin example-branch
```
When you create your own branch, use a name that describes that the branch is for as you'll use it for all changes you are collecting together to push as one group to egeria

You can also see that 'origin' will point to your github fork, whilst 'upstream' points to the egeria master branch
```
10:55 $ git remote -v
origin https://github.com/USER/egeria (fetch)
origin https://github.com/USER/egeria (push) upstream https://github.com/odpi/egeria.git (fetch) upstream https://github.com/odpi/egeria.git (push)
```


### Working with the egeria code in your personal environment

Now you can make your changes to the content.  

Once this is complete,
issue the <code>git status</code> to verify all of your changes are
included.  This will list any files that have been changed, but not included
for git control.  Use <code>git add</code> to add all of the files you want included.


For example:

```
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

You can now commit changes to your local repository, making sure to use the <code>-s</code> option to sign your changes (see below) and the <code>-m</code> option to provide a useful commit message. In the message you can make use of [special strings](https://blog.github.com/2011-10-12-introducing-issue-mentions/)to directly link to github issues. If this is done others following the issue will see the commits to your fork easily so can track the work going on even before you submit to the egeria repository

It's also essential to push the changes from your local machine up to github ready for the next step
```
$ git commit -s -m 'Best code change ever as per Issue #1433'
$ git push 

```

If you think there is ongoing work in a similar area to that of your changes, you may find it useful to pull
the latest master code prior to completing your changes ie
```
git pull upstream master
```
and then making any necessary changes to merge conflicts, and commit/push as above.

### Creating a pull request
The easiest way to create a pull request is by navigating to your local fork of the egeria repository ie https://github.com/<USER>/egeria, selecting your working branch,  and clicking on 'pull request'. Add an explanation and links to any JIRA or GitHub Issues, and then submit to the egeria maintainers for review & inclusion in the code. 

Further changes can be done using the same branch, and will get added to the same pull request
automatically.

### Cleaning up

Once all work has been completed, including changes appearing in master, only then can your temporary branch be deleted
```
git branch -d example-branch
git push -d origin example-branch
```
You may need to use <code>-D</code> if not all changes are merged, but check carefully!

## Getting a JIRA account and creating an issue

1. If you haven't already, create a [Linux Foundation account](https://identity.linuxfoundation.org). 
Note the username and password you selected.
2. Login to the [ODPi JIRA](https://jira.odpi.org/projects/EGERIA/issues/) with the Linux Foundation username and password you selected above.
3. In the menu bar, click 'Create'. The Create dialog will come up
4. The screen that comes up, you'll need to fill out the following items:
 * Project: 'Egeria' ( should be the default option )
 * Issue Type: Select 'Improvement' or 'Bug' as appropriate ( don't worry if you get this wrong )
 * Summary: One line subject of the feedback or issue seen
 * Description: Detailed explanation of the feedback or concerns seen with the spec.
 
 
 # Why the DCO?
 
 We have tried to make it as easy as possible to make contributions. 
 This applies to how we handle the legal aspects of contribution.
 
 We simply ask that when submitting a patch for review,
 the developer must include a sign-off statement in the commit message.
 This is the same approach that the
 [Linux® Kernel community](http://elinux.org/Developer_Certificate_Of_Origin)
 uses to manage code contributions.
 
 Here is an example Signed-off-by line, which indicates that the submitter accepts the DCO:
 
 <code>Signed-off-by: John Doe <john.doe@hisdomain.com></code>
 
 You can include this automatically when you commit a change
 to your local git repository using
 
 <code>git commit -s</code>.
 
 By signing your work, you are confirming that the origin of the content
 makes it suitable to add to this project.  See
 [Developer Certificate of Origin (DCO)](https://developercertificate.org/).
