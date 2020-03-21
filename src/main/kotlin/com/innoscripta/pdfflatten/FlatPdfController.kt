package com.innoscripta.pdfflatten

import com.itextpdf.licensekey.LicenseKey
import com.itextpdf.tool.xml.xtra.xfa.MetaData
import com.itextpdf.tool.xml.xtra.xfa.XFAFlattener
import com.itextpdf.tool.xml.xtra.xfa.XFAFlattenerProperties
import models.response.CreateFlatPdfResponse
import org.springframework.core.io.ClassPathResource
import org.springframework.core.io.InputStreamResource
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import java.io.*
import java.util.*

@RestController
@RequestMapping("/flat-pdf")
class FlatPdfController {

    @PostMapping
    fun create(@RequestPart("file") file: MultipartFile): CreateFlatPdfResponse {
        LicenseKey.loadLicenseFile(ClassPathResource("itextkey1581935128088_0.xml").inputStream)
        var outputFileName = ""
        multipartToFile(file, UUID.randomUUID().toString())?.let { outputFileName = manipulatePdf(it) }
        return CreateFlatPdfResponse(outputFileName)
    }

    @GetMapping
    fun download(@RequestParam fileName: String): ResponseEntity<InputStreamResource> {
        val inputStream: InputStream = FileInputStream(System.getProperty("user.dir") + "/outputs/" + fileName)
        val headers = HttpHeaders()
        headers.add("Content-Disposition", "inline; filename=$fileName")

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(InputStreamResource(inputStream));
    }

    @Throws(IllegalStateException::class, IOException::class)
    fun multipartToFile(multipart: MultipartFile, fileName: String): File? {
        val convFile = File(System.getProperty("java.io.tmpdir") + "/" + fileName)
        multipart.transferTo(convFile)
        return convFile
    }

    @Throws(Exception::class)
    protected fun manipulatePdf(file: File): String {

        val outputFileName = UUID.randomUUID().toString() + ".pdf"
        val outputFilePath = System.getProperty("user.dir") + "/outputs/" + outputFileName

        val flattenerProperties = XFAFlattenerProperties()
                .setPdfVersion(XFAFlattenerProperties.PDF_1_7)
                .createXmpMetaData()
                .setTagged()
                .setMetaData(
                        MetaData()
                                .setAuthor("Innoscripta")
                                .setLanguage("EN")
                                .setSubject("PCT data")
                                .setTitle("Flattened PDF")
                )

        val xfaf = XFAFlattener()
                .setFlattenerProperties(flattenerProperties)

        xfaf.flatten(FileInputStream(file), FileOutputStream(outputFilePath))

        return outputFileName
    }
}