package com.example.releasemanager.controller.model

data class DeployDto(
    val serviceName: String,
    val serviceVersionNumber: Int
)