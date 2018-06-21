package de.sevennerds.trackdefects.core.di

import de.sevennerds.trackdefects.presentation.base.navigation.BaseKey
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentLinkedQueue
import javax.inject.Inject
import javax.inject.Singleton




/**
 * Yes, I stole that from here:
 * https://github.com/Zhuinden/simple-stack/blob/master/simple-stack-example-mvvm-fragments/src/main/java/com/zhuinden/simplestackexamplemvvm/application/injection/MessageQueue.java
 */
@Singleton
class MessageQueue @Inject
constructor() {

    internal var messages: MutableMap<BaseKey, Queue<Message>> = ConcurrentHashMap()

    interface Receiver {
        fun receiveMessage(message: Message)
    }

    interface Message

    fun pushMessageTo(recipient: BaseKey, message: Message) {
        var messageQueue: Queue<Message>? = messages[recipient]
        if (messageQueue == null) {
            messageQueue = ConcurrentLinkedQueue()
            messages[recipient] = messageQueue
        }
        messageQueue.add(message)
    }

    fun requestMessages(receiverBaseKey: BaseKey, receiver: Receiver) {
        val messageQueue = messages[receiverBaseKey]
        if (messageQueue != null) {
            val messages = messageQueue.iterator()
            while (messages.hasNext()) {
                receiver.receiveMessage(messages.next())
                messages.remove()
            }
        }
    }
}