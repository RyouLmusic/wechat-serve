package com.hbr;

import com.hbr.netty.NettyWebSocketServer;
import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@MapperScan(value = {"com.hbr.mapper"})
@ComponentScan(basePackages = {"com.hbr", "com.n3r.idworker"}) //扫描之后才可以直接用@Autowired
public class WechatApplication {

    public static void main(String[] args) {
        SpringApplication.run(WechatApplication.class, args);

        // Netty的启动
        try {
            new NettyWebSocketServer(8888).run();
        } catch (Exception e) {
            System.out.println("NettyServerError:" + e.getMessage());
        }
    }

}
