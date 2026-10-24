<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Darwin - maintaining lineage across the layers

**Status:** design, agreed 24 Oct 2026.  All five steps of the implementation order are implemented.

This document describes the next role of the Darwin Product Dependency Manager: validating, maintaining and
completing the lineage relationships at every layer of the open metadata landscape, using the evidence in the layers
above and below.  It replaces the fixed three-step derivation described in the [README](../README.md#darwin-product-dependency-manager)
with one engine applied at every boundary between layers.

## The layers

Lineage is built up in layers.  Each layer has its own elements and its own lineage relationships, and is linked to the
layer beneath it by relationships that say which lower-level elements each upper-level element is made of.

| Layer | Elements | Lineage within the layer | Linked to the layer below by |
|---|---|---|---|
| **Data mesh** | `DigitalProduct` | `DigitalProductDependency` | `CollectionMembership` (product at end 1, member assets at end 2) |
| **System fabric** | `ITInfrastructure` (software servers, hosts, ...) | `DataFlow`, `ControlFlow`, `ProcessCall` | `SupportedSoftwareCapability` (infrastructure → `SoftwareCapability`), then `CapabilityAssetUse` (capability → asset) |
| **Data fabric** | `DataAsset`, `Process` | `DataFlow`, `ControlFlow`, `ProcessCall`; `DataSetContent` (a data set is a view over other assets) | the asset's schema - schema attributes anchored to the asset |
| **Schema detail** | schema attributes (tables, columns, ...) | `DataMapping` between schema attributes, and between schema attributes and processes | - |

The data mesh and the system fabric both sit on the data fabric: a digital product is made of data fabric assets, and
so is the work of a piece of infrastructure.  There is no direct link between the data mesh and the system fabric.

There are therefore three **boundaries**:

* schema detail ↔ data fabric
* data fabric ↔ system fabric
* data fabric ↔ data mesh

### Where lineage relationships come from

A lineage relationship is either **asserted** - created through the open metadata APIs by a person or a tool - or
**derived** from lineage traces, such as the OpenLineage events catalogued by the Open Lineage Cataloguer.  Some carry
an `iscQualifiedName` naming the [information supply chain](https://egeria-project.org/concepts/information-supply-chain/)
they belong to and some do not.

To Darwin, every relationship it did not create itself is *external*, whatever its origin.  Darwin recognizes its own
relationships by the `createdBy` in their header, as it does today.

A relationship Darwin created that anyone else has since updated (its `updatedBy` is not Darwin's) has been
**adopted** (agreed 24 Oct 2026).  It is external from then on: Darwin no longer updates or removes it, and validates
it against the layer beneath like any other external relationship - so a person's edit is never lost when the lineage
it was derived from changes, and an adopted relationship that the lineage no longer proves is reported as unproven.
Darwin's own Exception relationships are not adopted this way, so a steward can annotate an exception without
stopping Darwin from maintaining it.

## What Darwin does at each boundary

Each boundary has an **owner function** that maps an element of the lower layer to the element(s) of the upper layer
it belongs to:

| Boundary | Owner of a lower-layer element |
|---|---|
| schema detail → data fabric | a schema attribute: the data asset it is anchored to.  A process: itself (a `DataMapping` may end at a process). |
| data fabric → system fabric | the infrastructure that supports a `SoftwareCapability` linked to the element by `CapabilityAssetUse` - with a `useType` of `OWNS` for a data asset, and any `useType` for a process |
| data fabric → data mesh | the digital products that the element is a member of |

A **lineage path** in the lower layer between elements with different owners **proves** a relationship between the
owners in the upper layer.  The relationship type carries across the boundary:

| Path in the lower layer | Proves in the upper layer |
|---|---|
| `DataMapping` (schema detail) | `DataFlow` (data fabric) |
| `DataFlow`, `ProcessCall` and other `DataLineageRelationship` subtypes (data fabric) | `DataFlow` or `ProcessCall` (system fabric) - the type of the relationship that crosses between owners; `DigitalProductDependency` (data mesh) |
| `ControlFlow` (data fabric) | `ControlFlow` (system fabric) |

`ControlFlow` does not prove a `DigitalProductDependency`: a product depends on another product's data, not on the
order in which their processes run.

At each boundary Darwin does three things.

### 1. Add the missing relationships (upwards)

Every relationship proved from below that is not already in the upper layer is created by Darwin.  When the lineage
beneath no longer proves a relationship Darwin created, Darwin removes it.  External relationships are never removed.

This is what Darwin does today for `DataMapping` → asset `DataFlow`, asset lineage → server `DataFlow` and asset
lineage → `DigitalProductDependency`.  The new design extends it to every relationship type in the table above, to
processes as well as data assets, and to `ControlFlow`.

### 2. Validate the asserted relationships (downwards)

An external relationship in the upper layer that nothing in the layer below proves is **unproven**.  Darwin records
it with an `Exception` relationship whose `affectedRelationships` property lists the unproven relationships, as it does
for `DigitalProductDependency` today.

The absence of detail is not evidence against a relationship.  A relationship is only checked when the layer below is
**populated** for both of its ends; otherwise it is *unchecked*, and neither proved nor unproven:

| Upper-layer relationship | Checked when |
|---|---|
| data fabric `DataFlow` | each end has schema detail that takes part in a `DataMapping` - a data asset has at least one schema attribute that is at either end of a `DataMapping`; a process is at either end of a `DataMapping` |
| data fabric `ControlFlow`, `ProcessCall` | never - the schema detail cannot prove them |
| system fabric `DataFlow`, `ControlFlow`, `ProcessCall` | each end supports at least one capability that uses a data fabric element (by the rules of the owner function) |
| `DigitalProductDependency` | each product has at least one asset among its members |

The last row changes today's behaviour slightly: a dependency on a product that has no asset members is no longer
reported as unproven.

Each relationship type has its own exception type, created on first use: `ExceptionType::UnprovenDataFlow`,
`ExceptionType::UnprovenControlFlow`, `ExceptionType::UnprovenProcessCall`, and the existing
`ExceptionType::UnprovenDigitalProductDependency`.  The `Exception` relationship links the element that depends on the
unproven relationships - the consumer of the data or the receiver of the control (end 2 of a `DataFlow`, `ControlFlow`
or `ProcessCall`; end 1, the dependent product, of a `DigitalProductDependency`) - to the exception type.  As today, the
exception is updated as its list changes and removed once the list is empty.

### 3. Establish the information supply chain

All the lineage relationship types are multi-link: there is one relationship per information supply chain between the
same two elements.  Darwin establishes the `iscQualifiedName` in three ways.

* **Upwards.**  A relationship that Darwin derives takes the supply chain of the path that proved it (as today).
* **Within a path.**  An untagged relationship is treated as *supply chain unknown*, not as *no supply chain*.  A path
  keeps to one supply chain: the first tagged relationship on it fixes the supply chain for the whole path, untagged
  relationships may appear anywhere along it, and a relationship tagged with a different supply chain cannot join it.
  A path that is untagged throughout has an unknown supply chain, and proves an untagged relationship above, as today.
* **Downwards.**  An external relationship with a supply chain is proved by a path with the same supply chain or an
  unknown one.  When a path's supply chain is known - from a tagged relationship on the path, or from the external
  relationship it proves - every untagged relationship on the path is given that supply chain.

An untagged relationship is given a supply chain as follows:

* **An external relationship that does not name a supply chain** is filled in with it, on behalf of its home metadata
  collection when it is not local, as Darwin does for `DigitalProductDependency` today.  Darwin only fills in a blank;
  it never overwrites a supply chain.  This applies at every layer.
* **When an untagged relationship lies on paths for more than one supply chain**, the original is left untagged and
  Darwin creates a copy of it for each supply chain.  The copies are Darwin's own relationships and are removed when
  the paths that justify them go.

The untagged original is not filled in once it is known to be ambiguous: the first supply chain found is not more
correct than the others.

## Data sets as views

A `DataSetContent` relationship (data set at end 1, content at end 2) says that the data set is a view over the
content - it is not a flow of data.  Darwin treats the data set and its content as **equivalent** when following
lineage:

* A walk that starts from a data set also starts from each of its content elements.
* A walk that reaches a content element also reaches the data sets over it, so it can stop at a product whose member is
  the data set.

This matters for products: their members are often the abstract data sets (such as a `TabularDataSet`), while the
lineage from traces is attached to the physical elements beneath them.  The Open Lineage Cataloguer deliberately links
the abstractions above the physical elements with `DataSetContent` rather than giving them lineage of their own.

`DataSetContent` also carries an `iscQualifiedName`.  A data set that is a view for one supply chain is only equivalent
to its content on paths for that supply chain (or on paths whose supply chain is unknown).

## Scope

* **Adjacent layers only.**  Darwin derives relationships across a boundary, never shortcuts within one layer: a
  `DataFlow` A → P → B in the data fabric does not make Darwin create a `DataFlow` A → B.
* **One exception: tables and processes.**  The walks from the data fabric to the system fabric and the data mesh also
  follow the `DataMapping` relationships that run between a relational table and a process (agreed 24 Oct 2026).  A
  data set is often a view over one table (`DataSetContent` to the `RelationalTable`), and the table's lineage to
  and from its processes is recorded at the level of the table - the Open Lineage Cataloguer links the table and the
  process with `DataMapping`, and the asset the table belongs to with `DataFlow`.  Following only the asset-level
  lineage would mean treating the data set as a view over every table of the asset.  No relationship is derived
  from these mappings within the data fabric; they only extend the paths.
* **Information supply chain definitions** - `InformationSupplyChain`, its segments, and the `ImplementedBy`
  relationships to the elements that implement them - are another source of supply chain names.  They are out of scope
  for this round.

## The refresh

A refresh works upwards and then downwards, writing each layer before the next is read:

1. **schema detail → data fabric** - derive and reconcile the data fabric `DataFlow`s.
2. **data fabric → system fabric** - derive and reconcile the system fabric `DataFlow`, `ControlFlow` and `ProcessCall`
   relationships.
3. **data fabric → data mesh** - derive and reconcile the `DigitalProductDependency` relationships.
4. **Validation** - check every external relationship in each upper layer against the layer below, and maintain the
   exceptions.
5. **Supply chain** - fill in or copy the untagged relationships on the paths whose supply chain is known.

Writing each layer before reading the next replaces today's hand-over of the asset-level data flows "on their way to the
repository".  It costs some extra retrievals and keeps the levels independent.

Steps 4 and 5 use the paths found in steps 1 to 3.  The walk records, for each element it reaches, the relationship it
arrived by, so that the path that proved a relationship can be read back.

### Reconciliation

Each derivation rule is reconciled on its own, against Darwin's own relationships for that rule only.  Today the asset
and server data flows are reconciled together because both are `DataFlow` relationships created by Darwin.  The new
rules overlap more (several rules create `DataFlow`, and the supply chain copies are the same type as their originals),
so each rule records itself in the `derivation` of the relationships it creates - an attribute of every lineage
relationship, added to `LineageRelationship` in 6.3 for this purpose.  The reconciliation for a rule only considers
the relationships with its derivation.  The `label` is left to summarize the data that flows; a supply chain copy
carries the label of the relationship it copies.

Earlier releases recorded the rule in the `label`.  A relationship of Darwin's with no derivation and the rule's old
label is recognized as the rule's own, and moved over: its derivation is set and its label cleared.  The old labels
are the derivations, except for the server-level data flows, once labelled `derived from asset lineage` and now
`derived from data fabric lineage`.

## Configuration

`maxLineageDepth` (default 20) continues to bound every walk.  No new configuration properties are needed for the
defaults above.  The `useType` rules of the owner function could become configuration later if a deployment needs them.

## Audit log

The refresh-complete messages report, per boundary and relationship type, the counts of relationships derived, created,
removed, supply chains filled in, supply chain copies created and removed, unproven, and unchecked.  The unchecked
count shows how much of the landscape Darwin could not validate because the layer below is not catalogued.

## Testing

Each step is covered by darwin-fvt, by extending `DarwinArchiveWriter` with a fixture set per behaviour:

* a process supported by a server capability with a `useType` other than `OWNS` - the system fabric relationship is
  derived;
* `ControlFlow` between processes on two servers - a system fabric `ControlFlow` is derived, and no
  `DigitalProductDependency`;
* a `DataMapping` between a table and a process - a `DataFlow` between the table's asset and the process is derived (the
  shape the Open Lineage Cataloguer now produces);
* an asserted `DataFlow` between two assets whose schemas are mapped elsewhere but not to each other - unproven, with an
  `UnprovenDataFlow` exception;
* an asserted `DataFlow` between two assets without schemas - unchecked, with no exception;
* a path mixing tagged and untagged relationships - the untagged relationships are filled in;
* an untagged relationship on the paths of two supply chains - left untagged, with a Darwin copy for each;
* a product whose member is a data set over the tables that carry the lineage - the dependency is derived through
  `DataSetContent`.

## Implementation order

1. A generalised owner function and walk engine - layers, boundaries and type mapping as data, with today's three
   derivations re-expressed on it and today's FVT passing unchanged.
2. Upward derivation for every boundary and relationship type, including processes and `ControlFlow`.
3. Validation with an exception type per relationship type, and the populated/unchecked rule.
4. Supply chain inference within paths and downwards, with the copies for ambiguous relationships.
5. `DataSetContent` equivalence.

## Risks and open points

* **Derivation as the reconciliation scope.**  A user who edits the derivation of one of Darwin's relationships moves
  it out of its rule's scope, and Darwin will neither maintain nor remove it.  The derivation is a dedicated attribute,
  so this is far less likely than it was when the rule was recorded in the label.
* **Volume of supply chain copies.**  An untagged relationship on many supply chains' paths gets one copy per supply
  chain.  This is correct for multi-link relationships, but the audit log counts should be watched on large
  landscapes.
* **Full sweeps.**  Darwin reads every relationship of each type on every refresh.  The new boundaries add more of
  them.  An incremental approach, driven by the change events for the lineage relationships, may be needed later.
