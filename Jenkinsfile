pipeline {
    agent any

    tools {
        jdk 'JDK_17' // Name as per your configuration in Jenkins
    }

    environment {
        DOCKER_IMAGE = "ainakshi/devopsdemo"  // Docker image name
        DOCKER_TAG = "latest"  // Docker image tag
        DOCKER_REGISTRY = "docker.io"  // Docker registry
        KUBE_NAMESPACE = "default"  // Kubernetes namespace
        DOCKER_PASSWORD = credentials('docker_hub_password')  // Docker Hub password stored in Jenkins Credentials
    }

    stages {
        stage('Build') {
            steps {
                script {
                    // Build your application (adjust according to your project)
                    sh 'mvn clean install'
                }
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    // Build Docker image using the Dockerfile in the current directory
                    sh "docker build -t ainakshi/devopsdemo."
                }
            }
        }
        stage('Push Docker Image to Docker Hub') {
            steps {
                script {
                    // Push Docker image to Docker Hub
                    withCredentials([string(credentialsId: 'dockerhubpwd', variable: 'dockerhubpwd')]) {
                    sh 'docker login -u ainakshi -p${dockerhubpwd}'
                    }
                    sh 'docker push ainakshi/devopsdemo'
                }
            }
        }

        stage('Deploy to Kubernetes') {
            steps {
                script {
                    // Create Kubernetes deployment YAML and apply it to the cluster
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
                    // Expose the deployment as a service to access the app externally
                    sh """
                        kubectl expose deployment devopsdemo --type=LoadBalancer --name=devopsdemo-service --port=8080 --target-port=8080 --namespace=${KUBE_NAMESPACE}
                    """
                }
            }
        }
    }
}
