#!/bin/sh
# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project
echo 'Adding additional python modules'
conda install -y pandas
echo 'Starting to load open metadata labs from github'
sudo apt update && sudo  apt -y install software-properties-common && \
  add-apt-repository ppa:git-core/ppa  && sudo apt update && sudo apt -y install git

# New implementation. 

# Setup the sparse clone
cd ${HOME}
if [[ ! -d egeria ]]
then
  git clone --no-checkout https://github.com/odpi/egeria
fi

# These are safe to run repeatedly
cd egeria
git sparse-checkout init --cone
git sparse-checkout set open-metadata-resources/open-metadata-labs

# refresh
git pull

# Always go directly to the tutorials
cd open-metadata-resources/open-metadata-labs

echo '*****************************************************'
echo '*                                                   *'
echo '* Egeria open metadata labs notebooks ready for use *'
echo '*                                                   *'
echo '*****************************************************'
