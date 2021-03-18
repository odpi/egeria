# ui-chassis-spring

In the application properties file rules can be defined based on which the edges of the lineage graph will be inverted.
A rule must contain an edge type and can also contain the source node type and destination node type for further restrictions.
Eg:

```
lineage-display-config:
  rules:
    -
     edgeType: SemanticAssignment
     destinationNodeType: GlossaryTerm
     sourceNodeType: GlossaryCategory
```

In this example a rule is defined which will invert all the edges from the lineage graph of type SemanticAssignment
and have the destination node type GlossaryTerm and source node type GlossaryCategory.

[0] - [https://github.com/odpi/egeria-ui](https://github.com/odpi/egeria-ui)

# Start

Start the base server for the `ui-chassis-spring`.

```bash
$ git clone https://github.com/odpi/egeria-ui
$ cd egeria-ui
$ npm install
$ npm run build --api-url=http://api.production # or npm run start --api-url=http://api.production
```

```bash
$ mvn spring-boot:run -Dspring-boot.run.folders=/path/to/libs/ -Dspring-boot.run.arguments="--omas.server.name= --omas.server.url= --open.lineage.server.url= --open.lineage.server.name= --server.ssl.trust-store=/path/to/truststore.p12 --cors.allowed-origins=*"
```

# Start in development mode

```bash
$ git clone https://github.com/odpi/egeria-ui
$ cd egeria-ui
$ npm install
$ npm run start --api-url=http://api.production # will start a static server on http://localhost:8081

$ cd egeria/open-metadata-implementation/user-interfaces/ui-chassis/ui-chassis-spring/
$ mvn spring-boot:run -Dspring-boot.run.folders=/path/to/libs/ -Dspring-boot.run.arguments="--omas.server.name= --omas.server.url= --open.lineage.server.url= --open.lineage.server.name= --server.ssl.trust-store=/path/to/truststore.p12 --cors.allowed-origins=*"
```

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
