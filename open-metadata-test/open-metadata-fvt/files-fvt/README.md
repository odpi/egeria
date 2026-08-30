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

* **[FileSurveyFVT](src/test/java/org/odpi/openmetadata/filesfvt/FileSurveyFVT.java)** - `survey-csv-file`
  and `survey-data-file`, each against a file of the kind it is meant for, as separate cases so that a failure
  names which service broke. The file assets are created straight from their catalog templates because the
  Files pack ships no create-file action: files are meant to reach the catalogue through a cataloguer, which
  is the next test.

* **[FolderCatalogFVT](src/test/java/org/odpi/openmetadata/filesfvt/FolderCatalogFVT.java)** - hands a folder
  to the General Folder Cataloguer as a catalog target and checks the files inside it are catalogued. Nothing
  here calls the connector: the request goes to the view server, a governance service on the engine host
  attaches the target, and the connector runs in the *integration daemon* - a third server. That arrangement
  is as much what is being tested as the connector itself.

* **[CataloguersFVT](src/test/java/org/odpi/openmetadata/filesfvt/CataloguersFVT.java)** - every integration
  connector the pack ships is in the integration daemon and started. That is not a formality: a connector is
  instantiated from a stored connection by class name, so one missing from the runtime classpath, or one whose
  connection is wrong, does not fail the build - it fails inside a server, and is only visible in a status
  report. The check reports the exception a stopped connector recorded, so a cataloguer that fell over on its
  own missing directory does not look identical to one that is working.

* **[FileActionsFVT](src/test/java/org/odpi/openmetadata/filesfvt/FileActionsFVT.java)** - the file
  provisioning actions: `copy-file`, `move-file` and `delete-file`. Each case asserts against the file system
  *and* the repository, because these services do both: a copy that catalogued an asset without writing the
  file, or wrote the file without cataloguing it, would pass a test that looked in one place only. The three
  run in order and share a destination folder, because they are three stages of one story.

* **[DataFolderActionsFVT](src/test/java/org/odpi/openmetadata/filesfvt/DataFolderActionsFVT.java)** — a data
  folder through all three of its actions: `create-data-folder`, `catalog-data-folder`, `delete-data-folder`.
  A `DataFolder` is not a `FileFolder` under another name — the directory *is* the data set, and the files
  inside it are not catalogued separately — so the pack ships a separate template and separate actions, and
  this says the second set works rather than assuming it does because the first does. The delete case is the
  one that earns its place: `delete-data-folder` is given the template and the same placeholder values the
  create used and derives the qualified name from them, so it is the only action here that has to work out
  what it operates on. The test also checks the directory is **still on disk** afterwards — it removes the
  catalogue entry, not the data, and a test that only checked the asset had gone would pass on a service that
  deleted the user's files.

* **[FileTypeCataloguingFVT](src/test/java/org/odpi/openmetadata/filesfvt/FileTypeCataloguingFVT.java)** — the
  cataloguer picks a catalog template by the kind of file it found: a `.csv` arrives as a `CSVFile` and a
  `.json` as a `JSONFile`, not as plain `DataFile`s. [templates-fvt](../templates-fvt) already creates an
  element from every one of the pack's file templates directly; what is untested there, and tested here, is
  the step before that — something has to *choose* the template. A file catalogued as the wrong type is not
  wrong the way a missing asset is wrong; it is worse, because everything downstream that keys off the type
  quietly stops applying. The expected types come from `DeployedImplementationType` rather than string
  literals, so a rename in the model is a compile failure here.

* **[NewFileWatchdogFVT](src/test/java/org/odpi/openmetadata/filesfvt/NewFileWatchdogFVT.java)** — starts
  `watch-for-new-files-in-folder` on a folder and checks it is running. **This is a narrower claim than the
  other tests make.** A watchdog does not complete: it starts, registers its interest, and stays running, so
  waiting for a terminal status would time out on one that is working perfectly. What is asserted is that it
  started and stayed started — which catches the failure this connector family has shown repeatedly, a
  service that throws on start-up and so never watches anything. It does *not* show that the watchdog reacts
  to a new file; see below.

## Still to add

* **The watchdog's reaction.** `NewFileWatchdogFVT` shows the watchdog runs, not that it acts. Showing that
  means cataloguing a new file inside the watched folder and waiting for the action the watchdog initiates in
  response — a second engine action the test never requested and cannot name in advance. Worth having, and a
  larger test than the one that is here.

* **The file templates no governance action reaches.** `FileTypeCataloguingFVT` covers the types the folder
  cataloguer chooses between for the files this suite writes. The pack ships around twenty templates in all —
  Avro, Parquet, spreadsheets, source code, keystores and the rest — and the ones no action in this suite
  drives are created directly by [templates-fvt](../templates-fvt) instead. Widening the tree to one file per
  template would extend this suite's coverage to the choice as well as the creation.

* **The other four cataloguers, driven through cataloguing.** `CataloguersFVT` checks all five are running,
  and `FolderCatalogFVT` takes one of them all the way. The other four cannot be driven here without
  inventing a configuration — see `CataloguersFVT` for why.

## Clean-up

Everything is cleared at the **start** of a run rather than the end, so that a killed or crashed run leaves its
evidence to look at and the next run still starts from a known state. In the repository, every element the
suite causes to be created carries `files-fvt` somewhere in its qualified name; on disk, the tree is rebuilt.
Set `files.fvt.clear.down=false` to keep both for inspection — the tests will then be reading a repository
holding an earlier run's elements as well as this one's, and are likely to report that as a failure.
