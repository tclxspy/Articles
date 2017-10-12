# 固件#04--STM32L073RZ-Nucleo按键状态机 #

## 状态机是一种思想方法 ##

状态机由状态寄存器和组合逻辑电路构成，能够根据控制信号按照预先设定的状态进行状态转移，是协调相关信号动作、完成特定操作的控制中心。

状态机简写为FSM（Finite State Machine），主要分为2大类：

- 第一类，若输出只和状态有关而与输入无关，则称为Moore状态机
- 第二类，输出不仅和状态有关而且和输入有关系，则称为Mealy状态机

## 有限状态机 ##

是表示有限多个状态以及在这些状态之间转移和动作的数学模型。

思想广泛应用于硬件控制电路设计，也是软件上常用的一种处理方法(软 件上称为FMM--有限消息机)。它把复杂的控制逻辑分解成有限个稳定状态，在每个状态上判断事件，变连续处理为离散数字处理，符合计算机的工作特点。

同时，因为有限状态机具有有限个状态，所以可以在实际的工程上实现。**但这并不意味着其只能进行有限次的处理，相反，有限状态机是闭环系统，有限无穷，可以用有限的状态，处理无穷的事务。**

## 按键状态机 ##

按键的扫描方式主要有下面几种：


1. 死循环扫描方式：这种扫描方式，一般出现在大量的单片机或开发板的配套例程里面。这种方法最大的缺点就是占用CPU时间过长。当功能比较多的时候，就会造成**系统比较卡顿**的现象，使系统运行不流畅，造成的原因就是delay_ms(20)，因为在延时的20ms里面，CPU完全是死等在那里。

2. 中断方式：这种按键扫描方式一般是利用外部中断来实现，因为外部中断一般具有上升沿触发中断、下降沿触发中断、高电平触发中断、低电平触发中断等，外部中断检测到该信号后，进入外部中断处理程序，从而实现按键扫描。缺点是一般MCU的**外部中断口不多**。

3. ADC检测按键：这种方法一般是使用在具有ADC功能的单片机上，当IO口不足时，可以使用一个AD口来检测按键，每个按键按下时，AD采集到的数据不一样，从而来实现按键的检测。

4. 定时器定时扫描方式：这种方法和方法1有点类似，只是不会像方法1那样死等在那里消耗MCU运行时间，而是通过定时器扫描来实现。首先，MCU检测到按键后，定时器定时，达到一定时间后，继续扫描按键是否按下，然后计时按键按下的时间，当20ms以后按键还是按下状态，则执行按键操作，反之则认为是抖动。

## STM32L073RZ-Nucleo状态机实例 ##

以按键检测这个事件为例，可以把我们按按钮这个事件分成四个状态 ①松开态 ②抖动态 ③按下态 ④长按态.

![](https://i.imgur.com/K52Pbj5.jpg)

根据原理图，按键松开状态为1 按下为0. 每10ms检测一次按键电平，当前状态也随之改变（可能向同也可能变为另一状态）。 

```C

	/* 定义一个枚举类型列出该系统所有状态 */
	typedef enum
	{
	    NoKeyDown = 0,
	    KeySureDown=1 ,
	    OnceKeyDown=2,
	    ContiousKeyDown=3,
	}StateStatus;
```

main函数：

````c

	int main(void)
	{
	  HAL_Init();
	
	  /* Configure the system clock to 2 MHz */
	  SystemClock_Config();
	
	  /* Configure LED2 */
	  BSP_LED_Init(LED2);
	
	  BSP_PB_Init(BUTTON_USER, BUTTON_MODE_GPIO);  
	
	  /* Compute the prescaler value to have TIMx counter clock equal to 10000 Hz */
	  uwPrescalerValue = (uint32_t)(SystemCoreClock / 10000) - 1;//199
	
	  /* Set TIMx instance */
	  TimHandle.Instance = TIMx;
	
	  /* Initialize TIMx peripheral as follows:
	       + Period = 2500 - 1
	       + Prescaler = (SystemCoreClock/2500) - 1
	       + ClockDivision = 0
	       + Counter direction = Up
	  */
	  
	  //中断服务程序间隔时间为
	  //时间=1/频率×次数
	  //((1+TIM_Prescaler )/2M)*(1+TIM_Period )
	  //((1+199)/2M)*(1+99)=10ms  配置系统10ms产生一次中断
	  TimHandle.Init.Period            = 100 - 1;
	  TimHandle.Init.Prescaler         = uwPrescalerValue;
	  TimHandle.Init.ClockDivision     = 0;
	  TimHandle.Init.CounterMode       = TIM_COUNTERMODE_UP;
	
	  if (HAL_TIM_Base_Init(&TimHandle) != HAL_OK)
	  {
	    /* Initialization Error */
	    Error_Handler();
	  }
	
	  /*##-2- Start the TIM Base generation in interrupt mode ####################*/
	  /* Start Channel1 */
	  if (HAL_TIM_Base_Start_IT(&TimHandle) != HAL_OK)
	  {
	    /* Starting Error */
	    Error_Handler();
	  }
	
	  while (1)
	  {
	  }
	}
```

定时器中断函数(CPU每10ms调用一次中断服务程序，按键抖动一般为5~10ms 按一次按键的时间一般为20~30ms）:

```c

	void HAL_TIM_PeriodElapsedCallback(TIM_HandleTypeDef *htim)
	{  
	  uint32_t key=0;
	
	  key=ReadKeyStatus();
	
	   if(key == OnceKeyDown)
	   {
	      HAL_GPIO_WritePin(GPIOA,GPIO_PIN_5,GPIO_PIN_RESET);//短按灭灯灯
	   }
	   else if(key == ContiousKeyDown)
	   {
	      HAL_GPIO_WritePin(GPIOA,GPIO_PIN_5, GPIO_PIN_SET);//长按亮灯
	   }
	}
```

按键函数：

```C

	StateStatus ReadKeyStatus(void)
	{
	    static StateStatus state = NoKeyDown;
	    static int TimeCount = 0;
	    int KeyPress = HAL_GPIO_ReadPin(GPIOC,GPIO_PIN_13);   //读取按键输入,根据自己按键端口配置参数GPIOx，GPIO_Pin_n
	    StateStatus KeyReturn = NoKeyDown;
	
	
	    switch(state)
	    {
	        case NoKeyDown:
	            if(!KeyPress)
	            {
	                state = KeySureDown;
	            }
	            break;
	
	        case KeySureDown:    // 确认消抖
	            if(!KeyPress)
	            {
	                state = OnceKeyDown;
	                TimeCount = 0;
	            }
	            else
	            {
	                state = NoKeyDown;
	            }
	            break;
	
	        case OnceKeyDown:
	            if(KeyPress)    //如果按键松开，则认为是正常按下
	            {
	                state = NoKeyDown;
	                KeyReturn = OnceKeyDown;
	
	            }
	            /* 如果按键1s没松开则认为是长按状态 */
	            else if(++TimeCount >= 100)
	            {
	                state = ContiousKeyDown;
	                TimeCount = 0;
	                KeyReturn = ContiousKeyDown;
	            }
	            break;
	
	        case ContiousKeyDown:
	            if(KeyPress)
	            {
	                state = NoKeyDown;
	                KeyReturn = NoKeyDown;
	            }
	            /* 按键持续状态0.5s */
	            else if(++TimeCount>50)
	            {
	                KeyReturn = ContiousKeyDown;
	                TimeCount = 0;
	
	            }
	            else
	            {
	                KeyReturn = NoKeyDown;
	            }
	            break;
	    }
	    return KeyReturn;     
	}
```