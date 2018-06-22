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
* Issue a git pull request from [GitHub](https://github.com/odpi/egeria)to initiate the review.
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

The egeria content is located in git at https://github.com/odpi/egeria.
To create a copy of the code on your machine enter the following in a new directory.

```
$ git clone https://github.com/odpi/egeria.git
$ cd egeria
```

The git clone command creates a new directory called <code>egeria</code> containing the egeria content.

Alternatively, if you already have the egeria content on your machine,
you can use the following command from the top-level egeria directory to ensure you have an up-to-date copy of everything.

```
$ git pull origin master
```

The git pull request does a <code>git fetch</code> followed by a <code>git merge</code>.

Once you have the latest code on your machine, create a branch for your changes and push it back to the
egeria repository.  The commands below set up a branch called "example-branch" based off of master:

```
$ git checkout -b example-branch master
$ git push origin example-branch
```

When you create your own branch, use a name that describes that the branch is for.

Now you can add your changes to the content.  Once this is complete,
issue the <code>git status</code> to verify all of your changes are
included.  This will list any files that have been changed, but not included
to the patch.  Use <code>git add</code> to add all of the files you want included
and then use <code>git commit -s</code> to commit the changes.  Finally,
<code>git push</code> will push the code to the ODPi repository.

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

$ git commit -s
$ git push --set-upstream origin example-branch
```


The easiest way to create a pull request is from the [GitHub](https://github.com/odpi/egeria)
browser interface.  


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
