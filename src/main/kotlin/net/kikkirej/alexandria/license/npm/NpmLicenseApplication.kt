package net.kikkirej.alexandria.license.npm

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NpmLicenseApplication

fun main(args: Array<String>) {
	runApplication<NpmLicenseApplication>(*args)
}
