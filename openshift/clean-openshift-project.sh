#!/bin/bash

[ -f ~/playground/openshift/oc-bash/functions ] && . ~/playground/openshift/oc-bash/functions || { echo "~/playground/openshift/oc-bash/functions not found" 1>&2; exit 1; }

openshift_project_name="noss-dev-pipeline"

function main() {

    verify-project-name ${openshift_project_name}

    oc-delete bc/noss


}

main $@