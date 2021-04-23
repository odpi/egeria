<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Reviewing code changes

If you are asked to review a code change it will be located
in a Pull Request (PR) on one of Egeria's git repositories 
[(link to main repository as an example)](https://github.com/odpi/egeria/pulls).

Within the pull requests are a number of commits that describe the changes to particular
files that will be made when the pull request is merged into
the repository.

As a reviewer, you need to look at the code changes and
satisfy yourself that:

* The code change is neat and readable and follows the code style of the rest of the module.
* The logic is clear and there are comments if the logic is complex.
* The code does not have any obvious defects - such as likely to cause a null pointer exception.
* There are no uses of `log.error()` for logging errors that are not accompanied by an equivalent
message to the audit log.
* If new dependencies have been added - these are documented in the developer resources.
* If changes to the types have been made, these changes are:
  * only made to the current release's types (that is, in `OpenMetadataTypesArchive.java`).
  It is permissible to correct typos in the other files but not change the shape of the types
  in the types created in previous releases (that is files called `OpenMetadataTypesArchiveX_X.java`.
  *  documented in
[UML diagrams in the drawio files](../open-metadata-publication/website/open-metadata-types) and the
diagram has been exported as an image.

If you are also the code owner of the changed code then you also need to be sure that the changes are consistent
with the current and intended future design of the module.

----

* Return to [Developer Resources](.)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.