<!-- SPDX-License-Identifier: Apache-2.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Coco Pharmaceuticals

The Coco Pharmaceuticals samples follow the examples outlined in the Egeria tutorials and
are based on the description of the imaginary company and its employees in the
[ODPi Data Governance Project](https://odpi.github.io/data-governance/).

The samples themselves are provided as a set of Ansible playbooks for automating the load
and verification across the various components involved.

## Requirements

- Ansible v2.4.x (ideally the latest v2.7.x+)

When making use of the IBM Information Governance Catalog ("IGC"), you will also need:

- Network access to an IBM Information Server environment
- Inventory group names setup the same as `IBM.infosvr` role
- Following Ansible roles installed:
  - [IBM.infosvr](https://galaxy.ansible.com/IBM/infosvr)
  - [IBM.infosvr-metadata-asset-manager](https://galaxy.ansible.com/IBM/infosvr-metadata-asset-manager)
  - [IBM.infosvr-import-export](https://galaxy.ansible.com/IBM/infosvr-import-export)

## Included samples

Includes the following sample content:

- Glossary categories
- Glossary terms
- Term-to-term relationships
- Sample data files
- Sample databases, supporting a variety of database options
- Sample data within the database(s)
- Relationships between terms and file fields
- Relationships between terms and database columns

IBM Information Governance Catalog-specific:

- IBM Information Analyzer project containing the sample database tables
- IBM Information Server users
- IBM Information Server groups
- IBM IGC stewards

## Loading the samples to your environment

See `defaults/main.yml` for the default locations and names of the physical assets (database and files).
These can either be overridden directly in this file, or by using any of Ansible's mechanisms for 
overriding variables.

Running the playbook is as simple as:

```bash
ansible-playbook [-i hosts] deploy.yml
```

Where the inventory provided should contain at a minimum the following group names:

- `egeria-samples-db-host` group, defining the host containing the database into which the sample data should be loaded.
- `egeria-samples-files-host` group, defining the host where the sample data files should be loaded.

If using IGC as a metadata repository, the inventory should also contain the group names required by
the `IBM.infosvr` role for the repository, engine and domain (services) tiers.
(See `hosts.sample` for an example, re-using the IBM DB2 installation behind Information Server itself
as the location for the sample data and with all tiers of IBM Information Server running on a single host.)

The playbook will automatically create (or update) all of the needed assets, idempotently,
as well as kicking off the harvesting of metadata through IBM Metadata Asset Manager and
(in v11.7+) the automated analysis of the database samples through Information Analyzer.

If you have configured the [IGC Repository Connector](../../../../open-metadata-implementation/adapters/open-connectors/repository-services-connectors/open-metadata-collection-store-connectors/ibm-igc-repository-connector/README.md)
in advance of running the playbook, including the event mapper configuration, you will also
see events published into the cohort for all of these samples as well as part of the load 
process.

## Removing the samples from your environment

A removal playbook is also provided to clean the samples from your environment, allowing you
to repeat the load process if you so desire.

Run the removal playbook as follows:

```bash
ansible-playbook [-i hosts] remove.yml
```

Where the inventory provided is as described above for loading.

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
