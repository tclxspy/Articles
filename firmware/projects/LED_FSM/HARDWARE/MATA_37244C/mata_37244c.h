#ifndef __MATA_37244C_H
#define __MATA_37244C_H

/* Includes ------------------------------------------------------------------*/
#include "stm32l0xx_hal.h"
#include "stm32l0xx_hal_i2c.h"

#define I2C3_CLK_ENABLE()               __HAL_RCC_I2C3_CLK_ENABLE()
#define I2C3_SDA_GPIO_CLK_ENABLE()      __HAL_RCC_GPIOC_CLK_ENABLE()
#define I2C3_SCL_GPIO_CLK_ENABLE()      __HAL_RCC_GPIOC_CLK_ENABLE() 

#define I2C3_FORCE_RESET()              __HAL_RCC_I2C3_FORCE_RESET()
#define I2C3_RELEASE_RESET()            __HAL_RCC_I2C3_RELEASE_RESET()

/* Definition for I2Cx Pins */
#define I2C3_SCL_PIN                    GPIO_PIN_0
#define I2C3_SCL_GPIO_PORT              GPIOC
#define I2C3_SDA_PIN                    GPIO_PIN_1
#define I2C3_SDA_GPIO_PORT              GPIOC
#define I2C3_SCL_SDA_AF                 GPIO_AF7_I2C3

//#define MASTER_BOARD
//#define I2C3_ADDRESS        0xA0
#define I2C3_ADDRESS        0x16

/* I2C TIMING Register define when I2C clock source is SYSCLK */
/* I2C TIMING is calculated in case of the I2C Clock source is the SYSCLK = 32 MHz */
//#define I2C_TIMING    0x10A13E56 /* 100 kHz with analog Filter ON, Rise Time 400ns, Fall Time 100ns */ 
#define I2C3_TIMING      0x00B1112E /* 400 kHz with analog Filter ON, Rise Time 250ns, Fall Time 100ns */ 

/* Size of Transmission buffer */
#define TXBUFFERSIZE                    COUNTOF(aTxBuffer)
/* Size of Reception buffer */
//#define RXBUFFERSIZE                    TXBUFFERSIZE
#define RXBUFFERSIZE                    200
/* Exported macro ------------------------------------------------------------*/
#define COUNTOF(__BUFFER__)   (sizeof(__BUFFER__) / sizeof(*(__BUFFER__)))  


#define NUM_REG 0xFF

struct MATA_37244C_Map{
     uint8_t page;
     uint8_t regAddress;
     uint8_t data;
};

void I2C3_Init(void);
void MATA_37244C_Init(void);

#endif