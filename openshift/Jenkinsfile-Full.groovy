try {

    timeout(time: 20, unit: 'MINUTES') {

        def project=""

        node {

            stage("initialize") {

                echo "initializing ..."

                project = env.PROJECT_NAME
            }
        }

        node("maven") {

            stage("checkout") {

                echo "checking out from ${GIT_REPOSITORY_URL} ..."

                git url: "${GIT_REPOSITORY_URL}", branch: "master"

                echo "check out ok"
            }

            stage("build") {

                echo "building maven artifacts without tests ..."

                sh "mvn clean package -Dmaven.test.skip=true"

                echo "build ok"

                stash name:"war", includes:"target/novaordis-session-servlet.war"
            }

            stage("tests") {

                parallel {

                    stage("unit tests") {

                        sh "mvn clean test"

                    }

                    stage("coverage tests") {

                        sh "mvn clean test"
                    }
                }

                echo "tests ok"
            }

        }

//        node {
//
//            stage("build image") {
//
//                echo "building image ..."
//
//                unstash name:"war"
//
//                sh "oc start-build ${appName}-docker --from-file=target/ROOT.war -n ${project}"
//
//                openshiftVerifyBuild bldCfg: "${appName}-docker", namespace: project, waitTime: '20', waitUnit: 'min'
//            }
//
//            stage("Deploy") {
//
//                openshiftDeploy deploymentConfig: appName, namespace: project
//            }
//        }
    }
}
catch (err) {

    echo "build failure: ${err}"
    currentBuild.result = 'FAILURE'
    throw err
}