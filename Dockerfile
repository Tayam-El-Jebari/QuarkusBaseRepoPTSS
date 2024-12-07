# Root directory: Dockerfile
FROM registry.access.redhat.com/ubi8/openjdk-17:1.14

ENV LANGUAGE='en_US:en'

# Install dependencies
USER root
RUN microdnf install curl ca-certificates \
    && microdnf update \
    && microdnf clean all

# Configure the JAVA_OPTIONS, you can add -XshowSettings:vm to also display the heap size.
ENV JAVA_OPTIONS="-Dquarkus.http.host=0.0.0.0 -Djava.util.logging.manager=org.jboss.logmanager.LogManager"

# Copy the application
COPY build/quarkus-app/lib/ /deployments/lib/
COPY build/quarkus-app/*.jar /deployments/
COPY build/quarkus-app/app/ /deployments/app/
COPY build/quarkus-app/quarkus/ /deployments/quarkus/

EXPOSE 8080
USER 185

ENTRYPOINT [ "java", "-jar", "/deployments/quarkus-run.jar" ]
