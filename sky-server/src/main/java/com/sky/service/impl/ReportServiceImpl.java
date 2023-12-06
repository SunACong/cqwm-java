package com.sky.service.impl;


import com.sky.entity.Orders;
import com.sky.mapper.OrderDetailMapper;
import com.sky.mapper.OrderMapper;
import com.sky.mapper.UserMapper;
import com.sky.service.ReportService;
import com.sky.vo.OrderReportVO;
import com.sky.vo.SalesTop10ReportVO;
import com.sky.vo.TurnoverReportVO;
import com.sky.vo.UserReportVO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportServiceImpl implements ReportService {

    @Autowired
    private OrderMapper orderMapper;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private OrderDetailMapper orderDetailMapper;

    /**
     * 计算指定日期范围内的交易额统计报告
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return TurnoverReportVO 交易额统计报告
     */
    @Override
    public TurnoverReportVO turnoverStatistics(LocalDate begin, LocalDate end) {
        TurnoverReportVO turnoverReportVO = new TurnoverReportVO();


        List<LocalDate> localDateTimes = getDateTimeString(begin, end);
        String dateList = StringUtils.join(localDateTimes, ",");
        turnoverReportVO.setDateList(dateList);

        List<Double> turnoverLists = new ArrayList<>();
        for (LocalDate localDateTime : localDateTimes) {
            LocalDateTime beginTime = LocalDateTime.of(localDateTime, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDateTime, LocalTime.MAX);
            HashMap map = new HashMap();
            map.put("beginTime", beginTime);
            map.put("endTime", endTime);
            map.put("status", Orders.COMPLETED);
            Double sum = orderMapper.turnoverStatisticsByMap(map);
            sum = sum == null ? 0.0 : sum;
            turnoverLists.add(sum);
        }
        String turnoverListString = StringUtils.join(turnoverLists, ",");
        turnoverReportVO.setTurnoverList(turnoverListString);

        return turnoverReportVO;
    }

    /**
     * 获取用户统计数据
     *
     * @param begin 开始日期
     * @param end   结束日期
     * @return UserReportVO 用户统计数据
     */
    @Override
    public UserReportVO userStatistics(LocalDate begin, LocalDate end) {
        UserReportVO userReportVO = new UserReportVO();

        List<LocalDate> localDateTimes = getDateTimeString(begin, end);
        String dateList = StringUtils.join(localDateTimes, ",");
        userReportVO.setDateList(dateList);

        List<Integer> totalUserList = new ArrayList<>();

        List<Integer> newUserList = new ArrayList<>();

        for (LocalDate localDateTime : localDateTimes) {

            LocalDateTime beginTime = LocalDateTime.of(localDateTime, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDateTime, LocalTime.MAX);

            HashMap map = new HashMap();

            map.put("endTime", endTime);
            Integer userSum = userMapper.userStatisticsByMap(map);
            userSum = userSum == null ? 0 : userSum;
            totalUserList.add(userSum);

            map.put("beginTime", beginTime);
            Integer newUserSum = userMapper.userStatisticsByMap(map);
            newUserSum = newUserSum == null ? 0 : newUserSum;
            newUserList.add(newUserSum);

        }
        String totalUserListString = StringUtils.join(totalUserList, ",");
        userReportVO.setTotalUserList(totalUserListString);

        String newUserListString = StringUtils.join(newUserList, ",");
        userReportVO.setNewUserList(newUserListString);

        return userReportVO;
    }


    @Override
    public OrderReportVO ordersStatistics(LocalDate begin, LocalDate end) {
        OrderReportVO orderReportVO = new OrderReportVO();

        List<LocalDate> localDateTimes = getDateTimeString(begin, end);
        String dateList = StringUtils.join(localDateTimes, ",");
        orderReportVO.setDateList(dateList);


        LocalDateTime beginTime1 = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime1 = LocalDateTime.of(end.plusDays(1), LocalTime.MAX);

        Map map1 = new HashMap();
        map1.put("beginTime", beginTime1);
        map1.put("endTime", endTime1);
        Integer totalOrderCount = orderMapper.countOrder(map1);

        map1.put("status", Orders.COMPLETED);
        Integer validOrderCount = orderMapper.countOrder(map1);

        Double orderCompletionRate = validOrderCount == 0 ? 0 : (double) validOrderCount / totalOrderCount;

        List<Integer> orderCountList = new ArrayList<>();
        List<Integer> validOrderCountList = new ArrayList<>();

        for (LocalDate localDateTime : localDateTimes) {
            LocalDateTime beginTime = LocalDateTime.of(localDateTime, LocalTime.MIN);
            LocalDateTime endTime = LocalDateTime.of(localDateTime, LocalTime.MAX);

            HashMap map = new HashMap();
            map.put("beginTime", beginTime);
            map.put("endTime", endTime);
            Integer countAllTotal = orderMapper.countOrder(map);
            countAllTotal = countAllTotal == null ? 0 : countAllTotal;
            orderCountList.add(countAllTotal);


            map.put("status", Orders.COMPLETED);
            Integer countValidAllTotal = orderMapper.countOrder(map);
            countValidAllTotal = countValidAllTotal == null ? 0 : countValidAllTotal;
            validOrderCountList.add(countAllTotal);

        }
        String orderCountListString = StringUtils.join(orderCountList, ",");
        String validOrderCountListString = StringUtils.join(validOrderCountList, ",");

        orderReportVO.setOrderCountList(orderCountListString);
        orderReportVO.setValidOrderCountList(validOrderCountListString);
        orderReportVO.setTotalOrderCount(totalOrderCount);
        orderReportVO.setValidOrderCount(validOrderCount);
        orderReportVO.setOrderCompletionRate(orderCompletionRate);

        return orderReportVO;
    }

    @Override
    public SalesTop10ReportVO top10(LocalDate begin, LocalDate end) {

        SalesTop10ReportVO salesTop10ReportVO = new SalesTop10ReportVO();


        LocalDateTime beginTime = LocalDateTime.of(begin, LocalTime.MIN);
        LocalDateTime endTime = LocalDateTime.of(end.plusDays(1), LocalTime.MAX);
        Map map = new HashMap();
        map.put("beginTime", beginTime);
        map.put("endTime", endTime);
        List<Map<String, Object>> salesTop10List = orderDetailMapper.getSalesTop10(map);

        List<String> nameList = new ArrayList<>();
        List<Integer> numberList = new ArrayList<>();

        for (Map<String, Object> objectMap : salesTop10List) {
            nameList.add(objectMap.get("name").toString());
            numberList.add(Integer.parseInt(objectMap.get("number").toString()));
        }
        String nameListString = StringUtils.join(nameList, ",");
        String numberListString = StringUtils.join(numberList, ",");

        salesTop10ReportVO.setNameList(nameListString);
        salesTop10ReportVO.setNumberList(numberListString);
        return salesTop10ReportVO;
    }

    /**
     * 生成一个日期范围内的日期列表。
     *
     * @param begin 起始日期，不能为空。
     * @param end   结束日期，不能为空。
     * @return 日期列表，包含起始日期和结束日期。
     */
    private List<LocalDate> getDateTimeString(LocalDate begin, LocalDate end) {
        List<LocalDate> localDateTimes = new ArrayList<>();
        while (begin.isBefore(end)) {
            begin = begin.plusDays(1);
            localDateTimes.add(begin);
        }
        localDateTimes.add(begin.plusDays(1));
        return localDateTimes;
    }
}
