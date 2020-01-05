# synchronized注意事项

synchronized(object)中的object不能是String常量（别的线程可能也锁了同一个常量，如Jetty类库）、Integer（值一变可能就要产生新对象，享元模式）、Long等类型