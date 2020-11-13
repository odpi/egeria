<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Set long path names (Windows only)

If using git on windows, it is **essential** to configure long pathname support, since 
otherwise the git clone, and other git operations on Egeria **will** fail.

Ensure you have a current version of git installed (for example 2.17 or above), and in an Elevated command prompt run:

```posh
git config --system core.longpaths true
```

For more detail see [this article](https://github.com/msysgit/msysgit/wiki/Git-cannot-create-a-file-or-directory-with-a-long-path).

This is not required on MacOS or Linux.

----
* Return to [Git and GitHub Tutorial](.)
* Return to [Egeria Dojo - Making a contribution step by step](../egeria-dojo/egeria-dojo-day-2-3-contribution-to-egeria.md)


* Link to Git/GitHub overview in [developer-resources/tools](../../../developer-resources/tools/Git-GitHub.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.