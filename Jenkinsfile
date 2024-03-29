properties([pipelineTriggers([githubPush()])])

pipeline {
    agent { docker { image 'burin17/users-service-pipeline' } }
f
    stages {
        stage('Checkout SCM') {
          steps {
            checkout([
              $class: 'GitSCM',
              branches: [[name: 'master']],
              userRemoteConfigs: [[
                url: 'git@github.com:burin17/users-service.git',
                credentialsId: 'users-service-token',
              ]]
             ])
           }
        }

        stage('Build images') {
            steps {
                echo "Build of images started"
                sh '''
                    cd users-crud-service
                    mvn package -Dmaven.test.skip
                    docker docker build -t burin17/users-crud-service:v1
                    docker login -p ${DOCKERHUB_PASS} -u burin17
                    docker push burin17/users-crud-service:v1
                    cd ..
                    cd registry-service
                    mvn package -Dmaven.test.skip
                    docker docker build -t burin17/registry-service:v1
                    docker push burin17/registry-service:v1
                    cd ..
                    cd login-service
                    mvn package -Dmaven.test.skip
                    docker docker build -t burin17/login-service:v1
                    docker push burin17/login-service:v1
                    cd ..
                    cd api-gateway-service
                    mvn package -Dmaven.test.skip
                    docker docker build -t burin17/api-gateway-service:v1
                    docker push burin17/api-gateway-service:v1
                    cd ..
                    cd admin-deletion-service
                    mvn package -Dmaven.test.skip
                    docker docker build -t burin17/admin-deletion-service:v1
                    docker push burin17/admin-deletion-service:v1
                    cd ..
                '''
                echo "Build of images completed"
            }
        }
        stage('Deploy') {
            steps {
                echo "Deploy started"
                echo "Deploy finished"
            }
        }
    }
}
