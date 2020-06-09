package com.kuangji.paopao.dto.result;

import com.kuangji.paopao.model.Common;
import lombok.Data;

import java.util.List;
@Data
public class CommonResult {
    List<Common> commonList;
    Integer[] relatedIds;
}
