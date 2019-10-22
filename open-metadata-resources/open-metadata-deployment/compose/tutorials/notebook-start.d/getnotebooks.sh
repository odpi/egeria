#!/bin/sh
# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project
echo 'Starting to load open metadata labs from github'
sudo  apt-get -y update && sudo apt-get -y install git
cd 
rm -fr egeria
git init . 
git remote add origin https://github.com/odpi/egeria.git 
git config core.sparsecheckout true 
echo 'open-metadata-resources/open-metadata-labs/*' >> .git/info/sparse-checkout 
git pull --depth=1 origin master 
mv open-metadata-resources/open-metadata-labs/* . 
rm -rf open-metadata-resources 
rm -rf .git
echo 'Egeria open metadata labs successfully installed from github'
