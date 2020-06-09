package com.kuangji.paopao.dto.result;

import com.kuangji.paopao.model.Common;
import com.kuangji.paopao.model.UserLabel;
import lombok.Data;

@Data
public class UserLabelInfo extends UserLabel {
    private Common common;
}
