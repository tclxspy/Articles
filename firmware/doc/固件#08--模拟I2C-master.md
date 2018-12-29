# 固件#08--模拟I2C-master #

# 首先初始化GPIO #

![](https://i.imgur.com/1Cifo2p.jpg)

# 然后定义一些宏 #

![](https://i.imgur.com/pavJoPn.jpg)

# 接着定义一些分离的I2C函数 #
## I2C start ##

- 开始信号：SCL为高电平时，SDA由高电平向低电平跳变，开始传送数据。

![](https://i.imgur.com/KzFqHb4.jpg)

## I2C stop ##
- 结束信号：SCL为高电平时，SDA由低电平向高电平跳变，结束传送数据。

![](https://i.imgur.com/nHx2keq.jpg)

## I2C wait ACK ##

Master每发送完8bit数据后等待Slave的ACK。
即在第9个clock，若从IC发ACK，SDA会被拉低。
若没有ACK，SDA会被置高，这会引起Master发生RESTART或STOP流程。

![](https://i.imgur.com/RsfdfVq.jpg)

## I2C send ACK ##

![](https://i.imgur.com/ZO3kAst.jpg)

## I2C send NoACK ##

![](https://i.imgur.com/7KT06PS.jpg)

## I2C send one byte ##

![](https://i.imgur.com/Qkuwsm8.jpg)

## I2C receive one byte ##

![](https://i.imgur.com/I9KLage.jpg)

# 最后定义合起来的I2C函数 #
## 合起来I2C_write##

![](https://i.imgur.com/18Y0Nv2.jpg)

1. Master发起START
2. Master发送I2C addr（7bit）和w操作0（1bit），等待ACK
3. Slave发送ACK
4. Master发送reg addr_MSB（高8bit），等待ACK
5. Slave发送ACK
6. Master发送reg addr_LSB（低8bit），等待ACK
7. Slave发送ACK
8. Master发送data（8bit），即要写入寄存器中的数据，等待ACK
9. Slave发送ACK
10. 第8步和第9步可以重复多次，即顺序写多个寄存器
11. Master发起STOP

![](https://i.imgur.com/iLNDnmK.jpg)

## 合起来I2C_read##

![](https://i.imgur.com/98uIevy.jpg)

1.    Master发送I2C addr（7bit）和w操作0（1bit），等待ACK
2.    Slave发送ACK
3.    Master发送reg addr_MSB（高8bit），等待ACK
4.    Slave发送ACK
5.    Master发送reg addr_LSB（低8bit），等待ACK
6.    Slave发送ACK
7.    Master发起START
8.    Master发送I2C addr（7bit）和r操作1（1bit），等待ACK
9.    Slave发送ACK
10.   Slave发送data（8bit），即寄存器里的值
11.   Master发送ACK。或者Master发送NACK，要求结束。
12.   第10步和第11步可以重复多次，即顺序读多个寄存器

![](https://i.imgur.com/NUWbfzw.jpg)

## 合起来I2C_write data check byte##

写完check。

![](https://i.imgur.com/Lz5Wxq5.jpg)

**使用时，用这两个函数就可以了：合起来I2C-read和合起来I2C-write**