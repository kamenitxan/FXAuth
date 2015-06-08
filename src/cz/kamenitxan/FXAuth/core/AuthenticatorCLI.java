package cz.kamenitxan.FXAuth.core;

import cz.kamenitxan.FXAuth.gui.scenes.AController;

import java.security.GeneralSecurityException;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.util.Timer;
import java.util.TimerTask;

public class AuthenticatorCLI {
	Timer timer = null;
	AController aController = AController.getInstalnce();
	String name;
	String secret;

	int count = 1;

	public AuthenticatorCLI(String name, String secret) {
		this.name = name;
		this.secret = secret;
		aController.addSite(this);
	}

	public void reminder() {
		timer = new Timer();
		timer.scheduleAtFixedRate(new TimedPin(secret), 0, 1 * 1000);
	}
	public void stop() {
		timer.cancel();
	}


	class TimedPin extends TimerTask {
		private String secret;

		public TimedPin(String secret) {
			this.secret = secret;
		}

		String previouscode = "";

		public void run() {
			String newout = AuthenticatorCLI.computePin(secret);
			if (previouscode.equals(newout)) {
				System.out.print(".");
			} else {
				if (count <= 30) {
					for (int i = count + 1; i <= 30; i++) {
						System.out.print("+");
					}
				}
				aController.updateCode(newout, name);
				System.out.println(": " + newout + " :");
				count = 0;
			}
			previouscode = newout;
			count++;
			aController.updateTimer(count, name);
		}
	}

	public static String computePin(String secret) {
		if (secret == null || secret.length() == 0) {
			return "Null or empty secret";
		}

		try {
			final byte[] keyBytes = Base32String.decode(secret);
			Mac mac = Mac.getInstance("HMACSHA1");
			mac.init(new SecretKeySpec(keyBytes, ""));
			PasscodeGenerator pcg = new PasscodeGenerator(mac);
			return pcg.generateTimeoutCode();
		} catch (GeneralSecurityException e) {
			return "General security exception";
		} catch (Base32String.DecodingException e) {
			return "Decoding exception";
		}
	}

	public String getName() {
		return name;
	}

	public void addToUI() {
		aController.addSite(this);
	}
}
