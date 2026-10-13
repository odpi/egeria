<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Anchor Support Functional Verification Tests (anchor-fvt)

The anchor-fvt module tests **anchor support**: the `Anchors` classification that ties the elements of an
anchored graph to their anchor, and the operations that act on a whole graph - cascaded delete, archive,
purge, templated create and restore - together with undo, which acts on one element only.

An element's `anchorGUID` is the anchor it was created with. Most members of a graph name its root, so
the root's members are found by a single query on the classification. An element can also be anchored to
another member, such as an endpoint anchored to its connection, and those are found by asking about that
member.

## What it checks

Each test builds the same small graph: a catalogued database schema with its tables, columns, connection
and endpoint, plus a glossary term with an anchor of its own. It then checks exactly which elements an
operation takes.

```
databaseSchema (anchor) --Schema--> schemaType --AttributeForSchema--> table1, table2
table1 --NestedSchemaAttribute--> column1a, column1b
table2 --NestedSchemaAttribute--> column2a
column1a --ForeignKey--> column2a                 ASSOCIATION
table1 --LineageMapping--> table2                 DESIGN_LINEAGE, so a delete becomes an archive
databaseSchema --ResourceConnection--> connection --ConnectToEndpoint--> endpoint (anchored to connection)
table1 --SemanticAssignment--> glossaryTerm       its own anchor
```

* **Deleting or archiving the anchor** takes the whole graph, including the endpoint, and leaves the
  glossary term. An archive classifies every element as a `Memento` and gives each qualified name the same
  `_archivedOn_` suffix, not just the anchor's. A purge leaves nothing behind, even to a search that
  includes deleted elements.
* **Deleting or archiving an anchored member** takes the member, the elements at end 2 of its STRUCTURAL
  and ATTACHMENT relationships (see
  [relationship purpose](https://egeria-project.org/concepts/relationship-purpose/)), and anything anchored
  to it. It does not take its siblings, its anchor, or anything it is linked to for another purpose. For
  example, deleting `table1` takes its columns but not `table2` or `column2a`, even when `table1` also has
  an `AssignmentScope` to `table2`.
  This includes the default `LOOK_FOR_LINEAGE` delete, which becomes an archive because `table1` has
  lineage.
* **Refusals change nothing.** A database schema with tables cannot be deleted without cascade, and the
  refusal leaves every element in place.
* **More than one page of members.** An anchor with more members than one page holds is still deleted
  completely.
* **Type-specific cascades.** A host takes the platforms deployed on it, and a collection takes its
  members, but only when cascade is requested.
* **Templated create from the anchor.** Copying the database schema copies its whole graph, including the
  endpoint. Each copy is named from the `~{copyName}~` placeholder that the template carries in every
  qualified name. The copies are anchored the way the template is: the endpoint to the copied connection,
  everything else to the new database schema. The foreign key and lineage mapping join the copies, the
  glossary term is linked rather than copied, and the copy is `SourcedFrom` the template. Deleting or
  archiving either the copy or the template leaves the other untouched.
* **Templated create from a member.** Copying `table1` into another database schema copies the table and its
  columns, and nothing else. The copies are anchored to that database schema and attached under its schema
  type. The table keeps its link to the glossary term, which is outside the template's graph, but drops its
  links into the template's graph: its schema type, the foreign key's target column and the lineage
  mapping's target table. Deleting that database schema takes the added table with it.
* **Restore.** Each command that writes many instances (a delete, an archive, a templated create) is one
  operation. Every instance it writes carries the same `lastOperationId`, and restore uses that id to reverse
  exactly what the command changed. Restoring a database schema deleted with cascade brings back every element
  and every link. Restore can be asked for on any element the delete removed. An element removed by a separate,
  earlier delete stays deleted, and so does a link to an element outside the graph that has since been deleted.
  Restoring after an archive removes the Memento classifications and the `_archivedOn_` suffixes. A purged or
  active element can't be restored, and a deleted relationship can be restored on its own.
* **Undo.** Undoing the last update to an element or relationship puts its previous version back, and touches
  nothing else in the graph. An element that has never been updated has nothing to undo.
* **Recording and scoped search.** Each element records the anchor it was created with. Anchor-scoped
  searches return the anchor's direct members only.

The suite's own unit-level counterpart is `AnchoredElementCascadeTest` in
[generic-handlers](../../../open-metadata-implementation/common-services/generic-handlers). It also covers an
element anchored to a member that sits at end 1 of its link to that member, which no natural relational
structure produces.

## Running it

```bash
./gradlew :open-metadata-test:open-metadata-fvt:anchor-fvt:test -PrunAnchorFvt
```

```bash
./gradlew :open-metadata-test:open-metadata-fvt:anchor-fvt:test -PrunAnchorFvtInMemory
```

`-PrunAnchorFvt` uses the PostgreSQL repository and `-PrunAnchorFvtInMemory` the in-memory one. Run both: an
anchor's delete finds its members with a query on the `Anchors` classification, and the two repositories
answer that query in different ways. Only the types archive is loaded, so startup takes seconds. A full run
takes a few minutes on PostgreSQL and about 20 seconds in memory.

The PostgreSQL connection is set in `src/test/resources/application.properties`. The default is the
`egeria-shared-postgres` container on port 5442, with its repository in schema
`repository_anchorFvtMetadataStore`. Every element the suite creates has a qualified name starting
`anchor-fvt:`. Anything left behind by an interrupted run is purged before the next run starts.
