# 固件#10--STM32-MCU-TIMER中断#
## 讲解 ##
![](https://i.imgur.com/IGH8w2r.jpg)

    In this example TIM2 input clock (TIM2CLK) is set to APB1 clock (PCLK1),
    since APB1 prescaler is equal to 1, TIM2CLK = PCLK1 = HCLK = SystemCoreClock/2

系统时钟为32 MHz，此时得到TIM2时钟为16 MHz。
    
如果预分频值为16，那么预分频寄存器为15,TIM2计数时钟频率为16M/（15+1）=1 MHz。
![](https://i.imgur.com/weQVA4l.jpg)

如果从0~999数1000个数，那么需要的时间是（1+999）/1 MHz=1ms，也就是1ms触发一次中断。

## 设置1ms触发1次中断 ##

要设置为1ms触发一次，那么正向来推算Period和Prescaler寄存器的值。

    To get TIM2 counter clock at 1 MHz, the timer Prescaler is computed as follow :
    Prescaler = (TIM2CLK / TIM2 counter clock) - 1
    Prescaler = (SystemCoreClock/2/1MHz) - 1
    Prescaler = 15

因为TIM2 counter clock at 1 MHz，也就是需要数1000个数，才能有（1+999）/1 MHz=1ms。所以Period=999。

## 通用公式 ##
这里是时钟非系统时钟，如上例所示。

![](https://i.imgur.com/haUpQSV.jpg)