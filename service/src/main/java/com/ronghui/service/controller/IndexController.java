package com.ronghui.service.controller;

import com.ronghui.service.websocket.WebSocketServer;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;


@RestController
@Api(tags = "WebSocket", description = "主动推送API")
@CrossOrigin(origins = "*", maxAge = 3600)
@Slf4j
public class IndexController {
    /**
     * 群发消息内容
     * @param message
     * @return
     */
    @GetMapping(value = "/sendAll")
    @ApiOperation("群发消息内容")
    public String sendAllMessage(@RequestParam String message) {
        WebSocketServer.broadCastInfo(message);
        return "ok";
    }
    /**
     * 指定会话ID发消息
     * @param message 消息内容
     * @param id 连接会话ID
     * @return
     */
    @GetMapping(value = "/sendOne")
    @ApiOperation("根据连接连接会话ID发送消息")
    public String sendOneMessage(@RequestParam String message, @RequestParam String id) {
        WebSocketServer.sendMessage(id, message);
        return "ok";
    }
}
