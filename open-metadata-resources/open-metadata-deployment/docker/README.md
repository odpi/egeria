<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# docker

This directory contains docker build scripts & assets for creating any images
needed for Egeria demos & deployments, where such assets do not exist on known
repositories .

## Building

Docker images are not built by default.

They can be built manually or via specific jenkins jobs

To build

mvn -Ddocker -Ddocker.repo=planetf1 clean deploy

In this example:
 * The '-Ddocker' tells maven (indirectly)to activate the docker profile. This is how the build is enabled since it is disabled by default, since many will not have docker or want to incur the time to rebuild the images. Indirectly refers to the fact we are setting a property, which then causes the profile activation in a submodule
 * The '-Ddocker.repo=' specifies the repository to use, for dockerhub this is usually your username for individuals. There is no default and if this is not supplied the build will exit with an error
 * 'deploy' is needed since the pushing of images is currently bound to this stage of the maven lifecycle
 * Optionally '-Ddocker.registry=' can be used to specify an alternative container registry UDL. For example a local or corporate registry, or perhaps a public cloud service. If not specified docker hub is assumed.
 ## Limitations
 * All docker builds will currently set the tag (version) to be the same as the maven version ie 1.1-SNAPSHOT. This means that when testing it is imperitive to always force pull fresh images, or an old version may be used. For example when using kubernetes ensure ImagePullPolicy = 'Always'
. Previously every single change was versioned, but this led to significant overhead in storage as well as constant changes in the source code just to do a rebuild.

* If DOCKER_HOST is set to a remote host connecton using ssh, the docker build may fail. Refer to https://github.com/spotify/dockerfile-maven/issues/272 for further information

* If building on MacOS, the OSX credential store is not supported for docker logins (which is needed to push an image to the repo). To workaround this remove the line '"credsStore": "osxkeychain",' from ~/.docker/config.json and reissue 'docker login'. See https://github.com/spotify/dockerfile-maven/issues/273 for further info
