package com.lisz.lock;

import com.lisz.utils.M;

import java.io.IOException;

public class T48_NormalReference {
    public static void main(String[] args) throws IOException {
        M m = new M();
        //m = null; //加上这一句就会有"Finalize"输出，m被回收
        System.gc();
        System.in.read(); //这里要阻塞住，因为System.gc()是跑在别的线程里的
    }
}
