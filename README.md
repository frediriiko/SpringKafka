# SpringKafka

SpringKafka is a sample project that demonstrates integration between Spring Boot and Apache Kafka. This project provides example implementations and configurations to help you quickly set up a Kafka-based messaging system in your Spring applications.

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Project Structure](#project-structure)
- [Usage](#usage)
- [Docker & Kubernetes](#docker--kubernetes)
- [Contributing](#contributing)
- [License](#license)

## Overview

This repository is structured to showcase the use of Apache Kafka with Spring Boot. It includes example applications (e.g., a chat-app) and configuration files for both local development and deployment using Docker Compose and Kubernetes.

## Features

- **Spring Boot Integration:** Leverage Spring Boot for rapid development.
- **Apache Kafka Messaging:** Example usage of Kafka for asynchronous communication.
- **Multiple Modules:** Organized examples including a chat application and additional service modules.
- **Containerization:** Docker and Docker Compose configuration for easy setup.
- **Kubernetes Deployment:** Kubernetes manifests available for deploying the application in a cloud environment.

## Prerequisites

- **Java JDK 11** (or later)
- **Apache Kafka** (if running outside Docker)
- **Docker & Docker Compose** (for containerized environments)
- **Kubernetes Cluster** (if deploying with k8s)
- **Minikube** (for local Kubernetes deployment)

## Installation

1. **Clone the Repository**

   ```bash
   git clone https://github.com/frediriiko/SpringKafka.git
   cd SpringKafka
   ```

2. **Build the Project**

   Use Maven or Gradle (whichever is configured) to build the project:

   ```bash
   ./mvnw clean install
   ```

3. **Run Locally**

   To run the application locally, execute:

   ```bash
   ./mvnw spring-boot:run
   ```

## Project Structure

```plaintext
SpringKafka/
├── chat-app/           # Example chat application module demonstrating Kafka messaging
├── k8s/                # Kubernetes deployment configurations
├── spring/             # Main Spring Boot application modules and configuration
├── docker-compose.yml  # Docker Compose file to orchestrate containers
└── README.md           # This README file
```

## Usage

- **Sending and Receiving Messages:**
  - The application demonstrates how to produce and consume messages using Kafka.
  - Configure your Kafka broker settings in the application properties (`application.yml` or `application.properties`).

- **Testing:**
  - Unit and integration tests are included. Run tests with:

    ```bash
    ./mvnw test
    ```

## Docker & Kubernetes

- **Docker Compose:**
  - Use the `docker-compose.yml` file to spin up the application along with its dependencies (e.g., Kafka, Zookeeper):

    ```bash
    docker-compose up
    ```

- **Kubernetes:**
  - The `k8s` folder contains manifests for deploying the application to a Kubernetes cluster.
  - Apply the manifests with:

    ```bash
    kubectl apply -f k8s/
    ```

- **Accessing the Services:**
  - To forward the Spring Boot application’s port to your local machine, run:

    ```bash
    kubectl port-forward service/spring-app 8080:8080
    ```
    
  - For the React application, if you are using Minikube, retrieve the URL by executing:

    ```bash
    minikube service react-app --url
    ```

## Contributing

Contributions are welcome! Feel free to open issues or submit pull requests. For major changes, please open an issue first to discuss what you would like to change.

1. Fork the repository.
2. Create your feature branch: `git checkout -b feature/your-feature`
3. Commit your changes: `git commit -am 'Add some feature'`
4. Push to the branch: `git push origin feature/your-feature`
5. Open a pull request.

## License

This project is licensed under the [MIT License](LICENSE).
