package com.example.rpcfxdemoprovider;

import com.example.rpcfxcore.api.RpcfxRequest;
import com.example.rpcfxcore.api.RpcfxResponse;
import com.example.rpcfxcore.server.RpcfxInvoker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 起凤
 * @description: TODO
 * @date 2022/5/1
 */
@Controller
@RestController
public class Test {
    @Autowired
    RpcfxInvoker invoker;

    @RequestMapping("/test")
    public void test(){
        System.out.println("======== [test] ========");
    }

    @RequestMapping("/map")
    public RpcfxResponse invoke(@RequestBody RpcfxRequest request) {
        return invoker.invoke(request);
    }
}
