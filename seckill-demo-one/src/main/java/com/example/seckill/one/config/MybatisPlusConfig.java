package com.example.seckill.one.config;

import com.baomidou.mybatisplus.extension.plugins.OptimisticLockerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PaginationInterceptor;
import com.baomidou.mybatisplus.extension.plugins.PerformanceInterceptor;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("com.example.seckill.one.mapper")
public class MybatisPlusConfig {
    /***
     * plus 的性能优化
     * @return
     */
    /***
    @Bean
    public PerformanceInterceptor performanceInterceptor() {
        PerformanceInterceptor performanceInterceptor = new PerformanceInterceptor();
        //SQL是否格式化 默认false
        performanceInterceptor.setFormat(true);
        return performanceInterceptor;
    }
    */

    /**
     * @Description : mybatis-plus分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor() {
        return new PaginationInterceptor();
    }


    /**
     * 乐观锁 插件
     * @return
     */
    @Bean
    public OptimisticLockerInterceptor optimisticLoker() {
        return new OptimisticLockerInterceptor();
    }

}
