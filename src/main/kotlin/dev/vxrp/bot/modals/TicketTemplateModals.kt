package dev.vxrp.bot.modals

import dev.minn.jda.ktx.interactions.components.TextInputBuilder
import dev.vxrp.configuration.data.Translation
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle
import net.dv8tion.jda.api.interactions.modals.Modal

class TicketTemplateModals(val translation: Translation) {
    fun supportGeneralModal(): Modal {
        return Modal.create("ticket_general", translation.support.modalGeneralTitle).addComponents(
            ActionRow.of(
                TextInputBuilder(
                    id= "general_subject",
                    label = translation.support.modalGeneralSubjectTitle,
                    style = TextInputStyle.SHORT,
                    required = true,
                    placeholder = translation.support.modalGeneralSubjectPlaceholder).build()
            ),
            ActionRow.of(
                TextInputBuilder(
                    id= "general_explanation",
                    label = translation.support.modalComplaintExplanationTitle,
                    style = TextInputStyle.PARAGRAPH,
                    required = true,
                    placeholder = translation.support.modalComplaintExplanationPlaceholder).build()
            )
        ).build()
    }

    fun supportReportModal(userId: String): Modal {
        return Modal.create("ticket_report:$userId", translation.support.modalReportTitle).addComponents(
            ActionRow.of(
                TextInputBuilder(
                    id= "report_reason",
                    label = translation.support.modalReportReasonTitle,
                    style = TextInputStyle.SHORT,
                    required = true,
                    placeholder = translation.support.modalReportReasonPlaceholder).build()
            ),
            ActionRow.of(
                TextInputBuilder(
                    id= "report_proof",
                    label = translation.support.modalReportProofTitle,
                    style = TextInputStyle.PARAGRAPH,
                    required = true,
                    placeholder = translation.support.modalReportProofPlaceholder).build()
            )
        ).build()
    }

    fun supportErrorModal(): Modal {
        return Modal.create("ticket_error", translation.support.modalErrorTitle).addComponents(
            ActionRow.of(
                TextInputBuilder(
                    id= "error_problem",
                    label = translation.support.modalErrorProblemTitle,
                    style = TextInputStyle.SHORT,
                    required = true,
                    placeholder = translation.support.modalErrorProblemPlaceholder).build()
            ),
            ActionRow.of(
                TextInputBuilder(
                    id= "error_times",
                    label = translation.support.modalErrorTimesTitle,
                    style = TextInputStyle.SHORT,
                    required = true,
                    placeholder = translation.support.modalErrorTimesPlaceholder).build()
            ),
            ActionRow.of(
                TextInputBuilder(
                    id= "error_reproduce",
                    label = translation.support.modalErrorReproduceTitle,
                    style = TextInputStyle.PARAGRAPH,
                    required = true,
                    placeholder = translation.support.modalErrorReproducePlaceholder).build()
            ),
            ActionRow.of(
                TextInputBuilder(
                    id= "error_additional",
                    label = translation.support.modalErrorAdditionalTitle,
                    style = TextInputStyle.PARAGRAPH,
                    required = true,
                    placeholder = translation.support.modalErrorAdditionalPlaceholder).build()
            )
        ).build()
    }

    fun supportUnbanModal(): Modal {
        return Modal.create("ticket_unban", translation.support.modalUnbanTitle).addComponents(
            ActionRow.of(
                TextInputBuilder(
                    id= "unban_steamID",
                    label = translation.support.modalUnbanSteamIdTitle,
                    style = TextInputStyle.SHORT,
                    required = true,
                    placeholder = translation.support.modalUnbanSteamIdPlaceholder).build()
            ),
            ActionRow.of(
                TextInputBuilder(
                    id= "unban_reason",
                    label = translation.support.modalUnbanReasonTitle,
                    style = TextInputStyle.PARAGRAPH,
                    required = true,
                    placeholder = translation.support.modalUnbanReasonPlaceholder).build()
            )
        ).build()
    }

    fun supportComplaintModal(userId: String, anonymous: Boolean): Modal {
        return Modal.create("ticket_complaint:$userId:$anonymous", translation.support.modalComplaintTitle).addComponents(
            ActionRow.of(
                TextInputBuilder(
                    id= "complaint_subject",
                    label = translation.support.modalComplaintSubjectTitle,
                    style = TextInputStyle.SHORT,
                    required = true,
                    placeholder = translation.support.modalComplaintSubjectPlaceholder).build()
            ),
            ActionRow.of(
                TextInputBuilder(
                    id= "complaint_explanation",
                    label = translation.support.modalComplaintExplanationTitle,
                    style = TextInputStyle.PARAGRAPH,
                    required = true,
                    placeholder = translation.support.modalComplaintExplanationPlaceholder).build()
            )
        ).build()
    }

    fun supportApplicationModal(roleId: String): Modal {
        return Modal.create("ticket_application:$roleId", translation.support.modalApplicationTitle).addComponents(
            ActionRow.of(
                TextInputBuilder(
                    id= "application_name",
                    label = translation.support.modalApplicationNameTitle,
                    style = TextInputStyle.SHORT,
                    required = true,
                    placeholder = translation.support.modalApplicationNamePlaceholder).build()
            ),
            ActionRow.of(
                TextInputBuilder(
                    id= "application_age",
                    label = translation.support.modalApplicationAgeTitle,
                    style = TextInputStyle.SHORT,
                    required = true,
                    placeholder = translation.support.modalApplicationAgePlaceholder).build()
            ),
            ActionRow.of(
                TextInputBuilder(
                    id= "application_playtime",
                    label = translation.support.modalApplicationPlaytimeTitle,
                    style = TextInputStyle.SHORT,
                    required = true,
                    placeholder = translation.support.modalApplicationPlaytimePlaceholder).build()
            ),
            ActionRow.of(
                TextInputBuilder(
                    id= "application_reason_of_application",
                    label = translation.support.modalApplicationReasonsOfApplicationTitle,
                    style = TextInputStyle.PARAGRAPH,
                    required = true,
                    placeholder = translation.support.modalApplicationReasonsOfApplicationPlaceholder).build()
            ),
            ActionRow.of(
                TextInputBuilder(
                    id= "application_skills",
                    label = translation.support.modalApplicationSkillsTitle,
                    style = TextInputStyle.PARAGRAPH,
                    required = true,
                    placeholder = translation.support.modalApplicationSkillsPlaceholder).build()
            ),
        ).build()
    }
}