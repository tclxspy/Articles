# 固件#09--STM32-MCU-GPIO外部中断#
## 三种触发方式 ##
前面有介绍到GPIO的四种输入四种输出工作模式。这里讲介绍GPIO的中断模式：

![](https://i.imgur.com/flwUVzk.jpg)

分别是上升沿触发、下降沿触发以及上升/下降沿触发。

## 配置GPIO为中断模式 ##

![](https://i.imgur.com/zBSDXPL.jpg)

因为用的是PIN9、PIN10和PIN11，所以对应的是EXTI4-15-IRQn。

![](https://i.imgur.com/zMhR5M7.jpg)

## 中断函数 ##

这样当相应的PIN电平变化的时候，就会触发中断函数。

![](https://i.imgur.com/Mmkfjg9.jpg)

![](https://i.imgur.com/lld5kwh.jpg)