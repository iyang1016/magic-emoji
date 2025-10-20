<img width="626" height="626" alt="moji2" src="https://github.com/user-attachments/assets/c14799af-ac68-4187-829b-0255858cc55d" /># EMOGIC

> **Steganography Engine - Hide Messages in Plain Sight**

Communicate covertly using Unicode steganography. Your messages are monitored? Your chats are being watched? Send secret payloads hidden inside innocent-looking emojis. Only those who know can decode.

![EMOGIC](https://img.shields.io/badge/EMOGIC-Steganography-00FF41?style=for-the-badge&logo=data:image/svg+xml;base64,PHN2ZyB3aWR0aD0iMjQiIGhlaWdodD0iMjQiIHZpZXdCb3g9IjAgMCAyNCAyNCIgZmlsbD0ibm9uZSIgeG1sbnM9Imh0dHA6Ly93d3cudzMub3JnLzIwMDAvc3ZnIj48cGF0aCBkPSJNMTIgMkM2LjQ4IDIgMiA2LjQ4IDIgMTJzNC40OCAxMCAxMCAxMCAxMC00LjQ4IDEwLTEwUzE3LjUyIDIgMTIgMnoiIGZpbGw9IiMwMEZGNDEiLz48L3N2Zz4=)
![Platform](https://img.shields.io/badge/Platform-Web%20%7C%20Android-00D9FF?style=for-the-badge)
![License](https://img.shields.io/badge/License-MIT-FF006E?style=for-the-badge)

## 🔐 Why Use This?

**Scenario**: You need to communicate sensitive information but your messages are being monitored, filtered, or censored.

**Solution**: Hide your real message inside an emoji. To anyone watching, you just sent a 😀. To your friend with the decoder, it's your actual message.

### Real-World Use Cases

- **Bypass Content Filters**: Send messages through platforms that scan for keywords
- **Evade Surveillance**: Communicate without triggering monitoring systems
- **Private Conversations**: Share sensitive info in public channels undetected
- **Censorship Circumvention**: Get around message blocking and filtering
- **Covert Coordination**: Plan and organize without detection
- **Data Exfiltration**: Move information through monitored channels
- **Plausible Deniability**: "I just sent an emoji, officer"

## 💡 How It Works

**The Tech**: Unicode variation selectors - invisible characters that modify emoji appearance. We hijack these to store your actual message as binary data.

**The Magic**: 
1. Your message → Converted to bytes
2. Bytes → Encoded as invisible Unicode characters
3. Invisible characters → Attached to an emoji carrier
4. Result → Looks like 😀 but contains your hidden payload

**The Beauty**: Copy-paste anywhere. The hidden data survives through messaging apps, social media, emails, forums - anywhere that preserves Unicode.

## 🎯 Features

### Core Capabilities
- **Invisible Encoding**: Messages hidden in plain sight using Unicode variation selectors
- **Universal Compatibility**: Works on any platform that supports Unicode
- **No Encryption Keys**: No suspicious key exchange needed
- **Instant Decode**: Paste and reveal in seconds
- **Multiple Carriers**: 22 emojis + 26 alphabet letters to choose from
- **Cross-Platform**: Web app(*not mine* https://emoji-encoder.vercel.app/?mode=encode) + Native Android app

### EMOGIC Android App
- **🎨 Threat actor-Themed UI**: Cyberpunk aesthetic with neon green/cyan colors and monospace fonts
- **🧠 Smart Context**: Paste button only shows in decode mode for cleaner UX
- **⚡ Debounced Input**: Smooth typing without interruptions or keyboard closing
- **📊 Real-time Feedback**: Character/byte counter and loading indicators
- **📋 Quick Actions**: One-tap copy, paste, share, and clear buttons
- **🎯 Centered Layout**: Properly organized emoji/alphabet selector buttons
- **📱 Offline Capable**: Works without internet connection
- **💚 Made with Love**: By YANO

## 🚀 Quick Start

### Web Version
Access the web-based encoder at: https://emoji-encoder.vercel.app/?mode=encode
- Works on any device with a browser
- No installation required
- Clean, simple interface

### EMOGIC Android App
Download the APK from [Releases](../../releases) for a native mobile experience.

**Features**:
- Threat actor-themed cyberpunk UI
- Neon green/cyan color scheme
- Monospace fonts throughout
- Smart context-aware buttons
- Offline functionality
- Smooth, responsive design

**Requirements**: Android 7.0+ (API 24)

**Note**: Both sender and receiver need access to the encoder/decoder. Share the link or app discreetly.

## 📱 Platforms

### Web Application
- **Framework**: Next.js 14 with TypeScript
- **UI**: React with Radix UI components
- **Styling**: Tailwind CSS
- **Access**: https://emoji-encoder.vercel.app/?mode=encode
- Clean, fast, accessible from anywhere

### EMOGIC Android App
- **Language**: Kotlin
- **Minimum SDK**: API 24 (Android 7.0)
- **Target SDK**: API 34 (Android 14)
- **UI**: Material Design 3 with custom cyberpunk theme
- **Colors**: Neon green (#00FF41), Cyan (#00D9FF), Pink (#FF006E)
- **Typography**: Monospace fonts throughout
- **Features**: 
  - Context-aware UI (paste button only in decode mode)
  - Debounced input for smooth typing
  - Centered emoji/alphabet selectors (64dp buttons)
  - Real-time byte counter
  - Copy/share/clear actions
  - Loading indicators
  - Offline capable
- **Developer**: Made with 💖 by YANO

## 📖 Usage

### Encoding (Hiding Your Message)

1. Switch to **ENCODE** mode
2. Select your carrier emoji (😀, 🔥, 💀, etc.)
3. Type your actual message: `"Meet at the usual spot at 9"`
4. Copy the output - looks like just an emoji: 😀
5. Send it through any monitored channel

**What they see**: Just an emoji  
**What your friend decodes**: Your actual message

### Decoding (Reading Hidden Messages)

1. Switch to **DECODE** mode
2. Paste the emoji you received
3. Your hidden message appears instantly

**Pro Tip**: Use common emojis that fit the conversation context. A random 🔥 in a gaming chat? Normal. In a business email? Suspicious.

## 🎭 Operational Security Tips

**DO:**
- Use emojis that fit the conversation context
- Test with your recipient first on a secure channel
- Vary your emoji carriers (don't always use 😀)
- Keep messages reasonably short
- Use in combination with other privacy tools

**DON'T:**
- Use this as your only security measure
- Send obviously out-of-context emojis
- Assume this is unbreakable encryption (it's obfuscation)
- Use for highly sensitive operations without additional security
- Forget that metadata (who, when, where) is still visible

**Remember**: This is steganography (hiding), not cryptography (encrypting). It's about avoiding detection, not preventing decryption if discovered.

## 🛡️ Technical Details

**Encoding Method**: Unicode Variation Selectors (U+FE00-U+FE0F, U+E0100-U+E01EF)

**How it survives**:
- Messaging apps (WhatsApp, Telegram, Signal, Discord)
- Social media (Twitter, Reddit, Facebook)
- Email clients
- Forums and comment sections
- Any platform preserving Unicode

**Limitations**:
- Some platforms strip variation selectors (rare but possible)
- Very long messages create suspiciously long emoji strings
- Not actual encryption - just hiding in plain sight
- Requires both parties to have the encoder/decoder

## ⚠️ Legal Disclaimer

This tool is provided for educational and privacy purposes. Users are responsible for compliance with local laws and regulations. Do not use for illegal activities. The authors assume no liability for misuse.

**Intended Use**: Privacy protection, censorship resistance, educational purposes.

**Not Intended For**: Illegal activities, harassment, or circumventing legitimate security measures.

## 🔗 Links

- Web : [Deploy your own]
- Android : [Check Release](../../releases)

## 📄 License

MIT License - See LICENSE file for details.

## 📸 Screenshots

### EMOGIC Android App
*Cyberpunk-themed steganography engine with neon aesthetics*

**Features Showcase**:
- 🎨 Threat actor UI with neon green/cyan colors
- 📱 Smart context-aware buttons
- 🎯 Centered emoji selector grid
- ⚡ Smooth, debounced input
- 📊 Real-time byte counter
- 💚 Made with love by YANO

---

## 🛠️ Tech Stack

### Android App
- **Language**: Kotlin
- **UI Framework**: Material Design 3
- **Layout**: FlexboxLayout for emoji grid
- **Min SDK**: 24 (Android 7.0)
- **Target SDK**: 34 (Android 14)
- **Build Tool**: Gradle 8.2
- **Architecture**: Single Activity with reactive UI

### Web App
- **Framework**: Next.js 14
- **Language**: TypeScript
- **UI Library**: React + Radix UI
- **Styling**: Tailwind CSS
- **Deployment**: Vercel

---

**Built with 💖 by YANO** | Unicode Steganography | Privacy Through Obscurity

*"The best place to hide a message is where everyone can see it."*

**EMOGIC** - Steganography Engine v1.0
