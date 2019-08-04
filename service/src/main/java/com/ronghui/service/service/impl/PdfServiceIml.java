package com.ronghui.service.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ronghui.service.entity.Pdf;
import com.ronghui.service.mapper.PdfMapper;
import com.ronghui.service.service.PdfService;
import org.springframework.stereotype.Service;

/**
 * @program: monitor
 * @description:
 * @author: dongyang_wu
 * @create: 2019-07-12 12:02
 */
@Service("PdfService")
public class PdfServiceIml extends ServiceImpl<PdfMapper, Pdf> implements PdfService {
}