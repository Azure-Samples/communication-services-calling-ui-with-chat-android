// Copyright (c) Microsoft Corporation. All rights reserved.
// Licensed under the MIT License.

package com.azure.samples.communication.callingwithchat

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.allViews
import androidx.core.view.isVisible
import com.azure.android.communication.common.CommunicationTokenCredential
import com.azure.android.communication.common.CommunicationTokenRefreshOptions
import com.azure.android.communication.common.CommunicationUserIdentifier
import com.azure.android.communication.ui.calling.CallComposite
import com.azure.android.communication.ui.calling.CallCompositeBuilder
import com.azure.android.communication.ui.calling.models.CallCompositeCallScreenHeaderViewData
import com.azure.android.communication.ui.calling.models.CallCompositeCallScreenOptions
import com.azure.android.communication.ui.calling.models.CallCompositeCallStateCode
import com.azure.android.communication.ui.calling.models.CallCompositeCustomButtonViewData
import com.azure.android.communication.ui.calling.models.CallCompositeLocalOptions
import com.azure.android.communication.ui.calling.models.CallCompositeMultitaskingOptions
import com.azure.android.communication.ui.calling.models.CallCompositeTeamsMeetingLinkLocator
import com.azure.android.communication.ui.chat.ChatAdapter
import com.azure.android.communication.ui.chat.ChatAdapterBuilder
import com.azure.android.communication.ui.chat.presentation.ChatThreadView
import java.util.UUID

class TeamsMeetingActivity : AppCompatActivity() {
    companion object {
        private var callComposite: CallComposite? = null
        private var chatAdapter: ChatAdapter? = null
    }

    private val teamsMeetingLink = ""
    private val displayName = "USER_NAME"
    private val endpoint = ""
    private val communicationUserId = ""
    private val threadId = ""
    private val userToken = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teams_meeting)
        findViewById<Button>(R.id.startCallButton).setOnClickListener {
            it.isVisible = false
            findViewById<Button>(R.id.endCallButton).isVisible = true
            startCallComposite()
        }
        findViewById<Button>(R.id.endCallButton).setOnClickListener { callComposite?.dismiss() }
        findViewById<Button>(R.id.showHideChatButton).setOnClickListener { showHideChat() }
    }

    private fun startCallComposite() {
        val communicationTokenRefreshOptions = CommunicationTokenRefreshOptions({ userToken }, true)
        val communicationTokenCredential = CommunicationTokenCredential(communicationTokenRefreshOptions)
        val locator = CallCompositeTeamsMeetingLinkLocator(teamsMeetingLink)

        val localOptions = CallCompositeLocalOptions()
            .setCallScreenOptions(
                CallCompositeCallScreenOptions().setHeaderViewData(
                    CallCompositeCallScreenHeaderViewData().setCustomButtons(
                        listOf(
                            CallCompositeCustomButtonViewData(
                                UUID.randomUUID().toString(),
                                R.drawable.ic_fluent_chat_24_regular,
                                "Open Chat",
                            ) {
                                callComposite?.sendToBackground()
                                showChatUI()
                            }
                        )
                    )
                ))
        val callComposite = CallCompositeBuilder()
            .applicationContext(this.applicationContext)
            .credential(communicationTokenCredential)
            .displayName(displayName)
            .multitasking(CallCompositeMultitaskingOptions(true, true))
            .build()

        callComposite.addOnCallStateChangedEventHandler { callState ->
            if (callState.code == CallCompositeCallStateCode.CONNECTED) {
                connectChat()
            }
        }

        callComposite.addOnDismissedEventHandler {
            findViewById<Button>(R.id.startCallButton).isVisible = true
            findViewById<Button>(R.id.endCallButton).isVisible = false
        }

        callComposite.launch(this, locator, localOptions)
        TeamsMeetingActivity.callComposite = callComposite
    }

    private fun connectChat() {
        if (chatAdapter != null)
            return

        val communicationTokenRefreshOptions =
            CommunicationTokenRefreshOptions( { userToken }, true)
        val communicationTokenCredential =
            CommunicationTokenCredential(communicationTokenRefreshOptions)

        val chatAdapter = ChatAdapterBuilder()
            .endpoint(endpoint)
            .credential(communicationTokenCredential)
            .identity(CommunicationUserIdentifier(communicationUserId))
            .displayName(displayName)
            .threadId(threadId)
            .build()
        chatAdapter.connect(applicationContext)
        TeamsMeetingActivity.chatAdapter = chatAdapter

        findViewById<Button>(R.id.showHideChatButton).isVisible = true
    }

    private fun showChatUI() {
        chatAdapter?.let {
            // Create Chat Composite View
            val chatView = ChatThreadView(this, chatAdapter)
            val chatContainer = findViewById<ConstraintLayout>(R.id.chatContainer)
            chatContainer.removeAllViews()
            chatContainer.addView(
                chatView,
                ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            )
        }
    }

    private fun showHideChat() {
        val chatContainer = findViewById<ConstraintLayout>(R.id.chatContainer)
        chatContainer.allViews.any { it is ChatThreadView }.let { containsChatView ->
            if (containsChatView)
                chatContainer.removeAllViews()
            else
                showChatUI()
        }
    }
}