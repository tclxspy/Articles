# I2C #

## 简介 ##

I2C总线是由Philips公司开发的一种简单、双向二线制同步串行总线。SDA（串行数据线）和SCL（串行时钟线）都是双向I/O线，接口电路为开漏输出．需通过上拉电阻接电源VCC.

![](http://i.imgur.com/8ZsnW47.png)

I2C（内部集成电路）总线接口处理微控制器与串行 I2C 总线间的通信。它提供多主模式功
能，可以控制所有 I2C 总线特定的序列、协议、仲裁和时序。它支持标准模式 (Sm)、快速模
式 (Fm) 和超快速模式 (Fm+)。

-  从模式和主模式
-  多主模式功能
-  标准速度模式（高达 100 kHz）
-  快速模式（高达 400 kHz）
-  超快速模式（高达 1 MHz）
-  7 位和 10 位寻址模式


## I2C start/stop ##

![](http://i.imgur.com/rJSLMHs.png)

- 开始信号：SCL为高电平时，SDA由高电平向低电平跳变，开始传送数据。
- 结束信号：SCL为高电平时，SDA由低电平向高电平跳变，结束传送数据。

## I2C read ##

![](http://i.imgur.com/VQUCsb2.jpg)

1.    Master发送I2C addr（7bit）和w操作0（1bit），等待ACK
2.    Slave发送ACK
3.    Master发送reg addr（8bit），等待ACK
4.    Slave发送ACK
5.    Master发起START
6.    Master发送I2C addr（7bit）和r操作1（1bit），等待ACK
7.    Slave发送ACK
8.    Slave发送data（8bit），即寄存器里的值
9.    Master发送ACK。或者Master发送NACK，要求结束。
10.   第8步和第9步可以重复多次，即顺序读多个寄存器

## I2C write ##

![](http://i.imgur.com/dI1atUh.jpg)

1. Master发起START
2. Master发送I2C addr（7bit）和w操作0（1bit），等待ACK
3. Slave发送ACK
4. Master发送reg addr（8bit），等待ACK
5. Slave发送ACK
6. Master发送data（8bit），即要写入寄存器中的数据，等待ACK
7. Slave发送ACK
8. 第6步和第7步可以重复多次，即顺序写多个寄存器
9. Master发起STOP

## 例子 ##

I2C signal diagram: SDA(green) and SCL(yellow)

			write 0x55 DAC 

![](http://i.imgur.com/stWtVq5.png)

# USB to I2C #

通过USB虚拟串口实现PC与I2C设备进行通信。使用STM32L073xx MCU，硬件I2C和软件模拟I2C，用看门狗检测运行状态。

底层firmware实现代码和硬件电路图见链接：

- 此时I2C是做主机-->[https://github.com/tclxspy/USB_I2C_73](https://github.com/tclxspy/USB_I2C_73)
- 另附上I2C做从机的代码-->[https://github.com/tclxspy/QSFP28_PSM4](https://github.com/tclxspy/QSFP28_PSM4)

## USB安装成功： ##

![](http://i.imgur.com/1Y8jEYJ.jpg)

## 串口助手调试 ##

![](http://i.imgur.com/bnKzhsI.jpg)

## 部分代码main.c ##

```c

	#include "main.h"
	#include "stm32l0xx_hal.h"
	#include "usb_device.h"
	#include "i2c.h"
	#include "soft_i2c.h"
	#include "stm32l0xx_nucleo.h"
	#include "usbd_cdc_if.h"
	#include "modsel.h"
	
	/* Buffer used for transmission */
	//uint8_t aTxBuffer[] = {0x33, 0x34};
	//uint8_t aTxBuffer[] = {0x46, 0x54};//{"FT"}
	uint8_t aTxBuffer[RXBUFFERSIZE];
	
	/* Buffer used for reception */
	uint8_t aRxBuffer[RXBUFFERSIZE];
	__IO uint16_t hTxNumData = 0;
	__IO uint16_t hRxNumData = 0;
	uint8_t bTransferRequest = 0;
	
	/* USER CODE BEGIN Includes */
	
	/* USER CODE END Includes */
	
	/* Private variables ---------------------------------------------------------*/
	I2C_HandleTypeDef I2cHandle;
	IWDG_HandleTypeDef hiwdg;
	
	/* USER CODE BEGIN PV */
	/* Private variables ---------------------------------------------------------*/
	
	/* USER CODE END PV */
	
	/* Private function prototypes -----------------------------------------------*/
	void SystemClock_Config(void);
	static void MX_GPIO_Init(void);
	static void MX_IWDG_Init(void);
	
	/* USER CODE BEGIN PFP */
	/* Private function prototypes -----------------------------------------------*/
	
	/* USER CODE END PFP */
	
	/* USER CODE BEGIN 0 */
	
	/* USER CODE END 0 */
	
	int main(void)
	{
	  /* USER CODE BEGIN 1 */
	
	  /* USER CODE END 1 */
	
	  /* MCU Configuration----------------------------------------------------------*/
	
	  /* Reset of all peripherals, Initializes the Flash interface and the Systick. */
	  HAL_Init();
	
	  /* USER CODE BEGIN Init */
	
	  /* USER CODE END Init */
	
	  /* Configure the system clock */
	  SystemClock_Config();
	  
	  /* USER CODE BEGIN SysInit */
	
	  /* USER CODE END SysInit */
	
	  /* Initialize all configured peripherals */
	  MX_GPIO_Init();
	  MODSELL_Init();
	  /* I2C2 init function */ 
	#ifdef HARDWARE_I2C  
	  I2C_Init();  
	#else 
	  Soft_I2C_Init();
	#endif /* HARDWARE_I2C */
	  
	  MX_IWDG_Init();
	  MX_USB_DEVICE_Init();
	  
	  uint8_t len;
	  uint8_t t;
	  uint8_t deviceAddress;
	  uint8_t regAddress;
	  uint8_t length;
	  uint8_t read_write;
	  
	#ifdef HARDWARE_I2C
	  while(1)
	  {
	    if(USB_USART_RX_STA&0x8000)
	    {
	      read_write = USB_USART_RX_Buffer[3];
	      deviceAddress = USB_USART_RX_Buffer[4];
	      regAddress = USB_USART_RX_Buffer[5];
	      length = USB_USART_RX_Buffer[6];
	      
	      len = USB_USART_RX_STA&0x3FFF;
	      USB_USART_RX_STA=0;
	      
	      for(t=0;t<len-8;t++)
	      {
	        aTxBuffer[t]=USB_USART_RX_Buffer[t+8];
	      }
	      
	      if(read_write==MASTER_REQ_READ)
	      {
	        do
	        {
	          if(HAL_I2C_Mem_Read_IT(&I2cHandle, (uint16_t)deviceAddress, regAddress, I2C_MEMADD_SIZE_8BIT, (uint8_t*)aRxBuffer, length)!= HAL_OK)
	          {
	            /* Error_Handler() function is called when error occurs. */
	            Error_Handler();
	          }          
	          while (HAL_I2C_GetState(&I2cHandle) != HAL_I2C_STATE_READY){}
	        }
	        while(HAL_I2C_GetError(&I2cHandle) == HAL_I2C_ERROR_AF);
	        
	        CDC_Transmit_FS(aRxBuffer, length);       
	      }
	      
	      if(read_write==MASTER_REQ_WRITE)
	      {
	        do
	        {
	          if(HAL_I2C_Mem_Write_IT(&I2cHandle, (uint16_t)deviceAddress, regAddress, I2C_MEMADD_SIZE_8BIT, (uint8_t*)&aTxBuffer, len-8)!= HAL_OK)
	          {
	            /* Error_Handler() function is called when error occurs. */
	            Error_Handler();
	          }      
	          while (HAL_I2C_GetState(&I2cHandle) != HAL_I2C_STATE_READY){}
	        }
	        while(HAL_I2C_GetError(&I2cHandle) == HAL_I2C_ERROR_AF);
	      }
	    }   
	    
	    
	    /* Refresh IWDG: reload counter */
	    if(HAL_IWDG_Refresh(&hiwdg) != HAL_OK)
	    {
	      /* Refresh Error */
	      Error_Handler();
	    }
	  }
	#else
	while(1)
	  { 
	    if(USB_USART_RX_STA&0x8000)
	    { 
	      read_write = USB_USART_RX_Buffer[3];
	      deviceAddress = USB_USART_RX_Buffer[4];
	      regAddress = USB_USART_RX_Buffer[5];
	      length = USB_USART_RX_Buffer[6];
	      
	      len = USB_USART_RX_STA&0x3FFF;
	      USB_USART_RX_STA=0;
	      
	      for(t=0;t<len-8;t++)
	      {
	        aTxBuffer[t]=USB_USART_RX_Buffer[t+8];
	      }
	      
	      if(read_write==MASTER_REQ_READ)
	      {        
	        Soft_I2C_Read_Reg(deviceAddress, regAddress, aRxBuffer, length);       
	        CDC_Transmit_FS(aRxBuffer, length);
	      }  
	      
	      if(read_write==MASTER_REQ_WRITE)
	      {  
	        Soft_I2C_Send_Reg(deviceAddress, regAddress, aTxBuffer, len-8);
	      }
	    }
	    
	    /* Refresh IWDG: reload counter */
	    if(HAL_IWDG_Refresh(&hiwdg) != HAL_OK)
	    {
	      /* Refresh Error */
	      Error_Handler();
	    }
	  }
	#endif /* HARDWARE_I2C */
	}
	
	/** System Clock Configuration
	*/
	void SystemClock_Config(void)
	{
	  RCC_OscInitTypeDef RCC_OscInitStruct;
	  RCC_ClkInitTypeDef RCC_ClkInitStruct;
	  RCC_PeriphCLKInitTypeDef PeriphClkInit;
	
	    /**Configure the main internal regulator output voltage 
	    */
	  __HAL_PWR_VOLTAGESCALING_CONFIG(PWR_REGULATOR_VOLTAGE_SCALE1);
	
	    /**Initializes the CPU, AHB and APB busses clocks 
	    */
	  RCC_OscInitStruct.OscillatorType = RCC_OSCILLATORTYPE_HSI|RCC_OSCILLATORTYPE_HSI48;
	  RCC_OscInitStruct.HSIState = RCC_HSI_ON;
	  RCC_OscInitStruct.HSICalibrationValue = 16;
	  RCC_OscInitStruct.HSI48State = RCC_HSI48_ON;
	  RCC_OscInitStruct.PLL.PLLState = RCC_PLL_ON;
	  RCC_OscInitStruct.PLL.PLLSource = RCC_PLLSOURCE_HSI;
	  RCC_OscInitStruct.PLL.PLLMUL = RCC_PLLMUL_4;
	  RCC_OscInitStruct.PLL.PLLDIV = RCC_PLLDIV_2;
	  if (HAL_RCC_OscConfig(&RCC_OscInitStruct) != HAL_OK)
	  {
	    _Error_Handler(__FILE__, __LINE__);
	  }
	
	    /**Initializes the CPU, AHB and APB busses clocks 
	    */
	  RCC_ClkInitStruct.ClockType = RCC_CLOCKTYPE_HCLK|RCC_CLOCKTYPE_SYSCLK
	                              |RCC_CLOCKTYPE_PCLK1|RCC_CLOCKTYPE_PCLK2;
	  RCC_ClkInitStruct.SYSCLKSource = RCC_SYSCLKSOURCE_PLLCLK;
	  RCC_ClkInitStruct.AHBCLKDivider = RCC_SYSCLK_DIV1;
	  RCC_ClkInitStruct.APB1CLKDivider = RCC_HCLK_DIV1;
	  RCC_ClkInitStruct.APB2CLKDivider = RCC_HCLK_DIV1;
	
	  if (HAL_RCC_ClockConfig(&RCC_ClkInitStruct, FLASH_LATENCY_1) != HAL_OK)
	  {
	    _Error_Handler(__FILE__, __LINE__);
	  }
	
	  PeriphClkInit.PeriphClockSelection = RCC_PERIPHCLK_USB;
	  PeriphClkInit.UsbClockSelection = RCC_USBCLKSOURCE_HSI48;
	  if (HAL_RCCEx_PeriphCLKConfig(&PeriphClkInit) != HAL_OK)
	  {
	    _Error_Handler(__FILE__, __LINE__);
	  }
	
	    /**Configure the Systick interrupt time 
	    */
	  HAL_SYSTICK_Config(HAL_RCC_GetHCLKFreq()/1000);
	
	    /**Configure the Systick 
	    */
	  HAL_SYSTICK_CLKSourceConfig(SYSTICK_CLKSOURCE_HCLK);
	
	  /* SysTick_IRQn interrupt configuration */
	  HAL_NVIC_SetPriority(SysTick_IRQn, 0, 0);
	}
	
	
	
	/** Pinout Configuration
	*/
	static void MX_GPIO_Init(void)
	{
	  /* GPIO Ports Clock Enable */
	  //__HAL_RCC_GPIOB_CLK_ENABLE();
	  __HAL_RCC_GPIOA_CLK_ENABLE();
	}
	
	static void MX_IWDG_Init(void)
	{
	  hiwdg.Instance = IWDG;
	  hiwdg.Init.Prescaler = IWDG_PRESCALER_64;
	  hiwdg.Init.Window = 0xfff;
	  hiwdg.Init.Reload = 0xfff;
	  if (HAL_IWDG_Init(&hiwdg) != HAL_OK)
	  {
	    _Error_Handler(__FILE__, __LINE__);
	  }
	}
	
	/**
	  * @brief  This function is executed in case of error occurrence.
	  * @param  None
	  * @retval None
	  */
	void _Error_Handler(char * file, int line)
	{
	  /* USER CODE BEGIN Error_Handler_Debug */
	  /* User can add his own implementation to report the HAL error return state */
	  while(1) 
	  {
	  }
	  /* USER CODE END Error_Handler_Debug */ 
	}
	
	#ifdef USE_FULL_ASSERT
	
	/**
	   * @brief Reports the name of the source file and the source line number
	   * where the assert_param error has occurred.
	   * @param file: pointer to the source file name
	   * @param line: assert_param error line source number
	   * @retval None
	   */
	void assert_failed(uint8_t* file, uint32_t line)
	{
	  /* USER CODE BEGIN 6 */
	  /* User can add his own implementation to report the file name and line number,
	    ex: printf("Wrong parameters value: file %s on line %d\r\n", file, line) */
	  /* USER CODE END 6 */
	
	}
	#endif
```