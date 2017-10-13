#ifndef __LED_DRIVER_H
#define __LED_DRIVER_H

/* Includes ------------------------------------------------------------------*/
#include "stm32l0xx_hal.h"

/* User can use this section to tailor DACx instance used and associated
   resources */
/* Definition for DACx clock resources */
#define DACx                            DAC
#define DACx_CHANNEL_GPIO_CLK_ENABLE()  __HAL_RCC_GPIOA_CLK_ENABLE()

#define DACx_CLK_ENABLE()               __HAL_RCC_DAC_CLK_ENABLE()
#define DACx_FORCE_RESET()              __HAL_RCC_DAC_FORCE_RESET()
#define DACx_RELEASE_RESET()            __HAL_RCC_DAC_RELEASE_RESET()

/* Definition for DACx Channel Pin */
#define DACx_CHANNEL_PIN                GPIO_PIN_4
#define DACx_CHANNEL_GPIO_PORT          GPIOA

/* Definition for DACx's Channel */
#define DACx_CHANNEL                    DAC_CHANNEL_1
     
     
#define Driver_EN1_PIN                  GPIO_PIN_9
#define Driver_EN2_PIN                  GPIO_PIN_10
#define Driver_EN_GPIO_PORT             GPIOC

#define Driver_EN_GPIO_CLK_ENABLE()     __HAL_RCC_GPIOC_CLK_ENABLE()      
 

#define EEPROM_BIAS_START_ADDR   DATA_EEPROM_BASE             /* Start @ of user EEPROM area */
#define EEPROM_BIAS_END_ADDR     (EEPROM_BIAS_START_ADDR + 5)   /* End @ of user EEPROM area */
#define IS_BIAS_DATA_ADDRESS(__ADDRESS__)          (((__ADDRESS__) >= EEPROM_BIAS_START_ADDR) && ((__ADDRESS__) <= EEPROM_BIAS_END_ADDR))

void LED_Driver_Init(void);
void LED_Driver_SetValue(uint8_t channel, uint32_t value);
uint8_t LED_Driver_GetValue(uint8_t channel);

#endif