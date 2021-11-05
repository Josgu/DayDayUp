# Channel
`Channel`是一个读写数据的双向通道，可以从`Channel`中读入数据和写入数据，从通道中读写数据我们需要一个缓冲器也就是`Buffer`，数据将从`Channel`中读取写入到`Buffer`中，`Buffer`中的数据可以写入到Channel中。
![Channel&Buffer关系](images/Channel&Buffer关系.png)
## 常见的Channel
在`Java NIO` 中主要有以下`Channel`的实现类
- FileChannel 从文件中读取数据
- DatagramChannel 通过UDP协议读取数据
- SocketChannel 通过TCP协议读取数据
- ServerSocketChannel 通过监听Tcp连接，对每个Tcp连接创建SocketChannel

本文将对`FileChannel`进行详细介绍以达到理解Channel，其他Channel会根据实际情况提及
## FileChannel
### FileChannel示例程序
对于`FileChannel`的使用首先可以看以下示例程序
```Java
package com.ymplans.channel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * FileChannel使用的例子
 *
 * @author Jos
 */

public class FileChannelExample {

    public static void main(String[] args) {
        try (RandomAccessFile file = new RandomAccessFile("src/test/resources/FileChannelExample.txt", "rw")){
            // 获取文件的Channel
            FileChannel channel = file.getChannel();
            // 输出文件大小
            ByteBuffer writeBuffer = StandardCharsets.UTF_8.encode("GoodGoodStudyDayDayUp GoodGoodStudyDayDayUp GoodGoodStudyDayDayUp ");
            while (writeBuffer.hasRemaining()){
                channel.write(writeBuffer);  // 1
            }
            // 强制保存数据和元数据
            channel.force(true);  // 2
            System.out.println("文件大小为： " + channel.size());
            // 截取文件
            channel.truncate(44);  // 3
            System.out.println("截取后的文件大小为： " + channel.size());
            System.out.println("channel 的 position为：" + channel.position());
            // 设置position
            channel.position(0);  // 4
            System.out.println("channel 的 position为：" + channel.position());
            // 创建一个大小22字节的Buffer
            ByteBuffer buffer = ByteBuffer.allocate(22);
            int read = channel.read(buffer); // 5 将文件内容写入buffer
            while (read != -1){// 是否读到文件尾部

                buffer.flip(); // 切换成读模式

                while (buffer.hasRemaining()){ // 是否读完
                    System.out.print((char) buffer.get());
                }
                System.out.println();

                buffer.clear(); // 清空buffer 并切换成写模式
                read = channel.read(buffer); // 将文件写入buffer
            }
            channel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```
输出结果：
```
文件大小为： 66
截取后的文件大小为： 44
channel 的 position为：44
channel 的 position为：0
GoodGoodStudyDayDayUp 
GoodGoodStudyDayDayUp 
```
该程序中使用了大多数`FileChannel`的方法
1. **channel.write(writeBuffer);**  
   可以发现程序中对buffer的写入用了一个循环，这是因为channel也是有容量的，如果一次性write缓冲区的内容可能会导致截断，并不能将缓冲区的内容全部写入到文件中
2. **channel.force(true);**  
   对于操作系统来说，文件的保存不是立刻就进行存储，为了性能会先保存到缓存中，等系统调度进而保存缓存。force是直接让系统将文件进行保存，其变量是个布尔值，用来设置是否一并保存元信息
3. **channel.truncate(44);**  
   truecate操作会讲文件进行截断，我们可以观察保存后的文件长度为66字节，截断后文件为44字节。
4. **channel.position(0);**  
   这个方法可以设置channel的position位置，不带参数可以获取position位置。可以看到打印前后position位置为44 和 0，值得注意的是，当channel在write后position会一直在文件尾，所以如果继续进行read操作将读不出数据
5. **int read = channel.read(buffer);**  
   read方法可以将文件的内容读出到buffer中，通过打印我们可以看到输出了文件的全部内容

### 两个Channel传输数据
FileChannel 提供了方法`transferTo`和`transferFrom`为两个channel进行数据传输，具体示例程序如下：

```Java
package com.ymplans.channel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * Channel传输例子
 *
 * @author Jos
 */
public class ChannelTransferExample {

    public static void main(String[] args) {
        try (RandomAccessFile fromFile = new RandomAccessFile("src/test/resources/from.txt", "r");
             RandomAccessFile toFile = new RandomAccessFile("src/test/resources/to.txt", "rw")){
            FileChannel fromFileChannel = fromFile.getChannel();
            FileChannel toFileChannel = toFile.getChannel();
            System.out.printf("from文件大小：%d, to文件大小： %d\n", fromFileChannel.size(), toFileChannel.size());
            fromFileChannel.transferTo(0, fromFileChannel.size(),toFileChannel);
            System.out.printf("from文件大小：%d, to文件大小： %d\n", fromFileChannel.size(), toFileChannel.size());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
```

## Scatter/Gather
`Java NIO`原生支持分散/集中，这两个功能被用于从`Channel`中读数据和写数据，它们可以方便的支持多部分传输数据。例如，如果一个消息有消息头和消息体，可以用不同的`Buffer`去传输它们，也能轻松的从不同的缓冲区中获取到消息。
### 分散读
分散读意思是将一个`Channel`中的数据读出到多个`Buffer`中。  
```Java
package com.ymplans.channel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 分散读例子
 *
 * @author Jos
 */
public class ScatterReadExample {

    public static void main(String[] args) {
        try (RandomAccessFile file = new RandomAccessFile("src/test/resources/ScatterRead.txt", "rw")){
            FileChannel channel = file.getChannel();
            ByteBuffer aBuffer = ByteBuffer.allocate(13);
            ByteBuffer bBuffer = ByteBuffer.allocate(8);
            channel.read(new ByteBuffer[]{aBuffer, bBuffer});
            printBuffer(aBuffer);
            printBuffer(bBuffer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printBuffer(ByteBuffer aBuffer) {
        aBuffer.flip();
        while (aBuffer.hasRemaining()){
            System.out.print((char) aBuffer.get());
        }
        System.out.println();
    }

}
```
代码中文件的内容是`GoodGoodStudyDayDayUp`一共是21个字符，所以初始化了13和8字节大小的`ByteBuffer`对象，让其刚好读取到`GoodGoodStudy`和`DayDayUP`，因为在`aBuffer`写满时，才会写入下一个缓存区`bBuffer`，所以分散读比较适用固定大小的消息读取。
### 集中写
集中写是将多个`Buffer`数据写入到一个`Channel`中。 
```Java
package com.ymplans.channel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;

/**
 * 集中写例子
 *
 * @author Jos
 */
public class GatheringWriteExample {

    public static void main(String[] args) {
        try (RandomAccessFile file = new RandomAccessFile("src/test/resources/GatheringWrite.txt", "rw")){
            FileChannel channel = file.getChannel();
            ByteBuffer aBuffer = ByteBuffer.allocate(20);
            ByteBuffer bBuffer = ByteBuffer.allocate(10);
            aBuffer.put("GoodGoodStudy".getBytes(StandardCharsets.UTF_8));
            bBuffer.put("DayDayUp".getBytes(StandardCharsets.UTF_8));
            // 切换到读模式
            aBuffer.flip();
            bBuffer.flip();
            channel.write(new ByteBuffer[]{aBuffer, bBuffer});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```
写入结果为`GoodGoodStudyDayDayUp`, 代码中进行了读模式切换，而且可以发现，我们初始化了共30字节的缓存区，但写入的内容只有21字节，这是因为写入的内容只包括`position`和`limit`之间的数据。所以集中写相较于分散读可以动态的写入数据。