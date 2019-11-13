# PID和APC#
## 通过PID算法调TEC ##

比如让TEC工作点稳定在25°C，那么目标值就是25°C时的tempADC，假设为tempADC-Target。因为是闭环，反馈值设为tempADC-Feedback。

那么PID算法，每次设置TEC的DAC值，由如下公式计算出：

duty-DAC = Coeff-P * e + Coeff-I * (e + e1 + e2) + Coeff-D * (e - e1).

其中差值e = tempADC-Feedback - tempADC-Target， e1为上一次的e， e2为上上次的e。

![](https://i.imgur.com/vISSzrV.png)

## 光功率的APC控制 ##

APC控制分硬件实现和软件实现。

**硬件实现，**就是芯片内部实现了，用户只需要配置相应的寄存器即可。

![](https://i.imgur.com/ueBYvgC.jpg)

**软件实现**，类似PID算法。

比如让发出2dBm的光，可以得到该光对应的MPD的ADC，为目标值。因为是闭环，反馈值为MPD的ADC。有了目标值和反馈值，就可以跟进上述公式计算得出，每次调的bias，mod电流DAC。