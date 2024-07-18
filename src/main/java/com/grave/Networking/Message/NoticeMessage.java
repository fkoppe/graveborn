package com.grave.Networking.Message;

import com.jme3.network.serializing.Serializable;
import com.grave.Object.Notice;
import com.jme3.network.AbstractMessage;

@Serializable
public class NoticeMessage extends AbstractMessage {
    Notice notice;

    public NoticeMessage() {

    }
    
    public NoticeMessage(Notice notice_) {
        notice = notice_;
    }

    public Notice getNotice() {
        return notice;
    }
}
