<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Running Egeria using docker-compose

With this approach, you do not need to download, install, configure and understand all of the underlying components
that Egeria itself uses.

Simply using Docker you can get up-and-running with Egeria (and our tutorials) without any other software downloads
or installations.

## Docker

[Docker](https://www.docker.com) is a platform for developing and running "containers", which for extreme
over-simplification can be thought of as lightweight virtual machines.

As part of the Egeria project, we make a number of pre-built Docker containers available as well as re-using
other projects' containers: these have all of the software you need (like the JRE, Jupyter, Zookeeper, Kafka, and
Egeria itself) pre-installed within them. Using these, you only need to run a single command in order to download
and run all of the pieces necessary to work through the tutorials -- the only piece of software you need to download
and install yourself is Docker.

Docker Compose is a utility for running multiple containers together in a coordinated
way (for example, all accessible to each other over the same private network).

Docker (and Compose) can be installed:

- On Windows and MacOS, by [downloading and installing Docker Desktop](https://hub.docker.com/?overlay=onboarding)
(which includes Docker Compose).
- On Linux operating systems, Follow instructions in the [appropriate community](https://hub.docker.com/search/?type=edition&offering=community) You will also need to install [Docker Compose](https://github.com/docker/compose/releases).

You do need to make sure docker has at least 4GB to use for the tutorials, possibly as much as 6GB if you are going to look at compliance testing. On Windows, Mac This can be modified by clicking on the docker for desktop icon & going to preferences/resources. On Linux as long as the system has sufficient memory no specific config is needed.

## Running the environment 

1. Download the latest [egeria release](https://github.com/odpi/egeria/releases) & unpack

1. From within the `open-metadata-resources/open-metadata-deployment/compose/tutorials` directory, run the command:

    ```bash
    $ docker-compose -f ./egeria-tutorial.yaml up
    ```

1. You may need to wait while some containers are downloaded . They will then be started, which will likely take less than a minute. Once you see the following lines:

    ```text
    core_1     | Thu Sep 26 19:33:54 GMT 2019 OMAG server platform ready for configuration
    datalake_1 | Thu Sep 26 19:33:55 GMT 2019 OMAG server platform ready for configuration
    dev_1      | Thu Sep 26 19:33:56 GMT 2019 OMAG server platform ready for configuration
    factory_1  | Thu Sep 26 19:33:57 GMT 2019 OMAG server platform ready for configuration

    ```

     or activity has subsided (as there are many log entries output), your self-contained environment should be ready.  Go to http://localhost:18888 to open the Jupyter interface and
    you are ready to [start the Open Metadata Labs](../../open-metadata-labs).

1. To shutdown the environment press `CTRL-C` in the console where it was started.  To remove the containers,
    again within the `tutorials` directory run the command:

    ```bash
    $ docker-compose -f ./egeria-tutorial.yaml down
    ```
1. To update to the latest version of the code you can refresh the images you have downloaded by:

    ```bash
    $ docker-compose -f ./egeria-tutorial.yaml pull
    ```

**Important note**: Be aware that each time you run the environment it will reset to a clean tutorial. So if you make
any changes within the Notebooks that you want to keep, be sure to save these to a local file on your computer before
shutting down.

## Help

For additional help refer to our slack channels at http://http://slack.lfai.foundation

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
