package com.example.releasemanager.service

import com.example.releasemanager.controller.model.DeployDto

interface ReleaseManagerService {

    fun deploy(deployDto: DeployDto): Int

    fun getServices(systemVersion: Int): Map<String, Int>?

}