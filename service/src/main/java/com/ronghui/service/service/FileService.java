package com.ronghui.service.service;

import com.ronghui.server.network.Message;

import java.util.ArrayList;
import java.util.List;

public interface FileService {
    Message<String> completePic2PPT(String pptName, List<String> imgPaths);

    String createPPTWithImgUrl(final String name, final List<String> urlList);

    String createPDFWithImgUrl(final String name, final List<String> urlList);

    byte[] findFileByname(final String name);

    byte[] findFileById(final String id);
}
