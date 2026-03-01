# Business Card App

A simple yet elegant digital business card application built entirely with Jetpack Compose. This app showcases a modern, clean UI that displays professional and social contact information.

![App Screenshot](app/src/main/res/drawable/Screenshot_20260227_040114.png)

## Features

- **100% Kotlin & Jetpack Compose:** Built using the latest recommended tools for Android development.
- **Modern UI Design:** Features a two-tone gradient background, a circular profile image, and well-structured content.
- **Responsive Layout:** The UI is designed to adapt to various screen sizes, ensuring content is always centered and properly spaced.
- **Comprehensive Contact Information:** Displays primary contact details and a list of social media profiles, separated by a `HorizontalDivider` for clarity.
- **Resource-Driven:** All text content is externalized to `strings.xml` for easy management and future localization.
- **Material Design 3:** Utilizes components and icons from the Material Design 3 library.

## Technologies Used

- [Kotlin](https://kotlinlang.org/)
- [Jetpack Compose](https://developer.android.com/jetpack/compose)
- [Material Design 3](https://m3.material.io/)
- [Android Studio](https://developer.android.com/studio)

## How to Build

1.  **Clone the repository:**
    ```sh
    git clone <your-repository-url>
    ```
2.  **Open in Android Studio:**
    Open the cloned project directory in the latest version of Android Studio.
3.  **Sync and Run:**
    Allow Gradle to sync the project dependencies, then build and run the app on an Android emulator or a physical device.

## Code Overview

-   **`MainActivity.kt`**: The main entry point of the app. It contains all the Composable functions (`BusinessCardApp`, `ContactInformation`, `ContactRow`) that construct the UI.
-   **`strings.xml`**: Contains all the string resources, such as the person's name, title, and contact details.
-   **`ic_android_logo.xml`**: A vector drawable used for the profile image.
-   **`theme/` directory**: Contains theme-related files, including color schemes and typography settings.
