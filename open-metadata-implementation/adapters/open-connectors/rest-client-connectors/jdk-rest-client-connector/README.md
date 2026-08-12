<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2026. -->

# JDK REST Client Connector

`JDKRESTClientConnector` is the [REST client connector](../rest-client-connectors-api) implementation
that [`RESTClientFactory`](../rest-client-factory) hands out by default, so this is the connector that
actually makes the HTTP call for every Egeria client - OMAS, OMVS, OMES and integration connector alike -
unless the factory has been deliberately pointed at the [Spring-based connector](../spring-rest-client-connector)
instead.

## Why this exists alongside the Spring connector

It is built directly on the JDK's own `java.net.http.HttpClient` rather than Spring's `RestTemplate`, for
three reasons:

* **No third-party HTTP dependency.** `java.net.http.HttpClient` has been part of the JDK since Java 11,
  so Egeria's core REST call path no longer needs Spring at all.
* **PATCH actually works.** `RestTemplate`'s default `ClientHttpRequestFactory` wraps
  `java.net.HttpURLConnection`, which rejects the HTTP PATCH method outright
  (`ProtocolException: Invalid HTTP method: PATCH`). `java.net.http.HttpClient` has no such restriction,
  which is what makes it possible to complete connectors - such as the Unity Catalog connectors - that
  need to issue a PATCH.
* **Deserialization errors are not swallowed.** This connector calls Jackson's `ObjectMapper` directly to
  turn a response body into the expected class. Spring's `RestTemplate` interposes its own
  `HttpMessageConverter`/`RestClientException` handling in between, which tends to bury Jackson's own
  detailed error message (which field, which path, what type was expected) several levels down the
  exception's cause chain. Calling Jackson directly means that detail is exactly what callers see.

## Other implementation notes

* **URL placeholder encoding.** Values substituted into a `{0}`, `{1}`, ... placeholder are percent-encoded
  per RFC 3986 - every character outside the unreserved set is escaped, including a literal `+`, which is
  escaped to `%2B` rather than being left as a bare `+` (which some servers interpret as a space). This is
  what makes it safe to pass a regular expression containing a `+` - for example in `searchCriteria` - as
  a URL parameter.
* **Non-2xx HTTP status.** Most Egeria-to-Egeria REST calls report errors through a 200 response whose
  body encodes the failure (`relatedHTTPCode`, `exceptionClassName`, and so on), so this rarely triggers -
  but for calls to genuinely external REST APIs (Apache Atlas, Unity Catalog, ...) a non-2xx status is
  reported as a `RESTServerException` that includes the actual status code and response body, rather than
  attempting to deserialize an error page as if it were a successful response.
* **Connection reuse.** A single `HttpClient` is built once per connector instance and reused for every
  call, so connections are pooled rather than opened fresh each time (`RestTemplate`'s default factory,
  by contrast, is backed by `HttpURLConnection` and does not pool connections).

Return to [rest-client-connectors](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
