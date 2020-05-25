<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Docker tutorial

This tutorial explains how to use the Egeria Docker Container published to the
docker catalog at [https://hub.docker.com/r/odpi/egeria](https://hub.docker.com/r/odpi/egeria).

Egeria's docker container includes the Egeria install image.  When the container is started, an instance of
the Egeria runtime - that is the
[OMAG Server Platform](../../../open-metadata-implementation/admin-services/docs/concepts/omag-server-platform.md) -
is started at port http 8080.

This container can be incorporated into larger container orchestration environments or used standalone.
This tutorial describes how to use it standalone.  The [hands-on labs](../../open-metadata-labs)
use this container with either the `docker-compose` or `Kubernetes`
container orchestration services to create a complete open metadata solution.
Link to [Hands-on Labs Infrastructure Guide](../lab-infrastructure-guide) to learn more.

## Working with Egeria's docker image



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.