
### Skapa config

Notera att certifikat och secret-vars.yaml EJ är incheckade, dessa får man plocka fram själv.

    oc create secret generic "authsampleapp-test-certifikat" --from-file=test/certifikat/ --type=Opaque
    oc create secret generic "authsampleapp-test-env" --from-file=test/env/ --type=Opaque
    oc create configmap "authsampleapp-test-config" --from-file=test/config/
    oc create -f test/secret-vars.yaml
    oc create -f test/configmap-vars.yaml


### Skapa pipeline

    oc process pipelinetemplate-test-webapp -p APP_NAME=authsampleapp-test -p STAGE=test -p SECRET=nosecret -p TESTS="-" | oc apply -f -
