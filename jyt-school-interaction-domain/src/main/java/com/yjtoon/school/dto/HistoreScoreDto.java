package com.yjtoon.school.dto;

import com.yjtoon.school.domain.Exam;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
@Data
public class HistoreScoreDto {
    private BigDecimal myScore;
    private BigDecimal max_score;
    private BigDecimal avg_score;
    private BigDecimal total_score;
    private Exam exam;

}
