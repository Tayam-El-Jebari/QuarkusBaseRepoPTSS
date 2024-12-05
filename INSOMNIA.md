# Insomnia API Documentation

This repository includes an [Insomnia design document](https://docs.insomnia.rest/insomnia/design-documents) for testing and documenting our API endpoints. Insomnia design documents combine API specifications (e.g. Swagger), API collections, and tests in one place.

## Setup

1. Install [Insomnia](https://insomnia.rest/download)
2. Open Insomnia
3. Navigate to your desired workspace
3. Click on Create > Git Clone
4. Enter the repository URL and your credentials
> 📖 [Sync with Git](https://docs.insomnia.rest/insomnia/git-sync)

## Working with Insomnia Design Documents
An [Insomnia design document](https://docs.insomnia.rest/insomnia/design-documents) includes:
- API specifications (e.g. Swagger)
- API endpoint collections
- API tests

### API specification

The API specification (currently Swagger) provides a complete description of your API including:

* Available endpoints and operations (`GET`, `POST`, etc.)
* Operation parameters for both inputs and outputs
* Authentication methods
* Contact information, license, terms of use and other information

You can find and edit the Swagger documentation in the `SPEC` tab of your Insomnia workspace.

> 💡 Note: You can also generate a request collection based on your API spec with the `Generate Request Collection` button in the bottom right. After generating a collection, Insomnia creates a new environment. You can safely delete this as we maintain our own environment configurations.

### API endpoint collections

Collections in Insomnia organize your API requests into folders and subfolders. Each request includes:

* HTTP method (`GET`, `POST`, `PUT`, `DELETE`)
* URL with environment variables support
* Headers and authentication settings
* Request body (for `POST/PUT` requests)
* Query parameters
* Environment-specific configurations

Access your collections in the `DEBUG` tab to test endpoints individually.

#### Environments
We maintain three environment configurations:

* **Base Environment**: Contains shared variables used across all environments
* **Development**: Development-specific variables (extends Base Environment)
* **Production**: Production-specific variables (extends Base Environment)

To use environment variables in your requests, use the following syntax:

```
{{ _.variableName }}
```

For example:

* Base URL: `{{ _.baseUrl }}/api/v1/users`
* Authentication: `Bearer {{ _.authToken }}`

#### Advanced Features
Insomnia offers several powerful features to enhance your workflow:

* **[Template Tags](https://docs.insomnia.rest/insomnia/template-tags)**: Transform values through dynamic operations.
* **[Code Generation](https://docs.insomnia.rest/insomnia/generate-code-snippet)**: Generate code snippets in various languages using `Copy as Code`
* **[Request Chaining](https://docs.insomnia.rest/insomnia/chaining-requests)**: Use response data from one request in another

### API tests

Insomnia supports automated testing of your API endpoints through:

* Integration/API tests for individual endpoints
* Test suites for running multiple related tests
* Environment-specific test configurations
* Response validation

Create and run tests in the `TEST` tab to ensure API reliability.

### Git Workflow

> 💡 Unfortunately, I did not find a way to make it so that changes are automatically pushed and pulled to the `.insomnia` directory without making a commit. If this was possible it would make it so that you would only need GitHub Desktop or you preferred Git client for all changes. If someone finds a way to implement this, please let me know. 

#### For Insomnia Collection Changes
- Push changes: Use the `Commit` button in Insomnia
- Pull changes: Use the `Refresh` or `Pull` button to sync with the latest version

#### For Code Changes
- Use GitHub Desktop or your preferred Git client for all code-related changes

### Important Notes
- Always sync your Insomnia collection before making changes
- The design document is stored in the `.insomnia` directory
- After pulling changes through Git, you may need to click `Refresh` or `Pull` in Insomnia to refresh the view
