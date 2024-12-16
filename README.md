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

1. In your new repository, go to **Settings** > **Branches** in the left sidebar. The **Branch protection rules** page opens.
2. Select **Add branch protection rule** to create a new rule.
3. Set up protection for the `development` branch:
  - In **Branch name pattern**, enter `development`
  - Enable **Require a pull request before merging**
  - Under pull request settings, enable:
    - **Require approvals** and set the number to 2
    - **Dismiss stale pull request approvals when new commits are pushed**
  - Enable **Require status checks to pass before merging**
  - Enable **Require branches to be up to date before merging**
  - In the status checks search box, search for and select:
    - `build-and-analyze`
    - `test`
  - Select **Create** to save the rule
4. Create another rule for the `main` branch:
  - Select **Add branch protection rule** again
  - In **Branch name pattern**, enter `main`
  - Enable all settings from the development branch
  - Additionally enable:
    - **Do not allow bypassing the above settings**
    - **Require linear history**
  - Select **Create** to save the rule

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
