# SPDX-License-Identifier: Apache-2.0
# Copyright Contributors to the Egeria project

FROM alpine:3.9

# Install utilities we will use to configure containers
RUN apk add bash curl git jq openldap-clients postgresql-client mariadb-client && apk update && apk upgrade
COPY dist/check-availability.sh /check-availability.sh

ENTRYPOINT ["/check-availability.sh"]
