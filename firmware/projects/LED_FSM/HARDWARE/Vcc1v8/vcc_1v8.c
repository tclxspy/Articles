#include "vcc_1v8.h"
#include "main.h"

void VCC_1v8_Init(void)
{  
  GPIO_InitTypeDef  GPIO_InitStruct;

  VCC_1v8_EN_GPIO_CLK_ENABLE();
  
  GPIO_InitStruct.Pin       = VCC_1v8_EN_PIN;
  GPIO_InitStruct.Mode      = GPIO_MODE_OUTPUT_PP;
  GPIO_InitStruct.Speed     = GPIO_SPEED_FREQ_VERY_HIGH;
  
  HAL_GPIO_Init(VCC_1v8_EN_GPIO_PORT, &GPIO_InitStruct);
  HAL_GPIO_WritePin(VCC_1v8_EN_GPIO_PORT, VCC_1v8_EN_PIN, GPIO_PIN_SET);
}