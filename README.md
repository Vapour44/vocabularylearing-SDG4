# IELTS Master — SDG 4: Quality Education

**SDG Goal:** SDG 4 – Quality Education

## Problem
Many learners worldwide struggle to build English vocabulary due to a lack of accessible, engaging, and structured learning tools. Traditional methods such as memorizing word lists are ineffective and difficult to sustain, resulting in slow progress and low motivation.

## Why It Matters
English proficiency plays a critical role in academic achievement, career development, and global communication. Without consistent and effective vocabulary practice, learners are unable to reach their target scores in standardized assessments such as IELTS, leaving them at a disadvantage in higher education and the job market.

## Solution
IELTS Master is a mobile vocabulary learning app that allows users to set a personal learning goal, study curated word lists, track daily progress, and explore course materials — all within a structured and motivating interface. By making quality English learning accessible on any mobile device, the app supports SDG 4's mission of inclusive and equitable quality education for all.

---

## Features

- **Profile Setup** — Enter your name and target IELTS band score before starting.
- **Main Dashboard** — View today's study plan, progress bars, and quick-access feature buttons.
- **Reading Hub** — Browse reading materials with horizontal-scroll book cards and progress tracking.
- **Word Training** — Access multiple training modes (Self Check, Spelling, Listen & Choose, etc.).
- **Wordbook Manager** — Create, edit, delete, and share personal word books stored locally via Room Database.
- **Self-Test Screen** — Flash-card style vocabulary review; shake the device (Accelerometer sensor) to advance to the next word; tap "Don't Remember" to fetch a live dictionary definition and example sentence via the Free Dictionary API.
- **Community Screen** — Browse word books shared by other users, powered by Firebase Firestore (cloud sync).
- **Course Details** — Drill into any advertised course for a full syllabus description.
- **Edit Wordbook** — Rename an existing word book and save changes back to the local database.

---

## Tech Stack

| Layer | Technology |
|-------|-----------|
| UI | Jetpack Compose + Material 3 |
| Navigation | Jetpack Navigation Compose |
| Local Persistence | Room Database (SQLite) |
| Cloud / Backend | Firebase Firestore |
| REST API | Retrofit 2 + Gson — [Free Dictionary API](https://dictionaryapi.dev/) |
| Sensor | Android Accelerometer (shake detection) |
| Architecture | MVVM — single `UserViewModel` with shared state |

---

## Setup Instructions

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 11+
- A Google account (for Firebase)

### Steps

1. **Clone the repository**
   ```bash
   git clone https://github.com/Vapour44/vocabularylearing-SDG4.git
   cd vocabularylearing-SDG4/A207404_ZHANGKAIYI_CikguLzwan_lab
   ```

2. **Configure Firebase**
   - Go to the [Firebase Console](https://console.firebase.google.com/) and create a project (or use an existing one).
   - Register the Android app with package name `com.example.a207404_zhangkaiyi_cikgulzwan_lab2`.
   - Download the generated `google-services.json` and place it in the `app/` directory (replacing the existing file if present).
   - In the Firebase Console, enable **Cloud Firestore** (Start in test mode is fine for development).

3. **Open in Android Studio**
   - Open the `A207404_ZHANGKAIYI_CikguLzwan_lab` folder as an Android Studio project.
   - Let Gradle sync finish (it will download all dependencies automatically).

4. **Run the app**
   - Connect a physical Android device (API 24+) or start an emulator.
   - Click **Run ▶** in Android Studio.
   - For shake-detection testing, use a physical device; the emulator's virtual sensor requires manual input via the Extended Controls panel.

---

## Project Structure

```
app/src/main/java/.../
└── MainActivity.kt        # All screens, ViewModel, Room, Retrofit, Sensor logic
app/src/main/res/
└── drawable/              # Background image assets
AndroidManifest.xml        # INTERNET permission declared
app/build.gradle.kts       # Dependencies (Compose, Room, Firebase, Retrofit)
```
