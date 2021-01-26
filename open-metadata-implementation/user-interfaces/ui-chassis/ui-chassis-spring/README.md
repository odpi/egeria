# ui-chassis-spring

⚠️ The UI included in this spring-boot app under `/src/main/static` has been extracted and moved in a separate repository here [0]. ⚠️

In order to have working UI for this module you need to pass new arguments upon starting the application.

Arguments: `--zuul.routes.ui.url=https://ui.production`.

Also you need to make sure you have deployed statically the egeria-ui [0] project at a given URL (e.g. https://ui.production) so
that it can be linked to.

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
$ npm run build
$ ls ./build/prod/
$ # deploy the build/prod folder in a static server under a given URL (e.g. https://ui.production)
```

```bash
$ mvn spring-boot:run -Dspring-boot.run.folders=/path/to/libs/ -Dspring-boot.run.arguments="--theme=default --zuul.routes.ui.url=https://ui.production --omas.server.name= --omas.server.url= --open.lineage.server.url= --open.lineage.server.name= --server.ssl.trust-store=/path/to/truststore.p12"
```

# Start in development mode

```bash
$ git clone https://github.com/odpi/egeria-ui
$ cd egeria-ui
$ npm install
$ npm run start:dev # will start a static server on http://localhost:8081

$ cd egeria/open-metadata-implementation/user-interfaces/ui-chassis/ui-chassis-spring/
$ mvn spring-boot:run -Dspring-boot.run.folders=/path/to/libs/ -Dspring-boot.run.arguments="--theme=default --zuul.routes.ui.url=http://localhost:8081 --omas.server.name= --omas.server.url= --open.lineage.server.url= --open.lineage.server.name= --server.ssl.trust-store=/path/to/truststore.p12"
```

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
