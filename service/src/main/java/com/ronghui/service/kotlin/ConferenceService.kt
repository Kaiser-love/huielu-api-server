//package com.ronghui.service.kotlin
//
//
//import com.baomidou.mybatisplus.extension.service.IService
//import com.ronghui.service.common.response.ResultBean
//import com.ronghui.service.entity.Conference
//import org.springframework.http.ResponseEntity
//
///**
// * @program: AuthService
// * @description:
// * @author: dongyang_wu
// * @create: 2019-05-30 22:39
// */
//interface ConferenceService : IService<Conference> {
//
//    fun getsByTime(timeType: Int, query: String, timeValue: String, type: Int, page: Int, count: Int): ResponseEntity<ResultBean<Any>>?
//}
//
//@Service("ConferenceService")
//open class ConferenceServiceImpl(private val conferenceDao: ConferenceDao, private val baseDao: BaseDao<Conference>) : ServiceImpl<ConferenceMapper, Conference>(), ConferenceService {
//
//    override fun getsByTime(timeType: Int, query: String, timeValue: String, type: Int, page: Int, count: Int): ResponseEntity<ResultBean<Any>>? {
//        var sql = StringBuffer()
//        val split = timeValue.split(" ")
//        when (timeType) {
//            // 查找指定日期的数据(timeValue为%Y-%m-%d代表年月日,) 查找本年 本年本月 本年本月本日
//            0 -> {
//                sql.append("FROM conference WHERE DATE_FORMAT(")
//                        .append(query).append(",").append("\'" + timeValue + "\'").append(") = DATE_FORMAT(SYSDATE(),").append("\'" + timeValue + "\'")
//                        .append(") and ").append(StringConstant.STATUS).append(" = ").append(type)
//            }
//            // 查找指定日期上午、下午或晚上的数据（timeValue为0 %Y-%m 2019-08） 2 %Y-%m 2019-07
//            1 -> {
//                when (split[0]) {
//                    "0" -> sql.append("FROM conference WHERE DATE_FORMAT(")
//                            .append(query).append(",").append("\'" + "%H:%m:%s" + "\'")
//                            .append(")<=").append("\'" + "12:00:00" + "\' and DATE_FORMAT(")
//                            .append(query).append(",").append("\'" + split[1] + "\'").append(") = ")
//                            .append("\'" + split[2] + "\'")
//                            .append(" and ").append(StringConstant.STATUS).append(" = ").append(type)
//                    "1" -> sql.append("FROM conference WHERE DATE_FORMAT(")
//                            .append(query).append(",").append("\'" + "%H:%m:%s" + "\'")
//                            .append(")>").append("\'" + "12:00:00" + "\'")
//                            .append(" and DATE_FORMAT(")
//                            .append(query).append(",").append("\'" + "%H:%m:%s" + "\'")
//                            .append(")<=").append("\'" + "18:00:00" + "\' and DATE_FORMAT(")
//                            .append(query).append(",").append("\'" + split[1] + "\'").append(") = ")
//                            .append("\'" + split[2] + "\'")
//                            .append(" and ").append(StringConstant.STATUS).append(" = ").append(type)
//
//                    "2" -> sql.append("FROM conference WHERE DATE_FORMAT(")
//                            .append(query).append(",").append("\'" + "%H:%m:%s" + "\'")
//                            .append(")>").append("\'" + "18:00:00" + "\' and DATE_FORMAT(")
//                            .append(query).append(",").append("\'" + split[1] + "\'").append(") = ")
//                            .append("\'" + split[2] + "\'")
//                            .append(" and ").append(StringConstant.STATUS).append(" = ").append(type)
//                }
//            }
//            2 -> {
//                // timeValue为具体天数 代表近几天的数据
//                sql.append("FROM conference WHERE DATE_SUB(")
//                        .append(query).append(", INTERVAL ").append(timeValue).append(" DAY) <= SYSDATE() AND TO_DAYS(")
//                        .append(query).append(")  >= TO_DAYS(NOW()) ")
//                        .append(" and ").append(StringConstant.STATUS).append(" = ").append(type)
//            }
//            3 -> {
//                // %Y-%m-%d-%H:%i:%s 2019-10-02-18:00:00 %Y-%m-%d-%H:%i:%s 2019-11-02-18:00:00
//                sql.append("FROM conference WHERE DATE_FORMAT(")
//                        .append("start_time").append(",").append("\'" + split[0] + "\'")
//                        .append(")>").append("\'" + split[1] + "\'")
//                        .append(" and ")
//                        .append("DATE_FORMAT(")
//                        .append("end_time").append(",").append("\'" + split[2] + "\'")
//                        .append(")<").append("\'" + split[3] + "\'")
//                        .append(" and ").append(StringConstant.STATUS).append(" = ").append(type)
//            }
//        }
//        val sizeSql = StringConstant.COUNT_SQL + sql.toString()
//        val size = baseDao.findBySql(sizeSql)[0]
//        sql.append(" limit ").append(page * count).append(",").append(count)
//        val dataSql = StringConstant.ALL_SQL + sql.toString()
//        val data = baseDao.findBySql(dataSql, Conference::class.java)
//        return ResponseHelper.OK(data, Integer.valueOf(size.toString()))
//    }
//}
//
