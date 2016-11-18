package com.dewmobile.common.service;

/**
 * Created by acmac on 2016/06/16.
 */
public class EaseMobMessageServiceImpl implements EaseMobMessageService {
    @Override
    public boolean sendCmdMsg(String taskName, String target, String from, String action, String param, String ext, long executeTime, long expireTime) {

        System.out.println("EaseMobMessageServiceImpl sendCmdMsg");

        return true;
    }

    @Override
    public boolean sendTxtMsg(String taskName, String target, String from, String msg, String ext, long executeTime, long expireTime) {
        System.out.println("EaseMobMessageServiceImpl sendTxtMsg");

        return true;
    }
}
