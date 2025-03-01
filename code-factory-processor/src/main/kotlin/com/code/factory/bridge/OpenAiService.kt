package com.code.factory.bridge

import com.aallam.openai.api.chat.ChatChoice
import com.aallam.openai.api.chat.ChatCompletion
import com.aallam.openai.api.chat.ChatCompletionRequest
import com.aallam.openai.api.chat.ChatMessage
import com.aallam.openai.api.chat.ChatRole
import com.aallam.openai.api.core.Role
import com.aallam.openai.api.message.TextContent
import com.aallam.openai.api.model.ModelId
import com.aallam.openai.client.OpenAI
import com.google.devtools.ksp.processing.KSPLogger
import io.mockk.coEvery
import io.mockk.mockk

private const val SYSTEM = "You are Kotlin developer."
private const val USER_ASK_FIRST = "You had to implement interface:"
private const val USER_ASK_SECOND = "Return only code implementation please."
private const val MODEL = "gpt-4"

interface OpenAiService {
    suspend fun getCode(
        context: String,
        interfaceForCode: String,
    ): String
}

internal class OpenAiServiceImpl(
    private val openAi: OpenAI,
    private val logger: KSPLogger,
) : OpenAiService {
    override suspend fun getCode(
        context: String,
        interfaceForCode: String,
    ): String {
        val logString = StringBuilder()
        val chatCompletionRequest =
            ChatCompletionRequest(
                model = ModelId(MODEL),
                messages =
                    listOf(
                        ChatMessage(
                            role = ChatRole.System,
                            content = SYSTEM,
                        ),
                        ChatMessage(
                            role = ChatRole.User,
                            content = context,
                        ),
                        ChatMessage(
                            role = ChatRole.User,
                            content =
                                "$USER_ASK_FIRST $interfaceForCode $USER_ASK_SECOND"
                                    .also {
                                        logString.append("Request: ---->")
                                        logString.append("\n")
                                        logString.append(it)
                                        logString.append("\n")
                                        logString.append(context)
                                        logString.append("\n")
                                    },
                        ),
                    ),
            )
        val code = openAi.chatCompletion(chatCompletionRequest).choices.first().message.content ?: ""
        logString.append("Response: <----")
        logString.append("\n")
        logString.append(code)
        return code.also {
            logger.warn(logString.toString())
        }
    }
}

fun openAiService(
    apiKey: String,
    logger: KSPLogger,
): OpenAiService =
    OpenAiServiceImpl(
        openAi = OpenAI(apiKey),
        logger = logger,
    )

fun openAiServiceMock(logger: KSPLogger): OpenAiService {
    val openAiMock = mockk<OpenAI>()
    coEvery {
        openAiMock.chatCompletion(any())
    } returns
        ChatCompletion(
            id = "id",
            created = 0,
            model = ModelId("1"),
            choices =
                listOf(
                    ChatChoice(
                        index = 0,
                        message =
                            ChatMessage(
                                role = Role.User,
                                messageContent =
                                    com.aallam.openai.api.chat.TextContent(
                                        content =
                                            """
                                            Sure, the implementation of the interface `ForGenerate` can be done as follows:

                                            ```kotlin
                                                class GeneratedCode(): ForGenerate {
                                                   override fun plus(first: Int, second: Int): Int {
                                                   return first + second
                                                }
                                            }
                                            ```
                                            """.trimIndent(),
                                    ),
                            ),
                    ),
                ),
        )
    return OpenAiServiceImpl(
        openAi = openAiMock,
        logger = logger,
    )
}
