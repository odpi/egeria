<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# 0340 Dictionary

The dictionary model adds some basic term classification used to show how particular terms are used.

![UML](0340-Dictionary.png#pagewidth)

* **ActivityDescription** is a classification used to indicate that the term describes a verb, or an activity.
Most term definitions are nouns, they describe concepts or things.
However, it is useful to be able to define the assets of particular activities in the glossary.
The ActivityDescription classification highlights when a term describes such an activity.
  * OPERATION - describes a function or API call
  * ACTION - describes a governance action that results from evaluating governance rules.
  * TASK - describes a task performed by a person.
  * PROCESS - describes a process, which is a series of steps that are performed in a defined order.
  * PROJECT - describes a type of project.
  * OTHER - describes some other type of activity
* **AbstractConcept** means that the term describes an abstract concept.
* **DataValue** means that the glossary term describes a valid value for a data item.


Return to [Area 3](Area-3-models.md).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.