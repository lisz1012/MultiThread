## Container 概述

容器底层的物理结构只有两种：连续的数组、非连续的链表。Java容器的逻辑分类：Collection和Map，前者一个个地存，后者一对对地存（也可以说每次存一个entry）。Collection原来只有两大接口：
List和Set，而后来又加入了Queue，这个接口就是为高并发准备的。面试题：既然List可以从一头放入、另一头取出，为什么还要有个Queue接口呢？完成任务的取和装，最重要的一块是阻塞队列：BlockingQueue，
他的初衷就是为了线程池、高并发做准备，所以在对比Queue和其他容器的时候就扯高并发。写一些基础模块或中间件的时候才会使用到线程池，但是面试中常考（不少面试官为了卖弄，BS一下。会写甲骨文的不一定能
写出好文章）  

Hashtable和Vector自带synchronized锁基本不用，知道有这么个东西就可以了。Collections.synchronizedMap()加的锁又特别重，还是synchronized，并没有比原来的Hashtable高多少，也就是锁的粒度
稍微小一点吧。List和Map最早是从这两个类发展而来的。有的时候synchronized效率未必低，永远不要想当然，要写程序测试，有个测试框架叫JMH
