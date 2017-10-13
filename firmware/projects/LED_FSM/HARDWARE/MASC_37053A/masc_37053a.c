#include "masc_37053a.h"

I2C_HandleTypeDef hi2c2;

void I2C2_Init(void)
{  
  hi2c2.Instance             = I2C2;
  hi2c2.Init.Timing          = I2C2_TIMING;
  hi2c2.Init.OwnAddress1     = I2C2_ADDRESS;
  hi2c2.Init.AddressingMode  = I2C_ADDRESSINGMODE_7BIT;
  hi2c2.Init.DualAddressMode = I2C_DUALADDRESS_DISABLE;
  hi2c2.Init.OwnAddress2Masks = I2C_OA2_NOMASK;
  hi2c2.Init.OwnAddress2     = 0xFF;
  hi2c2.Init.GeneralCallMode = I2C_GENERALCALL_DISABLE;
  hi2c2.Init.NoStretchMode   = I2C_NOSTRETCH_DISABLE;  

  if(HAL_I2C_Init(&hi2c2) != HAL_OK)
  {

  }

  /* Enable the Analog I2C Filter */
  //HAL_I2CEx_ConfigAnalogFilter(&I2cHandle,I2C_ANALOGFILTER_ENABLE);

  if (HAL_I2CEx_ConfigAnalogFilter(&hi2c2, I2C_ANALOGFILTER_ENABLE) != HAL_OK)
  {

  }

    /**Configure Digital filter 
    */
  if (HAL_I2CEx_ConfigDigitalFilter(&hi2c2, 0) != HAL_OK)
  {
    
  }
}

void MASC_37053A_Init(void)
{  
  I2C2_Init();
  
  struct MASC_37053A_Map map[9]={
     {0x00, 0xA8, 0x53},
     {0x00, 0xA9, 0x46},
     {0x00, 0xAA, 0x2D},
     {0x00, 0xAB, 0x51},
     {0x00, 0xAC, 0x53},
     {0x00, 0xAD, 0x30},
     {0x00, 0xAE, 0x34},
     {0x00, 0xAF, 0x32},
     {0x00, 0xB0, 0x35}
  };

  struct MASC_37053A_Map *buf;
  buf=&map[0];
  uint8_t count=0;
  while(count<9)
  {
    uint8_t regAddress= buf->regAddress;
    uint8_t aTxBuffer[1]={buf->data};
    do
    {
      if(HAL_I2C_Mem_Write_IT(&hi2c2, (uint16_t)I2C2_ADDRESS, regAddress, I2C_MEMADD_SIZE_8BIT, (uint8_t*)&aTxBuffer, 1)!= HAL_OK)
      {
      /* Error_Handler() function is called when error occurs. */
        //Error_Handler();
      }      
      while (HAL_I2C_GetState(&hi2c2) != HAL_I2C_STATE_READY){}
    }while(HAL_I2C_GetError(&hi2c2) == HAL_I2C_ERROR_AF);
    
    buf++;
    count++;
  }
}