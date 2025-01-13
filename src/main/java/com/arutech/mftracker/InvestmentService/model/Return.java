package com.arutech.mftracker.InvestmentService.model;

import lombok.*;

@Data
@Setter
@Getter
@NoArgsConstructor
@ToString
public class Return {
    String interval ;
    String returnPercentage; ;
}
