package com.gallery.vo;

import lombok.Data;

@Data
public class DashboardVO {

    private Long totalUsers;
    private Long totalExhibitions;
    private Long totalReservations;
    private Long todayReservations;
    private Long totalComments;
    private Long totalVisits;
}
