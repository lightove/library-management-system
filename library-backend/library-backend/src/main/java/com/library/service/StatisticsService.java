package com.library.service;

import com.library.controller.StatisticsController;
import com.library.mapper.StatisticsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StatisticsService {

    @Autowired
    private StatisticsMapper statisticsMapper;

    public StatisticsController.CoreStatVO getCoreStatistics() {
        Integer totalBorrows = statisticsMapper.countTotalBorrows();
        Integer unReturnedCount = statisticsMapper.countUnReturnedBorrows();
        Integer returnedCount = totalBorrows - unReturnedCount;
        Integer totalBooks = statisticsMapper.countTotalBooks();

        StatisticsController.CoreStatVO coreStat = new StatisticsController.CoreStatVO();
        coreStat.setTotalBorrows(totalBorrows);
        coreStat.setUnReturnedCount(unReturnedCount);
        coreStat.setReturnedCount(returnedCount);
        coreStat.setTotalBooks(totalBooks);
        return coreStat;
    }

    public List<StatisticsController.HotBookVO> getHotBooks() {
        List<StatisticsController.HotBookVO> hotBookList = statisticsMapper.selectHotBooks();
        for (int i = 0; i < hotBookList.size(); i++) {
            hotBookList.get(i).setRank(i + 1);
        }
        return hotBookList;
    }

    public List<StatisticsController.MonthTrendVO> getMonthTrend() {
        return statisticsMapper.selectMonthTrend();
    }
}