#ifndef __MASC_37053A_H
#define __MASC_37053A_H

/* Includes ------------------------------------------------------------------*/
#include "stm32l0xx_hal.h"
#include "stm32l0xx_hal_i2c.h"


#define I2C2_CLK_ENABLE()               __HAL_RCC_I2C2_CLK_ENABLE()
#define I2C2_SDA_GPIO_CLK_ENABLE()      __HAL_RCC_GPIOB_CLK_ENABLE()
#define I2C2_SCL_GPIO_CLK_ENABLE()      __HAL_RCC_GPIOB_CLK_ENABLE() 

#define I2C2_FORCE_RESET()              __HAL_RCC_I2C2_FORCE_RESET()
#define I2C2_RELEASE_RESET()            __HAL_RCC_I2C2_RELEASE_RESET()

/* Definition for I2Cx Pins */
#define I2C2_SCL_PIN                    GPIO_PIN_10
#define I2C2_SCL_GPIO_PORT              GPIOB
#define I2C2_SDA_PIN                    GPIO_PIN_11
#define I2C2_SDA_GPIO_PORT              GPIOB
#define I2C2_SCL_SDA_AF                 GPIO_AF6_I2C2

//#define MASTER_BOARD
//#define I2C2_ADDRESS        0xA0
#define I2C2_ADDRESS        0x1A

/* I2C TIMING Register define when I2C clock source is SYSCLK */
/* I2C TIMING is calculated in case of the I2C Clock source is the SYSCLK = 32 MHz */
//#define I2C_TIMING    0x10A13E56 /* 100 kHz with analog Filter ON, Rise Time 400ns, Fall Time 100ns */ 
#define I2C2_TIMING      0x00B1112E /* 400 kHz with analog Filter ON, Rise Time 250ns, Fall Time 100ns */ 

/* Size of Transmission buffer */
#define TXBUFFERSIZE                    COUNTOF(aTxBuffer)
/* Size of Reception buffer */
//#define RXBUFFERSIZE                    TXBUFFERSIZE
#define RXBUFFERSIZE                    200
/* Exported macro ------------------------------------------------------------*/
#define COUNTOF(__BUFFER__)   (sizeof(__BUFFER__) / sizeof(*(__BUFFER__)))  


#define NUM_REG 0xFF

struct MASC_37053A_Map{
     uint8_t page;
     uint8_t regAddress;
     uint8_t data;
};

void I2C2_Init(void);
void MASC_37053A_Init(void);

#endif