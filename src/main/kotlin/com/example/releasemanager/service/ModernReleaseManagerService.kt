package com.example.releasemanager.service

import com.example.releasemanager.controller.model.DeployDto
import org.springframework.stereotype.Service

@Service
class ModernReleaseManagerService: ReleaseManagerService {

    private val versionToSystemMap: MutableMap<Int, Map<String, Int>> = HashMap(mapOf(0 to mapOf()))
    private val systemToVersionMap: HashMap<Map<String, Int>, Int> = HashMap()
    private var latestSystemVersion = 0

    override fun deploy(deployDto: DeployDto): Int {
        val (serviceName, serviceVersionNumber) = deployDto
        val currentServices = versionToSystemMap[latestSystemVersion]!!
        val updatedServices: Map<String, Int> = HashMap(currentServices)
            .also { it[serviceName] = serviceVersionNumber }
        if (!systemToVersionMap.contains(updatedServices)) {
            // prevent concurrent modification
            synchronized(systemToVersionMap) {
                // double-checking
                if (!systemToVersionMap.contains(updatedServices)) {
                    versionToSystemMap[++latestSystemVersion] = updatedServices
                    systemToVersionMap[updatedServices] = latestSystemVersion
                }
            }
        }
        // else -> if this set of services has been encountered before -> just return previous version
        return systemToVersionMap[updatedServices]!!
    }

    override fun getServices(systemVersion: Int): Map<String, Int>? {
        return versionToSystemMap[systemVersion]
    }

}