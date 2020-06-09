package com.kuangji.paopao.service;

public interface AccountService {
    Boolean doSubCommissionRecord(String orderNo);

    Boolean doRefund(String orderNo);
}
