package com.emojiencoder

/**
 * Emoji Encoder/Decoder using Unicode variation selectors
 * Port of the TypeScript encoding.ts implementation
 */
object EmojiEncoder {
    // Variation selectors block https://unicode.org/charts/nameslist/n_FE00.html
    // VS1..=VS16
    private const val VARIATION_SELECTOR_START = 0xFE00
    private const val VARIATION_SELECTOR_END = 0xFE0F

    // Variation selectors supplement https://unicode.org/charts/nameslist/n_E0100.html
    // VS17..=VS256
    private const val VARIATION_SELECTOR_SUPPLEMENT_START = 0xE0100
    private const val VARIATION_SELECTOR_SUPPLEMENT_END = 0xE01EF

    /**
     * Convert a byte value to a variation selector character
     * @param byte The byte value (0-255) to convert
     * @return The variation selector character or null if out of range
     */
    private fun toVariationSelector(byte: Int): String? {
        return when {
            byte in 0..15 -> {
                String(Character.toChars(VARIATION_SELECTOR_START + byte))
            }
            byte in 16..255 -> {
                String(Character.toChars(VARIATION_SELECTOR_SUPPLEMENT_START + byte - 16))
            }
            else -> null
        }
    }

    /**
     * Convert a variation selector code point back to a byte value
     * @param codePoint The Unicode code point to convert
     * @return The byte value or null if not a variation selector
     */
    private fun fromVariationSelector(codePoint: Int): Int? {
        return when {
            codePoint in VARIATION_SELECTOR_START..VARIATION_SELECTOR_END -> {
                codePoint - VARIATION_SELECTOR_START
            }
            codePoint in VARIATION_SELECTOR_SUPPLEMENT_START..VARIATION_SELECTOR_SUPPLEMENT_END -> {
                codePoint - VARIATION_SELECTOR_SUPPLEMENT_START + 16
            }
            else -> null
        }
    }

    /**
     * Encode text into an emoji with hidden variation selectors
     * @param emoji The emoji to use as the visible character
     * @param text The text to encode
     * @return The encoded string with the emoji and variation selectors
     */
    fun encode(emoji: String, text: String): String {
        // Convert the string to UTF-8 bytes
        val bytes = text.toByteArray(Charsets.UTF_8)
        val encoded = StringBuilder(emoji)

        for (byte in bytes) {
            val byteValue = byte.toInt() and 0xFF // Convert signed byte to unsigned
            toVariationSelector(byteValue)?.let { encoded.append(it) }
        }

        return encoded.toString()
    }

    /**
     * Decode text from an emoji with hidden variation selectors
     * @param text The text containing encoded emoji
     * @return The decoded text
     * @throws IllegalArgumentException if decoding fails
     */
    fun decode(text: String): String {
        val decoded = mutableListOf<Byte>()
        val codePoints = text.codePoints().toArray()

        for (codePoint in codePoints) {
            val byte = fromVariationSelector(codePoint)

            if (byte == null && decoded.isNotEmpty()) {
                break
            } else if (byte == null) {
                continue
            }

            decoded.add(byte.toByte())
        }

        return decoded.toByteArray().toString(Charsets.UTF_8)
    }
}
