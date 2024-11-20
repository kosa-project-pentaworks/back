package com.reservation.hospitalReviews.service.response;

import com.reservation.hospitalReviews.repository.custom.dto.FindHospitalReviewDto;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.PageImpl;

import java.util.List;
@Getter
public class FindHospitalReviewPageResponse {
    private final int number; // 현재 페이지
    private final int totalPage; // 전체 페이지
    private final Boolean previous;
    private final Boolean next;
    private final List<FindHospitalReviewDto> reviewList;



    public FindHospitalReviewPageResponse(PageImpl<FindHospitalReviewDto> reveiewPages) {
        this.number = nowPage(reveiewPages.getNumber());
        this.totalPage = reveiewPages.getTotalPages();
        this.previous = reveiewPages.hasPrevious();
        this.next = reveiewPages.hasNext();
        this.reviewList = reveiewPages.getContent();
    }
    public int nowPage(int number){
        return number + 1;
    }
}
