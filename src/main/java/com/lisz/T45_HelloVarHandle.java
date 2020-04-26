package com.lisz;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.VarHandle;

public class T45_HelloVarHandle {
    private static VarHandle handle;
    int x = 8;

    static {
        try {
            // MethodHandles.lookup()是固定的写法，返回Lookup对象，不用太关注。findVarHandle的意思就是：
            // 去T45_HelloVarHandle.class中找名字是"x"，类型是int的这么一个变量，找到之后请你给我指向它，然后通过handle
            // 就也能找到x这个变量了，也就是说int类型的一个变量也有了个引用指了过去相当于一个int类型的指针.VarHandle
            // 有了之后就可以直接操纵里面的值了, 最关键的是它可以做CAS，原子性的修改值，明明只是个int变量，不是 AtomicInteger，
            // 但是却可以当作AtomicInteger做原子性的修改（除了compareAndSet还有weakCompareAndSet）题外话: int x = 100
            // 是原子性的，而long x = 100不是。 1.9 之前只能用反射，但是效率要低得多，因为还要做各种检查，而VarHandle不需要，
            // 直接操作就行，VarHandle可以理解为直接操纵二进制码(用了cmpxchg CPU原语)
            handle = MethodHandles.lookup().findVarHandle(T45_HelloVarHandle.class, "x", int.class);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        T45_HelloVarHandle t = new T45_HelloVarHandle();

        System.out.println((int)handle.get(t));
        handle.set(t, 9);
        System.out.println(t.x);

        handle.compareAndSet(t, 9, 10); // Atomic，public final native，调用了CPU原语 cmpxchg
        System.out.println(t.x);

        int x1 = (int)handle.getAndAdd(t, 10); // Atomic，但是 x = x + 10或者x += 10不是原子性的，要加锁
        System.out.println(x1);
        System.out.println(t.x);
    }
}
