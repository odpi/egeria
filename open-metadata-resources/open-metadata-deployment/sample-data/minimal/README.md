<!-- SPDX-License-Identifier: Apache-2.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Minimal

The Minimal samples provide a low volume set of examples for very basic initial testing purposes.

The samples themselves broadly fit into two categories:

- data: provided as both sample files and database tables
- metadata: provided as archives that can be loaded into one or more metadata repositories

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

## Included samples

Data:

- 1 databases, loadable to IBM DB2
- 1 sample file (semi-colon separated CSV)

Metadata:

- 2 sample IBM DataStage jobs:
    - 1 job loading from file to table
    - 1 job loading from table to file

## Loading the samples to your environment

Begin by reviewing the sample inventory (`hosts.sample`) and default configuration (`defaults/main.yml`), and
make any changes necessary to represent your own environment.  In particular:

- Create your own inventory (eg. `hosts`), placing your own host name(s) within it (using `hosts.sample` as a template)
- Replace the credentials in the `defaults/main.yml` file, ie. for the database user

Then, run the `deploy.yml` playbook to deploy everything (using Ansible):

```shell script
$ ansible-playbook -i hosts deploy.yml
```

If you have configured the [IGC Repository Connector](https://github.com/odpi/egeria-connector-ibm-information-server)
in advance of running the playbook, including the event mapper configuration, you will also
see events published into the cohort for these samples as well as part of the load process.

## Removing the samples from your environment

A removal playbook is also provided to clean the samples from your environment, allowing you
to repeat the load process if you so desire.

Run the removal playbook as follows:

```bash
ansible-playbook -i hosts remove.yml
```

Where the inventory provided (`hosts`) is as described above for loading.

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

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
