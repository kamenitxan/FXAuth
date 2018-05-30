package cz.kamenitxan.FXAuth

import cz.kamenitxan.FXAuth.core.AuthenticatorCLI
import java.util

import scala.collection.mutable

object Main {
	private var secrets: mutable.ListBuffer[String] = mutable.ListBuffer[String]()

	def main(args: Array[String]): Unit = {
		getSecret(args)
	}

	private def getSecret(args: Array[String]) = {
		for (arg <- args) {
			val couple = arg.split(":")
			couple(0) = couple(0).substring(1)
			secrets + couple(1)
		}
	}
}

