# 🏆 Daily Wrestle Trivia

**Daily Wrestle Trivia** is a fully featured mobile trivia app built for wrestling fans!  
Players can test their wrestling knowledge across eras, companies, and pay-per-views — or take on the **Quiz Master** and other AI challengers in Versus Mode.

---

## 🎮 Features

- 🧠 **Over 1,000 curated trivia questions** covering WWE, WCW, ECW, AEW, TNA, and more.  
- 🎨 **Light and Dark Mode** support for dynamic UI themes.  
- 🔄 **Multiple Game Modes:**
  - **Daily Quiz** – a quick 10-question challenge that updates daily.
  - **Time Trial** – name all champions as fast as possible!
  - **Versus Mode** – compete against AI opponents like the *Quiz Master*, *Fact Fiend*, and *Trivia Titan*.
- 🎡 **Wheel of Trivia** to select random categories.
- 🌍 **Offline Ready** – all trivia questions stored locally with Room Database.
- 🔊 **Smooth Animations** throughout gameplay.

---

## ⚙️ Tech Stack

| Category | Technology |
|-----------|-------------|
| **Framework** | Kotlin Multiplatform / Android (Compose Multiplatform Ready) |
| **UI Layer** | Jetpack Compose |
| **Database** | Room Database (KMP version) |
| **Architecture** | MVVM (Model-View-ViewModel) |
| **Dependency Injection** | Koin / Hilt (depending on implementation) |
| **Asynchronous Tasks** | Kotlin Coroutines & Flows |
| **Navigation** | Jetpack Navigation for Compose |
| **JSON Parsing** | Kotlinx Serialization |
| **Image Loading** | Coil Compose |
| **Animations** | Compose Animation APIs |
| **Build Tools** | Gradle (KTS) |

---

## 📱 Design

The UI is designed to balance a **clean trivia experience** with the **energy of professional wrestling**:
- Vibrant character avatars like *Quiz Master* and *Trivia Titan*  
- Adaptive layout for portrait & landscape orientations  
- Optimized spacing and typography for readability  
- Custom icons and flat vector illustrations following Material You guidelines  

---

## 🔒 Data Storage

All trivia questions are preloaded from a large JSON dataset into Room for fast offline access.  
A question selection algorithm ensures:
- Questions are ordered by **least answered first**
- Randomized selection with fairness over time

---

## 🚀 Future Enhancements

- 🌐 Localization (English + Spanish support planned)  
- 👥 Online Leaderboards  
- 🧩 User-Created Trivia Packs  
- 💾 Cloud Sync for progress and stats  

---

## 🧑‍💻 Developer

Developed by **Daniel Butler**  
📱 Mobile Developer
- Feedback, contributions, and ideas welcome!
---

## 🏁 License

This project is licensed under the **MIT License** — feel free to use and modify with attribution.
