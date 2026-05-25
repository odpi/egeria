<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

![Stable](../../../images/egeria-content-status-released.png#pagewidth)

# Action Author OMVS

The Action Author Open Metadata View Service (OMVS) provides a REST API to support user interfaces (UIs)
relating to the set up and maintenance of the governance actions needed by your organization.

Governance actions can be a single operation, such as automatically classifying newly catalogued data.
These single operations are called **governance action types**.

Alternatively, a governance action may be a choreographed sequence of actions, where the result of one 
action determines which action(s) run next. The choreographed sequence of actions is called a 
**governance action process**.

## Further information

* [Action Author API Overview](https://egeria-project.org/services/omvs/action-author/overview/)
* [Governance Action Concept](https://egeria-project.org/concepts/governance-action)
* [Governance Action Type Concept](https://egeria-project.org/concepts/governance-action-type)
* [Governance Action Process Concept](https://egeria-project.org/concepts/governance-action-process)

Sample requests for the REST API can be found in `Egeria-api-action-author.http`.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.