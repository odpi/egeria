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
| Integration connector | `catalog/WSO2MIIntegrationConnector`, `catalog/WSO2MIIntegrationProvider` | **done, v1** — `refresh()` lists deployed APIs, applies the include/exclude filter, and catalogues each as a self-anchored `DeployedAPI` asset keyed by a qualified name built from the API's invocation URL (mirrors `OpenAPIMonitorIntegrationConnector`'s creation pattern for the same open metadata type — no template or content-pack element required). Idempotent: re-running `refresh()` finds the existing asset by qualified name and skips it (`WSO2MIAuditCode.SKIPPING_API`), same as `CATALOGED_API` for new ones. Kept `IntegrationConnectorBase` with the embedded resource connector, per the still-open design question below, rather than unilaterally switching to `DynamicIntegrationConnectorBase` + a catalog-target processor. |
| Survey action service | `survey/SurveyWSO2MIServerConnector`, `survey/SurveyWSO2MIServerProvider`, `survey/controls/WSO2MIAnnotationType`, `survey/ffdc/WSO2MISurveyErrorCode` | **done, v1** — per Mandy's odpi/egeria#9245 guidance to survey before cataloguing. `CHECK_ASSET` → `performCheckAssetAnalysisStep(WSO2MIResourceConnector.class, ...)`, `PROFILING_ASSOCIATED_RESOURCES` → `listAPIs()` into a `ResourceProfileAnnotationProperties` (API names), `PRODUCE_INVENTORY` → `writeNameListInventory`. Mirrors `SurveyApacheKafkaServerConnector` exactly. Registered as `EgeriaOpenConnectorDefinition.WSO2MI_API_SURVEY_SERVICE` (577). Verified: `compileJava`/`compileTestJava` clean, `checkstyleMain`/`checkstyleTest` clean, `ErrorCodeTest` 1/1 pass. Also fixed the same missing `test { useTestNG() }` gap in this module's `build.gradle` that PR #9244 fixed in `postgres-server-connectors` — without it Gradle's `test` task silently found zero tests. |

## What is left

1. ~~Compile against a full Egeria build~~ — **done.** `./gradlew :...:wso2mi-connectors:compileJava` is `BUILD SUCCESSFUL` (JDK 17). The module builds cleanly against the framework.
2. **Auth wiring — open design point with the maintainers (#9245).** WSO2 MI's Management API is a two-step exchange: Basic-auth `GET /management/login` → bearer token → subsequent calls. The current `WSO2MIResourceConnector` obtains the base REST client from `RESTClientFactory` (which applies the configured Basic credentials) for the login call and holds the token on the connector. This needs to be reconciled with whatever session-token pattern Egeria prefers — Mandy pointed at the "client-side secret" pattern; the exact fit for a *username + password → session token* flow (rather than a static secret) is the question posted on #9245.
3. ~~Integration connector body~~ — **done, v1, but via a simpler path than originally planned here.** Went looking for a closer reference than the DB connectors (which catalog a *server hosting many sub-databases*, a two-tier shape WSO2MI doesn't need) and found `OpenAPIMonitorIntegrationConnector` already catalogs the exact same target type, `DeployedAPI`, using direct `AssetClient.createAsset()` with `DeployedAPIProperties` — no template, no content-pack GUID, no `DynamicIntegrationConnectorBase`/catalog-target-processor split. Kept `WSO2MIIntegrationConnector` on `IntegrationConnectorBase` (its current base) with the embedded `WSO2MIResourceConnector`, rather than unilaterally deciding the open `DynamicIntegrationConnectorBase` question below. Each API becomes a self-anchored `DeployedAPI` asset, qualified name `"DeployedAPI:" + apiInfo.getUrl()` (the same identifier scheme `OpenAPIMonitorIntegrationConnector` uses), with `findExistingAsset`-style idempotency (`getAssetsByName` + qualified-name match) before creating. This avoids the "coordinate with maintainers on where the template lives" blocker entirely — no template dependency to resolve.

   **Still an open design question, not resolved here:** whether this should switch to `DynamicIntegrationConnectorBase` + a catalog-target processor (as `OracleServerIntegrationConnector` does), treating the WSO2 MI server itself as a `SoftwareServer` catalog target. That would let a `REST_API_MANAGER`/`API_MANAGER` capability own the APIs via `CAPABILITY_ASSET_USE_RELATIONSHIP` instead of self-anchoring them — closer to how the DB connectors model "server owns things." Not adopted for v1 since it's a bigger architectural change than the connector currently has, and the scaffold's own doc comment already flagged this as an open point for the maintainers rather than something to decide unilaterally.
4. **Template types.** Not needed for v1 — see point 3 above; direct-properties creation has no template dependency. Revisit only if the design question in point 3 is resolved in favour of the catalog-target-processor shape.
5. **Tests.** ~~Survey `ffdc` `ErrorCodeTest`~~ — **done.** ~~`ffdc` `AuditCodeTest`/`ErrorCodeTest` for `WSO2MIAuditCode`/`WSO2MIErrorCode`~~ — **done.** Still open: a mocked-REST test for `WSO2MIResourceConnector.login()` + `listAPIs()`; a test for `WSO2MIIntegrationConnector`'s cataloguing logic (needs a way to mock `AssetClient`/`IntegrationContext`, not yet worked out); and — once a real Micro Integrator instance is available — actually running the survey connector and the integration connector end to end, which is the whole point of building the survey connector first.
6. **Docs.** A page under `site/docs` and a `connector-configuration-factory` entry, if the maintainers want the connector shipped in the default configuration.

## v1 scope (unchanged from #9245)

Authenticate, then list deployed **APIs** only. Proxy Services, Sequences, Endpoints, Inbound
Endpoints, Connectors (`GET /management/connectors`), Message Stores/Processors, Templates and Tasks
are follow-up increments. Read-only / inbound-only — nothing provisions or writes back.
