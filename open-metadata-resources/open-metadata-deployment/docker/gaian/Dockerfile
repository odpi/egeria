# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project.

FROM openjdk:8-jre-alpine

# Working directory
WORKDIR /root/gaian
RUN wget https://github.com/gaiandb/gaiandb/blob/master/build/GAIANDB_V2.1.8_20160523.zip?raw=true -O GAIANDB.zip
RUN unzip GAIANDB.zip
RUN apk --no-cache add bash && apk --no-cache update && apk --no-cache upgrade

EXPOSE 6414

CMD ["/bin/bash", "-c", "/root/gaian/launchGaianServer.sh"]
