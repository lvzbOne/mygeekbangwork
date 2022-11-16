package com.example.kafak.demo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 此类型为需要使用的消息内容
 *
 * @author lvzb3
 */
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Order {

    private Long id;
    private Long ts;
    private String symbol;
    private Double price;

}
