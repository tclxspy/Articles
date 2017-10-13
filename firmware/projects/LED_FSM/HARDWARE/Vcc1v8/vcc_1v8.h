#ifndef __VCC_1V8_H
#define __VCC_1V8_H

#define VCC_1v8_EN_PIN                    GPIO_PIN_2
#define VCC_1v8_EN_GPIO_PORT              GPIOC

#define VCC_1v8_EN_GPIO_CLK_ENABLE()      __HAL_RCC_GPIOC_CLK_ENABLE() 

void VCC_1v8_Init(void);

#endif