package com.barber.tool;


import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Arrays;
import java.util.Collections;

/**
 * ClassName: CodeGenerator
 * Description: 代码生成器
 */
public class CodeGenerator {


    /**
     * 需要生成代码的表名
     * @param args
     */
    private static String[] tableNames = Arrays.asList("attendance", "evaluate", "haircutting_project", "reserve").toArray(new String[0]);


    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://127.0.0.1:3306/barber_shop", "root", "root")
                .globalConfig(builder -> {
                    builder.author("barber") // 设置作者
//                            .enableSwagger() // 开启 swagger 模式
//                            .fileOverride() // 覆盖已生成文件 /Users/chenfucheng/Desktop/怀平信息文档/信息录入系统/InformationEntryService/src/main/java
                            .outputDir("/Users/chenfucheng/Desktop/理发店管理系统/BarberShopService/src/main/java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com") // 设置父包名
                            .moduleName("barber") // 设置父包模块名
                            .entity("entity")
                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "/Users/chenfucheng/Desktop/理发店管理系统/BarberShopService/src/main/resources/mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tableNames) // 设置需要生成的表名
                            .addTablePrefix(""); // 设置过滤表前缀
                    builder.entityBuilder().enableChainModel()
                            .enableLombok()
                            .enableTableFieldAnnotation()
                            .enableActiveRecord();
                })
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }

}
