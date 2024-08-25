package com.arkadii.my_server

import org.springframework.core.io.ByteArrayResource
import org.springframework.core.io.Resource
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.security.SecureRandom
import java.util.concurrent.TimeUnit

@RestController
class MainController {
    @PostMapping("/upload")
    fun uploadData(@RequestBody data: ByteArray?): ResponseEntity<String> {
        TimeUnit.MILLISECONDS.sleep(100)
        return ResponseEntity("Data received", HttpStatus.OK)
    }

    @GetMapping("/download")
    fun downloadRandomData(@RequestParam(name = "size", defaultValue = "1") size: Int): ResponseEntity<Resource> {
        var currentSize = size
        if (currentSize > MAX_SIZE_MB) {
            currentSize = MAX_SIZE_MB
        }

        val dataSize = currentSize * 1024 * 1024
        val data = generateRandomData(dataSize)

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
        val data = ByteArray(size)
        val random = SecureRandom()
        random.nextBytes(data)
        return data
    }

    companion object {
        private const val MAX_SIZE_MB = 100
    }
}