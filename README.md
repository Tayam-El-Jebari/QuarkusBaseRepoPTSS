# ptss-support-quarkus

## Overview

This is a template repository for a Quarkus-based microservice project. It provides a starting point for new services within our platform ecosystem.

## Repository Template Usage

### How to Use This Template

1. Click the "Use this template" button on the GitHub repository page
2. Choose a name for your new repository
3. Select whether to copy only the main branch or all branches
4. Create your new repository based on this template

## Project Features

- Built with Quarkus, the Supersonic Subatomic Java Framework
- Kotlin support for service implementation
- Comprehensive CI/CD pipeline
- Integrated with SonarQube for code quality
- Performance testing with K6 
- Monitoring and observability with Prometheus
- Branch protection rules
- Native executable support

## Prerequisites

- Java Development Kit (JDK) 17+
- Maven 3.8.1+
- Docker (optional, for container builds)
- GraalVM (optional, for native compilation)

## Setup and Configuration

### Local Development Setup

1. Clone the repository
   ```bash
   git clone <repository-url>
   cd <repository-name>
   ```

2. Install dependencies
   ```bash
   ./mvnw clean install
   ```

### Running the Application

#### Development Mode
```bash
./mvnw compile quarkus:dev
```
- Enables live coding
- Access Dev UI at: http://localhost:8080/q/dev/

#### Production Build
```bash
# Regular JAR
./mvnw package

# Uber JAR
./mvnw package -Dquarkus.package.jar.type=uber-jar

# Native Executable
./mvnw package -Dnative
# Or with container build
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

### Branch Protection

Detailed branch protection rules are configured for `development` and `main` branches. See the [Branch Protection](#setting-up-branch-protection-rules) section for specifics.

### Code Quality

#### SonarQube Integration
1. Project is set up with SonarQube Cloud
2. Automatic analysis is enabled
3. New code definition can be customized

## Documentation References

### Internal Documentation
- [Helm Configuration](/helm/README.md)
- [CI/CD Documentation](/CI-CD.md)
- [Authentication Filter](/src/main/kotlin/org/ptss/support/security/README.md)
- [Code Formatting](/docs/code-formatting-spotless.md)
- [SonarQube Local Setup](/docs/sonarqube-local.md)

### Related Repositories
- [Platform Configuration](https://github.com/PTSS-Support/platform-config)
- [Identity Service](https://github.com/PTSS-Support/Identity-Service)
- [Questionnaire Service](https://github.com/PTSS-Support/Questionnaire-Service)
- And more...

## Technology Stack

- **Framework**: Quarkus
- **Language**: Kotlin
- **Build Tool**: Maven
- **Code Quality**: SonarQube
- **CI/CD**: GitHub Actions

## Contributing

1. Create a feature branch from `development`
2. Implement your changes
3. Create a pull request to `development`
4. Automated checks will:
   - Run tests
   - Perform SonarQube code quality analysis
5. Wait for review and approval from team members

## Quarkus Resources

- [Quarkus Website](https://quarkus.io/)
- [Quarkus Guides](https://quarkus.io/guides/)
- [Kotlin Guide](https://quarkus.io/guides/kotlin)