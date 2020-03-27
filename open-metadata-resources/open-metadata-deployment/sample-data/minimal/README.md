<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Minimal

The Minimal samples provide a low volume set of examples for very basic initial testing purposes. For the moment
these are focused on efforts for testing lineage across various connectors, and are thus starting with a minimal
set of targets to which to deploy.

The samples themselves include the following:

- 1 database:
    - `MINIMAL` containing all of the tables below
- 3 database tables (IBM DB2):
    - `EMPLNAME` with columns: `EMPID`, `FNAME`, `SURNAME`, `LOCID`
    - `WORKPLACE` with columns: `LOCID`, `LOCNAME`
    - `EMPLDIRECTORY` with columns: `EMPID`, `EMPNAME`, `LOCID`, `LOCNAME`
- 2 files under the `/data/files/minimal` directory (by default):
    - `names.csv` with fields: `Id`, `First`, `Last`, `Location`
    - `locations.csv` with fields: `Id`, `Name`
- 3 ETL jobs (IBM DataStage) under a project named `minimal`:
    - `flow1` loads the `names.csv` file into the `EMPLNAME` table
    - `flow2` loads the `locations.csv` file into the `WORKPLACE` table
    - `flow3` joins the `EMPLNAME` and `WORKPLACE` tables and loads the result to `EMPLDIRECTORY`
- 1 ETL abstraction (IBM DataStage "sequence") under the `minimal` project:
    - `sequence` orchestrates the execution of `flow1`, `flow2` and then `flow3`

## Requirements

If you want to make use of a pre-existing IBM InfoSphere Information Governance Catalog
repository, you will need:

- Ansible v2.4.x (ideally the latest v2.7.x+)
- Network access to the IBM Information Server environment
- Inventory group names setup the same as `IBM.infosvr` role
- Following Ansible roles installed:
  - [IBM.infosvr](https://galaxy.ansible.com/IBM/infosvr)
  - [IBM.infosvr-metadata-asset-manager](https://galaxy.ansible.com/IBM/infosvr-metadata-asset-manager)
  - [IBM.infosvr-import-export](https://galaxy.ansible.com/IBM/infosvr-import-export)

**Important**: Begin by reviewing the sample inventory (`hosts.sample`) and default configuration (`defaults/main.yml`), and
make any changes necessary to represent your own environment.  In particular:

- Create your own inventory (eg. `hosts`), placing your own host name(s) within it (using `hosts.sample` as a template)
- Replace the credentials in the `defaults/main.yml` file, ie. for the database user

## General deployment ordering

For repeatability, ie. to achieve the same starting point each time, follow this general ordering for deployment:

1. Shutdown your OMAG Server Platform that will run the IGC connector (proxy).
1. Empty your OMRS Kafka topic.
1. Remove your OMAG Server Platform configuration files (`*.config`, `*.registrystore`).
1. Run the samples removal script (see "Removing the samples from your environment" below) against your IBM Information
    Server environment.
1. Start your OMAG Server Platform.
1. Configure the IGC connector (proxy) in the OMAG Server Platform.
1. Run the samples deployment script (see "Loading the samples to your environment" below) against your IBM Information
    Server environment.

Should you want to "reset" your environment, simply re-run these steps.

## Loading the samples to your environment

### 1. Load the sample data

Assuming you have configured the settings as listed under "Requirements" above, run the `deploy.yml` playbook to
deploy everything (using Ansible):

```shell script
$ ansible-playbook -i hosts deploy-data.yml
```

This only needs to be done once and simply makes the data available, but does not harvest or
process any metadata.

### 2. Load the sample metadata, flow-by-flow

You can then load the flow-specific metadata, flow-by-flow, using the following:

```shell script
$ ansible-playbook -i hosts deploy-flowX.yml
```

Note that the flows are intended to build on each other, so you should really run this with
`deploy-flow1.yml` first, then `deploy-flow2.yml`, and so on.

If you have configured the [IGC Repository Connector](https://github.com/odpi/egeria-connector-ibm-information-server)
in advance of running the playbook, including the event mapper configuration, you will also
see events published into the cohort for these samples as well as part of the load process.

The flows build upon each other as follows:

- `deploy-flow1` contains only the `flow1` ETL job loading from file to table
- `deploy-flow2` adds another file-to-table ETL job (`flow2`) and also a process abstraction (`sequence`)
- `deploy-flow3` adds a table-to-table ETL job that includes a join (`flow3`), and updates `sequence`
- `deploy-flow4` updates the logic of `flow3` by adding a transformation that concatenates two columns after the join

Since they are cumulative, each latter flow attempts to re-load any objects needed from prior
flows, in case they do not already exist. In other words, from a clean environment, if you want
to jump straight to the `flow3` situation you can simply run `deploy-flow3` directly. Just be aware
that in the case where a flow contains updates to previous artifacts, by directly deploying a latter
flow without first loading the previous flows the update will not be done but simply an addition.

## Removing the samples from your environment

### 1. Removing the flows and metadata

A removal playbook is also provided to clean all of the minimal samples from your environment,
allowing you to repeat the load process if you so desire. (Note that these are not removed
flow-by-flow, but all at once.)

Again, assuming you have configured the settings as listed under "Requirements" above, run the removal playbook as
follows:

```shell script
$ ansible-playbook -i hosts remove.yml
```

Note that to fully remove artifacts from an IGC environment you must also allow import areas to
be deleted from IBM Metadata Asset Manager. This requires a manual configuration change as follows
(which must be completed before running the removal playbook):

!["Administration"](../docs/ibm-mam-enable-delete.png)

1. Navigate to the "Administration" tab.
1. Open the "Import Settings" under Navigation.
1. Tick the box next to "Allow users to delete import areas in which imports were shared to the repository".
1. Click the "Save" button.

The following metadata will also remain, as it cannot be deleted via API.  You can either leave it
(it will not impact re-deploying) or you can manually delete it from the environment using the appropriate
client.

- The local file system data connection (can only be deleted via Metadata Asset Manager)
- The database data connection (can only be deleted via Metadata Asset Manager)

### 2. Removing the underlying data

It is also possible to remove the underlying data (ie. the databases and the files). To do this,
run the following playbook:

```shell script
$ ansible-playbook -i hosts remove-data.yml
```

Note that this should only be done after first removing the metadata (above).

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
