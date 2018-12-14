
### Skapa config

Notera att certifikat och secret-vars.yaml EJ är incheckade, dessa får man plocka fram själv.

    oc create secret generic "authsampleapp-test-certifikat" --from-file=test/certifikat/ --type=Opaque
    oc create secret generic "authsampleapp-test-env" --from-file=test/env/ --type=Opaque
    oc create configmap "authsampleapp-test-config" --from-file=test/config/
    oc create -f test/secret-vars.yaml
    oc create -f test/configmap-vars.yaml


### Skapa pipeline

    oc process pipelinetemplate-test-webapp -p APP_NAME=authsampleapp-test -p STAGE=test -p SECRET=nosecret -p TESTS="-" | oc apply -f -

### Trigga pipeline från CLI

    oc start-build bc/authsampleapp-test-pipeline \
            --env=infraVersion=3.8.0.+ \
            --env=commonVersion=3.8.0.+ \
            --env=buildVersion=test3 \
            --env=gitUrl=https://github.com/sklintyg/auth-sample-app.git \
            --env=gitRef=develop

### Standalone deploy
Efter att en pipeline gått igenom och producerat en image så kan applikationen deployas enligt:

    oc project demointyg

    oc create secret generic "authsampleapp-demo-certifikat" --from-file=demo/certifikat/ --type=Opaque
    oc create secret generic "authsampleapp-demo-env" --from-file=demo/env/ --type=Opaque
    oc create configmap "authsampleapp-demo-config" --from-file=demo/config/
    oc create -f demo/secret-vars.yaml
    oc create -f demo/configmap-vars.yaml

    oc process deploytemplate-webapp \
            -p APP_NAME=authsampleapp-demo \
            -p IMAGE=docker-registry.default.svc:5000/dintyg/authsampleapp-test-verified:latest \
            -p STAGE=demo -p DATABASE_NAME=authsampleapp \
            -p HEALTH_URI=/ \
            -o yaml | oc apply -f -

### Rensa config

    oc delete secret authsampleapp-demo-certifikat
    oc delete secret authsampleapp-demo-env
    oc delete secret authsampleapp-demo-secret-envvar
    oc delete configmap authsampleapp-demo-configmap-envvar
    oc delete configmap authsampleapp-demo-config
