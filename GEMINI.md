# BubbleFeature

## Project Overview

**BubbleFeature** is an Android application designed to demonstrate the implementation of the **Android Bubbles API**. It follows **Clean Architecture** principles to separate concerns and ensure scalability.

## Architecture

The application is organized into three distinct layers:

### 1. Domain Layer (`com.mezo.bubblefeature.domain`)
*   **Pure Kotlin:** No Android dependencies (where possible).
*   **Entities:**
    *   `Message`: Core data model for chat messages.
*   **Repositories:**
    *   `ChatRepository`: Interface defining the contract for data operations (get messages, send message, show bubble).

### 2. Data Layer (`com.mezo.bubblefeature.data`)
*   **Implementation:** Handles the interaction with the Android system and data storage.
*   **Repositories:**
    *   `ChatRepositoryImpl`: Implementation of `ChatRepository`. Manages the in-memory message list (`StateFlow`) and delegates notification logic.
*   **Data Sources:**
    *   `NotificationDataSource`: Encapsulates all Android Notification and Bubble API logic (formerly `NotificationHelper`).

### 3. Presentation Layer (`com.mezo.bubblefeature.presentation`)
*   **UI & State:** Manages the user interface and view logic.
*   **Components:**
    *   `MainActivity` / `MainViewModel`: Handles permissions and the initial "Send Message" action.
    *   `BubbleActivity` / `BubbleViewModel`: Manages the chat interface inside the bubble.
    *   `ChatScreen`: A reusable Composable for the chat UI.
    *   `ViewModelFactory`: simple factory for manual dependency injection.

## Professional Enhancements

*   **Clean Architecture:** Strict separation of concerns (Domain, Data, Presentation) using Use Cases.
*   **Direct Reply:** Supports **Inline Reply** directly from the notification shade (RemoteInput) via `ReplyReceiver`.
*   **Edge-to-Edge:** Full system bar integration (`enableEdgeToEdge`) for a modern, immersive look.
*   **Dynamic UX:**
    *   **Typing Indicator:** Real-time feedback when **Mezoinnit** is "thinking" or "typing."
    *   **Message Grouping:** Visually links consecutive messages from the same sender, removing redundant avatars and tails for a premium flow.
    *   **Clear Chat:** Instant history management directly from the chat header.
*   **Visuals & Theming:**
    *   **Ultra-Premium Minimalist Design:** Focused on typography, spatial balance, and flat Material 3 tokens.
    *   **Username Integration:** Includes a professional handle (`@mezoinnit`) and an integrated "Active now" status dot.
    *   **Integrated Timestamps:** Seamlessly tucked into the message bubbles.
*   **Stability & UX Fixes:**
    *   **Background Popup Fix:** Guaranteed **heads-up notifications** on Android 16 via high-importance channels and max priority.
    *   **Shortcut Consistency:** Profile-aware bubble icons and stable task management.
*   **UI Polish:**
    *   **Interactive Chat:** Added simulated auto-replies to make the chat feel alive.
    *   **Chat Layout:** Switched to standard `reverseLayout = true` so messages anchor to the bottom.
    *   **Auto-Scroll:** List automatically scrolls to the newest message.
    *   **Timestamps:** Chat messages display formatted timestamps.
    *   **Keyboard Support:** Send messages directly from the keyboard (IME action).
    *   **UX:** content padding and smooth layout.
*   **Code Quality:**
    *   **Strings:** extracted all hardcoded strings to `strings.xml`.
    *   **Robust IDs:** Switched to `UUID` for message IDs to prevent collisions.
*   **Performance:**
    *   **LazyColumn:** Uses `key` parameter to avoid unnecessary recompositions.
    *   **Data Source:** Caches `IconCompat` to minimize resource loading during frequent updates.
    *   **App Size:** Aggressive ProGuard rules and resource shrinking enabled.
*   **Re-bubbling Fix:** Robust Intent handling (`NEW_TASK`, `CLEAR_TASK`, unique Request Codes) ensures bubbles always launch freshly.
*   **User Control:** Notifications default to standard shade entries; users explicitly opt-in to bubble them.

## Dependency Injection
*   **`BubbleApp`:** Acts as a simple Service Locator, initializing the `ChatRepository` singleton and providing it to activities.

## Building and Running

### Prerequisites
*   JDK 11 or higher.
*   Android Studio.
*   Android Device/Emulator (API 30+ recommended).

### Key Commands
*   **Build Debug APK:** `./gradlew assembleDebug`
*   **Install Debug APK:** `./gradlew installDebug`

## Usage
1.  Launch the app.
2.  Grant permissions if prompted.
3.  Click "Send Message" to post a notification.
4.  Open the notification shade and click the Bubble icon to float the conversation.