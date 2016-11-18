package com.dewmobile.common.service;

/**
 * Created by dengcb on 08/04/16.
 */
public interface EaseMobMessageService {
    /**
     * 通过环信的消息系统发送透传消息.
     *
     * @param taskName      - 任务名称
     * @param target        - 消息的目的用户ID, 多个用户逗号分隔, 尽量最多不超过20个
     * @param from          - 消息发送方ID
     * @param action        - action参数
     * @param param         - 参数 JsonNode.toString()
     * @param ext           - 扩展参数 JsonNode.toString()
     * @param executeTime    - 消息最早执行时间, 0 尽量立即执行
     * @param expireTime    - 消息过期时间, 如果超过此过期时间没有发送出去, 则忽略这条消息,注意消息会重试3次
     *
     * @return  是否投递成功, 返回true仅表示成功放入消息队列
     */
    public boolean sendCmdMsg(String taskName, String target, String from, String action, String param, String ext, long executeTime, long expireTime);

    /**
     * 通过环信的消息系统发送文本消息.
     *
     * @param taskName      - 任务名称
     * @param target        - 消息的目的用户ID, 多个用户逗号分隔, 尽量最多不超过20个
     * @param from          - 消息发送方ID
     * @param msg           - 消息内容
     * @param ext           - 扩展参数 JsonNode.toString()
     * @param executeTime    - 消息最早执行时间, 0 尽量立即执行
     * @param expireTime    - 消息过期时间, 如果超过此过期时间没有发送出去, 则忽略这条消息,注意消息会重试3次
     *
     * @return  是否投递成功, 返回true仅表示成功放入消息队列
     */
    public boolean sendTxtMsg(String taskName, String target, String from, String msg, String ext, long executeTime, long expireTime);
}