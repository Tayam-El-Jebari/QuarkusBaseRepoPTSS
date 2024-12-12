## Running the Plugin
Make sure the plugin is actually installed locally by running this command first:
```
./mvnw clean install 
```

Once the plugin is installed you can run it locally to format your code.

# Check the Formatting of the Entire Project
To check if your files conform to the defined format without making any changes, you can use the following command:

```
./mvnw spotless:check
```
This will not modify any files, but it will notify you if any files need formatting.

# Run Spotless to format the Entire Project
To format all files according to the rules set in the pom.xml, run the following Maven command:

```
./mvnw spotless:apply
```
This command will apply the defined formatting to all the files included in the configuration (like .gitattributes, .gitignore, and Kotlin files).

# Format Specific Files
If you want to format only specific files (for example, just Kotlin files), you can customize the includes and exclusions under the <includes> tag in your plugin configuration. However, to format all files, the previous command will suffice.


### Note: Spotless is configured to look at previous commits
Spotless supports a ratchet feature, which ensures that only files changed since a specific commit of origin/development are formatted. The configuration in the pom.xml includes the line:

```
<ratchetFrom>origin/development</ratchetFrom>
```
This means that any files changed since origin/development will be formatted according to the rules when you run mvn spotless:apply.