package com.kyawhtut.pos.service

interface ServiceInterface {

    fun startServer() {}

    fun stopServer() {}

    fun onServerStart(serverIP: String?) {}

    fun onServerError(error: String?) {}

    fun onServerStop() {}
}
