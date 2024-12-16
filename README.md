# ptss-support-quarkus

This project uses Quarkus, the Supersonic Subatomic Java Framework.

If you want to learn more about Quarkus, please visit its website: <https://quarkus.io/>.

## Setup

### Setting up branch protection rules

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

### Setting up a new project in SonarQube Cloud

1. On the top right of the SonarQube Cloud interface, select the ✚ (plus) menu and select **Analyze new project**. The **Analyze projects** page opens.
2. Select your organization.
3. Select the repositories you want to import.
4. Select the **Set up** button. The **Set up project for Clean as You Code** page opens.
5. Select the new code definition for your project.
6. Select the **Create project** button. The project is created and the automatic analysis is started.

## Running the application in dev mode

You can run your application in dev mode that enables live coding using:

```shell script
./mvnw compile quarkus:dev
```

> **_NOTE:_**  Quarkus now ships with a Dev UI, which is available in dev mode only at <http://localhost:8080/q/dev/>.

## Packaging and running the application

The application can be packaged using:

```shell script
./mvnw package
```

It produces the `quarkus-run.jar` file in the `target/quarkus-app/` directory.
Be aware that it’s not an _über-jar_ as the dependencies are copied into the `target/quarkus-app/lib/` directory.

The application is now runnable using `java -jar target/quarkus-app/quarkus-run.jar`.

If you want to build an _über-jar_, execute the following command:

```shell script
./mvnw package -Dquarkus.package.jar.type=uber-jar
```

The application, packaged as an _über-jar_, is now runnable using `java -jar target/*-runner.jar`.

## Creating a native executable

You can create a native executable using:

```shell script
./mvnw package -Dnative
```

Or, if you don't have GraalVM installed, you can run the native executable build in a container using:

```shell script
./mvnw package -Dnative -Dquarkus.native.container-build=true
```

You can then execute your native executable with: `./target/ptss-support-quarkus-1.0-SNAPSHOT-runner`

If you want to learn more about building native executables, please consult <https://quarkus.io/guides/maven-tooling>.

## Related Guides

- Kotlin ([guide](https://quarkus.io/guides/kotlin)): Write your services in Kotlin

## Provided Code

### REST

Easily start your REST Web Services

[Related guide section...](https://quarkus.io/guides/getting-started-reactive#reactive-jax-rs-resources)
