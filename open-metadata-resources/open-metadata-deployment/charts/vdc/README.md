<!-- SPDX-License-Identifier: Apache-2.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->
**Chart for full VDC stack**

This helm chart will deploy the following components:
* Apache Kafka
* An egeria Cohort with the following participants, all connected to the Kafka event bus & configured to exchange events & interoperate
    * Egeria
    * Apache Atlas (with native connector/event mapper)
    * IGC Event mapper & Connector (same Egeria code)
* Apache Ranger (mariaDB for storage, Solr for indexing)
* GaianDB
* PostGresSQL with sample data from coco pharmaceuticals

Additionally IBM Governance Catalog (IGC) 11.7 is required externally. Metadata from the postgresql data will be loaded into IGC automatically, as well as a suitable glossary for coco pharma. Ensure that vdc/templates/configmap.yaml is updated with the correct IP addresses for IGC.

You will require a kubernetes environment to install to. Testing has been done so far against
* IBMCloud (IKS)
* IBM Cloud Private (ICP 3.1)

This chart should work with other providers with the exception of the definitions around ingress/load balancing - which are how ports are made available external to the cluster

**Caveats**

Ranger (including usersync, sync with egeria), Gaian, LDAP are currently not configrred

**Additional configuration**

* vdc/values.yaml is the main place to configure preferences. It is recommended to only change what you need
* vdc/templates/configmap.yaml currently contains some additional configuration which is not pulled in from values. 
**Required helm repositories (prereq)**

```
helm repo add confluent https://confluentinc.github.io/cp-helm-charts/
helm repo update
```
**Installing a chart**

```
cd open-metadata-resources/open-metadata-deployment/charts
helm dependency update vdc
helm install vdc --name vdc
```
Note: If name is not specified an auto-generated name like 'slippery-lizard' will be used.

*** Load Balancing & Ingress ***

IBM Cloud

See https://cloud.ibm.com/docs/containers/cs_ingress.html#ingress




