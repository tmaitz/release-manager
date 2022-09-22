package com.example.releasemanager.service

import com.example.releasemanager.controller.model.DeployDto
import org.springframework.stereotype.Service

@Service
class ClassicReleaseManagerService : ReleaseManagerService {

    private val systemMap: MutableMap<Int, Map<String, Int>> = HashMap(mapOf(0 to mapOf()))
    private var latestSystemVersion = 0

    override fun deploy(deployDto: DeployDto): Int {
        val (serviceName, serviceVersionNumber) = deployDto
        val currentServices: Map<String, Int> = systemMap[latestSystemVersion]!!
        if (currentServices[serviceName] != serviceVersionNumber) {
            // prevent concurrent modification
            synchronized(systemMap) {
                // double-checking
                if (currentServices[serviceName] != serviceVersionNumber) {
                    val nextServices = HashMap(currentServices)
                        .also { it[serviceName] = serviceVersionNumber }
                    systemMap[++latestSystemVersion] = HashMap(nextServices)
                }
            }
        }
        return latestSystemVersion
    }

    override fun getServices(systemVersion: Int): Map<String, Int>? {
        return systemMap[systemVersion]
    }

}