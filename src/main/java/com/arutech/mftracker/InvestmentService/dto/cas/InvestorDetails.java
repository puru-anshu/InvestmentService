package com.arutech.mftracker.InvestmentService.dto.cas;

import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class InvestorDetails {
    private String emailId;
    private String name;
    private String address;
    private String state;
    private String country;
    private String phoneRes;
    private String mobile;
}
