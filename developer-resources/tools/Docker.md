<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the ODPi Egeria project 2020. -->

# Docker, docker-compose and DockerHub

**Docker** is a [simple container runtime and standard](https://www.docker.com/why-docker).
Every day, the egeria build processing creates
a docker image of egeria and pushes it to the docker catalog on the **Docker website**.

This docker image provides a simple way to bring a runnable version of Egeria onto your
machine.  It also provides the basis for a [Kubernetes](Kubernetes.md) deployment
of Egeria.

The docker catalog page for egeria is at
[https://hub.docker.com/r/odpi/egeria](https://hub.docker.com/r/odpi/egeria).
The **Overview** tab describes the docker container.

![Egeria Docker Page Overview Tab](egeria-docker-page-overview.png#pagewidth)

The **Tags** tab shows the different releases that are available.

![Egeria Docker Page Tags Tab](egeria-docker-page-tags.png#pagewidth)

----

### Getting the docker runtime

The docker image needs a runtime to execute.  It can run in the cloud using **Docker Hub** or
on your machine using the **Docker Desktop**. 
The description below describes how to use the docker desktop.

Docker Desktop supports running a docker image as a standalone container, or as part of group of containers
started with **docker-compose**.

Go to the [Docker website](https://www.docker.com/why-docker) and select
the **Download Docker Desktop**.

![DockerHub home page](docker-homepage.png#pagewidth)

Follow the instructions for you operating system.  For macOS, the docker desktop
is installed like a standard application.

![macOS install of docker desktop](docker-desktop-install.png#pagewidth)

Once it is installed, it can be launched like any
application, such as through the launchpad/start menu.

## Next steps

If you are working through the Egeria Dojo, you can
return to the guide for [Day 1 of the Egeria Dojo](../../open-metadata-resources/open-metadata-tutorials/egeria-dojo/egeria-dojo-day-1-3-1-1-platform-set-up-prerequisites.md)

Otherwise, if you are looking for guidance on
using the Egeria docker image, link to the [Docker tutorial](../../open-metadata-resources/open-metadata-tutorials/docker-tutorial)
to get docker running.  It describes
how to install, set up docker and make use of Egeria's docker image.

----
* Return to [Developer Tools](.)

---

* Link to the [Egeria Dojo Education](../../open-metadata-resources/open-metadata-tutorials/egeria-dojo)
* Link to the [Setting up the Hands-on Lab Infrastructure](../../open-metadata-resources/open-metadata-tutorials/lab-infrastructure-guide)
tutorial that uses docker.

----
* Link to [Egeria's Community Guide](../../Community-Guide.md) to find out more about joining the Egeria community.


----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.