<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# WSO2 Micro Integrator connector — scaffold status

Tracks odpi/egeria#9245. This is a **v1 scaffold**, not a finished connector.

## What is in place

| Area | File(s) | State |
|---|---|---|
| Module | `build.gradle`, `settings.gradle` entry | done — mirrors `unity-catalog-connectors` deps |
| FFDC | `ffdc/WSO2MIAuditCode`, `ffdc/WSO2MIErrorCode` | done |
| Domain model | `properties/APIInfo` | done |
| Management API DTOs | `resource/rest/LoginResponse`, `resource/rest/ListAPIsResponse` | done — verified against WSO2's published Management API shape |
| Resource connector | `resource/WSO2MIResourceConnector`, `resource/WSO2MIResourceProvider` | login (`GET /management/login`) + `listAPIs` (`GET /management/apis`) + `getAPI` implemented |
| Connector registration | `EgeriaOpenConnectorDefinition.WSO2MI_RESOURCE_CONNECTOR` (575), `WSO2MI_INTEGRATION_CONNECTOR` (576) | done |
| Controls | `controls/WSO2MIPlaceholderProperty`, `controls/WSO2MIConfigurationProperty` | minimal — host/port/serverName/userId/password placeholders; include/exclude API-list config properties |
| Integration connector | `catalog/WSO2MIIntegrationConnector`, `catalog/WSO2MIIntegrationProvider` | **stub** — wiring + `refresh()` loop present; the `APIInfo` → open-metadata-asset mapping is a `TODO` |
| Survey action service | `survey/SurveyWSO2MIServerConnector`, `survey/SurveyWSO2MIServerProvider`, `survey/controls/WSO2MIAnnotationType`, `survey/ffdc/WSO2MISurveyErrorCode` | **done, v1** — per Mandy's odpi/egeria#9245 guidance to survey before cataloguing. `CHECK_ASSET` → `performCheckAssetAnalysisStep(WSO2MIResourceConnector.class, ...)`, `PROFILING_ASSOCIATED_RESOURCES` → `listAPIs()` into a `ResourceProfileAnnotationProperties` (API names), `PRODUCE_INVENTORY` → `writeNameListInventory`. Mirrors `SurveyApacheKafkaServerConnector` exactly. Registered as `EgeriaOpenConnectorDefinition.WSO2MI_API_SURVEY_SERVICE` (577). Verified: `compileJava`/`compileTestJava` clean, `checkstyleMain`/`checkstyleTest` clean, `ErrorCodeTest` 1/1 pass. Also fixed the same missing `test { useTestNG() }` gap in this module's `build.gradle` that PR #9244 fixed in `postgres-server-connectors` — without it Gradle's `test` task silently found zero tests. |

## What is left

1. ~~Compile against a full Egeria build~~ — **done.** `./gradlew :...:wso2mi-connectors:compileJava` is `BUILD SUCCESSFUL` (JDK 17). The module builds cleanly against the framework.
2. **Auth wiring — open design point with the maintainers (#9245).** WSO2 MI's Management API is a two-step exchange: Basic-auth `GET /management/login` → bearer token → subsequent calls. The current `WSO2MIResourceConnector` obtains the base REST client from `RESTClientFactory` (which applies the configured Basic credentials) for the login call and holds the token on the connector. This needs to be reconciled with whatever session-token pattern Egeria prefers — Mandy pointed at the "client-side secret" pattern; the exact fit for a *username + password → session token* flow (rather than a static secret) is the question posted on #9245.
3. **Integration connector body** — researched; here is the concrete plan (from reading `OracleServerCatalogTargetProcessor` / `PostgresServerCatalogTargetProcessor`):

   a. **Switch to `DynamicIntegrationConnectorBase`** + a `WSO2MICatalogTargetProcessor extends CatalogTargetProcessorBase`. Each Micro Integrator instance is a catalog target with its own embedded `WSO2MIResourceConnector`. `WSO2MIIntegrationConnector.getNewRequestedCatalogTargetSkeleton()` returns the processor (mirror `OracleServerIntegrationConnector` lines 70-140).

   b. **`WSO2MITemplateType`** enum with a template name + a fixed template GUID per element kind (mirror `OracleTemplateType`). v1 needs one: the deployed-API asset template. The template itself is a metadata element that ships in the connector-configuration-factory / content packs — coordinate with maintainers on where it lives.

   c. **Per-API catalog logic** in the processor's `refreshCatalogTarget()`:
      - `resourceConnector.listAPIs()` → for each `APIInfo`
      - `integrationContext.elementShouldBeCatalogued(apiName, excluded, included)` — the include/exclude filter is built in
      - resolve `qualifiedName` from the template + placeholder properties (`propertyHelper.getResolvedStringPropertyFromTemplate`)
      - `openMetadataStore.getMetadataElementByUniqueName(qualifiedName, ...)` — if non-null, `auditLog.logMessage(SKIPPING_API)` (idempotency)
      - else `openMetadataStore.getMetadataElementFromTemplate(<DeployedAPI type>, <MI-instance anchor GUID>, false, ..., templateGUID, ..., placeholderProperties, <capability GUID>, CAPABILITY_ASSET_USE_RELATIONSHIP, useProps, true)` → returns the new element GUID; `auditLog.logMessage(CATALOGED_API)`

   d. **Open metadata type for a deployed REST API** — `OpenMetadataType.DEPLOYED_API` exists; confirm it's the right one vs a generic `Asset`/`DeployedConnector`, and whether the MI instance itself should be a `SoftwareServer` + `SoftwareCapability` (as the DB connectors do for the DB server + DB manager).
4. **Template types.** `WSO2MITemplateType` + template GUIDs, wired into the provider (`supportedTemplateTypes`), following `OracleTemplateType`.
5. **Tests.** ~~Survey `ffdc` `ErrorCodeTest`~~ — **done.** Still open: `ffdc` `AuditCodeTest`/`ErrorCodeTest` for the resource connector's own `WSO2MIAuditCode`/`WSO2MIErrorCode` (mirror the Oracle/UC ones); a mocked-REST test for `WSO2MIResourceConnector.login()` + `listAPIs()`; and — once a real Micro Integrator instance is available — actually running the survey connector end to end, which is the whole point of building it first.
6. **Docs.** A page under `site/docs` and a `connector-configuration-factory` entry, if the maintainers want the connector shipped in the default configuration.

## v1 scope (unchanged from #9245)

Authenticate, then list deployed **APIs** only. Proxy Services, Sequences, Endpoints, Inbound
Endpoints, Connectors (`GET /management/connectors`), Message Stores/Processors, Templates and Tasks
are follow-up increments. Read-only / inbound-only — nothing provisions or writes back.
