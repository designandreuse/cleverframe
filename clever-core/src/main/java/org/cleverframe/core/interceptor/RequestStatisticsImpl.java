package org.cleverframe.core.interceptor;

import org.cleverframe.common.attributes.CommonApplicationAttributes;
import org.cleverframe.common.attributes.CommonRequestAttributes;
import org.cleverframe.common.interceptor.IRequestStatistics;
import org.cleverframe.common.mapper.BeanMapper;
import org.cleverframe.common.time.DateTimeUtils;
import org.cleverframe.common.utils.ConversionUtils;
import org.cleverframe.common.vo.request.RequestInfo;
import org.cleverframe.core.CoreBeanNames;
import org.cleverframe.core.entity.AccessLog;
import org.cleverframe.core.service.AccessLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.concurrent.atomic.AtomicLong;

/**
 * 服务所有的请求统计默认实现类，不依赖任何中间件<br/>
 * 使用ApplicationAttributes存储访问统计数据，使用数据库存储请求信息<br/>
 * <p/>
 * 作者：LiZW <br/>
 * 创建时间：2016-5-19 17:09 <br/>
 */
public class RequestStatisticsImpl implements IRequestStatistics {

    @Autowired
    @Qualifier(CoreBeanNames.AccessLogService)
    private AccessLogService accessLogService;

    /**
     * 服务器本次启动后处理的请求总数,类型:long
     */
    private static AtomicLong REQUEST_COUNT_BY_START = new AtomicLong(0L);

    /**
     * 服务器当天处理请求总数(00:00:00--23:59:59),类型:long
     */
    private static AtomicLong REQUEST_COUNT_BY_DAY = new AtomicLong(0L);

    /**
     * 统计服务器当前小时处理请求总数(n:00:00-n:59:59),类型:long
     */
    private static AtomicLong REQUEST_COUNT_BY_HOUR = new AtomicLong(0L);

    /**
     * 最后一次请求的时间,类型:long
     */
    private static AtomicLong LAST_REQUEST_TIME = new AtomicLong(System.currentTimeMillis());

    /**
     * 服务器本次启动后处理的请求总数 加1
     *
     * @return 操作成功返回 true
     */
    @Override
    public boolean addRequestCountByStart(HttpServletRequest request, HttpServletResponse response) {
        request.getServletContext().setAttribute(CommonApplicationAttributes.REQUEST_COUNT_BY_START, REQUEST_COUNT_BY_START.incrementAndGet());
        return true;
    }

    /**
     * 服务器当天处理请求总数 加1
     *
     * @return 操作成功返回 true
     */
    @Override
    public boolean addRequestCountByDay(HttpServletRequest request, HttpServletResponse response) {
        Date endDate = DateTimeUtils.getDayEndTime(new Date(LAST_REQUEST_TIME.get()));
        if (endDate.compareTo(new Date()) < 0) {
            REQUEST_COUNT_BY_DAY.set(1L);
        } else {
            REQUEST_COUNT_BY_DAY.incrementAndGet();
        }
        request.getServletContext().setAttribute(CommonApplicationAttributes.REQUEST_COUNT_BY_DAY, REQUEST_COUNT_BY_DAY.get());
        return true;
    }

    /**
     * 统计服务器当前小时处理请求总数 加1
     *
     * @return 操作成功返回 true
     */
    @Override
    public boolean addRequestCountByHour(HttpServletRequest request, HttpServletResponse response) {
        Date endDate = DateTimeUtils.getHourEndTime(new Date(LAST_REQUEST_TIME.get()));
        if (endDate.compareTo(new Date()) < 0) {
            REQUEST_COUNT_BY_HOUR.set(1L);
        } else {
            REQUEST_COUNT_BY_HOUR.incrementAndGet();
        }
        request.getServletContext().setAttribute(CommonApplicationAttributes.REQUEST_COUNT_BY_HOUR, REQUEST_COUNT_BY_HOUR.get());
        return true;
    }

    /**
     * @return 服务器本次启动后处理的请求总数
     */
    @Override
    public long getRequestCountByStart(HttpServletRequest request, HttpServletResponse response) {
        return REQUEST_COUNT_BY_START.get();
    }

    /**
     * @return 服务器当天处理请求总数
     */
    @Override
    public long getRequestCountByDay(HttpServletRequest request, HttpServletResponse response) {
        return REQUEST_COUNT_BY_DAY.get();
    }

    /**
     * @return 统计服务器当前小时处理请求总数
     */
    @Override
    public long getRequestCountByHour(HttpServletRequest request, HttpServletResponse response) {
        return REQUEST_COUNT_BY_HOUR.get();
    }

    /**
     * 设置最后一次请求的时间
     * <b>注意:必须在请求数量统计之后调用，否则会导致 RequestCountByDay RequestCountByHour 无法清零</b>
     *
     * @return 操作成功返回 true
     */
    @Override
    public boolean setLastRequestTime(HttpServletRequest request, HttpServletResponse response) {
        LAST_REQUEST_TIME.set(System.currentTimeMillis());
        request.getServletContext().setAttribute(CommonApplicationAttributes.LAST_REQUEST_TIME, LAST_REQUEST_TIME);
        return true;
    }

    /**
     * @return 返回最后一次请求的时间
     */
    @Override
    public long getLastRequestTime(HttpServletRequest request, HttpServletResponse response) {
        return LAST_REQUEST_TIME.get();
    }

    /**
     * 设置当前请求请求时间
     *
     * @return 操作成功返回 true
     */
    @Override
    public boolean setRequestStartTime(HttpServletRequest request, HttpServletResponse response) {
        long lastRequestTime = System.currentTimeMillis();
        request.setAttribute(CommonRequestAttributes.REQUEST_START_TIME, lastRequestTime);
        return true;
    }

    /**
     * @return 返回当前请求请求时间
     */
    @Override
    public long getRequestStartTime(HttpServletRequest request, HttpServletResponse response) {
        return ConversionUtils.converter(request.getAttribute(CommonRequestAttributes.REQUEST_START_TIME), System.currentTimeMillis());
    }

    /**
     * 存储请求信息
     *
     * @param requestInfo 请求信息
     * @return 操作成功返回 true
     */
    @Override
    public boolean saveRequestInfo(HttpServletRequest request, HttpServletResponse response, RequestInfo requestInfo) {
        AccessLog accessLog = BeanMapper.mapper(requestInfo, AccessLog.class);
        accessLogService.addAccessLog(accessLog);
        return true;
    }
}
