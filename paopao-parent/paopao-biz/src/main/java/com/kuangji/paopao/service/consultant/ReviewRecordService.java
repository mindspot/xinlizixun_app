package com.kuangji.paopao.service.consultant;

import com.kuangji.paopao.model.consultant.ReviewRecord;

public interface ReviewRecordService {
    int addReview(ReviewRecord reviewRecord);

    ReviewRecord getLastReviewRecord(Integer refId, Integer refType);
}
