package com.kuangji.paopao.dto.result;

import com.kuangji.paopao.model.User;
import com.kuangji.paopao.model.consultant.PointHistory;
import lombok.Data;

@Data
public class PointHistoryResult extends PointHistory {
    private User fromUser;
    private User toUser;
}
