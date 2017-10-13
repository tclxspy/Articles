#include "led_driver.h"
#include "main.h"

DAC_HandleTypeDef    DacHandle;
static DAC_ChannelConfTypeDef sConfig;

void LED_Driver_Init()
{
  GPIO_InitTypeDef  GPIO_InitStruct;

  Driver_EN_GPIO_CLK_ENABLE();
  
  GPIO_InitStruct.Pin       = Driver_EN1_PIN;
  GPIO_InitStruct.Mode      = GPIO_MODE_OUTPUT_PP;
  GPIO_InitStruct.Speed     = GPIO_SPEED_FREQ_VERY_HIGH;
  
  HAL_GPIO_Init(Driver_EN_GPIO_PORT, &GPIO_InitStruct);
  HAL_GPIO_WritePin(Driver_EN_GPIO_PORT, Driver_EN1_PIN, GPIO_PIN_SET);
  
  GPIO_InitStruct.Pin       = Driver_EN2_PIN;
  HAL_GPIO_Init(Driver_EN_GPIO_PORT, &GPIO_InitStruct);
  HAL_GPIO_WritePin(Driver_EN_GPIO_PORT, Driver_EN2_PIN, GPIO_PIN_SET);
  
/*##-1- Configure the DAC peripheral #######################################*/
  DacHandle.Instance = DACx;

  if (HAL_DAC_Init(&DacHandle) != HAL_OK)
  {
    /* Initialization Error */
    //Error_Handler();
  }

  /*##-2- Configure DAC channel1 #############################################*/
  sConfig.DAC_Trigger = DAC_TRIGGER_NONE;
  sConfig.DAC_OutputBuffer = DAC_OUTPUTBUFFER_DISABLE;

  if (HAL_DAC_ConfigChannel(&DacHandle, &sConfig, DACx_CHANNEL) != HAL_OK)
  {
    /* Channel configuration Error */
    //Error_Handler();
  }

  uint32_t address = EEPROM_BIAS_START_ADDR;
  __IO uint8_t data8 = *(__IO uint8_t *)address;
  
  /*##-3- Set DAC Channel1 DHR register ######################################*/
  if (HAL_DAC_SetValue(&DacHandle, DACx_CHANNEL, DAC_ALIGN_8B_R, data8) != HAL_OK)
  {
    /* Setting value Error */
    //Error_Handler();
  }

  /*##-4- Enable DAC Channel1 ################################################*/
  if (HAL_DAC_Start(&DacHandle, DACx_CHANNEL) != HAL_OK)
  {
    /* Start Error */
    //Error_Handler();
  }
}

void LED_Driver_SetValue(uint8_t channel, uint32_t value)
{
  uint32_t address = EEPROM_BIAS_START_ADDR+channel;
  
  if(address > EEPROM_BIAS_END_ADDR)
  {
    return;
  }
  
      /* Check the parameters */
  assert_param(IS_BIAS_DATA_ADDRESS(Address));
  
  HAL_FLASHEx_DATAEEPROM_Unlock();
  
  if(channel==0x04)
  {  
    HAL_FLASHEx_DATAEEPROM_Program(FLASH_TYPEPROGRAMDATA_BYTE, address, value);
    HAL_FLASHEx_DATAEEPROM_Lock();
     
    GPIO_InitTypeDef  GPIO_InitStruct;

    Driver_EN_GPIO_CLK_ENABLE();
    
    GPIO_InitStruct.Pin       = Driver_EN1_PIN;
    GPIO_InitStruct.Mode      = GPIO_MODE_OUTPUT_PP;
    GPIO_InitStruct.Speed     = GPIO_SPEED_FREQ_VERY_HIGH;    
    
    HAL_GPIO_Init(Driver_EN_GPIO_PORT, &GPIO_InitStruct);
    HAL_GPIO_WritePin(Driver_EN_GPIO_PORT, Driver_EN1_PIN, (GPIO_PinState)(value&0x0001));
    
    value>>=1;
    GPIO_InitStruct.Pin       = Driver_EN2_PIN;
    HAL_GPIO_Init(Driver_EN_GPIO_PORT, &GPIO_InitStruct);
    HAL_GPIO_WritePin(Driver_EN_GPIO_PORT, Driver_EN2_PIN, (GPIO_PinState)(value&0x0001));
  
    return;
  }

  HAL_FLASHEx_DATAEEPROM_Program(FLASH_TYPEPROGRAMDATA_BYTE, address, value);
  HAL_FLASHEx_DATAEEPROM_Lock();
  
  if (HAL_DAC_SetValue(&DacHandle, DACx_CHANNEL, DAC_ALIGN_8B_R, value) != HAL_OK)
  {
    /* Setting value Error */
    //Error_Handler();
  } 
}

uint8_t LED_Driver_GetValue(uint8_t channel)
{
  uint32_t address = EEPROM_BIAS_START_ADDR+(channel-1);
  
    /* Check the parameters */
  assert_param(IS_BIAS_DATA_ADDRESS(Address));
  
  return *(__IO uint8_t *)address;
}
