pipeline {
    agent any

    environment {
        DOCKER_IMAGE = "devopsdemo"
        DOCKER_TAG = "latest"
        DOCKER_REGISTRY = "docker.io"
        KUBE_NAMESPACE = "default"
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
                    // Build Docker image
                    sh "docker build -t ${DOCKER_REGISTRY}/${DOCKER_IMAGE}:${DOCKER_TAG} ."
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
                    // Create Kubernetes deployment YAML (can also create a separate YAML file)
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
                        kubectl expose deployment devopsdemo --type=LoadBalancer --name=devopsdemo-service --port=8080 --target-port=8080 --namespace=${KUBE_NAMESPACE}
                    """
                }
            }
        }
    }
}
