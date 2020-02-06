def call(Map pipelineParams) {

    pipeline {
        agent {
               kubernetes {
      yaml """
apiVersion: v1
kind: Pod
metadata:
  name: jenkins-slave
  labels:
    role: test
spec:
  containers:
  - name: jenkins-agent
    image: artifactory.dev.maximus.axisb.com/
    command:
    - cat
    tty: true
    volumeMounts

"""
    }
             
 
    }



        stages {

            stage('build app image and push') {
                steps {
                    container(''){
                    sh '''
                    
                    docker login artifactory.dev.maximus.axisb.com/artifactory/docker -u=${ARTIFACTORY_USER} -p=${ARTIFACTORY_PASSWORD}
                    gradle build docker --stacktrace
                    docker push artifactory.dev.maximus.axisb.com/docker/${JENKINS_PIPELINE_NAME}:latest
                    docker tag artifactory.dev.maximus.axisb.com/docker/${JENKINS_PIPELINE_NAME}:latest artifactory.dev.maximus.axisb.com/docker/${JENKINS_PIPELINE_NAME}:${JENKINS_PIPELINE_LABEL}
                    docker push artifactory.dev.maximus.axisb.com/docker/${JENKINS_PIPELINE_NAME}:${JENKINS_PIPELINE_LABEL}
                    
                    '''
                }
            }
        }

            stage('Helm package and push') {
                steps {
                    container(''){
             sh '''
              sed -ri "s/^(\s*)(tag\s*:\slatest\s$)/\1tag: ${JENKINS_PIPELINE_LABEL}/" helm/${JENKINS_PIPELINE_NAME}/values.yaml
              helm lint helm/${JENKINS_PIPELINE_NAME}
              helm repo add stable https://kubernetes-charts.storage.googleapis.com/https://artifactory.dev.maximus.axisb.com/artifactory/helm
              helm package helm/${JENKINS_PIPELINE_NAME} --version ${JENKINS_PIPELINE_LABEL} 
              curl -u ${ARTIFACTORY_USER}:${ARTIFACTORY_PASSWORD} -T ${JENKINS_PIPELINE_NAME}-${JENKINS_PIPELINE_LABEL}.tgz "https://artifactory.dev.maximus.axisb.com/artifactory/helm/${HELM_DIRECTORY}/${JENKINS_PIPELINE_NAME}/${JENKINS_PIPELINE_NAME}-${JENKINS_PIPELINE_LABEL}.tgz" 
               '''
            }

        }
     }
        
    }
 }
} 



echo "$JOB_NAME" | awk -F[//] '{print $2}' ----> service name
SERVICE_NAME=$(echo "$JOB_NAME" | awk -F[//] '{print $2}')


=======================
$ /bin/bash -c docker login artifactory.dev.maximus.axisb.com/artifactory/docker -u=${ARTIFACTORY_USER} -p=${ARTIFACTORY_PASSWORD}
Run if Passed$ /bin/bash -c gradle build docker --stacktrace
Run if Passed$ /bin/bash -c docker push artifactory.dev.maximus.axisb.com/docker/${GO_PIPELINE_NAME}:latest
Run if Passed$ /bin/bash -c docker tag artifactory.dev.maximus.axisb.com/docker/${GO_PIPELINE_NAME}:latest artifactory.dev.maximus.axisb.com/docker/${GO_PIPELINE_NAME}:${GO_PIPELINE_LABEL}
Run if Passed$ /bin/bash -c docker push artifactory.dev.maximus.axisb.com/docker/${GO_PIPELINE_NAME}:${GO_PIPELINE_LABEL}
Run if Passed$ /bin/bash -c sed -ri "s/^(\s*)(tag\s*:\slatest\s$)/\1tag: ${GO_PIPELINE_LABEL}/" helm/${GO_PIPELINE_NAME}/values.yaml
Run if Passed$ /bin/bash -c helm lint helm/${GO_PIPELINE_NAME}
Run if Passed$ /bin/bash -c helm init --client-only --stable-repo-url https://artifactory.dev.maximus.axisb.com/artifactory/helm ----->> NO longer working
Run if Passed$ /bin/bash -c helm package helm/${GO_PIPELINE_NAME} --version ${GO_PIPELINE_LABEL}
Run if Passed$ /bin/bash -c curl -u ${ARTIFACTORY_USER}:${ARTIFACTORY_PASSWORD} -T ${GO_PIPELINE_NAME}-${GO_PIPELINE_LABEL}.tgz "https://artifactory.dev.maximus.axisb.com/artifactory/helm/${HELM_DIRECTORY}/${GO_PIPELINE_NAME}/${GO_PIPELINE_NAME}-${GO_PIPELINE_LABEL}.tgz"