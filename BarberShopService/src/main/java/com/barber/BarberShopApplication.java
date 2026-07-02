package com.barber;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * ClassName: BarberShopApplication
 */
@SpringBootApplication(scanBasePackages = "com.barber")
@MapperScan(markerInterface = BaseMapper.class, annotationClass = Mapper.class)
public class BarberShopApplication {
    public static void main(String[] args) {
        SpringApplication.run(BarberShopApplication.class, args);
    }
}


