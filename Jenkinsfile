pipeline {
    agent any

    tools {
        jdk 'JDK_17' // Specify the JDK version as per your Jenkins configuration
    }

    environment {
        DOCKER_IMAGE = "ainakshi/devopsdemo"    // Docker image name
        DOCKER_TAG = "latest"                  // Docker image tag
        DOCKER_REGISTRY = "docker.io"          // Docker registry
        KUBE_NAMESPACE = "default"            // Kubernetes namespace
        DOCKER_USERNAME = "ainakshi"          // Docker Hub username
        DOCKER_PASSWORD = credentials('docker_hub_password') // Docker Hub password stored in Jenkins credentials
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
                    // Login to Docker Hub
                    sh """
                        echo ${DOCKER_PASSWORD} | docker login -u ${DOCKER_USERNAME} --password-stdin
                    """
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
                    // Deploy the application to Kubernetes
                    sh """
                        kubectl apply -f - <<EOF
                        apiVersion: apps/v1
                        kind: Deployment
                        metadata:
                          name: devopsdemo
                          namespace: ${KUBE_NAMESPACE}
                        spec:
                          replicas: 1
                          selector:
                            matchLabels:
                              app: devopsdemo
                          template:
                            metadata:
                              labels:
                                app: devopsdemo
                            spec:
                              containers:
                              - name: devopsdemo
                                image: ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${DOCKER_TAG}
                                ports:
                                - containerPort: 8080
                        EOF
                    """
                }
            }
        }

        stage('Expose Kubernetes Service') {
            steps {
                script {
                    // Expose the deployment as a service
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
