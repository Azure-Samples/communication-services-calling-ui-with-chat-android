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
import com.azure.android.communication.ui.calling.models.CallCompositeCustomButtonViewData
import com.azure.android.communication.ui.calling.models.CallCompositeGroupCallLocator
import com.azure.android.communication.ui.calling.models.CallCompositeJoinLocator
import com.azure.android.communication.ui.calling.models.CallCompositeLocalOptions
import com.azure.android.communication.ui.calling.models.CallCompositeMultitaskingOptions
import com.azure.android.communication.ui.chat.ChatAdapter
import com.azure.android.communication.ui.chat.ChatAdapterBuilder
import com.azure.android.communication.ui.chat.presentation.ChatThreadView
import java.util.UUID

class GroupCallWithChatActivity : AppCompatActivity() {
    companion object {
        private var callComposite: CallComposite? = null
        private var chatAdapter: ChatAdapter? = null
    }

    private val groupId = UUID.fromString("")
    private val displayName = "USER_NAME"
    private val endpoint = ""
    private val communicationUserId = ""
    private val threadId = ""
    private val userToken = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_group_call_with_chat)
        findViewById<Button>(R.id.startCallButton).setOnClickListener {
            it.isVisible = false
            findViewById<Button>(R.id.endCallButton).isVisible = true
            startCallComposite()
        }
        findViewById<Button>(R.id.endCallButton).setOnClickListener { callComposite?.dismiss() }
        findViewById<Button>(R.id.connectChatButton).setOnClickListener { connectChat() }
        findViewById<Button>(R.id.showHideChatButton).setOnClickListener { showHideChat() }
    }

    private fun startCallComposite() {
        val communicationTokenRefreshOptions = CommunicationTokenRefreshOptions({ userToken }, true)
        val communicationTokenCredential =
            CommunicationTokenCredential(communicationTokenRefreshOptions)
        val locator: CallCompositeJoinLocator =
            CallCompositeGroupCallLocator(groupId)

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
                            connectChat()
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

        callComposite.addOnDismissedEventHandler {
            findViewById<Button>(R.id.startCallButton).isVisible = true
            findViewById<Button>(R.id.endCallButton).isVisible = false
        }

        GroupCallWithChatActivity.callComposite = callComposite

        callComposite.launch(this, locator, localOptions)
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
        GroupCallWithChatActivity.chatAdapter = chatAdapter

        findViewById<Button>(R.id.showHideChatButton).isVisible = true
        findViewById<Button>(R.id.connectChatButton).isVisible = false
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