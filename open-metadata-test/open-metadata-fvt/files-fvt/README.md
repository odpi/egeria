<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# File Connectors Functional Verification Tests (files-fvt)

This suite tests the **file connectors** — the file and folder survey action services, the folder cataloguers,
and the file resource connectors — together with the **Files content pack** that defines and drives them.

It is built the same way as its sibling
[postgres-fvt](../postgres-fvt), and for the same reason: it does not call the connectors directly. It stands
up the deployment they are designed to run in and then drives them the way an operator would, through the
**Automated Curation API**, by running the governance actions the content pack ships.

```bash
./gradlew :open-metadata-test:open-metadata-fvt:files-fvt:test -PrunFilesFvt
```

There is a second mode, which runs the same tests with no event bus at all:

```bash
./gradlew :open-metadata-test:open-metadata-fvt:files-fvt:test -PrunFilesFvtNoKafka
```

## The platform binds to an ephemeral port

`server.port=0`, so the operating system picks a free port at start-up and `OMAGPlatformExtension` reads back
what it was given, building every client URL from that.

This matters more here than it does elsewhere. The sibling suites each pin a port (9446–9451) and rely on
nobody using it, which breaks down as soon as two checkouts run the same suite, or somebody starts a dev
platform while a long run is going. Nothing in this suite depends on a particular number, so there is nothing
to keep in step and nothing to collide with. Set a real number if you need to reach the platform from outside
the test JVM while it is up — the port it chose is otherwise only visible in the start-up log.

## What it stands up

Four servers on one in-process platform, and — in the default mode — a real Apache Kafka broker:

| Server | What it is | What it runs |
| --- | --- | --- |
| `filesFvtMetadataStore` | Metadata access store | PostgreSQL local repository; `OpenMetadataTypes`, `CoreContentPack` and `FilesContentPack` loaded at start-up |
| `filesFvtEngineHost` | Engine host | `FileSurvey`, `FileGovernance` and `Stewardship` governance engines |
| `filesFvtIntegrationDaemon` | Integration daemon | `FilesIntegrationGroup`, where the folder cataloguers run |
| `filesFvtViewServer` | View server | Automated Curation OMVS |

Only three archives are loaded. The Core content pack is needed because the Files actions call its generic
governance services — create an asset from a template, delete it again, add it to an integration connector as
a catalog target — and because the survey processes end by writing their report out as markdown on the Core
pack's **Stewardship** engine. Nothing else is loaded: the other content packs would add load time and a large
amount of unrelated metadata for the suite's searches to work around.

## The directory tree under test

The suite builds its own tree under `files.fvt.data.directory` (inside the module's build directory by
default) and rebuilds it at the start of every run.

This is deliberate rather than incidental, and it is the same reasoning as postgres-fvt's database under test.
Surveying whatever happens to be on the machine gives nothing specific to assert — "the folder survey found 3
files and a nested folder" is a real assertion where "the survey found some files" is not. It also keeps a run
from walking a tree that something else is writing to.

Each folder gets the same shape, so any test can make the same assertions about the one it was given: three
files directly inside it, and one nested folder holding one more. The files have different extensions on
purpose — that is what decides which catalog template a file cataloguer picks for them, and a tree of one file
type would not exercise that choice at all.

## What it tests

* **[ContentPackFVT](src/test/java/org/odpi/openmetadata/filesfvt/ContentPackFVT.java)** — the content pack
  loaded, and the two governance servers picked up the parts of it they are configured to run. What the pack is
  supposed to contain is not written out: the test iterates the definitions in `core-content-pack` filtered to
  the Files pack and checks the repository against them, so adding a connector, an engine or a request type to
  the pack extends this test's coverage without anybody editing it.

* **[FolderSurveyFVT](src/test/java/org/odpi/openmetadata/filesfvt/FolderSurveyFVT.java)** — catalogues a
  folder from the `FileFolder` template and then surveys it, in the two steps a curator would use. This is the
  test the suite was written for. A folder survey against a template-created asset was reported failing with a
  `NullPointerException` inside `BasicFolderConnector`: the asset had no connection the survey could open it
  through, `getConnectorForAsset()` returned null by contract, and the survey service dereferenced it. The test
  checks for the connection before asking for the survey — so a missing connection is reported against the
  asset rather than as a survey failure — and then asserts the survey *completed* and produced a report with
  annotations on it. A survey that dies on a null connector still produces an engine action, so a test that
  only checked the action existed would pass while the thing under test was broken.

## Still to add

This suite covers the survey half of the Files content pack. The rest of what the pack ships is not yet
exercised:

* the **folder cataloguers** (`GENERAL_FOLDER_CATALOGUER`, `SAMPLE_DATA_CATALOGUER`, `CONTENT_PACK_CATALOGUER`,
  `SECRETS_STORE_CATALOGUER`, `MAINTAIN_LAST_UPDATE_CATALOGUER`) as catalog targets on the integration daemon,
  which is what postgres-fvt's `PostgresServerCatalogFVT` does for its cataloguer;
* the **file survey** and **CSV survey** services, as distinct from the folder survey;
* the **file governance actions** — copy, move and delete a file — and the folder delete actions;
* the twenty-odd **catalog templates** for individual file types, which
  [templates-fvt](../templates-fvt) already creates elements from, but not through the governance actions that
  choose between them.

## Clean-up

Everything is cleared at the **start** of a run rather than the end, so that a killed or crashed run leaves its
evidence to look at and the next run still starts from a known state. In the repository, every element the
suite causes to be created carries `files-fvt` somewhere in its qualified name; on disk, the tree is rebuilt.
Set `files.fvt.clear.down=false` to keep both for inspection — the tests will then be reading a repository
holding an earlier run's elements as well as this one's, and are likely to report that as a failure.
