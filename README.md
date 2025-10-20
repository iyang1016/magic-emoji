# Emoji Encoder

Hide secret messages in emojis using Unicode variation selectors! This project provides both a web application and an Android mobile app to encode and decode hidden messages in emojis.

## 🌟 Features

- **Encode Messages**: Hide any text inside an emoji or alphabet letter
- **Decode Messages**: Extract hidden messages from encoded emojis
- **Multiple Platforms**: Available as both a web app and Android app
- **Easy to Use**: Real-time encoding/decoding with a simple interface
- **Shareable**: Copy and paste encoded emojis anywhere

## 🎯 How It Works

The encoder uses Unicode variation selectors to hide data within emoji characters. Each byte of your message is converted to an invisible variation selector character. The emoji acts as a visible carrier, but the hidden message is preserved when you copy and paste the text.

### Technical Details

- Uses Unicode Variation Selectors (U+FE00..U+FE0F and U+E0100..U+E01EF)
- Supports all UTF-8 characters
- Compatible with any platform that preserves Unicode characters
- 22 popular emojis to choose from
- Full alphabet (a-z) support

## 🚀 Quick Start

### Web Version

The web version is built with Next.js and can be accessed online or run locally.

#### Running Locally

```bash
# Install dependencies
npm install

# Run development server
npm run dev

# Build for production
npm run build
npm start
```

Open [http://localhost:3000](http://localhost:3000) in your browser.

#### Testing

```bash
npm test
```

### Android Version

The Android app provides the same functionality in a native mobile experience.

#### Building the Android App

1. **Prerequisites**:
   - Android Studio (Arctic Fox or later)
   - Android SDK (API 24+)
   - Kotlin 1.9.20+

2. **Build Steps**:
   ```bash
   cd android-app
   ./gradlew assembleDebug
   ```

3. **Install on Device**:
   ```bash
   ./gradlew installDebug
   ```

For detailed instructions, see the [Android App README](android-app/README.md).

## 📱 Platforms

### Web Application
- **Framework**: Next.js 14
- **Language**: TypeScript
- **UI**: React with Radix UI components
- **Styling**: Tailwind CSS

### Android Application  
- **Language**: Kotlin
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34 (Android 14)
- **UI**: Material Design 3 with modern components
- **Features**: Copy/paste buttons, share functionality, dark mode support

## 📖 Usage

### Encoding a Message

1. Select "Encode" mode
2. Choose an emoji or letter
3. Type your message
4. Copy the encoded output
5. Share it anywhere!

### Decoding a Message

1. Select "Decode" mode
2. Paste the encoded emoji
3. Read the hidden message

## 🏗️ Project Structure

```
emoji-encoder/
├── app/                          # Next.js app directory
│   ├── encoder-decoder-content.tsx
│   ├── encoding.ts              # Encoding/decoding logic
│   ├── encoding.test.ts         # Tests
│   ├── emoji.ts                 # Emoji lists
│   └── page.tsx                 # Main page
├── components/                   # React components
│   └── emoji-selector.tsx
├── android-app/                  # Android application
│   ├── app/
│   │   └── src/
│   │       ├── main/
│   │       │   ├── java/com/emojiencoder/
│   │       │   │   ├── EmojiEncoder.kt
│   │       │   │   ├── EmojiLists.kt
│   │       │   │   └── MainActivity.kt
│   │       │   └── res/
│   │       └── test/            # Unit tests
│   └── README.md                # Android-specific docs
├── public/                       # Static assets
├── styles/                       # Global styles
└── package.json                  # Dependencies
```

## 🧪 Testing

### Web App Tests

```bash
npm test
```

Tests verify encoding and decoding across:
- Basic ASCII strings
- Unicode characters (Chinese, emojis, etc.)
- Special characters
- Empty strings
- All emoji and alphabet options

### Android App Tests

```bash
cd android-app
./gradlew test
```

Tests cover the same scenarios as the web version.

## 🛠️ Development

### Web Development

```bash
# Install dependencies
npm install

# Run development server with hot reload
npm run dev

# Lint code
npm run lint

# Build for production
npm run build
```

### Android Development

Open the `android-app` directory in Android Studio for:
- Code editing with full IDE support
- Visual layout editor
- Debugging tools
- Emulator integration

## 🔄 CI/CD Pipeline

The project includes automated CI/CD pipelines for building the Android app:

### Android CI Workflow
- **Triggers**: On push or pull request to `main` or `develop` branches when Android code changes
- **Tasks**:
  - Runs Android Lint checks
  - Executes unit tests
  - Builds debug and release APKs
  - Uploads build artifacts (APKs, lint reports, test results)

### Release Workflow
- **Triggers**: On version tags (e.g., `v1.0.0`) or manual workflow dispatch
- **Tasks**:
  - Builds web application
  - Builds Android APK
  - Creates GitHub release with both artifacts

All workflows use:
- JDK 17 (Temurin distribution)
- Android Gradle Plugin 8.2.0
- Gradle 8.2
- Kotlin 1.9.20

## 📄 License

See the LICENSE file for details.

## 🙏 Acknowledgments

This implementation uses Unicode variation selectors to hide data in text. The technique leverages the Unicode standard's provision for variation selectors to select different visual representations of characters.

## 🔗 Links

- [Web Version Demo](#) (Add your deployment URL)
- [Android App README](android-app/README.md)

## 🤝 Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## ⚠️ Known Limitations

- Some text editors and apps may not preserve variation selectors
- Very long messages will create correspondingly long strings
- The encoded message only works when copied/pasted (not typed)

## 💡 Use Cases

- Send secret messages to friends
- Hide metadata in social media posts
- Create fun puzzles and challenges
- Demonstrate Unicode features
- Educational purposes about character encoding

---

Made with ❤️ by yano using Unicode magic ✨
