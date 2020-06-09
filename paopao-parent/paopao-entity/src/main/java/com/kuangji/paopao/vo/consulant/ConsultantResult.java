package com.kuangji.paopao.vo.consulant;

import com.kuangji.paopao.model.User;
import com.kuangji.paopao.model.consultant.Consultant;
import com.kuangji.paopao.model.consultant.ConsultantRate;
import lombok.Data;

@Data
public class ConsultantResult extends Consultant {
    private User user;
    private ConsultantRate firstConsultantRate;
    private ConsultantRate nextConsultantRate;
}
