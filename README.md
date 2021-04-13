# spring-cloud-demo
A Spring Cloud demo

本demo需要本地拥有JDK 1.8以及Maven环境方可运行。

## Demo介绍

使用的SpringCloud能力:
* Spring Cloud Config 
* Eureka & Ribbon & Feign 服务注册与发现以及相关接口调用
* Hystrix: circuit breaker(断路器模式), fallback(后备模式), bulkhead(舱壁模式)


services:

```
* config-service: port 4001 | Spring Cloud Config服务。 在本demo中充当初始数据。
  config中分别配置了商品价格、商品库存以及商品价格调整(0.0 - 10.0). 其他服务启动时获取数据。
  config-service同样注册到eureka中，其他服务通过eurake获取配置. 

* eureka-service: port 4002 | eureka 服务注册服务

* price-service: port 4003 | 从config-service中读取产品对应价格。提供价格查询接口。

* inventory-service: port 4004 | 从config-service中读取产品对应库存。提供库存查询接口。

* order-service: port 4005 | 订购服务。接受订购产品类目，返回价格。
```

**编译**

在 ` spring-cloud-demo` 文件夹中运行 `mvn clean package`对所有服务进行编译.


**启动顺序**

```
1. eureka-service
2. config-service
3. price-service & inventory-service
4. order-service

注意开始测试前可能需要关闭**本地代理**
注意应等待 eureka-service 启动成功后方可启动其他服务。
且应等待config-service 启动成功后再启动price-service, inventory-service与order-service，因为后面三个服务需要从config中读取配置。

```

**APIs**



* 获取订购物品价格

| Methods | URL | 
| --- | --- |
| POST | http://localhost:4005/v1/order/ |

Request Body Example:
```json
{
	"orders": [
		{
		"name": "gtx1080",
		"count": 1
		},
		{
		"name": "rx5600",
		"count": 2}]
}
```

Response Example:
```json
{
    "amount": 630
}
```


* 获取价格列表

| Methods | URL | 
| --- | --- |
| GET | http://localhost:4003/v1/prices/ |

Responses:
```json 
{
    "gtx1080": 300,
    "rx5700": 300,
    "gtx1070": 200,
    "rtx2080": 500,
    "rx5600": 200
}
```

* 获取库存列表

| Methods | URL | 
| --- | --- |
| GET | http://localhost:4004/v1/inventory/ |

Responses:
```json 
{
    "gtx1080": 5,
    "rx5700": 5,
    "gtx1070": 5,
    "rtx2080": 3,
    "rx5600": 5
}
```


## 试用客户端弹性容错相关功能

注意更改代码后需要**重新编译** 


* 断路器：
尝试在 `inventory-service`服务的 `getInventoryPairs` 函数中加入一些sleep函数(已经准备好并注释掉了，可以直接取消注释即可试用)，使其运行时间超过`HystrixProperty` 中设定的超时时间。
此时请求 `inventory-service` 的API，观察程序运行与返回情况。

* 后备处理:
去掉 `inventory-service`服务的 `getInventoryPairs` 函数上 HystrixCommand 中对 `,fallbackMethod = "buildFallbackInventoryPairs"` 的注释，再次请求 `inventory-service` 的API，观察程序运行情况。

* 舱壁模式：
保留 `inventory-service` 中的sleep。在设置的时间内快速请求数次`order service` ，达到舱壁模式中配置的阈值要求，观察系统返回值。
因为`order service`中设置线程池中只有一个线程，且等待队列中最大为1，那么当第三次请求时，应当会快速失败，即立即使用fallback中的函数进行返回。

* 结合[Hystrix文档](https://github.com/Netflix/Hystrix/wiki/Configuration) 与 [实践博客](https://zhenbianshu.github.io/2018/09/hystrix_configuration_analysis.html) 对其他参数进行探索.
