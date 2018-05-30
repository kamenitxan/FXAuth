package cz.kamenitxan.FXAuth.core

import java.util.TimerTask

/**
  * Created by TPa on 30.05.18.
  */
class TimedPin(var secret: String, name: String) extends TimerTask {
	var previouscode = ""

	def run(): Unit = {
		var count = 1
		val newout = AuthenticatorCLI.computePin(secret)
		if (previouscode.equals(newout)) {
			//System.out.print(".");
		}
		else {
			if (count <= 30) {
				var i = count + 1
				while ( {
					i <= 30
				}) {
					//System.out.print("+");
					{
						i += 1; i - 1
					}
				}
			}
			println(name + " : " + newout + " :")
			count = 0
		}
		previouscode = newout
		count += 1
	}
}