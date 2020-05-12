package com.kpi.testing.util;

import com.kpi.testing.dto.ReportForUserReportTableDTO;

import java.util.ArrayList;
import java.util.List;

public class Pagination<T> {

    private final int limit;
    private final List<T> list;

    public Pagination(int limit, List<T> list) {
        this.limit = limit;
        this.list = list;
    }

    public List<T> getPage(int page){
        int start = (page == 0)? 0: page * limit;
        int end = start + limit;
        if (start > list.size()){
            return new ArrayList<>();
        }
        if (end > list.size()){
            end = list.size();
        }
        return list.subList(start, end);
    }

    public int getTotalPages() {
        return (int) Math.ceil((float) list.size()/limit);
    }

}
