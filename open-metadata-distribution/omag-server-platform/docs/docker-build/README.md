<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Egeria's assembly

The contents of this directory were initially created by the gradle build script
from the [egeria.git](https://github.com/odpi/egeria) repository.
It has two parts to it:

* All the files and built images for egeria are under the `assembly` directory.  
  Each directory contains a `README.md` that describes its contents.
* A `Docker` build file used to create a docker image.
* A directory called `dist` that contains the start up script run when a docker image built
  from this directory is run.

## Creating a docker image

The `Docker` file provides the context for creating a docker image based on the files in the `assembly` directory. 

First add or remove the files from under the `platform` directory to suit your needs.
Then run `docker build -t egeria-platform:{tagName} -f Dockerfile .` in this
directory to create the image, replacing `{tagName}` with a name or date as a tag for this version of the docker image.
The docker build packages everything under `platform` into the docker image.

The docker build is created by a remote docker daemon and the resulting image is
found under the images managed by that daemon.  For example, if you are using
[Docker Desktop](https://www.docker.com/products/docker-desktop/),
the docker image is located in the **Images** tab.

To run the resulting image from the command line, use `docker run -p 9443:9443  odpi/egeria-platform:{tagName}`, again replacing `{tagName}` with your chosen tag.



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.