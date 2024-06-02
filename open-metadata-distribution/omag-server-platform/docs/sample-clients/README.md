<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Sample clients

The open metadata samples illustrate how to use the various APIs of Egeria.
When you run them, they display help information describing the parameters
needed to run them successfully.

* **asset-create-avro-sample** catalogues an Apache Avro file in the open metadata ecosystem as an Asset.  This sample uses the [Asset Owner OMAS](https://egeria-project.org/services/omas/asset-owner/overview/).
* **asset-create-csv-sample** catalogues a CSV file in the open metadata ecosystem as an Asset.  This sample also uses the [Asset Owner OMAS](https://egeria-project.org/services/omas/asset-owner/overview/).
* **asset-deploy-sample** illustrates the use of the [IT Infrastructure OMAS](https://egeria-project.org/services/omas/it-infrastructure/overview/) APIs to catalog different types of infrastructure.
* **asset-look-up-sample** illustrates the use of the [Asset Consumer OMAS](https://egeria-project.org/services/omas/asset-consumer/overview/) APIs to search for and display the metadata linked to an Asset.
* **asset-reader-avro-sample** illustrates the use of the [Asset Consumer OMAS](https://egeria-project.org/services/omas/asset-consumer/overview/) APIs to create connectors to Avro files.
* **asset-reader-csv-sample** illustrates the use of the [Asset Consumer OMAS](https://egeria-project.org/services/omas/asset-consumer/overview/) APIs to create connectors to CSV files.
* **asset-set-up-sample** illustrates the use of a variety of [OMASs](https://egeria-project.org/services/omas/) APIs to catalog files.
* **config-metadata-server-sample** illustrates the use of the [Administration Services](https://egeria-project.org/services/admin-services/overview) APIs to create a configuration document for a Metadata Access Store.
* **glossary-manager-samples** show different styles of managing changes to glossary terms.
They are illustrated through the [Asset Manager OMAS glossary management API](https://odpi.github.io/egeria/org/odpi/openmetadata/accessservices/assetmanager/api/management/GlossaryManagementInterface.html).
This interface is used by [Glossary Manager OMVS](https://egeria-project.org/services/omvs/glossary-manager/overview) to provide the backend services for UI that provide complex management interfaces.
* **governance-leadership-sample** shows the appointment of different types of governance officers over time.  It uses the APIs from [Governance Program OMAS](https://egeria-project.org/services/omas/governance-program/overview/).
* **governance-subject-area-sample** shows the creation of Coco Pharmaceuticals' subject area definitions.  It uses the APIs from [Governance Program OMAS](https://egeria-project.org/services/omas/governance-program/overview/).
* **governance-zone-create-sample** shows the creation of Coco Pharmaceuticals' governance zone definitions.  It uses the APIs from [Governance Program OMAS](https://egeria-project.org/services/omas/governance-program/overview/).
* **subject-area-categories-sample** shows how to create a hierarchy of categories using the [Subject Area OMAS](https://egeria-project.org/services/omas/subject-area/overview/).


These sample clients need access to certifications to validate the platform
they are calling.  Copy the JAR files for the samples into the `platform` directory:

```bash
cp *.jar ../../platform
```
Change to the `platform` directory.
```bash
cd ../../platform
```

Then, once you have the platform running, you can run the samples with the following command:

```bash
java -Djavax.net.ssl.keyStore=keystore.p12 -Djavax.net.ssl.keyStorePassword=egeria -Djavax.net.ssl.trustStore=truststore.p12 -Djavax.net.ssl.trustStorePassword=egeria -jar {jarFileName}
```
Replace {jarFileName} with then name of sample's JAR file that you wish to run.
For example, to run the `asset-set-up-sample` enter:

```bash
java -Djavax.net.ssl.keyStore=keystore.p12 -Djavax.net.ssl.keyStorePassword=egeria -Djavax.net.ssl.trustStore=truststore.p12 -Djavax.net.ssl.trustStorePassword=egeria -jar asset-set-up-sample.jar
```


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.