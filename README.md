# Seckill
商城秒杀的示例，一步步改造，逐渐提升抗压能力

### 分支说明
- origin 只实现了秒杀业务逻辑，没有任何优化的原始分支，在高并发下会暴露问题
- static_page 在origin分支的基础上做前后端分离，并使用redis做简单的查询缓存，数据库使用乐观锁解决高并发问题
- async 在static_page的基础上，将秒杀接口改造为异步下单，引入rabbitmq做抗压，大量使用redis，前端几乎无阻塞
- security 在async的基础上，为秒杀接口提供安全校验，对抗压能力几乎没有影响

### 抗压测试
在服务器资源有限的情况下做测试

服务器：4核CPU主频2.9Ghz 4G内存

redis、mysql、rabbitmq、tomcat都部署在一台机器上，mysql和rabbitmq以docker方式启动

tomcat优化 使用G1GC，响应时间100ms自适应

5000个线程同时并发访问，每个线程10次循环，测试秒杀接口/seckill/doSeckill

- origin: QPS 450
- static_page: QPS 570
- async: QPS 900

