pipeline {
    agent any

    tools {
        jdk 'JDK_17' // Specify the JDK version as per your Jenkins configuration
    }

    environment {
        DOCKER_IMAGE = "ainakshi/devopsdemo"    // Docker image name
        DOCKER_TAG = "latest"                   // Docker image tag
        DOCKER_REGISTRY = "docker.io"           // Docker registry
        KUBE_NAMESPACE = "default"              // Kubernetes namespace
        KUBECONFIG = credentials('kubeconfig-jenkins')
    }

    stages {
        stage('Build') {
            steps {
                script {
                    // Build the application
                    sh 'mvn clean install'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build Docker image
                    sh "docker build -t ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${DOCKER_TAG} ."
                }
            }
        }

        stage('Login to Docker Hub') {
            steps {
                script {
                    // Use credentials to login to Docker Hub
                    withCredentials([usernamePassword(credentialsId: 'docker_hub_password', usernameVariable: 'DOCKER_USERNAME', passwordVariable: 'DOCKER_PASSWORD')]) {
                        sh """
                            echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
                        """
                    }
                }
            }
        }

        stage('Push Docker Image to Docker Hub') {
            steps {
                script {
                    // Push Docker image to Docker Hub
                    sh "docker push ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${DOCKER_TAG}"
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    // Apply the Kubernetes Deployment and Service YAML files
                    sh "kubectl apply -f deployment.yaml"
                    sh "kubectl apply -f service.yaml"
                }
            }
        }

        stage('Expose Kubernetes Service') {
            steps {
                script {
                    // Expose the deployment as a service (you can skip this step if it's already in your service.yaml)
                    sh """
                        kubectl expose deployment devopsdemo \
                            --type=LoadBalancer \
                            --name=devopsdemo-service \
                            --port=8080 \
                            --target-port=8080 \
                            --namespace=${KUBE_NAMESPACE}
                    """
                }
            }
        }
    }
}
