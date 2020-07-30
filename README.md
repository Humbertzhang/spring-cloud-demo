# spring-cloud-demo
My Spring Cloud learning demo

使用的SpringCloud能力:
* Spring Cloud Config 
* Eureka & Ribbon & Feign 服务注册与发现以及相关接口调用

TODO:
* 服务熔断

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

启动顺序:
```
1. eureka-service
2. config-service
3. price-service & inventory-service
4. order-service
```

APIs:

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