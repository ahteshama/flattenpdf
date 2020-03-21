package com.innoscripta.pdfflatten

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PdfFlattenApplication

fun main(args: Array<String>) {
	runApplication<PdfFlattenApplication>(*args)
}
