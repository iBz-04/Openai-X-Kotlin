package com.example.chatinv

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.chatinv.databinding.FragmentChatBinding

class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: ChatViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[ChatViewModel::class.java]
        
        viewModel.conversation.observe(viewLifecycleOwner) { conversationText ->
            binding.textviewConversation.text = conversationText
            // Scroll to the bottom when new messages arrive
            binding.scrollviewConversation.post {
                binding.scrollviewConversation.fullScroll(View.FOCUS_DOWN)
            }
        }
        
        binding.buttonSend.setOnClickListener {
            val userMessage = binding.edittextMessage.text.toString()
            if (userMessage.isNotBlank()) {
                viewModel.sendUserMessage(userMessage)
                binding.edittextMessage.text.clear()
            }
        }

        // Add enter key listener for sending messages
        binding.edittextMessage.setOnEditorActionListener { _, actionId, event ->
            if (actionId == android.view.inputmethod.EditorInfo.IME_ACTION_SEND ||
                (event != null && event.keyCode == android.view.KeyEvent.KEYCODE_ENTER && event.action == android.view.KeyEvent.ACTION_DOWN)
            ) {
                binding.buttonSend.performClick()
                true
            } else {
                false
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
} 