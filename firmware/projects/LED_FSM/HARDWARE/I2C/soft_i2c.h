#ifndef __I2C_SOFT_H
#define __I2C_SOFT_H

#include "stm32l0xx_hal.h"

#define I2C_SCL_PIN                    GPIO_PIN_10
#define I2C_SCL_GPIO_PORT              GPIOB
#define I2C_SDA_PIN                    GPIO_PIN_11
#define I2C_SDA_GPIO_PORT              GPIOB

#define I2C_SCL_GPIO_CLK_ENABLE()      __HAL_RCC_GPIOB_CLK_ENABLE()
#define I2C_SDA_GPIO_CLK_ENABLE()      __HAL_RCC_GPIOB_CLK_ENABLE()

#define SDA_H                          HAL_GPIO_WritePin(I2C_SDA_GPIO_PORT, I2C_SDA_PIN, GPIO_PIN_SET)
#define SDA_L                          HAL_GPIO_WritePin(I2C_SDA_GPIO_PORT, I2C_SDA_PIN, GPIO_PIN_RESET)
#define SCL_H                          HAL_GPIO_WritePin(I2C_SCL_GPIO_PORT, I2C_SCL_PIN, GPIO_PIN_SET)
#define SCL_L                          HAL_GPIO_WritePin(I2C_SCL_GPIO_PORT, I2C_SCL_PIN, GPIO_PIN_RESET)
#define Read_SDA                       HAL_GPIO_ReadPin(I2C_SDA_GPIO_PORT, I2C_SDA_PIN)

void Soft_I2C_Init(void);
void Soft_I2C_Start(void);
void Soft_I2C_Stop(void);
void Soft_I2C_Send_Byte(uint8_t data);
uint8_t Soft_I2C_Read_Byte(unsigned char ack);
uint8_t Soft_I2C_Wait_Ack(void);
void Soft_I2C_Ack(void);
void Soft_I2C_NAck(void);

void Soft_I2C_Send_One_Byte(uint8_t deviceAddress, uint8_t regAddress, uint8_t data);
uint8_t Soft_I2C_Read_One_Byte(uint8_t deviceAddress, uint8_t regAddress);

void Soft_I2C_Send_Reg(uint8_t deviceAddress, uint8_t regAddress, uint8_t data[], uint8_t length);
void Soft_I2C_Read_Reg(uint8_t deviceAddress, uint8_t regAddress, uint8_t data[], uint8_t length);

#endif