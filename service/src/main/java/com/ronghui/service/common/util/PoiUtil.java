package com.ronghui.service.common.util;

import com.csvreader.CsvReader;
import com.ronghui.service.common.constant.*;
import com.ronghui.service.common.exception.ApiException;
import com.ronghui.service.common.exception.ApiResultEnum;
import com.ronghui.service.context.SpringContextBeanService;
import com.ronghui.service.entity.Conference;
import com.ronghui.service.entity.Process;
import com.ronghui.service.jpa.dao.ConferenceDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @program: monitor
 * @description:
 * @author: dongyang_wu
 * @create: 2019-07-10 18:13
 */
public class PoiUtil {
    /**
     * CSV文件列分隔符
     */
    private static final String CSV_COLUMN_SEPARATOR = ",";

    /**
     * CSV文件行分隔符
     */
    private static final String CSV_RN = "\r\n";

    public static void exportData2Csv(List dataList, List columns, OutputStream os) {
        StringBuffer buf = new StringBuffer();
        for (Object item : columns) {
            buf.append(item.toString()).append(CSV_COLUMN_SEPARATOR);
        }
        buf.append(CSV_RN);
        try {
            // 设置数据
            for (int i = 0; i < dataList.size(); i++) {
                Object data = dataList.get(i);
                for (int j = 0; j < columns.size(); j++) {
                    String column = columns.get(j).toString();
                    boolean flag = false;
                    // 列名以id结尾且不是主键
                    if (column.charAt(column.length() - 2) == 'i' && column.charAt(column.length() - 1) == 'd' && column.length() > 2) {
                        column = column.substring(0, column.length() - 2);
                        flag = true;
                    }
                    Field field = data.getClass().getDeclaredField(StringUtil.lineToHump(column));
                    //设置对象的访问权限，保证对private的属性的访问
                    field.setAccessible(true);
                    if (field.get(data) != null && flag) {
                        Object o = field.get(data);
                        Field fieldItem = o.getClass().getDeclaredField("id");
                        fieldItem.setAccessible(true);
                        buf.append(fieldItem.get(o).toString()).append(CSV_COLUMN_SEPARATOR);
                        continue;
                    }
                    buf.append(field.get(data) != null ? field.get(data).toString() : "").append(CSV_COLUMN_SEPARATOR);
                }
                buf.append(CSV_RN);
            }
            // 写出响应
            OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(buf.toString());
            bw.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void importCsvDataFile(InputStream fileInputStream, String tableName) {
        try {
            Reader reader = new InputStreamReader(fileInputStream);
            CsvReader csvReader = new CsvReader(reader);
            // 读表头
            csvReader.readHeaders();
            List dataColumnList = Arrays.asList(csvReader.getHeaders());
            while (csvReader.readRecord()) {
                saveEntity(dataColumnList, tableName, csvReader);
            }
        } catch (Exception e) {
            System.out.println(e);
            throw new ApiException(ApiResultEnum.FILE_ERROR);
        }
    }

    public static void saveEntity(List<String> dataColumnList, String tableName, CsvReader csvReader) throws Exception {
        switch (tableName) {
            case "conference":
                ConferenceDao conferenceDao = SpringContextBeanService.getBean("ConferenceDao");
                String tableNameToIgnoreAttr = TableConstant.tableNameToIgnoreAttr.get(tableName);
                Object obj = Class.forName(String.format(TableConstant.ENTITY_BASE_PACKAGE, SqlConstant.EntityToSqlMap.get(tableName))).newInstance();
                dataColumnList.stream().filter(column -> !tableNameToIgnoreAttr.contains(column)).forEach(column -> {
                    try {
                        Field field = obj.getClass().getDeclaredField(column.trim().replace("\uFEFF", ""));
                        field.setAccessible(true);
                        field.set(obj, converAttributeValue(field.getType().getName(), csvReader.get(column)));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
                ReflectUtil.setFiledAttrValue(obj, "endTime", converAttributeValue(Timestamp.class.getTypeName(), ReflectUtil.getSourceData("startTime", obj)));
                ReflectUtil.setFiledAttrValue(obj, "description", "一场大会议");
                ReflectUtil.setFiledAttrValue(obj, "logo", "https://wx.qlogo.cn/mmopen/vi_32/kuRicCbWhUmg0ezS898737Fx8C2U053elBTzYWugRmPibtyRLRDhH6lnnIeBquuuEic");
                ReflectUtil.setFiledAttrValue(obj, "isPayment", EntityConstant.badProperty);
                ReflectUtil.setFiledAttrValue(obj, "clicksNumber", 0L);
                ReflectUtil.setFiledAttrValue(obj, "status", EntityConstant.notProperty);
                try {
                    Conference result = conferenceDao.save((Conference) obj);
                    Process.builder()
                            .fileId("5d064235c03618303d257fc5")
                            .type(0)
                            .videoUrl("http://1257353650.vod2.myqcloud.com/ce9ea327vodcq1257353650/4df79f675285890790148021832/f0.mp4")
                            .timeAxis(DateUtil.randomDate(null, null))
                            .description("会议主会场上半场")
                            .conferenceId(result.getId()).build().insert();
                    Process.builder()
                            .fileId("5d064235c03618303d257fc5")
                            .type(0)
                            .videoUrl("http://1257353650.vod2.myqcloud.com/ce9ea327vodcq1257353650/4df79f675285890790148021832/f0.mp4")
                            .timeAxis(DateUtil.randomDate(null, null))
                            .description("会议主会场下半场")
                            .conferenceId(result.getId()).build().insert();
                    Process.builder()
                            .fileId("5d064235c03618303d257fc5")
                            .type(1)
                            .videoUrl("http://1257353650.vod2.myqcloud.com/ce9ea327vodcq1257353650/4df79f675285890790148021832/f0.mp4")
                            .timeAxis(DateUtil.randomDate(null, null))
                            .description("会议分会场上半场")
                            .conferenceId(result.getId()).build().insert();
                    Process.builder()
                            .fileId("5d064235c03618303d257fc5")
                            .type(1)
                            .videoUrl("http://1257353650.vod2.myqcloud.com/ce9ea327vodcq1257353650/4df79f675285890790148021832/f0.mp4")
                            .timeAxis(DateUtil.randomDate(null, null))
                            .description("会议分会场下半场")
                            .conferenceId(result.getId()).build().insert();
                } catch (Exception e) {
                    System.out.println(e);
                }
                break;
            default:
                break;
        }
    }

    // 自动匹配类型
    public static Object converAttributeValue(String type, String value) {
        if ("long".equals(type) || Long.class.getTypeName().equals(type)) {
            if (!StringUtils.isEmpty(value) && value.contains(".")) {
                value = value.substring(0, value.indexOf("."));
            }
            return Long.parseLong(StringUtils.isEmpty(value) ? "0" : value);
        } else if ("double".equals(type) || Double.class.getTypeName().equals(type)) {
            return Double.parseDouble(StringUtils.isEmpty(value) ? "0" : value);
        } else if (Timestamp.class.getTypeName().equals(type)) {
            if (StringUtils.isEmpty(value) || !value.contains("-"))
                return DateUtil.randomDate(null, null);
            return Timestamp.valueOf(value);
        } else if ("int".equals(type) || Integer.class.getTypeName().equals(type)) {
            return Integer.valueOf(StringUtils.isEmpty(value) ? "0" : value);
        } else if ("byte".equals(type) || Byte.class.getTypeName().equals(type)) {
            if (StringUtils.isEmpty(value))
                value = "0";
            if (!StringUtils.isEmpty(value) && value.contains("."))
                value = value.substring(0, value.indexOf("."));
            return Byte.valueOf(value);
        } else {
            return value;
        }
    }

    public static void responseSetProperties(String fileName, HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        String userAgent = request.getHeader("User-Agent");
        // 设置文件后缀
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
        String fn = fileName + sdf.format(new Date()) + ".csv";
        // 如果没有userAgent，则默认使用IE的方式进行编码，因为毕竟IE还是占多数的
        String rtn = "filename=\"" + fn + "\"";
        if (userAgent != null) {
            userAgent = userAgent.toLowerCase();
            // IE浏览器，只能采用URLEncoder编码
            if (userAgent.indexOf("IE") != -1) {
                rtn = "filename=\"" + fn + "\"";
            }
            // Opera浏览器只能采用filename*
            else if (userAgent.indexOf("OPERA") != -1) {
                rtn = "filename*=UTF-8''" + fn;
            }
            // Safari浏览器，只能采用ISO编码的中文输出
            else if (userAgent.indexOf("SAFARI") != -1) {
                rtn = "filename=\"" + new String(fn.getBytes("UTF-8"), "ISO8859-1")
                        + "\"";
            }
            // FireFox浏览器，可以使用MimeUtility或filename*或ISO编码的中文输出
            else if (userAgent.indexOf("FIREFOX") != -1) {
                rtn = "filename*=UTF-8''" + fn;
            }
        }
        String headStr = "attachment;  " + rtn;
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/msexcel;charset=UTF-8");
        response.setHeader("Content-Disposition", headStr);
    }
}