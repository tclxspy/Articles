# 固件#06--STM32 MCU GPIO工作模式#

## 总结： 四种输入四种输出##

1. 浮空输入：GPIO-IN-FLOATING 

	> 可以做KEY识别，RX1

2. 上拉输入：GPIO-IPU

	> IO内部上拉电阻输入，默认是高电平 

3. 下拉输入：GPIO-IPD

	> IO内部下拉电阻输入，默认低高电平

4. 模拟输入：GPIO-AIN 

	> 应用ADC模拟输入，或者低功耗下省电

5. 开漏输出：GPIO-OUT-OD

	> IO输出0接GND，IO输出1，悬空，需要外接上拉电阻，才能实现输出高电平。当输出为1时，IO口的状态由上拉电阻拉高电平，但由于是开漏输出模式，这样IO口也就可以由外部电路改变为低电平或不变。可以读IO输入电平变化，实现的**IO双向功能**

6. 推挽输出：GPIO-OUT-PP

	> IO输出0-接GND， IO输出1 -接VCC，**读输入值是未知的**

7. 复用功能的推挽输出：GPIO-AF-PP
	
	> 片内外设功能

8. 复用功能的开漏输出：GPIO-AF-OD

	> 片内外设功能(I2C，TX1,MOSI,MISO.SCK.SS)


关于推挽输出和开漏输出，最后用一幅最简单的图形来概括：该图中左边的便是推挽输出模式，其中比较器输出高电平时下面的PNP三极管截止，而上面NPN三极管导通，输出电平VS+;当比较器输出低电平时则恰恰相反，PNP三极管导通，输出和地相连，为低电平。右边的则可以理解为开漏输出形式，需要接上拉。

![](https://i.imgur.com/lowPTqT.jpg)

## 输出高低电平 ##

推挽输出：GPIO-OUT-PP

![](https://i.imgur.com/md674RJ.jpg)

## 模拟I2C master ##

开漏输出：GPIO-OUT-OD

![](https://i.imgur.com/OZ3ShYc.jpg)

## 硬件I2C1 ##

复用功能的开漏输出：GPIO-AF-OD

![](https://i.imgur.com/32lxmlk.jpg)