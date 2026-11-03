# Open Metadata Digital Products and the Bitol standards - gap analysis

*Prepared 2026-11-03 from the 6.3-SNAPSHOT code.*

This note maps the digital product definitions in the **Open Metadata Digital Product Catalog** (the 34 products and 13 product families that the Jacquard harvester builds from `ProductDefinitionEnum`) to the Bitol **Open Data Product Standard (ODPS v1.1.0)** and **Open Data Contract Standard (ODCS v3.2.0)**, and identifies where Egeria's view of a digital product and its agreements differs from Bitol's.

Sources read:

* `nanny-connectors/.../jacquard/productcatalog/*` and `JacquardIntegrationConnector.getProduct()`, `addDataSpec()`, `addProductAsset()`, `addSubscriptionTypes()`
* `ProductManagerHandler.createSubscriptionType()`
* `nanny-connectors/.../subscriptions/CreateSubscriptionGovernanceActionConnector`
* `open-integration-framework/.../bitol/mapping/DataProductGenerator`, `DataContractGenerator`, `BitolGeneratorBase`, and the Bitol connectors' README

## 1. The two world views in one paragraph each

**Egeria.**  A digital product is a *collection* that ties a business offer to its implementation.  It carries product-management information (name, category, maturity, introduction / next-version / withdraw dates, service life, content and deployment status), the *questions* it answers and the *perspectives* of the people who ask them, a *community*, a *product manager role*, a *license type*, a *data specification* (DataSpec -> DataStructure -> DataField, with glossary terms), the *asset* that holds the data and its connection, and a menu of *subscription types*.  Each subscription type is a NotificationType carrying a service level objective and a notification interval.  When a consumer subscribes, a separate **DigitalSubscription** agreement is created per consumer, with the license, SLO, destination asset and the provisioning and cancellation processes.  Products are arranged into families, catalogs and folders, and depend on each other through lineage.

**Bitol.**  A data product (ODPS) is a *descriptor of ports*: what it consumes (input ports) and what it provides (output ports), each port pointing at a data contract, plus management ports, support channels and a team.  The data contract (ODCS) is a *provider-published specification* of one dataset: schema, quality rules, SLA properties, servers, access roles, pricing, support and team.  The contract is one-to-many - there is no consumer party in it - and there is no notion of a subscription, a license instance, or a consumer-specific agreement.

The short version: **Egeria models the offer and the deal; Bitol models the interface and the promise.**  Egeria's product is richer on the business side, Bitol's contract is richer on the technical side, and the thing Bitol calls a "contract" is not the thing Egeria calls an "agreement".

## 2. Element-level mapping

| Egeria concept (Open Metadata Digital Products) | Open metadata type | Bitol equivalent | Fit |
|---|---|---|---|
| Product catalog (`TOP_LEVEL`) | DigitalProductCatalog | ODPS `domain` (one string) | Partial - catalog is a structure, domain is a label |
| Product family / folder (`PRODUCTS`, `REFERENCE_DATA_SETS`, ...) | DigitalProductFamily, CollectionFolder | none | **Gap in Bitol** |
| Digital product | DigitalProduct | ODPS document | Good for identity fields |
| Product data set (TabularDataSet, ReferenceCodeTable ...) | Asset, member of the product | ODPS output port + ODCS `servers[]` | **Shape mismatch** (see 4.1) |
| Data specification | DataSpec -> DataStructure -> DataField via DataDescription | ODCS `schema[]` | Content fits, **attachment point differs** (see 4.2) |
| Data field identifier / core detail | MemberDataField `coverageCategory` | ODCS `primaryKey` / plain property | Partial |
| Glossary term on data field | SemanticAssignment | ODCS `authoritativeDefinitions` / `businessName` | Partial |
| License type (`INTERNAL_USE_ONLY`, `PERSONAL_DATA`) | LicenseType, GovernedBy | ODCS `description.usage` / `limitations`; no license object | **Gap in Bitol** |
| Service level objective (`SLO-DAILY-REFRESH` ...) | ServiceLevelObjective | ODCS `slaProperties[]` (`frequency`, `latency`, `retention` ...) | Good conceptually, different granularity |
| Subscription type (evaluation, daily, weekly, ongoing) | NotificationType anchored to product | none - ODCS has one SLA per contract | **Gap in Bitol** |
| Digital subscription | DigitalSubscription (Agreement) + AgreementItem + DigitalSubscriber + AgreementActor | none | **Gap in Bitol** |
| Product manager | PersonRole via **AssignmentScope** | ODPS / ODCS `team.members[]` | Fits, but **not exported** (see 4.4) |
| Community | Community + NoteLog | ODPS `support[]` channel | Partial |
| Questions the product answers | Question classification via SupplementaryProperties | `description.purpose` (free text); ODCS v3.2 `context` | **Gap in Bitol** |
| Perspectives | Perspective, ScopedBy on question | none | **Gap in Bitol** |
| Governance drivers (business imperative, policy, approach) | GovernanceDriver / Policy | none | **Gap in Bitol** |
| Product dependencies / data mesh | DigitalProductDependency | ODPS input port `contractId` | Partial - Bitol infers it from contracts |
| Zone membership (`digital-products`, `security`) | ZoneMembership | none (ODCS `roles` is closest) | **Gap in Bitol** |
| Connection / connector type for the data set | Connection, ConnectorType, Endpoint | ODCS `servers[]` | Partial |
| Delivery / provisioning process | ProvisioningActionProcess, NotificationSubscriber | none | **Gap in Bitol** |
| Solution blueprint and components | SolutionBlueprint, SolutionComponent | ODPS ports (implicitly) | Partial |

## 3. Property-level mapping of a DigitalProduct

| DigitalProduct property | Set by Jacquard? | ODPS field | Notes |
|---|---|---|---|
| `qualifiedName` | yes | - | Egeria-internal |
| `identifier` | yes (e.g. `OPEN-METADATA-...`) | `id` | Good |
| `productName` | yes | `name` | Good |
| `displayName` | yes | `name` (fallback) | |
| `description` | yes | `description.purpose` | Generator puts description into `purpose`; DigitalProduct `purpose` overwrites it if set |
| `versionIdentifier` | yes (`6.3-SNAPSHOT`) | `version` | Bitol expects semver; a SNAPSHOT suffix is legal but unusual |
| `category` | yes (`reference data` ...) | `type` (v1.1.0 vocabulary) | Not mapped today - `type` only round-trips from `bitol.type` |
| `contentStatus` | ACTIVE / APPROVED | `status` | Fits (`active`, `draft`/`proposed`) |
| `deploymentStatus` | ACTIVE / UNDER_DEVELOPMENT | none | **Gap in Bitol** |
| `introductionDate` | yes | `productCreatedTs` | Not mapped today - easy win |
| `maturity`, `serviceLife`, `nextVersionDate`, `withdrawDate` | no | none | **Gap in Bitol** |
| search keywords | no | `tags` | Mapped, but Jacquard sets none |
| external references | no | `authoritativeDefinitions` | Mapped, but Jacquard sets none |

## 4. What an exported Open Metadata Digital Product would look like today

If the Bitol Document Publisher were pointed at the Open Metadata Digital Product Catalog, `DataProductGenerator` would produce a valid but almost empty ODPS document for each product, and `DataContractGenerator` would produce nothing at all.  The reasons are structural:

### 4.1 No ports

The generator builds `inputPorts`/`outputPorts` only from **SolutionPorts on SolutionComponents that are collection members of the product**.  Jacquard's products have the data set asset as a collection member (`membershipType = "product data set"`) and no solution component members; the solution components Jacquard creates belong to a separate solution blueprint.  So every Jacquard product exports with no output port, which in Bitol terms is a product that provides nothing.

### 4.2 No contract

ODCS documents are generated only for **Agreements classified `DataSharingAgreement`**.  Jacquard attaches the data specification to the *product* through `DataDescription`, not to an agreement through `AgreementItem`; and the DigitalSubscriptions created at subscribe time are not classified `DataSharingAgreement`.  So the schema, SLOs and license - the content Bitol expects in a contract - are never emitted.

### 4.3 No domain for most products

`domain` is taken from a direct membership of a DigitalProductCatalog.  Jacquard's products are members of a DigitalProductFamily which is in turn a member of the catalog, so only the top-level family gets a domain.

### 4.4 No team

The generator builds `team` from elements linked by **ScopedBy** (a Team, or PersonRoles).  Jacquard links the product manager role through **AssignmentScope** (assignment type *Owner*).  The product manager therefore does not appear in the exported team.

### 4.5 No support

`support` is built from ContactDetails.  Jacquard's community (with its note log) is the support forum, but it is not ContactDetails, so it is not exported.

### 4.6 No management ports

Management ports come from Endpoints in the product's ResourceList.  Jacquard's endpoint hangs off the data set asset's Connection.

Items 4.3 - 4.6 are fixable in the generator (or by Jacquard adding the relationships the generator reads); 4.1 and 4.2 reflect a genuine modelling difference, discussed next.

## 5. The conceptual gaps

### 5.1 "Contract" versus "agreement"

| | Bitol ODCS data contract | Egeria Agreement / DigitalSubscription |
|---|---|---|
| Parties | Implicit: the provider, publishing to whoever reads it | Explicit: AgreementActor (requester, product owner) and DigitalSubscriber |
| Cardinality | One per dataset version | One per consumer per subscription type |
| Lifecycle | Document versions (`status`) | Agreement start/end, activity status, cancellation process |
| Terms | `description.usage`/`limitations`, `price`, `slaProperties` | GovernedBy license type, SLO; AgreementItem entitlements / restrictions / obligations; License instance with coverage dates |
| Execution | None - descriptive | Provisioning process delivering to the subscriber's destination asset |

Egeria today uses `Agreement + DataSharingAgreement` to hold an ODCS contract (provider-side specification) and `DigitalSubscription` for the consumer-side deal.  Both are Agreements, but only the first has a Bitol rendering.  Jacquard's catalog uses only the second.

**Implication:** to export the Open Metadata Digital Products as ODCS, each product (or each product version) needs a provider-side DataSharingAgreement whose AgreementItems are the data structures of its data spec and which is GovernedBy the license and SLOs.  Alternatively the generator could synthesise a contract from the product's DataDescription.

### 5.2 Subscription types and multiple service levels

Every Jacquard product offers up to four subscription types, each with its own SLO and notification interval.  ODCS has a single list of SLA properties per contract.  Options:

* one ODCS contract per subscription type (contract id = product id + subscription type identifier), or
* one contract with the strongest SLA, and the others as `customProperties`, or
* an ODPS output port per subscription type, each pointing at its own contract version.

The third is closest to Bitol's intent (ports are delivery mechanisms) and aligns with Egeria's subscription types being delivery choices rather than different data.

### 5.3 Licensing

Bitol has no license object.  Egeria's LicenseType (`INTERNAL_USE_ONLY`, `PERSONAL_DATA`) with summary, usage, entitlements, restrictions and obligations would have to be flattened into ODCS `description.usage` / `description.limitations` text or an `authoritativeDefinitions` entry of type `license` pointing at a URL.  The per-subscription License relationship (with coverage dates) has no Bitol home at all.

### 5.4 Product families, catalogs and folders

ODPS is a flat document with a single `domain` string.  Egeria's DigitalProductFamily - including families such as Valid Metadata Value Sets and Reference Data Sets whose member products are generated dynamically - has no counterpart.  A family could be exported as an ODPS document with one output port per member, but subscribing to a family (a supported Egeria feature) has no Bitol meaning.

### 5.5 Purpose, questions and perspectives

Egeria's catalog is organised around *what question does this product answer, for whom*.  Bitol has only `description.purpose` (free text) and, from v1.1.0 / v3.2.0, an AI `context` block.  The questions and perspectives could be rendered into `description.purpose` or `context.instructions`, but they lose their structure and their links to personas.

### 5.6 Product lifecycle

Egeria separates content status (is the description approved?) from deployment status (is the product running?) and adds maturity, service life, next-version and withdraw dates.  Bitol has a single `status` and `productCreatedTs`.

### 5.7 Governance context

Governance drivers, zones, the community and the subscription manager are all outside Bitol's scope.  ODCS `roles` is the only access-control concept and describes roles on the data, not zones on the metadata.

### 5.8 Where Bitol is richer

These ODCS features are well supported by the Bitol cataloguers but are **not used** by the Open Metadata Digital Products, so they are gaps on the Egeria side for this catalog, not in the type system:

* **Data quality rules** - none of the products declare quality checks, though the observability products report on quality.
* **Servers** - the delivery destinations (CSV file, PostgreSQL table) are subscriber-side; the product-side source is an in-memory tabular data set.
* **Pricing** - no cost information on the products.
* **Field classification and criticality** - the PERSONAL_DATA license is product-wide; individual fields are not marked confidential.
* **Field-level relationships** - identifiers in the data spec are not linked across products (e.g. the user identity GUID in USER_ACCOUNTS and USER_IDENTITIES).
* **Input ports** - the products' dependency on open metadata itself is not declared as an input.  More generally, the process that assembles a product's data (a pipeline, DAG task or integration connector) is recorded in Egeria as lineage into the product's data sets, not as a port.  This matters most for products such as the Coco Pharmaceuticals strategic digital products in `egeria-workspaces`, whose tables are loaded by the `coco_feed_*` Airflow DAGs and whose lineage arrives from OpenLineage events.

## 6. Suggested actions

Ordered from cheapest to most significant.

1. **Generator fixes** (no model change): map `introductionDate` -> `productCreatedTs`, `category` -> `type` when it matches the ODPS vocabulary, read AssignmentScope as well as ScopedBy for the team, follow family membership up to the catalog for `domain`, and render a linked Community as a support channel.
2. **Jacquard additions**: add search keywords and an external reference (the question URL or the docs page) to each product; link the endpoint of the delivery service through ResourceList so it becomes a management port.
3. **Ports for products**: have Jacquard add a SolutionComponent member to each product with one output SolutionPort per subscription type - this is also what the data mesh views want.
4. **Provider-side contracts**: create one DataSharingAgreement per product version holding the data spec structures as AgreementItems and GovernedBy the license and SLOs, and link it to the output ports.  This makes the catalog fully exportable and gives subscriptions something to reference.
5. **Decide the subscription story**: either treat a DigitalSubscription as "an instance of the ODCS contract for consumer X" (link it to the DataSharingAgreement) or accept that subscriptions remain Egeria-only.  This consumer-side model could be offered to the Bitol project as a proposal.
6. **Carry the rest losslessly**: anything Egeria-only (questions, perspectives, deployment status, maturity, license detail, subscription types) can be emitted as ODPS/ODCS `customProperties` entries tagged `vendor: egeria`.  `customProperties` is the standards' own extension point, and the `vendor` attribute (added in ODCS v3.2.0 and ODPS v1.1.0) marks which tool owns an entry, so no naming convention is needed.  The cataloguers already keep the full custom property list as JSON in `bitol.customProperties` when any entry has a vendor, so on re-import the Egeria entries can be recognised by their vendor and restored to their open metadata properties - a round trip through git loses nothing.

```yaml
customProperties:
  - property: deploymentStatus
    value: ACTIVE
    vendor: egeria
    description: Egeria deployment status, separate from the document status
```

### Implementation status (6.3)

| Action | What was done | Where |
|---|---|---|
| 1 | Team also reads AssignmentScope (role qualified name used when there is no username); a scoping Community becomes a support channel; domain is inherited through product families; `introductionDate` -> `productCreatedTs`; `category` -> `type` when it is an ODPS type; license types fill an empty `description.usage` in ODCS | `BitolGeneratorBase`, `DataProductGenerator`, `DataContractGenerator` |
| 2 | Jacquard adds search keywords (category and question perspectives) and an external reference per guiding question URL.  The management port is the Product Manager OMVS `data-product-document` URL, added only when the new `viewServerName` configuration property is set, because Jacquard has no other product-level management interface | `JacquardIntegrationConnector.addProductDiscoveryDetails`, `JacquardConfigurationProperty` |
| 3 | Each product with data gets a SolutionComponent member with one output SolutionPort per subscription type | `JacquardIntegrationConnector.addProductPortsAndContracts` |
| 4 | One DataSharingAgreement per subscription type (id `{product id}::{subscription type id}`, qualified name in the cataloguers' `DataContract::{id}::{version}` form).  Its items are the product's data structure, the output port and the notification type; it is GovernedBy the license type and the subscription type's SLO.  The daily, weekly and ongoing SLOs now carry ODCS SLA properties (`frequency` 1 d / 7 d, `latency` 1 h) | same method; `ProductGovernanceDefinition.getSLAProperties` |
| 5 | Chose "a subscription is an instance of the contract": a new DigitalSubscription gets an AgreementItem ("data contract") to the DataSharingAgreement that lists its notification type | `CreateSubscriptionGovernanceActionConnector.linkDataContract` |
| Input ports | The generator adds an ODPS input port for each process with data lineage (DataFlow, DataMapping or LineageMapping) into one of the product's member assets, or into the content of a member data set (following DataSetContent and data set membership two levels down, enough to reach the RelationalTables beneath a TabularDataSetCollection).  The port is named after the process; `contractId` comes from an agreement the process is an item of, or `bitol.contractId`; the process's qualified name and type, and the elements it supplies, are `vendor: egeria` custom properties.  A process that already has an input port of the same name is skipped.  When a process has no contract, the publisher catalogues a sparse draft one (id `{process qualified name}::input`, version `0.1.0`, status draft, description naming the elements the process reads, process and sources as agreement items) and publishes it with the product, ready to be filled out in the Bitol store | `DataProductGenerator.addAssemblingProcessPorts`, `DataContractMapper.ensureInputContract`, `BitolDocumentPublisherIntegrationConnector` |
| 6 | `vendor: egeria` custom properties written for product lifecycle, category, families, questions and license types (and license types / SLOs on contracts); on import they are excluded from additional properties and the lifecycle properties are restored | `BitolMapperBase`, `BitolGeneratorBase`, `DataProductMapper`, Bitol connectors README |

Known limits: the elements in actions 3-5 are linked only when first created, so a change to a product's subscription types or license after that is not reflected in existing contracts; the one-time (evaluation) SLO has no measurable SLA property; and the hour unit `h` follows the pattern of the ODCS examples (which only show `d` and `y`) rather than a confirmed vocabulary.  The draft input contracts are created only by the publisher: a document fetched through the Product Manager's `data-product-document` request (which must not change metadata) has no `contractId` on such a port until the publisher has run.  A process that feeds several products gets one draft, whose description names the first product it was drafted for.

## 7. Summary table of gaps

| Gap | Direction | Severity | Fix |
|---|---|---|---|
| No output ports on Jacquard products | Egeria data | High | Action 3 |
| No provider-side contract for Jacquard products | Egeria data | High | Action 4 |
| Consumer-specific subscriptions | Bitol model | High | Action 5 |
| Multiple service levels per product | Bitol model | Medium | Action 3 / 5.2 |
| License types and license instances | Bitol model | Medium | 5.3 / Action 6 |
| Product families and subscribing to a family | Bitol model | Medium | 5.4 / Action 6 |
| Assembling process not shown as an input port | Egeria generator | Medium | Input ports row above |
| Team, domain, support, management ports not exported | Egeria generator | Medium | Actions 1, 2 |
| Questions, perspectives, governance drivers, zones | Bitol model | Low | Action 6 |
| Deployment status, maturity, lifecycle dates | Bitol model | Low | Actions 1, 6 |
| Quality rules, pricing, field classification unused | Egeria data | Low | Content work in Jacquard |
