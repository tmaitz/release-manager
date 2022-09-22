package com.example.releasemanager.service

import org.springframework.stereotype.Service

@Service("classicReleaseManagerService")
class ReleaseManagerService {

    private val systemMap: MutableMap<Int, Map<String, Int>> = HashMap()
    private val currentServices: MutableMap<String, Int> = HashMap()
    private var currentSystemVersion = 0

    fun deploy(serviceName: String, serviceVersionNumber: Int): Int {
        if (currentServices[serviceName] != serviceVersionNumber) {
            // prevent concurrent modification
            synchronized(currentServices) {
                // double-checking
                if (currentServices[serviceName] != serviceVersionNumber) {
                    currentSystemVersion++
                    currentServices[serviceName] = serviceVersionNumber
                    systemMap[currentSystemVersion] = HashMap(currentServices)
                }
            }
        }
        return currentSystemVersion
    }

    fun getServicesInfo(systemVersion: Int): Map<String, Int>? {
        return systemMap[systemVersion]
    }

}