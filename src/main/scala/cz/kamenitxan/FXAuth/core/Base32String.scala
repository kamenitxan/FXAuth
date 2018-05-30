package cz.kamenitxan.FXAuth.core

import java.util


/**
  * Encodes arbitrary byte arrays as case-insensitive base-32 strings
  *
  * @author sweis@google.com (Steve Weis)
  * @author Neal Gafter
  */
object Base32String {
	private val DIGITS: Array[Char] = "ABCDEFGHIJKLMNOPQRSTUVWXYZ234567".toCharArray // RFC 4668/3548
	private val MASK = DIGITS.length() - 1
	private val SHIFT = Integer.numberOfTrailingZeros(DIGITS.length)
	private val CHAR_MAP = new util.HashMap[Character, Integer]()


	var i = 0
	while (i < DIGITS.length) {
		CHAR_MAP.put(DIGITS(i), i)
		i += 1
	}


	private[core] val SEPARATOR = "-"

	@throws[DecodingException]
	def decode(encoded: String): Array[Byte] = Base32String.decodeInternal(encoded)

	def encode(data: Array[Byte]): String = Base32String.encodeInternal(data)


	@throws[DecodingException]
	protected def decodeInternal(enc: String): Array[Byte] = { // Remove whitespace and separators
		var encoded = enc
		encoded = encoded.trim.replaceAll(Base32String.SEPARATOR, "").replaceAll(" ", "")
		// Canonicalize to all upper case
		encoded = encoded.toUpperCase
		val zero: Int = 0
		if (encoded.length eq zero) return new Array[Byte](0)
		val encodedLength = encoded.length
		val outLength = encodedLength * SHIFT / 8
		val result = new Array[Byte](outLength)
		var buffer = 0
		var next = 0
		var bitsLeft = 0
		for (c <- encoded.toCharArray) {
			if (!CHAR_MAP.containsKey(c)) throw new DecodingException("Illegal character: " + c)
			buffer <<= SHIFT
			buffer |= CHAR_MAP.get(c) & MASK
			bitsLeft += SHIFT
			if (bitsLeft >= 8) {
				result({
					next += 1; next - 1
				}) = (buffer >> (bitsLeft - 8)).toByte
				bitsLeft -= 8
			}
		}
		// We'll ignore leftover bits for now.
		//
		// if (next != outLength || bitsLeft >= SHIFT) {
		//  throw new DecodingException("Bits left: " + bitsLeft);
		// }
		result
	}

	protected def encodeInternal(data: Array[Byte]): String = {
		if (data.length == 0) return ""
		// SHIFT is the number of bits per output character, so the length of the
		// output is the length of the input multiplied by 8/SHIFT, rounded up.
		if (data.length >= (1 << 28)) { // The computation below will fail, so don't do it.
			throw new IllegalArgumentException
		}
		val outputLength = (data.length * 8 + SHIFT - 1) / SHIFT
		val result: StringBuilder = new StringBuilder(outputLength)
		var buffer = data(0)
		var next = 1
		var bitsLeft = 8
		while (bitsLeft > 0 || next < data.length) {
			if (bitsLeft < SHIFT) if (next < data.length) {
				buffer <<= 8
				buffer |= (data({
					next += 1; next - 1
				}) & 0xff)
				bitsLeft += 8
			}
			else {
				val pad = SHIFT - bitsLeft
				buffer <<= pad
				bitsLeft += pad
			}
			val index = MASK & (buffer >> (bitsLeft - SHIFT))
			bitsLeft -= SHIFT
			result + DIGITS(index)
		}
		result.toString
	}

}
