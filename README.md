# Google Calendar Integration with Spring Boot

This project is a Spring Boot application that integrates with Google Calendar using the Google Calendar API. The app allows users to add events from a frontend HTML form and displays the results on a confirmation page.

## Table of Contents

- [Prerequisites](#prerequisites)
- [Setting Up Google Cloud Console](#setting-up-google-cloud-console)
- [Cloning the Project](#cloning-the-project)
- [Building the Project](#building-the-project)
- [Running the Application](#running-the-application)

## Prerequisites

- Java 11 or higher
- Maven
- A Google account

## Setting Up Google Cloud Console

1. Go to the [Google Cloud Console](https://console.cloud.google.com/).
2. Create a new project:
   - Click on the project dropdown and select "New Project."
   - Enter a project name and click "Create."
3. Enable the Google Calendar API:
   - In the left sidebar, navigate to "APIs & Services" > "Library."
   - Search for "Google Calendar API" and click on it.
   - Click the "Enable" button.
4. Create credentials for your application:
   - Navigate to "APIs & Services" > "Credentials."
   - Click on "Create Credentials" and select "OAuth client ID."
   - Configure the consent screen if prompted.
   - Select "Web application" as the application type.
   - now download the client-id, client-secrets in json format.
5. Add test users:
   - In the OAuth consent screen configuration, add the email addresses of colleagues who will test the application.

## Cloning the Project

Clone this project into the directory of your choice:

```bash
git clone https://github.com/mshirazkamran/gci-test.git
```

Then, change into the project root directory:

```bash
cd gci-test
```

## Building the Project

Run the following command to build the project:

```bash
./mvnw clean package
```

## Running the Application

Run the created JAR file using the command below:

```bash
java -jar target/your-jarfile.jar
```

Replace `your-jarfile.jar` with the actual name of the JAR file generated in the `target` directory.
```
