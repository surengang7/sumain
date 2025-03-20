package com.sumain.common.entity;


import com.sumain.common.utils.DateUtils;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Data
public class MessageEntity implements Serializable {


    @Serial
    private static final long serialVersionUID = -3328377721612506155L;
    /**
     * 需要发送的消息类型
     */
    private List<String> messageType;  // 邮件，短信，站内信

    /**
     * 短信发送号码
     */
    private String phone;

    /**
     * 邮件发送地址
     */
    private String email;

    private String exchangeId;

    private String companyId;

    /**
     * 站内信发送用户
     */
    private String userId;

    /**
     * 业务名称
     */
    private String businessType;

    /**
     * 发送时间
     */
    private String sendTime;

    /**
     * 操作人ID
     */
    private String creatorId;

    /**
     * 所需要的参数
     */
    Map<String, String> param;



    public static MessageEntity init(){
        MessageEntity messageEntity = new MessageEntity();
//        messageEntity.setCreatorId(ThreadLocalContext.getRequestHeader().getUserId().toString());
        messageEntity.setSendTime(DateUtils.format(LocalDateTime.now()));
        messageEntity.setParam(new HashMap<>());
        return messageEntity;
    }


}
