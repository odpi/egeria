<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Apache Kafka (and Apache Zookeeper)

Apache Kafka is an event bus that is used to pass events between different Egeria servers.
It uses Apache Zookeeper to manage its components in a cluster.
Both technologies can be downloaded from Kafka's home page.

* [https://kafka.apache.org/](https://kafka.apache.org/)

There is a [QuickStart Guide provided by Kafka itself](https://kafka.apache.org/quickstart) covering
installation and basic usage.
You may alternatively wish to install kafka/zookeeper using a package manager such as 'homebrew' on MacOS.

For Egeria, the Kafka server needs to be running a `PLAINTEXT` listener on `localhost:9092`. From the directory
where Kafka is installed, check the `config/server.properties` file so that the `listeners` and `advertised.listeners`
are setup as follows:

```text
listeners=PLAINTEXT://localhost:9092
advertised.listeners=PLAINTEXT://localhost:5092
```


## Starting Kafka
Open a Terminal/Command window.
Change to the bin folder under Apache Kafka and run the following commands:

```bash
$ ./zookeeper-server-start.sh ../config/zookeeper.properties &
$ rm -rf /tmp/kafka-logs/*
$ ./kafka-server-start.sh ../config/server.properties
```

You should see Zookeeper and Kafka starting.

## Shutting down Kafka

```bash

$ ps
  PID TTY           TIME CMD
35119 ttys000    0:00.11 -bash
  828 ttys002    0:00.02 -bash
35770 ttys003    0:00.04 -bash
40213 ttys004    0:00.03 -bash
  859 ttys005    0:00.13 -bash
80637 ttys005    0:01.28 /Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/C
80929 ttys005    0:06.27 /Library/Java/JavaVirtualMachines/adoptopenjdk-8.jdk/C
  892 ttys007    0:00.06 -bash
  928 ttys010    0:00.09 -bash
$ kill -9 80637
$ kill -9 80929
$
```

----

* Return to [Tools](.)
----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.