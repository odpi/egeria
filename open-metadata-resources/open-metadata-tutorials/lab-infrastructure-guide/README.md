<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project. -->

# Guide to Running Egeria for the Hands on Labs

There are a number of approaches to running the environment used in
the [hands on labs](../../open-metadata-labs).
If you only want to get the environment running for the hands on labs, there are two choices:

* [Running Egeria in a self-contained environment using docker-compose](running-docker-compose.md).
* [Running Egeria in a self-contained environment using Kubernetes](running-kubernetes.md).

Using `docker-compose` is a more light-weight environment.  However the `kubernetes` environment
is likely to be closer to a production environment because, while docker-compose will start up
the environment, it does not actively manage it.  Kubernetes, on the other hand, is a full
orchestration service.

If you want to learn more about setting up egeria and its related technologies,
then this is the option for you ...

* [Running the Egeria ecosystem natively](running-natively.md).

## Further information

If you are entirely new to technologies like Git, Java, Maven, Kafka, ZooKeeper, Jupyter, Docker, Kubernetes, Helm, etc
the [Egeria Dojo](../egeria-dojo) provides self-paced education that covers these technologies
in the context of Egeria.


----
* Return to [Hands on Labs](../../open-metadata-labs)
* Try [Egeria Dojo](../egeria-dojo)
* Return to [Home Page](../../../index.md)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
