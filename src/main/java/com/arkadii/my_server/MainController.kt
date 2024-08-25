package com.arkadii.my_server

import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.SecureRandom

@RestController
class MainController {
    @PostMapping("/upload")
    fun uploadData(@RequestBody data: ByteArray?): ResponseEntity<String> {
        generateRandomData(10)
        return ResponseEntity("Data received", HttpStatus.OK)
    }

    @GetMapping("/download")
    fun downloadRandomData(@RequestParam(name = "size", defaultValue = "1") size: Int): ResponseEntity<Resource> {
        val data = generateRandomData(size)

        val resource = ByteArrayResource(data)

        val headers = HttpHeaders().apply {
            add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=random-data.bin")
            add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
        }

        return ResponseEntity.ok()
            .headers(headers)
            .contentLength(data.size.toLong())
            .body(resource)
    }

    private fun generateRandomData(size: Int): ByteArray {
        var currentSize = size

        if (currentSize > MAX_SIZE_MB) currentSize = MAX_SIZE_MB
        else if (currentSize <= 0) currentSize = NORMAL_SIZE_MB

        val dataSize = currentSize * 1024 * 1024
        val data = ByteArray(dataSize)
        val random = SecureRandom()
        random.nextBytes(data)
        return data
    }

    companion object {
        private const val MAX_SIZE_MB = 100
        private const val NORMAL_SIZE_MB = 10
    }
}