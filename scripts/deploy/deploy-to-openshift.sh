#!/bin/bash
# scripts/deploy/deploy-to-openshift.sh

set -e

# Go to project root directory (assuming script is in scripts/deploy/)
cd "$(dirname "$0")/../../"

# Configuration
APP_NAME="quarkus-base-app"
NAMESPACE="hotel-dev"
PORT=8080

# Build the application
echo "Building application..."
./mvnw clean package -DskipTests

# Create necessary OpenShift resources if they don't exist
echo "Creating OpenShift resources..."
oc project $NAMESPACE

# Deploy using Quarkus OpenShift extension
echo "Deploying to OpenShift..."
./mvnw quarkus:deploy -Dquarkus.kubernetes.deploy=true

# Wait for deployment to complete
echo "Waiting for deployment rollout..."
oc rollout status deployment/$APP_NAME -n $NAMESPACE

# Recreate service with correct port
echo "Configuring service..."
oc delete service $APP_NAME --ignore-not-found
oc expose deployment $APP_NAME --port=$PORT

# Create secure route
echo "Creating secure route..."
oc delete route $APP_NAME --ignore-not-found
oc create route edge $APP_NAME --service=$APP_NAME --port=$PORT

# Get and display the route URL
echo "Application route:"
ROUTE_URL=$(oc get route $APP_NAME -n $NAMESPACE -o jsonpath='{.spec.host}')
echo "https://$ROUTE_URL"

echo "Deployment completed successfully!"

# Show pod status
echo "Pod status:"
oc get pods -l app=$APP_NAME -n $NAMESPACE