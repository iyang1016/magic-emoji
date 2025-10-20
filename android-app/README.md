# Emoji Encoder - Android App

An Android application that allows you to encode hidden messages into emojis using Unicode variation selectors. This is an Android port of the web-based emoji encoder.

## Features

- **Modern Material Design 3 UI**: Beautiful, intuitive interface with gradient headers
- **Encode Mode**: Hide secret messages in emojis or alphabet letters
- **Decode Mode**: Extract hidden messages from encoded emojis
- **Enhanced Copy/Paste**: Dedicated buttons for clipboard operations
- **Share Functionality**: Direct sharing of encoded messages
- **Character Counter**: Real-time character count for input/output
- **Smart Mode Switching**: Automatic UI adaptation based on encode/decode mode
- **Error Handling**: Clear feedback with success/error messages
- **Dark Mode Support**: Automatic theme switching based on system preference
- **Emoji Selection**: Choose from 22 popular emojis with visual selection
- **Alphabet Selection**: Use standard alphabet letters (a-z)
- **Real-time Conversion**: Instant encoding/decoding as you type

## How It Works

The app uses Unicode variation selectors to hide data within emoji characters. Each byte of your message is converted to a variation selector character that is invisible but preserved when copying text. The emoji acts as a visible carrier for the hidden message.

### Technical Details

- Uses Unicode Variation Selectors (U+FE00..U+FE0F and U+E0100..U+E01EF)
- Supports all UTF-8 characters in messages
- Compatible with any platform that preserves Unicode characters

## Building the App

### Prerequisites

- Android Studio (Arctic Fox or later recommended)
- Android SDK (API 24 or higher)
- Kotlin 1.9.20 or higher
- Gradle 8.2 or higher

### Build Instructions

1. Clone the repository:
```bash
git clone [your-repo-url]
cd emoji-encoder/android-app
```

2. Open the project in Android Studio:
   - Launch Android Studio
   - Select "Open an Existing Project"
   - Navigate to the `android-app` directory
   - Click "OK"

3. Build and run:
   - Wait for Gradle to sync
   - Connect an Android device or start an emulator
   - Click the "Run" button (green play icon) or press Shift+F10

### Command Line Build

You can also build from the command line:

```bash
# Build debug APK
./gradlew assembleDebug

# Build release APK
./gradlew assembleRelease

# Install on connected device
./gradlew installDebug
```

The APK will be generated in `app/build/outputs/apk/`

## Usage

1. **Launch the app**
   - The app opens in "Encode" mode by default

2. **To Encode a Message**:
   - Ensure the switch is set to "Encode"
   - Select an emoji or letter from the picker
   - Type your message in the input field
   - The encoded version appears in the output field
   - Long-press the output to copy the encoded emoji

3. **To Decode a Message**:
   - Toggle the switch to "Decode"
   - Paste an encoded emoji in the input field
   - The hidden message appears in the output field

## Project Structure

```
android-app/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/emojiencoder/
│   │       │   ├── MainActivity.kt        # Main UI activity
│   │       │   ├── EmojiEncoder.kt        # Encoding/decoding logic
│   │       │   └── EmojiLists.kt          # Emoji and alphabet data
│   │       ├── res/
│   │       │   ├── layout/
│   │       │   │   └── activity_main.xml  # Main UI layout
│   │       │   └── values/
│   │       │       ├── strings.xml        # String resources
│   │       │       └── colors.xml         # Color resources
│   │       └── AndroidManifest.xml
│   ├── build.gradle                        # App-level build config
│   └── proguard-rules.pro
├── build.gradle                            # Project-level build config
├── settings.gradle                         # Project settings
└── gradle.properties                       # Gradle properties
```

## Key Components

### EmojiEncoder.kt
Core encoding/decoding logic that:
- Converts text to UTF-8 bytes
- Maps bytes to Unicode variation selectors
- Extracts bytes from variation selectors
- Handles character encoding/decoding

### MainActivity.kt
Main UI controller that:
- Manages encode/decode mode switching
- Handles emoji/letter selection
- Processes real-time input changes
- Displays output and error messages

### activity_main.xml
UI layout featuring:
- Mode toggle switch
- Input/output text fields
- Emoji selector grid
- Alphabet selector grid
- Error message display

## Dependencies

- **AndroidX Core**: Core Android functionality
- **AppCompat**: Backward compatibility
- **Material Components**: UI components
- **CardView**: Card UI container
- **FlexboxLayout**: Flexible grid layout for emoji buttons
- **Kotlin Standard Library**: Kotlin support

## Testing

The encoder functionality can be tested using the existing test from the web version:

```kotlin
// Test encoding and decoding
val emoji = "😀"
val message = "Hello, World!"
val encoded = EmojiEncoder.encode(emoji, message)
val decoded = EmojiEncoder.decode(encoded)
assert(decoded == message)
```

## Compatibility

- **Minimum SDK**: API 24 (Android 7.0 Nougat)
- **Target SDK**: API 34 (Android 14)
- **Architecture**: Works on ARM and x86 devices

## Known Limitations

- Some older Android keyboards may not preserve variation selectors when typing
- The encoded emoji needs to be copied/pasted to preserve the hidden data
- Very long messages may appear to lag slightly due to real-time encoding

## Contributing

Contributions are welcome! Feel free to:
- Report bugs
- Suggest features
- Submit pull requests

## License

This project inherits the license from the main emoji-encoder repository.

## Related Projects

- [Web Version](../): Next.js web application

## Acknowledgments

This Android app is based on the web version of emoji-encoder, which uses Unicode variation selectors to hide data in emojis.
