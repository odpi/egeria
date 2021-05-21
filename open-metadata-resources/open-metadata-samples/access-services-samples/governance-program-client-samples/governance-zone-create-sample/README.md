<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Governance Zone Set Up

As part of the set up of the [Coco Pharmaceuticals Governance Program](https://opengovernance.odpi.org/coco-pharmaceuticals/),
[Erin Overview](https://opengovernance.odpi.org/coco-pharmaceuticals/personas/erin-overview.html), the Information Architect at
Coco Pharmaceuticals, is setting up the
[Governance Zone Definitions](../../../../../open-metadata-implementation/access-services/docs/concepts/governance-zones).

The governance zones defines are as follows:

* **personal-files**
  * Display Name: Personal Files Zone
  * Description: Assets that are for an individual's use.  Initially the creator of the asset is the owner.
                 This person can reassign the asset to additional zones to increase its visibility or
                 reassign the ownership.
  * Criteria: Assets that should only be visible and editable to the owner.

* **quarantine**
  * Display Name: Quarantine Zone
  * Description: Assets from third parties that are being evaluated by the onboarding team.
                 The assets will move into the other zones once the asset has been catalogued and classified.
  * Criteria: Data sets just received and that are not yet properly catalogued.

* **data-lake**
  * Display Name: Data Lake Zone
  * Description: Assets for sharing that are read only.
  * Criteria: These are production assets that can be used for business decisions.

* **research**
  * Display Name: Research Zone
  * Description: Research data sets and findings
  * Criteria: Assets that are driving the development of new products and techniques.

* **clinical-trials**
  * Display Name: Clinical Trials Zone
  * Description: Patient data, protocols, outcomes and analysis used within a clinical trial.
                 This data is highly confidential and has restricted access.  It is also subject
                 to the data management requirements of the regulators.
  * Criteria: Asset supporting the clinical trials.

* **human-resources**
  * Display Name: Human Resources (Personnel) Zone
  * Description: Assets used to manage and support employees of Coco Pharmaceuticals.
  * Criteria: Assets controlled by the HR and management teams.

* **finance**
  * Display Name: Finance Zone
  * Description: Assets that support the financial management of Coco Pharmaceuticals.
  * Criteria: Assets controlled by the finance team.

* **infrastructure**
  * Display Name: IT Infrastructure Zone
  * Description: Assets that describe the IT infrastructure such as hosts, servers, applications
                 databases and network infrastructure descriptions.
  * Criteria: Assets controlled by the IT Infrastructure team.

* **development**
  * Display Name: Development and DevOps Zone
  * Description: Software development components and assets that support their ongoing development.
  * Criteria: Software development and devops assets.

* **manufacturing**
  * Display Name: Supply, Manufacturing and Distribution Zone
  * Description: Suppliers, manufacturing infrastructure, schedules and outputs.
  * Criteria: These are the assets that support the production of Coco Pharmaceutical's products.

* **sales**
  * Display Name: Sales Zone
  * Description: Customers, sales plans, orders and fulfilment tracking.
  * Criteria: Assets supported by the sales teams.

* **governance**
  * Display Name: Governance Zone
  * Description: Governance definitions, monitoring and reporting assets.
  * Criteria: Assets that support the governance team

* **trash-can**
  * Display Name: Trash Can Zone
  * Description: Asset that are in a holding zone ready to be deleted.
  * Criteria: Assets that are no longer required
                
These governance zones are defined in an enum called `GovernanceZoneSampleDefinitions.java` to make it easy to
change if you want to use the sample to define your own governance zone definitions.
     
----

* [Return to Governance Program Samples](..)


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.