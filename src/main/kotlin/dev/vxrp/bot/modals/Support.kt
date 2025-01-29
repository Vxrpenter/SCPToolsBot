package dev.vxrp.bot.modals

import dev.minn.jda.ktx.interactions.components.Modal
import dev.minn.jda.ktx.interactions.components.TextInputBuilder
import dev.vxrp.configuration.loaders.Translation
import net.dv8tion.jda.api.interactions.components.ActionRow
import net.dv8tion.jda.api.interactions.components.text.TextInputStyle
import net.dv8tion.jda.api.interactions.modals.Modal

class Support(val translation: Translation) {
    fun supportGeneralModal(): Modal {
        return Modal.create("support_general", translation.support.modalGeneralTitle).addComponents(
            ActionRow.of(
                TextInputBuilder(
                    id= "support_general_subject",
                    label = translation.support.modalGeneralSubjectTitle,
                    style = TextInputStyle.SHORT,
                    required = true,
                    placeholder = translation.support.modalGeneralSubjectPlaceholder).build()
            ),
            ActionRow.of(
                TextInputBuilder(
                    id= "support_general_explanation",
                    label = translation.support.modalComplaintExplanationTitle,
                    style = TextInputStyle.PARAGRAPH,
                    required = true,
                    placeholder = translation.support.modalComplaintExplanationPlaceholder).build()
            )
        ).build()
    }

    fun supportReportModal(userId: String): Modal {
        return Modal.create("support_report:$userId", translation.support.modalReportTitle).addComponents(
            ActionRow.of(
                TextInputBuilder(
                    id= "support_report_reason",
                    label = translation.support.modalReportReasonTitle,
                    style = TextInputStyle.SHORT,
                    required = true,
                    placeholder = translation.support.modalReportReasonPlaceholder).build()
            ),
            ActionRow.of(
                TextInputBuilder(
                    id= "support_report_proof",
                    label = translation.support.modalReportProofTitle,
                    style = TextInputStyle.PARAGRAPH,
                    required = true,
                    placeholder = translation.support.modalReportProofPlaceholder).build()
            )
        ).build()
    }

    fun supportErrorModal(): Modal {
        return Modal.create("support_error", translation.support.modalErrorTitle).addComponents(
            ActionRow.of(
                TextInputBuilder(
                    id= "support_error_problem",
                    label = translation.support.modalErrorProblemTitle,
                    style = TextInputStyle.SHORT,
                    required = true,
                    placeholder = translation.support.modalErrorProblemPlaceholder).build()
            ),
            ActionRow.of(
                TextInputBuilder(
                    id= "support_error_times",
                    label = translation.support.modalErrorTimesTitle,
                    style = TextInputStyle.SHORT,
                    required = true,
                    placeholder = translation.support.modalErrorTimesPlaceholder).build()
            ),
            ActionRow.of(
                TextInputBuilder(
                    id= "support_error_reproduce",
                    label = translation.support.modalErrorReproduceTitle,
                    style = TextInputStyle.PARAGRAPH,
                    required = true,
                    placeholder = translation.support.modalErrorReproducePlaceholder).build()
            ),
            ActionRow.of(
                TextInputBuilder(
                    id= "support_error_additional",
                    label = translation.support.modalErrorAdditionalTitle,
                    style = TextInputStyle.PARAGRAPH,
                    required = true,
                    placeholder = translation.support.modalErrorAdditionalPlaceholder).build()
            )
        ).build()
    }

    fun supportUnbanModal(): Modal {
        return Modal.create("support_unban", translation.support.modalUnbanTitle).addComponents(
            ActionRow.of(
                TextInputBuilder(
                    id= "support_unban_steamID",
                    label = translation.support.modalUnbanSteamIdTitle,
                    style = TextInputStyle.SHORT,
                    required = true,
                    placeholder = translation.support.modalUnbanSteamIdPlaceholder).build()
            ),
            ActionRow.of(
                TextInputBuilder(
                    id= "support_unban_reason",
                    label = translation.support.modalUnbanReasonTitle,
                    style = TextInputStyle.PARAGRAPH,
                    required = true,
                    placeholder = translation.support.modalUnbanReasonPlaceholder).build()
            )
        ).build()
    }

    fun supportComplaintModal(userId: String, anonymous: Boolean): Modal {
        return Modal.create("support_complaint:$userId:$anonymous", translation.support.modalComplaintTitle).addComponents(
            ActionRow.of(
                TextInputBuilder(
                    id= "support_complaint_subject",
                    label = translation.support.modalComplaintSubjectTitle,
                    style = TextInputStyle.SHORT,
                    required = true,
                    placeholder = translation.support.modalComplaintSubjectPlaceholder).build()
            ),
            ActionRow.of(
                TextInputBuilder(
                    id= "support_complaint_explanation",
                    label = translation.support.modalComplaintExplanationTitle,
                    style = TextInputStyle.PARAGRAPH,
                    required = true,
                    placeholder = translation.support.modalComplaintExplanationPlaceholder).build()
            )
        ).build()
    }

    fun supportApplicationModal(): Modal {
        return Modal.create("support_application", translation.support.modalApplicationTitle).addComponents(
            ActionRow.of(
                TextInputBuilder(
                    id= "support_application_name",
                    label = translation.support.modalApplicationNameTitle,
                    style = TextInputStyle.SHORT,
                    required = true,
                    placeholder = translation.support.modalApplicationNamePlaceholder).build()
            ),
            ActionRow.of(
                TextInputBuilder(
                    id= "support_application_age",
                    label = translation.support.modalApplicationAgeTitle,
                    style = TextInputStyle.SHORT,
                    required = true,
                    placeholder = translation.support.modalApplicationAgePlaceholder).build()
            ),
            ActionRow.of(
                TextInputBuilder(
                    id= "support_application_playtime",
                    label = translation.support.modalApplicationPlaytimeTitle,
                    style = TextInputStyle.SHORT,
                    required = true,
                    placeholder = translation.support.modalApplicationPlaytimePlaceholder).build()
            ),
            ActionRow.of(
                TextInputBuilder(
                    id= "support_application_reason_of_application",
                    label = translation.support.modalApplicationReasonsOfApplicationTitle,
                    style = TextInputStyle.PARAGRAPH,
                    required = true,
                    placeholder = translation.support.modalApplicationReasonsOfApplicationPlaceholder).build()
            ),
            ActionRow.of(
                TextInputBuilder(
                    id= "support_application_skills",
                    label = translation.support.modalApplicationSkillsTitle,
                    style = TextInputStyle.PARAGRAPH,
                    required = true,
                    placeholder = translation.support.modalApplicationSkillsPlaceholder).build()
            ),
        ).build()
    }
}