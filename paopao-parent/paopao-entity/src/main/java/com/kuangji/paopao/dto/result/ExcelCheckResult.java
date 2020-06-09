package com.kuangji.paopao.dto.result;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExcelCheckResult {
    String errorField;
    String errorMessage;
}
