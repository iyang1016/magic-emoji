package com.emojiencoder

import org.junit.Test
import org.junit.Assert.*

/**
 * Unit tests for EmojiEncoder
 * Port of the TypeScript encoding.test.ts implementation
 */
class EmojiEncoderTest {
    
    @Test
    fun testEncodeAndDecodeBasicStrings() {
        val testStrings = listOf(
            "Hello, World!",
            "Testing 123",
            "Special chars: !@#$%^&*()",
            "",  // empty string
            " "  // space only
        )

        for (emoji in EmojiLists.EMOJI_LIST) {
            for (testString in testStrings) {
                val encoded = EmojiEncoder.encode(emoji, testString)
                val decoded = EmojiEncoder.decode(encoded)

                // Ensure decoding returns the original string
                assertEquals("Failed for emoji: $emoji and string: '$testString'", 
                    testString, decoded)
            }
        }
    }

    @Test
    fun testEncodeAndDecodeUnicode() {
        val testStrings = listOf(
            "Unicode: 你好，世界",
            "Emoji: 😀😂🥰",
            "Mixed: Hello 你好 🌍"
        )

        val emoji = "😀"
        for (testString in testStrings) {
            val encoded = EmojiEncoder.encode(emoji, testString)
            val decoded = EmojiEncoder.decode(encoded)

            assertEquals("Failed for string: '$testString'", testString, decoded)
        }
    }

    @Test
    fun testAlphabetEncoding() {
        val testString = "Secret message"
        
        for (letter in EmojiLists.ALPHABET_LIST) {
            val encoded = EmojiEncoder.encode(letter.toString(), testString)
            val decoded = EmojiEncoder.decode(encoded)

            assertEquals("Failed for letter: $letter", testString, decoded)
        }
    }

    @Test
    fun testEmptyString() {
        val emoji = "🚀"
        val encoded = EmojiEncoder.encode(emoji, "")
        val decoded = EmojiEncoder.decode(encoded)

        assertEquals("", decoded)
        // Encoded empty string should just be the emoji
        assertEquals(emoji, encoded)
    }

    @Test
    fun testLongString() {
        val longString = "This is a longer test string with various characters 12345!@#$%"
        val emoji = "💯"
        
        val encoded = EmojiEncoder.encode(emoji, longString)
        val decoded = EmojiEncoder.decode(encoded)

        assertEquals(longString, decoded)
    }

    @Test
    fun testDecodeNonEncodedEmoji() {
        // Decoding a plain emoji should return empty or minimal content
        val plainEmoji = "😀"
        val result = EmojiEncoder.decode(plainEmoji)
        
        // Should return empty string or handle gracefully
        assertNotNull(result)
    }

    @Test
    fun testMultipleEncodeDecodeRounds() {
        val original = "Round trip test"
        val emoji = "🎉"
        
        // First round
        val encoded1 = EmojiEncoder.encode(emoji, original)
        val decoded1 = EmojiEncoder.decode(encoded1)
        assertEquals(original, decoded1)
        
        // Second round - encode the already decoded string
        val encoded2 = EmojiEncoder.encode(emoji, decoded1)
        val decoded2 = EmojiEncoder.decode(encoded2)
        assertEquals(original, decoded2)
    }
}
