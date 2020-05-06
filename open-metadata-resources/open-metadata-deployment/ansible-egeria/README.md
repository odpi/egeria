<!-- SPDX-License-Identifier: Apache-2.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# ODPi Egeria ansible playbooks

## Introduction

Ansible playbook suite for deploying and managing ODPi Egeria OMAG based applications on native Linux environments. Currently it is tested on RedHat Enterprise Linux distribution.

### Playbooks

Two main playbooks grouping logical sequence of tasks form the related roles.

- `sites.yml` - deploy OMAG server application on site and configure it via admin-service.

- `sites_cleanup.yml` - un-deploy/clean-up OMAG server application form site.

Everything is configurable via set of variables representing one logical application group (i.e. OMAG server profile). For more advanced deployment configurations host groups can be used to configure/deploy multiple instances of OMAG server applications in integrated mode. See configuration and variables section or examples for more details below.

>PRIVILEGE ESCALATION: 
>With the current version of the playbooks it is assumed that the ansible user can do privilege escalation / sudo to manage system level changes like creating systemd configuration files and controlling (start/stop/restart) the service units. For long term the playbooks will be improved so this will become optional so the system changes can be done beforehand by system admins if needed.

## Usage

#### Deploy

``` ansible-playbook site.yml -i inventories/sample -e target=omagserver1``` 


This will trigger sequence of tasks to install, configure and start OMAG server instances on the target groups described as `omagserver1` under inventory.

There is also an option to separately execute sequences by adding following self descriptive tags: `install`,`configure`,`start`.

``` ansible-playbook site_cleanup.yml -i inventories/sample -e target=omagserver1 -t install,configure```

This will install and configure the OMAG application but it will not start it.

#### Un-deploy

``` ansible-playbook site_cleanup.yml -i inventories/sample -e target=omagserver1```

This will invoke sequence of tasks and remove all the files, logs and service related to the specific server application instance on the target systems described in the inventory as `omagserver1`.

>Note that the target is just another input variable mapped to hosts so you can also assign multiple targets as comma separated list like `omagserver1`,`omagserver2` or use the predefined group `all`.

## Roles

### egeria-omag-server

**Deployment** is handled by `egeria-omag-server` role. This is configurable role that will download all the artifacts needed to configure and start egeria OMAG server application. Once this role has been executed against target, specific version of the core Egeria application `server-chassis-spring.jar` will be installed on the pre-configured location. If configured additional Egeria plug-in libraries will be also installed and loaded. One example is `egeria-connector-ibm-information-server-package.jar` if available in the configuration will be downloaded under `libs` folder in the home install directory. System service with pre-configured application name will be also configured and started for you. See role specific configuration and variable section for more details.

The sequence of tasks: 
- downloading the application artifacts; 
- creating the application structure on the filesystem;
- downloading additional libraries;
- copying application properties/configuration files and 
- creating system service and starting up. 


### egeria-omag-server-admin

**Administration** is handled by `egeria-omag-server-admin` role. It looks up for specific `admin_omag_*` set of configuration variables and calls server admin REST API endpoints to create/update the configuration documents for each Egeria component. See role specific configuration and variable section for more details.

The sequence of tasks:
- Configure servers - uses `admin_omag_server_config` dictionary variable;
- Configure repositories - uses `admin_omag_repo_config` dictionary variable;
- Enable access services - uses `admin_omag_access_services_config` dictionary variable;
- Start server instance - uses `admin_omag_server_startup` list.

Each of these sequences depends on the presence of of specific var. If not present or empty the specific configuration phase is skipped.

>Currently the roles are embedded and maintained into this project. Once they get more stable and mature can be also available via ansible-galaxy for other ansible projects using Egeria.

## Configuration and variables

### egeria-omag-server

`atr_artifactory_base` - Source Artifactory url. Default: `https://odpi.jfrog.io/odpi`

`atr_repository` - Source Artifactory repository. Default: `egeria-snapshot-local`

`home_dir` - Target server home directory (installation base). Default: `/opt/odpi/egeria`

`app_host` - Application host address or dns name on the target server.

`app_port` - Application port on the target server.

`app_name` - Application name. Also used for systemd service name.

`app_version`- Version of the egeria core executable (jar).

`app_version_suffix` - Version suffix (helps for testing snapshot versions). <!--#TODO: add more details-->

`app_force_replace` - Forces overwriting the destination artifacts. Useful for development and testing especially when snapshot versions are rebuilt.

`app_extra_libs` - Additional egeria plugin library (i.e. connectors).

`app_configs`-  Location of the application configuration files to be applied on the target server. <!--#TODO: add more details-->


### egeria-omag-server-admin

`admin_omag_server_config` - OMAG Server core configuration object. 
It can be used to define multiple server configurations (different logical servers). See Examples for details.

<!--
#TODO: add more details on how mode works and possible options
-->

`admin_omag_repo_config` - OMRS, repositories configuration object. You can define multiple repository configurations for servers. See Examples for details.

`admin_omag_access_services_config` - OMAS, access services configuration object. You can define multiple access services to be enabled per server or simply enable 'all' available. See Examples for details.

`admin_omag_server_startup` - List of services to initialize after configuration is performed. See Examples for details.


## Examples

**Example A.** OMAG Server with Asset Catalog OMAS and Local Graph Repository

```
app_port: 8080
app_host: localhost
app_name: egeria_omas
app_version: 1.6-SNAPSHOT
app_version_suffix: 1.6-SNAPSHOT
app_extra_libs:
app_configs:
admin_omag_server_config:
  omas-server:
    mode: server-chassis
    url: "http://{{ app_host }}:{{ app_port }}"
    user: admin
    cohort: mycohort
    cohort_topic: mytopic
    consumer: kafkaserver:9092
    producer: kafkaserver:9092
    auditlog_destination: slf4j
    auditlog_severity: []    
admin_omag_repo_config:
  omas-server:
    mode: local-graph-repository
admin_omag_access_services_config:
  omas-server:
    # all:
    asset-catalog:
      body: 
        supportedTypesForSearch:
          - Asset
          - RelationalColumn 
admin_omag_server_startup:
- omas-server
```

Check out the sample configuration `inventories/sample/hosts.yml` and adjust ansible_ vars to so you can connect to your target environment.
To deploy,configure and start the sample configuration run following command:

```
ansible-playbook sites.yml -i inventories/sample -e target=omagserver1
```

To un-deploy the sample configuration run following command:

```
ansible-playbook sites_cleanup.yml -i inventories/sample -e target=omagserver1
```