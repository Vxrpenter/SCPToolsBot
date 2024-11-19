package dev.vxrp.database.sqlite

import dev.vxrp.database.sqlite.tables.TicketTableManager
import org.sqlite.SQLiteDataSource
import java.io.File
import java.sql.Connection

class SqliteManager(folder: String, val file: String) {
    private val dir = System.getProperty("user.dir")
    private val connection: Connection

    init {
        val currentFolder = File("$dir/$folder/").also {
            if (!it.exists()) it.mkdirs()
        }

        val file = File("$dir$folder$file").also {
            if (!it.exists()) it.createNewFile()
        }

        val dataSource = SQLiteDataSource()
        dataSource.url = "jdbc:sqlite:${file.path}"
        connection = dataSource.connection
    }

    fun initializeTickets() {
        TicketTableManager(connection)
    }
}