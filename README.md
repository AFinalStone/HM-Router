
### 使用说明

##### 路由配置文件
需要在主工程的assets文件夹下放置2个路由配置文件：router_class_list.json、router_white_list.json

router_class_list.json配置文件格式如下：

```
{
  "main": [
    {
      "url": "hmiou://m.54jietiao.com/main/index",
      "iclass": "",
      "aclass": "com.hm.iou.router.demo.TestActivity1"
    }
  ]
}
```
router_white_list.json配置文件格式如下：

```
[
  "hmiou://m.54jietiao.com/main/index"
]
```

##### 路由跳转示例

```
Router.getInstance().buildWithUrl("hmiou://m.54jietiao.com/main/index").navigation(MainActivity.this);
```
