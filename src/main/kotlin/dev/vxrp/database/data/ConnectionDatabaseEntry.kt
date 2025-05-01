package dev.vxrp.database.data

data class ConnectionDatabaseEntry(
    val id: String,
    val status: Boolean?,
    val maintenance: Boolean?
)