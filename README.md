<p align="center">
    <h3 align="center">Serialization-Sequence</h3>
    <p align="center">
        Serialization-Sequence 是一个流水号生成器。
        <br>
        <a href="#"><strong>-- Home Page --</strong></a>
        <br>
        <br>
         <a href="http://www.gnu.org/licenses/gpl-3.0.html">
             <img src="https://img.shields.io/badge/license-GPLv3-blue.svg" >
         </a>
    </p>    
</p>

## Introduction
Serialization-Sequence 是一个流水号生成器。<br/>
可按年，月，日等配置格式生成指定格式的流水。比如${prefix(DO)}${yyMMdd}${6位流水}:DO20190822000001<br/>
可选择redis作为其生成中间件。内置了几个可替换的格式化方式，并可以通过参数，模板进行配置。
项目缘由是项目中需要一个可按天，月等格式生成流水号的服务，比如美团外卖每个店铺的第几号订单之类的场景，不是分布式的id如雪花算法的id生成场景。
也可借此项目学习编写spring-boot-stater便写了此项目，项目还很简单粗糙，主要是借助了redis的特性。

## Documentation

### 开始使用
    1. 下载项目
    2. 执行打包 mvn package
    3. 将serialization-sequence-spring-boot-stater添加到项目lib内，如是maven项目可增加如下依赖
    <dependency>
        <groupId>com.zealren</groupId>
        <artifactId>serialization-sequence-spring-boot-stater</artifactId>
        <version>${project.version}</version>
    </dependency>
    4. 项目配置文件内增加相关配置，见配置文件说明
    5. 在redis内设置要生成的序列号规则，如下例子，规则见规则说明
        hset SEQ:ORDER:1:ShipmentNo template rule:prefix-time:yyMMdd-seq
        hset SEQ:ORDER:1:ShipmentNo length 8
        hset SEQ:ORDER:1:ShipmentNo loopType 3
        hset SEQ:ORDER:1:SHIPMENT.prefix PO
    6. 项目内可以使用如下方法,会生成一个序列号格式如:PO2019082200000001
        @Resource
        private SequenceGenerator redisSequenceGenerator;
        @Resource
        private SequenceFormatter commonSequenceFormatter;
        String seqNo = redisSequenceGenerator.generateOneSeq("ORDER", "1", "ShipmentNo", commonSequenceFormatter, null);   
    
### 配置文件说明
- redis DB 序号:sequence.redis.database
- redis host:sequence.redis.host
- redis 密码:sequence.redis.password
- redis 端口:sequence.redis.port
- redis 超时时间:sequence.redis.timeout
- redis max-idle:sequence.redis.pool.max-idle
- redis min-idle:sequence.redis.pool.min-idle
- redis max-wait:sequence.redis.pool.max-wait=
    
### 序列号规则
    规则保存在redis内，是hash格式，如下例子
```
    hset SEQ:ORDER:1:ShipmentNo template rule:prefix-time:yyMMdd-seq
    hset SEQ:ORDER:1:ShipmentNo length 8
    hset SEQ:ORDER:1:ShipmentNo loopType 3
    hset SEQ:ORDER:1:SHIPMENT.prefix PO
```
  + 序列号的key规则为:SEQ:${domain}:${site}:${key}
    1. domain: 代表哪个业务组或系统
    2. site: 站点或子系统
    3. key: 序列号项，比如采购单(PO)
    
  + 序列号的key规则为:SEQ:${domain}:${site}:${key}
    1. template: 序列号格式化模板
    2. length: seq的长度，不足补零
    3. loopType: 循环规则
    4. prefix 前缀
  + template 规则
    1. rule:${key} 相当于是 hset 规则内 hget SEQ:ORDER:1:ShipmentNo ${key} 一般是 rule:prefix
    2. site:传入的站点 site
    3. time:${时间戳格式}: 时间戳格式如yyMMdd-seq
    4. seq:序列号不足length用0补足
    5. arg:${index}:传入的参数${index}下标，从0开始
  + loopType 规则 0 1 2 3 4
        
        NO_LOOP("0", "不循环"), YEAR("1", "year"), MONTH("2", "month"),DAY("3", "day"), MINUTE("4", "minute");
  + 例子说明
    
    generateOneSeq("ORDER", "1", "ShipmentNo", commonSequenceFormatter, null);
    那么按规则为${rule:prefix}+${time:yyMMdd}+${seq 8位}
              ${PO}+${20190822}+${00000001}
    
  
### 系统设计
    相当于使用redis客户端根据key规则生成的key按照循环格式inr,获取到index，然后按照规则进行格式化。
    用户可以自定义序列化规则和序列化类，默认是 commonSequenceFormatter
    占用的redis db 在配置项内配置
    循环规则内会产生历史数据，可以配置定时任务进行清除，或生产是指定过期时间，建议是使用job清除。
        
  
    
    
    