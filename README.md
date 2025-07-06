# LocalScholarAI

Local Scholar AI: Your Private, Offline Learning Companion
✨ Project Overview
Local Scholar AI is a modern, intuitive Android application designed to provide private, on-device text summarization. Built with Jetpack Compose Multiplatform and powered by Google's Gemini Nano via ML Kit GenAI, this app showcases the power of bringing advanced AI capabilities directly to mobile devices, ensuring data privacy and offline functionality.

This project is my initial major deliverable for my 6-month #GenAISprint, demonstrating practical application of Large Language Models in a real-world mobile context.

🚀 Key Features
On-Device Summarization: Quickly generate concise summaries of long texts directly on your device, without requiring an internet connection.

Absolute Privacy: All AI processing happens locally. Your notes and data never leave your device or get sent to external servers.

Intuitive User Interface: A clean, modern UI built with Jetpack Compose for a seamless user experience.

Offline Functionality: Summarize text anytime, anywhere, even without network connectivity.

💡 Why On-Device AI (Gemini Nano)?
Data Privacy: Sensitive notes and personal information remain entirely on the user's device.

Offline Accessibility: Enables AI functionality in environments without internet access (e.g., libraries, commutes).

Low Latency: Near-instantaneous results as there's no network roundtrip to a cloud server.

Cost Efficiency: No recurring API costs for inference, making the application more sustainable for users.

Sustainability: Reduces reliance on cloud data centers, potentially lowering energy consumption for individual inferences.

🛠️ Technologies Used
Kotlin: Primary programming language.

Jetpack Compose (Android): Modern toolkit for building native Android UI.

Google ML Kit GenAI APIs: For on-device access to Gemini Nano.

Google Gemini Nano: The highly efficient on-device Large Language Model.

Android SDK: Core Android development framework.

Gradle Kotlin DSL: Build system.

📱 Screenshots
Main Screen (Empty Input):
![home_screen](https://github.com/user-attachments/assets/d2e31c7f-4b3c-4b44-8f8e-aa67b46318b8)

Demo Video (Optional but highly recommended):
https://github.com/user-attachments/assets/d1e7a16c-2c23-4ead-9919-343b22f253d8 

Watch a quick demo of Local Scholar AI in action!

🚀 Getting Started
Follow these steps to get "Local Scholar AI" up and running on your local machine.

Prerequisites
Android Studio (Bumblebee 2021.1.1 or later recommended)

An Android device that supports Gemini Nano (e.g., Google Pixel 8 Pro/8/8a, Samsung Galaxy S24 series, or other compatible modern flagships). The app will run on other devices, but the on-device AI feature will not function.

An internet connection (for initial model download, if required by ML Kit GenAI, and for Gradle sync).

Installation
Clone the repository:

git clone https://github.com/your-username/LocalScholarAI.git
cd LocalScholarAI

Open in Android Studio:
Open the cloned project in Android Studio.

Sync Gradle:
Allow Gradle to sync dependencies. If you encounter issues, ensure your build.gradle.kts (Module: app) dependencies match the latest stable ML Kit GenAI version.
(Refer to MainActivity.kt for exact dependencies and AndroidManifest.xml permissions).

Run on Device:
Connect a compatible Android device via USB and run the application.

🤝 Contributing
This is a personal project as part of my accelerated AI Engineer learning journey. However, feel free to fork the repository, open issues for suggestions or bugs, or submit pull requests.

🛣️ My GenAI Sprint Journey
This project marks Week 1 of my 6-month journey to become a GenAI Expert. Follow my progress on LinkedIn for weekly project updates and insights!

Google Prompting Essentials Certificate (My recent certification!)

📧 Contact
Feel free to reach out to me!

LinkedIn: Your LinkedIn Profile

Email: your.email@example.com

📄 License
This project is licensed under the MIT License - see the LICENSE file for details.
