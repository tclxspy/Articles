#include "mata_37244c.h"

I2C_HandleTypeDef hi2c3;

void I2C3_Init(void)
{  
  hi2c3.Instance             = I2C3;
  hi2c3.Init.Timing          = I2C3_TIMING;
  hi2c3.Init.OwnAddress1     = I2C3_ADDRESS;
  hi2c3.Init.AddressingMode  = I2C_ADDRESSINGMODE_7BIT;
  hi2c3.Init.DualAddressMode = I2C_DUALADDRESS_DISABLE;
  hi2c3.Init.OwnAddress2Masks = I2C_OA2_NOMASK;
  hi2c3.Init.OwnAddress2     = 0xFF;
  hi2c3.Init.GeneralCallMode = I2C_GENERALCALL_DISABLE;
  hi2c3.Init.NoStretchMode   = I2C_NOSTRETCH_DISABLE;  

  if(HAL_I2C_Init(&hi2c3) != HAL_OK)
  {

  }

  /* Enable the Analog I2C Filter */
  //HAL_I2CEx_ConfigAnalogFilter(&I2cHandle,I2C_ANALOGFILTER_ENABLE);

  if (HAL_I2CEx_ConfigAnalogFilter(&hi2c3, I2C_ANALOGFILTER_ENABLE) != HAL_OK)
  {

  }

    /**Configure Digital filter 
    */
  if (HAL_I2CEx_ConfigDigitalFilter(&hi2c3, 0) != HAL_OK)
  {
    
  }
}

void MATA_37244C_Init(void)
{  
  I2C3_Init();
  
  struct MATA_37244C_Map map[9]={
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

  struct MATA_37244C_Map *buf;
  buf=&map[0];
  uint8_t count=0;
  while(count<9)
  {
    uint8_t regAddress= buf->regAddress;
    uint8_t aTxBuffer[1]={buf->data};
    do
    {
      if(HAL_I2C_Mem_Write_IT(&hi2c3, (uint16_t)I2C3_ADDRESS, regAddress, I2C_MEMADD_SIZE_8BIT, (uint8_t*)&aTxBuffer, 1)!= HAL_OK)
      {
      }      
      while (HAL_I2C_GetState(&hi2c3) != HAL_I2C_STATE_READY){}
    }while(HAL_I2C_GetError(&hi2c3) == HAL_I2C_ERROR_AF);
    
    buf++;
    count++;
  }
}