<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->


# Summary of BOM changes on 12/23/2024 for version 5.2 of Egeria
Not all changes suggested by Dependabot worked. The following summarizes the 
version updates that worked and those that will need further investigation and potentially code changes.
The BOM has been annotated to indicate proposed changes that did not work.

# Changes that worked
These version updates were successful.

| package                  | old version | new version     |
|--------------------------|-------------|-----------------| 
|  classgraphVersion       | 4.8.177       | 4.8.179     | 
|  commonsioVersion        | 2.16.1       | 2.18.0       | 
|   commonscliVersion    | 1.8.0           | 1.9.0      |
|   jenaVersion      | 5.0.0         | 5.2.0         |    
|   junitjupiterVersion    | 5.11.2        | 5.11.3   |   
|   jwtVersion      | 9.41.2        | 9.47          |   
|   kafkaVersion      | 3.7.0         | 3.9.0         |
|   lettuceVersion      | 6.3.2.RELEASE | 6.5.0.RELEASE |   
|    openlineageVersion      | 1.23.0        | 1.25.0    |   
|    postgresVersion     | 42.7.3        | 42.7.4        |  
|   nettyVersion      | 4.1.114.Final | 4.1.115.Final |   
|   prometheusVersion      | 1.13.6        | 1.14.2        |   
|   quartzVersion      | 2.3.2         | 2.5.0         |   
|  swaggerVersion       | 2.2.22        | 2.2.25        |   
|  jnrVersion       | 3.1.19        | 3.1.20        |   
| openhft | 2.26ea50 | 2.27ea5 | 


# Changes requiring further evaluation and potential changes
These changes either broke the build or caused an XTDB runtime error due to the Lucene
package renaming issue.

| package                | current version | proposed version | status                         |
|------------------------|-----------------|------------------|--------------------------------|
| jacksonDatabindVersion | 2.18.0          | 2.18.2           | xtdb runtime failure           |
| logbackVersion         | 1.5.6           | 1.5.8            | build breaks                   |
| cassandraVersion       | 4.1.5           | 5.0.1            | doesn't work                   |
| log4jVersion           | 2.24.1          | 2.24.3           | doesn't build                  |  
| jacksonjdk8Version     | 2.18.0          | 2.18.2           | runtime failure                |
| commonscodecVersion    | 1.16.0          | 1.17.0           | xtdb runtime failure - lucene  |
| commonstextVersion     | 1.11.0          |                  | xtdb runtime failure - lucene  |
| luceneVersion          | 8.11.3          |                  | xtdb runtime failure - lucene  |
|                        |                 |                  |                                |
|                        |                 |                  |                                |

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.