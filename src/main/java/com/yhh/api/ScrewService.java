package com.yhh.api;


import cn.smallbun.screw.core.Configuration;
import cn.smallbun.screw.core.engine.EngineConfig;
import cn.smallbun.screw.core.engine.EngineFileType;
import cn.smallbun.screw.core.engine.EngineTemplateType;
import cn.smallbun.screw.core.execute.DocumentationExecute;
import cn.smallbun.screw.core.process.ProcessConfig;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yhh 2022-04-25 09:11:43
 **/
public class ScrewService {
    private static final Logger log = LoggerFactory.getLogger(ScrewService.class);

    public static void create() {
        log.info("begin create doc");
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setDriverClassName(ScrewProperties.driverClassName);
        hikariConfig.setJdbcUrl(ScrewProperties.url);
        hikariConfig.setSchema(ScrewProperties.schema);
        hikariConfig.setUsername(ScrewProperties.username);
        hikariConfig.setPassword(ScrewProperties.password);
        //设置可以获取tables remarks信息
        hikariConfig.addDataSourceProperty("useInformationSchema", "true");
        hikariConfig.setMinimumIdle(3);
        hikariConfig.setMaximumPoolSize(5);
        DataSource dataSource = new HikariDataSource(hikariConfig);
        // 生成文件配置
        EngineConfig engineConfig = EngineConfig.builder()
                // 生成文件路径
                .fileOutputDir(ScrewProperties.fileOutputDir)
                // 打开目录
                .openOutputDir(false)
                // 文件类型
                .fileType(EngineFileType.valueOf(ScrewProperties.fileType))
                // 生成模板实现
                .produceType(EngineTemplateType.freemarker)
                // 输出文件名，不用带后缀
                .fileName(ScrewProperties.filename)
                .build();

        // 生成文档配置（包含以下自定义版本号、描述等配置连接）
        Configuration config = Configuration.builder()
                .version("1.0.0")
                .description("1")
                .dataSource(dataSource)
                .engineConfig(engineConfig)
                .produceConfig(getProcessConfig())
                .build();

        // 执行生成
        DocumentationExecute documentationExecute = new DocumentationExecute(config);
        documentationExecute.execute();
        log.info("it is ok");
    }

    private static ProcessConfig getProcessConfig() {
        // 忽略表名
        List<String> ignoreTableName = split(ScrewProperties.ignoreTableName);
        // 忽略表前缀
        List<String> ignorePrefix = split(ScrewProperties.ignorePrefix);
        // 忽略表后缀
        List<String> ignoreSuffix = split(ScrewProperties.ignoreSuffix);

        return ProcessConfig.builder()
                //根据名称指定表生成
                .designatedTableName(new ArrayList<>())
                //根据表前缀生成
                .designatedTablePrefix(new ArrayList<>())
                //根据表后缀生成
                .designatedTableSuffix(new ArrayList<>())
                //忽略表名
                .ignoreTableName(ignoreTableName)
                //忽略表前缀
                .ignoreTablePrefix(ignorePrefix)
                //忽略表后缀
                .ignoreTableSuffix(ignoreSuffix).build();
    }

    private static List<String> split(String txt) {
        String[] split = txt.split("#");
        List<String> ignore = new ArrayList<>();
        for (String s : split) {
            if (s != null && !s.isEmpty()) {
                ignore.add(s);
            }
        }
        return ignore;
    }
}
