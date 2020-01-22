# CAS简介

Compare and Set 无锁（乐观锁）优化、自旋cas(V, Expected, newValue), if (V == E) V = newValue; lese try again or fail。 有CPU原语支持：cmpxchg
ABA问题：在修改过程中V被连续修改两次，又回到了A。对于AtomicInteger，由于加法交换律，并无太大问题，但如果需要，可以加version解决，除了检查V之外，还要看
version，用AtomicStampedReference这个类，引入version，解决ABA问题。ABA如果涉及到了对象而不是基础数据类型的改变，则可能会有问题,比如：O->A->B，
O在修改的时候，突然有个线程改了O中某个引用的指向，指向了B然后改了B的内容，之后又把这个引用指回了A，现在O这个线程回来一看
没变，但是A的引用指向的内容却变化了，这会造成什么后果就不好说了。地址没变但是内容被修改了，这一点会不会出问题就不好说了。跟前女友复合之后，前女友已经经历了
很多。  

AtomicXXX不需要加锁，是怎么做到的？它们用了Unsafe这个类（了解即可），里面的方法很多。这个类1.8包括之前的jdk，只有用反射才能使用，这个类不能使用的原因还跟
classloader有关系。1.直接操作内存：allocateMemory putXX freeMemory pageSize 2. 直接生成类实例: allocateInstance 3.直接操作类或实例变量：
objectFieldOffset,getInt,getObject. 4. CAS相关操作weakCompareAndSetReference int Long 就是Unsafew类赋予了AtomicInteger CAS的能力。这里用了
弱指针，垃圾回收的时候效率比较高https://blog.csdn.net/zmx729618/article/details/54093532。看看Unsafe源码，就知道他用了饿汉模式的单例。
Unsafe.getUnsafe()获取。Unsafe是用来直接操纵JVM里的内存的，创建对象的时候直接把内存和对象头扔在那里，根据偏移量直接修改对象内存里的值。Unsafe这个类让我们
具备了类似写C/C++程序的能力，allocateMemory方法就类似于C里的malloc

对于private的属性的5种修改方法1. setter。2. 反射 3. Unsafe 4. cglib 5. asm