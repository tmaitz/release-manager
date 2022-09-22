package com.example.releasemanager.controller

import com.example.releasemanager.controller.model.DeployDto
import com.example.releasemanager.controller.model.ServiceDto
import com.example.releasemanager.service.ReleaseManagerService
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.web.bind.annotation.*

@RestController
class ReleaseManagerController(
    @Qualifier("modernReleaseManagerService")
    private val releaseManagerService: ReleaseManagerService
) {

    @PostMapping("/deploy")
    fun deploy(@RequestBody deployDto: DeployDto): Int {
        return releaseManagerService.deploy(deployDto)
    }

    @GetMapping("/services")
    fun getServices(@RequestParam systemVersion: Int): List<ServiceDto> {
        return releaseManagerService.getServices(systemVersion)
            ?.map { ServiceDto(it.key, it.value) } ?: emptyList()
    }

}