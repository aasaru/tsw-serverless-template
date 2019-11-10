package com.getchange.migrant

class MigrantOptions {
    companion object Options {
        const val migrantHistoryTableName = "migrant_migration_history"
        const val migrantVersionFieldName = "version"
        const val scriptNameFieldName = "script_name"
        const val checkSumFieldName = "checksum"
        const val runByFieldName = "run_by"
        const val timeRunFieldName = "timerun"
    }
}
