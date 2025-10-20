# Quick Start Guide

## For Users

### Using the Web App
1. Visit the deployed web app (or run locally with `npm run dev`)
2. Toggle between Encode/Decode mode
3. Select an emoji or letter
4. Type your message
5. Copy the encoded output and share!

### Using the Android App
1. Download the APK or build from source
2. Install on your Android device (API 24+)
3. Toggle between Encode/Decode mode
4. Select an emoji or letter  
5. Type your message
6. Long-press output to copy and share!

## For Developers

### Setting Up Web Development
```bash
git clone [your-repo-url]
cd emoji-encoder
npm install
npm run dev
```
Visit http://localhost:3000

### Setting Up Android Development
```bash
cd emoji-encoder/android-app
# Open in Android Studio
# Or build with Gradle:
./gradlew assembleDebug
./gradlew installDebug
```

### Running Tests

**Web:**
```bash
npm test
```

**Android:**
```bash
cd android-app
./gradlew test
```

## Examples

### Encoding
Input: `Hello, World!`
Emoji: `😀`
Output: `😀` (with hidden variation selectors)

### Decoding
Input: `😀` (with variation selectors)
Output: `Hello, World!`

## How the Encoding Works

1. Your message is converted to UTF-8 bytes
2. Each byte becomes a Unicode variation selector (invisible character)
3. The variation selectors are appended after the emoji
4. When copied, the whole string preserves the hidden message
5. Decoding extracts the bytes and converts back to text

## Supported Characters

- **Messages**: Any UTF-8 text (English, Chinese, emoji, etc.)
- **Carriers**: 22 emojis or 26 alphabet letters (a-z)
- **Platforms**: Any system that preserves Unicode (most modern apps)

## Tips

- ✅ Works great in: messengers, social media, notes apps
- ⚠️ May not work in: some SMS apps, terminals, older systems
- 💡 Best practice: Test with your target platform first
- 🔒 Remember: This is encoding, not encryption (not secure!)

## Troubleshooting

**Problem**: Decoded message is empty or wrong
- **Solution**: Make sure you copied the entire emoji including invisible characters

**Problem**: Android app won't install
- **Solution**: Check that you're running Android 7.0+ (API 24)

**Problem**: Web app won't build
- **Solution**: Ensure Node.js 18+ is installed, run `npm install` again

## Contributing

Found a bug? Want to add a feature? PRs welcome!

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Add tests
5. Submit a PR

## License

See LICENSE file for details.
