# Android Bubbles 🫧

A modest exploration into the **Android Bubbles API**, focused on creating a smooth and intuitive conversation experience. This project serves as a personal study on implementing modern notification features while adhering to **Clean Architecture** principles.

## Overview

The goal of this project was to understand the nuances of the Bubbles API and how it integrates with the rest of the Android ecosystem. It’s not just about the floating icons; it’s about maintaining a consistent state between the shade, the bubble, and the app itself.

## Architecture & Implementation

I've organized the code into three layers to keep things manageable and testable:

*   **Domain:** Pure Kotlin logic containing our core `Message` model and repository interfaces.
*   **Data:** Handles the "heavy lifting" of the Android Notification and Bubble APIs, ensuring notifications are delivered with the correct importance and shortcuts.
*   **Presentation:** A Jetpack Compose-based UI designed with Material 3, focusing on a clean and minimalist aesthetic.

## Key Learnings & Features

While building this, I focused on a few areas to make the experience feel more complete:

*   **Inline Replies:** Implementing `RemoteInput` so you can reply without even opening the bubble.
*   **Visual Flow:** Adding small touches like typing indicators and message grouping to make the chat feel more natural.
*   **Stability:** Working through the complexities of intent flags (`NEW_TASK`, `CLEAR_TASK`) to ensure bubbles launch reliably every time.
*   **Modern Layouts:** Using `enableEdgeToEdge` for a full-screen, immersive feel.

## Getting Started

If you'd like to explore the code or run the app:

1.  Clone the repository.
2.  Open in Android Studio (Jellyfish or newer recommended).
3.  Run on a device or emulator running **API 30+**.

## Roadmap

Planned features and improvements:
- [ ] **Data Persistence:** Moving from in-memory StateFlow to Room database.
- [ ] **Custom Avatars:** Support for dynamic profile pictures in bubbles.
- [ ] **Rich Media:** Sending images or stickers through the bubble interface.
- [ ] **Unit Testing:** Increasing coverage for Data and Presentation layers.

### Quick Commands
```bash
# Build the project
./gradlew assembleDebug

# Install on your device
./gradlew installDebug
```

---
*Developed as a stepping stone toward mastering complex Android notification systems.*
