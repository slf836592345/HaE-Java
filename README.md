# **HaE-Java  信息高亮与提取者（Java版本）**

设计灵感及代码实现参考了： https://github.com/gh0stkey/HaE，在此表示感谢

**简介：**

HaE-Java是基于Java开发的一款burpsuite插件，其内置并支持自定义正则表达式，可高亮标记敏感请求，方便后续深度挖掘。代码内部维护了一个简单的缓存池，界面响应速度还可以。



**解释下为何使用Java语言重新造一个轮子，理由有：**

1、学习Burp插件开发，复习Java开发（N年没写了）

2、方便后续进一步拓展

3、想不到了



**使用：**

1、将HaE-Java.xml放在burpsuite.jar的同级目录（配置文件路径是个坑，后面可能会解决）

2、切换到burpsuite的extender标签页，加载HaE-Java.jar即可使用



![image-20200611012603138](C:\Users\user\AppData\Roaming\Typora\typora-user-images\image-20200611012603138.png)

