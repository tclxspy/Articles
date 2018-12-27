# 固件#07--STM32-MCU-ADC编程#

## ADC简介##

12 位 ADC 是逐次趋近型模数转换器。它具有多达 19 个复用通道，可测量来自 16 个外部源
和 3 个内部源的信号。各种不同通道的 A/D 转换可在单次、连续、扫描或不连续采样模式下
进行。ADC 的结果存储在一个左对齐或右对齐的 16 位数据寄存器中。

ADC 具有模拟看门狗特性，允许应用检测输入电压是否超过了用户自定义的阈值上限或
下限。

采用了高效的低功耗模式，在低频下可实现极低的功耗。

内置硬件过采样器，可提高模拟性能，同时还能减轻 CPU 进行相关计算的负担。

## ADC主要特性##

![](https://i.imgur.com/qnjKm3Z.jpg)

# 编程步骤 #
## 初始化ADC ##

配置Clock, Resolution, Data Alignment and number of conversion等等。

![](https://i.imgur.com/l6RKZjN.jpg)

## 初始化GPIO ##

配置GPIO为模拟输入模式。

![](https://i.imgur.com/gRkY5gO.jpg)

## 配置ADC功能 ##

1. 设置为连续转换为模式
2. 选择转换通道
3. 设置采样时间
4. 使能VREFINT、温度传感器、ADC分频系数
5. 使能ADC
6. 等待ADRDY置1
7. 开启ADC转换
![](https://i.imgur.com/DT6d6NA.jpg)

## 读取ADC ##

前4次不算精确，放弃。取后面8次的平均值。

![](https://i.imgur.com/Kga6mnx.jpg)

# 附：使能ADC #
![](https://i.imgur.com/SKpEaYq.jpg)