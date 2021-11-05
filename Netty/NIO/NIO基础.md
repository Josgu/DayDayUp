# NIO 基础
NIO为非阻塞IO，有三大组件 Channel、Buffer、Selector

## Channel
`Channel`是一个读写数据的双向通道，可以从`Channel`中读入数据和写入数据，从通道中读写数据我们需要一个缓冲器也就是`Buffer`，数据将从`Channel`中读取写入到`Buffer`中，`Buffer`中的数据可以写入到Channel中。
![Channel&Buffer关系](images/Channel&Buffer关系.png)
### 常见的Channel
在`Java NIO` 中主要有以下`Channel`的实现类
- FileChannel 从文件中读取数据
- DatagramChannel 通过UDP协议读取数据
- SocketChannel 通过TCP协议读取数据
- ServerSocketChannel 通过监听Tcp连接，对每个Tcp连接创建SocketChannel

## Buffer
`Buffer`是与`Channel`进行交互的，`Buffer`通过`Channel`写入或者读取数据，`Buffer`的本质就是一个内存块，只不过`Buffer`类对内存块进行了封装，让我们更好的使用内存块来进行数据的读写。
### 常见的Buffer
在`Java NIO`中有以下常见的`Buffer`类型
- ByteBuffer
- MappedByteBuffer
- CharBuffer
- DoubleBuffer
- FloatBuffer
- IntBuffer
- LongBuffer
- ShortBuffer

可以看到这些`Buffer`处理着不同的数据类型，包括`byte、char、double、float、int、long、short`类型

## Selector
在`Java NIO`中，`Selector`是一个管理一个或者多个`Channel`实例，监测哪个`Channel`已经是准备好读或者写，通过`Selector`可以让单个线程管理多个`Channel`，进而管理多个网络连接。
![Channel&Selector&Thread关系](images/Channel&Selector&Thread关系.png)