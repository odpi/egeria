<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Get lineage graph

Several operations are available for querying lineage. This can be achieved by setting the scope parameter as described in the method below.

* Get the end to end lineage graph for an entity
* Get the ultimate source/destination lineage graph for an entity
* Get the vertical lineage graph for an entity

 ```
   /**
       * Returns the graph that the user will initially see when querying lineage. In the future, this method will be
       * extended to condense large paths to prevent cluttering of the users screen. The user will be able to extended
       * the condensed path by querying a different method.
       *
       * @param userId calling user.
       * @param scope ULTIMATE_SOURCE, ULTIMATE_DESTINATION, SOURCE_AND_DESTINATION, VERTICAL, END_TO_END.
       * @param guid The guid of the node of which the lineage is queried of.
       * @param displaynameMustContain Used to filter out nodes which displayname does not contain this value.
       * @param includeProcesses  Will filter out all processes and subprocesses from the response if false.
       * @return A subgraph containing all relevant paths, in graphSON format.
       * @throws InvalidParameterException one of the parameters is null or invalid
       */
      LineageVerticesAndEdges lineage(String userId, Scope scope, String guid, String displaynameMustContain, boolean includeProcesses)
              throws org.odpi.openmetadata.frameworks.connectors.ffdc.InvalidParameterException, PropertyServerException, OpenLineageException;
  }
 ```
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.