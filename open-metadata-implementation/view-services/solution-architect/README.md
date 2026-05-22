<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

![Stable](../../../images/egeria-content-status-released.png#pagewidth)

# Solution Architect OMVS

The Solution Architect Open Metadata View Service (OMVS) provides a REST API to support user interfaces (UIs)
relating to the definition and display of solution blueprints and their supporting solution components, 
information supply chains and design patterns.

During the planning phase of a project, architects typically use drawing tools to sketch out the new 
components that are to be developed and how they relate to existing components. 

* **Solution blueprints** are the open metadata equivalent of the sketch and they show the solution 
  components and actors involved and how they collaborate. The advantage of creating a solution blueprint 
  over a sketch diagram is that it is easy to visualize different levels of detail and, as the project rolls out, 
  the implementation of the components can be linked into the blueprint, providing traceability from project 
  intent to actual operation. 
* **Information supply chains** allow the modelling of key data flows needed by your organization. 
  These can then be linked to metadata about the systems and pipelines that implement them, providing a 
  means to summarize statistics from lineage about the operation of the data flows. 
* **Solution components** represent the individual parts of a solution.
* **Design patterns** provide reusable solutions to commonly occurring problems in solution design.

The Solution Architect OMVS supports the definition and display of these elements and their relationships.

## Further information

* [Solution Architect OMVS Overview](https://egeria-project.org/services/omvs/solution-architect/overview/)
* [Solution Blueprint Concept](https://egeria-project.org/concepts/solution-blueprint)
* [Information Supply Chain Concept](https://egeria-project.org/concepts/information-supply-chain)
* [Design Pattern Concept](https://egeria-project.org/concepts/design-pattern)

Sample requests for the REST API can be found in `Egeria-api-solution-architect.http`.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.