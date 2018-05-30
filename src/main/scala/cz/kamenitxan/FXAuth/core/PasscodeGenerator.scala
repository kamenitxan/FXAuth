package cz.kamenitxan.FXAuth.core

import java.io.{ByteArrayInputStream, DataInputStream, IOException}
import java.nio.ByteBuffer
import java.security.GeneralSecurityException
import javax.crypto.Mac

/**
  * An implementation of the HOTP generator specified by RFC 4226. Generates
  * short passcodes that may be used in challenge-response protocols or as
  * timeout passcodes that are only valid for a short period.
  *
  * The default passcode is a 6-digit decimal code and the default timeout
  * period is 5 minutes.
  *
  * @author sweis@google.com (Steve Weis)
  *
  */
object PasscodeGenerator {
	/** Default decimal passcode length */
	private val PASS_CODE_LENGTH = 6
	/** Default passcode timeout period (in seconds) */
	private val INTERVAL = 30
	/** The number of previous and future intervals to check */
	private val ADJACENT_INTERVALS = 1
	private val PIN_MODULO = Math.pow(10, PASS_CODE_LENGTH).asInstanceOf[Int]

	/*
	   * Using an interface to allow us to inject different signature
	   * implementations.
	   */ private[core] trait Signer {
		@throws[GeneralSecurityException]
		def sign(data: Array[Byte]): Array[Byte]
	}

	// To facilitate injecting a mock clock
	private[core] trait IntervalClock {
		def getIntervalPeriod: Int

		def getCurrentInterval: Long
	}

}

class PasscodeGenerator(val signer: PasscodeGenerator.Signer, val codeLength: Int, val intervalPeriod: Int) {

	/**
	  * @param mac A { @link Mac} used to generate passcodes
	  */
	def this(mac: Mac) {
		this(mac, PasscodeGenerator.PASS_CODE_LENGTH, PasscodeGenerator.INTERVAL)
	}

	private def padOutput(value: Int) = {
		var result = Integer.toString(value)
		var i = result.length
		while ( i < codeLength) {
			result = "0" + result
			i += 1
		}
		result
	}

	/**
	  * @return A decimal timeout code
	  */
	@throws[GeneralSecurityException]
	def generateTimeoutCode: String = generateResponseCode(clock.getCurrentInterval)

	@throws[GeneralSecurityException]
	def generateNextTimeoutCode: String = generateResponseCode(clock.getCurrentInterval + 1)

	/**
	  * @param challenge A long-valued challenge
	  * @return A decimal response code
	  * @throws GeneralSecurityException If a JCE exception occur
	  */
	@throws[GeneralSecurityException]
	def generateResponseCode(challenge: Long): String = {
		val value = ByteBuffer.allocate(8).putLong(challenge).array
		generateResponseCode(value)
	}

	/**
	  * @param challenge An arbitrary byte array used as a challenge
	  * @return A decimal response code
	  * @throws GeneralSecurityException If a JCE exception occur
	  */
	@throws[GeneralSecurityException]
	def generateResponseCode(challenge: Array[Byte]): String = {
		val hash = signer.sign(challenge)
		// Dynamically truncate the hash
		// OffsetBits are the low order bits of the last byte of the hash
		val offset = hash(hash.length - 1) & 0xF
		// Grab a positive integer value starting at the given offset.
		val truncatedHash = hashToInt(hash, offset) & 0x7FFFFFFF
		val pinValue = truncatedHash % PasscodeGenerator.PIN_MODULO
		padOutput(pinValue)
	}

	/**
	  * Grabs a positive integer value from the input array starting at
	  * the given offset.
	  *
	  * @param bytes the array of bytes
	  * @param start the index into the array to start grabbing bytes
	  * @return the integer constructed from the four bytes in the array
	  */
	private def hashToInt(bytes: Array[Byte], start: Int) = {
		val input = new DataInputStream(new ByteArrayInputStream(bytes, start, bytes.length - start))
		var `val` = 0
		try `val` = input.readInt
		catch {
			case e: IOException =>
				throw new IllegalStateException(e)
		}
		`val`
	}

	/**
	  * @param challenge A challenge to check a response against
	  * @param response  A response to verify
	  * @return True if the response is valid
	  */
	@throws[GeneralSecurityException]
	def verifyResponseCode(challenge: Long, response: Nothing): Boolean = {
		val expectedResponse = generateResponseCode(challenge)
		expectedResponse.equals(response)
	}

	/**
	  * Verify a timeout code. The timeout code will be valid for a time
	  * determined by the interval period and the number of adjacent intervals
	  * checked.
	  *
	  * @param timeoutCode The timeout code
	  * @return True if the timeout code is valid
	  */
	@throws[GeneralSecurityException]
	def verifyTimeoutCode(timeoutCode: Nothing): Boolean = verifyTimeoutCode(timeoutCode, PasscodeGenerator.ADJACENT_INTERVALS, PasscodeGenerator.ADJACENT_INTERVALS)

	/**
	  * Verify a timeout code. The timeout code will be valid for a time
	  * determined by the interval period and the number of adjacent intervals
	  * checked.
	  *
	  * @param timeoutCode     The timeout code
	  * @param pastIntervals   The number of past intervals to check
	  * @param futureIntervals The number of future intervals to check
	  * @return True if the timeout code is valid
	  */
	@throws[GeneralSecurityException]
	def verifyTimeoutCode(timeoutCode: Nothing, pastIntervals: Int, futureIntervals: Int): Boolean = {
		val currentInterval = clock.getCurrentInterval
		val expectedResponse = generateResponseCode(currentInterval)
		if (expectedResponse.equals(timeoutCode)) return true
		var i = 1
		while (i <= pastIntervals) {
			val pastResponse = generateResponseCode(currentInterval - i)
			if (pastResponse.equals(timeoutCode)) return true
			i += 1
		}
		var n = 1
		while (n <= futureIntervals) {
			val futureResponse = generateResponseCode(currentInterval + n)
			if (futureResponse.equals(timeoutCode)) return true
			n += 1
		}
		false
	}

	private val clock = new PasscodeGenerator.IntervalClock() {
		/**
		  * @return The current interval
		  */
		override def getCurrentInterval: Long = {
			val currentTimeSeconds = System.currentTimeMillis / 1000
			currentTimeSeconds / getIntervalPeriod
		}

		override def getIntervalPeriod: Int = intervalPeriod
	}
}