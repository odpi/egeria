<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Swagger API Documentation

The [OMAG Server Platform](https://egeria-project.org/concepts/omag-server)
supports the Swagger API for all of its REST 

## SpringFox Usage

[SpringFox](http://springfox.github.io/springfox/) is used to generate the swagger.json at runtime for the APIs that are deployed.  

### Maven dependencies

The following maven dependencies are added for the pieces needed for runtime display of the API.

#### SpringFox for Swagger 2 API 

This is configured using instructions from the 
[Springfox Swagger UI](http://springfox.github.io/springfox/docs/current/#springfox-swagger-ui) documentation.

The following dependency allows for Swagger 2 generation from existing spring controllers.

```xml
<!-- Spring Fox for Swagger API documentation generation -->
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger2</artifactId>
    <version>2.9.2</version>
</dependency>
```

#### SpringFox for Swagger UI

The following dependency allows for the Swagger UI to display the default generated API documentation.

```xml
<!-- Spring Fox Swagger UI dependency for presentation of generated swagger.json -->
<dependency>
    <groupId>io.springfox</groupId>
    <artifactId>springfox-swagger-ui</artifactId>
    <version>2.7.0</version>
</dependency>
```

### Code Additions

The following code was added to parse/enable the scanning for REST APIs in spring controllers. 

```java
@Bean
public Docket egeriaAPI()
{
    return new Docket(DocumentationType.SWAGGER_2)
            .select()
            .apis(RequestHandlerSelectors.any())
            .paths(PathSelectors.any())
            .build();
}
```

#### Customization

You can customize the endpoints/paths that are to be described by following the 
[Configuring SpringFox](http://springfox.github.io/springfox/docs/current/#configuring-springfox) documentation.

## Endpoints produced

The following endpoints are produced in the OMAG Server Platform. 

| Endpoint                                  | Module Dependency    | Description                                                                |
|-------------------------------------------|----------------------|----------------------------------------------------------------------------|
| /v2/api-docs                              | springfox-swagger2   | The primary endpoint to retrieve the swagger.json that describes the API.  |
| /swagger-resources                        | springfox-swagger2   | The endpoint describing what is produced by springfox for swagger.         |
| /swagger-ui.html                          | springfox-swagger-ui | The page that gives a visual swagger presentation of the APIs discovered.  |
| /swagger-resources/configuration/ui       | springfox-swagger-ui | The configuration/management API endpoint for the Swagger UI application.  |
| /swagger-resources/configuration/security | springfox-swagger-ui | The security management API endpoint for the Swagger UI application.       |



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
