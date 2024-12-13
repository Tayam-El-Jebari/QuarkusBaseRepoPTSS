## Step 1: Install the SonarQube for IDE Plugin

SonarQube for IDE is the IntelliJ IDEA plugin for SonarQube. It allows you to perform local analysis of your project.

1. Navigate to **File > Settings > Plugins**.
2. Search for `SonarQube for IDE` in the marketplace.
3. Click **Install** to add the plugin.
4. Restart IntelliJ IDEA if prompted.

## Step 2: Configure SonarQube for IDE for Your Project

1. Go to **File > Settings > Tools > SonarQube for IDE**.
2. To set up the SonarQube server:
    - Navigate to the **Settings** tab.
    - Click the **+** in the **Connections** box.
    - Enter the name `PTSS-Support` in the **Connection Name** box.
    - Keep the **SonarQube Cloud** option selected.
    - Press **Next**
    - Click on **Create token**, this will redirect you to the SonarCloud website on which you can link it to your IDE.
    - The SonarCloud `PTSS-Support` organization should show up, select it and continue.
    - Apply the settings you've created when back in the settings screen.

## Step 4: Configure SonarQube for IDE Rules

1. Go to **File > Settings > Tools > SonarQube for IDE > Rules**.
2. Select the programming languages applicable to your project (e.g., **Kotlin** or other languages with other services).

## Step 5: Analyze Your Code with SonarQube for IDE

- To analyze the entire project:
    - Right-click on the project or specific file.
    - Select **SonarQube for IDE > Analyze with SonarQube for IDE**.
    - The issues identified by SonarQube for IDE will appear in the **SonarQube for IDE Report** window.

- To analyze one file:
    - The issues identified by SonarQube for IDE will appear in the **SonarQube for IDE Report** window.