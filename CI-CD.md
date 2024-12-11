# CI/CD 
## PR-Validation
### CodeQL

### Overview
CodeQL is a semantic code analysis engine that helps identify vulnerabilities and coding errors in the source code. 
In our project, CodeQL is integrated into the GitHub Actions workflow for pull request validation.

### Location
The CodeQL configuration is located in the GitHub Actions workflow file:
`.github/workflows/pr-validation.yml`

### Configuration Details
- **Languages Analyzed**: Java (Note: Kotlin is also analyzed as Java)
- **Query Sets**:
    - `security-extended`
    - `security-and-quality`

### Adding Support for New Programming Languages

If you need to add support for a new programming language in CodeQL:

1. Modify the `languages` parameter in the CodeQL initialization step:
   ```yaml
   - name: Initialize CodeQL
     uses: github/codeql-action/init@v3
     with:
       languages: java,python  # Add new languages here
   ```

2. Ensure the new language is supported by GitHub's CodeQL:
    - Supported languages include: C/C++, C#, Go, Java, JavaScript/TypeScript, Python, Ruby

3. Update the workflow to include any language-specific setup or dependencies required for the new language.
