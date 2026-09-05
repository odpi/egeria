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

## What is left

1. ~~Compile against a full Egeria build~~ — **done.** `./gradlew :...:wso2mi-connectors:compileJava` is `BUILD SUCCESSFUL` (JDK 17). The module builds cleanly against the framework.
2. **Auth wiring — open design point with the maintainers (#9245).** WSO2 MI's Management API is a two-step exchange: Basic-auth `GET /management/login` → bearer token → subsequent calls. The current `WSO2MIResourceConnector` obtains the base REST client from `RESTClientFactory` (which applies the configured Basic credentials) for the login call and holds the token on the connector. This needs to be reconciled with whatever session-token pattern Egeria prefers — Mandy pointed at the "client-side secret" pattern; the exact fit for a *username + password → session token* flow (rather than a static secret) is the question posted on #9245.
3. **Integration connector body.** Decide `IntegrationConnectorBase` (single instance, current stub) vs `DynamicIntegrationConnectorBase` + a catalog-target processor (as `OracleServerIntegrationConnector`). Then implement the `APIInfo` → asset mapping: resolve the open metadata type for a deployed REST API, apply the include/exclude filters, make it idempotent (skip already-catalogued APIs), emit the audit codes.
4. **Template types.** `WSO2MITemplateType` + template GUIDs, wired into the provider (`supportedTemplateTypes`), following `OracleTemplateType`.
5. **Tests.** `ffdc` `AuditCodeTest` / `ErrorCodeTest` (mirror the Oracle/UC ones); a mocked-REST test for `WSO2MIResourceConnector.login()` + `listAPIs()`.
6. **Docs.** A page under `site/docs` and a `connector-configuration-factory` entry, if the maintainers want the connector shipped in the default configuration.

## v1 scope (unchanged from #9245)

Authenticate, then list deployed **APIs** only. Proxy Services, Sequences, Endpoints, Inbound
Endpoints, Connectors (`GET /management/connectors`), Message Stores/Processors, Templates and Tasks
are follow-up increments. Read-only / inbound-only — nothing provisions or writes back.
