<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Conformance Suite Harness for the Egeria Repositories (cts-fvt)

This module runs the [Open Metadata Conformance Suite](https://egeria-project.org/guides/cts/) against one
of Egeria's own repositories. Unlike its sibling suites it does not test an API surface itself: it stands up
the environment the conformance suite needs, lets the suite do the testing, and reports what the suite found.

Which repository is certified is chosen by which property starts the run:

```bash
./gradlew :open-metadata-test:open-metadata-fvt:cts-fvt:test -PrunCtsFvtPostgres
```

```bash
./gradlew :open-metadata-test:open-metadata-fvt:cts-fvt:test -PrunCtsFvtInMemory
```

The PostgreSQL run needs a reachable PostgreSQL server as well as Kafka. The in-memory run needs only
Kafka, starts from an empty repository every time, and is considerably quicker — which makes it the one to
reach for when checking a change to repository services code that both repositories sit beneath, and the
way to tell a defect in shared code apart from a defect in one connector.

Asking for both at once is refused rather than resolved to one of them.

## What it stands up

Two servers on one in-process platform, joined by a real cohort. Each kind of run uses its own servers and
its own cohort, so running one never meets anything left behind by the other — a cohort registry store
outlives the run that created it, and a server registered but not running is something the enterprise
connector would keep trying to reach:

| | PostgreSQL run | In-memory run |
| --- | --- | --- |
| Technology under test | `ctsFvtTutMetadataStore` | `ctsFvtTutInMemoryStore` |
| Conformance test server | `ctsFvtConformanceServer` | `ctsFvtInMemoryConformanceServer` |
| Cohort | `ctsFvtCohort` | `ctsFvtInMemoryCohort` |
| Results | `build/cts-fvt-report/postgres` | `build/cts-fvt-report/inmemory` |

The cohort is the part worth being explicit about, and it is why this harness needs Kafka when
`query-fvt`, `type-fvt` and `templates-fvt` do not. **The workbench never calls the technology under test
directly.** It waits for it to register in the cohort, picks it up through the enterprise
connector manager, and drives it from there. So a run exercises cohort registration and the OMRS event
exchange as much as it exercises the repository — if the cohort does not form, the workbench simply waits
and reports nothing, which is why "the workbench never completed" is reported as a cohort problem rather
than a timeout.

The conformance test server is configured and started **first**, so its workbench is already listening when
the technology under test registers.

Two things are deliberately *not* configured:

* **No access services on the technology under test.** The workbench drives the repository services
  directly through the enterprise connector, so access services would add start-up time and moving parts
  without adding anything the workbench looks at.
* **No content packs.** Only `OpenMetadataTypes.omarchive` is loaded, because the repository needs the
  types before the workbench can ask it to create instances of them. The workbench builds its own test
  instances; the content packs would add a large amount of unrelated metadata to work around.

The conformance test server gets an in-memory local repository and its enterprise access configuration
automatically — `enableRepositoryConformanceSuiteWorkbench` sets both up as a side effect, along with the
server type — so neither is configured here.

## What it asserts

The suite produces the results; this module decides what counts as a pass.

* **Every mandatory profile is conformant.** These are the requirements a repository has to meet to avoid
  doing harm to the other repositories it shares metadata with. `CONFORMANT_NO_SUPPORT` still counts as
  conformant — it means the repository does not offer the capability but does not misbehave when asked.
* **No test case failed.**
* **Optional profiles are reported, not asserted.** A repository that does not implement an optional
  capability is still conformant, and failing the run for that would make the harness useless for
  measuring where the repository actually stands.

Both servers send their audit log to the console and, through the SLF4J destination, to a single
`build/cts-fvt-data/logs/audit.log`. That file is what makes a run diagnosable: a conformance run is mostly
waiting, and when it waits forever the reason is always in the audit log - whether the cohort connected,
whether the remote member's registration arrived, and whether a connector could be built for it. Gradle
buffers a test JVM's console output until the task ends, so on a hung run the console alone tells you nothing
until it is too late to be useful. (The file-based audit log destination is deliberately not used: it writes
one file per record - hundreds during start-up alone.)

The full results are written to the run's own report directory — `conformance-summary.json` (profile by profile)
and `failed-test-cases.json` (every failed assertion, with the properties involved). Assertion messages
have room for the headline; a run that takes an hour deserves somewhere to put the rest.

## What it found

Standing this up was the test. Three defects had to be fixed before the workbench could run at all, and the
first of them broke cohort federation generally - not just the conformance suite.

**No server with a secrets store could call any remote cohort member.** When a server has a secrets store
configured, `OMRSEnterpriseConnectorManager` builds a secrets-store connection and attaches it to the remote
member's connection so it can authenticate. If the remote connection is already a `VirtualConnection` it adds
the secrets store to the existing embedded list; otherwise it wraps the plain connection in a new
`VirtualConnection` - and that branch never added the secrets store it had just built, setting an empty list
instead. The connector broker then rejected it: *"Virtual connection Local Repository Remote Connection has no
embedded connections"*. A remote member's connection always arrives from the cohort as a plain `Connection`,
so the wrapping branch is the normal path, not the edge case.

What made it hard to see is that everything *looked* healthy. Registration succeeded, the Kafka cohort topics
were created, both servers listed each other as remote members, and `connectionStatus` read `CONNECTED`. Only
the audit log said otherwise, once per registration attempt. The conformance workbench simply waited forever
for a technology under test that had, as far as the cohort was concerned, already joined.

**A restarted conformance server could not rejoin its own cohort.** Its local repository is in-memory and was
given a freshly generated metadata collection id on every start, while its cohort registry store is a file
that outlives the run - and lives under `data/servers`, outside `build`, so even a clean build leaves it in
place. The second run failed to start: *"the local metadata collection id has been changed ... since this
server registered with the cohort"*. Both servers now pin their ids through the administration services call
for it.

**The metadata collection id could not be set through the Java client.** That call sent the id as a
JSON-quoted string, and the endpoint - which binds it as an opaque `@RequestBody String` - stored it with the
quotation marks attached, answering 200 as it did so. The REST client connector now sends a string request
body as `text/plain`.

## Prerequisites

* A reachable **PostgreSQL** server — `egeria-shared-postgres` by default.
* A reachable **Apache Kafka** broker — `egeria-shared-kafka` by default. It must be the broker's EXTERNAL
  advertised listener (`oak.local:9194`), not the internal one: this harness runs on the host rather than
  inside the docker network, and a client connecting on the internal listener is handed back an address it
  cannot resolve.

Both, along with the platform's port (9450) and how long the harness waits for the workbench, are set in
`src/test/resources/application.properties`.

## Timing and scope

A full repository workbench run works through every open metadata type against a real database, and it is
slow. A measured run was still working steadily after **95 minutes**, having recorded over **5000 test
cases** across 19 profiles — not stalled, just not finished. A later run against PostgreSQL was still going
when it hit the **6 hour** limit (`cts.fvt.workbench.timeout.seconds`), having recorded **7923 test cases
without a single failure** — so six hours is not enough to certify a repository outright, and a run that ends
that way is out of time rather than non-conformant.

Because of that, the suite does **not** run every type by default. `cts.fvt.workbench.entity.types` ships
scoped to a small set of types, which makes an ordinary run a quick check of a change rather than an
overnight job:

```
cts.fvt.workbench.entity.types=Process,LicenseType,GovernanceZone,ResourceProfileAnnotation
```

Those four are not an arbitrary sample. Between them they cover **every attribute type in the model** — the
primitives, the enumerations, the arrays and the maps — so a short run still exercises each way a value can
be stored, retrieved and matched, rather than being fast because it skips things.

That coverage is the point, and it is what decides how much of the model a run needs to cover:

* A repository that **maps open metadata onto a different schema of its own** has to be tested against every
  type, because each type is a separate piece of mapping and a type that has never been exercised has never
  been shown to map. Nothing short of the full model certifies one.
* A **native** repository — the in-memory and PostgreSQL repositories here — stores every type the same way.
  What varies between types is the *pattern*: the attribute types, and whether the value sits on an entity, a
  relationship or a classification. Covering the patterns therefore gives the same assurance as covering the
  types, at a fraction of the cost.

So the default is aimed at the native repositories this repository ships. Reach for the full model when
certifying a repository that maps to a foreign schema.

Only entity types are named. The relationship and classification types follow from them:

* the named types are matched, and so is every **supertype** of each of them;
* a **relationship** type is tested when **both** of its ends are in that matched set;
* a **classification** type is tested when **any** of its valid entity types is in it.

Through that rule this list brings in the `License` and `AnnotationMatch` relationships and the `Anchors` and
`ZoneMembershipProfile` classifications. It is worth keeping in mind when changing the list: because the
supertypes come too, naming a type low in the hierarchy pulls `Referenceable` into the matched set, and with
it every relationship whose two ends are both `Referenceable`.

Widen it, or empty it, for a fuller answer — any `cts.fvt.*` setting can be overridden for a single run from
the command line:

```
./gradlew :open-metadata-test:open-metadata-fvt:cts-fvt:test -PrunCtsFvtPostgres \
    -Dcts.fvt.workbench.entity.types=Process,LicenseType,GovernanceZone,ResourceProfileAnnotation,Asset
```

Emptying it tests every entity type in the model. That is what certifying a repository means, and it is the
only setting that certifies one — but give it an overnight window and raise the timeout to match:

```
./gradlew :open-metadata-test:open-metadata-fvt:cts-fvt:test -PrunCtsFvtPostgres \
    -Dcts.fvt.workbench.entity.types= -Dcts.fvt.workbench.timeout.seconds=86400
```

The run prints which types it was scoped to, so a report is never ambiguous about what it covered.

The harness prints the number of test cases recorded each time it checks, so a long run visibly moves. If it
does hit the limit, the failure distinguishes the two cases, because they have nothing to do with each other:

* **test cases recorded** — the workbench was working and ran out of time. Raise the limit.
* **no test cases at all** — the workbench never started, so it never got hold of the technology under test
  through the cohort. The audit log is where the cause is; `OMRS-AUDIT-0114` reports a remote member whose
  connection could not be used.

## Cleaning up

The cohort registry stores live in `data/servers/<server>/cohorts/`, **outside `build`** — a clean build does
not remove them, and that is deliberate: they are how a server rejoins the cohort it already belongs to. They
only need clearing if a server's metadata collection id changes.

The technology under test stores metadata in the PostgreSQL schema `repository_ctsFvtTutMetadataStore`,
which persists between runs. The conformance suite creates and deletes its own instances, but a run that is
interrupted part-way can leave some behind. To start from clean:

```bash
docker exec egeria-shared-postgres psql -h 127.0.0.1 -p 5442 -U postgres -d egeria \
  -c 'DROP SCHEMA IF EXISTS "repository_ctsfvttutmetadatastore" CASCADE;'
```

The cohort's Kafka topics are named under `egeria.omag.cts-fvt`, so they cannot be confused with, or
consumed by, anything else using the same broker.
