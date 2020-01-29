def call(Map pipelineParams) {

pipeline {
  agent {
    docker { image 'docker:latest' }
  }
  
    stages {
        stage("Env Variables") {
            steps {
                sh "printenv | sort "
                echo "${pipelineParams.service}"
                sh " docker pull ${pipelineParams.service}:${env.GIT_COMMIT}"
            }
        }
    
        stage("test") {
            steps {
                sh ''' 
                whoami
                echo "hello" 
                '''
            }
    }
  }
    }   
}

