# seckill

在async分支的基础上，为秒杀接口增加安全校验
#### 秒杀接口隐藏
秒杀开始之前，先请求接口获取秒杀地址
- 1.点击秒杀按钮，发ajax去请求后台生成一个md5(UUID)字符串，后台将生成的字符串放入redis（过期时间很60s）后返回给前台
- 2.ajax回调中拿到传来的随机字符串，调用真实秒杀接口（/{pathStr}/doSeckill），秒杀接口改造成，带上PathVariable参数
- 3.秒杀收到的请求，先验证PathVariable，从redis里拿出path和前台传来的做比较，一致再执行正常的逻辑。

#### 数学公式验证码
点击秒杀之前，先输入验证码，分散用户请求
- 1.添加生成验证码接口，秒杀倒计时结束时前台请求后台，后台生成验证码图片，并将验证码里的数学公式计算结果，将结果存入redis，设置短暂的存活时间
- 2.在获取秒杀路径的时候，先验证验证码。把redis里的验证码结果和用户填的作对比。
- 3.计算验证码数学公式可使用 javax.script.ScriptEngine

#### 全局的校验/限流拦截器
+ 限流逻辑：缓存记录用户访问次数，缓存有效期1分钟，超过次数直接返回失败
- 通过拦截器减低对业务代码的侵入性（定义@AccessLimit注解），使用注解配合HandlerInterceptorAdapter，在请求进入接口方法之前就做校验，resolver晚于interceptor执行，可以在interceptor中获取用户数据，通过ThreadLocal传递给resolver，然后由resolver做参数绑定

因为使用redis，这部分新增的逻辑对QPS没太大影响

局域网单tomcat，G1GC

秒杀接口QPS：900/sec


