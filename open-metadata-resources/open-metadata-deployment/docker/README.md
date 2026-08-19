<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# docker

This directory contains the docker build assets for the images needed by Egeria demos and deployments,
where such assets do not exist on known repositories.

* **[platform](platform)** - the `Dockerfile` and start up script (`dist/entrypoint.sh`) for the
  *OMAG Server Platform* image that is published to
  [DockerHub](https://hub.docker.com/r/odpi/egeria-platform) and
  [Quay.io](https://quay.io/repository/odpi/egeria-platform?tab=tags&tag=latest).

## Building

The docker image is not built by the Gradle build.  Instead, the `Dockerfile` and its start up script are
copied into the root of the *omag-server-platform* distribution by the
[omag-server-platform](../../../open-metadata-distribution/omag-server-platform) assembly, alongside the
`assembly` directory that holds the files to package.

To build the image, unpack the distribution tar file, change to its root directory - the one containing
both `Dockerfile` and `assembly` - and run:

```bash
docker build -t egeria-platform:{tagName} -f Dockerfile .
```

replacing `{tagName}` with a name or date to tag this version of the image.  The build packages everything
under `assembly/platform` into `/deployments` in the image, so add or remove files there first if you want to
tailor the image - for example, to add your own connectors under `assembly/platform/lib`.

To run the resulting image:

```bash
docker run -p 7443:7443 egeria-platform:{tagName}
```

See [platform/README.md](platform) for the runtime parameters the image supports, how to persist data
across container restarts, and how to add extra libraries.

## Limitations

- The published images are tagged with the Egeria version (for example `6.1`).  This means that when
  testing against a snapshot build it is important to always force-pull fresh images, or an old version may be
  used.  For example, when using Kubernetes ensure `imagePullPolicy = 'Always'`.

## Kubernetes

Helm charts to deploy Egeria along with supporting infrastructure can be found at
https://github.com/odpi/egeria-charts .

----
* Return to [open-metadata-deployment](..)

----
License: [CC BY 4.0](https://creativecommons.org/licenses/by/4.0/),
Copyright Contributors to the ODPi Egeria project.
