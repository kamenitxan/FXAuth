package cz.kamenitxan.FXAuth.core

import java.security.GeneralSecurityException
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import java.util.Timer
import java.util.TimerTask

object AuthenticatorCLI {
	def computePin(secret: String): String = {
		val zero: Int = 0
		if (secret == null || (secret.length eq zero)) return "Null or empty secret"
		try {
			val keyBytes = Base32String.decode(secret)
			val mac = Mac.getInstance("HMACSHA1")
			mac.init(new SecretKeySpec(keyBytes, ""))
			val pcg = new PasscodeGenerator(mac)
			pcg.generateTimeoutCode
		} catch {
			case e: GeneralSecurityException =>
				"General security exception"
			case e: DecodingException =>
				"Decoding exception"
		}
	}
}

class AuthenticatorCLI(var name: String, var secret: String) {
	private[core] var timer: Timer = _
	private[core] var count = 1

	def reminder(): Unit = {
		timer = new Timer()
		timer.scheduleAtFixedRate(new TimedPin(secret, name), 0, 1 * 1000)
	}

	def stop(): Unit = timer.cancel()


	def getName: String = name

}