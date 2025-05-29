package dev.vxrp.configuration.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Translation(
    @SerialName("PERMISSIONS")
    val permissions: TranslationPermissions,
    @SerialName("SETTINGS")
    val settings: TranslationSettings,
    @SerialName("STATUS")
    val status: TranslationStatus,
    @SerialName("HELP")
    val help: TranslationHelp,
    @SerialName("PLAYER")
    val player: TranslationPlayer,
    @SerialName("SUPPORT")
    val support: TranslationSupport,
    @SerialName("APPLICATION")
    val application: TranslationApplication,
    @SerialName("VERIFY")
    val verify: TranslationVerify,
    @SerialName("NOTICE_OF_DEPARTURE")
    val noticeOfDeparture: TranslationNoticeOfDeparture,
    @SerialName("REGULARS")
    val regulars: TranslationRegulars,
    @SerialName("STATUS_BARS")
    val statusBars: TranslationStatusBars,
    @SerialName("BUTTONS")
    val buttons: TranslationButtons,
    @SerialName("SELECT_MENUS")
    val selectMenus: TranslationSelectMenus
)

@Serializable
data class TranslationPermissions(
    @SerialName("EMBED_NOT_FOUND_TITLE")
    val embedNotFoundTitle: String,
    @SerialName("EMBED_NOT_FOUND_BODY")
    val embedNotFoundBody: String,

    @SerialName("EMBED_COULD_NOT_SEND_PANEL_TITLE")
    val embedCouldNotSendPanelTitle: String,
    @SerialName("EMBED_COULD_NOT_SEND_PANEL_BODY")
    val embedCouldNotSendPanelBody: String,


    @SerialName("EMBED_COULD_NOT_SEND_MODAL_TITLE")
    val embedCouldNotSendModalTitle: String,
    @SerialName("EMBED_COULD_NOT_SEND_MODAL_BODY")
    val embedCouldNotSendModalBody: String,

    @SerialName("EMBED_COULD_NOT_SEND_COMMAND_TITLE")
    val embedCouldNotSendCommandTitle: String,
    @SerialName("EMBED_COULD_NOT_SEND_COMMAND_BODY")
    val embedCouldNotSendCommandBody: String,


    @SerialName("EMBED_COULD_NOT_SEND_TEMPLATE_TITLE")
    val embedCouldNotSendTemplateTitle: String,
    @SerialName("EMBED_COULD_NOT_SEND_TEMPLATE_BODY")
    val embedCouldNotSendTemplateBody: String,

    @SerialName("EMBED_TICKET_DENIED_TITLE")
    val embedTicketDeniedTitle: String,
    @SerialName("EMBED_TICKET_DENIED_BODY")
    val embedTicketDeniedBody: String,

    @SerialName("EMBED_NOTICE_OF_DEPARTURE_DENIED_TITLE")
    val embedNoticeOfDepartureDeniedTitle: String,
    @SerialName("EMBED_NOTICE_OF_DEPARTURE_DENIED_BODY")
    val embedNoticeOfDepartureDeniedBody: String,

    @SerialName("TEXT_INSUFFICIENT_PERMISSION")
    val textInsufficientPermission: String,
    @SerialName("TEXT_INTERACTION_DISABLED")
    val textInteractionDisabled: String
)

@Serializable
data class TranslationSettings(
    @SerialName("EMBED_SETTINGS_TITLE")
    val embedSettingsTitle: String,
    @SerialName("EMBED_SETTINGS_BODY")
    val embedSettingsBody: String,
    @SerialName("EMBED_SETTINGS_FIELD_LANGUAGE_TITLE")
    val embedSettingsFieldLanguageTitle: String,
    @SerialName("EMBED_SETTINGS_FIELD_LANGUAGE_VALUE")
    val embedSettingsFieldLanguageValue: String,
    @SerialName("EMBED_SETTINGS_FIELD_GUILD_TITLE")
    val embedSettingsFieldGuildTitle: String,
    @SerialName("EMBED_SETTINGS_FIELD_GUILD_VALUE")
    val embedSettingsFieldGuildValue: String,
    @SerialName("EMBED_SETTINGS_FIELD_DATABASE_TITLE")
    val embedSettingsFieldDatabaseTitle: String,
    @SerialName("EMBED_SETTINGS_FIELD_DATABASE_VALUE")
    val embedSettingsFieldDatabaseValue: String,
    @SerialName("EMBED_SETTINGS_FIELD_CEDMOD_TITLE")
    val embedSettingsFieldCedmodTitle: String,
    @SerialName("EMBED_SETTINGS_FIELD_CEDMOD_VALUE")
    val embedSettingsFieldCedmodValue: String,
    @SerialName("EMBED_SETTINGS_FIELD_VERSION_TITLE")
    val embedSettingsFieldVersionTitle: String,
    @SerialName("EMBED_SETTINGS_FIELD_VERSION_VALUE")
    val embedSettingsFieldVersionValue: String,
    @SerialName("EMBED_SETTINGS_FIELD_BUILD_TITLE")
    val embedSettingsFieldBuildTitle: String,
    @SerialName("EMBED_SETTINGS_FIELD_BUILD_VALUE")
    val embedSettingsFieldBuildValue: String,
    @SerialName("EMBED_SETTINGS_FIELD_GATEWAY_TITLE")
    val embedSettingsFieldGatewayTitle: String,
    @SerialName("EMBED_SETTINGS_FIELD_GATEWAY_VALUE")
    val embedSettingsFieldGatewayValue: String,
    @SerialName("EMBED_SETTINGS_FIELD_REST_TITLE")
    val embedSettingsFieldRestTitle: String,
    @SerialName("EMBED_SETTINGS_FIELD_REST_VALUE")
    val embedSettingsFieldRestValue: String,
    @SerialName("TEXT_CEDMOD_ONLINE")
    val textCedmodOnline: String,
    @SerialName("TEXT_CEDMOD_OFFLINE")
    val textCedmodOffline: String,
    @SerialName("TEXT_DATABASE_ONLINE")
    val textDatabaseOnline: String,
    @SerialName("TEXT_DATABASE_OFFLINE")
    val textDatabaseOffline: String
)

@Serializable
data class TranslationHelp(
    @SerialName("EMBED_PAGE_ONE_TITLE")
    val embedPageOneTitle: String,
    @SerialName("EMBED_PAGE_ONE_BODY")
    val embedPageOneBody: String,
    @SerialName("EMBED_PAGE_TWO_TITLE")
    val embedPageTwoTitle: String,
    @SerialName("EMBED_PAGE_TWO_BODY")
    val embedPageTwoBody: String,
    @SerialName("EMBED_PAGE_THREE_TITLE")
    val embedPageThreeTitle: String,
    @SerialName("EMBED_PAGE_THREE_BODY")
    val embedPageThreeBody: String,
    @SerialName("EMBED_PAGE_FOUR_TITLE")
    val embedPageFourTitle: String,
    @SerialName("EMBED_PAGE_FOUR_BODY")
    val embedPageFourBody: String,
    @SerialName("EMBED_PAGE_FIVE_TITLE")
    val embedPageFiveTitle: String,
    @SerialName("EMBED_PAGE_FIVE_BODY")
    val embedPageFiveBody: String,
    @SerialName("EMBED_PAGE_SIX_TITLE")
    val embedPageSixTitle: String,
    @SerialName("EMBED_PAGE_SIX_BODY")
    val embedPageSixBody: String,
    @SerialName("EMBED_FOOTER_TEXT")
    val embedFooterText: String,
    @SerialName("EMBED_FOOTER_IMG")
    val embedFooterImg: String
)

@Serializable
data class TranslationStatus(
    @SerialName("ACTIVITY_OFFLINE")
    val activityOffline: String,
    @SerialName("ACTIVITY_MAINTENANCE")
    val activityMaintenance: String,
    @SerialName("EMBED_ESTABLISHED_TITLE")
    val embedEstablishedTitle: String,
    @SerialName("EMBED_ESTABLISHED_BODY")
    val embedEstablishedBody: String,
    @SerialName("EMBED_ESTABLISHED_REASON_FIELD_NAME")
    val embedEstablishedReasonFieldName: String,
    @SerialName("EMBED_ESTABLISHED_REASON_FIELD_VALUE")
    val embedEstablishedReasonFieldValue: String,
    @SerialName("EMBED_ESTABLISHED_RESPONSE_FIELD_NAME")
    val embedEstablishedResponseFieldName: String,
    @SerialName("EMBED_ESTABLISHED_RESPONSE_FIELD_VALUE")
    val embedEstablishedResponseFieldValue: String,
    @SerialName("EMBED_LOST_TITLE")
    val embedLostTitle: String,
    @SerialName("EMBED_LOST_BODY")
    val embedLostBody: String,
    @SerialName("EMBED_LOST_REASON_FIELD_NAME")
    val embedLostReasonFieldName: String,
    @SerialName("EMBED_LOST_REASON_FIELD_VALUE")
    val embedLostReasonFieldValue: String,
    @SerialName("EMBED_LOST_RESPONSE_FIELD_NAME")
    val embedLostResponseFieldName: String,
    @SerialName("EMBED_LOST_RESPONSE_FIELD_VALUE")
    val embedLostResponseFieldValue: String,

    @SerialName("EMBED_ONLINE_TITLE")
    val embedOnlineTitle: String,
    @SerialName("EMBED_ONLINE_BODY")
    val embedOnlineBody: String,
    @SerialName("EMBED_ONLINE_REASON_FIELD_NAME")
    val embedOnlineReasonFieldName: String,
    @SerialName("EMBED_ONLINE_REASON_FIELD_VALUE")
    val embedOnlineReasonFieldValue: String,
    @SerialName("EMBED_ONLINE_RESPONSE_FIELD_NAME")
    val embedOnlineResponseFieldName: String,
    @SerialName("EMBED_ONLINE_RESPONSE_FIELD_VALUE")
    val embedOnlineResponseFieldValue: String,
    @SerialName("EMBED_OFFLINE_TITLE")
    val embedOfflineTitle: String,
    @SerialName("EMBED_OFFLINE_BODY")
    val embedOfflineBody: String,
    @SerialName("EMBED_OFFLINE_REASON_FIELD_NAME")
    val embedOfflineReasonFieldName: String,
    @SerialName("EMBED_OFFLINE_REASON_FIELD_VALUE")
    val embedOfflineReasonFieldValue: String,
    @SerialName("EMBED_OFFLINE_RESPONSE_FIELD_NAME")
    val embedOfflineResponseFieldName: String,
    @SerialName("EMBED_OFFLINE_RESPONSE_FIELD_VALUE")
    val embedOfflineResponseFieldValue: String,

    @SerialName("EMBED_MAINTENANCE_ON_TITLE")
    val embedMaintenanceOnTitle: String,
    @SerialName("EMBED_MAINTENANCE_ON_BODY")
    val embedMaintenanceOnBody: String,
    @SerialName("EMBED_MAINTENANCE_ON_REASON_FIELD_NAME")
    val embedMaintenanceOnReasonFieldName: String,
    @SerialName("EMBED_MAINTENANCE_ON_REASON_FIELD_VALUE")
    val embedMaintenanceOnReasonFieldValue: String,
    @SerialName("EMBED_MAINTENANCE_ON_RESPONSE_FIELD_NAME")
    val embedMaintenanceOnResponseFieldName: String,
    @SerialName("EMBED_MAINTENANCE_ON_RESPONSE_FIELD_VALUE")
    val embedMaintenanceOnResponseFieldValue: String,
    @SerialName("EMBED_MAINTENANCE_OFF_TITLE")
    val embedMaintenanceOffTitle: String,
    @SerialName("EMBED_MAINTENANCE_OFF_BODY")
    val embedMaintenanceOffBody: String,
    @SerialName("EMBED_MAINTENANCE_OFF_REASON_FIELD_NAME")
    val embedMaintenanceOffReasonFieldName: String,
    @SerialName("EMBED_MAINTENANCE_OFF_REASON_FIELD_VALUE")
    val embedMaintenanceOffReasonFieldValue: String,
    @SerialName("EMBED_MAINTENANCE_OFF_RESPONSE_FIELD_NAME")
    val embedMaintenanceOffResponseFieldName: String,
    @SerialName("EMBED_MAINTENANCE_OFF_RESPONSE_FIELD_VALUE")
    val embedMaintenanceOffResponseFieldValue: String,
    @SerialName("EMBED_PLAYERLIST_TITLE")
    val embedPlayerlistTitle: String,
    @SerialName("EMBED_PLAYERLIST_BODY")
    val embedPlayerlistBody: String,
    @SerialName("EMBED_PLAYERLIST_EMPTY")
    val embedPlayerlistEmpty: String,
    @SerialName("EMBED_PLAYERLIST_COULDNT_FETCH")
    val embedPlayerlistCouldntFetch: String,
    @SerialName("EMBED_PLAYERLIST_PLAYER")
    val embedPlayerlistPlayer: String,

    @SerialName("EMBED_STATUS_ACTIVATED_TITLE")
    val embedStatusActivatedTitle: String,
    @SerialName("EMBED_STATUS_ACTIVATED_BODY")
    val embedStatusActivatedBody: String,

    @SerialName("EMBED_STATUS_DEACTIVATED_TITLE")
    val embedStatusDeactivatedTitle: String,
    @SerialName("EMBED_STATUS_DEACTIVATED_BODY")
    val embedStatusDeactivatedBody: String
    )

@Serializable
data class TranslationPlayer(
    @SerialName("EMBED_STATISTICS_BODY")
    val embedStatisticsBody: String,
    @SerialName("EMBED_STEAMID_FIELD_NAME")
    val embedStatisticsSteamIdFieldName: String,
    @SerialName("EMBED_STEAMID_FIELD_VALUE")
    val embedStatisticsSteamIdFieldValue: String,
    @SerialName("EMBED_PLAYTIME_FIELD_NAME")
    val embedStatisticsPlaytimeFiledName: String,
    @SerialName("EMBED_PLAYTIME_FIELD_VALUE")
    val embedStatisticsPlaytimeFieldValue: String,
    @SerialName("EMBED_BANNED_FIELD_NAME")
    val embedStatisticsBannedFieldName: String,
    @SerialName("EMBED_BANNED_FIELD_VALUE")
    val embedStatisticsBannedFieldValue: String,
    @SerialName("EMBED_FILLER_ONE_FIELD_NAME")
    val embedStatisticsFillerOneFiledName: String,
    @SerialName("EMBED_FILLER_ONE_FIELD_VALUE")
    val embedStatisticsFillerOneFiledValue: String,
    @SerialName("EMBED_WARNS_FIELD_NAME")
    val embedStatisticsWarnsFieldName: String,
    @SerialName("EMBED_WARNS_FIELD_VALUE")
    val embedStatisticsWarnsFieldValue: String,
    @SerialName("EMBED_MUTES_FIELD_NAME")
    val embedStatisticsMutesFieldName: String,
    @SerialName("EMBED_MUTES_FIELD_VALUE")
    val embedStatisticsMutesFieldValue: String,
    @SerialName("EMBED_WATCHLIST_FIELD_NAME")
    val embedStatisticsWatchlistFieldName: String,
    @SerialName("EMBED_WATCHLIST_FIELD_VALUE")
    val embedStatisticsWatchlistFieldValue: String,
    @SerialName("EMBED_FILLER_TWO_FIELD_NAME")
    val embedStatisticsFillerTwoFiledName: String,
    @SerialName("EMBED_FILLER_TWO_FIELD_VALUE")
    val embedStatisticsFillerTwoFiledValue: String
)

@Serializable
data class TranslationSupport(
    @SerialName("EMBED_TEMPLATE_SUPPORT_TITLE")
    val embedTemplateSupportTitle: String,
    @SerialName("EMBED_TEMPLATE_SUPPORT_BODY")
    val embedTemplateSupportBody: String,

    @SerialName("EMBED_REPORT_USER_TITLE")
    val embedReportUserTitle: String,
    @SerialName("EMBED_REPORT_USER_BODY")
    val embedReportUserBody: String,

    @SerialName("EMBED_COMPLAINT_ANONYMOUS_TITLE")
    val embedComplaintAnonymousTitle: String,
    @SerialName("EMBED_COMPLAINT_ANONYMOUS_BODY")
    val embedComplaintAnonymousBody: String,

    @SerialName("EMBED_COMPLAINT_USER_TITLE")
    val embedComplaintUserTitle: String,
    @SerialName("EMBED_COMPLAINT_USER_BODY")
    val embedComplaintUserBody: String,

    @SerialName("EMBED_APPLICATION_POSITION_TITLE")
    val embedApplicationPositionTitle: String,
    @SerialName("EMBED_APPLICATION_POSITION_BODY")
    val embedApplicationPositionBody: String,

    @SerialName("MODAL_GENERAL_TITLE")
    val modalGeneralTitle: String,
    @SerialName("MODAL_GENERAL_SUBJECT_TITLE")
    val modalGeneralSubjectTitle: String,
    @SerialName("MODAL_GENERAL_SUBJECT_PLACEHOLDER")
    val modalGeneralSubjectPlaceholder: String,
    @SerialName("MODAL_GENERAL_EXPLANATION_TITLE")
    val modalGeneralExplanationTitle: String,
    @SerialName("MODAL_GENERAL_EXPLANATION_PLACEHOLDER")
    val modalGeneralExplanationPlaceholder: String,

    @SerialName("MODAL_REPORT_TITLE")
    val modalReportTitle: String,
    @SerialName("MODAL_REPORT_REASON_TITLE")
    val modalReportReasonTitle: String,
    @SerialName("MODAL_REPORT_REASON_PLACEHOLDER")
    val modalReportReasonPlaceholder: String,
    @SerialName("MODAL_REPORT_PROOF_TITLE")
    val modalReportProofTitle: String,
    @SerialName("MODAL_REPORT_PROOF_PLACEHOLDER")
    val modalReportProofPlaceholder: String,

    @SerialName("MODAL_ERROR_TITLE")
    val modalErrorTitle: String,
    @SerialName("MODAL_ERROR_PROBLEM_TITLE")
    val modalErrorProblemTitle: String,
    @SerialName("MODAL_ERROR_PROBLEM_PLACEHOLDER")
    val modalErrorProblemPlaceholder: String,
    @SerialName("MODAL_ERROR_TIMES_TITLE")
    val modalErrorTimesTitle: String,
    @SerialName("MODAL_ERROR_TIMES_PLACEHOLDER")
    val modalErrorTimesPlaceholder: String,
    @SerialName("MODAL_ERROR_REPRODUCE_TITLE")
    val modalErrorReproduceTitle: String,
    @SerialName("MODAL_ERROR_REPRODUCE_PLACEHOLDER")
    val modalErrorReproducePlaceholder: String,
    @SerialName("MODAL_ERROR_ADDITIONAL_TITLE")
    val modalErrorAdditionalTitle: String,
    @SerialName("MODAL_ERROR_ADDITIONAL_PLACEHOLDER")
    val modalErrorAdditionalPlaceholder: String,

    @SerialName("MODAL_UNBAN_TITLE")
    val modalUnbanTitle: String,
    @SerialName("MODAL_UNBAN_STEAMID_TITLE")
    val modalUnbanSteamIdTitle: String,
    @SerialName("MODAL_UNBAN_STEAMID_PLACEHOLDER")
    val modalUnbanSteamIdPlaceholder: String,
    @SerialName("MODAL_UNBAN_REASON_TITLE")
    val modalUnbanReasonTitle: String,
    @SerialName("MODAL_UNBAN_REASON_PLACEHOLDER")
    val modalUnbanReasonPlaceholder: String,

    @SerialName("MODAL_COMPLAINT_TITLE")
    val modalComplaintTitle: String,
    @SerialName("MODAL_COMPLAINT_SUBJECT_TITLE")
    val modalComplaintSubjectTitle: String,
    @SerialName("MODAL_COMPLAINT_SUBJECT_PLACEHOLDER")
    val modalComplaintSubjectPlaceholder: String,
    @SerialName("MODAL_COMPLAINT_EXPLANATION_TITLE")
    val modalComplaintExplanationTitle: String,
    @SerialName("MODAL_COMPLAINT_EXPLANATION_PLACEHOLDER")
    val modalComplaintExplanationPlaceholder: String,

    @SerialName("MODAL_APPLICATION_TITLE")
    val modalApplicationTitle: String,
    @SerialName("MODAL_APPLICATION_NAME_TITLE")
    val modalApplicationNameTitle: String,
    @SerialName("MODAL_APPLICATION_PLACEHOLDER_TITLE")
    val modalApplicationNamePlaceholder: String,
    @SerialName("MODAL_APPLICATION_AGE_TITLE")
    val modalApplicationAgeTitle: String,
    @SerialName("MODAL_APPLICATION_AGE_PLACEHOLDER")
    val modalApplicationAgePlaceholder: String,
    @SerialName("MODAL_APPLICATION_PLAYTIME_TITLE")
    val modalApplicationPlaytimeTitle: String,
    @SerialName("MODAL_APPLICATION_PLAYTIME_PLACEHOLDER")
    val modalApplicationPlaytimePlaceholder: String,
    @SerialName("MODAL_APPLICATION_REASON_OF_APPLICATION_TITLE")
    val modalApplicationReasonsOfApplicationTitle: String,
    @SerialName("MODAL_APPLICATION_REASON_OF_APPLICATION_PLACEHOLDER")
    val modalApplicationReasonsOfApplicationPlaceholder: String,
    @SerialName("MODAL_APPLICATION_SKILLS_TITLE")
    val modalApplicationSkillsTitle: String,
    @SerialName("MODAL_APPLICATION_SKILLS_PLACEHOLDER")
    val modalApplicationSkillsPlaceholder: String,

    @SerialName("MODAL_REASON_ACTION_TITLE")
    val modalReasonActionTitle: String,
    @SerialName("MODAL_REASON_ACTION_REASON_TITLE")
    val modalReasonActionReasonTitle: String,
    @SerialName("MODAL_REASON_ACTION_REASON_PLACEHOLDER")
    val modalReasonActionPlaceholder: String,

    @SerialName("EMBED_TICKET_GENERAL_TITLE")
    val embedTicketGeneralTitle: String,
    @SerialName("EMBED_TICKET_GENERAL_BODY")
    val embedTicketGeneralBody: String,

    @SerialName("EMBED_TICKET_REPORT_TITLE")
    val embedTicketReportTitle: String,
    @SerialName("EMBED_TICKET_REPORT_BODY")
    val embedTicketReportBody: String,

    @SerialName("EMBED_TICKET_ERROR_TITLE")
    val embedTicketErrorTitle: String,
    @SerialName("EMBED_TICKET_ERROR_BODY")
    val embedTicketErrorBody: String,

    @SerialName("EMBED_TICKET_UNBAN_TITLE")
    val embedTicketUnbanTitle: String,
    @SerialName("EMBED_TICKET_UNBAN_BODY")
    val embedTicketUnbanBody: String,

    @SerialName("EMBED_TICKET_COMPLAINT_TITLE")
    val embedTicketComplaintTitle: String,
    @SerialName("EMBED_TICKET_COMPLAINT_BODY")
    val embedTicketComplaintBody: String,

    @SerialName("EMBED_TICKET_APPLICATION_TITLE")
    val embedTicketApplicationTitle: String,
    @SerialName("EMBED_TICKET_APPLICATION_BODY")
    val embedTicketApplicationBody: String,

    @SerialName("EMBED_TICKET_CREATED_TITLE")
    val embedTicketCreatedTitle: String,
    @SerialName("EMBED_TICKET_CREATED_BODY")
    val embedTicketCreatedBody: String,

    @SerialName("EMBED_SETTINGS_TITLE")
    val embedSettingsTitle: String,
    @SerialName("EMBED_SETTINGS_BODY")
    val embedSettingsBody: String,

    @SerialName("EMBED_LOG_TITLE")
    val embedLogTitle: String,
    @SerialName("EMBED_LOG_BODY")
    val embedLogBody: String,

    @SerialName("EMBED_TICKET_CLAIMED_TITLE")
    val embedTicketClaimedTitle: String,
    @SerialName("EMBED_TICKET_CLAIMED_BODY")
    val embedTicketClaimedBody: String,

    @SerialName("EMBED_TICKET_NO_HANDLER_TITLE")
    val embedTicketNoHandlerTitle: String,
    @SerialName("EMBED_TICKET_NO_HANDLER_BODY")
    val embedTicketNoHandlerBody: String,

    @SerialName("EMBED_TICKET_OPENED_TITLE")
    val embedTicketOpenedTitle: String,
    @SerialName("EMBED_TICKET_OPENED_BODY")
    val embedTicketOpenedBody: String,

    @SerialName("EMBED_TICKET_PAUSED_TITLE")
    val embedTicketPausedTitle: String,
    @SerialName("EMBED_TICKET_PAUSED_BODY")
    val embedTicketPausedBody: String,

    @SerialName("EMBED_TICKET_SUSPENDED_TITLE")
    val embedTicketSuspendedTitle: String,
    @SerialName("EMBED_TICKET_SUSPENDED_BODY")
    val embedTicketSuspendedBody: String,

    @SerialName("EMBED_TICKET_CLOSED_TITLE")
    val embedTicketClosedTitle: String,
    @SerialName("EMBED_TICKET_CLOSED_BODY")
    val embedTicketClosedBody: String,

    @SerialName("EMBED_LOG_CLAIMED_TITLE")
    val embedLogClaimedTitle: String,
    @SerialName("EMBED_LOG_CLAIMED_BODY")
    val embedLogClaimedBody: String,

    @SerialName("EMBED_LOG_OPENED_TITLE")
    val embedLogOpenedTitle: String,
    @SerialName("EMBED_LOG_OPENED_BODY")
    val embedLogOpenedBody: String,

    @SerialName("EMBED_LOG_PAUSED_TITLE")
    val embedLogPausedTitle: String,
    @SerialName("EMBED_LOG_PAUSED_BODY")
    val embedLogPausedBody: String,

    @SerialName("EMBED_LOG_SUSPENDED_TITLE")
    val embedLogSuspendedTitle: String,
    @SerialName("EMBED_LOG_SUSPENDED_BODY")
    val embedLogSuspendedBody: String,

    @SerialName("EMBED_LOG_CLOSED_TITLE")
    val embedLogClosedTitle: String,
    @SerialName("EMBED_LOG_CLOSED_BODY")
    val embedLogClosedBody: String,
)

@Serializable
data class TranslationApplication(
    @SerialName("EMBED_ACTIVATION_MENU_TITLE")
    val embedActivationMenuTitle: String,
    @SerialName("EMBED_ACTIVATION_MENU_BODY")
    val embedActivationMenuBody: String,

    @SerialName("EMBED_DEACTIVATION_MENU_TITLE")
    val embedDeactivationMenuTitle: String,
    @SerialName("EMBED_DEACTIVATION_MENU_BODY")
    val embedDeactivationMenuBody: String,

    @SerialName("EMBED_APPLICATION_MESSAGE_TITLE")
    val embedApplicationMessageTitle: String,
    @SerialName("EMBED_APPLICATION_MESSAGE_BODY")
    val embedApplicationMessageBody: String,

    @SerialName("TEXT_ROLE_STATUS_TEMPLATE")
    val textRoleStatusTemplate: String,
    @SerialName("TEXT_STATUS_ACTIVE")
    val textStatusActive: String,
    @SerialName("TEXT_STATUS_DEACTIVATED")
    val textStatusDeactivated: String,

    @SerialName("EMBED_CHOOSE_POSITION_TITLE")
    val embedChoosePositionTitle: String,
    @SerialName("EMBED_CHOOSE_POSITION_BODY")
    val embedChoosePositionBody: String,

    @SerialName("MODAL_CHOOSE_COUNT_TITLE")
    val modalChooseCountTitle: String,
    @SerialName("MODAL_CHOOSE_COUNT_NUMBER_TITLE")
    val modalChooseCountNumberTitle: String,
    @SerialName("MODAL_CHOOSE_COUNT_NUMBER_PLACEHOLDER")
    val modalChooseCountNumberPlaceholder: String
)

@Serializable
data class TranslationVerify(
    @SerialName("EMBED_TEMPLATE_TITLE")
    val embedTemplateTitle: String,
    @SerialName("EMBED_TEMPLATE_BODY")
    val embedTemplateBody: String,

    @SerialName("EMBED_DATA_TITLE")
    val embedDataTitle: String,
    @SerialName("EMBED_DATA_BODY")
    val embedDataBody: String,

    @SerialName("EMBED_DATA_FIELD_VERIFIED_TITLE")
    val embedDataFieldVerifiedTitle: String,
    @SerialName("EMBED_DATA_FIELD_STEAMID_TITLE")
    val embedDataFieldSteamIdTitle: String,
    @SerialName("EMBED_DATA_FIELD_TIMESTAMP_TITLE")
    val embedDataFieldTimestampTitle: String,
    @SerialName("EMBED_DATA_FIELD_DELETE_VALUE")
    val embedDataFieldDeleteValue: String,



    @SerialName("EMBED_NO_DATA_TITLE")
    val embedNoDataTitle: String,
    @SerialName("EMBED_NO_DATA_BODY")
    val embedNoDataBody: String,

    @SerialName("EMBED_LOG_VERIFIED_TITLE")
    val embedLogVerifiedTitle: String,
    @SerialName("EMBED_LOG_VERIFIED_BODY")
    val embedLogVerifiedBody: String,

    @SerialName("EMBED_LOG_DELETED_TITLE")
    val embedLogDeletedTitle: String,
    @SerialName("EMBED_LOG_DELETED_BODY")
    val embedLogDeletedBody: String,

    @SerialName("EMBED_DELETION_SENT_TITLE")
    val embedDeletionSentTitle: String,
    @SerialName("EMBED_DELETION_SENT_BODY")
    val embedDeletionSentBody: String,
)

@Serializable
data class TranslationNoticeOfDeparture(
    @SerialName("EMBED_TEMPLATE_TITLE")
    val embedTemplateTitle: String,
    @SerialName("EMBED_TEMPLATE_BODY")
    val embedTemplateBody: String,

    @SerialName("MODAL_TITLE")
    val modalTitle: String,
    @SerialName("MODAL_TIME_TITLE")
    val modalTimeTitle: String,
    @SerialName("MODAL_TIME_PLACEHOLDER")
    val modalTimePlaceHolder: String,
    @SerialName("MODAL_EXPLANATION_TITLE")
    val modalExplanationTitle: String,
    @SerialName("MODAL_EXPLANATION_PLACEHOLDER")
    val modalExplanationPlaceHolder: String,

    @SerialName("EMBED_ENTER_VALID_DATE_TITLE")
    val embedEnterValidDateTitle: String,
    @SerialName("EMBED_ENTER_VALID_DATE_BODY")
    val embedEnterValidDateBody: String,

    @SerialName("EMBED_ENTER_FUTURE_DATE_TITLE")
    val embedEnterFutureDateTitle: String,
    @SerialName("EMBED_ENTER_FUTURE_DATE_BODY")
    val embedEnterFutureDateBody: String,

    @SerialName("EMBED_DECISION_TITLE")
    val embedDecisionTitle: String,
    @SerialName("EMBED_DECISION_BODY")
    val embedDecisionBody: String,

    @SerialName("EMBED_DECISION_SENT_TITLE")
    val embedDecisionSentTitle: String,
    @SerialName("EMBED_DECISION_SENT_BODY")
    val embedDecisionSentBody: String,

    @SerialName("EMBED_ACCEPTED_TITLE")

    val embedAcceptedTitle: String,
    @SerialName("EMBED_ACCEPTED_BODY")
    val embedAcceptedBody: String,

    @SerialName("EMBED_DISMISSED_TITLE")
    val embedDismissedTitle: String,
    @SerialName("EMBED_DISMISSED_BODY")
    val embedDismissedBody: String,

    @SerialName("MODAL_REASON_ACTION_TITLE")
    val modalReasonActionTitle: String,
    @SerialName("MODAL_REASON_ACTION_REASON_TITLE")
    val modalReasonActionReasonTitle: String,
    @SerialName("MODAL_REASON_ACTION_REASON_PLACEHOLDER")
    val modalReasonActionPlaceholder: String,

    @SerialName("EMBED_ACCEPTATION_SENT_TITLE")
    val embedAcceptationSentTitle: String,
    @SerialName("EMBED_ACCEPTATION_SENT_BODY")
    val embedAcceptationSentBody: String,

    @SerialName("EMBED_DISMISSING_SENT_TITLE")
    val embedDismissingSentTitle: String,
    @SerialName("EMBED_DISMISSING_SENT_BODY")
    val embedDismissingSentBody: String,

    @SerialName("EMBED_NOTICE_TITLE")
    val embedNoticeTitle: String,
    @SerialName("EMBED_NOTICE_BODY")
    val embedNoticeBody: String,

    @SerialName("EMBED_NOTICE_VIEW_TITLE")
    val embedNoticeViewTitle: String,
    @SerialName("EMBED_NOTICE_VIEW_BODY")
    val embedNoticeViewBody: String,

    @SerialName("EMBED_ENDED_TITLE")
    val embedEndedTitle: String,
    @SerialName("EMBED_ENDED_BODY")
    val embedEndedBody: String,

    @SerialName("EMBED_REVOKED_TITLE")
    val embedRevokedTitle: String,
    @SerialName("EMBED_REVOKED_BODY")
    val embedRevokedBody: String,

    @SerialName("EMBED_REVOKATION_SENT_TITLE")
    val embedRevokationSentTitle: String,
    @SerialName("EMBED_REVOKATION_SENT_BODY")
    val embedRevokationSentBody: String,
)

@Serializable
data class TranslationRegulars(
    @SerialName("EMBED_TEMPLATE_TITLE")
    val embedTemplateTitle: String,
    @SerialName("EMBED_TEMPLATE_BODY")
    val embedTemplateBody: String,

    @SerialName("EMBED_TEMPLATE_GROUP_BODY")
    val embedTemplateGroupBody: String,
    @SerialName("EMBED_TEMPLATE_ROLE_BODY")
    val embedTemplateRoleBody: String,

    @SerialName("TEXT_TEMPLATE_ROLE_TIMEFRAME")
    val textTemplateRoleTimeframe: String,
    @SerialName("TEXT_TEMPLATE_ROLE_XP")
    val textTemplateRoleXp: String,

    @SerialName("EMBED_SETTINGS_TITLE")
    val embedSettingsTitle: String,
    @SerialName("EMBED_SETTINGS_BODY")
    val embedSettingsBody: String,
    @SerialName("EMBED_SETTINGS_VIEW_TITLE")
    val embedSettingsViewTitle: String,
    @SerialName("EMBED_SETTINGS_VIEW_BODY")
    val embedSettingsViewBody: String,
    @SerialName("EMBED_SETTINGS_FIELD_GROUP_NAME")
    val embedSettingsFieldGroupName: String,
    @SerialName("EMBED_SETTINGS_FIELD_ROLE_NAME")
    val embedSettingsFieldRoleName: String,
    @SerialName("EMBED_SETTINGS_FIELD_PLAYTIME_NAME")
    val embedSettingsFieldPlaytimeName: String,
    @SerialName("EMBED_SETTINGS_FIELD_XP_NAME")
    val embedSettingsFieldXp: String,
    @SerialName("EMBED_SETTINGS_FIELD_LAST_CHECKED")
    val embedSettingsFieldLastChecked: String,

    @SerialName("EMBED_SYNC_GROUP_SELECT_TITLE")
    val embedSyncGroupSelectTitle: String,
    @SerialName("EMBED_SYNC_GROUP_SELECT_BODY")
    val embedSyncGroupSelectBody: String,

    @SerialName("EMBED_NOT_VERIFIED_TITLE")
    val embedNotVerifiedTitle: String,
    @SerialName("EMBED_NOT_VERIFIED_BODY")
    val embedNotVerifiedBody: String,

    @SerialName("EMBED_SYNC_SENT_TITLE")
    val embedSyncSentTitle: String,
    @SerialName("EMBED_SYNC_SENT_BODY")
    val embedSyncSentBody: String,

    @SerialName("EMBED_SYNC_DEACTIVATED_TITLE")
    val embedSyncDeactivatedTitle: String,
    @SerialName("EMBED_SYNC_DEACTIVATED_BODY")
    val embedSyncDeactivatedBody: String,

    @SerialName("EMBED_SYNC_REACTIVATED_TITLE")
    val embedSyncReactivatedTitle: String,
    @SerialName("EMBED_SYNC_REACTIVATED_BODY")
    val embedSyncReactivatedBody: String,

    @SerialName("EMBED_SYNC_REMOVED_CONFIRM_TITLE")
    val embedSyncRemovedConfirmTitle: String,
    @SerialName("EMBED_SYNC_REMOVED_CONFIRM_BODY")
    val embedSyncRemovedConfirmBody: String,

    @SerialName("EMBED_SYNC_REMOVED_MESSAGE_TITLE")
    val embedSyncRemovedTitle: String,
    @SerialName("EMBED_SYNC_REMOVED_MESSAGE_BODY")
    val embedSyncRemovedBody: String,
)

@Serializable
data class TranslationStatusBars(
    @SerialName("EMBED_TICKET_STATUS")
    val embedTicketStatus: String,
    @SerialName("EMBED_NOTICE_OF_DEPARTURE_ACCEPTED")
    val embedNoticeOfDepartureAccepted: String,
    @SerialName("EMBED_NOTICE_OF_DEPARTURE_DISMISSED")
    val embedNoticeOfDepartureDismissed: String,
    @SerialName("EMBED_NOTICE_OF_DEPARTURE_ENDED")
    val embedNoticeOfDepartureEnded: String,
    @SerialName("EMBED_NOTICE_OF_DEPARTURE_REVOKED")
    val embedNoticeOfDepartureRevoked: String
)

@Serializable
data class TranslationButtons(
    @SerialName("TEXT_SETTINGS_START")
    val textSettingsStart: String,
    @SerialName("TEXT_SETTINGS_CONFIGURE")
    val textSettingsConfigure: String,
    @SerialName("TEXT_SETTINGS_CURRENT")
    val textSettingsCurrent: String,
    @SerialName("TEXT_SETTINGS_INFORMATION")
    val textSettingsInformation: String,
    @SerialName("TEXT_SETTINGS_NEWS")
    val textSettingsNews: String,

    @SerialName("TEXT_RULES_PASTE")
    val textRulesPaste: String,
    @SerialName("TEXT_RULES_UPDATE")
    val textRulesUpdate: String,

    @SerialName("TEXT_PLAYER_STATS")
    val textPlayerStats: String,
    @SerialName("TEXT_PLAYER_MODERATION")
    val textPlayerModeration: String,
    @SerialName("TEXT_PLAYER_APPEAL")
    val textPlayerAppeal: String,
    @SerialName("TEXT_PLAYER_TICKET")
    val textPlayerTicket: String,
    @SerialName("TEXT_PLAYER_PANEL")
    val textPlayerPanel: String,

    @SerialName("TEXT_SUPPORT_ANONYMOUS_ACCEPT")
    val textSupportAnonymousAccept: String,
    @SerialName("TEXT_SUPPORT_ANONYMOUS_DENY")
    val textSupportAnonymousDeny: String,
    @SerialName("TEXT_SUPPORT_SETTINGS")
    val textSupportSettings: String,
    @SerialName("TEXT_SUPPORT_CLAIM")
    val ticketSupportClaim: String,
    @SerialName("TEXT_SUPPORT_CLOSE")
    val ticketSupportClose: String,

    @SerialName("TEXT_SUPPORT_SETTINGS_OPEN")
    val textSupportSettingsOpen: String,
    @SerialName("TEXT_SUPPORT_SETTINGS_PAUSE")
    val textSupportSettingsPause: String,
    @SerialName("TEXT_SUPPORT_SETTINGS_SUSPEND")
    val textSupportSettingsSuspend: String,
    @SerialName("TEXT_SUPPORT_SETTINGS_CLOSE")
    val textSupportSettingsClose: String,

    @SerialName("TEXT_SUPPORT_LOG_CLAIM")
    val textSupportLogClaim: String,
    @SerialName("TEXT_SUPPORT_LOG_OPEN")
    val textSupportLogOpen: String,
    @SerialName("TEXT_SUPPORT_LOG_PAUSE")
    val textSupportLogPause: String,
    @SerialName("TEXT_SUPPORT_LOG_SUSPEND")
    val textSupportLogSuspend: String,
    @SerialName("TEXT_SUPPORT_LOG_CLOSE")
    val textSupportLogClose: String,

    @SerialName("TEXT_APPLICATION_ACTIVATION_ADD")
    val textApplicationActivationAdd: String,
    @SerialName("TEXT_APPLICATION_ACTIVATION_REMOVE")
    val textApplicationActivationRemove: String,
    @SerialName("TEXT_APPLICATION_ACTIVATION_COMPLETE_SETUP")
    val textApplicationActivationCompleteSetup: String,
    @SerialName("TEXT_APPLICATION_DEACTIVATE")
    val textApplicationDeactivate: String,
    @SerialName("TEXT_APPLICATION_OPEN_TICKET")
    val textApplicationOpenTickets: String,

    @SerialName("TEXT_VERIFY_VERIFY")
    val textVerifyVerify: String,
    @SerialName("TEXT_VERIFY_SHOW_DATA")
    val textVerifyShowData: String,
    @SerialName("TEXT_VERIFY_DELETE")
    val textVerifyDelete: String,

    @SerialName("TEXT_NOTICE_OF_DEPARTURE_FILE")
    val textNoticeOfDepartureFile: String,
    @SerialName("TEXT_NOTICE_OF_DEPARTURE_ACCEPT")
    val textNoticeOfDepartureAccept: String,
    @SerialName("TEXT_NOTICE_OF_DEPARTURE_DISMISSED")
    val textNoticeOfDepartureDismissed: String,
    @SerialName("TEXT_NOTICE_OF_DEPARTURE_REVOKED")
    val textNoticeOfDepartureRevoked: String,
    @SerialName("TEXT_NOTICE_OF_DEPARTURE_DELETE")
    val textNoticeOfDepartureDelete: String,

    @SerialName("TEXT_REGULAR_OPEN_SETTINGS")
    val textRegularOpenSettings: String,
    @SerialName("TEXT_REGULAR_SYNC")
    val textRegularSync: String,
    @SerialName("TEXT_REGULAR_SYNC_DEACTIVATE")
    val textRegularSyncDeactivate: String,
    @SerialName("TEXT_REGULAR_SYNC_REACTIVATE")
    val textRegularSyncReactivate: String,
    @SerialName("TEXT_REGULAR_SYNC_REMOVE")
    val textRegularSyncRemove: String
)

@Serializable
data class TranslationSelectMenus(
    @SerialName("TEXT_SUPPORT_NAME_GENERAL")
    val textSupportNameGeneral: String,
    @SerialName("TEXT_SUPPORT_DESCRIPTION_GENERAL")
    val textsupportDescriptionGeneral: String,

    @SerialName("TEXT_SUPPORT_NAME_REPORT")
    val textSupportNameReport: String,
    @SerialName("TEXT_SUPPORT_DESCRIPTION_REPORT")
    val textSupportDescriptionReport: String,

    @SerialName("TEXT_SUPPORT_NAME_ERROR")
    val textSupportNameError: String,
    @SerialName("TEXT_SUPPORT_DESCRIPTION_ERROR")
    val textSupportDescriptionError: String,

    @SerialName("TEXT_SUPPORT_NAME_UNBAN")
    val textSupportNameUnban: String,
    @SerialName("TEXT_SUPPORT_DESCRIPTION_UNBAN")
    val textSupportDescriptionUnban: String,

    @SerialName("TEXT_SUPPORT_NAME_COMPLAINT")
    val textSupportNameComplaint: String,
    @SerialName("TEXT_SUPPORT_DESCRIPTION_COMPLAINT")
    val textSupportDescriptionComplaint : String,

    @SerialName("TEXT_SUPPORT_NAME_APPLICATION")
    val textSupportNameApplication: String,
    @SerialName("TEXT_SUPPORT_DESCRIPTION_APPLICATION")
    val textSupportDescriptionApplication : String,
)