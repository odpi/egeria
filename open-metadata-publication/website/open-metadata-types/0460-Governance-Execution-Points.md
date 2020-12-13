<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0460 Governance Execution Points

Governance Execution Points are classifications added to processes
activity that is supporting governance.  They are used by the governance teams to design and review their
governance procedures and processes.

A **ControlPoint** is a place in the processing where a decision needs to be made.  It may be
a choice on whether to tolerate a reported situation or
to resolve it - or it may be a decision on how to solve it.

A **VerificationPoint** describes processing that is testing if
a desired condition is true.  Quality rules are examples of
verification points.  The result of a verification point is the
output of the test.  It may, for example, be a boolean,
classification or a set of invalid values.

An **EnforcementPoint** describes processing that enforces an specific
condition.  For example, data may need to be encrypted at a certain point in the processing.
The encryption processing is an enforcement point.


![UML](0460-Governance-Execution-Points.png#pagewidth)

The governance execution points are often associated with:

* [Governance Action Types](0462-Governance-Action-Types.md)
* [Governance Actions](0463-Governance-Actions.md)
* [Governance Processes](0430-Technical-Controls.md)
* [Governance Procedures](0440-Organizational-Controls.md)

----

Return to [Area 4](Area-4-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.