package com.ronghui.service.controller;

import com.ronghui.service.common.constant.SqlConstant;
import com.ronghui.service.common.constant.TableConstant;
import com.ronghui.service.common.exception.ApiException;
import com.ronghui.service.common.exception.ApiResultEnum;
import com.ronghui.service.common.response.ResponseHelper;
import com.ronghui.service.common.response.ResultBean;
import com.ronghui.service.common.util.PoiUtil;
import com.ronghui.service.jpa.BaseDao;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;

/**
 * @program: monitor
 * @description:
 * @author: dongyang_wu
 * @create: 2019-07-10 18:11
 */
@Api(tags = "通用工具接口", description = "通用工具类")
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class CommonController {
    @Autowired
    private BaseDao baseDao;

    @ApiOperation("获取数据库表信息（0）或获取数据表的所有字段（表名,1）")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "mode", value = " 0获取所有数据库表 1获取相应表的所有字段信息", dataType = "int", paramType = "query")
    })
    @GetMapping("/common/database")
    public ResponseEntity<ResultBean> getTableColumn(@RequestParam(required = false) @ApiParam("表名") String tableName, @RequestParam Integer mode) {
        String sql = SqlConstant.QUERY_ALL_TABLE;
        if (mode == 1)
            sql = SqlConstant.QUERY_TABLIE_COLUMN + "\'" + tableName + "\'";
        List results = baseDao.findBySql(sql);
        List<HashMap> mapToList = new ArrayList<>();
        for (Object str : results) {
            HashMap<Object, Object> resultMap = new HashMap<>();
            resultMap.put("keyName", str);
            mapToList.add(resultMap);
        }
        return ResponseHelper.OK(mapToList);
    }

    @ApiOperation("导出指定条件数据库csv文件(连接符可取 = 或 like)")
    @GetMapping("/common/database/exportCsvDataFile")
    public ResponseEntity<ResultBean> getCsvByTableName(@RequestParam @ApiParam("数据库表名") String tableName, @RequestParam(required = false) String query, @RequestParam(required = false) String connection, @RequestParam(required = false) String queryString, HttpServletRequest request, HttpServletResponse response) {
        List dataColumnList = baseDao.findBySql(SqlConstant.QUERY_TABLIE_COLUMN + "\'" + tableName + "\'");
        List dataList;
        try (final OutputStream os = response.getOutputStream()) {
            if (StringUtils.isEmpty(query) || StringUtils.isEmpty(connection)) {
                dataList = baseDao.findBySql("select * from " + tableName, Class.forName(String.format(TableConstant.ENTITY_BASE_PACKAGE, SqlConstant.EntityToSqlMap.get(tableName))));
                System.out.println(dataList);
            } else {
                if (!"=".equals(connection) && !"like".equalsIgnoreCase(connection) && !"is".equalsIgnoreCase(connection))
                    return new ResponseEntity<>(ResultBean.error("connecttion参数出错"), HttpStatus.BAD_REQUEST);
                if (connection.equalsIgnoreCase("like"))
                    queryString = "%" + queryString + "%";
                dataList = baseDao.findBySql(SqlConstant.getQuerySql(tableName, query, connection, queryString), Class.forName(String.format(TableConstant.ENTITY_BASE_PACKAGE, SqlConstant.EntityToSqlMap.get(tableName))));
            }
            PoiUtil.responseSetProperties(tableName, request, response);
            PoiUtil.exportData2Csv(dataList, dataColumnList, os);
        } catch (Exception e) {
            return ResponseHelper.BadRequest("导出csv出错");
        }
        return ResponseHelper.OK("导出csv成功");
    }

    @ApiOperation("导入Csv数据库表")
    @PostMapping("/common/database/importCsvDataFile")
    public ResponseEntity<ResultBean> importCsvDataFile(@ApiParam(value = "文件信息", required = true) @RequestParam("file") MultipartFile file, @RequestParam @ApiParam("数据库表名") String tableName) {
        if (Objects.isNull(file) || file.isEmpty()) {
            throw new ApiException(ApiResultEnum.FILE_EMPTY_ERROR);
        }
        try {
            PoiUtil.importCsvDataFile(file.getInputStream(), tableName);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseHelper.OK("文件上传成功");
    }
}