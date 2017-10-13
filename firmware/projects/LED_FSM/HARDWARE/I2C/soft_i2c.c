#include "soft_i2c.h"
#include "main.h"
#include "stm32l0xx_hal.h"

void Soft_I2C_Init()
{
  GPIO_InitTypeDef  GPIO_InitStruct;
  
  I2C_SDA_GPIO_CLK_ENABLE();
  I2C_SCL_GPIO_CLK_ENABLE();
  
  GPIO_InitStruct.Pin       = I2C_SCL_PIN;
  GPIO_InitStruct.Mode      = GPIO_MODE_OUTPUT_PP;
  GPIO_InitStruct.Speed     = GPIO_SPEED_FREQ_LOW;
  
  HAL_GPIO_Init(I2C_SCL_GPIO_PORT, &GPIO_InitStruct);
  SCL_H;
  
  GPIO_InitStruct.Pin       = I2C_SDA_PIN;
  HAL_GPIO_Init(I2C_SDA_GPIO_PORT, &GPIO_InitStruct);
  SDA_H;
}

void SDA_OUT()
{
  GPIOB->MODER&=0xFF7FFFFF;
  GPIOB->MODER|=1<<22;
}

void SDA_IN()
{  
  GPIOB->MODER&=0xFF3FFFFF;
}

void Soft_I2C_Start()
{
  SDA_OUT();
  SDA_H;
  SCL_H;  
  __NOP();
  SDA_L;
  __NOP();
  SCL_L;   
}

void Soft_I2C_Stop()
{
  SDA_OUT();
  SCL_L;
  SDA_L;    
  __NOP();
  SCL_H; 
  SDA_H;      
  __NOP();
}

uint8_t Soft_I2C_Wait_Ack()
{
  uint8_t ucErrTime=0;
  SDA_IN();
  SDA_H;
  __NOP();
  SCL_H;
  __NOP();
  while(Read_SDA)
  {
    ucErrTime++;
    if(ucErrTime>1)
    {
      Soft_I2C_Stop();
      return 1;
    }
  }
  SCL_L;
  return 0;  
}

void Soft_I2C_Ack()
{  
  SCL_L;  
  SDA_OUT();
  SDA_L;
  __NOP();
  SCL_H;
  __NOP();
  SCL_L;
  __NOP();
}

void Soft_I2C_NAck()
{  
  SCL_L;  
  SDA_OUT();
  SDA_H;
  __NOP();
  SCL_H;
  __NOP();
  SCL_L;
  __NOP();
}

void Soft_I2C_Send_Byte(uint8_t data)
{
  SDA_OUT();
  SCL_L;
  for(uint8_t i=0;i<8;i++)
  {
    if((data&0x80)>>7)
    {
      SDA_H;
    }
    else
    {
      SDA_L;
    }
    data<<=1;
    __NOP();
    SCL_H;
    __NOP();
    SCL_L;
    __NOP();
  }
}

uint8_t Soft_I2C_Read_Byte(unsigned char ack)
{
  unsigned char i, receive=0;
  SDA_IN();
  for(i=0;i<8;i++)
  {
    SCL_L;
    __NOP();  
    __NOP();
    SCL_H; 
    receive<<=1; 
    if(Read_SDA)
    {
      receive++;
    }
    __NOP();
  }
  
  if(!ack)
  {
    Soft_I2C_NAck();
  }
  else
  {
    Soft_I2C_Ack();
  }
  return receive;
}

void Soft_I2C_Send_One_Byte(uint8_t deviceAddress, uint8_t regAddress, uint8_t data)
{
  Soft_I2C_Start();  
  Soft_I2C_Send_Byte(deviceAddress);
  Soft_I2C_Wait_Ack();
  Soft_I2C_Send_Byte(regAddress);
  Soft_I2C_Wait_Ack();
  Soft_I2C_Send_Byte(data);
  Soft_I2C_Wait_Ack();
  Soft_I2C_Stop();  
}

uint8_t Soft_I2C_Read_One_Byte(uint8_t deviceAddress, uint8_t regAddress)
{
  Soft_I2C_Start();  
  Soft_I2C_Send_Byte(deviceAddress);
  Soft_I2C_Wait_Ack();
  Soft_I2C_Send_Byte(regAddress);
  Soft_I2C_Wait_Ack();  
  Soft_I2C_Start();
  Soft_I2C_Send_Byte(deviceAddress+1);
  Soft_I2C_Wait_Ack();
  uint8_t temp = Soft_I2C_Read_Byte(0);  
  Soft_I2C_Stop();  
  return temp;
}

void Soft_I2C_Send_Reg(uint8_t deviceAddress, uint8_t regAddress, uint8_t data[], uint8_t length)
{
  Soft_I2C_Start();  
  Soft_I2C_Send_Byte(deviceAddress);
  Soft_I2C_Wait_Ack();  
  Soft_I2C_Send_Byte(regAddress);
  Soft_I2C_Wait_Ack();
  
  for(uint8_t i=0;i<length;i++)
  {
    Soft_I2C_Send_Byte(data[i]);
    Soft_I2C_Wait_Ack();
  }
  Soft_I2C_Stop();
  __NOP();
  __NOP();
}

void Soft_I2C_Read_Reg(uint8_t deviceAddress, uint8_t regAddress, uint8_t data[], uint8_t length)
{
  Soft_I2C_Start();
  
  Soft_I2C_Send_Byte(deviceAddress);
  Soft_I2C_Wait_Ack();
  Soft_I2C_Send_Byte(regAddress);
  Soft_I2C_Wait_Ack();  
  Soft_I2C_Start();
  Soft_I2C_Send_Byte(deviceAddress+1);
  Soft_I2C_Wait_Ack();
  
  if(length>1)
  {
    for(uint8_t i=0;i<length-1;i++)
    {
      data[i] = Soft_I2C_Read_Byte(1);  
    }
    data[length-1] = Soft_I2C_Read_Byte(0);  
  }
  else
  {
    data[0]=Soft_I2C_Read_Byte(0);
  }
  Soft_I2C_Stop();  
}