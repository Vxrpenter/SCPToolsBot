package dev.vxrp.updates.data

data class Updates(
    val version: String,
    val settings: UpdatesSettings,
    val configurationUpdate: UpdatesConfigurationUpdate,
    val translationUpdates: UpdatesTranslationUpdates,
    val regularsUpdate: UpdatesRegularsUpdate,
    val additionalInformation: String
)

data class UpdatesSettings(
    val regenerateConfig: Boolean,
    val regenerateStatusSettings: Boolean,
    val regenerateTicketSettings: Boolean,
    val regenerateCommands: Boolean,
    val regenerateConfiguration: Boolean,

    val regenerateTranslations: Boolean
)

data class UpdatesConfigurationUpdate(
    val config: Boolean,
    val statusSettings: Boolean,
    val ticketSettings: Boolean,
    val extra: UpdatesConfigurationUpdateExtra
)

data class UpdatesConfigurationUpdateExtra(
    val commands: Boolean,
    val launchConfiguration: Boolean
)

data class UpdatesTranslationUpdates(
    val enUs: Boolean,
    val deDe: Boolean
)

data class UpdatesRegularsUpdate(
    val config: Boolean,
    val manifest: Boolean
)