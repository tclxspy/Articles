# 原理 #

## 简介 ##

SPI(Serial Peripheral Interface--串行外设接口)总线系统是一种同步串行外设接口，它可以使MCU与各种外围设备以串行方式进行通信以交换信息。

- 主模式或从模式操作
- 基于三条线的全双工同步传输
- 基于双线的半双工同步传输，其中一条可作为双向数据线
- 基于双线的单工同步传输，其中一条可作为单向数据线
- 8 位到 16 位传输帧格式选

四个 I/O 引脚专用于与外部器件进行 SPI 通信。

- MISO：主输入/ 从输出数据。通常情况下，此引脚用于在从模式下发送数据和在主模式下接收数据。
- MOSI：主输出/ 从输入数据。通常情况下，此引脚用于在主模式下发送数据和在从模式下接收数据。
- SCK：SPI 主器件的串行时钟输出引脚以及 SPI 从器件的串行时钟输入引脚。
- NSS：从器件选择引脚。根据 SPI 和 NSS 设置，该引脚可用于：选择单个从器件以进行通信、同步数据帧或、检测多个主器件之间是否存在冲突。

![](http://i.imgur.com/lGdCmkG.jpg)

## 写操作 ##

下降沿数据有效。

![](http://i.imgur.com/wukYanY.jpg)

## 读操作 ##

![](http://i.imgur.com/HXGcfst.jpg)

# 代码 #

演示对芯片AD5317R的读写操作。

![](http://i.imgur.com/UatvMrV.jpg)

命令0011允许用户写入DAC寄存器并直接更新DAC输出。

例如，要回读通道A的DAC寄存器，应当实施如下操作
序列：

1. 将0x900000写入AD5317R输入寄存器。这会将器件配置为读取模式，同时选中通道A的DAC寄存器。注意，从DB15至DB0的所有数据位都是无关位。
2. 然后执行第二个写操作，写入NOP条件0x000000。在此写入期间，来自寄存器的数据在SDO线路上逐个输出。DB23至DB20包含未定义的数据，后16位则包含DB19至DB4 DAC寄存器内容。


## spi.h ##

```c

	#ifndef __SPI_H
	#define __SPI_H
	
	/* Includes ------------------------------------------------------------------*/
	#include "stm32l0xx_hal.h"
	
	#define SPI_SCK_PIN                     GPIO_PIN_4
	#define SPI_SCK_GPIO_PORT               GPIOC
	#define SPI_MOSI_PIN                    GPIO_PIN_7
	#define SPI_MOSI_GPIO_PORT              GPIOC
	#define SPI_MISO_PIN                    GPIO_PIN_8
	#define SPI_MISO_GPIO_PORT              GPIOC
	#define SPI_NSS_PIN                     GPIO_PIN_6
	#define SPI_NSS_GPIO_PORT               GPIOC
	 
	#define SPI_SCK_GPIO_CLK_ENABLE()       __HAL_RCC_GPIOC_CLK_ENABLE()
	#define SPI_MISO_GPIO_CLK_ENABLE()      __HAL_RCC_GPIOC_CLK_ENABLE()
	#define SPI_MOSI_GPIO_CLK_ENABLE()      __HAL_RCC_GPIOC_CLK_ENABLE()
	#define SPI_NSS_GPIO_CLK_ENABLE()       __HAL_RCC_GPIOC_CLK_ENABLE()
	
	
	#define MOSI_H  HAL_GPIO_WritePin(SPI_MOSI_GPIO_PORT, SPI_MOSI_PIN, GPIO_PIN_SET)  
	#define MOSI_L  HAL_GPIO_WritePin(SPI_MOSI_GPIO_PORT, SPI_MOSI_PIN, GPIO_PIN_RESET)  
	#define SCK_H   HAL_GPIO_WritePin(SPI_SCK_GPIO_PORT, SPI_SCK_PIN, GPIO_PIN_SET)  
	#define SCK_L   HAL_GPIO_WritePin(SPI_SCK_GPIO_PORT, SPI_SCK_PIN, GPIO_PIN_RESET)  
	#define MISO    HAL_GPIO_ReadPin(SPI_MISO_GPIO_PORT, SPI_MISO_PIN) 
	#define NSS_H   HAL_GPIO_WritePin(SPI_NSS_GPIO_PORT, SPI_NSS_PIN, GPIO_PIN_SET)  
	#define NSS_L   HAL_GPIO_WritePin(SPI_NSS_GPIO_PORT, SPI_NSS_PIN, GPIO_PIN_RESET) 
	
	
	#define EEPROM_DAC_START_ADDR   DATA_EEPROM_BASE+6             /* Start @ of user EEPROM area */
	#define EEPROM_DAC_END_ADDR     (EEPROM_BIAS_START_ADDR + 7)   /* End @ of user EEPROM area */
	#define IS_DAC_DATA_ADDRESS(__ADDRESS__)          (((__ADDRESS__) >= EEPROM_DAC_START_ADDR) && ((__ADDRESS__) <= EEPROM_DAC_END_ADDR))
	
	void SPI_Init(void);
	uint16_t SPI_ReadWrite_Byte(uint8_t cmd_addr, uint16_t data);
	void AD5317R_DAC_Write(uint8_t channel, uint16_t data);
	uint16_t AD5317R_DAC_Read(uint8_t channel);
	void AD5317R_DAC_Disable(uint8_t channel);
	void AD5317R_DAC_Enable(uint8_t channel);
	#endif
```

## spi.c ##

```c

	#include "spi.h"
	#include "main.h"
	#include "stm32l0xx_hal.h"
	
	void SPI_Init(void)
	{  
	  /*##-1- Enable peripherals and GPIO Clocks #################################*/
	  /* Enable GPIO TX/RX clock */
	  SPI_SCK_GPIO_CLK_ENABLE();
	  SPI_MISO_GPIO_CLK_ENABLE();
	  SPI_MOSI_GPIO_CLK_ENABLE();
	  SPI_NSS_GPIO_CLK_ENABLE();
	
	  /*##-2- Configure peripheral GPIO ##########################################*/
	  /* SPI SCK GPIO pin configuration  */
	  GPIO_InitTypeDef GPIO_InitStruct;
	  
	  GPIO_InitStruct.Pin       = SPI_SCK_PIN;
	  GPIO_InitStruct.Mode      = GPIO_MODE_OUTPUT_PP;
	  //GPIO_InitStruct.Pull      = GPIO_PULLDOWN;
	  GPIO_InitStruct.Speed     = GPIO_SPEED_FREQ_VERY_HIGH;
	  HAL_GPIO_Init(SPI_SCK_GPIO_PORT, &GPIO_InitStruct);
	  HAL_GPIO_WritePin(SPI_SCK_GPIO_PORT, SPI_SCK_PIN, GPIO_PIN_SET);
	
	  /* SPI MISO GPIO pin configuration  */
	  GPIO_InitStruct.Pin = SPI_MISO_PIN;
	  GPIO_InitStruct.Mode = GPIO_MODE_INPUT;
	  HAL_GPIO_Init(SPI_MISO_GPIO_PORT, &GPIO_InitStruct);
	
	  /* SPI MOSI GPIO pin configuration  */
	  GPIO_InitStruct.Pin = SPI_MOSI_PIN;
	  GPIO_InitStruct.Mode = GPIO_MODE_OUTPUT_PP;
	  HAL_GPIO_Init(SPI_MOSI_GPIO_PORT, &GPIO_InitStruct);
	  HAL_GPIO_WritePin(SPI_MOSI_GPIO_PORT, SPI_MOSI_PIN, GPIO_PIN_SET);
	  
	  GPIO_InitStruct.Pin = SPI_NSS_PIN;
	  GPIO_InitStruct.Mode = GPIO_MODE_OUTPUT_PP;
	  HAL_GPIO_Init(SPI_NSS_GPIO_PORT, &GPIO_InitStruct);
	  HAL_GPIO_WritePin(SPI_NSS_GPIO_PORT, SPI_NSS_PIN, GPIO_PIN_SET);
	}
	
	uint16_t SPI_ReadWrite_Byte(uint8_t cmd_addr, uint16_t data)
	{  
	  uint16_t temp=0;
	  NSS_L;  
	  SCK_H;    
	  __NOP();             /*读取第一bit数据 等待数据稳定 根据实际时钟调整*/
	  for(uint8_t i=0;i<8;i++)
	  {    
	    if(cmd_addr&0x80) 
	    {
	      MOSI_H;          /*若最高位为高，则输出高*/
	    }
	    else
	    {
	      MOSI_L;          /*若最高位为低，则输出低*/    
	    }    
	    __NOP();
	    cmd_addr <<= 1;
	    SCK_L;
	    temp <<= 1;        /*数据左移*/
	    if(MISO)
	    {
	      temp++;          /*若从从机接收到高电平，数据自加一*/
	    }
	    SCK_H;
	  }
	  
	  data <<= 6;
	  
	  for(uint8_t i=0;i<16;i++)
	  {    
	    if(data&0x8000) 
	    {
	      MOSI_H;          /*若最高位为高，则输出高*/
	    }
	    else
	    {
	      MOSI_L;          /*若最高位为低，则输出低*/    
	    }    
	    __NOP();
	    data <<= 1;
	    SCK_L;
	    temp <<= 1;        /*数据左移*/
	    if(MISO)
	    {
	      temp++;          /*若从从机接收到高电平，数据自加一*/
	    }
	    SCK_H;
	  }
	  NSS_H;
	  return temp;
	}
	
	void AD5317R_DAC_Disable(uint8_t channel)
	{
	  uint8_t data = *(__IO uint8_t *)EEPROM_DAC_START_ADDR; 
	  data= data+ 1<<((channel-1)*2);
	  SPI_ReadWrite_Byte(0x40, data);
	  
	  HAL_FLASHEx_DATAEEPROM_Unlock();
	  HAL_FLASHEx_DATAEEPROM_Program(FLASH_TYPEPROGRAMDATA_BYTE, EEPROM_DAC_START_ADDR, data);
	  HAL_FLASHEx_DATAEEPROM_Lock();
	}
	
	void AD5317R_DAC_Enable(uint8_t channel)
	{
	  uint8_t data = *(__IO uint8_t *)EEPROM_DAC_START_ADDR;  
	  data= data+ 0<<((channel-1)*2);
	  SPI_ReadWrite_Byte(0x40, data);
	  
	  HAL_FLASHEx_DATAEEPROM_Unlock();
	  HAL_FLASHEx_DATAEEPROM_Program(FLASH_TYPEPROGRAMDATA_BYTE, EEPROM_DAC_START_ADDR, data);
	  HAL_FLASHEx_DATAEEPROM_Lock();
	}
	
	void AD5317R_DAC_Write(uint8_t channel, uint16_t data)
	{
	  uint8_t cmd_addr = 0x30+(1<<((channel-1)*2));
	  SPI_ReadWrite_Byte(cmd_addr, data);
	}
	
	uint16_t AD5317R_DAC_Read(uint8_t channel)
	{
	  uint8_t cmd_addr = 0x90+(1<<((channel-1)*2));
	  SPI_ReadWrite_Byte(cmd_addr, 0x0000);
	  
	  uint16_t data = SPI_ReadWrite_Byte(0x00, 0x0000);
	  return data>>4;
	}
```