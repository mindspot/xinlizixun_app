package com.kuangji.paopao.controller;

import com.kuangji.paopao.model.consultant.ReviewRecord;
import com.kuangji.paopao.service.consultant.ReviewRecordService;
import com.kuangji.paopao.util.ServiceResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/review")
public class ReviewRecordController {
    @Autowired
    private ReviewRecordService reviewRecordService;

    @PostMapping("/add")
    public Object add(@RequestBody ReviewRecord reviewRecord) {
        int result = reviewRecordService.addReview(reviewRecord);
        return ServiceResultUtils.success(result > 0);
    }

    @GetMapping("/last-review-record")
    public Object getLastReviewRecord(@RequestParam Integer refId, @RequestParam(defaultValue = "1") Integer refType) {
        ReviewRecord record = reviewRecordService.getLastReviewRecord(refId, refType);
        return ServiceResultUtils.success(record);
    }
}
