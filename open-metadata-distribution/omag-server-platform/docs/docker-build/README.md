<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Egeria's install image

The contents of this directory were initially created by the gradle build script
from the [egeria.git](https://github.com/odpi/egeria) repository.
It has two parts to it:

* All the files and built images for egeria are under the `deployment` directory.  
  Each directory contains a readme that describes its contents.
* A `Docker` build file used to create a docker image
* A directory called `dist` that contains the start up script run when a docker image built
  from this directory is run.

## Creating a docker image

The Docker file creates a docker image based on the files in the deployment directory.

First add or remove the files from under the `deployment` directory to suit your needs.
Then run `docker build -t egeria-platform:{myversion} -f Dockerfile .` in this
directory to create the image, replacing `{myversion}` with a name or date as a tag for this version of the docker image.

To run the resulting image use `docker run  -p 9443:9443  odpi/egeria-platform:{myversion}`.



----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the Egeria project.