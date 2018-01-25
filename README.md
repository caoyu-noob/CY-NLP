# CY-NLP
目前只是一个对于三国志中发生事件相关的时间、地点、参与人物问题的粗略实现

##运行程序你需要：
1. JDK 1.8
2. 一个IDE，推荐IntelliJ IDEA
3. Maven

##运行步骤
1. 在SearchJava目录下运行命令 mvn clean install ， 下载相关依赖包并build工程，这里我使用的是
maven的中央仓库，使用其他仓库可能找不到依赖的分词工具thulac4j.
2. 下载[THULAC](http://thulac.thunlp.org/)的模型文件，并将其解压后放在SearchJava/models下面
3. 将项目导入IDE，确保所有的依赖包都已经导入，将JenaTest作为Main类，以Java Application形式运行，建议在VM Options后面加上 -Xms32m -Xmx1024m 以
免出现堆内存不足的情况。
4. 第一次运行需要先运行JenaTest中的以下函数，以运行该函数读取owl文件内容并以Jena TDB的形式存储。存储
后不需要再次进行文件读取。

```java
        //Please run this function when you run it in the first time in you local environment
        saveModels();
        //Please run this function when you run it in the first time in you local environment
```
5. 运行在命令行中输入问题，系统会自动回答问题，如无法回答该问题则会显示”无法回答“。
例如：

赤壁之战在哪里发生？

官渡之战是什么时候？

谁参加了江陵之战？
