<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Swagger API Documentation

The [OMAG Server Platform](https://egeria-project.org/concepts/omag-server)
supports the OpenAPI/Swagger API documentation for all of its REST APIs.

## SpringDoc Usage

[springdoc-openapi](https://springdoc.org/) is used to generate the OpenAPI description at runtime for the
APIs that are deployed, along with a Swagger UI page to browse them.

### Gradle dependency

The following runtime dependency in the `build.gradle` file for **platform-chassis-spring** adds OpenAPI 3
generation and the Swagger UI, automatically scanning the deployed spring controllers - no additional
configuration bean is needed.

```groovy
runtimeOnly 'org.springdoc:springdoc-openapi-starter-webmvc-ui'
```

## Endpoints produced

The following endpoints are produced in the OMAG Server Platform.

| Endpoint               | Description                                                       |
|-------------------------|--------------------------------------------------------------------|
| /v3/api-docs             | The primary endpoint to retrieve the OpenAPI description that describes the API. |
| /swagger-ui.html         | Redirects to the Swagger UI page that gives a visual presentation of the APIs discovered. |
| /swagger-ui/index.html   | The Swagger UI page that gives a visual presentation of the APIs discovered. |



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
