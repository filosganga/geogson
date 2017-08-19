#!/bin/bash

set -ev

MYDIR="$(dirname "$(readlink -f "$0")")"

BUILD_NUMBER=${CIRCLE_BUILD_NUM}

CURRENT_VERSION=$(mvn help:evaluate -Dexpression=project.version | grep -Ev '(^\[|Download\w+:)')
NEW_VERSION=$(echo ${CURRENT_VERSION} | sed -e "s/-SNAPSHOT/\.$BUILD_NUMBER/")

mvn versions:set -DnewVersion=${NEW_VERSION}
mvn -N versions:update-child-modules
mvn -s ${MYDIR}/settings.xml -DskipUnitTests=true deploy




