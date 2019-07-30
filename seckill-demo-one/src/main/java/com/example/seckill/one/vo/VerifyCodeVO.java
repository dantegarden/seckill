package com.example.seckill.one.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.awt.image.BufferedImage;

@Data
@Accessors(chain = true)
public class VerifyCodeVO {
    private BufferedImage image;
    private int calc;
}
