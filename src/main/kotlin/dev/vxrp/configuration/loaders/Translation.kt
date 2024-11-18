package dev.vxrp.configuration.loaders

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Translation(@SerialName("SUPPORT")
                       val support: TranslationSupport,
                       @SerialName("NOTICE_OF_DEPARTURE")
                       val noticeOfDeparture: TranslationNoticeOfDeparture,
                       @SerialName("REGULARS")
                       val regulars: TranslationRegulars,
                       @SerialName("LOGGING")
                       val logging:  TranslationLogging,
                       @SerialName("STATUS_BARS")
                       val statusBars: TranslationStatusBars,
                       @SerialName("BUTTONS")
                       val buttons: TranslationButtons)

@Serializable
data class TranslationSupport(@SerialName("EMBED_TEMPLATE_SUPPORT_TITLE")
                              val embedTemplateSupportTitle: String,
                              @SerialName("EMBED_TEMPLATE_SUPPORT_BODY")
                              val embedTemplateSupportBody: String,
                              @SerialName("EMBED_TEMPLATE_UNBAN_TITLE")
                              val embedTemplateUnbanTitle: String,
                              @SerialName("EMBED_TEMPLATE_UNBAN_BODY")
                              val embedTemplateUnbanBody: String,
                              @SerialName("MODAL_SUPPORT_TITLE")
                              val modalTitle: String,
                              @SerialName("MODAL_SUPPORT_SUBJECT_TITLE")
                              val modalSubjectTitle: String,
                              @SerialName("MODAL_SUPPORT_SUBJECT_PLACEHOLDER")
                              val modalSubjectPlaceholder: String,
                              @SerialName("MODAL_SUPPORT_EXPLANATION_TITLE")
                              val modalExplanationTitle: String,
                              @SerialName("MODAL_SUPPORT_EXPLANATION_PLACEHOLDER")
                              val modalExplanationPlaceholder: String,
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
                              @SerialName("MODAL_UNBAN_REASON_TO_UNBAN_TITLE")
                              val modalReasonToUnbanTitle: String,
                              @SerialName("MODAL_UNBAN_REASON_TO_UNBAN_PLACEHOLDER")
                              val modalReasonToUnbanPlaceholder: String,
                              @SerialName("MODAL_REASON_ACTION_TITLE")
                              val modalReasonActionTitle: String,
                              @SerialName("MODAL_REASON_ACTION_REASON_TITLE")
                              val modalReasonActionReasonTitle: String,
                              @SerialName("MODAL_REASON_ACTION_REASON_PLACEHOLDER")
                              val modalReasonActionPlaceholder: String,
                              @SerialName("EMBED_TICKET_SUPPORT_TITLE")
                              val embedTicketSupportTitle: String,
                              @SerialName("EMBED_TICKET_SUPPORT_BODY")
                              val embedTicketSupportBody: String,
                              @SerialName("EMBED_TICKET_SUPPORT_FOOTER")
                              val embedTicketSupportFooter: String,
                              @SerialName("EMBED_TICKET_SUPPORT_CREATED")
                              val embedTicketSupportCreated: String,
                              @SerialName("EMBED_UNBAN_TITLE")
                              val embedUnbanTitle: String,
                              @SerialName("EMBED_UNBAN_BODY")
                              val embedUnbanBody: String,
                              @SerialName("EMBED_UNBAN_FOOTER")
                              val embedUnbanFooter: String,
                              @SerialName("EMBED_UNBAN_CREATED")
                              val embedUnbanCreated: String,
                              @SerialName("EMBED_UNBAN_MESSAGE_ACCEPTED")
                              val embedUnbanMessageAccepted: String,
                              @SerialName("EMBED_UNBAN_MESSAGE_DISMISSED")
                              val embedUnbanMessageDismissed: String,
                              @SerialName("MESSAGE_UNBAN_SENT")
                              val messageUnbanSent: String)

@Serializable
data class TranslationNoticeOfDeparture(@SerialName("EMBED_TEMPLATE_TITLE")
                                        val embedTemplateTitle: String,
                                        @SerialName("EMBED_TEMPLATE_BODY")
                                        var embedTemplateBody: String,
                                        @SerialName("MODAL_TITLE")
                                        var modalTitle: String,
                                        @SerialName("MODAL_TIME_TITLE")
                                        var modalTimeTitle: String,
                                        @SerialName("MODAL_TIME_PLACEHOLDER")
                                        var modalTimePlaceHolder: String,
                                        @SerialName("MODAL_EXPLANATION_TITLE")
                                        var modalExplanationTitle: String,
                                        @SerialName("MODAL_EXPLANATION_PLACEHOLDER")
                                        var modalExplanationPlaceHolder: String,
                                        @SerialName("EMBED_DESCISION_TITLE")
                                        var embedDescisionTitle: String,
                                        @SerialName("EMBED_DESCISION_BODY")
                                        var embedDescisionBody: String,
                                        @SerialName("EMBED_DESCISION_FOOTER")
                                        val embedDescisionFooter: String,
                                        @SerialName("MESSAGE_DESCISION_CREATED")
                                        val messageDescisionCreated: String,
                                        @SerialName("EMBED_ACCEPTED")
                                        val embedAccepted: String,
                                        @SerialName("EMBED_DISMISSED")
                                        val embedDismissed: String,
                                        @SerialName("MESSAGE_DESCISION_SENT")
                                        val messageDescisionSent: String,
                                        @SerialName("EMBED_NOTICE_TITLE")
                                        val embedNoticeTitle: String,
                                        @SerialName("EMBED_NOTICE_BODY")
                                        val embedNoticeBody: String,
                                        @SerialName("EMBED_NOTICE_FOOTER")
                                        val embedNoticeFooter: String,
                                        @SerialName("EMBED_ENDED")
                                        val embedEnded: String,
                                        @SerialName("EMBED_REVOKED")
                                        val embedRevoked: String,
                                        @SerialName("MESSAGE_REVOKED")
                                        val messageRevoked: String,
                                        @SerialName("EMBED_ENDED_REPLACE")
                                        val embedEndedReplace: String,
                                        @SerialName("MESSAGE_DELETED_ENDED_REPLACE")
                                        val messageDeletedEndedReplace: String)

@Serializable
data class TranslationRegulars(@SerialName("EMBED_TEMPLATE_TITLE")
                               val embedTemplateTitle: String,
                               @SerialName("EMBED_TEMPLATE_BODY")
                               val embedTemplateBody: String,
                               @SerialName("EMBED_TEMPLATE_GROUP")
                               val embedTemplateGroup: String,
                               @SerialName("EMBED_TEMPLATE_ROLE")
                               val embedTemplateRole: String,
                               @SerialName("EMBED_SETTINGS_TITLE")
                               val embedSettingsTitle: String,
                               @SerialName("EMBED_SETTINGS_BODY")
                               val embedSettingsBody: String,
                               @SerialName("MODAL_DATA_TITLE")
                               val modalDataTitle: String,
                               @SerialName("MODAL_DATA_STEAMID_TITLE")
                               val modalDataSteamIdTitle: String,
                               @SerialName("MODAL_DATA_STEAMID_PLACEHOLDER")
                               val modalDataSteamIdPlaceholder: String,
                               @SerialName("MESSAGE_SYNC_GROUP_SELECT")
                               val messageSyncGroupSelect: String,
                               @SerialName("MESSAGE_SYNC_SELECT")
                               val messageSyncSelect: String,
                               @SerialName("MESSAGE_SYNC_DEACTIVATED")
                               val messageSyncDeactivated: String,
                               @SerialName("MESSAGE_SYNC_REACTIVATED")
                               val messageSyncReactivated: String,
                               @SerialName("MESSAGE_SYNC_REMOVED_CONFIRM")
                               val messageSyncRemovedConfirm: String,
                               @SerialName("MESSAGE_SYNC_REMOVED_MESSAGE")
                               val messageSyncRemovedMessage: String)

@Serializable
data class TranslationLogging(@SerialName("EMBED_TEMPLATE_SINGLE_MESSAGE_LOG")
                              val embedTemplateSingleMessageLog: String,
                              @SerialName("EMBED_TEMPLATE_CREATE_LOG")
                              val embedTemplateCreateLog: String,
                              @SerialName("EMBED_TEMPLATE_CLOSE_LOG")
                              val embedTemplateCloseLog: String,
                              @SerialName("EMBED_TEMPLATE_DATABASE_LOG")
                              val embedTemplateDatabaseLog: String,
                              @SerialName("COMPONENT_ACTION_SUPPORT_MESSAGE")
                              val componentActionSupportMessage: String,
                              @SerialName("COMPONENT_ACTION_TICKET_CREATE")
                              val componentActionTicketCreate: String,
                              @SerialName("COMPONENT_ACTION_SUPPORT_TICKET_CLOSE")
                              val componentActionSupportTicketClose: String,
                              @SerialName("COMPONENT_ACTION_NOTICE_OF_DEPARTURE_CREATE")
                              val componentActionNoticeOfDepartureCreate: String,
                              @SerialName("COMPONENT_ACTION_NOTICE_OF_DEPARTURE_DISMISS")
                              val componentActionNoticeOfDepartureDismiss: String,
                              @SerialName("COMPONENT_ACTION_NOTICE_OF_DEPARTURE_CLOSE")
                              val componentActionNoticeOfDepartureClose: String)

@Serializable
data class TranslationStatusBars(@SerialName("EMBED_TICKET_STATUS")
                                 val embedTicketStatus: String,
                                 @SerialName("EMBED_NOTICE_OF_DEPARTURE_ACCEPTED")
                                 val embedNoticeOfDepartureAccepted: String,
                                 @SerialName("EMBED_NOTICE_OF_DEPARTURE_DISMISSED")
                                 val embedNoticeOfDepartureDismissed: String,
                                 @SerialName("EMBED_NOTICE_OF_DEPARTURE_ENDED")
                                 val embedNoticeOfDepartureEnded: String,
                                 @SerialName("EMBED_NOTICE_OF_DEPARTURE_REVOKED")
                                 val embedNoticeOfDepartureRevoked: String)

@Serializable
data class TranslationButtons(@SerialName("TEXT_RULES_PASTE")
                              val textRulesPaste: String,
                              @SerialName("TEXT_RULES_UPDATE")
                              val textRulesUpdate: String,
                              @SerialName("TEXT_SUPPORT_CREATE_NEW_TICKET")
                              val textSupportCreateNewTicket: String,
                              @SerialName("TEXT_SUPPORT_CLOSE_TICKET")
                              val textSupportCloseTicket: String,
                              @SerialName("TEXT_SUPPORT_CLAIM_TICKET")
                              val textSupportClaimTicket: String,
                              @SerialName("TEXT_SUPPORT_SETTINGS_TICKET")
                              val textSupportSettingsTicket: String,
                              @SerialName("TEXT_UNBAN_CREATE_NEW_TICKET")
                              val textUnbanCreateNewTicket: String,
                              @SerialName("TEXT_UNBAN_ACCEPT_TICKET")
                              val textUnbanAcceptTicket: String,
                              @SerialName("TEXT_UNBAN_DISMISS_TICKET")
                              val textUnbanDismissTicket: String,
                              @SerialName("TEXT_UNBAN_SETTINGS_TICKET")
                              val textUnbanSettingsTicket: String,
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
                              val textRegularSyncRemove: String)
