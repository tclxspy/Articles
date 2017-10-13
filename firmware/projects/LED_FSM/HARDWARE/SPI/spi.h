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
#define SPI_RESET_PIN                   GPIO_PIN_5
#define SPI_RESET_GPIO_PORT             GPIOC
 
#define SPI_SCK_GPIO_CLK_ENABLE()       __HAL_RCC_GPIOC_CLK_ENABLE()
#define SPI_MISO_GPIO_CLK_ENABLE()      __HAL_RCC_GPIOC_CLK_ENABLE()
#define SPI_MOSI_GPIO_CLK_ENABLE()      __HAL_RCC_GPIOC_CLK_ENABLE()
#define SPI_NSS_GPIO_CLK_ENABLE()       __HAL_RCC_GPIOC_CLK_ENABLE()
#define SPI_RESET_GPIO_CLK_ENABLE()     __HAL_RCC_GPIOC_CLK_ENABLE()

#define MOSI_H  HAL_GPIO_WritePin(SPI_MOSI_GPIO_PORT, SPI_MOSI_PIN, GPIO_PIN_SET)  
#define MOSI_L  HAL_GPIO_WritePin(SPI_MOSI_GPIO_PORT, SPI_MOSI_PIN, GPIO_PIN_RESET)  
#define SCK_H   HAL_GPIO_WritePin(SPI_SCK_GPIO_PORT, SPI_SCK_PIN, GPIO_PIN_SET)  
#define SCK_L   HAL_GPIO_WritePin(SPI_SCK_GPIO_PORT, SPI_SCK_PIN, GPIO_PIN_RESET)  
#define MISO    HAL_GPIO_ReadPin(SPI_MISO_GPIO_PORT, SPI_MISO_PIN) 
#define NSS_H   HAL_GPIO_WritePin(SPI_NSS_GPIO_PORT, SPI_NSS_PIN, GPIO_PIN_SET)  
#define NSS_L   HAL_GPIO_WritePin(SPI_NSS_GPIO_PORT, SPI_NSS_PIN, GPIO_PIN_RESET) 


#define EEPROM_DAC_START_ADDR   DATA_EEPROM_BASE+6             /* Start @ of user EEPROM area */
#define EEPROM_DAC_END_ADDR     (EEPROM_BIAS_START_ADDR + 16)   /* End @ of user EEPROM area */
#define IS_DAC_DATA_ADDRESS(__ADDRESS__)          (((__ADDRESS__) >= EEPROM_DAC_START_ADDR) && ((__ADDRESS__) <= EEPROM_DAC_END_ADDR))
#define IS_DAC_CH_ADDRESS(__ADDRESS__)          (((__ADDRESS__) >= 1) && ((__ADDRESS__) <= 4))

void SPI_Init(void);
uint16_t SPI_ReadWrite_Byte(uint8_t cmd_addr, uint16_t data);
void AD5317R_DAC_Write(uint8_t regAddress, uint8_t data);
uint8_t AD5317R_DAC_Read(uint8_t regAddress);
#endif