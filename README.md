麻雀虽小，但五脏俱全 
---
sparrow 源自中国俗语 麻雀虽小，但五脏俱全，全力打造一个全新的低耦合，0依赖的高性能java基础框架。

- 有没有发现我们的工程代码其实很臃肿？
- 有没有发现我们依赖了很多没有用的jar包？
- 有没有发现在项目中因为jar 冲突而折腾很久？
- 有没有想过我只依赖jdk 就实现一个WEB工程？
- 有没有发现其实我们只需要一小块功能，而需要引入一个大框架？
- 有没有发现其实有些功能非常简单，而被框架限制了？
- 有没有发现其实有些功能原理不复杂，而框架实现很庞杂？因为不相信程序员！
- 有没有发现相似的框架提供的业务功能是一致的？但对外的接口是不同的？想不想统一？
- 有没有想过自己也实现一套JAVA-WEB 框架？

如果你也一样？
那么sparrow 非常适合你！

为此基于oop的基本思想，构建一层api,最大化的解耦。


框架特点
---
- 相信程序员

通过对原理有了更深入的了解，对写程序来讲会更简单，高效，很多框架之所以很重，很重要的一个原因是不相信程序员，这个框架从jdk出发，尽量不依赖第三方jar 包，让程序能跑起来，让程序更快。

- 从0开始

人脑思维是发散的，如果中间某个知识点断掉，可能就会产生知识盲点，这个盲点可能产生的影响很大的，尤其是技术更象是一层窗户纸，捅破了，简单，捅不破，如隔山。
所以我们从0开始，让知识连贯起来，消除盲点，不只让程序变得高效，更让程序员变得高效。

- 0依赖

框架实现最简单的，最核心的功能，尽量不依赖任何框架，包括spring。


- 解耦/隔离

sparrow模块 只定义了一些接口，具体实现在其他的模块中，是否依赖由业务端决定，最大化解耦。

- 扩展
 
遵循开闭原则，对业务提供扩展点。

项目架构及远期规划
---
架构中大部分功能已具基本的使用框架，但还需要进一步完善和优化，具体内容可查看架构详细介绍

jedis和rocket mq 客户端已具备基本的生产环境使用条件

其它模块还需进一步完善，欢迎有兴趣的小伙伴，一起加入
email:zh_harry#163.com



```aidl
cd sparrow-bom
mvn clean install -Dmaven.test.skip
cd ..
mvn clean install -Dmaven.test.skip
```

- sparrow 为保持代码整洁，所有的test 项目单独由一个sparrow-test 项目管理，该项目已有部分功能通过test case,朋友们可以下载了解sparrow 运行

     [测试用例 https://github.com/sparrowzoo/sparrow-test](https://github.com/sparrowzoo/sparrow-test)
 case 逐步完善中...
 
我们的愿景和未来
--------
让程序员脱离spring, 也能写代码,而且更快，更优雅


