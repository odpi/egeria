<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Egeria Content Status

The Egeria Community is constantly innovating in the field of metadata integration and governance.
The code is developed using an agile process.  As such, new code is continuously introduced.
The benefit is that there is plenty of opportunity to feedback and influence the development process.
The downside is that the **master** branch contains code at different stages of development.

The aim of this page is to document how we label the different modules so you can choose what to
consume.  These labels are found in the **README.md** files at the top level of each module.
Basically, a module will go through the phases in this order:

1. In Development
2. Technical Preview (optional)
3. Released
4. Deprecated

The current phase is shown at the top of the page.
The history of the phases that the module has gone through is shown towards the end of the page.

The description of these phases and the way they are labeled is described below.

----
## In Development

![In Development](../images/egeria-content-status-in-development.png)

A subsystem that is **in development** means that the Egeria community is still building the function.
The code is added continuously in small pieces to help the review and socialization process.
It may not run, or do something useful - it only promises not to break other function.
There will be [git issues](https://github.com/odpi/egeria/issues) describing the end state.

----
## Technical Preview

![Technical Preview](../images/egeria-content-status-tech-preview.png)

**Technical Preview** function is in a state that it can be tried.
The development is complete, there is documentation and there are samples, tutorials and
hands-on labs as appropriate.

The community is looking for feedback on the function before we release it.
This feedback may result in changes to the external interfaces.

----
## Released

![Released](../images/egeria-content-status-released.png)

This function is complete and can be used.  The interfaces will be supported until the function
is removed from the project via the deprecation process.  There will be ongoing extensions to
this function, but it will be done to ensure backward compatibility as far as possible.
If there is a need to break backward compatibility, this will be discussed and reviewed in the
community, with a documented timeline.

----
## Deprecated

![Deprecated](../images/egeria-content-status-deprecated.png)

This function has been previously been released.  However we believed there is no one interested in
using, maintaining and developing this function.  It may be removed in the future if there is
consensus in the community.  The README for this module will describe the timeline for this process.
If you see deprecated function that you need, please contact the community and vote for its continued
support.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.