<!-- SPDX-License-Identifier: CC-BY-4.0 -->
<!-- Copyright Contributors to the Egeria project. -->

# Note

This directory contains 'index.yaml' and charts in .tgz format as created by:
* helm package lab
* helm repo index --url https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/charts .

This should ideally be done automatically as part of the build process, however this is not yet the case

Any consumers of these charts can do

helm repo add https://github.com/odpi/egeria/tree/master/open-metadata-resources/open-metadata-deployment/charts
helm install lab
