<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0460 Governance Execution Points

A **governance execution point** defines specific activity that is supporting governance.

There are three types:

* A **Control Point** is a place in the processing where a decision needs to be made.  It may be
a choice on whether to tolerate a reported situation or
to resolve it - or it may be a decision on how to solve it.

* A **Verification Point** describes processing that is testing if
a desired condition is true.  Quality rules are examples of
verification points.  The result of a verification point is the
output of the test.  It may, for example, be a boolean,
classification or a set of invalid values.

* An **Enforcement Point** describes processing that enforces an specific
condition.  For example, data may need to be encrypted at a certain point in the processing.
The encryption processing is an enforcement point.

The **ExecutionPointDefinition** elements are created during the design of the
governance program.  They characterize the types of execution points that are needed to support the governance
requirements.  They are linked to the [Governance Definition](0401-Governance-Definitions.md)
that they support using the **ExecutionPointUse** relationship.
Typically the governance definitions linked to the governance execution point definitions are:

* [Governance Processes](0430-Technical-Controls.md)
* [Governance Procedures](0440-Organizational-Controls.md)

Often execution points need to be integrated with the normal activity of the
business, but they may also represent additional standalone activity.

The classifications **ControlPoint**, **VerificationPoint** and
**EnforcementPoint** are used to label governance implementation elements
with the type of execution point and the qualified name of the corresponding definition
if any.
They are often found on element such as:

* [Governance Action Types](0462-Governance-Action-Types.md)
* [Governance Actions](0463-Governance-Actions.md)

These classifications help in the review of the implementation of the
governance program and can be used to drive additional audit logging. 

![UML](0460-Governance-Execution-Points.png#pagewidth)



Return to [Area 4](Area-4-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.