<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
# Egeria Workshop Day 1
### Introduction :
 - [Agenda](https://github.com/odpi/egeria/wiki/Egeria-Meeting-1st,-2nd-October-2019-@-Huizen,-Netherlands)

### Review changes to Asset zones and recent Asset management content
- Asset Management definition
- Avro File Support - different programming languages
- ZoneMembership into classification: [1459](https://github.com/odpi/egeria/issues/1459)
- Schema registry for EventBus stream support in future.
### Data Engine Proxy: 
- Config store using encrypted files [1646](https://github.com/odpi/egeria/issues/1646)
- General cache/state persistence mechanism. [\#1622](https://github.com/odpi/egeria/issues/1622) 
### Data Engine OMAS: 
Whether we need to differentiate `Process Template` from `process`. Check conclusions on issue [\#1576](https://github.com/odpi/egeria/issues/1576)
### Asset Lineage OMAS & Open Lineage Services
- How can we build connector or in egeria for open lineage services.
- Cassandra/Elasticsearch with JanusGraph for Lineage.
- Security and encrypted credentials. Probably we can reuse same method from [1646](https://github.com/odpi/egeria/issues/1646)
### UI Discussion
- Authorization approach: token vs session. The conclusion is we will support both solutions for Egeria then companies can choose their own method.
- We have agreed on the UI Exception strategy for all UI components
### Dev-ops OMAS
- physical vs software
- cloud provider/infrastructure vs deployedSoftwareServer/software
- [Digital service lifecycle](https://github.com/odpi/data-governance/tree/main/docs/data-privacy-pack#digital-service-lifecycle)
- [Digital services](https://github.com/odpi/data-governance/tree/main/docs/digital-services)
### Deduplication through stewardship server 
- How can we detect duplicate metadata instances for the same physical assets.[\#1650](https://github.com/odpi/egeria/issues/1650)
### Salesforce use case of CIM model
- Join development open source with Linux.
- JSON-LD scanned into open-metadata-archive.
   - See area 5 models upcoming.
   - see book on common information models by Mandy.
### SAS
- How SAS is creating metadata modelsOpen Source the SAS mappings
- Possibility of unify existing SAS metadata model 
- Prepackage of SAS container for evaluation purpose
### Magic Box of Security
- Column based security tag estimation
- Location of the asset could change the confidentiality and not linked to asset ownership

# Egeria Workshop Day 2
### Review the schema attribute/type changes, area 5
- Changes for SchemaElement/SchemaAttribute [\#1317](https://github.com/odpi/egeria/issues/1317)
- Changes for attributes to be marked as `deprecated` [\#1669](https://github.com/odpi/egeria/issues/1669)
### Education how the platform services work
- OMAG servers vs platform. 
- egeria-server-config.ipynb
### Multi-tenant UI
- Move config out of single application.properties and into config per servername
- UI Chassis
- Discussion of UI tenants and how to form URI/URL
- Common URL strategy, Https://hostname/server/view/subview
### Egeria Build Process
- Azure process may take a few days.
- 1.1 soon, then 1.2 a month later.
### Process of dependency management and security checks
- [azure pipeline](http://dev.azure.com/odpi/egeria)
- [pipelines link](https://dev.azure.com/odpi/egeria/_build)
- [jfrog repo](https://odpi.jfrog.io/odpi/egeria-snapshot/)
- [artifacts](https://odpi.jfrog.io/odpi/egeria-snapshot/org/odpi/egeria/)
- update to make java 11 builds mandatory.
- parallel builds for java 11 & java 8.
- [Nexus reports](https://nexus-iq.wl.linuxfoundation.org/assets/index.html#/applicationReport/odpi-egeria/7bc6f8a496d3444ea78a044d9ee1629d/policy)
    for dependencies.
### [Dependabot](https://dependabot.com/) for security dependency updates.
- It can make auto PRs for updates for CVEs for main.
- We can set how often to update.
- Conclusion is we will use it for Egeria and see how it can benefit us. 
### Reviewing security scan results
- OWASP reports and currently we can only have one 
### SonarQube checks into the mandatory build
- We use [sonar cloud](https://sonarcloud.io/dashboard?id=odpi_egeria) check for findbugs.
- We have agreed we should address issues from every functionality call on Thursday.
### Release 1.1 - when should we cut this
- See release [notes](https://egeria.odpi.org/release-notes/) 
### Egeria Blog
- [Blog](https://www.odpi.org/category/blog/egeria)



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
